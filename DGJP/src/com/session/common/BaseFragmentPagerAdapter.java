package com.session.common;

import java.util.List;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * 该类主要用于fragment之间的切换
 * 
 * @author YJ Liang 2015年9月24日 下午3:12:05
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter
{
	private List<BaseFragment> fragments;
	public BaseFragmentPagerAdapter(FragmentManager fragmentManager, List<BaseFragment> fragments)
	{
		super(fragmentManager);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int index)
	{
		return fragments.get(index);
	}

	@Override
	public int getCount()
	{
		return fragments != null ? fragments.size() : 0;
	}

	/*@Override
	public int getItemPosition(Object object)
	{
		//这样才能在更换fragment后，从新绘制
		return POSITION_NONE;
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		//清楚fragmentManager中的对应的fragment缓存
		Fragment fragment = (Fragment)super.instantiateItem(container, position);
		Fragment currentFragment = fragments.get(position);
		if(!fragment.equals(currentFragment))
		{
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(fragment);
			fragmentTransaction.add(container.getId(), currentFragment);
			fragmentTransaction.attach(currentFragment);
			fragmentTransaction.commit();
			fragmentManager.executePendingTransactions();
			fragment = currentFragment;
		}
		return fragment;
	}*/
	
}
