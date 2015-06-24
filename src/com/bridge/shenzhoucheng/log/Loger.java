package com.bridge.shenzhoucheng.log;

import android.util.Log;

import com.bridge.shenzhoucheng.MyApplication;

public class Loger {

	// private static boolean debug = true;

	public static void e(String tag, String error) {
		if (MyApplication.logSwitch) {
			Log.e(tag, error);
		}
	}

	public static void i(String tag, String info) {
		if (MyApplication.logSwitch) {
			Log.i(tag, info);
		}
	}
}
