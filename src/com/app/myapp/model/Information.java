package com.app.myapp.model;

import java.util.Date;

import org.json.JSONException;

import com.app.myapp.ahibernate.Column;
import com.app.myapp.ahibernate.Id;
import com.app.myapp.ahibernate.Table;
import com.app.myapp.app.MyApplication;
import com.app.myapp.util.Constants;
import com.app.myapp.util.JSONHelper;

@Table(name = "Information")
public class Information {
	// Fields
	//@Id
	@Column(name = "ID", length = 20)
	private Integer id;
	
	@Column(name = "title", length = 50)
	private String title;
	
	@Column(name = "sendtime", length = 20)
	private Date sendtime;
	
	@Column(name = "source", length = 100)
	private String source;
	
	@Column(name = "content", length = 500)
	private String content;
	
	@Column(name = "image", length = 500)
	private String image;
	
	@Column(name = "type", length = 10)
	private Integer type;
	
	@Column(name = "status", length = 10)
	private Integer status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getSendtime() {
		return sendtime;
	}
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	//保存到本地
	public void save2Local() 
	{
		MyApplication app = MyApplication.getInstance();
		String seriaziedString = JSONHelper.toJSON(this);
		app.writeString(Constants.SESSION_USER_KEY, seriaziedString);
	}

	//获得本地对象
	public Information getFromLocal() 
	{
		MyApplication app = MyApplication.getInstance();
		String objString = app.getString(Constants.SESSION_USER_KEY);
		if (objString == "null" || objString == "") {
			return null;
		}
		Information session = null;
		try {
			session = JSONHelper.parseObject(objString, Information.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return session;
	}

	//清空本地数据
	public void Clear()
	{
		MyApplication app = MyApplication.getInstance();
		app.writeString(Constants.SESSION_USER_KEY, "");
	}
	//验证用户
	public static Information checkLogin()
	{
		Information info = new  Information();
		info = info.getFromLocal();
		return info;
	}
}
