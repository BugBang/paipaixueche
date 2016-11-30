package com.session.dgjp.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.Coupon;

import java.util.List;

public class CouponAdapter extends BaseAdapter {
    private List<Coupon> list;
    private Coupon selectedCoupon;
    private LayoutInflater inflater;
    private Coupon unselectCoupon;

    public CouponAdapter(Context context, List<Coupon> list) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        unselectCoupon = new Coupon();
        unselectCoupon.setCouponId(0);
        unselectCoupon.setCouponName(context.getString(R.string.unselect_coupon));
        unselectCoupon.setDiscountPrice(0);
    }

    public List<Coupon> getList() {
        return list;
    }

    public void setList(List<Coupon> list) {
        this.list = list;
    }

    public Coupon getSelectedCoupon() {
        return selectedCoupon;
    }

    public void setSelectedCoupon(Coupon selectedCoupon) {
        this.selectedCoupon = selectedCoupon;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() + 1 : 0;
    }

    @Override
    public Coupon getItem(int position) {
        if (position == getCount() - 1) {
            return unselectCoupon;
        } else {
            return list.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getCouponId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView nameTv = null;
        CheckBox checkBox = null;
        if (convertView != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            nameTv = holder.getNameTv();
            checkBox = holder.getCheckBox();
        } else {
            convertView = inflater.inflate(R.layout.coupon_item, parent, false);
            nameTv = (TextView) convertView.findViewById(R.id._name);
            checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            ViewHolder holder = new ViewHolder(nameTv, checkBox);
            convertView.setTag(holder);
        }
        Coupon item = getItem(position);
        nameTv.setText(item.getCouponName());
        checkBox.setChecked(item.equals(selectedCoupon));
        return convertView;
    }

    private static class ViewHolder {
        private TextView nameTv;
        private CheckBox checkBox;

        public ViewHolder(TextView nameTv, CheckBox checkBox) {
            super();
            this.nameTv = nameTv;
            this.checkBox = checkBox;
        }

        public TextView getNameTv() {
            return nameTv;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

}
