package com.session.dgjp.login;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.City;

public class CityAdapter extends BaseAdapter {

	private Context mContext;
	private List<City> mList;
	
	public CityAdapter(Context context, List<City> list) {
		mContext = context;
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public City getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		City item = getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_choose_dialog, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(item.getName());
		return convertView;
	}
	
	static class ViewHolder {
		TextView tvName;
	}

}
