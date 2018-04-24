package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import models.Batch;
import models.BatchSpecificMandatedElectives;
import models.Course;
import models.CoursePreference;
import models.Slot;
import models.Student;
import models.StudentPreference;

/**
 * This file manages all data output to files.
 */
public class DataOutput{
	
	/* This function takes in the courseList and prints the course preference lists */
	public static void printCoursePreferenceListToFile(ArrayList<Course> courseList,String outputFile) throws IOException{
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Write the header line
		bw.write("Course number,Preferences \n");
		
		//Loop through the courses
		for (Course c : courseList){
			bw.write(c.getcourseNumber());
			//Write this course preference list to the file
        	for (CoursePreference cp : c.coursePreferenceList){
        		bw.write("," + cp.getStudentObj().getRollNo());
        	}
        	bw.write("\n");
		}
    	bw.close();
	}
	
	/**
	 * Prints the allotment to the output file. Each row contains a student roll number, 
	 * an allotted course number and the preference number of the student that the course was at
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void printOutputToFile(ArrayList<Student> studentList,String outputFile) throws IOException{
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Header line for file
		bw.write("Student Roll Number, Course ID \n");
		
		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
				bw.write(s.getRollNo());
                bw.write(",");
                bw.write(sp.getCourseObj().getcourseNumberToPrint());
				bw.write("\n");
			}
		}
		bw.close();
	}
	
	/**
	 * Prints the number of students rejected by a course after it got full. This is a heuristic to measure
	 * the popularity of a course
	 * @param courseList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void printRejections(ArrayList<Course> courseList,String outputFile) throws IOException{
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Writing the header line
		bw.write("Course ID");
		bw.write(",");
		bw.write("Number of Rejections");
		bw.write(",");
		bw.write("Course Capacity");
		bw.write(",");
		bw.write("Number of Rejections / Capacity");
		bw.write("\n");
		
		//Filling up the table
		for (int i =0;i<courseList.size();i++){
			bw.write(courseList.get(i).getcourseNumber()); //Using 'courseNumber' instead of 'courseNumberToPrint' because it gives us a split up of rejections in the inside and outside department versions of the course
			bw.write(",");
			bw.write(Integer.toString(courseList.get(i).noOfRejections));
			bw.write(",");
			bw.write(Integer.toString(courseList.get(i).getCapacity()));
			bw.write(",");
			//Calculate the rejections ratios
			double noOfRejectionsRatio;
			if (courseList.get(i).getCapacity()==0){
				noOfRejectionsRatio = 0;
			}
			else{
				noOfRejectionsRatio = (double)(courseList.get(i).noOfRejections) / (double)(courseList.get(i).getCapacity());
				//Round off the ratio
				noOfRejectionsRatio = roundOff(noOfRejectionsRatio);
			}
			//Writing the rejections ratios
			bw.write(Double.toString(noOfRejectionsRatio));
			bw.write("\n");
		}
		bw.close();
	}
	

	/* This functions takes in the studentList and prints out those student preferences which 
	 * have zero capacity. The purpose is so that we can investigate whether the course should
	 * truly have a zero capacity, and to figure out why the student gave a preference for it.
	 */
	public static void printPreferencesWithZeroCapacity(ArrayList<Student> studentList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Header line for file	
		bw.write("Student Roll Number, Preference with zero capacity\n");
		
		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			for (StudentPreference sp : s.studentPreferenceList){
				if (sp.getCourseObj().getCapacity() == 0){
					bw.write(s.getRollNo());
			        bw.write(",");
			        bw.write(sp.getCourseObj().getcourseNumber());
			        bw.write("\n");
				}
			}
		}
		bw.close();
		
	}

	/**
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 * Outputs the the 'Effective Average Rank' and 'Credit Satisfaction Ratio' for each student
	 */
	public static void printPerStudentStatistics(ArrayList<Student> studentList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		//Header line for file
		bw.write("Student Roll No,EffectiveAverageRank,creditSatisfactionRatio\n");

		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			bw.write(s.getRollNo());
			bw.write(",");
			bw.write(Double.toString(roundOff(s.effectiveAverageRank)));
			bw.write(",");
			bw.write(Double.toString(roundOff(s.creditSatisfactionRatio)));
			bw.write("\n");
		}
		bw.close();
		
	}

	/**
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 * Outputs aggregate statistics like mean,variance,10 percentile of the 'Effective Average Rank' and 'Credit Satisfaction Ratio' for each student
	 */
	public static void printAggregateStatistics(ArrayList<Student> studentList,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		ArrayList<Double> effectiveAvgRanks = new ArrayList<Double>();
		ArrayList<Double> creditSatisfactionRatios = new ArrayList<Double>();
		for (Student s : studentList){
			effectiveAvgRanks.add(s.effectiveAverageRank);
			creditSatisfactionRatios.add(s.creditSatisfactionRatio);
		}
		
		bw.write("EFFECTIVE RANK STATISTICS");
		bw.write("\n Mean = ");
		double effectiveRankMean = GetStatistics.getMean(effectiveAvgRanks);
		bw.write(Double.toString(roundOff(effectiveRankMean)));
		bw.write("\n Standard Deviation = ");
		double effectiveRankSd = GetStatistics.getStandardDeviation(effectiveAvgRanks);
		bw.write(Double.toString(roundOff(effectiveRankSd)));
		bw.write("\n Lowest 10Percentile = ");
		double effectiveRank10Percentile = GetStatistics.getHighest10Percentile(effectiveAvgRanks);
		bw.write(Double.toString(roundOff(effectiveRank10Percentile)));
		
		bw.write("\n\nCREDIT SATISFACTION RATIO STATISTICS");
		bw.write("\n Mean = ");
		double creditSatisfactionMean = GetStatistics.getMean(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfactionMean)));
		bw.write("\n Standard Deviation = ");
		double creditSatisfactionSd = GetStatistics.getStandardDeviation(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfactionSd)));
		bw.write("\n Lowest 10Percentile = ");
		double creditSatisfaction10Percentile = GetStatistics.getLowest10Percentile(creditSatisfactionRatios);
		bw.write(Double.toString(roundOff(creditSatisfaction10Percentile)));
		
		bw.close();
		
	}

	/* Function is used to print the slot ordering used for the SlotBasedAlgorithm. It is not
	 * used if we use any of the other algorithms
	 */
	public static void printSlotOrderingUsed(ArrayList<Slot> slotOrderingUsed, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (Slot s : slotOrderingUsed){
			bw.write(s.getSlotName() + ",");
		}
		bw.close();
	}

	/* This function prints the errors found in the input data to a file. It is up to the user 
	 * to decide if the pruned errors are acceptable or not. If not, he should manually make the 
	 * required changes to the input
	 */
	public static void printInputDataErrorLog(String errorData,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(errorData);
		bw.close();
	}

	/**
	 * This students prints for every student, the set of courses allotted to that student.
	 * @param studentList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void printPerStudentAllottedCourses(ArrayList<Student> studentList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		//Header line for file
		bw.write("Student Roll Number, Electives Allotted \n");
			
		//Looping through the students and writing each one's allotted courses
		for (Student s : studentList){
			bw.write(s.getRollNo());
			for (StudentPreference sp : s.orderedListOfcoursesAllotted){
		        bw.write(",");
		        bw.write(sp.getCourseObj().getcourseNumber());
			}
			bw.write("\n");
		}
		bw.close();
	}

	/**
	 * This function prints for every course, the set of students allotted to that course
	 * @param studentList
	 * @param courseList
	 * @param outputFile
	 * @throws IOException
	 */
	public static void printPerCourseAllotedStudents(ArrayList<Student> studentList, ArrayList<Course> courseList,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		//Header line for file
		bw.write("Course ID, Total Capacity, Allotted Capacity, Students Allotted\n");
		//Loop through the courses and write the unfulfilled capacities of each course
		for (Course c : courseList){
			//Write course id
			bw.write(c.getcourseNumber());
			ArrayList<Student> studentsAllottedForThisCourse = new ArrayList<Student>();
			//Loop through the student list to find out which students were allotted to this course
			for (Student s : studentList){
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){
					if (sp.getCourseObj() == c){
						studentsAllottedForThisCourse.add(s);
						break;
					}
				}
			}
			//Write the capacity of the course and the number of students allotted
			bw.write(",");
			bw.write(Integer.toString(c.getCapacity()));
			bw.write(",");
			bw.write(Integer.toString(studentsAllottedForThisCourse.size()));
			//Write the roll numbers of all the students allotted to this course
			for (Student s : studentsAllottedForThisCourse){
				bw.write(",");
				bw.write(s.getRollNo());
			}
			bw.write("\n");
		}
		bw.close();
		
	}
	
	
	public static void printReasonsonsForNotAllottingPreferences(ArrayList<Student> studentList, String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		//Header line for file
		bw.write("Student Roll No,Course Number,Reason for Not allotting Preference\n");

		//Looping through the students and writing all preferences that were not allotted
		for (Student s : studentList){
			//first write invalid preferences
			for (StudentPreference sp : s.invalidPreferences){
				bw.write(s.getRollNo());
				bw.write(",");
				bw.write(sp.getCourseObj().getcourseNumber());
				bw.write(",");
				bw.write(sp.detailedReasonForNotAllottingThisPreference);
				bw.write("\n");
			}
			
			//next, for each valid preference compute the reason why it is not allotted, in case it was not allotted
			for (StudentPreference sp : s.studentPreferenceList){
				if (!s.orderedListOfcoursesAllotted.contains(sp)){
					bw.write(s.getRollNo());
					bw.write(",");
					bw.write(sp.getCourseObj().getcourseNumber());
					bw.write(",");
					bw.write(sp.detailedReasonForNotAllottingThisPreference);
					bw.write("\n");
				}
			}
		}
		bw.close();	
	}
	
	//Helper Function to round off to 2 decimal places
	public static double roundOff(double noOfRejectionsRatio){
		return (double)Math.round(noOfRejectionsRatio * 100) / 100;
	}

	public static void printExchangeUnstablePairs(String exchangeUnstablePairs,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if (!outfile.exists()) {
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
					
		//Header line for file
		bw.write("Student1 Roll No,Student1 Allotted Course,Student2 Roll No,Student2 Allotted Course,\n");
		bw.write(exchangeUnstablePairs);
		bw.close();	
	}

	public static void printBatchWiseAllotmentStatistics(ArrayList<Student> studentList, ArrayList<Batch> listOfbatches,String outputFile) throws IOException {
		//Open output file for writing
		File outfile = new File(outputFile);
		if(!outfile.exists()){
			outfile.createNewFile();
		}
		FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		//Header line for file
		bw.write("Batch,Total Strength,Number of students with non empty preference list,Number of credits allotted,Ratio,\n");
		for(Batch b : listOfbatches){
			String batch = b.getBatch();

			int numOfStudentsOfThisBatchTotal = 0;
			int numOfStudentsOfThisBatchWithNonEmptyPreferenceList = 0;
			int totalCreditsAllottedToThisBatch = 0;
			// Now for this batch we need to know the number of electives for all students.
			for(Student s : studentList){
				String rollNo = s.getRollNo();
				String thisStudentBatch = rollNo.substring(0,4);

				if(thisStudentBatch.equals(batch)){
					// Increment the number of students of this batch by 1
					numOfStudentsOfThisBatchTotal = numOfStudentsOfThisBatchTotal + 1;

					if(s.studentPreferenceList.size()!=0){
						// The student gave a non-empty preference list.
						numOfStudentsOfThisBatchWithNonEmptyPreferenceList = numOfStudentsOfThisBatchWithNonEmptyPreferenceList + 1;

						for(StudentPreference sp : s.orderedListOfcoursesAllotted){
							// For all the allotted courses add the number of credits
							totalCreditsAllottedToThisBatch = totalCreditsAllottedToThisBatch + sp.getCourseObj().getCredits();
						}	
					}
					
				}
			}

			float creditsPerStudent = (float)0.0;
			if(numOfStudentsOfThisBatchWithNonEmptyPreferenceList!=0){
				creditsPerStudent = (float)totalCreditsAllottedToThisBatch/(float)numOfStudentsOfThisBatchWithNonEmptyPreferenceList;
			}
			
			bw.write(batch);
			bw.write(",");
			bw.write(String.valueOf(numOfStudentsOfThisBatchTotal));
			bw.write(",");
			bw.write(String.valueOf(numOfStudentsOfThisBatchWithNonEmptyPreferenceList));
			bw.write(",");
			bw.write(String.valueOf(totalCreditsAllottedToThisBatch));
			bw.write(",");
			bw.write(String.valueOf(creditsPerStudent));
			bw.write("\n");
		}
		bw.close();
	}

	public static void printMandatedCourseStatistics(ArrayList<Student> studentList, ArrayList<BatchSpecificMandatedElectives> batchSpecificMandatedElectiveList, String outputFile){
		//Open output file for writing
		try{
			File outfile = new File(outputFile);
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			//Header line for file
			bw.write("Batch,Elective Type,Total Strength,Number of students who didn't give a preference for this elective type,Total Allotted\n");

			for(BatchSpecificMandatedElectives m : batchSpecificMandatedElectiveList){
				String batch = m.getBatch();
				String electiveType = m.getElectiveType();

				int numOfStudentsOfThisBatchTotal = 0;
				int numOfStudentsOfThisBatchAllottedThisElectiveType = 0;
				int numOfStudentsOfThisBatchNotGivenPreferencesForThisElectiveType = 0;

				for(Student s : studentList){
					String rollNo = s.getRollNo();
					String thisStudentBatch = rollNo.substring(0,4);

					if(thisStudentBatch.equals(batch)){
						// Increment the number of students of this batch by 1
						numOfStudentsOfThisBatchTotal = numOfStudentsOfThisBatchTotal + 1;

						for(StudentPreference sp : s.orderedListOfcoursesAllotted){
							// Get the course number
							String thisCourseNumber = sp.getCourseObj().getcourseNumber();
							String thisElectiveType = thisCourseNumber.substring(0,2);

							if(thisElectiveType.equals(electiveType)){
								// Increment the number of students of this batch who have got this elective type allotted by 1
								numOfStudentsOfThisBatchAllottedThisElectiveType = numOfStudentsOfThisBatchAllottedThisElectiveType + 1;
								break; // Important to break from the loop because otherwise it might count multiple times.
							}
						}


						boolean didStudentGiveAPreferenceForThisElectiveType = false;
						for(StudentPreference sp : s.studentPreferenceList){ //Note that we are not looking at the invalidPreferences here because we do not count those as valid preferences
							// Get the course number
							String thisCourseNumber = sp.getCourseObj().getcourseNumber();
							String thisElectiveType = thisCourseNumber.substring(0,2);

							if(thisElectiveType.equals(electiveType)){
								// Reached here implies that there exists a course in the student preference which is of this elective type.
								didStudentGiveAPreferenceForThisElectiveType = true; // mark the flag as true
								break;
							}
						}

						// check for the flag.
						if(didStudentGiveAPreferenceForThisElectiveType==false){
							// Reached here implies that this student has not given any preference for this elective type.
							numOfStudentsOfThisBatchNotGivenPreferencesForThisElectiveType = numOfStudentsOfThisBatchNotGivenPreferencesForThisElectiveType + 1;
						}
					}
				}
				bw.write(batch);
				bw.write(",");
				bw.write(electiveType);
				bw.write(",");
				bw.write(String.valueOf(numOfStudentsOfThisBatchTotal));
				bw.write(",");
				bw.write(String.valueOf(numOfStudentsOfThisBatchNotGivenPreferencesForThisElectiveType));
				bw.write(",");
				bw.write(String.valueOf(numOfStudentsOfThisBatchAllottedThisElectiveType));
				bw.write("\n");
			}
			bw.close();

		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}


	public static void createFolderForStudentEmails(ArrayList<Student> studentList, String outputFolder) throws IOException {
		new File(outputFolder).mkdir();

		//Looping through the students and writing all preferences that were not allotted
		for (Student s : studentList){
			//Open output file for writing
			File outfile = new File(outputFolder + "/" + s.getRollNo());
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
						
			bw.write("Dear Student,\n");
			
			//If the student has not registered
			if (s.studentPreferenceList.size()==0 && s.invalidPreferences.size()==0 && s.coreCourses.size()==0){
				bw.write("From the data obtained from workflow we see that you have not registered for courses during the registration week. Owing to this, no courses have been allotted to you via SEAT.\n");
				bw.write("\nPlease write to seat@wmail.iitm.ac.in if you have any issues with the allotment. No paper requests will be entertained.\n\n");
				bw.write("Best Regards,\n");
				bw.write("SEAT Team\n");
				bw.close();
				continue;
			}
			
			//If the student has registered
			bw.write("The Round-1 SEAT allocations for the Jan--May 2018 semester are done. ");
			//Write set of allotted Courses
			if (s.orderedListOfcoursesAllotted.size()==0){
				bw.write("You have not been allotted any elective course in this round of allotment. (This does not include core courses)\n");
			}
			else{
				bw.write("Please find below the elective courses allotted to you. This does not include the core courses.\n\nList of courses allotted:\n");
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){
					bw.write(sp.getCourseObj().getcourseNumberToPrint());
					bw.write("\n");
				}
			}
			bw.write("\n");
			
			//Write set of courses not allotted, along with reasons
			//If there are no courses in this section
			if (s.invalidPreferences.size()==0 && (s.studentPreferenceList.size()==s.orderedListOfcoursesAllotted.size())){
				bw.write("There is no elective on your preference list that has not been allotted\n");
			}
			else{
				bw.write("For your ready reference please find below the list of courses that were not allotted to you and the reason for SEAT not being able to allot that course to you.\n\n");
				bw.write("List of courses not allotted:\n");
				//Write set of Invalid preferences
				for (StudentPreference sp : s.invalidPreferences){
					//Only use outside department version of the course. This is because we do not want to give 2 reasons - 1 for the inside version, and 1 for the outside. Anyways, the inside and outside department versions have the same reason, unless 1 of them is allotted
					if (sp.getCourseObj().isAnInsideDeptCourse()==false){
						//Student could not get allotted to the inside department version of the course because it was an invalid preference
						bw.write(sp.getCourseObj().getcourseNumberToPrint());
						bw.write(" : ");
						bw.write(sp.reasonForNotAllottingThisPreference);
						bw.write("\n");
					}
				}
				
				//next, for each valid preference compute the reason why it is not allotted, in case it was not allotted
				for (StudentPreference sp : s.studentPreferenceList){
					if (!s.orderedListOfcoursesAllotted.contains(sp)){
						//Only use outside department version of the course. This is because we do not want to give 2 reasons - 1 for the inside version, and 1 for the outside. Anyways, the inside and outside department versions have the same reason, unless 1 of them is allotted
						if (sp.getCourseObj().isAnInsideDeptCourse()==false){
							//Check if the student got allotted to the inside department version of the course 
							boolean allottedToInsideDeptVersionOfCourse=false;
							for (StudentPreference sp2 : s.orderedListOfcoursesAllotted){
								if (sp2.getCourseObj().getCorrespondingOutsideDepartmentCourse()==sp.getCourseObj()){
									allottedToInsideDeptVersionOfCourse=true;
								}
							}
							if (allottedToInsideDeptVersionOfCourse==false){
								//If the student did not get allotted to the inside department version of the course
								bw.write(sp.getCourseObj().getcourseNumberToPrint());
								bw.write(" : ");
								bw.write(sp.reasonForNotAllottingThisPreference);
								bw.write("\n");
							}
							
						}
					}
				}
			}

			bw.write("\nPlease write to seat@wmail.iitm.ac.in if you have any issues with the allotment. No paper requests will be entertained.\n\n");
			bw.write("Best Regards,\n");
			bw.write("SEAT Team\n");
			bw.close();
		}
	}

	// This allotment is ADDED by SARANYA on 03-Jan-2018
	/*public static void createFolderForInstructorEmails(ArrayList<Student> studentList, String outputFolder) throws IOException {
		new File(outputFolder).mkdir();

		//Looping through the students and writing all preferences that were not allotted
		for (Student s : studentList){
			//Open output file for writing
			File outfile = new File(outputFolder + "/" + s.getRollNo());
			if (!outfile.exists()) {
				outfile.createNewFile();
			}
			FileWriter fw = new FileWriter(outfile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("Dear Student,\n");

			//If the student has not registered
			if (s.studentPreferenceList.size()==0 && s.invalidPreferences.size()==0 && s.coreCourses.size()==0){
				bw.write("From the data obtained from workflow we see that you have not registered for courses during the registration week. Owing to this, no courses have been allotted to you via SEAT.\n");
				bw.write("\nPlease write to seat@wmail.iitm.ac.in if you have any issues with the allotment. No paper requests will be entertained.\n\n");
				bw.write("Best Regards,\n");
				bw.write("SEAT Team\n");
				bw.close();
				continue;
			}

			//If the student has registered
			bw.write("The Round-1 SEAT allocations for the Jan--May 2018 semester are done. ");
			//Write set of allotted Courses
			if (s.orderedListOfcoursesAllotted.size()==0){
				bw.write("You have not been allotted any elective course in this round of allotment. (This does not include core courses)\n");
			}
			else{
				bw.write("Please find below the elective courses allotted to you. This does not include the core courses.\n\nList of courses allotted:\n");
				for (StudentPreference sp : s.orderedListOfcoursesAllotted){
					bw.write(sp.getCourseObj().getcourseNumberToPrint());
					bw.write("\n");
				}
			}
			bw.write("\n");

			//Write set of courses not allotted, along with reasons
			//If there are no courses in this section
			if (s.invalidPreferences.size()==0 && (s.studentPreferenceList.size()==s.orderedListOfcoursesAllotted.size())){
				bw.write("There is no elective on your preference list that has not been allotted\n");
			}
			else{
				bw.write("For your ready reference please find below the list of courses that were not allotted to you and the reason for SEAT not being able to allot that course to you.\n\n");
				bw.write("List of courses not allotted:\n");
				//Write set of Invalid preferences
				for (StudentPreference sp : s.invalidPreferences){
					//Only use outside department version of the course. This is because we do not want to give 2 reasons - 1 for the inside version, and 1 for the outside. Anyways, the inside and outside department versions have the same reason, unless 1 of them is allotted
					if (sp.getCourseObj().isAnInsideDeptCourse()==false){
						//Student could not get allotted to the inside department version of the course because it was an invalid preference
						bw.write(sp.getCourseObj().getcourseNumberToPrint());
						bw.write(" : ");
						bw.write(sp.reasonForNotAllottingThisPreference);
						bw.write("\n");
					}
				}

				//next, for each valid preference compute the reason why it is not allotted, in case it was not allotted
				for (StudentPreference sp : s.studentPreferenceList){
					if (!s.orderedListOfcoursesAllotted.contains(sp)){
						//Only use outside department version of the course. This is because we do not want to give 2 reasons - 1 for the inside version, and 1 for the outside. Anyways, the inside and outside department versions have the same reason, unless 1 of them is allotted
						if (sp.getCourseObj().isAnInsideDeptCourse()==false){
							//Check if the student got allotted to the inside department version of the course
							boolean allottedToInsideDeptVersionOfCourse=false;
							for (StudentPreference sp2 : s.orderedListOfcoursesAllotted){
								if (sp2.getCourseObj().getCorrespondingOutsideDepartmentCourse()==sp.getCourseObj()){
									allottedToInsideDeptVersionOfCourse=true;
								}
							}
							if (allottedToInsideDeptVersionOfCourse==false){
								//If the student did not get allotted to the inside department version of the course
								bw.write(sp.getCourseObj().getcourseNumberToPrint());
								bw.write(" : ");
								bw.write(sp.reasonForNotAllottingThisPreference);
								bw.write("\n");
							}

						}
					}
				}
			}

			bw.write("\nPlease write to seat@wmail.iitm.ac.in if you have any issues with the allotment. No paper requests will be entertained.\n\n");
			bw.write("Best Regards,\n");
			bw.write("SEAT Team\n");
			bw.close();
		}
	}*/
}
