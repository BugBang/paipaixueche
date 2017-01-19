package com.session.dgjp.school;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.Coach;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.view.StarBar;

import java.util.List;

/**
 * Created by user on 2017-01-09.
 */
public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.ViewHolder> {
    private int mItemWidth;
    private Context mContext;
    private Coach mCoach;

    public CoachListAdapter(Coach coach, Context context) {
        this.mCoach = coach;
        this.mContext = context;
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mItemWidth = dm.widthPixels / 3;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coach_list, viewGroup, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mItemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mCoach!=null) {
            Glide.with(mContext).load(Constants.RELEASE_IMG_URL + mCoach.getList().get(position).getHeadImg()).into(holder.mIvCoachPhoto);
            holder.mTvName.setText(String.format("%s", mCoach.getList().get(position).getTrainerName()));
            holder.mTvYear.setText(String.format("%d", mCoach.getList().get(position).getAge()));
            holder.mTvSex.setText(String.format("%s", mCoach.getList().get(position).getSex()));
            holder.mCoachStart.setStarMark((float) mCoach.getList().get(position).getScore());
            holder.mTvDistance.setText("0");
            holder.mTvMoney.setText(String.format("%d", mCoach.getList().get(position).getFee()/100));
            holder.mBtOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnOrderClickListener != null) {
                        mOnOrderClickListener.onOrderClick(mCoach.getList().get(position).getTrainerIdcard(),position);
                    }
                }
            });
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if (mCoach != null && mCoach.getList() != null && mCoach.getList().size() > 0) {
            return mCoach.getList().size();
        }
        return 0;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvCoachPhoto;
        public TextView mTvName;
        public TextView mTvYear;
        public TextView mTvSex;
        public StarBar mCoachStart;
        public TextView mTvDistance;
        public TextView mTvMoney;
        public Button mBtOrder;

        public ViewHolder(View view) {
            super(view);
            mIvCoachPhoto = (ImageView) view.findViewById(R.id.iv_coach_photo);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvYear = (TextView) view.findViewById(R.id.tv_year);
            mTvSex = (TextView) view.findViewById(R.id.tv_sex);
            mCoachStart = (StarBar) view.findViewById(R.id.coach_start);
            mTvDistance = (TextView) view.findViewById(R.id.tv_distance);
            mTvMoney = (TextView) view.findViewById(R.id.tv_money);
            mBtOrder = (Button) view.findViewById(R.id.bt_order);
        }
    }

    public void setCoach(Coach coach) {
        this.mCoach = coach;
    }

    public static interface OnOrderClickListener {
        void onOrderClick(String id,int position);
    }

    private OnOrderClickListener mOnOrderClickListener = null;

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.mOnOrderClickListener = listener;
    }
    private List<Trainer> list;
    public void setList(List<Trainer> list) {
        this.list = list;
    }
}
