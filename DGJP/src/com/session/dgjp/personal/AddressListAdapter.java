package com.session.dgjp.personal;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.R;
import com.session.dgjp.enity.Address;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AddressListAdapter extends BXBaseAdapter<Address.ListBean> {

    private AddressItemListener mAddressItemListener;

    public AddressListAdapter(List<Address.ListBean> listModel, Activity activity,AddressItemListener addressItemListener) {
        super(listModel, activity);
        this.mAddressItemListener = addressItemListener;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent, Address.ListBean model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_my_address, null);
        }
        ImageView mIvAddressType = ViewHolder.get(convertView, R.id.iv_address_type);
        TextView mTvAddressType = ViewHolder.get(convertView, R.id.tv_address_type);
        ImageView mIvNowSelect = ViewHolder.get(convertView,R.id.iv_now_select);
        TextView mTvPhone = ViewHolder.get(convertView, R.id.tv_phone);
        TextView mTvAddress = ViewHolder.get(convertView, R.id.tv_address);
        ImageView mIvDefaultAddress = ViewHolder.get(convertView, R.id.iv_default_address);
        TextView mTvDefaultAddress = ViewHolder.get(convertView, R.id.tv_default_address);
        TextView mTvToLocation = ViewHolder.get(convertView, R.id.tv_to_location);
        TextView mTvEditor = ViewHolder.get(convertView, R.id.tv_editor);
        TextView mTvDelete = ViewHolder.get(convertView, R.id.tv_delete);

        mIvNowSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressItemListener != null){
                    mAddressItemListener.onNowClick((Address.ListBean) view.getTag());
                }
            }
        });
        mTvDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressItemListener != null){
                    mAddressItemListener.onDefaultClick((Address.ListBean) view.getTag());
                }
            }
        });

        mTvToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressItemListener != null){
                    mAddressItemListener.onToLocaClick((Address.ListBean) view.getTag(),position);
                }
            }
        });

        mTvEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressItemListener != null){
                    mAddressItemListener.onEditorClick((Address.ListBean) view.getTag());
                }
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressItemListener != null){
                    mAddressItemListener.onDeleteClick((Address.ListBean) view.getTag());
                }
            }
        });

        if (model != null) {
            if(model.getPlace().equals("家里")){
                Picasso.with(mActivity).load(R.drawable.address_home).into(mIvAddressType);
            }else if (model.getPlace().equals("公司")){
                Picasso.with(mActivity).load(R.drawable.address_company).into(mIvAddressType);
            }else if (model.getPlace().equals("学校")){
                Picasso.with(mActivity).load(R.drawable.address_school).into(mIvAddressType);
            }else if (model.getPlace().equals("其他")){
                Picasso.with(mActivity).load(R.drawable.address_other).into(mIvAddressType);
            }
            mTvAddressType.setText(model.getPlace());
            if (model.getPlaceState() == 1){
                Picasso.with(mActivity).load(R.drawable.address_now_select).into(mIvNowSelect);
            }else {
                Picasso.with(mActivity).load(R.drawable.address_now_normal).into(mIvNowSelect);
            }
            mTvPhone.setText(model.getPhone());
            mTvAddress.setText(model.getCarAddress());
            if (model.getDefAddress() == 1){
                Picasso.with(mActivity).load(R.drawable.default_address_select).into(mIvDefaultAddress);
                mTvDefaultAddress.setText("默认地址");
                mTvDefaultAddress.setTextColor(0xffff4848);
            }else {
                Picasso.with(mActivity).load(R.drawable.default_address_normal).into(mIvDefaultAddress);
                mTvDefaultAddress.setText("设为默认");
                mTvDefaultAddress.setTextColor(0xff333333);
            }
            mIvNowSelect.setTag(model);
            mTvDefaultAddress.setTag(model);
            mTvToLocation.setTag(model);
            mTvEditor.setTag(model);
            mTvDelete.setTag(model);
        }
        return convertView;
    }

    public interface AddressItemListener{
        public abstract void onNowClick(Address.ListBean listBean);
        public abstract void onDefaultClick(Address.ListBean listBean);
        public abstract void onToLocaClick(Address.ListBean listBean,int position);
        public abstract void onEditorClick(Address.ListBean listBean);
        public abstract void onDeleteClick(Address.ListBean listBean);
    }
}
