package org.csi.yucca.dataservice.ingest.binary.hdfs;

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
	private InputStream is;

	public DeleteFilesHdfsAction(String user, String pathFile) {
		this.pathFile = pathFile;
		this.user = user;
	}

	@Override
	public Boolean run() throws Exception {
		FSDataOutputStream os = null;
		try {
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
			Configuration conf = new Configuration();
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
