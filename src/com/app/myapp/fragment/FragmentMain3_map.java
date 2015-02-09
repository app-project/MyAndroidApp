package com.app.myapp.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.app.myapp.R;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;
import com.app.myapp.util.Helper;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FragmentMain3_map extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FragmentMain3";
	//Activity
	private Activity mActivity;
	
	private PullToRefreshListView mPullToRefresh;
	private SimpleAdapter mAdapter;
	private Boolean isFirst=true;
	
	private List<Map<String, Object>> Simplelist ;
	//private LinkedList<Map<String,Object>> Simplelist;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View newsLayout = inflater.inflate(R.layout.fragment_3, container, false);  
        mActivity =this.getActivity();
        //list控件
        mPullToRefresh = (PullToRefreshListView) newsLayout.findViewById(R.id.listView3);
        //new 数据源list
       // Simplelist = new ArrayList<Map<String,Object>>();
        Simplelist = new LinkedList<Map<String,Object>>();
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
        mPullToRefresh.setMode(Mode.BOTH);  
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
        
        ///*
 		mPullToRefresh.setOnRefreshListener(
 				 new OnRefreshListener2<ListView>(){
					@Override
					public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
						Log.i(TAG, "OnRefreshListener2-------onPullDownToRefresh---------------------");
						//下拉刷新  降序
						new GetDataTask().execute(new String[]{"desc",String.valueOf(Simplelist.size()+9)});
					}
					@Override
					public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
						Log.i(TAG, "OnRefreshListener2-----onPullUpToRefresh-----------------------");
						//上拉刷新  升序
						new GetDataTask().execute(new String[]{"asc",String.valueOf(Simplelist.size()+9)});
					}
 				 }
 		);
 		//*/
 		/*
 		mPullToRefresh.setOnRefreshListener(
 			new OnRefreshListener<ListView>() {
	 			@Override
	 			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
	 				//String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
	 				String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
	 				// Update the LastUpdatedLabel
	 				label="下拉刷新!";
	 				//refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	 				
//	 				refreshView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");  
//	 				refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("上拉刷新...");  
//	 				refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel("上拉刷新...");  
//	 				
//	 				refreshView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新!");
//	 				refreshView.getLoadingLayoutProxy(true, false).setReleaseLabel("下拉刷新!");
//	 				refreshView.getLoadingLayoutProxy(true, false).setRefreshingLabel("下拉刷新!");
	 				
	 				// Do work to refresh the list here.
	 				new GetDataTask().execute(new String[]{"desc",String.valueOf(Simplelist.size()+9)});
	 			}
 			}
 				
 		);
 		*/
 		// Add an end-of-list listener
		mPullToRefresh.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				//Toast.makeText(mActivity, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		//获得数据
		Simplelist =getData();
		ListView actualListView = mPullToRefresh.getRefreshableView();
		
		//SimpleAdapter
		mAdapter=new SimpleAdapter(mActivity,Simplelist,R.layout.list_item3,new String[]{"num","name"},new int[]{R.id.txtList3Info,R.id.txtList3Name});
		
		actualListView.setAdapter(mAdapter);
		//加载数据 默认降序
		new GetDataTask().execute(new String[]{"desc","9"});
        return newsLayout;  
    }
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
				Thread.sleep(1500);
				Log.i(TAG,"doInBackground()-----------------------------------------");
				//String url = "http://www.ruiii.com/api/v6/Class/?parentid=&type=8";
				url =getString(R.string.servlet_uri)+"/userInfo?num="+params[1]+"&sort="+params[0];
				result = HttpRequest.request(url);
				
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
			 
			if (result != null && result.GetUserSuccessed()) {
				JSONObject resObject = result.GetReturnValue();
				try {
					String reString = resObject.getString("Items");
					if (!Helper.isEmpty(reString)) {
						//清空List
						Simplelist.clear();
						JSONArray items = new JSONArray(reString);
						//将json 转换对象
						//Simplelist.addAll(JsonToMyObject(Helper.getList(items)));
						Simplelist= JsonToMyObject(Helper.getList(items));
						//mAdapter.setData(Simplelist);
						//刷新Listview
						mAdapter.notifyDataSetChanged();
						mPullToRefresh.onRefreshComplete();
					}
				} catch (Exception e) {
					Log.e("TAG", e.getMessage());
				}
			} else {
				if (result != null) {
					Log.e("TAG", "服务器异常!");
				}
			}
			super.onPostExecute(result);
		}
	}

	 //数据源
	 private List<Map<String, Object>> getData()
	 {
		// List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		int count=9;
		 if(Simplelist ==null){
			 Simplelist = new ArrayList<Map<String, Object>>();
		 }else {
			 count +=Simplelist.size(); 
		 }
		 Map<String, Object> map = null;
	 
		 //循环
		 for(int i=Simplelist.size();i<count;i++)
		 {
			 map = new HashMap<String, Object>();
			 //map.put("num", i+1);
			// map.put("name", "tom"+i);
			 map.put("num", "");
			 map.put("name", "");
			 Simplelist.add(map);
		 }
		 return Simplelist;
	 }
	 
	 public  List<Map<String, Object>> JsonToMyObject(List<JSONObject> jlist)
	{
		 	Map<String, Object> map = null;
			for	(int i=0; i <jlist.size() ; i++)
			{
				final JSONObject jobj = jlist.get(i);
				try {
					 map = new HashMap<String, Object>();
					 map.put("num", jobj.getString("phone"));
					 map.put("name", jobj.getString("name"));
					 Simplelist.add(map);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			return Simplelist;
	}
	
}
