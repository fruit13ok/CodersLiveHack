package com.proto.coderslivehack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

// TODO: make a subaccumulated work time count for user stop the timer (stop before the section finish) add the record.

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
	String selectedProject, curProjTitle, curProjLanguage, curProjDescription, curProjStartDate, 
		curProjTimeSpent, curProjProgress, curProjGrade, curProjDistraction;
	TextView tvSelectedProject, tvWorkOrBreakTime, tvTimer, tvAccumulatedTimeSpent;
	Button btnStartTimer, btnPauseTimer, btnResumeTimer, btnStopTimer, 
		btnMarkProjectComplete, btnMarkProjectAbandon, btnTimerToHome, btnTimerToAssess;
	
	long countDownInterval, workTime1, workTime2, breakTime1, breakTime2, workTimeRemain;
	/**
	 * <b>accumulatedTimeSpent</b>, <b>partialSectionTimeSpent</b>, <b>recordedTimeSpent</b>, 
	 * and <b>stoppingPoint</b> measure in second <b><ins><i>NOT</i></ins></b> millisecond
	 */
	long accumulatedTimeSpent, recordedTimeSpent, stoppingPoint;

	//Declare a variable to hold count down timer's paused status
    private boolean isPaused, isCanceled;
	
    // 8 icon for start, pause, resume, stop
    Drawable drawableStart, drawableStartSubdued, drawablePause, drawablePauseSubdued,
    	drawableResume, drawableResumeSubdued, drawableStop, drawableStopSubdued;
    
    DBHelperAdapter dBHelperAdapter;
    
    boolean isWorking;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		isWorking = false;
		
		Intent fromSelectTaskIntent = getIntent();
		selectedProject = fromSelectTaskIntent.getStringExtra(SelectTaskActivity.EXTRA_SELECTED_TASK);
		curProjTitle = selectedProject.split("\\|")[1];
		curProjLanguage = selectedProject.split("\\|")[2];
		curProjDescription = selectedProject.split("\\|")[3];
		curProjStartDate = selectedProject.split("\\|")[4];
		curProjTimeSpent = selectedProject.split("\\|")[5];
		curProjGrade = selectedProject.split("\\|")[6];
		curProjDistraction = selectedProject.split("\\|")[7];
		curProjProgress = selectedProject.split("\\|")[8];
		
		tvSelectedProject = (TextView) findViewById(R.id.tvSelectedProject);
		tvSelectedProject.setText("Title: " + curProjTitle + 
				"\nProgress: " + curProjProgress + 
				"\nGrade: " + curProjGrade + 
				"\nDistraction: " + curProjDistraction);
		
		tvWorkOrBreakTime = (TextView) findViewById(R.id.tvWorkOrBreakTime);
		tvWorkOrBreakTime.setText("Not start work yet");
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		tvAccumulatedTimeSpent = (TextView) findViewById(R.id.tvAccumulatedTimeSpent);
		
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
		
        workTime1 = 30 * 1000;			//25 min, 25 * 60 * 1000
        workTime2 = 50 * 60 * 1000;		//50 min
        breakTime1 = 5 * 1000;			//5 min
        breakTime2 = 10 * 60 * 1000;	//10 min
        countDownInterval = 1000;
        workTimeRemain = 0;
        accumulatedTimeSpent = 0;		//!!! this is measure in second not millisecond
        recordedTimeSpent = 0;			//!!! this is measure in second not millisecond
        // may be useful for set max work load, for example no more than 16 hours a day.
        stoppingPoint = 120;			//!!! this is measure in second not millisecond
        
        //Init: not pause, not cancel flags, enable start, disabled pause, resume, and cancel buttons
        if(isCompleteOrAbandon())
        {
        	setButtonAndFlagState(false, false, false, false, false, false);
        	btnMarkProjectComplete.setEnabled(false);
        	btnMarkProjectAbandon.setEnabled(false);
        	if(isAssessed())
        	{
        		btnTimerToAssess.setEnabled(false);
        	}
        	else
        	{
        		btnTimerToAssess.setEnabled(true);
        	}
        }
        else
        {
        	setButtonAndFlagState(false, false, true, false, false, false);
        	btnMarkProjectComplete.setEnabled(true);
        	btnMarkProjectAbandon.setEnabled(true);
        	btnTimerToAssess.setEnabled(false);
        }
        
        btnStartTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//
				if(dBHelperAdapter.getProjectProgress(curProjTitle).equalsIgnoreCase("not start"))
				{
					String timeStamp = new SimpleDateFormat(
							"MM/dd/yyyy", Locale.US).format(Calendar.getInstance().getTime());
					dBHelperAdapter.updateStartTime(curProjTitle, timeStamp);
				}
				
				dBHelperAdapter.updateProjectProgress(curProjTitle, "working on");
				
				btnStartTimer.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableStartSubdued, null);

				setButtonAndFlagState(false, false, false, true, false, true);

                setWorkTimer(workTime1, countDownInterval);
			}
		});
        
        // pause is still dictate by current countdowntimer cycle
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
        
        // insertTimeSpentToDB() here is NOT redundant, it is for when user click pause then stop
        btnStopTimer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setButtonAndFlagState(false, true, true, false, false, false);

                //Notify the user that CountDownTimer is canceled/stopped
				tvTimer.setText("CountDownTimer stopped.");
				
				insertTimeSpentToDB();
			}
		});
        
        // TODO: do mark project as complete to database, may be the accumulation time spent too
        btnMarkProjectComplete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dBHelperAdapter.updateProjectProgress(curProjTitle, "completed");
				btnMarkProjectComplete.setEnabled(false);
	        	btnMarkProjectAbandon.setEnabled(false);
	        	btnTimerToAssess.setEnabled(true);
			}
		});
        
        // TODO: do mark project as abandon to database, may be the accumulation time spent too
        btnMarkProjectAbandon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dBHelperAdapter.updateProjectProgress(curProjTitle, "abandoned");
				btnMarkProjectComplete.setEnabled(false);
	        	btnMarkProjectAbandon.setEnabled(false);
	        	btnTimerToAssess.setEnabled(true);
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
		isWorking = true;
		//Initialize a new CountDownTimer instance, ex for 30 seconds 1 second per tick
        new CountDownTimer(mWorkTime,mCountDownInterval)
        {
        	// millisUntilFinished is pass from millisInFuture / mWorkTime
            public void onTick(long millisUntilFinished)
            {
                //do something in every tick
                if(isPaused || isCanceled || accumulatedTimeSpent >= stoppingPoint || !isWorking)
                {
                	if(accumulatedTimeSpent >= stoppingPoint)
                    {
                    	insertTimeSpentToDB();
                    	setButtonAndFlagState(false, true, false, false, false, false);
                    }
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                }
                else 
                {
                    //Display the remaining seconds to app interface
                	
                	long cdInMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                	long cdInSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                	long cdRemainSecond = cdInSeconds - TimeUnit.MINUTES.toSeconds(cdInMinutes);
                	tvTimer.setText("" + String.format(Locale.US, 
                			"%d min : %d sec", 
                			cdInMinutes, cdRemainSecond));
                	
                    //Put count down timer remaining time in a variable
                	workTimeRemain = millisUntilFinished;
                	tvWorkOrBreakTime.setText("You are working");
                	accumulatedTimeSpent++;
                }
            }
            public void onFinish()
            {
            	// here increase by 1 is because onFinish do count one last tick (second)
            	accumulatedTimeSpent++;
            	
            	// this makes infinite CountDownTimer until user pause or cancel/stop
            	// WARNING: will this cause resource drink or memory leak,
            	// may be but will not going to be a lot because:
            	// EX:
            	// a pomodoro section total 4 hours, 4 cycle each 1 hour; then there are only 4 call.
            	// also CountDownTimer is not a service will close if activity go to background.
//            	btnStartTimer.setEnabled(false);
//            	btnPauseTimer.setEnabled(false);
//            	btnResumeTimer.setEnabled(false);
//            	btnStopTimer.setEnabled(true);
            	setButtonAndFlagState(false, false, false, false, false, true);
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
		isWorking = false;
		new CountDownTimer(mBreakTime,ncountDownInterval)
        {
            public void onTick(long millisUntilFinished)
            {
                if(isCanceled || accumulatedTimeSpent >= stoppingPoint || isWorking)
                {
                	insertTimeSpentToDB();
                	setButtonAndFlagState(false, true, false, false, false, false);
                    cancel();
                }
                else 
                {
                	tvTimer.setText("" + millisUntilFinished / 1000);
                	long cdInMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                	long cdInSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                	long cdRemainSecond = cdInSeconds - TimeUnit.MINUTES.toSeconds(cdInMinutes);
                	tvTimer.setText("" + String.format(Locale.US, 
                			"%d min : %d sec", 
                			cdInMinutes, cdRemainSecond));
                	tvWorkOrBreakTime.setText("You are taking a break");
                }
            }
            public void onFinish()
            {
//            	btnStartTimer.setEnabled(false);
//            	btnPauseTimer.setEnabled(true);
//            	btnResumeTimer.setEnabled(false);
//            	btnStopTimer.setEnabled(true);
            	setButtonAndFlagState(false, false, false, true, false, true);
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
	
	//update db project time spent, unit of measurement is second
	public void insertTimeSpentToDB()
	{
		String timeSpentFromDB = dBHelperAdapter.get_TimeSpent(curProjTitle);
		// check for first insert of add to record
		if(!timeSpentFromDB.equalsIgnoreCase("null"))
		{
			// String hr:min:sec  to  long sec
			recordedTimeSpent = timeToSecond(timeSpentFromDB);
		}
		// long sec  to  String hr:min:sec
		long sttsInHours = TimeUnit.SECONDS.toHours(accumulatedTimeSpent + recordedTimeSpent);
    	long sttsInMinutes = TimeUnit.SECONDS.toMinutes(accumulatedTimeSpent + recordedTimeSpent);
    	long sttsInSeconds = TimeUnit.SECONDS.toSeconds(accumulatedTimeSpent + recordedTimeSpent);
    	long sttsRemainMinute = sttsInMinutes - TimeUnit.HOURS.toMinutes(sttsInHours);
    	long sttsRemainSecond = sttsInSeconds - TimeUnit.HOURS.toSeconds(sttsInHours) - 
    			TimeUnit.MINUTES.toSeconds(sttsRemainMinute);
    	String subTotleTimeSpent = String.format(Locale.US, 
            			"%d:%d:%d", 
            			sttsInHours, sttsRemainMinute, sttsRemainSecond);
    	// TODO: move this to top
    	tvAccumulatedTimeSpent.setText("project accumulatedTimeSpent: " + subTotleTimeSpent);
    	// do actual update to db
    	dBHelperAdapter.insert_TimeSpent(curProjTitle, subTotleTimeSpent);
    	
    	long msttsInHours = TimeUnit.SECONDS.toHours(accumulatedTimeSpent);
    	long msttsInMinutes = TimeUnit.SECONDS.toMinutes(accumulatedTimeSpent);
    	long msttsInSeconds = TimeUnit.SECONDS.toSeconds(accumulatedTimeSpent);
    	long msttsRemainMinute = msttsInMinutes - TimeUnit.HOURS.toMinutes(msttsInHours);
    	long msttsRemainSecond = msttsInSeconds - TimeUnit.HOURS.toSeconds(msttsInHours) - 
    			TimeUnit.MINUTES.toSeconds(msttsRemainMinute);
    	String msubTotleTimeSpent = String.format(Locale.US, 
            			"%d:%d:%d", 
            			msttsInHours, msttsRemainMinute, msttsRemainSecond);
    	tvWorkOrBreakTime.setText("section time spent: " + msubTotleTimeSpent);
    	accumulatedTimeSpent = 0;
	}
	
	public long timeToSecond(String strTime)
	{
		String[] tempTime = strTime.split(":");
		String tempHours = tempTime[0];
		String tempMinutes = tempTime[1];
		String tempSeconds = tempTime[2];
		long secondInHour = Long.parseLong(tempHours) * 60 * 60;
		long secondInMinute = Long.parseLong(tempMinutes) * 60;
		long second = Long.parseLong(tempSeconds);
		long subtotalSeconds = secondInHour + secondInMinute + second;
		return subtotalSeconds;
	}
	
	public boolean isCompleteOrAbandon()
	{
		String projectProgress = dBHelperAdapter.getProjectProgress(curProjTitle);
		return (projectProgress.equalsIgnoreCase("completed") || projectProgress.equalsIgnoreCase("abandoned"));
	}
	
	public boolean isAssessed()
	{
		String nGrade = dBHelperAdapter.get_ProductivityLevel(curProjTitle);
		String nDistraction = dBHelperAdapter.get_DistractionLevel(curProjTitle);
		// they start with 0 not null
		if(nGrade.equalsIgnoreCase("0") && nDistraction.equalsIgnoreCase("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
