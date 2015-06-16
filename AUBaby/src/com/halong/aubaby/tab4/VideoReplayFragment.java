package com.halong.aubaby.tab4;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.halong.aubaby.FragmentToOtherActivity;
import com.halong.aubaby.R;

public class VideoReplayFragment extends FragmentToOtherActivity {

	private ListView mListView;
	private List<String> mList;
	private VideoReplayAdapter mNoticeReplayAdapter;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab5_ontice_replay_list, null);
		mList = new ArrayList<String>();
		mListView = (ListView) view.findViewById(R.id.onlyListView);
		
		getData();
		
		
		View headView =inflater.inflate(R.layout.item_tab5_replay1, null);
		mListView.addHeaderView(headView);
		
		
		mNoticeReplayAdapter = new VideoReplayAdapter(getActivity(), mList);
		mListView.setAdapter(mNoticeReplayAdapter);
		return view;
	}

	private void getData() {
		// TODO Auto-generated method stub
		mList.add("1");
		mList.add("1");
		mList.add("1");
		mList.add("1");
		mList.add("1");
		mList.add("1");
	}

}
