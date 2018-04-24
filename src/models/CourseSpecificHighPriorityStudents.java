package models;

/**
 * An object of this class is used to represent information about 
 * any course which considers a student from this 'department' and 'year' as 
 * high priority (eg. Some HS courses consider 2nd year students from EE,CS and ME as 
 * high priority since they have a HS elective in their curriculum)
 * @author akshay
 *
 */
public class CourseSpecificHighPriorityStudents {
	public Course courseThatConsidersTheseStudentsHighPriority; 
	public String department;
	public int year;
	
	public CourseSpecificHighPriorityStudents(Course inp_course,String inp_department,int inp_year){
		courseThatConsidersTheseStudentsHighPriority = inp_course;
		department = inp_department;
		year = inp_year;
	}
}
