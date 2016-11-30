package com.session.dgjp.training;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.session.common.BaseDialog;
import com.session.common.BaseRequestTask;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.common.BaseOrderListFragment;
import com.session.dgjp.common.OptionListener;
import com.session.dgjp.enity.Order;
import com.session.dgjp.order.PaidOrderDetailActivity;
import com.session.dgjp.request.OperateOrderRequestData;
import com.session.dgjp.request.OrderListRequestData;

public class TrainingListFragment extends BaseOrderListFragment implements OptionListener {

    private Order currentOrder;
    private final static int COMMENT_RQ = 1;

    @Override
    protected int getContentRes() {
        return R.layout.training_list_fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COMMENT_RQ:
                if (Activity.RESULT_OK == resultCode) {
                    currentOrder.setIsEval(Order.EVALED);
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    @Override
    protected void initAdapter() {
        adapter = new TrainingAdapter(getActivity(), this);
    }

    @Override
    protected String getFinishFlag() {
        return OrderListRequestData.FINISHED;
    }


    @Override
    public void onOptionClick(final Order order) {
        currentOrder = order;
        if (Order.STATUS_FINISHED.equals(order.getStatus())) {
            Intent intent = new Intent(getActivity(), TrainingEvaluationActivity.class);
            intent.putExtra(Order.ID, order.getId());
            intent.putExtra(Order.EVAL_STATUS, order.getIsEval());
            startActivityForResult(intent, COMMENT_RQ);
        } else {
            //copy from OrderListFragment
            BaseDialog dialog = new BaseDialog.Builder(act).setTitle("确认订单").setMessage("练车时未签到，为保证正常计费，请下次签到")
                    .setPositiveButton("确定", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            operateOrder(order.getId(), OperateOrderRequestData.CONFIRM);
                            dialog.cancel();
                        }
                    }).setNegativeButton("取消", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
        Intent intent = new Intent(getActivity(), PaidOrderDetailActivity.class);
        intent.putExtra(Order.ID, ((Order) parent.getAdapter().getItem(position)).getId());
        startActivity(intent);
    }


    /**
     * 操作订单
     */
    private void operateOrder(final String id, final String operateType) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍候...", false);
        OperateOrderRequestData requestData = new OperateOrderRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setId(id);
        requestData.setOperateType(operateType);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {

            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                        /*if (OperateOrderRequestData.CANCEL_ORDER.equals(operateType)) {
							toastShort("取消订单成功");
							if(onOrderCancelListener != null)
							{
								onOrderCancelListener.onOrderCancel(order.getTrainer());
							}
						} else if (OperateOrderRequestData.START_SIGN.equals(operateType)) {
							toastShort("开始签到成功");
						} else if (OperateOrderRequestData.FINISH_SIGN.equals(operateType)) {
							toastShort("结束签到成功");
						} else if (OperateOrderRequestData.CONFIRM.equals(operateType)) {
							toastShort("订单确认成功");
						}*/
                            toastShort("订单确认成功");
                            reload();
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            operateOrder(id, operateType);
                            break;
                        default:
                            toastShort(msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("网络异常，请稍后重试");
                }
            }
        }.request(Constants.URL_OPERATE_ORDER, data, progressDialog, true);
    }
}
