package com.halong.aubaby.tab1;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.halong.aubaby.BaseActivity;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.R;

public class SearchActivity extends BaseActivity implements OnClickListener {
	private Button startDateButton, endDateButton;// 弹出日历控件按钮
	private Dialog dateDialog;// 日历控件
	private EditText startDateEditText, endDateEditText;// 显示选中的时间
	private EditText searchKeyEditText;
	private DatePickerDialog.OnDateSetListener onDateSetListener;// 日历控件点击事件
	private String date;// 日历控件时间
	private Button searchButton;// 搜索按钮

	/**
	 * 搜索页面
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		startDateButton = (Button) findViewById(R.id.startDateBtn);
		startDateButton.setOnClickListener(this);
		endDateButton = (Button) findViewById(R.id.endDateBtn);
		endDateButton.setOnClickListener(this);
		startDateEditText = (EditText) findViewById(R.id.startDateEditText);
		endDateEditText = (EditText) findViewById(R.id.endDateEditText);
		searchKeyEditText = (EditText) findViewById(R.id.searchKeyEdtTXT);
		searchButton = (Button) findViewById(R.id.searchBtn);
		searchButton.setOnClickListener(this);
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startDateBtn:
		case R.id.endDateBtn:
			// 选择日期
			onDateSetListener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
					switch (v.getId()) {
					case R.id.startDateBtn:
						startDateEditText.setText(date);
						break;
					case R.id.endDateBtn:
						endDateEditText.setText(date);
						break;
					default:
						break;
					}
				}
			};
			dialogShow();
			break;
		case R.id.searchBtn:
			Intent intent = new Intent();
			intent.putExtra(Keys.SEARCH_START_DATE, startDateEditText.getText()
					.toString().trim());
			intent.putExtra(Keys.SEARCH_END_DATE, endDateEditText.getText()
					.toString().trim());
			intent.putExtra(Keys.SEARCH_KEY, searchKeyEditText.getText()
					.toString().trim());
			intent.setClass(SearchActivity.this, SearchDetailActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			break;
		default:
			break;
		}

	}

	// 显示日历控件
	private void dialogShow() {
		dateDialog = new DatePickerDialog(this,

		onDateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar
				.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(
				Calendar.DAY_OF_MONTH));
		dateDialog.show();
	}
}
