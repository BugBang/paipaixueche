package com.session.dgjp.trainer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.NumberUtil;
import com.session.common.utils.PhotoUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.HomeActivity;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.Car;
import com.session.dgjp.enity.Course;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.TeachingSchedule;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.order.OrderPaymentActivity;
import com.session.dgjp.enity.Teaching;
import com.session.dgjp.request.CreateOrderRequestData;
import com.session.dgjp.request.TrainingFeeRequestData;
import com.session.dgjp.response.CreateOrderResponseData;
import com.session.dgjp.response.TrainingFeeResponseData;
import com.session.dgjp.trainer.OrderOptionDialog.OnConfirmListener;

public class OrderActivity extends BaseActivity implements OnConfirmListener, OnItemSelectedListener{

	private Trainer trainer;
	private Spinner courseSpinner;
	private TextView timeTv, periodTv, carnoTv, carNameTv, gearTypeTv, preferentialFeeTv, originalFeeTv;
	private Teaching teaching;//选择的时段
	private Car car;
	private int preferentialFee = 0;//活动价
	private int originalFee;//原价
	private List<TeachingSchedule> schedules;
	private List<Teaching> teachings;//课程对应时段
	private OrderOptionDialog orderOptionDialog;
	public final static String SCHEDULES = "schedules";
	private boolean courseEmptyFlag = false;//记录所选的课程是否没有教学安排,
	private final static int GET_TRAINING_FEE_SUCCESS = 1;
	private final static int GET_TRAINING_FEE_FAIL = 2;
	private final static int CREATE_ORDER_SUCCESS = 3;
	private final static int CREATE_ORDER_FAIL = 4;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case GET_TRAINING_FEE_SUCCESS:
					preferentialFee = msg.arg1;
					preferentialFeeTv.setText("");
					Resources resources = getResources();
					SpannableString ss1 = new SpannableString(NumberUtil.getMoneyFormat(preferentialFee/100f));
					ForegroundColorSpan fcs1 = new ForegroundColorSpan(resources.getColor(R.color.red));
					ss1.setSpan(fcs1, 0, ss1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					preferentialFeeTv.append(ss1);
					SpannableString ss2 = new SpannableString(resources.getString(R.string.yuan));
					ForegroundColorSpan fcs2 = new ForegroundColorSpan(resources.getColor(R.color.black_333333));
					ss2.setSpan(fcs2, 0, ss2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					preferentialFeeTv.append(ss2);
					preferentialFeeTv.setVisibility(View.VISIBLE);
					originalFee = msg.arg2;
					originalFeeTv.setText(getString(R.string.formated_money,originalFee/100.0));
					originalFeeTv.setVisibility(View.VISIBLE);
					break;
				case GET_TRAINING_FEE_FAIL:
					BaseResponse<TrainingFeeResponseData> response = (BaseResponse<TrainingFeeResponseData>)msg.obj;
					if(response != null)
					{
						if(response.toLogin())
						{
							toLogin();
						}else {
							preferentialFee = 0;
							//feeTv.setText(getString(R.string.formated_fee,fee/100.0));
							preferentialFeeTv.setVisibility(View.INVISIBLE);
							originalFee = 0;
							originalFeeTv.setVisibility(View.INVISIBLE);
						}
						toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
					}else {
						preferentialFee = 0;
						//feeTv.setText(getString(R.string.formated_fee,fee/100.0));
						preferentialFeeTv.setVisibility(View.INVISIBLE);
						originalFee = 0;
						originalFeeTv.setVisibility(View.INVISIBLE);
						toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
					}
					break;
				case CREATE_ORDER_SUCCESS:
					//toast((String)msg.obj, R.string.operation_success, Toast.LENGTH_SHORT);
					Intent intent = new Intent(HomeActivity.ACTION_ORDER_SUCCESS);
					LocalBroadcastManager.getInstance(OrderActivity.this).sendBroadcast(intent);
					Order order = new Order();
					order.setId((String)msg.obj);
					order.setSubmitTime(new Date());
					order.setPreferentialFee(preferentialFee);
					order.setOriginalFee(originalFee);
					order.setRemainTime(msg.arg1);
					order.setTrainer(trainer);
					intent = new Intent(OrderActivity.this, OrderPaymentActivity.class);
					intent.putExtra(Order.class.getName(), order);
					startActivity(intent);
					setResult(RESULT_OK);
					finish();
					break;
				case CREATE_ORDER_FAIL:
					BaseResponse<CreateOrderResponseData> baseResponse = (BaseResponse<CreateOrderResponseData>)msg.obj;
					if(baseResponse != null)
					{
						if(baseResponse.toLogin())
						{
							toLogin();
						}
						toast(baseResponse.getMsg(), R.string.submit_fail, Toast.LENGTH_SHORT);
					}else {
						toast(R.string.submit_fail, Toast.LENGTH_SHORT);
					}
					break;
				default:
					super.handleMessage(msg);
					break;
			}
		}
		
	};
	
	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.order_activity);
		initTitle(R.string.booking_driving_training, true);
		Intent intent = getIntent();
		trainer = (Trainer) intent.getSerializableExtra(Trainer.class.getName());
		ImageView headIv = (ImageView) findViewById(R.id.ivHead);
		PhotoUtil.showCirclePhoto(this, headIv, trainer.getPhotoUrl(), R.drawable.img_def_head);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
		ratingBar.setRating(trainer.getEval());
		BranchSchool branchSchool = trainer.getBranchSchool();
		((TextView) findViewById(R.id.school)).setText(branchSchool != null ? branchSchool.getName() : "");
		((TextView) findViewById(R.id.trainer)).setText(trainer.getName());
		((TextView)findViewById(R.id.eval)).setText(getString(R.string.eval, trainer.getEval()));
		((TextView)findViewById(R.id.times)).setText(String.valueOf(trainer.getOrderTimes()));
		View timeLayout = findViewById(R.id.time_layout);
		timeLayout.setOnClickListener(this);
		timeTv = (TextView) timeLayout.findViewById(R.id.time);
		View periodLayout = findViewById(R.id.period_layout);
		periodLayout.setOnClickListener(this);
		periodTv = (TextView)periodLayout.findViewById(R.id.period);
		preferentialFeeTv = (TextView)findViewById(R.id.preferential_fee);
		originalFeeTv = (TextView)findViewById(R.id.original_fee);
		View carLayout = findViewById(R.id.car_layout);
		carLayout.setOnClickListener(this);
		carnoTv = (TextView)carLayout.findViewById(R.id.carno);
		carNameTv = (TextView)carLayout.findViewById(R.id._name);
		gearTypeTv = (TextView)carLayout.findViewById(R.id.gear_type);
		findViewById(R.id.phone).setOnClickListener(this);
		findViewById(R.id.confirm).setOnClickListener(this);
		courseSpinner = (Spinner)findViewById(R.id.course);
		schedules = intent.getParcelableArrayListExtra(SCHEDULES);
		List<Course> courses = new ArrayList<Course>();
		/*if(schedules.size()==1)
		{
			if(schedules.get(0).getCourse().equals(Course.K2.getCode()))
			{
				courses.add(Course.K2);
			}else {
				courses.add(Course.K3);
			}
		}else {
			courses.add(Course.K2);
			courses.add(Course.K3);
		}*/
		courses.add(Course.K2);
		courses.add(Course.K3);
		ArrayAdapter<Course> courseAdapter = new ArrayAdapter<Course>(OrderActivity.this, R.layout.spinner_tv, courses);
		courseAdapter.setDropDownViewResource(R.layout.drop_down_textview);
		courseSpinner.setAdapter(courseAdapter);
		orderOptionDialog = new OrderOptionDialog(OrderActivity.this);
		orderOptionDialog.setTrainer(trainer);
		orderOptionDialog.setOnConfirmListener(OrderActivity.this);
		courseSpinner.setOnItemSelectedListener(OrderActivity.this);
		int position = 0;
		for(int i=0; i<schedules.size(); i++)
		{
			TeachingSchedule schedule = schedules.get(i);
			if(schedule != null && schedule.getTeachingList() != null && !schedule.getTeachingList().isEmpty())
			{
				position = i;
				break;
			}
		}
		courseSpinner.setSelection(position, true);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.time_layout:
		case R.id.period_layout:
		case R.id.car_layout:
			if(teachings != null && !teachings.isEmpty())
			{
				if(orderOptionDialog != null)
				{
					orderOptionDialog.show();
				}
			}else {
				toast(R.string.course_no_order, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.phone:
			String phone = trainer.getPhone();
			if(!TextUtil.isEmpty(phone))
			{
				startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone)));  
			}
			break;
		case R.id.confirm:
			if(teaching == null)
			{
				toast(R.string.please_select_period,Toast.LENGTH_SHORT);
				return;
			}
			if(car == null)
			{
				toast(R.string.please_select_car, Toast.LENGTH_SHORT);
				return;
			}
			if(preferentialFeeTv.getVisibility() != View.VISIBLE)
			{
				getFee();
				return;
			}
			
			submit();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	
	@Override
	public void onConfirm(List<Teaching> teachings, final Car car, String timeStr)
	{
		this.car = car;
		List<Integer> timeSlots = new ArrayList<Integer>();
		if(teachings != null && !teachings.isEmpty())
		{
			if(teaching == null)
			{
				teaching = new Teaching();
			}
			for(int i=0; i<teachings.size(); i++)
			{
				List<Integer> timeSlot = teachings.get(i).getTimeSlot();
				timeSlots.addAll(timeSlot);
			}
			periodTv.setText(getString(R.string.order_hour,timeSlots.size()));
			teaching.setTeachingTime(teachings.get(0).getTeachingTime());
			teaching.setTimeSlot(timeSlots);
		}else {
			periodTv.setText(getString(R.string.order_hour,0));
			teaching = null;
		}
		if(car != null)
		{
			carnoTv.setText(car.getCarno());
			carNameTv.setText(car.getEllipsisName());
			gearTypeTv.setText(car.getGearType());
			if(teaching != null)
			{
				//如果既选中汽车，也选中时段，则向后台查询费用
				getFee();
			}else {
				preferentialFee = 0;
				//feeTv.setText(getString(R.string.formated_fee,fee/100.0));
				preferentialFeeTv.setVisibility(View.INVISIBLE);
				originalFee = 0;
				originalFeeTv.setVisibility(View.INVISIBLE);
			}
			
		}else {
			carnoTv.setText("");
			carNameTv.setText("");
			gearTypeTv.setText("");
			preferentialFee = 0;
			//feeTv.setText(getString(R.string.formated_fee,fee/100.0));
			preferentialFeeTv.setVisibility(View.INVISIBLE);
			originalFee = 0;
			originalFeeTv.setVisibility(View.INVISIBLE);
		}
		timeTv.setText(timeStr);
	}

	public void submit()
	{
		progressDialog.setMessage(getText(R.string.submitting_request));
		progressDialog.show();
		AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					CreateOrderRequestData requestData = new CreateOrderRequestData();
					requestData.setStudentAccount(account.getAccount());
					requestData.setTrainerAccount(trainer.getAccount());
					requestData.setBranchSchoolId(trainer.getBranchSchool().getId());
					requestData.setCarno(car.getCarno());
					requestData.setCourse(((Course)courseSpinner.getSelectedItem()).getCode());
					requestData.setTeachingTime(DateUtil.NETWORK_DATE_SDF.format(teaching.getTeachingTime()));
					requestData.setTimeSlot(TextUtil.listToString(teaching.getTimeSlot()));
					requestData.setPreferentialFee(preferentialFee);
					requestData.setOriginalFee(originalFee);
					BaseRequest request = new BaseRequest(requestData);
					BaseResponse<CreateOrderResponseData> baseResponse = request.sendRequest(CreateOrderResponseData.class);
					Message msg = Message.obtain();
					if(BaseResponse.CODE_SUCCESS == baseResponse.getCode())
					{
						msg.what = CREATE_ORDER_SUCCESS;
						msg.obj = baseResponse.getResponseData().getOrderId();
						msg.arg1 = baseResponse.getResponseData().getRemainTime();
					}else {
						msg.what = CREATE_ORDER_FAIL;
						msg.obj = baseResponse;
					}
					handler.sendMessage(msg);
				} catch (Exception e)
				{
					handler.sendEmptyMessage(CREATE_ORDER_FAIL);
					LogUtil.e(TAG, e.toString(), e);
				}finally{
					progressDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		TeachingSchedule teachingSchedule = schedules.get(position);
		teachings = teachingSchedule.getTeachingList();
		if(teachings != null && !teachings.isEmpty())
		{
			if(courseEmptyFlag)
			{
				courseEmptyFlag = false;
			}else{
				timeTv.setText("");
				periodTv.setText("");
				carnoTv.setText("");
				gearTypeTv.setText("");
				carNameTv.setText("");
				originalFeeTv.setText("");
				preferentialFeeTv.setText("");
				orderOptionDialog.setTeachingSchedule(teachingSchedule);
				orderOptionDialog.clear();
			}
		}else{
			toast(R.string.course_no_order, Toast.LENGTH_SHORT);
			courseEmptyFlag = true;
			courseSpinner.setSelection(1-position,true);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{}
	
	private void getFee()
	{
		progressDialog.setMessage(getText(R.string.querying));
		progressDialog.show();
		AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					TrainingFeeRequestData requestData = new TrainingFeeRequestData();
					requestData.setAccount(account.getAccount());
					requestData.setStudentAccount(account.getAccount());
					requestData.setTrainerAccount(trainer.getAccount());
					requestData.setCourse(((Course)courseSpinner.getSelectedItem()).getCode());
					requestData.setCarno(car.getCarno());
					requestData.setTeachingTime(DateUtil.LOCAL_DATE_SDF.format(teaching.getTeachingTime()));
					List<Integer> timeSlot = teaching.getTimeSlot();
					requestData.setDuration(timeSlot.size()*60);
					requestData.setTimeSlot(TextUtil.listToString(timeSlot));
					BaseRequest request = new BaseRequest(requestData);
					BaseResponse<TrainingFeeResponseData> response = request.sendRequest(TrainingFeeResponseData.class);
					Message msg = Message.obtain();
					if(BaseResponse.CODE_SUCCESS == response.getCode())
					{
						msg.what = GET_TRAINING_FEE_SUCCESS;
						TrainingFeeResponseData responseData = response.getResponseData();
						msg.arg1 = responseData.getFee();
						msg.arg2 = responseData.getOriginalFee();
					}else{
						msg.what = GET_TRAINING_FEE_FAIL;
						msg.obj = response;
					}
					handler.sendMessage(msg);
				} catch (Exception e)
				{
					LogUtil.e(TAG, e.toString(),e);
					handler.sendEmptyMessage(GET_TRAINING_FEE_FAIL);
				}finally{
					progressDialog.dismiss();
				}
			}
		});
	}

}
