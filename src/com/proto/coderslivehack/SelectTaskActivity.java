package com.proto.coderslivehack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	CustomArrayAdapter customArrayAdapter;
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
		String strAllData = dBHelperAdapter.getAllData();
//		String strAllData = dBHelperAdapter.getAllDataNotComplete();
		final String[] arrAllData = strAllData.split("\\n");
		alProjectList = new ArrayList<String>(Arrays.asList(arrAllData));
		
		lvProjectList = (ListView) findViewById(R.id.lvProjectList);
		if(!alProjectList.isEmpty())
		{
			customArrayAdapter = new CustomArrayAdapter(getApplicationContext(), alProjectList);
		}
		else
		{
			customArrayAdapter = new CustomArrayAdapter(getApplicationContext(), Arrays.asList("list is empty"));
		}
		lvProjectList.setAdapter(customArrayAdapter);
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
			    selectedTask = alProjectList.get(position);
			    Toast.makeText(getApplicationContext(), "Items " +  selectedTask, Toast.LENGTH_SHORT).show();
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
	
	class CustomArrayAdapter extends ArrayAdapter<String>
	{
		List<String> lProjectList;

		// use to pass in parameter if it is a outer class, super() is require.
		// if it is inner class can be default constructor, super() is require.
		CustomArrayAdapter(Context context, List<String> lProjectList)
		{
			super(context, R.layout.select_list_item, lProjectList);
			this.lProjectList = lProjectList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// row is the view that we create with xml and going to access them here,
			// convertView is just the saved view of row if already loaded in the current pagination,
			// I did not use pagination because this list will not grow large, 
			// may be less than 10 item per year using this app.
			View row;
			// convertView is nice way to save memory for very large list by reuse those already loaded views
			// it is a standard way now, but still optional
			if (convertView == null)
			{
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.select_list_item, parent, false);
			}
			else
			{
				row = convertView;
			}
			
			String selectedRow = lProjectList.get(position);
			String curProjTitle = selectedRow.split("\\|")[1];
			String curProjLanguage = selectedRow.split("\\|")[2];
			String curProjDescription = selectedRow.split("\\|")[3];
			String curProjStartDate = selectedRow.split("\\|")[4];
			String curProjTimeSpent = selectedRow.split("\\|")[5];
			String curProjGrade = selectedRow.split("\\|")[6];
			String curProjDistraction = selectedRow.split("\\|")[7];
			String curProjProgress = selectedRow.split("\\|")[8];
			
			if(curProjProgress.equalsIgnoreCase("completed") || curProjProgress.equalsIgnoreCase("abandoned"))
			{
				if(!curProjGrade.equalsIgnoreCase("null") && !curProjDistraction.equalsIgnoreCase("null"))
				{
					row.setBackgroundColor(Color.GREEN);
				}
				else
				{
					row.setBackgroundColor(Color.YELLOW);
				}
			}
			else
			{
				row.setBackgroundColor(Color.RED);
			}
			
			TextView tvCLIProjectTile = (TextView) row.findViewById(R.id.tvCLIProjectTile);
			tvCLIProjectTile.setText(curProjTitle);
			TextView tvCLILanguage = (TextView) row.findViewById(R.id.tvCLILanguage);
			tvCLILanguage.setText(curProjLanguage);
			TextView tvCLIStartDate = (TextView) row.findViewById(R.id.tvCLIStartDate);
			tvCLIStartDate.setText(curProjStartDate);
			TextView tvCLITimeSpent = (TextView) row.findViewById(R.id.tvCLITimeSpent);
			tvCLITimeSpent.setText(curProjTimeSpent);
			TextView tvCLIDescription = (TextView) row.findViewById(R.id.tvCLIDescription);
			tvCLIDescription.setText(curProjDescription);
			
			return row;
		}
	}
	
	public boolean isCompleteOrAbandon()
	{
		String projectTitle = selectedTask.split(" ")[1];
		String projectProgress = dBHelperAdapter.getProjectProgress(projectTitle);
		return (projectProgress.equalsIgnoreCase("completed") || projectProgress.equalsIgnoreCase("abandoned"));
	}
}
