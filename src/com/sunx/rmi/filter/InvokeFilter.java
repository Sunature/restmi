package com.sunx.rmi.filter;

import com.sunx.rmi.service.RmiRequest;
import com.sunx.rmi.service.RmiResponse;

public abstract class InvokeFilter {

	public int corePoolSize = 10;
	public int maximumPoolSize = 20;
	public long keepAliveTime = 60;
	public int queueLength = 1000;

	public abstract boolean prevInvoke(RmiRequest req);

	public abstract boolean afterInvokeLock();

	public abstract <T> void afterInvoke(RmiRequest req, RmiResponse<T> resp);

}
