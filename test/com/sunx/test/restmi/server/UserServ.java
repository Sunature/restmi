package com.sunx.test.restmi.server;

import org.springframework.stereotype.Service;

import com.sunx.rmi.annotation.Action;
import com.sunx.rmi.annotation.Adaptor;
import com.sunx.test.restmi.model.User;
import com.sunx.util.JsonUtil;

@Adaptor("user")
@Service
public class UserServ {

	@Action
	public boolean addUser(User u) {
		System.out.println(JsonUtil.toJsonStr(u));
		return true;
	}

}
