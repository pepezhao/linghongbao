package com.bridge.shenzhoucheng.sp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SpImp {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	public SpImp(Context context) {
		sp = context.getSharedPreferences(SpPublic.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public String getUsermodel() {
		return sp.getString(SpPublic.USERMODEL, "");
	}

	public void setUsermodel(String usermodel) {
		editor.putString(SpPublic.USERMODEL, usermodel).toString();
		editor.commit();
	}

	public String getUser_id() {
		return sp.getString(SpPublic.USER_ID, "");
	}

	public void setUser_id(String user_id) {
		editor.putString(SpPublic.USER_ID, user_id).toString();
		editor.commit();
	}

	public String getCity_name() {
		return sp.getString(SpPublic.CITY_NAME, "");
	}

	public void setCity_name(String city_name) {
		editor.putString(SpPublic.CITY_NAME, city_name).toString();
		editor.commit();
	}

	private String user_id;
	private String city_name;
	private Boolean is_guided;

	private String tel;
	private String password;

	public String getTel() {
		return sp.getString(SpPublic.TEL, "");
	}

	public void setTel(String tel) {
		editor.putString(SpPublic.TEL, tel).toString();
		editor.commit();
	}

	public String getPassword() {
		return sp.getString(SpPublic.PASSWORD, "");
	}

	public void setPassword(String password) {
		editor.putString(SpPublic.PASSWORD, password).toString();
		editor.commit();
	}
	
}
