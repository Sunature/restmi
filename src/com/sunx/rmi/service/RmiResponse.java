package com.sunx.rmi.service;

import java.io.Serializable;

import com.sunx.util.log.LogUtil;

/**
 * @author Sun
 * @version
 * @param <T>
 * @since JDK 1.7
 */
public class RmiResponse<T> implements Serializable {

	private static final long serialVersionUID = 9203221356807970143L;

	private String code;
	private String msg;
	private Long timestamp;
	private T result;

	public boolean isSuccess() {
		return this.code.equals(RespCode.SUCCESS);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public T getResult() {
		return result;
	}

	@SuppressWarnings("unchecked")
	public void setResult(Serializable result) {
		try {
			this.result = (T) result;
		} catch (Exception ex) {
			LogUtil.ERROR("RmiResponse setResult error:" + ex.getMessage(),
					this.getClass());
			this.result = null;
			this.code = RespCode.ERROR;
		}
	}

}
