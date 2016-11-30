package com.session.dgjp.personal;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.MyCoupon;
import com.squareup.picasso.Picasso;

public class MyCouponAdapter extends BaseAdapter {

	private Context ctx;
	private List<MyCoupon> mList;

	public MyCouponAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	public void append(List<MyCoupon> list) {
		if (mList == null) {
			mList = list;
		} else {
			mList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public MyCoupon getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyCoupon coupon = getItem(position);
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(ctx, R.layout.item_coupon, null);
			holder = new ViewHolder();
			holder.ivBackground = (ImageView) convertView.findViewById(R.id.iv_background);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvRemark = (TextView) convertView.findViewById(R.id.tv_remark);
			convertView.setTag(holder);
		}
		Picasso.with(ctx).load(coupon.getBackground()).fit().into(holder.ivBackground);
		holder.tvMoney.setText(coupon.getDiscountPrice() / 100 + "");
		holder.tvTitle.setText(coupon.getCouponTitle());
		String expiryType = coupon.getExpiryType();
		String expiryValue = coupon.getExpiryValue();
		if ("T".equals(expiryType)) {
			if (expiryValue != null && expiryValue.length() >= 10) {
				holder.tvTime.setText("有效期至" + expiryValue.substring(0, 10));
				holder.tvTime.setBackgroundColor(0xaeffffff);
			}
		} else if ("D".equals(expiryType)) {
			holder.tvTime.setText("领取后" + expiryValue + "天内有效");
			holder.tvTime.setBackgroundColor(0xaeffffff);
		} else {
			holder.tvTime.setBackgroundColor(Color.TRANSPARENT);
		}
		holder.tvRemark.setText(coupon.getCouponRemark());
		return convertView;
	}

	private static class ViewHolder {

		private ImageView ivBackground;
		private TextView tvMoney, tvTitle, tvTime, tvRemark;
		
	}

}
