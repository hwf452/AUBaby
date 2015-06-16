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

public class LivesiteFragment extends FragmentToOtherActivity {

	private ListView mListView;

	private LivesiteAdapter livesiteAdapter;
	private List<String> mList;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tab4_livesite, null);

		mListView = (ListView) view.findViewById(R.id.livesiteListView);
		mList = new ArrayList<String>();
		mList.add("1");
		mList.add("1");
		mList.add("1");
		mList.add("1");

		livesiteAdapter = new LivesiteAdapter(getActivity(), mList);
		//填充头尾滚动位置
		View view1 = new View(getActivity());
		view1.setMinimumHeight((int) getResources().getDimension(R.dimen.view_smallsmallsmall_margin));
		view1.setClickable(false);
		mListView.addHeaderView(view1);
		View view2 = new View(getActivity());
		view2.setMinimumHeight((int) getResources().getDimension(R.dimen.view_smallsmallsmall_margin));
		view.setClickable(false);
		mListView.addFooterView(view2);
		
		mListView.setAdapter(livesiteAdapter);
		
		return view;
	}

}
