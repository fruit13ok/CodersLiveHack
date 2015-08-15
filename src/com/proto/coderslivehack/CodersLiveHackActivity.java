package com.proto.coderslivehack;

import java.util.List;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CodersLiveHackActivity extends FragmentActivity
{
	Button btnHomeToCreateTask, btnHomeToSelectTask, btnHomeToGraph, btnHomeToNumGitCommit;
	DBHelperAdapter dBHelperAdapter;
	private PagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTitle("Coders Live Hack");
		getActionBar().setIcon(R.drawable.ic_action_home);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
		
		// setStatusBar need api 21 now use 14
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.parseColor("#303F9F"));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coders_live_hack);
		
		btnHomeToCreateTask = (Button) findViewById(R.id.btnHomeToCreateTask);
		btnHomeToSelectTask = (Button) findViewById(R.id.btnHomeToSelectTask);
		btnHomeToGraph = (Button) findViewById(R.id.btnHomeToGraph);
		btnHomeToNumGitCommit = (Button) findViewById(R.id.btnHomeToNumGitCommit);
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		
//		if(dBHelperAdapter.isTableExists())
//		{
//			System.out.println("mytab: table exist");
//		}
//		else
//		{
//			System.out.println("mytab: table not exist");
//		}
		if(dBHelperAdapter.isDataExists())
		{
			System.out.println("mytab: data exist");
			btnHomeToSelectTask.setEnabled(true);
			btnHomeToGraph.setEnabled(true);
		}
		else
		{
			System.out.println("mytab: data not exist");
			btnHomeToSelectTask.setEnabled(false);
			btnHomeToGraph.setEnabled(false);
		}
		if(dBHelperAdapter.isGraphableDataExists())
		{
			System.out.println("mytab: graph data exist");
			btnHomeToGraph.setEnabled(true);
		}
		else
		{
			System.out.println("mytab: graph data not exist");
			btnHomeToGraph.setEnabled(false);
		}
		
		btnHomeToCreateTask.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent homeToCreateTaskIntent = new Intent(getBaseContext(), CreateTaskActivity.class);
				startActivity(homeToCreateTaskIntent);
				finish();
			}
		});
		btnHomeToSelectTask.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent homeToSelectTaskIntent = new Intent(getBaseContext(), SelectTaskActivity.class);
				startActivity(homeToSelectTaskIntent);
				finish();
			}
		});
		btnHomeToGraph.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent homeToGraphIntent = new Intent(getBaseContext(), GraphActivity.class);
				startActivity(homeToGraphIntent);
				finish();
			}
		});
		btnHomeToNumGitCommit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent homeToNumGitCommitIntent = new Intent(getBaseContext(), GetGitCommitActivity.class);
				startActivity(homeToNumGitCommitIntent);
				finish();
			}
		});
		
		initViewPager();
	}
	
	/**
	 * create a ViewPager to let user swipe through each page of instruction on how to use this app.
	 */
	private void initViewPager()
	{
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, FragmentHomePageGuide.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentCreateTaskGuide.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentSelectTaskGuide.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentTimerGuide.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentAssessGuide.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentGraphGuide.class.getName()));
		fragments.add(Fragment.instantiate(this, FragmentGitGuide.class.getName()));
		mPagerAdapter =new PagerAdapter(this.getSupportFragmentManager(), fragments);
		
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager1);
		pager.setAdapter(mPagerAdapter);
	}
}
