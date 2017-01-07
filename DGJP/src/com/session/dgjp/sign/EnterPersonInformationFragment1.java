package com.session.dgjp.sign;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.common.utils.Base64Coder;
import com.session.common.utils.TextUtil;
import com.session.common.utils.Uri2BitMapUtils;
import com.session.common.utils.ZoomBitmap;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.SignStudent;
import com.session.dgjp.request.GetStudentInfoRequestData;
import com.session.dgjp.request.UpDataSignInfoRequestData;
import com.session.dgjp.request.UpImgRequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

/**
 * Created by user on 2016-11-17.
 */
public class EnterPersonInformationFragment1 extends BaseFragment {


    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
    private ImageView mIvBack, mIvCardFront, mIvCardBack;
    private TextView mTvTitle, mTvIdCard, mTvPhone;
    private EditText mEtName, mEtAccount, mEtQqNum, mEtEmail, mEtAddress;
    private Button mBtNext;
    private String mName, mAccount, mIdCard, mQqNum, mEmail, mPhone, mAddress;

    public static final String CARD_FRONT = "card_front";
    public static final String CARD_BACK = "card_back";
    public static final int ICARD_FRONT = 0;
    public static final int ICARD_BACK = 1;
    private int currentClick;
    private String currentPhoto;

    public static final String Name = "name";
    public static final String ACCOUNT = "account";
    public static final String IDCARD = "idcard";
    public static final String QQ = "qq";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";

    public static EnterPersonInformationFragment1 newInstance() {
        return new EnterPersonInformationFragment1();
    }

    @Override
    protected int getContentRes() {
        return R.layout.enter_person_information_fragment1;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtAccount = (EditText) view.findViewById(R.id.et_account);
        mTvIdCard = (TextView) view.findViewById(R.id.et_id_card);
        mEtQqNum = (EditText) view.findViewById(R.id.et_qq_num);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);
        mTvPhone = (TextView) view.findViewById(R.id.et_phone);
        mEtAddress = (EditText) view.findViewById(R.id.et_address);
        mIvCardFront = (ImageView) view.findViewById(R.id.iv_card_front);
        mIvCardBack = (ImageView) view.findViewById(R.id.iv_card_back);
        mBtNext = (Button) view.findViewById(R.id.bt_next);

        mIvBack.setOnClickListener(this);
        mIvCardFront.setOnClickListener(this);
        mIvCardBack.setOnClickListener(this);
        mBtNext.setOnClickListener(this);

