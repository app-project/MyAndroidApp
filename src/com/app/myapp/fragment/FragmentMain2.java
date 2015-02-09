package com.app.myapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.app.myapp.R;
import com.app.myapp.adapter.listAdapter;
import com.app.myapp.http.HttpRequest;
import com.app.myapp.http.HttpResult;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;

public class FragmentMain2 extends Fragment {

 
	//TAG
	private final String TAG = "com.app.androidapp.fragment.FragmentMain2";
	private Activity mActivity;
	
	private ListView listView2;
	private View newsLayout;
	
	private List<User> listUser;
	private listAdapter listadapter;
	
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		Log.i(TAG,"onCreateView()");
		//获得View
        newsLayout = inflater.inflate(R.layout.fragment_2, container,  false);  
        //获得Activity
        mActivity =this.getActivity();
        //查找组件
        mfindViews();
        //设置事件
        setClick();
        
        //new 用户ArrayList
        listUser=new ArrayList();
        
        //Adapter列表
        listadapter = new listAdapter(mActivity);
		
        listadapter.setData(listUser);
        
        listView2.setAdapter(listadapter);
        //数据绑定
       // setListView();
        new DataTask().execute();
        
        return newsLayout;  
    }
	
	//查找元素
	private void mfindViews(){
		listView2 =(ListView) newsLayout.findViewById(R.id.listView2);
	}
	//设置事件
	private void setClick(){
		listView2.setOnItemClickListener(ListOnItemClick);
	}
	//事件
	private OnItemClickListener ListOnItemClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			User item1 = (User) listadapter.getItem(position);
			Log.i("TAG", item1.getName());
			//显示
			showToast(item1.getName());
		}
	};
	//setListView
	private void setListView()
	{
		Log.i(TAG,"setListView();");
		
		
		//清空数据源
		listUser.clear();
		//动态生成list
		try {
			listUser =getData(10);
		}catch(Exception e){
			e.printStackTrace();
			Log.e(TAG,e.toString());
		}
		 Log.i(TAG,"listUser.size:"+listUser.size());
		listadapter.setData(listUser);
		listadapter.notifyDataSetChanged();
		
		Log.i(TAG,"listadapter.getCount:"+listadapter.getCount());
		
		//赋值
        //listView2 = (ListView) newsLayout.findViewById(R.id.listView2);
        listView2.setAdapter(listadapter);
        //*/
	}
	//临时数据源
	private List<User> getData(int count)
	 {
		Log.i("TAG", "getData()");
		 List<User> list = new ArrayList<User>();
		 User user;
		 //循环
		 for(int i=1;i<count;i++)
		 {
			 user = new User();
			 
			 user.setPhone(i+"");
			 user.setName("T"+i);
			 
			 list.add(user);
		 }
		
		 return list;
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
	private class DataTask extends AsyncTask<Void, Void, HttpResult> implements
	OnCancelListener {
		//loading 
		private ProgressDialog dialog;
		@Override
		public void onCancel(DialogInterface dialog) {
			this.cancel(true);
			 dialog.dismiss();
		}
		
		@Override
		protected void onPreExecute() {
			Log.i(TAG,"onPreExecute()------------------------------");
			if (dialog == null || !dialog.isShowing())
			{
				dialog = new ProgressDialog(mActivity);
			}
			dialog = new ProgressDialog(mActivity);
			dialog.setMessage("loading...");
			//dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected void onPostExecute(HttpResult result) {
			Log.i(TAG,"onPostExecute()");
			try {
			    //  Thread.sleep(4000);
			      }catch(Exception e){}
			
			//暂时不防问网络上的
			//listUser = getData(100000);
			//loading
			 if (dialog != null && dialog.isShowing())
			 {
				 dialog.dismiss();
			 }
			
			if (result != null && result.GetUserSuccessed()) {
				///*
				JSONObject resObject = result.GetReturnValue();
				try {
		
					String reString = resObject.getString("Items");
					if (!Helper.isEmpty(reString)) {
						listUser.clear();
						JSONArray items = new JSONArray(reString);
						listUser.addAll(JsonToMyObject(Helper.getList(items)));
						
						listadapter.setData(listUser);
						listadapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					Log.e("TAG", e.getMessage());
				}
				// */
				
			} else {
				if (result != null) {
					//Log.e("TAG", result.getError());
					Log.e("TAG", "服务器异常!");
					//暂时不防问网络上的
					//listUser = getData(100000);
					//listadapter.setData(listUser);
					//listadapter.notifyDataSetChanged();
				}
			}
		
			super.onPostExecute(result);
		}

		@Override
		protected HttpResult doInBackground(Void... params) {
			Log.i(TAG,"doInBackground()");
			//String url = "http://www.ruiii.com/api/v6/Class/?parentid=&type=8";
			String url =getString(R.string.servlet_uri)+"/userInfo";
			return HttpRequest.request(url);
		}
	}
	
	//数据转换
	public static List<User> JsonToMyObject(List<JSONObject> jlist)
	{
		List<User> list = new ArrayList<User>();
		for	(int i=0; i < jlist.size(); i++)
		{
			final JSONObject jobj = jlist.get(i);
			User u = new User();
			try {
				u.setName(jobj.getString("name"));
				u.setPhone(jobj.getString("phone"));
				list.add(u);
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