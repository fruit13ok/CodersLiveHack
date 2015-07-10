package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateTaskActivity extends Activity
{
	EditText etSetProjectTitle, etSetProjectPLanguages, etSetProjectDescription;
	Button btnAddProjectToDB, btnCreateTaskToHome, btnCreateTaskToSelectTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		
		etSetProjectTitle = (EditText) findViewById(R.id.etSetProjectTitle);
		etSetProjectPLanguages = (EditText) findViewById(R.id.etSetProjectPLanguages);
		etSetProjectDescription = (EditText) findViewById(R.id.etSetProjectDescription);
		
		btnAddProjectToDB = (Button) findViewById(R.id.btnAddProjectToDB);
		btnAddProjectToDB.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO:
				// add to database stuffs
			}
		});
		btnCreateTaskToHome = (Button) findViewById(R.id.btnCreateTaskToHome);
		btnCreateTaskToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent createTaskToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(createTaskToHomeIntent);
				finish();
			}
		});
		btnCreateTaskToSelectTask = (Button) findViewById(R.id.btnCreateTaskToSelectTask);
		btnCreateTaskToSelectTask.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent createTaskToSelectTaskIntent = new Intent(getBaseContext(), SelectTaskActivity.class);
				startActivity(createTaskToSelectTaskIntent);
				finish();
			}
		});
	}
}
