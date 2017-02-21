package com.session.dgjx.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.common.utils.DateUtil;
import com.session.common.utils.PhotoUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderAdapter;
import com.session.dgjx.enity.Order;
import com.session.dgjx.enity.Student;

import java.text.SimpleDateFormat;

public class OrderAdapter extends BaseOrderAdapter {
    private final static String TAG = OrderAdapter.class.getSimpleName();
    private SimpleDateFormat mSdf;
    public OrderAdapter(Context context) {
        super(context);
        mSdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.student_item, parent, false);
            ImageView headIv = (ImageView) convertView.findViewById(R.id.ivHead);
            TextView studentTv = (TextView) convertView.findViewById(R.id.student);
            TextView timeTv = (TextView) convertView.findViewById(R.id.time);
            TextView courseTv = (TextView) convertView.findViewById(R.id.course);

            TextView time = (TextView) convertView.findViewById(R.id.tv_time);
            TextView timeLong = (TextView) convertView.findViewById(R.id.tv_time_long);
            TextView timeHour = (TextView) convertView.findViewById(R.id.tv_time_hour);
            TextView carno = (TextView) convertView.findViewById(R.id.carno);

            holder = new ViewHolder(headIv, studentTv, timeTv, courseTv ,time, timeLong, timeHour,carno);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Order item = list.get(position);
        Student student = item.getStudent();
        holder.getTimeTv().setText(DateUtil.getTimePeroidString(item.getBeginTime(), item.getEndTime()));
        holder.getStudentTv().setText(student.getName());
        holder.getCourseTv().setText(item.getCourse().getName());

        String str = mSdf.format(item.getBeginTime());
        holder.getTime().setText(str);
        holder.getTimeLong().setText(item.getTimeInterval());
        double orderDuration = item.getOrderDuration();
        holder.getTimeHour().setText(String.format("%s小时", orderDuration / 60));
        holder.getCarno().setText(item.getCar().getCarno());

        PhotoUtil.showPhoto(context, holder.getHeadIv(), student.getPhotoUrl(), R.drawable.img_def_head);
        return convertView;
    }

    private static class ViewHolder {
        private ImageView headIv;
        private TextView studentTv, timeTv, courseTv, time, timeLong, timeHour ,carno;

        public ViewHolder(ImageView headIv, TextView studentTv, TextView timeTv, TextView courseTv, TextView time, TextView timeLong, TextView timeHour,TextView carno) {
            super();
            this.headIv = headIv;
            this.studentTv = studentTv;
            this.timeTv = timeTv;
            this.courseTv = courseTv;
            this.time = time;
            this.timeLong = timeLong;
            this.timeHour = timeHour;
            this.carno = carno;
        }

        public TextView getCarno() {
            return carno;
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

        public ImageView getHeadIv() {
            return headIv;
        }

        public TextView getTimeTv() {
            return timeTv;
        }

        public TextView getStudentTv() {
            return studentTv;
        }

        public TextView getCourseTv() {
            return courseTv;
        }

    }

}
