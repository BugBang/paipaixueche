package com.session.dgjp.sign;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.SignSchoolList;
import com.session.dgjp.request.SignSchoolListRequsetData;
import com.session.dgjp.view.NoScrollListview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SignSchoolFragment extends BaseFragment implements AMapLocationListener {

    private Drawable tvSelect;
    private Drawable tvNormal;
    private EditText mEtSearch;
    private TextView[] mTvItem;
    private TextView mTvAll, mTvDistance, mTvTuition, mTvFilters, mTvCredit, mTvPassing, mTvSearch;
    private ImageView mIvBack;
    private LinearLayout mLlfilters;
    private NoScrollListview mListView;
    private int currentPosition;
    private boolean isShowFilter;
    private String currentInterface = "allType";
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient = null;
    private Gson mGson;
    private SignSchoolListAdapter mSignSchoolListAdapter;
    private PullToRefreshScrollView mPullToRefreshScrollView;

    private static final String All = "allType";
    private static final String DISTANCE = "distanceType"; //距离
    private static final String COST = "costType"; //学费
    private static final String CREDIT = "credit"; //信用
    private static final String ENROLL_NUM = "enrollNum"; //报名人数
    //    private static final String All = "allType";

    public static SignSchoolFragment newInstance() {
        return new SignSchoolFragment();
    }

    @Override
    protected int getContentRes() {
        return R.layout.sign_school_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSignSchoolListAdapter = new SignSchoolListAdapter(model, act);
        mlocationClient = new AMapLocationClient(act);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

        tvSelect = getResources().getDrawable(R.drawable.triangle_select);
        tvSelect.setBounds(0, 0, tvSelect.getMinimumWidth(), tvSelect.getMinimumHeight());

        tvNormal = getResources().getDrawable(R.drawable.triangle_normal);
        tvNormal.setBounds(0, 0, tvNormal.getMinimumWidth(), tvNormal.getMinimumHeight());

        mPullToRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (refreshView.isShownHeader()) {
                    mPullToRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mPullToRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新: " + str);
                    getData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    mPullToRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    mPullToRefreshScrollView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载:" + str);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mPullToRefreshScrollView.onRefreshComplete();
                            toastLong("onRefreshComplete");
                        }
                    }, 2000);
                }
            }
        });

        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mTvSearch = (TextView) view.findViewById(R.id.tv_search);
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvAll = (TextView) view.findViewById(R.id.tv_all);
        mTvDistance = (TextView) view.findViewById(R.id.tv_distance);
        mTvTuition = (TextView) view.findViewById(R.id.tv_tuition);
        mTvFilters = (TextView) view.findViewById(R.id.tv_filters);
        mTvCredit = (TextView) view.findViewById(R.id.tv_credit);
        mTvPassing = (TextView) view.findViewById(R.id.tv_passing);
        mLlfilters = (LinearLayout) view.findViewById(R.id.ll_filters);
        mListView = (NoScrollListview) view.findViewById(R.id.lv_school_list);
        mTvItem = new TextView[]{
                mTvAll,
                mTvDistance,
                mTvTuition,
                mTvFilters,
                mTvCredit,
                mTvPassing
        };

        mTvAll.setOnClickListener(this);
        mTvDistance.setOnClickListener(this);
        mTvTuition.setOnClickListener(this);
        mTvFilters.setOnClickListener(this);
        mTvCredit.setOnClickListener(this);
        mTvPassing.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvSearch.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<SignSchoolList.SignSchoolListModel> data = mSignSchoolListAdapter.getData();
                Bundle _b = new Bundle();
                _b.putInt(SignSchoolDetailsFragment.SCHOOL_ID, data.get(position).getId());
                _b.putString(SignSchoolDetailsFragment.SCHOOL_TITLE, data.get(position).getName());
                logI("id=" + data.get(position).getId());
                addFragment(R.id.content, SignSchoolDetailsFragment.newInstance(), _b);
            }
        });
        mListView.setAdapter(mSignSchoolListAdapter);
        intiEditView();

    }


    private void intiEditView() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSignSchoolListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        SignSchoolListRequsetData requestData = new SignSchoolListRequsetData();
        requestData.setLatitude(mLatitude);
        requestData.setLongitude(mLongitude);
        if (currentInterface.equals(All)) {
            requestData.setAllType(All);
        } else if (currentInterface.equals(DISTANCE)) {
            requestData.setDistanceType(DISTANCE);
        } else if (currentInterface.equals(COST)) {
            requestData.setCostType(COST);
        } else if (currentInterface.equals(CREDIT)) {
            requestData.setCredit(CREDIT);
        }else if (currentInterface.equals(ENROLL_NUM)) {
            requestData.setEnrollNum(ENROLL_NUM);
        }
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                logI("response="+response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseData(response);
                            mPullToRefreshScrollView.onRefreshComplete();
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getData();
                            mPullToRefreshScrollView.onRefreshComplete();
                            break;
                        default:
                            mPullToRefreshScrollView.onRefreshComplete();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mPullToRefreshScrollView.onRefreshComplete();
                    toastShort("网络异常，请稍后重试");
                } finally {

                }
            }
        }.request(Constants.URL_GET_SCHOOL_LIST, data, progressDialog, true);
    }

    private SignSchoolList signSchoolList;
    private List<SignSchoolList.SignSchoolListModel> model;

    private void parseData(String data) {
        if (mGson == null) {
            mGson = new Gson();
        }
        signSchoolList = mGson.fromJson(data, SignSchoolList.class);
        model = signSchoolList.getList();
        if (model != null) {
            mSignSchoolListAdapter.updateListViewData(model);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
                mTvFilters.setText("筛选");
                currentPosition = 0;
                currentInterface = "allType";
                getData();
                setItemState(currentPosition);
                break;
            case R.id.tv_distance:
                mTvFilters.setText("筛选");
                currentPosition = 1;
                currentInterface = "distanceType";
                getData();
                setItemState(currentPosition);
                break;
            case R.id.tv_tuition:
                mTvFilters.setText("筛选");
                currentPosition = 2;
                currentInterface = "costType";
                getData();
                setItemState(currentPosition);
                break;
            case R.id.tv_filters:
                currentPosition = 3;
                setLlfilterState();
                setItemState(currentPosition);
                break;
            case R.id.tv_credit:
                setLlfilterState();
                mTvFilters.setText("信用");
                currentPosition = 3;
                currentInterface = "credit";
                getData();
                setItemState(currentPosition);
                break;
            case R.id.tv_passing:
                setLlfilterState();
                mTvFilters.setText("报名人数");
                currentInterface= "enrollNum";
                currentPosition = 3;
                getData();
                setItemState(currentPosition);
                break;
            case R.id.iv_back:
                removeFragment();
                break;
            case R.id.tv_search:
                toastLong("tv_search");
                break;
        }
    }

    private void setLlfilterState() {
        if (!isShowFilter) {
            mLlfilters.setVisibility(View.VISIBLE);
            isShowFilter = !isShowFilter;
        } else {
            mLlfilters.setVisibility(View.GONE);
            isShowFilter = !isShowFilter;
        }
    }

    private void setItemState(int position) {
        for (int i = 0; i < mTvItem.length; i++) {
            mTvItem[i].setTextColor(Color.parseColor("#666666"));
            mTvItem[i].setCompoundDrawables(null, null, tvNormal, null);
        }
        mTvItem[position].setTextColor(Color.parseColor("#ff4848"));
        mTvItem[position].setCompoundDrawables(null, null, tvSelect, null);
    }

    private double mLatitude;
    private double mLongitude;

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                mLatitude = amapLocation.getLatitude();//获取纬度
                mLongitude = amapLocation.getLongitude();//获取经度
                getData();
            } else {
                mLatitude = 0;//获取纬度
                mLongitude = 0;//获取经度
                getData();
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}
