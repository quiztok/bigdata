package com.quiztok.api.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiztok.api.connector.NodeConnector;
import com.quiztok.api.exception.APIException;
import com.quiztok.api.util.statuscode.APIStatus;
import com.quiztok.api.util.statuscode.AgentStatus;
import com.quiztok.api.vo.AgentVO;

import generated.AccountTransaction;
import generated.Block;

@Service
public class AccountService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	NodeConnector nodeConnector;
	@Autowired
	AuthService authService;
	@Autowired
	AgentService agentService;
	
	// 계좌생성
	public Block createAccount(String email, AccountTransaction accountTransaction, String privateKey, Map<String,Object> accountMessage) {
		//Protocol check Status
		if (AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE, "CPU 사용량 50% 이상");
		}
		//AuthBlock 생성 여부 확인 로직 필요 
		Block authBlock = authService.searchAuth(accountTransaction.getKycBlockHash(), privateKey, null,null);
		if (authBlock == null) {
			throw new APIException(APIStatus.NO_AUTH_BLOCK, "검색된 AuthBlock이 없습니다.");
		}
		//검색된 AccountBlock 있으면 exception
		List<Block> blockList = searchAccountByKYCBlockHash(accountTransaction.getKycBlockHash(), privateKey, true, accountTransaction.getType());
		if(blockList.size() > 0) {
			LOG.info(blockList.toString());
			throw new APIException(APIStatus.DUPLICATED_ACCOUNT, "이미 존재하는 account가 있습니다");
		}
		
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(accountMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) accountMessage.get("encrypted");
		}
		if(accountMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) accountMessage.get("decrypted");
		}
		
		return nodeConnector.createAccount(email, accountTransaction, privateKey, encrypted,decrypted);
	}
	
	//KYCHash를 통한 계좌검색
	public List<Block> searchAccountByKYCBlockHash(String kycBlockHash, String privateKey, boolean verify, String... accountTypeNames){
		//Protocol check Status
		if (AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE, "CPU 사용량 50% 이상");
		}
		
		//AccountBlock 존재 확인 (KYCBlockHash 로 AccountBlock 검색)
		List<Block> blockList = nodeConnector.searchAccountByKYCHash(kycBlockHash, privateKey, accountTypeNames, verify);
		if(AgentVO.isUsingAgent()) {
			agentService.write(blockList);
		}
		
		return updateAccountForAccuracy(blockList, privateKey);
	}
	
	//계좌주소를 통한 계좌검색
	public Block searchAccountByAccountAddress(String accountAddress,@Nullable String privateKey, boolean verify) {
		if (AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE, "CPU 사용량 50% 이상");
		}
		
		List<Block> blockList = nodeConnector.searchAccountByAccountAddress(accountAddress, privateKey, verify);
		if(AgentVO.isUsingAgent()) {
			agentService.write(blockList);
		}
		blockList = updateAccountForAccuracy(blockList, privateKey);
		return blockList.get(0);
	}
	
	//거래로 인한 Ledger갱신
	public String updateAccountForLedger(Block block, String privateKey, String ledgerBlockHash) {
		String accountAddress = ((AccountTransaction)block.getTransaction()).getAccountAddress();
		System.out.println("aaaaaaa : "+ block.getHash());
		System.out.println("bbbbbbb : "+ ledgerBlockHash);
		return nodeConnector.updateAccountForLedger(accountAddress, block.getHash(), privateKey, ledgerBlockHash);
	}
	
	//회원 탈퇴시 접근을 막는 AccountBlock 생성
	public List<String> updateAccountForKYC(String kycBlockHash, String authBlockHash, String privateKey, boolean deactivate){
		
		return null;
	}
	
	//Accuracy 갱신
	private List<Block> updateAccountForAccuracy(List<Block> blockList, String privateKey){
		if(privateKey == null) {
			return blockList;
		}
		for (int i = 0; i < blockList.size(); i++) {
			Block block = blockList.get(i);
			AccountTransaction accountTransaction = (AccountTransaction) block.getTransaction();
			if(accountTransaction.getAccuracy() > 0) {
				blockList.set(i, nodeConnector.updateAccountForAccuracy(accountTransaction.getAccountAddress(), block.getHash(), privateKey));
			}
		}
		return blockList;
	}

	public void updateAccountForCharge(String accountAddress, String accountBlockHash, String privateKey, BigDecimal value) {
		
		nodeConnector.updateAccountForCharge(accountAddress, accountBlockHash, privateKey, value);
		
	}
}
