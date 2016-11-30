package com.session.dgjp.personal;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.WalletRecord;
import com.session.dgjp.request.CommonRequestData;
import com.session.dgjp.request.QueryBillLogListRequestData;

/** 我的钱包 */
public class MyWalletActivity extends BaseActivity {

	private int pageIndex = 1;
	private String endFlag;
	private boolean isLoading;
	private TextView tvBalance, tvNoData;
	private ListView lvHistory;
	private WalletRecordAdapter adapter;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_my_wallet);
		initTitle(R.string.my_wallet);
		tvBalance = (TextView) findViewById(R.id.tvBalance);
		tvNoData = (TextView) findViewById(R.id.no_data);
		lvHistory = (ListView) findViewById(R.id.lv_history);
		adapter = new WalletRecordAdapter(ctx);
		lvHistory.setAdapter(adapter);
		lvHistory.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (isLoading) {
						return;
					}
					int lastVisiblePosition = lvHistory.getLastVisiblePosition();
					if (lastVisiblePosition == (adapter.getCount() - 1)) {
						if ("1".equals(endFlag)) {
							queryBillLogList();
						}
					}
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		getAccountBalance();
	}

	/** 获取用户余额 */
	private void getAccountBalance() {
		ProgressDialog progressDialog = buildProcessDialog(null, "正在查询...", false);
		CommonRequestData requestData = new CommonRequestData();
		requestData.setAccount(account.getAccount());
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						JSONObject jo = new JSONObject(response);
						double balance = (double) (jo.optInt("balance") / 100.00);
						DecimalFormat decimalFormat = new DecimalFormat("0.00");
						tvBalance.setText(decimalFormat.format(balance));
						queryBillLogList();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						getAccountBalance();
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
		}.request(Constants.URL_GET_ACCOUNT_BALANCE, data, progressDialog, true);
	}

	/** 查询账单 */
	private void queryBillLogList() {
		isLoading = true;
		ProgressDialog progressDialog = buildProcessDialog(null, "正在查询...", false);
		QueryBillLogListRequestData requestData = new QueryBillLogListRequestData();
		requestData.setAccount(account.getAccount());
		requestData.setStudentAccount(account.getAccount());
		requestData.setPageIndex(pageIndex);
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						JSONObject object = new JSONObject(response);
						String listString = object.optString("list");
						List<WalletRecord> list = new Gson().fromJson(listString, new TypeToken<List<WalletRecord>>() {
						}.getType());
						if (list != null && list.size() > 0) {
							adapter.append(list);
							pageIndex++;
						}
						endFlag = object.optString("endFlag");
						if (adapter.getCount() == 0) {
							tvNoData.setVisibility(View.VISIBLE);
							lvHistory.setVisibility(View.GONE);
						} else {
							tvNoData.setVisibility(View.GONE);
							lvHistory.setVisibility(View.VISIBLE);
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						queryBillLogList();
						break;
					default:
						toastShort(msg);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
				} finally {
					isLoading = false;
				}
			}
		}.request(Constants.URL_QUERY_BILL_LOG_LIST, data, progressDialog, true);
	}

}
