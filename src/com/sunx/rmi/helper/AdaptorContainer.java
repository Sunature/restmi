package com.sunx.rmi.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public class AdaptorContainer {

	public static Map<String, Mapper> MAPPER = new HashMap<String, Mapper>();
	public static Map<String, Object> ADAPTOR = new HashMap<String, Object>();

	public static ApplicationContext context;

}
