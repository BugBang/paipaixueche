package com.session.dgjp.sign;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.session.common.BaseFragment;
import com.session.common.utils.TextUtil;
import com.session.common.utils.Uri2BitMapUtils;
import com.session.dgjp.R;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by user on 2016-11-17.
 */
public class EnterPersonInformationFragment1 extends BaseFragment {


    private ImageView mIvBack, mIvCardFront, mIvCardBack;
    private TextView mTvTitle;
    private EditText mEtName, mEtAccount, mEtIdCard, mEtQqNum, mEtEmail, mEtPhone, mEtAddress;
    private Button mBtNext;
    private String mName, mAccount, mIdCard, mQqNum, mEmail, mPhone, mAddress;

    public static final String CARD_FRONT = "card_front";
    public static final String CARD_BACK = "card_back";
    public static final int ICARD_FRONT = 0;
    public static final int ICARD_BACK = 1;
    private int currentClick;
    private String currentPhoto;
    private Bundle mBundle;

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
        mBundle = new Bundle();
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtAccount = (EditText) view.findViewById(R.id.et_account);
        mEtIdCard = (EditText) view.findViewById(R.id.et_id_card);
        mEtQqNum = (EditText) view.findViewById(R.id.et_qq_num);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtAddress = (EditText) view.findViewById(R.id.et_address);
        mIvCardFront = (ImageView) view.findViewById(R.id.iv_card_front);
        mIvCardBack = (ImageView) view.findViewById(R.id.iv_card_back);
        mBtNext = (Button) view.findViewById(R.id.bt_next);

        mIvBack.setOnClickListener(this);
        mIvCardFront.setOnClickListener(this);
        mIvCardBack.setOnClickListener(this);
        mBtNext.setOnClickListener(this);
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
                if (true) {
                    addFragment(R.id.content, EnterPersonInformationFragment2.newInstance(), mBundle);
                }
                break;

        }
    }

    private boolean checkData() {
        mName = mEtName.getText().toString().trim();
        mAccount = mEtAccount.getText().toString().trim();
        mIdCard = mEtIdCard.getText().toString().trim();
        mQqNum = mEtQqNum.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();
        mPhone = mEtPhone.getText().toString().trim();
        mAddress = mEtAddress.getText().toString().trim();
        if (TextUtil.isEmpty(mName) || TextUtil.isEmpty(mAccount) || TextUtil.isEmpty(mIdCard) ||
                TextUtil.isEmpty(mQqNum) || TextUtil.isEmpty(mEmail) || TextUtil.isEmpty(mPhone) ||
                TextUtil.isEmpty(mAddress)) {
            toastLong("请填写完整的个人信息");
            return false;
        }
        mBundle.putString(Name,mName);
        mBundle.putString(ACCOUNT,mAccount);
        mBundle.putString(IDCARD,mIdCard);
        mBundle.putString(QQ,mQqNum);
        mBundle.putString(EMAIL,mEmail);
        mBundle.putString(PHONE,mPhone);
        mBundle.putString(ADDRESS,mAddress);
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
                                File f = new File(dir, "upimage");
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
    private String photoPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == act.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    File f = new File(Environment.getExternalStorageDirectory()
                            + "/" + "PAIPAI_IMG" + "/" + "upimage");
                    try {
                        pictureUri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(act.getContentResolver(),
                                f.getAbsolutePath(), null, null));
                        photoPath = Uri2BitMapUtils.getPath(act, pictureUri);
                        try {
                            switch (currentClick) {
                                case ICARD_FRONT:
                                    Glide.with(act).load(pictureUri).centerCrop().into(mIvCardFront);
                                    //                                    photoPaths[0] = photoPath;
                                    break;
                                case ICARD_BACK:
                                    Glide.with(act).load(pictureUri).centerCrop().into(mIvCardBack);
                                    //                                    photoPaths[1] = photoPath;
                                    break;
                            }
                            //                            postWorkPhoto();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

                case CHOOSE_PICTURE:
                    pictureUri = data.getData();
                    photoPath = Uri2BitMapUtils.getPath(act, pictureUri);
                    if (pictureUri != null) {
                        try {
                            switch (currentClick) {
                                case ICARD_FRONT:
                                    Glide.with(act).load(pictureUri).centerCrop().into(mIvCardFront);
                                    //                                    photoPaths[0] = photoPath;
                                    break;
                                case ICARD_BACK:
                                    Glide.with(act).load(pictureUri).centerCrop().into(mIvCardBack);
                                    //                                    photoPaths[1] = photoPath;
                                    break;
                            }
                            //                            postWorkPhoto();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
