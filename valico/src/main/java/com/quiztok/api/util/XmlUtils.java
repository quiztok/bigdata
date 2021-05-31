package com.quiztok.api.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.quiztok.api.vo.AgentVO;
import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

import generated.Block;
import generated.NodeBalancingInfo;

public class XmlUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(XmlUtils.class);

	static File file = new File(AgentVO.getProtocolFilePath(),AgentVO.getProtocolFileName()+".xml");
	/*	public static void main(String[] args) throws IOException {
		generateXsd(new File("xsd/Block.xsd"));
	}*/
	 
	// Transction 설정 관련 사용시(1회성)
	public static final void generateXsd(File file) throws IOException {
		SchemaCompiler compiler = XJC.createSchemaCompiler();
		compiler.parseSchema(new InputSource(file.toURI().toString()));

		S2JJAXBModel model = compiler.bind();
		JCodeModel cm = model.generateCode(null, null);

		// src/main/java 최상단 패키지에서 /generated 패키지내에 필요한 자바 파일 생성함
		cm.build(new File("src/main/java"));
	}
	
	// Agent가 생성한 NodeInfo File Read시 사용
	public static final NodeBalancingInfo readNodeInfoFile()throws JAXBException, SAXParseException{
		return JAXB.unmarshal(file, NodeBalancingInfo.class);
	}
	
	//Node에게 받은 Block 값 Read
	public static final Block read (byte[] bytes) {
		InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(bytes));
		return JAXB.unmarshal(inputStream, Block.class);
	}
}
