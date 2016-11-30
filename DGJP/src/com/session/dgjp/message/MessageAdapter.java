package com.session.dgjp.message;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.MyMessage;

public class MessageAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<MyMessage> mList;
	private static SimpleDateFormat sdf;
	
	static {
		DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance();
		dateFormatSymbols.setAmPmStrings(new String[] { "上午", "下午" });
		sdf = new SimpleDateFormat("MM月dd日 a hh:mm", Locale.getDefault());
		sdf.setDateFormatSymbols(dateFormatSymbols);
	}

	public MessageAdapter(Context ctx) {
		mContext = ctx;
	}

	public MessageAdapter(Context ctx, List<MyMessage> list) {
		mContext = ctx;
		mList = list;
	}

	public List<MyMessage> getmList() {
		return mList;
	}

	public void setmList(List<MyMessage> mList) {
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList.get(position).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Holder holder;
		if (null == view) {
			view = View.inflate(mContext, R.layout.it_message, null);
			holder = new Holder();
			holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
			holder.tvMsg = (TextView) view.findViewById(R.id.tvMsg);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		MyMessage msg = mList.get(position);
		holder.tvTime.setText(sdf.format(msg.getSendTime()));
		holder.tvMsg.setText(msg.getContent());
		return view;
	}

	private class Holder {
		TextView tvTime;
		TextView tvMsg;
	}

}
