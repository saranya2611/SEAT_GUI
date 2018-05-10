package models;

import java.util.ArrayList;

/**
 * This class represents a single slot like A slot or B slot
 */
public class Slot {
	private String slotName; //The name of the slot (eg: A). Read only.
	private ArrayList<ClassTiming> classTimings; //The class timings of the slot. If A slot is on Mon:8-9,Tue:9-10 and Thursday:11-12, there will be 1 class timing object for each of them. Read only.
	
	/*constructor*/
	public Slot(String a,ArrayList<ClassTiming> b){
		slotName = a;
		classTimings = b;
	}

	/*Getter functions for private variables. No setter methods since they are read only.*/
	public String getSlotName(){
		return slotName;
	}
	public ArrayList<ClassTiming> getClassTimings(){
		return classTimings;
	}
	
	/*Simply loop through the slots (passed as argument) and find the matching slot*/
	public static Slot getSlotByName(String inp_slot, ArrayList<Slot> slots) {
		for (Slot s : slots){
			if (s.slotName.equalsIgnoreCase(inp_slot)){
				return s;
			}
		}
		return null;
	}

	/* This function checks if the current slot has a clash(overlap) with 'slot2' given in the argument
	 * It returns true if it does and false otherwise
	 */
	public boolean clashesWithSlot(Slot slot2) {
		//Looping through all possible pairs of timings of the 2 slots
		for (ClassTiming timing1 : classTimings){
			for (ClassTiming timing2 : slot2.classTimings){
				//For any of the class timings, if they are on the same day
				if (timing1.getThreeLetterDayOfTheWeek().equalsIgnoreCase(timing2.getThreeLetterDayOfTheWeek())){
					//Calculate the start and end time in minutes alone.
					int timing1StartTimeInMinutes = timing1.getStartHour()*60 + timing1.getStartMinute();
					int timing1EndTimeInMinutes = timing1.getEndHour()*60 + timing1.getEndMinute();
					int timing2StartTimeInMinutes = timing2.getStartHour()*60 + timing2.getStartMinute();
					int timing2EndTimeInMinutes = timing2.getEndHour()*60 + timing2.getEndMinute();
					
					//And if they overlap in any way return true 
					//(Sketch this condition out to understand why it is correct)
					if ((timing1StartTimeInMinutes<=timing2StartTimeInMinutes && timing2StartTimeInMinutes<timing1EndTimeInMinutes) || (timing2StartTimeInMinutes<=timing1StartTimeInMinutes && timing1StartTimeInMinutes<timing2EndTimeInMinutes)){
						return true;
					}
				}
			}
		}
		//If we reached this point, there is no clash. Return false;
		return false;
	}
}


