package com.sunx.rmi.Exception;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class RmiException extends Exception {

	private static final long serialVersionUID = 946272694948941897L;
	String errCode;
	String msg;

	public RmiException(String msg) {
		super(msg);
	}

	public RmiException(Throwable ex) {
		super(ex);
	}

	public RmiException(String errCode, String msg) {
		this(msg);
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
