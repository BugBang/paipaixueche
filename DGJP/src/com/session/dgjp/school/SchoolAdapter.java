package com.session.dgjp.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.session.common.utils.PhotoUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class SchoolAdapter extends BaseAdapter implements Filterable, OnClickListener
{
	private List<BranchSchool> list;
	private List<BranchSchool> filteredList;
	private LayoutInflater inflater;
	private Context context;
	private Filter filter;
	private OrderListener orderListener;
	
	public SchoolAdapter(Context context, OrderListener orderListener)
	{
		super();
		this.context = context;
		this.orderListener = orderListener;
		this.inflater = LayoutInflater.from(context);
	}
	
	public List<BranchSchool> getList()
	{
		return list;
	}

	public void setList(List<BranchSchool> list)
	{
		this.list = list;
	}

	@Override
	public int getCount()
	{
		return filteredList != null ? filteredList.size() : 0;
	}

	@Override
	public BranchSchool getItem(int position)
	{
		return filteredList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return filteredList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.school_item, parent, false);
			ImageView photoIv = (ImageView)convertView.findViewById(R.id.photo);
			TextView schoolTv = (TextView)convertView.findViewById(R.id.school);
			TextView phoneTv = (TextView)convertView.findViewById(R.id.phone);
			TextView addressTv = (TextView)convertView.findViewById(R.id.address); 
			ImageView hintIv = (ImageView)convertView.findViewById(R.id.hint); 
			TextView operationTv = (TextView)convertView.findViewById(R.id.operation);
			TextView distanceTv = (TextView)convertView.findViewById(R.id.distance);
			operationTv.setOnClickListener(this);
			viewHolder = new ViewHolder(photoIv, hintIv, schoolTv, phoneTv, addressTv, operationTv, distanceTv);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		BranchSchool item = filteredList.get(position);
		PhotoUtil.showRoundCornerPhoto(context, viewHolder.getPhotoIv(), item.getSmallPhotoUrl(), R.drawable.def_school);
		viewHolder.getSchoolTv().setText(item.getName());
		viewHolder.getPhoneTv().setText(item.getEllipsisPhone());
		viewHolder.getAddressTv().setText(item.getEllipsisAddress());
		viewHolder.getOperationTv().setTag(item);
		ImageView hintIv = viewHolder.getHintIv();
		if(BranchSchool.TYPE_APPLY.equals(item.getBranchSchoolType()))
		{
			hintIv.setImageResource(R.drawable.apply_school);
			hintIv.setVisibility(View.VISIBLE);
		}else if(BranchSchool.TYPE_LATEST.equals(item.getBranchSchoolType())){
			hintIv.setImageResource(R.drawable.last_order);
			hintIv.setVisibility(View.VISIBLE);
		}else {
			hintIv.setVisibility(View.INVISIBLE);
		}
		double distance = item.getDistance();
		TextView distanceTv = viewHolder.getDistanceTv();
		if(distance<0)
		{
			distanceTv.setVisibility(View.INVISIBLE);
		}else {
			distanceTv.setText(context.getString(R.string.distance_km, distance));
			distanceTv.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		private ImageView photoIv, hintIv;
		private TextView schoolTv, phoneTv, addressTv, operationTv, distanceTv;
		
		public ViewHolder(ImageView photoIv, ImageView hintIv, TextView schoolTv, TextView phoneTv, TextView addressTv, TextView operationTv, TextView distanceTv)
		{
			super();
			this.photoIv = photoIv;
			this.hintIv = hintIv;
			this.schoolTv = schoolTv;
			this.phoneTv = phoneTv;
			this.addressTv = addressTv;
			this.operationTv = operationTv;
			this.distanceTv = distanceTv;
		}
		
		public ImageView getPhotoIv()
		{
			return photoIv;
		}
		public TextView getSchoolTv()
		{
			return schoolTv;
		}
		public TextView getPhoneTv()
		{
			return phoneTv;
		}
		public TextView getAddressTv()
		{
			return addressTv;
		}
		public ImageView getHintIv()
		{
			return hintIv;
		}
		public TextView getOperationTv()
		{
			return operationTv;
		}
		public TextView getDistanceTv()
		{
			return distanceTv;
		}
	}

	@Override
	public Filter getFilter()
	{
		if(filter == null)
		{
			filter = new Filter()
			{
				@Override
				protected FilterResults performFiltering(CharSequence constraint)
				{
					FilterResults results = new FilterResults();
					List<BranchSchool> filteredList = null;
					String constraintStr = null;
					if(constraint == null || (constraintStr=constraint.toString().trim()).isEmpty())
					{
						filteredList = list;
					}else {
						constraint = constraintStr.toUpperCase(Locale.SIMPLIFIED_CHINESE);
						if(list != null)
						{
							filteredList = new ArrayList<BranchSchool>();
							int size = list.size();
							for(int i=0; i<size; i++)
							{
								BranchSchool item = list.get(i);
								if(item != null && item.getName() != null && item.getName().toUpperCase(Locale.SIMPLIFIED_CHINESE).contains(constraint))
								{
									filteredList.add(item);
								}
							}
						}
					}
					results.values = filteredList;
					if(filteredList != null)
					{
						results.count = filteredList.size();
					}else {
						results.count = 0;
					}
					return results;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results)
				{
					filteredList = (List<BranchSchool>)results.values;
					notifyDataSetChanged();
				}
			};
		}
		return filter;
	}
	
	/**
	 * 预约按钮的回调接口
	 * @author YJ Liang
	 * 2016  上午8:55:35
	 */
	public interface OrderListener{
		public abstract void onOrder(BranchSchool branchSchool);
	}

	@Override
	public void onClick(View view)
	{
		BranchSchool branchSchool = (BranchSchool)view.getTag();
		if(orderListener != null)
		{
			orderListener.onOrder(branchSchool);
		}
	}


}
