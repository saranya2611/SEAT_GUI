package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Course;
import models.CoursePreference;
import models.Slot;
import models.Student;
import models.StudentPreference;

public class SlotBasedHRalgorithm {

	public static ArrayList<Slot> runAlgorithm(ArrayList<Student> studentList,ArrayList<Course> courseList, ArrayList<Slot> slot_input, int heuristicToBeUsed) {
		//some declarations
		Student tempStudent;
		
		/*Initialization*/
		//Set every student's unallotted courses as his entire preferenceList initially, and allotted courses as empty list
		for (int i=0;i<studentList.size();i++){
			tempStudent = studentList.get(i);
			tempStudent.coursesUnallotted = CommonAlgorithmUtilities.copyArrayList(tempStudent.studentPreferenceList);
			tempStudent.orderedListOfcoursesAllotted = new ArrayList<StudentPreference>();
		}
					
		//Compute the set of students who have capacity to take atleast 1 course on their preference list
		ArrayList<Student> studentsWithCapacityLeft = CommonAlgorithmUtilities.copyArrayList(studentList);
		
		//Remove students with an empty preference list - necessary here. Else the 'runHRLoopForGivenSlotOnly()' will crash if it encounters an empty preference list
		CommonAlgorithmUtilities.removeStudentsWithNoCourseLeftToApplyTo(studentsWithCapacityLeft);
		
		//Get an ordering of the slots which we are going to use
		ArrayList<Slot> slots=null;
		if (heuristicToBeUsed==1){
			slots = getSlotOrderingByHeuristic1(studentList,slot_input);
		}
		else if (heuristicToBeUsed==2){
			slots = getSlotOrderingByHeuristic2(studentList,slot_input);
		}
		else{
			System.out.println("ERROR : Input for Heuristic number for the Slot based algorithm was incorrect. Exiting");
			System.exit(0);
		}
		
		
		/*Actual Algorithm Loop*/
		//The iterations of this loop correspond to the iterations in the name 'IterativeHR'
		for (int i = 0 ; i<slots.size();i++){
			/* Initialization for this iteration */
			//Set every course's allotted list for this iteration to be zero
			for (Course c : courseList){
				c.currentIterationStudentAllottedList = new ArrayList<CoursePreference>();
			}
			
			//Set every student's allotted course for this iteration to be nothing
			for (Student s : studentList){
				s.courseAllottedInCurrentIteration = null;
			}
			//Set the list of unallotted students for this iteration to be all the students who have capacity left
			ArrayList<Student> unallottedStudentsInIteration = CommonAlgorithmUtilities.copyArrayList(studentsWithCapacityLeft);
			
			//Run the classical HR algorithm
			runHRLoopForGivenSlotOnly(unallottedStudentsInIteration,slots.get(i));
			
			//Update some values that change after the HR iteration
			CommonAlgorithmUtilities.recalculateStudentsLeftoverCredits(studentsWithCapacityLeft);
			CommonAlgorithmUtilities.freezeAllotmentOfCurrentIteration(studentsWithCapacityLeft);
			
			//Remove Preferences that become invalid because of conflict or credit limit exceeding
			CommonAlgorithmUtilities.removeStudentPreferencesWithColourConflict(studentsWithCapacityLeft);
			CommonAlgorithmUtilities.removeStudentPreferencesWithSlotConflict(studentsWithCapacityLeft);
			CommonAlgorithmUtilities.removeStudentPreferencesWithCreditRequirementExceedingCreditLimit(studentsWithCapacityLeft);
			
			//Remove students with no capacity left
			CommonAlgorithmUtilities.removeStudentsWithNoCourseLeftToApplyTo(studentsWithCapacityLeft);	 
		}	 //Note that at the end of each iteration we have a valid allotment
		return slots;
	}

