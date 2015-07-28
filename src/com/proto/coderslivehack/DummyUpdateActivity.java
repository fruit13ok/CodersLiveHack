package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DummyUpdateActivity extends Activity
{
	String id, title, language, description, startTime, timeSpent, productivie, distraction;
	
	DBHelperAdapter dBHelperAdapter;
	
	EditText etId, etTitle, etLanguage, etDescription, etStartTime, etTimeSpent, etProductivie, etDistraction;
	Button btnUpdate, btnDummyUpdateToHome, btnShowRow;
	
	TextView tvLookingAtThisRow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_update);
		
		id = "";
		title = "";
		language = "";
		description = "";
		startTime = "";
		timeSpent = "";
		productivie = "";
		distraction = "";
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		
		etId = (EditText) findViewById(R.id.etId);
		etTitle = (EditText) findViewById(R.id.etDummyStartDate);
		etLanguage = (EditText) findViewById(R.id.etDummyGrade);
		etDescription = (EditText) findViewById(R.id.etDescription);
		etStartTime = (EditText) findViewById(R.id.etStartTime);
		etTimeSpent = (EditText) findViewById(R.id.etTimeSpent);
		etProductivie = (EditText) findViewById(R.id.etProductivie);
		etDistraction = (EditText) findViewById(R.id.etDistraction);
		
		tvLookingAtThisRow = (TextView) findViewById(R.id.tvLookingAtThisRow);
		
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				id = etId.getText().toString();
				title = etTitle.getText().toString();
				language = etLanguage.getText().toString();
				description = etDescription.getText().toString();
				startTime = etStartTime.getText().toString();
				timeSpent = etTimeSpent.getText().toString();
				productivie = etProductivie.getText().toString();
				distraction = etDistraction.getText().toString();
				
				int count = dBHelperAdapter.updateARowById(id, title, language, description, 
						startTime, timeSpent, productivie, distraction);
				Toast.makeText(getApplicationContext(), count + " row updated, id = " + id, Toast.LENGTH_SHORT).show();
			}
		});
		
		btnDummyUpdateToHome = (Button) findViewById(R.id.btnDummyUpdateToHome);
		btnDummyUpdateToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent dummyUpdateToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(dummyUpdateToHomeIntent);
				finish();
			}
		});
		
		btnShowRow = (Button) findViewById(R.id.btnShowRow);
		btnShowRow.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				id = etId.getText().toString();
				String str = dBHelperAdapter.getDataById(id);
				tvLookingAtThisRow.setText(str);
			}
		});
	}
}
