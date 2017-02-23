package com.session.dgjx.home;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.common.utils.TextUtil;
import com.session.dgjx.R;
import com.session.dgjx.enity.HomePager;
import com.session.dgjx.enity.Order;

import java.util.List;

/**
 * Created by user on 2017-02-14.
 */
public class HomeOrderListAdapter extends BXBaseAdapter<HomePager.ListBean> {

    private onOrderListItemListener mOnOrderListItemListener;

    public HomeOrderListAdapter(List<HomePager.ListBean> listModel, Activity activity ,onOrderListItemListener onOrderListItemListener) {
        super(listModel, activity);
        this.mOnOrderListItemListener = onOrderListItemListener;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, final HomePager.ListBean model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_student_order_list, null);
        }
        TextView mTvTime = ViewHolder.get(convertView,R.id.tv_time);
        TextView mTvTimeLong = ViewHolder.get(convertView,R.id.tv_time_long);
        ImageView mIvUserPhoto = ViewHolder.get(convertView,R.id.iv_user_photo);
        TextView mTvName = ViewHolder.get(convertView,R.id.tv_name);
        TextView mTvCallPhone = ViewHolder.get(convertView,R.id.tv_call_phone);
        Button mBtSign = ViewHolder.get(convertView,R.id.bt_sign);
        TextView mTvAddress = ViewHolder.get(convertView,R.id.tv_address);

        mTvCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOrderListItemListener.onCall(model);
            }
        });
        if (model != null){
            mTvTime.setText(String.format("%s-%s", model.getBeginTime(), model.getEndTime()));
            if (model.getNextOperate().equals(Order.STATUS_FINISHED)){
                mBtSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                mBtSign.setBackgroundResource(R.drawable.bt_gray_round);
            }else {
                mBtSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnOrderListItemListener.onSign(model);
                    }
                });
                mBtSign.setBackgroundResource(R.drawable.bt_red_round);
            }
            mBtSign.setText(model.getNextOperateName());
            if (!TextUtil.isEmpty(model.getOrderDuration())){
                double time = Double.parseDouble(model.getOrderDuration())/60;
                mTvTimeLong.setText(String.format("%s小时", time));
            }
            mTvName.setText(String.format("%s  %s", model.getStudentName(), model.getCourse()));
            mTvCallPhone.setText(model.getPhone());
            String address = model.getAddress();
            if (model.getPlaceState() == 1){
                address = address + "   需接送";
                SpannableString ss = new SpannableString(address);
                ss.setSpan(new ForegroundColorSpan(Color.RED), address.length() - 3, address.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTvAddress.setText(ss);
            }else if (model.getPlaceState() == 0){
                mTvAddress.setText(String.format("地址: %s", address));
            }

        }

        return convertView;
    }

    public interface onOrderListItemListener{
        void onCall(HomePager.ListBean model);
        void onSign(HomePager.ListBean model);
    }
}
