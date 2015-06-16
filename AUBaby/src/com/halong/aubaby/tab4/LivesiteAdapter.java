package com.halong.aubaby.tab4;

import java.util.List;

import com.halong.aubaby.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

public class LivesiteAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mList;
	private LayoutInflater mLayoutInflater;

	public LivesiteAdapter(Context context, List<String> list) {
		this.mList = list;
		this.mContext = context;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView = mLayoutInflater.inflate(R.layout.item_tab4_livesite,
					null);
		}

		RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.livesiteRelativeLayout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("LivesiteAdapter", "getView"+position);
			}
		});
		return convertView;
	}

}
