package com.quiztok.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiztok.api.service.AccountService;
import com.quiztok.api.service.LedgerService;
import com.quiztok.api.util.ValidationCheck;

import generated.Block;
import generated.ResultMessage;
import generated.To;

@RestController
@RequestMapping("/ledger")
public class LedgerController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LedgerController.class);

	@Autowired
	LedgerService ledgerService;
	@Autowired
	AccountService accountService;
	
	//내역 생성
	@PutMapping(value="", produces = {"application/xml","application/json"})
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "privateKey", "type","fromAddress",to[{"address" : "value","formValue" : "value","toValue" :"value"}]
	public ResultMessage createTransaction(@RequestBody Map<String,Object> transactionMessage) {
		//필수정보 유효성 검사(to의 경우 내부 내용 유효성 검증 한번더 필요)
		ValidationCheck.validation(transactionMessage,  "privateKey", "type","fromAddress","to");
		//필수정보 유효성 검사(to의 경우 내부 내용 유효성 검증 한번더 필요) - 임시
		To to = new To();
		ArrayList<To> toMessage = new ArrayList<>();
		ArrayList<Map<String,String>> arrayMap= (ArrayList<Map<String, String>>) transactionMessage.get("to");
		for(int i = 0; i < arrayMap.size(); i++) {
			to.setAddress(arrayMap.get(i).get("address").toString());
			to.setFromValue(new BigDecimal(arrayMap.get(i).get("fromValue").toString()));
			to.setToValue(new BigDecimal(arrayMap.get(i).get("toValue").toString()));
			toMessage.add(to);
		}
		LOG.warn("toMessage : "+ toMessage);
		List<String> result = ledgerService.createTransaction(transactionMessage.get("type").toString(),
				transactionMessage.get("fromAddress").toString(), transactionMessage.get("privateKey").toString(),
				toMessage,transactionMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(result);
		rm.setMessage(result != null ? "생성성공" : "생성실패");
		return rm;
	}
	
	//거래내역 조회
	@PostMapping(value = "/history", produces= {"application/xml","application/json"})
	public ResultMessage history(@RequestParam String accountAddress) {
		List<Block> blockList = ledgerService.history(accountAddress);
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(blockList);
		rm.setMessage(blockList.size() > 0 ? "조회성공" : "조회실패");
		return rm;
	}
	
	//충전
	@PutMapping(value="/charge", produces = {"application/xml","application/json"})
	public ResultMessage createTransactionCharge(@RequestParam String accountAddress,@RequestParam String privateKey,@RequestParam BigDecimal value) {
		List<String> result = ledgerService.createTransactionCharge(accountAddress, privateKey, value);
		ResultMessage rm = new ResultMessage();
		rm.setMessage(result.size() > 0 ?"충전성공":"충전실패");
		return rm;
	}
	
	
}
