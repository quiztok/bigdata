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

import com.quiztok.api.exception.APIException;
import com.quiztok.api.service.KYCService;
import com.quiztok.api.util.ValidationCheck;
import com.quiztok.api.util.statuscode.APIStatus;

import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/kyc")
public class KYCController {

	private static final Logger LOG = LoggerFactory.getLogger(KYCController.class);
	
	@Autowired
	KYCService kycService;
	
	//회원가입
	@PutMapping(value = "", produces= {"application/xml","application/json"})
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 Map Type으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "email", "pin", "name"
	public ResultMessage createKyc(@RequestBody Map<String,Object> kycMessage) {
		//필수 데이터  유효성 검증
		ValidationCheck.validation(kycMessage, "email","pin","name");
		LOG.info("회원가입"+"이메일 :"+kycMessage.get("email").toString() + " pin : "+ kycMessage.get("pin").toString()
				+" name :" + kycMessage.get("name").toString());
		List<String> result = kycService.createKyc(kycMessage.get("email").toString(),kycMessage.get("pin").toString(),
				kycMessage.get("name").toString(), kycMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(result);
		rm.setMessage(result != null ? "회원가입 성공" : "회원가입 실패");
		return rm;
	}
	
	//로그인
	@PostMapping(value="", produces= {"application/xml","application/json"})
	public ResultMessage searchKyc(@RequestParam String email,  @RequestParam String pin) {
		LOG.warn("Login_Multi, email : " + email + ", pin : " + pin);
		Block block = kycService.searchKyc(email, pin);
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().add(block);
		rm.setMessage(block != null ? "로그인 성공" : "로그인 회원 정보 없음");
		return rm;
	}
	
	//회원탈퇴
	@PutMapping(value="/deactivate", produces= {"application/xml","application/json"})
	public ResultMessage deactivate(@RequestParam String email,
									@RequestParam(required = false) String kycBlockHash, 
								    @RequestParam String privateKey, 
									@RequestParam String pin,
									@RequestParam String serviceCode) {
		
		if(kycBlockHash == null) {
			Block kycBlock = kycService.searchKyc(email, pin);
			if(kycBlock == null) {
				throw new APIException(APIStatus.NO_KYC_BLOCK, "비활성활 이메일이 존재하지 않습니다.");
			}
			kycBlockHash = kycBlock.getHash();
		}
		
		List<String> deactivatedKycBlockHash = kycService.deactivate(email, kycBlockHash, privateKey, pin,serviceCode);
		ResultMessage rm = new ResultMessage();
		rm.setMessage("회원탈퇴 성공");
		rm.getResult().addAll(deactivatedKycBlockHash);
		return rm;
	}
}
