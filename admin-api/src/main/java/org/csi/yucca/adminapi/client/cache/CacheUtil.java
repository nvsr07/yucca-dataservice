package org.csi.yucca.adminapi.client.cache;

import java.util.concurrent.TimeUnit;

import org.csi.yucca.adminapi.client.AdminApiClientDelegate;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheUtil {

	private static final String BACK_OFFICE_STREAMS = "/1/backoffice/streams/";
	private static final String BACK_OFFICE_API = "/1/backoffice/api/";
	
	private static final long DURATION = 10; 
	private static final long MAXIMUM_SIZE = 100; 
	
	private static LoadingCache<KeyCache, BackofficeDettaglioApiResponse> dettaglioApiCache;
	private static LoadingCache<KeyCache, BackofficeDettaglioStreamDatasetResponse> dettaglioStreamDatasetCache;

	static {
		dettaglioStreamDatasetCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).expireAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, BackofficeDettaglioStreamDatasetResponse>() {

					@Override
					public BackofficeDettaglioStreamDatasetResponse load(KeyCache key) throws Exception {

						return AdminApiClientDelegate.getFromAdminApi(
								key.getAdminApiBaseUrl() + BACK_OFFICE_STREAMS + Integer.toString(key.getId()),
								BackofficeDettaglioStreamDatasetResponse.class,
								key.getLogger(), 
								null);
					}
				});
	}
	
	static {
		dettaglioApiCache = CacheBuilder.newBuilder().maximumSize(MAXIMUM_SIZE).expireAfterWrite(DURATION, TimeUnit.MINUTES)
				.build(new CacheLoader<KeyCache, BackofficeDettaglioApiResponse>() {

					@Override
					public BackofficeDettaglioApiResponse load(KeyCache key) throws Exception {

						return AdminApiClientDelegate.getFromAdminApi(
								key.getAdminApiBaseUrl() + BACK_OFFICE_API + key.getCode(),
								BackofficeDettaglioApiResponse.class, key.getLogger(), null);
					}
				});
	}

	public static LoadingCache<KeyCache, BackofficeDettaglioApiResponse> getDettaglioApiCache() {
		return dettaglioApiCache;
	}

	public static LoadingCache<KeyCache, BackofficeDettaglioStreamDatasetResponse> getDettaglioStreamDatasetCache() {
		return dettaglioStreamDatasetCache;
	}
	
}
