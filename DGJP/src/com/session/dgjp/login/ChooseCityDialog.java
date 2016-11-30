package com.session.dgjp.login;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.session.dgjp.R;
import com.session.dgjp.enity.City;

/** 选择城市对话框 */
public abstract class ChooseCityDialog extends Dialog {

	private Context mContext;
	private List<City> mList;
	private ListView listView;
	private CityAdapter mAdapter;

	public ChooseCityDialog(Context context, List<City> list) {
		super(context);
		mContext = context;
		mList = list;
	}

	public ChooseCityDialog(Context context, int theme, List<City> list) {
		super(context, theme);
		mContext = context;
		mList = list;
	}

	public ChooseCityDialog(Context context, boolean cancelable, OnCancelListener cancelListener, List<City> list) {
		super(context, cancelable, cancelListener);
		mContext = context;
		mList = list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_choose);
		listView = (ListView) findViewById(R.id.list_view);
		mAdapter = new CityAdapter(mContext, mList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				City item = mList.get(position);
				onChoose(item.getId(), item.getName());
				cancel();
			}
		});
	}

	protected abstract void onChoose(long id, String name);
	
}
