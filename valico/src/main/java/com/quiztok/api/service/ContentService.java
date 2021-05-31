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
public class ContentService {

	private static final Logger LOG = LoggerFactory.getLogger(ContentService.class);

	@Autowired
	NodeConnector nodeConnector;

	// 컨텐츠 등록
	public List<String> createContent(String email, String kycBlockHash, String privateKey,String contentType, String contentSubject,
			String bannerImg, String state, List<String> quizzes,Map<String,Object> contentMessage) {
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(contentMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) contentMessage.get("encrypted");
		}
		if(contentMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) contentMessage.get("decrypted");
		}
		
		return nodeConnector.createContent(email,kycBlockHash,privateKey,contentType,contentSubject,bannerImg,state,quizzes,encrypted,decrypted);
	}

	// 컨텐츠 검색(기본검색)
	public List<Block> searchContent(String contentBlockHash) {
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return nodeConnector.searchContent(contentBlockHash);
	}
	
	//컨텐츠 검색(Publish의 생성으로인한 검색)
	public List<Block> searchContentByBlockHash(){
		
		
		return null;
	}
}
