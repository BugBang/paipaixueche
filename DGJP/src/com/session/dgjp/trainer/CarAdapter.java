package com.session.dgjp.trainer;

import java.util.List;

import com.session.common.utils.TextUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.Car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CarAdapter extends BaseAdapter
{
	private List<Car> list;
	private LayoutInflater inflater;
	private Car selectedCar;
	
	public CarAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}
	
	public void setSelectedCar(Car selectedCar)
	{
		this.selectedCar = selectedCar;
	}

	public List<Car> getList()
	{
		return list;
	}

	public void setList(List<Car> list)
	{
		this.list = list;
	}

	@Override
	public int getCount()
	{
		return list != null ? list.size() : 0;
	}

	@Override
	public Car getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return list.get(position).getCarId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.car_item, parent, false);
			CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			TextView carnoTv = (TextView)convertView.findViewById(R.id.carno);
			TextView nameTv = (TextView)convertView.findViewById(R.id._name);
			TextView gearTypeTv = (TextView)convertView.findViewById(R.id.gear_type);
			holder = new ViewHolder(carnoTv, nameTv, gearTypeTv, checkBox);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		Car item = list.get(position);
		holder.getCarnoTv().setText(item.getCarno());
		holder.getNameTv().setText(item.getEllipsisName());
		holder.getGearTypeTv().setText(item.getGearType());
		holder.getCheckBox().setChecked(item.equals(selectedCar));
		return convertView;
	}
	
	private static class ViewHolder
	{
		private TextView carnoTv, nameTv, gearTypeTv;
		private CheckBox checkBox;
		
		public ViewHolder(TextView carnoTv, TextView nameTv, TextView gearTypeTv, CheckBox checkBox)
		{
			super();
			this.carnoTv = carnoTv;
			this.nameTv = nameTv;
			this.gearTypeTv = gearTypeTv;
			this.checkBox = checkBox;
		}

		public TextView getCarnoTv()
		{
			return carnoTv;
		}

		public TextView getNameTv()
		{
			return nameTv;
		}

		public TextView getGearTypeTv()
		{
			return gearTypeTv;
		}

		public CheckBox getCheckBox()
		{
			return checkBox;
		}
		
	}

}
