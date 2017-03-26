package com.sunx.rmi.helper;

import com.sunx.rmi.Exception.RmiException;
import com.sunx.rmi.annotation.AdaptorScope;

public class AdaptorHelper implements Adaptor {

	@Override
	public Object getAdaptor(Mapper mapper) throws RmiException {
		Object obj = null;
		try {
			if (mapper.getScope() == AdaptorScope.prototype)
				return mapper.getAdaptor().newInstance();
			obj = AdaptorContainer.ADAPTOR.get(mapper.getClass().getName());
			if (obj == null) {
				obj = mapper.getAdaptor().newInstance();
				AdaptorContainer.ADAPTOR.put(mapper.getClass().getName(), obj);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RmiException(e);
		}
		return obj;
	}

}
