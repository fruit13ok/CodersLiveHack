package com.proto.coderslivehack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
 * TODO: currently just try to make a temp database, database need to change later
 * 
 * side note: on API level 16 CAN DELETE WHOLE DATABASE !!! with deleteDatabase(File)
 */
public class DBHelperAdapter
{
	DBHelper dBHelper;
	
	// use to pass in Context, like last one does in main
	public DBHelperAdapter(Context context)
	{
		dBHelper = new DBHelper(context);
	}
	
	// now only use by CreateTaskActivity
	// use to insert data to database, return inserted row id or -1 for fail to insert
	public long insertData(String projectTitle, String programmingLanguages, String projectDescription)
	{
		// need SQLiteDatabase object to point to your database before you can do anything
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		
		// ContentValues key value pair use to referring database columns
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.PROJECT_TITLE, projectTitle);
		contentValues.put(DBHelper.PROGRAMMING_LANGUAGES, programmingLanguages);
		contentValues.put(DBHelper.PROJECT_DESCRIPTION, projectDescription);
		// TODO: live it here I might not want to use default null value 
//		contentValues.put(DBHelper.START_TIME, "null");
//		contentValues.put(DBHelper.TIME_SPENT, "null");
//		contentValues.put(DBHelper.PRODUCTIVE_LEVEL, "null");
//		contentValues.put(DBHelper.DISTRACTION_LEVEL, "null");
		
		// TODO: might need to allow null value for database
		// 2nd arg nullColumnHack is to allow to insert null as database value by specify any column name,
		// null means not allow null value
		// id can use to check for insert error if return -1
		long id = sQLiteDatabase.insert(DBHelper.TABLE_NAME, null, contentValues);
		sQLiteDatabase.close();
		return id;
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
				dBHelper.START_TIME, dBHelper.TIME_SPENT, dBHelper.PRODUCTIVE_LEVEL, dBHelper.DISTRACTION_LEVEL};
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
			
//			stringBuffer.append(id + " " + projectTitle + " " + programmingLanguages + " " + projectDescription + " " + 
//					startTime + "\n");
					
			stringBuffer.append(id + " " + projectTitle + " " + programmingLanguages + " " + projectDescription + " " + 
					startTime + " " + timeSpent + " " + productiveLevel + " " + distractionLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
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
		
		// 4 columns
		private static final String ID = "_id";
		private static final String PROJECT_TITLE = "projectTitle";
		private static final String PROGRAMMING_LANGUAGES = "programmingLanguages";
		private static final String PROJECT_DESCRIPTION = "projectDescription";
		private static final String START_TIME = "startTime";
		private static final String TIME_SPENT = "timeSpent";
		private static final String PRODUCTIVE_LEVEL = "productiveLevel";
		private static final String DISTRACTION_LEVEL = "distractionLevel";
		
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
				DISTRACTION_LEVEL + " VARCHAR(255));";
		
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
