package com.sunx.rmi.service;

import com.sunx.rmi.Exception.RmiException;

/**
 * date: 2016年12月8日 上午9:22:09 <br/>
 * 
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public interface RmiService {

	public <T> RmiResponse<T> execute(RmiRequest req, String signature)
			throws RmiException;

	public String printApi();

	/**
	 * for .net or node ...
	 * 
	 * @param method
	 * @param params
	 * @param signature
	 * @return
	 */
	public String rest(String method, String ps, String signature)
			throws RmiException;

}
