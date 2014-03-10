package edu.mcscheduling.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {
	static public String getBasename(String token, String path) {
		String[] split = path.split(token);
		return split[split.length-1];
	}
	
	static public String getDirectory(String path) {
		String[] split = path.split("/");
		String dir = "";
		for (int i=0; i<split.length-2; i++) {
			dir += split[i] + "/";		
		}
		return dir + split[split.length-2];
	}
	
	static public boolean isMailAddress(String mail) {
		 String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		 Pattern regex = Pattern.compile(check);
		 Matcher matcher = regex.matcher(mail);
		 return matcher.matches();
	}
}
