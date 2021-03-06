package com.session.dgjp.training;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.BaseResponseData;
import com.session.common.utils.LogUtil;
import com.session.common.utils.PhotoUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.Label;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.ScoreLevel;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.helper.LabelHelper;
import com.session.dgjp.request.EvaluationRequestData;
import com.session.dgjp.request.OrderDetailRequestData;
import com.session.dgjp.request.PublicEvaluationRequestData;
import com.session.dgjp.response.EvaluationResponseData;
import com.session.dgjp.response.OrderDetailResponseData;

import java.util.ArrayList;
import java.util.List;

public class TrainingEvaluationActivity extends BaseActivity implements OnItemClickListener, OnRatingBarChangeListener {

    private Order order;
    private RatingBar scoreRatingBar;
    private TextView scoreLevelTv;
    private Button confirmBtn;
    private EditText commentEt;
    private GridView gridView;
    private LabelAdapter adapter;
    private List<Label> labels;
    private LinearLayout linearLayout;
    private List<Label> selectedLabels = new ArrayList<Label>();
    private final static int GET_ORDER_SUCCESS = 1;
    private final static int GET_ORDER_FAIL = 2;
    private final static int GET_LABELS_SUCCESS = 3;
    private final static int GET_LABELS_FAIL = 4;
    private final static int EVALUATE_SUCCESS = 5;
    private final static int EVALUATE_FAIL = 6;
    private final static int GET_EVALUATION_SUCCESS = 7;
    private final static int GET_EVALUATION_FAIL = 8;
    private final static int GET_DATA_FAIL = 9;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ORDER_SUCCESS:
                    Trainer trainer = order.getTrainer();
                    ((TextView) findViewById(R.id.times)).setText(String.valueOf(trainer.getOrderTimes()));
                    ((TextView) findViewById(R.id.trainer)).setText(trainer.getName());
                    ((TextView) findViewById(R.id.school)).setText(trainer.getBranchSchool().getName());
                    ImageView headIv = (ImageView) findViewById(R.id.ivHead);
                    PhotoUtil.showCirclePhoto(TrainingEvaluationActivity.this, headIv, trainer.getPhotoUrl(), R.drawable.img_def_head);
                    confirmBtn.setOnClickListener(TrainingEvaluationActivity.this);
                    findViewById(R.id.phone).setOnClickListener(TrainingEvaluationActivity.this);
                    float eval = trainer.getEval();
                    ((RatingBar) findViewById(R.id.rating_bar)).setRating(eval);
                    ((TextView) findViewById(R.id.eval)).setText(getString(R.string.eval, eval));
                    ((TextView) findViewById(R.id.duration)).setText(getString(R.string.order_duration, order.getOrderDuration() / 60));
                    break;
                case GET_ORDER_FAIL:
                case GET_EVALUATION_FAIL:
                case GET_DATA_FAIL:
                    BaseResponse baseResponse = (BaseResponse) msg.obj;
                    if (baseResponse != null) {
                        if (baseResponse.toLogin()) {
                            toLogin();
                        }
                        toast(baseResponse.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
                        finish();
                    }
                    break;
                case GET_LABELS_SUCCESS:
                    linearLayout.setVisibility(View.VISIBLE);
                    adapter.setList(labels);
                    adapter.notifyDataSetChanged();
                    break;
                case GET_LABELS_FAIL:
                    toast(R.string.get_labels_fail, Toast.LENGTH_SHORT);
                    finish();
                    break;
                case EVALUATE_SUCCESS: {
                    confirmBtn.setVisibility(View.GONE);
                    commentEt.setEnabled(false);
                    if (commentEt.getText().toString().trim().isEmpty()) {
                        commentEt.setVisibility(View.GONE);
                    }
                    scoreRatingBar.setIsIndicator(true);
                    gridView.setEnabled(false);
                    if (selectedLabels.isEmpty()) {
                        gridView.setVisibility(View.GONE);
                        linearLayout.findViewById(R.id.label_hint).setVisibility(View.GONE);
                    } else {
                        adapter.setList(selectedLabels);
                        adapter.notifyDataSetChanged();
                    }
                    toast((String) msg.obj, Toast.LENGTH_SHORT);
                    LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
                    layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.view_10);
                    setResult(RESULT_OK);
                    break;
                }
                case EVALUATE_FAIL:
                    BaseResponse<BaseResponseData> response = (BaseResponse<BaseResponseData>) msg.obj;
                    if (response != null) {
                        if (response.toLogin()) {
                            toLogin();
                        }
                        toast(response.getMsg(), R.string.submit_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.submit_fail, Toast.LENGTH_SHORT);
                    }
                    break;
                case GET_EVALUATION_SUCCESS: {
                    LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
                    layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.view_10);
                    linearLayout.setVisibility(View.VISIBLE);
                    confirmBtn.setVisibility(View.GONE);
                    commentEt.setEnabled(false);
                    scoreRatingBar.setIsIndicator(true);
                    gridView.setEnabled(false);
                    EvaluationResponseData responseData = (EvaluationResponseData) msg.obj;
                    String commentStr = responseData.getComment();
                    if (TextUtil.isEmpty(commentStr)) {
                        commentEt.setVisibility(View.GONE);
                    } else {
                        commentEt.setText(commentStr);
                    }
                    scoreRatingBar.setRating(responseData.getScore());
                    List<Label> labels = responseData.getLabelList();
                    if (labels == null || labels.isEmpty()) {
                        gridView.setVisibility(View.GONE);
                        linearLayout.findViewById(R.id.label_hint).setVisibility(View.GONE);
                    } else {
                        selectedLabels.clear();
                        selectedLabels.addAll(labels);
                        adapter.setList(labels);
                        adapter.notifyDataSetChanged();
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
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.training_evaluation_activity);
        Intent intent = getIntent();
        if (Order.EVALED.equals(intent.getStringExtra(Order.EVAL_STATUS))) {
            initTitle(R.string.evaluation_detail, true);
        } else {
            initTitle(R.string.training_evaluation, true);
        }

