package com.quiztok.api.vo;

public class ToVO {
	 //to[address, accountBlockHash, fromvalue, tovalue]
	private static String toaddress;
	private static String accountBlockHash;
	private static String fromValue;
	private static String toValue;
	public static String getToaddress() {
		return toaddress;
	}
	public static void setToaddress(String toaddress) {
		ToVO.toaddress = toaddress;
	}
	public static String getAccountBlockHash() {
		return accountBlockHash;
	}
	public static void setAccountBlockHash(String accountBlockHash) {
		ToVO.accountBlockHash = accountBlockHash;
	}
	public static String getFromValue() {
		return fromValue;
	}
	public static void setFromValue(String fromValue) {
		ToVO.fromValue = fromValue;
	}
	public static String getToValue() {
		return toValue;
	}
	public static void setToValue(String toValue) {
		ToVO.toValue = toValue;
	}
	
	
	
}
