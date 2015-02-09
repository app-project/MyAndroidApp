package com.app.myapp.activity;

import java.util.List;

import com.app.myapp.R;
import com.app.myapp.dao.CartInfoDao;
import com.app.myapp.dao.impl.CartInfoDaoImpl;
import com.app.myapp.model.CartInfo;
import com.app.myapp.model.Cigarette;
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
import android.widget.TextView;
import android.widget.Toast;

public class CigaretteDetailActivity extends Activity {

	private Cigarette cigaretteDetail;
	private ImageView img;
	private TextView txtPrice,txtName;
	private ImageButton btnback;
	private Button btnSubtract,btnAdd,btnAddCar;
	private EditText txtNum;
	private Integer num;
	
	private CartInfoDao cartinfoDao;
	//TAG
	private final String TAG = "com.app.myapp.activity.CigaretteDetailActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cigarettedetail);
		
		//实例化DAO
		cartinfoDao= new CartInfoDaoImpl(this);
		print();
		//查找元素
		findsView();
		
		//获取Intent
		Intent _intent = this.getIntent();
		cigaretteDetail = (Cigarette) _intent.getSerializableExtra("cigaretteDetail");
		if (cigaretteDetail==null) {
			Log.e(TAG, "null");
		}else {
			Log.i(TAG,cigaretteDetail.getPic());
			Log.i(TAG,cigaretteDetail.getName());
			
			//加载图片
			ImageManager.from(this).displayImage(img,cigaretteDetail.getPic(),0,100,100);
			txtName.setText(cigaretteDetail.getName());
			txtPrice.setText(String.valueOf(cigaretteDetail.getPrice()));
		}
		setClick();
		
	}	
	//查找元素
	private void findsView()
	{
		img = (ImageView) this.findViewById(R.id.imageView);
		txtPrice=(TextView) this.findViewById(R.id.txtPrice);
		txtName=(TextView) this.findViewById(R.id.txtName);
		//退回
		btnback = (ImageButton) this.findViewById(R.id.btn_back);
		
		//减数量
		btnSubtract = (Button) this.findViewById(R.id.btnSubtract);
		//增加数量
		btnAdd = (Button) this.findViewById(R.id.btnAdd);
		//加入购物车
		btnAddCar = (Button) this.findViewById(R.id.btnAddCar);
		
		//数量
		txtNum = (EditText) this.findViewById(R.id.txtNum);
	}
	
	//设置事件
	private void setClick()
	{
		btnback.setOnClickListener(onclick);
		btnAdd.setOnClickListener(onclick);
		btnSubtract.setOnClickListener(onclick);
		btnAddCar.setOnClickListener(onclick);
	}
	//事件处理
	private OnClickListener onclick=new OnClickListener () {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.btnAdd:
				num = Integer.valueOf(txtNum.getText().toString());
				Log.e(TAG, "num:"+num);
				txtNum.setText(String.valueOf(num+1));
				break;
			case R.id.btnSubtract:
				num = Integer.valueOf(txtNum.getText().toString());
				Log.e(TAG,"num:"+num);
				if(num >1){
					txtNum.setText(String.valueOf(num-1));
				}
				break;
			case R.id.btnAddCar:
				AddCar();
				break;
			default:
				break;
			}
		}
	};
	//加入购物车
	private void AddCar()
	{
		String message="成功添加至购物车!";
		CartInfo cartInfo; 
		num = Integer.valueOf(txtNum.getText().toString());
		if(num>0)
		{
			cartInfo = cartinfoDao.checkCartInfo("id", String.valueOf(cigaretteDetail.getId()));
			if(cartInfo ==null)
			{
				cartInfo=new CartInfo();
				cartInfo.setId(cigaretteDetail.getId());
				cartInfo.setName(cigaretteDetail.getName());
				cartInfo.setNum(num);
				cartInfo.setPic(cigaretteDetail.getPic());
				cartInfo.setPrice(cigaretteDetail.getPrice());
				
				//插入
				cartinfoDao.insert(cartInfo);
			}else {
				message = "购物车中已存在,请到购物车中修改!";
			}
			
		}else {
			message = "数量有误,请重新输入!";
		}
		//输出提示
		showToast(message);
	}
	
	private void print()
	{
		CartInfo cart;
		List<CartInfo> list=cartinfoDao.getAllCartInfo("id", "1");
		for(int i=0;i<list.size();i++)
		{
			cart = list.get(i);
					
			Log.i(TAG, "id:"+cart.getId()+ "             name:"+cart.getName());
		}
	}
	
	
	//提示信息
	private void showToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
}
