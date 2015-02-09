package com.app.myapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;

import android.R.string;
import android.util.Log;

import com.app.myapp.ahibernate.Column;
import com.app.myapp.ahibernate.Id;
import com.app.myapp.ahibernate.Table;
import com.app.myapp.app.MyApplication;
import com.app.myapp.util.Constants;
import com.app.myapp.util.JSONHelper;

@Table(name = "User")
public class User implements Serializable {
	// Fields
	//@Id
	@Column(name = "ID", length = 20)
	private Integer id;
	
	@Column(name = "validity", length = 10)
	private int validity;
	
	@Column(name = "loginName", length = 50)
	private String loginName;
	
	@Column(name = "password", length = 50)
	private String password;
	
	@Column(name = "name", length = 30)
	private String name;
	
	@Column(name = "sex", length = 10)
	private String sex;
	
	@Column(name = "age", length = 10)
	private Integer age;
	
	@Column(name = "birthday", length = 20)
	private Date birthday;
	
	@Column(name = "phone", length = 20)
	private String phone;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "address", length = 500)
	private String address;
	
	@Column(name = "pic", length = 500)
	private String pic;
	
	@Column(name = "regdatetime", length = 20)
	private Date regdatetime;
	
	@Column(name = "type", length = 10)
	private Integer type;
	
	@Column(name = "status", length = 10)
	private Integer status;
	
	@Column(name = "updatetime", length = 20)
	private Date updatetime;
	
	@Column(name = "pwdversion", length = 10)
	private Integer pwdversion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Date getRegdatetime() {
		return regdatetime;
	}
	public void setRegdatetime(Date regdatetime) {
		this.regdatetime = regdatetime;
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
	
	/**
	 * @return the updatetime
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
	/**
	 * @param updatetime the updatetime to set
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * @return the pwdversion
	 */
	public Integer getPwdversion() {
		return pwdversion;
	}
	/**
	 * @param pwdversion the pwdversion to set
	 */
	public void setPwdversion(Integer pwdversion) {
		this.pwdversion = pwdversion;
	}
	
	/**
	 * @return the validity
	 */
	public int getValidity() {
		return validity;
	}
	/**
	 * @param validity the validity to set
	 */
	public void setValidity(int validity) {
		this.validity = validity;
	}
	
	private final static String TAG="com.app.myapp.model.user";
	//保存到本地
	public void save2Local(String json) 
	{
		String seriaziedString ="";
		MyApplication app = MyApplication.getInstance();
		if("".equals(json.trim())){
			 seriaziedString = JSONHelper.toJSON(this);
		}else {
			seriaziedString = json;
		}
		app.writeString(Constants.SESSION_USER_KEY, seriaziedString);
	}

	//获得本地User对象
	public User getFromLocal() 
	{
		MyApplication app = MyApplication.getInstance();
		String objString = app.getString(Constants.SESSION_USER_KEY);
		if (objString == "null" || objString == "") {
			return null;
		}
		User session = null;
		try {
			session = JSONHelper.parseObject(objString, User.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return session;
	}

	//清空本地用户数据
	public static void Clear()
	{
		MyApplication app = MyApplication.getInstance();
		app.writeString(Constants.SESSION_USER_KEY, "");
	}
	//验证用户
	public static User checkLogin()
	{
		User user = new  User();
		try {
			user = user.getFromLocal();
			//用户是否有效期内
			if(user == null || !checkuserLastDate(user)){
				user=null;
			}
		} catch (Exception e) {
			Log.e(TAG,e.toString());
			user=null;
		}
		
		return user;
	}
	
	@SuppressWarnings("deprecation")
	private static boolean checkuserLastDate(User user)
	{
		Date dt=new Date();
		int Validity;
		Date Updatetime=null;
		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US); 
			Date dtDate =null;
        	//CHINA
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);   
            //ENGLISH
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
			//获得密码有效时间
			Validity = user.getValidity();
			Updatetime = user.getUpdatetime();
			if(Validity > 0 && Updatetime!=null){
				Updatetime.setDate(Updatetime.getDate()+Validity);
				try {
	            	dtDate = sdf.parse(Updatetime.toString());
				} catch (Exception e) {
					dtDate = sdf1.parse(Updatetime.toString());
				}
				//System.out.println(dtDate.getTime());
				//System.out.println(dt.getTime());
				//System.out.println(dtDate.getTime()-dt.getTime());
				//getTime 方法返回一个整数值，这个整数代表了从 1970 年 1 月 1 日开始计算到 Date 对象中的时间之间的毫秒数。
				if(dtDate.getTime()-dt.getTime()>=0){
					return true;
				}
			}else{
				Log.e(TAG+"checkuserLastDate","true");
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG+"checkuserLastDate",e.toString());
			return false;
		}
		return false;
	}
}
