package com.bridge.shenzhoucheng.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bridge.shenzhoucheng.info.Constants;
import com.bridge.shenzhoucheng.net.ConnectionDetector;
import com.bridge.shenzhoucheng.sp.SpImp;
import com.google.gson.Gson;
import com.ruizhi.customlibs.CommentDialog;
import com.ruizhi.customlibs.CustomDialog;
import com.ruizhi.customlibs.CustomProDialog;
import com.ruizhi.customlibs.EditDialog;

public abstract class BaseActivity extends Activity {
	public CommentDialog commentDialog;
	public CustomDialog customDialog;
	public CustomProDialog customProDialog;
	public EditDialog editDialog;

	public ConnectionDetector cd;
	public Boolean isInternetPresent = false;
	public Resources resource;
	public String pkgName;
	public Bundle bundle;
	public static String ONLINE = "online";
	public static Gson gson;
	public SpImp spImp;
	public InputMethodManager imm;

	public FinalHttp fh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		resource = this.getResources();
		pkgName = this.getPackageName();
		setContentView();
		bundle = new Bundle();

		fh = new FinalHttp();
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();
		spImp = new SpImp(getApplicationContext());

		commentDialog = new CommentDialog(this, resource.getIdentifier(
				"MyDialog", "style", pkgName));
		customDialog = new CustomDialog(this, resource.getIdentifier(
				"MyDialog", "style", pkgName));
		customProDialog = new CustomProDialog(this, resource.getIdentifier(
				"MyDialog", "style", pkgName));
		editDialog = new EditDialog(this, resource.getIdentifier(
				"IntegralDialog", "style", pkgName));

		gson = new Gson();
		// ExecSql.DB(this);
		findViewById();
		setListener();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionReceiver, intentFilter);
		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		super.onCreate(savedInstanceState);
	}

	public abstract void setContentView();

	public abstract void findViewById();

	public abstract void setListener();

	public abstract AjaxParams getJSONObject() throws JSONException;

	public abstract void Jump_intent(Class<?> cla, Bundle bundle);

	// // 点击EditText以外的任何区域隐藏键盘
	// // @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	//
	// if (ev.getAction() == MotionEvent.ACTION_DOWN) {
	// View v = getCurrentFocus();
	// if (v != null) {
	// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	// }
	// }
	// return super.dispatchTouchEvent(ev);
	// }

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// View v = getCurrentFocus();
	// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	// }
	// return super.onTouchEvent(event);
	// }

	@Override
	protected void onDestroy() {
		if (commentDialog.isShowing()) {
			commentDialog.dismiss();
		}
		if (customDialog.isShowing()) {
			customDialog.dismiss();
		}
		if (customProDialog.isShowing()) {
			customProDialog.dismiss();
		}
		if (editDialog.isShowing()) {
			editDialog.dismiss();
		}

		if (connectionReceiver != null) {
			unregisterReceiver(connectionReceiver);
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// IntentFilter filter = new IntentFilter();
		// filter.addAction(Constants.TI_ACTION);
		// this.registerReceiver(it_receiver, filter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// this.unregisterReceiver(it_receiver);

		super.onPause();
	}

	@SuppressLint("SimpleDateFormat")
	public String getData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		return str;
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter list_height_Adapter = listView.getAdapter();
		if (list_height_Adapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < list_height_Adapter.getCount(); i++) {
			View listItem = list_height_Adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (list_height_Adapter
						.getCount() - 1));
		((MarginLayoutParams) params).setMargins(1, 1, 1, 1);
		listView.setLayoutParams(params);
	}

	private BroadcastReceiver it_receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// customDialog.showDialog("???�?",
			// "??��??�???��?��?��??设�??�???��??...", "�?�?", "???�?", false);
			// customDialog.setCancelable(false);
			//
			// shared_editor.putString("member_info", "");
			// shared_editor.putInt("isLogin", 0);
			// shared_editor.commit();
			// MyApplication.setMember_info(new MemberInfoModel());
			// customDialog.getSureBtn().setOnClickListener(new
			// OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// startActivity(new Intent(BaseActivity.this,
			// LoginActivity.class));
			// finish();
			//
			// stopService(new Intent(BaseActivity.this,
			// NewMessageNotifyService.class));
			//
			// }
			// });

		}
	};

	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			// ConnectivityManager connectMgr = (ConnectivityManager)
			// getSystemService(CONNECTIVITY_SERVICE);
			// NetworkInfo mobNetInfo = connectMgr
			// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			// NetworkInfo wifiNetInfo = connectMgr
			// .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			// if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			//
			// // unconnect network
			// isInternetPresent = false;
			// // // 断开push链接
			// // // push++
			// // Intent newIntent = new Intent();
			// // newIntent.setClass(context, PushConnectService.class);
			// // newIntent.setAction(PushConnectService.ACTION_Quit);
			// // startService(newIntent);
			// // // push end
			//
			// } else {
			// // connect network
			// if (!isInternetPresent) {
			// // if (null != MyApplication.getUserModel()) {
			// // if (!StringUtils.isEmpty(MyApplication.getUserModel()
			// // .getId())
			// // && !StringUtils.isEmpty(MyApplication
			// // .getUserModel().getUser_login_code())) {
			// // new GetLoginTypeTask().execute(ONLINE);
			// // }
			// // }
			//
			// }
			// isInternetPresent = true;
			//
			// }

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_HOME
				&& event.getRepeatCount() == 0) {
			// /* 退出推送 */
			// Intent newIntent = new Intent();
			// newIntent.setClass(BaseActivity.this, PushConnectService.class);
			// newIntent.setAction(PushConnectService.ACTION_Quit);
			// this.startService(newIntent);
		}
		return super.onKeyDown(keyCode, event);
	}


	protected void postDataTask(String url, AjaxParams ajaxparams,
			AjaxCallBack<String> callBack) {
		fh.post(url, ajaxparams, callBack);
	}
}
