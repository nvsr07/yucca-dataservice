package org.csi.yucca.adminapi.client.cache;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;
import org.csi.yucca.adminapi.client.AdminApiClientDelegate;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.cache.key.KeyCache;
import org.csi.yucca.adminapi.client.cache.key.StreamDatasetByDatasetCodeDatasetVersionKeyCache;
import org.csi.yucca.adminapi.client.cache.key.StreamDatasetByIdDatasetDatasetVersionKeyCache;
import org.csi.yucca.adminapi.client.cache.key.StreamDatasetBySoCodeStreamCodeKeyCache;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;



public class CacheUtilNone {

	private static final long DURATION = 10; 
	private static final long MAXIMUM_SIZE = 100; 

	private static final String BACK_OFFICE_DATASETS = "/1/backoffice/datasets/";
	private static final String BACK_OFFICE_STREAMS = "/1/backoffice/streams/";
	private static final String BACK_OFFICE_DATASETS_DATASETCODE = "/1/backoffice/datasets/datasetCode=";
	private static final String BACK_OFFICE_MEASURE_UNITS = "/1/backoffice/measure_units/";
	private static final String BACK_OFFICE_API = "/1/backoffice/api/";
	private static final String BACK_OFFICE_TENANTS = "/1/backoffice/tenants";
	private static final String BACK_OFFICE_DATASETS_ORGANIZATION_CODE = "/1/backoffice/datasets/organizationCode=";
	private static final String BACK_OFFICE_ALLINEAMENTO_BY_ID_ORG = "/1/backoffice/allineamento/idOrganization=";
	private static final String BACK_OFFICE_ORGANIZATIONS = "/1/backoffice/organizations";
	
	private static Cache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByDatasetCodeCache;
	private static Cache<StreamDatasetByDatasetCodeDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByDatasetCodeDatasetVersionCache;	
	private static Cache<StreamDatasetByIdDatasetDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByIdDatasetDatasetVersionCache;
	private static Cache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByIdDatasetCache;
	private static Cache<StreamDatasetBySoCodeStreamCodeKeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetBySoCodeStreamCodeCache;
	private static Cache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetCache;
	private static Cache<KeyCache, BackofficeDettaglioApiResponse> dettaglioApiCache;
	private static Cache<KeyCache, MeasureUnitResponse> measureUnitCache;
	private static Cache<KeyCache, List<TenantManagementResponse>> tenantListCache;
	private static Cache<KeyCache, List<BackofficeDettaglioStreamDatasetResponse>> listaStreamDatasetByOrganizationCodeCache;
	private static Cache<KeyCache, List<AllineamentoScaricoDatasetResponse>> allineamentoResponseCache;
	private static Cache<KeyCache, List<OrganizationResponse>> organizationListCache;
	
	private static  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	
	static {
		
		dettaglioStreamDatasetCache = new Cache2kBuilder<KeyCache, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
	}

	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDataset(KeyCache keyCache) throws AdminApiClientException {
		try {
//			return dettaglioStreamDatasetCache.get(keyCache);
			
			return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
			
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	static {
		dettaglioStreamDatasetBySoCodeStreamCodeCache = new Cache2kBuilder<StreamDatasetBySoCodeStreamCodeKeyCache, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<StreamDatasetBySoCodeStreamCodeKeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(StreamDatasetBySoCodeStreamCodeKeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
	}

	static {
		allineamentoResponseCache = new Cache2kBuilder<KeyCache, List<AllineamentoScaricoDatasetResponse>>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, List<AllineamentoScaricoDatasetResponse>>() {
					public List<AllineamentoScaricoDatasetResponse> load(KeyCache key) throws Exception {
						return (List<AllineamentoScaricoDatasetResponse>) getList(BACK_OFFICE_ALLINEAMENTO_BY_ID_ORG, key, AllineamentoScaricoDatasetResponse.class);	
					};
				}).build();
				
	}
	
	
	static {
		organizationListCache = new Cache2kBuilder<KeyCache, List<OrganizationResponse>>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, List<OrganizationResponse>>() {
					public List<OrganizationResponse> load(KeyCache key) throws Exception {
						return (List<OrganizationResponse>) getList(BACK_OFFICE_ORGANIZATIONS, key, OrganizationResponse.class);	
					};
				}).build();
	}
	
	public static List<AllineamentoScaricoDatasetResponse> getAllineamento(KeyCache keyCache) throws Exception {
//		return allineamentoResponseCache.get(keyCache);
		return (List<AllineamentoScaricoDatasetResponse>) getList(BACK_OFFICE_ALLINEAMENTO_BY_ID_ORG, keyCache, AllineamentoScaricoDatasetResponse.class);
	}
	
	static {
		listaStreamDatasetByOrganizationCodeCache = new Cache2kBuilder<KeyCache, List<BackofficeDettaglioStreamDatasetResponse>>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, List<BackofficeDettaglioStreamDatasetResponse>>() {
					public List<BackofficeDettaglioStreamDatasetResponse> load(KeyCache key) throws Exception {
						return (List<BackofficeDettaglioStreamDatasetResponse>) getList(BACK_OFFICE_DATASETS_ORGANIZATION_CODE, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
	}
	
	static {
		tenantListCache = new Cache2kBuilder<KeyCache, List<TenantManagementResponse>>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, List<TenantManagementResponse>>() {
					public List<TenantManagementResponse> load(KeyCache key) throws Exception {
						return (List<TenantManagementResponse>) getList(BACK_OFFICE_TENANTS, key, TenantManagementResponse.class);	
					};
				}).build();
	}
	
	static {
		dettaglioStreamDatasetByDatasetCodeCache = new Cache2kBuilder<KeyCache, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
				
	}

	static {
		dettaglioStreamDatasetByDatasetCodeDatasetVersionCache = new Cache2kBuilder<StreamDatasetByDatasetCodeDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<StreamDatasetByDatasetCodeDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(StreamDatasetByDatasetCodeDatasetVersionKeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
		}

	static {
		dettaglioStreamDatasetByIdDatasetDatasetVersionCache = new Cache2kBuilder<StreamDatasetByIdDatasetDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<StreamDatasetByIdDatasetDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(StreamDatasetByIdDatasetDatasetVersionKeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
	
	}
	
	static {
		dettaglioStreamDatasetByIdDatasetCache = new Cache2kBuilder<KeyCache, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, key, BackofficeDettaglioStreamDatasetResponse.class);	
					};
				}).build();
	}
	
	static {
		measureUnitCache = new Cache2kBuilder<KeyCache, MeasureUnitResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, MeasureUnitResponse>() {
					public MeasureUnitResponse load(KeyCache key) throws Exception {
						return (MeasureUnitResponse) get(BACK_OFFICE_MEASURE_UNITS, key, MeasureUnitResponse.class);	
					};
				}).build();
	}
	
