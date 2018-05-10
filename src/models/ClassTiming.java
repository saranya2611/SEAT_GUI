package models;

/**
 * This class represents a single class timing (eg. Mon 8-9pm)
 * Each slot (like A slot) will have multiple (usually 3-4) class timings
 */
public class ClassTiming{
	private int startHour; //Represents the 'hours' field of the start time of the class. Read only
	private int endHour; //Represents the 'hours' field of the end time of the class. Read only
	private int startMinute; //Represents the 'minutes' field of the start time of the class. Read only
	private int endMinute; //Represents the 'minutes' field of the end time of the class. Read only
	private String threeLetterDayOfTheWeek; //The day of the week expressed in 3 capital letters (i.e. MON,TUE,WED,THUR,FRI,SAT,SUN). Read only
	
	//constructor
	public ClassTiming(int a,int b,int c,int d,String e){
		startHour = a;
		endHour = b;
		startMinute = c;
		endMinute = d;
		threeLetterDayOfTheWeek = e;
	}
	
	/* Getter Methods for private fields. No setter methods since they are read only*/
	public int getStartHour(){
		return startHour;
	}
	public int getEndHour(){
		return endHour;
	}
	public int getStartMinute(){
		return startMinute;
	}
	public int getEndMinute(){
		return endMinute;
	}
	public String getThreeLetterDayOfTheWeek(){
		return threeLetterDayOfTheWeek;
	}
	
}