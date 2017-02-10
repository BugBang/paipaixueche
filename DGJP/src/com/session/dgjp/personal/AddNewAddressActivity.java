package com.session.dgjp.personal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.TextUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.request.AddStudentAddressRequestData;

import org.json.JSONException;
import org.json.JSONObject;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

/**
 * Created by user on 2017-02-08.
 */
public class AddNewAddressActivity extends BaseActivity {
    private ImageView mIvBack;
    private TextView mTvType;
    private EditText mEtPhone;
    private TextView mTvLocation;
    private EditText mEtDetailAddress;
    private Button mBtSaveAddress;
    private String[] msType;
    private int id;
    private String mType;
    private String mPhone;
    private String mLocation;
    private String mDetailAddress;

    public static final int ADD_SUCCESS = 1 ;


    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_add_new_address);
        msType = new String[]{"家里", "公司", "学校", "其他"};
        initView();
        initListener();
        initIntent();
    }

    private void initIntent() {
        id = getIntent().getIntExtra("id",-1);
        mType = getIntent().getStringExtra("type");
        mPhone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        if (address.contains("-")){
            mLocation = address.split("-")[0];
            mDetailAddress = address.split("-")[1];
        }else {
            mLocation = "";
            mDetailAddress = address;
        }
        if (id != -1){
            mTvType.setCompoundDrawables(null, null, null, null);
            mTvType.setText(mType);
            mEtPhone.setText(mPhone);
            mTvLocation.setText(mLocation);
            mEtDetailAddress.setText(mDetailAddress);
        }
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvType = (TextView) findViewById(R.id.tv_type);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mEtDetailAddress = (EditText) findViewById(R.id.et_detail_address);
        mBtSaveAddress = (Button) findViewById(R.id.bt_save_address);
    }

    private void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(msType, mTvType, "请选择地址类型");
            }
        });
        mTvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomDialog d = new BottomDialog(AddNewAddressActivity.this);
                d.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                    @Override
                    public void onAddressSelected(Province province, City city, County county, Street street) {
                        String p = province != null ? province.name : "";
                        String ci = city != null ? city.name : "";
                        String co = county != null ? county.name : "";
                        String s = street != null ? street.name : "";
                        mLocation = p + ci + co + s;
                        mTvLocation.setText(mLocation);
                    }
                });
                d.show();
            }
        });
        mBtSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkData()){
                    addStudentAddress();
                }
            }
        });
    }

    private boolean checkData() {
        mType = mTvType.getText().toString().trim();
        mPhone = mEtPhone.getText().toString().trim();
        mDetailAddress = mEtDetailAddress.getText().toString().trim();
        if (TextUtil.isEmpty(mType)||TextUtil.isEmpty(mPhone)||
                TextUtil.isEmpty(mLocation)||TextUtil.isEmpty(mDetailAddress)){
            toastLong("请填写完整的地址信息");
            return false;
        }
        return true;
    }

    private AlertDialog alertDialog;
    private ListView mListView;
    private TextView mTvItemTitle;
    private LayoutInflater inflaterDl;

    private void clickItem(final String[] item, final TextView tvitem, String title) {
        inflaterDl = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.load_money_item_list, null);
        alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Transparent)).create();
        mTvItemTitle = (TextView) layout.findViewById(R.id.tv_item_title);
        mListView = (ListView) layout.findViewById(R.id.list);
        mTvItemTitle.setText(title);
        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.load_money_item_list_item, item));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setView(layout);
        alertDialog.show();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvitem.setCompoundDrawables(null, null, null, null);
                tvitem.setText(item[position]);
                alertDialog.dismiss();
            }
        });
    }

    private void addStudentAddress() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        AddStudentAddressRequestData requestData = new AddStudentAddressRequestData();
        requestData.setStuAccount(account.getAccount());
        requestData.setCarAddress(mLocation+"-"+mDetailAddress);
        requestData.setPhone(mPhone);
        requestData.setPlace(mType);
        if (id != -1){
            requestData.setId(id);
        }
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseAddResponse(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            addStudentAddress();
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
        }.request(Constants.URL_ADD_STUDENT_ADDRESS, data, progressDialog, true);
    }

    private void parseAddResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json != null && json.has("state")) {
                int state = json.optInt("state");
                if (state == 1) {
                    toastLong("保存成功");
                    setResult(ADD_SUCCESS);
                    finish();
                } else {
                    toastLong("保存失败,请稍候再试");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
