package com.app.myapp.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.*;


/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 *
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 *   to correct time pattern. Minutes should be mm not MM
 * 	(MM is month). 
 * @version $Revision$ $Date$
 */
public class DateUtil {
    //~ Static fields/initializers =============================================
	public static final String defaultTimestampPattern =
		"yyyy-MM-dd HH:mm:ss.SSS";
	public static final String defaultDatePattern = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String datePattern = "yyyy-MM-dd";
    private static final String timePattern = "HH:mm:ss S";
    private static final String datePatternWithAllNumber = "yyyyMMdd";
    private static final String datetimePattern = "yyyyMMddHHmmss";
	private static final SimpleDateFormat dateFormat = 
		new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	
	private static final String datetimetoPattern = "yyyy/MM/dd HH:mm:ss";

    //~ Methods ================================================================

    /**
     * Return default datePattern (MM/dd/yyyy)
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return datePattern;
    }
	/**
	 * 按照客户端日期格式转化日期，如：2005-11-07T16:21:27.123
	 * @param dateObj	Object	日期对象
	 * @return			String
	 * @version 0.1/2005-11-7
	 */
	public static String dateFormatForClient(Object dateObj) {
		return dateFormatForClient(dateObj, 0);
	}

	/**
	 * 按照客户端日期格式转化日期，如：2005-11-07T16:21:27.123
	 * @param dateObj	Object	日期对象
	 * @param dateFormate	int	日期格式化模式   0：在日期和时间中间加个‘T’（C#） 1：不加‘T’（其他）
	 * @return			String
	 * @version 0.1/2005-11-7
	 */
	public static String dateFormatForClient(
		Object dateObj,
		int dateFormatType) {
		String date = dateFormat(dateObj, defaultTimestampPattern);
		if (dateFormatType == 0) {
			return date == null ? null : date.replace(' ', 'T');
			//c#要求在日期和时间中间加个“T”
		} else {
			return date;
		}
	}
	
	/**
	 * 将日期对象格式化成字符串
	 * @param    dateObj     日期对象 Date/Calendar
	 * @return   String      格式化的日期表示
	 */
	public static String dateFormat(Object dateObj) {
		return dateFormat(dateObj, null);
	}

