package com.session.dgjp.school;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.R;
import com.session.dgjp.enity.CityAndSchoole;

import java.util.List;

/**
 * Created by user on 2017-01-12.
 */
public class SchoolCityAdapter extends BXBaseAdapter<CityAndSchoole.CountlistBean.ListBean> {

    private int mPosition = -100;

    public SchoolCityAdapter(List<CityAndSchoole.CountlistBean.ListBean> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, CityAndSchoole.CountlistBean.ListBean model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_school_city, null);
        }
        TextView tvCity = ViewHolder.get(convertView, R.id.tv_school_city);
        if (mPosition != -100 && mPosition == position) {
            tvCity.setBackgroundResource(R.color.white);
        } else {
            tvCity.setBackgroundResource(R.color.gray_bg);
        }
        tvCity.setText(model.getCounty());
        return convertView;
    }

    public void setItemColor(int position) {
        this.mPosition = position;
    }
}
