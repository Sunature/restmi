package com.sunx.rmi.helper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.sunx.rmi.Exception.RmiException;
import com.sunx.rmi.annotation.Action;
import com.sunx.rmi.annotation.Adaptor;
import com.sunx.rmi.service.RespCode;
import com.sunx.util.ClassUtil;
import com.sunx.util.FileScanUtil;
import com.sunx.util.RegexUtil;
import com.sunx.util.external.StringUtil;
import com.sunx.util.log.LogUtil;

/**
 * @author Sun
 * @version
 * @since JDK 1.7
 */
public class MappingHelper {

	private String pack_prev;
	private final String suffix = ".class";
	private long start = 0;

	public void loadContext(String pack) {
		this.start = System.currentTimeMillis();
		LogUtil.INFO("load rmicontext mapping ... ", MappingHelper.class);
		String root = null;
		URL url = MappingHelper.class.getResource("/");
		if (url != null)
			root = url.getPath();
		else {
			// run in jar , get the jarFile path
			root = MappingHelper.class.getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			JarFile jf = null;
			try {
				jf = new JarFile(root);
				Enumeration<JarEntry> es = jf.entries();
				String name;
				List<String> classPaths = new ArrayList<String>();
				String path = pack.replaceAll("[\\*]+", "[\\\\s\\\\S]*");
				while (es.hasMoreElements()) {
					name = es.nextElement().getName();
					if (name.endsWith(this.suffix)) {
						name = name.replace(this.suffix, "").replace("/", ".");
						if (RegexUtil.regex(path, name))
							classPaths.add(name);
					}
				}
				load(classPaths);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (jf != null)
					try {
						jf.close();
					} catch (IOException e) {
						jf = null;
						e.printStackTrace();
					}
			}
			return;
		}
		this.pack_prev = pack.substring(0, pack.indexOf("."));
		this.loadFile(new FileScanUtil().scanFiles(root, pack, this.suffix));
	}

	private String getPath(String path) {
		return path.substring(path.indexOf(this.pack_prev)).replace("/", ".")
				.replace("\\", ".").replace(this.suffix, "");
	}

	private void loadFile(List<File> fl) {
		List<String> classPaths = new ArrayList<String>();
		if (fl != null && fl.size() > 0)
			for (File f : fl)
				classPaths.add(getPath(f.getPath()));
		this.load(classPaths);
	}

	private void load(List<String> classPaths) {
		if (classPaths == null || classPaths.size() < 1) {
			LogUtil.DEBUG("load rmicontext mapping : End, no adaptor loaded.",
					MappingHelper.class);
			return;
		}
		boolean r = true;
		String keyPrev;
		for (String path : classPaths) {
			try {
				Class<?> clazz = Class.forName(path);
				Annotation an = clazz.getAnnotation(Adaptor.class);
				if (an == null)
					continue;
				Adaptor ad = (Adaptor) an;
				keyPrev = ad.value();
				if (StringUtil.isEmpty(keyPrev))
					keyPrev = clazz.getSimpleName();
				Method[] ms = clazz.getDeclaredMethods();
				if (ms == null || ms.length == 0)
					continue;
				Mapper mapper;
				for (Method m : ms) {
					try {
						this.checkReturnType(m);
					} catch (RmiException e) {
						e.printStackTrace();
						continue;
					}
					mapper = new Mapper();
					mapper.setAdaptor(clazz);
					mapper.setScope(ad.scope());
					an = m.getAnnotation(Action.class);
					if (an == null)
						continue;
					mapper.setAction(m);
					mapper.setParamNames(ClassUtil.getMethodParamNames(m));
					Action ac = (Action) an;
					mapper.setDesc(ac.desc());
					mapper.setName(keyPrev + "." + ac.value());
					if (StringUtil.isEmpty(ac.value()))
						mapper.setName(keyPrev + "." + m.getName());
					AdaptorContainer.MAPPER.put(mapper.getName(), mapper);
				}
			} catch (ClassNotFoundException e) {
				r = false;
				e.printStackTrace();
			}
		}
		LogUtil.INFO(
				new StringBuilder("load rmicontext mapping: ")
						.append(r ? "Success" : "hasError").append(",cost @")
						.append((System.currentTimeMillis() - start))
						.append("ms").toString(), MappingHelper.class);
	}

	private void checkReturnType(Method m) throws RmiException {
		Type mrtype = m.getGenericReturnType();
		// 泛型
		if (mrtype instanceof ParameterizedType)
			mrtype = ((ParameterizedType) mrtype).getActualTypeArguments()[0];
		Class<?> clazz = ((Class<?>) mrtype);
		// 检查返回类型
		if (mrtype != Void.TYPE && !clazz.isPrimitive()
				&& !Serializable.class.isAssignableFrom(clazz)
				&& !Number.class.isAssignableFrom(clazz))
			throw new RmiException(
					RespCode.ERROR,
					m.getGenericReturnType()
							+ " should implements Serializable or primitive or number .");
	}

}
