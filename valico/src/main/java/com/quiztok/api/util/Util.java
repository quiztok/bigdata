package com.quiztok.api.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import generated.Block;

public class Util {
	
	public static List<Block> toBlockList(List<proto.node.Block> blocks){
		List<Block> blockList = new ArrayList<>();
		if(blocks == null) {
			return blockList;
		}
		for(proto.node.Block block : blocks) {
			blockList.add(XmlUtils.read(block.getBlock().toByteArray()));
		}
		return blockList;
	}

	
	//SHA256 Chage 암호화를 위해 사용
	public static String SHA256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			//Applies sha256 to out input
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			for(int i = 0; i< hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
