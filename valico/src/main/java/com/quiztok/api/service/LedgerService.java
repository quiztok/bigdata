package com.quiztok.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiztok.api.connector.NodeConnector;
import com.quiztok.api.exception.APIException;
import com.quiztok.api.util.statuscode.APIStatus;
import com.quiztok.api.util.statuscode.AgentStatus;

import generated.AccountTransaction;
import generated.Block;
import generated.LedgerTransaction;
import generated.To;

@Service
public class LedgerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LedgerService.class);
	
	
	@Autowired
	NodeConnector nodeConnector;
	@Autowired
	AccountService accountService;

	//Ledger 조회
	public List<Block> history(String accountAddress) {
		//Protocol CPU 사용량 확인
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		return nodeConnector.searchLedger(accountAddress);
	}
	
	//Ledger 생성
	public List<String> createTransaction(String type, String fromAddress, String privateKey,ArrayList<To> toMessage,
			Map<String, Object> transasctionMessage) {
		//Protocol CPU 사용량 확인
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		
		//fromAddress로 from BlockHash 조회
		Block fromBlock = accountService.searchAccountByAccountAddress(fromAddress, privateKey, true);
		
		String fromBlockHash= fromBlock.getHash();
		
		//from 잔액 검증
		BigDecimal fromBalance = ((AccountTransaction)fromBlock.getTransaction()).getValue();
		BigDecimal fromValue = BigDecimal.ZERO;
		//toAddress로 ToBlockHash 조회
		for(To v : toMessage) {
			fromValue =  v.getFromValue().add(fromValue);
			Block toBlock = null;
			if(v.getAccountBlockHash() == null || v.getAccountBlockHash().equals("")) {
				toBlock = accountService.searchAccountByAccountAddress(v.getAddress(), null, true);
			}
			v.setAccountBlockHash(toBlock.getHash());
		}
		
		if(fromBalance.compareTo(fromValue) < 0) {
			throw new APIException(APIStatus.TRANSACTION_NOT_ENOUGH_VALUE, "잔액이 부족합니다");
		}
		
		LedgerTransaction ledgerTransaction = new LedgerTransaction();
		ledgerTransaction.setType(type);
		ledgerTransaction.setFromAddress(fromAddress);
		ledgerTransaction.setFromAccountBlockHash(fromBlockHash);
		Map<String,Object> encrypted = new HashMap<>();
		Map<String,Object> decrypted = new HashMap<>();
		if(transasctionMessage.get("encrypted") != null) {
			encrypted = (Map<String, Object>) transasctionMessage.get("encrypted");
		}
		if(transasctionMessage.get("decrypted") != null) {
			decrypted = (Map<String, Object>) transasctionMessage.get("decrypted");
		}
		
		List<String> result = nodeConnector.createLedger(ledgerTransaction,privateKey,toMessage,encrypted,decrypted);
		
		if(result.size() > 0) {
			fromBlock = accountService.searchAccountByAccountAddress(fromAddress, privateKey, false);
			accountService.updateAccountForLedger(fromBlock, privateKey, result.get(0));
		}
		//toAccount 갱신
		for(To v : toMessage) {
			Block toBlock = accountService.searchAccountByAccountAddress(v.getAddress(), null, true);
			accountService.updateAccountForLedger(toBlock, null, result.get(0));
		}
		
		return result;
	}
	
	
	//충전 - 테스트용
	public List<String> createTransactionCharge(String accountAddress, String privateKey, BigDecimal value) {
		
		//Protocol CPU 사용량 확인
		if(AgentService.checkProtocolInfo()) {
			LOG.warn("CPU사용률이 50%이상입니다");
			throw new APIException(AgentStatus.PROTOCOL_CPU_ORMORE,"CPU 사용량 50% 이상");
		}
		
		//fromAddress로 from BlockHash 조회
		Block accountBlock = accountService.searchAccountByAccountAddress(accountAddress, privateKey, true);
		String accountBlockHash= accountBlock.getHash();
		
		LedgerTransaction ledgerTransaction = new LedgerTransaction();
		ledgerTransaction.setType("CHARGE");
		ledgerTransaction.setFromAddress(accountAddress);
		ledgerTransaction.setFromAccountBlockHash(accountBlockHash);
		To to = new To();
		to.setAddress(accountAddress);
		to.setAccountBlockHash(accountBlockHash);
		to.setFromValue(BigDecimal.ZERO);
		to.setToValue(value);
		List<To> list = new ArrayList<>();
		list.add(to);
		
	
		List<String> ledgerResult = nodeConnector.createLedger(ledgerTransaction,privateKey,list,Collections.EMPTY_MAP,Collections.emptyMap());
		
		// input : accountBlockHash, privateKey, AccountMessage[accountAddress, value]
		if(ledgerResult.size() > 0) {
			accountService.updateAccountForCharge(accountAddress,accountBlockHash, privateKey, value);
		}
		
		return ledgerResult;
	}
	
	
}
