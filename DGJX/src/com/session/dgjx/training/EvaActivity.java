package com.session.dgjx.training;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.TextUtil;
import com.session.dgjx.Constants;
import com.session.dgjx.R;
import com.session.dgjx.event.EventMsg;
import com.session.dgjx.event.EventTag;
import com.session.dgjx.request.StudentEvaRequestData;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2017-02-17.
 */
public class EvaActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_eva_content)
    EditText mEtEvaContent;
    @BindView(R.id.bt_cancel)
    Button mBtCancel;
    @BindView(R.id.bt_confirm)
    Button mBtConfirm;
    private String mId;
    private String mEvaContent;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_train_eva);
        ButterKnife.bind(this);
        mId = getIntent().getStringExtra("id");
    }

    @OnClick({R.id.bt_cancel, R.id.bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_confirm:
                if (TextUtil.isEmpty(mId)){
                    toastLong("系统错误,请稍候重试!");
                    return;
                }
                if (checkData()){
                    postEva();
                }
                break;
        }
    }

    private boolean checkData() {
        mEvaContent = mEtEvaContent.getText().toString().trim();
        if (TextUtil.isEmpty(mEvaContent)){
            toastLong("评论内容不可为空");
            return false;
        }
        return true;
    }

    private void postEva() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等...", false);
        StudentEvaRequestData requestData = new StudentEvaRequestData();
        requestData.setId(mId);
        requestData.setCoachEvaluate(mEvaContent);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseData(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            postEva();
                            break;
                        default:
                            toastShort(msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("网络异常，请稍后重试");
                } finally {
                }
            }
        }.request(Constants.URL_POST_COACH_EVAL, data, progressDialog, true);
    }

    private void parseData(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json != null && json.has("state")) {
                int state = json.optInt("state");
                if (state == 1) {
                    toastLong("评论成功");
                    EventBus.getDefault().post(new EventMsg(null, EventTag.EVENT_COMMENT_FINISH));
                    finish();
                } else {
                    toastLong("评论失败,请稍候再试");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
