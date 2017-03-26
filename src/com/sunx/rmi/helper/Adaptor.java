package com.sunx.rmi.helper;

import com.sunx.rmi.Exception.RmiException;

public interface Adaptor {

	public Object getAdaptor(Mapper mapper) throws RmiException;

}
