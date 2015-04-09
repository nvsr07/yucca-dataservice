package org.csi.yucca.dataservice.ingest.binary.localfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.security.UserGroupInformation;

public class LocalFSUtils {

	public static InputStream readFile(String user, String remotePath)
	{
		InputStream input = null;
		FileInputStream file;
		try {
			file = new FileInputStream(remotePath);
		} catch (FileNotFoundException e) {
			file = null;
		}
		return file;
	}
	
	
//	public static URI writeFile(String user, String remotePath, InputStream is)
//	{
//		URI uri = null;
//		 try {
//				UserGroupInformation ugi
//	                = UserGroupInformation.createRemoteUser(user);
//
//				uri = ugi.doAs(new WriteFileHdfsAction(user, remotePath, is));
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//		return uri;
//	}
	
}
