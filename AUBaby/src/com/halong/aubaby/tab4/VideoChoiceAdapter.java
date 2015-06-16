package com.halong.aubaby.tab4;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.halong.aubaby.R;

class GridAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mList;
	private LayoutInflater mLayoutInflater;
	private OnClickListener mOnClickListener;

	public GridAdapter(Context context, List<String> list,OnClickListener onClickListener) {
		this.mList = list;
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOnClickListener=onClickListener;
	}

	public void setmList(List<String> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(
					R.layout.item_tab4_videochoice_body_grid, null);
		}
		ImageButton imageButton = (ImageButton) convertView
				.findViewById(R.id.videoChoiceImageButton);
		imageButton.setOnClickListener(mOnClickListener);
		return convertView;
	}

}
