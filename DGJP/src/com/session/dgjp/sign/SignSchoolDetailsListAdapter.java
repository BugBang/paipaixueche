package com.session.dgjp.sign;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.R;
import com.session.dgjp.enity.SchoolDetails;
import com.session.dgjp.enity.SignSchoolList;

import java.util.List;

public class SignSchoolDetailsListAdapter extends BXBaseAdapter<SignSchoolList.SignSchoolListModel> {

    private SignListener mSignListener;
    public SignSchoolDetailsListAdapter(List<SignSchoolList.SignSchoolListModel> listModel, Activity activity,SignListener signListener) {
        super(listModel, activity);
        this.mSignListener = signListener;
    }

    @Override
    public int getCount() {
//        return mListModel != null ? mListModel.size() : 0;
        return 5;
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, SignSchoolList.SignSchoolListModel model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sign_school_details_item, null);
        }
//        ImageView mIvClassPhoto = ViewHolder.get(convertView, R.id.iv_class_photo);
//        TextView mTvClassTitle = ViewHolder.get(convertView, R.id.tv_class_title);
//        TextView mClassSignNum = ViewHolder.get(convertView, R.id.class_sign_num);
//        TextView mTvClassDetail = ViewHolder.get(convertView, R.id.tv_class_detail);
//        TextView mTvClassMoney = ViewHolder.get(convertView, R.id.tv_class_money);
//        TextView mTvClassInstalments = ViewHolder.get(convertView, R.id.tv_class_instalments);
        Button mBtSignNow = ViewHolder.get(convertView, R.id.bt_sign_now);
        mBtSignNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignListener.onSign((SchoolDetails)v.getTag());
            }
        });
//        if (model != null) {
//            Glide.with(mActivity).load(Constants.TEST_URL+model.getSmallPhotoUrl()).into(mIvClassPhoto);
            mBtSignNow.setTag(null);
//        }
        return convertView;
    }

    public interface SignListener{
        public abstract void onSign(SchoolDetails schoolDetails);
    }
}
