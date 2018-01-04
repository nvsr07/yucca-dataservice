package org.csi.yucca.client;

import java.io.IOException;
import java.util.Iterator;

import org.csi.yucca.adminapi.client.AdminApiClientDelegate;
import org.csi.yucca.adminapi.client.AdminApiClientException;
import org.csi.yucca.adminapi.client.BackofficeDettaglioClient;
import org.csi.yucca.adminapi.response.BackofficeDettaglioApiResponse;
import org.csi.yucca.controller.TestBase;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AdminApiClientTest extends TestBase {

	@BeforeClass
	public void setUpSecretObject() throws IOException {
		super.setUpSecretObject("/testSecret.json");
	}
	
	@DataProvider(name="jsonGet")
	public Iterator<Object[]> getFromJsonGet(){  
		return super.getFromJson(
			     "/BackOfficeController_get_api_dataIn.json"
			     );
	}	

	
	@Test(dataProvider="jsonGet")
	public void BackofficeDettaglioApiTest(JSONObject dato) throws JSONException, InterruptedException {
		try {
			BackofficeDettaglioApiResponse resp = BackofficeDettaglioClient.getBackofficeDettaglioApi(
					this.secretObject.getString("adminapi.url") , dato.getString("adminapi.additionalpath"), "BackofficeDettaglioApiTest");
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
