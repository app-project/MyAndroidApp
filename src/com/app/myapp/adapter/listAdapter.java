package com.app.myapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.myapp.R;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;

public class listAdapter extends MyAdapter {
	//TAG
	private final String TAG = "com.app.androidapp.adapter.listAdapter";
	private List<User> list = new ArrayList<User>();
	private Context mContext;
	
	public listAdapter(Context _context) {
		this.mContext = _context;
	}
	
	public void setData(List<User> _list)
	{
		list =  _list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int pos, View v, ViewGroup p) {
		// TODO Auto-generated method stub
		VisitorItemLayout holder;
		if (v == null) {
			holder = new VisitorItemLayout(this.mContext);
		} else {
			holder = (VisitorItemLayout) v;
		}
		
		try 
		{
			final User object = list.get(pos);
			
			holder.phone.setText(object.getPhone());
			//22  截取
			holder.name.setText(Helper.getString(object.getName(),22));
			
			//net image loader
			//loadImage add to base 
			//loadImage(object.getPic()+"_200_160.jpg",holder.pic);
			if (object.getPic()!=null && !"".equals(object.getPic())) {
				loadImage(object.getPic(),holder.pic);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Log.e(TAG,msg);
		}
		return holder;
	}
	
	public class VisitorItemLayout extends RelativeLayout
	{
		public ImageView pic;
		public TextView name;
		public TextView phone;
		public VisitorItemLayout(Context context) 
		{
			super(context);
			((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item2, this);
			
			phone = (TextView)findViewById(R.id.txtPhone);
			name = (TextView)findViewById(R.id.txtName);
			pic =   (ImageView)findViewById(R.id.imageView2);
		}
	}
}
