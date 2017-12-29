package org.csi.yucca.client;

import java.util.Iterator;

import org.csi.yucca.adminapi.client.AdminApiClientDelegate;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.BackofficeDettaglioApiClient;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.controller.TestBase;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AdminApiClientTest extends TestBase {

	
	@DataProvider(name="jsonGet")
	public Iterator<Object[]> getFromJsonGet(){  
		return super.getFromJson(
			     "/BackOfficeController_get_api_dataIn.json"
			     );
	}	

	
	@Test(dataProvider="jsonGet")
	public void BackofficeDettaglioApiTest(JSONObject dato) throws JSONException, InterruptedException {
		try {
			BackofficeDettaglioApiResponse resp = BackofficeDettaglioApiClient.getBackofficeDettaglioApi(dato.getString("adminapi.additionalpath"));
			if (dato.getInt("expected.httpStatus.response")!=200)
				Assert.assertTrue(false);
			else
				Assert.assertNotNull(resp);
		}catch(AdminApiClientException e)
		{
			Assert.assertTrue(dato.getInt("expected.httpStatus.response")!=200);
		}
		
	}

	
}
