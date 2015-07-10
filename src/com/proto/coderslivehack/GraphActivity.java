package com.proto.coderslivehack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class GraphActivity extends Activity
{
	Spinner spFilterProgrammingLanguage;
	ArrayList<String> alFilterProgrammingLanguage;
	ArrayAdapter<String> aaFilterProgrammingLanguage;
	Button btnGraphToHome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		generateGraph();
		
		spFilterProgrammingLanguage = (Spinner) findViewById(R.id.spFilterProgrammingLanguage);
		alFilterProgrammingLanguage = new ArrayList<String>();
		alFilterProgrammingLanguage.add("item 1");
		alFilterProgrammingLanguage.add("item 2");
		aaFilterProgrammingLanguage = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, alFilterProgrammingLanguage);
		aaFilterProgrammingLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spFilterProgrammingLanguage.setAdapter(aaFilterProgrammingLanguage);
		spFilterProgrammingLanguage.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(parent.getContext(),
						"Selected : " + parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
				// TODO:
				// each time select from spinner generateGraph()
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		
		btnGraphToHome = (Button) findViewById(R.id.btnGraphToHome);
		btnGraphToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent graphToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(graphToHomeIntent);
				finish();
			}
		});
		
		// TODO:
		// horizontal double slider,
		// see my unsorted bookmark
	}
	
	// TODO:
	// generate graph, should take parameter come from spinner and seekbar, default is all data
	public void generateGraph()
	{
		
	}
}
