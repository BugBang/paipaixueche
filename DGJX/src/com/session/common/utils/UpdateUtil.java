package com.session.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.session.common.BaseDialog;
import com.session.common.BaseRequestTask;
import com.session.dgjx.Constants;
import com.session.dgjx.R;
import com.session.dgjx.request.CheckVersionRequestData;

/** 更新工具类 */
public final class UpdateUtil {
	
	private static VersionInfo mInfo;
	private static boolean isUpdate;
	
	private UpdateUtil() {
		// 更新工具类，不需要实例
	}

	public static void checkUpdate(final OnVersionInfoListener listener) {
		CheckVersionRequestData requestData = new CheckVersionRequestData();
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				VersionInfo info = null;
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						mInfo = new Gson().fromJson(response, VersionInfo.class);
						info = mInfo;
						listener.onVersionInfo(info);
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						checkUpdate(listener);
						break;
					default:
						listener.onVersionInfo(info);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					info = null;
					listener.onVersionInfo(info);
				}
			}
		}.request(Constants.URL_CHECK_VERSION, data, null, false);
	}

	/*public static void showUpdate(final Context ctx, String title, final int autoTime) {
		if (null == mInfo) {
			return;
		}
		DialogInterface.OnClickListener onDialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case Dialog.BUTTON_NEGATIVE:
					break;
				case Dialog.BUTTON_POSITIVE:
					update(ctx);// 确定更新
					break;
				}
				dialog.dismiss();
			}
		};
		boolean canCancle = 0 == mInfo.getType();
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setCancelable(canCancle);// 是否能点击框外取消
		if (null != title)
			builder.setTitle(title);
		builder.setMessage(VersionUtil.Singleton.getDescription());
		if (canCancle) {
			builder.setNegativeButton("取消", onDialogClickListener);
		}
		builder.setPositiveButton("确定", onDialogClickListener);
		AlertDialog dTmp = null;
		try {
			dTmp = builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		final AlertDialog dialog = dTmp;
		if (null == dialog) {
			return;
		}
		if (0 == autoTime) {
			dialog.setMessage(VersionUtil.Singleton.getDescription());
		} else {
			final Handler handler = new Handler();
			handler.post(new Runnable() {
				int s = autoTime;

				@Override
				public void run() {
					if (isUpdate) {
						handler.removeCallbacks(this);
						return;
					}
					if (s > 0) {
						dialog.setMessage("应用版本将在" + s + "秒后自动更新");
						s--;
						handler.postDelayed(this, 1000);
					} else {
						handler.removeCallbacks(this);
						update(ctx);// 确定更新
					}
				}
			});
		}
	}*/
	
	public static void showUpdate(final Context ctx, String title, final int autoTime) {
		if (null == mInfo) {
			return;
		}
		DialogInterface.OnClickListener onDialogClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case Dialog.BUTTON_NEGATIVE:
					break;
				case Dialog.BUTTON_POSITIVE:
					update(ctx);// 确定更新
					break;
				}
				dialog.dismiss();
			}
		};
		boolean canCancle = 0 == mInfo.getType();
		BaseDialog.Builder builder = new BaseDialog.Builder(ctx);
		if (null != title)
			builder.setTitle(title);
		builder.setMessage(mInfo.getDescription());
		if (canCancle) {
			builder.setNegativeButton("取消", onDialogClickListener);
		}
		builder.setPositiveButton("确定", onDialogClickListener);
		BaseDialog dialog = builder.create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
