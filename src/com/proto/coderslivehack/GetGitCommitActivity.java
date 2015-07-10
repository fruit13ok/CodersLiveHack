package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
				// TODO:
				tvNumOfGithobCommit.setText("lots of commit from " + 
						etGithubAccount.getText().toString() + "/" + 
						etGithubRepository.getText().toString());
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
}
