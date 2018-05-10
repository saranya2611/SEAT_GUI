package services;

import java.util.ArrayList;
import java.util.Iterator;

import models.Course;
import models.CoursePreference;
import models.DepartmentSpecificMaxCreditLimit;
import models.Student;
import models.StudentPreference;

/*
* Since the input could possibly have errors we try to spot such errors before running the algorithm.
* There are 2 types of errors
* a) Fixable : Student with an empty preference list can be removed from the allotment process
* b) Unfixable : A course, which is not in the course list appears in a student's preference list
* We fix the fixable errors and report it in the log. If there is an unfixable error, we report it and stop the program
*/

public class InputSanitization {

	/*
	 * The following 8 checks are performed to check if there are no errors in the input
	 */
	public static String sanitize(ArrayList<Student> studentList,ArrayList<Course> courseList,ArrayList<DepartmentSpecificMaxCreditLimit> deptSpecificMaxCreditLimitInfo) {
		
		//Run checks for these 8 types of errors, prune out the errors and record the errors.
		String error1 = checkForMaxCreditLimit(studentList,deptSpecificMaxCreditLimitInfo); //Note that this must come before the 'removePreferencesWhichExceedCreditLimit' function because this may modify the number of credits
		String error2 = removePreferencesWhichExceedCreditLimit(studentList);	
		String error3 = checkForRepeatedStudentPreferences(studentList);
		String error4 = checkForRepeatedCoursePreferences(courseList);
		String error5 = checkForRepeatedStudents(studentList);
		String error6 = checkForRepeatedCourses(courseList);		
		String error7 = checkForSlotClash(studentList);
		String error8 = checkForEmptyPreferenceList(studentList);
		String finalErrorMsg = error1 + error2 + error3 + error4 + error5 + error6 + error7 + error8;
		//Return all the errors that occoured
		if (finalErrorMsg.equalsIgnoreCase("")){ //If no error occoured
			return null;
		}
		else{ //If there was some error
			return finalErrorMsg;
		}
	}
	
	

	/* Removes those courses on a student's preference list who credits are greater 
	 * than the student's maximum elective credits. There is no use of keeping these.
	 * These are being reported because it is odd for a student to register for a course
	 * whose credits are greater than his maximum elective credits
	 */
	private static String removePreferencesWhichExceedCreditLimit(ArrayList<Student> studentList){
		String error = "";
		for (Student s : studentList){
			//This iterator must be used because it is the only way to safely remove elements
			for (Iterator<StudentPreference> iterator = s.studentPreferenceList.iterator(); iterator.hasNext();) {
				StudentPreference sp = iterator.next();		
				if (sp.getCourseObj().getCredits() > s.electiveCreditsLeft){
					error = error + "Removing Preference since it exceeds credit limit. Student : " + s.getRollNo() + " - Course : " + sp.getCourseObj().getcourseNumber() + "\n";
					iterator.remove();
					s.invalidPreferences.add(sp);
				}
			}
		}
		return error;
	}
	
	//Self explanatory
	private static String checkForEmptyPreferenceList(ArrayList<Student> studentList) {
		String error = "";
		for (Iterator<Student> iterator = studentList.iterator(); iterator.hasNext();) {
			Student s = iterator.next();
			if (s.studentPreferenceList.size()==0){
				error = error + "This student has an empty preference list or none of his courses are within his max credit limit. He is not removed for the purpose of statistics : " + s.getRollNo() + "\n";
				//iterator.remove(); - Do not remove it. It messes up the statistics and reasons for not allotting courses
			}
		}
		return error;
	}

	/**
	 * Checks if every student in the student list has no slot clash with a core for a course in his preference list
	 * @param studentList
	 * @return : Error message or empty string if there are no errors
	 */
	private static String checkForSlotClash(ArrayList<Student> studentList) {
		String error = "";
		for (Student s : studentList){
			for (Course core : s.coreCourses){
				for (Iterator<StudentPreference> iterator = s.studentPreferenceList.iterator(); iterator.hasNext();) {
					StudentPreference electivePreference = iterator.next();
					if (core.slotClashWithCourse(electivePreference.getCourseObj())){
						error = error + "Slot Clash of core course " + core.getcourseNumber() + " and elective course " + electivePreference.getCourseObj().getcourseNumber() + " for student " + s.getRollNo() + ". " + electivePreference.getCourseObj().getcourseNumber() + " removed \n";
						iterator.remove();
						s.invalidPreferences.add(electivePreference);
					}
				}
			}
		}
		return error;
	}

