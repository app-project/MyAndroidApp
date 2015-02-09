package com.app.myapp.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import com.app.myapp.R;
import com.app.myapp.app.MyApplication;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;
import com.app.myapp.util.JSONHelper;
import com.app.myapp.util.Network;

import android.util.Log;

public class HttpRequest {
	private static int TimeoutConnection = 12000;
	private static int TimeoutSocket = 12000;
	private static String TAG = "HttpRequest";

	public static HttpResult request(String url) {
		Log.i(TAG,"request:" + url);
		
		//用户id
		String userid = "";
		//密码版本
		String PwdVersion = "";
		//请求来源
		String source = "Android";
		
		//用户对象
		User info = new User();
		//获得本地用户对象
		info = info.getFromLocal();
		
		if(info!= null)
		{
			userid = String.valueOf(info.getId());
			PwdVersion = String.valueOf(info.getPwdversion());
		}
		HttpResult result = new HttpResult();
		result.SetResult(-1);
		
		//网络判断
		if (!Network.hasNetwork()) {
			result.SetResult(-1);
			result.SetError(MyApplication.getInstance().getString(R.string.no_network));
			
			Log.e(TAG, "request network error!");
			return result;
		}
		
		StringBuilder builder = new StringBuilder();
		try {
			JSONObject obj = null;
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = TimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = TimeoutSocket;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpGet get = new HttpGet(url);
			//设置header信息
			if(userid.length()>0)
			{
				get.setHeader("UserID", userid);
			}
			if(PwdVersion.length()>0)
			{
				get.setHeader("PwdVersion", PwdVersion);
			}
			
			get.setHeader("EiSource", source);
			get.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
			HttpResponse response = httpClient.execute(get);
			int int_result = response.getStatusLine().getStatusCode();
			if (int_result != 200) {
				Log.i(TAG,"request error: " + int_result );
				result.SetResult(-1);
				return result;
			}
			InputStreamReader inputStreamReader = new InputStreamReader(
					response.getEntity().getContent());
			BufferedReader reader = new BufferedReader(inputStreamReader);
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			reader.close();
			inputStreamReader.close();
			
			Log.i(TAG,"request result:" +  builder.toString());
			
			obj = new JSONObject(builder.toString());
			
			/*
			 * 只要用户验证成功 就更新本地用户信息
			 * */
			//更新本地用户信息
			 save2localUser(obj);
			
			result.SetResult(1);
			result.SetReturnValue(obj);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, e.getClass().toString() + "," + e.getMessage() + ","
					+ e.getStackTrace().toString());
			result.SetResult(-1);
//			result.SetError(KgApp.getInstance().getString(R.string.sys_fail)
//					+ "," + e.getMessage());
		}

