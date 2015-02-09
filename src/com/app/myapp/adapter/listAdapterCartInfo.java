package com.app.myapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.myapp.R;
import com.app.myapp.model.CartInfo;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;
import com.app.myapp.util.image.ImageManager;

public class listAdapterCartInfo extends MyAdapter {
	//TAG
	private final String TAG = "com.app.androidapp.adapter.listAdapterCartInfo";
	private List<CartInfo> list = new ArrayList<CartInfo>();
	private Context mContext;
	
	public listAdapterCartInfo(Context _context) {
		this.mContext = _context;
	}
	
	public void setData(List<CartInfo> _list)
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
			final CartInfo object = list.get(pos);
			//22  截取
			holder.name.setText(Helper.getString(object.getName(),22));
			holder.price.setText(String.valueOf(object.getPrice()));
			holder.num.setText(String.valueOf(object.getNum()));
			//net image loader
			if (object.getPic()!=null && !"".equals(object.getPic())) {
				//cache+network
				//loadImage(object.getPic(),holder.pic);
				//sdcard+cache+network
				//ImageManager.from(mContext).displayImage(holder.pic, object.getPic(),R.drawable.ic_launcher,100,100);
				loadImageNew(mContext,holder.pic, object.getPic(),R.drawable.ic_launcher,100,100);
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
		public TextView price;
		public TextView num;
		
		public VisitorItemLayout(Context context) 
		{
			super(context);
			((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_cartinfo, this);
			
			name = (TextView)findViewById(R.id.txtName);
			price = (TextView)findViewById(R.id.txtPrice);
			pic =   (ImageView)findViewById(R.id.imageCartinfo);
			num = (TextView)findViewById(R.id.txtCarInfoNum);
		}
	}
}
