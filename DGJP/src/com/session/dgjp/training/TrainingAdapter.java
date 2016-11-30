package com.session.dgjp.training;

import com.session.common.utils.DateUtil;
import com.session.dgjp.R;
import com.session.dgjp.common.BaseOrderAdapter;
import com.session.dgjp.common.OptionListener;
import com.session.dgjp.enity.Order;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrainingAdapter extends BaseOrderAdapter
{
	
	private int blue,gray;
	public TrainingAdapter(Context context, OptionListener optionListener)
	{
		super(context,optionListener);
		Resources resources = context.getResources();
		blue = resources.getColor(R.color.blue_00BFF3);
		gray = resources.getColor(R.color.gray_CCCCCC);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.training_record_item, parent, false);
			TextView courseTv = (TextView)convertView.findViewById(R.id.course);
			TextView timeTv = (TextView)convertView.findViewById(R.id.time);
			TextView trainerTv = (TextView)convertView.findViewById(R.id.trainer);
			TextView statusTv = (TextView)convertView.findViewById(R.id.status);
			TextView carnoTv = (TextView)convertView.findViewById(R.id.carno);
			statusTv.setOnClickListener(this);
			holder = new ViewHolder(courseTv, timeTv, trainerTv, statusTv, carnoTv);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		Order item = list.get(position);
		holder.getCourseTv().setText(item.getCourse().getName());
		holder.getTimeTv().setText(DateUtil.getTimePeroidString(item.getBeginTime(), item.getEndTime()));
		holder.getTrainerTv().setText(item.getTrainer().getName());
		holder.getCarnoTv().setText(item.getCar().getCarno());
		TextView statusTv = holder.getStatusTv(); 
		if(Order.STATUS_FINISHED.equals(item.getStatus()))
		{
			statusTv.setBackgroundResource(R.drawable.btn_white);
			statusTv.setTextColor(blue);
			statusTv.setEnabled(true);
			statusTv.setTag(item);
			if(Order.EVALED.equals(item.getIsEval()))
			{
				statusTv.setText(R.string.evaled);
			}else {
				statusTv.setText(R.string._eval);
			}
		}else if (Order.CONFIRM.equals(item.getNextOperate())){
			statusTv.setBackgroundResource(R.drawable.btn_white);
			statusTv.setTextColor(blue);
			statusTv.setEnabled(true);
			statusTv.setTag(item);
			statusTv.setText(item.getNextOperateName());
		}else {
			statusTv.setEnabled(false);
			statusTv.setTag(null);
			statusTv.setBackgroundResource(R.drawable.bg_gray_unable_2);
			statusTv.setTextColor(gray);
			statusTv.setText(item.getStatusName());
		}
		return convertView;
	}
	
	private class ViewHolder
	{
		private TextView courseTv, timeTv, trainerTv, statusTv, carnoTv;

		public ViewHolder(TextView courseTv, TextView timeTv, TextView trainerTv, TextView statusTv, TextView carnoTv)
		{
			super();
			this.courseTv = courseTv;
			this.timeTv = timeTv;
			this.trainerTv = trainerTv;
			this.statusTv = statusTv;
			this.carnoTv = carnoTv;
		}

		public TextView getCourseTv()
		{
			return courseTv;
		}

		public TextView getTimeTv()
		{
			return timeTv;
		}

		public TextView getTrainerTv()
		{
			return trainerTv;
		}

		public TextView getStatusTv()
		{
			return statusTv;
		}

		public TextView getCarnoTv()
		{
			return carnoTv;
		}
		
	}

}
