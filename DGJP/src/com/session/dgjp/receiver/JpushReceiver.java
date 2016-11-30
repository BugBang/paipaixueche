package com.session.dgjp.receiver;



import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.common.utils.GsonUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.HomeActivity;
import com.session.dgjp.db.MyMessageDao;
import com.session.dgjp.enity.Account;
import com.session.dgjp.enity.MyMessage;
import com.session.dgjp.login.WelcomActivity;
import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class JpushReceiver extends BroadcastReceiver
{
	private final static String TAG = JpushReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		//Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
		{
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			LogUtil.e(TAG, title+","+content+","+extras);
			if(extras != null && !TextUtil.isEmpty(extras))
			{
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(extras);
				if(element.isJsonObject())
				{
					JsonObject object = (JsonObject)element;
					String msgType = GsonUtil.getString(object, "typ");
					if(msgType != null)
					{
						MyMessage myMessage = new MyMessage();
						myMessage.setTitle(title);
						myMessage.setContent(content);
						myMessage.setExtras(extras);
						myMessage.setMsgType(msgType);
						Account account = AppInstance.getInstance().getAccount();
						if(account != null)
						{
							myMessage.setAccount(account.getAccount());
						}else {
							myMessage.setAccount(SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_ACCOUNT_ACCOUNT, ""));
						}
						long sendTime = GsonUtil.getLong(object, "st");//时间秒数
						sendTime = sendTime*1000;
						myMessage.setSendTime(sendTime);
						try
						{
							MyMessageDao dao = new MyMessageDao();
							dao.createOrUpdate(myMessage);
							Intent i = new Intent(HomeActivity.ACTION_MSG_COUNT_CHANGED);
							LocalBroadcastManager.getInstance(context).sendBroadcast(i);
						} catch (Exception e)
						{
							LogUtil.e(TAG,e.toString(),e);
						}
					}
				}else if(element.isJsonArray())
				{
					
				}else {
					
				}
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			//LogUtil.e(TAG, printBundle(bundle));
			Intent i = new Intent();
			if(AppInstance.getInstance().getAccount() != null)
			{
				//如果已经登录，则跳转到我的消息页面
				i.setClass(context, HomeActivity.class);
			}else{
				//跳转到登陆页面
				i.setClass(context, WelcomActivity.class);
			}
			// 打开自定义的Activity
			i.putExtras(bundle);
			//i.putExtra(name, value)
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			AppInstance.getInstance().getExtraMap().putExtra(HomeActivity.FROM_NOTIFICATION, true);
			context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
		{
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
		} else
		{
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	
	
	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle)
	{
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet())
		{
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA))
			{
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty())
				{
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try
				{
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext())
					{
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
					}
				} catch (JSONException e)
				{
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	

	// send msg to MainActivity
	/*private void processCustomMessage(Context context, Bundle bundle)
	{
		if (MainActivity.isForeground)
		{
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras))
			{
				try
				{
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0)
					{
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e)
				{

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}*/

}
