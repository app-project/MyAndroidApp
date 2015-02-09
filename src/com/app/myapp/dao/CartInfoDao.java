package com.app.myapp.dao;

import java.util.List;

import com.app.myapp.ahibernate.BaseDao;
import com.app.myapp.model.CartInfo;

public interface CartInfoDao extends BaseDao<CartInfo>{
	public CartInfo checkCartInfo(String field,String v);
	public List<CartInfo> getAllCartInfo(String field, String v) ;
}
