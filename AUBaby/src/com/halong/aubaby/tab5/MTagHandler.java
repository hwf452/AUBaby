package com.halong.aubaby.tab5;

import java.util.List;
import java.util.Map;

import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.tab1.OtherUserInfoActivity;
import com.halong.aubaby.tab1.ShuoShuoDetailActivity;
import com.halong.aubaby.R;

public class MTagHandler implements TagHandler {

	private Context mContext;
	private int mCount = 0;
	private MTagEntity entity;

	public MTagHandler(Context context, MTagEntity entity) {
		mContext = context;
		this.entity = entity;
	}

	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		// TODO Auto-generated method stub
		if (tag.toLowerCase().equals("img")) {
			if (opening) {

			} else {
				output.setSpan(new ImageClickSpan(), entity.mList.get(mCount),
						entity.mList.get(mCount + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				mCount = +2;
			}
		}
		if (tag.toLowerCase().equals("read")) {
			if (opening) {

			} else {
				output.setSpan(new ReadClickSpan(), entity.mList.get(mCount),
						entity.mList.get(mCount + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				mCount = +2;
			}
		}
	}

	private class ImageClickSpan extends ClickableSpan implements OnClickListener {

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext,OtherUserInfoActivity.class);
			intent.putExtra(Keys.USER_INFO_ID, entity.mMap.get("id"));
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			

		}
	}

	private class ReadClickSpan extends ClickableSpan implements OnClickListener {

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			if (entity.isS) {
				Intent intent = new Intent(mContext,NoticeActivity.class);
				intent.putExtra("id", Integer.parseInt(entity.mMap.get("notice")));
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			}else {
				Intent intent = new Intent(mContext,ShuoShuoDetailActivity.class);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			}
		}
	}

	public static class MTagEntity {
		public List<Integer> mList;
		public Map<String, String> mMap;
		public boolean isS;
	}

}
