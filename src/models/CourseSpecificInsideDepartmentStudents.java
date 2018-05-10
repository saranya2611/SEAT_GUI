package models;

/**
 * An object of this class is used to represent information about
 * any course which considers a student from this 'department' and 'year'
 *  as inside department (eg. EE3002 considers EP students as inside 
 *  department, and they occupy the inside department capacity)
 * @author akshay
 *
 */
public class CourseSpecificInsideDepartmentStudents {
	public Course courseThatConsidersTheseStudentsInsideDepartment; 
	public String department;
	public int year;
	
	public CourseSpecificInsideDepartmentStudents(Course inp_course,String inp_department,int inp_year){
		courseThatConsidersTheseStudentsInsideDepartment = inp_course;
		department = inp_department;
		year = inp_year;
	}
}
