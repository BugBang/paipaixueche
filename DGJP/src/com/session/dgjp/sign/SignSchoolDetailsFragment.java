package com.session.dgjp.sign;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.common.utils.DialogUtil;
import com.session.common.utils.IntentUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.SchoolDetails;
import com.session.dgjp.request.SignSchoolDetailsRequestData;
import com.session.dgjp.view.NoScrollListview;
import com.session.dgjp.view.StarBar;

/**
 * Created by user on 2016-11-15.
 */
public class SignSchoolDetailsFragment extends BaseFragment implements SignSchoolDetailsListAdapter.SignListener {

    private TextView mTvTime;
    private TextView mTvAddress;
    private TextView mTvCallCenter;

    private TextView mTvComments;
    private TextView mTvAllComments;

    private StarBar mCommentStart1;
    private TextView mCommentUserName1;
    private TextView mCommentContent1;
    private LinearLayout mLlComment1;

    private StarBar mCommentStart2;
    private TextView mCommentUserName2;
    private TextView mCommentContent2;
    private LinearLayout mLlComment2;

    private StarBar mCommentStart3;
    private TextView mCommentUserName3;
    private TextView mCommentContent3;
    private LinearLayout mLlComment3;

    private ImageView mIvBack;
    private TextView mTvTitle;
    private NoScrollListview mNoScrollListview;
    private Gson mGson;
    public static final String SCHOOL_ID = "school_id";
    public static final String SCHOOL_TITLE = "school_title";
    private SchoolDetails.ListBean.SchoolDetail mSchoolDetail;
    private DialogUtil mDialogUtil;

    public static SignSchoolDetailsFragment newInstance() {
        return new SignSchoolDetailsFragment();
    }

    @Override
    protected int getContentRes() {
        return R.layout.sign_school_details_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initBundle();
        getData();
    }

    private void initView() {
        mDialogUtil = new DialogUtil(act);

        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mTvAddress = (TextView) view.findViewById(R.id.tv_address);
        mTvCallCenter = (TextView) view.findViewById(R.id.tv_call_center);
        mTvComments = (TextView) view.findViewById(R.id.tv_comments);
        mTvAllComments = (TextView) view.findViewById(R.id.tv_all_comments);
        mCommentStart1 = (StarBar) view.findViewById(R.id.comment_start_1);
        mCommentUserName1 = (TextView) view.findViewById(R.id.comment_user_name_1);
        mCommentContent1 = (TextView) view.findViewById(R.id.comment_content_1);
        mLlComment1 = (LinearLayout) view.findViewById(R.id.ll_comment_1);

        mCommentStart2 = (StarBar) view.findViewById(R.id.comment_start_2);
        mCommentUserName2 = (TextView) view.findViewById(R.id.comment_user_name_2);
        mCommentContent2 = (TextView) view.findViewById(R.id.comment_content_2);
        mLlComment2 = (LinearLayout) view.findViewById(R.id.ll_comment_2);

        mCommentStart3 = (StarBar) view.findViewById(R.id.comment_start_3);
        mCommentUserName3 = (TextView) view.findViewById(R.id.comment_user_name_3);
        mCommentContent3 = (TextView) view.findViewById(R.id.comment_content_3);
        mLlComment3 = (LinearLayout) view.findViewById(R.id.ll_comment_3);


        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.lv_class_list);
        mNoScrollListview.setAdapter(new SignSchoolDetailsListAdapter(null, act, this));
        mIvBack.setOnClickListener(this);
        mTvCallCenter.setOnClickListener(this);
        mTvAllComments.setOnClickListener(this);
    }

    private int id;
    private void initBundle() {
        Bundle arguments = getArguments();
        if (arguments!=null){
            id = arguments.getInt(SCHOOL_ID);
            String title = arguments.getString(SCHOOL_TITLE);
            mTvTitle.setText(title);
        }
    }

    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        SignSchoolDetailsRequestData requestData = new SignSchoolDetailsRequestData();
        requestData.setId(id);

        String data = new Gson().toJson(requestData);

        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                logI(" response="+response);
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
        }.request(Constants.URL_GET_SCHOOL_DETAILS, data, progressDialog, true);
    }

    private void parseData(String data) {
        if (mGson == null) {
            mGson = new Gson();
        }
        SchoolDetails schoolDetails = mGson.fromJson(data, SchoolDetails.class);
        mSchoolDetail = schoolDetails.getList().get(0).getList().get(0);
        if (mSchoolDetail!=null){
            String timePeroid = mSchoolDetail.getTimePeroid();
            String[] split = timePeroid.split(",");
            String frontTime = split[0];
            String endTime = split[split.length-1];
            if (!(frontTime.indexOf(":")!=-1)){
                frontTime = frontTime+":00";
            }
            if (!(endTime.indexOf(":")!=-1)){
                endTime = endTime+":00";
            }
            mTvTime.setText(frontTime+" - "+endTime);
            mTvAddress.setText(mSchoolDetail.getAddress());
            String centerPhone = mSchoolDetail.getPhone();
            if (centerPhone.length() == 11){
                mTvCallCenter.setText(centerPhone);
            }else {
                String front = centerPhone.substring(0,4);
                String end = centerPhone.substring(4,centerPhone.length());
                mTvCallCenter.setText(front+" - "+end);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                removeFragment();
                break;
            case R.id.tv_call_center:
                clickServicePhone();
                break;
            case R.id.tv_all_comments:
                Bundle _b = new Bundle();
                _b.putInt(SignAllCommentFragment.ID,id);
                addFragment(R.id.content,SignAllCommentFragment.newInstance(),_b);
                break;
        }
    }

    private void clickServicePhone() {
        if (mSchoolDetail!=null){
            final String kf_phone = mSchoolDetail.getPhone();

            mDialogUtil.confirm("提示", "确定拨打客服电话?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (kf_phone != null) {
                        startActivity(IntentUtil.getCallNumberIntent(kf_phone));
                    } else {
                        toastLong("未找到客服电话");
                    }
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onSign(SchoolDetails schoolDetails) {
        addFragment(R.id.content, SignClassDetailFragment.newInstance(), null);
    }
}
