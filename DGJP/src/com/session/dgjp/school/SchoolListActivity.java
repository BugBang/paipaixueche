package com.session.dgjp.school;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.CityAndSchoole;
import com.session.dgjp.helper.BranchSchoolHelper;
import com.session.dgjp.request.CityAndSchoolRequestData;
import com.session.dgjp.trainer.TrainerListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-01-12.
 */
public class SchoolListActivity extends BaseActivity implements BranchSchoolHelper.BranchSchoolListener {
    private ImageView mIvBack;
    private ImageView mIvToCoach;
    private Spinner mTvCity;
    private ListView mLvCity;
    private ListView mLvSchool;
    private SchoolCityAdapter mCityAdapter;
    private SchoolListAdapter mSchoolAdapter;
    private TextView mTvHead;

    private final String mDongGuan = "东莞市";
    private final String mFoShan = "佛山市";
    private String mCurrentSelectCity;
    private String mCurrentTown;

    private List<CityAndSchoole.CountlistBean.ListBean> mTownList;
    private List<CityAndSchoole.BranchlistBean.ListBean> mSchoolList;
    private CityAndSchoole.CountlistBean mCountlist;
    private CityAndSchoole.BranchlistBean mBranchlist;
    private BranchSchoolHelper mBranchSchoolHelper;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_school_list);
        mCurrentSelectCity = mDongGuan;
        if (mBranchSchoolHelper == null) {
            mBranchSchoolHelper = new BranchSchoolHelper(this);
        }
        mBranchSchoolHelper.getBranchSchoolsAsync(null, null);
        getLocation();
        initView();
        getData();
    }

    private float mLatitude;
    private float mLongitude;
    private String mCity;

    private void getLocation() {
        mLatitude = SharedPreferencesUtil.getFloat(SharedPreferencesUtil.Latitude, -1f);
        mLongitude = SharedPreferencesUtil.getFloat(SharedPreferencesUtil.Longitude, -1f);
        mCity = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, " ");
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvToCoach = (ImageView) findViewById(R.id.iv_to_coach);
        mTvCity = (Spinner) findViewById(R.id.tv_city);
        mLvCity = (ListView) findViewById(R.id.lv_city);
        mLvSchool = (ListView) findViewById(R.id.lv_school);

        mTvCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    mCurrentSelectCity = mDongGuan;
                    mCurrentTown = "";
                    upDataListColor(-1);
                } else if (position == 1) {
                    mCurrentSelectCity = mFoShan;
                    mCurrentTown = "";
                    upDataListColor(-1);
                }
                tv.setTextColor(Color.RED);
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        View headView = getLayoutInflater().inflate(R.layout.item_around_school, null);
        mTvHead = (TextView) headView.findViewById(R.id.tv_around_school);
        mLvCity.addHeaderView(headView);

        mCityAdapter = new SchoolCityAdapter(mTownList, this);
        mSchoolAdapter = new SchoolListAdapter(mSchoolList, this);

        mLvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                upDataListColor(position - 1);
                if (position == 0) {//点击头布局
                    mCurrentTown = null;
                    getData();
                    return;
                }
                int _position = position - 1;
                if (mCountlist != null) {
                    mCurrentTown = mCountlist.getList().get(_position).getCounty();
                    getData();
                }

            }
        });
        mLvSchool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = mSchoolList.get(position).getBranchSchoolName();
                if (!schoolFlag) {
                    toastLong("发生错误,请稍候再试");
                    return;
                }
                if (schools == null || mSchoolList == null) {
                    toastLong("发生错误,请稍候再试");
                    return;
                }
                int a = 0;
                boolean b = false;
                for (int i = 0; i < schools.size(); i++) {
                    if (name.equals(schools.get(i).getName())) {
                        a = i;
                        b = true;
                        break;
                    }
                }
                if (!b){
                    toastLong("该驾校暂时无法预约");
                    return;
                }
                BranchSchool branchSchool = schools.get(a);
                if (branchSchool.getTrainerTotal() > 0) {
                    Intent intent = new Intent(SchoolListActivity.this, TrainerListActivity.class);
                    intent.putExtra(BranchSchool.class.getName(), branchSchool);
                    startActivity(intent);
                } else {
                    toast(R.string.school_no_trainer, Toast.LENGTH_SHORT);
                }
            }
        });
        mLvCity.setAdapter(mCityAdapter);
        mLvSchool.setAdapter(mSchoolAdapter);
    }


    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "加载中...", false);
        CityAndSchoolRequestData requestData = new CityAndSchoolRequestData();
        requestData.setCity(mCurrentSelectCity);
        requestData.setLat(mLatitude);
        requestData.setLng(mLongitude);
        if (!TextUtil.isEmpty(mCurrentTown)) {
            requestData.setCounty(mCurrentTown);
        }
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                Logger.i(response);
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
        }.request(Constants.URL_GET_CITY_AND_SCHOOL, data, progressDialog, true);
    }


    private void parseData(String response) {
        Gson gson = new Gson();
        CityAndSchoole cityAndSchoole = gson.fromJson(response, CityAndSchoole.class);
        if (TextUtil.isEmpty(mCurrentTown)) {
            mCountlist = cityAndSchoole.getCountlist();
            mTownList = mCountlist.getList();
            mCityAdapter.updateListViewData(mTownList);
        }
        mBranchlist = cityAndSchoole.getBranchlist();
        mSchoolList = mBranchlist.getList();
        mSchoolAdapter.updateListViewData(mSchoolList);
    }

    private void upDataListColor(int position) {
        if (position == -1) {
            mTvHead.setBackgroundResource(R.color.white);
        } else {
            mTvHead.setBackgroundResource(R.color.gray_bg);
        }
        mCityAdapter.setItemColor(position);
        mCityAdapter.notifyDataSetChanged();
    }

    private boolean schoolFlag;
    private List<BranchSchool> schools;

    @Override
    public void onGetBranchSchoolsSuccess(List<BranchSchool> list) {
        schoolFlag = true;
        if (schools == null) {
            schools = new ArrayList<BranchSchool>();
        }
        schools.clear();
        if (list != null) {
            schools.addAll(list);
        }
    }

    @Override
    public void onGetBranchSchoolsFail(int code, String msg) {
        schoolFlag = false;
    }
}
