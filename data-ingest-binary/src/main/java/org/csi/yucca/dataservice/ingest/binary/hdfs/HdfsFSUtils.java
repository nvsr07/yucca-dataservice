package org.csi.yucca.dataservice.ingest.binary.hdfs;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

public class HdfsFSUtils {

	public static InputStream readFile(String user, String remotePath) {
		InputStream input = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);

			input = ugi.doAs(new ReadFileHdfsAction(user, remotePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	public static String writeFile(String user, String remotePath,
			InputStream is) throws Exception {
		String uri = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);

			uri = ugi.doAs(new WriteFileHdfsAction(user, remotePath, is));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return uri;
	}
	
	public static Boolean deleteDir(String user, String remotePath) throws Exception {

		Boolean result = false;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			result = ugi.doAs(new DeleteFilesHdfsAction(user, remotePath));
		} catch (Exception e) {
			System.out.println("deleteDir, Exception!");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return result;
	}
	
	public static Integer sizeFile(String user, String remotePath) {
		InputStream input = null;
		Integer size = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			input = ugi.doAs(new ReadFileHdfsAction(user, remotePath));
			size = input.available();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	IOUtils.closeQuietly(input);
	    }
		
		return size;
	}

}
