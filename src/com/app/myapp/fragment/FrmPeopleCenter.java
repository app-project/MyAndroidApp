package com.app.myapp.fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapp.R;
import com.app.myapp.activity.LoginActivity;
import com.app.myapp.activity.UserDetailActivity;
import com.app.myapp.adapter.listAdapterThree;
import com.app.myapp.dao.UserDao;
import com.app.myapp.dao.impl.UserDaoImpl;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;
import com.app.myapp.model.User;
import com.app.myapp.util.DateUtil;
import com.app.myapp.util.Helper;
import com.app.myapp.util.JSONHelper;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import android.app.Fragment;

public class FrmPeopleCenter extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FrmPeopleCenter";
	
	private TextView userinfotb,userddtb,usersctb;
	private ImageButton btn_back;
	//Activity
	private Activity mActivity;
	
	private List<User> listUser;
	private listAdapterThree mAdapter;
	private Boolean isFirst=true;
	private View newsLayout;
	private UserDao userDao;
	private User user;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
        mActivity =this.getActivity();
        
        user = User.checkLogin();
        //验证用户登录是否有效
        if(user == null){
        	Intent intent=new Intent();
        	intent.setClass(mActivity, LoginActivity.class);
        	mActivity.startActivity(intent);
        	return newsLayout;
		}
        newsLayout = inflater.inflate(R.layout.activity_usercenter, container, false);  
        
        //list控件
        mfindViews();
        //绑定事件
        setClick();
     
        return newsLayout;  
    }
	//查找元素
	private void mfindViews(){
		userinfotb = (TextView)newsLayout.findViewById(R.id.userinfotb);
		userddtb = (TextView)newsLayout.findViewById(R.id.userddtb);
		usersctb = (TextView)newsLayout.findViewById(R.id.usersctb);
		btn_back = (ImageButton)newsLayout.findViewById(R.id.btn_back);
	}
	//设置事件
	private void setClick(){
		userinfotb.setOnClickListener(btnClick);
		userddtb.setOnClickListener(btnClick);
		usersctb.setOnClickListener(btnClick);
	}
	
	//btnClick 监听
	private OnClickListener btnClick=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent _i=new Intent();
			switch(v.getId()){
				case  R.id.userinfotb:
					//个人中心
					
					_i.setClass(mActivity, UserDetailActivity.class);
					startActivity(_i);
					break;
				
				case  R.id.userddtb:
					//我的订单
					_i.setClass(mActivity, LoginActivity.class);
					startActivity(_i);
					break;
					
				case  R.id.usersctb:
					//我的收藏
					_i.setClass(mActivity, LoginActivity.class);
					startActivity(_i);
					break;
					
				default:
					break;
			
			}
		}
	};

}
