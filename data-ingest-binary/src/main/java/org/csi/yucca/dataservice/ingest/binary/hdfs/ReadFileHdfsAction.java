package org.csi.yucca.dataservice.ingest.binary.hdfs;

import java.io.InputStream;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class ReadFileHdfsAction implements
		PrivilegedExceptionAction<InputStream> {

	private String pathFile;
	private String user;

	public ReadFileHdfsAction(String user, String pathFile) {
		this.pathFile = pathFile;
		this.user = user;
	}

	@Override
	public InputStream run() throws Exception {
		try {
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(
					pathFile);
			Configuration conf = new Configuration();
			conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/core-site.xml"));
			conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/hdfs-site.xml"));
			FileSystem fs = FileSystem.get(conf);

			return fs.open(pt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
