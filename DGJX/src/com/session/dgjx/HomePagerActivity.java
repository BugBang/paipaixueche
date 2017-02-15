package com.session.dgjx;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.DateUtil;
import com.session.common.utils.DialogUtil;
import com.session.common.utils.IntentUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjx.enity.HomePager;
import com.session.dgjx.home.HomeOrderListAdapter;
import com.session.dgjx.request.HomeDataRequestData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017-02-07.
 */
public class HomePagerActivity extends BaseActivity implements HomeOrderListAdapter.onOrderListItemListener {
    @BindView(R.id.iv_msg)
    ImageView mIvMsg;
    @BindView(R.id.tv_top)
    TextView mTvTop;
    @BindView(R.id.tv_month_comein)
    TextView mTvMonthComein;
    @BindView(R.id.tv_day_comein)
    TextView mTvDayComein;

    @BindView(R.id.tv_sunday)
    TextView mTvSunday;
    @BindView(R.id.tv_monday)
    TextView mTvMonday;
    @BindView(R.id.tv_tuesday)
    TextView mTvTuesday;
    @BindView(R.id.tv_wednesday)
    TextView mTvWednesday;
    @BindView(R.id.tv_thursday)
    TextView mTvThursday;
    @BindView(R.id.tv_friday)
    TextView mTvFriday;
    @BindView(R.id.tv_saturday)
    TextView mTvSaturday;
    @BindView(R.id.lv_order_list)
    ListView mLvOrderList;
    private String mTodayDate;
    private String[] mWeeks;
    private TextView[] mItems;
    private int mCurrentClick = -1;
    private Gson mGson;
    private List<HomePager.ListBean> mOrderList;
    private List<HomePager.OlistBean> mCoachData;
    private HomeOrderListAdapter mAdapter;
    private DialogUtil mDialogUtil;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_home_pager);
        mWeeks = new String[7];
        ButterKnife.bind(this);
        initListener();
        mDialogUtil = new DialogUtil(this);
        mAdapter = new HomeOrderListAdapter(mOrderList, this, this);
        mLvOrderList.setAdapter(mAdapter);//HomeOrderListAdapter
        mItems = new TextView[7];
        mItems[0] = mTvMonday;
        mItems[1] = mTvTuesday;
        mItems[2] = mTvWednesday;
        mItems[3] = mTvThursday;
        mItems[4] = mTvFriday;
        mItems[5] = mTvSaturday;
        mItems[6] = mTvSunday;
        initWeek();
        getDate();
        getData();
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        mTodayDate = DateUtil.NETWORK_DATE_SDF.format(date);
        List<Long> weekDayList = DateUtil.getWeekDayList(mTodayDate, "yyyy-MM-dd");
        for (int i = 0; i < weekDayList.size(); i++) {
            mWeeks[i] = DateUtil.formatDate(weekDayList.get(i), "yyyy-MM-dd");
        }
    }

    private void initWeek() {
        String weekOfDate = DateUtil.getWeekOfDate();
        if (weekOfDate.equals("周日")) {
            mCurrentClick = 6;
            mTvSunday.setText("今日");
            mTvSunday.setTextColor(getResources().getColor(R.color.app_color));
        } else if (weekOfDate.equals("周一")) {
            mCurrentClick = 0;
            mTvMonday.setText("今日");
            mTvMonday.setTextColor(getResources().getColor(R.color.app_color));
        } else if (weekOfDate.equals("周二")) {
            mCurrentClick = 1;
            mTvTuesday.setText("今日");
            mTvTuesday.setTextColor(getResources().getColor(R.color.app_color));
        } else if (weekOfDate.equals("周三")) {
            mCurrentClick = 2;
            mTvWednesday.setText("今日");
            mTvWednesday.setTextColor(getResources().getColor(R.color.app_color));
        } else if (weekOfDate.equals("周四")) {
            mCurrentClick = 3;
            mTvThursday.setText("今日");
            mTvThursday.setTextColor(getResources().getColor(R.color.app_color));
        } else if (weekOfDate.equals("周五")) {
            mCurrentClick = 4;
            mTvFriday.setText("今日");
            mTvFriday.setTextColor(getResources().getColor(R.color.app_color));
        } else if (weekOfDate.equals("周六")) {
            mCurrentClick = 5;
            mTvSaturday.setText("今日");
            mTvSaturday.setTextColor(getResources().getColor(R.color.app_color));
        }

    }

    private void initListener() {
        mIvMsg.setOnClickListener(this);
        mTvSunday.setOnClickListener(this);
        mTvMonday.setOnClickListener(this);
        mTvTuesday.setOnClickListener(this);
        mTvWednesday.setOnClickListener(this);
        mTvThursday.setOnClickListener(this);
        mTvFriday.setOnClickListener(this);
        mTvSaturday.setOnClickListener(this);
        //        mTvCallPhone.setOnClickListener(this);
    }

    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等...", false);
        HomeDataRequestData requestData = new HomeDataRequestData();
        requestData.setTrainerAccount(account.getAccount());
        //        requestData.setBeginTime("2017-02-10");
        requestData.setBeginTime(mTodayDate);

        $log("getdata = " + mTodayDate);
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
                            getData();
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
        }.request(Constants.URL_GET_STUDENT_ORDER_LIST_AND_COMEIN_DATA, data, progressDialog, true);
    }

    private void parseData(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        HomePager homePager = mGson.fromJson(response, HomePager.class);
        mOrderList = homePager.getList();
        mCoachData = homePager.getOlist();

        mTvMonthComein.setText(String.format("%d", mCoachData.get(0).getTotalMoney()));
        mTvDayComein.setText(String.format("%d", mCoachData.get(0).getFee()));

        mAdapter.updateListViewData(mOrderList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg:
                break;
            case R.id.tv_sunday:
                mCurrentClick = 6;
                mTodayDate = mWeeks[6];
                getData();
                setItemState();
                break;
            case R.id.tv_monday:
                mCurrentClick = 0;
                mTodayDate = mWeeks[0];
                getData();
                setItemState();
                break;
            case R.id.tv_tuesday:
                mCurrentClick = 1;
                mTodayDate = mWeeks[1];
                getData();
                setItemState();
                break;
            case R.id.tv_wednesday:
                mCurrentClick = 2;
                mTodayDate = mWeeks[2];
                getData();
                setItemState();
                break;
            case R.id.tv_thursday:
                mCurrentClick = 3;
                mTodayDate = mWeeks[3];
                getData();
                setItemState();
                break;
            case R.id.tv_friday:
                mCurrentClick = 4;
                mTodayDate = mWeeks[4];
                getData();
                setItemState();
                break;
            case R.id.tv_saturday:
                mCurrentClick = 5;
                mTodayDate = mWeeks[5];
                getData();
                setItemState();
                break;
            default:
                break;
        }
    }

    private void setItemState() {
        for (int i = 0; i < mItems.length; i++) {
            mItems[i].setTextColor(getResources().getColor(R.color.gray_888888));
        }
        mItems[mCurrentClick].setTextColor(getResources().getColor(R.color.app_color));
    }

    @Override
    public void onCall(HomePager.ListBean model) {
        callStudentPhone(model);
    }

    @Override
    public void onSign(HomePager.ListBean model) {
        toastLong(model.getStudentName());
    }

    private void callStudentPhone(HomePager.ListBean model) {
        if (model != null) {
            final String phone = model.getPhone();
            mDialogUtil.confirm("提示", "确定拨打电话?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (!TextUtil.isEmpty(phone)) {
                        startActivity(IntentUtil.getCallNumberIntent(phone));
                    } else {
                        toastLong("拨号失败");
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
}
