package nl.ghyze.pomodoro.view;

public class Screen {

	String screen;
	int index;
	
	public Screen(String screen, int index){
		this.screen = screen;
		this.index = index;
	}
	
	public String toString(){
		return screen;
	}
}
