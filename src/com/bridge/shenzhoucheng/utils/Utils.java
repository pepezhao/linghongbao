package com.bridge.shenzhoucheng.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
	private static ProgressDialog pDialog = null;
	private static Context context;
	private String filePath;
	private String interimJsonPath;
	public static ProgressDialog progressDialog;

	public static final String TAG = "PushDemoActivity";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	// 获取AppKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}

		catch (Exception ex) {
		}
	}

	/**
	 * 广播接收
	 * 
	 * @author Hlw
	 * 
	 */
	public static class Message_notification extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("Message_notification----------------"
					+ intent.getAction());
			if (intent.getAction().equals("meeting_notice")) {
				Bundle bd = intent.getExtras();

				System.out.println("Message_notification----------------" + bd);
				// String id = bd.getString("id");
				// String fromuserid = bd.getString("fromuserid");// 对方
				// String fromname = bd.getString("fromname");
				// String touserid = bd.getString("touserid");// 自己
				// String message = bd.getString("message");
				// SiyuanMainMenu.showDialog(context, id, fromuserid, fromname,
				// touserid, message);
			}
			if (intent.getAction().equals("com.service.alumni_talk_num")) {
				Bundle bd = intent.getExtras();
				JSONArray mArray;
				try {
					mArray = new JSONArray(
							bd.getString("alumni_talk_message_num"));
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject obj_ = mArray.optJSONObject(i);
						// Constants.TalkCount = obj_.getString("newnum");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static ProgressDialog dialog(Activity ac) {
		progressDialog = ProgressDialog
				.show(ac, "请稍�?..", "查询�?..", true, true);
		return progressDialog;
	}

	/**
	 * 获取时间
	 * 
	 * @return
	 */
	public static String getSysNowTime(String type) {
		// Date now = new Date();
		// java.text.DateFormat format = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String formatTime = format.format(now);
		// return formatTime;
		Date now = new Date();
		java.text.DateFormat format = new java.text.SimpleDateFormat(type);
		String formatTime = format.format(now);
		return formatTime;
	}

	/**
	 * 全屏
	 * 
	 * @param ac
	 */
	public static void HideFullscreen(Activity ac) {
		ac.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ac.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 手机信息管理
	 */
	public static TelephonyManager mTelephonyManager;
	/**
	 * 网络连接状�?
	 */
	public static String netWorkState = null;
	private static String networkType = null;

	/**
	 * 获取手机信息管理
	 * 
	 * @param context
	 * @return
	 */
	private static TelephonyManager getTelephonyManager(Context context) {
		if (mTelephonyManager == null) {
			mTelephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		return mTelephonyManager;
	}

	/**
	 * 获取手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		String phoneNumber;
		phoneNumber = getTelephonyManager(context).getLine1Number();
		if (phoneNumber == null)
			phoneNumber = "-1";
		return phoneNumber;
	}

	/**
	 * dialog
	 */
	public static ProgressDialog LoadingDialog(Context context) {
		pDialog = new ProgressDialog(context);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading....");
		return pDialog;
	}

	/**
	 * 有状态栏
	 * 
	 * @param ac
	 */
	public static void HideTitle(Activity ac) {
		ac.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ac.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	}

	public static AlertDialog dialog(Context context, String str) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(str);
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface aDialog, int which) {
				aDialog.dismiss();
			}
		});
		return builder.create();
	}

	public static AlertDialog dialog1(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("保存成功");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface aDialog, int which) {
				aDialog.dismiss();
			}
		});
		return builder.create();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		if ((networkType == null) && (context != null)) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
				if (activeNetInfo != null) {
					networkType = activeNetInfo.getTypeName();
					if (networkType.equals("WIFI")) {
						networkType = String.valueOf("wifi");
					} else if (networkType.equals("mobile")) {
						if (activeNetInfo.getExtraInfo().equals("uninet")) {
							networkType = String.valueOf("uninet");
						} else if (activeNetInfo.getExtraInfo()
								.equals("uniwap")) {
							networkType = String.valueOf("uniwap");
						} else if (activeNetInfo.getExtraInfo().equals("cmwap")) {
							networkType = String.valueOf("cmwap");
						} else if (activeNetInfo.getExtraInfo().equals("cmnet")) {
							networkType = String.valueOf("cmnet");
						} else if (activeNetInfo.getExtraInfo().equals("ctwap")) {
							networkType = String.valueOf("ctwap");
						} else if (activeNetInfo.getExtraInfo().equals("ctnet")) {
							networkType = String.valueOf("ctnet");
						} else if (activeNetInfo.getExtraInfo().equals("3gwap")) {
							networkType = String.valueOf("3gwap");
						} else if (activeNetInfo.getExtraInfo().equals("3gnet")) {
							networkType = String.valueOf("3gnet");
						}
					}
				} else {
					networkType = "";
				}
			}
		}
		return networkType;
	}

	/**
	 * 判断当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			System.out.println("**** newwork is off");
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				return false;
			}
			if (info == null) {
				System.out.println("**** newwork is off");
				return false;
			} else {
				if (info.isAvailable()) {
					System.out.println("**** newwork is on");
					return true;
				}
			}
		}
		System.out.println("**** newwork is off");
		return false;
	}

	/**
	 * install Application
	 * 
	 * @param apkFile
	 * @return
	 */
	public static Intent installApplication(String apkFile) {
		Uri uri = Uri.parse(apkFile);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.setData(uri);
		it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		it.setClassName("com.android.packageinstaller",
				"com.android.packageinstaller.PackageInstallerActivity");
		return it;
		// make sure the url_of_apk_file is readable for all users
		/*
		 * Uri installUri = Uri.fromParts("package", "", null); returnIt = new
		 * Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
		 */

	}

	/**
	 * uninstall application
	 * 
	 * @param activityInfo
	 *            activityInfo.packageName
	 * @return is the activityInfo is null. true if the param activityInfo isn't
	 *         null else return false.
	 */
	public static Intent uninstallApplication(String packageName) {
		if (packageName == null) {
			return null;
		}
		Uri uri = Uri.fromParts("package", packageName, null);
		Intent it = new Intent(Intent.ACTION_DELETE, uri);
		return it;
	}

	/**
	 * String转InputStream
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream StringToInputStream(String str) {
		InputStream in = new ByteArrayInputStream(str.getBytes());
		return in;
	}

	/**
	 * 把输入流转换成字符数�?
	 * 
	 * @param inputStream
	 *            输入�?
	 * @return 字符数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bout.write(buffer, 0, len);
		}
		bout.close();
		inputStream.close();
		return bout.toByteArray();
	}

	public static void deleteApk(String fileName) {
		File updateFile = new File(fileName);
		if (updateFile.exists()) {
			// 当不�?��的时候，清除之前的下载文件，避免浪费用户空间
			updateFile.delete();
		}
	}

	/**
	 * 创建随机�?
	 * 
	 * @return
	 */
	public static int myRandom() {
		int randomNum = 0;
		randomNum = (int) (Math.random() * 9000 + 1000);
		return randomNum;
	}

	/**
	 * 后台发�?短信
	 * 
	 * @param activity
	 * @param phoneNumber
	 * @param context
	 */
	public static void backstageSmsTo(Activity activity, String phoneNumber,
			String context) {
		SmsManager sms = SmsManager.getDefault();
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(DELIVERED), 0);
		sms.sendTextMessage(phoneNumber, null, context, sentPI, deliveredPI);
	}

	public static String postUrlData(String urlstr, String para) {
		String TAG = "";
		String result = "";
		Log.d(TAG, "[postUrlData]para=[" + para + "]");
		HttpURLConnection con = null;
		try {
			if (Thread.interrupted())
				throw new InterruptedException();
			URL url = new URL(urlstr);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000 /* milliseconds */);
			con.setConnectTimeout(15000 /* milliseconds */);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			con.setUseCaches(false);
			con.setInstanceFollowRedirects(true);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.connect();
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(para);
			out.flush();
			out.close();
			if (Thread.interrupted())
				throw new InterruptedException();

			// 获取数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine = null;
			// 使用循环来读取获得的数据
			while (((inputLine = reader.readLine()) != null)) {
				result += inputLine;
			}
			reader.close();
			con.disconnect();
			Log.d(TAG, "[postUrlData]result=[" + result + "]");
			if (Thread.interrupted())
				throw new InterruptedException();

		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			result = "InterruptedException";
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return result;
	}

	/**
	 * httpPost 请求方法
	 * 
	 * @param url
	 *            地址
	 * @param parameters
	 *            post的内�?
	 * @return
	 */
	public static String httpPost(String url, String parameters) {
		String result = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			// 创建�?��连接
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 30000);
			// if (AdManager.getNetworkType(context).equals("cmwap")) {
			// HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
			// httpClient.getParams().setParameter(
			// ConnRoutePNames.DEFAULT_PROXY, proxy);
			// }
			StringEntity stringEntity = new StringEntity(parameters, HTTP.UTF_8);
			httpPost.setEntity(stringEntity);
			HttpResponse rsp = httpClient.execute(httpPost);
			int code = rsp.getStatusLine().getStatusCode();
			if (code == HttpURLConnection.HTTP_OK) {
				HttpEntity httpEntity = rsp.getEntity();
				result = EntityUtils.toString(httpEntity);
			}
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		return result;
	}

	/**
	 * 
	 * @param 待验证的字符
	 *            �?
	 * 
	 * @return 如果是符合邮箱格式的字符�?返回<b>true</b>,否则�?b>false</b>
	 */

	public static boolean isEmail(String email) {

		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		email = email.toLowerCase();
		if (email.endsWith(".con"))
			return false;
		if (email.endsWith(".cm"))
			return false;
		if (email.endsWith("@gmial.com"))
			return false;
		if (email.endsWith("@gamil.com"))
			return false;
		if (email.endsWith("@gmai.com"))
			return false;
		return match(regex, email);
	}

	/**
	 * 
	 * @param 待验证的字符
	 *            �?
	 * 
	 * @return 如果是符合网�?��式的字符�?返回<b>true</b>,否则�?b>false</b>
	 */

	public static boolean isHomepage(String str) {

		String regex = "http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*";

		return match(regex, str);

	}

	/**
	 * 
	 * @param regex
	 *            正则表达式字符串
	 * 
	 * @param str
	 *            要匹配的字符�?
	 * 
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */

	private static boolean match(String regex, String str) {

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(str);

		return matcher.matches();

	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * �?��网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		boolean newWorkOK = false;
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectManager.getActiveNetworkInfo() != null) {
			newWorkOK = true;
		}
		return newWorkOK;
	}

	// 转换dip为px
	public static int convertDipOrPx(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	// 转换px为dip
	public static int convertPxOrDip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	public static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	public static long getFolderSize(java.io.File file) throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字：包名+类名
	 * @return true 在运行, false 不在运行
	 */

	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		Log.e("IntroActivity-----", "service is running?==" + isRunning);
		return isRunning;
	}

	/**
	 * 时间比较方法
	 * 
	 * @param time_format
	 * @param date1
	 * @param date2
	 * 
	 * @return 1 date1>date2, -1 date1<date2 ,0 date1<date2
	 */
	public static int compare_date(String time_format, String date1,
			String date2) {
		DateFormat df = new SimpleDateFormat(time_format);
		try {
			java.util.Date d1 = df.parse(date1);
			java.util.Date d2 = df.parse(date2);
			if (d1.getTime() > d2.getTime()) {

				return 1;
			} else if (d1.getTime() < d2.getTime()) {

				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;

	}

	public static boolean isOneMinutes(String date1, String date2) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date begin = null;
		java.util.Date end = null;
		try {
			begin = dfs.parse(date1);
			end = dfs.parse(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long between = (Math.abs(end.getTime() - begin.getTime())) / 1000;// 除以1000是为了转换成秒

		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60 / 60;
		if (day1 == 0 && hour1 == 0 && minute1 < 1) {
			return true;
		} else {
			return false;
		}
	}

	public static String DisplayDayTime(String date) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date begin = null;
		java.util.Date end1 = null;
		java.util.Date end = new Date(System.currentTimeMillis());// 获取当前时间;
		try {
			if (date != null) {
				begin = dfs.parse(date);
				String endsString = sdf.format(end);
				end1 = sdf.parse(endsString);
			} else {
				return "";
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long between = (end1.getTime() - begin.getTime()) / 1000 + (24 * 3600);// 除以1000是为了转换成秒
		// long between1 = (end1.getTime() - begin.getTime()) / 1000;//
		// 除以1000是为了转换成秒
		Log.e("between===", "" + between);
		// Log.e("between1===", ""+between1);
		String time;
		if (between < 0) {

			time = dfs.format(begin);
			return time;
		}
		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60 / 60;

		// if (day1 == 0) {
		// day1 = between1 / (24 * 3600);
		// }else {
		// day1 = between1 / (24 * 3600);
		// }

		if (day1 == 0) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
			time = sDateFormat.format(begin);
		} else if (day1 < 7) {
			time = day1 + "天前";
		} else if (day1 > 365) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			time = sDateFormat.format(begin);
		} else {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("MM月dd日");
			time = sDateFormat.format(begin);
		}
		return time;
	}

	// 判断当前activity
	public static boolean isCurrentActivity(Context context, String activityname) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		// String shortClassName = info.topActivity.getShortClassName(); // 类名
		String className = info.topActivity.getClassName(); // 完整类名
		// String packageName = info.topActivity.getPackageName(); // 包名
		if (className.equalsIgnoreCase(activityname)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	public static String getData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String getDataTime(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = null;
		try {
			curDate = formatter.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = formatter.format(curDate);
		return str;
	}

	// dip转像素
	public static int dipToPixels(Context context, int dip) {
		final float SCALE = context.getResources().getDisplayMetrics().density;
		float valueDips = dip;
		int valuePixels = (int) (valueDips * SCALE + 0.5f);
		return valuePixels;
	}

	// 判断文件是否存在
	public static boolean isFileExit(String path) {
		if (path == null) {
			return false;
		}
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}

	// 获取根目录
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

}
