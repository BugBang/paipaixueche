package com.session.dgjx.daytraining;

import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.session.common.utils.DateUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderAdapter;
import com.session.dgjx.common.OptionListener;
import com.session.dgjx.enity.Order;

public class DayTrainingAdapter extends BaseOrderAdapter implements OnClickListener {

	private OptionListener optionListener;

	public DayTrainingAdapter(Context context, OptionListener optionListener) {
		super(context);
		this.optionListener = optionListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.day_training_item, parent, false);
			TextView courseTv = (TextView) convertView.findViewById(R.id.course);
			TextView timeTv = (TextView) convertView.findViewById(R.id.time);
			TextView studentTv = (TextView) convertView.findViewById(R.id.student);
			TextView statusTv = (TextView) convertView.findViewById(R.id.status);
			TextView operationTv = (TextView) convertView.findViewById(R.id.operation);
			holder = new ViewHolder(courseTv, timeTv, studentTv, statusTv, operationTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Order item = list.get(position);
		holder.getCourseTv().setText(item.getCourse().getName());
		Date beginTime = item.getBeginTime();
		Date endTime = item.getEndTime();
		holder.getTimeTv().setText(DateUtil.getTimePeroidString(beginTime, endTime));
		holder.getStudentTv().setText(item.getStudent().getName());
		holder.getStatusTv().setText(item.getStatusName());
		TextView operationTv = holder.getOperationTv();
		//long timeNow = System.currentTimeMillis();
		operationTv.setText(item.getNextOperateName());
		/*switch (item.getStatus()) {
		case Order.STATUS_WAITING_TRAIN: {
			int hour1 = DateUtil.hourDiff(beginTime.getTime(), timeNow);
			int hour2 = DateUtil.hourDiff(timeNow, beginTime.getTime());
			if (hour1 >= 24) {
			} else if (hour1 < 24 && hour2 < 24) {
				operationTv.setTextColor(blue);
				operationTv.setText(R.string.start_sign_in);
				operationTv.setBackgroundResource(R.drawable.bg_blue);
				item.setOperation(Order.START_SIGN);
			} else if (hour2 < 7 * 24) {
			}
			break;
		}
		case Order.STATUS_TRAINING: {
			operationTv.setTextColor(blue);
			operationTv.setBackgroundResource(R.drawable.bg_blue);
			int hour = DateUtil.hourDiff(timeNow, endTime.getTime());
			if (hour < 24) {
				operationTv.setText(R.string.end_sign_in);
				item.setOperation(Order.FINISH_SIGN);
			} else if (hour < 7 * 24) {
			} else {
				operationTv.setText(R.string.unknown);
			}
			break;
		}
		default: {
			operationTv.setTextColor(blue);
			operationTv.setText(R.string.unknown);
			operationTv.setBackgroundResource(R.drawable.bg_blue);
			break;
		}
		}*/
		operationTv.setTag(item);
		operationTv.setOnClickListener(this);
		return convertView;
	}

	private class ViewHolder {
		private TextView courseTv, timeTv, studentTv, statusTv, operationTv;

		public ViewHolder(TextView courseTv, TextView timeTv, TextView studentTv, TextView statusTv, TextView operationTv) {
			super();
			this.courseTv = courseTv;
			this.timeTv = timeTv;
			this.studentTv = studentTv;
			this.statusTv = statusTv;
			this.operationTv = operationTv;
		}

		public TextView getCourseTv() {
			return courseTv;
		}

		public TextView getTimeTv() {
			return timeTv;
		}

		public TextView getStudentTv() {
			return studentTv;
		}

		public TextView getStatusTv() {
			return statusTv;
		}

		public TextView getOperationTv() {
			return operationTv;
		}

	}

	@Override
	public void onClick(View view) {
		Order order = (Order) view.getTag();
		if (optionListener != null) {
			optionListener.onOptionClick(order);
		}
	}

}
