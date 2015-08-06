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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetGitCommitActivity extends Activity
{
	EditText etGithubAccount, etGithubRepository;
	TextView tvNumOfGithobCommit;
	Button btnGetNumOfCommit, btnNumGitCommitToHome;
	
//	private static String URL = "https://api.github.com/repos/fruit13ok/CodersLiveHack/commits";
	private static String URL = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_git_commit);
		
		etGithubAccount = (EditText) findViewById(R.id.etGithubAccount);
		etGithubRepository = (EditText) findViewById(R.id.etGithubRepository);
		
		tvNumOfGithobCommit = (TextView) findViewById(R.id.tvNumOfGithobCommit);
		
		btnGetNumOfCommit = (Button) findViewById(R.id.btnGetNumOfCommit);
		btnGetNumOfCommit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// this URL return a JSONArray, so use JSONArray to store it
				// !!! CAREFUL !!! if remove the last part /commits, it will return JSONObject
				URL = "https://api.github.com/repos/" + 
						etGithubAccount.getText().toString() + "/" + 
						etGithubRepository.getText().toString() + "/commits";
//				new HttpGetTask().execute();
				new HttpGetTask().execute(URL);
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
