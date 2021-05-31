package com.quiztok.api.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiztok.api.vo.AgentVO;
import com.quiztok.api.vo.HostVO;


@EnableAutoConfiguration
@Configuration
@PropertySource(value = {"file:APIConfig.json"}, factory=APIConfiguration.JsonLoader.class)
public class APIConfiguration extends SpringBootServletInitializer{

	private static final Logger LOG = LoggerFactory.getLogger(APIConfiguration.class);
	
	
	public static class JsonLoader implements PropertySourceFactory{
		@Override
		public org.springframework.core.env.PropertySource<?> createPropertySource(String name,
				EncodedResource resource) throws IOException {
			Map readValue = new ObjectMapper().readValue(resource.getInputStream(), Map.class);
			return new MapPropertySource("json-source", readValue);
		}
	}
	
	@Bean
	public Object APIConfig(Environment e) {
		List<String> list = e.getProperty("host",List.class);
		String agentHost = e.getProperty("agentHost",String.class);
		int agentPort = e.getProperty("agentPort",int.class);
		boolean usingAgent = e.getProperty("usingAgent", boolean.class);
		boolean usingBalancing = e.getProperty("usingBalancing",boolean.class);
		boolean usingRedis = e.getProperty("usingRedis",boolean.class);
		String apiHost = e.getProperty("NodeHost",String.class);
		int apiPort = e.getProperty("NodePort",int.class);
		boolean usingFilter = e.getProperty("usingFilter", boolean.class);
		int retry = e.getProperty("retry",int.class);
		String protocolFilePath = e.getProperty("protocolInfoFilePath",String.class);
		String protocolFileName = e.getProperty("protocolInfoFileName",String.class);
		double protocolCpuPercent = e.getProperty("protocolCpuPercent", double.class);
		String logLevel = e.getProperty("logLevel",String.class);
		String version = e.getProperty("version",String.class);
		
		HostVO.setHost(list);
		HostVO.setApiHost(apiHost);
		HostVO.setApiPort(apiPort);
		HostVO.setUsingFilter(usingFilter);
		HostVO.setReTry(retry);
		HostVO.setLogLevel(logLevel);
		HostVO.setVersion(version);
		
		
		AgentVO.setAgentHost(agentHost);
		AgentVO.setAgentPort(agentPort);
		AgentVO.setUsingAgent(usingAgent);
		AgentVO.setUsingBalancing(usingBalancing);
		AgentVO.setUsingRedis(usingRedis);
		AgentVO.setProtocolFilePath(protocolFilePath);
		AgentVO.setProtocolFileName(protocolFileName);
		AgentVO.setProtocolCpuPercent(protocolCpuPercent);
		log();
		
		return new Object();
	}
	
	private void log() {
        String logLevel = HostVO.getLogLevel();
        LOG.info("LOG_LEVEL : " + logLevel);

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        root.setLevel(ch.qos.logback.classic.Level.toLevel(logLevel));
    }
}
