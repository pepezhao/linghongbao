package com.bridge.shenzhoucheng.login;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.shenzhoucheng.MainTabActivity;
import com.bridge.shenzhoucheng.MyApplication;
import com.bridge.shenzhoucheng.R;
import com.bridge.shenzhoucheng.base.BaseActivity;
import com.bridge.shenzhoucheng.info.Constants;
import com.bridge.shenzhoucheng.model.UserModel;
import com.bridge.shenzhoucheng.utils.StringUtils;

public class LoginPageActivity extends BaseActivity implements OnClickListener {

	//手机号
	private EditText et_Tel;
	//密码
	private EditText et_Password;
	//登录按钮
	private Button btn_Login;
	//找回密码
	private View ll_Find_Password;
	//注册
	private View ll_Register;
	//首页
	private View ll_Back_First_Page;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MyApplication.getInstance().addActivity(LoginPageActivity.this);

		String tel=spImp.getTel();
		
		if (!StringUtils.isEmpty(tel)) {
			et_Tel.setText(tel);
			//et_Password.setText(spImp.getPassword());
		}
		
		// try {
		// postDataTask(getJSONObject());
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login_page);
	}

	@Override
	public void findViewById() {
		// TODO Auto-generated method stub
		et_Tel = (EditText) findViewById(R.id.et_Tel);
		et_Password = (EditText) findViewById(R.id.et_Password);
		btn_Login = (Button) findViewById(R.id.btn_Login);
		ll_Back_First_Page=findViewById(R.id.ll_Back_First_Page);
		ll_Find_Password = findViewById(R.id.ll_Find_Password);
		ll_Register = findViewById(R.id.ll_Register);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btn_Login.setOnClickListener(this);
		ll_Back_First_Page.setOnClickListener(this);
		ll_Find_Password.setOnClickListener(this);
		ll_Register.setOnClickListener(this);
	}

	@Override
	public AjaxParams getJSONObject() throws JSONException {
		// TODO Auto-generated method stub
		AjaxParams jb = new AjaxParams();
		jb.put("mobile", et_Tel.getText().toString());
		jb.put("password", et_Password.getText().toString());
		jb.put("token", Constants.GENERAL);
		// jb.put("mobile", "13512345679");
		// jb.put("password", "111111");
		// jb.put("token", Constants.GENERAL);
		return jb;
	}

	@Override
	public void Jump_intent(Class<?> cla, Bundle bundle) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginPageActivity.this, cla);
		intent.putExtras(bundle);
		startActivity(intent);
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
				JSONObject json = new JSONObject(t);
				String s_result = json.getString("status");
				if ("1".equals(s_result)) {
					
					Toast.makeText(LoginPageActivity.this,json.getString("msg"), Toast.LENGTH_SHORT).show();
					String data = json.getString("data");
					if (!StringUtils.isEmpty(data)) {
						
						UserModel model = gson.fromJson(data, UserModel.class);
						
						System.out.println("data测试==》" + data);
						spImp.setUsermodel(data);
						
						spImp.setUser_id(model.getUid());
						System.out.println("model.getUid()测试==》"+spImp.getUser_id());
						
						System.out.println("Tel测试==》"+et_Tel.getText().toString());
						spImp.setTel(et_Tel.getText().toString());
						System.out.println("Password测试==》"+et_Password.getText().toString());
						spImp.setPassword(et_Password.getText().toString());

						// Intent intent=new
						// Intent(LoginPageActivity.this,MainTabActivity.class);
						// intent.putExtra("value",et_Tel.getText().toString()+";"+et_Password.getText().toString()+";"+model.toString()
						// );
						// //startActivity(intent);
						// startActivityForResult(intent, 10);
						
						//传值
						bundle.putString("value", et_Tel.getText().toString()+";"+et_Password.getText().toString()+";"+model.toString());
						//关闭等待状态图
						customProDialog.dismiss();
						Toast.makeText(LoginPageActivity.this, "登陆成功！",Toast.LENGTH_SHORT).show();
						//跳转页面
						Jump_intent(MainTabActivity.class, bundle);
						//关闭当前页面
						finish();
					} else {

					}
				} else {
					Toast.makeText(LoginPageActivity.this,json.getString("msg"), Toast.LENGTH_SHORT).show();
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

			Toast.makeText(LoginPageActivity.this, "您的网络不给力～",Toast.LENGTH_LONG).show();
			if (customProDialog != null) {
				customProDialog.dismiss();
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_Login:
			String tel = et_Tel.getText().toString();
			String password = et_Password.getText().toString();
			if (StringUtils.isEmpty(tel)) {
				Toast.makeText(this, "请输入手机号！", Toast.LENGTH_SHORT).show();
			} else if (!StringUtils.isPhoneNumberValid(tel)) {
				Toast.makeText(this, "请输入正确手机号！", Toast.LENGTH_SHORT).show();
			} else if (StringUtils.isEmpty(password)) {
				Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
			} else {
				try {
					customProDialog.showProDialog("登录中...");
					postDataTask(getJSONObject());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					customProDialog.dismiss();
				}
			}
//			if (tel != null && !tel.trim().equals("") && password != null
//					&& !password.trim().equals("")) {
//				try {
//					postDataTask(getJSONObject());
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else {
//				Toast.makeText(this, "请填写账号或密码!", Toast.LENGTH_SHORT).show();
//			}
			break;
		case R.id.ll_Back_First_Page:
//			Toast.makeText(this, "返回首页测试", Toast.LENGTH_SHORT).show();
			Jump_intent(MainTabActivity.class, bundle);
			finish();
			break;
		case R.id.ll_Register:
			Toast.makeText(this, "手机号注册登录测试", Toast.LENGTH_SHORT).show();
			//finish();
			break;
		case R.id.ll_Find_Password:
			Toast.makeText(this, "找回密码测试", Toast.LENGTH_SHORT).show();
			//finish();
			break;
		default:
			break;
		}
	}
}
