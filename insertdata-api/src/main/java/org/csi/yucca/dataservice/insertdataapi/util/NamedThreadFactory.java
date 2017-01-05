package org.csi.yucca.dataservice.insertdataapi.util;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

	private String threadName;

	public NamedThreadFactory(String threadName) {
		super();
		this.threadName = threadName;
	}

	public Thread newThread(Runnable r) {
		return new Thread(r, threadName);
	}

}
