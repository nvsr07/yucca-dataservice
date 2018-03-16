package org.csi.yucca.adminapi.client.cache;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class CacheUtil {

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
	
	private static LoadingCache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByDatasetCodeCache;
	private static LoadingCache<StreamDatasetByDatasetCodeDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByDatasetCodeDatasetVersionCache;	
	private static LoadingCache<StreamDatasetByIdDatasetDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByIdDatasetDatasetVersionCache;
	private static LoadingCache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByIdDatasetCache;
	private static LoadingCache<StreamDatasetBySoCodeStreamCodeKeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetBySoCodeStreamCodeCache;
	private static LoadingCache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetCache;
	private static LoadingCache<KeyCache, BackofficeDettaglioApiResponse> dettaglioApiCache;
	private static LoadingCache<KeyCache, MeasureUnitResponse> measureUnitCache;
	private static LoadingCache<KeyCache, List<TenantManagementResponse>> tenantListCache;
	private static LoadingCache<KeyCache, List<BackofficeDettaglioStreamDatasetResponse>> listaStreamDatasetByOrganizationCodeCache;
	private static LoadingCache<KeyCache, List<AllineamentoScaricoDatasetResponse>> allineamentoResponseCache;
	private static LoadingCache<KeyCache, List<OrganizationResponse>> organizationListCache;
	
	static {
		dettaglioStreamDatasetCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					@Override
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioStreamDatasetResponse> reload(final KeyCache key, BackofficeDettaglioStreamDatasetResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioStreamDatasetResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioStreamDatasetResponse>() {
							public BackofficeDettaglioStreamDatasetResponse call() throws Exception{
								return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}

	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDataset(KeyCache keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	static {
		dettaglioStreamDatasetBySoCodeStreamCodeCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<StreamDatasetBySoCodeStreamCodeKeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					@Override
					public BackofficeDettaglioStreamDatasetResponse load(StreamDatasetBySoCodeStreamCodeKeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioStreamDatasetResponse> reload(final StreamDatasetBySoCodeStreamCodeKeyCache key, BackofficeDettaglioStreamDatasetResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioStreamDatasetResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioStreamDatasetResponse>() {
							public BackofficeDettaglioStreamDatasetResponse call() throws Exception{
								return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_STREAMS, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}

	static {
		allineamentoResponseCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, List<AllineamentoScaricoDatasetResponse>>() {
					@SuppressWarnings("unchecked")
					@Override
					public List<AllineamentoScaricoDatasetResponse> load(KeyCache key) throws Exception {
						return (List<AllineamentoScaricoDatasetResponse>) getList(BACK_OFFICE_ALLINEAMENTO_BY_ID_ORG, key, AllineamentoScaricoDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<List<AllineamentoScaricoDatasetResponse>> reload(final KeyCache key, List<AllineamentoScaricoDatasetResponse> oldValue) throws Exception {
						
						ListenableFutureTask <List<AllineamentoScaricoDatasetResponse>> task = ListenableFutureTask.create(new Callable<List<AllineamentoScaricoDatasetResponse>>() {
							@SuppressWarnings("unchecked")
							public List<AllineamentoScaricoDatasetResponse> call() throws Exception{
								return (List<AllineamentoScaricoDatasetResponse>) getList(BACK_OFFICE_ALLINEAMENTO_BY_ID_ORG, key, AllineamentoScaricoDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	
	static {
		organizationListCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, List<OrganizationResponse>>() {
					@SuppressWarnings("unchecked")
					@Override
					public List<OrganizationResponse> load(KeyCache key) throws Exception {
						return (List<OrganizationResponse>) getList(BACK_OFFICE_ORGANIZATIONS, key, OrganizationResponse.class);
					}
					
					@Override
					public ListenableFuture<List<OrganizationResponse>> reload(final KeyCache key, List<OrganizationResponse> oldValue) throws Exception {
						
						ListenableFutureTask <List<OrganizationResponse>> task = ListenableFutureTask.create(new Callable<List<OrganizationResponse>>() {
							@SuppressWarnings("unchecked")
							public List<OrganizationResponse> call() throws Exception{
								return (List<OrganizationResponse>) getList(BACK_OFFICE_ORGANIZATIONS, key, OrganizationResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	public static List<AllineamentoScaricoDatasetResponse> getAllineamento(KeyCache keyCache) throws AdminApiClientException {
		try {
			return allineamentoResponseCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	static {
		listaStreamDatasetByOrganizationCodeCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, List<BackofficeDettaglioStreamDatasetResponse>>() {
					@SuppressWarnings("unchecked")
					@Override
					public List<BackofficeDettaglioStreamDatasetResponse> load(KeyCache key) throws Exception {
						return (List<BackofficeDettaglioStreamDatasetResponse>) getList(BACK_OFFICE_DATASETS_ORGANIZATION_CODE, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<List<BackofficeDettaglioStreamDatasetResponse>> reload(final KeyCache key, List<BackofficeDettaglioStreamDatasetResponse> oldValue) throws Exception {
						
						ListenableFutureTask <List<BackofficeDettaglioStreamDatasetResponse>> task = ListenableFutureTask.create(new Callable<List<BackofficeDettaglioStreamDatasetResponse>>() {
							@SuppressWarnings("unchecked")
							public List<BackofficeDettaglioStreamDatasetResponse> call() throws Exception{
								return (List<BackofficeDettaglioStreamDatasetResponse>) getList(BACK_OFFICE_DATASETS_ORGANIZATION_CODE, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	static {
		tenantListCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, List<TenantManagementResponse>>() {
					@SuppressWarnings("unchecked")
					@Override
					public List<TenantManagementResponse> load(KeyCache key) throws Exception {
						return (List<TenantManagementResponse>) getList(BACK_OFFICE_TENANTS, key, TenantManagementResponse.class);
					}
					
					@Override
					public ListenableFuture<List<TenantManagementResponse>> reload(final KeyCache key, List<TenantManagementResponse> oldValue) throws Exception {
						
						ListenableFutureTask <List<TenantManagementResponse>> task = ListenableFutureTask.create(new Callable<List<TenantManagementResponse>>() {
							@SuppressWarnings("unchecked")
							public List<TenantManagementResponse> call() throws Exception{
								return (List<TenantManagementResponse>) getList(BACK_OFFICE_TENANTS, key, TenantManagementResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	static {
		dettaglioStreamDatasetByDatasetCodeCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					@Override
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioStreamDatasetResponse> reload(final KeyCache key, BackofficeDettaglioStreamDatasetResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioStreamDatasetResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioStreamDatasetResponse>() {
							public BackofficeDettaglioStreamDatasetResponse call() throws Exception{
								return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}

	static {
		dettaglioStreamDatasetByDatasetCodeDatasetVersionCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<StreamDatasetByDatasetCodeDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					@Override
					public BackofficeDettaglioStreamDatasetResponse load(StreamDatasetByDatasetCodeDatasetVersionKeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioStreamDatasetResponse> reload(final StreamDatasetByDatasetCodeDatasetVersionKeyCache key, BackofficeDettaglioStreamDatasetResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioStreamDatasetResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioStreamDatasetResponse>() {
							public BackofficeDettaglioStreamDatasetResponse call() throws Exception{
								return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS_DATASETCODE, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}

	static {
		dettaglioStreamDatasetByIdDatasetDatasetVersionCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<StreamDatasetByIdDatasetDatasetVersionKeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					@Override
					public BackofficeDettaglioStreamDatasetResponse load(StreamDatasetByIdDatasetDatasetVersionKeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioStreamDatasetResponse> reload(final StreamDatasetByIdDatasetDatasetVersionKeyCache key, BackofficeDettaglioStreamDatasetResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioStreamDatasetResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioStreamDatasetResponse>() {
							public BackofficeDettaglioStreamDatasetResponse call() throws Exception{
								return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	static {
		dettaglioStreamDatasetByIdDatasetCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {
					@Override
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, key, BackofficeDettaglioStreamDatasetResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioStreamDatasetResponse> reload(final KeyCache key, BackofficeDettaglioStreamDatasetResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioStreamDatasetResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioStreamDatasetResponse>() {
							public BackofficeDettaglioStreamDatasetResponse call() throws Exception{
								return (BackofficeDettaglioStreamDatasetResponse) get(BACK_OFFICE_DATASETS, key, BackofficeDettaglioStreamDatasetResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	static {
		measureUnitCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, MeasureUnitResponse>() {
					@Override
					public MeasureUnitResponse load(KeyCache keyCache) throws Exception {
						return (MeasureUnitResponse)get(BACK_OFFICE_MEASURE_UNITS, keyCache, MeasureUnitResponse.class);
					}
					@Override
					public ListenableFuture<MeasureUnitResponse> reload(final KeyCache keyCache, MeasureUnitResponse oldValue) throws Exception {
						ListenableFutureTask <MeasureUnitResponse> task = ListenableFutureTask.create(new Callable<MeasureUnitResponse>() {
							public MeasureUnitResponse call() throws Exception{
								return (MeasureUnitResponse)get(BACK_OFFICE_MEASURE_UNITS, keyCache, MeasureUnitResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	static {
		dettaglioApiCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).refreshAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, BackofficeDettaglioApiResponse>() {
					@Override
					public BackofficeDettaglioApiResponse load(KeyCache key) throws Exception {
						return (BackofficeDettaglioApiResponse) get(BACK_OFFICE_API, key, BackofficeDettaglioApiResponse.class);
					}
					
					@Override
					public ListenableFuture<BackofficeDettaglioApiResponse> reload(final KeyCache key, BackofficeDettaglioApiResponse oldValue) throws Exception {
						
						ListenableFutureTask <BackofficeDettaglioApiResponse> task = ListenableFutureTask.create(new Callable<BackofficeDettaglioApiResponse>() {
							public BackofficeDettaglioApiResponse call() throws Exception{
								return (BackofficeDettaglioApiResponse) get(BACK_OFFICE_API, key, BackofficeDettaglioApiResponse.class);
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static MeasureUnitResponse getMeasureUnit(KeyCache keyCache) throws AdminApiClientException {
		try {
			return measureUnitCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	public static List<TenantManagementResponse> getTenants(KeyCache keyCache) throws AdminApiClientException {
		try {
			return tenantListCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	public static List<OrganizationResponse> getOrganizations(KeyCache keyCache) throws AdminApiClientException {
		try {
			return organizationListCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioApiResponse getDettaglioApi(KeyCache key) throws AdminApiClientException {
		try {
			return dettaglioApiCache.get(key);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByDatasetCode(KeyCache keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetByDatasetCodeCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByDatasetCodeDatasetVersion(StreamDatasetByDatasetCodeDatasetVersionKeyCache keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetByDatasetCodeDatasetVersionCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByIdDatasetDatasetVersion(StreamDatasetByIdDatasetDatasetVersionKeyCache keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetByIdDatasetDatasetVersionCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByIdDataset(KeyCache keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetByIdDatasetCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDataset(StreamDatasetBySoCodeStreamCodeKeyCache keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetBySoCodeStreamCodeCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	/**
	 * 
	 * @param keyCache
	 * @return
	 * @throws AdminApiClientException
	 */
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(KeyCache keyCache) throws AdminApiClientException {
		try {
			return listaStreamDatasetByOrganizationCodeCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
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
