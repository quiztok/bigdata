package com.quiztok.api.util.statuscode;

public class APIStatus {
	
	public static final int SUCCESS = 1; //boolean Type 정상
	public static final int MISSING_ARGUMENT = 3; //유효성 검증
	public static final int FATIL = 815;
	public static final int NODE_EXCEPTION = 810;

	public static final int NO_KYC_BLOCK = 804; //kycBlockHash 없음
	public static final int NO_SERVICE_BLOCK = 807; // serviceBlockHash 없음
	public static final int NO_AUTH_BLOCK = 809; //auth Block 없음
	public static final int NO_ACCOUNT_BLOCK = 814;
	
	public static final int DUPLICATED_ACCOUNT = 806; //이미 생성된 Account
	public static final int DUPLICATED_AUTH = 811;
	
	public static final int TRANSACTION_WRONG_VALUE = 812;
	public static final int TRANSACTION_NOT_ENOUGH_VALUE = 813;
	
}
