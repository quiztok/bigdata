package com.quiztok.api.controller;

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

import com.quiztok.api.service.KYCService;
import com.quiztok.api.service.PublishService;
import com.quiztok.api.util.ValidationCheck;

import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/publicsh")
public class PublishController {

	private static final Logger LOG = LoggerFactory.getLogger(PublishController.class);
	
	@Autowired
	PublishService publishService;
	@Autowired
	KYCService kycService;
	//공개블록 생성
	@PutMapping(value = "", produces = { "application/xml", "application/json" })
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "email", "republish", "state","term", "expiryDate"
	public ResultMessage createPublish(@RequestBody Map<String,Object> publishMessage) {
		//필수정보 유효성 검사
		ValidationCheck.validation(publishMessage,"email","republish","state","term","expiryDate");
		//Parameter 에kycBlockHash가 없을경우 email을 통해 search.
		String kycBlockHash = kycService.checkKycBlockHash(publishMessage);
		List<String> result = publishService.createPublish(publishMessage.get("email").toString(),kycBlockHash,
				Integer.parseInt(publishMessage.get("republish").toString()),publishMessage.get("state").toString(),
				Long.parseLong(publishMessage.get("term").toString()),Long.parseLong(publishMessage.get("expiryDate").toString())
				,publishMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(result);
		rm.setMessage(result != null ? "생성 성공" : "생성 실패");
		return rm;
	}
	
	//공개블록 검색
	@PostMapping(value = "", produces = { "application/xml", "application/json" })
	public ResultMessage searchPublish(@RequestParam String publishBlockHash) {
		List<Block> blockList = publishService.searchPublish(publishBlockHash);
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(blockList);
		rm.setMessage(blockList.size() > 0 ? "조회 성공" : "조회 실패");
		return rm;
	}
}
