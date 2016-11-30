package com.session.dgjx.order;

import com.session.common.utils.DateUtil;
import com.session.common.utils.PhotoUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderAdapter;
import com.session.dgjx.enity.Order;
import com.session.dgjx.enity.Student;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderAdapter extends BaseOrderAdapter
{
	private final static String TAG = OrderAdapter.class.getSimpleName();

	public OrderAdapter(Context context)
	{
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.student_item, parent, false);
			ImageView headIv = (ImageView) convertView.findViewById(R.id.ivHead);
			TextView studentTv = (TextView) convertView.findViewById(R.id.student);
			TextView timeTv = (TextView) convertView.findViewById(R.id.time);
			TextView courseTv = (TextView) convertView.findViewById(R.id.course);
			holder = new ViewHolder(headIv, studentTv, timeTv, courseTv);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Order item = list.get(position);
		Student student = item.getStudent();
		holder.getTimeTv().setText(DateUtil.getTimePeroidString(item.getBeginTime(), item.getEndTime()));
		holder.getStudentTv().setText(student.getName());
		holder.getCourseTv().setText(item.getCourse().getName());
		PhotoUtil.showPhoto(context, holder.getHeadIv(), student.getPhotoUrl(), R.drawable.img_def_head);
		return convertView;
	}

	private static class ViewHolder
	{
		private ImageView headIv;
		private TextView studentTv, timeTv, courseTv;

		public ViewHolder(ImageView headIv, TextView studentTv, TextView timeTv, TextView courseTv)
		{
			super();
			this.headIv = headIv;
			this.studentTv = studentTv;
			this.timeTv = timeTv;
			this.courseTv = courseTv;
		}

		public ImageView getHeadIv()
		{
			return headIv;
		}

		public TextView getTimeTv()
		{
			return timeTv;
		}

		public TextView getStudentTv()
		{
			return studentTv;
		}

		public TextView getCourseTv()
		{
			return courseTv;
		}

	}

}
