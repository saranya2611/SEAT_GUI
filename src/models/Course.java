package models;

import java.util.ArrayList;
import java.util.HashMap;

import services.Constants;


/**
 * This class represents a course offered in the institute (eg. CS1200)
 */
public class Course{ //Note that the terms 'course number' and 'courseNumber' have been used interchangeably. They both refer to the course number like CS2200.
	private String courseNumberToPrint; //(Also known as the course number)Has the course number that we need to use when printing the output. Should not be used for any other purposes. Read only.
	private String courseNumber; // Note that this name is a modified version of the original course number. It has $inside or $outside appended to it. This is the field that has to be used for all operations except while finally printing the output. If you want the original name of the course, read the This is a Read only field.
	private String department; //Department.Read only. 
	private int credits; //The number of credits of the course. Read only.
	private int capacity; //The number of students the course can accomodate. Read only.
	public int capacityStillFree; //The capacity left in the course. Will be modified during the course of the algorithm as the students get allocated to it. Can be modified.
	public ArrayList<Slot> slots; //The slots that the course is in. It is an arraylist in order to support courses with multiple slots. Read only.
	private int rankingCriteria; //takes only 3 values - 1,2,3 : as defined in service/constants.java
	public ArrayList<CoursePreference> coursePreferenceList; //The courses preference list over students. Read only.
	public ArrayList<CoursePreference> currentIterationStudentAllottedList; //The students allotted to the course during the algorithm. Can be modified anyhow, as is required by the algorithm
	public int noOfRejections; //keeps track of the number of rejections in the algorithm. It is used as a statistic to determine popular courses
	
	private boolean isInsideDepartmentCourse; //true if course is an inside department version of the course.
	private Course correspondingOutsideDepartmentCourse; //Gives the outside department course corresponding to this inside department course. If this itself is an outside department course, then this field is null.
	
	//constructor simply initializes the fields.
	public Course (String inp_courseNumber,String inp_department,int inp_capacity,int inp_rankingCriteria, int inp_credits,ArrayList<Slot> inp_slots,String inp_courseNumberToPrint,boolean inp_isInsideDepartmentCourse,Course inp_correspondingOutsideDepartmentCourse){
		courseNumber=inp_courseNumber;
		courseNumberToPrint = inp_courseNumberToPrint;
		department = inp_department;
		credits=inp_credits;
		rankingCriteria = inp_rankingCriteria;
		capacity = inp_capacity;
		capacityStillFree = capacity;
		noOfRejections = 0;
		slots = inp_slots;
		coursePreferenceList = new ArrayList<CoursePreference>();
		isInsideDepartmentCourse = inp_isInsideDepartmentCourse;
		correspondingOutsideDepartmentCourse = inp_correspondingOutsideDepartmentCourse;
	}
	
	/**
	 * Getter methods for private fields. No setter methods because they are intended to be read only
	 */
	public String getcourseNumber(){
		return courseNumber;
	}
	public int getCredits(){
		return credits;
	}
	public int getCapacity(){
		return capacity;
	}
	public int getRankingCriteria(){
		return rankingCriteria;
	}
	
	public boolean isAnInsideDeptCourse(){
		return isInsideDepartmentCourse;
	}
	
	public Course getCorrespondingOutsideDepartmentCourse(){
		return correspondingOutsideDepartmentCourse;
	}

	public String getcourseNumberToPrint(){
		return courseNumberToPrint;
	}
	/**
	 * 
	 * @param inp_courseNumber
	 * @param courseList
	 * This function searches the courseList for a course with courseNumber='inp_courseNumber' and return the course object
	 */
	public static Course getCourseBycourseNumber(String inp_courseNumber,ArrayList<Course> courseList){
		for (Course tempCourse : courseList){
			if (tempCourse == null){
				System.out.println("course in course List is null, as seen in the getCourseBycourseNumber function. Exiting");
				System.exit(1);
			}
			else {
				if (tempCourse.courseNumber.compareTo(inp_courseNumber)==0){
					return tempCourse;
				}
					
			}
		}
		return null; //course not found. 
		//Remember to account for the possibility of this function returning null, else you could get a null pointer exception
	}
	
	/**
	 * @param Course c
	 * @return true if this course has a slot clash with course 'c'. i.e. if any of their class timings overlap
	 */
	public boolean slotClashWithCourse(Course c){
		//Loop through all the combination of slots for the 2 courses
		for (Slot slot1 : slots){
			for (Slot slot2 : c.slots){
				//If even 1 combination of slots conflicts - return true
				if (slot1.clashesWithSlot(slot2)){
					return true;
				}
			}
		}
		//If we got so far, there is no slot clash.
		return false;
	}
	
	/**
	 * Tells you if the student is considered as an inside department student by this course
	 * @param st
	 * @param courseSpecificInsideDeptInfo
	 * @return
	 */
	public boolean studentIsInsideDepartment(Student st, ArrayList<CourseSpecificInsideDepartmentStudents> courseSpecificInsideDeptInfo){
		//Check if the student's department matches the department of the course
		if (st.getDepartment().equalsIgnoreCase(department)){
			return true; //The student is from the same department as the course
		}
		//If not, check if there is special information regarding this student
		//being treated as inside department
		for (CourseSpecificInsideDepartmentStudents insideDeptInfo : courseSpecificInsideDeptInfo){
			if (st.getDepartment().equalsIgnoreCase(insideDeptInfo.department) && st.getYear()==insideDeptInfo.year && insideDeptInfo.courseThatConsidersTheseStudentsInsideDepartment==this){
				return true; //The course inside department info says it is true
			}
		}
		
		//If not then the student is to be treated as outside department
		return false;
	}
	
	/**
	 * //Check if this course consideres 'st' as a high priority on it's preference list 
	 * (based on the list 'courseSpecificHighPriorityInfoinput' which gets it's
	 * information from the highPriorityCoursePreferencesConfigFile file )
	 * @param st
	 * @param courseSpecificHighPriorityInfo
	 * @return
	 */
	public boolean studentIsHighPriority(Student st, ArrayList<CourseSpecificHighPriorityStudents> courseSpecificHighPriorityInfo){
		for (CourseSpecificHighPriorityStudents highPriorityInfo : courseSpecificHighPriorityInfo){
			if (st.getDepartment().equalsIgnoreCase(highPriorityInfo.department) && st.getYear()==highPriorityInfo.year && highPriorityInfo.courseThatConsidersTheseStudentsHighPriority==this){
				return true; //The course inside department info says it is true
			}
		}
		return false; //No such information was found
	}
}