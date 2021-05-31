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
public class PublishService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PublishService.class);
	
	@Autowired
	NodeConnector nodeConnector;
	@Autowired
	ContentService contentService;
	
	
	//공개 블록 생성
	public List<String> createPublish(String email, String kycBlockHash, int republish, String state,long term,long expiryDate,
			Map<String,Object> publishMessage) {
		//Protocol CPU 사용량 확인
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(publishMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) publishMessage.get("encrypted");
		}
		if(publishMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) publishMessage.get("decrypted");
		}
		
		return nodeConnector.createPublish(email,kycBlockHash,republish,state,term,expiryDate,encrypted,decrypted);
	}
	
	//공개 블록 검색
	public List<Block> searchPublish(String publishBlockHash){
		//Protocol CPU 사용량 확인
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return nodeConnector.searchPublish(publishBlockHash);
	}

}
