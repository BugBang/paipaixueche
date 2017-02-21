package com.session.dgjx.daytraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.session.common.utils.DateUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderListFragment;
import com.session.dgjx.common.OptionListener;
import com.session.dgjx.enity.Course;
import com.session.dgjx.enity.Order;
import com.session.dgjx.request.OrderListRequestData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 当日培训
 */
public class DayTrainingListFragment extends BaseOrderListFragment implements OptionListener, OnItemSelectedListener {

    private Spinner courseSpinner;
    private boolean firstLoadingFlag = true;
    private final static int START_SIGN_RQ = 2;
    private final static int FINISH_SIGN_RQ = 3;

    @Override
    protected int getContentRes() {
        return R.layout.day_training_list_fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case START_SIGN_RQ:
            case FINISH_SIGN_RQ:
                adapter.clear();
                lastRecordValue = null;
                getOrders();
                break;

            default:
                break;
        }
    }

    @Override
    public void onOptionClick(final Order order) {
        if (Order.START_SIGN.equals(order.getNextOperate())) {
            // 开始签到
            Intent intent = new Intent(act, CreateQRCodeActivity.class);
            intent.putExtra("orderId", order.getId());
            intent.putExtra("operation", Order.START_SIGN);
            startActivityForResult(intent, START_SIGN_RQ);
        } else if (Order.FINISH_SIGN.equals(order.getNextOperate())) {
            // 结束签到
            Intent intent = new Intent(act, CreateQRCodeActivity.class);
            intent.putExtra("orderId", order.getId());
            intent.putExtra("operation", Order.FINISH_SIGN);
            startActivityForResult(intent, FINISH_SIGN_RQ);
        } else {
            toast(R.string.operate_fail_update_app, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void initAdapter() {
        adapter = new DayTrainingAdapter(getActivity(), this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        // 课程
        courseSpinner = (Spinner) view.findViewById(R.id.course);
    }

    /**
     * 订单取消的监听器
     *
     * @author YJ Liang 2016 上午9:11:17
     */
    public static interface OnOrderCancelListener {
        /**
         * 订单取消成功的回调方法
         *
         * @param id 订单id
         * @author YJ Liang 2016 上午9:11:36
         */
        public void onOrderCancel(String id);
    }

    @Override
    protected OrderListRequestData getRequestData() {
        OrderListRequestData requestData = new OrderListRequestData();
        requestData.setIsFinish(OrderListRequestData.UNFINISH);
        requestData.setCourse(((Course) courseSpinner.getSelectedItem()).getCode());
        Date date = new Date();
        requestData.setBeginTime(DateUtil.NETWORK_DATE_SDF.format(date));
        requestData.setEndTime(DateUtil.NETWORK_DATE_SDF.format(date));
        requestData.setLastRecordValue(lastRecordValue);
        return requestData;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        reload();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstLoadingFlag) {
            firstLoadingFlag = false;
            List<Course> courses = new ArrayList<Course>();
            courses.add(Course.K_ALL);
            courses.add(Course.K2);
            courses.add(Course.K3);
            ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity(), R.layout.spinner_tv_2, courses);
            adapter.setDropDownViewResource(R.layout.drop_down_textview);
            courseSpinner.setAdapter(adapter);
            courseSpinner.setOnItemSelectedListener(this);
        }
    }


}
