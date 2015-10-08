package nl.ghyze.pomodoro.controller;

public class OptionDialogModel {

    private String title;
    private String message;
    private Object[] choices;
    
    public OptionDialogModel(){
	
    }
    
    public void setTitle(String title){
	this.title = title;
    }
    
    public String getTitle(){
	return title;
    }
    
    public void setMessage(String message){
	this.message = message;
    }
    
    public String getMessage(){
	return message;
    }
    
    public void setChoices(Object[] choices){
	this.choices = choices;
    }
    
    public Object[] getChoices(){
	return choices;
    }
    
    public Object getDefaultChoice(){
	return choices[0];
    }
}
