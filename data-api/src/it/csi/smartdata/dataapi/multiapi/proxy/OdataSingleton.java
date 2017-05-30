package it.csi.smartdata.dataapi.multiapi.proxy;

import java.io.IOException;

import it.csi.smartdata.dataapi.multiapi.odata.SDPMultiApiServiceFactory;

import javax.servlet.ServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.solr.common.cloud.ClosableThread;

public class OdataSingleton {
	
	static Logger log = Logger.getLogger(OdataSingleton.class.getPackage().getName());

	
	private OdataSingleton singleton;

	public static OdataSingleton getInstance() {
		return new OdataSingleton();
	}

	private OdataSingleton()
	{
		CloseableHttpClient client=  HttpClientBuilder.create().build();
	}

	public void getOdataResponse(String dataset, String queryString,
			ServletResponse res) throws IOException {
		
		log.info("[OdataSingleton::getOdataResponse] dataset:"+dataset+",queryString:"+queryString);
		res.getWriter().append("PPPPPPPPPPPPPPP");
		
	}
	
	
}
