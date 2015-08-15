package com.proto.coderslivehack;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
	
	String allProjectTitleFromDB;
	String[] arAllProjectTitleFromDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTitle("Create New Task");
		getActionBar().setIcon(R.drawable.ic_action_create_list);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
		
		// setStatusBar need api 21 now use 14
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.parseColor("#303F9F"));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		
		etSetProjectTitle = (EditText) findViewById(R.id.etSetProjectTitle);
		etSetProjectPLanguages = (EditText) findViewById(R.id.etSetProjectPLanguages);
		etSetProjectDescription = (EditText) findViewById(R.id.etSetProjectDescription);
		
		btnAddProjectToDB = (Button) findViewById(R.id.btnAddProjectToDB);
		btnCreateTaskToHome = (Button) findViewById(R.id.btnCreateTaskToHome);
		btnCreateTaskToSelectTask = (Button) findViewById(R.id.btnCreateTaskToSelectTask);
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		
		if(dBHelperAdapter.isDataExists())
		{
			System.out.println("mytab: data exist");
			btnCreateTaskToSelectTask.setEnabled(true);
		}
		else
		{
			System.out.println("mytab: data not exist");
			btnCreateTaskToSelectTask.setEnabled(false);
		}
		
		btnAddProjectToDB.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO:
				// add to database stuffs
				projectTitle = etSetProjectTitle.getText().toString().trim();
				programmingLanguages = etSetProjectPLanguages.getText().toString().trim();
				projectDescription = etSetProjectDescription.getText().toString().trim();
				
				allProjectTitleFromDB = "";
				allProjectTitleFromDB = dBHelperAdapter.getAllProjectTitles();
				arAllProjectTitleFromDB = allProjectTitleFromDB.split("\\|");
				
				// to prevent insert empty project to db
				if(projectTitle.trim().isEmpty() || programmingLanguages.trim().isEmpty() || 
						projectDescription.trim().isEmpty())
				{
					Toast.makeText(getApplicationContext(), 
							"Please fill in all 3 fields, projectTitle.", Toast.LENGTH_LONG).show();
				}
				else if(Arrays.asList(arAllProjectTitleFromDB).contains(projectTitle))
				{
					Toast.makeText(getApplicationContext(), 
							"Project Title already exist, enter new title.", Toast.LENGTH_LONG).show();
				}
				else
				{
					long id = dBHelperAdapter.insertData(projectTitle, programmingLanguages, 
							projectDescription);

					// if db row id return -1, it mean insert fail
					if(id < 0)
					{
						Toast.makeText(getApplicationContext(), "unsuccessful", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "successful inserted a project", 
								Toast.LENGTH_LONG).show();
						btnCreateTaskToSelectTask.setEnabled(true);
					}
					
					// TODO:
					// for testing display data inside database, remove it later
					String allData = dBHelperAdapter.getAllData();
					Toast.makeText(getApplicationContext(), allData, Toast.LENGTH_LONG).show();
				}
			}
		});
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
