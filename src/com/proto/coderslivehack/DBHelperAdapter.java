package com.proto.coderslivehack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

// TODO:
// after finish code merge add a query to get ESTIMATION_LEVEL include it in display whole data 
/*
 * TODO: currently just try to make a temp database, database need to change later
 * 
 * side note: on API level 16 CAN DELETE WHOLE DATABASE !!! with deleteDatabase(File)
 */

/*
*
* The Following Methods below can Be Copy-and-pasted into your DBHelperAdapter.java file
*      ,because these are all new methods:
*
* 	public long insert_DistractionLevel(String project_Title,String distractionValue)
* 	public long insert_EstimationValue(String project_Title,String estimationValue)
* 	public long insert_ProductivityValues(String project_Title,String productivityValues)
* 	public long insert_TaskComments(String project_Title,String taskCommentsValues)
* 	public String get_TaskComment(String project_Title)
* 	public String get_DistractionLevel(String project_Title)
* 	public String get_ProductivityLevel(String project_Title)\
*
* 	Note: This method "public String getAllData()" is in the original DBHelperAdapter.java
* 	      file. I added all new columns to its return value, so you can copy and replace it
* 	      into your DBHelperAdapter.java file
*
 *
 * Added Two Columns to scheme, can copy and paste columns into your
 *     DBHelperAdapter.java file. I also added new columns to
 *     CREATE_TABLE string value, so copy and replace CREATE_TABLE
 *     string value. Remember to update Database version number for added columns.
 *
 * 	private static final String ESTIMATION_LEVEL = "estimationLevel";
 * 	private static final String TASK_COMMENTS = "taskComments";
 *
 * 	After these changes that completes all changes needed for DBHelperAdapter.java file
 */
public class DBHelperAdapter
{
	DBHelper dBHelper;
	
	// use to pass in Context, like last one does in main
	public DBHelperAdapter(Context context)
	{
		dBHelper = new DBHelper(context);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////// insert content //////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	// TODO: delete startDate and grade later,
	// when work on timer and assessment make update for startDate and grade later
	// now only use by CreateTaskActivity
	// use to insert data to database, return inserted row id or -1 for fail to insert
	public long insertData(String projectTitle, String programmingLanguages, String projectDescription,
			 String startDate, String grade, String distraction)
	{
		// need SQLiteDatabase object to point to your database before you can do anything
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		
		// ContentValues key value pair use to referring database columns
		ContentValues contentValues = new ContentValues();
		if(!projectTitle.isEmpty())
			contentValues.put(DBHelper.PROJECT_TITLE, projectTitle);
		if(!programmingLanguages.isEmpty())
			contentValues.put(DBHelper.PROGRAMMING_LANGUAGES, programmingLanguages);
		if(!projectDescription.isEmpty())
			contentValues.put(DBHelper.PROJECT_DESCRIPTION, projectDescription);
		if(!startDate.isEmpty())
			contentValues.put(DBHelper.START_TIME, startDate);
		if(!grade.isEmpty())
			contentValues.put(DBHelper.PRODUCTIVE_LEVEL, grade);
		if(!distraction.isEmpty())
			contentValues.put(DBHelper.DISTRACTION_LEVEL, distraction);
		
		// TODO: live it here I might not want to use default null value 
//		contentValues.put(DBHelper.START_TIME, "null");
//		contentValues.put(DBHelper.TIME_SPENT, "null");
//		contentValues.put(DBHelper.PRODUCTIVE_LEVEL, "null");
//		contentValues.put(DBHelper.PROJECT_PROGRESS, "null");
//		contentValues.put(DBHelper.DISTRACTION_LEVEL, "null");
		
		// TODO: might need to allow null value for database
		// 2nd arg nullColumnHack is to allow to insert null as database value by specify any column name,
		// null means not allow null value
		// id can use to check for insert error if return -1
		long id = sQLiteDatabase.insert(DBHelper.TABLE_NAME, null, contentValues);
		sQLiteDatabase.close();
		return id;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// update content ///////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public long insert_DistractionLevel(String project_Title,String distractionValue)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(dBHelper.DISTRACTION_LEVEL, distractionValue);
		String[] whereArgs={project_Title};
		return db.update(dBHelper.TABLE_NAME,
				contentValues, dBHelper.PROJECT_TITLE + "=?", whereArgs);
	}

	public long insert_EstimationValue(String project_Title,String estimationValue)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(dBHelper.ESTIMATION_LEVEL, estimationValue);
		String[] whereArgs={project_Title};
		return db.update(dBHelper.TABLE_NAME,
				contentValues, dBHelper.PROJECT_TITLE + "=?", whereArgs);
	}

