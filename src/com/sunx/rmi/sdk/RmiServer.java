package com.sunx.rmi.sdk;

import java.net.MalformedURLException;
import java.util.Properties;

import com.caucho.hessian.client.HessianProxyFactory;
import com.sunx.rmi.Exception.RmiException;
import com.sunx.rmi.common.Constaints;
import com.sunx.rmi.helper.SecuritHelper;
import com.sunx.rmi.service.RmiRequest;
import com.sunx.rmi.service.RmiResponse;
import com.sunx.rmi.service.RmiService;
import com.sunx.util.PropertyUtil;
import com.sunx.util.external.StringUtil;
import com.sunx.util.log.LogUtil;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class RmiServer {

	private RmiService service;

	public RmiServer() {
		this("/rmi.properties");
	}

	private Properties prop;

	public RmiServer(String path) {
		path = path.startsWith("/") ? path : "/" + path;
		prop = PropertyUtil.loadProperties(path);
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setChunkedPost(false);
		factory.setConnectTimeout(3000);
		factory.setReadTimeout(3000);
		try {
			String host = prop.getProperty(Constaints.HOST);
			StringBuilder p = new StringBuilder("http://").append(host);
			if (this.isNotDomain(host))
				p.append(":").append(prop.getProperty(Constaints.PORT));
			p.append(prop.getProperty(Constaints.PATH));
			this.service = (RmiService) factory.create(RmiService.class,
					p.toString());
		} catch (MalformedURLException | RmiException e) {
			e.printStackTrace();
			LogUtil.ERROR("init service error.", RmiServer.class);
		}
	}

	private static final String LOCAL = "localhost";

	private boolean isNotDomain(String host) throws RmiException {
		if (host == null)
			throw new RmiException("host can't be null.");
		return host.equals(LOCAL)
				|| StringUtil.isNumeric(host.replace('.', '0'));
	}

	public <T> RmiResponse<T> execute(RmiRequest req) throws RmiException {
		return this.service.execute(
				req,
				SecuritHelper.generateSign(req,
						prop.getProperty(Constaints.SIGN)));
	}

	public void printApi() {
		System.out.println(this.service.printApi());
	}

}
