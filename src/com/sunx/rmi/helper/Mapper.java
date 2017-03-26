package com.sunx.rmi.helper;

import java.lang.reflect.Method;

import com.sunx.rmi.annotation.AdaptorScope;

public class Mapper {

	// 服务名称
	private String name;
	// 提供服务的类
	private Class<?> adaptor;
	// 服务类对象作用域，默认singleton,prototype
	private AdaptorScope scope;
	// 方法
	private Method action;
	// 方法描述
	private String desc;
	// 方法参数名称数组
	private String[] paramNames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getAdaptor() {
		return adaptor;
	}

	public void setAdaptor(Class<?> adaptor) {
		this.adaptor = adaptor;
	}

	public AdaptorScope getScope() {
		return scope;
	}

	public void setScope(AdaptorScope scope) {
		this.scope = scope;
	}

	public Method getAction() {
		return action;
	}

	public void setAction(Method action) {
		this.action = action;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

}
