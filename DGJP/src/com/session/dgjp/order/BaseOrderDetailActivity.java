package com.session.dgjp.order;

import java.util.Date;

import com.session.common.BaseActivity;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.PhotoUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.Car;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.request.OrderDetailRequestData;
import com.session.dgjp.response.OrderDetailResponseData;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseOrderDetailActivity extends BaseActivity
{
	protected final static int GET_ORDER_SUCCESS = 1;
	protected final static int GET_ORDER_FAIL = 2;
	protected Order order;
	protected int operation;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case GET_ORDER_SUCCESS:
					((TextView)findViewById(R.id.course)).setText(order.getCourse().getName());
					Trainer trainer  = order.getTrainer();
					ImageView headIv = (ImageView) findViewById(R.id.ivHead);
					PhotoUtil.showCirclePhoto(BaseOrderDetailActivity.this, headIv, trainer.getPhotoUrl(), R.drawable.img_def_head);
					((TextView) findViewById(R.id.trainer)).setText(trainer.getName());
					((TextView) findViewById(R.id.school)).setText(trainer.getBranchSchool().getName());
					((RatingBar) findViewById(R.id.rating_bar)).setRating(trainer.getEval());
					((TextView)findViewById(R.id.times)).setText(String.valueOf(trainer.getOrderTimes()));
					((TextView)findViewById(R.id.eval)).setText(getString(R.string.eval, trainer.getEval()));
					Date beginTime = order.getBeginTime();
					Date endTime = order.getEndTime();
					((TextView) findViewById(R.id.time)).setText(getString(R.string.selected_order_time,DateUtil.LOCAL_SIMPLE_SDF.format(beginTime),DateUtil.NETWORK_SIMPLE_TIME_SDF.format(beginTime),DateUtil.NETWORK_SIMPLE_TIME_SDF.format(endTime)));
					((TextView) findViewById(R.id.period)).setText(getString(R.string.order_hour,order.getOrderDuration()/60));
					Car car = order.getCar();
					((TextView)findViewById(R.id.carno)).setText(car.getCarno());
					((TextView)findViewById(R.id._name)).setText(car.getEllipsisName());
					((TextView)findViewById(R.id.gear_type)).setText(car.getGearType());
					findViewById(R.id.phone).setOnClickListener(BaseOrderDetailActivity.this);
					showInfo();
					break;
				case GET_ORDER_FAIL:
					BaseResponse<OrderDetailResponseData> response = (BaseResponse<OrderDetailResponseData>)msg.obj;
					if(response != null)
					{
						if(response.toLogin())
						{
							toLogin();
						}else {
							finish();
						}
						toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
					}else {
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
	protected void init(Bundle savedInstanceState)
	{
		setContentView(getContentRes());
		initTitle(R.string.order_detail, true);
		progressDialog.setMessage(getText(R.string.querying));
		progressDialog.show();
		AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// 获取订单详情
					OrderDetailRequestData requestData = new OrderDetailRequestData();
					requestData.setId((String)getIntent().getStringExtra(Order.ID));
					BaseRequest request = new BaseRequest();
					request.setRequestData(requestData);
					BaseResponse<OrderDetailResponseData> response = request.sendRequest(OrderDetailResponseData.class);
					if (BaseResponse.CODE_SUCCESS == response.getCode())
					{
						order = response.getResponseData().getOrder();
						handler.sendEmptyMessage(GET_ORDER_SUCCESS);
					} else
					{
						Message msg = Message.obtain();
						msg.what = GET_ORDER_FAIL;
						msg.obj = response;
						handler.sendMessage(msg);
					}
				} catch (Exception e)
				{
					handler.sendEmptyMessage(GET_ORDER_FAIL);
					LogUtil.e(TAG, e.toString(), e);
				} finally
				{
					progressDialog.dismiss();
				}
			}
		});
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.phone:
				String phone = order.getTrainer().getPhone();
				if(!TextUtil.isEmpty(phone))
				{
					startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone)));  
				}
				break;
			default:
				super.onClick(view);
				break;
		}
	}
	
	protected abstract int getContentRes();
	protected abstract void showInfo();
}
