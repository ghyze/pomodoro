# TaskList
- current status (pending, in progress, done) - DONE
- Modify task - DONE
- improved TaskDialog
- Add task list button to main window - DONE
- Remove done tasks - DONE
- Task with start time and duration (meetings)
- Remove done tasks from list, create separate list with done tasks (maybe tabs?)
- Autoremove active task when set to done
- Set pending task to active when set to `in progress`, current active task to `pending` (it might not be done yet).
- Add note to task.
- Add actual time spent to task
- Note and actual time spent should be visible on a detail panel.

# Statemachine
- force continue pomodoro (in case of meetings), count towards second pomodoro. 
  - Only register second pomodoro if time is up.
- On long break, choose short or long break.
- Long break option after <configured number> _or more_ pomodoros.

# UI
- configurable theme, investigate Look and Feel framework. Default swing UI looks dated.
- Add pomodoro icon to window.

# Bug
- After break, next pomodoro is not started. Manual start required.
- Doubleclicking to make a task active does not remove it from the pending list. Resizing the window afterwards does.

# Optimization
- Reduce duplications in PomoButtonFactory.
- Review wiring in PomoApp