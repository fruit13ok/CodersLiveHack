package com.proto.coderslivehack;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// TODO: test and debug display time in hour : minute : second
// after I implemented to display hour : minute : second, 
// it appear that accumulated work time count faster than countdowntimer.
// one way to cheat the solution is only display hour : minute, 
// set countDownInterval to every minute instead of second, hopefully cut off extra second, 
// I am not sure, just wait for debug.

/*
 * I made one change to TimerActivity.java. I added to the method:
 *
 *      btnTimerToAssess.setOnClickListener(new OnClickListener()
 *
 * You can copy and replace the method. I just  added that when the button is pressed
 *      it sends the AsssessActivity.java class the current project name.
 */
public class TimerActivity extends Activity
{
	String selectedProject;
	TextView tvSelectedProject, tvWorkOrBreakTime, tvTimer;
	Button btnStartTimer, btnPauseTimer, btnResumeTimer, btnStopTimer, 
		btnMarkProjectComplete, btnMarkProjectAbandon, btnTimerToHome, btnTimerToAssess;
	
	long countDownInterval, workTime1, workTime2, breakTime1, breakTime2, workTimeRemain;
	/**
	 * <b>accumulatedTimeSpent</b> and <b>stoppingPoint</b> measure in second <b><ins><i>NOT</i></ins></b> millisecond
	 */
	long accumulatedTimeSpent, stoppingPoint;

	//Declare a variable to hold count down timer's paused status
    private boolean isPaused, isCanceled;
	
    // 8 icon for start, pause, resume, stop
    Drawable drawableStart, drawableStartSubdued, drawablePause, drawablePauseSubdued,
    	drawableResume, drawableResumeSubdued, drawableStop, drawableStopSubdued;
    
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
		
		drawableStart = getResources().getDrawable(R.drawable.ic_start);
		drawableStartSubdued = getResources().getDrawable(R.drawable.ic_start_subdued);
		drawablePause = getResources().getDrawable(R.drawable.ic_pause);
		drawablePauseSubdued = getResources().getDrawable(R.drawable.ic_pause_subdued);
    	drawableResume = getResources().getDrawable(R.drawable.ic_resume);
    	drawableResumeSubdued = getResources().getDrawable(R.drawable.ic_resume_subdued);
    	drawableStop = getResources().getDrawable(R.drawable.ic_stop);
    	drawableStopSubdued = getResources().getDrawable(R.drawable.ic_stop_subdued);
		
		btnStartTimer = (Button) findViewById(R.id.btnStartTimer);
		btnPauseTimer = (Button) findViewById(R.id.btnPauseTimer);
		btnResumeTimer = (Button) findViewById(R.id.btnResumeTimer);
		btnStopTimer = (Button) findViewById(R.id.btnStopTimer);
		
		btnMarkProjectComplete = (Button) findViewById(R.id.btnMarkProjectComplete);
		btnMarkProjectAbandon = (Button) findViewById(R.id.btnMarkProjectAbandon);
		
		btnTimerToHome = (Button) findViewById(R.id.btnTimerToHome);
		btnTimerToAssess = (Button) findViewById(R.id.btnTimerToAssess);
		
        workTime1 = 65 * 1000;			//25 min, 25 * 60 * 1000
        workTime2 = 50 * 60 * 1000;		//50 min
        breakTime1 = 5 * 1000;			//5 min
        breakTime2 = 10 * 60 * 1000;	//10 min
        countDownInterval = 1000;
        workTimeRemain = 0;
        accumulatedTimeSpent = 0;		//!!! this is measure in second not millisecond
        stoppingPoint = 120;			//!!! this is measure in second not millisecond
        
        //Init: not pause, not cancel flags, enable start, disabled pause, resume, and cancel buttons
        setButtonAndFlagState(false, false, true, false, false, false);
        
        btnStartTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				btnStartTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStartSubdued, null);

				setButtonAndFlagState(false, false, false, true, false, true);

                setWorkTimer(workTime1, countDownInterval);
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
				timerToAssessIntent.putExtra("SELECTED_PROJECT",selectedProject);
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
        	// millisUntilFinished is pass from millisInFuture / mWorkTime
            public void onTick(long millisUntilFinished)
            {
                //do something in every tick
                if(isPaused || isCanceled || accumulatedTimeSpent >= stoppingPoint)
                {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                }
                else 
                {
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
//                	tvTimer.setText("" + millisUntilFinished / 1000);
                	long cdInMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                	long cdInSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                	long cdRemainSecond = cdInSeconds - TimeUnit.MINUTES.toSeconds(cdInMinutes);
                	tvTimer.setText("" + String.format(Locale.US, 
                			"%d min : %d sec", 
                			cdInMinutes, cdRemainSecond));
                	
                    //Put count down timer remaining time in a variable
                	workTimeRemain = millisUntilFinished;
                	accumulatedTimeSpent++;
                	long atsInHours = TimeUnit.SECONDS.toHours(accumulatedTimeSpent);
                	long atsInMinutes = TimeUnit.SECONDS.toMinutes(accumulatedTimeSpent);
                	long atsInSeconds = TimeUnit.SECONDS.toSeconds(accumulatedTimeSpent);
                	long atsRemainMinute = atsInMinutes - TimeUnit.HOURS.toMinutes(atsInHours);
                	long atsRemainSecond = atsInSeconds - TimeUnit.HOURS.toSeconds(atsInHours) - 
                			TimeUnit.MINUTES.toSeconds(atsRemainMinute);
                	tvWorkOrBreakTime.setText("You are working ... this section worked: " + 
                			String.format(Locale.US, 
                        			"%d hr : %d min : %d sec", 
                        			atsInHours, atsRemainMinute, atsRemainSecond));
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
                setBreakTimer(breakTime1, countDownInterval);
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
                if(isCanceled || accumulatedTimeSpent >= stoppingPoint)
                {
                    cancel();
                }
                else 
                {
//                	tvTimer.setText("" + millisUntilFinished / 1000);
                	long cdInMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                	long cdInSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                	long cdRemainSecond = cdInSeconds - TimeUnit.MINUTES.toSeconds(cdInMinutes);
                	tvTimer.setText("" + String.format(Locale.US, 
                			"%d min : %d sec", 
                			cdInMinutes, cdRemainSecond));
                	tvWorkOrBreakTime.setText("You are taking a break ...");
                }
            }
            public void onFinish()
            {
            	btnStartTimer.setEnabled(false);
            	btnPauseTimer.setEnabled(true);
            	btnResumeTimer.setEnabled(false);
            	btnStopTimer.setEnabled(true);
                setWorkTimer(workTime1, countDownInterval);
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
        
        setButtonImage(startable, pauseble, resumeble, cancelable);
	}
	
	/**
	 * use with setButtonAndFlagState(),
	 * this method take care of switch between regular image and subdued image.
	 * @param startable
	 * @param pauseble
	 * @param resumeble
	 * @param cancelable
	 */
	public void setButtonImage(boolean startable, boolean pauseble, boolean resumeble, boolean cancelable)
	{
		if(startable)
			btnStartTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStart, null);
		else
			btnStartTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStartSubdued, null);
		
		if(pauseble)
			btnPauseTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawablePause, null);
		else
			btnPauseTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawablePauseSubdued, null);
		
		if(resumeble)
			btnResumeTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableResume, null);
		else
			btnResumeTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableResumeSubdued, null);
		
		if(cancelable)
			btnStopTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStop, null);
		else
			btnStopTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStopSubdued, null);
	}
}
