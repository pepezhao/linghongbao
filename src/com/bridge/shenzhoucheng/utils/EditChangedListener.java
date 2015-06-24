package com.bridge.shenzhoucheng.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bridge.shenzhoucheng.log.Loger;

public class EditChangedListener implements TextWatcher {
	private String TAG = "EditChangedListener";
	private Context mContext;
	private CharSequence temp;// 监听前的文本
	private int editStart;// 光标开始位置
	private int editEnd;// 光标结束位置
	private int charMaxNum = 10;

	private EditText edit;
	private TextView text;
	private Boolean is_textview = false;

	public EditChangedListener(Context mContext, int num, EditText editText,
			TextView textView) {
		this.mContext = mContext;
		if (num != 0) {
			this.charMaxNum = num;
		}

		this.edit = editText;
		if (null != textView) {
			this.text = textView;
			is_textview = true;
		}else {
			is_textview = false;
		}

	}

	public EditChangedListener(Context mContext, int num, EditText editText) {
		this.mContext = mContext;
		if (num != 0) {
			this.charMaxNum = num;
		}

		this.edit = editText;
		is_textview = false;

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

		Loger.e(TAG, "输入文本之前的状态");
		temp = s;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		Loger.e(TAG, "输入文字中的状态，count是一次性输入字符数");
		if (is_textview) {
			text.setText("还能输入" + (charMaxNum - s.length()) + "字符");
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

		Loger.e(TAG, "输入文字后的状态");
		/** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
		editStart = edit.getSelectionStart();
		editEnd = edit.getSelectionEnd();
		Loger.e(TAG, "editStart===" + editStart);
		Loger.e(TAG, "editEnd===" + editEnd);
		if (editStart == editEnd) {
			editStart = charMaxNum;
		}
		if (temp.length() > charMaxNum) {
			Toast.makeText(mContext, "你输入的字数已经超过了限制！", Toast.LENGTH_LONG)
					.show();
//			s.delete(charMaxNum-1, temp.length()-1);
//			s.subSequence(0, charMaxNum-1);
			int tempSelection = editStart;
			edit.setText(s.subSequence(0, charMaxNum));
			edit.setSelection(charMaxNum);
		}

	}
};