	/**
	 * 将日期对象格式化
	 * @param    dateObj     日期对象 Date/Calendar
	 * @param    pattern     日期和时间的表示格式
	 * @return   String      格式化的日期表示
	 */
	public static String dateFormat(Object dateObj, String pattern) {
		SimpleDateFormat sdf =
			new SimpleDateFormat(
				(pattern == null) ? defaultDatePattern : pattern,
				Locale.CHINA);
		if (dateObj == null) {
			return null;
		} else if (dateObj instanceof java.util.Calendar) { //Calendar
			java.util.Date date = ((java.util.Calendar) dateObj).getTime();
			return sdf.format(date);
		} else if (dateObj instanceof java.sql.Date) {
			java.util.Date date = new Date(((java.sql.Date) dateObj).getTime());
			return sdf.format(date);
		} else if (dateObj instanceof java.sql.Time) {
			java.util.Date date = new Date(((java.sql.Time) dateObj).getTime());
			return sdf.format(date);
		} else if (dateObj instanceof Timestamp) {
			java.util.Date date =
				new Date(((java.sql.Timestamp) dateObj).getTime());
			sdf =
				new SimpleDateFormat(
					(pattern == null) ? defaultTimestampPattern : pattern,
					Locale.CHINA);
			return sdf.format(date);
		} else if (dateObj instanceof java.util.Date) { //date
			return sdf.format((java.util.Date) dateObj);
		} else {
			return null;
		}
	}
    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate)
      throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        System.out.println("converting '" + strDate + "' to date with mask '"  + aMask + "'");

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     * 
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
        	System.out.println("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * 
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
      throws ParseException {
        Date aDate = null;

        try {
            System.out.println("converting date with pattern: " + datePattern);

            aDate = convertStringToDate(datePattern, strDate);
        } catch (ParseException pe) {
        	 System.out.println("Could not convert '" + strDate + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());
                    
        }

        return aDate;
    }
    
    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateTimeToString(Date aDate) {
        return getDateTime(datetimePattern, aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * 
     * @throws ParseException
     */
    public static Date convertStringToDateTime(String strDate)
      throws ParseException {
        Date aDate = null;

        try {
        	 System.out.println("converting date with pattern: " + datetimePattern);
            aDate = convertStringToDate(datetimePattern, strDate);
        } catch (ParseException pe) {
        	 System.out.println("Could not convert '" + strDate + "' to a date, throwing exception");
            //pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());
                    
        }

        return aDate;
    }
    
    
    public static Date expiredToDate(int day)
    {
    	/* 
	    Date obj = new Date();
		int date = obj.getDate();
		obj.setDate(date + day);	
		return obj;
		*/
    	return(adjust(new Date(), 0, 0, day, 0, 0, 0));
	}
    
    
    public static Date adjust(Date date, int year, int month, int day, int hour, int minute, int second)
    {  

    	Calendar startDT = Calendar.getInstance();  
    	startDT.setTime(date);
    	
    	if(year != 0)
    		startDT.add(Calendar.YEAR, year);
    	
    	if(month != 0)
    		startDT.add(Calendar.MONTH, month);
    	
    	if(day != 0)
    		startDT.add(Calendar.DAY_OF_MONTH, day);
    	
    	if(hour != 0)
    		startDT.add(Calendar.HOUR_OF_DAY, hour);
    	
    	if(minute != 0)
    		startDT.add(Calendar.MINUTE, minute);
    	
    	if(second != 0)
    		startDT.add(Calendar.SECOND, second);
    	
    	return(startDT.getTime());
    }
    
    
    public static int getYear(Date date)
    {
    	return(getField(date, Calendar.YEAR));
    }
    
    
    public static int getMonth(Date date)
    {
    	return(getField(date, Calendar.MONTH)+1);
    }
    
    
    public static int getDay(Date date)
    {
    	return(getField(date, Calendar.DATE));
    }
    
    
    public static int getHour(Date date)
    {
    	return(getField(date, Calendar.HOUR_OF_DAY));
    }
    
    public static int getMinute(Date date)
    {
    	return(getField(date, Calendar.MINUTE));
    }
    
    public static int getSecond(Date date)
    {
    	return(getField(date, Calendar.SECOND));
    }
    
    public static int getDayOfWeek(Date date)
    {
    	return(getField(date, Calendar.DAY_OF_WEEK));
    }
    
    public static int getDayOfYear(Date date)
    {
    	return(getField(date, Calendar.DAY_OF_YEAR));
    }
    
    private static int getField(Date date, int field)
    {
    	Calendar startDT = Calendar.getInstance();  
    	startDT.setTime(date);
    	return(startDT.get(field));
    }
    
    
    public static Date getDates(String year,String type ,String value){
    	int month=1;
    	if(type.equals("1")){
    		month=Integer.parseInt(value,10);
    	}else if(type.equals("2")){
    		if(value.equals("1")){
    			month=3;
    		}else if(value.equals("2")){
    			month=6;
    		}else if(value.equals("3")){
    			month=9;
    		}else if(value.equals("4")){
    			month=12;
    		}
    	}else if(type.equals("3")){
    		if(value.equals("1")){
    			month=6;
    		}else if(value.equals("2")){
    			month=12;
    		}
    	}else if(type.equals("4")){
    		month=12;
    	}
    	
        Date obj = new Date(Integer.parseInt(year,10)-1900,month,1);
        System.out.println(obj.toString());
    	int date = obj.getDate();
    	obj.setDate(date -1);
    	return obj;     
    	}
    
	/**
	 * 将时间戳转化成ORACLE的日期格式
	 * @param 	timestamp		String 日期字符串 		2005-01-01 12:10:11.123
	 * @return					String ORACLE格式日期	TO_TIMESTAMP('2005-01-01 12:10:11.123','yyyy-mm-dd hh24:mi:ssxff')
	 * @version 0.2/2005-12-20		
	 */
	public static String formatTimestamp(String timestamp) {
		if (timestamp == null)
			return null;
		else if (timestamp.indexOf('.') > 0) { //timestamp
			return "TO_TIMESTAMP('"
				+ timestamp
				+ "','yyyy-mm-dd hh24:mi:ssxff')";
		} else //date 
			return "TO_DATE('" + timestamp + "','yyyy-mm-dd hh24:mi:ss')";
	}
	public static Date toDate(String dateStr) {
		return toDate(dateStr, null);
	}

	
	/**
	 * 根据输入的日期字符串构造日期对象，并返回
	 * @param dateStr	String	日期字符串，如：2005-01-01 12:00:00.000
	 * @param pattern	String	日期格式
	 * @return			Date
	 */
	public static Date toDate(String dateStr, String pattern) {
		SimpleDateFormat sdf =
			new SimpleDateFormat(
				(pattern == null) ? defaultDatePattern : pattern,
				Locale.CHINA);
		Date dateReturn = null;
		try {
			dateReturn = sdf.parse(dateStr);
		} catch (ParseException e) {

		}
		return dateReturn;
	}

	/**
	 * 根据输入的日期字符串按照默认日期格式（yyyy-MM-dd HH:mm:ss），构造日历对象（Calendar）并返回
	 * @param dateStr	String	日期字符串，如：2005-01-01 12:00:00.000
	 * @return			Calendar	
	 */
	public static Calendar toCalendar(String dateStr) {
		return toCalendar(dateStr, null);
	}

	/**
	 * 根据输入的日期字符串构造日历对象（Calendar）并返回
	 * @param dateStr	String	日期字符串，如：2005-01-01 12:00:00.000
	 * @param pattern	String	日期格式
	 * @return
	 */
	public static Calendar toCalendar(String dateStr, String pattern) {
		Date date = toDate(dateStr, pattern);
		Calendar calendarReturn = Calendar.getInstance();
		calendarReturn.setTime(date);
		return calendarReturn;
	}
	
	/**
	 * 将日期转换为8位数字的字符串，格式为yyyyMMdd，如：20100421
	 */
	public static String getDateStringOfAllNumber(Date date){
		if(date == null){
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePatternWithAllNumber, Locale.CHINA);
		return sdf.format(date);
	}
	
	/**
	 * @param void
	 * @return 当前年
	 */
	public static final String getCurrentYear() {
		String dy = "";
		dy = dateFormat.format(new Date());
		dy = dy.substring(0, 4);
		return dy;
	}
	
	/**
	 * @param void
	 * @return 当前年
	 */
	public static final String getCurrentShortYear() {
		String dy = "";
		dy = dateFormat.format(new Date());
		dy = dy.substring(2, 4);
		return dy;
	}

	/**
	 * @param void
	 * @return 当前月
	 */
	public static final String getCurrentMonth() {
		String dm = "";
		dm = dateFormat.format(new Date());
		dm = dm.substring(5, 7);
		return dm;
	}

	/**
	 * @param void
	 * @return 当前日
	 */
	public static final String getCurrentDay() {
		String dd = "";
		dd = dateFormat.format(new Date());
		dd = dd.substring(8, 10);
		return dd;
	}
    
  public static void main(String [] args){  
    	//System.out.println(DateUtil.getDates("2008","1","1").toLocaleString());
    
    	}
        
}

    