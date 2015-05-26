package org.csi.yucca.dataservice.ingest.binary;

public class MyRunnable implements Runnable {

	Object parameter = null;

	public MyRunnable(Object _parameter) {
		parameter = _parameter;
	}

	public void run() {
	}

	public Object getParameter() {
		return parameter;
	}

}