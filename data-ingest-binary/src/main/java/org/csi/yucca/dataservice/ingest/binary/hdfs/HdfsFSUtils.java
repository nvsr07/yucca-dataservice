package org.csi.yucca.dataservice.ingest.binary.hdfs;

import java.io.InputStream;

import org.apache.hadoop.security.UserGroupInformation;

public class HdfsFSUtils {

	public static InputStream readFile(String user, String remotePath) {
		InputStream input = null;
		try {
			UserGroupInformation ugi = UserGroupInformation
					.createRemoteUser(user);

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
			throw new Exception(e.getMessage());
		}
		return uri;
	}

}
