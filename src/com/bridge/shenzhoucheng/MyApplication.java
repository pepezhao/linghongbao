package com.bridge.shenzhoucheng;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.navisdk.util.common.StringUtils;
import com.bridge.shenzhoucheng.info.Constants;
import com.bridge.shenzhoucheng.sp.SpImp;
import com.google.gson.Gson;

public class MyApplication extends Application {
	public static MyApplication instance;
	private List<Activity> activityList = new LinkedList<Activity>();

	public static Boolean isUpdate = false;
	public static Boolean isSetUpdate = false;
	public static String versionCode = "V1.0.1";
	public static final int dbVersion = 1;

	public static String pak_Name;
	public static String Version_Name;
	public static String Phone_model;
	public static String Phone_system_version;
	public static String dbNam = "";
	public static String packageNameString = "";

	public static String dbNameBase = "basebuke.db";

	// 是否运行登陆页面开关
	public static Boolean is_one_login = true;
	// log开关
	public static Boolean logSwitch = true;
	public static String user_id = "";

	public SharedPreferences sp;
	public Editor editor;

	// 当前天时间戳
	public static Long CurrentTime;

	private SpImp spImp;
	private int new_collect = 0;
	private Gson gson;

	private Boolean isUpdateBoolean = false;

	private FinalDb db;
	private boolean showGuide2 = false;
	private boolean showGuide1 = false;
	private boolean downloading = false;
	public Boolean isMessage = false;

	// baiduMap++
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	// 选择纬度
	private Double Latitude = 0.0;
	// 选择精度
	private Double Longitude = 0.0;
	// 选择城市
	private String CityName = "";
	// 当前定位城市
	private String current_city_name = "";
	// 当前维度
	private Double current_Latitude = 0.0;
	// 当前精度
	private Double current_Longitude = 0.0;

	// baiduMap++ end

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// jpush++
		// JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		// JPushInterface.init(this); // 初始化 JPush
		// jpush end

		getCurrentVersion();

		sp = getSharedPreferences("bukeba", MODE_PRIVATE);
		editor = sp.edit();
		gson = new Gson();

		instance = this;

		pak_Name = getApplicationContext().getPackageName();

		// StaticFactory.IMAGE_PATH = "/RuiZhi/" + pak_Name + "/image/";
		Constants.Icon = Environment.getExternalStorageDirectory()
				+ "/CollarRedEnvelope/" + pak_Name + "/" + "icon" + "/";
		if (ExistSDCard()) {
			packageNameString = Environment.getExternalStorageDirectory()
					+ "/CollarRedEnvelope/" + pak_Name + "/";

			File file = new File(packageNameString);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		spImp = new SpImp(getApplicationContext());

		PackageInfo info = null;
		try {
			info = getApplicationContext().getPackageManager().getPackageInfo(
					pak_Name, 0);
			// int curVersionName = info.versionName;
			int curVersionCode = info.versionCode;

		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// baidu++
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());

		InitLocation();
		mLocationClient.start();
		// baidu end

	}

	// 单例实现返回MyApplication实例
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// Activity加入到List中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历每个Activity退出
	public void exit() {
		Log.e("MyApplication", "size==" + activityList.size());
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	// 遍历每个Activity退出
	public void finishActivity() {
		Log.e("MyApplication", "size==" + activityList.size());
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	private boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion() {

		try {
			PackageInfo info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			Version_Name = info.versionName;
			versionCode = "V" + Version_Name;
			Phone_model = Build.MODEL;// 手机型号
			Phone_system_version = "android " + Build.VERSION.RELEASE;// android系统版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	// baidu++
	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getCurrent_city_name() {
		return current_city_name;
	}

	public void setCurrent_city_name(String current_city_name) {
		this.current_city_name = current_city_name;
	}

	public Double getCurrent_Latitude() {
		return current_Latitude;
	}

	public void setCurrent_Latitude(Double current_Latitude) {
		this.current_Latitude = current_Latitude;
	}

	public Double getCurrent_Longitude() {
		return current_Longitude;
	}

	public void setCurrent_Longitude(Double current_Longitude) {
		this.current_Longitude = current_Longitude;
	}

	private void InitLocation() {
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			String tempcity = location.getCity();
			if (StringUtils.isEmpty(tempcity)) {
				current_city_name = "";
			} else {
				current_city_name = tempcity;
			}

			current_Latitude = location.getLatitude();
			current_Longitude = location.getLongitude(); 
			CityName = spImp.getCity_name();	

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			mLocationClient.stop();
			Log.i("BaiduLocationApiDem", sb.toString());
		}

	}
	// baiduMap END
}
