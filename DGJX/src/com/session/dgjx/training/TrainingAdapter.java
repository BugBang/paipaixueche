package com.session.dgjx.training;

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

import java.text.SimpleDateFormat;

public class TrainingAdapter extends BaseOrderAdapter implements OnClickListener {
    private int red, gray,white;
    private OptionListener optionListener;
    private SimpleDateFormat mSdf;
    private onTrainingListener mOnTrainingListener;

    public TrainingAdapter(Context context, OptionListener optionListener,onTrainingListener onTrainingListener) {
        super(context);
        Resources resources = context.getResources();
        red = resources.getColor(R.color.app_color);
        gray = resources.getColor(R.color.gray_CCCCCC);
        white = resources.getColor(R.color.text_white);
        this.optionListener = optionListener;
        this.mOnTrainingListener = onTrainingListener;
        mSdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.training_item, parent, false);
            TextView courseTv = (TextView) convertView.findViewById(R.id.course);
            TextView timeTv = (TextView) convertView.findViewById(R.id.time);
            TextView trainerTv = (TextView) convertView.findViewById(R.id.student);
            TextView statusTv = (TextView) convertView.findViewById(R.id.status);
            TextView carnoTv = (TextView) convertView.findViewById(R.id.carno);

            TextView time = (TextView) convertView.findViewById(R.id.tv_time);
            TextView timeLong = (TextView) convertView.findViewById(R.id.tv_time_long);
            TextView timeHour = (TextView) convertView.findViewById(R.id.tv_time_hour);
            TextView eva = (TextView) convertView.findViewById(R.id.tv_eva);

            statusTv.setOnClickListener(this);
            holder = new ViewHolder(courseTv, timeTv, trainerTv, statusTv, carnoTv, time, timeLong, timeHour,eva);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Order item = list.get(position);
        holder.getCourseTv().setText(item.getCourse().getName());
        holder.getTimeTv().setText(DateUtil.getTimePeroidString(item.getBeginTime(), item.getEndTime()));
        holder.getStudentTv().setText(item.getStudent().getName());
        holder.getCarnoTv().setText(item.getCar().getCarno());

        String str = mSdf.format(item.getBeginTime());
        holder.getTime().setText(str);
        holder.getTimeLong().setText(item.getTimeInterval());
        double orderDuration = item.getOrderDuration();
        holder.getTimeHour().setText(String.format("%s小时", orderDuration / 60));
        TextView statusTv = holder.getStatusTv();
        TextView eva = holder.getEva();
        if (Order.STATUS_FINISHED.equals(item.getStatus())) {
            if (Order.EVALED.equals(item.getIsEval())) {
                statusTv.setText("已培训");
                statusTv.setBackgroundResource(R.drawable.bg_gray_finish);
                statusTv.setTextColor(white);
                statusTv.setEnabled(true);
                statusTv.setTag(item);

                eva.setText("已评价");
                eva.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnTrainingListener.onFinishEva();
                    }
                });
                eva.setVisibility(View.VISIBLE);
            } else {
                statusTv.setText("已培训");
                statusTv.setEnabled(false);
                statusTv.setTag(null);
                statusTv.setBackgroundResource(R.drawable.bg_gray_finish);
                statusTv.setTextColor(white);

                eva.setText("评价");
                eva.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnTrainingListener.onNoEva(item);
                    }
                });
                eva.setVisibility(View.VISIBLE);

            }
        } else {
            eva.setVisibility(View.GONE);
            statusTv.setEnabled(false);
            statusTv.setTag(null);
            statusTv.setBackgroundResource(R.drawable.bg_gray_unable_2);
            statusTv.setTextColor(white);
            statusTv.setText(item.getStatusName());
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView courseTv, timeTv, studentTv, statusTv, carnoTv, time, timeLong, timeHour,eva;

        public ViewHolder(TextView courseTv, TextView timeTv,
                          TextView studentTv, TextView statusTv, TextView carnoTv
                , TextView time, TextView timeLong, TextView timeHour,TextView eva) {
            super();
            this.courseTv = courseTv;
            this.timeTv = timeTv;
            this.studentTv = studentTv;
            this.statusTv = statusTv;
            this.carnoTv = carnoTv;
            this.time = time;
            this.timeLong = timeLong;
            this.timeHour = timeHour;
            this.eva = eva;
        }

        public TextView getEva() {
            return eva;
        }

        public void setEva(TextView eva) {
            this.eva = eva;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getTimeLong() {
            return timeLong;
        }

        public TextView getTimeHour() {
            return timeHour;
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

        public TextView getCarnoTv() {
            return carnoTv;
        }

    }

    @Override
    public void onClick(View view) {
        Order order = (Order) view.getTag();
        if (optionListener != null) {
            optionListener.onOptionClick(order);
        }
    }

    interface onTrainingListener{
        void onFinishEva();
        void onNoEva(Order order);
    }

}
