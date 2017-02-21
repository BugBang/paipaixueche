package com.session.dgjx.personal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BXBaseAdapter;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.ViewHolder;
import com.session.dgjx.Constants;
import com.session.dgjx.R;
import com.session.dgjx.enity.ComeIn;
import com.session.dgjx.request.CoachComeInRequestData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2017-02-17.
 */
public class ComeInActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.lv_come_in)
    ListView mLvComeIn;
    private Gson mGson;
    private List<ComeIn.ListBean> mComeInList;
    private ComeInAdapter mAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_come_in);
        ButterKnife.bind(this);
        mAdapter = new ComeInAdapter(mComeInList, ComeInActivity.this);
        mLvComeIn.setAdapter(mAdapter);
        getComeIn();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }


    private void getComeIn() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等...", false);
        CoachComeInRequestData requestData = new CoachComeInRequestData();
        requestData.setAccount(account.getAccount());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseComeInData(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getComeIn();
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
        }.request(Constants.URL_GET_COACH_INCOME_DATA, data, progressDialog, true);
    }



    private void parseComeInData(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        ComeIn comeIn = mGson.fromJson(response, ComeIn.class);
        mComeInList = comeIn.getList();
        mAdapter.updateListViewData(mComeInList);
    }

    class ComeInAdapter extends BXBaseAdapter<ComeIn.ListBean> {
        public ComeInAdapter(List<ComeIn.ListBean> listModel, Activity activity) {
            super(listModel, activity);
        }

        @Override
        public View getItemView(int position, View convertView, ViewGroup parent, ComeIn.ListBean model) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_come_in, null);
            }
            TextView mTvName = ViewHolder.get(convertView, R.id.tv_name);
            TextView mTvDate = ViewHolder.get(convertView, R.id.tv_date);
            TextView mTvSubj = ViewHolder.get(convertView, R.id.tv_subj);
            TextView mTvMoney = ViewHolder.get(convertView, R.id.tv_money);

            if (model != null){
                mTvName.setText(model.getStname());
                mTvDate.setText(model.getCreatetime());
                mTvSubj.setText(model.getCourse());
                mTvMoney.setText(model.getPrice());
            }

            return convertView;
        }

    }
}
