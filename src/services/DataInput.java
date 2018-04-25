package services;

import models.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class reads data from the input file and populates the java objects.
 * All reading from files is done in this file.
 * <p>
 * ALL FILE FORMATS ARE GIVEN IN THE README AND CODE DOCUMENTATION
 */
public class DataInput {

    /**
     * This function reads the data from the file and creates the student objects.
     * It takes the input file as an argument
     *
     * @param deptSpecificMaxCreditLimitInfo
     */
    public static ArrayList<Student> populateStudents(String inputFile) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        ArrayList<Student> studentList = new ArrayList<Student>();
        Student newStudent;

        //reading input line by line and adding a new student for every line.
        try {
            //open input file and start reading
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read input file line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);
                //create the new student object and add to student list
                newStudent = new Student(inputLine[0], Float.parseFloat(inputLine[1]), Integer.parseInt(inputLine[2]));
                studentList.add(newStudent);
            }
            br.close(); //closing file pointer
            //just some exception handling.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentList;

    }

    /**
     * This function reads the data from the file and creates the course objects.
     * It takes the input file as an argument
     *
     * @param slots
     * @param errorMsgList
     */
    public static ArrayList<Course> populateCourses(String inputFile, ArrayList<Slot> slots, String[] errorMsgList) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        ArrayList<Course> courseList = new ArrayList<Course>();
        Course insideDeptCourse;
        Course outsideDeptCourse;
        Slot tempSlot;

        //reading input line by line and adding a new course for every line.
        try {
            //open input file and start reading
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read input file line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);

                //Add the slots for the course
                ArrayList<Slot> tempSlotList = new ArrayList<Slot>();
                for (int i = 6; i < inputLine.length; i++) {
                    tempSlot = Slot.getSlotByName(inputLine[i], slots);
                    if (tempSlot == null) { //If we could not find the slot : Course is in an invalid slot. Throw error
                        errorMsgList[0] = "Course " + inputLine[0] + " is in the slot " + inputLine[i] + " which is not mentioned in the slot timings input.";
                        br.close();
                        return null;
                    }
                    tempSlotList.add(tempSlot);
                }


                //Compute all the parameters required to invoke the 'new Course()' constructor
                String courseNumber = inputLine[0];
                String courseNumberInsideDepartment = courseNumber + Constants.insideDepartmentSuffix;
                String courseNumberOutsideDepartment = courseNumber + Constants.outsideDepartmentSuffix;

                String department = inputLine[1];
                int totalCapacity = Integer.parseInt(inputLine[2]);
                int outsideDepartmentCapacity = Integer.parseInt(inputLine[3]);
                int insideDepartmentCapacity = totalCapacity - outsideDepartmentCapacity;
                int rankingCriteria = Integer.parseInt(inputLine[4]);
                int credits = Integer.parseInt(inputLine[5]);

                //create the 2 new course object - 1 for inside department, and the other for outside department. Add them to the course list
                outsideDeptCourse = new Course(courseNumberOutsideDepartment, department, outsideDepartmentCapacity, rankingCriteria, credits, tempSlotList, courseNumber, false, null);
                insideDeptCourse = new Course(courseNumberInsideDepartment, department, insideDepartmentCapacity, rankingCriteria, credits, tempSlotList, courseNumber, true, outsideDeptCourse);
                courseList.add(insideDeptCourse);
                courseList.add(outsideDeptCourse);
            }
            br.close(); //closing file pointer
            //just some exception handling.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courseList;
    }


    /**
     * This function populates the studentPreferenceList field of all students by reading the input preference list
     * file (whose name is passed as an argument).
     * <p>
     * It also ensures that every student and course on the preference lists also exists in the student and course list respectively
     *
     * @param courseSpecificInsideDeptInfo
     */
    public static String populateStudentPreferenceLists(ArrayList<Student> studentList, ArrayList<Course> courseList, String inputFile, ArrayList<CourseSpecificInsideDepartmentStudents> courseSpecificInsideDeptInfo) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        Student tempStudent;
        Course tempCourseInsideDept;
        Course tempCourseOutsideDept;
        int newColourNumber = 1000;

        //reading input line by line and adding a new student for every line.
        try {
            //open input file and start reading
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read input file line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);

                //Get the various columns that are got after split the line
                String studentRollNumber = inputLine[0];
                String originalcourseNumber = inputLine[1];

                //Set colour to -1 if the input file says NULL. Else read the value.
                int colour;
                if (inputLine[2].equalsIgnoreCase("NULL")) {
                    colour = -1;
                } else {
                    colour = Integer.parseInt(inputLine[2]);
                }

                //Now if the colour = 'no colour' option, give it a new, unused colour number
                if (colour == Constants.noColourOption) {
                    colour = newColourNumber++;
                }

                String type = inputLine[3];

                //Set colour to -1 if the input file says NULL. Else read the value.
                int preferenceNumber;
                if (inputLine[4].equalsIgnoreCase("NULL")) {
                    preferenceNumber = -1;
                } else {
                    preferenceNumber = Integer.parseInt(inputLine[4]);
                }

                //Get the student object corresponding to this preference
                tempStudent = Student.getStudentByRollNo(studentRollNumber, studentList);
                if (tempStudent == null) { //If the student does not exist in the student list - throw an error
                    br.close();
                    return "Student : " + studentRollNumber + " does not exist but has an entry in the student preference list";
                }

                //Get the inside department course object corresponding to this preference
                tempCourseInsideDept = Course.getCourseBycourseNumber(originalcourseNumber + Constants.insideDepartmentSuffix, courseList);
                if (tempCourseInsideDept == null) { //The course does not exist in the course list - error
                    br.close();
                    return "Course: " + originalcourseNumber + " does not exist, but the student " + tempStudent.getRollNo() + " has given a preference for it";
                }

                //Get the outside department course object corresponding to this preference
                tempCourseOutsideDept = Course.getCourseBycourseNumber(originalcourseNumber + Constants.outsideDepartmentSuffix, courseList);
                if (tempCourseOutsideDept == null) { //The course does not exist in the course list - error
                    br.close();
                    return "Course: " + originalcourseNumber + " does not exist, but the student " + tempStudent.getRollNo() + " has given a preference for it";
                }

                //Add to the list of core courses or elective courses depending on what is given
                if (type.equalsIgnoreCase(Constants.coreCourse) || type.equalsIgnoreCase(Constants.backlogCourse)) { //Is a core course
                    tempStudent.coreCourses.add(tempCourseInsideDept);
                } else { //else if the course is an elective
                    if (tempCourseInsideDept.studentIsInsideDepartment(tempStudent, courseSpecificInsideDeptInfo)) { //If the course considers the student as inside department
                        //Add both the inside and outside department versions of the course (in that respective order)
                        //The reason for using the multiplication by 2 when we add the preference number, is that - if a student has 2 courses on his preference list (c1,c2) which consider him as inside department, each of those will have an inside and outside version. So his preference list should actually look like - (c1Inside,c1Outside,c2Inside,c2Outside). Hence we use the multiplication by 2 to achieve this expanded preference list. The possible gaps that this may introduce is fixed in the latter part of this function
                        tempStudent.studentPreferenceList.add(new StudentPreference(tempCourseInsideDept, colour, preferenceNumber * 2));
                        tempStudent.studentPreferenceList.add(new StudentPreference(tempCourseOutsideDept, colour, preferenceNumber * 2 + 1));
                    } else { //else the course considers the student as outside department
                        //Add only the outside department version of the course
                        tempStudent.studentPreferenceList.add(new StudentPreference(tempCourseOutsideDept, colour, preferenceNumber * 2 + 1));
                    }

                }


            }
            br.close(); //closing file pointer
            //just some exception handling.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Now that we have read the student preference list, loop through the set of students.
        for (Student st : studentList) {
            //Sort the preference list based on the preference numbers provided
            Collections.sort(st.studentPreferenceList);
            //Now relabel the preference numbers : This may be necessary if the original preference numbers had gaps, and also because we could have introduced gaps when we created the preference object
            for (int i = 0; i < st.studentPreferenceList.size(); i++) {
                st.studentPreferenceList.get(i).setPreferenceNumberToPositionInPreferenceList(st.studentPreferenceList);
            }
        }
        return null;
    }

    /**
     * This method is used to load the course preference lists from an existing one
     *
     * @param studentList - List of students objects
     * @param courseList  - list of course objects
     * @param inpFile     - file where the course preference lists are located
     *                    <p>
     *                    It also ensures that every student and course on the preference lists also exists in the student and course list respectively
     */
    public static String populateCoursePreferenceLists(ArrayList<Student> studentList, ArrayList<Course> courseList, String inpFile) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        Student tempStudent;
        Course tempCourse;

        //reading input line by line and adding a new student for every line.
        try {
            BufferedReader br = new BufferedReader(new FileReader(inpFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);
                tempCourse = Course.getCourseBycourseNumber(inputLine[0], courseList);  //The first string has the course Id with which we can get the course object it corresponds to
                if (tempCourse == null) { //The course does not exist in the course list : return error message
                    br.close();
                    return "Course : " + inputLine[0] + " does not exist, but it has an entry in the preference list";
                }

                for (int i = 1; i < inputLine.length; i++) {  //loop through the students and add them to the course's preference list
                    tempStudent = Student.getStudentByRollNo(inputLine[i], studentList);
                    if (tempStudent == null) { //The student does not exist in the student list : return error message
                        br.close();
                        return "Student : " + inputLine[i] + " does not exist but was on the preference list of course " + inputLine[0];
                    }
                    tempCourse.coursePreferenceList.add(new CoursePreference(i, tempStudent));
                }
            }
            br.close();//closing file pointer
            //just some exception handling
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This function populates the slots by reading slot timings from the input
     * file (whose name is passed as an argument).
     */
    public static ArrayList<Slot> populateSlots(String inpFile) {
        //declarations
        String line;
        String[] inputLine;
        ArrayList<Slot> slots = new ArrayList<Slot>();
        ArrayList<ClassTiming> tempClassTimings;

        try {
            BufferedReader br = new BufferedReader(new FileReader(inpFile));
            //Skip the first line since it will be the header row
            br.readLine();
            while ((line = br.readLine()) != null) {  //read line by line
                line.replaceAll("\\s+", ""); //Remove all white spaces
                inputLine = line.split(","); //Split line on commas
                String slotName = inputLine[0]; //First column of csv file is the slot name

                tempClassTimings = new ArrayList<ClassTiming>(); //Create a list of class timings for this slot

                for (int i = 1; i < inputLine.length; i++) {  //loop through the class timings and add them to the list
                    String[] timing = inputLine[i].split("="); //Split line on '='
                    String dayOfTheWeek = timing[0]; //First part is the day of the week
                    String[] duration = timing[1].split("-"); //Remaining part is the duration and is split again at '-'
                    String startTime = duration[0]; //The first part corresponds to the start time of the slot
                    String endTime = duration[1]; //The second part corresponds to the end time of the slot
                    int startHour = Integer.parseInt(startTime.split(":")[0]); //Get the hour part of the start time
                    int startMinute = Integer.parseInt(startTime.split(":")[1]); //Get the minute part of the start time
                    int endHour = Integer.parseInt(endTime.split(":")[0]); //Get the hour part of the end time
                    int endMinute = Integer.parseInt(endTime.split(":")[1]); //Get the minute part of the end time
                    tempClassTimings.add(new ClassTiming(startHour, endHour, startMinute, endMinute, dayOfTheWeek));
                }
                slots.add(new Slot(slotName, tempClassTimings)); //Add the new slot
            }
            br.close();//closing file pointer
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return slots;
    }


    /**
     * This function reads the config file which specifies certain courses that consider other department
     * students as inside department
     *
     * @param inpFile      - name of the input file
     * @param courseList   - courseList that has already been populated
     * @param errorMsgList - holder for any error message
     * @return The list of CourseSpecificInsideDepartmentStudents which contain information about courses that consider other department
     * students as inside department
     */
    public static ArrayList<CourseSpecificInsideDepartmentStudents> populateInsideDepartmentConfigs(String inpFile, ArrayList<Course> courseList, String[] errorMsgList) {

        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        ArrayList<CourseSpecificInsideDepartmentStudents> courseSpecificInsideDeptInfo = new ArrayList<CourseSpecificInsideDepartmentStudents>();
        Course tempCourseInsideDept, tempCourseOutsideDept;
        //reading input line by line and adding a new student for every line.
        try {
            BufferedReader br = new BufferedReader(new FileReader(inpFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);

                tempCourseInsideDept = Course.getCourseBycourseNumber(inputLine[0] + Constants.insideDepartmentSuffix, courseList);  //The first string has the course Id with which we can get the course object it corresponds to
                if (tempCourseInsideDept == null) { //The course does not exist in the course list : return error message
                    br.close();
                    errorMsgList[0] = "Course : " + inputLine[0] + " does not exist, but it has an entry in the inside department config file";
                    return null;
                }

                tempCourseOutsideDept = Course.getCourseBycourseNumber(inputLine[0] + Constants.outsideDepartmentSuffix, courseList);  //The first string has the course Id with which we can get the course object it corresponds to
                if (tempCourseOutsideDept == null) { //The course does not exist in the course list : return error message
                    br.close();
                    errorMsgList[0] = "Course : " + inputLine[0] + " does not exist, but it has an entry in the inside department config file";
                    return null;
                }

                for (int i = 1; i < inputLine.length; i++) {  //loop through the departments and add 2 CourseSpecificHighPriorityStudents objects for each of them (1 for the inside department version, and 1 for the outside department version)
                    String departmentAndYear = inputLine[i];
                    String department = departmentAndYear.substring(0, 2);
                    int year = Integer.parseInt("20" + departmentAndYear.substring(2, 4));
                    CourseSpecificInsideDepartmentStudents insideDeptInfo1 = new CourseSpecificInsideDepartmentStudents(tempCourseInsideDept, department, year);
                    CourseSpecificInsideDepartmentStudents insideDeptInfo2 = new CourseSpecificInsideDepartmentStudents(tempCourseOutsideDept, department, year);
                    courseSpecificInsideDeptInfo.add(insideDeptInfo1);
                    courseSpecificInsideDeptInfo.add(insideDeptInfo2);
                }
            }
            br.close();//closing file pointer
            //just some exception handling
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseSpecificInsideDeptInfo;
    }

    /**
     * This function reads the config file which specifies certain courses that consider some department
     * students as high priority
     *
     * @param inpFile      - name of the input file
     * @param courseList   - courseList that has already been populated
     * @param errorMsgList - holder for any error message
     * @return The list of CourseSpecificHighPriorityStudents which contain information about courses that consider other department
     * students as high priority
     */
    public static ArrayList<CourseSpecificHighPriorityStudents> populateHighPriorityCoursePreferences(String inpFile, ArrayList<Course> courseList, String[] errorMsgList) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        ArrayList<CourseSpecificHighPriorityStudents> courseSpecificHighPriorityInfo = new ArrayList<CourseSpecificHighPriorityStudents>();
        Course tempCourseInsideDept, tempCourseOutsideDept;
        //reading input line by line and adding a new student for every line.
        try {
            BufferedReader br = new BufferedReader(new FileReader(inpFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);

                tempCourseInsideDept = Course.getCourseBycourseNumber(inputLine[0] + Constants.insideDepartmentSuffix, courseList);  //The first string has the course Id with which we can get the course object it corresponds to
                if (tempCourseInsideDept == null) { //The course does not exist in the course list : return error message
                    br.close();
                    errorMsgList[0] = "Course : " + inputLine[0] + " does not exist, but it has an entry in the inside department config file";
                    return null;
                }

                tempCourseOutsideDept = Course.getCourseBycourseNumber(inputLine[0] + Constants.outsideDepartmentSuffix, courseList);  //The first string has the course Id with which we can get the course object it corresponds to
                if (tempCourseOutsideDept == null) { //The course does not exist in the course list : return error message
                    br.close();
                    errorMsgList[0] = "Course : " + inputLine[0] + " does not exist, but it has an entry in the inside department config file";
                    return null;
                }

                for (int i = 1; i < inputLine.length; i++) {  //loop through the departments and add 2 CourseSpecificHighPriorityStudents objects for each of them (1 for the inside department version, and 1 for the outside department version)
                    String departmentAndYear = inputLine[i];
                    String department = departmentAndYear.substring(0, 2);
                    int year = Integer.parseInt("20" + departmentAndYear.substring(2, 4));
                    CourseSpecificHighPriorityStudents tempHighPriorityInfo1 = new CourseSpecificHighPriorityStudents(tempCourseInsideDept, department, year);
                    CourseSpecificHighPriorityStudents tempHighPriorityInfo2 = new CourseSpecificHighPriorityStudents(tempCourseOutsideDept, department, year);
                    courseSpecificHighPriorityInfo.add(tempHighPriorityInfo1);
                    courseSpecificHighPriorityInfo.add(tempHighPriorityInfo2);
                }
            }
            br.close();//closing file pointer
            //just some exception handling
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseSpecificHighPriorityInfo;
    }

    /**
     * This function reads the input of custom maxCredits limit (other than the default 60) that some departments
     * have specified
     *
     * @param inpFile
     * @return - The list of DepartmentSpecificMaxCreditLimit which contain information about departments that
     * have different maxCredits limits for their students (other than the default 60)
     */
    public static ArrayList<DepartmentSpecificMaxCreditLimit> populateMaxCreditsLimits(String inpFile) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        ArrayList<DepartmentSpecificMaxCreditLimit> deptSpecificMaxCreditLimitInfo = new ArrayList<DepartmentSpecificMaxCreditLimit>();

        //reading input line by line and adding a new student for every line.
        try {
            BufferedReader br = new BufferedReader(new FileReader(inpFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);
                String departmentAndYear = inputLine[0];
                String department = departmentAndYear.substring(0, 2);
                int year = Integer.parseInt("20" + departmentAndYear.substring(2, 4));
                int maxCreditsLimit = Integer.parseInt(inputLine[1]);
                DepartmentSpecificMaxCreditLimit tempLimit = new DepartmentSpecificMaxCreditLimit(department, year, maxCreditsLimit);
                deptSpecificMaxCreditLimitInfo.add(tempLimit);
            }
            br.close();//closing file pointer
            //just some exception handling
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deptSpecificMaxCreditLimitInfo;
    }

    /**
     * This function reads the studentList, which is an array of all students and makes a exhaustive list of possible batches.
     *
     * @param studentList - The array of students
     * @return The list of Batches
     */
    public static ArrayList<Batch> populateListOfBatches(ArrayList<Student> studentList) {
        ArrayList<Batch> listOfBatches = new ArrayList<Batch>();

        HashMap<String, String> batches = new HashMap<String, String>();
        for (Student s : studentList) {
            String rollNo = s.getRollNo();
            String batch = rollNo.substring(0, 4);

            // If we have not yet added the batch add the batch
            if (!batches.containsKey(batch)) {
                batches.put(batch, batch);
            }
        }

        for (String b : batches.keySet()) {
            Batch batchToAdd = new Batch(b);
            listOfBatches.add(batchToAdd);
        }

        return listOfBatches;
    }

    /**
     * This function reads the config file which specifies certain batches for which few elective types are recommended
     *
     * @param inpFile      - name of the input file
     * @param errorMsgList - holder for any error message
     * @return The list of BatchSpecificMandatedElectives which contain information about batches and the mandated electives
     */
    public static ArrayList<BatchSpecificMandatedElectives> populateBatchSpecificMandatedElectiveType(String inpFile) {
        //Some declarations
        String line;
        String[] inputLine;
        String splitBy = ",";
        ArrayList<BatchSpecificMandatedElectives> batchSpecificMandatedElectiveTypeInfo = new ArrayList<BatchSpecificMandatedElectives>();

        //reading input line by line and adding a new student for every line.
        try {
            BufferedReader br = new BufferedReader(new FileReader(inpFile));
            //Skip the first line since it will be the header row
            br.readLine();
            //read line by line
            while ((line = br.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(splitBy);

                String batch = inputLine[0];
                for (int i = 1; i < inputLine.length; i++) {  //loop through the elective types and add BatchSpecificMandatedElectiveType object.
                    String electiveType = inputLine[i];
                    BatchSpecificMandatedElectives tempMandatedElectiveTypeInfo = new BatchSpecificMandatedElectives(batch, electiveType);
                    batchSpecificMandatedElectiveTypeInfo.add(tempMandatedElectiveTypeInfo);
                }
            }
            br.close();//closing file pointer
            //just some exception handling
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchSpecificMandatedElectiveTypeInfo;
    }

}