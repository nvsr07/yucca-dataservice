package it.csi.smartdata.dataapi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public class OdataTestBase {

	
	protected JSONObject secretObject = new JSONObject();

	
	
	protected String makeUrl(JSONObject dato, String format) {
//		String aaurlToCall=dato.get("odata.url")+"/"+dato.get("odata.apicode")+"/"+
//				dato.get("odata.entityset")+"?"+
//				"$top="+dato.get("odata.top")+"&"+
//				"$skip="+dato.get("odata.skip")+"&"+
//				"$orderby="+dato.get("odata.order")+
//				"&$format=json";
		
		String urlToCall=dato.get("odata.url")+"/"+dato.get("odata.apicode")+"/";
		if (StringUtils.isNotEmpty(dato.getString("odata.entityset"))) {
			urlToCall+=dato.get("odata.entityset");
		}
		boolean added=false;
		if (dato.getInt("odata.top")>0) {
			added=true;
			urlToCall+="?" + "$top="+dato.get("odata.top");
		}
		if (dato.getInt("odata.skip")>0) {
			urlToCall+=(added ? "&" : "?") + "$skip="+dato.get("odata.skip");
			added=true;
		}
		if (StringUtils.isNotEmpty(dato.getString("odata.orderby"))) {
			urlToCall+=(added ? "&" : "?") + "$orderby="+dato.get("odata.orderby");
			added=true;
		}
		if (StringUtils.isNotEmpty(dato.getString("odata.filter"))) {
			urlToCall+=(added ? "&" : "?") + "$filter="+dato.get("odata.filter");
			added=true;
		}
		
		if (null!=format) {
			urlToCall+=(added ? "&" : "?") + "$format="+format;
		}
		
		
		
		return urlToCall;
	}
	
	
	public void setUpSecretObject(String file) throws IOException {
		String str = readFile(file);
		secretObject=  new JSONObject(str);
	}	
	
	
	public Iterator<Object[]> getFromJson(String file)
	{

		
		ArrayList<Object[]> data = new ArrayList<>();

		String str = readFile(file);
		JSONObject json =  new JSONObject(str);
		JSONArray jsArray = json.getJSONArray("data");

		for (int i = 0; i < jsArray.length(); i++) {
			JSONObject arr = jsArray.getJSONObject(i);

			// merge with secret

			Iterator iterSecret = secretObject.keys();
			String tmp_key;
			while(iterSecret.hasNext()) {
				tmp_key = (String) iterSecret.next();
				if (!arr.has(tmp_key))
				{
					arr.put(tmp_key, secretObject.get(tmp_key));
				}
			}


			data.add(new Object[]{arr});
		}



		return data.iterator();
	}
	
	
	protected String readFile(String file)
	{
		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			InputStream inputStream = this.getClass().getResourceAsStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			br = new BufferedReader(inputStreamReader);
			while ((line = br.readLine()) != null) {
				jsonData += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return jsonData;
	}	
	
	
}
