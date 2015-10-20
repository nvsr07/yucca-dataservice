package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.InputStream;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class DeleteFilesHdfsAction implements PrivilegedExceptionAction<Boolean> {

	private String pathFile;
	private String user;
	private String pwd;
	private String knoxurl;
	private InputStream is;

	public DeleteFilesHdfsAction(String user, String pwd, String pathFile, String knoxurl) {
		this.pathFile = pathFile;
		this.user = user;
		this.pwd = pwd;
		this.knoxurl = knoxurl;
	}

	@Override
	public Boolean run() throws Exception {
		FSDataOutputStream os = null;
		try {

			Configuration conf = new Configuration();
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
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
			
			
			if (fs.exists(pt)) {
				System.out.println("File trovato, cancello!");
				Path path = new Path(this.pathFile);
				return fs.delete(path, true);
			} else {
				System.out.println("File NO trovato, Exception!");
				throw new Exception("File not found!");
			}
			
			/*
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
			conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/core-site.xml"));
			conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/hdfs-site.xml"));
			System.out.println("Parto da " + pathFile);
			System.out.println(" e cerco " + pt.toString());
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(pt)) {
				System.out.println("File trovato, cancello!");
				Path path = new Path(this.pathFile);
				return fs.delete(path, true);
			} else {
				System.out.println("File NO trovato, Exception!");
				throw new Exception("File not found!");
			}
			*/

		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
			throw new Exception(e.getMessage());
		} finally {
			IOUtils.closeStream(is);
			IOUtils.closeStream(os);
		}
	}

}
