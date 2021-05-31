package com.quiztok.api.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXParseException;

import com.quiztok.api.connector.AgentConnector;
import com.quiztok.api.util.XmlUtils;
import com.quiztok.api.vo.AgentVO;

import generated.Block;
import generated.NodeBalancingInfo;
import proto.agent.BlockResponse;
import proto.agent.BlockStatus;

@Service
public class AgentService {

	private static final Logger LOG = LoggerFactory.getLogger(AgentService.class);

	@Autowired
	private AgentConnector agentConnector;

	// Protocol 상태 확인
	public static boolean checkProtocolInfo() {
		if (AgentVO.isUsingBalancing()) {
			LOG.warn("Balancing 사용 안함");
			return false;
		}
		try {
			// Agent 사용 여부 확인
			if (!AgentVO.isUsingAgent())
				return false;
			// Protocol Status 확인
			NodeBalancingInfo nodeBalanceInfo = XmlUtils.readNodeInfoFile();
			LOG.info("프로토콜 상태 확인 : " + nodeBalanceInfo.getProtocolCPU());

			if (Double.parseDouble(nodeBalanceInfo.getProtocolCPU()) < AgentVO.getProtocolCpuPercent()) {
				// AppServer에게 다른 노드에게 요청 하도록 보내기
				AgentVO.setProtocolHost(nodeBalanceInfo.getProtocolHost());
				return true;
			}
		} catch (SAXParseException e) {
			LOG.info("XML 생성 중에 들어왔다");
			LOG.error("", e);
			return true;
		} catch (JAXBException c) {
			LOG.error("", c);
		}
		return false;
	}

	// Agent 에 Write 요청(List Type)
	public void writeBlockHashList(List<Block> list) {
		if (!AgentVO.isUsingAgent() || !AgentVO.isUsingRedis()) {
			LOG.warn("Agent 사용 안함");
			return;
		}
		BlockResponse result = null;
		if (list.size() == 0) {
			LOG.warn("blockList Size 0 이다");
			return;
		}
		for (Block block : list) {
			agentConnector.redisWrite(block.getPreviousHash(), "");
			result = agentConnector.redisWrite(block.getHash(), block.getNodeName());
			// API 입장에서는 실패 시 작업 처리 필요
			if (result.getStatus() == BlockStatus.WRITE_FAIL || result.getStatus() == BlockStatus.SEARCH_FAIL) {
				LOG.error("Write_실패한 BlockHash" + block.getHash());
			} else {
				LOG.warn("write요청:" + result.getStatus().toString());
				// Search_SUCCESS로 반환시 Redis 검색된 값 Return
			}
		}
	}

	// Agent 에 Write 요청(BlockHash)
	public void write(List<Block> blockList) {
		LOG.info("미작업!!!!!!!!!!!!!!!!!!");
	}

	// Agent 에 Write 요청(BlockHash)
	public void writeBlockHash(String blockHash, String nodeName) {
		BlockResponse result = null;
		if (!AgentVO.isUsingAgent() || !AgentVO.isUsingRedis()) {
			LOG.warn("Agent 사용 안함");
			return;
		}
		result = agentConnector.redisWrite(blockHash, nodeName);
		LOG.warn("write요청:" + result.getStatus().toString());
		// Write 요청 실패 이후 로직 구현 필요(미구현)
	}

}
