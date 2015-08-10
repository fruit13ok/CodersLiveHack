package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * This completes changes and additions to CreateTaskActivity.java
 */
public class CreateTaskActivity extends Activity
{
	EditText etSetProjectTitle, etSetProjectPLanguages, etSetProjectDescription;
	Button btnAddProjectToDB, btnCreateTaskToHome, btnCreateTaskToSelectTask;
	
	DBHelperAdapter dBHelperAdapter;
	
	String projectTitle, programmingLanguages, projectDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		
		etSetProjectTitle = (EditText) findViewById(R.id.etSetProjectTitle);
		etSetProjectPLanguages = (EditText) findViewById(R.id.etSetProjectPLanguages);
		etSetProjectDescription = (EditText) findViewById(R.id.etSetProjectDescription);
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		
		btnAddProjectToDB = (Button) findViewById(R.id.btnAddProjectToDB);
		btnAddProjectToDB.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO:
				// add to database stuffs
				projectTitle = etSetProjectTitle.getText().toString();
				programmingLanguages = etSetProjectPLanguages.getText().toString();
				projectDescription = etSetProjectDescription.getText().toString();
				
				long id = dBHelperAdapter.insertData(projectTitle, programmingLanguages, 
						projectDescription);

				if(id < 0)
				{
					Toast.makeText(getApplicationContext(), "unsuccessful", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "successful inserted a project", 
							Toast.LENGTH_LONG).show();
				}
				
				// TODO:
				// for testing display data inside database, remove it later
				String allData = dBHelperAdapter.getAllData();
				Toast.makeText(getApplicationContext(), allData, Toast.LENGTH_LONG).show();
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
