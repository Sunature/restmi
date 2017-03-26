package com.sunx.rmi.filter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sunx.rmi.service.RmiRequest;
import com.sunx.rmi.service.RmiResponse;

public class AfterInvokeHandler {

	private static volatile AfterInvokeHandler afterInvokeHandler;

	public static AfterInvokeHandler getHandler(InvokeFilter invoke) {
		if (afterInvokeHandler == null) {
			synchronized (AfterInvokeHandler.class) {
				if (afterInvokeHandler == null)
					afterInvokeHandler = new AfterInvokeHandler(invoke);
			}
		}
		return afterInvokeHandler;
	}

	private ThreadPoolExecutor executor;
	private InvokeFilter invoke;

	private AfterInvokeHandler(InvokeFilter invoke) {
		this.invoke = invoke;
		this.executor = new ThreadPoolExecutor(invoke.corePoolSize,
				invoke.maximumPoolSize, invoke.keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(invoke.queueLength),
				new ThreadPoolExecutor.DiscardPolicy());
	}

	public <T> void handle(final RmiRequest req, final RmiResponse<T> resp) {
		this.executor.execute(new Runnable() {
			public void run() {
				invoke.afterInvoke(req, resp);
			}
		});
	}

}
