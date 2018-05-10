package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import main.ExecuteStepsForAllotment;
import services.CheckInputFormats;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static javax.swing.JFileChooser.FILES_ONLY;

public class defaultSetOfFilesWizard3 extends JFrame {
    public JLabel jLabelToSelectOptionsWizard3;
    public JLabel selectedDefaultDirectoryLabelWizard3;
    public JLabel courseDetailsSegmentLabelWizard2;
    public JTextField selectedDefaultDirTextFieldWizard3;
    public JButton insideDepartmentCheckButtonWizard3;
    public JButton insideDepartmentBrowseButtonWizard3;
    public JTextField insideDepartmentTextFieldWizard3;
    public JLabel insideDepartmentLabelWizard3;
    public JLabel batchwiseMandatedElectivesLabelWizard3;
    public JTextField batchwiseMandateElectivesTextFieldWizard3;
    public JButton bathcwiseMandatedElectivesCheckButtonWizard3;
    public JButton batchwiseMandatedElectivesBrowseButtonWizard3;
    public JComboBox selectAlgorithmTypeComboBoxWizard3;
    public JButton generateAllotmentsButtonWizard3;
    public JLabel batchwiseCreditLimitLabelWizard3;
    public JTextField batchwiseCreditLimitTextFieldWizard3;
    public JButton batchwiseCreditLimitCheckButtonWizard3;
    public JButton batchwiseCreditLimitBrowseButtonWizard3;
    public JLabel selectAlgorithmTypeLabelWizard3;
    public JLabel outputScreenLabelWizard3;
    public JButton prevButtonWizard3;
    public JPanel JPanelWizard3;
    public JButton analyseAllotmentsButton;
    public JTextArea displayOutputProgressTextArea;

//	private inputToAllotmentsWizard1 wizard1;
//	private defaultSetOfFilesWizard2 wizard2;

    formValues fV = formValues.getInstance();

    JFileChooser chosenDirWizard3 = new JFileChooser();
    String defaultDir, outputDir;
    String chosenTitle;
    String errorMsg;
    public Image redCrossIcon, getGreenTickIcon;
    public int algorithmIndex;
    public String algorithmName;

    public void printMessage(String s) {
        System.out.println(s);
        displayOutputProgressTextArea.append(s + "\n");
    }

    public void printErrorMessage(String s) {
        System.out.println(s);
        displayOutputProgressTextArea.append("\n" + s);
        JOptionPane.showMessageDialog(null, s + "\n " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void printProgressNotification(String s) {
        displayOutputProgressTextArea.append(s + "\n");
    }

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

//	public static void main (String[] args) throws SQLException, ClassNotFoundException, IOException {
//		defaultSetOfFilesWizard3 JPanelWizard3 = new defaultSetOfFilesWizard3 ();
//	}

//	public defaultSetOfFilesWizard3 currentClass;
//
//	public void setCurrentClass (defaultSetOfFilesWizard3 currentClass) {
//		this.currentClass = currentClass;
//	}

    public defaultSetOfFilesWizard3() {

        super();
        $$$setupUI$$$();

        //Dimension dim = Toolkit.getDefaultToolkit ().getScreenSize ();
        //this.setLocation (dim.width / 4 - this.getSize ().width / 2, dim.height / 4 - this.getSize ().height / 2);
        this.setSize(1500, 800);
        getContentPane().add(JPanelWizard3);
        setVisible(true);
        setTitle("IITM-SEAT");

        JScrollPane scrPaneWizard3 = new JScrollPane(JPanelWizard3);
        add(scrPaneWizard3);

        JPanelWizard3.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        // Initially set all check buttons invisible
        insideDepartmentCheckButtonWizard3.setVisible(false);
        batchwiseCreditLimitCheckButtonWizard3.setVisible(false);
        bathcwiseMandatedElectivesCheckButtonWizard3.setVisible(false);

        // Row 1: Selected Default Directory
        formValues fV = formValues.getInstance();
        selectedDefaultDirTextFieldWizard3.setText(formValues.getDefaultDirPath());
        chosenDirWizard3.setCurrentDirectory(new File(formValues.getDefaultDirPath()));
        //selectedDefaultDirTextFieldWizard3.setText ("/home/saranya/Desktop/SEAT_Project/jan-may-2018/round1/28NovAllotment1500PM");
        //chosenDirWizard3.setCurrentDirectory (new File (selectedDefaultDirTextFieldWizard3.getText ()));
        chosenDirWizard3.setDialogTitle(chosenTitle);
        chosenDirWizard3.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // Connect wizard 2 with prev button
        prevButtonWizard3.setEnabled(true);
        prevButtonWizard3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultSetOfFilesWizard2 wizard2 = new defaultSetOfFilesWizard2();
                wizard2.setVisible(true);
                dispose();
            }
        });

