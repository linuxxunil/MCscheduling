package edu.mcscheduling.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
	public static String transMD5(String input) {
		if(input.equals(""))
			return null;
		
		String password="";
		
		try {
			MessageDigest md= MessageDigest.getInstance("MD5");
			
			//MessageDigest md = MessageDigest.getInstance("SHA-1");
		    md.update(input.getBytes());
		    password=byteToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return password;
	}
	
	private static String byteToHex(byte[] input){
		String password="";
		String stmp="";
		for (int n=0; n<input.length; n++){
			stmp=java.lang.Integer.toHexString(input[n] & 0xFF);
			if (stmp.length()==1) 
				password=password+"0"+stmp;
			else 
				password=password+stmp;
		}
		     return password;
	}
}
