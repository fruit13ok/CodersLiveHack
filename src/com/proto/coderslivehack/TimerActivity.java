package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends Activity
{
	String selectedProject;
	TextView tvSelectedProject, tvWorkOrBreakTime, tvTimer;
	Button btnStartTimer, btnPauseTimer, btnResumeTimer, btnStopTimer, 
	btnMarkProjectComplete, btnMarkProjectAbandon, btnTimerToHome, btnTimerToAssess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		Intent fromSelectTaskIntent = getIntent();
		selectedProject = fromSelectTaskIntent.getStringExtra(SelectTaskActivity.EXTRA_SELECTED_TASK);
		
		tvSelectedProject = (TextView) findViewById(R.id.tvSelectedProject);
		tvSelectedProject.setText(selectedProject);
		tvWorkOrBreakTime = (TextView) findViewById(R.id.tvWorkOrBreakTime);
		tvWorkOrBreakTime.setText("Working");
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		
		btnStartTimer = (Button) findViewById(R.id.btnStartTimer);
		btnPauseTimer = (Button) findViewById(R.id.btnPauseTimer);
		btnResumeTimer = (Button) findViewById(R.id.btnResumeTimer);
		btnStopTimer = (Button) findViewById(R.id.btnStopTimer);
		btnMarkProjectComplete = (Button) findViewById(R.id.btnMarkProjectComplete);
		btnMarkProjectAbandon = (Button) findViewById(R.id.btnMarkProjectAbandon);
		
		btnTimerToHome = (Button) findViewById(R.id.btnTimerToHome);
		btnTimerToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent timerToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(timerToHomeIntent);
				finish();
			}
		});
		btnTimerToAssess = (Button) findViewById(R.id.btnTimerToAssess);
		btnTimerToAssess.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent timerToAssessIntent = new Intent(getBaseContext(), AssessActivity.class);
				startActivity(timerToAssessIntent);
				finish();
			}
		});
	}
}
