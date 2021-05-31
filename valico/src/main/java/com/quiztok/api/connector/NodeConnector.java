package com.quiztok.api.connector;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.quiztok.api.exception.APIException;
import com.quiztok.api.util.Util;
import com.quiztok.api.vo.HostVO;

import generated.AccountTransaction;
import generated.Block;
import generated.LedgerTransaction;
import generated.To;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.node.AccountMessage;
import proto.node.ApproachStatus;
import proto.node.AuthMessage;
import proto.node.ContentMessage;
import proto.node.KycMessage;
import proto.node.LedgerMessage;
import proto.node.Monitor;
import proto.node.NodeAPIServiceGrpc;
import proto.node.NodeAPIServiceGrpc.NodeAPIServiceBlockingStub;
import proto.node.NodeRequest;
import proto.node.NodeResponse;
import proto.node.PublishMessage;
import proto.node.ServiceMessage;

@Component
@DependsOn({"APIConfig"})
public class NodeConnector {

	private static final Logger LOG = LoggerFactory.getLogger(NodeConnector.class);
	private ManagedChannel channel;
	private NodeAPIServiceBlockingStub blockingStub;

	@Autowired
	private NodeConnector() {
		this.channel = ManagedChannelBuilder.forAddress(HostVO.getApiHost(), HostVO.getApiPort()).usePlaintext().build();
		this.blockingStub = NodeAPIServiceGrpc.newBlockingStub(channel);
	}

