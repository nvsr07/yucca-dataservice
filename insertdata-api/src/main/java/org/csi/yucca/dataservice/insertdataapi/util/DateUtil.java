package org.csi.yucca.dataservice.insertdataapi.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import javax.xml.bind.DatatypeConverter;

public class DateUtil {
    public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String _msDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String _secDateFormat_TZ = "yyyy-MM-dd'T'HH:mm:ssZ";
    
    
    public static Date multiParseDate(String dataString)
    {
    	Date o=null;
    	SimpleDateFormat format = new SimpleDateFormat(_msDateFormat);
        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        o = format.parse(dataString, new ParsePosition(0));
        if (o == null) {
            // try older format with no ms
            format = new SimpleDateFormat(_secDateFormat);
            format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            o = format.parse(dataString, new ParsePosition(0));
            if (o == null) {
                // try timezone
                format = new SimpleDateFormat(_msDateFormat_TZ);
                format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                o = format.parse(dataString, new ParsePosition(0));
                if (o == null) {
                    // try older format timezone
                    format = new SimpleDateFormat(_secDateFormat_TZ);
                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                    o = format.parse(dataString, new ParsePosition(0));
                }
            
                
            }
        }
         
        return o;
    }
}
