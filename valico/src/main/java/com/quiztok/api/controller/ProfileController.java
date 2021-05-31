package com.quiztok.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiztok.api.service.KYCService;
import com.quiztok.api.service.ProfileService;
import com.quiztok.api.util.ValidationCheck;

import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	ProfileService profileService;
	@Autowired
	KYCService kycService;
	
	//개인정보 생성
	@PutMapping(value = "", produces = { "application/xml", "application/json" })
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "email","privateKey"
	public ResultMessage createProfile(@RequestBody Map<String, Object> profileMessage) {
		//필수정보 유효성 검사
		ValidationCheck.validation(profileMessage, "email","privateKey");
		//Parameter 에kycBlockHash가 없을경우 email을 통해 search.
		String kycBlockHash = kycService.checkKycBlockHash(profileMessage);
		List<String> result = profileService.createProfile(profileMessage.get("email").toString(), kycBlockHash,
				profileMessage.get("privateKey").toString(), profileMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(result);
		rm.setMessage(result != null ? "생성 성공" : "생성 실패");
		return rm;
	}
	
	//개인정보 검색
	@PostMapping(value = "", produces = { "application/xml", "application/json" })
	public ResultMessage searchProfile(@RequestParam String kycBlockHash,@RequestParam String privateKey) {
		List<Block> blockList = profileService.searchProfile(kycBlockHash, privateKey);
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(blockList);
		rm.setMessage(blockList.size() > 0 ? "조회 성공" : "조회 실패");
		return rm;
	}

	
}
