package org.csi.yucca.dataservice.insertdataapi.exception;

public class MongoAccessException extends Exception {
	public MongoAccessException(Throwable e) {
		initCause(e);
	}
}
