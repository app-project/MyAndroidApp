package com.app.myapp.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.app.myapp.R;
import com.app.myapp.activity.CigaretteDetailActivity;
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

public class FragmentMain3 extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FragmentMain3";
	//Activity
	private Activity mActivity;
	
	private PullToRefreshListView mPullToRefresh;
	private List<User> listUser;
	private listAdapterThree mAdapter;
	private Boolean isFirst=true;
	private View newsLayout;
	private UserDao userDao;
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
        
        newsLayout = inflater.inflate(R.layout.fragment_3, container, false);  
        //list控件
        mfindViews();
        //绑定事件
        setClick();
        
        userDao= new UserDaoImpl(mActivity);
        
        //new 数据源list
       // Simplelist = new ArrayList<Map<String,Object>>();
       //new 用户ArrayList
        listUser=new ArrayList<User>();
        
        /* 
         * Mode.BOTH：同时支持上拉下拉 
         * Mode.PULL_FROM_START：只支持下拉Pulling Down 
         * Mode.PULL_FROM_END：只支持上拉Pulling Up 
         */  
        /* 
         * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。  
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。 
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法， 
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.  
         */  
        mPullToRefresh.setMode(Mode.PULL_FROM_START);  
        init();
        /* 
         * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器； 
         * setOnLastItemVisibleListener(OnLastItemVisibleListener listener):设置是否到底部监听器； 
         * setOnPullEventListener(OnPullEventListener listener);设置事件监听器； 
         * onRefreshComplete()：设置刷新完成 
         */  
        /* 
         * pulltorefresh.setOnScrollListener() 
         */  
        // SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动      
        // SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）      
        // SCROLL_STATE_IDLE(0) 停止滚动         
        /* 
         * setOnLastItemVisibleListener 
         * 当用户拉到底时调用   
         */  
        /* 
         * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程 ，他的回调函数是onTouchEvent（MotionEvent event）, 
         * 然后通过判断event.getAction()是MotionEvent.ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。 
         * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为 
         */ 
		
		//获得RefreshableView
		ListView actualListView = mPullToRefresh.getRefreshableView();
		//Adapter列表
		mAdapter = new listAdapterThree(mActivity);
		
		mAdapter.setData(listUser);
		//SimpleAdapter
		//mAdapter=new SimpleAdapter(mActivity,Simplelist,R.layout.list_item3,new String[]{"num","name"},new int[]{R.id.txtList3User,R.id.txtList3Name});
		
		actualListView.setAdapter(mAdapter);
		//加载数据 默认降序
		//new GetDataTask().execute(new String[]{"desc","9"});
		//new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
		new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
		
        return newsLayout;  
    }
	//查找元素
	private void mfindViews(){
		mPullToRefresh = (PullToRefreshListView) newsLayout.findViewById(R.id.listView3);
	}
	//设置事件
	private void setClick(){
		mPullToRefresh.setOnRefreshListener(refreshListener);
		mPullToRefresh.setOnItemClickListener(mOnItemClickListener);
	}
	
	/* 
     * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器； 
     * setOnLastItemVisibleListener(OnLastItemVisibleListener listener):设置是否到底部监听器； 
     * setOnPullEventListener(OnPullEventListener listener);设置事件监听器； 
     * onRefreshComplete()：设置刷新完成 
     */  
    /* 
     * pulltorefresh.setOnScrollListener() 
     */  
    // SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动      
    // SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）      
    // SCROLL_STATE_IDLE(0) 停止滚动         
    /* 
     * setOnLastItemVisibleListener 
     * 当用户拉到底时调用   
     */  
    /* 
     * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程 ，他的回调函数是onTouchEvent（MotionEvent event）, 
     * 然后通过判断event.getAction()是MotionEvent.ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。 
     * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为 
     */ 
	//上下刷新 [一]
	private OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>(){
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			Log.i(TAG, "OnRefreshListener2-------onPullDownToRefresh---------------------");
			//下拉刷新  降序
			//new GetDataTask().execute(new String[]{"desc",String.valueOf(listUser.size()+9)});
			//new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
			new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
		}
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			Log.i(TAG, "OnRefreshListener2-----onPullUpToRefresh-----------------------");
			//上拉刷新  升序
			//new GetDataTask().execute(new String[]{"asc",String.valueOf(listUser.size()+9)});
			//new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
			new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
		}
	};
	
	
	//上下刷新 [二]
	private OnRefreshListener<ListView> refreshListenerTwo = new OnRefreshListener<ListView>(){
		@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				//String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				//label="下拉刷新!";
				//refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
//				refreshView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");  
//				refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("上拉刷新...");  
//				refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel("上拉刷新...");  
//				
//				refreshView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新!");
//				refreshView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新!");
//				refreshView.getLoadingLayoutProxy(true, false).setRefreshingLabel("下拉刷新!");
				
				// Do work to refresh the list here.
				new GetDataTask().execute(new String[]{String.valueOf(listUser.size()),"9"});
			}
	};
	//事件
	private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@SuppressWarnings("rawtypes")
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
				Log.d(TAG, "onItemClick,mActionMode is  not null, select item " + position + " to select");
				User item = (User) mPullToRefresh.getRefreshableView().getAdapter().getItem(position);
				Log.i("TAG", item.getName()+":"+DateUtil.dateFormat(item.getUpdatetime(),DateUtil.getDatePattern()));
				//显示
				showToast(item.getName()+":"+DateUtil.dateFormat(item.getUpdatetime(),DateUtil.getDatePattern()));
				
				Intent _intent = new Intent();
				_intent.setClass(mActivity, UserDetailActivity.class);
				Bundle bd=new Bundle();
				//传递item
				bd.putSerializable("userDetail",item);  
				_intent.putExtras(bd);
				//启动新的Activity
				startActivity(_intent);
		}
	};
	//上/下拉刷新提示
	 private void init(){
	        //下拉刷新..
	        ILoadingLayout startLabels = mPullToRefresh.getLoadingLayoutProxy(true, false); 
			startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示    
			startLabels.setRefreshingLabel("正在载入...");// 刷新时    
			startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
			
			//上拉刷新
			ILoadingLayout endLabels = mPullToRefresh.getLoadingLayoutProxy(false, true);    
			endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示    
			endLabels.setRefreshingLabel("正在载入...");// 刷新时    
			endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示  
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
			Log.i(TAG,"onPreExecute()------------------------------isFirst:"+isFirst);
			if(isFirst)
			{
				if (dialog == null || !dialog.isShowing())
				{
					dialog = new ProgressDialog(mActivity);
				}
				dialog = new ProgressDialog(mActivity);
				dialog.setMessage("loading...");
				//dialog.setIndeterminate(true);
				dialog.setCancelable(false);
				dialog.show();
				isFirst=false;
			}
		}
		
		@Override
		protected HttpResult doInBackground(String... params) {
			// Simulates a background job.
			HttpResult result = new HttpResult();
			String url ="";
			try {
				//Thread.sleep(1500);
				Log.i(TAG,"doInBackground()-----------------------------------------");
				url =getString(R.string.struts_uri)+"/com.app.content/user_list.action?start="+params[0]+"&limit="+params[1];
				result = HttpRequest.request(url);
			} catch (Exception e) {
				Log.e(TAG,e.toString());
				return result;
			}
			return result;
		}

		@Override
		protected void onPostExecute(HttpResult result) {
			Log.i(TAG,"onPostExecute()-----------------------------------------");
			//loading
			 if (dialog != null && dialog.isShowing())
			 {
				 dialog.dismiss();
			 }
			if (result != null && result.GetUserSuccessed()) {
				JSONObject resObject = result.GetReturnValue();
				try {
					String reString = resObject.getString("Items");
					String count = resObject.getString("total");
					if(!Helper.isEmpty(count) && Integer.valueOf(count) ==0){
						Log.e("TAG", "无最新数据!");
					}else {
						if (!Helper.isEmpty(reString)) {
							//类似分页
							//listUser.addAll(JsonToMyObject(getList(items,User.class)));
							List<User> list=(List<User>) getUserList(reString, User.class);
							listUser.addAll(list);
							
							/********objArray********************************************/
							/***save2sqlite**/
							User[] objArray=(User[]) JSONHelper.parseArray(reString,User.class);
							save2sqlite(objArray);
							//Log.e(TAG,"============================================save2sqlite======================");
							/********printUserSqlite********************************************/
							 //输出
							//Log.e(TAG,"============================================printUserSqlite======================");
							printUserSqlite();
						}
					}
				} catch (Exception e) {
					Log.e("TAG", e.getMessage());
				}
			} else {
				if (result != null) {
					Log.e("TAG", "服务器异常!");
				}
				if(!"".equals(result.getError())){
					showToast(result.getError());
				}
			}
			//刷新Listview
			mAdapter.notifyDataSetChanged();
			mPullToRefresh.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
	 
	/**
	 * 反序列化泛型集合
	 * @param jsonStr	json字符串
	 * @param clazz	对象类型
	 * @return	反序列化后的list
	 * @throws JSONException 
	 */
	private  <T> List<T> getUserList(String strJson, Class<T> clazz){
		T[] ObjectArry=(T[]) JSONHelper.parseArray(strJson,clazz);
		if (clazz == null ) {
			return null;
		}
		//new list
		List<T> list = null;
		//IMAGE SERVER_URI
		String imgURI=this.getString(R.string.struts_uri);
		try {
			//根据class new一个Obj
			T obj = JSONHelper.newInstance(clazz);
			if (obj == null) {
				return null;
			}
			list = (List<T>) new ArrayList<T>();
			for (int i = 0; i < ObjectArry.length; i++) {
				obj=null;
				obj=(T) ObjectArry[i];
				if(((User) obj).getPic()==null || "".equals(((User) obj).getPic().trim())){
					((User) obj).setPic(imgURI+"/image.jpg");
				}else {
					((User) obj).setPic(imgURI+"/"+((User) obj).getPic());
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//数据转换
	public  List<User> JsonToMyObject(List<JSONObject> jlist)
	{
		List<User> list = new ArrayList<User>();
		String imgURI=this.getString(R.string.struts_uri);
		for	(int i=0; i < jlist.size(); i++)
		{
			final JSONObject jobj = jlist.get(i);
			User u = new User();
			try {
				//System.out.println("----------------------------;;"+jobj.getString("name")+imgURI+"/"+jobj.getString("pic"));
				u.setName(jobj.getString("name"));
				u.setPhone(jobj.getString("phone"));
				u.setPic(imgURI+"/"+jobj.getString("pic"));
				list.add(u);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/*
	 * object[]保存到sqlite
	 * 
	 * */
	private void save2sqlite(Object[] array)
	{
		System.out.println(array.length);
		//userDao= new UserDaoImpl(mActivity);
		//保存到sqlet
		for(int i =0;i<array.length;i++){
			User bean =(User) array[i];
			userDao.insert(bean);
			System.out.println(bean.getLoginName());
		}
	}
	
	//输出sqlite对象
	private void printUserSqlite(){
		//userDao= new UserDaoImpl(mActivity);
		//Cursor cursor= dbrade.rawQuery("select * from userm where id =?", new String[]{"1"});
		//String sql="select * from userm where id =?";
		String sql="select * from user ";
		String[] selectionArgs=new String[]{};
		List<User> lis=userDao.rawQuery(sql, selectionArgs);
		User beanUser;
		for(int i=0;i<lis.size();i++){
			beanUser=new User();
			beanUser = lis.get(i);
			System.out.println("loginName:---------------------:"+beanUser.getLoginName());
		}
	}
		
	//提示信息
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
}
