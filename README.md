# Pomodoro Timer

A lightweight Java desktop application implementing the Pomodoro Technique - a time management method that uses focused work intervals separated by short breaks.

## Overview

This application provides a compact, always-on-top timer widget that helps you maintain productivity using the Pomodoro Technique. Work in 25-minute focused sessions (pomodoros), take 5-minute breaks, and track your progress throughout the day.

## Features

### Core Timer Functionality
- **25-minute work sessions** (Pomodoros) with customizable duration
- **5-minute short breaks** between pomodoros
- **15-minute long breaks** after a configurable number of pomodoros (default: 3)
- **Color-coded states**: Red for work, Green for breaks, Blue for idle
- **Visual progress indicators**: Track completed pomodoros at a glance
- **Auto-reset**: Automatically reset after extended idle periods

### User Interface
- **Compact window**: Small 140x100 pixel widget that stays on top
- **System tray integration**: Real-time countdown in system tray with dynamic icons
- **Multi-monitor support**: Position the timer on any connected screen
- **Flexible positioning**: Place the timer in any screen corner
- **Interactive dialogs**: Choose what to do when pomodoros or breaks complete

### Task Management
- **Task tracking window**: Create and manage tasks with estimated pomodoros
- **Todo/Done tabs**: Tasks organized into separate tabs — "Todo" (pending/in-progress) and "Done" (completed)
- **Task lifecycle states**: Track tasks through PENDING → IN_PROGRESS → DONE states
- **Auto state transitions**: Activating a task automatically transitions it to IN_PROGRESS; deactivating returns it to PENDING
- **Auto-deactivation on completion**: Setting a task to DONE automatically deactivates it and clears the active task
- **Edit tasks**: Modify task name, estimated pomodoros, or state via edit button
- **Remove tasks**: Delete individual tasks with confirm dialog (prevents accidental deletion)
- **Automatic tracking**: Completed pomodoros are automatically counted toward your active task
- **Progress monitoring**: See estimated vs. actual pomodoros for each task
- **Active task display**: Current task shown prominently at the top, always visible
- **Persistent storage**: Tasks are automatically saved and restored across application restarts
- **Unique task identity**: Each task has a persistent UUID that survives name changes

### Statistics & Activity Logging
- **Automatic event logging**: All pomodoro and task events are logged to CSV files
- **Daily log rotation**: Separate log files created for each day
- **Pomodoro events**: Tracks started, completed, canceled, and break_ended events
- **Task events**: Tracks created, activated, deactivated, state_changed, pomo_incremented, edited, and removed events
- **External analysis**: CSV format enables analysis by external tools
- **Log location**: `~/.pomodoro/logs/` directory
  - `pomodoro_events_YYYY-MM-DD.csv` - Pomodoro state changes
  - `task_events_YYYY-MM-DD.csv` - Task lifecycle events

### Customization
- Configure pomodoro duration (default: 25 minutes)
- Configure short break duration (default: 5 minutes)
- Configure long break duration (default: 15 minutes)
- Set number of pomodoros before long break
- Adjust auto-reset timeout
- Choose screen and corner position for the timer window

## Requirements

- Java 17 or higher
- Windows, macOS, or Linux with GUI support

## Building

This project uses Maven for build management:

```bash
mvn clean package
```

This creates a standalone executable JAR with all dependencies in the `target` directory:
```
pomodoro-0.2-jar-with-dependencies.jar
```

## Running

Execute the compiled JAR file:

```bash
java -jar target/pomodoro-0.2-jar-with-dependencies.jar
```

Or simply double-click the JAR file on systems with Java properly configured.

## Usage

### Starting a Pomodoro
1. Click the **Play** button to start a 25-minute work session
2. The timer will count down, showing remaining time
3. Focus on your task until the timer completes

### When a Pomodoro Completes
A dialog will appear with options:
- **SAVE**: Count this pomodoro and start your break
- **DISCARD**: Don't count this pomodoro, but still take a break
- **CONTINUE**: Skip the break and start another pomodoro immediately

### Managing Tasks
1. Access the task window via the right-click menu or task button on main window
2. Click "Add Task" to create a new task
3. Enter task name, estimated number of pomodoros, and optional state
4. Double-click a task to make it active — it automatically transitions to **In Progress**
5. Completed pomodoros automatically increment the active task counter
6. Edit tasks using the "..." button (change name, estimate, or state)
7. Change task state via dropdown (Pending, In Progress, Done)
   - Setting a task to **Done** automatically deactivates it and moves it to the Done tab
8. Remove tasks using the "X" button (confirmation required)
9. Switch between **Todo** and **Done** tabs to review completed tasks

### Customizing Settings
1. Right-click on the timer window
2. Select "Settings"
3. Adjust timings, positioning, and behavior as needed
4. All numeric fields provide real-time validation feedback (invalid values shown in red/pink)
5. Click "OK" to apply changes

## Project Structure

The codebase follows a **domain-driven package structure** for modularity:

- **src/main/java/nl/ghyze/** - Root package
  - **pomodoro/** - Core timer application
    - **model/** - Core timer data model (Pomodoro, PomodoroType)
    - **controller/** - Timer control loop and event handling
    - **view/** - Main window GUI components
    - **statemachine/** - State machine (state management, hooks, messages, break calculation)
    - **optiondialog/** - Interactive completion dialog system
    - **persistence/** - Shared persistence utilities
  - **settings/** - Settings subsystem (model, UI, persistence)
  - **tasks/** - Task management subsystem (model, UI, persistence)
  - **statistics/** - Statistics and event tracking

## Technology Stack

- **Language**: Java 17
- **Build Tool**: Maven
- **GUI**: Java Swing
- **Persistence**: Files stored in `~/.pomodoro/` directory
  - **Tasks**: `~/.pomodoro/tasks.json` (JSON format using GSON)
  - **Settings**: `~/.pomodoro/settings.properties` (properties format)
  - **Statistics**: `~/.pomodoro/logs/` (daily-rotated CSV files)
    - `pomodoro_events_YYYY-MM-DD.csv` - Pomodoro state transitions
    - `task_events_YYYY-MM-DD.csv` - Task lifecycle events
- **Testing**: JUnit, EasyMock
- **Code Generation**: Lombok (reduces boilerplate)

## License

Version 0.2

## Author

ghyze
