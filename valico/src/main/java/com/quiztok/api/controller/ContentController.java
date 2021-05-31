package com.quiztok.api.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiztok.api.service.ContentService;
import com.quiztok.api.service.KYCService;
import com.quiztok.api.util.ValidationCheck;

import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/content")
public class ContentController {

	private static final Logger LOG = LoggerFactory.getLogger(ContentController.class);

	@Autowired
	ContentService contentService;
	@Autowired
	KYCService kycService;

	// 콘텐츠 생성
	@PutMapping(value = "", produces = { "application/xml", "application/json" })
	///encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//현재 Test 환경으로 필요에따라 필수정보 변경 가능함.
	//input 필수 Kye명칭 : "email", "privateKey","contentType","contentSubject","bannerImg","state",List<String>"quizzes"
	public ResultMessage createContent(@RequestBody Map<String,Object> contentMessage) {
		//필수정보 유효성 검사
		ValidationCheck.validation(contentMessage, "email", "privateKey","contentType","contentSubject","bannerImg","state","quizzes");
		List<String> quizzes =  (List<String>) contentMessage.get("quizzes");
		for (int i = 0; i < quizzes.size(); i++) {
			System.out.println(quizzes.get(i));
		}
		//Parameter 에kycBlockHash가 없을경우 email을 통해 search.
		String kycBlockHash = kycService.checkKycBlockHash(contentMessage);
		List<String> result = contentService.createContent(contentMessage.get("email").toString(), kycBlockHash, 
				contentMessage.get("privateKey").toString(), contentMessage.get("contentType").toString(),
				contentMessage.get("contentSubject").toString(),contentMessage.get("bannerImg").toString(),
				contentMessage.get("state").toString(), quizzes, contentMessage);
		ResultMessage rm = new ResultMessage();
		rm.getResult().addAll(result);
		rm.setMessage(result != null ? "생성 성공" : "생성 실패");
		return rm;
	}

	// 콘텐츠 검색 -- Protocol 과 얘기 필요
	@GetMapping(value = "/{contentBlockHash}", produces = { "application/xml", "application/json" })
	public ResultMessage searchContent(@RequestParam String contentBlockHash) {
		List<Block> blockList = contentService.searchContent(contentBlockHash);
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(blockList);
		rm.setMessage(blockList.size() > 0 ? "검색성공" : "검색실패");
		return rm;
	}

}
