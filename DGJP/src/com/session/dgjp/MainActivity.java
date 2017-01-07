package com.session.dgjp;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;

import com.session.common.BaseActivity;
import com.session.dgjp.usb.OnTabActivityResultListener;
import com.session.dgjp.view.NoScollerViewPager;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends BaseActivity{
	
	private ArrayList<View> mlistview = new ArrayList<View>(); //viewpager中的内容  
    private LocalActivityManager mactivityManager = null;  
    private NoScollerViewPager mvp_content = null;  
    private String[] mlistTag = {"home","drive","me"}; //activity标识  
    private RadioGroup mRadioGroup;
    private static Activity[] activities ;


    @Override
    protected void init(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_main);
        HomePagerActivity homePagerActivity = new HomePagerActivity();
        HomeActivity homeActivity = new HomeActivity();
        com.session.dgjp.personal.PersonalCenterActivity personalCenterActivity = new com.session.dgjp.personal.PersonalCenterActivity();
        activities = new Activity[3];
        activities[0] = homePagerActivity;
        activities[1] = homeActivity;
        activities[0] = personalCenterActivity;
		mactivityManager = new LocalActivityManager(this, true);
        mactivityManager.dispatchCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		Intent intent = new Intent(getApplicationContext(), HomePagerActivity.class);
        View v1 = getView(mlistTag[0], intent);
        mlistview.add(v1);  
        Intent intent2 = new Intent(getApplicationContext(), HomeActivity.class);
        View v2 = getView(mlistTag[1], intent2);  
        mlistview.add(v2);
        Intent intent3 = new Intent(getApplicationContext(), com.session.dgjp.personal.PersonalCenterActivity.class);
        View v3 = getView(mlistTag[2], intent3);  
        mlistview.add(v3);
		mvp_content = (NoScollerViewPager) findViewById(R.id.vp_content);
		mvp_content.setOffscreenPageLimit(3);
        mvp_content.setAdapter(new MyPagerAdapter(mlistview));  
        mvp_content.setCurrentItem(0);  
        mvp_content.setOnPageChangeListener(new MyOnPageChangeListener());  
        
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_bottom);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
            
          @Override 
          public void onCheckedChanged(RadioGroup group, int checkedId) {  
              if(checkedId==R.id.bt_main){
            	if (mvp_content.getCurrentItem()!=0) {
            		  mvp_content.setCurrentItem(0);
  				}
              }  
              else if(checkedId==R.id.bt_practice_driving){ 
            	if (mvp_content.getCurrentItem()!=1) {
            		mvp_content.setCurrentItem(1);
                }
              }  
              else if(checkedId==R.id.bt_me){  
            	  if (mvp_content.getCurrentItem()!=2) {
            		  mvp_content.setCurrentItem(2);
                  }
              }
          }  
      });
        
	}
	
    private View getView(String id, Intent intent) {  
        return mactivityManager.startActivity(id, intent).getDecorView();  
    }  
    
    public class MyPagerAdapter extends PagerAdapter {  
        List<View> listview =  new ArrayList<View>();  
        public MyPagerAdapter(List<View> list) {  
            this.listview = list;  
        }  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object obj) {  
            container.removeView(listview.get(position));  
        }  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
            container.addView(listview.get(position),0);  
            return listview.get(position);  
        }  
        @Override  
        public int getCount() {  
            return listview.size();  
        }  
        @Override  
        public boolean isViewFromObject(View v, Object obj) {  
            return v == obj;  
        }  
    }  
    /** 
     * 页卡切换监听 
     */  
    public class MyOnPageChangeListener implements OnPageChangeListener {  
  
        @Override  
        public void onPageSelected(int arg0) {  
//            int pos = 0;//记录hsv_column滚动位置  
//            Log.i("onPageSelected", "position="+arg0);  
//            loadCurActivity(arg0);  
        }  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
        }  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取当前活动的Activity实例
        Activity subActivity = activities[mvp_content.getCurrentItem()];
        //判断是否实现返回值接口
        if (subActivity instanceof OnTabActivityResultListener) {
            //获取返回值接口实例
            OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
            //转发请求到子Activity
            listener.onTabActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
