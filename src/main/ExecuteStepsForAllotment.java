package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import models.Batch;
import models.BatchSpecificMandatedElectives;
import models.Course;
import models.CourseSpecificHighPriorityStudents;
import models.CourseSpecificInsideDepartmentStudents;
import models.DepartmentSpecificMaxCreditLimit;
import models.Slot;
import models.Student;
import services.CheckInputFormats;
import services.CommonAlgorithmUtilities;
import services.Constants;
import services.DataInput;
import services.DataOutput;
import services.ExchangeUnstablePairs;
import services.FirstPreferenceAllotmentAlgorithm;
import services.GenerateCoursePreference;
import services.GetStatistics;
import services.InputSanitization;
import services.IterativeHRalgorithm;
import services.MakeInputListsUnmodifiable;
import services.OutputSanitization;
import services.ReasonsForNotAllottingPreferences;
import services.SlotBasedHRalgorithm;

/*
 * This is the function where the execution of the program starts
 */
public class ExecuteStepsForAllotment {
	
	public static boolean GUIpresentGlobal;
	
	public static void executeAllotmentSteps(String slotsFile, String studentListFile, String courseListFile, String studentPreferenceListFile, String coursePreferenceListFile, boolean generateFreshCoursePreferences, int algorithm, String outputFolder, String insideDepartmentConfigFile, String highPriorityCoursePreferencesConfigFile, String departmentWiseMaxCreditLimitFile, String batchSpecificMandatedElectivesFile, boolean GUIpresent, gui.defaultSetOfFilesWizard3 wizard3) throws IOException {
		
		//Some declarations
		ArrayList<Student> studentList;
		ArrayList<Student> originalStudentList;//Is a deep copy(hence modifications to student list will not modify it) of the studentList
		ArrayList<Course> courseList;
		ArrayList<Slot> slots;
		String errorMsg;
        ArrayList<DepartmentSpecificMaxCreditLimit> deptSpecificMaxCreditLimitInfo = new ArrayList<DepartmentSpecificMaxCreditLimit>();
        ArrayList<CourseSpecificHighPriorityStudents> courseSpecificHighPriorityInfo = new ArrayList<CourseSpecificHighPriorityStudents>();
        ArrayList<CourseSpecificInsideDepartmentStudents> courseSpecificInsideDeptInfo = new ArrayList<CourseSpecificInsideDepartmentStudents>();
        ArrayList<BatchSpecificMandatedElectives> batchSpecificMandatedElectivesInfo = new ArrayList<BatchSpecificMandatedElectives>();
        ArrayList<Batch> listOfBatches = new ArrayList<Batch>();
        GUIpresentGlobal = GUIpresent;
        
        //Creating the output folder if it doesn't exist
        new File(outputFolder).mkdir();
        
      	/* CHECKING FILE FORMATS FOR INPUT DATA (to ensure that the file reading doesn't face any error)*/
        wizard3.printProgressNotification("Checking file formats of Input files");
        
        //Check slots file format
        errorMsg = CheckInputFormats.checkSlotsFileFormat(slotsFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the slots file. " + errorMsg);
			System.exit(1);
		}
		
		//Check student list file format
		errorMsg = CheckInputFormats.checkStudentListFileFormat(studentListFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the studentList file. " + errorMsg);
			System.exit(1);
		}
		
		//Checking course list file fomat
		errorMsg = CheckInputFormats.checkCourseListFileFormat(courseListFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the course list file. " + errorMsg);
			System.exit(1);
		}
		
		//Checking the student preference list file format
		errorMsg = CheckInputFormats.checkStudentPreferenceListFileFormat(studentPreferenceListFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the student preference list file. " + errorMsg);
			System.exit(1);
		}
		
		//Checking the course preference list file format if we are taking it as an input (and not generating a fresh list)
		if (generateFreshCoursePreferences==false){
			errorMsg = CheckInputFormats.checkCoursePreferenceListFileFormat(coursePreferenceListFile);
			if (errorMsg!=null){
				wizard3.printMessage("Exiting due to error in the format of the course preference list file. " + errorMsg);
				System.exit(1);
			}
		}
		
		//Check insideDepartmentConfigFile file format
		errorMsg = CheckInputFormats.checkInsideDepartmentConfigFileFormat(insideDepartmentConfigFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the studentList file. " + errorMsg);
			System.exit(1);
		}
		
		//Check specialCoursePreferencesConfigFile file format
		errorMsg = CheckInputFormats.checkHighPriorityCoursePreferencesConfigFileFormat(highPriorityCoursePreferencesConfigFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the studentList file. " + errorMsg);
			System.exit(1);
		}
		
		//Check departmentWiseMaxCreditLimitFile file format
		errorMsg = CheckInputFormats.checkDepartmentWiseMaxCreditLimitFileFormat(departmentWiseMaxCreditLimitFile);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in the format of the studentList file. " + errorMsg);
			System.exit(1);
		}
		
		
		
		
		/* DATA INPUT */
		wizard3.printProgressNotification("Populating data.......");
		
