package com.session.dgjp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.Base64Coder;
import com.session.common.utils.ZoomBitmap;
import com.session.dgjp.request.UpImgRequestData;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by user on 2016-12-08.
 */
public class UpActivity extends BaseActivity {

    private static final String HOST = "http://10.0.0.189:8080/DGFDS/upload/uploadIdcard.flow";
    // 显示图片
    private ImageView image;
    // 两个but
    private Button take;
    private Button selete;
    // 记录文件名
    private String filename;
    // 上传的bitmap
    private Bitmap upbitmap;
    private Button up;

    Intent intent;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.uptest);

        image = (ImageView) findViewById(R.id.iv);

        Button button1 = (Button) findViewById(R.id.bt1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                filename = "xiaochun" + System.currentTimeMillis() + ".jpg";
                System.out.println(filename);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), filename)));
                startActivityForResult(intent, 1);
            }
        });
        Button button2 = (Button) findViewById(R.id.bt2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, 2);
            }
        });
        Button button3 = (Button) findViewById(R.id.bt3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logI("onClick");
                //				myDialog = ProgressDialog.show(this, "Loading...", "Please wait...", true, false);
                new Thread(new Runnable() {
                    public void run() {
                        upload();
                        //	                    myHandler.sendMessage(new Message());
                    }
                }).start();
            }
        });
    }

    public void upload() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        upbitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] b = stream.toByteArray();
        // 将图片流以字符串形式存储下来
        String file = new String(Base64Coder.encodeLines(b));

        logI("file="+file);
//        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        UpImgRequestData requestData = new UpImgRequestData();
        requestData.setFile(file);
//        requestData.setName("namenamenamenamenamenamenamenamenamenamenamenamename");
        String data = new Gson().toJson(requestData);
        logI("data="+data);

        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                logI("code=" + code);
                logI("msg=" + msg);
                logI("response=" + response);
                switch (code) {
                    case BaseRequestTask.CODE_SUCCESS:
                        //                        parseData(response);
                        break;
                    case BaseRequestTask.CODE_SESSION_ABATE:
                        //                        getData();
                        break;
                    default:
                        break;
                }
            }
        }.request(Constants.URL_UP_LOAD_IDCARD, data, null, true);



//        HttpClient client = new DefaultHttpClient();
//        // 设置上传参数
//        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        formparams.add(new BasicNameValuePair("file", file));
//        HttpPost post = new HttpPost(HOST);
//        UrlEncodedFormEntity entity;
//        try {
//            System.out.println("try");
//            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
//            post.addHeader("Accept",
//                    "text/javascript, text/html, application/xml, text/xml");
//            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//            post.addHeader("Connection", "Keep-Alive");
//            post.addHeader("Cache-Control", "no-cache");
//            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
//            post.setEntity(entity);
//            System.out.println("try1");
//            HttpResponse response = client.execute(post);
//            System.out.println(response.getStatusLine().getStatusCode());
//            HttpEntity e = response.getEntity();
//            System.out.println(EntityUtils.toString(e));
//            if (200 == response.getStatusLine().getStatusCode()) {
//                System.out.println("上传完成");
//            } else {
//                System.out.println("上传失败");
//            }
//            client.getConnectionManager().shutdown();
//        } catch (Exception e) {
//            System.out.println("catch=");
//            e.printStackTrace();
//        }
    }

    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("data="+data);
        switch (requestCode) {
            case 1:
                //解成bitmap,方便裁剪
                Bitmap bitmap= BitmapFactory.decodeFile(Environment.
                        getExternalStorageDirectory().getPath()+"/"+filename);
                float wight=bitmap.getWidth();
                float height=bitmap.getHeight();
                //	          ZoomBitmap.zoomImage(bitmap, wight/8, height/8);
                image.setImageBitmap(ZoomBitmap.zoomImage(bitmap, wight/8, height/8));
                upbitmap=ZoomBitmap.zoomImage(bitmap, wight/8, height/8);
                break;
            case 2:
                if(data!=null){
                    image.setImageURI(data.getData());
                    System.out.println(getAbsoluteImagePath(data.getData()));
                    upbitmap=BitmapFactory.decodeFile(getAbsoluteImagePath(data.getData()));
                    //剪一下，防止测试的时候上传的文件太大
                    upbitmap=ZoomBitmap.zoomImage(upbitmap, upbitmap.getWidth()/8, upbitmap.getHeight()/8);
                }
                break;
            default:
                break;
        }
    }
}
