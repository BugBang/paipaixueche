package com.session.dgjp.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.session.common.BaseActivity;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constants.APPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case 0:
				// toastShort("支付成功");
				// Intent intent = new Intent(this, PaidOrderDetailActivity.class);
				// PayResp payResp = (PayResp) resp;
				// intent.putExtra(Order.ID, payResp.extData);
				// intent.putExtra(PaidOrderDetailActivity.NEW_ORDER, true);
				// startActivity(intent);
				finish();
				break;
			case -1:
				toastShort("支付失败");
				finish();
				break;
			case -2:
				toastShort("支付取消");
				finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

}
