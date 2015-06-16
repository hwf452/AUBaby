package com.halong.aubaby.tab5;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.halong.aubaby.R;
import com.halong.aubaby.contant.ContantUtil;
import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.entity.TeacherSignListEntity;
import com.halong.aubaby.push.DemoApplication;
import com.halong.aubaby.wcf.SignService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherSignAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<TeacherSignListEntity.users> mList;
	private ImageLoader imageLoader;// 图片加载线程
	private DisplayImageOptions headOptions;// 图片加载设置
	private SignService signInService;
	private SignService signOutService;
	private Gson gson;

	public TeacherSignAdapter(Context context) {
		DemoApplication app = (DemoApplication) context.getApplicationContext();
		imageLoader = app.getImageLoader();
		headOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.head).cacheOnDisc(true).build();
		mContext = context;
		mList = new ArrayList<TeacherSignListEntity.users>();
		signInSuccess();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.include_item_teacher_sign, null);
		ImageView headImageView = (ImageView) view.findViewById(R.id.headImg);
		imageLoader
				.displayImage(ContantUtil.PICTURE_URL
						+ mList.get(arg0).getHeadPhotoURL(), headImageView,
						headOptions);
		headImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent().setClass(mContext,
						SignListActivity.class);
				intent.putExtra(Keys.USER_INFO_ID, mList.get(arg0).getId_user());
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_right_in, R.anim.push_left_out);
			}
		});
		TextView nameTextView = (TextView) view.findViewById(R.id.nameTxt);
		nameTextView.setText(mList.get(arg0).getName());

		Button signButton = (Button) view.findViewById(R.id.signBtn);
		TextView signInTextView = (TextView) view
				.findViewById(R.id.signInTextView);
		TextView signOutTextView = (TextView) view
				.findViewById(R.id.signOutTextView);
		// 显示签入信息
		if (mList.get(arg0).getSignInRemark().equals("")
				&& mList.get(arg0).getSignInTime().equals("")
				&& mList.get(arg0).getSignOutUserName().equals("")) {
			signInTextView.setText(R.string.no_sign_in);

		} else {
			signInTextView.setText(mList.get(arg0).getSignInTime() + " "
					+ mList.get(arg0).getSignInRemark() + " "
					+ mList.get(arg0).getSignInUserName());
		}
		// 显示签出信息
		if (mList.get(arg0).getSignOutTime().equals("")
				&& mList.get(arg0).getSignOutRemark().equals("")
				&& mList.get(arg0).getSignOutUserName().equals("")) {

			signOutTextView.setText(R.string.no_sign_out);

		} else {
			signOutTextView.setText(mList.get(arg0).getSignOutTime() + " "
					+ mList.get(arg0).getSignOutRemark() + " "
					+ mList.get(arg0).getSignOutUserName());

		}
		// signbutton执行的方法。如果有没有签入内容执行签入方法，之后不执行。有无签出内容执行签出方法。都有内容不执行方法
		if (mList.get(arg0).getSignInRemark().equals("")
				&& mList.get(arg0).getSignInTime().equals("")
				&& mList.get(arg0).getSignOutUserName().equals("")) {
			signButton.setText(R.string.sign_in);
			signButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					signInService.signIn(mList.get(arg0).getId_user());
				}
			});
		} else if (mList.get(arg0).getSignOutRemark().equals("")
				&& mList.get(arg0).getSignOutTime().equals("")
				&& mList.get(arg0).getSignOutUserName().equals("")) {
			signButton.setText(R.string.sign_out);
			signButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					signOutService.signOut(mList.get(arg0).getId_user());
				}
			});
		} else {
			signButton.setText(R.string.sign_in);
			signButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					signInService.signIn(mList.get(arg0).getId_user());
				}
			});
		}

		return view;
	}

	public void addTeacherSignList(ArrayList<TeacherSignListEntity.users> list) {
		// TODO Auto-generated method stub
		this.mList.addAll(list);
	}

	public void clearTeacherSignList() {
		this.mList.clear();
		TeacherSignAdapter.this.notifyDataSetChanged();
	}

	public TeacherSignListEntity.users getTeacherListLastObject() {
		// TODO Auto-generated method stub
		return mList.get(mList.size() - 1);
	}

	private void signInSuccess() {
		// TODO Auto-generated method stub
		gson = new Gson();
		signInService = new SignService(mContext) {
			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				super.getBBSpaceData(content);
				try {

					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						TeacherSignListEntity entity = gson.fromJson(content,
								TeacherSignListEntity.class);
						for (int i = 0; i < mList.size(); i++) {
							if (mList.get(i).getId_user()
									.equals(entity.getUsers()[0].getId_user())) {
								
//								Toast.makeText(
//										mContext,
//										entity.getUsers()[0].getName()
//												+ mContext
//														.getString(R.string.sign_in_success),
//										Toast.LENGTH_SHORT).show();
								mList.set(i, entity.getUsers()[0]);
								TeacherSignAdapter.this.notifyDataSetChanged();
								return;
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				super.getBBSpaceDataFailure();
				Toast.makeText(mContext, R.string.sign_in__failure,
						Toast.LENGTH_SHORT).show();
			}
		};

		signOutService = new SignService(mContext) {
			@Override
			public void getBBSpaceData(String content) {
				// TODO Auto-generated method stub
				super.getBBSpaceData(content);

				try {

					JSONObject jsonObject = new JSONObject(content);
					boolean result = jsonObject.getBoolean("result");
					if (result) {
						TeacherSignListEntity entity = gson.fromJson(content,
								TeacherSignListEntity.class);
						for (int i = 0; i < mList.size(); i++) {
							if (mList.get(i).getId_user()
							.equals(entity.getUsers()[0].getId_user())) {
//								Toast.makeText(
//										mContext,
//										entity.getUsers()[0].getName()
//												+ mContext
//														.getString(R.string.sign_out_success),
//										Toast.LENGTH_SHORT).show();
								mList.set(i, entity.getUsers()[0]);
								TeacherSignAdapter.this.notifyDataSetChanged();
								return;
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void getBBSpaceDataFailure() {
				// TODO Auto-generated method stub
				super.getBBSpaceDataFailure();
				Toast.makeText(mContext, R.string.sign_out__failure,
						Toast.LENGTH_SHORT).show();
			}
		};
	}
}
