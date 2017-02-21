package com.session.dgjx.daytraining;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.session.common.BaseActivity;
import com.session.common.utils.DensityUtil;
import com.session.common.utils.MD5Util;
import com.session.dgjx.R;
import com.session.dgjx.enity.Order;

import java.util.Hashtable;

/**
 * 生成二维码
 */
public class CreateQRCodeActivity extends BaseActivity {

    private int QR_WIDTH, QR_HEIGHT;
    private ImageView ivQrcode;
    private TextView tvHint;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_create_qrcode);
        ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
        tvHint = (TextView) findViewById(R.id.tvHint);
        String operation = getIntent().getStringExtra("operation");
        if (operation == null) {
            finish();
        }

        if (Order.START_SIGN.equals(operation)) {
            initTitle(R.string.start_qrcode);
            tvHint.setText(R.string.start_hint);
        } else if (Order.FINISH_SIGN.equals(operation)) {
            initTitle(R.string.end_qrcode);
            tvHint.setText(R.string.end_hint);
        } else {

        }
        String orderId = getIntent().getStringExtra("orderId");
        QR_WIDTH = DensityUtil.dip2px(ctx, 100);
        QR_HEIGHT = DensityUtil.dip2px(ctx, 100);
        createQRImage("01" + MD5Util.encode(orderId + "DGFDS"), ivQrcode);
    }

    /**
     * 生成二维码图片
     */
    private void createQRImage(String url, ImageView imageView) {
        try {
            // 判断URL合法性
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 显示到一个ImageView上面
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
