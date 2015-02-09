package com.app.myapp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.os.Environment;

public class Helper
{
	
	public static String getString(String str, int len) {
		int strLen = str.length();
		if (strLen > len) {
			return str.substring(0, len) + "...";
		}
		return str;
	}

	public static String subString(String text, int length, String endWith) {
		int textLength = text.length();
		int byteLength = 0;
		StringBuffer returnStr = new StringBuffer();
		for (int i = 0; i < textLength && byteLength < length * 2; i++) {
			String str_i = text.substring(i, i + 1);
			if (str_i.getBytes().length == 1) {
				byteLength++;
			} else {
				byteLength += 2;
			}
			returnStr.append(str_i);
		}
		try {
			if (byteLength < text.getBytes("GBK").length) 
			{
				returnStr.append(endWith);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnStr.toString();
	}
	
	
	public static Date strToDate(String input)
	{
        //String date = "2010-10-1 12:22:30";  
        Date d = null;  
        try {  
        	SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");           
            d = format.parse(input);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            d = new Date();
        }
        return d;
	}
	
	public static boolean isString(String str) {
		Pattern pattern = Pattern.compile("[a-z A-Z]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0 || s.equals("[]");
	}

	public static boolean isMoreChar(String s, int len) {
		return s.trim().length() >= len;
	}

	public static boolean isLessChar(String s, int len) {
		return s.trim().length() < len;
	}

	public static boolean isValidEmail(String email) {
		if (isEmpty(email)) {
			return false;
		}
		boolean matched = Pattern
				.matches(
						"[a-zA-Z_\\.]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}",
						email);
		return matched;
	}

	public static boolean isEquals(String s1, String s2) {
		return s1.equals(s1);
	}


	public static boolean isValidMobiNumber(String number) {
		if (isEmpty(number)) {
			return false;
		}
		boolean matched = Pattern.matches("1\\d{10}", number);
		return matched;
	}

	public static boolean isNotEmpty(String s) {
		return s != null && s.length() > 0;
	}


	public static String getLocalTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static String subStringTime(String str) {
		String timeM = str.substring(6, 7);
		String month = "月";
		String day = "日";
		String timeD = str.substring(8, 10);
		String endMinute = str.substring(11, 16);
		String result = timeM + month + timeD + day + endMinute;
		return result;
	}


	public static long getDaynumber(String firstdate, String enddate) {
		long daynumber = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date first = sdf.parse(firstdate);
			Date end = sdf.parse(enddate);
			daynumber = end.getTime() - first.getTime();
			daynumber = daynumber / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return daynumber;
	}

	public static String ChangeTime(int time) {
		String hour = time / 3600 + "";
		int b = time % 3600;
		String fen = b / 60 + "";
		String miao = b % 60 + "";
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		if (fen.length() == 1) {
			fen = "0" + fen;
		}
		if (miao.length() == 1) {

		}
		String aa = "";
		if (!"00".equals(hour)) {
			aa = hour + "ʱ" + fen + "分" + miao + "秒";
		} else if (!"00".equals(fen)) {
			aa = fen + "分" + miao + "秒";
		} else {
			aa = miao + "秒";
		}

		return aa;
	}

	public static List<JSONObject> getList(JSONArray jsonArray) {
		List<JSONObject> list = null;
		try {
			JSONObject jsonObject;
			list = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				list.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {

			int i = 0xFF & b;
			if (i < 0x10) {
				hexString.append("0" + Integer.toHexString(i));
			} else {
				hexString.append(Integer.toHexString(i));
			}
		}
		return hexString.toString();
	}

	public static String md5(String s) {
		try {

			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static double getDistance(double lat1, double lon1, double lat2,
			double lon2) {
		float[] results = new float[1];
		Location.distanceBetween(lat1, lon1, lat2, lon2, results);
		return results[0];
	}

	public static boolean hasSDCard() {
		String t = android.os.Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(t);
	}

}