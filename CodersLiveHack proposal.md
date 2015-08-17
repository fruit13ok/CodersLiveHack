# Coders Live Hack Project

### Original Prpposal

1. **Project Idea**  
CodersLiveHack, the productivity tracker app for rookie  programmers inspired by the Pomodoro Technique. CodersLiveHack helps rookie  programmers measure productivity, track tasks, and  focus their time.  The application  measures coders' learning by keeping track of their hours  of study on a particular  programming subject, similar to Pomodoro Technique. It retrieves commit data from their GitHub account to track their productivity. Coders' learning and productivity is  displayed as a graph so they can see their progress easily. CodersLiveHack’s  goal is to inspire new coders while finding out their strengths and weaknesses as a programmer.  

2. **Development approach**  
Development of CodersLiveHack consist of 10 milestones completed over the course of 10 weeks. Early milestones focus on creating a minimum viable product for users, the second half of milestones is reserved for refinement, debugging and documentation of application. CodersLiveHack will be built as a native android application.  

  1. `Target user`  
  CodersLiveHack target users are software development students. Students use this app when student begin learning programming can record more studying data possible to facilitate student future learning. It is a productivity tracking app similar to Pomodoro Technique. Many people already know how to use Pomodoro for many years, so this app can attract user from both students, teachers, and parents. It also able to extract commit data from GitHub which unique from other Pomodoro app, most software developers need to use GitHub, it is good to teach programming student to learn the basic usage of GitHub.

  2. `usage`  
    1. At the start of the app the user generates a set of task.
    2. After the user has selected a task to start they hit the start button and a timer will start and their Github commits will be recorded.
    3. When the user has finished the task they hit the stop button and are shown a graphical view of their latest session.

  3. `Definition`  
    1. `Pomodoro Technique`: The Pomodoro Technique is a time management method developed by Francesco Cirillo in the late 1980s. [Pomodoro Technique wikipedia](http://en.wikipedia.org/wiki/Pomodoro_Technique)  
    2. `GitHub`: GitHub is a web-based Git repository hosting service, which offers all of the distributed revision control and source code management (SCM) functionality of Git as well as adding its own features. [GitHub wikipedia](http://en.wikipedia.org/wiki/GitHub)

  4 `App interfaces`  
    1. `Home page`  
    Home page when the app start. It can go to Setup task list page, Request GitHub commit data page, and Graph setup page.  
    This page have all instructions. Press tab or button to task list page.  
    ![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_home.png)
    2. `Setup task list page`  
    Use EditText to input each new task and save into ListView to show the to-do list of the day.  
    User can categorize their task for search.  
    All user input is part of 1 new row to the database.  
    ![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_create.png)
    3. `Tracking timer page`  
    Timer will time each task, use 4 Button to interact with the timer: Start, pause, complete, abandon.  
    -pause: Only allow to pause in the current timer section, now just manual pause.  
    -section: No save section, each section ends and record of fraction of the total timeframe (2 hour / 4 hour).
    ![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_timer.png)![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_assess.png)  
    4. `Request GitHub commit data page`  
    Use HttpURLConnection to request JSON data from GitHub by specifying repository and account.  
    Use EditText to input repository and user account name.  
    ![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_git.png)  
    5. `Graph setup page`  
    Use EditText to input time range, keyword, category of tracked data.  
    Search result from database will display in a list. User select the data for graph.  
    Use Spinner to select type of graph.  
    ![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_graph_filter.png)
    6. `Graph result page`  
    Use either GraphView or AChartEngine library to draw bar chart and line graph.  
    -Line graph: Each dot is a project, number of project to display depends on user filter  
    -Bar graph: Each bar is a task of one single project  
    ![alt text](http://hills.ccsf.edu/~yliu192/CodersLiveHack/propose_graph.png)
    7. `Database`  
    Use SQLite database to store recorded data.  
    Not store GitHub data, commit data can be change, request each time.  
    -schema: _id, proj name, proj task, project tag (user define), languages, project start timestamp (x-axis date and time), task time spent until complete or abandon (y-axis duration), total project time spend, productive level, distraction level.  
      
