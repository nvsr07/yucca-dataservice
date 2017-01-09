package org.csi.yucca.dataservice.insertdataapi.exception;

public class MongoAccessException extends Exception {

	private static final long serialVersionUID = 1L;

	public MongoAccessException(Throwable e) {
		initCause(e);
	}
}
