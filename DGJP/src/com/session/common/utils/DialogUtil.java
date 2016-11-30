package com.session.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by user on 2016-09-19.
 */
public class DialogUtil {
    private Activity mActivity = null;

    public DialogUtil(Activity activity) {
        this.mActivity = activity;
    }

    public Dialog alert(CharSequence title, CharSequence message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public Dialog alert(int title, int message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public Dialog confirm(CharSequence title, CharSequence message,
                          DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(title);
        builder.setMessage(message);

        try {
            builder.setPositiveButton("确定", confirmListener);
        } catch (Exception e) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        try {
            builder.setNegativeButton("取消", cancelListener);
        } catch (Exception e) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public Dialog confirm(int title, int message, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        try {
            builder.setPositiveButton("确定", confirmListener);
        } catch (Exception e) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        try {
            builder.setNegativeButton("取消", cancelListener);
        } catch (Exception e) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    // 弹出自定义的窗体
    public Dialog showView(CharSequence title, View view, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (title != "")
            builder.setTitle(title);
        builder.setView(view);

        try {
            builder.setPositiveButton("确定", confirmListener);
        } catch (Exception e) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        try {
            builder.setNegativeButton("取消", cancelListener);
        } catch (Exception e) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public Dialog showView(int title, View view, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (title != 0)
            builder.setTitle(title);
        builder.setView(view);

        try {
            builder.setPositiveButton("确定", confirmListener);
        } catch (Exception e) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        try {
            builder.setNegativeButton("取消", cancelListener);
        } catch (Exception e) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    // 弹出自定义的信息
    public Dialog showMsg(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (title != "")
            builder.setTitle(title);
        builder.setMessage(message);
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public Dialog showMsg(int title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (title != 0)
            builder.setTitle(title);
        builder.setMessage(message);
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

//    public Dialog showLoading(String message) {
//        ProgressDialog dialog = new ProgressDialog(mActivity, R.style.dialogBase);
//        TextView txt = dialog.getmTxtMsg();
//        if (message != null && txt != null)
//        {
//            txt.setText(message);
//        }
//        dialog.show();
//        dialog.setCancelable(false);
//        return dialog;
//    }
}
