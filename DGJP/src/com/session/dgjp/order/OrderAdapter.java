package com.session.dgjp.order;

import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.session.common.utils.DateUtil;
import com.session.dgjp.R;
import com.session.dgjp.common.BaseOrderAdapter;
import com.session.dgjp.common.OptionListener;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.Trainer;

public class OrderAdapter extends BaseOrderAdapter{

	public OrderAdapter(Context context, OptionListener optionListener) {
		super(context,optionListener);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.order_item, parent, false);
			TextView courseTv = (TextView) convertView.findViewById(R.id.course);
			TextView timeTv = (TextView) convertView.findViewById(R.id.time);
			TextView trainerTv = (TextView) convertView.findViewById(R.id.trainer);
			TextView statusTv = (TextView) convertView.findViewById(R.id.status);
			TextView operationTv = (TextView) convertView.findViewById(R.id.operation);
			operationTv.setOnClickListener(this);
			holder = new ViewHolder(courseTv, timeTv, trainerTv, statusTv, operationTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Order item = list.get(position);
		holder.getCourseTv().setText(item.getCourse().getName());
		Date beginTime = item.getBeginTime();
		Date endTime = item.getEndTime();
		holder.getTimeTv().setText(DateUtil.getTimePeroidString(beginTime, endTime));
		Trainer trainer = item.getTrainer();
		holder.getTrainerTv().setText(trainer.getName());
		holder.getStatusTv().setText(item.getStatusName());
		TextView operationTv = holder.getOperationTv();
		/*long timeNow = System.currentTimeMillis();
		switch (item.getStatus()) {
			case Order.STATUS_WAITING_TRAIN: {
				int hour1 = DateUtil.hourDiff(beginTime.getTime(), timeNow);
				int hour2 = DateUtil.hourDiff(timeNow, beginTime.getTime());
				if (hour1 >= 24) {
					//operationTv.setTextColor(red);
					operationTv.setText(R.string.cancel_order);
					//operationTv.setBackgroundResource(R.drawable.bg_red);
					item.setOperation(Order.CANCEL_ORDER);
				} else if (hour1 < 24 && hour2 < 24) {
					//operationTv.setTextColor(blue);
					operationTv.setText(R.string.start_sign_in);
					//operationTv.setBackgroundResource(R.drawable.bg_blue);
					item.setOperation(Order.START_SIGN);
				} else {
					//operationTv.setTextColor(blue);
					operationTv.setText(R.string.confirm_order);
					//operationTv.setBackgroundResource(R.drawable.bg_blue);
					item.setOperation(Order.CONFIRM);
				}
				break;
			}
			case Order.STATUS_TRAINING: {
				//operationTv.setTextColor(blue);
				//operationTv.setBackgroundResource(R.drawable.bg_blue);
				int hour = DateUtil.hourDiff(timeNow, endTime.getTime());
				if (hour < 24) {
					operationTv.setText(R.string.end_sign_in);
					item.setOperation(Order.FINISH_SIGN);
				} else {
					operationTv.setText(R.string.confirm_order);
					item.setOperation(Order.CONFIRM);
				}
				break;
			}
			case Order.STATUS_PAYMENT:
			{
				operationTv.setText(R.string.pay);
				item.setOperation(Order.PAY);
				break;
			}
			default: {
				//operationTv.setTextColor(blue);
				operationTv.setText(R.string.unknown);
				//operationTv.setBackgroundResource(R.drawable.bg_blue);
				break;
			}
		}*/
		operationTv.setText(item.getNextOperateName());
		operationTv.setTag(item);
		return convertView;
	}

	private class ViewHolder {
		private TextView courseTv, timeTv, trainerTv, statusTv, operationTv;

		public ViewHolder(TextView courseTv, TextView timeTv, TextView trainerTv, TextView statusTv, TextView operationTv) {
			super();
			this.courseTv = courseTv;
			this.timeTv = timeTv;
			this.trainerTv = trainerTv;
			this.statusTv = statusTv;
			this.operationTv = operationTv;
		}

		public TextView getCourseTv() {
			return courseTv;
		}

		public TextView getTimeTv() {
			return timeTv;
		}

		public TextView getTrainerTv() {
			return trainerTv;
		}

		public TextView getStatusTv() {
			return statusTv;
		}

		public TextView getOperationTv() {
			return operationTv;
		}

	}

}
