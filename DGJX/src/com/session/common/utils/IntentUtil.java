package com.session.common.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by user on 2016-11-24.
 */
public class IntentUtil {
    /**
     * 获得打开本地图库的intent
     *
     * @return
     */
    public static Intent getSelectLocalImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 获得用于拍照的intent
     *
     * @return
     */
    public static Intent getTakePhotoIntent(File saveFile) {
        if (saveFile == null) {
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
        return intent;
    }

    /**
     * 获得用于打电话的intent
     *
     * @return
     */
    public static Intent getCallNumberIntent(String number) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        return intent;
    }

    /**
     * 获得用于发邮件的intent
     *
     * @return
     */
    public static Intent getEmailIntent(String email) {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL,email);
        i.putExtra(Intent.EXTRA_TEXT,"");
        i.putExtra(Intent.EXTRA_SUBJECT,"问题反馈");
        i.setType("message/rfc822");
        return i;
    }
}
