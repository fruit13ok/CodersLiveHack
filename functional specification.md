# Coders Live Hack Project

## Functional Specification

### Introduction
The Coders Live Hack Project is an Android app that use a database to keep track of programming students' works progress, time it, and self assessment, and reflect their improvement as a programmer on a graph that consist of all their recorded work satistics. Also this app can fetch for number of commit from GitHub, if user store their project on GitHub.


### Views and User Experience

The app consist of the following views / features

* Homepage
* Create task page
* Select task page
* Work timer page
* Self assessment page
* Graph page
* GitHub commit page

#### Homepage
* A user guide of this app.

#### Create task page
* User create projects and keep them in database.

#### Select task page
* It display all stored project from database. It shows project info and progress color coded.

#### Work timer page
* User the timer to accumulate total work timespent for a project. The timer has a fix auto work and break cycle of 50 minutes : 10 minutes. User can pause the timer menually too. User will use timer page to mark the progress of the project.

#### Self assessment page
* This page is important for produce graph data. It input final grade, and distraction level of each finished project into database.

#### Graph page
* The fraph will drew 2 data lines "grade" (dotted with bubble) and "distraction level" (red) to represent the projects results. User can press the bubble to see more info of the project, swipe to see more of the graph, and filter the project by programming language.

#### GitHub commit page
* If user had uploaded their project to GitHub, this page can fetch the number of commits from GitHub. This page need to have internet connection to do the fetching.