        getData();
    }

    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等...", false);
        GetStudentInfoRequestData requestData = new GetStudentInfoRequestData();
        requestData.setAccount(account.getAccount());
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
        }.request(Constants.URL_GET_STUDENT_INFO, data, progressDialog, true);
    }

    private void parseData(String response) {
        Gson mGson = new Gson();
        if (response != null) {
            SignStudent signStudent = mGson.fromJson(response, SignStudent.class);
            mIdCard = signStudent.getList().get(0).getIdcard();
            mPhone = signStudent.getList().get(0).getPhone();

            mEtName.setText(signStudent.getList().get(0).getName());
            mEtAccount.setText(signStudent.getList().get(0).getPopulation());
            mEtQqNum.setText(signStudent.getList().get(0).getQq());
            mEtEmail.setText(signStudent.getList().get(0).getEmail());
            mEtAddress.setText(signStudent.getList().get(0).getAddress());
            mTvIdCard.setText(mIdCard);
            mTvPhone.setText(mPhone);

            String frontImg = signStudent.getList().get(0).getFrontImg();
            String oppositeImg = signStudent.getList().get(0).getOppositeImg();

            Glide.with(act).load("http://120.76.168.132:8080/files/" + frontImg)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_img)
                    .into(mIvCardFront);
            Glide.with(act).load("http://120.76.168.132:8080/files/" + oppositeImg)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_img)
                    .into(mIvCardBack);
        } else {
            mTvIdCard.setText("未知");
            mTvPhone.setText("未知");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                removeFragment();
                break;
            case R.id.iv_card_front:
                currentPhoto = CARD_FRONT;
                currentClick = ICARD_FRONT;
                showChoosePicDialog();
                break;
            case R.id.iv_card_back:
                currentPhoto = CARD_BACK;
                currentClick = ICARD_BACK;
                showChoosePicDialog();
                break;
            case R.id.bt_next:
                if (checkData()) {
                    postSignData();
                }
                break;

        }
    }

    private void postSignData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "提交中...", false);
        UpDataSignInfoRequestData requestData = new UpDataSignInfoRequestData();

        requestData.setName(mName);
        requestData.setPopulation(mAccount);
        requestData.setIdcard(mIdCard);
        requestData.setQq(mQqNum);
        requestData.setEmail(mEmail);
        requestData.setPhone(mPhone);
        requestData.setAddress(mAddress);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                switch (code) {
                    case BaseRequestTask.CODE_SUCCESS:
                        addFragment(R.id.content, EnterPersonInformationFragment2.newInstance(), null);
                        break;
                    case BaseRequestTask.CODE_SESSION_ABATE:
                        postSignData();
                        break;
                    default:
                        break;
                }
            }
        }.request(Constants.URL_UPDATA_STUDENT_INFO, data, progressDialog, true);
    }

    private boolean checkData() {
        mName = mEtName.getText().toString().trim();
        mAccount = mEtAccount.getText().toString().trim();
        mQqNum = mEtQqNum.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();
        mAddress = mEtAddress.getText().toString().trim();
        if (TextUtil.isEmpty(mName) || TextUtil.isEmpty(mAccount) ||
                TextUtil.isEmpty(mQqNum) || TextUtil.isEmpty(mEmail) ||
                TextUtil.isEmpty(mAddress)) {
            toastLong("请填写完整的个人信息");
            return false;
        }

        boolean matcher = Pattern.matches(REGEX_EMAIL, mEmail);
        if (!matcher) {
            toastLong("请输入正确的邮箱格式");
            return false;
        }
        return true;
    }

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(act, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("选择照片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        //先验证手机是否有sdcard
                        String status = Environment.getExternalStorageState();
                        if (status.equals(Environment.MEDIA_MOUNTED)) {
                            try {
                                File dir = new File(Environment.getExternalStorageDirectory() + "/" + "PAIPAI_IMG");
                                if (!dir.exists())
                                    dir.mkdirs();
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(dir, currentPhoto);
                                Uri u = Uri.fromFile(f);
                                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                                startActivityForResult(intent, TAKE_PICTURE);
                            } catch (ActivityNotFoundException e) {
                                toastLong("没有找到储存目录");
                            }
                        } else {
                            toastLong("没有储存卡");
                        }
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    private Uri pictureUri;
    //    private String photoPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == act.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    File f = new File(Environment.getExternalStorageDirectory() + "/" + "PAIPAI_IMG" + "/" + currentPhoto);
                    try {
                        pictureUri = Uri.parse(MediaStore.Images.Media.insertImage(act.getContentResolver(), f.getAbsolutePath(), null, null));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                            + "/" + "PAIPAI_IMG" + "/" + currentPhoto);
                    float wight = bitmap.getWidth();
                    float height = bitmap.getHeight();
                    mUpBitMap = ZoomBitmap.zoomImage(bitmap, wight / 8, height / 8);

                    postWorkPhoto();
                    break;

                case CHOOSE_PICTURE:
                    if (data == null) {
                        return;
                    }
                    pictureUri = data.getData();
                    mUpBitMap = BitmapFactory.decodeFile(Uri2BitMapUtils.getPath(act, pictureUri));
                    mUpBitMap = ZoomBitmap.zoomImage(mUpBitMap, mUpBitMap.getWidth() / 8, mUpBitMap.getHeight() / 8);
                    //                    switch (currentClick) {
                    //                        case ICARD_FRONT:
                    //                            Glide.with(act).load(pictureUri).centerCrop().into(mIvCardFront);
                    //                            break;
                    //                        case ICARD_BACK:
                    //                            Glide.with(act).load(pictureUri).asBitmap().centerCrop().into(mIvCardBack);
                    //                            break;
                    //                    }
                    postWorkPhoto();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap mUpBitMap;

    private void postWorkPhoto() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mUpBitMap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] b = stream.toByteArray();
        String file = Base64Coder.encodeLines(b);
        ProgressDialog progressDialog = buildProcessDialog(null, "上传中...", false);
        UpImgRequestData requestData = new UpImgRequestData();
        if (currentClick == ICARD_FRONT) {
            requestData.setFileName("F" + account.getAccount());
        } else if (currentClick == ICARD_BACK) {
            requestData.setFileName("B" + account.getAccount());
        }
        requestData.setPhone(account.getPhone());
        requestData.setFile(file);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                switch (code) {
                    case BaseRequestTask.CODE_SUCCESS:
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            if (json.has("flag")) {
                                String flag = json.optString("flag");
                                if (flag.equals("1")) {
                                    toastLong("上传成功");
                                    switch (currentClick) {
                                        case ICARD_FRONT:
                                            Glide.with(act).load(pictureUri).centerCrop().skipMemoryCache(true).into(mIvCardFront);
                                            break;
                                        case ICARD_BACK:
                                            Glide.with(act).load(pictureUri).centerCrop().skipMemoryCache(true).into(mIvCardBack);
                                            break;
                                    }
                                } else {
                                    toastLong("上传失败,请重试");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case BaseRequestTask.CODE_SESSION_ABATE:
                        postWorkPhoto();
                        break;
                    default:
                        break;
                }
            }
        }.request(Constants.URL_UP_LOAD_IDCARD, data, progressDialog, true);
    }

}
