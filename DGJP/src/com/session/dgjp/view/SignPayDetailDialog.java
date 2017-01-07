package com.session.dgjp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.session.common.utils.AnimUtil;
import com.session.dgjp.R;

/**
 * Created by user on 2017-01-03.
 */
public class SignPayDetailDialog extends Dialog implements View.OnClickListener {
    TextView mTvTotal;
    TextView mTvTestTotal;
    LinearLayout mLlTestSpace;
    LinearLayout mLlTestTotal;

    TextView mTvFeeTest1;
    TextView mTvFeeTest2;
    TextView mTvFeeTest3;
    TextView mTvFeePapers;

    TextView mTvSpaceFee;
    TextView mTvDataFee;

    TextView mTvFeePhysical;
    TextView mTvFeeResidence;
    TextView mTvFeePhoto;
    TextView mTvResidencePhoto;

    TextView mTvConfirm;

    CheckBox mIvPhysical;
    CheckBox mIvResidence;
    CheckBox mIvPhoto;
    CheckBox mIvResidencePhoto;

    ImageView mIvArrDown;

    boolean mIsShow;

    private String mTotal, mTestTotal, mFeeTest1, mFeeTest2, mFeeTest3, mFeePapers, mFeeSpace, mFeeData, mFeePhysical,
            mFeeResidence, mFeePhoto, mResidencePhoto;

    private onConfirmOnclickListener mOnConfirmOnclickListener;
    private onPhysicalOnclickListener mOnPhysicalOnclickListener;
    private onResidenceOnclickListener mOnResidenceOnclickListener;
    private onPhotoOnclickListener mOnPhotoOnclickListener;
    private onResidencePhotoOnclickListener mOnResidencePhotoOnclickListener;

    public void setOnConfirmOnclickListener(onConfirmOnclickListener onclickListener) {
        this.mOnConfirmOnclickListener = onclickListener;
    }

    public void setOnPhysicalOnclickListener(onPhysicalOnclickListener onclickListener) {
        this.mOnPhysicalOnclickListener = onclickListener;
    }

    public void setOnResidenceOnclickListener(onResidenceOnclickListener onclickListener) {
        this.mOnResidenceOnclickListener = onclickListener;
    }

    public void setOnPhotoOnclickListener(onPhotoOnclickListener onclickListener) {
        this.mOnPhotoOnclickListener = onclickListener;
    }

    public void setOnResidencePhotoOnclickListener(onResidencePhotoOnclickListener onclickListener) {
        this.mOnResidencePhotoOnclickListener = onclickListener;
    }


    public SignPayDetailDialog(Context context) {
        this(context, R.style.SignPayDetailDialog);
    }

