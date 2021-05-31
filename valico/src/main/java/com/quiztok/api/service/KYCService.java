package com.quiztok.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiztok.api.connector.NodeConnector;
import com.quiztok.api.exception.APIException;
import com.quiztok.api.util.statuscode.APIStatus;
import com.quiztok.api.util.statuscode.AgentStatus;
import com.quiztok.api.vo.AgentVO;
import com.quiztok.api.vo.HostVO;

import generated.Block;

@Service
public class KYCService {

	private static final Logger LOG = LoggerFactory.getLogger(KYCService.class);
	
	@Autowired
	private NodeConnector nodeConnector;
	@Autowired
	private AgentService agentService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthService authService;
	@Autowired
	private ServiceService serviceService;
	
	//회원가입
	public List<String> createKyc(String email, String pin, String name, Map<String, Object> kycMessage) {
		//Protocol CPU 사용량 확인
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		//Email 중복 여부 확인
		Block block = searchKyc(email,null);
		if(block != null) {
			LOG.warn("CheckEmail 중복있음 : "+email);
			return null;
		}
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(kycMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) kycMessage.get("encrypted");
		}
		if(kycMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) kycMessage.get("decrypted");
		}
		
		return nodeConnector.createKyc(email, pin, name, encrypted,decrypted);
	}
		
	//로그인
	public Block searchKyc(String email, String pin) {
		//Protocol CPU 가 50% 이상일 경우 다른 노드에게 요청 하도록 거부.
		if(AgentService.checkProtocolInfo()) {
			//거부에 대한 BlockList 빈값을 보낼지 Command 를 보낼지에 대한 규정 필요
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		List<Block> blockList = nodeConnector.searchKyc(email, pin, true);
		if(AgentVO.isUsingAgent()) {
			agentService.write(blockList);
		}
		return blockList.size() >0 ? blockList.get(0) : null;
	}
		
	//회원탈퇴
	public List<String> deactivate(String email, String kycBlockHash, String privateKey, String pin, String serviceCode) {
	/*	if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		//Service 비활성화
		//List<String> deactivatedServiceBlockHash = serviceService.updateService(email, kycBlockHash, privateKey, serviceCode, true);
		
		
		//auth 비활성화
		String deactivatedAuthBlockHash = authService.updateAuthForKYC(email, kycBlockHash, null, privateKey, true);
		//account 비활성화 (다수)
		accountService.updateAccountForKYC(kycBlockHash,deactivatedAuthBlockHash, privateKey, true);
		//kyc 비활성화
		List<String> deactivatedKycBlockHash = nodeConnector.deactivate(email, kycBlockHash, privateKey, pin);
		
		return deactivatedKycBlockHash;*/
		return null;
	}
	
	public String checkKycBlockHash(Map<String,Object> map) {
		String kycBlockHash = map.get("kycBlockHash") != null ? map.get("kycBlockHash").toString() : null;
		String email = map.get("email") != null ? map.get("email").toString() : null;
		// kycBlockHash 가 없을경우 email을 통해 구함(KYCBlock이 생성 되지 않았을경우 생성불가)
		if (kycBlockHash == null) {
			Block kycBlock = searchKyc(email, null);
			if (kycBlock == null) {
				throw new APIException(APIStatus.NO_KYC_BLOCK, "해당 Eamil로 가입된 계정이 존재하지 않습니다.");
			}
			kycBlockHash = kycBlock.getHash();
		}
		return kycBlockHash;
	}
}
