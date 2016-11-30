package com.session.dgjp.personal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.WalletRecord;

public class WalletRecordAdapter extends BaseAdapter {

	private Context ctx;
	private List<WalletRecord> mList;
	private List<String> mYearMonthList;

	public WalletRecordAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public void append(List<WalletRecord> list) {
		if (mList == null) {
			mList = list;
			mYearMonthList = new ArrayList<String>();
		} else {
			mList.addAll(list);
		}
		for (int i = 0; i < list.size(); i++) {
			String logTime = list.get(i).getLogTime();
			if (logTime != null && logTime.length() >= 7) {
				String yearMonth = logTime.substring(0, 7);
				// 如果没有该年月，则将其添加到mList中
				if (!mYearMonthList.contains(yearMonth)) {
					mYearMonthList.add(yearMonth);
					WalletRecord record = new WalletRecord();
					record.setYearMonth(yearMonth);
					mList.add(mList.size() - list.size() + i, record);
				}
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public WalletRecord getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WalletRecord record = getItem(position);
		String yearMonth = record.getYearMonth();
		if (yearMonth != null) {
			View view = View.inflate(ctx, R.layout.item_wallet_month, null);
			TextView tvMonth = (TextView) view.findViewById(R.id.tv_month);
			String[] split = yearMonth.split("-");
			tvMonth.setText(split[0] + "年" + split[1] + "月");
			return view;
		}
		ViewHolder holder;
		if (convertView != null && convertView instanceof LinearLayout) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(ctx, R.layout.item_wallet_record, null);
			holder = new ViewHolder();
			holder.tvTypeName = (TextView) convertView.findViewById(R.id.tv_type_name);
			holder.tvLogTime = (TextView) convertView.findViewById(R.id.tv_log_time);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);
		}
		holder.tvTypeName.setText(record.getTypeName());
		holder.tvLogTime.setText(record.getLogTime());
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		if (record.getMoney() < 0) {
			holder.tvMoney.setText(decimalFormat.format(record.getMoney() / 100));
			holder.tvMoney.setTextColor(ctx.getResources().getColor(R.color.red));
		} else {
			holder.tvMoney.setText("+" + decimalFormat.format(record.getMoney() / 100));
			holder.tvMoney.setTextColor(ctx.getResources().getColor(R.color.blue_00BFF3));
		}
		return convertView;
	}

	private static class ViewHolder {

		private TextView tvTypeName, tvLogTime, tvMoney;
	}

}
