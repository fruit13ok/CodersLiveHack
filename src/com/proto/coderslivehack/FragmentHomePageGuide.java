package com.proto.coderslivehack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * a fragment for viewpager contain layout of home guide
 */
public class FragmentHomePageGuide extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
		{
			return null;
		}
		return (RelativeLayout) inflater.inflate(R.layout.fragment_homepageguide_layout, container, false);
	}
}
