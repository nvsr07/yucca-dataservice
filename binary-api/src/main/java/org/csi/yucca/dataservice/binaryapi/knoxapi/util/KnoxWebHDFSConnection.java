package org.csi.yucca.dataservice.binaryapi.knoxapi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatus;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatusContainer;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatuses;
import org.csi.yucca.dataservice.binaryapi.knoxapi.json.FileStatusesContainer;
import org.csi.yucca.dataservice.binaryapi.util.BinaryConfig;
import org.csi.yucca.dataservice.binaryapi.util.json.JSonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * 
===== HTTP GET <br/>
<li>OPEN (see FileSystem.open)
<li>GETFILESTATUS (see FileSystem.getFileStatus)
<li>LISTSTATUS (see FileSystem.listStatus)
<li>GETCONTENTSUMMARY (see FileSystem.getContentSummary)
<li>GETFILECHECKSUM (see FileSystem.getFileChecksum)
<li>GETHOMEDIRECTORY (see FileSystem.getHomeDirectory)
<li>GETDELEGATIONTOKEN (see FileSystem.getDelegationToken)
<li>GETDELEGATIONTOKENS (see FileSystem.getDelegationTokens)
<br/>
===== HTTP PUT <br/>
<li>CREATE (see FileSystem.create)
<li>MKDIRS (see FileSystem.mkdirs)
<li>CREATESYMLINK (see FileContext.createSymlink)
<li>RENAME (see FileSystem.rename)
<li>SETREPLICATION (see FileSystem.setReplication)
<li>SETOWNER (see FileSystem.setOwner)
<li>SETPERMISSION (see FileSystem.setPermission)
<li>SETTIMES (see FileSystem.setTimes)
<li>RENEWDELEGATIONTOKEN (see FileSystem.renewDelegationToken)
<li>CANCELDELEGATIONTOKEN (see FileSystem.cancelDelegationToken)
<br/>
===== HTTP POST <br/>
APPEND (see FileSystem.append)
<br/>
===== HTTP DELETE <br/>
DELETE (see FileSystem.delete)

 */
public class KnoxWebHDFSConnection {

	protected static final Logger logger = LoggerFactory.getLogger(KnoxWebHDFSConnection.class);

	private String httpfsUrl = BinaryConfig.getKnoxUrl();
	private String principal = BinaryConfig.getKnoxUser();
	private String password = BinaryConfig.getKnoxPwd();

	static {
		System.setProperty("jsse.enableSNIExtension", "false");
	}
	
	private static CloseableHttpClient getHttpClientKnox() {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		//Settata la http GET, setto le credenziali di KNOX!
		return client;
	}

	private static CloseableHttpClient getHttpClientKnox(boolean disableRedirect) {
		
		if (disableRedirect)
			return HttpClientBuilder.create().disableRedirectHandling().build();
		else 
			return getHttpClientKnox();
	}
	
