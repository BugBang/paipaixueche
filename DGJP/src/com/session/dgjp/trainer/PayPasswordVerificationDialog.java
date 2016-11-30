package com.session.dgjp.trainer;

import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.GridPasswordView.OnPasswordChangedListener;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.BaseResponseData;
import com.session.common.utils.LogUtil;
import com.session.common.utils.NumberUtil;
import com.session.common.utils.ToastUtil;
import com.session.dgjp.R;
import com.session.dgjp.login.LoginActivity;
import com.session.dgjp.request.PayPasswordVerificationRequestData;
import com.session.dgjp.response.OrderListResponseData;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PayPasswordVerificationDialog extends Dialog implements View.OnClickListener, OnPasswordChangedListener, DialogInterface.OnShowListener
{
	
	private final static String TAG = PayPasswordVerificationDialog.class.getSimpleName();
	private int fee;
	private GridPasswordView gridPasswordView;
	ProgressDialog progressDialog;
	private Context context;
	private VerificationListener verificationListener;
	private TextView contentTv;
	private EditText inputView;

	public PayPasswordVerificationDialog(Context context)
	{
		super(context,R.style.DialogBase);
		this.context = getContext();
		setContentView(R.layout.pay_password_verificaiton_dialog);
		contentTv = (TextView)findViewById(R.id.content);
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getText(R.string.submitting_request));
		gridPasswordView = (GridPasswordView)findViewById(R.id.password);
		gridPasswordView.setOnPasswordChangedListener(this);
		inputView = gridPasswordView.getmInputView();
		this.setOnShowListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}
	
	public int getFee()
	{
		return fee;
	}

	public void setFee(int fee)
	{
		this.fee = fee;
	}
	
	public VerificationListener getVerificationListener()
	{
		return verificationListener;
	}

	public void setVerificationListener(VerificationListener verificationListener)
	{
		this.verificationListener = verificationListener;
	}

	private final static int VERIFY_SUCCESS = 1;
	private final static int VERIFY_FAIL = 2;
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case VERIFY_SUCCESS:
					if(verificationListener != null)
					{
						verificationListener.onVerifySuccess();
					}
					dismiss();
					break;
				case VERIFY_FAIL:
					BaseResponse<OrderListResponseData> baseResponse = (BaseResponse<OrderListResponseData>)msg.obj;
					if(baseResponse != null)
					{
						if(baseResponse.toLogin())
						{
							context.startActivity(new Intent(context,LoginActivity.class));
						}else {
							if(verificationListener != null)
							{
								verificationListener.onVerifyFail();
							}
						}
						ToastUtil.toast(context, baseResponse.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
					}else {
						ToastUtil.toast(context, R.string.query_datas_fail, Toast.LENGTH_SHORT);
						if(verificationListener != null)
						{
							verificationListener.onVerifyFail();
						}
					}
					break;
				default:
					super.handleMessage(msg);
					break;
			}
		}
		
	};

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.back:
				dismiss();
				break;
			default:
				break;
		}
	}
	
	@Override
	public void show()
	{
		super.show();
		contentTv.setText("");
		Resources resources = context.getResources();
		SpannableString ss1 = new SpannableString("该订单的学费为");
		ForegroundColorSpan fcs1 = new ForegroundColorSpan(resources.getColor(R.color.black_333333));
		ss1.setSpan(fcs1, 0, ss1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		contentTv.append(ss1);
		SpannableString ss2 = new SpannableString(NumberUtil.getMoneyFormat(fee/100f));
		ForegroundColorSpan fcs2 = new ForegroundColorSpan(resources.getColor(R.color.red));
		ss2.setSpan(fcs2, 0, ss2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		contentTv.append(ss2);
		SpannableString ss3 = new SpannableString(context.getString(R.string.yuan));
		ForegroundColorSpan fcs3 = new ForegroundColorSpan(resources.getColor(R.color.black_333333));
		ss3.setSpan(fcs3, 0, ss3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		contentTv.append(ss3);
		gridPasswordView.setPassword("");
		inputView.requestFocus();
	}

	public static interface VerificationListener
	{
		public abstract void onVerifySuccess();
		
		public abstract void onVerifyFail();
	}

	@Override
	public void onTextChanged(String psw)
	{}

	@Override
	public void onInputFinish(final String password)
	{
		progressDialog.show();
		AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					BaseRequest request = new BaseRequest();
					PayPasswordVerificationRequestData requestData = new PayPasswordVerificationRequestData();
					requestData.setPayPassword(password);
					request.setRequestData(requestData);
					BaseResponse<BaseResponseData> response = request.sendRequest(BaseResponseData.class);
					if(BaseResponse.CODE_SUCCESS == response.getCode())
					{
						handler.sendEmptyMessage(VERIFY_SUCCESS);
					}else {
						Message msg = Message.obtain();
						msg.what = VERIFY_FAIL;
						msg.obj = response;
						handler.sendMessage(msg);
					}
				} catch (Exception e)
				{
					handler.sendEmptyMessage(VERIFY_FAIL);
					LogUtil.e(TAG,e.toString(),e);
				}finally{
					progressDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void dismiss()
	{
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive())
		{
			imm.hideSoftInputFromWindow(inputView.getApplicationWindowToken(), 0);
		}
		super.dismiss();
	}

	@Override
	public void onShow(DialogInterface arg0)
	{
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(inputView, InputMethodManager.SHOW_FORCED);
	}
	
}
