package com.app.myapp.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.app.myapp.ahibernate.BaseDaoImpl;
import com.app.myapp.ahibernate.MyDBHelper;
import com.app.myapp.dao.CartInfoDao;
import com.app.myapp.dao.DBHelper;
import com.app.myapp.model.CartInfo;
import com.app.myapp.model.Cigarette;
import com.app.myapp.model.User;
import com.app.myapp.util.Constants;

public class CartInfoDaoImpl extends BaseDaoImpl<CartInfo>  implements CartInfoDao
{
	//版本号
	private static final int DBVERSION =1;
	//private static final Class<?>[] modelClasses = { User.class, User.class };
	/*
	 * 1>Context 	上下文
	 * 2>db.name  	数据库名
	 * 3>SQLiteDatabase.CursorFactory
	 * 4>DBVERSION  版本
	 * 5>modelClasses 模版
	 * 
	 */
	public CartInfoDaoImpl(Context context) {
		//super(new MyDBHelper(context,Constants.DBNAME,null,DBVERSION,modelClasses),CartInfo.class);
		super(new DBHelper(context,Constants.DBNAME,DBVERSION),CartInfo.class);
	}
	 
	@Override
	public CartInfo checkCartInfo(String field, String v) 
	{
		List<CartInfo> list=null;
		CartInfo Cart = null;
		String sql="select * from CartInfo where 1=1 ";
		if(field.equals("")){
			return Cart;
		}else {
			sql +=" and " + field +" =?"; 
			list=this.rawQuery(sql, new String[]{v});
		}
		
		//存在返回第一个
		if(list.size()>0){
			Cart = list.get(0);
		}
		return Cart;
	}
	@Override
	public List<CartInfo> getAllCartInfo(String field, String v) 
	{
		List<CartInfo> list=new ArrayList<CartInfo>();
		String sql="select * from CartInfo where 1=1 ";
		String[] selectionArgs=new String[]{};
		if(field.equals("")){
			list=this.rawQuery(sql,selectionArgs);
			return list;
		}else {
			sql +=" and " + field +" =?"; 
			list=this.rawQuery(sql, new String[]{v});
		}
		return list;
	}
	
}