		return result;
	}
	
	//Map<key,value>
	
	
	public static HttpResult post(String url,Map<String,String> params) {
		Log.i(TAG,"post:" + url);
		
		//用户id
		String userid = "";
		//密码版本
		String PwdVersion = "";
		//请求来源
		String source = "Android";
		
		//用户对象
		User info = new User();
		//获得本地用户对象
		info = info.getFromLocal();
		
		if(info!= null)
		{
			userid = String.valueOf(info.getId());
			PwdVersion = String.valueOf(info.getPwdversion());
		}
		 
		HttpResult result = new HttpResult();
		result.SetResult(-1);
		//网络判断
		if (!Network.hasNetwork()) {
			result.SetResult(-1);
			result.SetError(MyApplication.getInstance().getString(R.string.no_network));
			
			Log.e(TAG, "post network error!");
			
			return result;
		}
		
		StringBuilder builder = new StringBuilder();
		try {
			JSONObject obj = null;
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = TimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = TimeoutSocket;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			
			if(params!= null)
			{
				List <NameValuePair> netparams=new ArrayList<NameValuePair>();
				for (String key : params.keySet()) {

					netparams.add(new BasicNameValuePair(key,params.get(key)));

				}
				post.setEntity(new UrlEncodedFormEntity(netparams,HTTP.UTF_8));
			}
			
			//设置header信息
			if(userid.length()>0)
			{
				post.setHeader("UserID", userid);
			}
			if(PwdVersion.length()>0)
			{
				post.setHeader("PwdVersion", PwdVersion);
			}
			
			post.setHeader("EiSource", "android");
			
			post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
			HttpResponse response = httpClient.execute(post);
			int int_result = response.getStatusLine().getStatusCode();
			if (int_result != 200) {
				Log.i(TAG, "post error: " + int_result );
				result.SetResult(-1);
				return result;
			}
			InputStreamReader inputStreamReader = new InputStreamReader(
					response.getEntity().getContent());
			BufferedReader reader = new BufferedReader(inputStreamReader);
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			reader.close();
			inputStreamReader.close();
			
			Log.i(TAG, "post result:" +  builder.toString());
		
			obj = new JSONObject(builder.toString());
			
			/*
			 * 只要用户验证成功 就更新本地用户信息
			 * */
			//更新本地用户信息
			 save2localUser(obj);
			
			result.SetResult(1);
			result.SetReturnValue(obj);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, e.getClass().toString() + "," + e.getMessage() + ","
					+ e.getStackTrace().toString());
			result.SetResult(-1);
		}

		return result;
	}

	public static HttpResult upload(String url,String path) {
		Log.i(TAG,"post" + url);
		
//		UserInfo info = new UserInfo();
//		info = info.getFromLocal();
//		String userid = "";
//		String usertoken = "";
//		String source = "Android";
//		if(info!= null)
//		{
//			userid = info.UserID;
//			usertoken = StrHelper.decryptToken(info.UserToken);
//		}
		
		HttpResult result = new HttpResult();
		result.SetResult(-1);
//		if (!Network.hasNetwork()) {
//			result.SetResult(-1);
//			result.SetError(KgApp.getInstance().getString(R.string.no_network));
//			
//			Loger.i( "post network error!");
//			
//			return result;
//		}
		StringBuilder builder = new StringBuilder();
		try {
			JSONObject obj = null;
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = TimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = TimeoutSocket;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
//			if(userid.length()>0)
//				post.setHeader(MyUserID", userid);
//			if(usertoken.length()>0)
//				post.setHeader("MyUserToken", usertoken);
//			post.setHeader("MySource", source);
			post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
			
			 File file = new File(path);
			 MultipartEntity mpEntity = new MultipartEntity();
			 ContentBody cbFile = new FileBody(file);
			 mpEntity.addPart("userfile", cbFile);
			 post.setEntity(mpEntity);
			
			HttpResponse response = httpClient.execute(post);
			int int_result = response.getStatusLine().getStatusCode();
			if (int_result != 200) {
				Log.i(TAG,"post error: " + int_result );
				result.SetResult(-1);
//				result.SetError(KgApp.getInstance()
//						.getString(R.string.sys_fail));
				return result;
			}
			InputStreamReader inputStreamReader = new InputStreamReader(
					response.getEntity().getContent());
			BufferedReader reader = new BufferedReader(inputStreamReader);
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			reader.close();
			inputStreamReader.close();
			
			Log.i(TAG,"post result:" +  builder.toString());
		
			//Save2Local(builder.toString(),url);
			obj = new JSONObject(builder.toString());
			
			result.SetResult(1);
			result.SetReturnValue(obj);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, e.getClass().toString() + "," + e.getMessage() + ","
					+ e.getStackTrace().toString());
			result.SetResult(-1);
//			result.SetError(KgApp.getInstance().getString(R.string.sys_fail)
//					+ "," + e.getMessage());
		}
		return result;
	}
	
	//保存到本地
	private static void save2localUser(JSONObject obj){
		boolean usersuccess;
		User Userbean=new User();
		if(obj!=null){
			try {
				//用户信息是否成功返回
				usersuccess = obj.getBoolean("usersuccess");
				if(usersuccess)
				{
					 String reString = obj.getString("user");
					 if (!Helper.isEmpty(reString)){
						 //清除之前信息
						 User.Clear();
						//reString=reString.replace("\"","\'");
						//Userbean = (User)JSONHelper.parseObject(reString,  User.class);
						//保存本地
						Userbean.save2Local(reString);
					 }
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	@SuppressWarnings("deprecation")
	private static boolean checkuserLastDate(User user)
	{
		Date dt=new Date();
		int Validity;
		Date Updatetime=null;
		//a.after(b)返回一个boolean，如果a的时间在b之后（不包括等于）返回true
		//b.before(a)返回一个boolean，如果b的时间在a之前（不包括等于）返回true
		try {
			Validity = user.getValidity();
			Updatetime = user.getUpdatetime();
			if(Validity!=0){
				Updatetime.setDate(Updatetime.getDate()+Validity);
				//如果当前时间在最后一次跟新时间前
				if(dt.before(Updatetime)){
					return true;
				}
			}else{
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG,e.toString());
			return false;
		}
		return false;
	}*/
}
