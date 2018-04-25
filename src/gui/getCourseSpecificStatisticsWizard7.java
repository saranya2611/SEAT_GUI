package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
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

import static gui.formValues.courseListNameWithPath;
import static gui.formValues.perCourseAllottedStatisticsFileNameWithPath;

public class getCourseSpecificStatisticsWizard7 extends JFrame {
    public String perCourseStatisticsFileName, courseListFileName;
    String line, chosenDirectoryName, courseListName, errorMsg, enteredCourseNumber, matchedString, courseName, slotsOfMatchedString;
    int lineNo;
    String[] inputLine;
    String splitByComma = ",";
    String splitByNewLine = "\n";
    JButton checkButton;
    String courseNumberPattern = "^[A-Za-z][A-Za-z][0-9][0-9][0-9][0-9][AaBbCcDd\\+\\*]*";
    ArrayList<String> arrayOfMatchingsFromCourseList, arrayOfMatchingsFromPerCourseStatisticsFile, arrayOfMatchings;
    private JPanel jPanelCoursewiseAllotmentWizard7;
    private JLabel jLabelCoursewiseAllotmentAnalysisWizard7;
    private JLabel chosenDirectoryLabelWizard7;
    private JTextField chosenDirectoryTextFieldWizard7;
    private JButton chosenDirectoryCheckButtonWizard7;
    private JButton chosenDirectoryBrowseButtonWizard7;
    private JScrollPane JScrollPaneCourseDetailsWizard7;
    private JTextArea courseStatsTextAreaWizard7;
    private JLabel courseNumberLabelWizard7;
    private JTextField courseNumberTextFieldWizard7;
    private JButton courseNumberCheckButtonWizard7;
    private JLabel titleFieldCourseAnalysisWizard7;
    private JLabel courseListLabelWizard7;
    private JTextField courseListTextFieldWizard7;
    private JButton courseListCheckButtonWizard7;
    private JButton courseNumberGetDetailsButtonWizard7;
    private JButton courseListBrowseButtonWizard7;
    private JButton goToWizard5FromWizard7;

