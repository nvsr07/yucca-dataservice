package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
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
	private String fileName;
	private InputStream is;

	public WriteFileHdfsAction(String user, String pwd, String pathFile, String knoxurl, String knoxgroup, InputStream is, String fileName) {
		this.pathFile = pathFile;
		this.user = user;
		this.pwd = pwd;
		this.knoxurl = knoxurl;
		this.knoxgroup = knoxgroup;
		this.fileName = fileName;
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
				
				SimpleDateFormat sdconnSWebHDFS = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateSWebHDFS = new Date();
				System.out.println("start FileSystem get Conn ( filename="+fileName+") => " + sdconnSWebHDFS.format(dateSWebHDFS));
				
				fs = FileSystem.get(uri, conf);

				
				SimpleDateFormat sdconnEWebHDFS = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateEWebHDFS = new Date();
				System.out.println("end FileSystem get Conn ( filename="+fileName+") => " + sdconnEWebHDFS.format(dateEWebHDFS));
				
				
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
				
				SimpleDateFormat sdconnSFSCreate = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateSFSCreate = new Date();
				System.out.println("start FileSystem CREATE ( filename="+fileName+") => " + sdconnSFSCreate.format(dateSFSCreate));
				
				os = fs.create(pt);

				
				SimpleDateFormat sdconnEFSCreate = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateEWebHDFS = new Date();
				System.out.println("end FileSystem CREATE ( filename="+fileName+") => " + sdconnEFSCreate.format(dateEWebHDFS));
				
				SimpleDateFormat sdScopyBytes = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateScopyBytes = new Date();
				System.out.println("start copyBytes ( filename="+fileName+") => " + sdScopyBytes.format(dateScopyBytes));
				
				IOUtils.copyBytes(is, os, conf);
				
				SimpleDateFormat sdEcopyBytes = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateEcopyBytes = new Date();
				System.out.println("end copyBytes ( filename="+fileName+") => " + sdEcopyBytes.format(dateEcopyBytes));
				
				SimpleDateFormat sdSPermission = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateSPermission = new Date();
				System.out.println("start Permission ( filename="+fileName+") => " + sdSPermission.format(dateSPermission));
				
				System.out.println("Setto Permission");
				fs.setPermission(pt, new FsPermission(FsAction.READ_WRITE,FsAction.READ_WRITE,FsAction.NONE)); //660
				System.out.println("Setto Owner");
				fs.setOwner(pt, user, knoxgroup);
				
				SimpleDateFormat sdEPermission = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				Date dateEPermission = new Date();
				System.out.println("end Permission ( filename="+fileName+") => " + sdEPermission.format(dateEPermission));
				
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
