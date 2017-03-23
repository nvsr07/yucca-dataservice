package org.csi.yucca.dataservice.ingest.binary.hdfs;

import java.io.InputStream;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;

public class WriteFileHdfsAction implements PrivilegedExceptionAction<String> {

	private String pathFile;
	private String user;
	private InputStream is;

	public WriteFileHdfsAction(String user, String pathFile, InputStream is) {
		this.pathFile = pathFile;
		this.user = user;
		this.is = is;
	}

	@Override
	public String run() throws Exception {
		FSDataOutputStream os = null;
		try {
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(
					pathFile);
			Configuration conf = new Configuration();
			conf.addResource(new org.apache.hadoop.fs.Path(
					"src/main/resources/core-site.xml"));
			conf.addResource(new org.apache.hadoop.fs.Path(
					"src/main/resources/hdfs-site.xml"));
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(pt)) {
				throw new Exception("File already exists!");
			} else {
				os = fs.create(pt);
				IOUtils.copyBytes(is, os, conf);
				return pathFile;
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw e;
			throw new Exception(e.getMessage());
		} finally {
			IOUtils.closeStream(is);
			IOUtils.closeStream(os);
		}
	}

}
