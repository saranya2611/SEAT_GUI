package main;

import java.io.IOException;
import java.util.Scanner;

/**
 * This function is the main function that will be called when there is no GUI
 * This function is responsible for taking all the inputs from the user from the terminal,
 * and it also prints all the required information to the terminal.
 * <p>
 * This class is not supposed to do anything other than text input/output
 *
 * @author akshay
 */
public class MainWithoutGUI {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        boolean generateFreshCoursePreferences = true;

        /* TAKING USER INPUT AND SENDING IT TO THE EXECUTE STEPS FOR ALLOTMENT FUNCTION*/

        //Read slots file
        System.out.println("Please enter the input csv file name where the slot timings are stored");
        String slotsFile = sc.nextLine();

        //Read student list file
        System.out.println("Please enter the input csv file name where the list of students+CGPAs in the above format is stored");
        String studentListFile = sc.nextLine();

        //Read course list file
        System.out.println("Please enter the input csv file name where the list of courses+rankingCriteria+capacity in the above format is stored");
        String courseListFile = sc.nextLine();

        //Read student preference list file
        System.out.println("Please enter the input csv file name where the preference list in the above format are stored");
        String studentPreferenceListFile = sc.nextLine();

        //Take the input on whether the course preference list is to be generated or read from an existing file.
        System.out.println("Course Preference List Generation. Please enter 1 to create New Course Preference Lists, 2 to read from existing Course Preference List from a file: ");
        int inp = sc.nextInt();
        sc.nextLine(); //This is necessary because the sc.nextInt() doesn't consume the '\n' and this is read by the sc.nextLine() appearing after this. Hence this is just meant to consume the '\n' character
        String coursePreferenceListFile = null;

        //If you want to create a fresh course preference list
        if (inp == 1) {
            System.out.println("Please enter the location of file where you want to save the freshly generated course preferences: ");
            coursePreferenceListFile = sc.nextLine();
            generateFreshCoursePreferences = true;
        }
        //Otherwise if you want to read the course preference list from a file
        else if (inp == 2) {
            System.out.println("Please enter the location of file from where you want the course preferences to be read: ");
            coursePreferenceListFile = sc.nextLine();
            generateFreshCoursePreferences = false;
        }
        //else if we get an invalid input
        else {
            System.out.println("ERROR : Input was neither 1 nor 2. Exiting");
            System.exit(1);
        }

        //Reading which algorithm to use
        System.out.println("Which algorithm to run?");
        System.out.println("1. Iterative HR");
        System.out.println("2. First Preference Allotment");
        System.out.println("3. Slotwise HR (with Heuristic 1)");
        System.out.println("4. Slotwise HR (with Heuristic 2)");
        int algorithm = sc.nextInt();
        sc.nextLine(); //This is necessary because the sc.nextInt() doesn't consume the '\n' and this is read by the sc.nextLine() appearing after this. Hence this is just meant to consume the '\n' character

        //Reading the folder to which the output is to be printed
        System.out.println("Please enter output folder to print to");
        String outputFolder = sc.nextLine();

        //Read the list of courses that consider other department students as inside department. Read the User Manual for more instructions
        System.out.println("Please enter the input csv file name where the list of courses that consider other department students as inside department. If you do not require this functionality, just enter the name of a blank csv file.");
        String insideDepartmentConfigFile = sc.nextLine();

        //Read the list of courses that consider some students as high priority
        System.out.println("Please enter the input csv file name where the list of courses that consider certain students as high priority students. If you do not require this functionality, just enter the name of a blank csv file.");
        String highPriorityCoursePreferencesConfigFile = sc.nextLine();

        //Read the batches and their specified mandated electives
        System.out.println("Please enter the input csv file name where the list of batched and the corresponding mandated electives are listed. If you do not require this functionality, just enter the name of a blank csv file.");
        String batchSpecificMandatedElectivesFile = sc.nextLine();

        //Read the deparment&year wise limit on the max credits for students
        System.out.println("Please enter the input csv file name where the the deparment&year wise limit on the max credits for students");
        String departmentWiseMaxCreditLimitFile = sc.nextLine();

        //Call the executeAllotmentSteps function to run the allotment
        gui.defaultSetOfFilesWizard3 wizard3 = null;
        ExecuteStepsForAllotment.executeAllotmentSteps(slotsFile, studentListFile, courseListFile, studentPreferenceListFile, coursePreferenceListFile, generateFreshCoursePreferences, algorithm, outputFolder, insideDepartmentConfigFile, highPriorityCoursePreferencesConfigFile, departmentWiseMaxCreditLimitFile, batchSpecificMandatedElectivesFile, false, wizard3);

        //Program has ended
        System.out.println("Execution over.......");
        sc.close();
    }

    public static void displayMessage(String s) {
        System.out.println(s);
    }

    public static void printProgress(String s) {
        System.out.println(s);
    }
}
