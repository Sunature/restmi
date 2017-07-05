package com.sunx.rmi.servlet;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.caucho.hessian.server.HessianServlet;
import com.sunx.rmi.Exception.RmiException;
import com.sunx.rmi.common.Constaints;
import com.sunx.rmi.filter.AfterInvokeHandler;
import com.sunx.rmi.filter.InvokeFilter;
import com.sunx.rmi.helper.Adaptor;
import com.sunx.rmi.helper.AdaptorContainer;
import com.sunx.rmi.helper.AdaptorContext;
import com.sunx.rmi.helper.AdaptorHelper;
import com.sunx.rmi.helper.Mapper;
import com.sunx.rmi.helper.SecuritHelper;
import com.sunx.rmi.service.RespCode;
import com.sunx.rmi.service.RmiRequest;
import com.sunx.rmi.service.RmiResponse;
import com.sunx.rmi.service.RmiService;
import com.sunx.util.JsonUtil;
import com.sunx.util.external.StringUtil;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class RmiServlet extends HessianServlet implements RmiService {

	private static final long serialVersionUID = 3004169127976213160L;

	private InvokeFilter invokeFilter;
	private Properties prop;

	private Adaptor adaptor;
	{
		if (AdaptorContainer.context != null)
			adaptor = new AdaptorContext();
		else
			adaptor = new AdaptorHelper();
	}

	public RmiServlet(Properties prop, InvokeFilter invokeFilter) {
		this.prop = prop;
		this.invokeFilter = invokeFilter;
	}

	@Override
	public <T> RmiResponse<T> execute(RmiRequest req, String signature)
			throws RmiException {
		// 验证请求
		if (req == null || !StringUtil.areNotEmpty(signature, req.getMethod()))
			throw new RmiException(RespCode.BAD_REQUEST, "Bad request.");
		// 验证签名
		try {
			if (!signature.equals(SecuritHelper.generateSign(req,
					prop.getProperty(Constaints.SIGN))))
				throw new RmiException(RespCode.SIGN_ERROR,
						"Incorrect signature.");
		} catch (NoSuchAlgorithmException e) {
			throw new RmiException(RespCode.SIGN_ERROR, e.getMessage());
		}
		return this.process(req);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String rest(String method, String ps, String signature) {
		try {
			// 验证请求
			if (!StringUtil.areNotEmpty(method, signature))
				throw new RmiException(RespCode.BAD_REQUEST, "Bad request.");
			// 验证签名
			if (!signature.equals(SecuritHelper.generateSign(method, ps,
					prop.getProperty(Constaints.SIGN))))
				throw new RmiException(RespCode.SIGN_ERROR,
						"Incorrect signature.");
			RmiRequest req = new RmiRequest(method);
			if (!StringUtil.isEmpty(ps)) {
				Map<String, Object> p = new HashMap<String, Object>();
				req.addParams(JsonUtil.toObject(ps, p.getClass()));
			}
			RmiResponse<?> res = this.process(req);
			return JsonUtil.toJsonStr(res);
		} catch (RmiException e) {
			RmiResponse<?> res = new RmiResponse<String>();
			res.setCode(RespCode.ERROR);
			res.setMsg(e.getMessage());
			res.setTimestamp(System.currentTimeMillis());
			return JsonUtil.toJsonStr(res);
		}
	}

	private <T> RmiResponse<T> process(RmiRequest req) throws RmiException {
		boolean r = true;
		// 过滤器
		if (this.invokeFilter != null)
			r = this.invokeFilter.prevInvoke(req);
		if (!r)
			return null;
		// 匹配接口
		Mapper mp = AdaptorContainer.MAPPER.get(req.getMethod());
		if (mp == null)
			throw new RmiException(RespCode.ERROR,
					"Adaptor or method undefined.");
		Method m = mp.getAction();
		// 装配参数
		int len = mp.getParamNames().length;
		Object[] args = new Object[len];
		if (mp.getParamNames().length > 0 && req.getParams() != null) {
			Type[] pts = m.getGenericParameterTypes();
			// 参数数量对应
			if (pts.length != req.getParams().size())
				throw new RmiException(RespCode.ERROR,
						"arguments count mismatch");
			for (int i = 0; i < len; i++) {
				args[i] = req.getParams().get(mp.getParamNames()[i]);
				// 检查参数类型是否相符
				if (args[i] != null && args[i].getClass() != pts[i]) {
					// 对JSON类型参数处理
					if (args[i].getClass().getSimpleName().equals("JSONObject"))
						args[i] = JsonUtil.toObject(args[i].toString(),
								(Class<?>) pts[i]);
					else
						throw new RmiException(RespCode.ERROR, "argument:"
								+ mp.getParamNames()[i] + " , type mismatch");
				}
			}
		}
		// 执行方法
		Type mrtype = m.getGenericReturnType();
		RmiResponse<T> resp = new RmiResponse<T>();
		resp.setCode(RespCode.SUCCESS);
		Object result = null;
		try {
			Object target = this.adaptor.getAdaptor(mp);
			if (mrtype == Void.TYPE)
				if (m.getParameterTypes().length == 0)
					m.invoke(target);
				else
					m.invoke(target, args);
			else {
				if (m.getParameterTypes().length == 0)
					result = m.invoke(target);
				else
					result = m.invoke(target, args);
			}
		} catch (RmiException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			resp.setCode(RespCode.ERROR);
			resp.setMsg("Api invoke error : " + e.getMessage());
		}
		resp.setResult((Serializable) result);
		resp.setTimestamp(System.currentTimeMillis());
		// 异步回调
		if (this.invokeFilter != null && this.invokeFilter.afterInvokeLock())
			AfterInvokeHandler.getHandler(this.invokeFilter).handle(req, resp);
		// 返回结果
		return resp;
	}

	@Override
	public String printApi() {
		if (AdaptorContainer.MAPPER.isEmpty()) {
			return "no service supply.";
		}
		Iterator<String> it = AdaptorContainer.MAPPER.keySet().iterator();
		StringBuilder api = new StringBuilder(
				"Api list:\n********************************\n");
		while (it.hasNext()) {
			Mapper m = AdaptorContainer.MAPPER.get(it.next());
			api.append("ApiName: ").append(m.getName()).append("\n");
			Class<?>[] types = m.getAction().getParameterTypes();
			api.append("Parameters: ");
			if (types != null && types.length > 0) {
				for (int i = 0, len = types.length; i < len; i++) {
					api.append(types[i].getName()).append(" ")
							.append(m.getParamNames()[i]);
					if (i < len - 1)
						api.append(",");
				}
			}
			api.append("\n");
			api.append("Return: ").append(m.getAction().getGenericReturnType())
					.append("\n");
			api.append("Description: ").append(m.getDesc()).append("\n");
			api.append("********************************\n");
		}
		return api.toString();
	}

}
