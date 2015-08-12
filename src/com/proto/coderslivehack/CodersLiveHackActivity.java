package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CodersLiveHackActivity extends Activity
{
	Button btnHomeToCreateTask, btnHomeToSelectTask, btnHomeToGraph, btnHomeToNumGitCommit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTitle("Coders Live Hack");
		getActionBar().setIcon(R.drawable.ic_action_home);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coders_live_hack);
		
		btnHomeToCreateTask = (Button) findViewById(R.id.btnHomeToCreateTask);
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
		btnHomeToSelectTask = (Button) findViewById(R.id.btnHomeToSelectTask);
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
		btnHomeToGraph = (Button) findViewById(R.id.btnHomeToGraph);
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
		btnHomeToNumGitCommit = (Button) findViewById(R.id.btnHomeToNumGitCommit);
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
	}
}
