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
import com.app.myapp.model.Cigarette;
import com.app.myapp.model.User;
import com.app.myapp.util.Helper;
import com.app.myapp.util.image.ImageManager;

public class listAdapterCigarette extends MyAdapter {
	//TAG
	private final String TAG = "com.app.androidapp.adapter.listAdapterCigarette";
	private List<Cigarette> list = new ArrayList<Cigarette>();
	private Context mContext;
	
	public listAdapterCigarette(Context _context) {
		this.mContext = _context;
	}
	
	public void setData(List<Cigarette> _list)
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
			final Cigarette object = list.get(pos);
			//22  截取
			holder.name.setText(Helper.getString(object.getName(),22));
			
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
	
	
    public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {  
        System.out.println("图片是否变成圆角模式了+++++++++++++");
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
  
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
        final float roundPx = pixels;  
  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        System.out.println("pixels+++++++"+pixels);
  
        return output;  
    }

	
	public class VisitorItemLayout extends RelativeLayout
	{
		public ImageView pic;
		public TextView name;
		public VisitorItemLayout(Context context) 
		{
			super(context);
			((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_cigarette, this);
			
			name = (TextView)findViewById(R.id.txtName);
			pic =   (ImageView)findViewById(R.id.imageCigarette);
		}
	}
}
