package com.app.myapp.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.myapp.R;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends Activity {
	
	final String TAG = "com.android.androidapplogin.RegistActivity";
	private Button btnRegist;
	private EditText editUsername,editPassword,editqrPassword;
	private ImageButton btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		//查找控件
		findViews();
		//点击事件监听
		setListensers();
		
	}
	
	//查找Views
	private void findViews()
	{
		btnRegist = (Button)this.findViewById(R.id.btnRegistId);
		editUsername = (EditText)this.findViewById(R.id.editUsername);
		editPassword = (EditText)this.findViewById(R.id.editPassword);
		editqrPassword = (EditText)this.findViewById(R.id.editqrPassword);
		btn_back = (ImageButton)this.findViewById(R.id.btn_back);
	}
	
	//监控
	private void setListensers()
	{
		btnRegist.setOnClickListener(btnClick);
		btn_back.setOnClickListener(btnClick);
	}
	
	//btnClick 事件
	private OnClickListener btnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name = editUsername.getText().toString();
			String password =editPassword.getText().toString();
			String qrpassword =editqrPassword.getText().toString();
			
			Intent _i=new Intent();
			//根据id区分操作
			switch(v.getId()){
				case  R.id.btn_back:
					//返回
					ReturnBcak();
					break;
					
				case  R.id.btnRegistId:
					if(checkReg(name,password,qrpassword)){
						//注册提交数据
						Regist(name,password);
					}
					break;
				default:
					break;
			
			}
		}
	};

	private Boolean checkReg(String loginName,String pwd,String qrpwd) {
		// TODO Auto-generated method stub
		//登录名
		if("".equals(loginName.trim()))
		{
			showToast("请输入登录名!");
			return false;
		}
		//密码
		if("".equals(pwd.trim()))
		{
			showToast("密码不能为空!");
			return false;
		}
		//确认密码
		if(pwd.trim().equals(qrpwd.trim()))
		{
			return true;
		}else{
			
			showToast("密码不一致!");
			return false;
		}
	}
	
	//注册
	private void Regist(final String name,final String password){
		//加载数据 默认降序
		new GetDataTask().execute(new String[]{name,password});
	}
	
	//返回
	private void ReturnBcak(){
		
		//跳转
		Intent _intent=new Intent();
		_intent.setClass(RegistActivity.this, LoginActivity.class);
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
					dialog = new ProgressDialog(RegistActivity.this);
				}
				dialog = new ProgressDialog(RegistActivity.this);
				dialog.setTitle("请等待");
				dialog.setMessage("正在提交信息...");
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
				
				//url = "http://www.ruiii.com/api/v6/Class/?parentid=&type=8";
				//url =getString(R.string.struts_uri)+"/com.app.content/Login.action?loginName="+params[0]+"&loginPassword="+params[1];
				//result = HttpRequest.request(url);
				
				//post请求
				url =getString(R.string.struts_uri)+"/com.app.content/register.action";
				Map<String, String> registMap=new HashMap<String, String>();
				
				//普通 参数
				//loginMap.put("loginName", params[0]);
				//loginMap.put("loginPassword", params[1]);
				
				//bean
				registMap.put("bean.LoginName", params[0]);
				registMap.put("bean.Password", params[1]);
				
				result = HttpRequest.post(url, registMap);
				
			} catch (Exception e) {
				Log.e(TAG,e.toString());
				return result;
			}
			return result;
		}

		@Override
		protected void onPostExecute(HttpResult result) {
			Log.i(TAG,"onPostExecute()");
			//loading
			 if (dialog != null && dialog.isShowing())
			 {
				 dialog.dismiss();
			 }
			 if(result == null){
				 showToast("服务器异常!");
			 }else {
				 JSONObject resObject = result.GetReturnValue();
				 try {
					 if(resObject ==null){
						 showToast("服务器异常!");
					 }else if(result.GetUserSuccessed()){
						//成功
						showToast(resObject.getString("msg"));
						//跳转
						Intent _intent=new Intent();
						_intent.setClass(RegistActivity.this, LoginActivity.class);
						startActivity(_intent);
					}else {
						showToast(resObject.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("注册异常!");
				}
				 
			}
			super.onPostExecute(result);
		}
	}
	
	///Toast 组件  
	private void showToast(String message)
	{
		Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
	}

}
