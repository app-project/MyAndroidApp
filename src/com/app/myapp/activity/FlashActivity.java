package com.app.myapp.activity;

import com.app.myapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
/*
 * 欢迎界面
 * */
public class FlashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flash);
		
		/*try {
			Thread.sleep(9000);
			//跳转页面
			Intent intent=new Intent(FlashActivity.this,MainActivity.class);  
	        startActivity(intent);  
	        FlashActivity.this.finish();  
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
}
