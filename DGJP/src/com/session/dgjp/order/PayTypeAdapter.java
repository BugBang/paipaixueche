package com.session.dgjp.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.dgjp.R;
import com.session.dgjp.enity.PayType;

import java.util.List;

public class PayTypeAdapter extends BaseAdapter {
    private List<PayType> list;
    private Context context;
    private LayoutInflater inflater;
    private PayType selectedItem;

    public PayTypeAdapter(Context context, List<PayType> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public List<PayType> getList() {
        return list;
    }

    public void setList(List<PayType> list) {
        this.list = list;
    }

    public PayType getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(PayType selectedItem) {
        this.selectedItem = selectedItem;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public PayType getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iconIv = null;
        TextView nameTv = null;
        CheckBox checkBox = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pay_type_item, parent, false);
            iconIv = (ImageView) convertView.findViewById(R.id.icon);
            nameTv = (TextView) convertView.findViewById(R.id._name);
            checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            ViewHolder holder = new ViewHolder(iconIv, nameTv, checkBox);
            convertView.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            iconIv = holder.getIconIV();
            nameTv = holder.getNameTv();
            checkBox = holder.getCheckBox();
        }
        PayType item = list.get(position);
        checkBox.setChecked(item.equals(selectedItem));
        if (PayType.YUE.equals(item.getId())) {
            iconIv.setImageResource(R.drawable.balance);
            nameTv.setText(item.getPayTypeName() + "(" + context.getString(R.string.formated_money, item.getBalance() / 100.0) + ")");
            checkBox.setEnabled(true);
        } else if (PayType.ALIPAY.equals(item.getId())) {
            iconIv.setImageResource(R.drawable.alipay);
            nameTv.setText(item.getPayTypeName());
            checkBox.setEnabled(true);
        } else if (PayType.WEIXIN.equals(item.getId())) {
            iconIv.setImageResource(R.drawable.we_chat);
            nameTv.setText(item.getPayTypeName());
            checkBox.setEnabled(true);
        } else {
            iconIv.setImageDrawable(null);
            nameTv.setText(R.string.unknown);
            checkBox.setEnabled(false);
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView iconIV;
        private TextView nameTv;
        private CheckBox checkBox;
        private PayType payType;

        public ViewHolder(ImageView iconIV, TextView nameTv, CheckBox checkBox) {
            super();
            this.iconIV = iconIV;
            this.nameTv = nameTv;
            this.checkBox = checkBox;
        }

        public ImageView getIconIV() {
            return iconIV;
        }

        public TextView getNameTv() {
            return nameTv;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

}
