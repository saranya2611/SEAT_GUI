package services;
/**
 * This class is going to be used to define all constants to be used in the project.
 * No integer or string should be hardcoded. Instead it should be added as a constant here and then used.
 * To be included here are
 * 1. Ranking criteria constants eg. #define stratifiedRandom 2
 * 2. Department criteria constants eg. #define Humanities 3
 */
public class Constants{
	
	//Ranking criteria constants
	public static final int random = 1;
	public static final int stratifiedRandom = 2;
	public static final int CGPA = 3;
	
	//Output file names
	public static final String inputDataErrorLogFileName = "InputDataErrorLog.txt";
	public static final String slotOrderingOutputFileName = "SlotOrderingUsed";
	
	//Inside and outside department suffixes
	public static final String insideDepartmentSuffix = "$inside";
	public static final String outsideDepartmentSuffix = "$outside";
	
	//String values for Core,Elective and Backlog courses
	public static final String coreCourse = "CORE";
	public static final String electiveCourse = "ELEC";
	public static final String backlogCourse = "BACKLOG";
	
	//Default limit on the maxCredits for a student, if the department has not provided any
	public static final int maxCreditDefaultLimit = 72;
	
	//Colour code for the 'no colour' option
	public static final int noColourOption = 0;
}