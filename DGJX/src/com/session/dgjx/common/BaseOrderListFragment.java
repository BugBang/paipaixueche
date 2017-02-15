package com.session.dgjx.common;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.session.common.BaseFragment;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.dgjx.R;
import com.session.dgjx.enity.Order;
import com.session.dgjx.order.OrderDetailActivity;
import com.session.dgjx.request.OrderListRequestData;
import com.session.dgjx.response.OrderListResponseData;

import java.util.List;

public abstract class BaseOrderListFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener2<ListView> {
    protected PullToRefreshListView listView;
    protected BaseOrderAdapter adapter;
    protected String lastRecordValue = null;
    protected TextView noDataTv;
    //protected int endFlag = EndFlag.Flag_1.getValue();
    protected final static int GET_ORDERS_SUCCESS = 1;
    protected final static int GET_ORDERS_FAIL = 2;
    //private boolean flag = true;
    protected Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ORDERS_SUCCESS:
                    OrderListResponseData responseData = (OrderListResponseData) msg.obj;
                    List<Order> orders = responseData.getList();
                    List<Order> list = adapter.getList();
                    if (list == null) {
                        adapter.setList(orders);
                    } else {
                        list.addAll(orders);
                    }
                    adapter.notifyDataSetChanged();
                    //endFlag = responseData.getEndFlag();
                    if (orders != null && !orders.isEmpty()) {
                        setLastRecordValue(orders.get(orders.size() - 1));
                    }
                    listView.onRefreshComplete();
                    if (adapter.getList() != null && !adapter.getList().isEmpty()) {
                        noDataTv.setVisibility(View.GONE);
                    } else {
                        noDataTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case GET_ORDERS_FAIL:
                    adapter.setList(null);
                    adapter.notifyDataSetChanged();
                    //endFlag = EndFlag.Flag_1.getValue();
                    listView.onRefreshComplete();
                    BaseResponse<OrderListResponseData> response = (BaseResponse<OrderListResponseData>) msg.obj;
                    if (response != null) {
                        if (response.toLogin()) {
                            toLogin();
                        }
                        toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    }
                    noDataTv.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void init(Bundle savedInstanceState) {
        noDataTv = (TextView) view.findViewById(R.id.no_data);
        listView = (PullToRefreshListView) view.findViewById(R.id.list_view);
        listView.setMode(Mode.BOTH);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        ListView lv = listView.getRefreshableView();
        lv.setHeaderDividersEnabled(false);
        lv.setFooterDividersEnabled(false);
        lv.setDivider(getResources().getDrawable(R.drawable.divider));
        initAdapter();
        listView.setAdapter(adapter);
    }

    protected abstract void initAdapter();

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        reload();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getOrders();
    }

    protected void reload() {
        lastRecordValue = null;
        adapter.clear();
        getOrders();
    }

    protected void getOrders() {
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.SERIAL_EXECUTOR.execute(new OrderRunnable(getRequestData()));
    }

    protected abstract OrderListRequestData getRequestData();


    protected class OrderRunnable implements Runnable {
        private OrderListRequestData requestData;

        public OrderRunnable(OrderListRequestData requestData) {
            super();
            this.requestData = requestData;
        }

        @Override
        public void run() {
            try {
                BaseRequest request = new BaseRequest(requestData);
                BaseResponse<OrderListResponseData> response = request.sendRequest(OrderListResponseData.class);
                Message msg = Message.obtain();
                if (response.getCode() == BaseResponse.CODE_SUCCESS) {
                    msg.what = GET_ORDERS_SUCCESS;
                    msg.obj = response.getResponseData();
                } else {
                    msg.what = GET_ORDERS_FAIL;
                    msg.obj = response;
                }
                handler.sendMessage(msg);
            } catch (Exception e) {
                handler.sendEmptyMessage(GET_ORDERS_FAIL);
                LogUtil.e(TAG, e.toString(), e);
            } finally {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(Order.ID, ((Order) parent.getAdapter().getItem(position)).getId());
        startActivity(intent);
    }

    private void setLastRecordValue(Order order) {
        lastRecordValue = DateUtil.NETWORK_TIME_SDF.format(order.getBeginTime()) + "," + order.getId();
    }

}
