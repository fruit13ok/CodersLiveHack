# Coders Live Hack Project

## Tecgnical Specification

1. **Hardware Spec**  
	1. `device`  
This app will work on android devices.
	2. `minSdkVersion`  
Oldest android phone can run on: API 14, Version 4.0.1 - 4.0.2, Ice Cream Sandwich.
	3. `targetSdkVersion`  
Newest android phone can run on: API 21, Version 5.0, Lollipop.


2. **Database**  
	1. `related files`  
DBHelperAdapter.java

	2. `SQLite local internal database`  
Database is bind with the app, data should only be access by the app, database will be remove when user remove the app.  
We are not resposible for data lost or corruption.  
Current no feature for user to transfer database.  
Developer can menually access database if user IDE's emulator, or by unlock your phone's root user account at your own risk.

	3. `database API`  
SQLiteDatabase, SQLiteOpenHelper  
Using SQLiteDatabase and SQLiteOpenHelper to create a helper and adapter class to manage database. Use SQLiteDatabase methods to interact with database.

	4. `database table`  

add screen shoots...


3. **General Layout**  
	1. `screenOrientation`  
Orientations are set to fix, no rotation.


4. **Homepage**  
	1. `related files`  
CodersLiveHackActivity.java, PagerAdapter.java, FragmentAssessGuide.java, FragmentCreateTaskGuide.java, FragmentGitGuide.java, FragmentGraphGuide.java, FragmentHomePageGuide.java, FragmentSelectTaskGuide.java, FragmentTimerGuide.java, activity_coders_live_hack.xml, fragment_assessguide_layout.xml, fragment_createtaskguide_layout.xml, fragment_gitguide_layout.xml, fragment_graphguide_layout.xml, fragment_homepageguide_layout.xml, fragment_selecttaskguide_layout.xml, fragment_timerguide_layout.xml, android-support-v4.jar

	2. `ViewPager`  
It use for displaying user guides.
		1. `Fragment`  
		Each page in ViewPager is a Fragment with layout.
		2. `FragmentPagerAdapter`  
		Manage swipe through each Fragment.
	3. `Button`  
There are 4 buttons for nevigate to 4 pages:  
Create task page, Select task page, Graph page, GitHub commit page.

add screen shoots...


5. **Create task page**  
	1. `related files`  
CreateTaskActivity.java, activity_create_task.xml

	2. `EditText`  
There are 3 text fields to input project info.
	3. `Button`  
There is 1 button to submit the input to database.  
There are 2 buttons for nevigate to 2 pages:  
Home page, Select task page

add screen shoots...


6. **Select task page**  
	1. `related files`  
SelectTaskActivity.java, activity_select_task.xml, select_list_item.xml

	2. `TextView`  
Show color coded progress info.
	3. `ListView`  
Display each recorded project.
		1. `ArrayAdapter`  
		Customize the ArrayAdapter with a our own View.
	4. `Button`  
There are 2 buttons for nevigate to 2 pages:  
Work timer page, Home page.

add screen shoots...


7. **Work timer page**  
	1. `related files`  
TimerActivity.java, activity_timer.xml

	2. `TextView`  
There are 2 text fields for label and display project info.  
There are 2 text fields for display timer info and timer.
	3. `Button`  
There are 4 buttons use to control timer.  
There are 2 buttons use to change project progress.  
There are 2 buttons for nevigate to 2 pages:  
Home page, Self assessment page.

add screen shoots...


8. Self assessment page
	1 `related files`  
AssessActivity.java, activity_assess.xml, array_self_assetment.xml

	2. `TextView`  
There are 2 text fields for label 2 dropdown lists.
	3. `Spinner`  
There are 2 dropdown lists for select level of productivity and distraction to put to database.
	4. `EditText`  
Use for input optional comment of the project to database.
	5. `Button`  
There is 1 button use to submit the input to database.  
There are 2 buttons for nevigate to 2 pages:  
Home page, Graph page.

add screen shoots...


9. **Graph page**  
	1. `related files`  
GraphActivity.java, activity_graph.xml, GraphView-4.0.1.jar

	2. `Spinner`  
A dropdown list of programming languages of fully finished project.
	3. `GraphView`  
This 3rd party API use to do graph to show project grade and distraction. Points inside are selectable.
	4. `TextView`  
It use to display selected project info.
	5. `Button`  
There is 1 button to confirm the dropdown list selection.  
There is 1 button for nevigate to 1 pages:  
Home page

add screen shoots...


10. **GitHub commit page**  
	1. `related files`  
GetGitCommitActivity.java, activity_get_git_commit.xml

	2. `TextView`  
There are 2 text fields for label github account name and repository name.  
There is 1 text field for display fetch result of number of commit.
	3. `EditText`  
There are 2 input text fields for input user's project github account name and repository name.
	4. `Button`  
There is 1 button for submit the user input and do fetch.  
There is 1 button for nevigate to 1 pages:  
Home page

add screen shoots...


11. **3rd party API**  
	1. `GraphView 4.0.4`  
Used to drew graph.

add url...


12. **Utility created**  
...


13. **Assets**  
	1. `graphical tool`  
		1. `Markdown`  
		Used to write and format this tech spec.
		2. `GIMP`  
		Used to edit image.
		3. `MS-Paint`  
		Used to edit image.
	2. `general styles`  
		1. `App icon`  
		add image ...
		2. `Colors`  
		StatusBar #303F9F  
		ActionBar #3F51B5  
		Button #3949ab  
		TextView #e8eaf6  
		TextView (Guide) #ffc400
		3. `Font`  
		Roboto (Regular)

14. **Side note**  
First default datapoint in the graph is alway touching x-axis. It is used for initial point on the graph inorder to drew a line.


	

