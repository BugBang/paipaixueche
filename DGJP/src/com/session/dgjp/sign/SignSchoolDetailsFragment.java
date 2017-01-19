package com.session.dgjp.sign;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.common.utils.DialogUtil;
import com.session.common.utils.IntentUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.WebViewActivity;
import com.session.dgjp.enity.SchoolDetails;
import com.session.dgjp.request.SignSchoolDetailsRequestData;
import com.session.dgjp.view.NoScrollListview;
import com.session.dgjp.view.StarBar;

import java.util.List;

/**
 * Created by user on 2016-11-15.
 */
public class SignSchoolDetailsFragment extends BaseFragment implements SignSchoolDetailsListAdapter.SignListener {

    private TextView mTvTime;
    private TextView mTvAddress;
    private TextView mTvCallCenter;

    private TextView mTvComments;
    private TextView mTvAllComments;

    private LinearLayout mLlComment1;
    private StarBar mCommentStart1;
    private TextView mCommentUserName1;
    private TextView mCommentContent1;

    private LinearLayout mLlComment2;
    private StarBar mCommentStart2;
    private TextView mCommentUserName2;
    private TextView mCommentContent2;

    private LinearLayout mLlComment3;
    private StarBar mCommentStart3;
    private TextView mCommentUserName3;
    private TextView mCommentContent3;


    private ImageView mIvBack;
    private ImageView mIvTopImg;
    private TextView mTvTitle;
    private NoScrollListview mNoScrollListview;
    private Gson mGson;
    public static final String SCHOOL_ID = "school_id";
    public static final String SCHOOL_TITLE = "school_title";
    private DialogUtil mDialogUtil;
    private SignSchoolDetailsListAdapter mSignSchoolDetailsListAdapter;
    private String mPanoramaUrl;
    private ImageView mIvPanorama;
    private String mTitle;

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
        mIvTopImg = (ImageView) view.findViewById(R.id.iv_top_img);
        mIvPanorama = (ImageView) view.findViewById(R.id.iv_panorama);

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

        mSignSchoolDetailsListAdapter = new SignSchoolDetailsListAdapter(mScList, act, this);

        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.lv_class_list);
        mNoScrollListview.setAdapter(mSignSchoolDetailsListAdapter);
        mIvBack.setOnClickListener(this);
        mIvPanorama.setOnClickListener(this);
        mTvCallCenter.setOnClickListener(this);
        mTvAllComments.setOnClickListener(this);
    }

    private int id;

    private void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt(SCHOOL_ID);
            mTitle = arguments.getString(SCHOOL_TITLE);
            mTvTitle.setText(mTitle);
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
            }
        }.request(Constants.URL_GET_SCHOOL_DETAILS, data, progressDialog, true);
    }

    private SchoolDetails.BsListBean mBsListBean; //驾校信息
    private List<SchoolDetails.ScListBean> mScList;//班别信息
    private List<SchoolDetails.EListBean> mElList; //评论信息

    private SchoolDetails mSchoolDetails;

    private void parseData(String data) {
        if (mGson == null) {
            mGson = new Gson();
        }
        mSchoolDetails = mGson.fromJson(data, SchoolDetails.class);
        if (mSchoolDetails != null) {
            setSchoolData();
            setClassData();
            setCommentData();
        }
    }

    private void setSchoolData() {
        mBsListBean = mSchoolDetails.getBsList().get(0);
        String timePeroid = mBsListBean.getTimePeroid();
        String img = mBsListBean.getPhotoUrl();
        mPanoramaUrl = mBsListBean.getPanoramicUrl();

        Glide.with(this).load(Constants.URL_IMG_IP+img).placeholder(R.drawable.placeholder_img).into(mIvTopImg);
        mTvTime.setText(timePeroid);

        mTvAddress.setText(mBsListBean.getAddress());
        String centerPhone = mBsListBean.getPhone();
        if (centerPhone.length() == 11) {
            mTvCallCenter.setText(centerPhone);
        } else {
            String front = centerPhone.substring(0, 4);
            String end = centerPhone.substring(4, centerPhone.length());
            mTvCallCenter.setText(front + " - " + end);
        }
    }

    private void setClassData() {
        mScList = mSchoolDetails.getScList();
        mSignSchoolDetailsListAdapter.updateListViewData(mScList);
    }

    private void setCommentData() {
        mElList = mSchoolDetails.getEList();
        switch (mElList.size()) {
            case 0:
                mLlComment1.setVisibility(View.GONE);
                mLlComment2.setVisibility(View.GONE);
                mLlComment3.setVisibility(View.GONE);
                break;
            case 1:
                mLlComment2.setVisibility(View.GONE);
                mLlComment3.setVisibility(View.GONE);
                mCommentContent1.setText(mElList.get(0).getEComment());
                mCommentStart1.setStarMark(mElList.get(0).getEScore());
                mCommentUserName1.setText(mElList.get(0).getEName());
                break;
            case 2:
                mLlComment3.setVisibility(View.GONE);
                mCommentContent1.setText(mElList.get(0).getEComment());
                mCommentStart1.setStarMark(mElList.get(0).getEScore());
                mCommentUserName1.setText(mElList.get(0).getEName());

                mCommentContent2.setText(mElList.get(1).getEComment());
                mCommentStart2.setStarMark(mElList.get(1).getEScore());
                mCommentUserName2.setText(mElList.get(1).getEName());
                break;
            case 3:
                mCommentContent1.setText(mElList.get(0).getEComment());
                mCommentStart1.setStarMark(mElList.get(0).getEScore());
                mCommentUserName1.setText(mElList.get(0).getEName());
                mCommentContent2.setText(mElList.get(1).getEComment());
                mCommentStart2.setStarMark(mElList.get(1).getEScore());
                mCommentUserName2.setText(mElList.get(1).getEName());
                mCommentContent3.setText(mElList.get(2).getEComment());
                mCommentStart3.setStarMark(mElList.get(2).getEScore());
                mCommentUserName3.setText(mElList.get(2).getEName());
                break;
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
                _b.putInt(SignAllCommentFragment.ID, id);
                addFragment(R.id.content, SignAllCommentFragment.newInstance(), _b);
                break;
            case R.id.iv_panorama:
                if (!TextUtil.isEmpty(mPanoramaUrl)){
                    Intent intent = new Intent();
                    intent.setClass(act, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.LOAD_URL, mPanoramaUrl);
                    intent.putExtra(WebViewActivity.TITLE, mTitle+"全景");
                    startActivity(intent);
                }else {
                    toastLong("该分校暂时无法预览全景");
                }

                break;
        }
    }

    private void clickServicePhone() {
        if (mBsListBean != null) {
            final String kf_phone = mBsListBean.getPhone();
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
    public void onSign(SchoolDetails.ScListBean scListBean) {
        addFragment(R.id.content, SignClassDetailFragment.newInstance(), null);
    }

}
