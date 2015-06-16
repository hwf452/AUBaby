package com.halong.aubaby.tab2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.ClassDetailEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.widget.NoScrollGridView;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhoneListActivity extends BackActivity {
	private Button mCallMainTeacher;
	private NoScrollGridView userGridView;
	private ClassDetailEntity cEntity;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonelist);

		Bundle bundle = getIntent().getExtras();
		cEntity = (ClassDetailEntity) bundle
				.getSerializable(Keys.CLASS_DETAIL_ENTITY);

		DemoApplication app = (DemoApplication) getApplicationContext();
		mImageLoader = app.getImageLoader();
		mOptions = new DisplayImageOptions.Builder().cacheOnDisc(true).build();
		userGridView = (NoScrollGridView) findViewById(R.id.userGridView);
		mCallMainTeacher = (Button) findViewById(R.id.button1);
		mCallMainTeacher.setVisibility(View.VISIBLE);
		mCallMainTeacher.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PhoneListActivity.this,
						CallAdminActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Keys.CLASS_DETAIL_ENTITY, cEntity);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
		});
		initView();

	}

	private void initView() {
		userGridView.setFocusable(false);
		userGridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				View view = convertView;
				ViewHolder holder = null;
				if (view == null) {
					holder = new ViewHolder();
					view = getLayoutInflater().inflate(R.layout.item_call_list,
							null);
					holder.headImageView = (ImageView) view
							.findViewById(R.id.headImg);
					holder.nameTextView = (TextView) view
							.findViewById(R.id.nameTxt);
					holder.numberTextView = (TextView) view
							.findViewById(R.id.numberTxt);
					holder.callButton = (Button) view
							.findViewById(R.id.callBtn);
					view.setTag(holder);

				} else {
					holder = (ViewHolder) view.getTag();
				}
				mImageLoader.displayImage(ContantUtil.PICTURE_URL
						+ cEntity.getObjinfo().getUser()[position].getImg(),
						holder.headImageView, mOptions);
				holder.nameTextView
						.setText(cEntity.getObjinfo().getUser()[position]
								.getName());
				holder.numberTextView
						.setText(cEntity.getObjinfo().getUser()[position]
								.getMobile());
				holder.callButton
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								callPhone(position);
							}

						});
				return view;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return cEntity.getObjinfo().getUser().length;
			}
		});

	}

	public static class ViewHolder {
		private ImageView headImageView;
		private TextView nameTextView, numberTextView;
		private Button callButton;
	}

	private void callPhone(int position) {
		// 取得输入的电话号码串
		String inputStr = cEntity.getObjinfo().getUser()[position].getMobile();
		// 如果输入不为空创建打电话的Intent
		if (inputStr.trim().length() != 0) {
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + inputStr));
			startActivity(phoneIntent);
		}
		// 否则Toast提示一下
		else {
			Toast.makeText(PhoneListActivity.this, R.string.call_error,
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mImageLoader.clearMemoryCache();
		mImageLoader.stop();
	}

}
