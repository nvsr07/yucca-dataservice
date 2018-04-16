package org.csi.yucca.adminapi.client.db;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.response.AllineamentoScaricoDatasetResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;
import org.csi.yucca.adminapi.response.OrganizationResponse;
import org.csi.yucca.adminapi.response.TenantManagementResponse;



public class CacheUtilDB {

	private static final long DURATION = 10; 
	private static final long TIME_SPAN = 24; 
	private static final boolean REFRESH_AHEAD = true;
	private static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.SECONDS;
	private static final TimeUnit RESILIENCE_TIME_UNIT = TimeUnit.HOURS;
	
	// ######################################################################################################
	
	private static Cache<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetCache;
	static {
		
		dettaglioStreamDatasetCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCacheDB key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) AdminDBClientDelegate.getInstance().getStreamService()
								.selectStreamByIdStream(key.getIdStream(), key.getOnlyInstalled()).getObject();
					};
				}).build();
	}
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDataset(KeyCacheDB keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	// ######################################################################################################

	private static Cache<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetBySoCodeStreamCodeCache;
	
	static {
		dettaglioStreamDatasetBySoCodeStreamCodeCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCacheDB key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse) AdminDBClientDelegate.getInstance().getStreamService()
						.selectStreamBySoCodeStreamCode(key.getSmartObjectCode(), key.getStreamCode(), key.getOnlyInstalled()).getObject();						
					};
				}).build();
	}
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetBySO(KeyCacheDB keyCache) throws AdminApiClientException {
		try {
			return dettaglioStreamDatasetBySoCodeStreamCodeCache.get(keyCache);	
		} 
		catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}
	
	// ######################################################################################################

	private static Cache<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByDatasetCodeCache;
	
	static {
		dettaglioStreamDatasetByDatasetCodeCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCacheDB key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse)
								AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByDatasetCode(
										key.getDatasetCode(), key.getOnlyInstalled()).getObject();
					};
				}).build();
				
	}

	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByDatasetCode(KeyCacheDB keyCache) throws Exception {
		return dettaglioStreamDatasetByDatasetCodeCache.get(keyCache);	
	}

	// ######################################################################################################

	private static Cache<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByIdDatasetCache;
	static {
		dettaglioStreamDatasetByIdDatasetCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCacheDB key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse)
								AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByIdDataset(key.getIdDataset(), 
										key.getOnlyInstalled()).getObject();
					};
				}).build();
	}
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByIdDataset(KeyCacheDB keyCache) throws Exception {
		return dettaglioStreamDatasetByIdDatasetCache.get(keyCache);	
	}
	
	// ######################################################################################################
	
	private static Cache<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByDatasetCodeDatasetVersionCache;
	static {
		dettaglioStreamDatasetByDatasetCodeDatasetVersionCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCacheDB key) throws Exception {
						
						return (BackofficeDettaglioStreamDatasetResponse)
								AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByDatasetCodeDatasetVersion(
										key.getDatasetCode(), key.getDatasetVersion()).getObject();
					};
				}).build();
	}
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByDatasetCodeDatasetVersion(KeyCacheDB keyCache) throws Exception {
		return dettaglioStreamDatasetByDatasetCodeDatasetVersionCache.get(keyCache);	
	}

	// ######################################################################################################
	
	private static Cache<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetByIdDatasetDatasetVersionCache;
	static {
		dettaglioStreamDatasetByIdDatasetDatasetVersionCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioStreamDatasetResponse>() {
					public BackofficeDettaglioStreamDatasetResponse load(KeyCacheDB key) throws Exception {
						return (BackofficeDettaglioStreamDatasetResponse)
								AdminDBClientDelegate.getInstance().getDatasetService()
								.selectDatasetByIdDatasetDatasetVersion(key.getIdDataset(), key.getDatasetVersion()).getObject();
					};
				}).build();
	}
	public static BackofficeDettaglioStreamDatasetResponse getDettaglioStreamDatasetByIdDatasetDatasetVersion(KeyCacheDB keyCache) throws Exception {
		return dettaglioStreamDatasetByIdDatasetDatasetVersionCache.get(keyCache);	
	}
	
	// ######################################################################################################

	private static Cache<KeyCacheDB, MeasureUnitResponse> measureUnitCache;

	static {
		measureUnitCache = new Cache2kBuilder<KeyCacheDB, MeasureUnitResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, MeasureUnitResponse>() {
					public MeasureUnitResponse load(KeyCacheDB key) throws Exception {
						return (MeasureUnitResponse)
								AdminDBClientDelegate.getInstance().getComponentService().selectMeasureUnit(
										key.getIdMeasureUnit()).getObject();
					};
				}).build();
	}

	public static MeasureUnitResponse getMeasureUnit(KeyCacheDB keyCache) throws Exception {
		return measureUnitCache.get(keyCache);	
	}
	
	
	// ######################################################################################################
	
	private static Cache<KeyCacheDB, BackofficeDettaglioApiResponse> dettaglioApiCache;
	static {
		
		dettaglioApiCache = new Cache2kBuilder<KeyCacheDB, BackofficeDettaglioApiResponse>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, BackofficeDettaglioApiResponse>() {
					public BackofficeDettaglioApiResponse load(KeyCacheDB key) throws Exception {
						return (BackofficeDettaglioApiResponse)AdminDBClientDelegate.getInstance().getApiService().selectBackofficeLastInstalledDettaglioApi(key.getApiCode()).getObject();
					};
				}).build();
	}
	public static BackofficeDettaglioApiResponse getDettaglioApi(KeyCacheDB key) throws AdminApiClientException, ExecutionException {
		return dettaglioApiCache.get(key);	
	}

	// ######################################################################################################
	
	private static Cache<KeyCacheDB, List<TenantManagementResponse>> tenantListCache;
	
	static {
		tenantListCache = new Cache2kBuilder<KeyCacheDB, List<TenantManagementResponse>>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, List<TenantManagementResponse>>() {
					@SuppressWarnings("unchecked")
					public List<TenantManagementResponse> load(KeyCacheDB key) throws Exception {
						return (List<TenantManagementResponse>)AdminDBClientDelegate.getInstance().getTenantService().selectTenants(null).getObject();
					};
				}).build();
	}
	public static List<TenantManagementResponse> getTenants(KeyCacheDB keyCache) throws Exception {
		return tenantListCache.get(keyCache);	
	}
	
	// ######################################################################################################
	
	private static Cache<KeyCacheDB, List<BackofficeDettaglioStreamDatasetResponse>> listaStreamDatasetByOrganizationCodeCache;
	static {
		listaStreamDatasetByOrganizationCodeCache = new Cache2kBuilder<KeyCacheDB, List<BackofficeDettaglioStreamDatasetResponse>>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, List<BackofficeDettaglioStreamDatasetResponse>>() {
					@SuppressWarnings("unchecked")
					public List<BackofficeDettaglioStreamDatasetResponse> load(KeyCacheDB key) throws Exception {
						return (List<BackofficeDettaglioStreamDatasetResponse>)
								AdminDBClientDelegate.getInstance().getDatasetService().selectDatasetByOrganizationCode(
										key.getOrganizationCode()).getObject();
					};
				}).build();
	}
	public static List<BackofficeDettaglioStreamDatasetResponse> getListStreamDataset(KeyCacheDB keyCache) throws Exception {
		return listaStreamDatasetByOrganizationCodeCache.get(keyCache);	
	}
	
	// ######################################################################################################

	private static Cache<KeyCacheDB, List<AllineamentoScaricoDatasetResponse>> allineamentoResponseCache;
	static {
		allineamentoResponseCache = new Cache2kBuilder<KeyCacheDB, List<AllineamentoScaricoDatasetResponse>>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, List<AllineamentoScaricoDatasetResponse>>() {
					@SuppressWarnings("unchecked")
					public List<AllineamentoScaricoDatasetResponse> load(KeyCacheDB key) throws Exception {
						return (List<AllineamentoScaricoDatasetResponse>)
								AdminDBClientDelegate.getInstance().getDatasetService().selectAllineamentoScaricoDataset(
										key.getIdOrganization()).getObject();
					};
				}).build();
				
	}
	public static List<AllineamentoScaricoDatasetResponse> getAllineamento(KeyCacheDB keyCache) throws Exception {
		return allineamentoResponseCache.get(keyCache);	
	}

	// ######################################################################################################
	
	private static Cache<KeyCacheDB, List<OrganizationResponse>> organizationListCache;
	static {
		organizationListCache = new Cache2kBuilder<KeyCacheDB, List<OrganizationResponse>>(){}.resilienceDuration(TIME_SPAN, RESILIENCE_TIME_UNIT).expireAfterWrite(DURATION, EXPIRE_TIME_UNIT).refreshAhead(REFRESH_AHEAD)
				.loader(new CacheLoader<KeyCacheDB, List<OrganizationResponse>>() {
					@SuppressWarnings("unchecked")
					public List<OrganizationResponse> load(KeyCacheDB key) throws Exception {
						return (List<OrganizationResponse>)
								AdminDBClientDelegate.getInstance().getClassificationService().selectOrganization().getObject();
					};
				}).build();
	}
	public static List<OrganizationResponse> getOrganizations(KeyCacheDB keyCache) throws Exception {
		return organizationListCache.get(keyCache);	
	}
	
	// ######################################################################################################
	
}
