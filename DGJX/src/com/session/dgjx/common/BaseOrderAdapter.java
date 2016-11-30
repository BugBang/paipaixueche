package com.session.dgjx.common;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.session.dgjx.enity.Order;

public abstract class BaseOrderAdapter extends BaseAdapter {

	protected List<Order> list;
	protected Context context;
	protected LayoutInflater inflater;

	public BaseOrderAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public List<Order> getList() {
		return list;
	}

	public void setList(List<Order> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Order getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void clear() {
		if (list != null && !list.isEmpty()) {
			list.clear();
			notifyDataSetChanged();
		}
	}

}
