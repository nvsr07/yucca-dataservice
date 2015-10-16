package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

public class HdfsFSUtils {

	public static InputStream readFile(String user, String pwd, String remotePath, String knoxurl) {
		InputStream input = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			System.out.println("user in readFile = " + user);
			System.out.println("pwd in readFile = " + pwd);
			System.out.println("remotePath in readFile = " + remotePath);
			System.out.println("knoxurl in readFile = " + knoxurl);

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date dateS = new Date();
			System.out.println("start readFile => " + sdf.format(dateS));

			input = ugi.doAs(new ReadFileHdfsAction(user, pwd, remotePath, knoxurl));
			
			Date dateF = new Date();
			System.out.println("end readFile => " + sdf.format(dateF));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	public static String writeFile(String user, String pwd, String remotePath, String knoxurl, String knoxgroup, InputStream is) throws Exception {
		String uri = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			System.out.println("user in writeFile = " + user);
			System.out.println("pwd in writeFile = " + pwd);
			System.out.println("remotePath in writeFile = " + remotePath);
			System.out.println("knoxgroup in writeFile = " + knoxgroup);

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date dateS = new Date();
			System.out.println("start writeFile => " + sdf.format(dateS));

			uri = ugi.doAs(new WriteFileHdfsAction(user, pwd, remotePath, knoxurl, knoxgroup, is));
			
			Date dateF = new Date();
			System.out.println("end writeFile => " + sdf.format(dateF));
		} catch (Exception e) {
			System.out.println("writeFile, Exception!");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		System.out.println("uri in writeFile = " + uri);
		return uri;
	}
	
	public static Boolean deleteDir(String user, String pwd, String remotePath, String knoxurl) throws Exception {

		Boolean result = false;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			result = ugi.doAs(new DeleteFilesHdfsAction(user, pwd, remotePath, knoxurl));
		} catch (Exception e) {
			System.out.println("deleteDir, Exception!");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return result;
	}
	
	public static Integer sizeFile(String user, String pwd, String remotePath, String knoxurl) {
		InputStream input = null;
		Integer size = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			input = ugi.doAs(new ReadFileHdfsAction(user, pwd, remotePath, knoxurl));
			size = input.available();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	IOUtils.closeQuietly(input);
	    }
		
		return size;
	}

}
