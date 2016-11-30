package com.session.dgjp.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

import com.google.gson.Gson;
import com.session.common.BaseDialog;
import com.session.common.BaseRequestTask;
import com.session.common.utils.DateUtil;
import com.session.common.utils.MD5Util;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.ScanQRCodeActivity;
import com.session.dgjp.common.BaseOrderListFragment;
import com.session.dgjp.common.OptionListener;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.request.OperateOrderRequestData;
import com.session.dgjp.request.OrderListRequestData;

/** 我的预约 */
public class OrderListFragment extends BaseOrderListFragment implements OptionListener {

	//private Order order;
	private final static int PAYMENT_RQ = 1;
	private final static int START_SIGN_RQ = 2;
	private final static int FINISH_SIGN_RQ = 3;
	
	@Override
	protected int getContentRes() {
		return R.layout.order_list_fragment;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PAYMENT_RQ)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				reload();
				//从列表中删除该记录
				/*List<Order> orders = adapter.getList();
				int size = orders.size();
				if(order.equals(orders.get(size-1)))
				{
					//如果是最后一个，这需要修改lastrecordvalue
					if(size == 1)
					{
						lastRecordValue = null;
					}else{
						setLastRecordValue(orders.get(size-2));
					}
				}
				orders.remove(order);
				adapter.notifyDataSetChanged();*/
			}
		}else{
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				String orderId = bundle.getString("orderId");
				if (TextUtils.isEmpty(result) || TextUtils.isEmpty(orderId)) {
					return;
				}
				if (result.equals("01" + MD5Util.encode(orderId + "DGFDS"))) {
					switch (requestCode) {
					case START_SIGN_RQ: // 开始签到
						operateOrder(orderId, OperateOrderRequestData.START_SIGN);
						break;
					case FINISH_SIGN_RQ: // 结束签到
						operateOrder(orderId, OperateOrderRequestData.FINISH_SIGN);
						break;
					default:
						break;
					}
				} else {
					toastShort("您扫描的不是该订单的二维码，请核对后再扫描");
				}
			}
		}
	}

	@Override
	public void onOptionClick(final Order order) {
		//this.order = order;
		if(Order.CANCEL_ORDER.equals(order.getNextOperate()))
		{
			BaseDialog dialog = new BaseDialog.Builder(act).setTitle("提示").setMessage("您确定要取消订单吗？")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							operateOrder(order.getId(), OperateOrderRequestData.CANCEL_ORDER);
							dialog.cancel();
						}
					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create();
					dialog.show();
		}else if(Order.START_SIGN.equals(order.getNextOperate()))
		{
			Intent intent = new Intent(act, ScanQRCodeActivity.class);
			intent.putExtra("orderId", order.getId());
			startActivityForResult(intent, START_SIGN_RQ);
		}else if(Order.FINISH_SIGN.equals(order.getNextOperate()))
		{
			Intent intent = new Intent(act, ScanQRCodeActivity.class);
			intent.putExtra("orderId", order.getId());
			startActivityForResult(intent, FINISH_SIGN_RQ);
		}else if(Order.CONFIRM.equals(order.getNextOperate()))
		{
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
		}else if(Order.PAY.equals(order.getNextOperate()))
		{
			Intent intent = new Intent(getActivity(), UnpaidOrderDetailActivity.class);
			intent.putExtra(Order.ID, order.getId());
			startActivityForResult(intent, PAYMENT_RQ);
		}else {
			toast(R.string.operate_fail_update_app, Toast.LENGTH_SHORT);
		}
	}
	
	/** 操作订单 */
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
						if (OperateOrderRequestData.CANCEL_ORDER.equals(operateType)) {
							toastShort("取消订单成功");
						} else if (OperateOrderRequestData.START_SIGN.equals(operateType)) {
							toastShort("开始签到成功");
						} else if (OperateOrderRequestData.FINISH_SIGN.equals(operateType)) {
							toastShort("结束签到成功");
						} else if (OperateOrderRequestData.CONFIRM.equals(operateType)) {
							toastShort("订单确认成功");
						}
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

	@Override
	protected void initAdapter() {
		adapter = new OrderAdapter(act, this);
	}

	@Override
	protected String getFinishFlag() {
		return OrderListRequestData.UNFINISH;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id)
	{
		Intent intent = new Intent();
		Order order = (Order)parent.getAdapter().getItem(position);
		intent.putExtra(Order.ID, order.getId());
		if(Order.PAY.equals(order.getNextOperate()))
		{
			intent.setClass(getActivity(), UnpaidOrderDetailActivity.class);
			startActivityForResult(intent, PAYMENT_RQ);
		}else{
			intent.setClass(getActivity(), PaidOrderDetailActivity.class);
			startActivity(intent);
		}

	}

}
