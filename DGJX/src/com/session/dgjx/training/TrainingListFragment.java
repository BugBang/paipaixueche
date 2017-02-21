package com.session.dgjx.training;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.DatePickerDialog;
import com.session.common.DatePickerDialog.OnDateSelectedListener;
import com.session.common.utils.DateUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderListFragment;
import com.session.dgjx.common.OptionListener;
import com.session.dgjx.enity.Order;
import com.session.dgjx.event.EventMsg;
import com.session.dgjx.event.EventTag;
import com.session.dgjx.request.OrderListRequestData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;

public class TrainingListFragment extends BaseOrderListFragment implements OnDateSelectedListener, OptionListener, TrainingAdapter.onTrainingListener {
    private TextView beginTimeTv, endTimeTv, timeTv;
    private DatePickerDialog datePickerDialog;
    private boolean firstLoadingFlag = true;

    @Override
    protected int getContentRes() {
        return R.layout.training_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        datePickerDialog = new DatePickerDialog(getActivity());
        datePickerDialog.setOnDateSelectedListener(this);
        beginTimeTv = (TextView) view.findViewById(R.id.begin_time);
        endTimeTv = (TextView) view.findViewById(R.id.end_time);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        endTimeTv.setTag(date);
        endTimeTv.setText(DateUtil.LOCAL_DATE_SDF.format(date));
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        date = calendar.getTime();
        beginTimeTv.setTag(date);
        beginTimeTv.setText(DateUtil.LOCAL_DATE_SDF.format(date));
        beginTimeTv.setOnClickListener(this);
        endTimeTv.setOnClickListener(this);
        //view.findViewById(R.id.search).setOnClickListener(this);
    }

    @Override
    protected void initAdapter() {
        adapter = new TrainingAdapter(getActivity(), this,this);
    }

    @Override
    protected OrderListRequestData getRequestData() {
        OrderListRequestData requestData = new OrderListRequestData();
        requestData.setIsFinish(OrderListRequestData.FINISHED);
        requestData.setBeginTime(DateUtil.NETWORK_DATE_SDF.format((Date) beginTimeTv.getTag()));
        requestData.setEndTime(DateUtil.NETWORK_DATE_SDF.format((Date) endTimeTv.getTag()));
        requestData.setLastRecordValue(lastRecordValue);
        return requestData;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                Date beginTime = (Date) beginTimeTv.getTag();
                Date endTime = (Date) endTimeTv.getTag();
                if (beginTime.after(endTime)) {
                    toast(R.string.begintime_before_endtime, Toast.LENGTH_SHORT);
                    break;
                }
                reload();
                break;
            case R.id.begin_time:
            case R.id.end_time:
                timeTv = (TextView) view;
                Date date = (Date) timeTv.getTag();
                if (date == null) {
                    date = new Date();
                }
                datePickerDialog.setDate(date);
                datePickerDialog.show();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onDateSelected(Date date) {
        timeTv.setTag(date);
        timeTv.setText(DateUtil.LOCAL_SIMPLE_SDF.format(date));
        reload();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstLoadingFlag) {
            firstLoadingFlag = false;
            getOrders();
        }
    }

    @Override
    public void onOptionClick(Order order) {
        Intent intent = new Intent(getActivity(), TrainingRecordActivity.class);
        intent.putExtra(Order.ID, order.getId());
        startActivity(intent);
    }

    @Override
    public void onFinishEva() {
        toastLong("该订单已经评价过了");
    }

    @Override
    public void onNoEva(Order order) {
        Intent intent = new Intent(act,EvaActivity.class);
        intent.putExtra("id",order.getId());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(EventMsg messageEvent) {
        switch (messageEvent.getImsg()) {
            case EventTag.EVENT_COMMENT_FINISH:
                reload();
                break;
        }
    }
}
