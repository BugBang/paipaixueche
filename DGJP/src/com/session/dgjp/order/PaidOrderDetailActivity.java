package com.session.dgjp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.session.dgjp.HomeActivity;
import com.session.dgjp.R;

public class PaidOrderDetailActivity extends BaseOrderDetailActivity{

	public final static String NEW_ORDER = "new_order";//是否是刚完成支付的订单
	
	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.back:
			case R.id.ivTitleLeft:
				//判断，如果是刚支付成功的订单，则刷新我的预约列表
				if(getIntent().getBooleanExtra(NEW_ORDER, false))
				{
					Intent intent = new Intent(this,HomeActivity.class);
					intent.putExtra(HomeActivity.PAY_SUCCESS, true);
					startActivity(intent);
				}
				finish();
				break;
			default:
				super.onClick(view);
				break;
		}
	}

	@Override
	protected int getContentRes()
	{
		return R.layout.paid_order_detail_activity;
	}

	@Override
	protected void showInfo()
	{
		((TextView)findViewById(R.id.original_fee)).setText(getString(R.string.formated_money,order.getOriginalFee()/100.0));
		((TextView)findViewById(R.id.fee)).setText(getString(R.string.formated_money,order.getFee()/100.0));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && getIntent().getBooleanExtra(NEW_ORDER, false))
		{
			Intent intent = new Intent(this,HomeActivity.class);
			intent.putExtra(HomeActivity.PAY_SUCCESS, true);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
