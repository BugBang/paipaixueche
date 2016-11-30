package com.session.dgjp.trainer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.LogUtil;
import com.session.common.utils.PhotoUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.Course;
import com.session.dgjp.enity.Teaching;
import com.session.dgjp.enity.TeachingSchedule;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.request.TeachingScheduleRequestData;
import com.session.dgjp.request.TrainerListRequestData;
import com.session.dgjp.response.TeachingScheduleResponseData;
import com.session.dgjp.response.TrainerListResponseData;
import com.session.dgjp.trainer.TrainerAdapter.OrderListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainerListActivity extends BaseActivity implements OrderListener, TextWatcher, FilterListener {
    private EditText searchEt;
    private View trainerResetBtn;
    private TrainerAdapter adapter;
    private BranchSchool branchSchool;
    private Trainer trainer;
    private TextView noDataTv;
    private final static int ORDER_RQ = 1;
    private final static int GET_TRAINERS_SUCCESS = 1;
    private final static int GET_TRAINERS_FAIL = 2;
    private final static int GET_TEACHING_SCHEDULE_SUCCESS = 3;
    private final static int GET_TEACHING_SCHEDULE_FAIL = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_TRAINERS_SUCCESS:
                    TrainerListResponseData responseData = (TrainerListResponseData) msg.obj;
                    adapter.setList(responseData.getList());
                    adapter.getFilter().filter(searchEt.getText().toString().trim(), TrainerListActivity.this);
                    break;
                case GET_TRAINERS_FAIL:
                    BaseResponse<TrainerListResponseData> baseResponse = (BaseResponse<TrainerListResponseData>) msg.obj;
                    if (baseResponse != null) {
                        if (baseResponse.toLogin()) {
                            toLogin();
                        }
                        toast(baseResponse.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    }
                    noDataTv.setVisibility(View.VISIBLE);
                    break;
                case GET_TEACHING_SCHEDULE_SUCCESS:
                    List<TeachingSchedule> schedules = (List<TeachingSchedule>) msg.obj;
                    if (schedules == null) {
                        toast(R.string.trainer_no_order, Toast.LENGTH_SHORT);
                        break;
                    }
                    int sum = 0;
                    for (int i = 0; i < schedules.size(); i++) {
                        List<Teaching> teachings = schedules.get(i).getTeachingList();
                        if (teachings != null && !teachings.isEmpty()) {
                            sum++;
                        }
                    }
                    if (sum == 0) {
                        toast(R.string.trainer_no_order, Toast.LENGTH_SHORT);
                        break;
                    }
                    Intent intent = new Intent(TrainerListActivity.this, OrderActivity.class);
                    intent.putExtra(Trainer.class.getName(), trainer);
                    intent.putParcelableArrayListExtra(OrderActivity.SCHEDULES, (ArrayList<TeachingSchedule>) schedules);
                    startActivityForResult(intent, ORDER_RQ);
                    break;
                case GET_TEACHING_SCHEDULE_FAIL:
                    BaseResponse<TeachingScheduleResponseData> response = (BaseResponse<TeachingScheduleResponseData>) msg.obj;
                    if (response != null) {
                        if (response.toLogin()) {
                            toLogin();
                        }
                        toast(response.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
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
        setContentView(R.layout.trainer_list_activity);
        branchSchool = (BranchSchool) getIntent().getSerializableExtra(BranchSchool.class.getName());
        ((TextView) findViewById(R.id.school)).setText(branchSchool.getName());
        ((TextView) findViewById(R.id.phone)).setText(branchSchool.getEllipsisPhone());
        ((TextView) findViewById(R.id.address)).setText(branchSchool.getEllipsisAddress());
        PhotoUtil.showRoundCornerPhoto(this, (ImageView) findViewById(R.id.photo), branchSchool.getSmallPhotoUrl(), R.drawable.def_school);
        noDataTv = (TextView) findViewById(R.id.no_data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new TrainerAdapter(this, this);
        listView.setAdapter(adapter);
        searchEt = (EditText) findViewById(R.id.search);
        trainerResetBtn = findViewById(R.id.reset);
        trainerResetBtn.setOnClickListener(this);
        searchEt.addTextChangedListener(this);
        findViewById(R.id.ivTitleLeft).setOnClickListener(this);
        getTrainers();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getTrainers() {
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TrainerListRequestData requestData = new TrainerListRequestData();
                    requestData.setBranchSchoolId(branchSchool.getId());
                    BaseRequest request = new BaseRequest(requestData);
                    BaseResponse<TrainerListResponseData> response = request.sendRequest(TrainerListResponseData.class);
                    Message msg = Message.obtain();
                    if (response.getCode() == BaseResponse.CODE_SUCCESS) {
                        msg.what = GET_TRAINERS_SUCCESS;
                        msg.obj = response.getResponseData();
                    } else {
                        msg.what = GET_TRAINERS_FAIL;
                        msg.obj = response;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(GET_TRAINERS_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    progressDialog.dismiss();
                }
            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 0) {
            trainerResetBtn.setVisibility(View.GONE);
        } else {
            trainerResetBtn.setVisibility(View.VISIBLE);
        }
        adapter.getFilter().filter(editable.toString().trim(), TrainerListActivity.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                searchEt.setText("");
                break;
            case R.id.ivTitleLeft:
                finish();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onOrder(final Trainer trainer) {
        this.trainer = trainer;
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TeachingScheduleRequestData requestData = new TeachingScheduleRequestData();
                    requestData.setTrainerAccount(trainer.getAccount());
                    BaseRequest request = new BaseRequest(requestData);
                    BaseResponse<TeachingScheduleResponseData> response = request.sendRequest(TeachingScheduleResponseData.class);
                    Message msg = Message.obtain();
                    if (BaseResponse.CODE_SUCCESS == response.getCode()) {
                        msg.what = GET_TEACHING_SCHEDULE_SUCCESS;
                        List<TeachingSchedule> list = response.getResponseData().getList();
                        List<TeachingSchedule> schedules = new ArrayList<TeachingSchedule>();
                        for (int i = 0; i < list.size(); i++) {
                            TeachingSchedule teachingSchedule = list.get(i);
                            if (teachingSchedule.getCourse().equals(Course.K2.getCode()) || teachingSchedule.getCourse().equals(Course.K3.getCode())) {
                                schedules.add(teachingSchedule);
                            }
                        }
                        Bundle bundle = new Bundle();
                        msg.setData(bundle);
                        msg.obj = schedules;
                    } else {
                        msg.what = GET_TEACHING_SCHEDULE_FAIL;
                        msg.obj = response;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(GET_TEACHING_SCHEDULE_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            noDataTv.setVisibility(View.GONE);
        } else {
            noDataTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ORDER_RQ:
                if (resultCode == RESULT_OK) {
                    //教练订单次数增加1次
                    trainer.setOrderTimes(trainer.getOrderTimes() + 1);
                    //如果该教练本来是最近预约的教练，直接刷新列表，否则需要对教练列表重新排序
                    if (Trainer.TYPE_LATEST.equals(trainer.getTrainerType())) {
                        adapter.notifyDataSetChanged();
                    } else {
                        List<Trainer> trainers = adapter.getList();
                        trainers.get(0).setTrainerType(Trainer.TYPE_OTHER);
                        trainers.remove(trainer);
                        Collections.sort(trainers);
                        trainer.setTrainerType(Trainer.TYPE_LATEST);
                        trainers.add(0, trainer);
                        adapter.getFilter().filter(searchEt.getText().toString().trim(), this);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}
