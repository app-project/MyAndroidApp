package com.app.myapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.myapp.R;
import com.app.myapp.dao.UserDao;
import com.app.myapp.dao.impl.UserDaoImpl;
import com.app.myapp.fragment.FmMain;
import com.app.myapp.fragment.FragmentMain2;
import com.app.myapp.fragment.FragmentMain3;
import com.app.myapp.fragment.FragmentMain4;
import com.app.myapp.fragment.FrmCart;
import com.app.myapp.fragment.FrmCigarette;
import com.app.myapp.fragment.FrmPeopleCenter;
import com.app.myapp.model.User;

//public class MainActivity extends Activity implements OnClickListener {
public class MainActivity extends FragmentActivity  {
	//TAG
	private final String TAG = "com.app.androidapp.activity.MainActivity";
	
	//底部操作
	private ImageView imageV1,imageV2,imageV3,imageV4;
	
	//记录当前操作
	private int InClick;
    
    //滑动切换
    private ViewPager viewPager;// 页卡内容
	private List<Fragment> views;// Tab页面列表
	private Fragment fragment1, frmCigarette, frmcart, fragment4,FPeopleCenter;// 各个页卡
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.i(TAG,"onCreate()");
		
		//获得管理fragment的管理类
		//fragmentManager = this.getFragmentManager();
		//设置layout
		setContentView(R.layout.activity_main);
		//查找元素
		mfindViews();
		//初始化fragment
		initViewPager();
		//设置事件
		setClick();
	}
	
	//用户登录 验证   -----测试
	private void checkLogin() {
		User bean=new User();
		bean = User.checkLogin();
		//验证登录
		if(bean!=null){
			Log.i(TAG, "user-------------------------------------:"+bean.getLoginName());
		}else {
			Log.e(TAG, "*******************************需要登录*********************************");
		}		
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
	
	// 初始化页卡内容
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		views = new ArrayList<Fragment>();
		fragment1 = new FmMain();
		frmCigarette = new FrmCigarette();
		//fragment3 = new FragmentMain3();
		frmcart = new FrmCart();
		fragment4 = new FragmentMain4();
		FPeopleCenter = new FrmPeopleCenter();
		
		views.add(fragment1);
		views.add(frmCigarette);
		views.add(frmcart);
		//views.add(fragment4);
		views.add(FPeopleCenter);
		
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(0);
		//viewPager.setOnPageChangeListener(new PageChangeLisener());
	}
	
	class ViewPagerAdapter extends FragmentPagerAdapter {

		/* (non-Javadoc)
		 * @see android.support.v4.app.FragmentPagerAdapter#finishUpdate(android.view.ViewGroup)
		 */
		@Override
		public void finishUpdate(ViewGroup container) {
			
			Log.i(TAG, "viewPager.getCurrentItem:"+viewPager.getCurrentItem());
			//恢复图片标记
			clearSelection();
			//设置图片背景
			switch(viewPager.getCurrentItem()){
				case 0:
					imageV1.setImageResource(R.drawable.guide_home_on);
					break;
				case 1:
					imageV2.setImageResource(R.drawable.guide_discover_on);
					break;
				case 2:
					imageV3.setImageResource(R.drawable.guide_cart_on);
					break;
				case 3:
					imageV4.setImageResource(R.drawable.guide_account_on);
					break;
				default:
					Log.i(TAG,"default");
					break;
			}
			
			super.finishUpdate(container);
		}
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			views = MainActivity.this.views;
		}
		@Override
		public Fragment getItem(int arg0) {
			return views.get(arg0);
		}
		@Override
		public int getCount() {
			return views.size();
		}
	}
	
	//事件
	private OnClickListener imageOnClick = new OnClickListener(){
		
		//@Override
		public void onClick(View v) {
			//判断是否重复点击
			if(InClick == v.getId()){
				Log.i(TAG,"重复点击!");
				return ;
			}
			clearSelection();
			InClick =  v.getId();
			switch(v.getId()){
				case R.id.ImageView01:
					Log.i(TAG,v.getId()+"");
					viewPager.setCurrentItem(0);
					imageV1.setImageResource(R.drawable.guide_home_on);
					break;
				case R.id.ImageView02:
					Log.i(TAG,v.getId()+"");
					viewPager.setCurrentItem(1);
					imageV2.setImageResource(R.drawable.guide_discover_on);
					break;
				case R.id.ImageView03:
					Log.i(TAG,v.getId()+"");
					viewPager.setCurrentItem(2);
					imageV3.setImageResource(R.drawable.guide_cart_on);
					break;
				case R.id.ImageView04:
					Log.i(TAG,v.getId()+"");
					viewPager.setCurrentItem(3);
					imageV4.setImageResource(R.drawable.guide_account_on);
					break;
				default:
					Log.i(TAG,"default");
					break;
			}
		}
		
	};

	/*//setTabSelection  设置不同的Fragment
	private void setTabSelection(int index){
		Log.i(TAG,"setTabSelection:"+index);
		clearSelection();
		FragmentTransaction transaction=fragmentManager.beginTransaction();
		
		switch(index){
			case 0:
				if(fragment1  == null){
					fragment1 = new FragmentMain1(); 
					//fragment1 =(FragmentMain) fragmentManager.findFragmentById(R.id.content);
				}
				 transaction.replace(R.id.content, fragment1);
				 imageV1.setImageResource(R.drawable.bottom_me_act);
				 //imageV1.setBackgroundColor(Color.parseColor("#00ffff"));
				break;
			case 1:
				if(fragment2  == null){
					fragment2 = new FragmentMain2();  
				}
				Log.i(TAG,"fragment2");
				transaction.replace(R.id.content, fragment2);
				imageV2.setImageResource(R.drawable.bottom_me_act);
				//imageV2.setBackgroundColor(Color.parseColor("#00ffff"));
				break;
			case 2:
				if(fragment3  == null){
					fragment3 = new FragmentMain3();  
				}
				 transaction.replace(R.id.content, fragment3);
				 imageV3.setImageResource(R.drawable.bottom_me_act);
				 //imageV3.setBackgroundColor(Color.parseColor("#00ffff"));
				break;
			case 3:
				if(fragment4  == null){
					fragment4 = new FragmentMain4();  
				}
				 transaction.replace(R.id.content, fragment4);
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
	}*/
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
    		
		 imageV1.setImageResource(R.drawable.bt_menu_0_select);
		 imageV2.setImageResource(R.drawable.bt_menu_2_select);
		 imageV3.setImageResource(R.drawable.bt_menu_3_select);
		 imageV4.setImageResource(R.drawable.bt_menu_4_select);
    }  
	 
}
