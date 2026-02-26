# TaskList
- improved TaskDialog
- Task with start time and duration (meetings)
- Add note to task.
- Add actual time spent to task
- Note and actual time spent should be visible on a detail panel.
- Introduce TaskService. Handling of tasks is now done in TaskFrame, but it should be done in a separate service. TaskFrame should only be responsible for displaying the tasks and handling user input. TaskService should handle the logic of adding, removing, updating and retrieving tasks.

# Statemachine
- force continue pomodoro (in case of meetings), count towards second pomodoro. 
  - Only register second pomodoro if time is up.
  - Without break, count and display next pomodoro. This could go beyond the configured number of pomodoros, in which case a new pomodoro should be visible
    - For exampole, the number of pomodoros configured before a long break is 3, and we're in the 4th pomodoro. There should be 4 pomodoros visible on the screen.  
- On long break, choose short or long break.
- Long break option after <configured number> _or more_ pomodoros.

# UI
- configurable theme, investigate Look and Feel framework. Default swing UI looks dated.
- Add pomodoro icon to window.

# Bug

# Optimization
- Review wiring in PomoApp