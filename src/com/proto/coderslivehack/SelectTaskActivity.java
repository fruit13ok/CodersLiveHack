package com.proto.coderslivehack;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectTaskActivity extends Activity
{
	ArrayList<String> alProjectList = new ArrayList<String>();
	ArrayAdapter<String> aaProjectList;
	ListView lvProjectList;
	Button btnSelectProjectToWork, btnSelectTaskToHome, btnSelectTaskToDummyUpdate;
	
	DBHelperAdapter dBHelperAdapter;
	
	public final static String EXTRA_SELECTED_TASK = "selected project for timer";
	String selectedTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_task);
		
		selectedTask = "";
		
		// populate the alProjectList by query the database
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
//		String strAllData = dBHelperAdapter.getAllData();
		String strAllData = dBHelperAdapter.getAllDataNotComplete();
		final String[] arrAllData = strAllData.split("\\n");
		alProjectList = new ArrayList<String>(Arrays.asList(arrAllData));
		
		lvProjectList = (ListView) findViewById(R.id.lvProjectList);
		if(!alProjectList.isEmpty())
		{
			aaProjectList = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, 
					alProjectList);
		}
		else
		{
			aaProjectList = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, 
//					new ArrayList<String>(){{add("default project 1"); add("default project 2");}});
					Arrays.asList("default project 1", "default project 2"));
		}
		lvProjectList.setAdapter(aaProjectList);
		lvProjectList.setOnItemClickListener(new OnItemClickListener()
		{
			// 4 arguments
			// parent:	The AdapterView/ListView where the click happened
			// view:		The view within the ListView that was clicked
			// position:	The position of the view in the list
			// id:		The row id of the item that was clicked
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(), "clicked "+((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			    selectedTask = ((TextView)view).getText().toString();
			}
		});
		
		btnSelectProjectToWork = (Button) findViewById(R.id.btnSelectProjectToWork);
		btnSelectProjectToWork.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO:
				// intent to TimerActivity, check pass string first,
				// if user not select any task or selected the completed task toast to use.
				// for now only select incomplete task can go to timer, (talk to Joel to confirm).
				Intent selectTaskToTimerIntent = new Intent(getBaseContext(), TimerActivity.class);
				// TODO: after most this app done, should not allow user go to timer without select valid project.
				if(selectedTask.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "no project select", Toast.LENGTH_LONG).show();
//					selectTaskToTimerIntent.putExtra(EXTRA_SELECTED_TASK, "no project select");
//					startActivity(selectTaskToTimerIntent);
//					finish();
				}
//				else if(isCompleteOrAbandon())
//				{
//					// later just toast
//					selectTaskToTimerIntent.putExtra(EXTRA_SELECTED_TASK, "project has complete or abandon");
//					startActivity(selectTaskToTimerIntent);
//					finish();
//				}
				else
				{
					selectTaskToTimerIntent.putExtra(EXTRA_SELECTED_TASK, selectedTask);
					startActivity(selectTaskToTimerIntent);
					finish();
				}
			}
		});
		
		btnSelectTaskToHome = (Button) findViewById(R.id.btnSelectTaskToHome);
		btnSelectTaskToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent selectTaskToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(selectTaskToHomeIntent);
				finish();
			}
		});
		
		btnSelectTaskToDummyUpdate = (Button) findViewById(R.id.btnSelectTaskToDummyUpdate);
		btnSelectTaskToDummyUpdate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent selectTaskToDummyUpdateIntent = new Intent(getBaseContext(), DummyUpdateActivity.class);
				startActivity(selectTaskToDummyUpdateIntent);
				finish();
			}
		});
	}
	
//	public boolean isCompleteOrAbandon()
//	{
//		String projectTitle = selectedTask.split(" ")[1];
//		String projectProgress = dBHelperAdapter.getProjectProgress(projectTitle);
//		return (projectProgress.equalsIgnoreCase("completed") || projectProgress.equalsIgnoreCase("abandoned"));
//	}
}
