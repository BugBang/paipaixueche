package com.session.dgjx.order;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.PhotoUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjx.R;
import com.session.dgjx.enity.Car;
import com.session.dgjx.enity.Order;
import com.session.dgjx.enity.Student;
import com.session.dgjx.request.OrderDetailRequestData;
import com.session.dgjx.response.OrderDetailResponseData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends BaseActivity {

    private final static int GET_ORDER_SUCCESS = 1;
    private final static int GET_ORDER_FAIL = 2;
    private Order order;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ORDER_SUCCESS:
                    ImageView headIv = (ImageView) findViewById(R.id.ivHead);
                    ((TextView) findViewById(R.id.course)).setText(order.getCourse().getName());
                    Student student = order.getStudent();
                    PhotoUtil.showPhoto(OrderDetailActivity.this, headIv, student.getPhotoUrl(), R.drawable.img_def_head);
                    ((TextView) findViewById(R.id.student)).setText(student.getName());
                    ((TextView) findViewById(R.id.school)).setText(student.getBranchSchool().getName());
                    /*((TextView) findViewById(R.id.apply_time)).setText(DateUtil.NETWORK_DATE_SDF.format(student.getApplyTime()));*/
                    Date beginTime = order.getBeginTime();
                    Date endTime = order.getEndTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    ((TextView) findViewById(R.id.time)).setText(getString(R.string.selected_order_time, DateUtil.LOCAL_SIMPLE_SDF.format(beginTime), sdf.format(beginTime), sdf.format(endTime)));
                    ((TextView) findViewById(R.id.period)).setText(getString(R.string.order_hour, order.getOrderDuration() / 60));
                    ((TextView) findViewById(R.id.fee)).setText(getString(R.string.order_fee, order.getFee() / 100.0));
                    Car car = order.getCar();
                    ((TextView) findViewById(R.id.carno)).setText(car.getCarno());
                    ((TextView) findViewById(R.id._name)).setText(car.getEllipsisName());
                    ((TextView) findViewById(R.id.gear_type)).setText(car.getGearType());
                    findViewById(R.id.phone).setOnClickListener(OrderDetailActivity.this);
                    break;
                case GET_ORDER_FAIL:
                    BaseResponse<OrderDetailResponseData> response = (BaseResponse<OrderDetailResponseData>) msg.obj;
                    if (response != null) {
                        if (response.toLogin()) {
                            toLogin();
                        } else {
                            finish();
                        }
                        toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
                        finish();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.order_detail_activity);
        initTitle(R.string.order_detail, true);
        findViewById(R.id.back).setOnClickListener(this);
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取订单详情
                    OrderDetailRequestData requestData = new OrderDetailRequestData();
                    requestData.setId((String) getIntent().getStringExtra(Order.ID));
                    BaseRequest request = new BaseRequest();
                    request.setRequestData(requestData);
                    BaseResponse<OrderDetailResponseData> response = request.sendRequest(OrderDetailResponseData.class);
                    if (BaseResponse.CODE_SUCCESS == response.getCode()) {
                        order = response.getResponseData().getOrder();
                        handler.sendEmptyMessage(GET_ORDER_SUCCESS);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = GET_ORDER_FAIL;
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(GET_ORDER_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone:
                String phone = order.getStudent().getPhone();
                if (!TextUtil.isEmpty(phone)) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

}
