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
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

// TODO:
// to solve the cutoff first DataPoint problem,
// put a dummy DataPoint in front on the ArrayList

/*
 * this version I removed the RangSeekBar because this third party API is does not go well with GraphView API.
 * so I just use GraphView enable it scrolling.
 * 
 * problem in this version the Series reset with new data, 
 * the updating is not consistent for bar graph, first point hidden on line graph,
 * maybe about this API have problem with openGL, may be threading problem.
 * These problems happen both in single object use update or create new object every time.
 */
public class GraphActivity extends Activity
{
	// these 2 use to format date
	String datePattern;
	Locale currentLocale;
//	SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.US);
	SimpleDateFormat sdf;
	
	// spinner is not make to use for executing code, should add a button later to execute the selected spinner
	Spinner spFilterProgrammingLanguage;
	ArrayList<String> alFilterProgrammingLanguage;
	ArrayAdapter<String> aaFilterProgrammingLanguage;
	String allLanguageFromDB;
	String[] arAllLanguageFromDB;
	Button btnFilter;
	
	// use to navigate 
	Button btnGraphToHome;
	
	// use these 3 to label the graph x y axis, may be legend too, because the spacing of this API have some problem,
	// use tvSelectedProjectInfo to display info of a data point to viewer.
	TextView tvXaxisLabel, tvSelectedProjectInfo;
	
	// use to access database
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
		
		//
		graph = (GraphView) findViewById(R.id.graph);
//		series = new BarGraphSeries<DataPoint>();
		seriesDateGrade = new LineGraphSeries<DataPoint>();
		// set date label formatter
		gridLabelRenderer = graph.getGridLabelRenderer();
		// set manual x bounds
		// the following given feature have problem.
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
		arAllLanguageFromDB = allLanguageFromDB.split(" ");
		
		// TODO:
		// when have time make grade and distraction always come in pair, may be in createtaskactivity,
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
				//TODO
				tvSelectedProjectInfo.setText(strDBDateAndGrades);
			}
		});
		
		//
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
		try {
			cal.setTime(sdf.parse(strDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date d = cal.getTime();
		return d;
	}
	
	/**
	 * Convert the given string of query date grade result into ArrayList of type DataPoint.<br>
	 * mStrDBDateAndGrades is the single string contain whole query result from database.<br>
	 * mStrDBDateAndGrades has 2 delimiters:<br>
	 * 		-"single space" separate date and grade,<br>
	 * 		-"new line" separate each row of record.<br>
	 * @param mStrDBDateAndGrades
	 * @return
	 */
	public ArrayList<DataPoint> datesGradesToDataPoints(String mStrDBDateAndGrades)
	{
		ArrayList<DataPoint> alDataPoints = new ArrayList<DataPoint>();
		String[] strDBDateAndGradeRows = mStrDBDateAndGrades.split("\\n");
		Date oneDate = null;
		String oneGrade = null;
		for (int i = 0; i < strDBDateAndGradeRows.length; i++)
		{
//			System.out.println(i + ": " + strDBDateAndGradeRows[i]);
			
			// default delimiter is already space
			// I did not check empty token, just assume getting 2 tokens, should add checking later
			stDateGrade = new StringTokenizer(strDBDateAndGradeRows[i], "\\|");
			oneDate = stringToDate(stDateGrade.nextElement().toString());
			oneGrade = stDateGrade.nextElement().toString();
			alDataPoints.add(new DataPoint(oneDate, Double.parseDouble(oneGrade)));
		}
		// need to convert from double to Date
//		System.out.println( "mytab: " + 
//		new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date( (long)( alDataPoints.get(0).getX() ) ) ) + 
//		", " + alDataPoints.get(0).getY());
		return alDataPoints;
	}
	
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
	 * initialize the graph, data, and everything, and display the graph.
	 * @param iArDGDP
	 * @param iArDDDP
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
	 * change new set of data points, remove the series and add it back to graphview.
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
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		graph.addSeries(seriesDateGrade);
//		setViewPort(nArDP);
//	}
	
	/**
	 * Series is to set how the data point and line, look and act.<br>
	 * The data point listener is here.
	 * @param mArDP
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
		
		// setting for bar graph
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
		
//		seriesDateGrade.setOnDataPointTapListener(new OnDataPointTapListener()
//		{
//		    @Override
//		    public void onTap(Series series, DataPointInterface dataPoint)
//		    {
//		    	// To get date from double type to a Date type
//		    	long l = (long) (dataPoint.getX());
//		    	Date d = new Date(l);
//		    	String s = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(d);
//		        Toast.makeText(getApplicationContext(), 
//		        		"date: "+ s + ", grade: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
//		        
//		        DataWholeRow = dBHelperAdapter.getAllDataByDateAndGrade(s, ""+(int)dataPoint.getY());
//		        tvSelectedProjectInfo.setText("date: "+ s + ", grade: " + dataPoint.getY() + 
//		        		"\nid | title | lang | des | start | spent | prod | dist | prog | com\n" + 
//		        		DataWholeRow + "\n" + strDBDateAndGrades);
//		    }
//		});
		
		/**
         * Joel Version Of method call setOnDataPointTapListener
         */
        seriesDateGrade.setOnDataPointTapListener(new OnDataPointTapListener()
        {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint)
            {
                // To get date from double type to a Date type
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
                
//                if(description.length() <= 1)
                if(description.isEmpty() || description == null)
                {
                    description = "No Project description found";
                }

//                if(comments.length() >= 1 && comments != null)
                if(comments.isEmpty() || comments == null)
                {
//                    Toast.makeText(getApplicationContext(),
//                            "Comments: " + comments.length() , Toast.LENGTH_SHORT).show();
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
	
	public void setDateDistractionSeries(DataPoint[] mArDP)
	{
		seriesDateDistraction = new LineGraphSeries<DataPoint>(mArDP);
//		seriesDateDistraction.setTitle("series title");
		seriesDateDistraction.setColor(Color.RED);
		
		// setting for line graph
//		seriesDateDistraction.setDrawDataPoints(true);
//		seriesDateDistraction.setDataPointsRadius(30);
//		seriesDateDistraction.setThickness(8);
//		paint = new Paint();
//		paint.setStyle(Paint.Style.FILL);
//		paint.setStrokeWidth(10);
//		paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
//		seriesDateDistraction.setCustomPaint(paint);
		
//		seriesDateDistraction.setOnDataPointTapListener(new OnDataPointTapListener()
//		{
//		    @Override
//		    public void onTap(Series series, DataPointInterface dataPoint)
//		    {
//		    	// To get date from double type to a Date type
//		    	long l = (long) (dataPoint.getX());
//		    	Date d = new Date(l);
//		    	String s = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(d);
//		        Toast.makeText(getApplicationContext(), 
//		        		"date: "+ s + ", grade: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
//		        
//		        DataWholeRow = dBHelperAdapter.getAllDataByDateAndGrade(s, ""+(int)dataPoint.getY());
//		        tvSelectedProjectInfo.setText("date: "+ s + ", grade: " + dataPoint.getY() + 
//		        		"\nid | title | lang | des | start | spent | prod | dist | prog | com\n" + 
//		        		DataWholeRow + "\n" + strDBDateAndGrades);
//		    }
//		});
	}
	
	/**
	 * GridLabelRenderer is to set what to display on the 2 axis, label and title.<br>
	 * This version of api is ok but waiting for a better upgrade.
	 */
	public void setGridLabelRenderer()
	{
		gridLabelRenderer.setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
		gridLabelRenderer.setNumHorizontalLabels(4); // only 4 because of the space
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
