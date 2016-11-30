package com.session.dgjp.personal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.MyCoupon;
import com.session.dgjp.request.CouponListRequestData;

/** 我的优惠券 */
public class MyCouponActivity extends BaseActivity {

	private boolean isUsable = true;
	private TextView tvNoData;
	private ListView lvCouponUsable;
	private ListView lvCouponUnusable;
	private MyCouponAdapter usableAdapter;
	private MyCouponAdapter unusableAdapter;
	private ArrayList<MyCoupon> usableCoupons;
	private ArrayList<MyCoupon> unusableCoupons;
	private Button btnSwitch;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_my_coupon);
		initTitle(R.string.my_coupon);
		tvNoData = (TextView) findViewById(R.id.no_data);
		lvCouponUsable = (ListView) findViewById(R.id.lv_coupon_usable);
		lvCouponUnusable = (ListView) findViewById(R.id.lv_coupon_unusable);
		usableAdapter = new MyCouponAdapter(ctx);
		unusableAdapter = new MyCouponAdapter(ctx);
		lvCouponUsable.setAdapter(usableAdapter);
		lvCouponUnusable.setAdapter(unusableAdapter);
		btnSwitch = (Button) findViewById(R.id.btnSwitch);
		btnSwitch.setOnClickListener(this);
		usableCoupons = new ArrayList<MyCoupon>();
		unusableCoupons = new ArrayList<MyCoupon>();
		queryStudentUsableCouponList();
	}

	/** 获取学员优惠券列表 */
	private void queryStudentUsableCouponList() {
		ProgressDialog progressDialog = buildProcessDialog(null, "正在查询...", false);
		CouponListRequestData requestData = new CouponListRequestData();
		requestData.setAccount(account.getAccount());
		requestData.setBranchSchoolId(account.getBranchSchoolId());
		requestData.setIsUseable("");
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						JSONObject object = new JSONObject(response);
						String listString = object.optString("list");
						List<MyCoupon> list = new Gson().fromJson(listString, new TypeToken<List<MyCoupon>>() {
						}.getType());
						if (list != null && list.size() > 0) {
							btnSwitch.setVisibility(View.VISIBLE);
							for (MyCoupon myCoupon : list) {
								if ("Y".equals(myCoupon.getUseable())) {
									usableCoupons.add(myCoupon);
								} else {
									unusableCoupons.add(myCoupon);
								}
							}
							usableAdapter.append(usableCoupons);
							unusableAdapter.append(unusableCoupons);
							if (usableAdapter.getCount() == 0) {
								tvNoData.setVisibility(View.VISIBLE);
								lvCouponUsable.setVisibility(View.GONE);
							} else {
								tvNoData.setVisibility(View.GONE);
								lvCouponUsable.setVisibility(View.VISIBLE);
							}
						} else {
							tvNoData.setVisibility(View.VISIBLE);
							lvCouponUsable.setVisibility(View.GONE);
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						queryStudentUsableCouponList();
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
		}.request(Constants.URL_QUERY_STUDENT_USABLE_COUPON_LIST, data, progressDialog, true);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSwitch:
			if (isUsable) {
				lvCouponUsable.setVisibility(View.GONE);
				if (unusableAdapter.getCount() == 0) {
					tvNoData.setVisibility(View.VISIBLE);
					lvCouponUnusable.setVisibility(View.GONE);
				} else {
					tvNoData.setVisibility(View.GONE);
					lvCouponUnusable.setVisibility(View.VISIBLE);
				}
				isUsable = false;
				btnSwitch.setText("查看可用券");
			} else {
				lvCouponUnusable.setVisibility(View.GONE);
				if (usableAdapter.getCount() == 0) {
					tvNoData.setVisibility(View.VISIBLE);
					lvCouponUsable.setVisibility(View.GONE);
				} else {
					tvNoData.setVisibility(View.GONE);
					lvCouponUsable.setVisibility(View.VISIBLE);
				}
				isUsable = true;
				btnSwitch.setText("查看不可用券");
			}
			break;
		default:
			super.onClick(v);
			break;
		}
	}

}