    // Constructor function
    public getCourseSpecificStatisticsWizard7() {
        super();
        $$$setupUI$$$();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
        setSize(1200, 800);

        JScrollPane scrPaneWizard7 = new JScrollPane(jPanelCoursewiseAllotmentWizard7);
        add(scrPaneWizard7);

        jPanelCoursewiseAllotmentWizard7.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        // Decide which components to be visible and which components are need not to be visible
        chosenDirectoryBrowseButtonWizard7.setEnabled(true);
        chosenDirectoryTextFieldWizard7.setEnabled(false);
        chosenDirectoryTextFieldWizard7.setVisible(true);
        chosenDirectoryCheckButtonWizard7.setVisible(false);
        courseListCheckButtonWizard7.setVisible(false);
        courseListTextFieldWizard7.setEnabled(true);
        courseListTextFieldWizard7.setEditable(false);
        courseListBrowseButtonWizard7.setEnabled(true);
        courseNumberCheckButtonWizard7.setVisible(false);
        courseNumberTextFieldWizard7.setEnabled(true);
        courseNumberTextFieldWizard7.setEditable(true);

        // Row 1: Selected directory from previous window
        chosenDirectoryTextFieldWizard7.setText(formValues.getAnalyseAllotmentDirPath());
        chosenDirectoryName = chosenDirectoryTextFieldWizard7.getText();
        formValues.setAnalyseAllotmentDirPath(chosenDirectoryName);
        File directoryName = new File(chosenDirectoryName);
        errorMsg = "Directory not found....!!!";
        enableFileDirectoryExistenceCheckButton(chosenDirectoryCheckButtonWizard7, directoryName, errorMsg);
        chosenDirectoryBrowseButtonWizard7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenDirectoryBrowseButtonWizard7.setEnabled(true);
                chosenDirectoryBrowseButtonWizard7.setEnabled(true);
                JFileChooser jfc = new JFileChooser(formValues.getAnalyseAllotmentDirPath());
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int outputFilesDir = jfc.showOpenDialog(null);
                if (outputFilesDir == JFileChooser.APPROVE_OPTION) {
                    File outputFilesDirName = jfc.getSelectedFile();
                    chosenDirectoryBrowseButtonWizard7.setText(outputFilesDirName.getAbsolutePath());
                    chosenDirectoryName = chosenDirectoryTextFieldWizard7.getText();
                    formValues.setAnalyseAllotmentDirPath(chosenDirectoryName);
                    File directoryName = new File(chosenDirectoryName);
                    enableFileDirectoryExistenceCheckButton(chosenDirectoryCheckButtonWizard7, directoryName, errorMsg);
                }
            }
        });

        /* Load the required files. The files required to give complete details about the student in query are
         *  1) ../courseList.csv -->this will be outside the generated allotment folder. This is one of the input for the executeAllotments code
         *                                                                         --> This file has multiple entries for a course with different slots and each entry has 8 columns: CourseNo,DepartmentCode,MaxOverall2rength,MinOtherBranch2rength,AllocationType,Credit,Slot,Additional Slot,
         *  2) perCourseAllotedStudents.csv .csv --> has the list of students allotted to a course along with number of total seats given to SEAT and  total number of seats allotted through SEAT;
         *                                                          --> Pattern : Course ID, Total Capacity, Allotted Capacity, Students Allotted {RollNo1, RollNo2, RollNo3, ....}
         */

        // Row 1: Get (1) Default chosen directory
        courseListCheckButtonWizard7.setVisible(false);
        // Check the existence of default file
        chosenDirectoryBrowseButtonWizard7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(formValues.getAnalyseAllotmentDirPath());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int courseListFileFlag = jfc.showOpenDialog(null);
                if (courseListFileFlag == JFileChooser.APPROVE_OPTION) {
                    File courseListFile = jfc.getSelectedFile();
                    courseListTextFieldWizard7.setText(courseListFile.getAbsolutePath());
                    courseListName = courseListTextFieldWizard7.getText();
                    formValues.setCourseListNameWithPath(courseListName);
                    System.out.println("Course list " + courseListName);
                    courseListName = formValues.getCourseListNameWithPath();
                    errorMsg = CheckInputFormats.checkCourseListFileFormat(courseListName);
                    if (errorMsg != null) {
                        enableFileDirectoryExistenceCheckButton(courseListCheckButtonWizard7, courseListFile, errorMsg);
                        courseStatsTextAreaWizard7.append("\n Course List: " + formValues.getCourseListNameWithPath());
                        courseStatsTextAreaWizard7.append("\n============================================================== \n ");
                    } else {
                        enableFileDirectoryExistenceCheckButton(courseListCheckButtonWizard7, courseListFile, errorMsg);
                        courseStatsTextAreaWizard7.append("\n Course List: " + formValues.getCourseListNameWithPath());
                        courseStatsTextAreaWizard7.append("\n============================================================== \n ");
                    }
                }
            }
        });

        // Row-2 Get courseList.csv
        // Row 2: Get (1) registrationDataWithBS17CS16ME16changes.csv
        courseListCheckButtonWizard7.setVisible(false);
        // Check the existence of default file
        courseListBrowseButtonWizard7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(formValues.getAnalyseAllotmentDirPath());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int courseListFileFlag = jfc.showOpenDialog(null);
                if (courseListFileFlag == JFileChooser.APPROVE_OPTION) {
                    File courseListFile = jfc.getSelectedFile();
                    courseListTextFieldWizard7.setText(courseListFile.getAbsolutePath());
                    courseListName = courseListTextFieldWizard7.getText();
                    formValues.setCourseListNameWithPath(courseListName);
                    //System.out.println("Student preference list " + courseListName);
                    courseListName = formValues.getCourseListNameWithPath();
                    errorMsg = CheckInputFormats.checkCourseListFileFormat(courseListName);
                    if (errorMsg != null) {
                        enableFileDirectoryExistenceCheckButton(courseListCheckButtonWizard7, courseListFile, errorMsg);
                        courseStatsTextAreaWizard7.append("\n Course List: " + formValues.getCourseListNameWithPath());
                        courseStatsTextAreaWizard7.append("\n============================================================== \n ");
                    } else {
                        enableFileDirectoryExistenceCheckButton(courseListCheckButtonWizard7, courseListFile, errorMsg);
                        courseStatsTextAreaWizard7.append("\n Course List: " + formValues.getCourseListNameWithPath());
                        courseStatsTextAreaWizard7.append("\n============================================================== \n ");
                    }
                }
            }
        });

        // Row -3 : Get student Course number textfield action
        courseNumberTextFieldWizard7.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enteredCourseNumber = courseNumberTextFieldWizard7.getText();
                    if (enteredCourseNumber.matches(courseNumberPattern)) {
                        System.out.println("\n Entered course number is :" + enteredCourseNumber);
                        courseNumberGetDetailsButtonWizard7.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "\n Course number MUST be of form : [A-Z][A-Z][0-9][0-9][0-9][0-9][A/B/C/D/*/+] \n Eg: AE1100, HS3002A, ME3110+\n ", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Row-3 Get details button click action
        courseNumberGetDetailsButtonWizard7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enteredCourseNumber = courseNumberTextFieldWizard7.getText();
                System.out.println("\n Course number is :::::: " + enteredCourseNumber);
                // Get (1) course list
                courseListName = formValues.getCourseListNameWithPath();

                // Get (2) perCourseAllotedStudents.csv
                formValues.setPerCourseAllottedStatisticsFileNameWithPath(chosenDirectoryName + '/' + "perCourseAllotedStudents.csv");
                perCourseStatisticsFileName = formValues.getPerCourseAllottedStatisticsFileNameWithPath();

                if (enteredCourseNumber.matches(courseNumberPattern)) {
                    System.out.println("\n Course number is :" + enteredCourseNumber);

                    // Get the details of a course from course list
                    try {
                        arrayOfMatchingsFromPerCourseStatisticsFile = getCourseWiseDetails(perCourseStatisticsFileName, splitByNewLine, enteredCourseNumber, arrayOfMatchingsFromPerCourseStatisticsFile);
                        arrayOfMatchingsFromCourseList = getCourseWiseDetails(courseListName, splitByNewLine, enteredCourseNumber, arrayOfMatchingsFromCourseList);
                        System.out.println(arrayOfMatchingsFromCourseList);
                        if (arrayOfMatchingsFromCourseList.isEmpty()) {
                            courseStatsTextAreaWizard7.append("\n \n Details of  " + enteredCourseNumber.toUpperCase() + " : ");
                            courseStatsTextAreaWizard7.append("\n----------------------------------------------------");
                            courseStatsTextAreaWizard7.append("\n Either this course is not a B.Tech/DD course (OR) the entered course number does not exists.!!!!");
                            JOptionPane.showMessageDialog(null, "\n Either this course is not a B.Tech/DD course  (OR) the entered course number does not exists.!!", "Course number not found", JOptionPane.WARNING_MESSAGE);
                        } else {
                            courseStatsTextAreaWizard7.append("\n \n Details of  " + enteredCourseNumber.toUpperCase() + " : ");
                            courseStatsTextAreaWizard7.append("\n----------------------------------------------------");
                            postProcessCourseDetailsForDisplay(arrayOfMatchingsFromPerCourseStatisticsFile, enteredCourseNumber, perCourseStatisticsFileName);
                            postProcessCourseDetailsForDisplay(arrayOfMatchingsFromCourseList, enteredCourseNumber, courseListName);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "\n Course number MUST be of form : [A-Z][A-Z][0-9][0-9][0-9][0-9][A/B/C/D/*/+] \n Eg: AE1100, HS3002A, ME3203+\n ", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        goToWizard5FromWizard7.addActionListener(new ActionListener() {
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
                chosenDirectoryCheckButtonWizard7.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("redCross.png"));
                checkButton.setIcon(new ImageIcon(img));
                chosenDirectoryCheckButtonWizard7.setEnabled(true);
                JOptionPane.showMessageDialog(null, errorMsg, "Input Format Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // General function to get course-wise details
    public ArrayList<String> getCourseWiseDetails(String fileName, String fieldSeparator, String subString, ArrayList<String> arrayOfMatchings) throws IOException {
        BufferedReader buffRead = null; // Initialize a buffer
        int index = 1;
        //System.out.println("FileName : " + fileName + "\n Course number: " + subString);
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
                    //System.out.println("matchedString : " + matchedString);
                    arrayOfMatchings.add(loadedOutputFileContent);
                    System.out.println("content in index arrayOfMatchings[" + index + "] :" + loadedOutputFileContent);
                    index++;
                }
            }
            // System.out.printf("Length of array Of Matchings : %d\n", arrayOfMatchings.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOfMatchings;
    }

    // General function to post process students elective preferences
    public void postProcessCourseDetailsForDisplay(ArrayList<String> arrayOfMatchings, String substring, String fileName) {
        if (fileName.equals(perCourseAllottedStatisticsFileNameWithPath)) {
            int iteration = 0;
            int totalCapacity = 0;
            int allottedCapacity = 0;
            int entryCount = arrayOfMatchings.size();
            ArrayList<String> courseDict = new ArrayList<String>();
            ArrayList<String> allottedStudents = new ArrayList<String>();
            // System.out.println("\n Length of array of matched contents: " + entryCount);
            //System.out.println("\n Array with matched elements:" + arrayOfMatchings);
            if (entryCount != 0) {
                while (iteration < entryCount) {
                    matchedString = arrayOfMatchings.get(iteration).split("\\$")[0];
                    System.out.println("\n substring passed :" + substring);
                    System.out.println("\n matched string not pruned :" + matchedString);
                    courseDict.add(matchedString);
                    courseName = matchedString.replace(matchedString, substring);
                    if (!courseDict.contains(substring)) {
                        if (!matchedString.contains("slot")) {
                            totalCapacity = totalCapacity + Integer.parseInt(arrayOfMatchings.get(iteration).split(splitByComma)[1]);
                            allottedCapacity = allottedCapacity + Integer.parseInt(arrayOfMatchings.get(iteration).split(splitByComma)[2]);
                            int length = arrayOfMatchings.get(iteration).split(splitByComma).length;
                            for (int rollNo = 3; rollNo < length; rollNo++) {
                                allottedStudents.add(arrayOfMatchings.get(iteration).split(splitByComma)[rollNo]);
                            }
                        } else {
                            totalCapacity = Integer.parseInt(arrayOfMatchings.get(iteration).split(splitByComma)[1]);
                            allottedCapacity = allottedCapacity + Integer.parseInt(arrayOfMatchings.get(iteration).split(splitByComma)[2]);
                            int length = arrayOfMatchings.get(iteration).split(splitByComma).length;
                            for (int rollNo = 3; rollNo < length; rollNo++) {
                                allottedStudents.add(arrayOfMatchings.get(iteration).split(splitByComma)[rollNo]);
                            }
                        }
                    }
                    iteration++;
                }
                System.out.println("\n Course Name :" + courseName.toUpperCase());
                System.out.println("Capacity of this course given to SEAT: " + totalCapacity);
                System.out.println("Total number of  students allotted to this course : " + allottedCapacity);
                System.out.println("Allotted Students : " + allottedStudents);
                courseStatsTextAreaWizard7.append("\n Course Name :" + courseName.toUpperCase());
                courseStatsTextAreaWizard7.append("\n Capacity of this course given to SEAT : " + totalCapacity);
                courseStatsTextAreaWizard7.append("\n Total number of  students allotted to this course : " + allottedCapacity);
                courseStatsTextAreaWizard7.append("\n Allotted Students : " + allottedStudents);
            } else {
                courseStatsTextAreaWizard7.append("\n The courses is not in the courseList.csv");
            }
        }

        if (fileName.equals(courseListNameWithPath)) {
            int iter = 0;
            int entryCount = arrayOfMatchings.size();
            ArrayList<String> courseDict = new ArrayList<String>();
            ArrayList<String> slotsDict = new ArrayList<String>();
            while (iter < entryCount) {
                matchedString = arrayOfMatchings.get(iter).split("\\$")[0];
                courseDict.add(matchedString);
                slotsOfMatchedString = arrayOfMatchings.get(iter).split(",")[6];
                slotsOfMatchedString.replaceAll("\\s+", ""); //Remove all whitespace
                slotsDict.add(slotsOfMatchedString);
                slotsOfMatchedString = arrayOfMatchings.get(iter).split(",")[7];
                slotsOfMatchedString.replaceAll("\\s+", ""); //Remove all whitespace
                slotsDict.add(slotsOfMatchedString);
                slotsOfMatchedString = arrayOfMatchings.get(iter).split(",")[8];
                slotsOfMatchedString.replaceAll("\\s+", ""); //Remove all whitespace
                slotsDict.add(slotsOfMatchedString);
                iter++;
            }
            courseStatsTextAreaWizard7.append("\n Slots of this course : " + slotsDict);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        jPanelCoursewiseAllotmentWizard7 = new JPanel();
        jPanelCoursewiseAllotmentWizard7.setLayout(new FormLayout("fill:15px:noGrow,fill:274px:grow,left:4dlu:noGrow,fill:534px:noGrow,left:4dlu:noGrow,fill:48px:noGrow,left:4dlu:noGrow,fill:146px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:61px:noGrow,top:10dlu:noGrow,center:58px:noGrow,top:13dlu:noGrow,center:74px:noGrow,top:12dlu:noGrow,center:80px:noGrow,top:8dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:335px:noGrow,top:5dlu:noGrow,top:4dlu:noGrow,center:10px:noGrow,center:49px:noGrow"));
        jPanelCoursewiseAllotmentWizard7.setBackground(new Color(-14793908));
        jPanelCoursewiseAllotmentWizard7.setForeground(new Color(-1));
        CellConstraints cc = new CellConstraints();
        panel1.add(jPanelCoursewiseAllotmentWizard7, cc.xy(1, 1));
        jLabelCoursewiseAllotmentAnalysisWizard7 = new JLabel();
        Font jLabelCoursewiseAllotmentAnalysisWizard7Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 30, jLabelCoursewiseAllotmentAnalysisWizard7.getFont());
        if (jLabelCoursewiseAllotmentAnalysisWizard7Font != null)
            jLabelCoursewiseAllotmentAnalysisWizard7.setFont(jLabelCoursewiseAllotmentAnalysisWizard7Font);
        jLabelCoursewiseAllotmentAnalysisWizard7.setForeground(new Color(-1774964));
        jLabelCoursewiseAllotmentAnalysisWizard7.setText("Course Specific Allotment Analysis ....");
        jPanelCoursewiseAllotmentWizard7.add(jLabelCoursewiseAllotmentAnalysisWizard7, cc.xyw(1, 1, 8, CellConstraints.CENTER, CellConstraints.FILL));
        chosenDirectoryLabelWizard7 = new JLabel();
        Font chosenDirectoryLabelWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryLabelWizard7.getFont());
        if (chosenDirectoryLabelWizard7Font != null)
            chosenDirectoryLabelWizard7.setFont(chosenDirectoryLabelWizard7Font);
        chosenDirectoryLabelWizard7.setForeground(new Color(-131585));
        chosenDirectoryLabelWizard7.setText("<html><center> From the chosen directory : <center><html>");
        jPanelCoursewiseAllotmentWizard7.add(chosenDirectoryLabelWizard7, cc.xyw(1, 3, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        chosenDirectoryTextFieldWizard7 = new JTextField();
        jPanelCoursewiseAllotmentWizard7.add(chosenDirectoryTextFieldWizard7, cc.xy(4, 3, CellConstraints.FILL, CellConstraints.CENTER));
        chosenDirectoryCheckButtonWizard7 = new JButton();
        chosenDirectoryCheckButtonWizard7.setBorderPainted(false);
        chosenDirectoryCheckButtonWizard7.setContentAreaFilled(false);
        Font chosenDirectoryCheckButtonWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryCheckButtonWizard7.getFont());
        if (chosenDirectoryCheckButtonWizard7Font != null)
            chosenDirectoryCheckButtonWizard7.setFont(chosenDirectoryCheckButtonWizard7Font);
        chosenDirectoryCheckButtonWizard7.setText("");
        jPanelCoursewiseAllotmentWizard7.add(chosenDirectoryCheckButtonWizard7, cc.xy(6, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        chosenDirectoryBrowseButtonWizard7 = new JButton();
        chosenDirectoryBrowseButtonWizard7.setBorderPainted(false);
        chosenDirectoryBrowseButtonWizard7.setEnabled(false);
        Font chosenDirectoryBrowseButtonWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryBrowseButtonWizard7.getFont());
        if (chosenDirectoryBrowseButtonWizard7Font != null)
            chosenDirectoryBrowseButtonWizard7.setFont(chosenDirectoryBrowseButtonWizard7Font);
        chosenDirectoryBrowseButtonWizard7.setIcon(new ImageIcon(getClass().getResource("/gui/dir1-small.png")));
        chosenDirectoryBrowseButtonWizard7.setText("Browse");
        jPanelCoursewiseAllotmentWizard7.add(chosenDirectoryBrowseButtonWizard7, cc.xy(8, 3));
        JScrollPaneCourseDetailsWizard7 = new JScrollPane();
        jPanelCoursewiseAllotmentWizard7.add(JScrollPaneCourseDetailsWizard7, cc.xywh(2, 11, 7, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
        courseStatsTextAreaWizard7 = new JTextArea();
        courseStatsTextAreaWizard7.setEditable(false);
        Font courseStatsTextAreaWizard7Font = this.$$$getFont$$$("Bitstream Charter", Font.BOLD, 16, courseStatsTextAreaWizard7.getFont());
        if (courseStatsTextAreaWizard7Font != null) courseStatsTextAreaWizard7.setFont(courseStatsTextAreaWizard7Font);
        courseStatsTextAreaWizard7.setLineWrap(true);
        courseStatsTextAreaWizard7.setWrapStyleWord(true);
        JScrollPaneCourseDetailsWizard7.setViewportView(courseStatsTextAreaWizard7);
        courseNumberLabelWizard7 = new JLabel();
        Font courseNumberLabelWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseNumberLabelWizard7.getFont());
        if (courseNumberLabelWizard7Font != null) courseNumberLabelWizard7.setFont(courseNumberLabelWizard7Font);
        courseNumberLabelWizard7.setForeground(new Color(-131585));
        courseNumberLabelWizard7.setText("<html><center> Enter a course number : <br> ( Eg: AM1100,HS3002A,<br>MS3510+ ) <center><html>");
        jPanelCoursewiseAllotmentWizard7.add(courseNumberLabelWizard7, cc.xy(2, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        courseNumberTextFieldWizard7 = new JTextField();
        jPanelCoursewiseAllotmentWizard7.add(courseNumberTextFieldWizard7, cc.xy(4, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        courseNumberCheckButtonWizard7 = new JButton();
        courseNumberCheckButtonWizard7.setBorderPainted(false);
        courseNumberCheckButtonWizard7.setContentAreaFilled(false);
        Font courseNumberCheckButtonWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseNumberCheckButtonWizard7.getFont());
        if (courseNumberCheckButtonWizard7Font != null)
            courseNumberCheckButtonWizard7.setFont(courseNumberCheckButtonWizard7Font);
        courseNumberCheckButtonWizard7.setText("");
        jPanelCoursewiseAllotmentWizard7.add(courseNumberCheckButtonWizard7, cc.xy(6, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        titleFieldCourseAnalysisWizard7 = new JLabel();
        Font titleFieldCourseAnalysisWizard7Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 26, titleFieldCourseAnalysisWizard7.getFont());
        if (titleFieldCourseAnalysisWizard7Font != null)
            titleFieldCourseAnalysisWizard7.setFont(titleFieldCourseAnalysisWizard7Font);
        titleFieldCourseAnalysisWizard7.setForeground(new Color(-301758));
        titleFieldCourseAnalysisWizard7.setText("Course Details :");
        jPanelCoursewiseAllotmentWizard7.add(titleFieldCourseAnalysisWizard7, cc.xyw(2, 9, 2, CellConstraints.LEFT, CellConstraints.BOTTOM));
        courseListLabelWizard7 = new JLabel();
        courseListLabelWizard7.setAutoscrolls(true);
        courseListLabelWizard7.setDoubleBuffered(true);
        courseListLabelWizard7.setFocusCycleRoot(true);
        courseListLabelWizard7.setFocusTraversalPolicyProvider(true);
        Font courseListLabelWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseListLabelWizard7.getFont());
        if (courseListLabelWizard7Font != null) courseListLabelWizard7.setFont(courseListLabelWizard7Font);
        courseListLabelWizard7.setForeground(new Color(-131585));
        courseListLabelWizard7.setIconTextGap(0);
        courseListLabelWizard7.setInheritsPopupMenu(true);
        courseListLabelWizard7.setText("<html> <center> Select Course List <br> (CourseNo,Dept.Code,Capacity,<br>AllocationType,Credit,Slots,) : </center></html> ");
        courseListLabelWizard7.setVerticalAlignment(1);
        courseListLabelWizard7.setVerticalTextPosition(3);
        jPanelCoursewiseAllotmentWizard7.add(courseListLabelWizard7, cc.xy(2, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        courseListTextFieldWizard7 = new JTextField();
        jPanelCoursewiseAllotmentWizard7.add(courseListTextFieldWizard7, cc.xy(4, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        courseListCheckButtonWizard7 = new JButton();
        courseListCheckButtonWizard7.setBorderPainted(false);
        courseListCheckButtonWizard7.setContentAreaFilled(false);
        Font courseListCheckButtonWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseListCheckButtonWizard7.getFont());
        if (courseListCheckButtonWizard7Font != null)
            courseListCheckButtonWizard7.setFont(courseListCheckButtonWizard7Font);
        courseListCheckButtonWizard7.setText("");
        jPanelCoursewiseAllotmentWizard7.add(courseListCheckButtonWizard7, cc.xy(6, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        courseNumberGetDetailsButtonWizard7 = new JButton();
        courseNumberGetDetailsButtonWizard7.setBorderPainted(false);
        Font courseNumberGetDetailsButtonWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseNumberGetDetailsButtonWizard7.getFont());
        if (courseNumberGetDetailsButtonWizard7Font != null)
            courseNumberGetDetailsButtonWizard7.setFont(courseNumberGetDetailsButtonWizard7Font);
        courseNumberGetDetailsButtonWizard7.setText("Get Details");
        jPanelCoursewiseAllotmentWizard7.add(courseNumberGetDetailsButtonWizard7, cc.xy(8, 7));
        courseListBrowseButtonWizard7 = new JButton();
        courseListBrowseButtonWizard7.setAutoscrolls(true);
        courseListBrowseButtonWizard7.setBorderPainted(false);
        courseListBrowseButtonWizard7.setFocusCycleRoot(true);
        courseListBrowseButtonWizard7.setFocusTraversalPolicyProvider(true);
        courseListBrowseButtonWizard7.setFocusable(true);
        Font courseListBrowseButtonWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseListBrowseButtonWizard7.getFont());
        if (courseListBrowseButtonWizard7Font != null)
            courseListBrowseButtonWizard7.setFont(courseListBrowseButtonWizard7Font);
        courseListBrowseButtonWizard7.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        courseListBrowseButtonWizard7.setIconTextGap(2);
        courseListBrowseButtonWizard7.setSelected(false);
        courseListBrowseButtonWizard7.setText("Browse");
        jPanelCoursewiseAllotmentWizard7.add(courseListBrowseButtonWizard7, cc.xy(8, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        goToWizard5FromWizard7 = new JButton();
        goToWizard5FromWizard7.setBorderPainted(false);
        Font goToWizard5FromWizard7Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, goToWizard5FromWizard7.getFont());
        if (goToWizard5FromWizard7Font != null) goToWizard5FromWizard7.setFont(goToWizard5FromWizard7Font);
        goToWizard5FromWizard7.setText("Go Back");
        jPanelCoursewiseAllotmentWizard7.add(goToWizard5FromWizard7, cc.xy(4, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
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
}
