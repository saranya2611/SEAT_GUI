package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * This class reads data from the input file and ensures that it is in the correct format
 *
 * ALL FILE FORMATS ARE GIVEN IN THE README AND CODE DOCUMENTATION
 *
 */
public class CheckInputFormats {
	/** Self explanatory */
	public static String checkStudentListFileFormat(String inputFile){
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be exactly 3 columns
				if (inputLine.length!=3){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have 3 columns";
					break;
				}
				//First column is a string and can be anything. Second column is float
				String decimalPattern = "([0-9]*)\\.([0-9]*)";
				String integerPattern = "([0-9]*)";
				if ((!Pattern.matches(decimalPattern, inputLine[1])) && (!Pattern.matches(integerPattern, inputLine[1]))){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Second column should be a decimal(floating point) number like '9.15'";
					break;
				}
				//Third column is an integer
				
				if (!Pattern.matches(integerPattern, inputLine[2])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Third column should be an integer like '45'";
					break;
				}
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}
	
	/** Self explanatory */
	public static String checkCourseListFileFormat(String inputFile){
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		String error = null;
		int lineNo=1; //Start with 1 because we want to skip the header line
		
		//reading input line by line and adding a new course for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);

				//There should be atleast 5 columns
				if (inputLine.length<7){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have at least 7 columns";
					break;
				}
				//First 2 columns are strings and can be anything. Third column is an integer
				String integerPattern = "([0-9]*)";
				if (!Pattern.matches(integerPattern, inputLine[2])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Third column should be an integer.";
					break;
				}
				
				//Fourth column is an integer
				if (!Pattern.matches(integerPattern, inputLine[3])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Fourth column should be an integer";
					break;
				}
				
				//Fifth column is an integer
				if (!Pattern.matches(integerPattern, inputLine[4])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Fifth column should be an integer";
					break;
				}
				
				//Sixth column is an integer
				if (!Pattern.matches(integerPattern, inputLine[5])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Sizth column should be an integer";
					break;
				}
				
				//Seventh column onwards, all are strings so we do not care
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}
	
	/** Self explanatory */
	public static String checkStudentPreferenceListFileFormat(String inputFile){
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be exactly 5 columns
				if (inputLine.length!=5){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have 5 columns";
					break;
				}
				//First and second columns are strings and can be anything. Third column is integer or NULL
				String integerPattern = "([0-9]*)";
				if (!(Pattern.matches(integerPattern, inputLine[2]) || inputLine[2].equalsIgnoreCase("NULL"))){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Third column should be an integer or null";
					break;
				}
				
				//Fourth column is a string. Fifth column is integer or NULL
				if (!(Pattern.matches(integerPattern, inputLine[4]) || inputLine[4].equalsIgnoreCase("NULL"))){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Fifth column should be an integer or null";
					break;
				}
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}
	
	/** Self explanatory */
	public static String checkCoursePreferenceListFileFormat(String inputFile){
		return null; //No file format. There can be arbitrary number of columns
	}
	
	/** Self explanatory */
	public static String checkSlotsFileFormat(String inputFile){
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			outerLoop: //label used for breaking this loop
			while ((line = br.readLine()) != null) {
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be exactly 3 columns
				if (inputLine.length<2){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have atleast 2 columns";
					break;
				}
				//Looping through all the columns and checking if the format is correct
				for(int i=1;i<inputLine.length;i++){
			        //Each column (apart from the first) should have a "="
					if (!inputLine[i].contains("=")){
			        	error = "Error in file : " + inputFile + " at line number " + lineNo + ". Column " + i + " should have a '=' symbol";
						break outerLoop;
			        }
					
					//Get the timing part of the class timing
					String [] timing = inputLine[i].split("="); //Split line on '='
			        
			        //The 'timing' string should have a "-" symbol
					if (!timing[1].contains("-")){
			        	error = "Error in file : " + inputFile + " at line number " + lineNo + ". Column " + i + " should have a '-' symbol";
			        	break outerLoop;
			        }
					
					//Get the start time and end time
			        String[] duration = timing[1].split("-"); //Remaining part is the duration and is split again at '-'
			        String startTime = duration[0]; //The first part corresponds to the start time of the slot
			        String endTime = duration[1]; //The second part corresponds to the end time of the slot
			        
			        //Ensure that the startTime and endTime are valid timings
					String timePattern = "([0-9][0-9]?)\\:([0-9][0-9])";
					if (!Pattern.matches(timePattern, startTime) || !Pattern.matches(timePattern, endTime)){
			        	error = "Error in file : " + inputFile + " at line number " + lineNo + ". Column " + i + " does not have a valid timing.";
			        	break outerLoop;
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
		return error;
	}

	public static String checkDepartmentWiseMaxCreditLimitFileFormat(String inputFile) {
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be exactly 3 columns
				if (inputLine.length!=2){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have 3 columns";
					break;
				}
				//First column is a string and can be anything. Second column is an integer
				String integerPattern = "([0-9]*)";
				if (!Pattern.matches(integerPattern, inputLine[1])){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". Third column should be an integer like '45'";
					break;
				}
			}
			br.close(); //closing file pointer
			//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}

	public static String checkHighPriorityCoursePreferencesConfigFileFormat(String inputFile) {
		return checkForAtleast2columnsInFile(inputFile);
	}
	

	public static String checkInsideDepartmentConfigFileFormat(String inputFile) {
		return checkForAtleast2columnsInFile(inputFile);
	}
	
	public static String checkBatchSpecificMadatedElectivesFileFormat(String inputFile){
		return checkForAtleast2columnsInFile (inputFile);
	}
	
	/** A function that opens the file, checks that it has at least 2 columns
	 * and then shuts it
	 * @param inputFile
	 * @return Errors found in the file
	 */
	private static String checkForAtleast2columnsInFile(String inputFile) {
		//First check if the file exists
		File inFile = new File(inputFile);
		if (!inFile.exists()) {
			return "The following Input file name given does not exist : " + inputFile + "\n";
		}
		
		//Some declarations
		String line;
		String [] inputLine;
		String splitBy = ",";
		int lineNo=1; //Start with 1 because we want to skip the header line
		String error = null;

		//reading input line by line and adding a new student for every line.
		try {
			//open input file and start reading
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			//Skip the first line since it will be the header row
			br.readLine();
			//read input file line by line
			while ((line = br.readLine()) != null) {
				lineNo += 1; //Increment the line number
				inputLine = line.split(splitBy);
				//There should be atleast 2 columns
				if (inputLine.length<2){
					error = "Error in file : " + inputFile + " at line number " + lineNo + ". File should have atleast 2 columns";
					break;
				}
			}
			br.close(); //closing file pointer
		//just some exception handling.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}
}
