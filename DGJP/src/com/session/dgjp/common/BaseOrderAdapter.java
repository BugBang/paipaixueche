package com.session.dgjp.common;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

import com.session.dgjp.enity.Order;

public abstract class BaseOrderAdapter extends BaseAdapter implements OnClickListener {

	protected List<Order> list;
	protected Context context;
	protected LayoutInflater inflater;
	protected OptionListener optionListener;

	public BaseOrderAdapter(Context context, OptionListener optionListener) {
		super();
		this.context = context;
		this.optionListener = optionListener;
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
	
	@Override
	public void onClick(View view)
	{
		Order order = (Order) view.getTag();
		if (optionListener != null) {
			optionListener.onOptionClick(order);
		}
	}
	
	

}
