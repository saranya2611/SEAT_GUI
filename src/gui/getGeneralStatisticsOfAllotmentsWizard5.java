package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class getGeneralStatisticsOfAllotmentsWizard5 extends JFrame {
    public String chosenDirectoryName, aggregatedStatisticsFileName, batchwiseAllotmentStatisticsFileName, allotmentsFileName, allottedMandatedElectiveDetailsFileName;
    public int lineNo;
    public String aggregatedStatisticsFileContent;
    public String batchwiseAllotmentStatisticsFileContent, allotmentsFileContent, allottedMandatedElectiveDetailsFileContent;
    public String matchedString = null;
    String batchNumberPattern = "^[A-Za-z][A-Za-z][0-9][0-9]";
    String line;
    JButton checkButton;
    String[] inputLine;
    ArrayList<String> arrayOfMatchings;
    String splitByComma = ",";
    String splitByNewLine = "\n";
    private JPanel jPanelGeneralAllotmentStatisticsWizard5;
    private JLabel jLabelGeneralAllotmentAnalysisWizard5;
    private JLabel chosenDirectoryLabelWizard5;
    private JTextField chosenDirectoryTextFieldWizard5;
    private JButton chosenDirectoryCheckButtonWizard5;
    private JButton chosenDirectoryBrowseButtonWizard5;
    private JLabel titleFieldGeneralAllotmentAnalysisWizard5;
    private JLabel batchNumberLabelWizard5;
    private JTextField batchNumberTextFieldWizard5;
    private JButton batchNumberCheckButtonWizard5;
    private JScrollPane TextAreaScrollPaneWizard5;
    private JTextArea generalStatsTextAreaWizard5;
    private JButton batchNumberGetDetailsButtonWizard5;
    private JButton prevWindowButtonWizard5;


    public getGeneralStatisticsOfAllotmentsWizard5() {
        super();
        $$$setupUI$$$();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
        setSize(1100, 900);

        JScrollPane scrPaneWizard5 = new JScrollPane(jPanelGeneralAllotmentStatisticsWizard5);
        add(scrPaneWizard5);

        jPanelGeneralAllotmentStatisticsWizard5.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        // Disable browse Button and display the default directory chosen in the previous step
        chosenDirectoryBrowseButtonWizard5.setEnabled(true);
        chosenDirectoryTextFieldWizard5.setEnabled(false);
        chosenDirectoryTextFieldWizard5.setVisible(true);
        chosenDirectoryCheckButtonWizard5.setVisible(false);
        batchNumberCheckButtonWizard5.setVisible(false);
        batchNumberTextFieldWizard5.setEnabled(true);
        batchNumberTextFieldWizard5.setEditable(true);

        // Row1: Directory with output files chosen from previous wizard
        chosenDirectoryTextFieldWizard5.setText(formValues.getAnalyseAllotmentDirPath());
        chosenDirectoryName = chosenDirectoryTextFieldWizard5.getText();
        formValues.setAnalyseAllotmentDirPath(chosenDirectoryName);
        File directoryName = new File(chosenDirectoryName);
        enableFileDirectoryExistenceCheckButton(chosenDirectoryCheckButtonWizard5, directoryName);
        chosenDirectoryBrowseButtonWizard5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenDirectoryBrowseButtonWizard5.setEnabled(true);
                chosenDirectoryBrowseButtonWizard5.setEnabled(true);
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int outputFilesDir = jfc.showOpenDialog(null);
                if (outputFilesDir == JFileChooser.APPROVE_OPTION) {
                    File outputFilesDirName = jfc.getSelectedFile();
                    chosenDirectoryBrowseButtonWizard5.setText(outputFilesDirName.getAbsolutePath());
                    chosenDirectoryName = chosenDirectoryTextFieldWizard5.getText();
                    formValues.setAnalyseAllotmentDirPath(chosenDirectoryName);
                    File directoryName = new File(chosenDirectoryName);
                    enableFileDirectoryExistenceCheckButton(chosenDirectoryCheckButtonWizard5, directoryName);
                }
            }
        });

        // Check for the aggregated statisticsFile -This file contains only one column with strings and numbers. Contains many rows
        // Loading the aggregated statistics file
        chosenDirectoryName = formValues.getAnalyseAllotmentDirPath();
        aggregatedStatisticsFileName = formValues.getAggregatedStatisticsFileName();
        generalStatsTextAreaWizard5.append("\n aggregatedStats:  " + aggregatedStatisticsFileName);
        generalStatsTextAreaWizard5.append("\n --------------------------------------------------------------------------------------------------------------------------------------------------------------- \n ");
        aggregatedStatisticsFileContent = readFiles(aggregatedStatisticsFileName, splitByNewLine, aggregatedStatisticsFileContent);
        generalStatsTextAreaWizard5.append("============================================================== \n ");

        // Check for the total number of allotment in output.csv file - This file contains 2 columns with strings and numbers. Contains many rows
        // Loading the aggregated statistics file
        allotmentsFileName = formValues.getAllotmentsFileName();
        generalStatsTextAreaWizard5.append("\n allotmentsFileStats:  " + allotmentsFileName);
        generalStatsTextAreaWizard5.append("\n --------------------------------------------------------------------------------------------------------------------------------------------------------------- ");
        allotmentsFileContent = readFiles(allotmentsFileName, splitByNewLine, allotmentsFileContent);
        generalStatsTextAreaWizard5.append("\n  ============================================================== \n ");


        // Row 2: Enter the batch name to get batch-specific details - Key listener with enter key
        batchNumberTextFieldWizard5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String enteredBatchNumber = batchNumberTextFieldWizard5.getText();
                    if (enteredBatchNumber.matches(batchNumberPattern)) {
                        System.out.println("\n Entered batch number is :" + enteredBatchNumber);
                        // To get the batch-wise general allotment details
                        batchwiseAllotmentStatisticsFileName = formValues.getBatchwiseAllotmentStatisticsFileName();
                        batchwiseAllotmentStatisticsFileContent = readFiles(batchwiseAllotmentStatisticsFileName, splitByNewLine, batchwiseAllotmentStatisticsFileContent);
                        //System.out.println(Arrays.toString(arrayOfMatchings.toArray()));
                        arrayOfMatchings = getBatchWiseDetails(batchwiseAllotmentStatisticsFileName, splitByNewLine, enteredBatchNumber, arrayOfMatchings);
                        postProcessBatchSpecificAllotmentDetailsForDisplay(arrayOfMatchings, enteredBatchNumber, batchwiseAllotmentStatisticsFileName);

                        // To get the batch-wise Recommended electives details
                        allottedMandatedElectiveDetailsFileName = formValues.getAllottedMandatedElectiveDetailsFileName();
                        allottedMandatedElectiveDetailsFileContent = readFiles(allottedMandatedElectiveDetailsFileName, splitByNewLine, allottedMandatedElectiveDetailsFileContent);
                        arrayOfMatchings = getBatchWiseDetails(allottedMandatedElectiveDetailsFileName, splitByNewLine, enteredBatchNumber, arrayOfMatchings);
                        postProcessBatchSpecificAllotmentDetailsForDisplay(arrayOfMatchings, enteredBatchNumber, allottedMandatedElectiveDetailsFileName);
                        generalStatsTextAreaWizard5.append("\n  ============================================================== \n ");

                    } else {
                        JOptionPane.showMessageDialog(null, "\n Batch-name MUST be of form : [A-Za-z][A-Za-z][0-9][0-9] \n Eg: ME16, me16, CS15, cs15\n ", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                super.keyPressed(e);
            }
        });

        // Row 2: Enter the batch name to get batch-specific details - Action listener with button click
        batchNumberGetDetailsButtonWizard5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                batchwiseAllotmentStatisticsFileName = formValues.getBatchwiseAllotmentStatisticsFileName();
                batchwiseAllotmentStatisticsFileContent = readFiles(batchwiseAllotmentStatisticsFileName, splitByNewLine, batchwiseAllotmentStatisticsFileContent);
                String enteredBatchNumber = batchNumberTextFieldWizard5.getText();
                if (enteredBatchNumber.matches(batchNumberPattern)) {
                    generalStatsTextAreaWizard5.append("\n  Batch-wise statistics ");
                    generalStatsTextAreaWizard5.append("\n  Batch-name: " + enteredBatchNumber);

                    //generalStatsTextAreaWizard5.append("\n General allotment statistics of batch " + enteredBatchNumber + " : " + arrayOfMatchings);
                    arrayOfMatchings = getBatchWiseDetails(batchwiseAllotmentStatisticsFileName, splitByNewLine, enteredBatchNumber, arrayOfMatchings);
                    postProcessBatchSpecificAllotmentDetailsForDisplay(arrayOfMatchings, enteredBatchNumber, batchwiseAllotmentStatisticsFileName);

                    //generalStatsTextAreaWizard5.append("\n \n recommended elective(s) allotment statistics of batch " + enteredBatchNumber + " : " + arrayOfMatchings);
                    arrayOfMatchings = getBatchWiseDetails(allottedMandatedElectiveDetailsFileName, splitByNewLine, enteredBatchNumber, arrayOfMatchings);
                    postProcessBatchSpecificAllotmentDetailsForDisplay(arrayOfMatchings, enteredBatchNumber, allottedMandatedElectiveDetailsFileName);
                    generalStatsTextAreaWizard5.append("\n  ============================================================== \n ");

                }
            }
        });

        prevWindowButtonWizard5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeAllotmentsWizard4 wizard4 = new analyzeAllotmentsWizard4();
                wizard4.setVisible(true);
                dispose();
            }
        });
    }

    // General function to check the existence of file or folder
    public void enableFileDirectoryExistenceCheckButton(JButton currentCheckButton, File file1) {
        if (file1.exists()) {
            checkButton = currentCheckButton;
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("greenYes.png"));
                checkButton.setIcon(new ImageIcon(img));
                chosenDirectoryCheckButtonWizard5.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("redCross.png"));
                checkButton.setIcon(new ImageIcon(img));
                chosenDirectoryCheckButtonWizard5.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // General Function for reading the file contents
    public String readFiles(String fileName, String fieldSeparator, String loadedOutputFileContent) {
        BufferedReader buffRead = null; // Initialize a buffer
        lineNo = 0; // Start reading from line number 1:
        try {
            buffRead = new BufferedReader(new FileReader(fileName));
            if (!fileName.equals(aggregatedStatisticsFileName)) {
                buffRead.readLine(); //Skip the first line since it will be the header row
            }
            //read input file line by line
            while ((line = buffRead.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(fieldSeparator);

                //Get the column after splitting the line with delimiter specified in fieldSeparator without header in first line
                loadedOutputFileContent = inputLine[0];
                lineNo++;

                if (fileName.equals(aggregatedStatisticsFileName)) {
                    generalStatsTextAreaWizard5.append(loadedOutputFileContent);
                    generalStatsTextAreaWizard5.append("\n");
                }
            }
            if (fileName.equals(allotmentsFileName)) {
                generalStatsTextAreaWizard5.append("\n Total number of allotments : " + lineNo + " ( [Student,course] Pairs )");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedOutputFileContent;
    }

    // General function to read the file contents and return the matched strings
    public ArrayList<String> getBatchWiseDetails(String fileName, String fieldSeparator, String subString, ArrayList<String> arrayOfMatchings) {
        BufferedReader buffRead = null; // Initialize a buffer
        lineNo = 0; // Start reading from line number 1:
        int index = 1;
        try {
            buffRead = new BufferedReader(new FileReader(fileName));
            if (!fileName.equals(aggregatedStatisticsFileName)) {
                buffRead.readLine(); //Skip the first line since it will be the header row
            }
            //read input file line by line
            arrayOfMatchings = new ArrayList<String>();
            while ((line = buffRead.readLine()) != null) {
                line.replaceAll("\\s+", ""); //Remove all whitespace
                inputLine = line.split(fieldSeparator);

                //Get the column after splitting the line with delimiter specified in fieldSeparator without header in first line
                String loadedOutputFileContent = inputLine[0];
                lineNo++;
                if (loadedOutputFileContent.toLowerCase().contains(subString.toLowerCase())) {
                    matchedString = loadedOutputFileContent;
                    System.out.println("matchedString : " + matchedString);
                    arrayOfMatchings.add(loadedOutputFileContent);
                    System.out.println("content in index arrayOfMatchings[" + index + "] :" + loadedOutputFileContent);
                    index++;
                }
            }
            System.out.printf("Length of array Of Matchings : %d\n", arrayOfMatchings.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOfMatchings;
    }

    // Post-process batch specific allotment details
    public void postProcessBatchSpecificAllotmentDetailsForDisplay(ArrayList<String> arrayOfMatchings, String batchName, String fileName) {

        if (fileName.equals(batchwiseAllotmentStatisticsFileName)) {
            int iteration = 0;
            int entryCount = arrayOfMatchings.size();
            /*System.out.println("\n Length of array of matched contents: " + entryCount);
            System.out.println("\n Array with matched elements:" + arrayOfMatchings);
            System.out.println("\n Overall allotment statistics of " + batchName.toUpperCase() + " batch: ");*/
            while (iteration < entryCount) {
                String matchedString = arrayOfMatchings.get(iteration); //.split(splitByComma);
                String[] matchedStringArray = matchedString.split(splitByComma);
                generalStatsTextAreaWizard5.append("\n General allotment statistics of " + batchName.toUpperCase() + " batch: ");
                generalStatsTextAreaWizard5.append("\n ---------------------------------------------------------------------------");
                generalStatsTextAreaWizard5.append("\n Total number of students in this batch : " + matchedStringArray[1]);
                generalStatsTextAreaWizard5.append("\n No. of students who GAVE preferences  :  " + matchedStringArray[2]);
                int studentCountWithPreferences = Integer.parseInt(matchedStringArray[1]) - Integer.parseInt(matchedStringArray[2]);
                generalStatsTextAreaWizard5.append("\n No. of students who DID NOT GIVE any preferences :  " + studentCountWithPreferences);
                generalStatsTextAreaWizard5.append("\n Allotted Credits Ratio (tot. allotted credits/tot. strength) : " + matchedStringArray[4]);
                iteration++;
            }
        }
        if (fileName.equals(allottedMandatedElectiveDetailsFileName)) {
            //Batch,Elective Type,Total Strength,Number of students who didn't give a preference for this elective type,Total Allotted
            int iteration = 0;
            int entryCount = arrayOfMatchings.size();
            System.out.println("Length of array of matched contents: " + entryCount);
            generalStatsTextAreaWizard5.append("\n \n Recommended electives allotment statistics of " + batchName.toUpperCase() + " batch: ");
            generalStatsTextAreaWizard5.append("\n ----------------------------------------------------------------------------");
            if (entryCount == 0) {
                generalStatsTextAreaWizard5.append("\n No Recommended electives for this batch....!!!");
            } else {
                generalStatsTextAreaWizard5.append("\n This batch has " + entryCount + " recommended electives ");
                while (iteration < entryCount) {
                    String matchedString = arrayOfMatchings.get(iteration); //.split(splitByComma);
                    String[] matchedStringArray = matchedString.split(splitByComma);
                    int count = iteration + 1;
                    generalStatsTextAreaWizard5.append("\n \n Recommended elective  (" + count + ")  : " + matchedStringArray[1]);
                    generalStatsTextAreaWizard5.append("\n Total number of students in this batch : " + matchedStringArray[2]);
                    generalStatsTextAreaWizard5.append("\n No. of students WITHOUT preferences for this elective :  " + matchedStringArray[3]);
                    int studentCountWithPreferences = Integer.parseInt(matchedStringArray[2]) - Integer.parseInt(matchedStringArray[3]);
                    generalStatsTextAreaWizard5.append("\n No. of students WITH preferences for this elective :  " + studentCountWithPreferences);
                    generalStatsTextAreaWizard5.append("\n Out of " + studentCountWithPreferences + " students,  number of students allotted to " + matchedStringArray[1] + " : " + matchedStringArray[4]);
                    iteration++;
                }
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
        jPanelGeneralAllotmentStatisticsWizard5 = new JPanel();
        jPanelGeneralAllotmentStatisticsWizard5.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:3dlu:noGrow,fill:d:grow,left:9dlu:noGrow,fill:413px:noGrow,left:4dlu:noGrow,fill:66px:noGrow,left:4dlu:noGrow,fill:167px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:noGrow,top:16dlu:noGrow,center:58px:noGrow,center:15px:noGrow,center:44px:noGrow,top:4dlu:noGrow,top:24dlu:noGrow,top:4dlu:noGrow,center:473px:grow,top:4dlu:noGrow,top:17dlu:noGrow,center:max(d;4px):noGrow,center:15px:noGrow"));
        jPanelGeneralAllotmentStatisticsWizard5.setAutoscrolls(true);
        jPanelGeneralAllotmentStatisticsWizard5.setBackground(new Color(-14793908));
        jPanelGeneralAllotmentStatisticsWizard5.setDoubleBuffered(false);
        jPanelGeneralAllotmentStatisticsWizard5.setForeground(new Color(-367294));
        jLabelGeneralAllotmentAnalysisWizard5 = new JLabel();
        Font jLabelGeneralAllotmentAnalysisWizard5Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 32, jLabelGeneralAllotmentAnalysisWizard5.getFont());
        if (jLabelGeneralAllotmentAnalysisWizard5Font != null)
            jLabelGeneralAllotmentAnalysisWizard5.setFont(jLabelGeneralAllotmentAnalysisWizard5Font);
        jLabelGeneralAllotmentAnalysisWizard5.setForeground(new Color(-1774964));
        jLabelGeneralAllotmentAnalysisWizard5.setText("General Allotment Analysis ....");
        CellConstraints cc = new CellConstraints();
        jPanelGeneralAllotmentStatisticsWizard5.add(jLabelGeneralAllotmentAnalysisWizard5, cc.xyw(3, 1, 7, CellConstraints.CENTER, CellConstraints.FILL));
        chosenDirectoryLabelWizard5 = new JLabel();
        Font chosenDirectoryLabelWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryLabelWizard5.getFont());
        if (chosenDirectoryLabelWizard5Font != null)
            chosenDirectoryLabelWizard5.setFont(chosenDirectoryLabelWizard5Font);
        chosenDirectoryLabelWizard5.setForeground(new Color(-131585));
        chosenDirectoryLabelWizard5.setText("<html><center> From the chosen directory : <center><html>");
        jPanelGeneralAllotmentStatisticsWizard5.add(chosenDirectoryLabelWizard5, cc.xy(3, 3, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        chosenDirectoryTextFieldWizard5 = new JTextField();
        chosenDirectoryTextFieldWizard5.setEditable(false);
        chosenDirectoryTextFieldWizard5.setEnabled(false);
        Font chosenDirectoryTextFieldWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 20, chosenDirectoryTextFieldWizard5.getFont());
        if (chosenDirectoryTextFieldWizard5Font != null)
            chosenDirectoryTextFieldWizard5.setFont(chosenDirectoryTextFieldWizard5Font);
        chosenDirectoryTextFieldWizard5.setText("");
        jPanelGeneralAllotmentStatisticsWizard5.add(chosenDirectoryTextFieldWizard5, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.CENTER));
        chosenDirectoryCheckButtonWizard5 = new JButton();
        chosenDirectoryCheckButtonWizard5.setBorderPainted(false);
        chosenDirectoryCheckButtonWizard5.setContentAreaFilled(false);
        chosenDirectoryCheckButtonWizard5.setText("");
        jPanelGeneralAllotmentStatisticsWizard5.add(chosenDirectoryCheckButtonWizard5, cc.xy(7, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        chosenDirectoryBrowseButtonWizard5 = new JButton();
        chosenDirectoryBrowseButtonWizard5.setBorderPainted(false);
        chosenDirectoryBrowseButtonWizard5.setEnabled(false);
        Font chosenDirectoryBrowseButtonWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, chosenDirectoryBrowseButtonWizard5.getFont());
        if (chosenDirectoryBrowseButtonWizard5Font != null)
            chosenDirectoryBrowseButtonWizard5.setFont(chosenDirectoryBrowseButtonWizard5Font);
        chosenDirectoryBrowseButtonWizard5.setIcon(new ImageIcon(getClass().getResource("/gui/dir1-small.png")));
        chosenDirectoryBrowseButtonWizard5.setText("Browse");
        jPanelGeneralAllotmentStatisticsWizard5.add(chosenDirectoryBrowseButtonWizard5, cc.xy(9, 3, CellConstraints.CENTER, CellConstraints.CENTER));
        titleFieldGeneralAllotmentAnalysisWizard5 = new JLabel();
        Font titleFieldGeneralAllotmentAnalysisWizard5Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 26, titleFieldGeneralAllotmentAnalysisWizard5.getFont());
        if (titleFieldGeneralAllotmentAnalysisWizard5Font != null)
            titleFieldGeneralAllotmentAnalysisWizard5.setFont(titleFieldGeneralAllotmentAnalysisWizard5Font);
        titleFieldGeneralAllotmentAnalysisWizard5.setForeground(new Color(-301758));
        titleFieldGeneralAllotmentAnalysisWizard5.setText("Statistics Details :");
        jPanelGeneralAllotmentStatisticsWizard5.add(titleFieldGeneralAllotmentAnalysisWizard5, cc.xyw(1, 7, 3, CellConstraints.CENTER, CellConstraints.BOTTOM));
        batchNumberLabelWizard5 = new JLabel();
        Font batchNumberLabelWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, batchNumberLabelWizard5.getFont());
        if (batchNumberLabelWizard5Font != null) batchNumberLabelWizard5.setFont(batchNumberLabelWizard5Font);
        batchNumberLabelWizard5.setForeground(new Color(-131585));
        batchNumberLabelWizard5.setText("<html><center> Enter a batch number : <br> ( Eg: ME15, CS16 ) <center><html>");
        jPanelGeneralAllotmentStatisticsWizard5.add(batchNumberLabelWizard5, cc.xy(3, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        batchNumberTextFieldWizard5 = new JTextField();
        batchNumberTextFieldWizard5.setEditable(false);
        batchNumberTextFieldWizard5.setEnabled(false);
        Font batchNumberTextFieldWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 20, batchNumberTextFieldWizard5.getFont());
        if (batchNumberTextFieldWizard5Font != null)
            batchNumberTextFieldWizard5.setFont(batchNumberTextFieldWizard5Font);
        batchNumberTextFieldWizard5.setText("");
        jPanelGeneralAllotmentStatisticsWizard5.add(batchNumberTextFieldWizard5, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.CENTER));
        batchNumberCheckButtonWizard5 = new JButton();
        batchNumberCheckButtonWizard5.setBorderPainted(false);
        batchNumberCheckButtonWizard5.setContentAreaFilled(false);
        batchNumberCheckButtonWizard5.setText("");
        jPanelGeneralAllotmentStatisticsWizard5.add(batchNumberCheckButtonWizard5, cc.xy(7, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        TextAreaScrollPaneWizard5 = new JScrollPane();
        TextAreaScrollPaneWizard5.setVerticalScrollBarPolicy(20);
        TextAreaScrollPaneWizard5.setWheelScrollingEnabled(true);
        jPanelGeneralAllotmentStatisticsWizard5.add(TextAreaScrollPaneWizard5, cc.xyw(3, 9, 7, CellConstraints.FILL, CellConstraints.FILL));
        generalStatsTextAreaWizard5 = new JTextArea();
        generalStatsTextAreaWizard5.setEditable(false);
        Font generalStatsTextAreaWizard5Font = this.$$$getFont$$$("Bitstream Charter", Font.BOLD, 16, generalStatsTextAreaWizard5.getFont());
        if (generalStatsTextAreaWizard5Font != null)
            generalStatsTextAreaWizard5.setFont(generalStatsTextAreaWizard5Font);
        generalStatsTextAreaWizard5.setLineWrap(true);
        generalStatsTextAreaWizard5.setWrapStyleWord(true);
        TextAreaScrollPaneWizard5.setViewportView(generalStatsTextAreaWizard5);
        batchNumberGetDetailsButtonWizard5 = new JButton();
        batchNumberGetDetailsButtonWizard5.setBorderPainted(false);
        Font batchNumberGetDetailsButtonWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, batchNumberGetDetailsButtonWizard5.getFont());
        if (batchNumberGetDetailsButtonWizard5Font != null)
            batchNumberGetDetailsButtonWizard5.setFont(batchNumberGetDetailsButtonWizard5Font);
        batchNumberGetDetailsButtonWizard5.setText("Get Details");
        jPanelGeneralAllotmentStatisticsWizard5.add(batchNumberGetDetailsButtonWizard5, cc.xy(9, 5, CellConstraints.CENTER, CellConstraints.CENTER));
        prevWindowButtonWizard5 = new JButton();
        prevWindowButtonWizard5.setBorderPainted(false);
        Font prevWindowButtonWizard5Font = this.$$$getFont$$$("Century Schoolbook L", -1, 16, prevWindowButtonWizard5.getFont());
        if (prevWindowButtonWizard5Font != null) prevWindowButtonWizard5.setFont(prevWindowButtonWizard5Font);
        prevWindowButtonWizard5.setText("Go Back");
        jPanelGeneralAllotmentStatisticsWizard5.add(prevWindowButtonWizard5, cc.xy(5, 11, CellConstraints.CENTER, CellConstraints.CENTER));
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
        return jPanelGeneralAllotmentStatisticsWizard5;
    }
}
