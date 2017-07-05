package com.sunx.test.restmi.client;

import com.sunx.rmi.Exception.RmiException;
import com.sunx.rmi.sdk.RmiServer;
import com.sunx.rmi.service.RmiRequest;
import com.sunx.rmi.service.RmiResponse;

public class ClientTest {

	public static void main(String[] args) {
		RmiServer r = new RmiServer();
		r.printApi();
		RmiRequest req = new RmiRequest("ServiceTest.test");
		req.addParam("a", "simplest");
		req.addParam("bgg", "大道至简");
		RmiResponse<String> resp;
		try {
			resp = r.execute(req);
			if (resp.isSuccess()) {
				System.out.println(resp.getResult());
			}
		} catch (RmiException e) {
			e.printStackTrace();
		}
		System.out.println("end...");
	}

}