	// 회원가입
	public List<String> createKyc(String email, String pin, String name,Map<String,Object> encrypted,Map<String,Object> decrypted) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setEmail(email)
				.setKycMessage(KycMessage.newBuilder()
						.setPin(pin)
						.setName(name).build())
				.setVersion(HostVO.getVersion());
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for(int i = 0; i<HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createKyc(builder.build());
			if(confirm(response) == true) {
				/*	List<String> list  = new ArrayList<>();
					for(proto.node.Block b : response.getBlockList()) {
						list.add(b.getHash());
					};
					return list;*/
					return response.getBlockList().stream().map(b->b.getHash()).collect(Collectors.toList());
					//return response.getBlockList().stream().map(b->b.getHash()).collect(Collectors.toList()).toArray(new String[0]);
				}
		}
		throw new APIException();
	}

	// 로그인
	public List<Block> searchKyc(String email, String pin, boolean verify) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setEmail(email)
				.setRequireVerify(verify);
		if(pin != null) {
			builder.setKycMessage(KycMessage.newBuilder().setPin(pin).build());
		}
		NodeResponse response = blockingStub.searchKyc(builder.build());
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}

	// 회원탈퇴
	public String[] deactivate(String email, String kycBlockHash,String privateKey, String pin) {
		NodeRequest request = NodeRequest.newBuilder()
												.setEmail(email)
												.setKYCBlockHash(kycBlockHash)
												.setPrivateKey(privateKey)
												.setKycMessage(KycMessage.newBuilder()
														.setPin(pin)
														.build()).build();
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.deactivate(request);
			if (confirm(response) == true) {
				//return response.getBlock(0).getHash();
				return response.getBlockList().stream().map(block -> block.getHash()).collect(Collectors.toList()).toArray(new String[0]);
			}
		}
		throw new APIException();
	}
	
	//서비스가입 블록생성
	public List<String> createService(String email, String kycBlockHash, String privateKey, String serviceCode, String serviceSign,
			Map<String,Object> encrypted, Map<String,Object> decrypted) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setEmail(email)
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey)
				.setVersion(HostVO.getVersion())
				.setServiceMessage(ServiceMessage.newBuilder()
						.setServiceCode(serviceCode)
						.setServiceSign(serviceSign).build());
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createService(builder.build());
			if(confirm(response) == true){
				//return response.getBlockList().stream().map(block -> block.getHash()).collect(Collectors.toList()).toArray(new String[0]);
				return response.getBlockList().stream().map(block -> block.getHash()).collect(Collectors.toList());
			}
		}
		throw new APIException();
	}
	
	//서비스 검색
	public List<Block> searchService(String kycBlockHash,String privateKey ,String serviceCode) {
		NodeRequest request = NodeRequest.newBuilder()
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey)
				.setServiceMessage(ServiceMessage.newBuilder()
						.setServiceCode(serviceCode).build()).build();
		NodeResponse response = blockingStub.searchService(request);
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}
	
	//서비스 갱신 -- 구현예정
	public List<String> updateService(String email, String kycBlockHash, String privateKey, String serviceCode,
			boolean deactivate){
		
		return null;
	}
	
	//서비스 탈퇴
	public String[] deactivateService(){
		
		return null;
	}

	// 사용자인증 정보 생성
	public List<String> createAuth(String email, String kycBlockHash,String privateKey,String serviceCode,String authType,
			String sourceType,String authId,Map<String, Object> encrypted, Map<String,Object>decrypted){
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setEmail(email)
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey)
				.setVersion(HostVO.getVersion())
				.setServiceMessage(ServiceMessage.newBuilder()
						.setServiceCode(serviceCode).build())
				.setAuthMessage(AuthMessage.newBuilder()
						.setAuthType(authType)
						.setSourceType(sourceType)
						.setAuthId(authId).build());
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createAuth(builder.build());
			if(confirm(response) == true) {
//				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList()).toArray(new String[0]);
				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList());
			}
		}
		throw new APIException();
	}

	// 사용자인증 정보 검색
	public List<Block> searchAuth(String kycBlockHash, String privateKey, String serviceCode){
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey);
						if(serviceCode != null) {
							builder.setAuthMessage(AuthMessage.newBuilder()
									.setServiceCode(serviceCode).build());
						}
						
		NodeResponse response = blockingStub.searchAuth(builder.build());
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}
	
	// 사용자인증 정보 변경으로 인한 KYC 갱신
	public List<String> updateAuthForKYC(String email, String autBlockHash, String privateKey, String kycBlockHash,boolean deactivate){
				
		return null;
	}

	// 계좌개설
	public Block createAccount(String email, AccountTransaction accountTransaction, String privateKey,Map<String,Object> encrypted,Map<String,Object>decrypted) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setVersion(HostVO.getVersion())
				.setEmail(email)
				.setKYCBlockHash(accountTransaction.getKycBlockHash())
				.setAccountMessage(AccountMessage.newBuilder()
						.setType(accountTransaction.getType())
						.setNickname(accountTransaction.getNickname())
						.setCurrencyCode(accountTransaction.getCurrencyCode())
						.build())
				.setPrivateKey(privateKey);
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for (int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createAccount(builder.build());
			if (confirm(response) == true) {
				return Util.toBlockList(response.getBlockList()).get(0);
			}
		}
		throw new APIException();
	}

	// 계좌 검색(From)
	public List<Block> searchAccountByKYCHash(String kycBlockHash,String privateKey, String[] accountTypeNames, boolean verify){
		NodeRequest request = NodeRequest.newBuilder()
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey)
				.addAllAccountTypeName(Arrays.asList(accountTypeNames))
				.setRequireVerify(verify)
				.build();
		NodeResponse response = blockingStub.searchAccountByKYCHash(request);
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}

	// 계좌 검색(FromAddress / ToAddress)
	public List<Block> searchAccountByAccountAddress(String accountAddress,String privateKey,boolean verify){
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setAccountAddress(accountAddress);
		if(privateKey != null) {
			builder.setPrivateKey(privateKey);
			builder.setRequireVerify(verify);
		}
		NodeResponse response = blockingStub.searchAccountByAccountAddress(builder.build());
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}

	// 거래 이후 잔액갱신
	public String updateAccountForLedger(String accountAddress, String accountBlockHash, String privateKey,String ledgerBlockHash) {
		NodeRequest.Builder builder = NodeRequest.newBuilder().setAccountAddress(accountAddress)
				.setAccountBlockHash(accountBlockHash)
				.setLedgerBlockHash(ledgerBlockHash);
		if (privateKey != null) {
			builder.setPrivateKey(privateKey);
		}

		for (int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.updateAccountForLedger(builder.build());
			if (confirm(response) == true) {
				return response.getBlock(0).getHash();
			}
		}
		throw new APIException();

	}
	
	//충전 후 갱신
	public void updateAccountForCharge(String accountAddress, String accountBlockHash, String privateKey, BigDecimal value) {
		
		NodeRequest request = NodeRequest.newBuilder()
				.setAccountAddress(accountAddress)
				.setAccountBlockHash(accountBlockHash)
				.setPrivateKey(privateKey)
				.setVersion(HostVO.getVersion())
				.setAccountMessage(AccountMessage.newBuilder()
						.setValue(value.toString()).build()).build();
		
		NodeResponse response = blockingStub.updateAccountForCharge(request);
		confirm(response);
	}

	// 계좌 잔액갱신 
	public Block updateAccountForAccuracy (String accountAddress, String accountBlockHash, String privateKey) {
		NodeRequest request = NodeRequest.newBuilder().setAccountAddress(accountAddress)
				.setAccountBlockHash(accountBlockHash).setPrivateKey(privateKey).build();
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for (int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.updateAccountForAccuracy(request);
			if (confirm(response) == true) {
				return Util.toBlockList(response.getBlockList()).get(0);
			}
		}
		throw new APIException();
	}

	// KYC 변동으로 인한 잔액갱신

	// 회원탈퇴
	
	//사용자 정보 생성(Profile)
	public List<String> createProfile(String email,String kycBlockHash,String privateKey, Map<String,Object> encrypted, Map<String,Object> decrypted) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setVersion(HostVO.getVersion())
				.setEmail(email)
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey);
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createProfile(builder.build());
			if(confirm(response) == true) {
				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList());
			}
		}
		
		throw new APIException();
	}
	
	//사용자 정보 검색(Profile)
	public List<Block> searchProfile(String kycBlockHash, String privateKey){
		NodeRequest request = NodeRequest.newBuilder()
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey)
				.build();
		NodeResponse response = blockingStub.searchProfile(request);
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}
	
	//컨텐츠 생성
	public List<String> createContent(String email,String kycBlockHash,String privateKey,String contentType,
			String contentSubject,String bannerImg,String state,List<String> quizzes,Map<String,Object> encrypted, Map<String,Object>decrypted) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setVersion(HostVO.getVersion())
				.setEmail(email)
				.setKYCBlockHash(kycBlockHash)
				.setPrivateKey(privateKey)
				.setContentMessage(ContentMessage.newBuilder()
						.setType(contentType)
						.setSubject(contentSubject)
						.setBannerImg(bannerImg)
						.setState(state)
						.addAllQuiz(quizzes).build());
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		
		//Config File에 설정한 ReTry횟수 만큼 재시도
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createContent(builder.build());
			if(confirm(response) == true) {
//				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList()).toArray(new String[0]);
				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList());
			}
		}
		throw new APIException();
	}
	
	//컨텐츠 검색(기본)
	public List<Block> searchContent(String contentBlockHash){
		NodeRequest request = NodeRequest.newBuilder()
				.setContentBlockHash(contentBlockHash)
				.build();
		NodeResponse response = blockingStub.searchContentByBlockHash(request);
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}
	
	//컨텐츠 검색(Publish)
	public List<Block> searchContentByBlockHash(){
		
		return null;
	}
	
	//공개 블록생성
	public List<String> createPublish(String email, String kycBlockHash, int republish, String state,long term,long expiryDate,
			Map<String,Object>encrypted, Map<String,Object>decrypted) {
		NodeRequest.Builder builder = NodeRequest.newBuilder()
				.setVersion(HostVO.getVersion())
				.setEmail(email)
				.setKYCBlockHash(kycBlockHash)
				.setPublishMessage(PublishMessage.newBuilder()
						.setRepublish(republish)
						.setState(state)
						.setTerm(term)
						.setExpirtyDate(expiryDate).build());
				if(encrypted != null) {
					builder.putAllEncryptedTransactionObject(changeEntry(encrypted));
				}
				if(decrypted != null) {
					builder.putAllTransactionObject(changeEntry(decrypted));
				}
		
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createPublish(builder.build());
			if(confirm(response) == true) {
//				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList()).toArray(new String[0]);
				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList());
			}
		}
		throw new APIException();
	}
	
	//공개 검색 (BlockHash로 조회)
	public List<Block> searchPublish(String publishBlockHash){
		NodeRequest request= NodeRequest.newBuilder()
				.setPublishBlockHash(publishBlockHash).build();
		NodeResponse response = blockingStub.searchPublishAll(request);
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}
	
	//거래내역 생성
	public List<String> createLedger(LedgerTransaction ledgerTransaction, String privateKey, List<To> toMessage,
			Map<String,Object>encrypted,Map<String,Object>decrypted) {
	
		LedgerMessage.Builder builder = LedgerMessage.newBuilder()
						.setType(ledgerTransaction.getType())
						.setFromAddress(ledgerTransaction.getFromAddress())
						.setFromAccountBlockHash(ledgerTransaction.getFromAccountBlockHash())
						.setFromPrivateKey(privateKey);
		for(To to : toMessage) {
			builder.addTo(LedgerMessage.To.newBuilder()
					.setAddress(to.getAddress())
					.setAccountBlockHash(to.getAccountBlockHash())
					.setFromValue(to.getFromValue().toString())
					.setToValue(to.getToValue().toString())
					.build());
		}
		
		for(int i = 0; i < HostVO.getReTry(); i++) {
			NodeResponse response = blockingStub.createLedger(NodeRequest.newBuilder().setVersion(HostVO.getVersion())
					.putAllEncryptedTransactionObject(changeEntry(encrypted))
					.putAllTransactionObject(changeEntry(decrypted))
					.setLedgerMessage(builder.build()).build());
			if(confirm(response) == true) {
//				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList()).toArray(new String[0]);
				return response.getBlockList().stream().map(block->block.getHash()).collect(Collectors.toList());
			}
		}
		throw new APIException();
	}
	
	//거래내역 조회
	public List<Block> searchLedger(String accountAddress){
		NodeRequest request = NodeRequest.newBuilder()
				.setAccountAddress(accountAddress).build();
		
		NodeResponse response = blockingStub.searchLedger(request);
		confirm(response);
		return Util.toBlockList(response.getBlockList());
	}

	// Protocol 결과값 확인
	private static boolean confirm(NodeResponse response) {

		if (response.getStatus() == ApproachStatus.SUCCESS) {
			return true;
		} else if (response.getStatus() == ApproachStatus.TRY_AGAIN) {
			return false;
		}

		throw new APIException(response.getStatus());
	}
	//관리자 블록 모니터
	public List<Block> monitor(String hash) {
		NodeResponse response = blockingStub.monitor( Monitor.newBuilder()
							.setHash(hash)
							.build());
		
		return Util.toBlockList(response.getBlockList());
	}
	
	//관리자 모니터 모니터
	public List<Block> monitorUpdate(String hash) {
		Monitor.Builder builder = Monitor.newBuilder();
		if(hash != null && !hash.equals("")) {
			builder.setHash(hash);
		}
		NodeResponse response = blockingStub.monitorUpdate(builder.build());
		return Util.toBlockList(response.getBlockList());
	}
	
	//Entry 
	private Map<String,String> changeEntry(Map<String,Object> map){
		Map<String,String> resultMap = new HashMap<>();
		for(Entry<String, Object> a : map.entrySet()) {
			resultMap.put(a.getKey(), a.getValue().toString());
		}
		
		return resultMap;
	}
}
