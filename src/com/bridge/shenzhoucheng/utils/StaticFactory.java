package com.bridge.shenzhoucheng.utils;

import android.os.Environment;

public class StaticFactory {

	public static String SD_PATH = Environment.getExternalStorageDirectory()
			.getPath();
	// public static String IMAGE_PATH="/RuiZhi/"+Constants.pak_Name+"/image/";
	public static String IMAGE_PATH = "/Bukeba/";
	public static String Icon;
	// public static String IMAGE_PATH="/youyou/";
	// public static String IMAGE_SU=".youyou";
	public static String UESP = "UESharedPreferences";
	public static String ISCONTACTBROADCAST = "isContactBroadcast";
	public static String USERNAME = "userName";

	public static String DATA_SP = "data_sp";
	public static String NEW_CURSOR = "new_cursor";// 最顶id
	public static String BREAKPOINT_CURSOR = "breakpoint_cursor";// 断点id
																	// 和最顶id没全获取
	public static String OLD_CURSOR = "old_cursor";// 断点id 和最顶id没全获取
	public static String LAST_ID = "last_id";// 分页最后的id

}
