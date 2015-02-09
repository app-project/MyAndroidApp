package com.app.myapp.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.app.myapp.ahibernate.BaseDaoImpl;
import com.app.myapp.ahibernate.MyDBHelper;
import com.app.myapp.dao.CigaretteDao;
import com.app.myapp.dao.DBHelper;
import com.app.myapp.model.Cigarette;
import com.app.myapp.util.Constants;

public class CigaretteDaoImpl extends BaseDaoImpl<Cigarette>  implements CigaretteDao
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
	 * */
	public CigaretteDaoImpl(Context context) {
		//super(new MyDBHelper(context,Constants.DBNAME,null,DBVERSION,modelClasses),Cigarette.class);
		super(new DBHelper(context,Constants.DBNAME,DBVERSION),Cigarette.class);
	}
	@Override
	public Cigarette checkCigarette(String field, String v) 
	{
		List<Cigarette> list=null;
		Cigarette cigarette = null;
		String sql="select * from cigarette where 1 =1 ";
		if(field.equals("")){
			return cigarette;
		}else {
			sql +=" and " + field +" =?"; 
			list=this.rawQuery(sql, new String[]{v});
		}
		
		//存在返回第一个
		if(list.size()>0){
			cigarette = list.get(0);
		}
		return cigarette;
	}
	@Override
	public List<Cigarette> getAllCigaretteCart(String field, String v) 
	{
		List<Cigarette> list=new ArrayList<Cigarette>();
		String sql="select * from Cigarette where 1=1 ";
		if(field.equals("")){
			return list;
		}else {
			sql +=" and " + field +" =?"; 
			list=this.rawQuery(sql, new String[]{v});
		}
		return list;
	}
}