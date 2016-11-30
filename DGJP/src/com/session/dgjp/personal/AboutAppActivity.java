package com.session.dgjp.personal;

import com.session.common.BaseActivity;
import com.session.dgjp.R;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/** 关于 */
public class AboutAppActivity extends BaseActivity {

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_about_app);
		initTitle(R.string.about);
		TextView textView = (TextView)findViewById(R.id.declare_detail);
		String appName = getString(R.string.app_name);
		textView.setText(getString(R.string.declare_detail, appName, appName));
		ImageView imageView = (ImageView)findViewById(R.id.img_about_logo);
		imageView.setImageResource(R.drawable.img_about_logo);
	}

}
