# Coders Live Hack Project  

## Milestones  

1. **Completed milestones**  
  1. Created basic placeholder interface use actionbar and fragment for navigation.
  2. Found issue with interface use actionbar and fragment for navigation. I can not make it to interact with host  activity and timer service at the sametime. This milestone take a long time.
  3. Changed interface and navigation from fragment to normal intent and button interface.
  4. Done some basic create task, select task, little bit of timer, and database. 
  5. Now can create table, insert data by create task, query all by select task, timer have work and break countdowntimer call back to back in a cycle.
  6. Try to take Aaron's suggestion to use a boudle slider bar to scroll the graph. I found 3rd party API RangSeekBar does double slide scroll. The graph API GraphView is also 3rd party. I try to put them to work together. I only able to make it work it graph data point are not going to change at a single generation of the graph, if data set changes or scroll until no more datapoint graph will break. Because of this does not seem robust, I am not going to use RangSeekBar, also GraphView itself can enable scrollable, so I just use that.
  7. Done the core of the graph page with multi-line graph, added a dummy tool page to change database data to make developing easier.
  8. Joel completed assess page and related update query, I merged the code into the app.
  9. Changed some code in assess page, database, and graph page.
  10. Added to timer page track timespent, done complete and abandon button to update the database progress status. done stop button update the database progress status to "working".
  11. Added to create page initial insert set progress to "not start" instead of "null".
  12. Added custom listview and color and color coded progress for select task page, added condition to restrict when to display buttons in timer page to make it robust, change delimiter from "space" to "|"and added orderby for database in related to graph, fixed some bugs.
  13. Added timer page start button add timestemp to database.
  14. Remove dummy edittext from create page, so as to database.
  15. For database, added estimation value during insert, comment out insert_EstimationValue().
  16. On each page set either portrit view or landscape view, more robust.
  17. Change some display in each page to mkae them look cleaner, removed estimation value it is extra.
  18. Changed spinner in graph page from statically define languages to dynamically query graphable languages from database, praghable means languages of totally done project, added condition to database and createtaskactivity to prevent insert empty project to database, organized buttons, locale will now be auto detacted, added internet connection check and message for githubactivity.
  19. Added check for unique project name before insert to database, added requestFocus and imeOptions to EditText, added colored text logo "CLH" and app title, activity icon and activity title, removed temp title from body.
  20. added 3 methods to check empty database or data, fixed minor bug, added Joel's color schema.
  21. changed Joel's color schema due to color different between emulator and real phone (may be because current android material design stable colors is for newer phone only, it does require current api level in code), completed home page with ViewPager image guide with embeded text for ALPHA version.


2. **To do milestones for the future**  
  1. Timer can display time spent in more location if wanted to. If there are more responsive timer API out there replace the timer, currently if user click button that control timer faster than 1 click per second timer tracking will be off (this may be my issue I don't have deep knowledge of timer service).
  2. Possible enhancement for the customlistview, I did not use pagination because this list will not grow large.
  3. Continue test, debug, and enhance for both code, design, and feature, can add network feature, more Github feature, and may add google feature.
  4. Continue check for updates of 3rd party API I am using, see if useful. If there is no datapoint graph will crash, since this is part of the graphview issue, now solution will be check for empty data before allow user go to graph. tivity, I set can be scroll, and the last 10 datapoint or all if less, this looks ugly if 10 data, 5 happen in January 2014, another 5 happen in January 2015, it will be hard / no way to select the datapoint. Solution may be last 30 day of datapoint or less.