		//Read the slots data
		slots = DataInput.populateSlots(slotsFile);
		
		
		//Read the input course list
		String [] errorMsgList = new String[1]; //We are using an array for the error since we want to simulate a pass by reference for the errorMsgList argument
		courseList = DataInput.populateCourses(courseListFile,slots,errorMsgList);
		if (courseList==null){ //There was some error. That is why it is null
			wizard3.printMessage("Exiting. " + errorMsgList[0]);
			System.exit(1);
		}
  
		//Read the inside department config file
		errorMsgList = new String[1]; //We are using an array for the error since we want to simulate a pass by reference for the errorMsgList argument
        courseSpecificInsideDeptInfo = DataInput.populateInsideDepartmentConfigs(insideDepartmentConfigFile,courseList,errorMsgList);
		if (courseSpecificInsideDeptInfo==null){ //There was some error. That is why it is null
			wizard3.printMessage("Exiting. " + errorMsgList[0]);
			System.exit(1);
		}
		
		//Read the High Priority Course Preferences
		errorMsgList = new String[1]; //We are using an array for the error since we want to simulate a pass by reference for the errorMsgList argument
		courseSpecificHighPriorityInfo = DataInput.populateHighPriorityCoursePreferences(highPriorityCoursePreferencesConfigFile,courseList,errorMsgList);
		if (courseSpecificHighPriorityInfo==null){ //There was some error. That is why it is null
			wizard3.printMessage("Exiting. " + errorMsgList[0]);
			System.exit(1);
		}
		
		//Read the department wise maxCredits limits
		deptSpecificMaxCreditLimitInfo =  DataInput.populateMaxCreditsLimits(departmentWiseMaxCreditLimitFile);

		//Read the batch specific mandated electives list
		batchSpecificMandatedElectivesInfo = DataInput.populateBatchSpecificMandatedElectiveType(batchSpecificMandatedElectivesFile);
		
		//Read the student list data
		studentList = DataInput.populateStudents(studentListFile);
		//Make a deep copy of the student list (since it may get modified in the Input Sanitization)
		originalStudentList = CommonAlgorithmUtilities.copyArrayList(studentList);

		// TODO : Need to complete this function.
		listOfBatches = DataInput.populateListOfBatches(studentList);

		
		//Read the input student preference lists
		errorMsg = DataInput.populateStudentPreferenceLists(studentList,courseList,studentPreferenceListFile,courseSpecificInsideDeptInfo);
		if (errorMsg!=null){
			wizard3.printMessage("Exiting due to error in student preferences file. " + errorMsg);
			System.exit(1);
		}
		
		//Course preference list
		//If you want to create a fresh course preference list
		if(generateFreshCoursePreferences==true){
			GenerateCoursePreference.createNewCoursePreferences(studentList, courseList,courseSpecificHighPriorityInfo,courseSpecificInsideDeptInfo);
			DataOutput.printCoursePreferenceListToFile(courseList,outputFolder + "/" + coursePreferenceListFile);
		}
		//Otherwise if you want to read the course preference list from a file
		else{
			//Read the input course preference list list
			errorMsg = DataInput.populateCoursePreferenceLists(studentList, courseList,coursePreferenceListFile);
			if (errorMsg!=null){
				wizard3.printMessage("Exiting. " + errorMsg);
				System.exit(1);
			}
		}
		
		
		/* CALCULATE ELECTIVE CREDITS LEFT  FOR EACH STUDENT */
        for (Student st : studentList){
			//Calculate the max elective credits that the student has
			st.calculateMaxElectiveCredits();
			st.electiveCreditsLeft = st.getMaxElectiveCreditsInSem();
        }

       
        
        
		/* INPUT SANITIZATION */
        wizard3.printProgressNotification("Sanitizing Input......");
        errorMsg = InputSanitization.sanitize(studentList, courseList,deptSpecificMaxCreditLimitInfo);
        if (errorMsg==null){ //If there were no errors
			DataOutput.printInputDataErrorLog("No errors",outputFolder + "/" + Constants.inputDataErrorLogFileName);
		}
		else{ //Else if there was an error, log it
			wizard3.printMessage("Input Data has errors. Error log printed to the file 'InputDataErrorLog'");
			DataOutput.printInputDataErrorLog(errorMsg,outputFolder + "/" + Constants.inputDataErrorLogFileName);
		}
		
		
  
		/* FREEZE INPUT LISTS (MAKE UNMODIFIABLE)
		//The purpose of doing this is so that the algorithms do not modify the input lists by mistake.
		MakeInputListsUnmodifiable.makePreferenceListsUnmodifiable(studentList, courseList);
		MakeInputListsUnmodifiable.makeCourseSlotsUnmodifiable(courseList);
		//Make the 3 main lists : studentList,courseList and slots unmodifiable
		studentList = (ArrayList<Student>) Collections.unmodifiableList(studentList);
		courseList = (ArrayList<Course>) Collections.unmodifiableList(courseList);
		slots = (ArrayList<Slot>) Collections.unmodifiableList(slots);
		//Do not shift the previous 3 lines of code to a function. Java uses pass by value, and these statments in a function will have no effect in the main function
		*/
		
