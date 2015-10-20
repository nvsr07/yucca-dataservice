package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.File;
import java.io.InputStream;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class ReadFileHdfsAction implements PrivilegedExceptionAction<InputStream> {

	private String pathFile;
	private String user;
	private String pwd;
	private String knoxurl;

	public ReadFileHdfsAction(String user, String pwd, String pathFile, String knoxurl) {
		this.pathFile = pathFile;
		this.user = user;
		this.pwd = pwd;
		this.knoxurl = knoxurl;
	}

	@Override
	public InputStream run() throws Exception {
		
		System.setProperty("javax.net.ssl.trustStore", "repository/resources/security/client-truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType","JKS");
		System.setProperty("javax.net.debug","ssl");
		
		System.out.println("Entro in run");
		System.out.println(new File(".").getAbsolutePath());
		
		try {
			
			Configuration conf = new Configuration();
			conf.set("fs.swebhdfs.impl", KnoxWebHdfsFileSystem.class.getName());
			if (this.knoxurl!=null) {
				conf.set("knox.username", user);
				conf.set("knox.password", pwd);
			}
			System.out.println("Conf Object " + conf);
			FileSystem fs = null;
			if (knoxurl!=null)
			{
				java.net.URI uri = new java.net.URI(knoxurl);
				fs = FileSystem.get(uri,conf);
			}
			else {
				fs = FileSystem.get(conf);
			}
			
			System.out.println("FileSystem Object " + fs);
			
			System.out.println("Filesystem URI : " + fs.getUri());
			System.out.println("Filesystem Home Directory : " + fs.getHomeDirectory());
			System.out.println("Filesystem Working Directory : " + fs.getWorkingDirectory());
			
			
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
			//Configuration conf = new Configuration();
			//conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/core-site.xml"));
			//conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/hdfs-site.xml"));
			//FileSystem fs = FileSystem.get(conf);

			return fs.open(pt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
