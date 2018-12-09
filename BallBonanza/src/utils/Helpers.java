package utils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Helpers {
	
	public static URL getResourceURL(Object o, String resource)
	{
		return o.getClass().getClassLoader().getResource(resource);
	}
	
	public static String getResourceURIString(Object o, String resource)
	{
		try {
			return getResourceURL(o, resource).toURI().toString();
		} catch (URISyntaxException e) {
			System.err.println("Columns: URL malformed");
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getStackTraceString(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
	}
	
	public static String getCurrentTimeUTC()
	{
		SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormatUtc.format(new Date());
	}
	
	public static String utcToLocalTime(String utcDate)
	{
		try {
			SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = dateFormatUtc.parse(utcDate);
			
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return dateFormatLocal.format(date);
		} catch (ParseException e) {
			System.err.println("Columns: Could not parse date");
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static boolean validIndex(int index, int arrayLen)
	{
		return index >= 0 && index < arrayLen;
	}
	
	public static long getTimeSpan(Date start, Date end)
	{
		return end.getTime() - start.getTime();
	}
	
	public static String getTimeSpanString(Date start, Date end)
	{
	    long diffInSeconds = (end.getTime() - start.getTime()) / 1000;

	    long diff[] = new long[] { 0, 0, 0, 0 };
	    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
	    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
	    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
	    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));

	    String dayString = diff[0] > 0 ? String.format("%d d", diff[0]) : "";
	    String hourString = diff[1] > 0 ? String.format("%d h", diff[1]) : "";
	    String minuteString = diff[2] > 0 ? String.format("%d min", diff[2]) : "";
	    String secondString = diff[3] > 0 ? String.format("%d s", diff[3]) : "";
	    
	    String hourSpacing = !dayString.equals("") && !hourString.equals("") ? " " : "";
	    String minuteSpacing = (!dayString.equals("") || !hourString.equals("")) && !minuteString.equals("") ? " " : "";
	    String secondSpacing = (!dayString.equals("") || !hourString.equals("") || !minuteString.equals("")) && !secondString.equals("") ? " " : "";
	    
	    return dayString + hourSpacing + hourString + minuteSpacing + minuteString + secondSpacing + secondString;
	}
	
	public static String getTimeSpanString(long diffInMilliSeconds)
	{
		long diffInSeconds = diffInMilliSeconds / 1000;
		
		long diff[] = new long[] { 0, 0, 0, 0 };
	    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
	    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
	    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
	    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));

	    String dayString = diff[0] > 0 ? String.format("%d d", diff[0]) : "";
	    String hourString = diff[1] > 0 ? String.format("%d h", diff[1]) : "";
	    String minuteString = diff[2] > 0 ? String.format("%d min", diff[2]) : "";
	    String secondString = diff[3] > 0 ? String.format("%d s", diff[3]) : "";
	    
	    String hourSpacing = !dayString.equals("") && !hourString.equals("") ? " " : "";
	    String minuteSpacing = (!dayString.equals("") || !hourString.equals("")) && !minuteString.equals("") ? " " : "";
	    String secondSpacing = (!dayString.equals("") || !hourString.equals("") || !minuteString.equals("")) && !secondString.equals("") ? " " : "";
	    
	    return dayString + hourSpacing + hourString + minuteSpacing + minuteString + secondSpacing + secondString;
	}
}
