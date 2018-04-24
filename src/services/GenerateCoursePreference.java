package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import models.Course;
import models.CoursePreference;
import models.CourseSpecificHighPriorityStudents;
import models.CourseSpecificInsideDepartmentStudents;
import models.Student;
import models.StudentPreference;

public class GenerateCoursePreference{
	
	/**
	 * This comparator is used for sorting 'course_preference' objects in the priority queue.
	 * It says - sort based on 'temporaryPreferenceNo' and then tie break with 'tieBreakingCriteria'
	 */
	private static GPAComparator gpaCompare = new GPAComparator();
	private static class GPAComparator implements Comparator<Student> { 
		@Override
		public int compare(Student s1, Student s2) {
			if (s1.getCGPA()>s2.getCGPA()){
				return -1;
			}
			else{
				return 1;
			}
		}
	}
	
	/**
	 * This method is used to create a fresh course preference list and dump it to a file.
	 * @param studentList
	 * @param courseList
	 * @param courseSpecificHighPriorityInfo 
	 * @param courseSpecificInsideDeptInfo 
	 * @param outputFile
	 */
	public static String createNewCoursePreferences(ArrayList<Student> studentList, ArrayList<Course> courseList, ArrayList<CourseSpecificHighPriorityStudents> courseSpecificHighPriorityInfo, ArrayList<CourseSpecificInsideDepartmentStudents> courseSpecificInsideDeptInfo){

		// Iterate over each course. Creating a course preference list in each iteration.
		for(Course tempCourse : courseList){
			//we will generate the course preference list for 'tempCourse' in this iteration
			
			//Compute the list of students applying for this course
			ArrayList<Student> studentsApplyingForThisCourse = new ArrayList<Student>();
			for (Student s : studentList){
				for (StudentPreference sp : s.studentPreferenceList){
					if (sp.getCourseObj()==tempCourse){
						studentsApplyingForThisCourse.add(s);
						break;
					}
				}
			}
			
			//Create a list of all the high priority students applying for the course
			ArrayList<Student> highPriorityAndInsideDeptStudentsInCourse = new ArrayList<Student>();
			ArrayList<Student> lowPriorityAndOutsideDeptStudentsInCourse = new ArrayList<Student>();
			ArrayList<Student> highPriorityAndOutsideDeptStudentsInCourse = new ArrayList<Student>();
			ArrayList<Student> lowPriorityAndInsideDeptStudentsInCourse = new ArrayList<Student>();
			

			for (Student st : studentsApplyingForThisCourse){
				//If the student is marked as high priority - add to the high priority list
				if (tempCourse.studentIsHighPriority(st, courseSpecificHighPriorityInfo)){
					if (tempCourse.studentIsInsideDepartment(st, courseSpecificInsideDeptInfo)){
						highPriorityAndInsideDeptStudentsInCourse.add(st);
					}
					else{
						highPriorityAndOutsideDeptStudentsInCourse.add(st);
					}
				}
				else{ //If the student is not marked as high priority
					if (tempCourse.studentIsInsideDepartment(st, courseSpecificInsideDeptInfo)){
						lowPriorityAndInsideDeptStudentsInCourse.add(st);
					}
					else{
						lowPriorityAndOutsideDeptStudentsInCourse.add(st);
					}
				}
			}
			
			// Now based on the ranking criteria, sort the students within their list (high priority or others)
			switch (tempCourse.getRankingCriteria()) {
				// If course rankingCriteria is Random.
				case Constants.random:{
					//Randomly shuffle the set of students applying for this course within their priority list
					Collections.shuffle(highPriorityAndInsideDeptStudentsInCourse);
					Collections.shuffle(highPriorityAndOutsideDeptStudentsInCourse);
					Collections.shuffle(lowPriorityAndOutsideDeptStudentsInCourse);
					Collections.shuffle(lowPriorityAndInsideDeptStudentsInCourse);
					break;
				}
                    
				// If course rankingCriteria is stratified random
				case Constants.stratifiedRandom:{
					//Shuffle the set of students applying for this course within their priority list
					highPriorityAndInsideDeptStudentsInCourse = getStratifiedRandomList(highPriorityAndInsideDeptStudentsInCourse,tempCourse);
					highPriorityAndOutsideDeptStudentsInCourse = getStratifiedRandomList(highPriorityAndOutsideDeptStudentsInCourse,tempCourse);
					lowPriorityAndInsideDeptStudentsInCourse = getStratifiedRandomList(lowPriorityAndInsideDeptStudentsInCourse,tempCourse);
					lowPriorityAndOutsideDeptStudentsInCourse = getStratifiedRandomList(lowPriorityAndOutsideDeptStudentsInCourse,tempCourse);
					break;
				}
				// If course rankingCriteria is cgpa
				case Constants.CGPA:{
					//Sort the students based on GPA within their priority list
					Collections.sort(highPriorityAndInsideDeptStudentsInCourse,gpaCompare);
					Collections.sort(highPriorityAndOutsideDeptStudentsInCourse,gpaCompare);
					Collections.sort(lowPriorityAndInsideDeptStudentsInCourse,gpaCompare);
					Collections.sort(lowPriorityAndOutsideDeptStudentsInCourse,gpaCompare);

					break;
				}
				default:{
					return "Course " + tempCourse.getcourseNumber() + " has unrecognized course ranking criteria. Exiting";
				}
			}
			
			/*  
			 * Now add the students to the studentsApplyingForThisCourseInOrder array in the following order
			 * 1. High priority outside department
			 * 2. High priority inside department
			 * 3. Low priority outside department
			 * 4. Low priority inside department
			 * (Actually we just need high priority before low priority, and outside department
			 * before inside department (because in the outside department course, outsided 
			 * department students should get a higher preference. In the inside department 
			 * course, we anyways have only inside department students). In the use case,
			 * High/Low Priority is only used for HS/MA courses - and everyone is
			 * outside deparmtent, and the inside/outside is used for other courses, and the
			 * priority of everyone is low.
			 */
			
			ArrayList<Student> studentsApplyingForThisCourseInOrder = new ArrayList<Student>();
			studentsApplyingForThisCourseInOrder.addAll(highPriorityAndOutsideDeptStudentsInCourse);
			studentsApplyingForThisCourseInOrder.addAll(highPriorityAndInsideDeptStudentsInCourse);
			studentsApplyingForThisCourseInOrder.addAll(lowPriorityAndOutsideDeptStudentsInCourse);
			studentsApplyingForThisCourseInOrder.addAll(lowPriorityAndInsideDeptStudentsInCourse);
			
			//Next add the students in 'studentsApplyingForThisCourseInOrder' as the course's preferences
			for (int i = 0; i<studentsApplyingForThisCourseInOrder.size(); i++){
            	tempCourse.coursePreferenceList.add(new CoursePreference(i+1,studentsApplyingForThisCourseInOrder.get(i)));
            }
			
		
		}
			
		return null; //that is : no errors
	}

	
	
