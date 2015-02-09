package com.app.myapp.app;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

public class MyApplication extends Application{
	private static String PREFS_NAME = "com.app.myapp";
	
	private static MyApplication instance;
	Context mContext;
	private Activity mainActivity;
	
	private HttpClient httpClient;
	
	public void setTabMainActivity(Activity context) {
		this.mainActivity = context;
	}
	
	public Activity getTabMainActivity() {
		return this.mainActivity;
	}
	

	public static MyApplication getInstance() {
		return instance;
	}
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		Log.i("KgApp", "onCreate");
		super.onCreate();
		instance = this;
		mContext = this.getApplicationContext();
		httpClient = this.createHttpClient();
		super.onCreate();
	}

	public void writeString(String key, String value) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		SharedPreferences settings = getSharedPreferences(
				PREFS_NAME, 0);
		return settings.getString(key, "");
	}

	
	// 创建HttpClient实例
	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https",SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(connMgr, params);
	}
		
	// 关闭连接管理器并释放资源
	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	// 对外提供HttpClient实例
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.shutdownHttpClient();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		this.shutdownHttpClient();
	}
}
