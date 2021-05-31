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

import com.quiztok.api.service.AuthService;
import com.quiztok.api.service.KYCService;
import com.quiztok.api.service.ServiceService;
import com.quiztok.api.util.ValidationCheck;

import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthService authService;
	@Autowired
	KYCService kycService;
	@Autowired
	ServiceService serviceService;

	// 사용자 인증 등록
	// KYCHash, PerivousHash, AuthType, serviceCode, SourceCode, SourceObject
	@PutMapping(value = "", produces = { "application/xml", "application/json" })
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "email","privateKey","serviceCode","authType","sourceType","authId" 
	public ResultMessage createAuth(@RequestBody Map<String,Object> authMessage) {
		//필수정보 유효성 검사
		ValidationCheck.validation(authMessage,"email","privateKey","serviceCode","authType","sourceType","authId");
		//Parameter 에kycBlockHash가 없을경우 email을 통해 가져옴.
		String kycBlockHash = kycService.checkKycBlockHash(authMessage);
		List<String> authBlockHash = authService.createAuth(authMessage.get("email").toString(), kycBlockHash,
				authMessage.get("privateKey").toString(),authMessage.get("serviceCode").toString(),authMessage.get("authType").toString(),
				authMessage.get("sourceType").toString(), authMessage.get("authId").toString(), authMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(authBlockHash);
		rm.setMessage(authBlockHash != null ? "사용자인증 성공" : "서비스 가입되지 않은 사용자");
		return rm;
	}

	// 사용자 인증 검색
	@PostMapping(value = "", produces = { "application/xml", "application/json" })
	public ResultMessage searchAuth(@RequestParam String kycBlockHash,
			@RequestParam String privateKey, @RequestParam(required = false) String authType,
			@RequestParam(required = false) String serviceCode) {
		Block block = authService.searchAuth(kycBlockHash, privateKey, authType, serviceCode);
		ResultMessage rm = new ResultMessage();
		rm.setMessage("auth검색");
		rm.getBlocks().add(block);
		return rm;
	}

	// 사용자 인증 갱신
	/*
	 * @PutMapping(value="", produces = {"application/xml","application/json"})
	 * public ResultMessage updateAuth() { if (kycBlockHash == null) { Block
	 * kycBlock = KYCService.login(email, null); if (kycBlock == null) { throw new
	 * APIException(APIStatus.NO_KYC_BLOCK, "이메일이 존재하지 않습니다."); } kycBlockHash =
	 * kycBlock.getHash(); }
	 * 
	 * return null; }
	 */
}