	/* This function runs the HR algorithm, but only considers those preferences which are in the 
	 * given input slot
	 */
	private static void runHRLoopForGivenSlotOnly(ArrayList<Student> unallottedStudentsInIteration, Slot slot) {
		//some declarations
		Student tempStudent;
		Course tempCourse = null;
		StudentPreference tempStudentPreference;
		CoursePreference tempCoursePreference;
		
		/*Run the classical HR algorithm within this loop, but only look at courses in the given inp_slot*/
		while (!unallottedStudentsInIteration.isEmpty()){

			//Pick the first student from the list of unallotted students for this iteration
			tempStudent = unallottedStudentsInIteration.remove(0);
			
			//get the 1st course on his preference list (i.e. unallotted courses list) that is in the given slot
			int tempStudentPreferenceIndex = -1;
			for (int i=0;i<tempStudent.coursesUnallotted.size();i++){
				if (tempStudent.coursesUnallotted.get(i).getCourseObj().slots.contains(slot)){
					tempStudentPreferenceIndex = i;
					break;
				}
			}
			
			//If there was no course in his preference list from this slot - just continue
			if (tempStudentPreferenceIndex==-1){
				continue;
			}
			else{ //We found the top course in his preference list from inp_slot. Remove that preference. We will allot it.
				tempStudentPreference = tempStudent.coursesUnallotted.remove(tempStudentPreferenceIndex);
			}
			
			//add the course as the student's allotted course
			tempStudent.courseAllottedInCurrentIteration = tempStudentPreference;
			
			//Add the student to the course's allotted list
			tempCourse = tempStudentPreference.getCourseObj();
			tempCoursePreference = CoursePreference.getCoursePreferenceByRollNo(tempCourse.coursePreferenceList,tempStudent.getRollNo());
			if (tempCoursePreference==null){
				System.out.println("ERROR during Slot Based Algorithm. getCoursePreferenceByRollNo() function returned null");
				System.exit(0);
			}
			tempCourse.currentIterationStudentAllottedList.add(tempCoursePreference);
			
			//At this point
			//if the course is not full - no problem
			if (tempCourse.capacityStillFree != 0){
				tempCourse.capacityStillFree = tempCourse.capacityStillFree-1;  //just decrement the capacity still free counter by 1
			}
			//else we need to evict the worst student
			else{
				int maxPreferenceNo = 0;  // a course_preference with the maxPreferenceNo is the worst course_preference
				CoursePreference worstCoursePreference = null;
				
				//find the worst course preference
				for (CoursePreference cp : tempCourse.currentIterationStudentAllottedList){
					if (cp.getPreferenceNo() > maxPreferenceNo){
						maxPreferenceNo = cp.getPreferenceNo();
						worstCoursePreference = cp;
					}
				}
				
				//increment the course's noOfRejections by 1, since it rejected a person
				tempCourse.noOfRejections +=1;
				
				//evict (delete) that course preference object
				tempCourse.currentIterationStudentAllottedList.remove(worstCoursePreference);
				worstCoursePreference.getStudentObj().courseAllottedInCurrentIteration = null;
				
				//Add the student back to the list of unallotted students
				unallottedStudentsInIteration.add(worstCoursePreference.getStudentObj());
			}
		}
		
	}

