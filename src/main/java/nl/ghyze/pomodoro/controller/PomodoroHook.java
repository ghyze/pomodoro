package nl.ghyze.pomodoro.controller;

public interface PomodoroHook
{
   void completed();

   void canceled();
}
