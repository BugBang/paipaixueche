package com.session.dgjx.training;

import com.session.common.utils.DateUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderAdapter;
import com.session.dgjx.common.OptionListener;
import com.session.dgjx.enity.Order;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TrainingAdapter extends BaseOrderAdapter implements OnClickListener
{
	private int blue,gray;
	private OptionListener optionListener;
	public TrainingAdapter(Context context, OptionListener optionListener)
	{
		super(context);
		Resources resources = context.getResources();
		blue = resources.getColor(R.color.blue_00BFF3);
		gray = resources.getColor(R.color.gray_CCCCCC);
		this.optionListener = optionListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.training_item, parent, false);
			TextView courseTv = (TextView)convertView.findViewById(R.id.course);
			TextView timeTv = (TextView)convertView.findViewById(R.id.time);
			TextView trainerTv = (TextView)convertView.findViewById(R.id.student);
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
		holder.getStudentTv().setText(item.getStudent().getName());
		holder.getCarnoTv().setText(item.getCar().getCarno());
		TextView statusTv = holder.getStatusTv();
		if(Order.STATUS_FINISHED.equals(item.getStatus()))
		{	
			if(Order.EVALED.equals(item.getIsEval()))
			{
				statusTv.setText(R.string.evaled);
				statusTv.setBackgroundResource(R.drawable.btn_white);
				statusTv.setTextColor(blue);
				statusTv.setEnabled(true);
				statusTv.setTag(item);
			}else {
				statusTv.setText(R.string.uneval);
				statusTv.setEnabled(false);
				statusTv.setTag(null);
				statusTv.setBackgroundResource(R.drawable.bg_gray_unable_2);
				statusTv.setTextColor(gray);
			}
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
		private TextView courseTv, timeTv, studentTv, statusTv, carnoTv;

		public ViewHolder(TextView courseTv, TextView timeTv, TextView studentTv, TextView statusTv, TextView carnoTv)
		{
			super();
			this.courseTv = courseTv;
			this.timeTv = timeTv;
			this.studentTv = studentTv;
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

		public TextView getStudentTv()
		{
			return studentTv;
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
	
	@Override
	public void onClick(View view)
	{
		Order order = (Order) view.getTag();
		if (optionListener != null) {
			optionListener.onOptionClick(order);
		}
	}

}
