package it.csi.smartdata.dataapi.multiapi.proxy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
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
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		
	}

	public void getOdataResponse(String path, Map<String, String[]> params,
			ServletResponse res) throws IOException, URISyntaxException {
		
		log.info("[OdataSingleton::getOdataResponse] path:"+path+",queryString:"+params);
		URIBuilder builder  = new URIBuilder("http://api.smartdatanet.it");
		
		builder.setHost("api.smartdatanet.it");
		builder.setScheme("http");
		builder.setPath("/api/"+path+"/");
	
		List<NameValuePair> nvpList = new ArrayList<>(params.size());
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			if (entry.getValue()!=null && entry.getValue().length>0)
				nvpList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()[0]));
		}
		
		builder.setParameters(nvpList);
		
		CloseableHttpClient client=  HttpClientBuilder.create().setConnectionManager(cm).build();
		
		HttpGet get = new HttpGet(builder.build());
		
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
	
		IOUtils.copyLarge(resp.getEntity().getContent(), res.getOutputStream());
		
		IOUtils.closeQuietly(resp.getEntity().getContent());

		get.releaseConnection();
	}
	
	
}
