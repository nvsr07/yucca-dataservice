package org.csi.yucca.dataservice.metadataapi.service;

import java.net.UnknownHostException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.delegate.I18nDelegate;
import org.csi.yucca.dataservice.metadataapi.service.response.SimpleResponse;

@Path("/cache")
public class CacheService extends AbstractService {

	static Logger log = Logger.getLogger(CacheService.class);

	@GET
	@Path("/clear/translations")
	@Produces("application/json; charset=UTF-8")
	public String clearTranslations() throws NumberFormatException, UnknownHostException {

		I18nDelegate.clearCache();
		SimpleResponse response = new SimpleResponse();
		response.setMessage("Translations clear");
		return response.toJson();
	}

	@GET
	@Path("/view/translations")
	@Produces("application/json; charset=UTF-8")
	public String viewTranslations() throws NumberFormatException, UnknownHostException {

		Map<String, Map<String, String>> translationsMap = I18nDelegate.viewTranslationsMap();
		SimpleResponse response = new SimpleResponse();
		response.setMessage("Translations map");
		response.setObject(translationsMap);
		return response.toJson();
	}

}