	/**
	 * This is a heuristic used to order the slots for the slot based HR algorithm. 
	 * We will be doing HR iteratively 1 slot at a time in the order provided by this algorithm
	 * @param studentList
	 * @param slot_input 
	 * @return - Each slot is given a score which is calculated as (r-i+1) for every preference, 
	 * where r = worst rank given by the student, and i = rank for the preference.
	 * This score is then summed over all preferences of all students.
	 * The slots are then ordered in a descending order based on their scores.
	 */
	private static ArrayList<Slot> getSlotOrderingByHeuristic2(ArrayList<Student> studentList, ArrayList<Slot> slot_input) {
		//Declarations
		ArrayList<Slot> slotsInOrder = new ArrayList<Slot>(); //Final slot ordering to be returned
		HashMap <String,Integer> slotScores = new HashMap<String,Integer>(); //Map of slots to their scores
		
		//Set the initial score for each slot to be 0
		for (Slot sl : slot_input){
			slotScores.put(sl.getSlotName(),0);
		}
		
		//Loop through every student's preference list
		for (Student s : studentList){
			for (int i=0;i<s.studentPreferenceList.size();i++){
				//Loop through all the slots in the student's preference list
				for (Slot slot : s.studentPreferenceList.get(i).getCourseObj().slots){
					//Add a score of (n-i+1) for every slot of every student preference (n=size of studentPreference list.i=rank of student preference)
					slotScores.put(slot.getSlotName(),slotScores.get(slot.getSlotName())+ (s.studentPreferenceList.size()-i));
				}
			}
		}
		
		/* Now we need to sort the slots based on their counts*/
		while (slotScores.size()!=0){
			//Iterate through the remaining slots to find the one with the highest count
			int maxCount = -1;
			String keyWithMaxCount = null;
			for (Map.Entry<String, Integer> entry : slotScores.entrySet()) {
				String key = entry.getKey();
			    Integer value = entry.getValue();
			    if (value>maxCount){ //If this slot beat the previous slot with the maxCount
			    	keyWithMaxCount = key; //Set this slot to have the new max value
			    	maxCount = value;
			    }
			    else if (value == maxCount){ //The 2 slots tie on the count
			    	if (key.compareTo(keyWithMaxCount)<0){ //Tie breaker is the lexicographical ordering of the slots
			    		keyWithMaxCount = key;
				    	maxCount = value;
			    	}
			    }
			    
			}
			//Add the slot with the highest count as the next entry in the 'slotsInOrder' arraylist
			slotsInOrder.add(Slot.getSlotByName(keyWithMaxCount,slot_input));
			slotScores.remove(keyWithMaxCount); //After removing the highest count slot, we will repeat the process until we have gone through all slots
		}
		return slotsInOrder;
	}
	
	/**
	 * This is a heuristic used to order the slots for the slot based HR algorithm. 
	 * We will be doing HR iteratively 1 slot at a time in the order provided by this algorithm
	 * @param studentList
	 * @return - Each slot is given a score based on how many students have their first preference course in
	 * the slot. The slots are then ordered in descending order based on this
	 */
	private static ArrayList<Slot> getSlotOrderingByHeuristic1(ArrayList<Student> studentList, ArrayList<Slot> slot_input) {
		//Declarations
		ArrayList<Slot> slotsInOrder = new ArrayList<Slot>(); //Final slot ordering to be returned
		HashMap <String,Integer> slotScores = new HashMap<String,Integer>(); //Map of slots to their scores
				
		//Set the initial score for each slot to be 0
		for (Slot sl : slot_input){
			slotScores.put(sl.getSlotName(),0);
		}
		
		//Loop through each student
		for (Student s : studentList){
			//Loop through the slots of his top preference
			for (Slot slot : s.studentPreferenceList.get(0).getCourseObj().slots){
				//For each of these slots, increment the score of that slot by 1
				slotScores.put(slot.getSlotName(),slotScores.get(slot.getSlotName())+1);
			}
		}
		
		/* Now we need to sort the slots based on their counts*/
		while (slotScores.size()!=0){
			//Iterate through the remaining slots to find the one with the highest count
			int maxCount = -1;
			String keyWithMaxCount = null;
			for (Map.Entry<String, Integer> entry : slotScores.entrySet()) {
				String key = entry.getKey();
			    Integer value = entry.getValue();
			    if (value>maxCount){//If this slot beat the previous slot with the maxCount
			    	keyWithMaxCount = key; //Set this slot to have the new max value
			    	maxCount = value;
			    }
			    else if (value == maxCount){ //The 2 slots tie on the count
			    	if (key.compareTo(keyWithMaxCount)<0){ //Tie breaker is the lexicographical ordering of the slots
			    		keyWithMaxCount = key;
				    	maxCount = value;
			    	}
			    }
			    
			}
			//Add the slot with the highest count as the next entry in the 'slotsInOrder' arraylist
			slotsInOrder.add(Slot.getSlotByName(keyWithMaxCount,slot_input));
			slotScores.remove(keyWithMaxCount);
		}
		return slotsInOrder;
	}
}
