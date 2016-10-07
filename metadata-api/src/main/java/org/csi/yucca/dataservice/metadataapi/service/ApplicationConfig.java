package org.csi.yucca.dataservice.metadataapi.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.csi.yucca.dataservice.metadataapi.filter.AuthorizationInterceptor;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public ApplicationConfig() {
		singletons.add(new DetailService());
		singletons.add(new SearchService());
		singletons.add(new CkanService());
		singletons.add(new CacheService());
		singletons.add(new DcatService());
		singletons.add(new ResourceService());
	}

	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(AuthorizationInterceptor.class);
		return classes;
	}
}
