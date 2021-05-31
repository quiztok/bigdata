package com.quiztok.api.vo;

public class AgentVO {
	
	private static String agentHost; // Agent Host 설정
	private static int agentPort; // Agent Port 설정
	private static boolean usingAgent; // Agent 사용 유,무
	private static boolean usingBalancing; // Protocol Balancing check 사용 여부
	private static boolean usingRedis; // Redis 사용 여부 
	private static String protocolFilePath; // Save Protocol Balancing File Path
	private static String protocolFileName; // Save Protocol File Name
	private static double protocolCpuPercent; // Protocol CPU 응답 한계 설정 값
	private static String protocolHost; // 가장 한가한 Node IP
	
	public static String getAgentHost() {
		return agentHost;
	}
	public static void setAgentHost(String agentHost) {
		AgentVO.agentHost = agentHost;
	}
	public static int getAgentPort() {
		return agentPort;
	}
	public static void setAgentPort(int agentPort) {
		AgentVO.agentPort = agentPort;
	}
	public static boolean isUsingAgent() {
		return usingAgent;
	}
	public static void setUsingAgent(boolean usingAgent) {
		AgentVO.usingAgent = usingAgent;
	}
	public static boolean isUsingBalancing() {
		return usingBalancing;
	}
	public static void setUsingBalancing(boolean usingBalancing) {
		AgentVO.usingBalancing = usingBalancing;
	}
	public static boolean isUsingRedis() {
		return usingRedis;
	}
	public static void setUsingRedis(boolean usingRedis) {
		AgentVO.usingRedis = usingRedis;
	}
	public static String getProtocolFilePath() {
		return protocolFilePath;
	}
	public static void setProtocolFilePath(String protocolFilePath) {
		AgentVO.protocolFilePath = protocolFilePath;
	}
	public static String getProtocolFileName() {
		return protocolFileName;
	}
	public static void setProtocolFileName(String protocolFileName) {
		AgentVO.protocolFileName = protocolFileName;
	}
	public static double getProtocolCpuPercent() {
		return protocolCpuPercent;
	}
	public static void setProtocolCpuPercent(double protocolCpuPercent) {
		AgentVO.protocolCpuPercent = protocolCpuPercent;
	}
	public static String getProtocolHost() {
		return protocolHost;
	}
	public static void setProtocolHost(String protocolHost) {
		AgentVO.protocolHost = protocolHost;
	}

	
}
