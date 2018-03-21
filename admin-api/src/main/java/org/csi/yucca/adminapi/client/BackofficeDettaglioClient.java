package org.csi.yucca.adminapi.client;

import org.csi.yucca.adminapi.client.cache.CacheUtil;
import org.csi.yucca.adminapi.client.cache.key.KeyCache;
import org.csi.yucca.adminapi.client.cache.key.StreamDatasetByDatasetCodeDatasetVersionKeyCache;
import org.csi.yucca.adminapi.client.cache.key.StreamDatasetByIdDatasetDatasetVersionKeyCache;
import org.csi.yucca.adminapi.client.cache.key.StreamDatasetBySoCodeStreamCodeKeyCache;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.adminapi.response.BackofficeDettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.response.MeasureUnitResponse;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

public class BackofficeDettaglioClient {

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdStream(
			String adminApiBaseUrl, Integer idStream, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioStreamDataset(
					new KeyCache(adminApiBaseUrl, logger).id(idStream).addParams("onlyInstalled", onlyInstalled));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetBySoCodeStreamCode(
			String adminApiBaseUrl, String soCode, String streamCode, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioStreamDataset(new StreamDatasetBySoCodeStreamCodeKeyCache(adminApiBaseUrl, logger)
					.soCode(soCode).streamCode(streamCode).addParams("onlyInstalled", onlyInstalled));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCode(
			String adminApiBaseUrl, String datasetCode, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioStreamDatasetByDatasetCode(
					new KeyCache(adminApiBaseUrl, logger).code(datasetCode).addParams("onlyInstalled", onlyInstalled));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDataset(
			String adminApiBaseUrl, Integer IdDataset, Boolean onlyInstalled, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioStreamDatasetByIdDataset(
					new KeyCache(adminApiBaseUrl, logger).id(IdDataset).addParams("onlyInstalled", onlyInstalled));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByDatasetCodeDatasetVersion(
			String adminApiBaseUrl, String datasetCode, Integer datasetVersion, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioStreamDatasetByDatasetCodeDatasetVersion(
					new StreamDatasetByDatasetCodeDatasetVersionKeyCache(adminApiBaseUrl, logger).datasetCode(datasetCode)
							.datasetVersion(datasetVersion));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioStreamDatasetResponse getBackofficeDettaglioStreamDatasetByIdDatasetDatasetVersion(
			String adminApiBaseUrl, Integer idDataset, Integer datasetVersion, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioStreamDatasetByIdDatasetDatasetVersion(
					new StreamDatasetByIdDatasetDatasetVersionKeyCache(adminApiBaseUrl, logger).idDataset(idDataset)
							.datasetVersion(datasetVersion));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static MeasureUnitResponse getMeasureUnit(String adminApiBaseUrl, Integer idMeasureUnit, String logger)
			throws AdminApiClientException {
		try {
			return CacheUtil.getMeasureUnit(new KeyCache(adminApiBaseUrl, logger).id(idMeasureUnit));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

	public static BackofficeDettaglioApiResponse getBackofficeDettaglioApi(String adminApiBaseUrl, String codApi,
			String logger) throws AdminApiClientException {
		try {
			return CacheUtil.getDettaglioApi(new KeyCache(adminApiBaseUrl, logger).code(codApi));
		} catch (InvalidCacheLoadException exception) {
			return null;
		} catch (Exception e) {
			throw new AdminApiClientException(e);
		}
	}

}
