package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import models.Course;
import models.CoursePreference;
import models.Slot;
import models.Student;
import models.StudentPreference;

/* FREEZE THE PREFERENCE LISTS (so that they can't be modified) :
 * The purpose of doing this is so that the algorithms do not modify the input lists by mistake.
 * If the input lists are modified by the algorithm, it will affect all the steps coming after it
 * including - Statistics,OutputSanitization,DataOutput
 */
public class MakeInputListsUnmodifiable {
	//Makes input lists unmodifiable. Note that the individual list elements can still be modified
	public static void makePreferenceListsUnmodifiable(ArrayList<Student> studentList,ArrayList<Course> courseList){
		//All student preference lists (both elective and core) are made unmodifiable
		for (Student s : studentList){
			s.studentPreferenceList = (ArrayList<StudentPreference>) Collections.unmodifiableList(s.studentPreferenceList);
			s.coreCourses = (ArrayList<Course>) Collections.unmodifiableList(s.coreCourses);
		}
		//All course preference lists are made unmodifiable
		for (Course c : courseList){
			c.coursePreferenceList = (ArrayList<CoursePreference>) Collections.unmodifiableList(c.coursePreferenceList);
		}
	}

	//Makes the slots array unmodifiable
	public static void makeCourseSlotsUnmodifiable(ArrayList<Course> courseList) {
		for (Course c : courseList){
			c.slots = (ArrayList<Slot>) Collections.unmodifiableList(c.slots);
		}
		
	}
}
