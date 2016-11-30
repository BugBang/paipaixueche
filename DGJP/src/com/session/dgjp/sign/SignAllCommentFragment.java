package com.session.dgjp.sign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.SignAllComment;
import com.session.dgjp.request.SignSchoolCommentRequestData;

import java.util.List;

/**
 * Created by user on 2016-11-25.
 */
public class SignAllCommentFragment extends BaseFragment {

    private ListView mListView;
    private ImageView mImageView;
    public static final String ID = "id";
    private int id;
    private SignAllComment mSignAllComment;
    private List<SignAllComment.SignAllCommentModel> mSignAllCommentModel;
    private SignSchoolCommentListAdapter mSignSchoolCommentListAdapter;

    public static SignAllCommentFragment newInstance() {
        return new SignAllCommentFragment();
    }

    @Override
    protected int getContentRes() {
        return R.layout.sign_all_comment_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mSignSchoolCommentListAdapter = new SignSchoolCommentListAdapter(mSignAllCommentModel, act);
        if (arguments != null) {
            id = arguments.getInt(ID);
        }
        mImageView = (ImageView) view.findViewById(R.id.iv_back);
        mListView = (ListView) view.findViewById(R.id.lv_comment_list);

        mImageView.setOnClickListener(this);
        mListView.setAdapter(mSignSchoolCommentListAdapter);

        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                removeFragment();
                break;

            default:
                break;
        }
    }

    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        SignSchoolCommentRequestData requestData = new SignSchoolCommentRequestData();
        requestData.setId(id);
        logI("id=" + id);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseData(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getData();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("网络异常，请稍后重试");
                } finally {

                }
            }
        }.request(Constants.URL_GET_SCHOOL_COMMENT, data, progressDialog, true);
    }

    private Gson mGson;

    private void parseData(String data) {
        if (mGson == null) {
            mGson = new Gson();
        }
        mSignAllComment = mGson.fromJson(data, SignAllComment.class);
        mSignAllCommentModel = mSignAllComment.getList();
        if (mSignAllCommentModel != null) {
            mSignSchoolCommentListAdapter.updateListViewData(mSignAllCommentModel);
        }
    }
}
