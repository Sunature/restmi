package com.sunx.rmi.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sunx.util.JsonUtil;
import com.sunx.util.external.StringUtil;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class RmiRequest implements Serializable {

	private static final long serialVersionUID = -7283399293301418888L;

	private String key;
	private String secret;
	private String nonce;
	private long timestamp;
	private String method;
	private Map<String, Object> params;
	private String ps;

	public RmiRequest(String key, String secret, String method) {
		this(method);
		this.setKey(key);
		this.setSecret(secret);
	}

	public RmiRequest(String method) {
		this.nonce = StringUtil.createRandomNum(6);
		this.timestamp = System.currentTimeMillis();
		this.method = method;
		this.params = new HashMap<String, Object>();
	}

	public void addParam(String name, Object value) {
		this.params.put(name, value);
		this.ps = JsonUtil.toJsonStr(this.params);
	}

	public void addParams(Map<String, Object> params) {
		this.params.putAll(params);
		this.ps = JsonUtil.toJsonStr(this.params);
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
