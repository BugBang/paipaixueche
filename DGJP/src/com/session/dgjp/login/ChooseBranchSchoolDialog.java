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
import com.session.dgjp.enity.BranchSchool;

/** 选择分校对话框 */
public abstract class ChooseBranchSchoolDialog extends Dialog {

	private Context mContext;
	private List<BranchSchool> mList;
	private ListView listView;
	private BranchSchoolAdapter mAdapter;

	public ChooseBranchSchoolDialog(Context context, List<BranchSchool> list) {
		super(context);
		mContext = context;
		mList = list;
	}

	public ChooseBranchSchoolDialog(Context context, int theme, List<BranchSchool> list) {
		super(context, theme);
		mContext = context;
		mList = list;
	}

	public ChooseBranchSchoolDialog(Context context, boolean cancelable, OnCancelListener cancelListener, List<BranchSchool> list) {
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
		mAdapter = new BranchSchoolAdapter(mContext, mList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BranchSchool item = mList.get(position);
				onChoose(item.getId(), item.getName());
				cancel();
			}
		});
	}

	protected abstract void onChoose(long id, String name);
	
}
