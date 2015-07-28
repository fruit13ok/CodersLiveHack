package com.proto.coderslivehack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// TODO: implement display time in hour : minute : second
public class TimerActivity extends Activity
{
	String selectedProject;
	TextView tvSelectedProject, tvWorkOrBreakTime, tvTimer;
	Button btnStartTimer, btnPauseTimer, btnResumeTimer, btnStopTimer, 
		btnMarkProjectComplete, btnMarkProjectAbandon, btnTimerToHome, btnTimerToAssess;
	
	long countDownInterval, workTime, breakTime, workTimeRemain, accumulatedTimeSpent;

	//Declare a variable to hold count down timer's paused status
    private boolean isPaused, isCanceled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		Intent fromSelectTaskIntent = getIntent();
		selectedProject = fromSelectTaskIntent.getStringExtra(SelectTaskActivity.EXTRA_SELECTED_TASK);
		
		tvSelectedProject = (TextView) findViewById(R.id.tvSelectedProject);
		tvSelectedProject.setText(selectedProject);
		tvWorkOrBreakTime = (TextView) findViewById(R.id.tvWorkOrBreakTime);
		tvWorkOrBreakTime.setText("Not start work yet");
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		
		btnStartTimer = (Button) findViewById(R.id.btnStartTimer);
		btnPauseTimer = (Button) findViewById(R.id.btnPauseTimer);
		btnResumeTimer = (Button) findViewById(R.id.btnResumeTimer);
		btnStopTimer = (Button) findViewById(R.id.btnStopTimer);
		btnMarkProjectComplete = (Button) findViewById(R.id.btnMarkProjectComplete);
		btnMarkProjectAbandon = (Button) findViewById(R.id.btnMarkProjectAbandon);
		btnTimerToHome = (Button) findViewById(R.id.btnTimerToHome);
		btnTimerToAssess = (Button) findViewById(R.id.btnTimerToAssess);
		
        workTime = 25000;
        breakTime = 5000;
        countDownInterval = 1000;
        workTimeRemain = 0;
        accumulatedTimeSpent = 0;
        
        //Init: not pause, not cancel flags, enable start, disabled pause, resume, and cancel buttons
        setButtonAndFlagState(false, false, true, false, false, false);
        
        btnStartTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setButtonAndFlagState(false, false, false, true, false, true);

                setWorkTimer(workTime, countDownInterval);
			}
		});
        
        btnPauseTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setButtonAndFlagState(true, false, false, false, true, true);
			}
		});
        
        btnResumeTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setButtonAndFlagState(false, false, false, true, false, true);

                // use the remaining timer length
                setWorkTimer(workTimeRemain, countDownInterval);
			}
		});
        
        btnStopTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setButtonAndFlagState(false, true, true, false, false, false);

                //Notify the user that CountDownTimer is canceled/stopped
				tvTimer.setText("CountDownTimer Canceled/stopped.");
			}
		});
        
        // TODO: do mark project as complete to database, may be the accumulation time spent too
        btnMarkProjectComplete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
			}
		});
        
        // TODO: do mark project as abandon to database, may be the accumulation time spent too
        btnMarkProjectAbandon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
			}
		});
        
		btnTimerToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent timerToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(timerToHomeIntent);
				finish();
			}
		});
		
		btnTimerToAssess.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent timerToAssessIntent = new Intent(getBaseContext(), AssessActivity.class);
				startActivity(timerToAssessIntent);
				finish();
			}
		});
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////// 2 count down timers //////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Timer will start when click start, or resume.<br>
	 * Parameters are count duration and how fast it should tick. <br>
	 * Example:<br>
	 * millisInFuture = 30000 milliseconds (30 seconds), countDownInterval = 1000 (1 seconds).
	 * <br><br>
	 * Timer will cancel when click pause or cancel.<br>
	 * 
	 * @param mWorkTime
	 * @param mCountDownInterval
	 */
	public void setWorkTimer(long mWorkTime, long mCountDownInterval)
	{
		//Initialize a new CountDownTimer instance, ex for 30 seconds 1 second per tick
        new CountDownTimer(mWorkTime,mCountDownInterval)
        {
        	// millisUntilFinished is pass from millisInFuture
            public void onTick(long millisUntilFinished)
            {
                //do something in every tick
                if(isPaused || isCanceled || accumulatedTimeSpent >= 50)
                {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                }
                else 
                {
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                	tvTimer.setText("" + millisUntilFinished / 1000);
                    //Put count down timer remaining time in a variable
                	workTimeRemain = millisUntilFinished;
                	accumulatedTimeSpent++;
                	tvWorkOrBreakTime.setText("You are working ... this section worked: " + 
                			accumulatedTimeSpent + " seconds");
                }
            }
            public void onFinish()
            {
            	// this makes infinite CountDownTimer until user pause or cancel/stop
            	// WARNING: will this cause resource drink or memory leak,
            	// may be but will not going to be a lot because:
            	// EX:
            	// a pomodoro section total 4 hours, 4 cycle each 1 hour; then there are only 4 call.
            	// also CountDownTimer is not a service will close if activity go to background.
            	btnStartTimer.setEnabled(false);
            	btnPauseTimer.setEnabled(false);
            	btnResumeTimer.setEnabled(false);
            	btnStopTimer.setEnabled(true);
                setBreakTimer(breakTime, countDownInterval);
            }
        }.start();
	}
	
	/**
	 * Timer will not allow to pause but can be cancel
	 * @param mBreakTime
	 * @param ncountDownInterval
	 */
	public void setBreakTimer(long mBreakTime, long ncountDownInterval)
	{
		new CountDownTimer(mBreakTime,ncountDownInterval)
        {
            public void onTick(long millisUntilFinished)
            {
                if(isCanceled || accumulatedTimeSpent >= 50)
                {
                    cancel();
                }
                else 
                {
                	tvTimer.setText("" + millisUntilFinished / 1000);
                	tvWorkOrBreakTime.setText("You are taking a break ...");
                }
            }
            public void onFinish()
            {
            	btnStartTimer.setEnabled(false);
            	btnPauseTimer.setEnabled(true);
            	btnResumeTimer.setEnabled(false);
            	btnStopTimer.setEnabled(true);
                setWorkTimer(workTime, countDownInterval);
            }
        }.start();
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////// boolean flags for buttons ///////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Specify the current state for enable or disable button, isPaused, and isCanceled.
	 * @param isPaused
	 * @param isCanceled
	 * @param startable
	 * @param pauseble
	 * @param resumeble
	 * @param cancelable
	 */
	public void setButtonAndFlagState(boolean isPaused, boolean isCanceled, 
			boolean startable, boolean pauseble, boolean resumeble, boolean cancelable)
	{
        this.isPaused = isPaused;
        this.isCanceled = isCanceled;
        
		btnStartTimer.setEnabled(startable);
        btnPauseTimer.setEnabled(pauseble);
        btnResumeTimer.setEnabled(resumeble);
        btnStopTimer.setEnabled(cancelable);
	}
}
