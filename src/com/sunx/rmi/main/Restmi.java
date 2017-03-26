package com.sunx.rmi.main;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;

import com.sunx.rmi.common.Constaints;
import com.sunx.rmi.filter.InvokeFilter;
import com.sunx.rmi.helper.AdaptorContainer;
import com.sunx.rmi.helper.MappingHelper;
import com.sunx.rmi.servlet.RmiServlet;
import com.sunx.util.PropertyUtil;

/**
 * date: 2016年12月8日 上午9:26:40 <br/>
 * 
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class Restmi {

	private void startupServer() {
		Server server = new Server(Integer.parseInt(this.prop
				.getProperty(Constaints.PORT)));
		HandlerList handlerList = new HandlerList();
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new RmiServlet(this.prop,
				this.filter)), this.prop.getProperty(Constaints.PATH));
		handlerList.addHandler(context);
		handlerList.addHandler(new DefaultHandler());
		server.setHandler(handlerList);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Restmi() {
	}

	Properties prop;
	InvokeFilter filter;
	MappingHelper mphelper;

	public Restmi(InvokeFilter filter) {
		this.filter = filter;
	}

	public void startup(String propertiesFileRelativePath) {
		if (!propertiesFileRelativePath.startsWith("/"))
			propertiesFileRelativePath = "/" + propertiesFileRelativePath;
		prop = PropertyUtil.loadProperties(propertiesFileRelativePath);
		this.mphelper = new MappingHelper();
		this.mphelper.loadContext(this.prop.getProperty(Constaints.PACKAGE));
		final Restmi rmi = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				rmi.startupServer();
			}
		}).start();
		this.mphelper = null;
	}

	public void startup() {
		this.startup("/rmi.properties");
	}

	public void startup(ApplicationContext springContext) {
		AdaptorContainer.context = springContext;
		this.startup();
	}

	public void startup(ApplicationContext springContext,
			String propertiesFileRelativePath) {
		AdaptorContainer.context = springContext;
		this.startup(propertiesFileRelativePath);
	}

}
