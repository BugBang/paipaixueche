package com.session.dgjp.school;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.CityAndSchoole;
import com.session.dgjp.view.StarBar;

import java.util.List;

/**
 * Created by user on 2017-01-12.
 */
public class SchoolListAdapter extends BXBaseAdapter<CityAndSchoole.BranchlistBean.ListBean> {

    public SchoolListAdapter(List<CityAndSchoole.BranchlistBean.ListBean> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, CityAndSchoole.BranchlistBean.ListBean model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_school_list, null);
        }
        ImageView mIvClassPhoto = ViewHolder.get(convertView, R.id.iv_school_photo);
        TextView mTvSchoolTitle = ViewHolder.get(convertView, R.id.tv_school_title);
        TextView mSchoolLength = ViewHolder.get(convertView, R.id.tv_length);
        StarBar mStartBar = ViewHolder.get(convertView, R.id.starBar);
        TextView mTvSchoolSignNum = ViewHolder.get(convertView, R.id.tv_sign_num);

        Glide.with(mActivity).load(Constants.RELEASE_IMG_URL+model.getSmallPhotoUrl()).into(mIvClassPhoto);
        mTvSchoolTitle.setText(model.getBranchSchoolName());
        mSchoolLength.setText(String.format("距离 %skm", model.getDistance()));
        mStartBar.setStarMark(model.getScore());
        mTvSchoolSignNum.setText(String.format("%s人报名", model.getTrainerTotal()));
        return convertView;
    }
}
