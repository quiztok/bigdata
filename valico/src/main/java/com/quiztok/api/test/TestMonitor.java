package com.quiztok.api.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiztok.api.service.TestMonitorService;

import generated.ResultMessage;

@RestController
@RequestMapping("/monitor")
public class TestMonitor {
	
	@Autowired
	TestMonitorService service;
	
	//monitor
	@GetMapping("")
	public ResultMessage monitor(@RequestParam(required = false) String hash) {
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(service.monitor(hash));
		return rm;
	}
	
	
	//monitor Update
	@GetMapping("/update")
	public ResultMessage monitorUpdate(@RequestParam(required = false) String hash) {
		ResultMessage rm = new ResultMessage();
		rm.getBlocks().addAll(service.monitorUpdate(hash));
		return rm;
	}
}
