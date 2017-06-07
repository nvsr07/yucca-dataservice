package it.csi.smartdata.dataapi.multiapi.proxy;

import it.csi.smartdata.dataapi.multiapi.constants.SDPDataMultiApiConfig;
import it.csi.smartdata.dataapi.multiapi.util.ReplacingInputStream;

import java.io.FilterReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class OdataSingleton {
	
	static Logger log = Logger.getLogger(OdataSingleton.class.getPackage().getName());

	
	public final static  OdataSingleton INSTANCE  = new OdataSingleton();

	private PoolingHttpClientConnectionManager cm;
	
	private OdataSingleton()
	{
		
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(2000);
		cm.setDefaultMaxPerRoute(2000);
		
	}

	public void getOdataResponse(String path, Map<String, String[]> params,
			ServletResponse res) throws IOException, URISyntaxException {
		
		log.info("[OdataSingleton::getOdataResponse] path:"+path+",queryString:"+params);
		URIBuilder builder  = new URIBuilder(SDPDataMultiApiConfig.instance.getMultiapiExternalOdataBaseUrl());
		
		builder.setPath("/api/"+path+"/");
	
		List<NameValuePair> nvpList = new ArrayList<>(params.size());
		String topParaFound=null;
		String skipParaFound=null;
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			if (entry.getValue()!=null && entry.getValue().length>0)
			{
				nvpList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()[0]));
				if (entry.getKey().equals("$top"))
					topParaFound = entry.getValue()[0];
				if (entry.getKey().equals("$skip"))
					skipParaFound = entry.getValue()[0];
			}
		}
		int top;
		int skip;
		if (topParaFound==null){
			nvpList.add(new BasicNameValuePair("$top", "5000"));
			top = 10000;
		}
		else 
			top = Integer.parseInt(topParaFound);
		if (skipParaFound==null)
			skip = 0;
		else
			skip = Integer.parseInt(skipParaFound);
		
		builder.setParameters(nvpList);
	
			
		
		CloseableHttpClient client=  HttpClientBuilder.create().setConnectionManager(cm).build();
		HttpGet get = new HttpGet(builder.build());
		RequestConfig requestConfig = RequestConfig.custom()
				  .setSocketTimeout(1000*60*5)
				  .setConnectTimeout(1000*60*5)
				  .setConnectionRequestTimeout(1000*60*5)
				  .build();

		get.setConfig(requestConfig);
		
		String token = SDPDataMultiApiConfig.instance.getMultiapiToken();
		if (StringUtils.isNotBlank(token)){
			log.info("[OdataSingleton::getOdataResponse] Token defined!");
			get.addHeader("Authorization", "Bearer "+token);
		}
		else {
			log.info("[OdataSingleton::getOdataResponse] Token undefined!");
		}
		
		CloseableHttpResponse resp = client.execute(get);
		
		if (resp.getEntity()!=null && resp.getEntity().getContentType()!=null)
		{
			log.info("[OdataSingleton::getOdataResponse] Content type:" +resp.getEntity().getContentType().getValue());
			res.setContentType(resp.getEntity().getContentType().getValue());
		}
		if (resp.getEntity()!=null && resp.getEntity().getContentEncoding()!=null)
		{
			log.info("[OdataSingleton::getOdataResponse] Content Encoding:" +resp.getEntity().getContentEncoding().getValue());
			res.setCharacterEncoding(resp.getEntity().getContentEncoding().getValue());
		}
	
		
		ReplacingInputStream ris = new ReplacingInputStream(
				new ReplacingInputStream(resp.getEntity().getContent(), 
						SDPDataMultiApiConfig.instance.getMultiapiExternalOdataBaseUrl().getBytes("UTF-8"), 
						(SDPDataMultiApiConfig.instance.getMultiapiOdataBaseUrl()+SDPDataMultiApiConfig.instance.getMultiapiName()+"/").getBytes("UTF-8")),
						"/DataEntities".getBytes("UTF-8"),
						"__DataEntities".getBytes("UTF-8"));
				
		IOUtils.copyLarge(ris, res.getOutputStream());
		
		IOUtils.closeQuietly(resp.getEntity().getContent());
		

		get.releaseConnection();
	}
	
	
}
