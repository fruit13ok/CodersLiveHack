package com.proto.coderslivehack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This page will graph 2 data lines "grade" (dotted with bubble) and "distraction level" (red)
 * to represent the projects results.<br> 
 * User can press the bubble to see more info of the project, swipe to see more of the graph, 
 * and filter the project by programming language.
 */
public class GraphActivity extends Activity
{
	// use to format date
	String datePattern;
	Locale currentLocale;
	SimpleDateFormat sdf;
	
	Spinner spFilterProgrammingLanguage;
	ArrayList<String> alFilterProgrammingLanguage;
	ArrayAdapter<String> aaFilterProgrammingLanguage;
	String allLanguageFromDB;
	String[] arAllLanguageFromDB;
	Button btnFilter;
	
	Button btnGraphToHome;
	
	// label the graph x y axis, may be legend too, because the spacing of this API have some problem,
	// use tvSelectedProjectInfo to display info of a data point to viewer.
	TextView tvXaxisLabel, tvSelectedProjectInfo;
	
	// these are use to access database
	DBHelperAdapter dBHelperAdapter;
	String dataWholeRow;
	
	// these are use for convert string of query result to data point array.
	String strDBDateAndGrades;
	String strDBDateAndDistractions;
	ArrayList<DataPoint> alDateGradeDataPoints;
	ArrayList<DataPoint> alDateDistractionDataPoints;
	DataPoint[] arrDateGradeDataPoints;
	DataPoint[] arrDateDistractionDataPoints;
	StringTokenizer stDateGrade;
	StringTokenizer stDateDistraction;
	
	// these are use for graph
	GraphView graph;
//	BarGraphSeries<DataPoint> series;
	LineGraphSeries<DataPoint> seriesDateGrade;
	LineGraphSeries<DataPoint> seriesDateDistraction;
	Paint paint;
	GridLabelRenderer gridLabelRenderer;
	Viewport viewport;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTitle("Graph of Projects");
		getActionBar().setIcon(R.drawable.ic_action_graph);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
		
		// setStatusBar need api 21 now use 14
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.parseColor("#303F9F"));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		datePattern = "MM/dd/yyyy";
		currentLocale = getResources().getConfiguration().locale;
		sdf = new SimpleDateFormat(datePattern, currentLocale);
		
		tvXaxisLabel = (TextView) findViewById(R.id.tvXaxisLabel);
		tvSelectedProjectInfo = (TextView) findViewById(R.id.tvSelectedProjectInfo);
		tvSelectedProjectInfo.setMovementMethod(new ScrollingMovementMethod());
		
		alDateGradeDataPoints = new ArrayList<DataPoint>();
		
		dBHelperAdapter = new DBHelperAdapter(getApplicationContext());
		strDBDateAndGrades = "";
		strDBDateAndDistractions = "";
		dataWholeRow = "";
		

		graph = (GraphView) findViewById(R.id.graph);
