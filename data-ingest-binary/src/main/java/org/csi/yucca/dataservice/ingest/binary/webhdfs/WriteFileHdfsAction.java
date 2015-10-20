package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;

public class WriteFileHdfsAction implements PrivilegedExceptionAction<String> {

	private String pathFile;
	private String user;
	private String pwd;
	private String knoxurl;
	private String knoxgroup;
	private InputStream is;

	public WriteFileHdfsAction(String user, String pwd, String pathFile, String knoxurl, String knoxgroup, InputStream is) {
		this.pathFile = pathFile;
		this.user = user;
		this.pwd = pwd;
		this.knoxurl = knoxurl;
		this.knoxgroup = knoxgroup;
		this.is = is;
	}

	@Override
	public String run() throws Exception {
		
		System.setProperty("javax.net.ssl.trustStore", "repository/resources/security/client-truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType","JKS");
		System.setProperty("javax.net.debug","ssl");
		
		System.out.println("Entro in run");
		System.out.println(new File(".").getAbsolutePath());
		
		Configuration conf = new Configuration();
		FSDataOutputStream os = null;
		try {
			System.out.println("try");
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
			conf.set("fs.swebhdfs.impl", KnoxWebHdfsFileSystem.class.getName());
			FileSystem fs = null;
			if (this.knoxurl!=null) {
				System.out.println("Setto user");
				conf.set("knox.username", user);
				System.out.println("Setto pwd");
				conf.set("knox.password", pwd);
				//conf.set("knox.groupname", knoxgroup);
				System.out.println("Setto knoxurl");
				java.net.URI uri = new java.net.URI(knoxurl);
				System.out.println("Setto fs");
				System.out.println("uri = " + uri.toString());
				System.out.println("conf = " + conf.toString());
				fs = FileSystem.get(uri, conf);
				System.out.println("FileSystem Object " + fs.toString());
			} else {
				fs = FileSystem.get(conf);
				System.out.println("FileSystem Object " + fs.toString());
			}
			System.out.println("Conf Object = " + conf);
			
			if (fs.exists(pt)) {
				System.out.println("File already exists!");
				throw new Exception("File already exists!");
			} else {
				os = fs.create(pt);
				IOUtils.copyBytes(is, os, conf);
				System.out.println("Setto Permission");
				fs.setPermission(pt, new FsPermission(FsAction.READ_WRITE,FsAction.READ_WRITE,FsAction.NONE)); //660
				System.out.println("Setto Owner");
				fs.setOwner(pt, user, knoxgroup);
				return pathFile;
			}
		} catch (Exception e) {
			System.out.println("Exception e = " + e.getMessage());
			e.printStackTrace();
			//throw e;
			throw new Exception(e.getMessage());
		} finally {
			IOUtils.closeStream(is);
			IOUtils.closeStream(os);
		}
	}

}
