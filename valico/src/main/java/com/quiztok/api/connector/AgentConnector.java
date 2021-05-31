package com.quiztok.api.connector;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.quiztok.api.vo.AgentVO;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.agent.AgentGrpc;
import proto.agent.AgentGrpc.AgentBlockingStub;
import proto.agent.BlockRequest;
import proto.agent.BlockResponse;


@Component
@DependsOn({"APIConfig"})
public class AgentConnector {
	
	private ManagedChannel channel;
	private AgentBlockingStub blockingStub;
	
	private AgentConnector() {
		this.channel = ManagedChannelBuilder.forAddress(AgentVO.getAgentHost(), AgentVO.getAgentPort()).usePlaintext().build();
		this.blockingStub = AgentGrpc.newBlockingStub(channel);
	}
	
	//API Redis Write
	public BlockResponse redisWrite(String blockHash,String nodeName) {
		return blockingStub.apiWriteRedis(BlockRequest.newBuilder()
				.setBlockHash(blockHash)
				.setNodeName(nodeName)
				.build());
	}
	
	//API Redis Read
	public BlockResponse redisRead(String blockHash) {
		return blockingStub.apiReadRedis(BlockRequest.newBuilder()
				.setBlockHash(blockHash)
				.build());
	}

}
