package com.quiztok.api.vo;

import java.util.List;

public class HostVO {

	private static List<String> host; //접속 허용 IP List
	private static String usingRedis; // redis 사용 여부
	private static String apiHost; // API IP
	private static int apiPort; // API port
	private static boolean usingFilter; // Filter 사용 여부
	private static int reTry; // 요청 재시도 횟수
	private static String logLevel; //Log Level 설정
	private static String version; // API Version 설정
	
	public static List<String> getHost() {
		return host;
	}
	public static void setHost(List<String> host) {
		HostVO.host = host;
	}
	public static String getUsingRedis() {
		return usingRedis;
	}
	public static void setUsingRedis(String usingRedis) {
		HostVO.usingRedis = usingRedis;
	}
	public static String getApiHost() {
		return apiHost;
	}
	public static void setApiHost(String apiHost) {
		HostVO.apiHost = apiHost;
	}
	public static int getApiPort() {
		return apiPort;
	}
	public static void setApiPort(int apiPort) {
		HostVO.apiPort = apiPort;
	}
	public static boolean isUsingFilter() {
		return usingFilter;
	}
	public static void setUsingFilter(boolean usingFilter) {
		HostVO.usingFilter = usingFilter;
	}
	public static int getReTry() {
		return reTry;
	}
	public static void setReTry(int reTry) {
		HostVO.reTry = reTry;
	}
	public static String getLogLevel() {
		return logLevel;
	}
	public static void setLogLevel(String logLevel) {
		HostVO.logLevel = logLevel;
	}
	public static String getVersion() {
		return version;
	}
	public static void setVersion(String version) {
		HostVO.version = version;
	}
	
}
