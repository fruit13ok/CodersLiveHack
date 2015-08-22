package com.proto.coderslivehack;

import java.util.ArrayList;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This page is important for produce graph data.<br>
 * It input final grade, and distraction level of each finished project into database.
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
		setTitle("Self-Assessment");
		getActionBar().setIcon(R.drawable.ic_action_assess);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
		
		// setStatusBar need api 21 now use 14
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.parseColor("#303F9F"));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assess);

        mDBHelperAdapter = new DBHelperAdapter(getApplicationContext());

        //Get the current name of the project for inserting values into the database
        Intent fromTimerTask = getIntent();
        mCurrProject = fromTimerTask.getStringExtra("SELECTED_PROJECT");
		projectInfo = mCurrProject.split("\\|");
		
		mCurrProject = projectInfo[1];

		spDistraction = (Spinner) findViewById(R.id.spDistraction);
		aaDistraction = ArrayAdapter.createFromResource(this,
				R.array.distractionValues,
				android.R.layout.simple_spinner_item);

		aaDistraction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDistraction.setAdapter(aaDistraction);

		spDistraction.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				mDistraction_value = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				mDistraction_value = parent.getItemAtPosition(0).toString();
			}
		});

		spProductivity = (Spinner) findViewById(R.id.spProductivity);
		aaProductivity = ArrayAdapter.createFromResource(this,
				R.array.productivityValues,
				android.R.layout.simple_spinner_item);

		aaProductivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spProductivity.setAdapter(aaProductivity);

		spProductivity.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				mProductivity_value = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				mProductivity_value = parent.getItemAtPosition(0).toString();
			}
		});

		etTaskComments = (EditText) findViewById(R.id.etTaskComments);
		btnAssessToHome = (Button) findViewById(R.id.btnAssessToHome);
		btnAssessToGraph = (Button) findViewById(R.id.btnAssessToGraph);

		if(mDBHelperAdapter.isGraphableDataExists())
		{
			System.out.println("mytab: graph data exist");
			btnAssessToGraph.setEnabled(true);
		}
		else
		{
			System.out.println("mytab: graph data not exist");
			btnAssessToGraph.setEnabled(false);
		}
		
		btnSubmitToDB = (Button) findViewById(R.id.btnSubmitToDB);
		btnSubmitToDB.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// user must submit the assessment to database before go else where,
				mDBHelperAdapter.insert_DistractionLevel(mCurrProject,mDistraction_value);
				mDBHelperAdapter.insert_ProductivityValues(mCurrProject, mProductivity_value);

				mTaskCommentsValues = etTaskComments.getText().toString().trim();

				mDBHelperAdapter.insert_TaskComments(mCurrProject,mTaskCommentsValues);

				//TODO: delete test later
				Toast.makeText(getApplicationContext(),
						"Dis Val entered into DB is : " + mDBHelperAdapter.get_DistractionLevel(mCurrProject),
						Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(),
						"Prod Val entered into DB is : " + mDBHelperAdapter.get_ProductivityLevel(mCurrProject),
						Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(),
						"Task Comment Value entered into DB : " + mDBHelperAdapter.get_TaskComment(mCurrProject),
						Toast.LENGTH_SHORT).show();

				btnAssessToGraph.setEnabled(true);
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
