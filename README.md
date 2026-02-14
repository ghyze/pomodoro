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
- **Automatic tracking**: Completed pomodoros are automatically counted toward your active task
- **Progress monitoring**: See estimated vs. actual pomodoros for each task
- **Active task display**: Current task shown prominently at the top

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
1. Access the task window via the right-click menu
2. Click "Add Task" to create a new task
3. Enter task name and estimated number of pomodoros
4. Double-click a task to make it active
5. Completed pomodoros automatically increment the active task counter

### Customizing Settings
1. Right-click on the timer window
2. Select "Settings"
3. Adjust timings, positioning, and behavior as needed
4. Click "Save" to apply changes

## Project Structure

- **src/main/java/nl/ghyze/pomodoro/** - Main source code
  - **model/** - Data models (Pomodoro, Settings, Task)
  - **controller/** - Business logic and state management
  - **view/** - GUI components and rendering
  - **tasks/** - Task management functionality
  - **optiondialog/** - Interactive dialog system
  - **statistics/** - Usage tracking

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.1 (minimal usage)
- **Build Tool**: Maven
- **GUI**: Java Swing
- **Persistence**: Java Preferences API
- **Testing**: JUnit, EasyMock

## License

Version 0.2

## Author

ghyze
