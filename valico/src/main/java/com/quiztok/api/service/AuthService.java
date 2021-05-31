package com.quiztok.api.service;

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

import generated.Block;

@Service
public class AuthService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private NodeConnector nodeConnector;
	@Autowired
	private ServiceService serviceService;

	// 사용자 인증 등록
	public List<String> createAuth(String email, String kycBlockHash, String privateKey, String serviceCode, String authType,
			String sourceType, String authId, Map<String,Object> authMessage) {
		//Protocol CPU 50%이상 사용여부 체크
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		//service가입여부 확인
		List<Block> searviceBlockList = serviceService.searchService(kycBlockHash, privateKey, serviceCode);
		if(searviceBlockList.size() <= 0) {
			throw new APIException(APIStatus.NO_SERVICE_BLOCK,"서비스 가입이 되어있지 않습니다.");
		}
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(authMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) authMessage.get("encrypted");
		}
		if(authMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) authMessage.get("decrypted");
		}
		
		List<String> authHash = nodeConnector.createAuth(email, kycBlockHash, privateKey, serviceCode, authType, sourceType, authId, encrypted, decrypted);
		return authHash;
	}

	// 사용자 인증 검색
	public Block searchAuth(String kycBlockHash, String privateKey, @Nullable String authType, @Nullable String serviceCode) {
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
	
		List<Block> blockList = nodeConnector.searchAuth(kycBlockHash, privateKey, serviceCode);
		if(blockList.size() <= 0) {
			throw new APIException(APIStatus.NO_AUTH_BLOCK,"사용자 인증이 되지 않았습니다.");
		}
		return blockList.get(0);
	}

	// 사용자 인증 갱신 -- 추후
	public List<String> updateAuthForKYC(String email, String oldKycBlockHash, @Nullable String updatedKycBlockHash,
			String privateKey, boolean deactivate) {
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		Block authBlock = searchAuth(oldKycBlockHash, privateKey, null,null);
		if (authBlock != null) {
			return nodeConnector.updateAuthForKYC(email, authBlock.getHash(), privateKey,
					updatedKycBlockHash == null ? oldKycBlockHash : updatedKycBlockHash, deactivate);
		} else {
			return null;
		}
	}

}
