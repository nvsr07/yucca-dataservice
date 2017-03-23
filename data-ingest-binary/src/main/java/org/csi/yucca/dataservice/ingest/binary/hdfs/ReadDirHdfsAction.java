package org.csi.yucca.dataservice.ingest.binary.hdfs;

import java.io.File;
import java.io.Reader;
import java.security.PrivilegedExceptionAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.csi.yucca.dataservice.ingest.binary.ListOfFiles;
import org.csi.yucca.dataservice.ingest.binary.SequenceHDFSReader;

public class ReadDirHdfsAction implements PrivilegedExceptionAction<Reader> {

	private String pathFile;
	private Integer version;
	private Integer versionCurrent;

	public ReadDirHdfsAction(String user, String pathFile, Integer version) {
		this.pathFile = pathFile;
		this.version = version;
		this.versionCurrent = 0;
	}

	public ReadDirHdfsAction(String user, String pathFile, Integer version, Integer versionCurrent) {
		this.pathFile = pathFile;
		this.version = version;
		this.versionCurrent = versionCurrent;
	}

	@Override
	public Reader run() throws Exception {
		try {
			
			System.out.println("Entro in run di ReadDirHdfsAction (hdfs)");
			System.out.println(new File(".").getAbsolutePath());
			
			org.apache.hadoop.fs.Path pt = new org.apache.hadoop.fs.Path(pathFile);
			Configuration conf = new Configuration();
			conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/core-site.xml"));
			conf.addResource(new org.apache.hadoop.fs.Path("src/main/resources/hdfs-site.xml"));
			
			FileSystem fs = FileSystem.get(conf);
		
			System.out.println("FileSystem Object " + fs);
			System.out.println("Filesystem URI : " + fs.getUri());
			System.out.println("Filesystem Home Directory : " + fs.getHomeDirectory());
			System.out.println("Filesystem Working Directory : " + fs.getWorkingDirectory());
			System.out.println("Filesystem PathFile : " + pathFile);
			
			RemoteIterator<LocatedFileStatus> ritr = fs.listFiles(pt, false);
			System.out.println("Ottengo la lista" + ritr.toString());
			ListOfFiles list = new ListOfFiles(null);
			Integer countFileIntoDir = 0;
			
			while(ritr.hasNext()){
				FileStatus mFile = ritr.next();
				countFileIntoDir++;
				if (mFile.isFile()){
					org.apache.hadoop.fs.Path myPath = mFile.getPath();
					String myFileName = myPath.getName();
					System.out.println("Analizzo il file " + myFileName);
					if (myFileName.substring(myFileName.lastIndexOf("-") + 1).equals(this.version.toString()+".csv")){
						org.apache.hadoop.fs.Path localPath = org.apache.hadoop.fs.Path.getPathWithoutSchemeAndAuthority(myPath);
						System.out.println("Faccio OPEN sul file " + localPath.toString());
						try {
							System.out.println("Inizio lettura sul file " + myFileName);
							System.out.println("Inserisco il file " + myFileName + " nella lista!");
							list.addElement(localPath);
							System.out.println("File " + myFileName + " inserito!");
						} catch (Exception ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
							System.out.println("To String = " + ex.toString());
							System.out.println("getMsg = " + ex.getMessage());
						}
						System.out.println("File OK");
					} else {

						System.out.println("File Ko!");
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
