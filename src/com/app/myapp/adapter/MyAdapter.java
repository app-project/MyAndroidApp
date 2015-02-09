package com.app.myapp.adapter;

//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.ruiii.liangpingou.util.AsyncImageLoader;

//import com.mynews.web.http.HttpImage;

import com.app.myapp.R;
import com.app.myapp.util.AsyncImageLoader;
import com.app.myapp.util.image.ImageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyAdapter extends BaseAdapter
{
	static String TAG = "BaseAdapter";
	
	//cache+network
	protected void loadImage(final String url, final ImageView imageView)
	{
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		Drawable cacheImage = asyncImageLoader.loadDrawable(url,
				new AsyncImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable) {
						imageView.setImageDrawable(imageDrawable);
						imageView.setClickable(true);
					}
				});
		if (cacheImage != null) {
			imageView.setImageDrawable(cacheImage);
			imageView.setClickable(true);
		}
	}
	
	/**
	 * 显示图片固定大小图片的缩略图，一般用于显示列表的图片，可以大大减小内存使用
	 * 
	 * @param imageView 加载图片的控件
	 * @param url 加载地址
	 * @param resId 默认图片
	 * @param width 指定宽度
	 * @param height 指定高度
	 */
	//sdcard+cache+network
	protected void loadImageNew(Context context,final ImageView imageView, final String url,final int defualtImage,int width,int height)
	{
		
		ImageManager.from(context).displayImage(imageView,url,defualtImage,width,height);
	}
	
	@Override
	public int getCount() {
		return 0;
	}
	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}
	
 
}
