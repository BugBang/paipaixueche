package com.session.dgjp.trainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.Teaching;

import java.util.List;

public class TeachingAdapter extends BaseAdapter {
    private List<Teaching> list;
    private Context context;
    private LayoutInflater inflater;
    private List<Teaching> teachings;//可预约时间
    List<Teaching> selectedTeachings;

    public void setTeachings(List<Teaching> teachings) {
        this.teachings = teachings;
    }

    public TeachingAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public List<Teaching> getList() {
        return list;
    }

    public void setList(List<Teaching> list) {
        this.list = list;
    }

    public void setSelectedTeachings(List<Teaching> selectedTeachings) {
        this.selectedTeachings = selectedTeachings;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Teaching getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.period_tv, parent, false);
        }
        Teaching item = list.get(position);
        CheckedTextView periodTv = (CheckedTextView) convertView;
        periodTv.setText(context.getString(R.string.period_hour, item.getTimeSlot().get(0)));
        periodTv.setTag(item);
        if (teachings != null) {
            boolean flag = false;
            //比较学员选择的课程和教练安排的课程，如果不一致，则按钮设为enabled=false
            for (int i = 0; i < teachings.size(); i++) {
                Teaching teaching = teachings.get(i);
                //比较日期，如果不一致，则按钮设为enabled=false;
                if (teaching.getTeachingTime().equals(item.getTeachingTime())) {
                    List<Integer> timeSlot = teaching.getTimeSlot();
                    Integer slot = item.getTimeSlot().get(0);
                    for (int j = 0; j < timeSlot.size(); j++) {
                        if (slot.equals(timeSlot.get(j))) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            periodTv.setEnabled(flag);
        } else {
            periodTv.setEnabled(false);
        }
        periodTv.setChecked(selectedTeachings.contains(item));
        return convertView;
    }

}
