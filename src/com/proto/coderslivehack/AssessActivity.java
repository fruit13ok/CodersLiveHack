package com.proto.coderslivehack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AssessActivity extends Activity
{
	Spinner spProductivity, spDistraction, spDefficulty;
	ArrayList<String> alProductivity, alDistraction, alDefficulty;
	ArrayAdapter<String> aaProductivity, aaDistraction, aaDefficulty;
	Button btnSubmitToDB, btnAssessToHome, btnAssessToGraph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assess);
		
		spProductivity = (Spinner) findViewById(R.id.spProductivity);
		alProductivity = new ArrayList<String>();
		alProductivity.add("item 1");
		alProductivity.add("item 2");
		aaProductivity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alProductivity);
		aaProductivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spProductivity.setAdapter(aaProductivity);
		spProductivity.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(parent.getContext(),
						"Selected : " + parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		spDistraction = (Spinner) findViewById(R.id.spDistraction);
		alDistraction = new ArrayList<String>();
		alDistraction.add("item 1");
		alDistraction.add("item 2");
		aaDistraction = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alDistraction);
		aaDistraction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDistraction.setAdapter(aaDistraction);
		spDistraction.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(parent.getContext(),
						"Selected : " + parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		spDefficulty = (Spinner) findViewById(R.id.spDefficulty);
		alDefficulty = new ArrayList<String>();
		alDefficulty.add("item 1");
		alDefficulty.add("item 2");
		aaDefficulty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alDefficulty);
		aaDefficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDefficulty.setAdapter(aaDefficulty);
		spDefficulty.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(parent.getContext(),
						"Selected : " + parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		
		btnAssessToHome = (Button) findViewById(R.id.btnAssessToHome);
		btnAssessToHome.setVisibility(View.GONE);
		
		btnAssessToGraph = (Button) findViewById(R.id.btnAssessToGraph);
		btnAssessToGraph.setVisibility(View.GONE);
		
		btnSubmitToDB = (Button) findViewById(R.id.btnSubmitToDB);
		btnSubmitToDB.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO:
				// user must submit the assessment to database before go else where,
				btnAssessToHome.setVisibility(View.VISIBLE);
				btnAssessToGraph.setVisibility(View.VISIBLE);
			}
		});
		btnAssessToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent assessToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(assessToHomeIntent);
				finish();
			}
		});
		btnAssessToGraph.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent assessToGraphIntent = new Intent(getBaseContext(), GraphActivity.class);
				startActivity(assessToGraphIntent);
				finish();
			}
		});
	}
}