//		if (0 == autoTime) {
//			
//		} else {
//			final Handler handler = new Handler();
//			handler.post(new Runnable() {
//				int s = autoTime;
//				
//				@Override
//				public void run() {
//					if (isUpdate) {
//						handler.removeCallbacks(this);
//						return;
//					}
//					if (s > 0) {
//						dialog.setMessage("应用版本将在" + s + "秒后自动更新");
//						s--;
//						handler.postDelayed(this, 1000);
//					} else {
//						handler.removeCallbacks(this);
//						update(ctx);// 确定更新
//					}
//				}
//			});
//		}
	}

	/** 下载文件的数据库ID */
	private static long fileId;

	/** 下载更新 */
	@SuppressLint("InlinedApi")
	private static void update(Context ctx) {
		if (isUpdate) {
			return;
		}
		isUpdate = true;
		if (8 >= android.os.Build.VERSION.SDK_INT) {
			// downloadHttp(ctx);
			return;
		}
		CompleteReceiver recerver = new CompleteReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		ctx.registerReceiver(recerver, filter);

		DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(mInfo.getUrl());
		Request request = new Request(uri);
		// 设置允许使用的网络类型
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		request.allowScanningByMediaScanner();
		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false);
		// 显示下载界面
		request.setVisibleInDownloadsUi(true);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS,
				mInfo.getUrl().substring(mInfo.getUrl().lastIndexOf('/') + 1));
		// request.setDestinationInExternalFilesDir(this, android.os.Environment.DIRECTORY_DOWNLOADS, "duofangtong.apk");
		try {
			fileId = downloadManager.enqueue(request);// 停用删除原生的下载管理器会报错，这时候转用Http下载
		} catch (Exception e) {
			e.printStackTrace();
			downloadHttp(ctx);
		}
	}
	
	/**
	 * 原生下载管理器无法使用时改用Http直接下载
	 */
	private static void downloadHttp(final Context ctx) {
		if (isUpdate) {
			return;
		}
		isUpdate = true;
		final NotificationManager notifyManager = (NotificationManager) ctx.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		final int notifyId = 101;
		final Notification notify = new Notification();
		notify.icon = R.drawable.ic_launcher;
		notify.tickerText = "开始下载";
		notify.when = System.currentTimeMillis();
		RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.notification_download);
		contentView.setImageViewResource(R.id.ivIcon, R.drawable.ic_launcher);
		contentView.setProgressBar(R.id.pbUpdate, 100, 0, false);
		contentView.setTextViewText(R.id.tvTitle, "正在下载...");
		notify.flags |= Notification.FLAG_ONGOING_EVENT;
		notify.flags |= Notification.FLAG_NO_CLEAR;
		notify.contentView = contentView;
		new AsyncTask<Void, Integer, String>() {
			int readSize = 0;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 开始下载
				notifyManager.notify(notifyId, notify);
			}

			@Override
			protected String doInBackground(Void... params) {
				int fileSize = 0;
				int downSize = 0;
				String name = mInfo.getUrl().substring(mInfo.getUrl().lastIndexOf('/') + 1);
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					URL myURL = new URL(mInfo.getUrl()); // 取得URL
					URLConnection conn = myURL.openConnection(); // 建立联机
					conn.connect();
					fileSize = conn.getContentLength(); // 获取文件长度
					is = conn.getInputStream(); // InputStream 下载文件
					File downFile;
					if (is == null) {
						throw new RuntimeException("stream is null");
					}
					if (isExistedSD()) {
						// 有SD卡，则存入SD卡
						File temp = new File(Environment.getExternalStorageDirectory() + "/Download/");
						if (!temp.exists())
							temp.mkdirs();
						downFile = new File(temp, name);
						downFile.createNewFile();
						fos = new FileOutputStream(downFile);
					} else {
						downFile = new File(ctx.getFilesDir() + "/" + name);
						if (!downFile.exists()) {
							downFile.createNewFile();
						}
						fos = new FileOutputStream(downFile);
					}
					// 将文件写入临时盘
					byte buf[] = new byte[1024 * 1024];
					while ((readSize = is.read(buf)) != -1) {
						fos.write(buf, 0, readSize);
						downSize += readSize;
						onProgressUpdate(downSize * 100 / fileSize);
					}
					return downFile.getAbsolutePath();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fos)
							fos.close();
						if (null != is)
							is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				RemoteViews contentView = notify.contentView;
				contentView.setTextViewText(R.id.tvMsg, (readSize < 0 ? 0 : readSize) + "b/s   " + values[0].intValue() + "%");
				contentView.setProgressBar(R.id.pbUpdate, 100, values[0].intValue(), false);
				// 更新UI
				notifyManager.notify(notifyId, notify);
				super.onProgressUpdate(values);
			}

			@Override
			protected void onPostExecute(String result) {
				isUpdate = false;
				notifyManager.cancel(notifyId);
				if (null != result) {
					// 安装
					install(ctx, result);
				}
				super.onPostExecute(result);
			}

			@Override
			protected void onCancelled() {
				isUpdate = false;
				notifyManager.cancel(notifyId);
				super.onCancelled();
			}

		}.execute();
	}
	
	/**
	 * SD卡是否存在
	 * @return SD卡是否存在
	 */
	public static boolean isExistedSD() {
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/** 安装更新 */
	private static void install(Context ctx, final String url) {
		// 保存在App文件下，不可以直接安装
		try {
			Process process = Runtime.getRuntime().exec("chmod a+r " + url);// 赋予apk可读权限
			int res = process.waitFor();
			if (0 == res) {// 命令成功执行
			} else {// 命令执行出现异常
				Runtime.getRuntime().exec("chmod 644 " + url);// 赋予apk可读权限，某些手机chmod不支持chmod a+r的方式
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent in = new Intent(Intent.ACTION_VIEW);
		in.setDataAndType(Uri.fromFile(new File(url)), "application/vnd.android.package-archive");
		if (null != in.resolveActivity(ctx.getPackageManager())) {
			ctx.startActivity(in);
		}
	}

	private static class CompleteReceiver extends BroadcastReceiver {

		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void onReceive(Context context, Intent intent) {
			DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if (id != fileId) {
				return;// 不是更新包
			}
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {// 文件下载完了
				isUpdate = false;

				Query query = new Query();
				query.setFilterById(id);
				Cursor cursor = downloadManager.query(query);
				cursor.moveToFirst();
				int index = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
				String path = null;
				try {
					path = cursor.getString(index);
				} catch (Exception e) {
					e.printStackTrace();
				}
				cursor.close();
				if (null != path) {
					install(context, UriUtil.getPath(context, Uri.parse(path)));
				}
				// 注销监听广播
				context.unregisterReceiver(this);
			}
		}

	}

	public interface OnVersionInfoListener {
		public void onVersionInfo(VersionInfo info);
	}

}