//		series = new BarGraphSeries<DataPoint>();
		seriesDateGrade = new LineGraphSeries<DataPoint>();
		// set date label formatter
		gridLabelRenderer = graph.getGridLabelRenderer();
		// set manual x bounds
		// the following given feature have problem, I am not using it.
		// !!!add / minus day with a double number: num_of_day*24*60*60*1000, ONLY within a month!!!
		viewport = graph.getViewport();
		
		// date and grade data
		strDBDateAndGrades = dBHelperAdapter.getAllDateGrade();
		alDateGradeDataPoints = datesGradesToDataPoints(strDBDateAndGrades);
		alDateGradeDataPoints.add(0, new DataPoint(subtractOneDay(), 0));
		arrDateGradeDataPoints = alDateGradeDataPoints.toArray(new DataPoint[alDateGradeDataPoints.size()]);
		// date and distraction data
		strDBDateAndDistractions = dBHelperAdapter.getAllDateDistraction();
		alDateDistractionDataPoints = datesDistractionsToDataPoints(strDBDateAndDistractions);
		alDateDistractionDataPoints.add(0, new DataPoint(subtractOneDay(), 0));
		arrDateDistractionDataPoints = alDateDistractionDataPoints.toArray(new DataPoint[alDateDistractionDataPoints.size()]);
		// initialize graph
		initGraph(arrDateGradeDataPoints, arrDateDistractionDataPoints);
		
		allLanguageFromDB = "";
		allLanguageFromDB = dBHelperAdapter.getAllLanguages();
		arAllLanguageFromDB = allLanguageFromDB.split("\\|");
		
		// Dynamically add languages from db to spinner,
		// only add language that at least 1 project totally done.
		spFilterProgrammingLanguage = (Spinner) findViewById(R.id.spFilterProgrammingLanguage);
		alFilterProgrammingLanguage = new ArrayList<String>();
		alFilterProgrammingLanguage.add("No selection");
		alFilterProgrammingLanguage.add("All");
		for(String aLanguage : arAllLanguageFromDB)
		{
			if(!dBHelperAdapter.getDateGradeByLanguage(aLanguage).isEmpty() && 
					!dBHelperAdapter.getDateDistractionByLanguage(aLanguage).isEmpty())
			{
				alFilterProgrammingLanguage.add(aLanguage);
			}
		}
		
		aaFilterProgrammingLanguage = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, alFilterProgrammingLanguage);
		aaFilterProgrammingLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spFilterProgrammingLanguage.setAdapter(aaFilterProgrammingLanguage);
		spFilterProgrammingLanguage.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("No selection"))
				{
				}
				else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("All"))
				{
					strDBDateAndGrades = dBHelperAdapter.getAllDateGrade();
//					Toast.makeText(parent.getContext(), strDBDateAndGrades, Toast.LENGTH_SHORT).show();
					alDateGradeDataPoints = datesGradesToDataPoints(strDBDateAndGrades);
					alDateGradeDataPoints.add(0, new DataPoint(subtractOneDay(), 0));
					arrDateGradeDataPoints = alDateGradeDataPoints.toArray(
							new DataPoint[alDateGradeDataPoints.size()]);
					
					strDBDateAndDistractions = dBHelperAdapter.getAllDateDistraction();
					alDateDistractionDataPoints = datesDistractionsToDataPoints(strDBDateAndDistractions);
					alDateDistractionDataPoints.add(0, new DataPoint(subtractOneDay(), 0));
					arrDateDistractionDataPoints = alDateDistractionDataPoints.toArray(
							new DataPoint[alDateDistractionDataPoints.size()]);
				}
				else
				{
					strDBDateAndGrades = dBHelperAdapter.getDateGradeByLanguage(
							parent.getItemAtPosition(position).toString());
//					Toast.makeText(parent.getContext(), strDBDateAndGrades, Toast.LENGTH_SHORT).show();
					alDateGradeDataPoints = datesGradesToDataPoints(strDBDateAndGrades);
					alDateGradeDataPoints.add(0, new DataPoint(subtractOneDay(), 0));
					arrDateGradeDataPoints = alDateGradeDataPoints.toArray(
							new DataPoint[alDateGradeDataPoints.size()]);
					
					strDBDateAndDistractions = dBHelperAdapter.getDateDistractionByLanguage(
							parent.getItemAtPosition(position).toString());
					alDateDistractionDataPoints = datesDistractionsToDataPoints(strDBDateAndDistractions);
					alDateDistractionDataPoints.add(0, new DataPoint(subtractOneDay(), 0));
					arrDateDistractionDataPoints = alDateDistractionDataPoints.toArray(
							new DataPoint[alDateDistractionDataPoints.size()]);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		btnFilter = (Button) findViewById(R.id.btnFilter);
		btnFilter.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				updateGraph(arrDataPoints);
				initGraph(arrDateGradeDataPoints, arrDateDistractionDataPoints);
				tvSelectedProjectInfo.setText(strDBDateAndGrades);
			}
		});
		
		btnGraphToHome = (Button) findViewById(R.id.btnGraphToHome);
		btnGraphToHome.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent graphToHomeIntent = new Intent(getBaseContext(), CodersLiveHackActivity.class);
				startActivity(graphToHomeIntent);
				finish();
			}
		});
	}

	/**
	 * String to Date with custom format
	 * @param strDate
	 * @param dateFormat
	 * @return Date
	 */
	public Date stringToDate(String strDate)
	{
		Calendar cal = Calendar.getInstance();
		try
		{
			cal.setTime(sdf.parse(strDate));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		Date d = cal.getTime();
		return d;
	}
	
	/**
	 * Convert the given string of query date grade result into ArrayList of type DataPoint.<br>
	 * mStrDBDateAndGrades is the single string contain whole query result from database.<br>
	 * mStrDBDateAndGrades has 2 delimiters:<br>
	 * 		-"|" separate date and grade,<br>
	 * 		-"new line" separate each row of record.<br>
	 * @param mStrDBDateAndGrades
	 * @return ArrayList of DataPoint
	 */
	public ArrayList<DataPoint> datesGradesToDataPoints(String mStrDBDateAndGrades)
	{
		ArrayList<DataPoint> alDataPoints = new ArrayList<DataPoint>();
		String[] strDBDateAndGradeRows = mStrDBDateAndGrades.split("\\n");
		Date oneDate = null;
		String oneGrade = null;
		for (int i = 0; i < strDBDateAndGradeRows.length; i++)
		{
			//TODO: I did not check empty token, just assume getting 2 tokens, should add checking later
			stDateGrade = new StringTokenizer(strDBDateAndGradeRows[i], "\\|");
			oneDate = stringToDate(stDateGrade.nextElement().toString());
			oneGrade = stDateGrade.nextElement().toString();
			alDataPoints.add(new DataPoint(oneDate, Double.parseDouble(oneGrade)));
		}
		return alDataPoints;
	}
	
	/**
	 * Convert the given string of query date distraction result into ArrayList of type DataPoint.<br>
	 * mStrDBDateAndDistractions is the single string contain whole query result from database.<br>
	 * mStrDBDateAndDistractions has 2 delimiters:<br>
	 * 		-"|" separate date and grade,<br>
	 * 		-"new line" separate each row of record.<br>
	 * @param mStrDBDateAndDistractions
	 * @return ArrayList of DataPoint
	 */
	public ArrayList<DataPoint> datesDistractionsToDataPoints(String mStrDBDateAndDistractions)
	{
		ArrayList<DataPoint> alDataPoints = new ArrayList<DataPoint>();
		String[] strDBDateAndDistractionRows = mStrDBDateAndDistractions.split("\\n");
		Date oneDate = null;
		String oneDistraction = null;
		for (int i = 0; i < strDBDateAndDistractionRows.length; i++)
		{
			stDateDistraction = new StringTokenizer(strDBDateAndDistractionRows[i], "\\|");
			oneDate = stringToDate(stDateDistraction.nextElement().toString());
			oneDistraction = stDateDistraction.nextElement().toString();
			alDataPoints.add(new DataPoint(oneDate, Double.parseDouble(oneDistraction)));
		}
		return alDataPoints;
	}
	
	/**
	 * Initialize a new graph, data, and everything, and display the graph.<br>
	 * I choose not to use the next method updateGraph() for stability and robust.
	 * @param iArDGDP array of date and grade data point
	 * @param iArDDDP array of date and distraction data point
	 */
	public void initGraph(DataPoint[] iArDGDP, DataPoint[] iArDDDP)
	{
		graph.removeSeries(seriesDateGrade);
		graph.removeSeries(seriesDateDistraction);
		setDateGradeSeries(iArDGDP);		//setup seriesDateGrade
		setDateDistractionSeries(iArDDDP);	//setup seriesDateDistraction
		graph.addSeries(seriesDateGrade);
		graph.addSeries(seriesDateDistraction);
		setGridLabelRenderer();
		setViewPort(iArDGDP);
	}
	
	/**
	 * Change new set of data points, remove the series and add it back to graphview.
	 * Update not yet working, not in use.
	 * @param nArDP
	 */
//	public void updateGraph(DataPoint[] nArDP)
//	{
////		series.resetData(nArDP);
////		setViewPort(nArDP);
////		graph.removeSeries(series);
////		graph.addSeries(series);
//		
//		graph.removeSeries(seriesDateGrade);
//		seriesDateGrade.resetData(nArDP);
////		try {
////			new Thread().sleep(5000);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//		graph.addSeries(seriesDateGrade);
//		setViewPort(nArDP);
//	}
	
	/**
	 * Series is to set how the data point and line, look and act for grade line.<br>
	 * The data point listener is here.
	 * @param mArDP array of data point
	 */
	public void setDateGradeSeries(DataPoint[] mArDP)
	{
//		series = new BarGraphSeries<DataPoint>(mArDP);
		seriesDateGrade = new LineGraphSeries<DataPoint>(mArDP);
//		series.resetData(mArDP);
		seriesDateGrade.setTitle("series title");
		seriesDateGrade.setColor(Color.GREEN);
		
		// setting for line graph
		seriesDateGrade.setDrawDataPoints(true);
		seriesDateGrade.setDataPointsRadius(60);
		seriesDateGrade.setThickness(8);
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(10);
		paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
		seriesDateGrade.setCustomPaint(paint);
		
		// the setting for bar graph
		// I am using line graph
//		series.resetData(mArDP);
//		series.setTitle("series title");
//		series.setColor(Color.GREEN);
//		series.setDrawValuesOnTop(true);
//		series.setValuesOnTopColor(Color.RED);
//		series.setSpacing(50);
//		series.setValueDependentColor(new ValueDependentColor<DataPoint>()
//		{
//			@Override
//			public int get(DataPoint data)
//			{
//				// if you want other range of color, play around the color,
//				// http://www.rapidtables.com/web/color/color-wheel.htm
//				// the first one use all 3 r,g,b range from black to white
////				return Color.rgb((int)(data.getY()*25.5), (int)(data.getY()*25.5), (int)(data.getY()*25.5));
//				return Color.rgb((int)(data.getY()*25.5), (int)(data.getY()*25.5), 255);
//			}
//		});
		
        seriesDateGrade.setOnDataPointTapListener(new OnDataPointTapListener()
        {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint)
            {
                // To get date from double type to a String type through long and Date
                long l = (long) (dataPoint.getX());
                Date d = new Date(l);
                String s = new SimpleDateFormat(datePattern, currentLocale).format(d);
                Toast.makeText(getApplicationContext(),
                        "date: "+ s + ", grade: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();

                dataWholeRow = dBHelperAdapter.getAllDataByDateAndGrade(s, "" + (int) dataPoint.getY(),"|");
                String[] allData = dataWholeRow.split("\\|");
                String projectTitle = allData[1];
                String projectLanguage = allData[2];
                String description = allData[3];
                String startDate = allData[4];
                String timeSpent = allData[5];
                String grade = allData[6];
                String distractionLevel = allData[7];
                String finalProgress = allData[8];
                String comments = allData[9];
                
                if(description.isEmpty() || description == null)
                {
                    description = "No Project description found";
                }

                if(comments.isEmpty() || comments == null)
                {
                    comments = "No Project Comments found ";
                }
                
                tvSelectedProjectInfo.setText("Title: " + projectTitle + " | " + "Start Date: " + s
                        + " | "+ "Language: " + projectLanguage + " | " + "Grade: " + dataPoint.getY()
                        + "\n" + "Time Spent: " + timeSpent + " | " + "Distraction: " + distractionLevel
                        + " | " + "Final Progress: " + finalProgress
                        + "\n" + "scroll for Description and Comments..."
                        + "\n" + "\n" + "Description:\n" + description
                        + "\n" + "Comments: \n" + comments);
            }
        });
	}
	
	/**
	 * Series is to set how the data point and line, look and act for distraction line.<br>
	 * The data point listener is here.
	 * @param mArDP array of data point
	 */
	public void setDateDistractionSeries(DataPoint[] mArDP)
	{
		seriesDateDistraction = new LineGraphSeries<DataPoint>(mArDP);
		seriesDateDistraction.setColor(Color.RED);
	}
	
	/**
	 * GridLabelRenderer is to set what to display on the 2 axis, label and title.<br>
	 * This version of api is ok but waiting for a better upgrade.
	 */
	public void setGridLabelRenderer()
	{
		gridLabelRenderer.setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
		gridLabelRenderer.setNumHorizontalLabels(4); // only 4 because of the screen space
		gridLabelRenderer.setNumVerticalLabels(6);
	}
	
	/**
	 * ViewPort is to only show a party of the whole graph by set min and max of x and y.<br>
	 * You can make the graph scrollable and scalable.
	 * @param myArDP
	 */
	public void setViewPort(DataPoint[] myArDP)
	{
		viewport.setScrollable(true);
//		viewport.setScalable(true);
		
//		viewport.setMinX(stringToDate("07/01/2015").getTime());
//		viewport.setMaxX(stringToDate("07/30/2015").getTime());
		
		// range last 10 or full range if has less
		// example have 18 entries of data, so I can range last 10 data
		if(myArDP.length >= 11)
		{
//			viewport.setMinX(myArDP[0].getX());
			viewport.setMinX(myArDP[myArDP.length - 11].getX());
		}
		else
		{
			viewport.setMinX(myArDP[0].getX());
		}
		
		viewport.setMaxX(myArDP[myArDP.length - 1].getX());
		viewport.setXAxisBoundsManual(true);
		viewport.setMinY(0);
		viewport.setMaxY(10);
		viewport.setYAxisBoundsManual(true);
	}
	
	/**
	 * On current version of GraphView API,<br>
	 * I found the graph has to have at least 1 data point for it to function.<br>
	 * At least to have 2 points for the listener to work on the second point.<br>
	 * So, this method is to insert a placeholder date 1 day before the first recorded date.<br>
	 * <br>
	 * NOTE: this method still not taking care the whole data point problem, need to upgrade.
	 * @return Date
	 */
	public Date subtractOneDay()
	{
		long nl = (long) (alDateGradeDataPoints.get(0).getX());
    	Date nd = new Date(nl);
    	String ns = new SimpleDateFormat(datePattern, currentLocale).format(nd);
    	Calendar ncal = Calendar.getInstance();
		try {
			ncal.setTime(sdf.parse(ns));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ncal.add(Calendar.DATE, -1);
		return ncal.getTime();
	}
}
