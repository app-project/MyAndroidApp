package com.app.myapp.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.myapp.R;
import com.app.myapp.fragment.FmMain;
import com.app.myapp.fragment.FragmentMain2;
import com.app.myapp.fragment.FragmentMain3;
import com.app.myapp.fragment.FragmentMain4;

//public class MainActivity extends Activity implements OnClickListener {
public class MainActivity_fragment_Bak extends Activity  {
	
	
	//TAG
	private final String TAG = "com.app.androidapp.activity.MainActivity";
	
	//private TextView txtV;
	//底部操作
	private ImageView imageV1,imageV2,imageV3,imageV4;
	//记录当前
	private int InClick;
	private FragmentManager fragmentManager;
	
	//Fragment 不同操作的内容
	FmMain  fragment1;
	FragmentMain2  fragment2;
	FragmentMain3  fragment3;
	FragmentMain4  fragment4;
	
	//坐标
	private float startX = 0;  
    private float endX = 0;  
    private int touchSlop;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.i(TAG,"onCreate()");
		//获得管理fragment的管理类
		fragmentManager = this.getFragmentManager();
		//设置layout
		setContentView(R.layout.activity_main);
		//查找元素
		mfindViews();
		//设置事件
		setClick();
	}
	
	//查找元素
	private void mfindViews(){
		imageV1 =(ImageView) findViewById(R.id.ImageView01);
		imageV2 =(ImageView) findViewById(R.id.ImageView02);
		imageV3 =(ImageView) findViewById(R.id.ImageView03);
		imageV4 =(ImageView) findViewById(R.id.ImageView04);
	}
	//设置事件
	private void setClick(){
		imageV1.setOnClickListener(imageOnClick);
		imageV2.setOnClickListener(imageOnClick);
		imageV3.setOnClickListener(imageOnClick);
		imageV4.setOnClickListener(imageOnClick);
	}
	
	//事件
	private OnClickListener imageOnClick = new OnClickListener(){
		
		//@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//判断是否重复点击
			if(InClick == v.getId()){
				Log.i(TAG,"重复点击!");
				return ;
			}
			InClick =  v.getId();
			switch(v.getId()){
				case R.id.ImageView01:
					Log.i(TAG,v.getId()+"");
					setTabSelection(0);
					break;
				case R.id.ImageView02:
					Log.i(TAG,v.getId()+"");
					setTabSelection(1);
					break;
				case R.id.ImageView03:
					Log.i(TAG,v.getId()+"");
					setTabSelection(2);
					break;
				case R.id.ImageView04:
					Log.i(TAG,v.getId()+"");
					setTabSelection(3);
					break;
				default:
					Log.i(TAG,"default");
					break;
			}
		}
		
	};

	//setTabSelection  设置不同的Fragment
	private void setTabSelection(int index){
		Log.i(TAG,"setTabSelection:"+index);
		clearSelection();
		FragmentTransaction transaction=fragmentManager.beginTransaction();
		
		switch(index){
			case 0:
				if(fragment1  == null){
					fragment1 = new FmMain(); 
					//fragment1 =(FragmentMain) fragmentManager.findFragmentById(R.id.content);
				}
				// transaction.replace(R.id.content, fragment1);
				 imageV1.setImageResource(R.drawable.bottom_me_act);
				 //imageV1.setBackgroundColor(Color.parseColor("#00ffff"));
				break;
			case 1:
				if(fragment2  == null){
					fragment2 = new FragmentMain2();  
				}
				Log.i(TAG,"fragment2");
				//transaction.replace(R.id.content, fragment2);
				imageV2.setImageResource(R.drawable.bottom_me_act);
				//imageV2.setBackgroundColor(Color.parseColor("#00ffff"));
				break;
			case 2:
				if(fragment3  == null){
					fragment3 = new FragmentMain3();  
				}
				 //transaction.replace(R.id.content, fragment3);
				 imageV3.setImageResource(R.drawable.bottom_me_act);
				 //imageV3.setBackgroundColor(Color.parseColor("#00ffff"));
				break;
			case 3:
				if(fragment4  == null){
					fragment4 = new FragmentMain4();  
				}
				 //transaction.replace(R.id.content, fragment4);
				 imageV4.setImageResource(R.drawable.bottom_me_act);
				//imageV4.setBackgroundColor(Color.parseColor("#00ffff"));
				break; 
			default:
				break;
		}
		
		//是否将上次操作加入到回退栈中
		//transaction.addToBackStack(null); 
		 
		 //提交
	     transaction.commit(); 
	}
	//清空颜色
	private void clearSelection() {  
		/*
    		//btn_news.setTextColor(Color.parseColor("#000000"));
		imageV1.setBackgroundColor(Color.parseColor("#00ffff"));
		imageV2.setBackgroundColor(Color.parseColor("#00ffff"));
		imageV3.setBackgroundColor(Color.parseColor("#00ffff"));
		imageV4.setBackgroundColor(Color.parseColor("#00ffff"));
    	imageV1.setBackgroundColor(0);
    	imageV2.setBackgroundColor(0);
    	imageV3.setBackgroundColor(0);
    	imageV4.setBackgroundColor(0);
		 */
    		
		 imageV1.setImageResource(R.drawable.bottom);
		 imageV2.setImageResource(R.drawable.bottom);
		 imageV3.setImageResource(R.drawable.bottom);
		 imageV4.setImageResource(R.drawable.bottom);
    		
    }  
	 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		   switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	            startX = event.getX();  
	            break;  
	        case MotionEvent.ACTION_UP:  
	            endX = event.getX();  
	            break;  
	        default:  
	            break;  
	        }  
		
		   Log.i(TAG,"onTouchEvent----------------------startX===="+startX+"          endX===="+endX);
		return super.onTouchEvent(event);
	}
}
