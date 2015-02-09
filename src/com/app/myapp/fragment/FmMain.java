package com.app.myapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.app.myapp.R;
import com.app.myapp.adapter.Adapter_GridView;
import com.app.myapp.view.AbOnItemClickListener;
import com.app.myapp.view.AbSlidingPlayView;

public class FmMain extends Fragment {

	//TAG
	private final String TAG = "com.app.androidapp.fragment.FragmentMain1";
	private Activity mActivity;
	
	//顶部标题栏
	private TextView tv_top_title;
	//分类的九宫格
	private GridView gridView_classify;
	//首页轮播
	private AbSlidingPlayView viewPager;
	//扫一扫
	private ImageView iv_shao;
	// 分类九宫格的资源文件
	private int[] pic_path_classify = { R.drawable.menu_guide_1, R.drawable.menu_guide_2, R.drawable.menu_guide_3, R.drawable.menu_guide_4, R.drawable.menu_guide_5, R.drawable.menu_guide_6, R.drawable.menu_guide_7, R.drawable.menu_guide_8 };
	private Adapter_GridView adapter_GridView_classify;
	/**存储首页轮播的界面*/
	private ArrayList<View> allListView;
	/**首页轮播的界面的资源*/
	private int[] resId = {R.drawable.menu_viewpager_2,R.drawable.show_m1 ,R.drawable.menu_viewpager_3, R.drawable.menu_viewpager_1,R.drawable.menu_viewpager_4, R.drawable.menu_viewpager_5 };
	
	private View newsLayout;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		//inflate:作用--->填充一个新的视图层次结构从指定的XML资源文件中
		/*
		 * inflate()方法中参数： 
		 * 1.想要用的布局文件的id 
		 * 2.持有选项卡的内容，获取FrameLayout 
		 * 3.true：将此处解析的xml文件做为根视图View 
		 * */     

        newsLayout = inflater.inflate(R.layout.fragment_1, container,false);  
        Log.i(TAG,"onCreateView");
        mActivity =this.getActivity();
        
        initView(newsLayout);
        
        //mfindViews();
        //setClick();
        //setListView();
     
        return newsLayout;  
    }
	
	private void initView(View view) {
//		iv_shao=(ImageView) view.findViewById(R.id.iv_shao);
//		iv_shao.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				//跳转到二维码扫描
//				Intent intent=new Intent(getActivity(),CaptureActivity.class); 
//				startActivity(intent);
//			}
//		});
//		tv_top_title=(TextView) view.findViewById(R.id.tv_top_title);
//		tv_top_title.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				//挑战到宝贝搜索界面
//				Intent intent=new Intent(getActivity(),WareActivity.class);
//				startActivity(intent);
//			}
//		});
		
		gridView_classify = (GridView) view.findViewById(R.id.my_gridview);
		gridView_classify.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter_GridView_classify = new Adapter_GridView(getActivity(), pic_path_classify);
//		adapter_GridView_hot = new Adapter_GridView_hot(getActivity(), pic_path_hot);
		gridView_classify.setAdapter(adapter_GridView_classify);
//		my_gridView_hot.setAdapter(adapter_GridView_hot);

		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		//设置播放方式为顺序播放
		viewPager.setPlayType(1);
		//设置播放间隔时间
		viewPager.setSleepTime(3000);

//		gridView_classify.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				//挑战到宝贝搜索界面
//				Intent intent = new Intent(getActivity(), WareActivity.class);
//				startActivity(intent);
//			}
//		});
//		my_gridView_hot.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				//跳转到宝贝详情界面
//				Intent intent = new Intent(getActivity(), BabyActivity.class);
//				startActivity(intent);
//			}
//		});
		
		initViewPager();
	}

	private void initViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < resId.length; i++) {
			//导入ViewPager的布局
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			imageView.setImageResource(resId[i]);
			allListView.add(view);
		}
		
		
		viewPager.addViews(allListView);
		//开始轮播
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				//跳转到详情界面
//				Intent intent = new Intent(getActivity(), BabyActivity.class);
//				startActivity(intent);
			}
		});
	}

}
