# TaskList
- current status (pending, in progress, done)
- Modify task
- improved add task dialog
- Add task list button to main window - DONE
- Remove done tasks

# Statemachine
- force continue pomodoro (in case of meetings), count towards second pomodoro. 
  - Only register second pomodoro if time is up.
- On long break, choose short or long break.
- Long break option after <configured number> _or more_ pomodoros.

# UI
- configurable theme, investigate Look and Feel framework. Default swing UI looks dated.

# Bug
- After break, next pomodoro is not started. Manual start required.

# Optimization
- Reduce duplications in PomoButtonFactory.
- Review wiring in PomoApp