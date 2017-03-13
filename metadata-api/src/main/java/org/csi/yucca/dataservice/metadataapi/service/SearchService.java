package org.csi.yucca.dataservice.metadataapi.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.model.output.v01.Metadata;
import org.csi.yucca.dataservice.metadataapi.model.output.v02.Result;
import org.csi.yucca.dataservice.metadataapi.service.response.ErrorResponse;
import org.csi.yucca.dataservice.metadataapi.service.response.ListResponse;
import org.csi.yucca.dataservice.metadataapi.util.FacetParams;

@Path("/search")
public class SearchService extends AbstractService {

	@Context
	ServletContext context;
	static Logger log = Logger.getLogger(SearchService.class);

	@GET
	@Path("/full")
	// @Consumes({ "*/*", "application/vnd.sdp-metadata.v1+json" })
	@Produces({ "application/json; charset=UTF-8", "application/vnd.sdp-metadata.v1+json" })
	public String searchFull(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("end") Integer end,
			@QueryParam("tenant") String tenant, @QueryParam("domain") String domain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		List<Metadata> metadataList = org.csi.yucca.dataservice.metadataapi.delegate.v01.metadata.MetadataDelegate.getInstance().search(userAuth, q, start, end, tenant, domain,
				opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang, null);
		ListResponse response = new ListResponse();
		response.setCount(metadataList.size());
		response.setResult(metadataList);
		return response.toJson();
	}

	@GET
	@Path("/v02/")
	// @Consumes({ "application/vnd.sdp-metadata.v2+json" })
	@Produces({ "application/json; charset=UTF-8", "application/vnd.sdp-metadata.v2+json" })
	public String searchFullV2(@Context HttpServletRequest request, @QueryParam("q") String q, @QueryParam("start") Integer start, @QueryParam("rows") Integer rows,
			@QueryParam("sort") String sort, @QueryParam("tenant") String tenant, @QueryParam("organization") String organization, @QueryParam("domain") String domain,
			@QueryParam("subdomain") String subdomain, @QueryParam("opendata") Boolean opendata, @QueryParam("geolocalized") Boolean geolocalized,
			@QueryParam("minLat") Double minLat, @QueryParam("minLon") Double minLon, @QueryParam("maxLat") Double maxLat, @QueryParam("maxLon") Double maxLon,
			@QueryParam("lang") String lang, @QueryParam("facet.field") String facetFields, @QueryParam("facet.prefix") String facetPrefix,
			@QueryParam("facet.sort") String facetSort, @QueryParam("facet.contains") String facetContains,
			@QueryParam("facet.contains.ignoreCase") String facetContainsIgnoreCase, @QueryParam("facet.limit") String facetLimit, @QueryParam("facet.offset") String facetOffset,
			@QueryParam("facet.mincount") String facetMinCount, @QueryParam("facet.missing") String facetMissing) throws NumberFormatException, UnknownHostException {

		String userAuth = (String) request.getSession().getAttribute("userAuth");

		FacetParams facetParams = null;
		if (facetFields != null)
			facetParams = new FacetParams(facetFields, facetPrefix, facetSort, facetContains, facetContainsIgnoreCase, facetLimit, facetOffset, facetMinCount, facetMissing);

		String result;
		try {
			Result searchResult = org.csi.yucca.dataservice.metadataapi.delegate.v02.metadata.MetadataDelegate.getInstance().search(userAuth, q, start, rows, sort, tenant,
					organization, domain, subdomain, opendata, geolocalized, minLat, minLon, maxLat, maxLon, lang, null, facetParams);
			result = searchResult.toJson();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new ErrorResponse("", "Invalid param").toJson();
		}

		return result;
	}

}
