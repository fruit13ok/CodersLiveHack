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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * Replace old AssessActivity.java with this new version I created.
 */
public class AssessActivity extends Activity
{
	
	Spinner spDistraction, spProductivity;
	EditText etTaskComments;
	ArrayAdapter<CharSequence> aaDistraction;
	ArrayAdapter<CharSequence> aaProductivity;
    String mDistraction_value, mProductivity_value;
	Button btnSubmitToDB, btnAssessToHome, btnAssessToGraph;
    DBHelperAdapter mDBHelperAdapter;
    String mCurrProject;
	String[] projectInfo;
	String mTaskCommentsValues;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assess);

        mDBHelperAdapter = new DBHelperAdapter(getApplicationContext());

        //Get the current name of the project for inserting values into the database
        Intent fromTimerTask = getIntent();
        mCurrProject = fromTimerTask.getStringExtra("SELECTED_PROJECT");
		projectInfo = mCurrProject.split(" ");


		StringBuilder stringBuilder = new StringBuilder();
        String programLanguage = "java";
        String value = "";
        int index = 1;

		//Currently chopping off rest of string based on programming language,
		//add note: add unique char between project "name"
		//known bug if user does not select task but starts self-assessment program will crash

		while (value.compareToIgnoreCase(programLanguage) != 0)
		{
			stringBuilder.append(" " + value);
			value = projectInfo[index].toString();
			index++;
		}

		//the users current project name
		mCurrProject = stringBuilder.toString().trim();


		spDistraction = (Spinner) findViewById(R.id.spDistraction);
		aaDistraction = ArrayAdapter.createFromResource(this,
				R.array.distractionValues,
				android.R.layout.simple_spinner_item);

		aaDistraction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDistraction.setAdapter(aaDistraction);

		spDistraction.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mDistraction_value = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mDistraction_value = parent.getItemAtPosition(0).toString();
			}
		});



		spProductivity = (Spinner) findViewById(R.id.spProductivity);
		aaProductivity = ArrayAdapter.createFromResource(this,
				R.array.productivityValues,
				android.R.layout.simple_spinner_item);

		aaProductivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spProductivity.setAdapter(aaProductivity);

		spProductivity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				mProductivity_value = parent.getItemAtPosition(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				mProductivity_value = parent.getItemAtPosition(0).toString();

			}
		});

		//Reference etTaskComments edit text
		etTaskComments = (EditText) findViewById(R.id.etTaskComments);


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
				// user must submit the assessment to database before go else where,

				//Submit Distraction and Productivity values to database
				mDBHelperAdapter.insert_DistractionLevel(mCurrProject,mDistraction_value);
				mDBHelperAdapter.insert_ProductivityValues(mCurrProject, mProductivity_value);

				mTaskCommentsValues = etTaskComments.getText().toString().trim();

				mDBHelperAdapter.insert_TaskComments(mCurrProject,mTaskCommentsValues);

				Toast.makeText(getApplicationContext(),
						"Dis Val entered into DB is : " + mDBHelperAdapter.get_DistractionLevel(mCurrProject),
						Toast.LENGTH_SHORT).show();

				Toast.makeText(getApplicationContext(),
						"Prod Val entered into DB is : " + mDBHelperAdapter.get_ProductivityLevel(mCurrProject),
						Toast.LENGTH_SHORT).show();

				Toast.makeText(getApplicationContext(),
						"Task Comment Value entered into DB : " + mDBHelperAdapter.get_TaskComment(mCurrProject),
						Toast.LENGTH_SHORT).show();

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
	
/*
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
*/
}
