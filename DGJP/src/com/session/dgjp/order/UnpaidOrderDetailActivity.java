package com.session.dgjp.order;

import java.util.Date;

import com.session.common.BaseDialog;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.BaseResponseData;
import com.session.dgjp.request.OperateOrderRequestData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.dgjp.HomeActivity;
import com.session.dgjp.R;
import com.session.dgjp.enity.Order;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UnpaidOrderDetailActivity extends BaseOrderDetailActivity
{
	private CountDownTimer countDownTimer;
	
	private final static int CANCEL_SUCCESS = 1;
	private final static int CANCEL_FAIL = 2;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case CANCEL_SUCCESS:
				setResult(RESULT_OK);
				finish();
				break;
			case CANCEL_FAIL:
			{
				BaseResponse baseResponse = (BaseResponse) msg.obj;
				if (baseResponse != null)
				{
					if (baseResponse.toLogin())
					{
						toLogin();
					}
					toast(baseResponse.getMsg(), R.string.cancel_fail, Toast.LENGTH_SHORT);
				} else
				{
					toast(R.string.cancel_fail, Toast.LENGTH_SHORT);
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
	protected void init(Bundle savedInstanceState)
	{
		super.init(savedInstanceState);
		findViewById(R.id.confirm).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.confirm:
				Intent intent = new Intent(this,OrderPaymentActivity.class);
				intent.putExtra(Order.class.getName(), order);
				startActivity(intent);
				break;
			case R.id.cancel:
				BaseDialog dialog = new BaseDialog.Builder(this).setTitle("提示").setMessage("您是否要取消该订单？").setPositiveButton("确定", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
						progressDialog.setMessage(getText(R.string.cancel_order));
						progressDialog.show();
						AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable()
						{
							@Override
							public void run()
							{
								try
								{
									OperateOrderRequestData requestData = new OperateOrderRequestData();
									requestData.setId(order.getId());
									requestData.setOperateType(OperateOrderRequestData.CANCEL_ORDER);
									BaseRequest baseRequest = new BaseRequest(requestData);
									BaseResponse<BaseResponseData> baseResponse = baseRequest.sendRequest(BaseResponseData.class);
									if(baseResponse.getCode() == BaseResponse.CODE_SUCCESS)
									{
										handler.sendEmptyMessage(CANCEL_SUCCESS);
									}else{
										Message msg = Message.obtain();
										msg.what = CANCEL_FAIL;
										msg.obj = baseResponse;
										handler.sendMessage(msg);
									}
								} catch (Exception e)
								{
									handler.sendEmptyMessage(CANCEL_FAIL);
									LogUtil.e(TAG, e.toString(),e);
								}finally{
									if(progressDialog.isShowing())
									{
										progressDialog.dismiss();
									}
								}
							}
						});
					}
				}).setNegativeButton("取消", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				}).create();
				dialog.show();
				
				break;
			default:
				super.onClick(view);
				break;
		}
	}

	@Override
	protected int getContentRes()
	{
		return R.layout.unpaid_order_detail_activity;
	}

	@Override
	protected void showInfo()
	{
		((TextView)findViewById(R.id.preferential_fee)).setText(getString(R.string.formated_money,order.getPreferentialFee()/100.0));
		final TextView countDownTv = (TextView)findViewById(R.id.count_down);
		countDownTimer = new CountDownTimer(order.getRemainTime()*1000,1000)
		{
			@Override
			public void onTick(long millisUntilFinished)
			{
				countDownTv.setText(DateUtil.NETWORK_SIMPLE_MIN_SEC_SDF.format(new Date(millisUntilFinished)));
				order.setRemainTime((int)(millisUntilFinished/1000));
			}
			
			@Override
			public void onFinish()
			{
				toast("支付超时，该订单已取消", Toast.LENGTH_SHORT);
				setResult(RESULT_OK);
				finish();
			}
		};
		countDownTimer.start();
	}

	@Override
	protected void onDestroy()
	{
		if(countDownTimer != null)
		{
			countDownTimer.cancel();
		}
		super.onDestroy();
	}
	
	
	
}
