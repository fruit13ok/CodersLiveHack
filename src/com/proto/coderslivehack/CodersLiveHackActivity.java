package com.proto.coderslivehack;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public class CodersLiveHackActivity extends Activity implements TabListener
{
	List<Fragment> fragList = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//		setContentView(R.layout.activity_coders_live_hack);

		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab = bar.newTab();
		//		tab.setText("Tab " + 1);
		tab.setIcon(R.drawable.ic_action_home);
		tab.setTabListener(this);
		bar.addTab(tab);

		bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_task).setTabListener(this));
		bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_timer).setTabListener(this));
		bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_graph).setTabListener(this));
		bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_git).setTabListener(this));
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		HomeFrg homeFrg = new HomeFrg();		
		TaskFrg taskFrg = new TaskFrg();
		TimerFrg timerFrg = new TimerFrg();
		GraphFrg graphFrg = new GraphFrg();
		GitFrg gitFrg = new GitFrg();

		if (fragList.size() > tab.getPosition())
		{
			fragList.get(tab.getPosition());
		}

		Bundle data = new Bundle();
		data.putInt("idx",  tab.getPosition());

		if(tab.getPosition() == 0)
		{
			setTitle("homeFrg title");
			homeFrg.setArguments(data);
			fragList.add(homeFrg);
			ft.replace(android.R.id.content, homeFrg);
		}
		else if(tab.getPosition() == 1)
		{
			setTitle("taskFrg title");
			taskFrg.setArguments(data);
			fragList.add(taskFrg);
			ft.replace(android.R.id.content, taskFrg);
		}
		else if(tab.getPosition() == 2)
		{
			setTitle("timerFrg title");
			timerFrg.setArguments(data);
			fragList.add(timerFrg);
			ft.replace(android.R.id.content, timerFrg);
		}
		else if(tab.getPosition() == 3)
		{
			setTitle("graphFrg title");
			graphFrg.setArguments(data);
			fragList.add(graphFrg);
			ft.replace(android.R.id.content, graphFrg);
		}
		else if(tab.getPosition() == 4)
		{
			setTitle("gitFrg title");
			gitFrg.setArguments(data);
			fragList.add(gitFrg);

			ft.replace(android.R.id.content, gitFrg);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		if (fragList.size() > tab.getPosition())
		{
			ft.remove(fragList.get(tab.getPosition()));
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
	}
}
