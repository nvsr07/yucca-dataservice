package org.csi.yucca.dataservice.binaryapi.knoxapi;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;
import org.csi.yucca.dataservice.binaryapi.ListOfFiles;
import org.csi.yucca.dataservice.binaryapi.SequenceHDFSReader;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatus;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatusContainer;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatuses;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatusesContainer;
import org.csi.yucca.dataservice.binaryapi.knoxapi.util.KnoxWebHDFSConnection;
import org.csi.yucca.dataservice.binaryapi.mongo.singleton.Config;

public class HdfsFSUtils {

	private static Logger logger = RootLogger.getLogger("KnoxHdfsFSUtils");

	public static InputStream readFile(String remotePath, String fileName)
			throws Exception{
		return readFile(remotePath+"/"+fileName);
	}
	


	public static InputStream readFile(String pathForUri) throws Exception {
		InputStream input = null;
		logger.info("[KnoxHdfsFSUtils::readFile] info for path:["+pathForUri+"]");
		try {
			
			input = new KnoxWebHDFSConnection().open(pathForUri);
			logger.info("[KnoxHdfsFSUtils::readFile] info for path:["+pathForUri+"] END");
		} catch (Exception e) {
			logger.error("[KnoxHdfsFSUtils::readFile] info for path:["+pathForUri+"] Error", e);
			throw e;
		}
		return input;
	}

	public static Reader readDir(String remotePath, Integer version) throws Exception {
		logger.info("[KnoxHdfsFSUtils::readDir] read directory:["+remotePath+"]");
		Reader input = null;
		try {

			FileStatusesContainer filesc  = new KnoxWebHDFSConnection().listStatus(remotePath);
			ListOfFiles list = new ListOfFiles(new ArrayList<String>());
			
			
			Integer countFileIntoDir = 0;
			
			if (filesc!=null && filesc.getFileStatuses()!=null && filesc.getFileStatuses().getFileStatus()!=null) {
				for (int i = 0; i < filesc.getFileStatuses().getFileStatus().length; i++) {
					FileStatus currentFile = filesc.getFileStatuses().getFileStatus()[i];
					logger.info("[KnoxHdfsFSUtils::readDir] analyze:["+remotePath+"]+["+currentFile.getPathSuffix()+"]");
					if (currentFile.getType().equals("FILE")) {
						countFileIntoDir++;
						String myFileName = currentFile.getPathSuffix();
						if ((myFileName.substring(myFileName.lastIndexOf("-") + 1).equals(version.toString()+".csv")) 
								|| (version.equals(0))){
							logger.info("[KnoxHdfsFSUtils::readDir] add element:["+remotePath+"/"+currentFile.getPathSuffix()+"]");
							list.addElement(remotePath+"/"+currentFile.getPathSuffix());
						} else {
							logger.info("[KnoxHdfsFSUtils::readDir] SKIP element:["+remotePath+"/"+currentFile.getPathSuffix()+"]");
						}
						
						
					} else 
					{
						logger.info("[KnoxHdfsFSUtils::readDir] SKIP element (directory?):["+remotePath+"]+["+currentFile.getPathSuffix()+"]");
					}
				}
			}
			if (countFileIntoDir.equals(0)){
				logger.warn("[KnoxHdfsFSUtils::readDir] No elements found in :["+remotePath+"]");
			}
			
			Reader sis = new SequenceHDFSReader(list);
			logger.info("[KnoxHdfsFSUtils::readDir] read directory:["+remotePath+"] END");
			return sis;
		} catch (Exception e) {
			logger.error("[KnoxHdfsFSUtils::readDir] Unexpected Error ",e);
			throw e;
		}
	}

	public static String writeFile(String remotePath, InputStream is, String fileName) throws Exception {
		logger.info("[KnoxHdfsFSUtils::statusFile] info for path:["+remotePath+"]["+fileName+"]");
		
		try {
			logger.info("[WriteFileHdfsAction::writeFile] check for file exists:["+remotePath+"]["+fileName+"]");
			FileStatusContainer fs = new KnoxWebHDFSConnection().getFileStatus(remotePath+"/"+fileName);
			if (fs != null){
				logger.error("[WriteFileHdfsAction::writeFile] FileNotFoundException Error getFileStatus = " + fs.getFileStatus());
				throw new Exception("File ["+remotePath+"/"+fileName+"] already exists!");
			}
		} 
		catch (FileNotFoundException fe){
			logger.error("[WriteFileHdfsAction::writeFile] FileNotFoundException Error", fe);
			throw fe;
		} // correct that file doesn't exist
		catch (Exception e){
			logger.error("[WriteFileHdfsAction::writeFile] Exception Error",e);
			throw e;
		}
		
		
		String uri = null;
		try {
			uri = new KnoxWebHDFSConnection().create(remotePath+"/"+fileName, is);
			
			new KnoxWebHDFSConnection().setPermission(uri,"660");
			new KnoxWebHDFSConnection().setOwner(uri, Config.KNOX_USER, Config.KNOX_GROUP);
			
		} catch (Exception e) {
			System.out.println("writeFile, Exception!");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		System.out.println("uri in writeFile = " + uri);
		return uri;
	}
	
	
	public static FileStatus statusFile(String remotePath) throws Exception {
		logger.info("[KnoxHdfsFSUtils::statusFile] info for path:["+remotePath+"]");
		FileStatus fs = null;
		try {
			
			FileStatusContainer fsc = new KnoxWebHDFSConnection().getFileStatus(remotePath);
			if (fsc!=null)
				fs =fsc.getFileStatus();
			logger.info("[KnoxHdfsFSUtils::readFile] info for path:["+remotePath+"] END");
		} catch (Exception e) {
			logger.error("[KnoxHdfsFSUtils::readFile] info for path:["+remotePath+"] Error", e);
			throw e;
		}
		return fs;
		
	}

}
