package com.app.myapp.dao;



import android.content.Context;

import com.app.myapp.ahibernate.MyDBHelper;
import com.app.myapp.model.CartInfo;
import com.app.myapp.model.Cigarette;
import com.app.myapp.model.User;

public class DBHelper extends MyDBHelper {
	
	/*
	 *1. 购物车明细表
	 *2. 香烟表
	 *3. 用户表
	 * */
	private static final Class<?>[] clazz = { CartInfo.class, Cigarette.class,User.class };
	
	public DBHelper(Context context,String DBNAME,int DBVERSION) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}
}
