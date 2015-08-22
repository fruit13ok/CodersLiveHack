package com.proto.coderslivehack;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * This adapter use by home page's ViewPager to swipe through a given list of fragments
 */
public class PagerAdapter extends FragmentPagerAdapter
{
	private List<Fragment> fragments;
	
	public PagerAdapter(FragmentManager fm, List<Fragment> fragments)
	{
		super(fm);
		this.fragments = fragments;
	}

	/*
	 * return fragment to display for that page
	 */
	@Override
	public Fragment getItem(int arg0)
	{
		return this.fragments.get(arg0);
	}

	/*
	 * return total number of page
	 */
	@Override
	public int getCount()
	{
		return this.fragments.size();
	}

	/*
	 *  returns page title for indicator
	 */
    @Override
    public CharSequence getPageTitle(int position)
    {
        return "page " + position;
    }
}