        scoreRatingBar = (RatingBar) findViewById(R.id.score_rating_bar);
        scoreRatingBar.setOnRatingBarChangeListener(this);
        scoreLevelTv = (TextView) findViewById(R.id.score_level);
        scoreRatingBar.setRating(3f);
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        confirmBtn = (Button) linearLayout.findViewById(R.id.confirm);
        commentEt = (EditText) linearLayout.findViewById(R.id.comment);
        gridView = (GridView) linearLayout.findViewById(R.id.grid_view);
        adapter = new LabelAdapter(TrainingEvaluationActivity.this, selectedLabels);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(TrainingEvaluationActivity.this);
        final String id = intent.getStringExtra(Order.ID);
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取订单详情
                    OrderDetailRequestData requestData = new OrderDetailRequestData();
                    requestData.setId(id);
                    BaseRequest request = new BaseRequest(requestData);
                    BaseResponse<OrderDetailResponseData> response = request.sendRequest(OrderDetailResponseData.class);
                    if (BaseResponse.CODE_SUCCESS == response.getCode()) {
                        order = response.getResponseData().getOrder();
                        handler.sendEmptyMessage(GET_ORDER_SUCCESS);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = GET_ORDER_FAIL;
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }

                    // 获取评价详情
                    if (order != null) {
                        if (Order.EVALED.equals(order.getIsEval())) {
                            EvaluationRequestData evaluationRequestData = new EvaluationRequestData();
                            evaluationRequestData.setOrderId(id);
                            request.setRequestData(evaluationRequestData);
                            BaseResponse<EvaluationResponseData> evaluationResponse = request.sendRequest(EvaluationResponseData.class);
                            Message msg = Message.obtain();
                            if (BaseResponse.CODE_SUCCESS == evaluationResponse.getCode()) {
                                msg.what = GET_EVALUATION_SUCCESS;
                                msg.obj = evaluationResponse.getResponseData();
                            } else {
                                msg.what = GET_EVALUATION_FAIL;
                                msg.obj = evaluationResponse;
                            }
                            handler.sendMessage(msg);
                        } else {
                            // 获取标签
                            LabelHelper helper = new LabelHelper();
                            labels = helper.getLabelsSync();
                            if (labels != null && !labels.isEmpty()) {
                                handler.sendEmptyMessage(GET_LABELS_SUCCESS);
                            } else {
                                handler.sendEmptyMessage(GET_LABELS_FAIL);
                            }
                        }
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(GET_DATA_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.confirm:
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            progressDialog.setMessage(getText(R.string.submitting_request));
                            progressDialog.show();
                            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        PublicEvaluationRequestData requestData = new PublicEvaluationRequestData();
                                        requestData.setScore(Math.round(scoreRatingBar.getRating()));
                                        requestData.setComment(commentEt.getText().toString().trim());
                                        requestData.setOrderId(order.getId());
                                        String ids = "";
                                        for (int i = 0; i < selectedLabels.size(); i++) {
                                            if (i == 0) {
                                                ids += selectedLabels.get(i).getId();
                                            } else {
                                                ids += "," + selectedLabels.get(i).getId();
                                            }
                                        }
                                        requestData.setLabelIds(ids);
                                        BaseRequest request = new BaseRequest();
                                        request.setRequestData(requestData);
                                        BaseResponse<BaseResponseData> response = request.sendRequest(BaseResponseData.class);
                                        Message msg = Message.obtain();
                                        if (BaseResponse.CODE_SUCCESS == response.getCode()) {
                                            msg.what = EVALUATE_SUCCESS;
                                            msg.obj = response.getMsg();
                                        } else {
                                            msg.what = EVALUATE_FAIL;
                                            msg.obj = response;
                                        }
                                        handler.sendMessage(msg);
                                    } catch (Exception e) {
                                        handler.sendEmptyMessage(EVALUATE_FAIL);
                                        LogUtil.e(TAG, e.toString(), e);
                                    } finally {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }

                    }
                };
                BaseDialog dialog = new BaseDialog.Builder(this).setTitle(R.string.hint).setMessage(getString(R.string.submit_evaluation_or_not)).setPositiveButton(R.string.confirm, onClickListener).setNegativeButton(R.string.cancel, onClickListener).create();
                dialog.show();
                break;
            case R.id.phone:
                String phone = order.getTrainer().getPhone();
                if (!TextUtil.isEmpty(phone)) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        Label label = (Label) checkedTextView.getTag();
        if (!checkedTextView.isChecked()) {
            selectedLabels.add(label);
        } else {
            selectedLabels.remove(label);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        int score = Math.round(ratingBar.getRating());
        if (score < 1) {
            ratingBar.setRating(1);
            return;
        }
        ScoreLevel[] values = ScoreLevel.values();
        for (int i = 0; i < values.length; i++) {
            ScoreLevel scoreLevel = values[i];
            if (score == scoreLevel.getScore()) {
                scoreLevelTv.setText(scoreLevel.getName());
                break;
            }
        }
    }

}
