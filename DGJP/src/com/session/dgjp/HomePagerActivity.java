package com.session.dgjp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.DialogUtil;
import com.session.common.utils.IntentUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.dgjp.enity.EaseUser;
import com.session.dgjp.enity.HomePager;
import com.session.dgjp.home.CarouselViewAdapter;
import com.session.dgjp.request.GetEaseAccountRequestData;
import com.session.dgjp.request.HomePagerRequestData;
import com.session.dgjp.service.LocationService;
import com.session.dgjp.share.ShareActivity;
import com.session.dgjp.sign.SignActivity;
import com.session.dgjp.view.CarouselView;

import java.util.List;

public class HomePagerActivity extends BaseActivity implements OnClickListener {

    private CarouselView mTopAdView;
    private PosterAdapter mPosterAdapter;
    private String[] mAdvs;

    private PullToRefreshScrollView mPullToRefreshScrollView;

    private LinearLayout mSign;
    private LinearLayout mLlDriveFlow;
    private LinearLayout mlLlRecommend;
    private LinearLayout mLlServe;
    private LinearLayout mLlExperience;
    private LinearLayout mLlAssistant;

    private LinearLayout mLlCallCenter;

    private ImageView mIvAd;

    private Gson mGson;
    private Intent mIntent;
    private boolean mIsCheck;
    private String mEaseName;
    private String mPassword;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_home_pager);
        mIntent = new Intent();
        mDialogUtil = new DialogUtil(this);
        getLocalEase();
        startLocalService();
        getData();
        getEaseAccount();
        initView();
    }

    private void getLocalEase() {
        mEaseName = SharedPreferencesUtil.getString(SharedPreferencesUtil.EASE_USER_NAME, "");
        mPassword = SharedPreferencesUtil.getString(SharedPreferencesUtil.EASE_USER_PASSWORD, "");
    }

    private void startLocalService() {
        Intent mapIntent = new Intent(HomePagerActivity.this, LocationService.class);
        startService(mapIntent);
    }

    private void initView() {
        mLlDriveFlow = (LinearLayout) findViewById(R.id.ll_drive_flow);
        mlLlRecommend = (LinearLayout) findViewById(R.id.ll_recommend);
        mLlServe = (LinearLayout) findViewById(R.id.ll_serve);
        mLlExperience = (LinearLayout) findViewById(R.id.ll_experience);
        mLlCallCenter = (LinearLayout) findViewById(R.id.ll_call_center);
        mLlAssistant = (LinearLayout) findViewById(R.id.ll_assistant);
        mIvAd = (ImageView) findViewById(R.id.iv_ad);

        mSign = (LinearLayout) findViewById(R.id.sign);
        mTopAdView = (CarouselView) findViewById(R.id.top_ad_view);
        //		mTvLocal = (TextView) findViewById(R.id.tv_local);
        //		mTvCall = (TextView) findViewById(R.id.tv_call);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
        //去掉下拉刷新,暂时不需要
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                toastLong("onRefresh");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mPullToRefreshScrollView.onRefreshComplete();
                        toastLong("onRefreshComplete");
                    }
                }, 2000);
            }
        });
        mSign.setOnClickListener(this);
        mLlDriveFlow.setOnClickListener(this);
        mlLlRecommend.setOnClickListener(this);
        mLlServe.setOnClickListener(this);
        mLlExperience.setOnClickListener(this);
        mLlAssistant.setOnClickListener(this);
        mLlCallCenter.setOnClickListener(this);
        //		mTvLocal.setOnClickListener(this);
        //		mTvCall.setOnClickListener(this);
        mPosterAdapter = new PosterAdapter(this);
        mTopAdView.setAdapter(mPosterAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //		case R.id.tv_local:
            //			toastLong("tv_local");
            //			break;
            //		case R.id.tv_call:
            //			toastLong("tv_call");
            //			break;
            case R.id.sign:
                checkIntent();
                mIntent.setClass(this, SignActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_drive_flow:
                checkIntent();
                mIntent.setClass(this, WebViewActivity.class);
                mIntent.putExtra(WebViewActivity.LOAD_URL, "http://www.papaxueche.com/cheet/process.html");
                mIntent.putExtra(WebViewActivity.TITLE, "学车流程");
                startActivity(mIntent);
                break;
            case R.id.ll_recommend:
                checkIntent();
                mIntent.setClass(this, ShareActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_serve:
                checkIntent();
                mIntent.setClass(this, WebViewActivity.class);
                mIntent.putExtra(WebViewActivity.LOAD_URL, "http://www.papaxueche.com/cheet/service.html");
                mIntent.putExtra(WebViewActivity.TITLE, "服务优势");
                startActivity(mIntent);
                break;
            case R.id.ll_experience:
                checkIntent();
                mIntent.setClass(this, WebViewActivity.class);
                mIntent.putExtra(WebViewActivity.LOAD_URL, "http://www.papaxueche.com/cheet/address.html");
                mIntent.putExtra(WebViewActivity.TITLE, "体验门店");
                startActivity(mIntent);
                break;
            case R.id.ll_call_center:
                clickServicePhone();
                break;
            case R.id.ll_assistant:
                mIsCheck = true;
                toChat();
                break;
        }
    }

    private void toChat() {
        if (ChatClient.getInstance().isLoggedInBefore()) {
            Intent intent = new IntentBuilder(HomePagerActivity.this)
                    .setServiceIMNumber("kefuchannelimid_148313")
                    .setVisitorInfo(ContentFactory.createVisitorInfo(null)
                            .nickName(account.getName())
                            .phone(account.getPhone()))
                    .build();
            startActivity(intent);
        } else {
            toastLong("登录中...请稍等");
            getEaseAccount();
        }
    }

    private void getEaseAccount() {
        GetEaseAccountRequestData requestData = new GetEaseAccountRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setPhone(account.getPhone());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            paresData(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getEaseAccount();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.request(Constants.URL_GET_EASE_USER, data, null, true);
    }


    private void paresData(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        EaseUser easeUser = mGson.fromJson(response, EaseUser.class);
        final String name = easeUser.getUserName();
        final String password = easeUser.getPassword();

        //        if (easeUser.getResult().equals("success2")) {
        CreateEase(name, password);
        //            return;
        //        }
        if (!mEaseName.equals(name)) {
            SharedPreferencesUtil.saveString(SharedPreferencesUtil.EASE_USER_NAME, name);
            SharedPreferencesUtil.saveString(SharedPreferencesUtil.EASE_USER_PASSWORD, password);
            ChatClient.getInstance().logout(true, new Callback() {
                @Override
                public void onSuccess() {
                    LoginEase(name, password);
                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
            //            return;
        }
        //        if (!ChatClient.getInstance().isLoggedInBefore()){
        //            LoginEase(name,password);
        //        }
    }

    private void CreateEase(final String name, final String passWord) {

        ChatClient.getInstance().createAccount(name, passWord, new Callback() {
            @Override
            public void onSuccess() {
                LoginEase(name, passWord);
            }

            @Override
            public void onError(int i, String s) {
                LoginEase(name, passWord);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void LoginEase(String name, String passWord) {
        ChatClient.getInstance().login(name, passWord, new Callback() {
            @Override
            public void onSuccess() {
                if (mIsCheck) {
                    mIsCheck = false;
                    toChat();
                }
            }

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    private void checkIntent() {
        if (mIntent == null) {
            mIntent = new Intent();
        }
    }

    private DialogUtil mDialogUtil;

    private void clickServicePhone() {
        //        if (mBsListBean != null) {
        //            final String kf_phone = mBsListBean.getPhone();
        mDialogUtil.confirm("提示", "确定拨打客服电话?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //                    if (kf_phone != null) {
                startActivity(IntentUtil.getCallNumberIntent("076923123666"));
                //                    } else {
                //                        toastLong("未找到客服电话");
                //                    }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //        }
    }


    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        HomePagerRequestData requestData = new HomePagerRequestData();
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
        }.request(Constants.URL_GET_HOME_IMG, data, progressDialog, true);
    }

    private void parseData(String data) {
        if (mGson == null) {
            mGson = new Gson();
        }
        HomePager homePager = mGson.fromJson(data, HomePager.class);
        if (homePager != null) {
            List<HomePager.AdvertisementListBean> advertisementList = homePager.getAdvertisementList();
            List<HomePager.RotationListBean> rotationList = homePager.getRotationList();
            String advUrl = advertisementList.get(0).getFileUrl();
            Glide.with(this).load(Constants.RELEASE_IMG_URL + advUrl).into(mIvAd);
            mAdvs = new String[rotationList.size()];
            for (int i = 0; i < rotationList.size(); i++) {
                mAdvs[i] = rotationList.get(i).getFileUrl();
            }
            mPosterAdapter = new PosterAdapter(this);
            mTopAdView.setAdapter(mPosterAdapter);
        }
    }

    class PosterAdapter implements CarouselViewAdapter {

        Context mContext;
        private LayoutInflater inflater;

        public PosterAdapter(Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public View getView(final int position) {
            View view = inflater.inflate(R.layout.top_ad_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Glide.with(mContext)
                    .load(Constants.RELEASE_IMG_URL + mAdvs[position])
                    .placeholder(R.drawable.placeholder_img)
                    .into(imageView);
            //            imageView.setOnClickListener(new View.OnClickListener() {;
            return view;
        }

        @Override
        public int getCount() {
            if (mAdvs != null && mAdvs.length > 0) {
                return mAdvs.length;
            }
            return 0;
        }
    }
}
