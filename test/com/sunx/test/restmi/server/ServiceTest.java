package com.sunx.test.restmi.server;

import com.sunx.rmi.annotation.Action;
import com.sunx.rmi.annotation.Adaptor;

@Adaptor
public class ServiceTest {

	@Action
	public String test(String a, String b) {
		System.out.println(a + b);
		return a + b;
	}

}
