package it.csi.smartdata.odata.dbconfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoSingleton {

	private static MongoClient mongo= null;
	private static Map<String,String> mongoParams = null;
	private static MongoCredential credential=null;

	private MongoSingleton() throws NumberFormatException, UnknownHostException{
		mongoParams = ConfigParamsSingleton.getInstance().getParams();

		credential = MongoCredential.createMongoCRCredential(mongoParams.get("MONGO_USERNAME"), mongoParams.get("MONGO_DB_AUTH"), mongoParams.get("MONGO_PASSWORD").toCharArray());

		List<ServerAddress> servers =new ArrayList<ServerAddress>();

//		serverAdd = new ServerAddress(mongoParams.get("MONGO_HOST"), Integer.parseInt(mongoParams.get("MONGO_PORT")));
		String[] hosts = mongoParams.get("MONGO_HOST").split(";");
		String[] ports = mongoParams.get("MONGO_PORT").split(";");
		ServerAddress serverAdd=null;
		for(int i=0;i<hosts.length;i++){
			String addr = hosts[i];
			Integer port = null;
			try{
				port= Integer.parseInt(ports[i]);
			}catch(Exception e){
				port=27017;
			}
			serverAdd = new ServerAddress(addr,port);
			if(addr!=null && !"".equals(addr))
				servers.add(serverAdd);
		}

		if("true".equals(mongoParams.get("MONGO_DB_AUTH_FLAG")))
			mongo = new MongoClient(servers,Arrays.asList(credential));
		else
			mongo = new MongoClient(servers);


	}
	public static MongoClient getMongoClient() throws NumberFormatException, UnknownHostException{
		if(mongo ==null)
			new MongoSingleton();
		return mongo;
	}
}
