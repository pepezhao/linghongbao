package com.bridge.shenzhoucheng;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.shenzhoucheng.base.BaseActivity;
import com.bridge.shenzhoucheng.info.Constants;
import com.bridge.shenzhoucheng.log.Loger;
import com.bridge.shenzhoucheng.model.UserModel;
import com.bridge.shenzhoucheng.utils.StringUtils;

public class TestMainActivity extends BaseActivity {

	private TextView two_textview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try {
			postDataTask(getJSONObject());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_test_main);

	}

	@Override
	public void findViewById() {
		// TODO Auto-generated method stub
		two_textview = (TextView) findViewById(R.id.two_textview);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public AjaxParams getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		AjaxParams jb = new AjaxParams();
		jb.put("mobile", "13512345679");
		jb.put("password", "111111");
		jb.put("token", Constants.GENERAL);

		return jb;
	}

	@Override
	public void Jump_intent(Class<?> cla, Bundle bundle) {
		// TODO Auto-generated method stub

	}

	/**
	 * 訪問接口的方法
	 * 
	 * @param url
	 * @param param
	 */
	private void postDataTask(AjaxParams para) {
		postDataTask(Constants.URL + Constants.LOGIN_URL, para, callback);
	}

	AjaxCallBack<String> callback = new AjaxCallBack<String>() {

		@Override
		public void onSuccess(String t) {
			// TODO Auto-generated method stub
			super.onSuccess(t);
			try {
				two_textview.setText(t);
				JSONObject json = new JSONObject(t);
				String s_result = json.getString("status");
				if ("1".equals(s_result)) {

					Toast.makeText(TestMainActivity.this,
							json.getString("msg"), Toast.LENGTH_SHORT).show();

					String data = json.getString("data");
					if (!StringUtils.isEmpty(data)) {
						UserModel model = gson.fromJson(data, UserModel.class);

						two_textview.setText(model.toString());
						spImp.setUsermodel(data);
						spImp.setUser_id(model.getUid());
						
					} else {

					}

				} else {
					Toast.makeText(TestMainActivity.this,
							json.getString("msg"), Toast.LENGTH_SHORT).show();
				} 

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			customProDialog.dismiss();

		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			// TODO Auto-generated method stub
			super.onFailure(t, errorNo, strMsg);

			Toast.makeText(TestMainActivity.this, "您的网络不给力～", Toast.LENGTH_LONG)
					.show();
			if (customProDialog != null) {
				customProDialog.dismiss();
			}
		}

	};
}