	/**
	 * This function computes a stratified random sorting for the given 'stList'
	 * @param tempCourse 
	 * @param studentList
	 * @return
	 */
	public static ArrayList<Student> getStratifiedRandomList(ArrayList<Student> stList, Course tempCourse) {
		ArrayList<Student> listToBeReturned = new ArrayList<Student>(); //Is the list that will be returned at the end of the function
		
		//We loop through the values of 'nextRank' (represents the number that students rank this course at) 
		//We pick and add those students to the course's preference list which rank the course at 'nextRank'
		//Terminate when all the students who have applied for this course get added to the course's preference list
		for (int nextRank = 1;stList.size()>0;nextRank++){
			//This array will hold all the students who rank the course at number 'nextRank'
			ArrayList<Student> studentsWithCourseAtNextRank = new ArrayList<Student>();
			//Looping through the students to find those who rank the course at number 'nextRank'
			for (Student s : stList){
				for (StudentPreference sp : s.studentPreferenceList){
					if (sp.getCourseObj()==tempCourse && sp.getPreferenceNo()==nextRank){
						studentsWithCourseAtNextRank.add(s);
						break;
					}
				}
			}
			
			//Now that we have found the set of students who rank the course at 'nextRank'
			//Randomly shuffle these set of students
			Collections.shuffle(studentsWithCourseAtNextRank);
			
			//Add all these students in order, to the list to be returned
			listToBeReturned.addAll(studentsWithCourseAtNextRank);
            	
			//Remove these students from the 'stList' list
			stList.removeAll(studentsWithCourseAtNextRank);
		}
		
		return listToBeReturned;
	}
}