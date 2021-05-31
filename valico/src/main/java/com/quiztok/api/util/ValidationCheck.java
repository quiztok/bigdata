package com.quiztok.api.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import com.quiztok.api.exception.APIException;
import com.quiztok.api.util.statuscode.APIStatus;

import generated.To;

public class ValidationCheck {
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	public static void validation(Map<String,Object> message,String...keyArgs) {
		if(message == null) {
			return;
		}
		if(keyArgs != null) {
			for(String key : keyArgs) {
				Object obj = message.get(key);
				if(obj == null) {
					throw new APIException(APIStatus.MISSING_ARGUMENT, "필수정보 " + key + "이(가) 없습니다.");
				}
			}
		}
	}
	
	//Ledger To List 전용 - 로직구현필요
	public static void ledgerValidation(ArrayList<To> to) {
		for(To t : to) {
			System.out.println(t.toString());
			if(t.getAddress() == null ||t.getFromValue() == null || t.getToValue() == null) {
				throw new APIException(APIStatus.MISSING_ARGUMENT,"Ledger의 To내용에 NULL을 포함할수 없습니다.");
			}else if(t.getAddress().equals("") || t.getFromValue().equals(BigDecimal.ZERO)/*|| t.getToValue().equals("")*/) {
				throw new APIException(APIStatus.MISSING_ARGUMENT,"Ledger의 To 내용에 빈문자 또는 0원을 보낼수 없습니다.");
			}
		}
	}
	
}
