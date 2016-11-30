package com.session.dgjp.message;

import java.util.ArrayList;
import java.util.List;

import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.common.utils.LogUtil;
import com.session.dgjp.R;
import com.session.dgjp.db.MyMessageDao;
import com.session.dgjp.enity.MyMessage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/** 我的消息 */
public class MessageActivity extends BaseActivity implements OnItemClickListener {

	private MessageAdapter adapter;
	private MyMessageDao messageDao;
	private TextView noDataTv;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_message);
		initTitle(R.string.my_message, true);
		noDataTv = (TextView)findViewById(R.id.no_data);
		ListView listView = (ListView) findViewById(R.id.list_view);
		try {
			messageDao = new MyMessageDao();
			List<MyMessage> messages = messageDao.getMyMessages(account.getAccount());
			adapter = new MessageAdapter(this, messages);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			// 将未读的消息更改为已读
			if (messages != null && !messages.isEmpty()) {
				List<MyMessage> unReadMessages = new ArrayList<MyMessage>();
				for (int i = 0; i < messages.size(); i++) {
					MyMessage message = messages.get(i);
					if (!message.isReaded()) {
						message.setReaded(true);
						unReadMessages.add(message);
					}
				}
				if (!unReadMessages.isEmpty()) {
					for (int j = 0; j < unReadMessages.size(); j++) {
						messageDao.createOrUpdate(unReadMessages.get(j));
					}
				}
				noDataTv.setVisibility(View.GONE);
			}else {
				noDataTv.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), e);
			toast(R.string.get_message_fail, Toast.LENGTH_SHORT);
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.drop_down_textview, getResources().getStringArray(R.array.message_operations));
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							if (which == DialogInterface.BUTTON_POSITIVE) {
								try {
									if (messageDao.delete(id) > 0) {
										MessageActivity.this.adapter.getmList().remove(position);
										MessageActivity.this.adapter.notifyDataSetChanged();
										if(MessageActivity.this.adapter.getmList().isEmpty())
										{
											noDataTv.setVisibility(View.VISIBLE);
										}
									} else {
										toast(R.string.delete_message_fail, Toast.LENGTH_SHORT);
									}
								} catch (Exception e) {
									LogUtil.e(TAG, e.toString(), e);
								}
							}
						}
					};
					BaseDialog baseDialog = new BaseDialog.Builder(MessageActivity.this).setTitle(R.string.hint).setMessage(getString(R.string.delete_record_or_not)).setPositiveButton(R.string.confirm, onClickListener).setNegativeButton(R.string.cancel, onClickListener).create();
					baseDialog.show();
					break;
				default:
					break;
				}
			}
		});
		builder.create().show();
	}

}
