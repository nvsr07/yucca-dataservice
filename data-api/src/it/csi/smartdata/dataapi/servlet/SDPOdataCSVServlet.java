package it.csi.smartdata.dataapi.servlet;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


public class SDPOdataCSVServlet extends HttpServlet {

	static Logger log = Logger.getLogger(SDPOdataCSVServlet.class);
	
	@Override
	  protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
	          throws ServletException, IOException {
		
		
		 Pattern pattern = Pattern.compile("((/Date\\(){1}[0-9]{13}[\\+\\-]{1}[0-9]{4}\\)/){1}");
		 Matcher matcher = null; 
				 
				 
		
		
		try { 
			log.debug("[SDPOdataCSVServlet::doGet] BEGIN");
			
			String restoUri=request.getParameter("restoUri");
			
			
			String porta="8080";
			
			try { 
				porta=SDPDataApiConfig.getInstance().getWebLocalHostPort();
			} catch (Exception e) {

			}
			
			String newUri=SDPDataApiConstants.SDP_WEB_SERVLET_URL+restoUri;
			if (request.getQueryString()!=null && request.getQueryString().length()>0) newUri=newUri+"?"+request.getQueryString();
			
			newUri="http://127.0.0.1:"+porta+"/odata"+newUri;
			log.info("[SDPOdataCSVServlet::doGet] +++++++++++++++++++++++++++++++++++++++++ "+newUri);
			
			HttpClient client = new HttpClient();
			HttpMethod method = new GetMethod(newUri);
			
			int risposta = client.executeMethod(method);
			byte[] responseBody = method.getResponseBody();
			method.releaseConnection();
			//System.out.println(new String(responseBody));
			
			
			JSONObject output = new JSONObject(new String(responseBody));
			 JSONArray docs = output.getJSONObject("d").getJSONArray("results")	;
			 
			 String rigaintestazione="";
			 String curriga="";
			 ArrayList<String> righe=new ArrayList<String>();
			 for (int i=0; i<docs.length();i++) {
				 rigaintestazione="";
				 curriga="";
				 JSONObject curObj=(JSONObject)docs.getJSONObject(i);
				 Iterator<String> it= curObj.keySet().iterator();
				 
				 
				 //START YUCCA-721
				 int colCount=0;
				 //END YUCCA-721
				 while (it.hasNext()) {
					 String curKey=it.next();
					 if (!curKey.equalsIgnoreCase("__metadata") && 
							 !curKey.equalsIgnoreCase("internalId") && 
							 !curKey.equalsIgnoreCase("datasetVersion") && 
							 !curKey.equalsIgnoreCase("idDataset") ) {
						 String curVal=(curObj.isNull(curKey) ?  "" : ""+curObj.get(curKey) );
						 //START YUCCA-721
						 rigaintestazione += (colCount==0 ? curKey : ";"+curKey);
						 //END YUCCA-721
						 
						 
						 matcher = pattern.matcher(curVal);						 
						 
						 if (matcher.matches()) {
							 try {
								  String segno="+";
								  if (curVal.indexOf("+")>=0) segno="+"; 
								  if (curVal.indexOf("-")>=0) segno="-"; 
								  Long datetimestamp = Long.parseLong(curVal.split("["+segno+"]")[0].replaceAll("\\D", ""));
								  Long offset = Long.parseLong(curVal.split("["+segno+"]")[1].replaceAll("\\D", ""));

								 
								 Calendar calendar = new GregorianCalendar();
								  calendar.setTimeInMillis(datetimestamp);
								  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
								  formatter.setCalendar(calendar);
								  formatter.setTimeZone(TimeZone.getTimeZone("GMT"+segno+(offset/60)));
								  //String newTime = formatter.format(calendar.getTime());
								  String newTime = formatter.format(getJSONDateTime(curVal));
								  
								  curVal=newTime;
								 
							 } catch (Exception e) {
								 
							 }
						 }	else if (curKey.equalsIgnoreCase("Binaries")) {
							 
							 
							 try {
								 curVal=curObj.getJSONObject("Binaries").getJSONObject("__deferred").getString("uri");
							 } catch (Exception e) {
								 
							 }
							 
						 }
						 
						 //START YUCCA-721
						 //curriga += (curriga.length()==0 ? curVal : ";"+curVal);
						 curriga += (colCount==0 ? curVal : ";"+curVal);
						 //START YUCCA-721
						 
						 //START YUCCA-721
						 colCount++;
						 //END YUCCA-721
						 
						 
					 }
							 
				 }
				 righe.add(curriga);
			 
			 }
			 righe.add(0, rigaintestazione);
			
			 
			 response.addHeader("Content-Disposition", "attachment;filename=myfilename.csv");
			 response.setContentType("text/csv");
			 StringBuilder sb = new StringBuilder();
			 for(String s : righe){
				 response.getOutputStream().println(s);
			        //sb.append(s);
				 
			    }
			 response.getOutputStream().close();
//			 ByteArrayInputStream stream = new ByteArrayInputStream( sb.toString().getBytes("UTF-8") );
//			 
//			 write(stream, response.getOutputStream());
			
			
//			ServletContext context = this.getServletContext();
//			RequestDispatcher dispatcher = context.getRequestDispatcher();			
//			
//			HttpServletResponseWrapper respLocal=new HttpServletResponseWrapper(response);
//			
//			dispatcher.forward(request, respLocal);
//			
//			log.info("-------------------------------------------------------------------------------------------------------------------");
//			
//			
//			ByteArrayOutputStream byte1=new ByteArrayOutputStream();
//			respLocal.getOutputStream().write(byte1.toByteArray());
//		    String s=byte1.toString();
//			log.info(s);
			
			
		} catch (Exception e) {
			log.error("[SDPOdataCSVServlet::doGet] " +e);
			
			if (e instanceof ServletException) throw (ServletException)e;
			if (e instanceof IOException) throw (IOException)e;
			throw new ServletException(e.getMessage());
			
		} finally {
			log.debug("[SDPOdataCSVServlet::doGet] END");

		}

		
	}
	
	

	
	  private void write(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		    int b;
		    while ((b = inputStream.read()) != -1) {
		      outputStream.write(b);
		    }
		 
		    outputStream.flush();
		  }
	
	  
		private Date getJSONDateTime(String jsonDate) throws ParseException {

			Pattern pt = Pattern.compile("\\/Date\\((\\d+)([\\-\\+]\\d+)\\)\\/");

			Matcher mt = pt.matcher(jsonDate);

			String smillis = null;
			String tz = null;

			if (mt.find() && mt.groupCount() == 2) {
				smillis = mt.group(1);
				tz = mt.group(2);
			}

			if (tz.isEmpty())
				tz = "+0000";

			String sign = tz.substring(0, 1);
			int hh = Integer.parseInt(tz.substring(1, 3));
			int mm = Integer.parseInt(tz.substring(3, 5));

			long millis = Long.parseLong(smillis);

			long offset_errato = hh * (60 ^ 2) * 1000 + mm * 60 * 1000;

			long offset=Long.parseLong(tz.substring(1))*60*1000;
			
			
			
			Date dt = null;

			if (sign.contains("+")) {

				dt = new Date(millis - offset);
			}

			else if (sign.contains("-")) {

				dt = new Date(millis + offset);
			}

			return dt;

		}
	  
}
