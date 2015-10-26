package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

public class HdfsFSUtils {

	public static InputStream readFile(String user, String pwd, String remotePath, String knoxurl, String fileName) {
		InputStream input = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			System.out.println("user in readFile = " + user);
			System.out.println("pwd in readFile = " + pwd);
			System.out.println("remotePath in readFile = " + remotePath);
			System.out.println("knoxurl in readFile = " + knoxurl);

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date dateS = new Date();
			System.out.println("start readFile ("+fileName+") => " + sdf.format(dateS));

			input = ugi.doAs(new ReadFileHdfsAction(user, pwd, remotePath, knoxurl, fileName));
			System.out.println("input = " + input.toString());
			
			Date dateF = new Date();
			System.out.println("end readFile ("+fileName+") => " + sdf.format(dateF));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	public static String writeFile(String user, String pwd, String remotePath, String knoxurl, String knoxgroup, InputStream is, String fileName) throws Exception {
		String uri = null;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			System.out.println("user in writeFile = " + user);
			System.out.println("pwd in writeFile = " + pwd);
			System.out.println("remotePath in writeFile = " + remotePath);
			System.out.println("knoxgroup in writeFile = " + knoxgroup);

			uri = ugi.doAs(new WriteFileHdfsAction(user, pwd, remotePath, knoxurl, knoxgroup, is, fileName));
			
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
	
	public static Long sizeFile(String user, String pwd, String remotePath, String knoxurl, String fileName) {
		InputStream input = null;
		Long size = null;
		
		try {
			//UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);
			
			System.out.println("user in readFile = " + user);
			System.out.println("pwd in readFile = " + pwd);
			System.out.println("remotePath in readFile = " + remotePath);
			System.out.println("knoxurl in readFile = " + knoxurl);

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date dateS = new Date();
			System.out.println("start readFile ("+fileName+") => " + sdf.format(dateS));
			
			ReadFileHdfsAction rfha = new ReadFileHdfsAction(user, pwd, remotePath, knoxurl, fileName);

			//input = ugi.doAs(rfha);
			size = rfha.getSizeFile();
			
			//Long l = input.getSizeFile();
			//System.out.println("input = " + input.toString());
			
			Date dateF = new Date();
			System.out.println("end readFile ("+fileName+") => " + sdf.format(dateF));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	IOUtils.closeQuietly(input);
	    }
		
		return size;
	}

}
