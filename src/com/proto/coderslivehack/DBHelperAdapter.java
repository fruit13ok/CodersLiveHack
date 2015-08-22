package com.proto.coderslivehack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
 * TODO: this database is up for change later.
 * 
 * side note: on API level 16 CAN DELETE WHOLE DATABASE !!! with deleteDatabase(File)
 */
/**
 * Local SQLite database in the phone bind with this app.<br>
 * This database has 1 table to store project data.<br>
 * 
 * NOTE: possible future upgrade can be make the database transferable.
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
	
	// TODO:
	// make title to be unique
	// 
	// SQLITE way looks problematic, I am not going to use:
	// CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + 
	// ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	// PROJECT_TITLE + " VARCHAR(255) UNIQUE, " + ...
	// and
	// sQLiteDatabase.insertWithOnConflict()
	//
	// Currently I use java, query all unique title, compare to current input title
	
	/**
	 * Use to insert data to database, return inserted row id or -1 for fail to insert.<br>
	 * Use by CreateTaskActivity.
	 * @param projectTitle
	 * @param programmingLanguages
	 * @param projectDescription
	 * @return id or -1 for fail
	 */
	public long insertData(String projectTitle, String programmingLanguages, String projectDescription)
	{
		if(!projectTitle.trim().isEmpty() && !programmingLanguages.trim().isEmpty() && 
				!projectDescription.trim().isEmpty())
		{
			// need SQLiteDatabase object to point to your database before you can do anything
			SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
			
			// ContentValues key value pair use to referring database columns
			ContentValues contentValues = new ContentValues();
			// TODO: later make sure projectTitle, programmingLanguages, and projectDescription not empty
			if(!projectTitle.isEmpty())
				contentValues.put(DBHelper.PROJECT_TITLE, projectTitle);
			if(!programmingLanguages.isEmpty())
				contentValues.put(DBHelper.PROGRAMMING_LANGUAGES, programmingLanguages);
			if(!projectDescription.isEmpty())
				contentValues.put(DBHelper.PROJECT_DESCRIPTION, projectDescription);
			
			contentValues.put(DBHelper.PROJECT_PROGRESS, "not start");
			
			// TODO: live it here I might not want to use default null value 
//			contentValues.put(DBHelper.START_TIME, "null");
//			contentValues.put(DBHelper.TIME_SPENT, "null");
//			contentValues.put(DBHelper.PRODUCTIVE_LEVEL, "null");
//			contentValues.put(DBHelper.PROJECT_PROGRESS, "null");
//			contentValues.put(DBHelper.DISTRACTION_LEVEL, "null");
			
			// TODO: might need to allow null value for database
			// 2nd arg nullColumnHack is to allow to insert null as database value by specify any column name,
			// null means not allow null value
			// id can use to check for insert error if return -1
			long id = sQLiteDatabase.insert(DBHelper.TABLE_NAME, null, contentValues);
			sQLiteDatabase.close();
			return id;
		}
		else
		{
			return -1;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// update content ///////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * update project distraction value to database table.
	 * @param project_Title
	 * @param distractionValue
	 * @return id or -1 for fail
	 */
	public long insert_DistractionLevel(String project_Title,String distractionValue)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.DISTRACTION_LEVEL, distractionValue);
		String[] whereArgs={project_Title};
		return db.update(DBHelper.TABLE_NAME,
				contentValues, DBHelper.PROJECT_TITLE + "=?", whereArgs);
	}

	/**
	 * update project productivity value to database table.
	 * @param project_Title
	 * @param productivityValues
	 * @return id or -1 for fail
	 */
	public long insert_ProductivityValues(String project_Title,String productivityValues)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.PRODUCTIVE_LEVEL, productivityValues);
		String[] whereArgs={project_Title};
		return db.update(DBHelper.TABLE_NAME,
				contentValues, DBHelper.PROJECT_TITLE + "=?", whereArgs);
	}

	/**
	 * update project optional task comment value to database table.
	 * @param project_Title
	 * @param taskCommentsValues
	 * @return id or -1 for fail
	 */
	public long insert_TaskComments(String project_Title,String taskCommentsValues)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.TASK_COMMENTS, taskCommentsValues);
		String[] whereArgs={project_Title};
		return db.update(DBHelper.TABLE_NAME,
				contentValues, DBHelper.PROJECT_TITLE + "=?", whereArgs);
	}
	
	/**
	 * update project time spent value to database table.
	 * @param project_Title
	 * @param timeSpentValue
	 * @return id or -1 for fail
	 */
	public long insert_TimeSpent(String project_Title,String timeSpentValue)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.TIME_SPENT, timeSpentValue);
		String[] whereArgs={project_Title};
		return db.update(DBHelper.TABLE_NAME,
				contentValues, DBHelper.PROJECT_TITLE + "=?", whereArgs);
	}
	
	/**
	 * update project progress to database table.
	 * @param project_Title
	 * @param projectProgress
	 * @return id or -1 for fail
	 */
	public long updateProjectProgress(String project_Title,String projectProgress)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.PROJECT_PROGRESS, projectProgress);
		String[] whereArgs={project_Title};
		return db.update(DBHelper.TABLE_NAME,
				contentValues, DBHelper.PROJECT_TITLE + "=?", whereArgs);
	}
	
	/**
	 * update project start time to database table.<br>
	 * SHOULD ONLY call this method ONCE per project.
	 * @param project_Title
	 * @param startTime
	 * @return id or -1 for fail
	 */
	public long updateStartTime(String project_Title,String startTime)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.START_TIME, startTime);
		String[] whereArgs={project_Title};
		return db.update(DBHelper.TABLE_NAME,
				contentValues, DBHelper.PROJECT_TITLE + "=?", whereArgs);
	}
	
	/**
	 * dummy method update the row by match id, use by developer only, delete it later.<br>
	 * I use this method to make dummy records for graph.
	 * @param mId
	 * @param mTitle
	 * @param mLanguage
	 * @param mDescription
	 * @param mStartTime
	 * @param mTimeSpent
	 * @param mProductivie
	 * @param mDistraction
	 * @param mProgress
	 * @param mComment
	 * @return id or -1 for fail
	 */
	public int updateARowById(String mId, String mTitle, String mLanguage, String mDescription, 
			String mStartTime, String mTimeSpent, String mProductivie, String mDistraction, 
			String mProgress, String mComment)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		if(!mTitle.isEmpty())
			contentValues.put(DBHelper.PROJECT_TITLE, mTitle);
		if(!mLanguage.isEmpty())
			contentValues.put(DBHelper.PROGRAMMING_LANGUAGES, mLanguage);
		if(!mDescription.isEmpty())
			contentValues.put(DBHelper.PROJECT_DESCRIPTION, mDescription);
		if(!mStartTime.isEmpty())
			contentValues.put(DBHelper.START_TIME, mStartTime);
		if(!mTimeSpent.isEmpty())
			contentValues.put(DBHelper.TIME_SPENT, mTimeSpent);
		if(!mProductivie.isEmpty())
			contentValues.put(DBHelper.PRODUCTIVE_LEVEL, mProductivie);
		if(!mDistraction.isEmpty())
			contentValues.put(DBHelper.DISTRACTION_LEVEL, mDistraction);
		if(!mProgress.isEmpty())
			contentValues.put(DBHelper.PROJECT_PROGRESS, mProgress);
		if(!mComment.isEmpty())
			contentValues.put(DBHelper.TASK_COMMENTS, mComment);
		
		String[] whereArgs = {mId};
		
		int count = sQLiteDatabase.update(DBHelper.TABLE_NAME, contentValues, DBHelper.ID + " =?", whereArgs);
		return count;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// select query //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * get optional project comment
	 * @param project_Title
	 * @return String 
	 */
	public String get_TaskComment(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.TASK_COMMENTS};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(DBHelper.TABLE_NAME,
				columns, DBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(DBHelper.TASK_COMMENTS);
			String taskCom = cursor.getString(index2);

			buffer.append(taskCom);
		}
		return buffer.toString();
	}

	/**
	 * get project distraction level
	 * @param project_Title
	 * @return String
	 */
	public String get_DistractionLevel(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.DISTRACTION_LEVEL};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(DBHelper.TABLE_NAME,
				columns, DBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(DBHelper.DISTRACTION_LEVEL);
			int disLvl = cursor.getInt(index2);

			buffer.append(disLvl);
		}
		return buffer.toString();
	}

	/**
	 * get project productivity level
	 * @param project_Title
	 * @return String
	 */
	public String get_ProductivityLevel(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.PRODUCTIVE_LEVEL};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(DBHelper.TABLE_NAME,
				columns, DBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(DBHelper.PRODUCTIVE_LEVEL);
			int prodLvl = cursor.getInt(index2);

			buffer.append(prodLvl);
		}
		return buffer.toString();
	}
	
	/**
	 * get project time spent
	 * @param project_Title
	 * @return String
	 */
	public String get_TimeSpent(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.TIME_SPENT};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(DBHelper.TABLE_NAME,
				columns, DBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(DBHelper.TIME_SPENT);
			String timeSpent = cursor.getString(index2);

			buffer.append(timeSpent);
		}
		return buffer.toString();
	}
	
	/**
	 * get project progress
	 * @param project_Title
	 * @return String
	 */
	public String getProjectProgress(String project_Title)
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.PROJECT_PROGRESS};
		String[] selectionArgs ={project_Title};
		StringBuffer buffer = new StringBuffer();
		Cursor cursor = db.query(DBHelper.TABLE_NAME,
				columns, DBHelper.PROJECT_TITLE + "=?", selectionArgs,
				null, null, null);

		while(cursor.moveToNext())
		{
			int index2  = cursor.getColumnIndex(DBHelper.PROJECT_PROGRESS);
			String projectProgress = cursor.getString(index2);

			buffer.append(projectProgress);
		}
		return buffer.toString();
	}
	
	/**
	 * get all computer languages of projects
	 * I use distinct = true to get unique language name, and use orderby to ascending. 
	 * @return String of none or more languages
	 */
	public String getAllLanguages()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.PROGRAMMING_LANGUAGES};
		Cursor cursor = sQLiteDatabase.query(true, DBHelper.TABLE_NAME, columns, null, null, null, null, 
				DBHelper.PROGRAMMING_LANGUAGES + " ASC", null);
		StringBuffer stringBuffer = new StringBuffer();
		
		while(cursor.moveToNext())
		{
			int programmingLanguagesColIndex = cursor.getColumnIndex(DBHelper.PROGRAMMING_LANGUAGES);
			String programmingLanguages = cursor.getString(programmingLanguagesColIndex);

			stringBuffer.append(programmingLanguages + "|");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * get all project titles.<br>
	 * current version title should be unique. 
	 * @return String of none more more project titles
	 */
	public String getAllProjectTitles()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.PROJECT_TITLE};
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		
		while(cursor.moveToNext())
		{
			int projectTitleColIndex = cursor.getColumnIndex(DBHelper.PROJECT_TITLE);
			String projectTitle = cursor.getString(projectTitleColIndex);
			stringBuffer.append(projectTitle + "|");
		}
		return stringBuffer.toString();
	}

	/**
	 * get all the data of a project by given date and grade.<br>
	 * <br>
	 * NOTE: This method need to upgrade next time (my need to upgrade outside of database too),<br>
	 * in order to deal with same date same grade, which cause only showing 1 point on graph.
	 * @param mDate
	 * @param mGrade
	 * @param delimiter
	 * @return String of all data of project(s) matched date and grade
	 */
	public String getAllDataByDateAndGrade(String mDate, String mGrade,String delimiter)
    {
        SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
        // return data
        String[] columns = {DBHelper.ID, DBHelper.PROJECT_TITLE,
                DBHelper.PROGRAMMING_LANGUAGES, DBHelper.PROJECT_DESCRIPTION,
                DBHelper.START_TIME, DBHelper.TIME_SPENT, DBHelper.PRODUCTIVE_LEVEL,
                DBHelper.DISTRACTION_LEVEL, DBHelper.PROJECT_PROGRESS,
                DBHelper.TASK_COMMENTS};
        // match condition values
        String[] selectionArgs = {"%" + mDate + "%", "%" + mGrade + "%"};
//        Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns,
//                DBHelper.START_TIME + " LIKE ? AND " + DBHelper.PRODUCTIVE_LEVEL + " LIKE ?",
//                selectionArgs, null, null, null, null);
        Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns,
                DBHelper.START_TIME + " LIKE ? AND " + DBHelper.PRODUCTIVE_LEVEL + " LIKE ?",
                selectionArgs, null, null, DBHelper.START_TIME + " ASC", null);
        
        StringBuffer stringBuffer = new StringBuffer();
        while(cursor.moveToNext())
        {
            int idColIndex = cursor.getColumnIndex(DBHelper.ID);
            int id = cursor.getInt(idColIndex);
            int projectTitleColIndex = cursor.getColumnIndex(DBHelper.PROJECT_TITLE);
            String projectTitle = cursor.getString(projectTitleColIndex);
            int programmingLanguagesColIndex = cursor.getColumnIndex(DBHelper.PROGRAMMING_LANGUAGES);
            String programmingLanguages = cursor.getString(programmingLanguagesColIndex);
            int projectDescriptionColIndex = cursor.getColumnIndex(DBHelper.PROJECT_DESCRIPTION);
            String projectDescription = cursor.getString(projectDescriptionColIndex);
            int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
            String startTime = cursor.getString(startTimeColIndex);
            int timeSpentColIndex = cursor.getColumnIndex(DBHelper.TIME_SPENT);
            String timeSpent = cursor.getString(timeSpentColIndex);
            int productiveLevelColIndex = cursor.getColumnIndex(DBHelper.PRODUCTIVE_LEVEL);
            String productiveLevel = cursor.getString(productiveLevelColIndex);
            int distractionLevelColIndex = cursor.getColumnIndex(DBHelper.DISTRACTION_LEVEL);
            String distractionLevel = cursor.getString(distractionLevelColIndex);
            int projectProgressColIndex = cursor.getColumnIndex(DBHelper.PROJECT_PROGRESS);
            String projectProgress = cursor.getString(projectProgressColIndex);
            int taskCommentsColIndex = cursor.getColumnIndex(DBHelper.TASK_COMMENTS);
            String taskComments = cursor.getString(taskCommentsColIndex);

            stringBuffer.append(id + delimiter + projectTitle + delimiter + programmingLanguages + 
            		delimiter + projectDescription + delimiter + startTime + delimiter + timeSpent + 
            		delimiter + productiveLevel + delimiter + distractionLevel + delimiter + 
            		projectProgress + delimiter + taskComments + "\n");
        }
        return stringBuffer.toString();
    }
	
	/**
	 * get all data
	 * @return String of all data
	 */
	public String getAllData()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		
		// SELECT _id, projectTitle, programmingLanguages, projectDescription FROM projectTable;
		// args:
		// CursorFactory	???
		// distinct			unique
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
		String[] columns = {DBHelper.ID, DBHelper.PROJECT_TITLE, 
				DBHelper.PROGRAMMING_LANGUAGES, DBHelper.PROJECT_DESCRIPTION,
				DBHelper.START_TIME, DBHelper.TIME_SPENT, DBHelper.PRODUCTIVE_LEVEL, 
				DBHelper.DISTRACTION_LEVEL, DBHelper.PROJECT_PROGRESS, 
				DBHelper.TASK_COMMENTS};
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
		// use to store the database by append each cells to it separate by space, and each row separate by newline
		StringBuffer stringBuffer = new StringBuffer();
		// if can move to next then move
		while(cursor.moveToNext())
		{
			// TODO:
			// startTime need to convert string to date and time.
			// might have datatype problem, if anything goes wrong use String for both java and sqlite.
			// to get the data from cursor object,
			// first find the integer index representation of the column item,
			// then convert the index to the value of correct type.
			int idColIndex = cursor.getColumnIndex(DBHelper.ID);
			int id = cursor.getInt(idColIndex);
			int projectTitleColIndex = cursor.getColumnIndex(DBHelper.PROJECT_TITLE);
			String projectTitle = cursor.getString(projectTitleColIndex);
			int programmingLanguagesColIndex = cursor.getColumnIndex(DBHelper.PROGRAMMING_LANGUAGES);
			String programmingLanguages = cursor.getString(programmingLanguagesColIndex);
			int projectDescriptionColIndex = cursor.getColumnIndex(DBHelper.PROJECT_DESCRIPTION);
			String projectDescription = cursor.getString(projectDescriptionColIndex);
			int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int timeSpentColIndex = cursor.getColumnIndex(DBHelper.TIME_SPENT);
			String timeSpent = cursor.getString(timeSpentColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(DBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(DBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			int projectProgressColIndex = cursor.getColumnIndex(DBHelper.PROJECT_PROGRESS);
			String projectProgress = cursor.getString(projectProgressColIndex);
			int taskCommentsColIndex = cursor.getColumnIndex(DBHelper.TASK_COMMENTS);
			String taskComments = cursor.getString(taskCommentsColIndex);
					
			stringBuffer.append(id + "|" + projectTitle + "|" + programmingLanguages + "|" + 
					projectDescription + "|" + startTime + "|" + timeSpent + "|" + productiveLevel + "|" + 
					distractionLevel + "|" + projectProgress+ "|" + taskComments + "\n");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * get all data by match given id.<br>
	 * use by the dummy page.
	 * @return String of all data
	 */
	public String getDataById(String mId)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.ID, DBHelper.PROJECT_TITLE, 
				DBHelper.PROGRAMMING_LANGUAGES, DBHelper.PROJECT_DESCRIPTION,
				DBHelper.START_TIME, DBHelper.TIME_SPENT, DBHelper.PRODUCTIVE_LEVEL, 
				DBHelper.DISTRACTION_LEVEL, DBHelper.PROJECT_PROGRESS, 
				DBHelper.TASK_COMMENTS};
		String[] selectionArgs = {mId};
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, 
				DBHelper.ID + " =?", selectionArgs, null, null, null, null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int idColIndex = cursor.getColumnIndex(DBHelper.ID);
			int id = cursor.getInt(idColIndex);
			int projectTitleColIndex = cursor.getColumnIndex(DBHelper.PROJECT_TITLE);
			String projectTitle = cursor.getString(projectTitleColIndex);
			int programmingLanguagesColIndex = cursor.getColumnIndex(DBHelper.PROGRAMMING_LANGUAGES);
			String programmingLanguages = cursor.getString(programmingLanguagesColIndex);
			int projectDescriptionColIndex = cursor.getColumnIndex(DBHelper.PROJECT_DESCRIPTION);
			String projectDescription = cursor.getString(projectDescriptionColIndex);
			int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int timeSpentColIndex = cursor.getColumnIndex(DBHelper.TIME_SPENT);
			String timeSpent = cursor.getString(timeSpentColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(DBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(DBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			int projectProgressColIndex = cursor.getColumnIndex(DBHelper.PROJECT_PROGRESS);
			String projectProgress = cursor.getString(projectProgressColIndex);
			int taskCommentsColIndex = cursor.getColumnIndex(DBHelper.TASK_COMMENTS);
			String taskComments = cursor.getString(taskCommentsColIndex);
			
			stringBuffer.append(id + "|" + projectTitle + "|" + programmingLanguages + "|" + 
					projectDescription + "|" + startTime + "|" + timeSpent + "|" + productiveLevel + "|" + 
					distractionLevel + "|" + projectProgress + "|" + taskComments + "\n");
		}
		return stringBuffer.toString();
	}

	/**
	 * TODO: should change the graph from using start date to complete date,
	 * it is not a solution to the same date same grade problem, but it should be better.
	 * <br><br>
	 * for GraphActivity:
	 * when selected in spinner, match condition is language with column LIKE '%variable%', 
	 *		display start date and productivity/grade,
	 * 		the return String have space as column delimiter and new line as row delimiter,
	 * 		currently user can only select 1 language, each project can have multiple languages.
	 * <br>
	 * The return START_TIME must be in ascending order because GraphView x-axis is refer to date.
	 * 
	 * @param mProgLanguage
	 * @return String of all date and grade match the given language.
	 */
	public String getDateGradeByLanguage(String mProgLanguage)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		// return data
		String[] columns = {DBHelper.START_TIME, DBHelper.PRODUCTIVE_LEVEL};
		// match condition values
		String[] selectionArgs = {"%" + mProgLanguage + "%"};
		// match language and return in ascending order
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, 
				DBHelper.PROGRAMMING_LANGUAGES + " LIKE ?", selectionArgs, null, null, 
				DBHelper.START_TIME + " ASC", null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(DBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			
			if(productiveLevel != null)
				stringBuffer.append(startTime + "|" + productiveLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * TODO: should change the graph from using start date to complete date,
	 * same issue as getDateGradeByLanguage() method.
	 * <br><br>
	 * for GraphActivity:
	 * when selected in spinner, match condition is language with column LIKE '%variable%', 
	 *		display start date and distraction,
	 * 		the return String have space as column delimiter and new line as row delimiter,
	 * 		currently user can only select 1 language, each project can have multiple languages.
	 * <br>
	 * The return START_TIME must be in ascending order because GraphView x-axis is refer to date.
	 * 
	 * @param mProgLanguage
	 * @return String of all date and distraction match the given language.
	 */
	public String getDateDistractionByLanguage(String mProgLanguage)
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		// return data
		String[] columns = {DBHelper.START_TIME, DBHelper.DISTRACTION_LEVEL};
		// match condition values
		String[] selectionArgs = {"%" + mProgLanguage + "%"};
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, 
				DBHelper.PROGRAMMING_LANGUAGES + " LIKE ?", selectionArgs, null, null, 
				DBHelper.START_TIME + " ASC", null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(DBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			
			if(distractionLevel != null)
				stringBuffer.append(startTime + "|" + distractionLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * get all date and grade.
	 * @return String of all date and grade
	 */
	public String getAllDateGrade()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.START_TIME, DBHelper.PRODUCTIVE_LEVEL};
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, null, null, null, null, 
				DBHelper.START_TIME + " ASC", null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int productiveLevelColIndex = cursor.getColumnIndex(DBHelper.PRODUCTIVE_LEVEL);
			String productiveLevel = cursor.getString(productiveLevelColIndex);
			
			if(productiveLevel != null)
				stringBuffer.append(startTime + "|" + productiveLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	/**
	 * get all date and distraction
	 * @return String of all date and distraction
	 */
	public String getAllDateDistraction()
	{
		SQLiteDatabase sQLiteDatabase = dBHelper.getWritableDatabase();
		String[] columns = {DBHelper.START_TIME, DBHelper.DISTRACTION_LEVEL};
		Cursor cursor = sQLiteDatabase.query(DBHelper.TABLE_NAME, columns, null, null, null, null, 
				DBHelper.START_TIME + " ASC", null);
		StringBuffer stringBuffer = new StringBuffer();
		while(cursor.moveToNext())
		{
			int startTimeColIndex = cursor.getColumnIndex(DBHelper.START_TIME);
			String startTime = cursor.getString(startTimeColIndex);
			int distractionLevelColIndex = cursor.getColumnIndex(DBHelper.DISTRACTION_LEVEL);
			String distractionLevel = cursor.getString(distractionLevelColIndex);
			
			if(distractionLevel != null)
				stringBuffer.append(startTime + "|" + distractionLevel + "\n");
		}
		return stringBuffer.toString();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////// schema ////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * create database, table, and contain each columns, using SQLiteOpenHelper
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
				TASK_COMMENTS + " VARCHAR(255));";
		
		private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		
		private Context context;

		/**
		 * pass Context to this constructor from main to adapter by create adapter object,
		 * and pass it from adapter by create helper object.
		 * <br><br>
		 * EX:
		 * constructor called, got Context from main -> adapter -> helper
		 * @param context
		 */
		public DBHelper(Context context)
		{
			// these parameters are for SQLiteOpenHelper
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
			// TODO: remove testing toast later
			Toast.makeText(context, "constructor called, got Context from main -> adapter -> helper", 
					Toast.LENGTH_LONG).show();
		}

		/*
		 *  call when database create the first time, can manually call to recreate
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try
			{
				// execSQL execute a non select query
				// this helper adapter did not use rawQuery() for select query either, 
				// because we use SQLiteOpenHelper
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
	
	/**
	 * check if database and its table got created.<br>
	 * as long as this helper adapter class got created database and table schema will be created.
	 * @return boolean
	 */
	public boolean isTableExists()
	{
		SQLiteDatabase mDatabase = dBHelper.getReadableDatabase();
	    Cursor cursor = mDatabase.rawQuery(
	    		"select DISTINCT tbl_name from sqlite_master where tbl_name = '"+
	    				"projectTable"+"'", null);
	    if(cursor!=null)
	    {
	        if(cursor.getCount()>0)
	        {
	            cursor.close();
	            return true;
	        }
	        cursor.close();
	    }
	    return false;
	}
	
	/**
	 * check if any data exist, it mean at least 1 project created store in database.<br>
	 * if true allow "select" page, not "graph" pages.<br>
	 * if false not allow "select" and "graph" pages.
	 * @return boolean
	 */
	public boolean isDataExists()
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		Cursor mcursor = db.rawQuery("SELECT * FROM "+"projectTable", null);
		if(mcursor!=null)
	    {
	        if(mcursor.getCount()>0)
	        {
	        	mcursor.close();
	            return true;
	        }
	        mcursor.close();
	    }
	    return false;
	}
	
	/**
	 * check if graph data exist, it mean at least 1 project totally done and assessed.
	 * if true allow "graph" pages.<br>
	 * if false not allow "graph" pages.
	 * @return boolean
	 */
	public boolean isGraphableDataExists()
	{
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		Cursor ncursor = db.rawQuery(
				"SELECT * FROM "+"projectTable"+
						" WHERE productiveLevel IS NOT null OR productiveLevel != ''", null);
		if(ncursor!=null)
	    {
	        if(ncursor.getCount()>0)
	        {
	        	ncursor.close();
	            return true;
	        }
	        ncursor.close();
	    }
	    return false;
	}
}
