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
import com.quiztok.api.service.ServiceService;
import com.quiztok.api.util.ValidationCheck;
import com.quiztok.api.util.statuscode.APIStatus;

import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/service")
public class ServiceController {
	// KYCHash, PreviousHash, ServiceCode(Unique), ServiceSign, SerivceObject

	private static final Logger LOG = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	ServiceService serviceService;
	@Autowired
	KYCService kycService;

	// 서비스 가입
	@PutMapping(value = "", produces = { "application/xml", "application/json" })
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "email", "privateKey","serviceCode","serviceSign"
	public ResultMessage createService(@RequestBody Map<String,Object> serviceMessage) {
		//필수정보 유효성 검사
		ValidationCheck.validation(serviceMessage, "email","privateKey","serviceCode","serviceSign");
		//Parameter 에kycBlockHash가 없을경우 email을 통해 search.
		String kycBlockHash = kycService.checkKycBlockHash(serviceMessage);
		List<String> signUpServiceBlockHash = serviceService.createService(serviceMessage.get("email").toString(), 
				kycBlockHash, serviceMessage.get("privateKey").toString(), serviceMessage.get("serviceCode").toString(),
				serviceMessage.get("serviceSign").toString(), serviceMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(signUpServiceBlockHash);
		rm.setMessage(signUpServiceBlockHash != null ? "가입 성공" : "가입 실패");
		return rm;
	}

	// 서비스 조회
	@PostMapping(value = "", produces = { "application/xml", "application/json" })
	public ResultMessage searchService(@RequestParam String kycBlockHash,
			@RequestParam String privateKey,@RequestParam String serviceCode) {
		List<Block> blockList = serviceService.searchService(kycBlockHash, privateKey, serviceCode);
		
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(blockList);
		rm.setMessage(blockList.size() > 0 ? "조회 성공" : "조회 실패");
		return rm;
	}

	// 서비스 갱신 -- 추후 구현
	@PutMapping(value = "/update", produces = { "application/xml", "application/json" })
	public ResultMessage updateSearvice(@RequestParam String email, @RequestParam(required = false) String kycBlockHash,
			@RequestParam String privateKey,@RequestParam String serviceCode,
			boolean deactivate) {
		// kycBlockHash 가 없을경우 email을 통해 구함
		if (kycBlockHash == null) {
			Block kycBlock = kycService.searchKyc(email, null);
			if (kycBlock == null) {
				throw new APIException(APIStatus.NO_KYC_BLOCK, "비활성활 이메일이 존재하지 않습니다.");
			}
			kycBlockHash = kycBlock.getHash();
		}
		// deactivate 회원탈퇴 로 인한 갱신 일경우 true, 일반갱신 일경우 false
		deactivate = false;
		List<String> updateServiceBlockHash = serviceService.updateService(email, kycBlockHash, privateKey, serviceCode,
				deactivate);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(updateServiceBlockHash);
		rm.setMessage(updateServiceBlockHash != null ? "갱신 성공" : "갱신 실패");
		return rm;
	}

	// 서비스 해지 -- 현재 미구현
	@PutMapping(value = "/deactivate", produces = { "application/xml", "application/json" })
	public ResultMessage deactivateService(@RequestParam String email,@RequestParam String serviceCode,@RequestParam String privateKey,@RequestParam String pin) {

		return null;
	}

}
