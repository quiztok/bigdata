package com.quiztok.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiztok.api.connector.NodeConnector;

import generated.Block;

@Service
public class TestMonitorService {

	@Autowired
	NodeConnector nodeConnector;

	public List<Block> monitor(String hash) {
		return nodeConnector.monitor(hash);
	}

	public List<Block> monitorUpdate(String hash) {
		return nodeConnector.monitorUpdate(hash);
	}

}
