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
import android.widget.Spinner;
import android.widget.Toast;

// TODO:
// after finish code merge make insert_EstimationValue() from an update() into part of insert()
/*
 * Three Added Variables:
 *
 * Spinner spEstimation
 * ArrayAdapter<CharSequence> aaEstimation
 * String mEstimationValue
 *
 * Added a method call to insert the estimation Value into database within the
 *      onClick method of the btnAddProjectToDB button.
 *
 * This completes changes and additions to CreateTaskActivity.java
 */
/*
 * Added a call to insert the estimation Value into database
 *      when the user presses btnAddProjectToDB.
 */
public class CreateTaskActivity extends Activity
{
	EditText etSetProjectTitle, etSetProjectPLanguages, etSetProjectDescription;
	Button btnAddProjectToDB, btnCreateTaskToHome, btnCreateTaskToSelectTask;
	
	DBHelperAdapter dBHelperAdapter;
	
	Spinner spEstimation;
	ArrayAdapter<CharSequence> aaEstimation;
	String projectTitle, programmingLanguages, projectDescription, mEstimationValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		
		etSetProjectTitle = (EditText) findViewById(R.id.etSetProjectTitle);
		etSetProjectPLanguages = (EditText) findViewById(R.id.etSetProjectPLanguages);
		etSetProjectDescription = (EditText) findViewById(R.id.etSetProjectDescription);
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		
		spEstimation = (Spinner) findViewById(R.id.spTimeEstimation);
		aaEstimation = ArrayAdapter.createFromResource(this,
				R.array.estimationValues,
				android.R.layout.simple_spinner_item);
		aaEstimation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spEstimation.setAdapter(aaEstimation);
		spEstimation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				mEstimationValue = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				mEstimationValue = parent.getItemAtPosition(0).toString();
			}
		});
		
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
						projectDescription, mEstimationValue);

				
				//TODO: see top of page
				/**
				 * Added a call to insert the estimation Value into database
				 *      when the user presses btnAddProjectToDB.
				 */
//				dBHelperAdapter.insert_EstimationValue(projectTitle,mEstimationValue);
				
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
