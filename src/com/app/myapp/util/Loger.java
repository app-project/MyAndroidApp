package com.app.myapp.util;

import android.util.Config;
import android.util.Log;

public class Loger {
	
	static boolean DEBUG = true;
	static String  TAG = "MyTag";
	
	public static void e(Exception e) {
		if(!DEBUG)
			return;
		
		Log.e("TAG", e.getClass().toString() + "," + e.getMessage() + ","
				+ e.getStackTrace().toString());
		e.printStackTrace();
	}
	
	public static void e(String tag,String error) {
		if(!DEBUG)
			return;
		
		Log.e(tag,error);
		//e.printStackTrace();
	}
	
	public static void i(String info) {
		if(!DEBUG)
			return;
		
		Log.i("TAG", info);
	}
	
	public static void i(String tag,String info) {
		if(!DEBUG)
			return;
		
		Log.i(tag, info);
	}
	
	public static void d(String tag, String info) {
		if(!DEBUG)
			return;
		
		Log.d(tag, info);
	}
}
