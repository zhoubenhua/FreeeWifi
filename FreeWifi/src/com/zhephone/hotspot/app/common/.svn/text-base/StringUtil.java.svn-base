package com.zhephone.hotspot.app.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class StringUtil {
	// 电话号码的规格检验
	public static final String Phone="^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
	public static final String URL_REG_EXPRESSION = "^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*";
	public static final String EMAIL_REG_EXPRESSION = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
	public static boolean isPhone(String s) {
		if (s == null) {
			return false;
		}
		return Pattern.matches(Phone,s);//false手机号输入不规范
	}
	
	
	public static boolean isNull(String s) {
		if (s == null) {
			return true;
		}
		return s.trim()=="";
	}
	public static boolean isUrl(String s) {
		if (s == null) {
			return false;
		}
		return Pattern.matches(URL_REG_EXPRESSION, s);
	}

	public static boolean isEmail(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches(EMAIL_REG_EXPRESSION, s);
	}

	public static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches("\\s*", s);
	}

	public static String join(String spliter, Object[] arr) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		if (spliter == null) {
			spliter = "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				break;
			}
			if (arr[i] == null) {
				continue;
			}
			builder.append(arr[i].toString());
			builder.append(spliter);
		}
		return builder.toString();
	}

	public static String fromFile(File f) throws IOException {
		InputStream is = new FileInputStream(f);
		byte[] bs = new byte[is.available()];
		is.read(bs);
		is.close();
		return new String(bs);
	}


}
