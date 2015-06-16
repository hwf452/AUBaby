package com.halong.aubaby.tab4;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.widget.NoScrollGridView;
import com.halong.aubaby.R;

public class VideoChoiceFragment extends FragmentToOtherActivity implements
		 OnClickListener {

	private ListView mListView;
	private List<String> mList;
	private ImageButton mTab4VideoHeaderImageButton;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab4_videochoice, null);
		mListView = (ListView) view.findViewById(R.id.vidoeListView);

		mList = new ArrayList<String>();
		mList.add("1");
		mList.add("1");
		mList.add("1");
		mList.add("1");

		mListView.addHeaderView(inflater.inflate(
				R.layout.item_tab4_videochoice_today, null));

		mListView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				NoScrollGridView gridView;
				if (convertView == null) {
					convertView = inflater.inflate(
							R.layout.item_tab4_videochoice_body, null);
					gridView = (NoScrollGridView) convertView
							.findViewById(R.id.tab4VidoeGridView);

					gridView.setAdapter(new GridAdapter(getActivity(), mList,
							VideoChoiceFragment.this));

				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mList.size();
			}
		});

		mTab4VideoHeaderImageButton = (ImageButton) mListView
				.findViewById(R.id.tab4VideoHeaderImageButton);
		mTab4VideoHeaderImageButton.setOnClickListener(this);
		return view;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.v("VideoChoiceFragment", v.getId() + "--------onClick");
		switch (v.getId()) {
		case R.id.tab4VideoHeaderImageButton:
			toOtherActivity(VideoActivity.class);
			break;
		case R.id.videoChoiceImageButton:
			toOtherActivity(VideoActivity.class);
			break;

		default:
			break;
		}
	}



}
