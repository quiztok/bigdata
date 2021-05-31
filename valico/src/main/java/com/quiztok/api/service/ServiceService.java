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
import io.grpc.stub.ServerCallStreamObserver;

@Service
public class ServiceService {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceService.class);

	@Autowired
	NodeConnector nodeConnector;

	// 서비스 가입
	public List<String> createService(String email,String kycBlockHash, String privateKey, String serviceCode, String serviceSign,
			Map<String,Object> serviceMessage) {
		//Protocol CPU 가 50% 이상일 경우 다른 노드에게 요청 하도록 거부.
		if(AgentService.checkProtocolInfo()) {
			//거부에 대한 BlockList 빈값을 보낼지 Command 를 보낼지에 대한 규정 필요
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(serviceMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) serviceMessage.get("encrypted");
		}
		if(serviceMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) serviceMessage.get("decrypted");
		}
		return nodeConnector.createService(email,kycBlockHash, privateKey, serviceCode, serviceSign,encrypted,decrypted);
	}

	// 서비스 조회
	public List<Block> searchService(String kycBlockHash,String privateKey ,String serviceCode) {
		//Protocol CPU 가 50% 이상일 경우 다른 노드에게 요청 하도록 거부.
		if(AgentService.checkProtocolInfo()) {
			//거부에 대한 BlockList 빈값을 보낼지 Command 를 보낼지에 대한 규정 필요
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return nodeConnector.searchService(kycBlockHash, privateKey, serviceCode);
	}

	// 서비스 갱신
	public List<String> updateService(String email, String kycBlockHash, String privateKey, String serviceCode,
			boolean deactivate) {
		//Protocol CPU 가 50% 이상일 경우 다른 노드에게 요청 하도록 거부.
		if(AgentService.checkProtocolInfo()) {
			//거부에 대한 BlockList 빈값을 보낼지 Command 를 보낼지에 대한 규정 필요
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return nodeConnector.updateService(email, kycBlockHash, privateKey, serviceCode, deactivate);
	}

	// 서비스 탈퇴 -- 현재 미구현
	public List<String> deactivateService() {
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU 50% 이상");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return null;
	}

}