	public long insert_ProductivityValues(String project_Title,String productivityValues)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(dBHelper.PRODUCTIVE_LEVEL, productivityValues);
		String[] whereArgs={project_Title};
		return db.update(dBHelper.TABLE_NAME,
				contentValues, dBHelper.PROJECT_TITLE + "=?", whereArgs);
	}

	public long insert_TaskComments(String project_Title,String taskCommentsValues)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(dBHelper.TASK_COMMENTS, taskCommentsValues);
		String[] whereArgs={project_Title};
		return db.update(dBHelper.TABLE_NAME,
				contentValues, dBHelper.PROJECT_TITLE + "=?", whereArgs);
	}
	
	// dummy update the row by match id, use by developer only, delete it later
	public int updateARowById(String mId, String mTitle, String mLanguage, String mDescription, 
			String mStartTime, String mTimeSpent, String mProductivie, String mDistraction)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		if(!mTitle.isEmpty())
			contentValues.put(dBHelper.PROJECT_TITLE, mTitle);
		if(!mLanguage.isEmpty())
			contentValues.put(dBHelper.PROGRAMMING_LANGUAGES, mLanguage);
		if(!mDescription.isEmpty())
			contentValues.put(dBHelper.PROJECT_DESCRIPTION, mDescription);
		if(!mStartTime.isEmpty())
			contentValues.put(dBHelper.START_TIME, mStartTime);
		if(!mTimeSpent.isEmpty())
			contentValues.put(dBHelper.TIME_SPENT, mTimeSpent);
		if(!mProductivie.isEmpty())
			contentValues.put(dBHelper.PRODUCTIVE_LEVEL, mProductivie);
		if(!mDistraction.isEmpty())
			contentValues.put(dBHelper.DISTRACTION_LEVEL, mDistraction);
		
		String[] whereArgs = {mId};
		
		int count = sQLiteDatabase.update(dBHelper.TABLE_NAME, contentValues, dBHelper.ID + " =?", whereArgs);
		return count;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// select query //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	public String get_TaskComment(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {dBHelper.TASK_COMMENTS};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(dBHelper.TABLE_NAME,
				columns, dBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(dBHelper.TASK_COMMENTS);
			String taskCom = cursor.getString(index2);

			buffer.append(taskCom);
		}
		return buffer.toString();
	}

	public String get_DistractionLevel(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {dBHelper.DISTRACTION_LEVEL};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(dBHelper.TABLE_NAME,
				columns, dBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(dBHelper.DISTRACTION_LEVEL);
			int disLvl = cursor.getInt(index2);

			buffer.append(disLvl);
		}
		return buffer.toString();
	}

	public String get_ProductivityLevel(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {dBHelper.PRODUCTIVE_LEVEL};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(dBHelper.TABLE_NAME,
				columns, dBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(dBHelper.PRODUCTIVE_LEVEL);
			int prodLvl = cursor.getInt(index2);

			buffer.append(prodLvl);
		}
		return buffer.toString();
	}
	
	public String getAllDataByDateAndGrade(String mDate, String mGrade)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		// return data
		String[] columns = {dBHelper.ID, dBHelper.PROJECT_TITLE, 
				dBHelper.PROGRAMMING_LANGUAGES, dBHelper.PROJECT_DESCRIPTION,
				dBHelper.START_TIME, dBHelper.TIME_SPENT, dBHelper.PRODUCTIVE_LEVEL, 
				dBHelper.DISTRACTION_LEVEL, dBHelper.PROJECT_PROGRESS, 
				dBHelper.ESTIMATION_LEVEL, dBHelper.TASK_COMMENTS};
		// match condition values
		String[] selectionArgs = {"%" + mDate + "%", "%" + mGrade + "%"};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, 
				dBHelper.START_TIME + " LIKE ? AND " + dBHelper.PRODUCTIVE_LEVEL + " LIKE ?", 
				selectionArgs, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int idColIndex = cursor.getColumnIndex(dBHelper.ID);
			int id = cursor.getInt(idColIndex);
			int projectTitleColIndex = cursor.getColumnIndex(dBHelper.PROJECT_TITLE);
			String projectTitle = cursor.getString(projectTitleColIndex);
			int programmingLanguagesColIndex = cursor.getColumnIndex(dBHelper.PROGRAMMING_LANGUAGES);
			String programmingLanguages = cursor.getString(programmingLanguagesColIndex);
			int projectDescriptionColIndex = cursor.getColumnIndex(dBHelper.PROJECT_DESCRIPTION);
			String projectDescription = cursor.getString(projectDescriptionColIndex);
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int timeSpentColIndex = cursor.getColumnIndex(dBHelper.TIME_SPENT);
			String timeSpent = cursor.getString(timeSpentColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(dBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(dBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			int projectProgressColIndex = cursor.getColumnIndex(dBHelper.PROJECT_PROGRESS);
			String projectProgress = cursor.getString(projectProgressColIndex);
			int estimationLevelColIndex = cursor.getColumnIndex(dBHelper.ESTIMATION_LEVEL);
			String estimationLevel = cursor.getString(estimationLevelColIndex);
			int taskCommentsColIndex = cursor.getColumnIndex(dBHelper.TASK_COMMENTS);
			String taskComments = cursor.getString(taskCommentsColIndex);
			
			stringBuffer.append(id + " " + projectTitle + " " + programmingLanguages + " " + 
					projectDescription + " " + startTime + " " + timeSpent + " " + productiveLevel + " " + 
					distractionLevel + " " + projectProgress + " " + estimationLevel + " " + taskComments + "\n");
		}
		return stringBuffer.toString();
	}
	
	// display / query all data
	public String getAllData()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		
		// SELECT _id, projectTitle, programmingLanguages, projectDescription FROM projectTable;
		// args:
		// table			table name
		// columns			list of column names you want to access, not always need all
		// selection		like the where clause condition, null means select all
		// selectionArgs	like the where clause value, null if selection is null
		// groupBy			
		// having			
		// orderBy			
		// limit	
		//
		// Cursor can access the returned data as a subset of the table
		String[] columns = {dBHelper.ID, dBHelper.PROJECT_TITLE, 
				dBHelper.PROGRAMMING_LANGUAGES, dBHelper.PROJECT_DESCRIPTION,
				dBHelper.START_TIME, dBHelper.TIME_SPENT, dBHelper.PRODUCTIVE_LEVEL, 
				dBHelper.DISTRACTION_LEVEL, dBHelper.PROJECT_PROGRESS, 
				dBHelper.ESTIMATION_LEVEL, dBHelper.TASK_COMMENTS};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
		// use to store the database by append each cells to it separate by space, and each row separate by newline
		StringBuffer stringBuffer = new StringBuffer();
		// like if can move to next
		while(cursor.moveToNext())
		{
			// TODO:
			// startTime need to convert string to date and time.
			// might have datatype problem, if anything goes wrong use String for both java and sqlite.
			// to get the data from cursor object,
			// first find the integer index representation of the column item,
			// then convert the index to the value of correct type.
			int idColIndex = cursor.getColumnIndex(dBHelper.ID);
			int id = cursor.getInt(idColIndex);
			int projectTitleColIndex = cursor.getColumnIndex(dBHelper.PROJECT_TITLE);
			String projectTitle = cursor.getString(projectTitleColIndex);
			int programmingLanguagesColIndex = cursor.getColumnIndex(dBHelper.PROGRAMMING_LANGUAGES);
			String programmingLanguages = cursor.getString(programmingLanguagesColIndex);
			int projectDescriptionColIndex = cursor.getColumnIndex(dBHelper.PROJECT_DESCRIPTION);
			String projectDescription = cursor.getString(projectDescriptionColIndex);
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int timeSpentColIndex = cursor.getColumnIndex(dBHelper.TIME_SPENT);
			String timeSpent = cursor.getString(timeSpentColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(dBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(dBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			int projectProgressColIndex = cursor.getColumnIndex(dBHelper.PROJECT_PROGRESS);
			String projectProgress = cursor.getString(projectProgressColIndex);
			int estimationLevelColIndex = cursor.getColumnIndex(dBHelper.ESTIMATION_LEVEL);
			String estimationLevel = cursor.getString(estimationLevelColIndex);
			int taskCommentsColIndex = cursor.getColumnIndex(dBHelper.TASK_COMMENTS);
			String taskComments = cursor.getString(taskCommentsColIndex);
					
			stringBuffer.append(id + " " + projectTitle + " " + programmingLanguages + " " + 
					projectDescription + " " + startTime + " " + timeSpent + " " + productiveLevel + " " + 
					distractionLevel + " " + projectProgress + " " + estimationLevel + " " + taskComments + "\n");
		}
		return stringBuffer.toString();
	}
	
	public String getDataById(String mId)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {dBHelper.ID, dBHelper.PROJECT_TITLE, 
				dBHelper.PROGRAMMING_LANGUAGES, dBHelper.PROJECT_DESCRIPTION,
				dBHelper.START_TIME, dBHelper.TIME_SPENT, dBHelper.PRODUCTIVE_LEVEL, 
				dBHelper.DISTRACTION_LEVEL, dBHelper.PROJECT_PROGRESS, 
				dBHelper.ESTIMATION_LEVEL, dBHelper.TASK_COMMENTS};
		String[] selectionArgs = {mId};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, 
				dBHelper.ID + " =?", selectionArgs, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int idColIndex = cursor.getColumnIndex(dBHelper.ID);
			int id = cursor.getInt(idColIndex);
			int projectTitleColIndex = cursor.getColumnIndex(dBHelper.PROJECT_TITLE);
			String projectTitle = cursor.getString(projectTitleColIndex);
			int programmingLanguagesColIndex = cursor.getColumnIndex(dBHelper.PROGRAMMING_LANGUAGES);
			String programmingLanguages = cursor.getString(programmingLanguagesColIndex);
			int projectDescriptionColIndex = cursor.getColumnIndex(dBHelper.PROJECT_DESCRIPTION);
			String projectDescription = cursor.getString(projectDescriptionColIndex);
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int timeSpentColIndex = cursor.getColumnIndex(dBHelper.TIME_SPENT);
			String timeSpent = cursor.getString(timeSpentColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(dBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(dBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			int projectProgressColIndex = cursor.getColumnIndex(dBHelper.PROJECT_PROGRESS);
			String projectProgress = cursor.getString(projectProgressColIndex);
			int estimationLevelColIndex = cursor.getColumnIndex(dBHelper.ESTIMATION_LEVEL);
			String estimationLevel = cursor.getString(estimationLevelColIndex);
			int taskCommentsColIndex = cursor.getColumnIndex(dBHelper.TASK_COMMENTS);
			String taskComments = cursor.getString(taskCommentsColIndex);
			
			stringBuffer.append(id + " " + projectTitle + " " + programmingLanguages + " " + 
					projectDescription + " " + startTime + " " + timeSpent + " " + productiveLevel + " " + 
					distractionLevel + " " + projectProgress + " " + estimationLevel + " " + taskComments + "\n");
		}
		return stringBuffer.toString();
	}
	
	// TODO:
	// for GraphActivity:
	// when selected in spinner, match condition is language with column LIKE '%variable%', 
	//		display start date and productivity/grade,
	// 		the return String have space as column delimiter and new line as row delimiter,
	// 		currently user can only select 1 language, each project can have multiple languages.
	// when selected in graph point, match condition are start date and productivity/grade, display all for now.
	public String getDateGradeByLanguage(String mProgLanguage)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		// return data
		String[] columns = {dBHelper.START_TIME, dBHelper.PRODUCTIVE_LEVEL};
		// match condition values
		String[] selectionArgs = {"%" + mProgLanguage + "%"};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, 
				dBHelper.PROGRAMMING_LANGUAGES + " LIKE ?", selectionArgs, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(dBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			
			if(productiveLevel != null)
				stringBuffer.append(startTime + " " + productiveLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	public String getDateDistractionByLanguage(String mProgLanguage)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		// return data
		String[] columns = {dBHelper.START_TIME, dBHelper.DISTRACTION_LEVEL};
		// match condition values
		String[] selectionArgs = {"%" + mProgLanguage + "%"};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, 
				dBHelper.PROGRAMMING_LANGUAGES + " LIKE ?", selectionArgs, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(dBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			
			if(distractionLevel != null)
				stringBuffer.append(startTime + " " + distractionLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	public String getAllDateGrade()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {dBHelper.START_TIME, dBHelper.PRODUCTIVE_LEVEL};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(dBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			
			if(productiveLevel != null)
				stringBuffer.append(startTime + " " + productiveLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	public String getAllDateDistraction()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {dBHelper.START_TIME, dBHelper.DISTRACTION_LEVEL};
		Cursor cursor = sQLiteDatabase.query(dBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(dBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(dBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			
			if(distractionLevel != null)
				stringBuffer.append(startTime + " " + distractionLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////// schema ////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 *  create database and contain each columns 
	 */
	static class DBHelper extends SQLiteOpenHelper
	{
		private static final String DATABASE_NAME = "tempCodersLiveHackDatabase";
		private static final String TABLE_NAME = "projectTable";
		// if only change schema not version, nothing will change
		// look for logcat error about no column ...
		private static final int DATABASE_VERSION = 1;
		
		// columns
		private static final String ID = "_id";
		private static final String PROJECT_TITLE = "projectTitle";
		private static final String PROGRAMMING_LANGUAGES = "programmingLanguages";
		private static final String PROJECT_DESCRIPTION = "projectDescription";
		private static final String START_TIME = "startTime";
		private static final String TIME_SPENT = "timeSpent";
		private static final String PRODUCTIVE_LEVEL = "productiveLevel";
		private static final String PROJECT_PROGRESS = "projectProgress";
		private static final String DISTRACTION_LEVEL = "distractionLevel";
		private static final String ESTIMATION_LEVEL = "estimationLevel";
		private static final String TASK_COMMENTS = "taskComments";
		
		// TODO:
		// if sqlite datatype DATETIME not work use VARCHAR(255)
		// query to create and drop table
		private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + 
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				PROJECT_TITLE + " VARCHAR(255), " + 
				PROGRAMMING_LANGUAGES + " VARCHAR(255), " + 
				PROJECT_DESCRIPTION + " VARCHAR(255), " + 
				START_TIME + " DATETIME, " + 
				TIME_SPENT + " DOUBLE, " + 
//				START_TIME + " VARCHAR(255), " + 
//				TIME_SPENT + " VARCHAR(255), " + 
				PRODUCTIVE_LEVEL + " VARCHAR(255), " + 
				PROJECT_PROGRESS  + " VARCHAR(255), " + 
				DISTRACTION_LEVEL + " VARCHAR(255), " + 
				TASK_COMMENTS + " VARCHAR(255)," +
				ESTIMATION_LEVEL + " VARCHAR(255));";
		
		private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		
		private Context context;

		// pass Context here from main to adapter by create adapter object,
		// and pass it from adapter by create helper object.
		public DBHelper(Context context)
		{
			// these parameters are for SQLiteOpenHelper
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
			Toast.makeText(context, "constructor called, got Context from main -> adapter -> helper", 
					Toast.LENGTH_LONG).show();
		}

		/*
		 * call when database create the first time, can manually call to recreate 
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try
			{
				// execSQL execute a non select query
				// this helper adapter did not use rawQuery() for select query either, because we use SQLiteOpenHelper
				db.execSQL(CREATE_TABLE);
				Toast.makeText(context, "onCreate called", Toast.LENGTH_LONG).show();
			}
			catch (SQLException e)
			{
				Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
			}
		}

		/*
		 * only update when DATABASE_VERSION increase, 
		 * for update to database schema, not content, 
		 * drop and recreate table
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			try
			{
				Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
				db.execSQL(DROP_TABLE);
				onCreate(db);
			}
			catch (SQLException e)
			{
				Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
			}
		}
	}
}
