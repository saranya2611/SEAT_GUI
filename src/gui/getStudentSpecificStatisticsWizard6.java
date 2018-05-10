package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import demo.LegendTitleToImageDemo1;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import services.CheckInputFormats;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static gui.formValues.studentPreferenceListNameWithPath;


public class getStudentSpecificStatisticsWizard6 extends JFrame {
    public String perStudentAllottedCoursesFileName, perStudentStatisticsFileName, rejectionReasoningsFileName;
    String line, chosenDirectoryName, studentPreferenceListName, errorMsg, enteredRollNumber, matchedString, matchedString1;
    int courseCapacityFullFlag;
    String[] inputLine;
    String splitByComma = ",";
    String splitByNewLine = "\n";
    JButton checkButton;
    String rollNumberPattern = "^[A-Za-z][A-Za-z][0-9][0-9][Bb][0-9][0-9][0-9]";
    ArrayList<String> arrayOfMatchingsFromStudentPreferences, arrayOfMatchingsFromAllottedCourses, arrayOfMatchingsFromAllottementStatistics, arrayOfMatchingsFromRejectionReasons, arrayOfMatchings, coreCourses;
    private JLabel jLabelStudentwiseAllotmentAnalysisWizard6;
    private JLabel chosenDirectoryLabelWizard6;
    private JTextField chosenDirectoryTextFieldWizard6;
    private JButton chosenDirectoryCheckButtonWizard6;
    private JButton chosenDirectoryBrowseButtonWizard6;
    private JLabel studentRollNumberLabelWizard6;
    private JTextField studentRollNumberTextFieldWizard6;
    private JButton studentRollNumberCheckButtonWizard6;
    private JButton studentRollNumberGetDetailsButtonWizard6;
    private JLabel titleFieldAnalysisWizard6;
    private JTextArea studentStatsTextAreaWizard6;
    private JScrollPane JScrollPaneStudentDetailsWizard6;
    private JPanel jPanelStudentwiseAllotmentWizard6;
    private JLabel studentPreferenceListLabelWizard6;
    private JTextField studentPreferenceTextFieldWizard6;
    private JButton studentPreferenceCheckButtonWizard6;
    private JButton studentPreferenceBrowseButtonWizard6;
    private JButton goToWizard5FromWizard6;
    private PieChart coreElectivePiePlotDemo;
    private PieChart electiveAcceptRejectPiePlotDemo;
    private int totalNumberOfRejectedCourses;

