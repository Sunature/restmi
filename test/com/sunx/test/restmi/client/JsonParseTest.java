package com.sunx.test.restmi.client;

import java.lang.reflect.Type;

import com.sunx.test.restmi.model.User;
import com.sunx.util.JsonUtil;

public class JsonParseTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String a = "{\"created\":null,\"email\":\"\",\"logName\":\"aaaaa\",\"logPwd\":\"\",\"mobile\":\"\",\"modified\":null,\"roleId\":0,\"status\":0,\"trueName\":\"\"}";

		User u = JsonUtil.toObject(a, User.class);
		System.out.println(u.getLogName());

		String b = "{\"created\":null,\"email\":\"\",\"logName\":\"bbbbbb\",\"logPwd\":\"\"}";

		u = JsonUtil.toObject(b, User.class);
		System.out.println(u.getLogName());

		String c = "{\"asd\":null,\"jjh\":\"\",\"dd\":\"ccc\",\"yyh\":\"\"}";

		u = JsonUtil.toObject(c, User.class);
		System.out.println(u.getLogName());

		Type t = (Type) User.class;
		System.out.println(t);

		// fastJson: type方式，当结构不对应时，会抛出setter not found 异常 ， 转为class方式可以容错
		System.out.println(JsonUtil.toObject(c, t));
		System.out.println(JsonUtil.toObject(c, (Class<?>) t));
	}

}
