package com.session.common;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.session.dgjx.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class DatePickerDialog extends Dialog {
	private final static String TAG = DatePickerDialog.class.getSimpleName();
	private Calendar calendar = Calendar.getInstance();
	private DatePicker datePicker;
	private Button confirmBtn;
	private TextView dateTv;
	private OnDateSelectedListener onDateSelectedListener;
	private OnDateChangedListener onDateChangedListener = new OnDateChangedListener()
	{
		@Override
		public void onDateChanged(DatePicker view, int year, int month,
				int day)
		{
			calendar.set(year, month, day);
			String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CHINA);
			dateTv.setText(getContext().getString(R.string.date_format,year,month+1,day,dayOfWeek));
		}
	};
	public DatePickerDialog(Context context) {
		super(context,R.style.date_pick_dialog);
		init();
	}
	
	private void init()
	{
		//View view = View.inflate(context, R.layout.date_picker_dialog, null);
		setContentView(R.layout.date_picker_dialog);
		datePicker = (DatePicker)findViewById(R.id.date_picker);
		datePicker.setMaxDate(System.currentTimeMillis());
		dateTv = (TextView)findViewById(R.id.date);
		findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				cancel();
			}
		});
		confirmBtn = (Button)findViewById(R.id.confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Date date = getDate();
				if(onDateSelectedListener != null)
				{
					onDateSelectedListener.onDateSelected(date);
				}
				cancel();
			}
		});
		//this.setView(view);
	}
	
	public void setDate(Date date)
	{
		calendar.setTime(date);
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), onDateChangedListener);
		String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CHINA);
		dateTv.setText(getContext().getString(R.string.date_format,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE),dayOfWeek));
	}
	
	public int getYear()
	{
		return datePicker.getYear();
	}
	
	public int getMonth()
	{
		return datePicker.getMonth();
	}
	
	public int getDayOfMonth()
	{
		return datePicker.getDayOfMonth();
	}
	
	
	public Date getDate()
	{
		calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
		return calendar.getTime();
	}
	
	public void setOnConfirmListener(View.OnClickListener onClickListener)
	{
		if(onClickListener != null)
		{
			confirmBtn.setOnClickListener(onClickListener);
		}
	}
	
	public OnDateSelectedListener getOnDateSelectedListener()
	{
		return onDateSelectedListener;
	}

	public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener)
	{
		this.onDateSelectedListener = onDateSelectedListener;
	}

	public static interface OnDateSelectedListener
	{
		public abstract void onDateSelected(Date date);
	}
	
	
}
