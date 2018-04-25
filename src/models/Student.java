package models;

import services.Constants;

import java.util.ArrayList;

/**
 * This class represents a physical student.
 */
public class Student {
    public int maxCreditsInSem; //The maximum credits that the student has stated he can take. It includes both elective and core credits. Read only.
    public int electiveCreditsLeft; //The elective credits left. This can change during the algorithm as this student gets allotted courses.
    public ArrayList<Course> coreCourses; //Original set of core courses. Will not be modified once read. Is set to 'unmodifiable' in the DataInput
    public ArrayList<StudentPreference> studentPreferenceList; //Original preference list. Will not be modified once read. Is set to 'unmodifiable' in the DataInput
    public ArrayList<StudentPreference> coursesUnallotted; //Set of courses from the preference list which the student can still apply to. Can be modified during the algorithm
    public ArrayList<StudentPreference> orderedListOfcoursesAllotted; //List of courses from the preference list which the student has been allotted to. Can be modified during the algorithm. It is an ordered list in the sense that the order of the arraylist should maintain the order in which the allotments to these preferences was made. This order is important for the 'ReasonsForNotAllottingPreference' module, where we iterate through the ordered list of allotted courses to compute why a particular elective was not allotted.
    public ArrayList<StudentPreference> invalidPreferences;    //Set of courses from the preference list which are invalid (could be because of a slot clash with a core course, or that the course takes more credits that the student's maxElectiveCreditsInSem
    public StudentPreference courseAllottedInCurrentIteration; //The course that the student is allotted in the current iteration of the iterative type of algorithms. Will be modified during the algorithm.
    //for statistics
    public double effectiveAverageRank; //Definition given in the documentation.
    public double creditSatisfactionRatio; //Definition given in the documentation.
    private String rollNo; //Read only.
    private float cgpa; //Read only.
    private int maxElectiveCreditsInSem; //The maximum  elective credits that the student can take. Will be calculated by subtracting the core course credits from 'maxCreditsInSem'. Read only.

    /* constructor :simply initializes the fields. */
    public Student(String inp_rollNo, float inp_cgpa, int inp_maxCredits) {
        rollNo = inp_rollNo;
        cgpa = inp_cgpa;
        coreCourses = new ArrayList<Course>();
        studentPreferenceList = new ArrayList<StudentPreference>();
        orderedListOfcoursesAllotted = new ArrayList<StudentPreference>();
        coursesUnallotted = new ArrayList<StudentPreference>();
        invalidPreferences = new ArrayList<StudentPreference>();
        maxCreditsInSem = inp_maxCredits;
    }

    /**
     * @param inp_rollNo
     * @param studentList
     * @return = This function searches the studentList for a student with the given rollNo and returns the student object
     */
    public static Student getStudentByRollNo(String inp_rollNo, ArrayList<Student> studentList) {
        for (Student tempStudent : studentList) {
            if (tempStudent == null) {
                System.out.println("Student in student List is null, as seen in the getStudentByRollNo function. Exiting");
                System.exit(1);
            } else {
                if (tempStudent.rollNo.compareTo(inp_rollNo) == 0) {
                    return tempStudent;
                }

            }
        }
        return null; //student not found
    }

    /**
     * Getter methods for private fields. No setter methods since they are read only.
     */
    public String getRollNo() {
        return rollNo;
    }

    public float getCGPA() {
        return cgpa;
    }

    public int getMaxElectiveCreditsInSem() {
        return maxElectiveCreditsInSem;
    }

    /**
     * @param inpCourse
     * @return - returns true if this students has 'inpCourse' in his preference list. Otherwise returns false.
     */
    public boolean hasCourseInPreferences(Course inpCourse) {
        for (int i = 0; i < studentPreferenceList.size(); i++) {
            if (studentPreferenceList.get(i).getCourseObj().getcourseNumber().equalsIgnoreCase(inpCourse.getcourseNumber())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the maximum elective credits for this student.
     * It does this by subtracting the credits of the core course from the 'maxCreditsInSem' variable
     */
    public void calculateMaxElectiveCredits() {
        maxElectiveCreditsInSem = maxCreditsInSem;
        for (Course c : coreCourses) {
            maxElectiveCreditsInSem -= c.getCredits();
        }
        electiveCreditsLeft = maxElectiveCreditsInSem;
    }

    /**
     * Returns the string corresponding to the department of the student by extracting
     * the first 2 letters from his roll number
     *
     * @return department string
     */
    public String getDepartment() {
        String department = rollNo.substring(0, 2);
        return department;
    }

    /**
     * Computes the year of registratio of the student from the 3rd and 4th character
     * in the roll number of the student.
     *
     * @return Year of registration of the student
     */
    public int getYear() {
        String yearString = "20" + rollNo.substring(2, 4);
        return Integer.parseInt(yearString);
    }

    /**
     * Computes the maxCreditLimit that a student can have
     * By default, it is set to the Constants.maxCreditDefaultLimit value (at the time of writing this was 60)
     * But if the department has provided a separate limit, then we retu
     *
     * @param deptSpecificMaxCreditLimitInfo
     * @return maxCreditLimit that the student can have
     */
    public int maxCreditLimitForStudent(ArrayList<DepartmentSpecificMaxCreditLimit> deptSpecificMaxCreditLimitInfo) {
        //The credit limit is initially set to the default value
        int maxCreditLimit = Constants.maxCreditDefaultLimit;

        //If we find a special credit limit provided by the department, change the
        //maxCreditLimit to reflect that.
        for (DepartmentSpecificMaxCreditLimit limitInfo : deptSpecificMaxCreditLimitInfo) {
            //Both the department and year of the student should match
            if (limitInfo.department.equalsIgnoreCase(getDepartment()) && limitInfo.year == getYear()) {
                maxCreditLimit = limitInfo.maxCreditLimit;
            }
        }
        return maxCreditLimit;
    }
}