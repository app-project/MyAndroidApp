package com.app.myapp.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.app.myapp.R;
import com.app.myapp.activity.LoginActivity;
import com.app.myapp.adapter.listAdapterCartInfo;
import com.app.myapp.dao.CartInfoDao;
import com.app.myapp.dao.impl.CartInfoDaoImpl;
import com.app.myapp.model.CartInfo;
import com.app.myapp.model.User;

public class FrmCart extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FrmCart";
	//Activity
	private Activity mActivity;
	
	private List<CartInfo> listCartInfo;
	private listAdapterCartInfo mAdapter;
	private View newsLayout;
	private ListView listView;
	
	private CartInfoDao cartinfoDao;
	private static ProgressDialog dialog;
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
        mActivity =this.getActivity();
        
        //验证用户登录是否有效
        if(User.checkLogin()==null){
        	Intent intent=new Intent();
        	intent.setClass(mActivity, LoginActivity.class);
        	mActivity.startActivity(intent);
        	return newsLayout;
		}
        
        newsLayout = inflater.inflate(R.layout.frm_cartinfo, container, false);  
        //list控件
        mfindViews();
        //绑定事件
        setClick();
        
        cartinfoDao= new CartInfoDaoImpl(mActivity);
        
        //new 数据源list
        listCartInfo =new ArrayList<CartInfo>();
        
		
		//Adapter列表
		mAdapter = new listAdapterCartInfo(mActivity);
		
		mAdapter.setData(listCartInfo);
		
		//数据绑定
		listView.setAdapter(mAdapter);
		
		loadCartInfo();
		//加载数据 默认降序
        return newsLayout;  
    }
	//查找元素
	private void mfindViews(){
		//获得listView
		listView = (ListView) newsLayout.findViewById(R.id.listCigarette);
	}
	//设置事件
	private void setClick(){
		
	}
	
	 //加载数据
	 private void loadCartInfo()
	 {
		 Log.e(TAG, "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		 //loading
			if(dialog==null)
			{
				dialog=new ProgressDialog(mActivity);
			}
			dialog.setTitle("cart");
			dialog.setMessage("加载中...");
			dialog.setCancelable(false);
			dialog.show();
		 
		 //副线程
		 Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<CartInfo> list =new ArrayList<CartInfo>();
				try {
					//业务逻辑层
					 list = cartinfoDao.getAllCartInfo("","");
					 Log.e(TAG, "--------------------------------------------------------------list.size:"+list.size());
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					FrmCart.this.handler.sendEmptyMessage(0);
				}
				if(list.size()>0)
				{
					FrmCart.this.handler.sendEmptyMessage(1);
				}else {
					FrmCart.this.handler.sendEmptyMessage(0);
				}
				listCartInfo.clear();
				listCartInfo=list;
			}
		});
		//启动线程
		thread.start();
	 }
	 
	 
	//Handler
	private static class IHandler extends Handler{

		private final WeakReference<Fragment> mFrmCart;
		
		public IHandler (Fragment frmCart){
			mFrmCart=new WeakReference<Fragment>(frmCart);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//关闭 loading
			if(dialog!=null)
			{
				dialog.dismiss();
			}			
			switch(msg.what){
				case 1:
					//((FrmCart) mFrmCart.get()).showToast("");
					break;
				default:
					break;
			}
			((FrmCart) mFrmCart.get()).mAdapter.setData(((FrmCart) mFrmCart.get()).listCartInfo);
			//刷新Listview
			((FrmCart) mFrmCart.get()).mAdapter.notifyDataSetChanged();
			((FrmCart) mFrmCart.get()).listView.refreshDrawableState();
		}
	}
		//IHandler持有LoginActivity对象
	private IHandler handler=new IHandler(this);
	 
	 
	 
	/* 
	//输出sqlite对象
	private void printUserSqlite(){
		//userDao= new UserDaoImpl(mActivity);
		//Cursor cursor= dbrade.rawQuery("select * from userm where id =?", new String[]{"1"});
		//String sql="select * from userm where id =?";
		String sql="select * from Cigarette ";
		String[] selectionArgs=new String[]{};
		List<Cigarette> lis=cigaretteDao.rawQuery(sql, selectionArgs);
		Cigarette beanCigarette;
		for(int i=0;i<lis.size();i++){
			beanCigarette=new Cigarette();
			beanCigarette = lis.get(i);
			System.out.println("loginName:---------------------:"+beanCigarette.getName());
		}
	}*/
	 
	
	//提示信息
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
}
