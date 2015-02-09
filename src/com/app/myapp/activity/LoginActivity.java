package com.app.myapp.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapp.R;
import com.app.myapp.dao.UserDao;
import com.app.myapp.dao.impl.UserDaoImpl;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;
import com.app.myapp.util.JSONHelper;


public class LoginActivity extends Activity {

	//TAG name
	final String TAG = "com.android.androidapplogin.LoginActivity";
	//private UserDao userDao=new UserDaoImpl();
	
	public static final String MSG_SERVLET_ERROR="服务器错误!";
	public static final String MSG_LOGIN_ERROR="登陆出错!";
	
	public static String URI="";
	
	//private static ProgressDialog dialog;
	
	private Button btnLogin;
	private TextView btnReset;
	private EditText editUsername,editPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		URI=(String)LoginActivity.this.getString(R.string.struts_uri);
		Log.i(TAG,URI);
		//控件
		findViews();
		//事件
		setListensers();
		//获得本地登录信息
		getFromLocal();
	}

	//查找Views
	private void findViews()
	{
		btnLogin = (Button)this.findViewById(R.id.btnSubmit);
		btnReset = (TextView)this.findViewById(R.id.textRegister);
		//用户名+密码
		editUsername = (EditText)this.findViewById(R.id.loginName);
		editPassword = (EditText)this.findViewById(R.id.pwd);
	}
	
	//监控
	private void setListensers()
	{
		btnLogin.setOnClickListener(btnClick);
		btnReset.setOnClickListener(btnClick);
	}
	
	//btnClick 事件
	private OnClickListener btnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			//根据id区分操作
			switch(v.getId()){
				case  R.id.btnSubmit:
					//登录验证
					Login();
					break;
					
				case  R.id.textRegister:
					//注册
					//Login(name,password);
					Regist();
					break;
				default:
					break;
			}
		}
	};
	
	//登录
	private void Login(){
		//用户名密码
		String name = editUsername.getText().toString();
		String password =editPassword.getText().toString();
		
		//加载数据 默认降序
		new GetDataTask().execute(new String[]{name,password});
	}
	
	//注册
	private void Regist(){
		//跳转
		Intent _intent=new Intent();
		_intent.setClass(LoginActivity.this, RegistActivity.class);
		startActivity(_intent);
	}

	 /*
		 * Params, Progress, Result
		 * 
		 * 三种泛型类型分别代表
		 * 		1.“启动任务执行的输入参数”、
		 * 		2.“后台任务执行的进度”、
		 * 		3.“后台计算结果的类型”。
		 * 在特定场合下，并不是所有类型都被使用，如果没有被使用，可以用java.lang.Void类型代替。
		 * 
		 */
		private class GetDataTask extends AsyncTask<String, Void, HttpResult> {
			
			private ProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				Log.i(TAG,"onPreExecute()------------------------------isFirst:");
					if (dialog == null || !dialog.isShowing())
					{
						dialog = new ProgressDialog(LoginActivity.this);
					}
					dialog = new ProgressDialog(LoginActivity.this);
					dialog.setTitle("请等待");
					dialog.setMessage("登陆中...");
					//dialog.setIndeterminate(true);
					dialog.setCancelable(false);
					dialog.show();
			}
			
			@Override
			protected HttpResult doInBackground(String... params) {
				// Simulates a background job.
				HttpResult result = new HttpResult();
				String url ="";
				try {
					//Thread.sleep(1500);
					Log.i(TAG,"doInBackground()-----------------------------------------");
					//get请求
					//url =getString(R.string.struts_uri)+"/com.app.content/Login.action?loginName="+params[0]+"&loginPassword="+params[1];
					//result = HttpRequest.request(url);
					
					//post请求
					url =getString(R.string.struts_uri)+"/com.app.content/login.action";
					Map<String, String> loginMap=new HashMap<String, String>();
					
					//普通 参数
					//loginMap.put("loginName", params[0]);
					//loginMap.put("loginPassword", params[1]);
					
					//bean
					loginMap.put("bean.LoginName", params[0]);
					loginMap.put("bean.Password", params[1]);
					
					result = HttpRequest.post(url, loginMap);
					
				} catch (Exception e) {
					Log.e(TAG,e.toString());
					return result;
				}
				return result;
			}

			@Override
			protected void onPostExecute(HttpResult result) {
				Log.i(TAG,"onPostExecute()");
				User Userbean=new User();
				//loading
				 if (dialog != null && dialog.isShowing())
				 {
					 dialog.dismiss();
				 }
				 if(result == null){
					//服务器错误
					 showToast(MSG_SERVLET_ERROR);
				 }else {
					 try {
						 JSONObject resObject = result.GetReturnValue();
						 
						 if(resObject ==null){
							 //服务器错误
							 showToast(MSG_SERVLET_ERROR);
						 }else if(result.GetUserSuccessed()){
							 
							//跳转
							Intent _intent=new Intent();
							_intent.setClass(LoginActivity.this, MainActivity.class);
							startActivity(_intent);
							//销毁当前Activity
							finish();
							/* String reString = resObject.getString("Items");
							 
							 if (!Helper.isEmpty(reString)) 
							 {
								//将json 转换对象
								// reString=reString.replace("\"","\'");
								Userbean = (User)JSONHelper.parseObject(reString,  User.class);
								if(Userbean !=null){
									//保存到本地
									Userbean.save2Local();
									//成功
									showToast(resObject.getString("msg"));
									//跳转
									Intent _intent=new Intent();
									_intent.setClass(LoginActivity.this, MainActivity.class);
									startActivity(_intent);
								}else {
									//登录异常
									showToast(MSG_LOGIN_ERROR);
								} 
							 }*/
						}else {
							if(resObject.getString("msg").equals("")){
								showToast(resObject.getString("msg"));
							}else {
								showToast(result.getError());
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						//登录异常
						showToast(MSG_LOGIN_ERROR);
					}
				}
				super.onPostExecute(result);
			}
		}
	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	///Toast 组件  
	private void showToast(String message)
	{
		Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
	}
	
	//获取用户登   
	private void getFromLocal() {
		User bean=new User();
		bean = bean.getFromLocal();
		//getFromLocal
		if(bean!=null){
			Log.e(TAG, "user-------------------------------------:"+bean.getLoginName());
			editUsername.setText(bean.getLoginName());
		}else {
			Log.e(TAG, "**********无记录");
		}
	}

}