    public SignPayDetailDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign_pay_detail);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mIvPhysical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnPhysicalOnclickListener != null) {
                    if (isChecked) {
                        mOnPhysicalOnclickListener.onPhysicalClick();
                    } else {
                        mOnPhysicalOnclickListener.onPhysicalUnClick();
                    }
                }
            }
        });

        mIvResidence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnResidenceOnclickListener != null) {
                    if (isChecked) {
                        mOnResidenceOnclickListener.onResidenceClick();
                    } else {
                        mOnResidenceOnclickListener.onResidenceUnClick();
                    }
                }
            }
        });

        mIvPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnPhotoOnclickListener != null) {
                    if (isChecked) {
                        mOnPhotoOnclickListener.onPhotoClick();
                    } else {
                        mOnPhotoOnclickListener.onPhotoUnClick();
                    }
                }
            }
        });

        mIvResidencePhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnResidencePhotoOnclickListener != null) {
                    if (isChecked) {
                        mOnResidencePhotoOnclickListener.onResidencePhotoClick();
                    } else {
                        mOnResidencePhotoOnclickListener.onResidenceUnPhotoClick();
                    }
                }
            }
        });
    }

    private void initData() {
        if (mTotal != null)
            mTvTotal.setText(mTotal);
        if (mTestTotal != null)
            mTvTestTotal.setText(mTestTotal);
        if (mFeeTest1 != null)
            mTvFeeTest1.setText(mFeeTest1);
        if (mFeeTest2 != null)
            mTvFeeTest2.setText(mFeeTest2);
        if (mFeeTest3 != null)
            mTvFeeTest3.setText(mFeeTest3);
        if (mFeePapers != null)
            mTvFeePapers.setText(mFeePapers);
        if (mFeeSpace != null)
            mTvSpaceFee.setText(mFeeSpace);
        if (mFeeData != null)
            mTvDataFee.setText(mFeeData);
        if (mFeePhysical != null)
            mTvFeePhysical.setText(mFeePhysical);
        if (mFeeResidence != null)
            mTvFeeResidence.setText(mFeeResidence);
        if (mFeePhoto != null)
            mTvFeePhoto.setText(mFeePhoto);
        if (mResidencePhoto != null)
            mTvResidencePhoto.setText(mResidencePhoto);
    }

    private void initView() {
        mTvTotal = (TextView) findViewById(R.id.tv_total);
        mTvTestTotal = (TextView) findViewById(R.id.tv_test_total);
        mTvFeeTest1 = (TextView) findViewById(R.id.tv_fee_test1);
        mTvFeeTest2 = (TextView) findViewById(R.id.tv_fee_test2);
        mTvFeeTest3 = (TextView) findViewById(R.id.tv_fee_test3);
        mTvFeePapers = (TextView) findViewById(R.id.tv_fee_papers);
        mTvSpaceFee = (TextView) findViewById(R.id.tv_space_fee);
        mTvDataFee = (TextView) findViewById(R.id.tv_data_fee);
        mTvFeePhysical = (TextView) findViewById(R.id.tv_fee_physical);
        mTvFeeResidence = (TextView) findViewById(R.id.tv_fee_residence);
        mTvFeePhoto = (TextView) findViewById(R.id.tv_fee_photo);
        mTvResidencePhoto = (TextView) findViewById(R.id.tv_residence_photo);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);

        mIvPhysical = (CheckBox) findViewById(R.id.iv_physical);
        mIvResidence = (CheckBox) findViewById(R.id.iv_residence);
        mIvPhoto = (CheckBox) findViewById(R.id.iv_photo);
        mIvResidencePhoto = (CheckBox) findViewById(R.id.iv_residence_photo);

        mLlTestSpace = (LinearLayout) findViewById(R.id.ll_test_space);
        mLlTestTotal = (LinearLayout) findViewById(R.id.ll_test_total);
        mIvArrDown = (ImageView) findViewById(R.id.iv_arr_down);


        mLlTestSpace.setVisibility(View.GONE);

        mTvConfirm.setOnClickListener(this);
        mLlTestTotal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_test_total:
                showTestSpace();
                break;
            case R.id.tv_confirm:
                mOnConfirmOnclickListener.onConfirmClick();
                break;
        }
    }

    private void showTestSpace() {
        if (!mIsShow) {
            mLlTestSpace.setVisibility(View.VISIBLE);
            AnimUtil.RotationAnimator(0f, 180f, 0, 0, 0, 0, mIvArrDown, 500);
            mIsShow = true;
        } else {
            AnimUtil.RotationAnimator(180f, 0f, 0, 0, 0, 0, mIvArrDown, 500);
            mLlTestSpace.setVisibility(View.GONE);
            mIsShow = false;
        }
    }

    public void upDataTotalFee(String total){
        mTvTotal.setText(total);
    }

    public void setTotal(String total) {
        mTotal = total;
    }

    public void setTestTotal(String testTotal) {
        mTestTotal = testTotal;
    }

    public void setFeeTest1(String feeTest1) {
        mFeeTest1 = feeTest1;
    }

    public void setFeeTest2(String feeTest2) {
        mFeeTest2 = feeTest2;
    }

    public void setFeeTest3(String feeTest3) {
        mFeeTest3 = feeTest3;
    }

    public void setFeePapers(String feePapers) {
        mFeePapers = feePapers;
    }

    public void setFeeSpace(String feeSpace) {
        mFeeSpace = feeSpace;
    }

    public void setFeeData(String feeData) {
        mFeeData = feeData;
    }

    public void setFeePhysical(String feePhysical) {
        mFeePhysical = feePhysical;
    }

    public void setFeeResidence(String feeResidence) {
        mFeeResidence = feeResidence;
    }

    public void setFeePhoto(String feePhoto) {
        mFeePhoto = feePhoto;
    }

    public void setResidencePhoto(String residencePhoto) {
        mResidencePhoto = residencePhoto;
    }


    public interface onConfirmOnclickListener {
        public void onConfirmClick();
    }

    public interface onPhysicalOnclickListener {
        public void onPhysicalClick();

        public void onPhysicalUnClick();
    }

    public interface onResidenceOnclickListener {
        public void onResidenceClick();

        public void onResidenceUnClick();
    }

    public interface onPhotoOnclickListener {
        public void onPhotoClick();

        public void onPhotoUnClick();
    }

    public interface onResidencePhotoOnclickListener {
        public void onResidencePhotoClick();

        public void onResidenceUnPhotoClick();
    }
}
