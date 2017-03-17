package org.csi.yucca.dataservice.metadataapi.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.csi.yucca.dataservice.metadataapi.filter.AuthorizationRequestFilter;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public ApplicationConfig() {
		//singletons.add(new DetailService());
		//singletons.add(new SearchService());
		singletons.add(new org.csi.yucca.dataservice.metadataapi.service.v01.MetadataService());
		singletons.add(new org.csi.yucca.dataservice.metadataapi.service.v02.MetadataService());
		singletons.add(new CkanService());
		singletons.add(new DcatService());
		singletons.add(new ResourceService());
		
	}


	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<Class<?>>(Arrays.asList(AuthorizationRequestFilter.class));
	}
	
	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Map<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return super.getProperties();
	}
}
