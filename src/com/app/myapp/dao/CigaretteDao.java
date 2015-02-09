package com.app.myapp.dao;

import java.util.List;

import com.app.myapp.ahibernate.BaseDao;
import com.app.myapp.model.Cigarette;

public interface CigaretteDao extends BaseDao<Cigarette>{
	public Cigarette checkCigarette(String field,String v);
	public List<Cigarette> getAllCigaretteCart(String field, String v) ;
}
