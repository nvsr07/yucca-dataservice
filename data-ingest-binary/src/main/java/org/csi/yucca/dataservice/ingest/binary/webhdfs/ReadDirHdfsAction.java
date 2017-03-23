package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.csi.yucca.dataservice.ingest.binary.ListOfFiles;
import org.csi.yucca.dataservice.ingest.binary.SequenceHDFSReader;

public class ReadDirHdfsAction implements PrivilegedExceptionAction<Reader> {

	private String pathFile;
	private String user; 
	private String pwd;
	private String knoxurl;
	private Integer version;

	public ReadDirHdfsAction(String user, String pwd, String pathFile, String knoxurl, Integer version) {
		this.pathFile = pathFile;
		this.user = user;
		this.pwd = pwd;
		this.knoxurl = knoxurl;
		this.version = version;
	}
	
	public Long getSizeFile() throws Exception {
		
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
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);

		
			FileStatus fStatus = fs.getFileStatus(pt);
			Long size = fStatus.getLen();
			
			return size;
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Reader run() throws Exception {
		
		System.setProperty("javax.net.ssl.trustStore", "repository/resources/security/client-truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType","JKS");
		System.setProperty("javax.net.debug","ssl");
		
		System.out.println("Entro in run di ReadDirHdfsAction (webhdfs)");
		System.out.println(new File(".").getAbsolutePath());
		
		try {
			
			Configuration conf = new Configuration();
			conf.set("fs.swebhdfs.impl", KnoxWebHdfsFileSystem.class.getName());
			System.out.println("Conf Object " + conf);
			
			FileSystem fs = null;
			if (knoxurl!=null) {
				
				conf.set("knox.username", user);
				conf.set("knox.password", pwd);
				
				java.net.URI uri = new java.net.URI(knoxurl);
				fs = FileSystem.get(uri,conf);
				
				System.out.println("FS settata con url e conf");
			} else {
				fs = FileSystem.get(conf);
				
				System.out.println("FS settata solo con conf");
			}
			
			System.out.println("FileSystem Object " + fs.toString());
			System.out.println("Filesystem URI : " + fs.getUri());
			System.out.println("Filesystem Home Directory : " + fs.getHomeDirectory());
			System.out.println("Filesystem Working Directory : " + fs.getWorkingDirectory());
			System.out.println("pathFile : " + pathFile);
			
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
			RemoteIterator<LocatedFileStatus> ritr = fs.listFiles(pt, false);
			ListOfFiles list = new ListOfFiles(new ArrayList<org.apache.hadoop.fs.Path>());
			System.out.println("Ottengo la lista" + ritr.toString());
			Integer countFileIntoDir = 0;
			
			while(ritr.hasNext()){
				FileStatus mFile = ritr.next();
				countFileIntoDir++;
				if (mFile.isFile()){
					org.apache.hadoop.fs.Path myPath = mFile.getPath();
					String myFileName = myPath.getName();
					System.out.println("Analizzo il file " + myFileName);
					
					if ((myFileName.substring(myFileName.lastIndexOf("-") + 1).equals(this.version.toString()+".csv")) || (this.version.equals(0))){
						org.apache.hadoop.fs.Path localPath = org.apache.hadoop.fs.Path.getPathWithoutSchemeAndAuthority(myPath);
						System.out.println("Aggiungo file alla lista" + localPath.toString());
						try {
							System.out.println("Inizio lettura sul file " + myFileName);
							System.out.println("Inserisco il file " + myFileName + " nella lista!");
							list.addElement(localPath);
							System.out.println("File " + myFileName + " inserito!");
						} catch (Exception ex) {
							ex.printStackTrace();
							System.out.println("To String = " + ex.toString());
							System.out.println("getMsg = " + ex.getMessage());
						}

						System.out.println("File OK");
					} else {
						System.out.println("File Ko");
					}
				} else {
					//che faccio?
					System.out.println("Esco MALE???? mFile = " + mFile.toString());
				}
	        }
			if (countFileIntoDir.equals(0)){
				System.out.println("Cartella VUOTA!!!!");
			}
			
			Reader sis = new SequenceHDFSReader(fs, list);
			System.out.println("Esco BENE!!");
			
			return sis;
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Esco MALISSIMO!!!");
		return null;
	}
}
