package com.app.myapp.dao;

import com.app.myapp.ahibernate.BaseDao;
import com.app.myapp.model.User;

public interface UserDao extends BaseDao<User>{
	//登录验证
	//public void userLogin(String loginName,String loginPassword) throws Exception;
}
