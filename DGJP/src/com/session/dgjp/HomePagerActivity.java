package com.session.dgjp;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.dgjp.enity.HomePager;
import com.session.dgjp.home.CarouselViewAdapter;
import com.session.dgjp.request.HomePagerRequestData;
import com.session.dgjp.sign.SignActivity;
import com.session.dgjp.view.CarouselView;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class HomePagerActivity extends BaseActivity implements OnClickListener {

    private CarouselView mTopAdView;
    private PosterAdapter mPosterAdapter;
    //    private int[] mAdvs = {R.drawable.main_top_ad, R.drawable.main_top_ad, R.drawable.main_top_ad};
    private String[] mAdvs;
    //	private TextView mTvLocal,mTvCall;

    private PullToRefreshScrollView mPullToRefreshScrollView;

    private LinearLayout mSign;
    private LinearLayout mLlDriveFlow;
    private LinearLayout mlLlRecommend;
    private LinearLayout mLlServe;
    private LinearLayout mLlExperience;

    private ImageView mIvAd;

    private static final String TEST_IMG_URL = "http://10.0.0.189:8080/DGFDS_WEB/files";
    private static final String RE_IMG_URL = "http://www.papaxueche.com/";
    private Gson mGson;

    //    base_url=http://10.0.0.189:8080/DGFDS_WEB/files
    //            #base_url=http://www.papaxueche.com/
    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_home_pager);
        getData();
        initView();
    }

    private void initView() {
        mLlDriveFlow = (LinearLayout) findViewById(R.id.ll_drive_flow);
        mlLlRecommend = (LinearLayout) findViewById(R.id.ll_recommend);
        mLlServe = (LinearLayout) findViewById(R.id.ll_serve);
        mLlExperience = (LinearLayout) findViewById(R.id.ll_experience);
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
                Intent intent = new Intent(this, SignActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_drive_flow:
                Intent intent1 = new Intent(this, WebViewActivity.class);
                intent1.putExtra(WebViewActivity.LOAD_URL, "http://10.0.0.43/cheet/process.html");
                intent1.putExtra(WebViewActivity.TITLE, "学车流程");
                startActivity(intent1);
                break;
            case R.id.ll_recommend:
                //                Intent intent2 = new Intent(this, WebViewActivity.class);
                //                intent2.putExtra("url","10.0.0.43/cheet/invite.html");
                //                startActivity(intent2);
                                showShare();
//                getData();
                break;
            case R.id.ll_serve:
                Intent intent3 = new Intent(this, WebViewActivity.class);
                intent3.putExtra(WebViewActivity.LOAD_URL, "http://10.0.0.43/cheet/service.html");
                intent3.putExtra(WebViewActivity.TITLE, "服务优势");
                startActivity(intent3);
                break;
            case R.id.ll_experience:
                Intent intent4 = new Intent(this, WebViewActivity.class);
                intent4.putExtra(WebViewActivity.LOAD_URL, "http://10.0.0.43/cheet/address.html");
                intent4.putExtra(WebViewActivity.TITLE, "体验门店");
                startActivity(intent4);
                break;
        }
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
        if (homePager!=null){
            List<HomePager.AdvertisementListBean> advertisementList = homePager.getAdvertisementList();
            List<HomePager.RotationListBean> rotationList = homePager.getRotationList();
            String advUrl = advertisementList.get(0).getFileUrl();
            Glide.with(this).load(TEST_IMG_URL+advUrl).placeholder(R.drawable.placeholder_img).into(mIvAd);
            mAdvs = new String[rotationList.size()];
            for (int i = 0; i < rotationList.size(); i++) {
                mAdvs[i] = rotationList.get(i).getFileUrl();
            }
            mPosterAdapter = new PosterAdapter(this);
            mTopAdView.setAdapter(mPosterAdapter);
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("拍拍学车");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("www.xiaoduantui.com");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
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
                    .load(TEST_IMG_URL+mAdvs[position])
                    .placeholder(R.drawable.placeholder_img)
                    //                    .placeholder(R.mipmap.placeholder_img)
                    //                    .error(R.mipmap.load_error_img )
                    .into(imageView);
            //            imageView.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    LocalUser localUser = App.getApplication().getmLocalUser();
            //                    if (localUser==null){
            //                        ToastUtils.showToast("请先登录");
            //                        HomeActivity activity = (HomeActivity)mActivity;
            //                        activity.setCurrentTab(1);
            //                        return;
            //                    }
            //                    Intent intent = new Intent(mActivity, WebViewActivity.class);
            //                    intent.putExtra(WebViewActivity.EXTRA_TITLE,mAdvs.get(position).getTitle());
            //                    intent.putExtra(WebViewActivity.EXTRA_URL,mAdvs.get(position).getUrl()+
            //                            "&email2="+localUser.getUserName()+"&pwd2="+localUser.getUserPassword()+"&from2=app");
            //                    startActivity(intent);
            //                }
            //            });
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
