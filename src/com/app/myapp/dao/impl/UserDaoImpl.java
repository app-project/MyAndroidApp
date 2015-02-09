package com.app.myapp.dao.impl;

import android.content.Context;

import com.app.myapp.ahibernate.BaseDaoImpl;
import com.app.myapp.dao.DBHelper;
import com.app.myapp.dao.UserDao;
import com.app.myapp.model.User;
import com.app.myapp.util.Constants;

public class UserDaoImpl extends BaseDaoImpl<User>  implements UserDao
{
	//版本号
	private static final int DBVERSION =1;
	//private static final Class<?>[] modelClasses = { User.class, User.class };
	/*public UserDaoImpl(Context context) {
		super(new DBHelper(context,DBVERSION,modelClasses),User.class);
	}*/
	
	/*
	 * 1>Context 	上下文
	 * 2>db.name  	数据库名
	 * 3>SQLiteDatabase.CursorFactory
	 * 4>DBVERSION  版本
	 * 5>modelClasses 模版
	 * 
	 * */
	public UserDaoImpl(Context context) {
		//super(new MyDBHelper(context,Constants.DBNAME,null,DBVERSION,modelClasses),User.class);
		super(new DBHelper(context,Constants.DBNAME,DBVERSION),User.class);
	}
	
}