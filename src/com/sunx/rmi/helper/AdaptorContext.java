package com.sunx.rmi.helper;

import com.sunx.rmi.Exception.RmiException;

public class AdaptorContext implements Adaptor {

	@Override
	public Object getAdaptor(Mapper mapper) throws RmiException {
		return AdaptorContainer.context.getBean(mapper.getAdaptor());
	}

}
