package com.session.dgjp.sign;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.SchoolDetails;

import java.util.List;

public class SignSchoolDetailsListAdapter extends BXBaseAdapter<SchoolDetails.ScListBean> {

    private SignListener mSignListener;
    public SignSchoolDetailsListAdapter(List<SchoolDetails.ScListBean> listModel, Activity activity,SignListener signListener) {
        super(listModel, activity);
        this.mSignListener = signListener;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent,SchoolDetails.ScListBean model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sign_school_details_item, null);
        }
        ImageView mIvClassPhoto = ViewHolder.get(convertView, R.id.iv_class_photo);
        TextView mTvClassTitle = ViewHolder.get(convertView, R.id.tv_class_title);
//        TextView mClassSignNum = ViewHolder.get(convertView, R.id.class_sign_num);
        TextView mTvClassDetail = ViewHolder.get(convertView, R.id.tv_class_detail);
        TextView mTvClassMoney = ViewHolder.get(convertView, R.id.tv_class_money);
//        TextView mTvClassInstalments = ViewHolder.get(convertView, R.id.tv_class_instalments);
        Button mBtSignNow = ViewHolder.get(convertView, R.id.bt_sign_now);
        mBtSignNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignListener.onSign((SchoolDetails.ScListBean)v.getTag());
            }
        });
        if (model != null) {
            Glide.with(mActivity).load(Constants.TEST_URL+model.getPhoto()).placeholder(R.drawable.placeholder_img).into(mIvClassPhoto);
            mTvClassTitle.setText(model.getClassName());
            mTvClassDetail.setText(model.getClassTitle());
            mTvClassMoney.setText("￥"+model.getClassCost());
            mBtSignNow.setTag(model);
        }
        return convertView;
    }

    public interface SignListener{
        public abstract void onSign(SchoolDetails.ScListBean scListBean);
    }
}
