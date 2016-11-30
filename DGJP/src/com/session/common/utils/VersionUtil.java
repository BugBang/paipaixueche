package com.session.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.widget.RemoteViews;

public enum VersionUtil {
	Singleton;

	private VersionUtil() {
	}

	/** 更新类型 */
	private int type;
	/** 版本编码 */
	private int code;
	/** 版本名称 */
	private String version;
	/** 更新包所在目录 */
	private String url;
	/** 更新包文件名 */
	private String fileName;
	/** 更新包文件类型 */
	private String fileType;
	/** 更新包地址 */
	private String description;
	/** 是否正在更新 */
	private boolean isUpdate;
	/** 是否已经获取到版本信息 */
	private boolean isGetVersionInfo;

	/** 更新类型，0表示不强制更新，1表示新版本强制更新，2表示只要版本不同就必须更新 */
	public int getType() {
		return type;
	}

	/** 版本编码 */
	public int getCode() {
		return code;
	}

	/** 版本名称 */
	public String getVersion() {
		return version;
	}

	/** 更新包所在目录 */
	public String getUrl() {
		return url;
	}

	/** 更新包文件名 */
	public String getFileName() {
		return fileName;
	}

	/** 更新包文件类型 */
	public String getFileType() {
		return fileType;
	}

	/** 更新包更新内容描述 */
	public String getDescription() {
		return description;
	}

	/** 更新报的绝对地址 */
	public String getUpdateUrl() {
		return url + fileName + "." + fileType;
	}

	/** 是否在在下载更新 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/** 是否已经获取到更新信息 */
	public boolean isGetVersionInfo() {
		return isGetVersionInfo;
	}

	/**
	 * 是否应该更新版本
	 * @param versionCode 当前版本号
	 * @return 是否应该更新
	 */
	public boolean shouldUpdate(int versionCode) {
		switch (type) {
		case 0:// 非强制更新
			return versionCode < code;
		case 1:// 强制新版本更新
			return versionCode < code;
		case 2:// 强制不同版本更新
			return versionCode != code;
		}
		return false;
	}

	public void showUpdate(final Context ctx, String title, final int autoTime) {
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
		boolean canCancle = 0 == type;
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
	}

	/** 下载文件的数据库ID */
	private long fileId;

	/** 下载更新 */
	@SuppressLint("InlinedApi")
	private void update(Context ctx) {
		if (isUpdate) {
			return;
		}
		isUpdate = true;
		if (8 >= android.os.Build.VERSION.SDK_INT) {
			downloadHttp(ctx);
			return;
		}
		CompleteReceiver recerver = new CompleteReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		ctx.registerReceiver(recerver, filter);

		DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(getUpdateUrl());
		Request request = new Request(uri);
		// 设置允许使用的网络类型
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		request.allowScanningByMediaScanner();
		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false);
		// 显示下载界面
		request.setVisibleInDownloadsUi(true);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, "dft.apk");
		// request.setDestinationInExternalFilesDir(this, android.os.Environment.DIRECTORY_DOWNLOADS, "duofangtong.apk");
		try {
			fileId = downloadManager.enqueue(request);// 停用删除原生的下载管理器会报错，这时候转用Http下载
		} catch (Exception e) {
			e.printStackTrace();
			downloadHttp(ctx);
		}
	}

	/** 原生下载管理器无法使用时改用Http直接下载 */
	private void downloadHttp(final Context ctx) {
		final NotificationManager notifyManager = (NotificationManager) ctx.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		final int notifyId = 101;
		final Notification notify = new Notification();
//		notify.icon = R.drawable.applogo;
		notify.tickerText = "开始下载";
		notify.when = System.currentTimeMillis();
//		RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.layout_download_notification);
//		contentView.setImageViewResource(R.id.ivIcon, R.drawable.ic_launcher);
//		contentView.setProgressBar(R.id.pbUpdate, 100, 0, false);
//		contentView.setTextViewText(R.id.tvTitle, "正在下载……");
		notify.flags |= Notification.FLAG_ONGOING_EVENT;
		notify.flags |= Notification.FLAG_NO_CLEAR;
//		notify.contentView = contentView;
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
				String name = fileName;
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					URL myURL = new URL(getUpdateUrl()); // 取得URL
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
//				contentView.setTextViewText(R.id.tvMsg, (readSize < 0 ? 0 : readSize) + "b/s   " + values[0].intValue() + "%");
//				contentView.setProgressBar(R.id.pbUpdate, 100, values[0].intValue(), false);
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
	private void install(Context ctx, final String url) {
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

	/**
	 * 获取版本信息
	 * @param versionXmlUrl 版本配置xml文件地址
	 * @param listener 回调监听接口
	 */
	public void getVersionInfo(final String versionXmlUrl, final onGetVersionListener listener) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					URL url = new URL(versionXmlUrl);
					URLConnection urlCon = url.openConnection();
					InputSource is = new InputSource(urlCon.getInputStream());
					is.setEncoding("UTF-8");
					// 解析XML
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser saxParser;
					saxParser = spf.newSAXParser();
					// 创建解析器
					ParsingXMLElements handler = new ParsingXMLElements();
					saxParser.parse(is, handler);
					Map<String, String> maps = handler.getElement();
					Singleton.fileName = maps.get("filename");
					Singleton.fileType = maps.get("filetype");
					Singleton.version = maps.get("version");
					Singleton.type = Integer.valueOf(maps.get("type"));
					Singleton.code = Integer.valueOf(maps.get("code"));
					Singleton.url = maps.get("url");
					Singleton.description = maps.get("description");
					isGetVersionInfo = true;
					return "ok";
				} catch (Exception e) {
					// 无网络时会报错
					isGetVersionInfo = false;
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (null != listener) {
					listener.onGetVersion(Singleton);
				}
			}

		}.execute();
	}

	private class ParsingXMLElements extends DefaultHandler {
		private Map<String, String> element; // 存储XML元素键值
		private String elementName; // 前一个元素名称

		public Map<String, String> getElement() {
			return element;
		}

		/** 文档解析开始 */
		@Override
		public void startDocument() throws SAXException {
			element = new HashMap<String, String>();
		}

		/** 文档解析结束 */
		@Override
		public void endDocument() throws SAXException {
		}

		/** 节点解析开始 */
		@Override
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
			elementName = localName;
		}

		/** 节点解析结束 */
		@Override
		public void endElement(String namespaceURI, String localName, String fullName) throws SAXException {
		}

		/** 节点解析 */
		@Override
		public void characters(char[] chars, int start, int length) throws SAXException {
			if (null != elementName) {
				// String old=new String(chars);
				// String s=String.valueOf(chars, start, length).replace("\\r", "\r").replace("\\n", "\n");
				// Log.e("old + s", old+"---"+s);
				// 将元素内容加到Map中
				element.put(elementName, String.valueOf(chars, start, length).replace("\\r", "\r").replace("\\n", "\n"));
				elementName = null;
			}
		}
	}

	private class CompleteReceiver extends BroadcastReceiver {

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
				if (null != path){
//					install(context, UriUtil.getPath(context, Uri.parse(path)));
				}
				// 注销监听广播
				context.unregisterReceiver(this);
			}
		}

	}

	public interface onGetVersionListener {

		/** 当获取到版本信息时被调用 */
		public void onGetVersion(VersionUtil version);

	}

}
