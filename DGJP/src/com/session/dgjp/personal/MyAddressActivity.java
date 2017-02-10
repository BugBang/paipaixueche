package com.session.dgjp.personal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.Address;
import com.session.dgjp.request.AddStudentAddressRequestData;
import com.session.dgjp.request.DeleteStudentAddressRequestData;
import com.session.dgjp.request.EditorDefaultAddressRequestData;
import com.session.dgjp.request.EditorDefaultPlaceAddressRequestData;
import com.session.dgjp.request.GetStudentAddressRequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2017-02-07.
 */
public class MyAddressActivity extends BaseActivity implements AddressListAdapter.AddressItemListener {
    private ImageView mIvBack;
    private ListView mLvAddress;
    private ImageView mIvAddAddress;
    private ScrollView mScrollView;
    private AddressListAdapter mAddressListAdapter;
    private List<Address.ListBean> mListAddress;
    private Gson mGson;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_my_address);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLvAddress = (ListView) findViewById(R.id.lv_address);
        mIvAddAddress = (ImageView) findViewById(R.id.iv_add_address);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mScrollView.smoothScrollTo(0, 0);
        mAddressListAdapter = new AddressListAdapter(mListAddress, this, this);
        mLvAddress.setAdapter(mAddressListAdapter);
        getStudentAddress();
        initListener();
    }

    private void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddAddress(-1, "", "", "");
            }
        });
    }

    private void toAddAddress(int id, String type, String phone, String address) {
        Intent intent = new Intent(MyAddressActivity.this, AddNewAddressActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("phone", phone);
        intent.putExtra("address", address);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddNewAddressActivity.ADD_SUCCESS) {
            getStudentAddress();
        }
        if (resultCode == EventsActivity.LOCATION_SUCCESS) {
            String address = data.getStringExtra("address");
            int id = data.getIntExtra("id", -1);
            int position = data.getIntExtra("position",-1);
            upDataAddress(position,id, address);
            //            getStudentAddress();
        }
    }

    private void upDataAddress(final int position,final int id, final String address) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        AddStudentAddressRequestData requestData = new AddStudentAddressRequestData();
        requestData.setStuAccount(account.getAccount());
        requestData.setCarAddress(address);
        requestData.setPhone(mListAddress.get(position).getPhone());
        requestData.setPlace(mListAddress.get(position).getPlace());
        requestData.setId(id);
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
                            upDataAddress(position,id,address);
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
                    getStudentAddress();
                } else {
                    toastLong("更新地址失败,请稍候再试");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getStudentAddress() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        GetStudentAddressRequestData requestData = new GetStudentAddressRequestData();
        requestData.setStuAccount(account.getAccount());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseAddress(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getStudentAddress();
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
        }.request(Constants.URL_GET_STUDENT_ADDRESS, data, progressDialog, true);
    }

    private void parseAddress(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        Address address = mGson.fromJson(response, Address.class);
        mListAddress = address.getList();
        mAddressListAdapter.updateListViewData(mListAddress);
    }

    @Override
    public void onNowClick(Address.ListBean listBean) {
        if (listBean.getPlaceState() == 1) {
            return;
        }
        editorDefaultAddress(listBean.getId());
    }

    @Override
    public void onDefaultClick(Address.ListBean listBean) {
        if (listBean.getDefAddress() == 1) {
            return;
        }
        editorDefaultPlaceAddress(listBean.getId());
    }

    @Override
    public void onToLocaClick(Address.ListBean listBean,int position) {
        if (listBean != null) {
            Intent intent = new Intent(MyAddressActivity.this, EventsActivity.class);
            intent.putExtra("id", listBean.getId());
            intent.putExtra("position", position);
            startActivityForResult(intent, 1);
        }
    }


    @Override
    public void onEditorClick(Address.ListBean listBean) {
        toAddAddress(listBean.getId(), listBean.getPlace(), listBean.getPhone(), listBean.getCarAddress());
    }

    @Override
    public void onDeleteClick(Address.ListBean listBean) {
        deleteAddress(listBean.getId());
    }


    private void editorDefaultAddress(final int id) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        EditorDefaultAddressRequestData requestData = new EditorDefaultAddressRequestData();
        requestData.setStuAccount(account.getAccount());
        requestData.setId(id);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseEditDefaultResponse(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            editorDefaultAddress(id);
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
        }.request(Constants.URL_EDITOR_DEFAULT_STUDENT_ADDRESS, data, progressDialog, true);
    }

    private void editorDefaultPlaceAddress(final int id) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        EditorDefaultPlaceAddressRequestData requestData = new EditorDefaultPlaceAddressRequestData();
        requestData.setStuAccount(account.getAccount());
        requestData.setId(id);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseEditDefaultResponse(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            editorDefaultPlaceAddress(id);
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
        }.request(Constants.URL_EDITOR_DEFAULT_PLACE_STUDENT_ADDRESS, data, progressDialog, true);
    }

    private void deleteAddress(final int id) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        DeleteStudentAddressRequestData requestData = new DeleteStudentAddressRequestData();
        requestData.setStuAccount(account.getAccount());
        requestData.setId(id);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseDeleteResponse(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            deleteAddress(id);
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
        }.request(Constants.URL_DELETE_STUDENT_ADDRESS, data, progressDialog, true);
    }

    private void parseEditDefaultResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json != null && json.has("state")) {
                int state = json.optInt("state");
                if (state == 1) {
                    toastLong("设置成功");
                    getStudentAddress();
                } else {
                    toastLong("设置失败,请稍候再试");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseDeleteResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json != null && json.has("state")) {
                int state = json.optInt("state");
                if (state == 1) {
                    toastLong("删除成功");
                    getStudentAddress();
                } else {
                    toastLong("删除失败,请稍候再试");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
