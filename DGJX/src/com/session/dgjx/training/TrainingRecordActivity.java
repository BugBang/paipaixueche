package com.session.dgjx.training;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.PhotoUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjx.R;
import com.session.dgjx.enity.Order;
import com.session.dgjx.enity.ScoreLevel;
import com.session.dgjx.enity.Student;
import com.session.dgjx.request.EvaluationRequestData;
import com.session.dgjx.request.OrderDetailRequestData;
import com.session.dgjx.response.EvaluationResponseData;
import com.session.dgjx.response.OrderDetailResponseData;

public class TrainingRecordActivity extends BaseActivity implements OnRatingBarChangeListener
{

	private Order order;
	private RatingBar scoreRatingBar;
	private TextView scoreLevelTv;
	private final static int GET_ORDER_SUCCESS = 1;
	private final static int GET_ORDER_FAIL = 2;
	private final static int GET_EVALUATION_SUCCESS = 7;
	private final static int GET_EVALUATION_FAIL = 8;
	private final static int GET_DATA_FAIL = 9;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case GET_ORDER_SUCCESS:
				Student student = order.getStudent();
				((TextView) findViewById(R.id.student)).setText(student.getName());
				((TextView) findViewById(R.id.school)).setText(student.getBranchSchool().getName());
				/*((TextView) findViewById(R.id.apply_time)).setText(DateUtil.NETWORK_DATE_SDF.format(student.getApplyTime()));*/
				ImageView headIv = (ImageView) findViewById(R.id.ivHead);
				PhotoUtil.showPhoto(TrainingRecordActivity.this, headIv, student.getPhotoUrl(), R.drawable.img_def_head);
				findViewById(R.id.phone).setOnClickListener(TrainingRecordActivity.this);
				((TextView) findViewById(R.id.duration)).setText(getString(R.string.order_duration, order.getOrderDuration() / 60));
				break;
			case GET_ORDER_FAIL:
			case GET_EVALUATION_FAIL:
			case GET_DATA_FAIL:
				BaseResponse<OrderDetailResponseData> response = (BaseResponse<OrderDetailResponseData>)msg.obj;
				if(response != null)
				{
					if(response.toLogin())
					{
						toLogin();
					}else{
						finish();
					}
					toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
				}else {
					toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
					finish();
				}
				break;
			case GET_EVALUATION_SUCCESS:
			{
				scoreRatingBar.setRating(((EvaluationResponseData)msg.obj).getScore());
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
		setContentView(R.layout.training_record_activity);
		initTitle(R.string.evaluation_detail, true);
		scoreRatingBar = (RatingBar) findViewById(R.id.score_rating_bar);
		scoreRatingBar.setOnRatingBarChangeListener(this);
		scoreLevelTv = (TextView)findViewById(R.id.score_level);
		scoreRatingBar.setRating(3);
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
					String id = getIntent().getStringExtra(Order.ID);
					OrderDetailRequestData requestData = new OrderDetailRequestData();
					requestData.setId(id);
					BaseRequest request = new BaseRequest(requestData);
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

					// 获取评价详情
					if (order != null)
					{
						EvaluationRequestData evaluationRequestData = new EvaluationRequestData();
						evaluationRequestData.setOrderId(id);
						request.setRequestData(evaluationRequestData);
						BaseResponse<EvaluationResponseData> evaluationResponse = request.sendRequest(EvaluationResponseData.class);
						Message msg = Message.obtain();
						if (BaseResponse.CODE_SUCCESS == evaluationResponse.getCode())
						{
							msg.what = GET_EVALUATION_SUCCESS;
							msg.obj = evaluationResponse.getResponseData();
						} else
						{
							msg.what = GET_EVALUATION_FAIL;
							msg.obj = evaluationResponse;
						}
						handler.sendMessage(msg);
					}
				} catch (Exception e)
				{
					handler.sendEmptyMessage(GET_DATA_FAIL);
					LogUtil.e(TAG, e.toString(), e);
				} finally
				{
					progressDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void onClick(View view)
	{

		switch (view.getId())
		{
			case R.id.phone:
				String phone = order.getStudent().getPhone();
				if (!TextUtil.isEmpty(phone))
				{
					startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
				}
				break;
			default:
				super.onClick(view);
				break;
		}
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
	{
		int score = Math.round(ratingBar.getRating());
		ScoreLevel[] values = ScoreLevel.values();
		for(int i=0; i<values.length; i++)
		{
			ScoreLevel scoreLevel = values[i];
			if(score == scoreLevel.getScore())
			{
				scoreLevelTv.setText(scoreLevel.getName());
				break;
			}
		}
	}

}
