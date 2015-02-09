package com.app.myapp.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.app.myapp.R;
import com.app.myapp.adapter.listAdapterInfo;
import com.app.myapp.adapter.listAdapterThree;
import com.app.myapp.dao.InformationDAO;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;
import com.app.myapp.model.Information;
import com.app.myapp.util.Helper;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import android.app.Fragment;

public class FragmentMain4 extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FragmentMain4";
	//Activity
	private Activity mActivity;
	
	private Boolean isFirst=true;
	private List<Information> listInfo;
	private View newsLayout;
	private InformationDAO infoDao;
	
	
	private PullToRefreshListView mPullRefreshListView;
	private listAdapterInfo mAdapter;
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        newsLayout = inflater.inflate(R.layout.fragment_4, container, false);  
        mActivity =this.getActivity();
        
        Log.e(TAG, TAG);
        
        /*//list控件
        mfindViews();
        //绑定事件
        setClick();
        
        //new 信息ArrayList
        listInfo=new ArrayList<Information>();
     
        mPullRefreshListView.setMode(Mode.PULL_FROM_START); 
        init();
        
        //获得RefreshableView
  		ListView actualListView = mPullRefreshListView.getRefreshableView();
  		//Adapter列表
  		mAdapter = new listAdapterInfo(mActivity);
  		
  		mAdapter.setData(listInfo);
  		
  		actualListView.setAdapter(mAdapter);
		//加载数据 默认降序
		new GetDataTask().execute(new String[]{String.valueOf(listInfo.size()),"9"});
        
       */
		
        return newsLayout;  
    }
	
	//查找元素
	private void mfindViews(){
		mPullRefreshListView = (PullToRefreshListView) newsLayout.findViewById(R.id.listView4);
	}
	//设置事件
	private void setClick(){
		mPullRefreshListView.setOnRefreshListener(refreshListener);
		mPullRefreshListView.setOnItemClickListener(mOnItemClickListener);
	}
	
	//上/下拉刷新提示
	 private void init(){
	        //下拉刷新..
	        ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy(true, false); 
			startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示    
			startLabels.setRefreshingLabel("正在载入...");// 刷新时    
			startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
			
			//上拉刷新
			ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(false, true);    
			endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示    
			endLabels.setRefreshingLabel("正在载入...");// 刷新时    
			endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示  
     }
	 
	//上下刷新 
	private OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>(){
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			Log.i(TAG, "OnRefreshListener2-------onPullDownToRefresh---------------------");
			//下拉刷新  降序
			new GetDataTask().execute(new String[]{String.valueOf(listInfo.size()),"9"});
		}
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			Log.i(TAG, "OnRefreshListener2-----onPullUpToRefresh-----------------------");
			//上拉刷新  升序
			new GetDataTask().execute(new String[]{String.valueOf(listInfo.size()),"9"});
		}
	};
	
	//点击事件
	private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
				Log.d(TAG, "onItemClick,mActionMode is  not null, select item " + position + " to select");
				Information item1 = (Information) mPullRefreshListView.getRefreshableView().getAdapter().getItem(position);
				Log.i("TAG", item1.getTitle());
				//显示
				showToast(item1.getTitle());
		}
	};
	 
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
				url =getString(R.string.struts_uri)+"/com.app.content/info_list.action?start="+params[0]+"&limit="+params[1];
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
			if (result != null && result.GetActSuccessed()) {
				JSONObject resObject = result.GetReturnValue();
				try {
					String reString = resObject.getString("Items");
					String count = resObject.getString("total");
					if(!Helper.isEmpty(count) && Integer.valueOf(count) ==0){
						Log.e("TAG", "无最新数据!");
					}else {
						if (!Helper.isEmpty(reString)) {
							//清空List
							//listUser.clear();
							JSONArray items = new JSONArray(reString);
							//将json 转换对象
							//Simplelist.addAll(JsonToMyObject(Helper.getList(items)));
							//listUser= JsonToMyObject(Helper.getList(items));
							
							//类似分页
							listInfo.addAll(JsonToMyObject(Helper.getList(items)));
							Log.e("=======================", listInfo.toString());
							/********objArray********************************************/
							//Object[] objArray=Json2ObjectArray(reString,User.class);
							//Log.e(TAG,"objArray====================================objArray.length:"+objArray.length);
							/********save2sqlite********************************************/
							//save2sqlite(objArray);
							//Log.e(TAG,"============================================save2sqlite======================");
							/********printUserSqlite********************************************/
							 //输出
							Log.e(TAG,"============================================printUserSqlite======================");
							printUserSqlite();
						}
					}
				} catch (Exception e) {
					Log.e("TAG", e.getMessage());
				}
			} else {
				if (result != null) {
					Log.e("==============", "FragmentMain4");
					Log.e("TAG", "服务器异常!");
				}
			}
			//刷新Listview
			mAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
	private void save2sqlite(Object[] array)
	{
		System.out.println(array.length);
		//保存到sqlet
		for(int i =0;i<array.length;i++){
			Information bean =(Information) array[i];
			infoDao.insert(bean);
			System.out.println(bean.getTitle());
		}
	}
	
	//输出sqlite对象
	private void printUserSqlite(){
		String sql="select * from information ";
		String[] selectionArgs=new String[]{};
		List<Information> lis=infoDao.rawQuery(sql, selectionArgs);
		Information beanUser;
		for(int i=0;i<lis.size();i++){
			beanUser=new Information();
			beanUser = lis.get(i);
			System.out.println("title:---------------------:"+beanUser.getTitle());
		}
	}
	
	//数据转换
	public  List<Information> JsonToMyObject(List<JSONObject> jlist)
	{
		List<Information> list = new ArrayList<Information>();
		String imgURI=this.getString(R.string.struts_uri);
		for	(int i=0; i < jlist.size(); i++)
		{
			final JSONObject jobj = jlist.get(i);
			Information info = new Information();
			try {
				//System.out.println("----------------------------;;"+jobj.getString("name")+imgURI+"/"+jobj.getString("pic"));
				info.setTitle(jobj.getString("title"));
				info.setContent(jobj.getString("content"));
				info.setImage(imgURI+"/"+jobj.getString("image"));
				list.add(info);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//提示信息
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
	
}
