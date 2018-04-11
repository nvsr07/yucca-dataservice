package org.csi.yucca.adminapi.client.db;

import org.csi.yucca.adminapi.service.ApiService;
import org.csi.yucca.adminapi.service.ClassificationService;
import org.csi.yucca.adminapi.service.ComponentService;
import org.csi.yucca.adminapi.service.DatasetService;
import org.csi.yucca.adminapi.service.StreamService;
import org.csi.yucca.adminapi.service.TenantService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AdminDBClientDelegate {

	private static AdminDBClientDelegate instance;

	private StreamService streamService;
	private DatasetService datasetService;
	private ComponentService componentService;
	private ApiService apiService;
	private TenantService tenantService;
	private ClassificationService classificationService;
	
	private ApplicationContext context;

	private AdminDBClientDelegate() {
		this.context     = new AnnotationConfigApplicationContext(AppConfig.class);
		streamService    = context.getBean(StreamService.class);
		datasetService   = context.getBean(DatasetService.class);
		componentService = context.getBean(ComponentService.class);
		apiService       = context.getBean(ApiService.class);
		tenantService    = context.getBean(TenantService.class);
		classificationService = context.getBean(ClassificationService.class);
	}

	public static AdminDBClientDelegate getInstance() {
		if (instance == null) {
			instance = new AdminDBClientDelegate();
		}
		return instance;
	}

	public void close() {
		((AnnotationConfigApplicationContext) this.context).close();
	}

	public StreamService getStreamService() {
		return streamService;
	}

	public void setStreamService(StreamService streamService) {
		this.streamService = streamService;
	}

	public DatasetService getDatasetService() {
		return datasetService;
	}

	public void setDatasetService(DatasetService datasetService) {
		this.datasetService = datasetService;
	}

	public ComponentService getComponentService() {
		return componentService;
	}

	public void setComponentService(ComponentService componentService) {
		this.componentService = componentService;
	}

	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	public TenantService getTenantService() {
		return tenantService;
	}

	public void setTenantService(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	public ClassificationService getClassificationService() {
		return classificationService;
	}

	public void setClassificationService(ClassificationService classificationService) {
		this.classificationService = classificationService;
	}
	
}
