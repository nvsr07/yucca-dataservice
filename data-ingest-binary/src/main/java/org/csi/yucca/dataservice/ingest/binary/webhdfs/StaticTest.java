package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PrivilegedAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.StringInterner;

public class StaticTest implements PrivilegedAction<String> {
	
	static String[] files = {"core-site.xml", 
						     "hdfs-site.xml"};
	
	static StringInterner lf;
	
	private String user,pwd;
	private String op;
	private String confPath;
	private String remPath;
	private String localFile;
	private String knoxurl;
	private boolean beVerbose;
	
	static final String help = "** static test of hdfs functionality mini help **\n" +
	                           "launch as:\n\n" +
			                   "java StaticTest [swithes] <op>\n\n" +
	                           "Where:\n" +
			                   "op one of:\n" +
	                           "ls                 list files\n"+
			                   "cpto               copy local file to hdfs\n" +
	                           "cpfrom             copy hdfs file to local\n\n" +
	                           "switches:\n" +
			                   "-knoxurl=url       if null stardard fs, not null knox (swebhdfs://tst-knox1.pochdp.csi.it:8443/gateway/default/) \n" +
			                   "-user=user         impersonalize user \n" +
			                   "-pwd=password      user password for knox (if knox) \n" +
	                           "-confp=config-path looks in config-path for core-site.xml and hdfs-site-path\n" +
	                           "-rpath=rem-path    remote(hdfs) path\n" +
			                   "-lfile=local-file  local file\n" +
                               "-v                 be verbose\n";
	
	private void usage() {
		System.out.println(help);
		System.exit(0);
	}
	
	private StaticTest(String[] args) {
		if (args == null || args.length < 1)
			usage();
	    for (String s: args) {
	    	if (s.startsWith("-")) {
	    		if (s.startsWith("-user="))
	    			user = s.substring("-user=".length());
	    		if (s.startsWith("-pwd="))
	    			pwd = s.substring("-pwd=".length());
	    		else if (s.startsWith("-confp="))
	    			confPath = s.substring("-confp=".length());
	    		else if (s.startsWith("-rpath="))
	    			remPath = s.substring("-rpath=".length());
	    		else if (s.startsWith("-lfile="))
	    			localFile = s.substring("-lfile=".length());
	    		else if (s.startsWith("-knoxurl="))
	    			knoxurl = s.substring("-knoxurl=".length());
	    		else if (s.startsWith("-v"))
	    			beVerbose = true;;
	    	} else 
	    	  op = s;
	    }
	    if (beVerbose) {
	    	System.out.printf("arg parsing:\n");
	    	System.out.printf("  config path %s\n",confPath);
	    	System.out.printf("  impersonalize user %s\n",user);
	    	System.out.printf("  remotePath %s\n",remPath);
	    	System.out.printf("  localFile %s\n",localFile);
	    	System.out.printf("  operation %s\n",op);
	    	System.out.printf("\n");
	    }
	    int err = 0;
//	    if (confPath == null) {
//	    	System.err.println("no configuration path use -confp switch");
//	    	err++;
//	    }
	    if (user == null) {
	    	System.err.println("no impersonalized user use -user switch");
	    	err++;
	    }
	    if (op == null ) {
	    	System.err.println("no operation (one of: ls cpto cpfrom");
	    	err++;
	    }
	    if (!"ls cpto cpfrom readdir".contains(op)) {
	    	System.err.println("wrong operation (one of: readdir ls cpto cpfrom");
	    	err++;
	    }
	    
	    if (err > 0)
	    	System.exit(0);
	}
	
	private void exec() {
		UserGroupInformation ugi = UserGroupInformation.createRemoteUser(user);

		ugi.doAs(this);
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Static test of HDFS functionality Start");
//		Authenticator.setDefault(new Authenticator(){
//			 public PasswordAuthentication getPasswordAuthentication() {
//		            return (new PasswordAuthentication("smartlab","Ux0mu5it".toCharArray()));
//		        }
//		});
//		
		StaticTest me = new StaticTest(args);
		me.exec();
		System.out.println("all test passed");
		System.out.println("Static test of HDFS functionality End");
		
	}

	@Override
	public String run() {
		/*System.setProperty("javax.net.ssl.trustStore", "D:\\pochdp.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "pochdp");
		System.setProperty("javax.net.ssl.trustStoreType","JKS");		*/
//		System.setProperty("javax.net.ssl.trustStore", "/appserv/wso400/wso005ptf_node01/repository/resources/security/client-truststore.jks");
//		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
//		System.setProperty("javax.net.ssl.trustStoreType","JKS");
//		System.setProperty("javax.net.debug","ssl");
		
		Configuration conf = new Configuration();
		conf.set("fs.swebhdfs.impl", KnoxWebHdfsFileSystem.class.getName());
		if (knoxurl!=null)
		{
			conf.set("knox.username", user);
			conf.set("knox.password", pwd);
		}
		if (confPath!=null)
			for (String s : files) {
				System.out.println("Conf addResource :" + confPath + s);
				conf.addResource(new org.apache.hadoop.fs.Path(confPath + s));
			}
		System.out.println("Conf Object " + conf);
		try {
			FileSystem fs = null;
			if (knoxurl!=null)
			{
				java.net.URI uri = new java.net.URI(knoxurl);
				fs = FileSystem.get(uri,conf);
			}
			else {
				fs = FileSystem.get(conf);
			}
			 
			if (beVerbose) {
				System.out.println("FileSystem Object " + fs);
			
				System.out.println("Filesystem URI : " + fs.getUri());
				System.out.println("Filesystem Home Directory : " + fs.getHomeDirectory());
				System.out.println("Filesystem Working Directory : " + fs.getWorkingDirectory());
			}
			
			if (op.equals("ls")) {
				Path rp = null;
				if (remPath == null)
					rp = new Path(".");
				else
					rp = new Path(remPath);
 				FileStatus[] status = fs.listStatus(rp);
            
 				for(FileStatus st : status) {
 					System.out.printf("%-18s %s%s%s %6d %s%n",
                	   	              st.getOwner(),
                		              st.isDirectory() ? "d" : " ",
                		              st.isSymlink()   ? "s" : " ",
                		              st.getPermission(),
                		              st.getLen(),
                		              st.getPath().getName());
 				}
            } else if (op.equals("cpto")) {
            	FSDataOutputStream os = null;
            	InputStream is = new FileInputStream(localFile);
            	os = fs.create(new Path(remPath));
            	IOUtils.copyBytes(is, os, conf);
            	is.close();
            	os.close();
            } else if (op.equals("cpfrom")) {
				Path rp = null;
				if (remPath == null)
					rp = new Path(".");
				else
					rp = new Path(remPath);
            	InputStream is = fs.open(rp);
            	if (localFile == null)
            		localFile = rp.getName();
            	OutputStream os = new FileOutputStream(localFile);
            	IOUtils.copyBytes(is, os, conf);
            	is.close();
            	os.close();
            } else if (op.equals("readdir")) {
            	ReadDirHdfsAction action = new ReadDirHdfsAction(user, pwd, remPath, knoxurl,0);
            	try {
					Reader rd = action.run();
					int data = rd.read();
			        System.out.print(""+(char)data);
				    while(data != -1){
				        data = rd.read();
				        char dataChar = (char) data;
				        System.out.print(""+dataChar);
				    }
				    
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		
		} catch (IOException e) {
			System.out.println("Fail with Exception");
			e.printStackTrace();
			System.exit(0);
		} catch (URISyntaxException e) {
			System.out.println("Fail with Exception");
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}
