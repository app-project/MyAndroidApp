package com.app.myapp.http;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpResult {
	private JSONObject _returnValue;
	private int _result;
	private String _error;
	private Date _datetime;

	private static String TAG = "HttpResult";
	
	public HttpResult() {

	}

	public Date GetDateTime() {
		return this._datetime;
	}
	
	public void SetDateTime(Date datatime) {
		this._datetime = datatime;
	}

	public void SetResult(int res) {
		this._result = res;
	}

	public void SetReturnValue(JSONObject val) {
		this._returnValue = val;
	}

	public void SetError(String error) {
		this._error = error;
	}

	public int GetResult() {
		return this._result;
	}
	//用户信息是否成功
	public boolean GetUserSuccessed() {
		if( this._result!=1){
			return false;
		}
		boolean success = false;
		try {
			success =	this.GetReturnValue().getBoolean("usersuccess");
			if(success!= true)
			{
				this.SetError(this.GetReturnValue().getString("msg"));
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getClass().toString() + "," + e.getMessage() + ","
					+ e.getStackTrace().toString());
			e.printStackTrace();
		}
		return success;
	}
	//操作是否成功
	public boolean GetActSuccessed() {
		if( this._result!=1){
			return false;
		}
		boolean success = false;
		try {
			success =	this.GetReturnValue().getBoolean("actsuccess");
			if(success!= true)
			{
				this.SetError(this.GetReturnValue().getString("msg"));
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getClass().toString() + "," + e.getMessage() + ","
					+ e.getStackTrace().toString());
			e.printStackTrace();
		}
		return success;
	}

	public JSONObject GetReturnValue() {
		return this._returnValue;
	}

	public String getError() {
		return this._error;
	}
}