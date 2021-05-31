package com.quiztok.api.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiztok.api.service.AccountService;
import com.quiztok.api.service.AuthService;
import com.quiztok.api.service.KYCService;
import com.quiztok.api.util.ValidationCheck;

import generated.AccountTransaction;
import generated.Block;
import generated.ResultMessage;

@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService accountService;
	@Autowired
	KYCService kycService;
	@Autowired
	AuthService autService;

	// 계좌 생성
	@PutMapping(value = "", produces = { "application/xml", "application/json" })
	//encryptedTransactionObject에 들어갈 내용 예시(블록 안에서 정보감춤)이므로 해당 정보 정의 필요합니다.
	//TransactionObject에 들어갈 내용 예시(블록 안에서 정보공개)이므로 해당 정보 정의 필요합니다.
	//데이터 숨김 KeyName : encrypted , 데이터공개 KeyName : decrypted 으로 지정해 map 형식으로 입력.
	//input 필수 Kye명칭 : "email", "privateKey", "accountTypeName","accountNickName","currencyCode"
	public ResultMessage createAccount(@RequestBody Map<String,Object> accountMessage) {
		//필수정보 유효성 검사
		ValidationCheck.validation(accountMessage, "email","privateKey","accountTypeName","accountNickName","currencyCode");
		//Parameter 에kycBlockHash가 없을경우 email을 통해 가져옴.
		String kycBlockHash = kycService.checkKycBlockHash(accountMessage);
		AccountTransaction accountTransaction = new AccountTransaction();
		accountTransaction.setKycBlockHash(kycBlockHash);
		accountTransaction.setType(accountMessage.get("accountTypeName").toString());
		accountTransaction.setNickname(accountMessage.get("accountNickName").toString());
		accountTransaction.setCurrencyCode(accountMessage.get("currencyCode").toString());
		Block accountBlock = accountService.createAccount(accountMessage.get("email").toString(),accountTransaction,
				accountMessage.get("privateKey").toString(), accountMessage);
		ResultMessage rm = new ResultMessage();
		rm.setMessage("계좌생성 성공");
		rm.getBlocks().add(accountBlock);
		return rm;
	}

	// 계좌 조회
	@PostMapping(value = "", produces = { "application/xml", "application/json" })
	public ResultMessage searchAccountByKYCBlockHash(@RequestParam String kycBlockHash, @RequestParam String privateKey,
			@RequestParam(required = false, defaultValue = "") String[] accountTypeNames) {

		List<Block> blockList = accountService.searchAccountByKYCBlockHash(kycBlockHash, privateKey, true, accountTypeNames);
		ResultMessage rm = new ResultMessage();
		rm.setMessage("계좌 조회");
		rm.getBlocks().addAll(blockList);
		return rm;
	}

	// 상대방 계좌로 조회
	@GetMapping(value = "address/{accountAddress}", produces = { "application/xml", "application/json" })
	public ResultMessage searchAccountByAddress(@PathVariable String accountAddress) {

		LOG.info("SearchAccountByAddress_Multi 조회_시도 accountAddress :" + accountAddress);
		Block block = accountService.searchAccountByAccountAddress(accountAddress, null, true);
		LOG.warn("상대방 계좌로 조회 : " + block);

		ResultMessage rm = new ResultMessage();
		rm.setMessage("상대방 조회");
		rm.getBlocks().add(block);
		return rm;
	}
	
}
