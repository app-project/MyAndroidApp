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
import com.app.myapp.adapter.listAdapterCigarette;
import com.app.myapp.dao.CigaretteDao;
import com.app.myapp.dao.impl.CigaretteDaoImpl;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;
import com.app.myapp.model.Cigarette;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;
import com.app.myapp.util.JSONHelper;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import android.app.Fragment;

public class FrmCigarette extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FragmentMain3";
	//Activity
	private Activity mActivity;
	
	private PullToRefreshListView mPullToRefresh;
	private List<Cigarette> listCigarette;
	private listAdapterCigarette mAdapter;
	private Boolean isFirst=true;
	private View newsLayout;
	
	private CigaretteDao cigaretteDao;
	
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
        
        newsLayout = inflater.inflate(R.layout.frm_cigarette, container, false);  
        //list控件
        mfindViews();
        //绑定事件
        setClick();
        
        cigaretteDao= new CigaretteDaoImpl(mActivity);
        
        //new 数据源list
        listCigarette =new ArrayList<Cigarette>();
        
        mPullToRefresh.setMode(Mode.PULL_FROM_START);  
        init();
		
		//获得RefreshableView
		ListView actualListView = mPullToRefresh.getRefreshableView();
		//Adapter列表
		mAdapter = new listAdapterCigarette(mActivity);
		
		mAdapter.setData(listCigarette);
		//SimpleAdapter
		//mAdapter=new SimpleAdapter(mActivity,Simplelist,R.layout.list_item3,new String[]{"num","name"},new int[]{R.id.txtList3User,R.id.txtList3Name});
		
		actualListView.setAdapter(mAdapter);
		//加载数据 默认降序
		new GetDataTask().execute(new String[]{String.valueOf(listCigarette.size()),"9"});
        return newsLayout;  
    }
	//查找元素
	private void mfindViews(){
		//获得listView
		mPullToRefresh = (PullToRefreshListView) newsLayout.findViewById(R.id.listCigarette);
	}
	//设置事件
	private void setClick(){
		mPullToRefresh.setOnRefreshListener(refreshListener);
		mPullToRefresh.setOnItemClickListener(mOnItemClickListener);
	}
	
	//上下刷新 [一]
	private OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>(){
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			Log.i(TAG, "OnRefreshListener2-------onPullDownToRefresh---------------------");
			//下拉刷新
			new GetDataTask().execute(new String[]{String.valueOf(listCigarette.size()),"9"});
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			Log.i(TAG, "OnRefreshListener2-----onPullUpToRefresh-----------------------");
			//上拉刷新
			new GetDataTask().execute(new String[]{String.valueOf(listCigarette.size()),"9"});
		}
	};
	
	
	//上下刷新 [二]
	private OnRefreshListener<ListView> refreshListenerTwo = new OnRefreshListener<ListView>(){
		@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
				new GetDataTask().execute(new String[]{String.valueOf(listCigarette.size()),"9"});
			}
	};
	//事件
	private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@SuppressWarnings("rawtypes")
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
				Log.d(TAG, "onItemClick,mActionMode is  not null, select item " + position + " to select");
				Cigarette item = (Cigarette) mPullToRefresh.getRefreshableView().getAdapter().getItem(position);
				//Log.i("TAG", item.getName()+":"+DateUtil.dateFormat(item.getUpdatetime(),DateUtil.getDatePattern()));
				Log.i("TAG", item.getName());
				//显示
				//showToast(item.getName());
				
				Intent _intent = new Intent();
				_intent.setClass(mActivity, CigaretteDetailActivity.class);
				Bundle bd=new Bundle();
				//传递item
				bd.putSerializable("cigaretteDetail",item);  
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
				dialog.setCancelable(false);
				dialog.show();
				isFirst=false;
			}
		}
		
		@Override
		protected HttpResult doInBackground(String... params) {
			HttpResult result = new HttpResult();
			String url ="";
			try {
				//Thread.sleep(1500);
				Log.i(TAG,"doInBackground()-----------------------------------------");
				url =getString(R.string.struts_uri)+"/com.app.content/cigarette_list.action?start="+params[0]+"&limit="+params[1];
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
							//类似分页
							List<Cigarette> list=(List<Cigarette>) getList(reString, Cigarette.class);
							listCigarette.addAll(list);
							
							/***save2sqlite**/
							Log.e(TAG,"============================================save2sqlite======================");
							//Cigarette[] objArray=(Cigarette[]) JSONHelper.parseArray(reString,Cigarette.class);
							//save2sqlite(objArray);
							/********printUserSqlite********************************************/
							 //输出
							Log.e(TAG,"============================================printUserSqlite======================");
							//printUserSqlite();
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
	private  <T> List<T> getList(String strJson, Class<T> clazz){
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
				if(((Cigarette) obj).getPic()==null || "".equals(((Cigarette) obj).getPic().trim())){
					((Cigarette) obj).setPic(imgURI+"/image.jpg");
				}else {
					((Cigarette) obj).setPic(imgURI+"/"+((Cigarette) obj).getPic());
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		Cigarette cigarette;
		//保存到sqlet
		for(int i =0;i<array.length;i++){
			Cigarette bean =(Cigarette) array[i];
			cigarette=new Cigarette();
			try {
				cigarette = cigaretteDao.checkCigarette("id",String.valueOf(bean.getId()));
				if( cigarette ==null ){
					cigaretteDao.insert(bean);
					Log.i(TAG, "id:"+ bean.getId() +"; name=" + bean.getName() +"的数据已存入sqlit");
				}else {
					Log.i(TAG, "sqlit中已存在id:"+ cigarette.getId() +"; name=" + cigarette.getName() +"的数据");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
	}
	
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
	}
	 
	
	//提示信息
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
}