    // Constructor function
    public getStudentSpecificStatisticsWizard6() {
        super();
        $$$setupUI$$$();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
        setSize(1200, 800);

        JScrollPane scrPaneWizard6 = new JScrollPane(jPanelStudentwiseAllotmentWizard6);
        add(scrPaneWizard6);

        jPanelStudentwiseAllotmentWizard6.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        // Decide which components to be visible and which components are need not to be visible
        chosenDirectoryBrowseButtonWizard6.setEnabled(true);
        chosenDirectoryTextFieldWizard6.setEnabled(false);
        chosenDirectoryTextFieldWizard6.setVisible(true);
        chosenDirectoryCheckButtonWizard6.setVisible(false);
        studentPreferenceCheckButtonWizard6.setVisible(false);
        studentPreferenceTextFieldWizard6.setEnabled(true);
        studentPreferenceTextFieldWizard6.setEditable(false);
        studentPreferenceBrowseButtonWizard6.setEnabled(true);
        studentRollNumberCheckButtonWizard6.setVisible(false);
        studentRollNumberTextFieldWizard6.setEnabled(true);
        studentRollNumberTextFieldWizard6.setEditable(true);


        // Row 1: Selected directory from previous window
        chosenDirectoryTextFieldWizard6.setText(formValues.getAnalyseAllotmentDirPath());
        chosenDirectoryName = chosenDirectoryTextFieldWizard6.getText();
        formValues.setAnalyseAllotmentDirPath(chosenDirectoryName);
        File directoryName = new File(chosenDirectoryName);
        errorMsg = "Directory not found....!!!";
        enableFileDirectoryExistenceCheckButton(chosenDirectoryCheckButtonWizard6, directoryName, errorMsg);
        chosenDirectoryBrowseButtonWizard6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenDirectoryBrowseButtonWizard6.setEnabled(true);
                chosenDirectoryBrowseButtonWizard6.setEnabled(true);
                JFileChooser jfc = new JFileChooser(formValues.getAnalyseAllotmentDirPath());
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int outputFilesDir = jfc.showOpenDialog(null);
                if (outputFilesDir == JFileChooser.APPROVE_OPTION) {
                    File outputFilesDirName = jfc.getSelectedFile();
                    chosenDirectoryBrowseButtonWizard6.setText(outputFilesDirName.getAbsolutePath());
                    chosenDirectoryName = chosenDirectoryTextFieldWizard6.getText();
                    formValues.setAnalyseAllotmentDirPath(chosenDirectoryName);
                    File directoryName = new File(chosenDirectoryName);
                    enableFileDirectoryExistenceCheckButton(chosenDirectoryCheckButtonWizard6, directoryName, errorMsg);
                }
            }
        });

        /* Load the required files. The files required to give complete details about the student in query are
         *  1) ../registrationDataWithBS17CS16ME16changes.csv -->this will be outside the generated allotment folder. This is one of the input for the executeAllotments code
         *                                                                         --> This too have multiple entries for a student and each entry has 5 columns: RollNo,CourseNo,ColorGroup,Type,SortOrder
         *  2) perStudentAllottedCourses.csv --> has the list of courses alloted to a student ; Pattern : rollNo, course1, course 2, course 3, ....
         *  3) perStudentStatistics.csv --> has the effective average rank and credit satisfaction ratio ; Pattern : RollNo, effectiveAverageRank, creditSatisfactionRatio
         *  4) reasonsForNotAllottingPreferences.csv --> has multiple entries for a student; every row has rollNo, rejectedCourse, and reason for rejecting*/

        // Row 2: Get (1) registrationDataWithBS17CS16ME16changes.csv
        studentPreferenceCheckButtonWizard6.setVisible(false);
        // Check the existence of default file
        studentPreferenceBrowseButtonWizard6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(formValues.getAnalyseAllotmentDirPath());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int studentPreferenceFileFlag = jfc.showOpenDialog(null);
                if (studentPreferenceFileFlag == JFileChooser.APPROVE_OPTION) {
                    File studentPreferenceFile = jfc.getSelectedFile();
                    studentPreferenceTextFieldWizard6.setText(studentPreferenceFile.getAbsolutePath());
                    studentPreferenceListName = studentPreferenceTextFieldWizard6.getText();
                    formValues.setStudentPreferenceListNameWithPath(studentPreferenceListName);
                    //System.out.println("Student preference list " + studentPreferenceListName);
                    studentPreferenceListName = formValues.getStudentPreferenceListNameWithPath();
                    errorMsg = CheckInputFormats.checkStudentPreferenceListFileFormat(studentPreferenceListName);
                    if (errorMsg != null) {
                        enableFileDirectoryExistenceCheckButton(studentPreferenceCheckButtonWizard6, studentPreferenceFile, errorMsg);
                        studentStatsTextAreaWizard6.append("\n Student Preference List: " + formValues.getStudentPreferenceListNameWithPath());
                        studentStatsTextAreaWizard6.append("\n============================================================== \n ");
                    } else {
                        enableFileDirectoryExistenceCheckButton(studentPreferenceCheckButtonWizard6, studentPreferenceFile, errorMsg);
                        studentStatsTextAreaWizard6.append("\n Student Preference List: " + formValues.getStudentPreferenceListNameWithPath());
                        studentStatsTextAreaWizard6.append("\n============================================================== \n ");
                    }
                }
            }
        });


        // Row -3 : Get student roll number textfield action
        studentRollNumberTextFieldWizard6.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enteredRollNumber = studentRollNumberTextFieldWizard6.getText();
                    if (enteredRollNumber.matches(rollNumberPattern)) {
                        System.out.println("\n Entered roll number is :" + enteredRollNumber);
                        studentRollNumberGetDetailsButtonWizard6.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "\n Roll number MUST be of form : [A-Z][A-Z][0-9][0-9][B][0-9][0-9][0-9] \n Eg: ME16B001, me16b001, Cs15b001, cs15b001\n ", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Row-3 Get details button click action
        studentRollNumberGetDetailsButtonWizard6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get student Roll number
                enteredRollNumber = studentRollNumberTextFieldWizard6.getText();
                System.out.println("\n Roll number is :::::: " + enteredRollNumber);
                // (1) Student preference list
                studentPreferenceListName = formValues.getStudentPreferenceListNameWithPath();

                // Get (2) perStudentAllottedCourses.csv
                formValues.setPerStudentAllottedCoursesFileName(chosenDirectoryName + '/' + "perStudentAllottedCourses.csv");
                perStudentAllottedCoursesFileName = formValues.getPerStudentAllottedCoursesFileName();

                // Get (3) perStudentsStatistics.csv
                formValues.setPerStudentAllottedStatisticsFileName(chosenDirectoryName + '/' + "perStudentStatistics.csv");
                perStudentStatisticsFileName = formValues.getPerStudentAllottedStatsFileName();

                // Get (4) reasonsForNotAllottingPreferences.csv
                formValues.setRejectionReasonsFileName(chosenDirectoryName + '/' + "reasonsForNotAllottingPreferences.csv");
                rejectionReasoningsFileName = formValues.getRejectionReasonsFileName();
                if (enteredRollNumber.matches(rollNumberPattern)) {
                    System.out.println("\n Roll number is :" + enteredRollNumber);

                    // Get the per student allotted statistics
                    try {
                        arrayOfMatchingsFromAllottementStatistics = getStudentWiseDetails(perStudentStatisticsFileName, splitByNewLine, enteredRollNumber, arrayOfMatchingsFromAllottementStatistics);
                        if (arrayOfMatchingsFromAllottementStatistics.isEmpty()) {
                            studentStatsTextAreaWizard6.append("\n \n \n \n ==================================================== \n");
                            studentStatsTextAreaWizard6.append("\n Allotment statistics of   " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n-----------------------------------------------------");
                            studentStatsTextAreaWizard6.append("\n Either this student did not register any elective courses (OR) the entered roll number does not exists.!!!!");
                            JOptionPane.showMessageDialog(null, "\n Either this student did not register any elective courses (OR) the entered roll number does not exists.!!", "Roll Number not found", JOptionPane.WARNING_MESSAGE);
                        } else {
                            studentStatsTextAreaWizard6.append("\n \n \n \n ==================================================== \n");
                            studentStatsTextAreaWizard6.append("\n Allotment statistics of   " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n-----------------------------------------------------");
                            postProcessPerStudentAllottedElectivesForDisplay(arrayOfMatchingsFromAllottementStatistics, enteredRollNumber, perStudentStatisticsFileName);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    // Get the preferences given by the student from the registration data
                    try {
                        arrayOfMatchingsFromStudentPreferences = getStudentWiseDetails(studentPreferenceListName, splitByNewLine, enteredRollNumber, arrayOfMatchingsFromStudentPreferences);
                        if (arrayOfMatchingsFromStudentPreferences.isEmpty()) {
                            studentStatsTextAreaWizard6.append("\n \n Preference List of  " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n----------------------------------------------------");
                            studentStatsTextAreaWizard6.append("\n Either this student did not register any elective courses (OR) the entered roll number does not exists.!!!!");
                            JOptionPane.showMessageDialog(null, "\n Either this student did not register any elective courses (OR) the entered roll number does not exists.!!", "Roll Number not found", JOptionPane.WARNING_MESSAGE);
                        } else {
                            studentStatsTextAreaWizard6.append("\n \n Preference List of  " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n----------------------------------------------------");
                            postProcessPreferenceDetailsForDisplay(arrayOfMatchingsFromStudentPreferences, enteredRollNumber, studentPreferenceListName);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    // Display pie chart for core and elective
                    postProcessPerStudentPreferencesForChartDisplay(arrayOfMatchingsFromStudentPreferences, enteredRollNumber);

                    // Get the per student allotted courses details
                    try {
                        arrayOfMatchingsFromAllottedCourses = getStudentWiseDetails(perStudentAllottedCoursesFileName, splitByNewLine, enteredRollNumber, arrayOfMatchingsFromAllottedCourses);
                        if (arrayOfMatchingsFromAllottedCourses.isEmpty()) {
                            studentStatsTextAreaWizard6.append("\n \n Elective preferences allotted for  " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n----------------------------------------------------------------");
                            studentStatsTextAreaWizard6.append("\n Either this student did not get allotted to any of his/her preferences (OR) the entered roll number does not exists.!!!!");
                            JOptionPane.showMessageDialog(null, "\n Either this student did not get allotted to any of his/her preferences (OR) the entered roll number does not exists.!!", "Roll Number not found", JOptionPane.WARNING_MESSAGE);
                        } else {
                            studentStatsTextAreaWizard6.append("\n \n Elective preferences allotted for  " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n----------------------------------------------------------------");
                            postProcessPerStudentAllottedElectivesForDisplay(arrayOfMatchingsFromAllottedCourses, enteredRollNumber, perStudentAllottedCoursesFileName);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    // Get the reasons for rejections
                    try {
                        arrayOfMatchingsFromRejectionReasons = getStudentWiseDetails(rejectionReasoningsFileName, splitByNewLine, enteredRollNumber, arrayOfMatchingsFromRejectionReasons);
                        if (arrayOfMatchingsFromRejectionReasons.isEmpty()) {
                            studentStatsTextAreaWizard6.append("\n \n Elective preferences rejected for  " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n----------------------------------------------------------------");
                            studentStatsTextAreaWizard6.append("\n All his/her preferred courses were allotted (OR) this student did not register any elective courses (OR) the entered roll number does not exists.!!!!");
                            JOptionPane.showMessageDialog(null, "\n All his/her preferred courses were allotted (OR) this student did not register any elective courses (OR) the entered roll number does not exists.!!", "Roll Number not found", JOptionPane.WARNING_MESSAGE);
                        } else {
                            studentStatsTextAreaWizard6.append("\n \n Elective preferences rejected for  " + enteredRollNumber.toUpperCase() + " : ");
                            studentStatsTextAreaWizard6.append("\n----------------------------------------------------------------");
                            postProcessPerStudentAllottedElectivesForDisplay(arrayOfMatchingsFromRejectionReasons, enteredRollNumber, rejectionReasoningsFileName);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    //Display pie chart for elective allocation and rejection - Hari
                    postProcessPerStudentAllottedElectivesForChartDisplay(arrayOfMatchingsFromAllottedCourses, arrayOfMatchingsFromRejectionReasons, enteredRollNumber);

                } else {
                    JOptionPane.showMessageDialog(null, "\n Roll number MUST be of form : [A-Z][A-Z][0-9][0-9][B][0-9][0-9][0-9] \n Eg: ME16B001, me16b001, Cs15b001, cs15b001\n ", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        goToWizard5FromWizard6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeAllotmentsWizard4 wizard4 = new analyzeAllotmentsWizard4();
                wizard4.setVisible(true);
                dispose();
            }
        });
    }

    // General function to check the existence of file or folder
    public void enableFileDirectoryExistenceCheckButton(JButton currentCheckButton, File file1, String errorMsg) {
        checkButton = currentCheckButton;
        if (file1.exists()) {
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("greenYes.png"));
                checkButton.setIcon(new ImageIcon(img));
                chosenDirectoryCheckButtonWizard6.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("redCross.png"));
                checkButton.setIcon(new ImageIcon(img));
                chosenDirectoryCheckButtonWizard6.setEnabled(true);
                JOptionPane.showMessageDialog(null, errorMsg, "Input Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // General function to get student-wise details
    public ArrayList<String> getStudentWiseDetails(String fileName, String fieldSeparator, String subString, ArrayList<String> arrayOfMatchings) throws IOException {
        BufferedReader buffRead = null; // Initialize a buffer
        int index = 1;
        //System.out.println("FileName : " + fileName + "\n Roll number: " + subString);
        try {
            buffRead = new BufferedReader(new FileReader(fileName));
            buffRead.readLine(); //Skip the first line since it will be the header row
            //read input file line by line
            arrayOfMatchings = new ArrayList<String>();
            while ((line = buffRead.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(fieldSeparator);
                //System.out.println("Current Input Line " + inputLine[0]);

                //Get the column after splitting the line with delimiter specified in fieldSeparator without header in first line
                String loadedOutputFileContent = inputLine[0];
                if (loadedOutputFileContent.toLowerCase().contains(subString.toLowerCase())) {
                    matchedString = loadedOutputFileContent;
                    arrayOfMatchings.add(loadedOutputFileContent);
                    //System.out.println("content in index arrayOfMatchings[" + index + "] :" + loadedOutputFileContent);
                    index++;
                }
            }
            //System.out.printf("Length of array Of Matchings : %d\n", arrayOfMatchings.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOfMatchings;
    }

    // General function to post process students elective preferences
    public void postProcessPreferenceDetailsForDisplay(ArrayList<String> arrayOfMatchings, String substring, String fileName) {
        int numberOfCore = 0, numberOfElec = 0;
        if (fileName.equals(studentPreferenceListNameWithPath)) {
            int iteration = 0;
            int entryCount = arrayOfMatchings.size();
            // System.out.println("\n Length of array of matched contents: " + entryCount);
            //System.out.println("\n Array with matched elements:" + arrayOfMatchings);
            if (entryCount != 0) {
                while (iteration < entryCount) {
                    String matchedString = arrayOfMatchings.get(iteration); //.split(splitByComma);
                    //  System.out.println("Matched elective: " + matchedString);
                    //  if (matchedString.contains("ELEC")) {
                    studentStatsTextAreaWizard6.append("\n" + matchedString);
                    if (matchedString.toLowerCase().contains("core"))
                        numberOfCore++;
                    else if (matchedString.toLowerCase().contains("elec"))
                        numberOfElec++;
                    // }
                    iteration++;
                }
            } else {
                studentStatsTextAreaWizard6.append("\n The student did not register any core courses as well as elective courses");
            }
        }
    }

    // Chart display function to post process students elective preferences
    public void postProcessPerStudentPreferencesForChartDisplay(ArrayList<String> arrayOfMatchings, String enteredRollNumber) {
        if (coreElectivePiePlotDemo instanceof PieChart) {
            coreElectivePiePlotDemo.dispose();
        }
        int numberOfCore = 0, numberOfElec = 0;
        int iteration = 0;
        int entryCount = arrayOfMatchings.size();
        System.out.println("\n Length of array of matched contents: " + entryCount);
        System.out.println("\n Array with matched elements:" + arrayOfMatchings);
        if (entryCount != 0) {
            while (iteration < entryCount) {
                String matchedString = arrayOfMatchings.get(iteration);
                if (matchedString.toLowerCase().contains("core"))
                    numberOfCore++;
                else if (matchedString.toLowerCase().contains("elec"))
                    numberOfElec++;
                // }
                iteration++;
            }
            Slice[] coreElectivePiePlotSlices = {
                    new Slice(numberOfCore, "Core (" + Integer.toString(numberOfCore) + ")"), new Slice(numberOfElec, "Elective (" + Integer.toString(numberOfElec) + ")")
            };
            coreElectivePiePlotDemo = new PieChart(enteredRollNumber.toUpperCase() + ": Core and Elective Preferences", coreElectivePiePlotSlices);
            PiePlot coreElectivePiePlot = (PiePlot) coreElectivePiePlotDemo.chart.getPlot();
            coreElectivePiePlot.setSectionPaint(coreElectivePiePlotSlices[0].name, new Color(55, 94, 151)); // Sky blue color
            coreElectivePiePlot.setSectionPaint(coreElectivePiePlotSlices[1].name, new Color(255, 187, 0)); // sunflower color
            coreElectivePiePlotDemo.setSize(600, 600);
            RefineryUtilities.positionFrameOnScreen(coreElectivePiePlotDemo, 0.1, 0.1);
            coreElectivePiePlotDemo.setVisible(true);
        } else {
            studentStatsTextAreaWizard6.append("\n The student did not register any core courses as well as elective courses");
            JOptionPane.showMessageDialog(null, "\n The student did not register any core courses as well as elective courses", "No student record found", JOptionPane.ERROR_MESSAGE);
        }
    }

    // General function to post process students elective preferences
    public void postProcessPerStudentAllottedElectivesForDisplay(ArrayList<String> arrayOfMatchings, String substring, String fileName) {

        System.out.println("FileName in postProcessing " + fileName);
        if (electiveAcceptRejectPiePlotDemo instanceof PieChart) {
            electiveAcceptRejectPiePlotDemo.dispose();
        }

        if (fileName.equals(formValues.perStudentAllottedCoursesFileName)) {
            int iteration = 1;
            //System.out.println("\n Length of array of matched contents: " + entryCount);
            int allottedCourseCount = (arrayOfMatchings.get(0).split(splitByComma).length);
            //System.out.println("\n Array with matched elements:" + arrayOfMatchings);
            if (allottedCourseCount > 1) {
                while (iteration < allottedCourseCount) {
                    String matchedString = arrayOfMatchings.get(0).split(splitByComma)[iteration].split("\\$")[0];
                    //System.out.println("Allotted elective: [" + iteration + "]" + matchedString);
                    studentStatsTextAreaWizard6.append("\n" + matchedString);
                    iteration++;
                }
            } else {
                studentStatsTextAreaWizard6.append("\n All the preferred courses for this student were rejected (OR) the student did not register any elective courses ");
            }
        }

        if (fileName.equals(formValues.perStudentAllottedStatisticsFileName)) {
            int iteration = 1;
            int fieldCount = arrayOfMatchings.get(0).split(splitByComma).length - 1;
            System.out.println("Statistics " + arrayOfMatchings.get(0));
            if (fieldCount > 1) {
                String effectiveRank = arrayOfMatchings.get(0).split(splitByComma)[1];
                String creditSatistfaction = arrayOfMatchings.get(0).split(splitByComma)[2];
                studentStatsTextAreaWizard6.append("\n Effective Average Rank : " + effectiveRank + "\n Credit satisfaction ratio : " + creditSatistfaction);
            } else {
                studentStatsTextAreaWizard6.append("\n The student did not register any elective courses");
            }
        }

        if (fileName.equals(formValues.rejectionReasonsFileName)) {
            int iteration = 0;
            ArrayList<String> rejectedCourseDict = new ArrayList<String>();
            ArrayList<String> allottedCourseDict = new ArrayList<String>();
            int index = 1, dictIndex = 0;
            int rejectionCount = arrayOfMatchings.size();
            if (rejectionCount > 0) {
                while (iteration < rejectionCount) {
                    String matchedString = arrayOfMatchings.get(iteration);
                    //System.out.println("Rejected Course [" + iteration + "] :" + matchedString);
                    String rejectedCourseName = matchedString.split(splitByComma)[1].split("\\$")[0];
                    System.out.println("Array of allotted course list : " + arrayOfMatchingsFromAllottedCourses);
                    String listAsString = String.join(",", arrayOfMatchingsFromAllottedCourses);
                    if ((!rejectedCourseDict.contains(rejectedCourseName)) && (!listAsString.contains(rejectedCourseName))) {
                        if (!matchedString.contains("Already allotted to inside department version of the course")) {
                            rejectedCourseDict.add(rejectedCourseName);
                            dictIndex++;
                            String rejectedReasons = matchedString.split(splitByComma)[2];
                            studentStatsTextAreaWizard6.append("\n Rejected Elective : [" + index + "] : " + rejectedCourseName + " , Rejection Reason : " + rejectedReasons);
                            index++;
                            totalNumberOfRejectedCourses = index;
                        }
                    }
                    iteration++;
                }
            } else {
                studentStatsTextAreaWizard6.append("\n The student got allotted to all his preferences (OR) did not register for any elective courses");
            }
        }

    }

    // Chart display function to post process students elective preferences 
    public void postProcessPerStudentAllottedElectivesForChartDisplay(ArrayList<String> arrayOfMatchingsFromAllottedCourses, ArrayList<String> arrayOfMatchingsFromRejectionReasons, String enteredRollNumber) {

        if (electiveAcceptRejectPiePlotDemo instanceof PieChart) {
            electiveAcceptRejectPiePlotDemo.dispose();
        }
        int numberOfCourseAllocated = 0, numberOfCourseRejected = 0, numberOfCourseRejected_courseCapacityFull = 0;
        StringBuilder electiveAllocatedLegend = new StringBuilder();
        StringBuilder electiveRejectedLegend = new StringBuilder();
        StringBuilder electiveRejectedLegend_capacityFull = new StringBuilder();
        int allottedCourseCount = (arrayOfMatchingsFromAllottedCourses.get(0).split(splitByComma).length);
        int tempAllottedCourseCount = allottedCourseCount - 1;
        System.out.println("\n Length of array of allotted courses: " + tempAllottedCourseCount);
        System.out.println("\n Array with matched elements:" + arrayOfMatchings);
        electiveAllocatedLegend.append("Allocated(" + tempAllottedCourseCount + ")\n---------");
        int allottedIteration = 1;
        if (allottedCourseCount > 1) {
            while (allottedIteration < allottedCourseCount) {
                String matchedString = arrayOfMatchingsFromAllottedCourses.get(0).split(splitByComma)[allottedIteration].split("\\$")[0];
                //System.out.println("Allotted elective: [" + iteration + "]" + matchedString);
                electiveAllocatedLegend.append("\n" + matchedString);
                allottedIteration++;
            }
        } else {
            studentStatsTextAreaWizard6.append("\n All the preferred courses for this student were rejected (OR) the student did not register any elective courses ");
        }
        numberOfCourseAllocated = allottedIteration - 1;

        ArrayList<String> rejectionCourseDict = new ArrayList<String>();
        ArrayList<String> capacityFullRejectionCourseDict = new ArrayList<String>();
        int rejectionIndex = 1, rejectionDictIndex = 0, capacityFullRejectionIndex = 1, rejectionIteration = 0;
        int tempRejectionCount = totalNumberOfRejectedCourses - 1; //(arrayOfMatchingsFromRejectionReasons.size());
        System.out.println("\n Length of array of rejected courses: " + tempRejectionCount);
        System.out.println("\n Array with matched rejection elements:" + arrayOfMatchingsFromRejectionReasons);
        int rejectionCount = arrayOfMatchingsFromRejectionReasons.size();
        electiveRejectedLegend.append("Rejected(" + tempRejectionCount + ")\n--------");

        if (rejectionCount > 0) {
            while (rejectionIteration < rejectionCount) {
                String matchedString = arrayOfMatchingsFromRejectionReasons.get(rejectionIteration);
                System.out.println("Rejected Course [" + rejectionIteration + "] :" + matchedString);
                String rejectedCourseName = matchedString.split(splitByComma)[1].split("\\$")[0];
                String listAsString = String.join(",", arrayOfMatchingsFromAllottedCourses);
                if ((!rejectionCourseDict.contains(rejectedCourseName)) && (!listAsString.contains(rejectedCourseName))) {
                    if (!matchedString.contains("Already allotted to inside department version of the course")) {
                        rejectionCourseDict.add(rejectedCourseName);
                        rejectionDictIndex++;
                        electiveRejectedLegend.append("\n" + rejectedCourseName);
                        rejectionIndex++;
                    }
                }
                rejectionIteration++;
            }
            numberOfCourseRejected = rejectionCourseDict.size();
        } else {
            studentStatsTextAreaWizard6.append("\n The student got allotted to all his preferences (OR) did not register for any elective courses");
        }
        if (numberOfCourseAllocated != 0 && numberOfCourseRejected != 0) {
            Slice[] slices = {
                    new Slice(numberOfCourseAllocated, electiveAllocatedLegend.toString()), new Slice(numberOfCourseRejected, electiveRejectedLegend.toString())
            };
            electiveAcceptRejectPiePlotDemo = new PieChart(enteredRollNumber.toUpperCase() + ": Allotted and Rejected Electives", slices);
            PiePlot electiveAcceptRejectPiePlot = (PiePlot) electiveAcceptRejectPiePlotDemo.chart.getPlot();
            electiveAcceptRejectPiePlot.setSectionPaint(slices[0].name, new Color(63, 104, 28)); // grass green
            electiveAcceptRejectPiePlot.setSectionPaint(slices[1].name, new Color(251, 101, 66)); // sunset color
            electiveAcceptRejectPiePlotDemo.setSize(600, 600);
            RefineryUtilities.positionFrameOnScreen(electiveAcceptRejectPiePlotDemo, 0.9, 0.1);
            electiveAcceptRejectPiePlotDemo.setVisible(true);

        } else if (numberOfCourseAllocated == 0 && numberOfCourseRejected != 0) {
            Slice[] slices = {
                    new Slice(numberOfCourseRejected, electiveRejectedLegend.toString())
            };
            electiveAcceptRejectPiePlotDemo = new PieChart(enteredRollNumber.toUpperCase() + ": Allotted and Rejected Electives", slices);
            PiePlot electiveAcceptRejectPiePlot = (PiePlot) electiveAcceptRejectPiePlotDemo.chart.getPlot();
            electiveAcceptRejectPiePlot.setSectionPaint(slices[0].name, new Color(251, 101, 66)); // sunset color
            electiveAcceptRejectPiePlotDemo.setSize(600, 600);
            RefineryUtilities.positionFrameOnScreen(electiveAcceptRejectPiePlotDemo, 0.9, 0.1);
            electiveAcceptRejectPiePlotDemo.setVisible(true);
        } else if (numberOfCourseAllocated != 0 && numberOfCourseRejected == 0) {
            Slice[] slices = {
                    new Slice(numberOfCourseAllocated, electiveAllocatedLegend.toString())
            };
            electiveAcceptRejectPiePlotDemo = new PieChart(enteredRollNumber.toUpperCase() + ": Allotted and Rejected Electives", slices);
            PiePlot electiveAcceptRejectPiePlot = (PiePlot) electiveAcceptRejectPiePlotDemo.chart.getPlot();
            electiveAcceptRejectPiePlot.setSectionPaint(slices[0].name, new Color(63, 104, 28)); // grass green
            electiveAcceptRejectPiePlotDemo.setSize(600, 600);
            RefineryUtilities.positionFrameOnScreen(electiveAcceptRejectPiePlotDemo, 0.9, 0.1);
            electiveAcceptRejectPiePlotDemo.setVisible(true);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jPanelStudentwiseAllotmentWizard6 = new JPanel();
        jPanelStudentwiseAllotmentWizard6.setLayout(new FormLayout("fill:15px:noGrow,fill:274px:grow,left:4dlu:noGrow,fill:534px:noGrow,left:4dlu:noGrow,fill:48px:noGrow,left:4dlu:noGrow,fill:159px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:61px:noGrow,top:10dlu:noGrow,center:58px:noGrow,top:13dlu:noGrow,center:74px:noGrow,top:12dlu:noGrow,center:47px:noGrow,top:8dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:335px:noGrow,top:5dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:47px:noGrow"));
        jPanelStudentwiseAllotmentWizard6.setBackground(new Color(-14793908));
        jPanelStudentwiseAllotmentWizard6.setForeground(new Color(-1));
        jLabelStudentwiseAllotmentAnalysisWizard6 = new JLabel();
        Font jLabelStudentwiseAllotmentAnalysisWizard6Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 32, jLabelStudentwiseAllotmentAnalysisWizard6.getFont());
        if (jLabelStudentwiseAllotmentAnalysisWizard6Font != null)
            jLabelStudentwiseAllotmentAnalysisWizard6.setFont(jLabelStudentwiseAllotmentAnalysisWizard6Font);
        jLabelStudentwiseAllotmentAnalysisWizard6.setForeground(new Color(-1774964));
        jLabelStudentwiseAllotmentAnalysisWizard6.setText("Student Specific Allotment Analysis ....");
        CellConstraints cc = new CellConstraints();
        jPanelStudentwiseAllotmentWizard6.add(jLabelStudentwiseAllotmentAnalysisWizard6, cc.xyw(1, 1, 8, CellConstraints.CENTER, CellConstraints.FILL));
        chosenDirectoryLabelWizard6 = new JLabel();
        Font chosenDirectoryLabelWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryLabelWizard6.getFont());
        if (chosenDirectoryLabelWizard6Font != null)
            chosenDirectoryLabelWizard6.setFont(chosenDirectoryLabelWizard6Font);
        chosenDirectoryLabelWizard6.setForeground(new Color(-131585));
        chosenDirectoryLabelWizard6.setText("<html><center> From the chosen directory : <center><html>");
        jPanelStudentwiseAllotmentWizard6.add(chosenDirectoryLabelWizard6, cc.xyw(1, 3, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        chosenDirectoryTextFieldWizard6 = new JTextField();
        jPanelStudentwiseAllotmentWizard6.add(chosenDirectoryTextFieldWizard6, cc.xy(4, 3, CellConstraints.FILL, CellConstraints.CENTER));
        chosenDirectoryCheckButtonWizard6 = new JButton();
        chosenDirectoryCheckButtonWizard6.setBorderPainted(false);
        chosenDirectoryCheckButtonWizard6.setContentAreaFilled(false);
        chosenDirectoryCheckButtonWizard6.setText("");
        jPanelStudentwiseAllotmentWizard6.add(chosenDirectoryCheckButtonWizard6, cc.xy(6, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        chosenDirectoryBrowseButtonWizard6 = new JButton();
        chosenDirectoryBrowseButtonWizard6.setBorderPainted(false);
        chosenDirectoryBrowseButtonWizard6.setEnabled(false);
        Font chosenDirectoryBrowseButtonWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryBrowseButtonWizard6.getFont());
        if (chosenDirectoryBrowseButtonWizard6Font != null)
            chosenDirectoryBrowseButtonWizard6.setFont(chosenDirectoryBrowseButtonWizard6Font);
        chosenDirectoryBrowseButtonWizard6.setIcon(new ImageIcon(getClass().getResource("/gui/dir1-small.png")));
        chosenDirectoryBrowseButtonWizard6.setText("Browse");
        jPanelStudentwiseAllotmentWizard6.add(chosenDirectoryBrowseButtonWizard6, cc.xy(8, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        JScrollPaneStudentDetailsWizard6 = new JScrollPane();
        jPanelStudentwiseAllotmentWizard6.add(JScrollPaneStudentDetailsWizard6, cc.xywh(2, 11, 7, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
        studentStatsTextAreaWizard6 = new JTextArea();
        studentStatsTextAreaWizard6.setEditable(false);
        Font studentStatsTextAreaWizard6Font = this.$$$getFont$$$("Bitstream Charter", Font.BOLD, 16, studentStatsTextAreaWizard6.getFont());
        if (studentStatsTextAreaWizard6Font != null)
            studentStatsTextAreaWizard6.setFont(studentStatsTextAreaWizard6Font);
        studentStatsTextAreaWizard6.setLineWrap(true);
        studentStatsTextAreaWizard6.setWrapStyleWord(true);
        JScrollPaneStudentDetailsWizard6.setViewportView(studentStatsTextAreaWizard6);
        studentRollNumberLabelWizard6 = new JLabel();
        Font studentRollNumberLabelWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentRollNumberLabelWizard6.getFont());
        if (studentRollNumberLabelWizard6Font != null)
            studentRollNumberLabelWizard6.setFont(studentRollNumberLabelWizard6Font);
        studentRollNumberLabelWizard6.setForeground(new Color(-131585));
        studentRollNumberLabelWizard6.setText("<html><center> Enter a roll number : <br> ( Eg: AE13B001 ) <center><html>");
        jPanelStudentwiseAllotmentWizard6.add(studentRollNumberLabelWizard6, cc.xyw(1, 7, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        studentRollNumberTextFieldWizard6 = new JTextField();
        jPanelStudentwiseAllotmentWizard6.add(studentRollNumberTextFieldWizard6, cc.xy(4, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        studentRollNumberCheckButtonWizard6 = new JButton();
        studentRollNumberCheckButtonWizard6.setBorderPainted(false);
        studentRollNumberCheckButtonWizard6.setContentAreaFilled(false);
        studentRollNumberCheckButtonWizard6.setText("");
        jPanelStudentwiseAllotmentWizard6.add(studentRollNumberCheckButtonWizard6, cc.xy(6, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        titleFieldAnalysisWizard6 = new JLabel();
        Font titleFieldAnalysisWizard6Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 26, titleFieldAnalysisWizard6.getFont());
        if (titleFieldAnalysisWizard6Font != null) titleFieldAnalysisWizard6.setFont(titleFieldAnalysisWizard6Font);
        titleFieldAnalysisWizard6.setForeground(new Color(-301758));
        titleFieldAnalysisWizard6.setText("Student Details :");
        jPanelStudentwiseAllotmentWizard6.add(titleFieldAnalysisWizard6, cc.xyw(2, 9, 2, CellConstraints.LEFT, CellConstraints.BOTTOM));
        studentPreferenceListLabelWizard6 = new JLabel();
        studentPreferenceListLabelWizard6.setAutoscrolls(true);
        studentPreferenceListLabelWizard6.setDoubleBuffered(true);
        studentPreferenceListLabelWizard6.setFocusCycleRoot(true);
        studentPreferenceListLabelWizard6.setFocusTraversalPolicyProvider(true);
        Font studentPreferenceListLabelWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentPreferenceListLabelWizard6.getFont());
        if (studentPreferenceListLabelWizard6Font != null)
            studentPreferenceListLabelWizard6.setFont(studentPreferenceListLabelWizard6Font);
        studentPreferenceListLabelWizard6.setForeground(new Color(-131585));
        studentPreferenceListLabelWizard6.setIconTextGap(0);
        studentPreferenceListLabelWizard6.setInheritsPopupMenu(true);
        studentPreferenceListLabelWizard6.setText("<html> <center> Select student's preferenceList <br> ( RollNo, Courseno, Color, Type, SortOrder ) : </center></html> ");
        studentPreferenceListLabelWizard6.setVerticalAlignment(1);
        studentPreferenceListLabelWizard6.setVerticalTextPosition(3);
        jPanelStudentwiseAllotmentWizard6.add(studentPreferenceListLabelWizard6, cc.xy(2, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        studentPreferenceTextFieldWizard6 = new JTextField();
        jPanelStudentwiseAllotmentWizard6.add(studentPreferenceTextFieldWizard6, cc.xy(4, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        studentPreferenceCheckButtonWizard6 = new JButton();
        studentPreferenceCheckButtonWizard6.setBorderPainted(false);
        studentPreferenceCheckButtonWizard6.setContentAreaFilled(false);
        studentPreferenceCheckButtonWizard6.setText("");
        jPanelStudentwiseAllotmentWizard6.add(studentPreferenceCheckButtonWizard6, cc.xy(6, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentRollNumberGetDetailsButtonWizard6 = new JButton();
        studentRollNumberGetDetailsButtonWizard6.setBorderPainted(false);
        Font studentRollNumberGetDetailsButtonWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentRollNumberGetDetailsButtonWizard6.getFont());
        if (studentRollNumberGetDetailsButtonWizard6Font != null)
            studentRollNumberGetDetailsButtonWizard6.setFont(studentRollNumberGetDetailsButtonWizard6Font);
        studentRollNumberGetDetailsButtonWizard6.setText("Get Details");
        jPanelStudentwiseAllotmentWizard6.add(studentRollNumberGetDetailsButtonWizard6, cc.xy(8, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentPreferenceBrowseButtonWizard6 = new JButton();
        studentPreferenceBrowseButtonWizard6.setAutoscrolls(true);
        studentPreferenceBrowseButtonWizard6.setBorderPainted(false);
        studentPreferenceBrowseButtonWizard6.setFocusCycleRoot(true);
        studentPreferenceBrowseButtonWizard6.setFocusTraversalPolicyProvider(true);
        studentPreferenceBrowseButtonWizard6.setFocusable(true);
        Font studentPreferenceBrowseButtonWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentPreferenceBrowseButtonWizard6.getFont());
        if (studentPreferenceBrowseButtonWizard6Font != null)
            studentPreferenceBrowseButtonWizard6.setFont(studentPreferenceBrowseButtonWizard6Font);
        studentPreferenceBrowseButtonWizard6.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        studentPreferenceBrowseButtonWizard6.setIconTextGap(2);
        studentPreferenceBrowseButtonWizard6.setSelected(false);
        studentPreferenceBrowseButtonWizard6.setText("Browse");
        jPanelStudentwiseAllotmentWizard6.add(studentPreferenceBrowseButtonWizard6, cc.xy(8, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        goToWizard5FromWizard6 = new JButton();
        goToWizard5FromWizard6.setBorderPainted(false);
        Font goToWizard5FromWizard6Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, goToWizard5FromWizard6.getFont());
        if (goToWizard5FromWizard6Font != null) goToWizard5FromWizard6.setFont(goToWizard5FromWizard6Font);
        goToWizard5FromWizard6.setText("Go Back");
        jPanelStudentwiseAllotmentWizard6.add(goToWizard5FromWizard6, cc.xy(4, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jPanelStudentwiseAllotmentWizard6;
    }
}


//// Supporting data class for Chart
//class Slice {
//    double value;
//    String name;
//
//    public Slice(double value, String name) {
//        this.value = value;
//        this.name = name;
//    }
//}