        /* RUN THE MAIN ALGORITHM */
        if(algorithm==1){
        	wizard3.printProgressNotification("Running the Iterative HR algorithm......");
            IterativeHRalgorithm.runAlgorithm(studentList,courseList);
        }
        else if(algorithm==2){
        	wizard3.printProgressNotification("Running the First Preference Allotment algorithm......");
            FirstPreferenceAllotmentAlgorithm.runAlgorithm(studentList,courseList);
        }
        else if(algorithm==3){
        	wizard3.printProgressNotification("Running the Slotwise HR algorithm (with Heuristic 1)......");
            ArrayList<Slot> slotOrderingUsed = SlotBasedHRalgorithm.runAlgorithm(studentList,courseList,slots,1);
            DataOutput.printSlotOrderingUsed(slotOrderingUsed,outputFolder + "/" + Constants.slotOrderingOutputFileName);
        }
        else if(algorithm==4){
        	wizard3.printProgressNotification("Running the Slotwise HR algorithm (with Heuristic 2)......");
            ArrayList<Slot> slotOrderingUsed = SlotBasedHRalgorithm.runAlgorithm(studentList,courseList,slots,2);
            DataOutput.printSlotOrderingUsed(slotOrderingUsed,outputFolder + "/" + Constants.slotOrderingOutputFileName);
        }
        else{
        	wizard3.printProgressNotification("ERROR : Input for Algorithm number was incorrect. Exiting");
            System.exit(1);
        }

        
        /* OUTPUT SANITIZATION */
        wizard3.printProgressNotification("Sanitizing Output ....");
        errorMsg = OutputSanitization.sanitize(studentList, courseList);
        if (errorMsg!=null){
        	wizard3.printMessage("Output Sanitization : Exiting. " + errorMsg);
			System.exit(1);
		}
        
        
        /* COMPUTE STATISTICS */
        GetStatistics.computePerStudentStatistics(studentList);
        
        /* COMPUTE THE REASONS FOR ALL THE POSSIBLE STUDENT-COURSE ALLOTMENTS THAT WERE NOT MADE*/
        ReasonsForNotAllottingPreferences.computeReasonsonsForNotAllottingPreferences(originalStudentList);
        
        /* COMPUTE THE LIST OF EXCHANGE UNSTABLE PAIRS */
        String exchangeUnstablePairs = ExchangeUnstablePairs.computeExchangeUnstablePairs(studentList);
       
        /* PRINT OUTPUT */
        wizard3.printProgressNotification("Printing Output ....");
        //Write the output pairs of student-course
        DataOutput.printOutputToFile(studentList,outputFolder + "/output.csv");
        //Write the per student statistics
        DataOutput.printPerStudentStatistics(studentList,outputFolder + "/perStudentStatistics.csv");
        //Write the aggregate statistics
        DataOutput.printAggregateStatistics(studentList,outputFolder + "/aggregateStatistics.csv");
        //Write the number of rejections statistic
        DataOutput.printRejections(courseList,outputFolder + "/rejections.csv");
        //Write the list of student preferences that have capacity 0. Not compulsory. Just so that if we feel like the course could be taken by SEAT students, we can do something about it.*/
        DataOutput.printPreferencesWithZeroCapacity(studentList,outputFolder + "/preferencesWithZeroCapacity.csv");
        //Write the set of courses allotted for each student
        DataOutput.printPerStudentAllottedCourses(studentList,outputFolder + "/perStudentAllottedCourses.csv");
        //Write the set of students allotted to each course
        DataOutput.printPerCourseAllotedStudents(studentList,courseList,outputFolder + "/perCourseAllotedStudents.csv");
        //Write the reason for every student-course pair that was not allotted
        DataOutput.printReasonsonsForNotAllottingPreferences(studentList,outputFolder + "/reasonsForNotAllottingPreferences.csv");
      //Write the reason for every student-course pair that was not allotted
        DataOutput.createFolderForStudentEmails(studentList,outputFolder+"/studentEmails");
		//Write the reason for every student-course pair that was not allotted // ADDED by SARANYA on 03-Jan-2018
		//DataOutput.createFolderForInstructorEmails(studentList,outputFolder+"/courseInstructorEmails"); // ADDED by SARANYA on 03-Jan-2018
        //Write out the set of exchange unstable pairs for this allotment
        DataOutput.printExchangeUnstablePairs(exchangeUnstablePairs,outputFolder + "/exchangeUnstablePairs.csv");
        //Write the mandated elective statistics for this allotment
        DataOutput.printMandatedCourseStatistics(studentList,batchSpecificMandatedElectivesInfo,outputFolder+"/mandatedElectiveStatistics.csv");
        //Write the batchwise allotment statistics for this allotment
        DataOutput.printBatchWiseAllotmentStatistics(studentList,listOfBatches,outputFolder+"/batchwiseAllotmentStatistics.csv");
	}
	
}