	private static HttpClientContext getHttpContext(){
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(BinaryConfig.getKnoxUser(), BinaryConfig.getKnoxPwd()));
		// Add AuthCache to the execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		return context;
	}
	

	public KnoxWebHDFSConnection() {
	}

	public KnoxWebHDFSConnection(String httpfsUrl, String principal, String password) {
		this.httpfsUrl = httpfsUrl;
		this.principal = principal;
		this.password = password;
	}

	protected static long copy(InputStream input, OutputStream result) throws IOException {
		byte[] buffer = new byte[12288]; // 8K=8192 12K=12288 64K=
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			result.write(buffer, 0, n);
			count += n;
			result.flush();
		}
		result.flush();
		return count;
	}

	

	/*
	 * ========================================================================
	 * GET
	 * ========================================================================
	 */
	/**
	 * <b>GETHOMEDIRECTORY</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/?op=GETHOMEDIRECTORY"
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getHomeDirectory() throws MalformedURLException, IOException {
		String spec = MessageFormat.format("/?op=GETHOMEDIRECTORY&user.name={0}", this.principal);

		return genericGetForStringCall(spec);
	}

	/**
	 * <b>OPEN</b>
	 * 
	 * curl -i -L "http://<HOST>:<PORT>/<PATH>?op=OPEN
	 * [&offset=<LONG>][&length=<LONG>][&buffersize=<INT>]"
	 * 
	 * @param path
	 * @param os
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public InputStream open(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=OPEN&user.name={1}", URLUtil.encodePath(path), this.principal);

		HttpGet get;
		CloseableHttpClient client = null;
		HttpResponse response = null;
		
		try {
			get = new HttpGet(new URL(httpfsUrl+spec).toURI());
			get.setHeader("Content-Type", "application/octet-stream");
			client = getHttpClientKnox();
			response = client.execute(get,getHttpContext());
			
			if (response.getStatusLine().getStatusCode()>=400 && response.getStatusLine().getStatusCode()<400)
			{
				throw new FileNotFoundException(path);
			}
			if (response.getStatusLine().getStatusCode()>=500)
			{
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				throw new IOException("Knox excpetion ["+response.getStatusLine().getStatusCode()+"] for path ["+path+"]. Message:["+responseString+"]");
			}

			return response.getEntity().getContent();

		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			//client.close();
		}

		
		
	}

	/**
	 * <b>GETCONTENTSUMMARY</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=GETCONTENTSUMMARY"
	 * 
	 * @param path
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getContentSummary(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=GETCONTENTSUMMARY&user.name={1}", URLUtil.encodePath(path), this.principal);
		return genericGetForStringCall(spec);
		
	}

	/**
	 * <b>LISTSTATUS</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=LISTSTATUS"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	public FileStatusesContainer listStatus(String path) throws Exception {
		String spec = MessageFormat.format("{0}?op=LISTSTATUS&user.name={1}", URLUtil.encodePath(path), this.principal);
		logger.info("[KnoxWebHDFSConnection::listStatus] Knox httpfsUrl:"+httpfsUrl);
		logger.info("[KnoxWebHDFSConnection::listStatus] Knox spec:"+spec);
		logger.info("[KnoxWebHDFSConnection::listStatus] URI:"+new URL(httpfsUrl+spec).toURI());
		
		HttpGet get;
		try {
			get = new HttpGet(new URL(httpfsUrl+spec).toURI());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		}
		HttpResponse response = getHttpClientKnox().execute(get,getHttpContext());
		
		if (response.getStatusLine().getStatusCode()>=400 && response.getStatusLine().getStatusCode()<400)
		{
			throw new FileNotFoundException(path);
		}
		if (response.getStatusLine().getStatusCode()>=500)
		{
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			throw new Exception("Knox excpetion ["+response.getStatusLine().getStatusCode()+"] for path ["+path+"]. Message:["+responseString+"]");
		}

		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		logger.info("[KnoxWebHDFSConnection::listStatus] responseString:"+responseString);

		
		Gson gson = JSonHelper.getInstance();
		FileStatusesContainer fs = gson.fromJson(responseString,FileStatusesContainer.class);
		
		logger.info("[KnoxWebHDFSConnection::listStatus] fs:"+fs);
		if (fs!=null)
			logger.info("[KnoxWebHDFSConnection::listStatus] fs.getFileStatus():"+fs.getFileStatuses());
		
		return fs;
	}

	/**
	 * <b>GETFILESTATUS</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=GETFILESTATUS"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	public FileStatusContainer getFileStatus(String path) throws Exception {
		String spec = MessageFormat.format("{0}?op=GETFILESTATUS&user.name={1}", URLUtil.encodePath(path), this.principal);
		HttpGet get;
		try {
			get = new HttpGet(new URL(httpfsUrl+spec).toURI());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		}
		HttpResponse response = getHttpClientKnox().execute(get,getHttpContext());
		
		if (response.getStatusLine().getStatusCode()>=400 && response.getStatusLine().getStatusCode()<400)
		{
			throw new FileNotFoundException(path);
		}
		if (response.getStatusLine().getStatusCode()>=500)
		{
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			throw new Exception("Knox excpetion ["+response.getStatusLine().getStatusCode()+"] for path ["+path+"]. Message:["+responseString+"]");		}

		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");

		
		Gson gson = JSonHelper.getInstance();
		FileStatusContainer fs = gson.fromJson(responseString,FileStatusContainer.class);
		
		
		return fs;
		
	}

	/**
	 * <b>GETFILECHECKSUM</b>
	 * 
	 * curl -i "http://<HOST>:<PORT>/<PATH>?op=GETFILECHECKSUM"
	 * 
	 * @param path
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getFileCheckSum(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=GETFILECHECKSUM&user.name={1}", URLUtil.encodePath(path), this.principal);
		return genericGetForStringCall(spec);
		
		
	}

	private String genericGetForStringCall(String spec)
			throws MalformedURLException, IOException, ClientProtocolException {
		HttpGet get;
		CloseableHttpClient client = null;
		try {
			get = new HttpGet(new URL(httpfsUrl+spec).toURI());
			client = getHttpClientKnox();
			HttpResponse response = client.execute(get,getHttpContext());
			return EntityUtils.toString(response.getEntity());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			if (client!=null)
				client.close();
		}
	}

	private String genericPutForStringCall(String spec)
			throws ParseException, Exception {
		HttpPut put;
		CloseableHttpClient client = null;
		try {
			put = new HttpPut(new URL(httpfsUrl+spec).toURI());
			client = getHttpClientKnox();
			HttpResponse response = client.execute(put,getHttpContext());
			logger.info("[KnoxWebHDFSConnection:genericPutForStringCall] - response = " + response);
			logger.info("[KnoxWebHDFSConnection:genericPutForStringCall] - getStatusLine = " + response.getStatusLine());
			logger.info("[KnoxWebHDFSConnection:genericPutForStringCall] - getStatusCode = " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode()>299)
				throw new Exception(EntityUtils.toString(response.getEntity()));
			return EntityUtils.toString(response.getEntity());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			if (client!=null)
				client.close();
		}
	}

	private String genericDeleteForStringCall(String spec)
			throws MalformedURLException, IOException, ClientProtocolException {
		HttpDelete delete;
		CloseableHttpClient client = null;
		try {
			delete = new HttpDelete(new URL(httpfsUrl+spec).toURI());
			client = getHttpClientKnox(false);
			HttpResponse response = client.execute(delete,getHttpContext());
			return EntityUtils.toString(response.getEntity());
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			if (client!=null)
				client.close();
		}
	}
	
	/*
	 * ========================================================================
	 * PUT
	 * ========================================================================
	 */
	/**
	 * <b>CREATE</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=CREATE
	 * [&overwrite=<true|false>][&blocksize=<LONG>][&replication=<SHORT>]
	 * [&permission=<OCTAL>][&buffersize=<INT>]"
	 * 
	 * @param path
	 * @param is
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String create(String path, InputStream is) throws MalformedURLException, IOException{
		String resp = null;
		String spec = MessageFormat.format("{0}?op=CREATE&user.name={1}", URLUtil.encodePath(path), this.principal);

		logger.info("[KnoxWebHDFSConnection:create] - httpfsUrl = " + httpfsUrl);
		logger.info("[KnoxWebHDFSConnection:create] - spec = " + spec);
		
		String redirectUrl = null;
		CloseableHttpClient client = null;
		HttpPut put;
		try {
			logger.info("[KnoxWebHDFSConnection:create] - httpfsUrl+spec = " + httpfsUrl+spec);
			put = new HttpPut(new URL(httpfsUrl+spec).toURI());
			client = getHttpClientKnox(true);
			HttpResponse response = client.execute(put, getHttpContext());
			
			if (response.getStatusLine().getStatusCode() == 307)
				redirectUrl = response.getFirstHeader("Location").getValue();

			
			if (redirectUrl != null) {
				HttpPut put2;
				CloseableHttpResponse response2=null;
				try {
					put2 = new HttpPut(redirectUrl);
					InputStreamEntity entity = new InputStreamEntity(is, -1, ContentType.APPLICATION_OCTET_STREAM);
					
		            entity.setContentType("binary/octet-stream");
		            entity.setChunked(true); 

		            BufferedHttpEntity myEntity = null;
		            try {
		                myEntity = new BufferedHttpEntity(entity);
		            } catch (IOException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		            }

		            put2.setEntity(myEntity);
					
					//put2.setEntity(entity);
					response2 = getHttpClientKnox().execute(put2,getHttpContext());
					resp = response2.getFirstHeader("Location").getValue();
				}
				finally {
					if (response2!=null)
						response2.close();
				}
				
			}
						
			
		} catch (URISyntaxException e) {
			throw new MalformedURLException(e.getMessage());
		} finally {
			client.close();
		}
		

		

		return resp;
	}

	/**
	 * <b>MKDIRS</b>
	 * 
	 * curl -i -X PUT
	 * "http://<HOST>:<PORT>/<PATH>?op=MKDIRS[&permission=<OCTAL>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String mkdirs(String path) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=MKDIRS&user.name={1}", URLUtil.encodePath(path), this.principal);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>CREATESYMLINK</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=CREATESYMLINK
	 * &destination=<PATH>[&createParent=<true|false>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String createSymLink(String srcPath, String destPath) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=CREATESYMLINK&destination={1}&user.name={2}",
				URLUtil.encodePath(srcPath), URLUtil.encodePath(destPath), this.principal);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>RENAME</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=RENAME
	 * &destination=<PATH>[&createParent=<true|false>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String rename(String srcPath, String destPath) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=RENAME&destination={1}&user.name={2}",
				URLUtil.encodePath(srcPath), URLUtil.encodePath(destPath), this.principal);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETPERMISSION</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETPERMISSION
	 * [&permission=<OCTAL>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String setPermission(String path, String permission) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETPERMISSION&permission={1}&user.name={2}", URLUtil.encodePath(path),permission, this.principal);
		logger.info("[KnoxWebHDFSConnection:setPermission] - spec = " + spec);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETOWNER</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETOWNER
	 * [&owner=<USER>][&group=<GROUP>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String setOwner(String path, String owner, String group) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETOWNER&owner={1}&group={2}&user.name={3}", URLUtil.encodePath(path), owner, group, this.principal);
		logger.info("[setOwner] - spec = " + spec);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETREPLICATION</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETREPLICATION
	 * [&replication=<SHORT>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String setReplication(String path) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETREPLICATION&user.name={1}", URLUtil.encodePath(path), this.principal);
		return genericPutForStringCall(spec);
	}

	/**
	 * <b>SETTIMES</b>
	 * 
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=SETTIMES
	 * [&modificationtime=<TIME>][&accesstime=<TIME>]"
	 * 
	 * @param path
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	public String setTimes(String path) throws ParseException, Exception {
		String spec = MessageFormat.format("{0}?op=SETTIMES&user.name={1}", URLUtil.encodePath(path), this.principal);
		return genericPutForStringCall(spec);
	}

	/*
	 * ========================================================================
	 * POST
	 * ========================================================================
	 */
	/**
	 * curl -i -X POST
	 * "http://<HOST>:<PORT>/<PATH>?op=APPEND[&buffersize=<INT>]"
	 * 
	 * @param path
	 * @param is
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String append(String path, InputStream is) throws MalformedURLException, IOException{
		String resp = null;
//		ensureValidToken();
//		String spec = MessageFormat.format("/{0}?op=APPEND&user.name={1}", URLUtil.encodePath(path), this.principal);
//		String redirectUrl = null;
//		HttpURLConnection conn = authenticatedURL.openConnection(new URL(new URL(httpfsUrl), spec), token);
//		conn.setRequestMethod("POST");
//		conn.setInstanceFollowRedirects(false);
//		conn.connect();
//		logger.info("Location:" + conn.getHeaderField("Location"));
//		resp = result(conn, true);
//		if (conn.getResponseCode() == 307)
//			redirectUrl = conn.getHeaderField("Location");
//		conn.disconnect();
//
//		if (redirectUrl != null) {
//			conn = authenticatedURL.openConnection(new URL(redirectUrl), token);
//			conn.setRequestMethod("POST");
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setUseCaches(false);
//			conn.setRequestProperty("Content-Type", "application/octet-stream");
//			// conn.setRequestProperty("Transfer-Encoding", "chunked");
//			final int _SIZE = is.available();
//			conn.setRequestProperty("Content-Length", "" + _SIZE);
//			conn.setFixedLengthStreamingMode(_SIZE);
//			conn.connect();
//			OutputStream os = conn.getOutputStream();
//			copy(is, os);
//			// Util.copyStream(is, os);
//			is.close();
//			os.close();
//			resp = result(conn, true);
//			conn.disconnect();
//		}

		return resp;
	}

	/*
	 * ========================================================================
	 * DELETE
	 * ========================================================================
	 */
	/**
	 * <b>DELETE</b>
	 * 
	 * curl -i -X DELETE "http://<host>:<port>/<path>?op=DELETE
	 * [&recursive=<true|false>]"
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public String delete(String path) throws MalformedURLException, IOException {
		String spec = MessageFormat.format("{0}?op=DELETE&user.name={1}", URLUtil.encodePath(path), this.principal);
		return genericDeleteForStringCall(spec);
	}

	// Begin Getter & Setter
	public String getHttpfsUrl() {
		return httpfsUrl;
	}

	public void setHttpfsUrl(String httpfsUrl) {
		this.httpfsUrl = httpfsUrl;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// End Getter & Setter
}