package com.bridge.shenzhoucheng;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.bridge.shenzhoucheng.homepage.HomePageActivity;
import com.bridge.shenzhoucheng.me.MeActivity;
import com.bridge.shenzhoucheng.paybill.PayBillActivity;
import com.bridge.shenzhoucheng.redmall.RedMallActivity;

public class MainTabActivity extends TabActivity {
	private Context mContext;

	private TabHost mTabHost;

	//测试登陆页传值
//	private TextView tv_value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(MainTabActivity.this);

		setContentView(R.layout.activity_maintab);

		//接值
//		tv_value=(TextView) findViewById(R.id.tv_value);
//		Intent intent=getIntent();
//		tv_value.setText(intent.getStringExtra("value"));
		
		mContext = MainTabActivity.this;

		mTabHost = getTabHost();

		// 加载底部Tab布局
		RelativeLayout tab1 = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.maintab_home_page, null);

		RelativeLayout tab2 = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.maintab_red_mall, null);
		RelativeLayout tab3 = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.maintab_pay_bill, null);
		RelativeLayout tab4 = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.maintab_me, null);

		mTabHost.setup(getLocalActivityManager());

		TabSpec ts1 = mTabHost.newTabSpec("tab_one");
		ts1.setIndicator(tab1);// 这句话就是设置每个小tab显示的内容
		// 第1个Activity
		// ts1.setContent(new Intent(this, HomePageActivity.class));
		ts1.setContent(new Intent(this, HomePageActivity.class));
		mTabHost.addTab(ts1);

		TabSpec ts2 = mTabHost.newTabSpec("tab_two");
		ts2.setIndicator(tab2);
		// 第2个Activity
		ts2.setContent(new Intent(this, RedMallActivity.class));
		mTabHost.addTab(ts2);

		TabSpec ts3 = mTabHost.newTabSpec("tab_three");
		ts3.setIndicator(tab3);
		// 第3个Activity
		ts3.setContent(new Intent(this, PayBillActivity.class));
		mTabHost.addTab(ts3);

		TabSpec ts4 = mTabHost.newTabSpec("tab_four");
		ts4.setIndicator(tab4);
		// 第4个Activity
		ts4.setContent(new Intent(this, MeActivity.class));
		mTabHost.addTab(ts4);

		final TabWidget tabWidget = mTabHost.getTabWidget();

		/************ 去白钱 **********************/
		tabWidget.setStripEnabled(false);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				// new GetDataTask().execute(ONLINE);
				// if (tabId.equals("tab_four")) {
				// if (message_new_imageview.getVisibility() == View.VISIBLE) {
				// message_new_imageview.setVisibility(View.GONE);
				// MyApplication.getInstance().isMessage = false;
				// }
				// }
			}
		});

		mTabHost.setCurrentTab(0);
		// mTabHost.setCurrentTab(StringUtils.toInt(current_page));

	}

}
