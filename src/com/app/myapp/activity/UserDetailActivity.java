package com.app.myapp.activity;

import com.app.myapp.R;
import com.app.myapp.model.User;
import com.app.myapp.util.image.ImageManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class UserDetailActivity extends Activity {

	private User userDetail;
	private ImageView img;
	private TextView txtLoginName;
	private RadioButton male,female;
	private EditText uBirthday,uPhone,uMail,uAdress;
	private ImageButton btnback,btnsave;
	//TAG
	private final String TAG = "com.app.myapp.activity.UserDetailActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdetail);
		findsView();
		
		//获取Intent
		Intent _intent = this.getIntent();
		userDetail = (User) _intent.getSerializableExtra("userDetail");
		if (userDetail==null) {
			Log.e(TAG, "null");
		}else {
			Log.e(TAG,userDetail.getPic());
			Log.i(TAG,userDetail.getPic());
			Log.i(TAG,userDetail.getLoginName());
			
			//加载图片
			ImageManager.from(this).displayImage(img,userDetail.getPic(),0,100,100);
			txtLoginName.setText(userDetail.getLoginName());
			uBirthday.setText(userDetail.getBirthday().toString());
			uPhone.setText(userDetail.getPhone());
			uMail.setText(userDetail.getEmail());
			uAdress.setText(userDetail.getAddress());
			
		}
		setClick();
	}
	
	//查找元素
	private void findsView()
	{
		img = (ImageView) this.findViewById(R.id.imageView);
		txtLoginName=(TextView) this.findViewById(R.id.uNameId);
		uBirthday = (EditText)this.findViewById(R.id.inputDate);
		uPhone = (EditText)this.findViewById(R.id.uPhoneId);
		uMail = (EditText)this.findViewById(R.id.uMailId);
		uAdress = (EditText)this.findViewById(R.id.uAdressId);
		male = (RadioButton)this.findViewById(R.id.male);
		female = (RadioButton)this.findViewById(R.id.female);
		
		//退回
		btnback = (ImageButton) this.findViewById(R.id.btn_back);
		//保存
		btnsave = (ImageButton) this.findViewById(R.id.btn_save);
	}
	
	//设置事件
	private void setClick()
	{
		btnback.setOnClickListener(onclick);
		btnsave.setOnClickListener(onclick);
	}
	//事件处理
	private OnClickListener onclick=new OnClickListener () {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_save:
				SaveUser();
				break;
			default:
				break;
			}
		}
	};
	
	//保存
	private void SaveUser()
	{
		//加载数据 默认降序
		//new GetDataTask().execute(new String[]{uname,uage,uphone,umail,uadress,umale,ufemale});
	}
}
