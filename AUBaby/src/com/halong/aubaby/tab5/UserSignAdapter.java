package com.halong.aubaby.tab5;

import java.util.ArrayList;

import com.halong.aubaby.R;
import com.halong.aubaby.entity.UserSignListEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserSignAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<UserSignListEntity.list> mList;

	public UserSignAdapter(Context context) {
		mContext = context;
		mList = new ArrayList<UserSignListEntity.list>();

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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.include_item_student_sign, null);
		((TextView) view.findViewById(R.id.dateTextView)).setText(mList.get(
				arg0).getKey());
		for (int i = 0; i < mList.get(arg0).getValue().length; i++) {
			if (i == 0) {
				((TextView) view.findViewById(R.id.signInTextView))
						.setText(mList.get(arg0).getValue()[i].getSignTime()
								+ "   "
								+ mList.get(arg0).getValue()[i].getRemark()+"   "+mList.get(arg0).getValue()[i].getOperatorName());
			} else {
				TextView signTextView = (TextView) view
						.findViewById(R.id.signOutTextView);

				signTextView.setText(mList.get(arg0).getValue()[i]
						.getSignTime()
						+ "   "
						+ mList.get(arg0).getValue()[i].getRemark()+"   "+mList.get(arg0).getValue()[i].getOperatorName());
				signTextView.setVisibility(View.VISIBLE);
			}

		}
		return view;
	}

	public void addUserSignList(ArrayList<UserSignListEntity.list> list) {
		// TODO Auto-generated method stub
		this.mList.addAll(list);
	}

	public void clearUserList() {
		// TODO Auto-generated method stub

		this.mList.clear();
		UserSignAdapter.this.notifyDataSetChanged();
	}

	public UserSignListEntity.list getUserListLastObject() {
		// TODO Auto-generated method stub
		return mList.get(mList.size() - 1);

	}
}
