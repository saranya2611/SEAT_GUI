package services;

import java.util.ArrayList;

import models.Course;
import models.Student;
import models.StudentPreference;

/**
 * This function is mainly used compute the exchange unstable pairs.
 * A pair of students is exchange unstable if they each have 1 allotted course that
 * they would exchange with the other student, such that both students
 * are better off in the allotment (lexicographically better off)
 * @author akshay
 *
 */
public class ExchangeUnstablePairs {
	public static String computeExchangeUnstablePairs(ArrayList<Student> studentList) {
		String exchangeUnstablePairs = "";
		//Loop through every pair of course allotments
		for (int i=0;i<studentList.size();i++){
			Student s1 = studentList.get(i);
			for (int j=i+1;j<studentList.size();j++){
				Student s2 = studentList.get(j);
				for (StudentPreference sp1 : s1.orderedListOfcoursesAllotted){
					for (StudentPreference sp2 : s2.orderedListOfcoursesAllotted){
						
						//Firstly ensure that the 2 courses for exchange occour on the 2 preference lists
						if (StudentPreference.getStudentPreferenceBycourseNumber(s1.studentPreferenceList,sp2.getCourseObj().getcourseNumber())==null
								|| StudentPreference.getStudentPreferenceBycourseNumber(s2.studentPreferenceList,sp1.getCourseObj().getcourseNumber())==null){
							continue; //If the courses to be exchanged don't occour on the other student's preference list, exchange instability not possible here
						}
						//Check for exchange instability only if the exchange benefits both students
						if (sp1.getPreferenceNo() < StudentPreference.getStudentPreferenceBycourseNumber(s1.studentPreferenceList,sp2.getCourseObj().getcourseNumber()).getPreferenceNo()
							|| sp2.getPreferenceNo() < StudentPreference.getStudentPreferenceBycourseNumber(s2.studentPreferenceList,sp1.getCourseObj().getcourseNumber()).getPreferenceNo()){
							continue; //If the exchange does not benefit both students, exchange instability not possible here
						}
						
						//The 2 preferences should not refer to the same course
						if (sp1.getCourseObj()==sp2.getCourseObj()){
							continue; //If true, exchange instability not possible here
						}
						
						//sp2.getCourseObj() should not slot conflict with the core courses of s1
						if (checkCoreCourseSlotClash(s1,sp2.getCourseObj())){
							continue; //If slot clash exists, exchange instability not possible here
						}
						
						//sp1.getCourseObj() should not slot conflict with the core courses of s2
						if (checkCoreCourseSlotClash(s2,sp1.getCourseObj())){
							continue; //If slot clash exists, exchange instability not possible here
						}
						
						//sp2.getCourseObj() should not slot conflict with the allotted elective courses (apart from sp1) of s1
						if (checkElecitveCourseSlotClash(s1,sp2.getCourseObj(),sp1.getCourseObj())){
							continue; //If there is a slot clash, exchange instability not possible here
						}
						
						//sp1.getCourseObj() should not slot conflict with the allotted elective courses (apart from sp2) of s2
						if (checkElecitveCourseSlotClash(s2,sp1.getCourseObj(),sp2.getCourseObj())){
							continue; //If there is a slot clash, exchange instability not possible here
						}
						
						//sp2.getCourseObj() should not colour conflict with the allotted elective courses (apart from sp1) of s1
						if (checkElecitveCourseColourClash(s1,sp2.getCourseObj(),sp1.getCourseObj())){
							continue; //If there is a colour clash, exchange instability not possible here
						}
						
						//sp1.getCourseObj() should not colour conflict with the allotted elective courses (apart from sp2) of s2
						if (checkElecitveCourseColourClash(s2,sp1.getCourseObj(),sp2.getCourseObj())){
							continue; //If there is a colour clash, exchange instability not possible here
						}
						
						//sp2.getCourseObj() should fit into the remaining credits for s1 (after removing sp1, because we are trying to exchange)
						if (!checkIfCourseFitsInCredits(s1,sp2.getCourseObj(),sp1.getCourseObj())){
							continue; //If the course does not fit in the remaining credits, exchange instability not possible here
						}
						
						//sp1.getCourseObj() should fit into the remaining credits for s2 (after removing sp2, because we are trying to exchange)
						if (!checkIfCourseFitsInCredits(s2,sp1.getCourseObj(),sp2.getCourseObj())){
							continue; //If the course does not fit in the remaining credits, exchange instability not possible here
						}
						
						//If we reached this far, the 2 courses can be easily exchanged in order to get a
						//better allotment for both students. Record it
						exchangeUnstablePairs += s1.getRollNo() + "," + sp1.getCourseObj().getcourseNumber() + "," + s2.getRollNo() + "," + sp2.getCourseObj().getcourseNumber() + "\n";
					}
				}
			}
		}
		return exchangeUnstablePairs;
	}

	/* Checks slot clash between 'courseObj' and the core courses of student 's' */
	private static boolean checkCoreCourseSlotClash(Student s, Course courseObj) {
		for (Course core : s.coreCourses){
			if (core.slotClashWithCourse(courseObj)){
				return true;
			}
		}
		//If we reached here, not core course slot clash
		return false;
	}
	
	/**
	 * @param s
	 * @param courseObj
	 * @param exception
	 * @return : Checks slot clash between 'courseObj' and the allotted elective courses of student 's' (except for the 'exceptionCourse')
	 */
	private static boolean checkElecitveCourseSlotClash(Student s, Course courseObj, Course exceptionCourse) {
		for (StudentPreference allotted : s.orderedListOfcoursesAllotted){
			if (allotted.getCourseObj()!=exceptionCourse && allotted.getCourseObj().slotClashWithCourse(courseObj)){
				return true;
			}
		}
		//If we reached here, no clash with allotted electives
		return false;
	}
	
	
	/**
	 * @param s
	 * @param courseObj
	 * @param exception
	 * @return : Checks colour clash between 'courseObj' and the allotted elective courses of student 's' (except for the 'exceptionCourse')
	 */
	private static boolean checkElecitveCourseColourClash(Student s, Course courseObj, Course exceptionCourse) {
		for (StudentPreference allotted : s.orderedListOfcoursesAllotted){
			if (allotted.getCourseObj()!=exceptionCourse && allotted.getColour()==StudentPreference.getStudentPreferenceBycourseNumber(s.studentPreferenceList, courseObj.getcourseNumber()).getColour()){
				return true;
			}
		}
		//If we reached here, no clash with allotted electives
		return false;
	}
	
	/**
	 *
	 * @param s
	 * @param courseObj
	 * @param exceptionCourse
	 * @return Check if courseObj fits in with all the core+allottedElective courses of s, (except for the
	 * exceptionCourse, because that is the course we are going to exchange out.)
	 */
	private static boolean checkIfCourseFitsInCredits(Student s, Course courseObj, Course exceptionCourse) {
		int totalCredits = 0;
		//Count the core credits
		for (Course core : s.coreCourses){
			totalCredits += core.getCredits();
		}
		
		//Count elective credits (except for the exception course)
		for (StudentPreference sp : s.orderedListOfcoursesAllotted){
			if (sp.getCourseObj()!=exceptionCourse){
				totalCredits += sp.getCourseObj().getCredits();
			}
		}
		
		//Check if the course fits in the remaining credits
		return courseObj.getCredits () <= s.maxCreditsInSem - totalCredits;
	}
}
