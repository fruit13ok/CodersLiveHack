package com.proto.coderslivehack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GetGitCommitActivity extends Activity
{
	EditText etGithubAccount, etGithubRepository;
	TextView tvNumOfGithobCommit;
	Button btnGetNumOfCommit, btnNumGitCommitToHome;
	
//	private static String URL = "https://api.github.com/repos/fruit13ok/CodersLiveHack/commits";
	private static String URL = "";
	
	ConnectivityManager cm;
	NetworkInfo activeNetwork;
	boolean isConnected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTitle("Number of Github Commits");
		getActionBar().setIcon(R.drawable.ic_action_git2);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
		
		// setStatusBar need api 21 now use 14
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.parseColor("#303F9F"));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_git_commit);
		
//		cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//		activeNetwork = cm.getActiveNetworkInfo();
//		isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		etGithubAccount = (EditText) findViewById(R.id.etGithubAccount);
		etGithubRepository = (EditText) findViewById(R.id.etGithubRepository);
		
		tvNumOfGithobCommit = (TextView) findViewById(R.id.tvNumOfGithobCommit);
		
		btnGetNumOfCommit = (Button) findViewById(R.id.btnGetNumOfCommit);
		btnGetNumOfCommit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// these 3 lines plus this 1 line in manifest file to check connectivity.
				// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
				// only try connect to github if have connection.
				cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				activeNetwork = cm.getActiveNetworkInfo();
				isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
				
				if(isConnected)
				{
					// this URL return a JSONArray, so use JSONArray to store it
					// !!! CAREFUL !!! if remove the last part /commits, it will return JSONObject
					URL = "https://api.github.com/repos/" + 
							etGithubAccount.getText().toString() + "/" + 
							etGithubRepository.getText().toString() + "/commits";
//					new HttpGetTask().execute();
					new HttpGetTask().execute(URL);
				}
				else
				{
					Toast.makeText(getApplicationContext(), 
							"Need internet connection, please turn on data or wifi", 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnNumGitCommitToHome = (Button) findViewById(R.id.btnNumGitCommitToHome);
		btnNumGitCommitToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent numGitCommitToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(numGitCommitToHomeIntent);
				finish();
			}
		});
	}
	
//	private class HttpGetTask extends AsyncTask<Void, Void, String>
	private class HttpGetTask extends AsyncTask<String, Void, String>
	{
		private static final String TAG = "HttpGetTask";
		
		@Override
//		protected String doInBackground(Void... params)
		protected String doInBackground(String... params)
		{
			String strData = "";
			HttpURLConnection httpUrlConnection = null;

			try
			{
//				httpUrlConnection = (HttpURLConnection) new URL(URL).openConnection();
				httpUrlConnection = (HttpURLConnection) new URL(params[0]).openConnection();

				InputStream in = new BufferedInputStream(httpUrlConnection.getInputStream());

				strData = readStream(in);

			}
			catch (MalformedURLException exception)
			{
				Log.e(TAG, "MalformedURLException");
			}
			catch (IOException exception)
			{
				Log.e(TAG, "IOException");
			}
			finally
			{
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return strData;
		}

		@Override
		protected void onPostExecute(String mStrData)
		{
			tvNumOfGithobCommit.setText("" + parseNumOfCommit(mStrData));
		}

		private String readStream(InputStream in)
		{
			BufferedReader reader = null;
			StringBuffer strbData = new StringBuffer("");
			try
			{
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null)
				{
					strbData.append(line);
				}
			}
			catch (IOException e)
			{
				Log.e(TAG, "IOException");
			}
			finally
			{
				if (reader != null)
				{
					try
					{
						reader.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			return strbData.toString();
		}
	}
	
	public int parseNumOfCommit(String myStrData)
	{
		JSONArray ja = null;
		try
		{
			ja = new JSONArray(myStrData);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		System.out.println("mytab: length = " + ja.length());
//		for (int i = 0; i < ja.length(); i++)
//		{
//	        JSONObject explrObject = ja.getJSONObject(i);
//		}
		return ja.length();
	}
}