	/**
	 * Checks if some course is repeated in the course list
	 * @param courseList
	 * @return : Error message or null if there are no errors
	 */
	private static String checkForRepeatedCourses(ArrayList<Course> courseList) {
		String error = "";
		ArrayList<Course> repeatedCourses = new ArrayList<Course>();
		for (int i=0;i<courseList.size();i++){
			for (int j=i+1;j<courseList.size();j++){
				Course c1 = courseList.get(i);
				Course c2 = courseList.get(j);
				if (c1.getcourseNumber().equalsIgnoreCase(c2.getcourseNumber())){ //We have a repeat
					error = error + "Repeated Course in course list : " + c1.getcourseNumber() + ". Repeated Course is being removed. \n";
					repeatedCourses.add(c2);
				}
			}
		}
		
		courseList.removeAll(repeatedCourses);
		return error;
	}

	/**
	 * Checks if some student is repeated in the student list
	 * @param studentList
	 * @return : Error message or null if there are no errors
	 */
	private static String checkForRepeatedStudents(ArrayList<Student> studentList) {
		String error = "";
		ArrayList<Student> repeatedStudents = new ArrayList<Student>();
		for (int i=0;i<studentList.size();i++){
			for (int j=i+1;j<studentList.size();j++){
				Student s1 = studentList.get(i);
				Student s2 = studentList.get(j);
				if (s1.getRollNo().equalsIgnoreCase(s2.getRollNo())){ //We have a repeat
					error = error + "Repeated Student in student list : " + s1.getRollNo() + ". Repeated student is being removed\n";
					repeatedStudents.add(s2);
				}
			}
		}
		
		studentList.removeAll(repeatedStudents);
		return error;
	}

	/**
	 * Checks if some course has a course preference that is repeated
	 * @param courseList
	 * @return : Error message or null if there are no errors
	 */
	private static String checkForRepeatedCoursePreferences(ArrayList<Course> courseList) {
		String error = "";
		for (Course c : courseList){
			ArrayList<CoursePreference> repeatedCoursePreferences = new ArrayList<CoursePreference>();
			for (int i=0;i<c.coursePreferenceList.size();i++){
				for (int j=i+1;j<c.coursePreferenceList.size();j++){
					CoursePreference cp1 = c.coursePreferenceList.get(i);
					CoursePreference cp2 = c.coursePreferenceList.get(j);
					if (cp1.getStudentObj().getRollNo().equalsIgnoreCase(cp2.getStudentObj().getRollNo())){ //We have a repeat
						error = error + "Repeated Student " +  cp2.getStudentObj().getRollNo() + " for preference list of course: " + c.getcourseNumber() + ". Removing repeated course preference\n";
						repeatedCoursePreferences.add(cp2);
					}
				}
			}
			c.coursePreferenceList.removeAll(repeatedCoursePreferences);
		}
		return error;
	}

	/**
	 * Checks if some student has a student preference that is repeated
	 * @param studentList
	 * @return : Error message or null if there are no errors
	 */
	private static String checkForRepeatedStudentPreferences(ArrayList<Student> studentList) {
		String error = "";
		for (Student s : studentList){
			ArrayList<StudentPreference> repeatedStudentPreferences = new ArrayList<StudentPreference>();
			for (int i=0;i<s.studentPreferenceList.size();i++){
				for (int j=i+1;j<s.studentPreferenceList.size();j++){
					StudentPreference sp1 = s.studentPreferenceList.get(i);
					StudentPreference sp2 = s.studentPreferenceList.get(j);
					if (sp1.getCourseObj().getcourseNumber().equalsIgnoreCase(sp2.getCourseObj().getcourseNumber())){ //We have a repeat
						error = error + "Repeated Course " + sp1.getCourseObj().getcourseNumber() + " in the preference list of student " + s.getRollNo() + ". Removing repeated student preference\n";
						repeatedStudentPreferences.add(sp2);
					}
				}
			}
			s.studentPreferenceList.removeAll(repeatedStudentPreferences);
		}
		return error;
	}
		
	/* Checks if the all the students in studentList have their credits within 
	 * the set max credit limit. (Either the set default of 60, or else whatever
	 * the department has provided)
	 */
	private static String checkForMaxCreditLimit(ArrayList<Student> studentList,ArrayList<DepartmentSpecificMaxCreditLimit> deptSpecificMaxCreditLimitInfo) {
		String error = "";
		for (Student s : studentList){
			//Check if the maxCredits entered by the student is within the limits provided
			//by his department. If not, then reduce it
			int maxCreditLimitForStudent = s.maxCreditLimitForStudent(deptSpecificMaxCreditLimitInfo);
			if (s.maxCreditsInSem > maxCreditLimitForStudent){
				s.maxCreditsInSem = maxCreditLimitForStudent;
				//Recalculate the elective credits left, since we modified the max credits.
				s.calculateMaxElectiveCredits();
				s.electiveCreditsLeft = s.getMaxElectiveCreditsInSem();
				//Construct the error message
				error = error + "Encountered student with roll number " + s.getRollNo() + " exceeding his max credits. Student's max credits were reduced\n";
			}
		}
		return error;
	}	

}