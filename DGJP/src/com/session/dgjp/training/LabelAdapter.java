package com.session.dgjp.training;

import java.util.List;

import com.session.dgjp.R;
import com.session.dgjp.enity.Label;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class LabelAdapter extends BaseAdapter
{

	private List<Label> list;
	private Context context;
	private LayoutInflater inflater;
	private List<Label> selectedLabels;
	
	public LabelAdapter(Context context, List<Label> selectedLabels)
	{
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.selectedLabels = selectedLabels;
	}
	
	public List<Label> getList()
	{
		return list;
	}

	public void setList(List<Label> list)
	{
		this.list = list;
	}

	@Override
	public int getCount()
	{
		return list != null ? list.size() : 0;
	}

	@Override
	public Label getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.label_item, parent, false);
		}
		Label item = list.get(position);
		CheckedTextView checkedTextView = (CheckedTextView)convertView;
		checkedTextView.setText(item.getName());
		checkedTextView.setTag(item);
		checkedTextView.setChecked(selectedLabels.contains(item));
		return convertView;
	}
}
