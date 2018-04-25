package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import services.CheckInputFormats;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class defaultSetOfFilesWizard2 extends JFrame {
    public JLabel jLabelToSelectOptionsWizard2;
    public JLabel selectedDefaultDirectoryLabelWizard2;
    public JTextField selectedDefaultDirTextFieldWizard2;
    public JLabel courseDetailsSegmentLabelWizard2;
    public JLabel slotConfigFileLabelWizard2;
    public JTextField slotConfigFileTextFieldWizard2;
    public JButton slotConfigFileCheckButtonWizard2;
    public JButton slotConfigFileButtonWizard2;
    public JLabel courseDetailsListLabelWizard2;
    public JTextField courseDetailsListTextFieldWizard2;
    public JButton courseDetailsListButtonWizard2;
    public JButton courseDetailsListCheckButtonWizrd2;
    public JLabel coursePrefListGenerationLabelWizard2;
    public JComboBox coursePrefListGenerationComboBoxWizard2;
    public JLabel selectAnExistingCoursePrefListLabelWizard2;
    public JTextField selectAnExistingCoursePrefListTextFieldWizard2;
    public JButton selectAnExistingCoursePrefListCheckButtonWizard2;
    public JButton selectAnExistingCoursePrefListButtonWizard2;
    public JLabel studentCgpaListLabelWizard2;
    public JTextField studentCgpaListTextFieldWizard2;
    public JButton studentCgpaListCheckButtonWizard2;
    public JButton studentCgpaListButtonWizard2;
    public JLabel studentPreferenceListLabelWizard2;
    public JTextField studentPreferenceListTextFieldWizard2;
    public JButton studentPrefListCheckButtonWizard2;
    public JButton studentPrefListButtonWizard2;
    public JLabel selectHighPriorityStudentsListLabelWizard2;
    public JTextField selectHighPriorityStudentsListTextFieldWizard2;
    public JButton selectHighPriorityStudentsListCheckButtonWizard2;
    public JButton selectHighPriorityStudentsListButtonWizard2;
    public JButton prevButtonWizard2;
    public JButton nextButtonWizard2;
    public JPanel jPanelWizard2;
    public JLabel studentDetailsSegmentLabelWizard2;


    public boolean usingExistingCourseListFlag;
    public int comboCheckFlag;
    public Image redCrossIcon, getGreenTickIcon;
    public String directoryChosenFromWizard1;
    JFileChooser chosenDirFromWizard1 = new JFileChooser();
    String chosenTitle;
    String defaultDir;
    String errorMsg;
    String slotsFile, courseListFile, existingCoursePreferenceFile;
    String studentCgpaFile, studentPreferenceFile, highPriorityFile;
    JButton checkButton;
    formValues fV = formValues.getInstance();

//	private inputToAllotmentsWizard1 wizard1;
//	private defaultSetOfFilesWizard2 wizard2;
//	private defaultSetOfFilesWizard3 wizard3;

    public defaultSetOfFilesWizard2() {
        super();
        $$$setupUI$$$();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
        this.setSize(1500, 800);

        getContentPane().add(jPanelWizard2);
        setVisible(true);
        setTitle("IITM-SEAT");
        JScrollPane scrPaneWizard2 = new JScrollPane(jPanelWizard2);
        add(scrPaneWizard2);

        jPanelWizard2.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });


        // Connect wizard 1 with prev button
        prevButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputToAllotmentsWizard1 wizard1 = new inputToAllotmentsWizard1();
                wizard1.setVisible(true);
                dispose();
            }
        });

        // Row 1: chosen default directory from previous wizard
        directoryChosenFromWizard1 = formValues.getDefaultDirPath();
        System.out.println(directoryChosenFromWizard1);
        selectedDefaultDirTextFieldWizard2.setText(directoryChosenFromWizard1);
        chosenDirFromWizard1.setCurrentDirectory(new File(directoryChosenFromWizard1));
        chosenDirFromWizard1.setDialogTitle(chosenTitle);
        chosenDirFromWizard1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // Make previous button enabled
        prevButtonWizard2.setEnabled(true);

        // Make all check buttons invisible
        courseDetailsListCheckButtonWizrd2.setVisible(false);
        selectAnExistingCoursePrefListCheckButtonWizard2.setVisible(false);
        selectHighPriorityStudentsListCheckButtonWizard2.setVisible(false);
        slotConfigFileCheckButtonWizard2.setVisible(false);
        studentCgpaListCheckButtonWizard2.setVisible(false);
        studentPrefListCheckButtonWizard2.setVisible(false);

        // ###################### Row 2: slot-config files browseButton click action listener ################################
        defaultDir = selectedDefaultDirTextFieldWizard2.getText();
        slotConfigFileTextFieldWizard2.setText(defaultDir + "/" + "new_slot_config.csv");
        // Check the existence of default files
        slotsFile = slotConfigFileTextFieldWizard2.getText();
        File slotConfigFile = new File(slotsFile);
        formValues.setSlotConfigFileName(slotConfigFileTextFieldWizard2.getText());
        enableFileDirectoryExistenceCheckButton(slotConfigFileCheckButtonWizard2, slotConfigFile, errorMsg);
        slotConfigFileButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirFromWizard1.getCurrentDirectory());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int slotConfigFileFlag = jfc.showOpenDialog(null);
                if (slotConfigFileFlag == JFileChooser.APPROVE_OPTION) {
                    File slotConfigFileName = jfc.getSelectedFile();
                    slotConfigFileTextFieldWizard2.setText(slotConfigFileName.getAbsolutePath());
                    slotsFile = slotConfigFileTextFieldWizard2.getText();
                    formValues.setSlotConfigFileName(slotConfigFileTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkSlotsFileFormat(slotsFile);
                    if (errorMsg != null) {
                        printErrorMessage(slotConfigFileCheckButtonWizard2, "\nExiting due to error in the format of the slots file. ");
                        enableFileDirectoryExistenceCheckButton(slotConfigFileCheckButtonWizard2, slotConfigFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(slotConfigFileCheckButtonWizard2, slotConfigFileName, errorMsg);
                    }

                }
            }
        });

        // ###################### Row 2: slot-config files textfield keyPress listener ################################
        slotConfigFileTextFieldWizard2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    slotsFile = slotConfigFileTextFieldWizard2.getText();
                    File slotConfigFile = new File(slotsFile);
                    formValues.setSlotConfigFileName(slotConfigFileTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkSlotsFileFormat(slotsFile);
                    if (errorMsg != null) {
                        printErrorMessage(slotConfigFileCheckButtonWizard2, "\nExiting due to error in the format of the slots file. ");
                        enableFileDirectoryExistenceCheckButton(slotConfigFileCheckButtonWizard2, slotConfigFile, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(slotConfigFileCheckButtonWizard2, slotConfigFile, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });


        // ###################### Row 3: course details list  browseButton click action listener ################################
        courseDetailsListTextFieldWizard2.setText(defaultDir + "/" + "courseList.csv");
        // Check the existence of defaultly chosen file
        courseListFile = courseDetailsListTextFieldWizard2.getText();
        formValues.setCourseDetailsFileNameWithPath(courseDetailsListTextFieldWizard2.getText());
        File courseList = new File(courseListFile);
        enableFileDirectoryExistenceCheckButton(courseDetailsListCheckButtonWizrd2, courseList, errorMsg);
        courseDetailsListButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirFromWizard1.getCurrentDirectory());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int courseListFileFlag = jfc.showOpenDialog(null);
                if (courseListFileFlag == JFileChooser.APPROVE_OPTION) {
                    File courseListFileName = jfc.getSelectedFile();
                    courseDetailsListTextFieldWizard2.setText(courseListFileName.getAbsolutePath());
                    courseListFile = courseDetailsListTextFieldWizard2.getText();
                    formValues.setCourseDetailsFileNameWithPath(courseDetailsListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkCourseListFileFormat(courseListFile);
                    if (errorMsg != null) {
                        printErrorMessage(courseDetailsListCheckButtonWizrd2, "\nExiting due to error in the format of the course list file. ");
                        enableFileDirectoryExistenceCheckButton(courseDetailsListCheckButtonWizrd2, courseListFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(courseDetailsListCheckButtonWizrd2, courseListFileName, errorMsg);
                    }
                }
            }
        });

        // ###################### Row 3: course details list textfield keyPress listener ################################
        courseDetailsListTextFieldWizard2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    courseListFile = courseDetailsListTextFieldWizard2.getText();
                    File courseList = new File(courseListFile);
                    formValues.setCourseDetailsFileNameWithPath(courseDetailsListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkCourseListFileFormat(courseListFile);
                    if (errorMsg != null) {
                        printErrorMessage(courseDetailsListCheckButtonWizrd2, "\nExiting due to error in the format of the course list file. ");
                        enableFileDirectoryExistenceCheckButton(courseDetailsListCheckButtonWizrd2, courseList, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(courseDetailsListCheckButtonWizrd2, courseList, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });


        // ###################### Row 4: course preference generation dropdown box ################################
        selectAnExistingCoursePrefListCheckButtonWizard2.setVisible(false);
        selectAnExistingCoursePrefListTextFieldWizard2.setEnabled(false);
        selectAnExistingCoursePrefListTextFieldWizard2.setEditable(false);
        selectAnExistingCoursePrefListButtonWizard2.setEnabled(false);

        coursePrefListGenerationComboBoxWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (coursePrefListGenerationComboBoxWizard2.getSelectedIndex() == 1) {
                    usingExistingCourseListFlag = true;
                    formValues.setUsingExistingCoursePreferenceListFlag(usingExistingCourseListFlag);
                    selectAnExistingCoursePrefListTextFieldWizard2.setEnabled(false);
                    selectAnExistingCoursePrefListTextFieldWizard2.setEditable(false);
                    selectAnExistingCoursePrefListButtonWizard2.setEnabled(false);
                    selectAnExistingCoursePrefListCheckButtonWizard2.setVisible(false);
                    selectAnExistingCoursePrefListTextFieldWizard2.setText("coursePreference.csv");
                    formValues.setNewCoursePreferenceFileNameWithPath(selectAnExistingCoursePrefListTextFieldWizard2.getText());

                } else if (coursePrefListGenerationComboBoxWizard2.getSelectedIndex() == 2) {
                    usingExistingCourseListFlag = false;
                    formValues.setUsingExistingCoursePreferenceListFlag(usingExistingCourseListFlag);
                    selectAnExistingCoursePrefListTextFieldWizard2.setEnabled(true);
                    selectAnExistingCoursePrefListTextFieldWizard2.setEditable(true);
                    selectAnExistingCoursePrefListButtonWizard2.setEnabled(true);
                    selectAnExistingCoursePrefListTextFieldWizard2.setText(defaultDir + "/" + "coursePreference.csv");
                    // Check the existence of default file
                    String coursePrefFileName = selectAnExistingCoursePrefListTextFieldWizard2.getText();
                    File existingCoursePreferenceFileName = new File(coursePrefFileName);
                    formValues.setExistingCoursePreferenceFileNameWithPath(selectAnExistingCoursePrefListTextFieldWizard2.getText());
                    System.out.println("CheckPoint1: " + formValues.getExistingCoursePreferenceFileNameWithPath());
                    enableFileDirectoryExistenceCheckButton(selectAnExistingCoursePrefListCheckButtonWizard2, existingCoursePreferenceFileName, errorMsg);

                    // Row5 is related with the dropDown/Combo box in Row 4
                    // ################## Row 5: Selecting an existing course preference list browseButton click listener ###########
                    selectAnExistingCoursePrefListButtonWizard2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser jfc = new JFileChooser(chosenDirFromWizard1.getCurrentDirectory());
                            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            int existingCoursePreferenceFileFlag = jfc.showOpenDialog(null);
                            if (existingCoursePreferenceFileFlag == JFileChooser.APPROVE_OPTION) {
                                File existingCoursePreferenceFileName = jfc.getSelectedFile();
                                selectAnExistingCoursePrefListTextFieldWizard2.setText(existingCoursePreferenceFileName.getAbsolutePath());
                                formValues.setExistingCoursePreferenceFileNameWithPath(selectAnExistingCoursePrefListTextFieldWizard2.getText());
                                System.out.println("CheckPoint2: " + formValues.getExistingCoursePreferenceFileNameWithPath());
                                errorMsg = CheckInputFormats.checkCoursePreferenceListFileFormat(existingCoursePreferenceFile);
                                selectAnExistingCoursePrefListCheckButtonWizard2.setVisible(true);
                                if (errorMsg != null) {
                                    printErrorMessage(selectAnExistingCoursePrefListCheckButtonWizard2, "\nExiting due to error in the format of the  existing course preference list file. ");
                                    enableFileDirectoryExistenceCheckButton(selectAnExistingCoursePrefListCheckButtonWizard2, existingCoursePreferenceFileName, errorMsg);
                                } else {
                                    enableFileDirectoryExistenceCheckButton(selectAnExistingCoursePrefListCheckButtonWizard2, existingCoursePreferenceFileName, errorMsg);
                                }
                            }
                        }
                    });

                    // ################## Row 5: Selecting an existing course preference list textField key listener ###########
                    selectAnExistingCoursePrefListTextFieldWizard2.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                String coursePrefFileName = selectAnExistingCoursePrefListTextFieldWizard2.getText();
                                File existingCoursePreferenceFileName = new File(coursePrefFileName);
                                formValues.setExistingCoursePreferenceFileNameWithPath(selectAnExistingCoursePrefListTextFieldWizard2.getText());
                                errorMsg = CheckInputFormats.checkCoursePreferenceListFileFormat(existingCoursePreferenceFile);
                                selectAnExistingCoursePrefListCheckButtonWizard2.setVisible(true);
                                if (errorMsg != null) {
                                    printErrorMessage(selectAnExistingCoursePrefListCheckButtonWizard2, "\nExiting due to error in the format of the  existing course preference list file. ");
                                    enableFileDirectoryExistenceCheckButton(selectAnExistingCoursePrefListCheckButtonWizard2, existingCoursePreferenceFileName, errorMsg);
                                } else {
                                    enableFileDirectoryExistenceCheckButton(selectAnExistingCoursePrefListCheckButtonWizard2, existingCoursePreferenceFileName, errorMsg);
                                }
                            }
                            super.keyPressed(e);
                        }
                    });
                }
            }
        });
        // ###################### Row 6: students list with rollnumber, cgpa and max credits browseButton click actionlistener ################################
        studentCgpaListCheckButtonWizard2.setVisible(false);
        studentCgpaListTextFieldWizard2.setText(defaultDir + "/" + "studentDetails.csv");
        // Check the existence of default file
        studentCgpaFile = studentCgpaListTextFieldWizard2.getText();
        File studentCgpaFileName = new File(studentCgpaFile);
        formValues.setStudentCGPAFileNameWithPath(studentCgpaListTextFieldWizard2.getText());
        enableFileDirectoryExistenceCheckButton(studentCgpaListCheckButtonWizard2, studentCgpaFileName, errorMsg);

        studentCgpaListButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirFromWizard1.getCurrentDirectory());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int studentCgpaFileFlag = jfc.showOpenDialog(null);
                if (studentCgpaFileFlag == JFileChooser.APPROVE_OPTION) {
                    File studentCgpaFileName = jfc.getSelectedFile();
                    studentCgpaListTextFieldWizard2.setText(studentCgpaFileName.getAbsolutePath());
                    studentCgpaFile = studentCgpaListTextFieldWizard2.getText();
                    formValues.setStudentCGPAFileNameWithPath(studentCgpaListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkStudentListFileFormat(studentCgpaFile);
                    if (errorMsg != null) {
                        printErrorMessage(studentCgpaListCheckButtonWizard2, "\nExiting due to error in the format of the  existing course preference list file. ");
                        enableFileDirectoryExistenceCheckButton(studentCgpaListCheckButtonWizard2, studentCgpaFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(studentCgpaListCheckButtonWizard2, studentCgpaFileName, errorMsg);
                    }
                }
            }
        });
        // ###################### Row 6: students list with rollnumber, cgpa and max credits textfield key listener ################################
        studentCgpaListTextFieldWizard2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    studentCgpaFile = studentCgpaListTextFieldWizard2.getText();
                    File studentCgpaFileName = new File(studentCgpaFile);
                    formValues.setStudentCGPAFileNameWithPath(studentCgpaListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkStudentListFileFormat(studentCgpaFile);
                    if (errorMsg != null) {
                        printErrorMessage(studentCgpaListCheckButtonWizard2, "\nExiting due to error in the format of the  existing course preference list file. ");
                        enableFileDirectoryExistenceCheckButton(studentCgpaListCheckButtonWizard2, studentCgpaFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(studentCgpaListCheckButtonWizard2, studentCgpaFileName, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });

        // ###################### Row 7: students preference list with rollnumber, prefereed courses, sort order, colorCode etc browseButton click actionListener ################################
        studentPrefListCheckButtonWizard2.setVisible(false);
        studentPreferenceListTextFieldWizard2.setText(defaultDir + "/" + "studentPreferenceList.csv");
        // Check the existence of default file
        studentPreferenceFile = studentPreferenceListTextFieldWizard2.getText();
        File studentPreferenceFileName = new File(studentPreferenceFile);
        formValues.setStudentPreferenceListNameWithPath(studentPreferenceListTextFieldWizard2.getText());
        enableFileDirectoryExistenceCheckButton(studentPrefListCheckButtonWizard2, studentPreferenceFileName, errorMsg);

        studentPrefListButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirFromWizard1.getCurrentDirectory());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int studentPreferenceFileFlag = jfc.showOpenDialog(null);
                if (studentPreferenceFileFlag == JFileChooser.APPROVE_OPTION) {
                    File studentPreferenceFileName = jfc.getSelectedFile();
                    studentPreferenceListTextFieldWizard2.setText(studentPreferenceFileName.getAbsolutePath());
                    studentPreferenceFile = studentPreferenceListTextFieldWizard2.getText();
                    formValues.setStudentPreferenceListNameWithPath(studentPreferenceListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkStudentPreferenceListFileFormat(studentPreferenceFile);
                    if (errorMsg != null) {
                        printErrorMessage(studentPrefListCheckButtonWizard2, "\nExiting due to error in the format of the student  preference list file. ");
                        enableFileDirectoryExistenceCheckButton(studentPrefListCheckButtonWizard2, studentPreferenceFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(studentPrefListCheckButtonWizard2, studentPreferenceFileName, errorMsg);
                    }
                }
            }
        });

        // ###################### Row 7: students preference list with rollnumber, prefereed courses, sort order, colorCode etc textfield key listener ################################
        studentPreferenceListTextFieldWizard2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    studentPreferenceFile = studentPreferenceListTextFieldWizard2.getText();
                    File studentPreferenceFileName = new File(studentPreferenceFile);
                    formValues.setStudentPreferenceListNameWithPath(studentPreferenceListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkStudentPreferenceListFileFormat(studentPreferenceFile);
                    if (errorMsg != null) {
                        printErrorMessage(studentPrefListCheckButtonWizard2, "\nExiting due to error in the format of the student  preference list file. ");
                        enableFileDirectoryExistenceCheckButton(studentPrefListCheckButtonWizard2, studentPreferenceFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(studentPrefListCheckButtonWizard2, studentPreferenceFileName, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });


        // ###################### Row 8: highpriority students list mandatory courses etc browseButton click listener ###############################
        selectHighPriorityStudentsListCheckButtonWizard2.setVisible(false);
        selectHighPriorityStudentsListTextFieldWizard2.setText(defaultDir + "/" + "highPriorityStudents.csv");
        // Check the existence of default file
        highPriorityFile = selectHighPriorityStudentsListTextFieldWizard2.getText();
        File highPriorityFileName = new File(highPriorityFile);
        formValues.setHighPriorityStudentDetailsFileNameWithPath(selectHighPriorityStudentsListTextFieldWizard2.getText());
        enableFileDirectoryExistenceCheckButton(selectHighPriorityStudentsListCheckButtonWizard2, highPriorityFileName, errorMsg);

        selectHighPriorityStudentsListButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirFromWizard1.getCurrentDirectory());
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int highPriorityFileFlag = jfc.showOpenDialog(null);
                if (highPriorityFileFlag == JFileChooser.APPROVE_OPTION) {
                    File highPriorityFileName = jfc.getSelectedFile();
                    selectHighPriorityStudentsListTextFieldWizard2.setText(highPriorityFileName.getAbsolutePath());
                    highPriorityFile = selectHighPriorityStudentsListTextFieldWizard2.getText();
                    formValues.setHighPriorityStudentDetailsFileNameWithPath(selectHighPriorityStudentsListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkHighPriorityCoursePreferencesConfigFileFormat(highPriorityFile);
                    if (errorMsg != null) {
                        printErrorMessage(selectHighPriorityStudentsListCheckButtonWizard2, "\nExiting due to error in the format of the high priority course list file. ");
                        enableFileDirectoryExistenceCheckButton(selectHighPriorityStudentsListCheckButtonWizard2, highPriorityFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(selectHighPriorityStudentsListCheckButtonWizard2, highPriorityFileName, errorMsg);
                    }
                }
            }
        });

        // ###################### Row 8: highpriority students list mandatory courses etc textfield action listener ################################
        selectHighPriorityStudentsListTextFieldWizard2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    highPriorityFile = selectHighPriorityStudentsListTextFieldWizard2.getText();
                    File highPriorityFileName = new File(highPriorityFile);
                    formValues.setHighPriorityStudentDetailsFileNameWithPath(selectHighPriorityStudentsListTextFieldWizard2.getText());
                    errorMsg = CheckInputFormats.checkHighPriorityCoursePreferencesConfigFileFormat(highPriorityFile);
                    if (errorMsg != null) {
                        printErrorMessage(selectHighPriorityStudentsListCheckButtonWizard2, "\nExiting due to error in the format of the high priority course list file. ");
                        enableFileDirectoryExistenceCheckButton(selectHighPriorityStudentsListCheckButtonWizard2, highPriorityFileName, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(selectHighPriorityStudentsListCheckButtonWizard2, highPriorityFileName, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });

        if ((studentPrefListCheckButtonWizard2.getIcon() == redCrossIcon)) {
            String warningMessage = "Check student's preference list file in 7-th row";
            nextWizardNavigation(warningMessage);
        }

        // Look for changesin the drop-down box at row-4
        coursePrefListGenerationComboBoxWizard2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("Selected Index: " + comboCheckFlag);
                comboCheckFlag = coursePrefListGenerationComboBoxWizard2.getSelectedIndex();
            }
        });

        // Connect wizard 3 with next button on buttonClick
        nextButtonWizard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File slotConfFile = new File(slotConfigFileTextFieldWizard2.getText());
                File courseDetailsFile = new File(courseDetailsListTextFieldWizard2.getText());
                File existingCoursePrefFile = new File(selectAnExistingCoursePrefListTextFieldWizard2.getText());
                File studentCgpaListFile = new File(studentCgpaListTextFieldWizard2.getText());
                File studentPrefListFile = new File(studentPreferenceListTextFieldWizard2.getText());
                File highPriorityListFile = new File(selectHighPriorityStudentsListTextFieldWizard2.getText());
                comboCheckFlag = coursePrefListGenerationComboBoxWizard2.getSelectedIndex();
                String warningMessage;
                if (comboCheckFlag == 0) {
                    System.out.println("\n A) Selected Index: " + comboCheckFlag);
                    warningMessage = "Select one option for generating the course preference list in 4-th row";
                    nextWizardNavigation(warningMessage);
                }

                System.out.println(((coursePrefListGenerationComboBoxWizard2.getSelectedIndex() == 2) && (existingCoursePrefFile.exists())));
                if (slotConfFile.exists() && courseDetailsFile.exists() && studentCgpaListFile.exists() && studentPrefListFile.exists() && highPriorityListFile.exists() && ((coursePrefListGenerationComboBoxWizard2.getSelectedIndex() == 0) || (coursePrefListGenerationComboBoxWizard2.getSelectedIndex() == 1) || ((coursePrefListGenerationComboBoxWizard2.getSelectedIndex() == 2) && (existingCoursePrefFile.exists())))) {
                    nextButtonWizard2.setEnabled(true);
                    nextButtonWizard2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            defaultSetOfFilesWizard3 wizard3 = new defaultSetOfFilesWizard3();
                            wizard3.setVisible(true);
                            dispose();
                        }
                    });
                } else {
                    warningMessage = "Please check the input fields flagged with Red-cross";
                    nextWizardNavigation(warningMessage);
                }
            }
        });
    }

    public void printErrorMessage(JButton currentCheckButton, String s) {
        System.out.println(s);
//		fV.displayOutputProgressTextArea.concat ("\n" + s);
        checkButton = currentCheckButton;
        JOptionPane.showMessageDialog(null, s + "\n " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        checkButton.setVisible(true);
        try {
            Image img = ImageIO.read(getClass().getResource("redCross.png"));
            redCrossIcon = img;
            checkButton.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextWizardNavigation(String s) {
        JOptionPane.showMessageDialog(null, s, "Options Incomplete", JOptionPane.WARNING_MESSAGE);
    }

    public void printProgressNotification(String s) {
        System.out.println("\n Proceeding to execute allotments");
        // fV.displayOutputProgressTextArea.concat ("\n Proceeding to execute allotments");
        //displayOutputProgressTextArea.append (s/ + "\n");
    }

//	public static void main (String[] args) throws SQLException, ClassNotFoundException, IOException {
//		defaultSetOfFilesWizard2 jPanelWizard2 = new defaultSetOfFilesWizard2 ();
//	}

//	public defaultSetOfFilesWizard2 (final inputToAllotmentsWizard1 wizard1) {

    public void enableFileDirectoryExistenceCheckButton(JButton currentCheckButton, File file1, String errorMsg) {
        if ((file1.exists()) && (errorMsg == null)) {
            currentCheckButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("greenYes.png"));
                getGreenTickIcon = img;
                currentCheckButton.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ((file1.exists()) && (errorMsg != null)) {
            currentCheckButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("redCross.png"));
                redCrossIcon = img;
                currentCheckButton.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, errorMsg, "Input Format Error", JOptionPane.ERROR_MESSAGE);
        } else {
            currentCheckButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("redCross.png"));
                redCrossIcon = img;
                currentCheckButton.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        jPanelWizard2 = new JPanel();
        jPanelWizard2.setLayout(new FormLayout("fill:473px:grow,left:4dlu:noGrow,fill:292px:noGrow,left:60dlu:noGrow,fill:230px:noGrow,left:9dlu:noGrow,fill:45px:noGrow,left:4dlu:noGrow,fill:134px:noGrow,left:4dlu:noGrow,fill:14px:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:17dlu:noGrow,center:max(d;4px):noGrow,top:15dlu:noGrow,center:34px:noGrow,top:12dlu:noGrow,center:max(d;4px):noGrow,top:10dlu:noGrow,center:max(d;4px):noGrow,top:11dlu:noGrow,center:max(d;4px):noGrow,top:11dlu:noGrow,center:max(d;4px):noGrow,top:13dlu:noGrow,top:21dlu:noGrow,center:14px:noGrow,center:38px:noGrow,top:17dlu:noGrow,center:max(d;4px):noGrow,top:14dlu:noGrow,center:max(d;4px):noGrow,top:13dlu:noGrow,center:34px:noGrow"));
        jPanelWizard2.setBackground(new Color(-14793908));
        Font jPanelWizard2Font = this.$$$getFont$$$(null, -1, 16, jPanelWizard2.getFont());
        if (jPanelWizard2Font != null) jPanelWizard2.setFont(jPanelWizard2Font);
        jPanelWizard2.setForeground(new Color(-131585));
        jLabelToSelectOptionsWizard2 = new JLabel();
        Font jLabelToSelectOptionsWizard2Font = this.$$$getFont$$$("Century Schoolbook L", Font.BOLD, 28, jLabelToSelectOptionsWizard2.getFont());
        if (jLabelToSelectOptionsWizard2Font != null)
            jLabelToSelectOptionsWizard2.setFont(jLabelToSelectOptionsWizard2Font);
        jLabelToSelectOptionsWizard2.setForeground(new Color(-1774964));
        jLabelToSelectOptionsWizard2.setText("Generate student elective allotments....");
        CellConstraints cc = new CellConstraints();
        jPanelWizard2.add(jLabelToSelectOptionsWizard2, cc.xyw(1, 3, 11, CellConstraints.CENTER, CellConstraints.FILL));
        selectedDefaultDirectoryLabelWizard2 = new JLabel();
        Font selectedDefaultDirectoryLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, selectedDefaultDirectoryLabelWizard2.getFont());
        if (selectedDefaultDirectoryLabelWizard2Font != null)
            selectedDefaultDirectoryLabelWizard2.setFont(selectedDefaultDirectoryLabelWizard2Font);
        selectedDefaultDirectoryLabelWizard2.setForeground(new Color(-131585));
        selectedDefaultDirectoryLabelWizard2.setText("Selected Default Directory : ");
        jPanelWizard2.add(selectedDefaultDirectoryLabelWizard2, cc.xy(1, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        selectedDefaultDirTextFieldWizard2 = new JTextField();
        Font selectedDefaultDirTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, selectedDefaultDirTextFieldWizard2.getFont());
        if (selectedDefaultDirTextFieldWizard2Font != null)
            selectedDefaultDirTextFieldWizard2.setFont(selectedDefaultDirTextFieldWizard2Font);
        jPanelWizard2.add(selectedDefaultDirTextFieldWizard2, cc.xyw(3, 5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        courseDetailsSegmentLabelWizard2 = new JLabel();
        Font courseDetailsSegmentLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", Font.BOLD, 18, courseDetailsSegmentLabelWizard2.getFont());
        if (courseDetailsSegmentLabelWizard2Font != null)
            courseDetailsSegmentLabelWizard2.setFont(courseDetailsSegmentLabelWizard2Font);
        courseDetailsSegmentLabelWizard2.setForeground(new Color(-301758));
        courseDetailsSegmentLabelWizard2.setText("       Lists Related to Course Details         ");
        jPanelWizard2.add(courseDetailsSegmentLabelWizard2, cc.xy(1, 7, CellConstraints.RIGHT, CellConstraints.CENTER));
        slotConfigFileLabelWizard2 = new JLabel();
        slotConfigFileLabelWizard2.setEnabled(true);
        slotConfigFileLabelWizard2.setFocusable(true);
        Font slotConfigFileLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, slotConfigFileLabelWizard2.getFont());
        if (slotConfigFileLabelWizard2Font != null) slotConfigFileLabelWizard2.setFont(slotConfigFileLabelWizard2Font);
        slotConfigFileLabelWizard2.setForeground(new Color(-131585));
        slotConfigFileLabelWizard2.setHorizontalAlignment(2);
        slotConfigFileLabelWizard2.setHorizontalTextPosition(2);
        slotConfigFileLabelWizard2.setIconTextGap(2);
        slotConfigFileLabelWizard2.setInheritsPopupMenu(true);
        slotConfigFileLabelWizard2.setText("Select file with time-table slots and timings : ");
        slotConfigFileLabelWizard2.putClientProperty("html.disable", Boolean.FALSE);
        jPanelWizard2.add(slotConfigFileLabelWizard2, cc.xy(1, 9, CellConstraints.RIGHT, CellConstraints.CENTER));
        slotConfigFileTextFieldWizard2 = new JTextField();
        Font slotConfigFileTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, slotConfigFileTextFieldWizard2.getFont());
        if (slotConfigFileTextFieldWizard2Font != null)
            slotConfigFileTextFieldWizard2.setFont(slotConfigFileTextFieldWizard2Font);
        jPanelWizard2.add(slotConfigFileTextFieldWizard2, cc.xyw(3, 9, 3, CellConstraints.FILL, CellConstraints.CENTER));
        slotConfigFileCheckButtonWizard2 = new JButton();
        slotConfigFileCheckButtonWizard2.setBorderPainted(false);
        slotConfigFileCheckButtonWizard2.setContentAreaFilled(false);
        Font slotConfigFileCheckButtonWizard2Font = this.$$$getFont$$$(null, -1, 16, slotConfigFileCheckButtonWizard2.getFont());
        if (slotConfigFileCheckButtonWizard2Font != null)
            slotConfigFileCheckButtonWizard2.setFont(slotConfigFileCheckButtonWizard2Font);
        slotConfigFileCheckButtonWizard2.setText("");
        jPanelWizard2.add(slotConfigFileCheckButtonWizard2, cc.xy(7, 9, CellConstraints.CENTER, CellConstraints.DEFAULT));
        slotConfigFileButtonWizard2 = new JButton();
        slotConfigFileButtonWizard2.setActionCommand("Button");
        slotConfigFileButtonWizard2.setBorderPainted(false);
        slotConfigFileButtonWizard2.setContentAreaFilled(true);
        Font slotConfigFileButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, slotConfigFileButtonWizard2.getFont());
        if (slotConfigFileButtonWizard2Font != null)
            slotConfigFileButtonWizard2.setFont(slotConfigFileButtonWizard2Font);
        slotConfigFileButtonWizard2.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        slotConfigFileButtonWizard2.setIconTextGap(2);
        slotConfigFileButtonWizard2.setLabel("Browse");
        slotConfigFileButtonWizard2.setText("Browse");
        slotConfigFileButtonWizard2.putClientProperty("html.disable", Boolean.FALSE);
        jPanelWizard2.add(slotConfigFileButtonWizard2, cc.xy(9, 9, CellConstraints.CENTER, CellConstraints.CENTER));
        courseDetailsListLabelWizard2 = new JLabel();
        courseDetailsListLabelWizard2.setDoubleBuffered(true);
        courseDetailsListLabelWizard2.setFocusCycleRoot(true);
        courseDetailsListLabelWizard2.setFocusTraversalPolicyProvider(true);
        Font courseDetailsListLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseDetailsListLabelWizard2.getFont());
        if (courseDetailsListLabelWizard2Font != null)
            courseDetailsListLabelWizard2.setFont(courseDetailsListLabelWizard2Font);
        courseDetailsListLabelWizard2.setForeground(new Color(-131585));
        courseDetailsListLabelWizard2.setText("Select course details list ( Courseno, credits, slots, etc ) : ");
        jPanelWizard2.add(courseDetailsListLabelWizard2, cc.xy(1, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        courseDetailsListTextFieldWizard2 = new JTextField();
        Font courseDetailsListTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, courseDetailsListTextFieldWizard2.getFont());
        if (courseDetailsListTextFieldWizard2Font != null)
            courseDetailsListTextFieldWizard2.setFont(courseDetailsListTextFieldWizard2Font);
        jPanelWizard2.add(courseDetailsListTextFieldWizard2, cc.xyw(3, 11, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        courseDetailsListButtonWizard2 = new JButton();
        courseDetailsListButtonWizard2.setBorderPainted(false);
        courseDetailsListButtonWizard2.setContentAreaFilled(true);
        courseDetailsListButtonWizard2.setDoubleBuffered(true);
        courseDetailsListButtonWizard2.setFocusCycleRoot(true);
        courseDetailsListButtonWizard2.setFocusTraversalPolicyProvider(true);
        Font courseDetailsListButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, courseDetailsListButtonWizard2.getFont());
        if (courseDetailsListButtonWizard2Font != null)
            courseDetailsListButtonWizard2.setFont(courseDetailsListButtonWizard2Font);
        courseDetailsListButtonWizard2.setHorizontalAlignment(11);
        courseDetailsListButtonWizard2.setHorizontalTextPosition(11);
        courseDetailsListButtonWizard2.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        courseDetailsListButtonWizard2.setIconTextGap(2);
        courseDetailsListButtonWizard2.setSelected(false);
        courseDetailsListButtonWizard2.setSelectedIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        courseDetailsListButtonWizard2.setText("Browse");
        courseDetailsListButtonWizard2.putClientProperty("hideActionText", Boolean.FALSE);
        courseDetailsListButtonWizard2.putClientProperty("html.disable", Boolean.FALSE);
        jPanelWizard2.add(courseDetailsListButtonWizard2, cc.xy(9, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
        courseDetailsListCheckButtonWizrd2 = new JButton();
        courseDetailsListCheckButtonWizrd2.setBorderPainted(false);
        courseDetailsListCheckButtonWizrd2.setContentAreaFilled(false);
        Font courseDetailsListCheckButtonWizrd2Font = this.$$$getFont$$$(null, -1, 16, courseDetailsListCheckButtonWizrd2.getFont());
        if (courseDetailsListCheckButtonWizrd2Font != null)
            courseDetailsListCheckButtonWizrd2.setFont(courseDetailsListCheckButtonWizrd2Font);
        courseDetailsListCheckButtonWizrd2.setText("");
        jPanelWizard2.add(courseDetailsListCheckButtonWizrd2, cc.xy(7, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
        coursePrefListGenerationLabelWizard2 = new JLabel();
        Font coursePrefListGenerationLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, coursePrefListGenerationLabelWizard2.getFont());
        if (coursePrefListGenerationLabelWizard2Font != null)
            coursePrefListGenerationLabelWizard2.setFont(coursePrefListGenerationLabelWizard2Font);
        coursePrefListGenerationLabelWizard2.setForeground(new Color(-131585));
        coursePrefListGenerationLabelWizard2.setText("Course preference list generation : ");
        jPanelWizard2.add(coursePrefListGenerationLabelWizard2, cc.xy(1, 13, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        coursePrefListGenerationComboBoxWizard2 = new JComboBox();
        coursePrefListGenerationComboBoxWizard2.setDoubleBuffered(true);
        coursePrefListGenerationComboBoxWizard2.setEditable(true);
        coursePrefListGenerationComboBoxWizard2.setFocusCycleRoot(true);
        coursePrefListGenerationComboBoxWizard2.setFocusTraversalPolicyProvider(true);
        Font coursePrefListGenerationComboBoxWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, coursePrefListGenerationComboBoxWizard2.getFont());
        if (coursePrefListGenerationComboBoxWizard2Font != null)
            coursePrefListGenerationComboBoxWizard2.setFont(coursePrefListGenerationComboBoxWizard2Font);
        coursePrefListGenerationComboBoxWizard2.setInheritsPopupMenu(true);
        coursePrefListGenerationComboBoxWizard2.setMaximumRowCount(5);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Choose one from following:");
        defaultComboBoxModel1.addElement("1 - Create new course preference list");
        defaultComboBoxModel1.addElement("2 - Use  existing course preference list");
        coursePrefListGenerationComboBoxWizard2.setModel(defaultComboBoxModel1);
        coursePrefListGenerationComboBoxWizard2.setPopupVisible(false);
        coursePrefListGenerationComboBoxWizard2.setRequestFocusEnabled(true);
        coursePrefListGenerationComboBoxWizard2.putClientProperty("html.disable", Boolean.FALSE);
        jPanelWizard2.add(coursePrefListGenerationComboBoxWizard2, cc.xyw(3, 13, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        selectAnExistingCoursePrefListLabelWizard2 = new JLabel();
        Font selectAnExistingCoursePrefListLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, selectAnExistingCoursePrefListLabelWizard2.getFont());
        if (selectAnExistingCoursePrefListLabelWizard2Font != null)
            selectAnExistingCoursePrefListLabelWizard2.setFont(selectAnExistingCoursePrefListLabelWizard2Font);
        selectAnExistingCoursePrefListLabelWizard2.setForeground(new Color(-131585));
        selectAnExistingCoursePrefListLabelWizard2.setText("Select an existing course preference list : ");
        jPanelWizard2.add(selectAnExistingCoursePrefListLabelWizard2, cc.xy(1, 15, CellConstraints.RIGHT, CellConstraints.CENTER));
        selectAnExistingCoursePrefListTextFieldWizard2 = new JTextField();
        selectAnExistingCoursePrefListTextFieldWizard2.setEnabled(true);
        Font selectAnExistingCoursePrefListTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, selectAnExistingCoursePrefListTextFieldWizard2.getFont());
        if (selectAnExistingCoursePrefListTextFieldWizard2Font != null)
            selectAnExistingCoursePrefListTextFieldWizard2.setFont(selectAnExistingCoursePrefListTextFieldWizard2Font);
        jPanelWizard2.add(selectAnExistingCoursePrefListTextFieldWizard2, cc.xyw(3, 15, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        selectAnExistingCoursePrefListCheckButtonWizard2 = new JButton();
        selectAnExistingCoursePrefListCheckButtonWizard2.setBorderPainted(false);
        selectAnExistingCoursePrefListCheckButtonWizard2.setContentAreaFilled(false);
        Font selectAnExistingCoursePrefListCheckButtonWizard2Font = this.$$$getFont$$$(null, -1, 16, selectAnExistingCoursePrefListCheckButtonWizard2.getFont());
        if (selectAnExistingCoursePrefListCheckButtonWizard2Font != null)
            selectAnExistingCoursePrefListCheckButtonWizard2.setFont(selectAnExistingCoursePrefListCheckButtonWizard2Font);
        selectAnExistingCoursePrefListCheckButtonWizard2.setText("");
        jPanelWizard2.add(selectAnExistingCoursePrefListCheckButtonWizard2, cc.xy(7, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
        selectAnExistingCoursePrefListButtonWizard2 = new JButton();
        selectAnExistingCoursePrefListButtonWizard2.setBorderPainted(false);
        selectAnExistingCoursePrefListButtonWizard2.setContentAreaFilled(true);
        selectAnExistingCoursePrefListButtonWizard2.setDoubleBuffered(true);
        selectAnExistingCoursePrefListButtonWizard2.setEnabled(false);
        selectAnExistingCoursePrefListButtonWizard2.setFocusCycleRoot(true);
        selectAnExistingCoursePrefListButtonWizard2.setFocusTraversalPolicyProvider(true);
        Font selectAnExistingCoursePrefListButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, selectAnExistingCoursePrefListButtonWizard2.getFont());
        if (selectAnExistingCoursePrefListButtonWizard2Font != null)
            selectAnExistingCoursePrefListButtonWizard2.setFont(selectAnExistingCoursePrefListButtonWizard2Font);
        selectAnExistingCoursePrefListButtonWizard2.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        selectAnExistingCoursePrefListButtonWizard2.setIconTextGap(2);
        selectAnExistingCoursePrefListButtonWizard2.setText("Browse");
        jPanelWizard2.add(selectAnExistingCoursePrefListButtonWizard2, cc.xy(9, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentCgpaListLabelWizard2 = new JLabel();
        studentCgpaListLabelWizard2.setBackground(new Color(-8203));
        studentCgpaListLabelWizard2.setEnabled(true);
        Font studentCgpaListLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentCgpaListLabelWizard2.getFont());
        if (studentCgpaListLabelWizard2Font != null)
            studentCgpaListLabelWizard2.setFont(studentCgpaListLabelWizard2Font);
        studentCgpaListLabelWizard2.setForeground(new Color(-131585));
        studentCgpaListLabelWizard2.setText("<html><center>Select students list including regular and <br> honours students ( RollNo, CGPA, MaxCredits ) : </center></html>");
        jPanelWizard2.add(studentCgpaListLabelWizard2, cc.xy(1, 19, CellConstraints.RIGHT, CellConstraints.FILL));
        studentCgpaListTextFieldWizard2 = new JTextField();
        Font studentCgpaListTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, studentCgpaListTextFieldWizard2.getFont());
        if (studentCgpaListTextFieldWizard2Font != null)
            studentCgpaListTextFieldWizard2.setFont(studentCgpaListTextFieldWizard2Font);
        jPanelWizard2.add(studentCgpaListTextFieldWizard2, cc.xyw(3, 19, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        studentCgpaListCheckButtonWizard2 = new JButton();
        studentCgpaListCheckButtonWizard2.setBorderPainted(false);
        studentCgpaListCheckButtonWizard2.setContentAreaFilled(false);
        Font studentCgpaListCheckButtonWizard2Font = this.$$$getFont$$$(null, -1, 16, studentCgpaListCheckButtonWizard2.getFont());
        if (studentCgpaListCheckButtonWizard2Font != null)
            studentCgpaListCheckButtonWizard2.setFont(studentCgpaListCheckButtonWizard2Font);
        studentCgpaListCheckButtonWizard2.setText("");
        jPanelWizard2.add(studentCgpaListCheckButtonWizard2, cc.xy(7, 19, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentCgpaListButtonWizard2 = new JButton();
        studentCgpaListButtonWizard2.setActionCommand("Button");
        studentCgpaListButtonWizard2.setBorderPainted(false);
        studentCgpaListButtonWizard2.setContentAreaFilled(true);
        Font studentCgpaListButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentCgpaListButtonWizard2.getFont());
        if (studentCgpaListButtonWizard2Font != null)
            studentCgpaListButtonWizard2.setFont(studentCgpaListButtonWizard2Font);
        studentCgpaListButtonWizard2.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        studentCgpaListButtonWizard2.setIconTextGap(2);
        studentCgpaListButtonWizard2.setLabel("Browse");
        studentCgpaListButtonWizard2.setText("Browse");
        jPanelWizard2.add(studentCgpaListButtonWizard2, cc.xy(9, 19, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentPreferenceListLabelWizard2 = new JLabel();
        studentPreferenceListLabelWizard2.setAutoscrolls(true);
        studentPreferenceListLabelWizard2.setDoubleBuffered(true);
        studentPreferenceListLabelWizard2.setFocusCycleRoot(true);
        studentPreferenceListLabelWizard2.setFocusTraversalPolicyProvider(true);
        Font studentPreferenceListLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentPreferenceListLabelWizard2.getFont());
        if (studentPreferenceListLabelWizard2Font != null)
            studentPreferenceListLabelWizard2.setFont(studentPreferenceListLabelWizard2Font);
        studentPreferenceListLabelWizard2.setForeground(new Color(-131585));
        studentPreferenceListLabelWizard2.setIconTextGap(0);
        studentPreferenceListLabelWizard2.setInheritsPopupMenu(true);
        studentPreferenceListLabelWizard2.setText("<html> <center> Select student's preferenceList <br> ( RollNo, Courseno, Color, Type, SortOrder ) : </center></html> ");
        studentPreferenceListLabelWizard2.setVerticalAlignment(1);
        studentPreferenceListLabelWizard2.setVerticalTextPosition(3);
        jPanelWizard2.add(studentPreferenceListLabelWizard2, cc.xy(1, 21, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        studentPreferenceListTextFieldWizard2 = new JTextField();
        Font studentPreferenceListTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, studentPreferenceListTextFieldWizard2.getFont());
        if (studentPreferenceListTextFieldWizard2Font != null)
            studentPreferenceListTextFieldWizard2.setFont(studentPreferenceListTextFieldWizard2Font);
        jPanelWizard2.add(studentPreferenceListTextFieldWizard2, cc.xyw(3, 21, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        studentPrefListCheckButtonWizard2 = new JButton();
        studentPrefListCheckButtonWizard2.setBorderPainted(false);
        studentPrefListCheckButtonWizard2.setContentAreaFilled(false);
        Font studentPrefListCheckButtonWizard2Font = this.$$$getFont$$$(null, -1, 16, studentPrefListCheckButtonWizard2.getFont());
        if (studentPrefListCheckButtonWizard2Font != null)
            studentPrefListCheckButtonWizard2.setFont(studentPrefListCheckButtonWizard2Font);
        studentPrefListCheckButtonWizard2.setText("");
        jPanelWizard2.add(studentPrefListCheckButtonWizard2, cc.xy(7, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentPrefListButtonWizard2 = new JButton();
        studentPrefListButtonWizard2.setAutoscrolls(true);
        studentPrefListButtonWizard2.setBorderPainted(false);
        studentPrefListButtonWizard2.setContentAreaFilled(true);
        studentPrefListButtonWizard2.setFocusCycleRoot(true);
        studentPrefListButtonWizard2.setFocusTraversalPolicyProvider(true);
        studentPrefListButtonWizard2.setFocusable(true);
        Font studentPrefListButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, studentPrefListButtonWizard2.getFont());
        if (studentPrefListButtonWizard2Font != null)
            studentPrefListButtonWizard2.setFont(studentPrefListButtonWizard2Font);
        studentPrefListButtonWizard2.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        studentPrefListButtonWizard2.setIconTextGap(2);
        studentPrefListButtonWizard2.setSelected(false);
        studentPrefListButtonWizard2.setText("Browse");
        jPanelWizard2.add(studentPrefListButtonWizard2, cc.xy(9, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
        selectHighPriorityStudentsListLabelWizard2 = new JLabel();
        selectHighPriorityStudentsListLabelWizard2.setDoubleBuffered(true);
        selectHighPriorityStudentsListLabelWizard2.setFocusCycleRoot(true);
        selectHighPriorityStudentsListLabelWizard2.setFocusTraversalPolicyProvider(true);
        Font selectHighPriorityStudentsListLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, selectHighPriorityStudentsListLabelWizard2.getFont());
        if (selectHighPriorityStudentsListLabelWizard2Font != null)
            selectHighPriorityStudentsListLabelWizard2.setFont(selectHighPriorityStudentsListLabelWizard2Font);
        selectHighPriorityStudentsListLabelWizard2.setForeground(new Color(-131585));
        selectHighPriorityStudentsListLabelWizard2.setText("<html> <center> Select file with list of high priority students <br> ( Course, Batch ) : </center></html>");
        jPanelWizard2.add(selectHighPriorityStudentsListLabelWizard2, cc.xy(1, 23, CellConstraints.RIGHT, CellConstraints.CENTER));
        selectHighPriorityStudentsListTextFieldWizard2 = new JTextField();
        selectHighPriorityStudentsListTextFieldWizard2.setDoubleBuffered(true);
        selectHighPriorityStudentsListTextFieldWizard2.setDragEnabled(true);
        selectHighPriorityStudentsListTextFieldWizard2.setFocusCycleRoot(true);
        selectHighPriorityStudentsListTextFieldWizard2.setFocusTraversalPolicyProvider(true);
        Font selectHighPriorityStudentsListTextFieldWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, selectHighPriorityStudentsListTextFieldWizard2.getFont());
        if (selectHighPriorityStudentsListTextFieldWizard2Font != null)
            selectHighPriorityStudentsListTextFieldWizard2.setFont(selectHighPriorityStudentsListTextFieldWizard2Font);
        selectHighPriorityStudentsListTextFieldWizard2.setInheritsPopupMenu(true);
        jPanelWizard2.add(selectHighPriorityStudentsListTextFieldWizard2, cc.xyw(3, 23, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        selectHighPriorityStudentsListCheckButtonWizard2 = new JButton();
        selectHighPriorityStudentsListCheckButtonWizard2.setBorderPainted(false);
        selectHighPriorityStudentsListCheckButtonWizard2.setContentAreaFilled(false);
        Font selectHighPriorityStudentsListCheckButtonWizard2Font = this.$$$getFont$$$(null, -1, 16, selectHighPriorityStudentsListCheckButtonWizard2.getFont());
        if (selectHighPriorityStudentsListCheckButtonWizard2Font != null)
            selectHighPriorityStudentsListCheckButtonWizard2.setFont(selectHighPriorityStudentsListCheckButtonWizard2Font);
        selectHighPriorityStudentsListCheckButtonWizard2.setText("");
        jPanelWizard2.add(selectHighPriorityStudentsListCheckButtonWizard2, cc.xy(7, 23, CellConstraints.CENTER, CellConstraints.DEFAULT));
        selectHighPriorityStudentsListButtonWizard2 = new JButton();
        selectHighPriorityStudentsListButtonWizard2.setBorderPainted(false);
        selectHighPriorityStudentsListButtonWizard2.setContentAreaFilled(true);
        selectHighPriorityStudentsListButtonWizard2.setDoubleBuffered(true);
        selectHighPriorityStudentsListButtonWizard2.setFocusCycleRoot(true);
        selectHighPriorityStudentsListButtonWizard2.setFocusTraversalPolicyProvider(true);
        Font selectHighPriorityStudentsListButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, selectHighPriorityStudentsListButtonWizard2.getFont());
        if (selectHighPriorityStudentsListButtonWizard2Font != null)
            selectHighPriorityStudentsListButtonWizard2.setFont(selectHighPriorityStudentsListButtonWizard2Font);
        selectHighPriorityStudentsListButtonWizard2.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        selectHighPriorityStudentsListButtonWizard2.setIconTextGap(2);
        selectHighPriorityStudentsListButtonWizard2.setInheritsPopupMenu(true);
        selectHighPriorityStudentsListButtonWizard2.setText("Browse");
        jPanelWizard2.add(selectHighPriorityStudentsListButtonWizard2, cc.xy(9, 23, CellConstraints.CENTER, CellConstraints.DEFAULT));
        studentDetailsSegmentLabelWizard2 = new JLabel();
        Font studentDetailsSegmentLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", Font.BOLD, 18, studentDetailsSegmentLabelWizard2.getFont());
        if (studentDetailsSegmentLabelWizard2Font != null)
            studentDetailsSegmentLabelWizard2.setFont(studentDetailsSegmentLabelWizard2Font);
        studentDetailsSegmentLabelWizard2.setForeground(new Color(-301758));
        studentDetailsSegmentLabelWizard2.setText("      Lists Related to Student Details         ");
        jPanelWizard2.add(studentDetailsSegmentLabelWizard2, cc.xy(1, 17, CellConstraints.RIGHT, CellConstraints.CENTER));
        prevButtonWizard2 = new JButton();
        prevButtonWizard2.setBorderPainted(false);
        prevButtonWizard2.setContentAreaFilled(true);
        prevButtonWizard2.setEnabled(false);
        Font prevButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 14, prevButtonWizard2.getFont());
        if (prevButtonWizard2Font != null) prevButtonWizard2.setFont(prevButtonWizard2Font);
        prevButtonWizard2.setText("<< Prev");
        jPanelWizard2.add(prevButtonWizard2, cc.xy(3, 25, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        nextButtonWizard2 = new JButton();
        nextButtonWizard2.setBorderPainted(false);
        Font nextButtonWizard2Font = this.$$$getFont$$$("Century Schoolbook L", -1, 14, nextButtonWizard2.getFont());
        if (nextButtonWizard2Font != null) nextButtonWizard2.setFont(nextButtonWizard2Font);
        nextButtonWizard2.setText("Next >>");
        jPanelWizard2.add(nextButtonWizard2, cc.xy(5, 25, CellConstraints.LEFT, CellConstraints.DEFAULT));
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
        return jPanelWizard2;
    }
}
