package com.session.dgjx.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.dgjx.R;
import com.session.dgjx.common.BaseOrderListFragment;
import com.session.dgjx.enity.Course;
import com.session.dgjx.enity.OrderDate;
import com.session.dgjx.request.OrderListRequestData;
import com.session.dgjx.request.TeachingDateScheduleRequestData;
import com.session.dgjx.response.TeachingDateScheduleResponseData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderListFragment extends BaseOrderListFragment implements OnItemSelectedListener {

    private Spinner courseSpinner, dateSpinner;
    private final static int GET_ORDER_DATE_SUCCESS = 1;
    private final static int GET_ORDER_DATE_FAIL = 2;
    private Handler orderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ORDER_DATE_SUCCESS: {
                    List<OrderDate> orderDates = (List<OrderDate>) msg.obj;
                    orderDates.add(0, new OrderDate(null));
                    initDateSpinner(orderDates);
                    break;
                }
                case GET_ORDER_DATE_FAIL: {
                    BaseResponse<TeachingDateScheduleResponseData> response = (BaseResponse<TeachingDateScheduleResponseData>) msg.obj;
                    if (response != null) {
                        if (response.toLogin()) {
                            toLogin();
                        } else {
                            generateEmptyOrderDate();
                        }
                        toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        generateEmptyOrderDate();
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    }
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };

    @Override
    protected int getContentRes() {
        return R.layout.order_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        //日期
        dateSpinner = (Spinner) view.findViewById(R.id.date);
        List<OrderDate> orderDates = new ArrayList<OrderDate>();
        orderDates.add(0, new OrderDate(null));
        ArrayAdapter<OrderDate> dateAdapter = new ArrayAdapter<OrderDate>(getActivity(), R.layout.spinner_tv_2, orderDates);
        dateAdapter.setDropDownViewResource(R.layout.drop_down_textview);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setSelection(0, true);
        dateSpinner.setOnItemSelectedListener(this);
        //课程
        courseSpinner = (Spinner) view.findViewById(R.id.course);
        List<Course> courses = new ArrayList<Course>();
        courses.add(Course.K_ALL);
        courses.add(Course.K2);
        courses.add(Course.K3);
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity(), R.layout.spinner_tv_2, courses);
        adapter.setDropDownViewResource(R.layout.drop_down_textview);
        courseSpinner.setAdapter(adapter);
        courseSpinner.setOnItemSelectedListener(this);

    }

    private void initDateSpinner(List<OrderDate> orderDates) {
        ArrayAdapter<OrderDate> dateAdapter = new ArrayAdapter<OrderDate>(getActivity(), R.layout.spinner_tv_2, orderDates);
        dateAdapter.setDropDownViewResource(R.layout.drop_down_textview);
        dateSpinner.setAdapter(dateAdapter);
    }

    private void getOrderDates(final String course) {
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TeachingDateScheduleRequestData requestData = new TeachingDateScheduleRequestData();
                    requestData.setCourse(course);
                    BaseRequest request = new BaseRequest();
                    request.setRequestData(requestData);
                    BaseResponse<TeachingDateScheduleResponseData> response = request.sendRequest(TeachingDateScheduleResponseData.class);
                    Message msg = Message.obtain();
                    if (BaseResponse.CODE_SUCCESS == response.getCode()) {
                        msg.what = GET_ORDER_DATE_SUCCESS;
                        msg.obj = response.getResponseData().getList();
                    } else {
                        msg.what = GET_ORDER_DATE_FAIL;
                        msg.obj = response;
                    }
                    orderHandler.sendMessage(msg);
                } catch (Exception e) {
                    orderHandler.sendEmptyMessage(GET_ORDER_DATE_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    progressDialog.dismiss();
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.course:
                getOrderDates(((Course) parent.getAdapter().getItem(position)).getCode());
                break;
            case R.id.date:
                reload();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * 预约成功的回调接口
     *
     * @author YJ Liang
     *         2016  上午8:55:05
     */
    public static interface OnOrderSuccessListener extends Serializable {
        public abstract void OnOrderSuccess();
    }

    @Override
    protected void initAdapter() {
        adapter = new OrderAdapter(getActivity());
    }

    @Override
    protected OrderListRequestData getRequestData() {
        OrderListRequestData requestData = new OrderListRequestData();
        requestData.setIsFinish(OrderListRequestData.UNFINISH);
        requestData.setCourse(((Course) courseSpinner.getSelectedItem()).getCode());
        Date date = ((OrderDate) dateSpinner.getSelectedItem()).getDate();
        if (date != null) {
            requestData.setBeginTime(DateUtil.NETWORK_DATE_SDF.format(date));
            requestData.setEndTime(DateUtil.NETWORK_DATE_SDF.format(date));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            requestData.setBeginTime(DateUtil.NETWORK_DATE_SDF.format(calendar.getTime()));
        }
        requestData.setLastRecordValue(lastRecordValue);
        return requestData;
    }

    private void generateEmptyOrderDate() {
        List<OrderDate> orderDates = new ArrayList<OrderDate>();
        orderDates.add(0, new OrderDate(null));
        initDateSpinner(orderDates);
    }

}
