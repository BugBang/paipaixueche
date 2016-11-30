package com.session.common;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * 该类主要用于fragment之间的切换
 * @author YJ Liang
 * 2015年9月24日 下午3:12:05
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter
{
	private List<BaseFragment> fragments;
	public BaseFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments)
	{
		super(fm);
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
		return fragments.size();
	}
	
}