        // Row 2: insideDepartment details - Component 1 : Browse button component clickActionListener
        defaultDir = selectedDefaultDirTextFieldWizard3.getText();
        insideDepartmentTextFieldWizard3.setText(defaultDir + "/" + "insideDepartmentSpecification.csv");
        final String insideDeptFileName = insideDepartmentTextFieldWizard3.getText();
        final File insideDeptFile = new File(insideDeptFileName);
        enableFileDirectoryExistenceCheckButton(insideDepartmentCheckButtonWizard3, insideDeptFile, errorMsg);
        insideDepartmentBrowseButtonWizard3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirWizard3.getCurrentDirectory());
                jfc.setFileSelectionMode(FILES_ONLY);
                int insideDeptFileFlag = jfc.showOpenDialog(null);
                if (insideDeptFileFlag == JFileChooser.APPROVE_OPTION) {
                    File insideDeptFile = jfc.getSelectedFile();
                    insideDepartmentTextFieldWizard3.setText(insideDeptFile.getAbsolutePath());
                    String insideDeptFileName = insideDepartmentTextFieldWizard3.getText();
                    formValues.setInsideDepartmentDetailsListNameWithPath(insideDeptFileName);
                    // Check whether the default config file exists or not.
                    errorMsg = CheckInputFormats.checkInsideDepartmentConfigFileFormat(insideDeptFileName);
                    if (errorMsg != null) {
                        printErrorMessage("\nExiting due to error in the format of the slots file. " + errorMsg);
                        enableFileDirectoryExistenceCheckButton(insideDepartmentCheckButtonWizard3, insideDeptFile, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(insideDepartmentCheckButtonWizard3, insideDeptFile, errorMsg);
                    }
                }
            }
        });

        // Row 2: insideDepartment details - Component 2: Text field of inside department details field enterKeyListener
        insideDepartmentTextFieldWizard3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String insideDeptFileName = insideDepartmentTextFieldWizard3.getText();
                    formValues.setInsideDepartmentDetailsListNameWithPath(insideDeptFileName);
                    File insideDeptFile = new File(insideDeptFileName);
                    errorMsg = CheckInputFormats.checkInsideDepartmentConfigFileFormat(insideDeptFileName);
                    if (errorMsg != null) {
                        printErrorMessage("\nExiting due to error in the format of inside department details list. " + errorMsg);
                        enableFileDirectoryExistenceCheckButton(insideDepartmentCheckButtonWizard3, insideDeptFile, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(insideDepartmentCheckButtonWizard3, insideDeptFile, errorMsg);
                    }
                    //System.out.println ("KeyEvent Captured");
                }
                super.keyPressed(e);
            }
        });

        // Row 3: batch-wise mandatedElectives list - Component 1 : Browse button component clickActionListener
        batchwiseMandateElectivesTextFieldWizard3.setText(defaultDir + "/" + "batchSpecificMandatedElectives.csv");
        final String mandatedElectivesFileName = batchwiseMandateElectivesTextFieldWizard3.getText();
        final File mandatedElectivesFile = new File(mandatedElectivesFileName);
        enableFileDirectoryExistenceCheckButton(bathcwiseMandatedElectivesCheckButtonWizard3, mandatedElectivesFile, errorMsg);
        batchwiseMandatedElectivesBrowseButtonWizard3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirWizard3.getCurrentDirectory());
                jfc.setFileSelectionMode(FILES_ONLY);
                int mandatedElectivesFileFlag = jfc.showOpenDialog(null);
                if (mandatedElectivesFileFlag == JFileChooser.APPROVE_OPTION) {
                    File mandatedElectivesFile = jfc.getSelectedFile();
                    batchwiseMandateElectivesTextFieldWizard3.setText(mandatedElectivesFile.getAbsolutePath());
                    String mandatedElectivesFileName = batchwiseMandateElectivesTextFieldWizard3.getText();
                    formValues.setMandatedElectivesListNameWithPath(mandatedElectivesFileName);
                    // Check whether the default config file exists or not.
                    errorMsg = CheckInputFormats.checkBatchSpecificMadatedElectivesFileFormat(mandatedElectivesFileName);
                    if (errorMsg != null) {
                        printErrorMessage("\n Exiting due to error in the format of batch-wise mandated electives list \n " + errorMsg);
                        enableFileDirectoryExistenceCheckButton(bathcwiseMandatedElectivesCheckButtonWizard3, mandatedElectivesFile, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(bathcwiseMandatedElectivesCheckButtonWizard3, mandatedElectivesFile, errorMsg);
                    }
                }
            }
        });

        // Row 3: batch-wise mandatedElectives list - Component 2: Text field of inside department details field enterKeyListener
        batchwiseMandateElectivesTextFieldWizard3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String mandatedElectivesFile = batchwiseMandateElectivesTextFieldWizard3.getText();
                    File mandatedElectivesFilePath = new File(mandatedElectivesFile);
                    formValues.setMandatedElectivesListNameWithPath(mandatedElectivesFile);
                    // Check whether the default config file exists or not.
                    errorMsg = CheckInputFormats.checkBatchSpecificMadatedElectivesFileFormat(mandatedElectivesFile);
                    if (errorMsg != null) {
                        printErrorMessage("\nExiting due to error in the format of batch-wise mandated electives list \n " + errorMsg);
                        enableFileDirectoryExistenceCheckButton(bathcwiseMandatedElectivesCheckButtonWizard3, mandatedElectivesFilePath, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(bathcwiseMandatedElectivesCheckButtonWizard3, mandatedElectivesFilePath, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });

        // Row 4: batch-wise creditLimits detailsList - Component 1 : Browse button component clickActionListener
        batchwiseCreditLimitTextFieldWizard3.setText(defaultDir + "/" + "maxCreditLimits.csv");
        final String creditLimitFileName = batchwiseCreditLimitTextFieldWizard3.getText();
        final File creditLimitFile = new File(creditLimitFileName);
        enableFileDirectoryExistenceCheckButton(batchwiseCreditLimitCheckButtonWizard3, creditLimitFile, errorMsg);
        batchwiseCreditLimitBrowseButtonWizard3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(chosenDirWizard3.getCurrentDirectory());
                jfc.setFileSelectionMode(FILES_ONLY);
                int creditLimitFileFlag = jfc.showOpenDialog(null);
                if (creditLimitFileFlag == JFileChooser.APPROVE_OPTION) {
                    File creditLimitFile = jfc.getSelectedFile();
                    batchwiseCreditLimitTextFieldWizard3.setText(creditLimitFile.getAbsolutePath());
                    String creditLimitFileName = batchwiseCreditLimitTextFieldWizard3.getText();
                    formValues.setBatchwiseCreditLimitFileNameWithPath(creditLimitFileName);
                    creditLimitFile = new File(creditLimitFileName);
                    // Check whether the default config file exists or not.
                    errorMsg = CheckInputFormats.checkDepartmentWiseMaxCreditLimitFileFormat(creditLimitFileName);
                    if (errorMsg != null) {
                        printErrorMessage("\nExiting due to error in the format of batch-wise mandated electives list \n " + errorMsg);
                        enableFileDirectoryExistenceCheckButton(batchwiseCreditLimitCheckButtonWizard3, creditLimitFile, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(batchwiseCreditLimitCheckButtonWizard3, creditLimitFile, errorMsg);
                    }
                }
            }
        });

        // Row 4: batch-wise creditLimits detailsList - Component 2: Text field of inside department details field enterKeyListener
        batchwiseCreditLimitTextFieldWizard3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String creditLimitFileName = batchwiseCreditLimitTextFieldWizard3.getText();
                    File creditLimitFile = new File(creditLimitFileName);
                    formValues.setBatchwiseCreditLimitFileNameWithPath(creditLimitFileName);
                    // Check whether the default config file exists or not.
                    errorMsg = CheckInputFormats.checkDepartmentWiseMaxCreditLimitFileFormat(creditLimitFileName);
                    if (errorMsg != null) {
                        printErrorMessage("\nExiting due to error in the format of batch-wise mandated electives list \n " + errorMsg);
                        enableFileDirectoryExistenceCheckButton(batchwiseCreditLimitCheckButtonWizard3, creditLimitFile, errorMsg);
                    } else {
                        enableFileDirectoryExistenceCheckButton(batchwiseCreditLimitCheckButtonWizard3, creditLimitFile, errorMsg);
                    }
                }
                super.keyPressed(e);
            }
        });

        // Row 5: algorithmSelection ComboBox - Component 1 : dropDown button component clickActionListener
        selectAlgorithmTypeComboBoxWizard3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                algorithmIndex = selectAlgorithmTypeComboBoxWizard3.getSelectedIndex();
                if (algorithmIndex == 2) {
                    algorithmName = "FirstPreference";
                    outputDir = defaultDir + "/" + algorithmName;
                    generateAllotmentsButtonWizard3.setEnabled(true);
                } else if (algorithmIndex == 1) {
                    algorithmName = "IterativeHR";
                    outputDir = defaultDir + "/" + algorithmName;
                    generateAllotmentsButtonWizard3.setEnabled(true);
                } else if (algorithmIndex == 3) {
                    algorithmName = "SlotBasedHeuristic1";
                    outputDir = defaultDir + "/" + algorithmName;
                    generateAllotmentsButtonWizard3.setEnabled(true);
                } else if (algorithmIndex == 4) {
                    algorithmName = "SlotBasedHeuristic1";
                    outputDir = defaultDir + "/" + algorithmName;
                    generateAllotmentsButtonWizard3.setEnabled(true);
                } else {
                    generateAllotmentsButtonWizard3.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "\n Please choose one algorithm type from the drop box at 5-th row \n ", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Row 6: Component 1: Previous Button clickActionListener
        prevButtonWizard3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultSetOfFilesWizard2 wizard2 = new defaultSetOfFilesWizard2();
                wizard2.setVisible(true);
                dispose();
            }
        });

        // Row 6: Component 2: Generate Allotments Button clickActionListener
        generateAllotmentsButtonWizard3.setEnabled(true);
        generateAllotmentsButtonWizard3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String slotConfigFileName = formValues.getSlotConfigFileName();
                String studentListFile = formValues.getStudentCGPAFileNameWithPath();
                System.out.println("coursePreferenceListFile: " + studentListFile);
                String courseListFile = formValues.getCourseDetailsFileNameWithPath();
                String studentPreferenceListFile = formValues.getStudentPreferenceListNameWithPath();
                String coursePreferenceListFile;
                boolean generateFreshCoursePreferences = formValues.getUsingExistingCoursePreferenceListFlag();
                if (generateFreshCoursePreferences == false) {
                    coursePreferenceListFile = formValues.getExistingCoursePreferenceFileNameWithPath();
                    System.out.println("false - use existing coursePreferenceListFile from: " + coursePreferenceListFile);
                } else {
                    coursePreferenceListFile = "coursePreferenceList.csv";
                    System.out.println("true - create new coursePreferenceListFile in folder: " + coursePreferenceListFile);
                }
                String highPriorityCoursePreferencesConfigFile = formValues.getHighPriorityStudentDetailsFileNameWithPath();
                String insideDepartmentDetailsFile = insideDeptFileName;
                String departmentWiseMaxCreditLimitFile = creditLimitFileName;
                String batchSpecificMandatedElectivesFile = mandatedElectivesFileName;

                if (insideDeptFile.exists() && mandatedElectivesFile.exists() && creditLimitFile.exists() && (algorithmIndex != 0)) {
                    // String slotsFile, String studentListFile, String courseListFile, String studentPreferenceListFile, String coursePreferenceListFile, boolean generateFreshCoursePreferences, int algorithm, String outputFolder, String insideDepartmentConfigFile, String highPriorityCoursePreferencesConfigFile, String departmentWiseMaxCreditLimitFile, String batchSpecificMandatedElectivesFile, boolean GUIpresent
                    System.out.println("ConfigFileName: " + slotConfigFileName + "\n StudentList :" + studentListFile + "\n courseList :" + courseListFile + "\n studentPreferenceList :" + studentPreferenceListFile + "\n coursePrefList :" + coursePreferenceListFile + "\n coursePrefOption :" + generateFreshCoursePreferences + "\n algorithmIndex :" + String.valueOf(algorithmIndex) + "\n outputDir :" + outputDir + "\n InsideDeptFile :" + insideDepartmentDetailsFile + "\n highPriorityList :" + highPriorityCoursePreferencesConfigFile + "\n maxCreditLimitFile :" + departmentWiseMaxCreditLimitFile + "\n mandatedElecFile :" + batchSpecificMandatedElectivesFile + "\n GUIFlag :" + true + "\n \n \n");
                    try {
                        //////// String slotsFile, String studentListFile, String courseListFile, String studentPreferenceListFile, String coursePreferenceListFile, boolean generateFreshCoursePreferences, int algorithm, String outputFolder, String insideDepartmentConfigFile, String highPriorityCoursePreferencesConfigFile, String departmentWiseMaxCreditLimitFile, String batchSpecificMandatedElectivesFile, boolean GUIpresent
                        displayOutputProgressTextArea.append("\n ======================================================= \n");
                        displayOutputProgressTextArea.append("\n Set of Input Files and Options");
                        displayOutputProgressTextArea.append("\n ---------------------------------------");
                        displayOutputProgressTextArea.append("\n \n Proceeding to execute the algorithm \n");
                        displayOutputProgressTextArea.append("\n ConfigFileName: " + slotConfigFileName + "\n StudentList :" + studentListFile + "\n courseList :" + courseListFile + "\n studentPreferenceList :" + studentPreferenceListFile + "\n coursePrefList :" + coursePreferenceListFile + "\n coursePrefOption :" + generateFreshCoursePreferences + "\n algorithmIndex :" + String.valueOf(algorithmIndex) + "\n outputDir :" + outputDir + "\n InsideDeptFile :" + insideDepartmentDetailsFile + "\n highPriorityList :" + highPriorityCoursePreferencesConfigFile + "\n maxCreditLimitFile :" + departmentWiseMaxCreditLimitFile + "\n mandatedElecFile :" + batchSpecificMandatedElectivesFile + "\n GUIFlag :" + true + "\n");
                        displayOutputProgressTextArea.append("\n \n Proceeding to execute the algorithm \n");
                        // From without GUI code
                        // ExecuteStepsForAllotment.executeAllotmentSteps(slotsFile,studentListFile,courseListFile,studentPreferenceListFile,coursePreferenceListFile,generateFreshCoursePreferences,algorithm,outputFolder,insideDepartmentConfigFile,highPriorityCoursePreferencesConfigFile,departmentWiseMaxCreditLimitFile,batchSpecificMandatedElectivesFile,false,wizard3);
                        ExecuteStepsForAllotment.executeAllotmentSteps(slotConfigFileName, studentListFile, courseListFile, studentPreferenceListFile, coursePreferenceListFile, generateFreshCoursePreferences, algorithmIndex, outputDir, insideDepartmentDetailsFile, highPriorityCoursePreferencesConfigFile, departmentWiseMaxCreditLimitFile, batchSpecificMandatedElectivesFile, true, defaultSetOfFilesWizard3.this);
                        displayOutputProgressTextArea.append("\n \n Allotments generated successfully...! \n");
                        displayOutputProgressTextArea.append("\n ======================================================= \n");
                        JOptionPane.showMessageDialog(null, "Allotments generated successfully...! \n Outputs are stored in \n  " + outputDir, "Allotment Generation", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e1) {
                        System.out.println("\n Inside Catch ....!");
                        e1.printStackTrace();
                    }
                } else {
                    generateAllotmentsButtonWizard3.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "\n Please choose one algorithm type from the drop box at 5-th row \n ", "Error", JOptionPane.ERROR_MESSAGE);  //selectAlgorithmTypeComboBoxWizard3.addItemListener (listener);
                }
            }
        });

        // Row 6: Component 3: Analyse Allotments Button clickActionListener
        analyseAllotmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formValues.getAnalyseAllotmentDirPath();
                analyzeAllotmentsWizard4 wizard4 = new analyzeAllotmentsWizard4();
                wizard4.setVisible(true);
                dispose();
            }
        });
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
        JPanelWizard3 = new JPanel();
        JPanelWizard3.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:492px:grow,fill:39px:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:338px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:5dlu:noGrow,fill:40px:noGrow,left:4dlu:noGrow,fill:111px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:8dlu:noGrow,center:d:noGrow,top:9dlu:noGrow,center:36px:noGrow,top:6dlu:noGrow,center:74px:noGrow,top:9dlu:noGrow,center:max(d;4px):noGrow,top:9dlu:noGrow,center:max(d;4px):noGrow,top:8dlu:noGrow,center:43px:noGrow,top:8dlu:noGrow,center:49px:noGrow,top:8dlu:noGrow,center:38px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:14px:noGrow"));
        JPanelWizard3.setBackground(new Color(-14793908));
        JPanelWizard3.setForeground(new Color(-131585));
        selectedDefaultDirectoryLabelWizard3 = new JLabel();
        Font selectedDefaultDirectoryLabelWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.PLAIN, 17, selectedDefaultDirectoryLabelWizard3.getFont());
        if (selectedDefaultDirectoryLabelWizard3Font != null)
            selectedDefaultDirectoryLabelWizard3.setFont(selectedDefaultDirectoryLabelWizard3Font);
        selectedDefaultDirectoryLabelWizard3.setForeground(new Color(-131585));
        selectedDefaultDirectoryLabelWizard3.setText("Selected Default Directory : ");
        CellConstraints cc = new CellConstraints();
        JPanelWizard3.add(selectedDefaultDirectoryLabelWizard3, cc.xy(3, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        insideDepartmentLabelWizard3 = new JLabel();
        Font insideDepartmentLabelWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.PLAIN, 17, insideDepartmentLabelWizard3.getFont());
        if (insideDepartmentLabelWizard3Font != null)
            insideDepartmentLabelWizard3.setFont(insideDepartmentLabelWizard3Font);
        insideDepartmentLabelWizard3.setForeground(new Color(-131585));
        insideDepartmentLabelWizard3.setText("<html> <p align=\"right\"> List of course-wise inside <br> department specification <br> ( CourseNumber, DepartmentYear ) :  </p></html>");
        insideDepartmentLabelWizard3.setDisplayedMnemonic('Y');
        insideDepartmentLabelWizard3.setDisplayedMnemonicIndex(113);
        JPanelWizard3.add(insideDepartmentLabelWizard3, cc.xy(3, 9, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        selectedDefaultDirTextFieldWizard3 = new JTextField();
        Font selectedDefaultDirTextFieldWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, selectedDefaultDirTextFieldWizard3.getFont());
        if (selectedDefaultDirTextFieldWizard3Font != null)
            selectedDefaultDirTextFieldWizard3.setFont(selectedDefaultDirTextFieldWizard3Font);
        selectedDefaultDirTextFieldWizard3.setText("");
        JPanelWizard3.add(selectedDefaultDirTextFieldWizard3, cc.xyw(5, 5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        insideDepartmentCheckButtonWizard3 = new JButton();
        insideDepartmentCheckButtonWizard3.setBorderPainted(false);
        insideDepartmentCheckButtonWizard3.setContentAreaFilled(false);
        Font insideDepartmentCheckButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, insideDepartmentCheckButtonWizard3.getFont());
        if (insideDepartmentCheckButtonWizard3Font != null)
            insideDepartmentCheckButtonWizard3.setFont(insideDepartmentCheckButtonWizard3Font);
        insideDepartmentCheckButtonWizard3.setText("");
        JPanelWizard3.add(insideDepartmentCheckButtonWizard3, cc.xy(11, 9, CellConstraints.CENTER, CellConstraints.DEFAULT));
        insideDepartmentBrowseButtonWizard3 = new JButton();
        insideDepartmentBrowseButtonWizard3.setActionCommand("Button");
        insideDepartmentBrowseButtonWizard3.setBorderPainted(false);
        Font insideDepartmentBrowseButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, insideDepartmentBrowseButtonWizard3.getFont());
        if (insideDepartmentBrowseButtonWizard3Font != null)
            insideDepartmentBrowseButtonWizard3.setFont(insideDepartmentBrowseButtonWizard3Font);
        insideDepartmentBrowseButtonWizard3.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        insideDepartmentBrowseButtonWizard3.setIconTextGap(2);
        insideDepartmentBrowseButtonWizard3.setLabel("Browse");
        insideDepartmentBrowseButtonWizard3.setText("Browse");
        insideDepartmentBrowseButtonWizard3.putClientProperty("html.disable", Boolean.FALSE);
        JPanelWizard3.add(insideDepartmentBrowseButtonWizard3, cc.xy(13, 9, CellConstraints.CENTER, CellConstraints.CENTER));
        batchwiseMandatedElectivesLabelWizard3 = new JLabel();
        Font batchwiseMandatedElectivesLabelWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.PLAIN, 17, batchwiseMandatedElectivesLabelWizard3.getFont());
        if (batchwiseMandatedElectivesLabelWizard3Font != null)
            batchwiseMandatedElectivesLabelWizard3.setFont(batchwiseMandatedElectivesLabelWizard3Font);
        batchwiseMandatedElectivesLabelWizard3.setForeground(new Color(-131585));
        batchwiseMandatedElectivesLabelWizard3.setText("<html> <center> List of batch-wise mandated electives <br> ( BatchName, MandatedElectives ) :  </center></html>");
        JPanelWizard3.add(batchwiseMandatedElectivesLabelWizard3, cc.xy(3, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        batchwiseMandateElectivesTextFieldWizard3 = new JTextField();
        Font batchwiseMandateElectivesTextFieldWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, batchwiseMandateElectivesTextFieldWizard3.getFont());
        if (batchwiseMandateElectivesTextFieldWizard3Font != null)
            batchwiseMandateElectivesTextFieldWizard3.setFont(batchwiseMandateElectivesTextFieldWizard3Font);
        batchwiseMandateElectivesTextFieldWizard3.setText("");
        JPanelWizard3.add(batchwiseMandateElectivesTextFieldWizard3, cc.xyw(5, 11, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        bathcwiseMandatedElectivesCheckButtonWizard3 = new JButton();
        bathcwiseMandatedElectivesCheckButtonWizard3.setBorderPainted(false);
        bathcwiseMandatedElectivesCheckButtonWizard3.setContentAreaFilled(false);
        Font bathcwiseMandatedElectivesCheckButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, bathcwiseMandatedElectivesCheckButtonWizard3.getFont());
        if (bathcwiseMandatedElectivesCheckButtonWizard3Font != null)
            bathcwiseMandatedElectivesCheckButtonWizard3.setFont(bathcwiseMandatedElectivesCheckButtonWizard3Font);
        bathcwiseMandatedElectivesCheckButtonWizard3.setText("");
        JPanelWizard3.add(bathcwiseMandatedElectivesCheckButtonWizard3, cc.xy(11, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
        batchwiseMandatedElectivesBrowseButtonWizard3 = new JButton();
        batchwiseMandatedElectivesBrowseButtonWizard3.setActionCommand("Button");
        batchwiseMandatedElectivesBrowseButtonWizard3.setBorderPainted(false);
        Font batchwiseMandatedElectivesBrowseButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, batchwiseMandatedElectivesBrowseButtonWizard3.getFont());
        if (batchwiseMandatedElectivesBrowseButtonWizard3Font != null)
            batchwiseMandatedElectivesBrowseButtonWizard3.setFont(batchwiseMandatedElectivesBrowseButtonWizard3Font);
        batchwiseMandatedElectivesBrowseButtonWizard3.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        batchwiseMandatedElectivesBrowseButtonWizard3.setIconTextGap(2);
        batchwiseMandatedElectivesBrowseButtonWizard3.setLabel("Browse");
        batchwiseMandatedElectivesBrowseButtonWizard3.setText("Browse");
        batchwiseMandatedElectivesBrowseButtonWizard3.putClientProperty("html.disable", Boolean.FALSE);
        JPanelWizard3.add(batchwiseMandatedElectivesBrowseButtonWizard3, cc.xy(13, 11, CellConstraints.CENTER, CellConstraints.CENTER));
        batchwiseCreditLimitLabelWizard3 = new JLabel();
        Font batchwiseCreditLimitLabelWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.PLAIN, 17, batchwiseCreditLimitLabelWizard3.getFont());
        if (batchwiseCreditLimitLabelWizard3Font != null)
            batchwiseCreditLimitLabelWizard3.setFont(batchwiseCreditLimitLabelWizard3Font);
        batchwiseCreditLimitLabelWizard3.setForeground(new Color(-131585));
        batchwiseCreditLimitLabelWizard3.setText("<html> <center> List of batch-wise credit limits <br> ( BatchName, CreditLimit ) :  </center></html>");
        JPanelWizard3.add(batchwiseCreditLimitLabelWizard3, cc.xy(3, 13, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        batchwiseCreditLimitTextFieldWizard3 = new JTextField();
        Font batchwiseCreditLimitTextFieldWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, batchwiseCreditLimitTextFieldWizard3.getFont());
        if (batchwiseCreditLimitTextFieldWizard3Font != null)
            batchwiseCreditLimitTextFieldWizard3.setFont(batchwiseCreditLimitTextFieldWizard3Font);
        batchwiseCreditLimitTextFieldWizard3.setText("");
        JPanelWizard3.add(batchwiseCreditLimitTextFieldWizard3, cc.xyw(5, 13, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        batchwiseCreditLimitCheckButtonWizard3 = new JButton();
        batchwiseCreditLimitCheckButtonWizard3.setBorderPainted(false);
        batchwiseCreditLimitCheckButtonWizard3.setContentAreaFilled(false);
        Font batchwiseCreditLimitCheckButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, batchwiseCreditLimitCheckButtonWizard3.getFont());
        if (batchwiseCreditLimitCheckButtonWizard3Font != null)
            batchwiseCreditLimitCheckButtonWizard3.setFont(batchwiseCreditLimitCheckButtonWizard3Font);
        batchwiseCreditLimitCheckButtonWizard3.setText("");
        JPanelWizard3.add(batchwiseCreditLimitCheckButtonWizard3, cc.xy(11, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
        batchwiseCreditLimitBrowseButtonWizard3 = new JButton();
        batchwiseCreditLimitBrowseButtonWizard3.setActionCommand("Button");
        batchwiseCreditLimitBrowseButtonWizard3.setBorderPainted(false);
        Font batchwiseCreditLimitBrowseButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, batchwiseCreditLimitBrowseButtonWizard3.getFont());
        if (batchwiseCreditLimitBrowseButtonWizard3Font != null)
            batchwiseCreditLimitBrowseButtonWizard3.setFont(batchwiseCreditLimitBrowseButtonWizard3Font);
        batchwiseCreditLimitBrowseButtonWizard3.setIcon(new ImageIcon(getClass().getResource("/gui/csv-outline-small.png")));
        batchwiseCreditLimitBrowseButtonWizard3.setIconTextGap(2);
        batchwiseCreditLimitBrowseButtonWizard3.setLabel("Browse");
        batchwiseCreditLimitBrowseButtonWizard3.setText("Browse");
        batchwiseCreditLimitBrowseButtonWizard3.putClientProperty("html.disable", Boolean.FALSE);
        JPanelWizard3.add(batchwiseCreditLimitBrowseButtonWizard3, cc.xy(13, 13, CellConstraints.CENTER, CellConstraints.CENTER));
        selectAlgorithmTypeLabelWizard3 = new JLabel();
        Font selectAlgorithmTypeLabelWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.PLAIN, 17, selectAlgorithmTypeLabelWizard3.getFont());
        if (selectAlgorithmTypeLabelWizard3Font != null)
            selectAlgorithmTypeLabelWizard3.setFont(selectAlgorithmTypeLabelWizard3Font);
        selectAlgorithmTypeLabelWizard3.setForeground(new Color(-131585));
        selectAlgorithmTypeLabelWizard3.setText("<html> <center> Select algorithm to generate allotments :  </center></html>");
        JPanelWizard3.add(selectAlgorithmTypeLabelWizard3, cc.xy(3, 15, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        selectAlgorithmTypeComboBoxWizard3 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Choose one of the following");
        defaultComboBoxModel1.addElement("1. Iterative HR Algorithm");
        defaultComboBoxModel1.addElement("2. First Preference Algorithm");
        defaultComboBoxModel1.addElement("3. Slotbased Heuristic1 Algorithm");
        defaultComboBoxModel1.addElement("4. Slotbased Heuristic2 Algorithm");
        selectAlgorithmTypeComboBoxWizard3.setModel(defaultComboBoxModel1);
        JPanelWizard3.add(selectAlgorithmTypeComboBoxWizard3, cc.xyw(5, 15, 5));
        generateAllotmentsButtonWizard3 = new JButton();
        generateAllotmentsButtonWizard3.setBorderPainted(false);
        Font generateAllotmentsButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, generateAllotmentsButtonWizard3.getFont());
        if (generateAllotmentsButtonWizard3Font != null)
            generateAllotmentsButtonWizard3.setFont(generateAllotmentsButtonWizard3Font);
        generateAllotmentsButtonWizard3.setText("Generate Allotments");
        JPanelWizard3.add(generateAllotmentsButtonWizard3, cc.xy(7, 17, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setAlignmentX(1.0f);
        Font scrollPane1Font = this.$$$getFont$$$("Bitstream Charter", Font.BOLD, 16, scrollPane1.getFont());
        if (scrollPane1Font != null) scrollPane1.setFont(scrollPane1Font);
        JPanelWizard3.add(scrollPane1, cc.xyw(3, 23, 11, CellConstraints.FILL, CellConstraints.FILL));
        displayOutputProgressTextArea = new JTextArea();
        displayOutputProgressTextArea.setAlignmentX(0.5f);
        displayOutputProgressTextArea.setEditable(false);
        Font displayOutputProgressTextAreaFont = this.$$$getFont$$$("Bitstream Charter", Font.BOLD, 16, displayOutputProgressTextArea.getFont());
        if (displayOutputProgressTextAreaFont != null)
            displayOutputProgressTextArea.setFont(displayOutputProgressTextAreaFont);
        displayOutputProgressTextArea.setLineWrap(true);
        displayOutputProgressTextArea.setMargin(new Insets(0, 0, 0, 0));
        displayOutputProgressTextArea.setMinimumSize(new Dimension(993, 20));
        displayOutputProgressTextArea.setWrapStyleWord(true);
        scrollPane1.setViewportView(displayOutputProgressTextArea);
        jLabelToSelectOptionsWizard3 = new JLabel();
        Font jLabelToSelectOptionsWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.BOLD, 24, jLabelToSelectOptionsWizard3.getFont());
        if (jLabelToSelectOptionsWizard3Font != null)
            jLabelToSelectOptionsWizard3.setFont(jLabelToSelectOptionsWizard3Font);
        jLabelToSelectOptionsWizard3.setForeground(new Color(-1774964));
        jLabelToSelectOptionsWizard3.setText("Generate student elective allotments....");
        JPanelWizard3.add(jLabelToSelectOptionsWizard3, cc.xyw(5, 3, 6, CellConstraints.CENTER, CellConstraints.FILL));
        prevButtonWizard3 = new JButton();
        prevButtonWizard3.setBorderPainted(false);
        Font prevButtonWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, prevButtonWizard3.getFont());
        if (prevButtonWizard3Font != null) prevButtonWizard3.setFont(prevButtonWizard3Font);
        prevButtonWizard3.setLabel("Previous");
        prevButtonWizard3.setText("Previous");
        JPanelWizard3.add(prevButtonWizard3, cc.xy(5, 17, CellConstraints.CENTER, CellConstraints.DEFAULT));
        analyseAllotmentsButton = new JButton();
        analyseAllotmentsButton.setBorderPainted(false);
        Font analyseAllotmentsButtonFont = this.$$$getFont$$$("Century Schoolbook L", -1, 16, analyseAllotmentsButton.getFont());
        if (analyseAllotmentsButtonFont != null) analyseAllotmentsButton.setFont(analyseAllotmentsButtonFont);
        analyseAllotmentsButton.setText("Analyse Allotments");
        JPanelWizard3.add(analyseAllotmentsButton, cc.xy(9, 17, CellConstraints.CENTER, CellConstraints.DEFAULT));
        insideDepartmentTextFieldWizard3 = new JTextField();
        Font insideDepartmentTextFieldWizard3Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, insideDepartmentTextFieldWizard3.getFont());
        if (insideDepartmentTextFieldWizard3Font != null)
            insideDepartmentTextFieldWizard3.setFont(insideDepartmentTextFieldWizard3Font);
        insideDepartmentTextFieldWizard3.setText("");
        JPanelWizard3.add(insideDepartmentTextFieldWizard3, cc.xyw(5, 9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        courseDetailsSegmentLabelWizard2 = new JLabel();
        Font courseDetailsSegmentLabelWizard2Font = this.$$$getFont$$$("Century Schoolbook L", Font.BOLD, 20, courseDetailsSegmentLabelWizard2.getFont());
        if (courseDetailsSegmentLabelWizard2Font != null)
            courseDetailsSegmentLabelWizard2.setFont(courseDetailsSegmentLabelWizard2Font);
        courseDetailsSegmentLabelWizard2.setForeground(new Color(-301758));
        courseDetailsSegmentLabelWizard2.setText("<html> <p align=\"right\"> List related to  department-wise <br> details :  </p></html>");
        JPanelWizard3.add(courseDetailsSegmentLabelWizard2, cc.xy(3, 7, CellConstraints.RIGHT, CellConstraints.CENTER));
        outputScreenLabelWizard3 = new JLabel();
        Font outputScreenLabelWizard3Font = this.$$$getFont$$$("Century Schoolbook L", Font.BOLD, 20, outputScreenLabelWizard3.getFont());
        if (outputScreenLabelWizard3Font != null) outputScreenLabelWizard3.setFont(outputScreenLabelWizard3Font);
        outputScreenLabelWizard3.setForeground(new Color(-301758));
        outputScreenLabelWizard3.setText("Output Screen:");
        JPanelWizard3.add(outputScreenLabelWizard3, cc.xy(3, 19, CellConstraints.LEFT, CellConstraints.CENTER));
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
        return JPanelWizard3;
    }


    /**
     * @noinspection ALL
     */

    /**
     * @noinspection ALL
     */
}
