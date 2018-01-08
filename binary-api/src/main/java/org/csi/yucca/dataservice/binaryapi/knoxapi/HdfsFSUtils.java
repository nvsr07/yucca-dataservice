package org.csi.yucca.dataservice.binaryapi.knoxapi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;
import org.csi.yucca.dataservice.binaryapi.HDFSFileProps;
import org.csi.yucca.dataservice.binaryapi.ListOfFiles;
import org.csi.yucca.dataservice.binaryapi.SequenceHDFSReader;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatus;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatusContainer;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatusesContainer;
import org.csi.yucca.dataservice.binaryapi.knoxapi.util.KnoxWebHDFSConnection;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

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
	public static Reader readDir(String remotePath, Integer version, int maxFields, String[] headerLine, String[] extractpostValuesMetadata ) throws Exception {
		return readDir( remotePath,  version,  maxFields,  headerLine,  extractpostValuesMetadata, null);
	}
	
	
	public static Reader readDir(String remotePath, Integer version, int maxFields, String headerLine[], String[] extractpostValuesMetadata,HashMap<Integer, Integer> mapVersionMaxFileds) throws Exception {
		logger.info("[KnoxHdfsFSUtils::readDir] read directory:["+remotePath+"]");
		Reader input = null;
		try {

			FileStatusesContainer filesc  = new KnoxWebHDFSConnection().listStatus(remotePath);
			ListOfFiles list = new ListOfFiles();
			
			
			Integer countFileIntoDir = 0;
			
			if (filesc!=null && filesc.getFileStatuses()!=null && filesc.getFileStatuses().getFileStatus()!=null) {
				for (int i = 0; i < filesc.getFileStatuses().getFileStatus().length; i++) {
					FileStatus currentFile = filesc.getFileStatuses().getFileStatus()[i];
					logger.info("[KnoxHdfsFSUtils::readDir] analyze:["+remotePath+"]+["+currentFile.getPathSuffix()+"]");
					if (currentFile.getType().equals("FILE") && currentFile.getPathSuffix().contains(".csv")) {
						countFileIntoDir++;
						String myFileName = currentFile.getPathSuffix();
						String versionStr=myFileName.substring(myFileName.lastIndexOf("-") + 1,myFileName.lastIndexOf(".csv"));
						logger.info("[KnoxHdfsFSUtils::readDir] :["+remotePath+"/"+currentFile.getPathSuffix()+"] has version="+versionStr);
						if ((myFileName.substring(myFileName.lastIndexOf("-") + 1).equals(version.toString()+".csv")) 
								|| (version.equals(0))){
							logger.info("[KnoxHdfsFSUtils::readDir] ))) add element:["+remotePath+"/"+currentFile.getPathSuffix()+"]");
							
							HDFSFileProps prp=new HDFSFileProps(); 
							prp.setDatasetVersion(Integer.parseInt(versionStr));
							prp.setFullFilePath(remotePath+"/"+currentFile.getPathSuffix());

							
							logger.info("[KnoxHdfsFSUtils::readDir] ))) add element:  versionStr="+versionStr +   "    maxfields="+mapVersionMaxFileds.get(new Integer(versionStr))    );
							if (null!=mapVersionMaxFileds && null!=mapVersionMaxFileds.get(new Integer(versionStr))) {
								prp.setMaxFileds(mapVersionMaxFileds.get(new Integer(versionStr)));
							}
							
							//list.addElement(remotePath+"/"+currentFile.getPathSuffix());
							list.addElement(prp);
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
			
			Reader sis = new SequenceHDFSReader(list,maxFields,headerLine,extractpostValuesMetadata);
			
			// try to fix max size (50 MB)
//			HDFSFileProps curF=(HDFSFileProps) list.nextElement();
//        	String p = curF.getFullFilePath();
//        	CSVReader csv = new CSVReader(new InputStreamReader(new KnoxWebHDFSConnection().open(p)), ',', '"', 1);
//			Reader sis = new TryReader(csv,maxFields,headerLine,extractpostValuesMetadata);

			
			logger.info("[KnoxHdfsFSUtils::readDir] read directory:["+remotePath+"] END");
			return sis;
		} catch (Exception e) {
			logger.error("[KnoxHdfsFSUtils::readDir] Unexpected Error ",e);
			throw e;
		}
	}
	
	
	

	public static String writeFile(String remotePath, InputStream is, String fileName) throws Exception {
		logger.info("[KnoxHdfsFSUtils::writeFile] info for path:["+remotePath+"]["+fileName+"]");
		
		try {
			logger.info("[WriteFileHdfsAction::writeFile] check for file exists:["+remotePath+"]["+fileName+"]");
			FileStatusContainer fs = new KnoxWebHDFSConnection().getFileStatus(remotePath+"/"+fileName);
			if (fs.getFileStatus() != null){
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
			logger.info("[WriteFileHdfsAction::writeFile] InputStream:["+is+"]");
			logger.info("[WriteFileHdfsAction::writeFile] path before create:["+remotePath+"/"+fileName+"]");
			uri = new KnoxWebHDFSConnection().create(remotePath+"/"+fileName, is);
			
			logger.info("[WriteFileHdfsAction::writeFile] uri after create:["+uri+"]");
			new KnoxWebHDFSConnection().setPermission(remotePath+"/"+fileName, "660");
			//new KnoxWebHDFSConnection().setOwner(remotePath+"/"+fileName, Config.KNOX_USER, Config.KNOX_GROUP);
			
		} catch (Exception e) {
			logger.error("[WriteFileHdfsAction::writeFile] - writeFile, Exception!");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		logger.error("[WriteFileHdfsAction::writeFile] - uri in writeFile = " + uri);
		return uri;
	}
	
	
	public static FileStatus statusFile(String remotePath) throws Exception {
		logger.info("[KnoxHdfsFSUtils::statusFile] info for path:["+remotePath+"]");
		FileStatus fs = null;
		try {
			
			FileStatusContainer fsc = new KnoxWebHDFSConnection().getFileStatus(remotePath);
			if (fsc != null)
				fs = fsc.getFileStatus();
			logger.info("[KnoxHdfsFSUtils::statusFile] info for path:["+remotePath+"] END");
		} catch (Exception e) {
			logger.error("[KnoxHdfsFSUtils::statusFile] info for path:["+remotePath+"] Error", e);
			throw e;
		}
		return fs;
		
	}

}

 class TryReader extends Reader {
	CSVReader csvIn;
	StringReader buf;
	
	int maxFields;
	int curMaxFields=0;
	String[] headerLine;
	String[] extractpostValuesMetadata;
	public TryReader(CSVReader csv, int maxFields, String[] headerLine, String[] extractpostValuesMetadata) throws IOException {
		this.csvIn = csv;
		this.maxFields = maxFields;
		this.headerLine = headerLine;
		this.extractpostValuesMetadata = extractpostValuesMetadata;
		System.out.println("Inizializzazione");
		nextLine(true);
	}
	
	@Override
	public void close() throws IOException {
		csvIn.close();
	}
	@Override
	public int read(char[] c, int off, int len) throws IOException {
		if (buf == null) {
			System.out.println("buff nullo!");
			return -1;
		} else if (c == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > c.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int n = buf.read(c, off, len);
		if (n <= 0) {
			nextLine(false);
			return read(c, off, len);
		}
		return n;
		
	}

	private void nextLine(boolean writeHeader) throws IOException {
		if (csvIn!=null)
		{
			String[] fields = csvIn.readNext();
			if (fields==null) {
				System.out.println("fields null!");
				csvIn = null;
			}
			else {
				if (fields[0].length()>24)
				{
					System.out.println("-->"+fields[0].length());
				}
				
				StringWriter sw = new StringWriter();
				CSVWriter csvw =new CSVWriter(sw,';',CSVWriter.DEFAULT_QUOTE_CHARACTER,"\n" );
				if (writeHeader) csvw.writeNext(headerLine);
				else csvw.writeNext(fields);
				
				
				buf = new StringReader(sw.toString());
				
				
				csvw.flush();
				csvw.close();
			}
		}
		else
		{			
			System.out.println("csvIn nullo");
			buf = null;
		}
		
	}
}
