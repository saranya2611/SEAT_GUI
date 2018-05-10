package gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class analyzeAllotmentsWizard4 extends JFrame {
    private JLabel titleFieldAnalysisWizard4;
    private JLabel jLabelAnalysisWizard4;
    private JLabel outputDirectoryLabelWizard4;
    private JTextField outputDirectoryTextFieldWizard1;
    private JButton outputDirectoryBrowseButtonWizard1;
    private JButton outputDirectoryCheckButtonWizard1;
    private JButton getAllotmentStatisticsButtonWizard4;
    private JButton getCourseStatisticsButtonWizard4;
    private JButton getStudentStatisticsButtonWizard4;
    private JPanel jPanelAnalyseWizard4;
    private JButton prevButtonWizard4;

    public String outputDirectory;
    public JButton checkButton;

    public String chosenDirectoryName, aggregatedStatisticsFileName, batchwiseAllotmentStatisticsFileName, allotmentsFileName, allotedMandatedElectiveDetailsFileName;
    public String errorMsg;
    public int lineNo;

    File aggregatedStatisticsFile, batchwiseAllotmentStatisticsFile, allotmentsFile, allotedMandatedElectiveDetailsFile;

    public void enableFileDirectoryExistenceCheckButton(JButton currentCheckButton, File file1) {
        if (file1.exists()) {
            checkButton = currentCheckButton;
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("greenYes.png"));
                checkButton.setIcon(new ImageIcon(img));
                getAllotmentStatisticsButtonWizard4.setEnabled(true);
                getStudentStatisticsButtonWizard4.setEnabled(true);
                getCourseStatisticsButtonWizard4.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkButton.setVisible(true);
            try {
                Image img = ImageIO.read(getClass().getResource("redCross.png"));
                checkButton.setIcon(new ImageIcon(img));
                getAllotmentStatisticsButtonWizard4.setEnabled(false);
                getStudentStatisticsButtonWizard4.setEnabled(false);
                getCourseStatisticsButtonWizard4.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public analyzeAllotmentsWizard4() {
        super();
        $$$setupUI$$$();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
        setSize(1100, 500);

        JScrollPane scrPaneWizard4 = new JScrollPane(jPanelAnalyseWizard4);
        add(scrPaneWizard4);

        jPanelAnalyseWizard4.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        // Row1: Directory with the output files of an allotment method
        outputDirectoryCheckButtonWizard1.setVisible(false);
        outputDirectoryTextFieldWizard1.setText(formValues.getAnalyseAllotmentDirPath());
        outputDirectoryBrowseButtonWizard1.setVisible(true);
        outputDirectoryBrowseButtonWizard1.setEnabled(true);
        outputDirectoryTextFieldWizard1.setEnabled(false);

        outputDirectoryBrowseButtonWizard1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputDirectoryBrowseButtonWizard1.setEnabled(true);
                outputDirectoryTextFieldWizard1.setEnabled(true);
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int outputFilesDir = jfc.showOpenDialog(null);
                if (outputFilesDir == JFileChooser.APPROVE_OPTION) {
                    File outputFilesDirName = jfc.getSelectedFile();
                    outputDirectoryTextFieldWizard1.setText(outputFilesDirName.getAbsolutePath());
                    outputDirectory = outputDirectoryTextFieldWizard1.getText();
                    formValues.setAnalyseAllotmentDirPath(outputDirectory);
                    File directoryName = new File(outputDirectory);
                    enableFileDirectoryExistenceCheckButton(outputDirectoryCheckButtonWizard1, directoryName);
                }
            }
        });

        // Row2: Get overall allotment statistics
        getAllotmentStatisticsButtonWizard4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assigning the names of set of required files from the chosen directory
                chosenDirectoryName = formValues.getAnalyseAllotmentDirPath();
                aggregatedStatisticsFileName = chosenDirectoryName + "/aggregateStatistics.csv";
                formValues.setAggregatedStatisticsFileName(aggregatedStatisticsFileName);
                batchwiseAllotmentStatisticsFileName = chosenDirectoryName + "/batchwiseAllotmentStatistics.csv";
                formValues.setBatchwiseAllotmentStatisticsFileName(batchwiseAllotmentStatisticsFileName);
                allotmentsFileName = chosenDirectoryName + "/output.csv";
                formValues.setAllotmentsFileName(allotmentsFileName);
                allotedMandatedElectiveDetailsFileName = chosenDirectoryName + "/mandatedElectiveStatistics.csv";
                formValues.setAllottedMandatedElectiveDetailsFileName(allotedMandatedElectiveDetailsFileName);

                aggregatedStatisticsFile = new File(aggregatedStatisticsFileName);
                batchwiseAllotmentStatisticsFile = new File(batchwiseAllotmentStatisticsFileName);
                allotmentsFile = new File(allotmentsFileName);
                allotedMandatedElectiveDetailsFile = new File(allotedMandatedElectiveDetailsFileName);

                if (aggregatedStatisticsFile.exists() && batchwiseAllotmentStatisticsFile.exists() && allotmentsFile.exists() && allotedMandatedElectiveDetailsFile.exists()) {
                    getGeneralStatisticsOfAllotmentsWizard5 generalStatsWizard5 = new getGeneralStatisticsOfAllotmentsWizard5();
                    generalStatsWizard5.setVisible(true);
                    dispose();
                } else {
                    if (!aggregatedStatisticsFile.exists() || !batchwiseAllotmentStatisticsFile.exists() || !allotmentsFile.exists() || !allotedMandatedElectiveDetailsFile.exists()) {
                        errorMsg = "\n Specified Path: \" " + chosenDirectoryName + "  \" does not contain allotment output file(s)  ! \n Re-enter a valid folder name with generated allotments...! ";
                        JOptionPane.showMessageDialog(null, errorMsg, "Files Not Found", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Row3: Get student-wise allotment statistics
        getStudentStatisticsButtonWizard4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getStudentSpecificStatisticsWizard6 studentSpecificStatisticsWizard6 = new getStudentSpecificStatisticsWizard6();
                studentSpecificStatisticsWizard6.setVisible(true);
                dispose();
            }
        });

        // Row3: Get course-wise allotment statistics
        getCourseStatisticsButtonWizard4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCourseSpecificStatisticsWizard7 courseSpecificStatisticsWizard7 = new getCourseSpecificStatisticsWizard7();
                courseSpecificStatisticsWizard7.setVisible(true);
                dispose();
            }
        });
        prevButtonWizard4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputToAllotmentsWizard1 wizard1 = new inputToAllotmentsWizard1();
                wizard1.setVisible(true);
                dispose();
            }
        });
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jPanelAnalyseWizard4 = new JPanel();
        jPanelAnalyseWizard4.setLayout(new FormLayout("fill:285px:grow,left:9dlu:noGrow,fill:452px:noGrow,left:9dlu:noGrow,fill:31px:noGrow,left:8dlu:noGrow,fill:143px:noGrow,left:4dlu:noGrow,fill:19px:noGrow", "center:12px:noGrow,top:4dlu:noGrow,center:d:noGrow,top:16dlu:noGrow,center:max(d;4px):noGrow,top:17dlu:noGrow,center:max(d;4px):noGrow,top:21dlu:noGrow,center:max(d;4px):noGrow,top:23dlu:noGrow,center:max(d;4px):noGrow,top:22dlu:noGrow,center:max(d;4px):noGrow,top:16dlu:noGrow,center:37px:noGrow"));
        jPanelAnalyseWizard4.setBackground(new Color(-14793908));
        jLabelAnalysisWizard4 = new JLabel();
        Font jLabelAnalysisWizard4Font = this.$$$getFont$$$("Gentium Book Basic", Font.BOLD, 32, jLabelAnalysisWizard4.getFont());
        if (jLabelAnalysisWizard4Font != null) jLabelAnalysisWizard4.setFont(jLabelAnalysisWizard4Font);
        jLabelAnalysisWizard4.setForeground(new Color(-1774964));
        jLabelAnalysisWizard4.setText("Allotment Analysis ....");
        CellConstraints cc = new CellConstraints();
        jPanelAnalyseWizard4.add(jLabelAnalysisWizard4, cc.xyw(3, 5, 2, CellConstraints.CENTER, CellConstraints.FILL));
        outputDirectoryLabelWizard4 = new JLabel();
        Font outputDirectoryLabelWizard4Font = this.$$$getFont$$$("Century Schoolbook L", -1, 18, outputDirectoryLabelWizard4.getFont());
        if (outputDirectoryLabelWizard4Font != null)
            outputDirectoryLabelWizard4.setFont(outputDirectoryLabelWizard4Font);
        outputDirectoryLabelWizard4.setForeground(new Color(-131585));
        outputDirectoryLabelWizard4.setText("<html><center>Select directory with generated <br>allotment files : <center><html>");
        jPanelAnalyseWizard4.add(outputDirectoryLabelWizard4, cc.xy(1, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        outputDirectoryTextFieldWizard1 = new JTextField();
        outputDirectoryTextFieldWizard1.setEditable(false);
        outputDirectoryTextFieldWizard1.setEnabled(false);
        Font outputDirectoryTextFieldWizard1Font = this.$$$getFont$$$("Century Schoolbook L", -1, 20, outputDirectoryTextFieldWizard1.getFont());
        if (outputDirectoryTextFieldWizard1Font != null)
            outputDirectoryTextFieldWizard1.setFont(outputDirectoryTextFieldWizard1Font);
        outputDirectoryTextFieldWizard1.setText("");
        jPanelAnalyseWizard4.add(outputDirectoryTextFieldWizard1, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.CENTER));
        outputDirectoryBrowseButtonWizard1 = new JButton();
        outputDirectoryBrowseButtonWizard1.setBorderPainted(false);
        outputDirectoryBrowseButtonWizard1.setEnabled(false);
        Font outputDirectoryBrowseButtonWizard1Font = this.$$$getFont$$$("Century Schoolbook L", -1, 20, outputDirectoryBrowseButtonWizard1.getFont());
        if (outputDirectoryBrowseButtonWizard1Font != null)
            outputDirectoryBrowseButtonWizard1.setFont(outputDirectoryBrowseButtonWizard1Font);
        outputDirectoryBrowseButtonWizard1.setIcon(new ImageIcon(getClass().getResource("/gui/dir1-small.png")));
        outputDirectoryBrowseButtonWizard1.setText("Browse");
        jPanelAnalyseWizard4.add(outputDirectoryBrowseButtonWizard1, cc.xy(7, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        outputDirectoryCheckButtonWizard1 = new JButton();
        outputDirectoryCheckButtonWizard1.setBorderPainted(false);
        outputDirectoryCheckButtonWizard1.setContentAreaFilled(false);
        outputDirectoryCheckButtonWizard1.setText("");
        jPanelAnalyseWizard4.add(outputDirectoryCheckButtonWizard1, cc.xy(5, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        getAllotmentStatisticsButtonWizard4 = new JButton();
        getAllotmentStatisticsButtonWizard4.setBorderPainted(false);
        getAllotmentStatisticsButtonWizard4.setText("Get Chosen Allotment's Statistics");
        jPanelAnalyseWizard4.add(getAllotmentStatisticsButtonWizard4, cc.xy(3, 9, CellConstraints.CENTER, CellConstraints.DEFAULT));
        getStudentStatisticsButtonWizard4 = new JButton();
        getStudentStatisticsButtonWizard4.setBorderPainted(false);
        getStudentStatisticsButtonWizard4.setText("Get Student Specific Statistics");
        jPanelAnalyseWizard4.add(getStudentStatisticsButtonWizard4, cc.xy(3, 11, CellConstraints.CENTER, CellConstraints.DEFAULT));
        getCourseStatisticsButtonWizard4 = new JButton();
        getCourseStatisticsButtonWizard4.setBorderPainted(false);
        getCourseStatisticsButtonWizard4.setText("Get Course Specific Statistics");
        jPanelAnalyseWizard4.add(getCourseStatisticsButtonWizard4, cc.xy(3, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
        prevButtonWizard4 = new JButton();
        prevButtonWizard4.setBorderPainted(false);
        prevButtonWizard4.setText("Go Back");
        jPanelAnalyseWizard4.add(prevButtonWizard4, cc.xy(3, 15, CellConstraints.CENTER, CellConstraints.DEFAULT));
        titleFieldAnalysisWizard4 = new JLabel();
        Font titleFieldAnalysisWizard4Font = this.$$$getFont$$$("Lobster Two", Font.BOLD, 60, titleFieldAnalysisWizard4.getFont());
        if (titleFieldAnalysisWizard4Font != null) titleFieldAnalysisWizard4.setFont(titleFieldAnalysisWizard4Font);
        titleFieldAnalysisWizard4.setForeground(new Color(-301758));
        titleFieldAnalysisWizard4.setText("Welcome to SEAT");
        jPanelAnalyseWizard4.add(titleFieldAnalysisWizard4, cc.xyw(2, 3, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
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
        return jPanelAnalyseWizard4;
    }
}
