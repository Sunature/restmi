package com.sunx.rmi.helper;

import java.lang.reflect.Field;

import com.sunx.rmi.service.RmiRequest;
import com.sunx.util.external.MD5Util;
import com.sunx.util.external.SHAUtil;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class SecuritHelper {

	public static String generateSign(RmiRequest er, String sign) {
		Field[] fields = er.getClass().getDeclaredFields();
		Field.setAccessible(fields, true);
		int len = fields.length;
		String[] arr = new String[len + 1];
		Object f;
		for (int i = 0; i < len; i++) {
			try {
				if (fields[i].getName().equals("params"))
					arr[i] = "";
				else {
					f = fields[i].get(er);
					arr[i] = f == null ? "" : f.toString();
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		arr[len] = sign;
		return SHAUtil.getSHA1(arr);
	}

	public static String generateSign(String method, String params, String sign) {
		String con = method.concat(params);
		con = MD5Util.md5Hex(con);
		con += sign;
		return MD5Util.md5Hex(con);
	}
}
