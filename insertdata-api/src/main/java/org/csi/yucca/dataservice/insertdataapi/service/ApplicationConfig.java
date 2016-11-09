package org.csi.yucca.dataservice.insertdataapi.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class ApplicationConfig extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public ApplicationConfig() {
		singletons.add(new StreamService());
		singletons.add(new DatasetService());
		singletons.add(new MediaService());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
//		classes.add(AuthorizationInterceptor.class);
		return classes;
	}
}
