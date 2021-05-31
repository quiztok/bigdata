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
import com.quiztok.api.util.statuscode.AgentStatus;

import generated.Block;

@Service
public class ProfileService {

	private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);
	
	@Autowired
	NodeConnector nodeConnector;
	@Autowired
	AgentService agentService;
	
	
	//개인정보 생성
	public List<String> createProfile(String email,String kycBlockHash, String privateKey,
			Map<String,Object> profileMassage) {
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(profileMassage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) profileMassage.get("encrypted");
		}
		if(profileMassage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) profileMassage.get("decrypted");
		}
		
		return nodeConnector.createProfile(email,kycBlockHash, privateKey, encrypted, decrypted);
	}
	
	//개인정보 검색
	public List<Block> searchProfile(String kycBlockHash, String privateKey){
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return nodeConnector.searchProfile(kycBlockHash, privateKey);
	}
}