	static {
		dettaglioApiCache = new Cache2kBuilder<KeyCache, BackofficeDettaglioApiResponse>(){}.resilienceDuration(24, TimeUnit.HOURS).expireAfterWrite(DURATION, TimeUnit.MINUTES).refreshAhead(true)
				.loader(new CacheLoader<KeyCache, BackofficeDettaglioApiResponse>() {
					public BackofficeDettaglioApiResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioApiResponse) get(BACK_OFFICE_API, key, BackofficeDettaglioApiResponse.class);	
					};
				}).build();
				
				
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static MeasureUnitResponse getMeasureUnit(KeyCache keyCache) throws Exception {
//		return measureUnitCache.get(keyCache);
		return (MeasureUnitResponse) get(BACK_OFFICE_MEASURE_UNITS, keyCache, MeasureUnitResponse.class);
	}
	
	public static List<TenantManagementResponse> getTenants(KeyCache keyCache) throws Exception {
//		return tenantListCache.get(keyCache);
		return (List<TenantManagementResponse>) getList(BACK_OFFICE_TENANTS, keyCache, TenantManagementResponse.class);
	}
	
	public static List<OrganizationResponse> getOrganizations(KeyCache keyCache) throws Exception {
//		return organizationListCache.get(keyCache);	
		return (List<OrganizationResponse>) getList(BACK_OFFICE_ORGANIZATIONS, keyCache, OrganizationResponse.class);
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public static BackofficeDettaglioApiResponse getDettaglioApi(KeyCache key) throws Exception {
//		return dettaglioApiCache.get(key);
		return (BackofficeDettaglioApiResponse) get(BACK_OFFICE_API, key, BackofficeDettaglioApiResponse.class);
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByDatasetCode(KeyCache keyCache) throws Exception {
//		return dettaglioStreamDatasetByDatasetCodeCache.get(keyCache);
		return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
	}

	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByDatasetCodeDatasetVersion(StreamDatasetByDatasetCodeDatasetVersionKeyCache keyCache) throws Exception {
//		return dettaglioStreamDatasetByDatasetCodeDatasetVersionCache.get(keyCache);
		return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByIdDatasetDatasetVersion(StreamDatasetByIdDatasetDatasetVersionKeyCache keyCache) throws Exception {
//		return dettaglioStreamDatasetByIdDatasetDatasetVersionCache.get(keyCache);	
		return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByIdDataset(KeyCache keyCache) throws Exception {
//		return dettaglioStreamDatasetByIdDatasetCache.get(keyCache);
		return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetBySO(StreamDatasetBySoCodeStreamCodeKeyCache keyCache) throws Exception {
//		return dettaglioStreamDatasetBySoCodeStreamCodeCache.get(keyCache);
		return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
	}

	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(KeyCache keyCache) throws Exception {
//		return listaStreamDatasetByOrganizationCodeCache.get(keyCache);
		return (List<BackofficeDettaglioStreamDatasetResponse>) getList(BACK_OFFICE_DATASETS_ORGANIZATION_CODE, keyCache, BackofficeDettaglioStreamDatasetResponse.class);
	}
	
	/**
	 * 
	 * @param url
	 * @param keyCache
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Object get(String url, KeyCache keyCache, @SuppressWarnings("rawtypes") Class clazz )throws Exception{
		return AdminApiClientDelegate.getFromAdminApi(
				   keyCache.getAdminBaseUrl() + url + keyCache.getKeyUrl(),
				   clazz,
				   keyCache.getLogger(), 
				   keyCache.getParams());			
	}
	
	@SuppressWarnings("unchecked")
	private static Object getList(String url, KeyCache keyCache, @SuppressWarnings("rawtypes") Class clazz )throws Exception{

		return AdminApiClientDelegate.getListFromAdminApi(
				keyCache.getAdminBaseUrl() + url + keyCache.getKeyUrl(),
				clazz,
				keyCache.getLogger(), 
				keyCache.getParams());

	}
	
}
