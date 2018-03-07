package nl.ghyze.pomodoro.controller;

public interface PomodoroHook
{
	void started();
	
   void completed();

   void canceled();
}
