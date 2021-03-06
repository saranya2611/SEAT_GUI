package gui;

public class formValues {
	
	public static formValues instance;
	
	private formValues(){ // Singleton set
	}
	
	public synchronized static formValues getInstance() // creating instance for this class
	{
		if (instance==null)
			instance = new formValues ();
		return instance;
	}
	
	// #################################################################################################
	// Default Directory path in form1 (inputToAllotmentsWizard1.java)
	public static String defaultDirPath="";
	public static void setDefaultDirPath (String defaultDirPath) {
		formValues.defaultDirPath = defaultDirPath;
	}
	public static String getDefaultDirPath () {
		return defaultDirPath;
	}

	// #################################################################################################
	// Default Directory path in form1 (inputToAllotmentsWizard1.java)
	public static String analyseAllotmentDirPath="";
	public static void setAnalyseAllotmentDirPath (String analyseAllotmentDirPath) {
		formValues.analyseAllotmentDirPath = analyseAllotmentDirPath;
	}
	public static String getAnalyseAllotmentDirPath () {
		return analyseAllotmentDirPath;
	}

	//####################################################################################################
	// New slot config file path (new_slot_config.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String slotConfigFileName;
	public static void setSlotConfigFileName (String slotConfigFileName) {
		formValues.slotConfigFileName = slotConfigFileName;
	}
	public static String getSlotConfigFileName () {
		return slotConfigFileName;
	}
	
	//####################################################################################################
	// course details file path (courseDetails.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String courseDetailsFileNameWithPath;
	public static void setCourseDetailsFileNameWithPath (String courseDetailsFileNameWithPath) {
		formValues.courseDetailsFileNameWithPath = courseDetailsFileNameWithPath;
	}
	public static String getCourseDetailsFileNameWithPath () {
		return courseDetailsFileNameWithPath;
	}
	
	//####################################################################################################
	// course preference option (course preference options dropDownBox from FORM2 - defaultSetOfFilesWizard2)
	public static boolean usingExistingCoursePreferenceListFlag;
	public static void setUsingExistingCoursePreferenceListFlag (boolean usingExistingCoursePreferenceListFlag) {
			formValues.usingExistingCoursePreferenceListFlag = usingExistingCoursePreferenceListFlag;
	}
	public static boolean getUsingExistingCoursePreferenceListFlag () {
		return usingExistingCoursePreferenceListFlag;
	}

	//####################################################################################################
	// course preference file path (coursePreferenceFile.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String existingCoursePreferenceFileNameWithPath;
	public static void setExistingCoursePreferenceFileNameWithPath (String existingCoursePreferenceFileNameWithPath) {
		formValues.existingCoursePreferenceFileNameWithPath = existingCoursePreferenceFileNameWithPath;
	}
	public static String getExistingCoursePreferenceFileNameWithPath () {
		return existingCoursePreferenceFileNameWithPath;
	}

	//####################################################################################################
	// course preference file path (coursePreferenceFile.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String newCoursePreferenceFileNameWithPath;
	public static void setNewCoursePreferenceFileNameWithPath (String newCoursePreferenceFileNameWithPath) {
		formValues.newCoursePreferenceFileNameWithPath = newCoursePreferenceFileNameWithPath;
	}
	public static String getNewCoursePreferenceFileNameWithPath () {
		return newCoursePreferenceFileNameWithPath;
	}

	//####################################################################################################
	// student's CGPA details file path (studentCGPAWithHonoursStudents.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String studentCGPAFileNameWithPath;
	public static void setStudentCGPAFileNameWithPath (String studentCGPAFileNameWithPath) {
		formValues.studentCGPAFileNameWithPath = studentCGPAFileNameWithPath;
	}
	public static String getStudentCGPAFileNameWithPath () {
		return studentCGPAFileNameWithPath;
	}
	
	//####################################################################################################
	// student's preference list file path (studentsPreferenceList.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String studentPreferenceListNameWithPath;
	public static void setStudentPreferenceListNameWithPath (String studentPreferenceListNameWithPath) {
		formValues.studentPreferenceListNameWithPath = studentPreferenceListNameWithPath;
	}
	public static String getStudentPreferenceListNameWithPath () {
		return studentPreferenceListNameWithPath;
	}
	
	//####################################################################################################
	// highPriority student's details file path (highPriorityStudentDetails.csv from FORM2 - defaultSetOfFilesWizard2)
	public static String highPriorityStudentDetailsFileNameWithPath;
	public static void setHighPriorityStudentDetailsFileNameWithPath (String highPriorityStudentDetailsFileNameWithPath) {
		formValues.highPriorityStudentDetailsFileNameWithPath = highPriorityStudentDetailsFileNameWithPath;
	}
	public static String getHighPriorityStudentDetailsFileNameWithPath () {
		return highPriorityStudentDetailsFileNameWithPath;
	}
	
	//####################################################################################################
	// inside department specification file path (insideDepartmentDetails.csv from FORM3 - defaultSetOfFilesWizard3)
	public static String insideDepartmentDetailsListNameWithPath;
	public static void setInsideDepartmentDetailsListNameWithPath (String insideDepartmentDetailsListNameWithPath) {
	
		formValues.insideDepartmentDetailsListNameWithPath = insideDepartmentDetailsListNameWithPath;
	}
	public static String getInsideDepartmentDetailsListNameWithPath () {
		return insideDepartmentDetailsListNameWithPath;
	}
	
	//####################################################################################################
	// madated electives details file path (mandatedElectives.csv from FORM3 - defaultSetOfFilesWizard3)
	public static String mandatedElectivesListNameWithPath;
	public static void setMandatedElectivesListNameWithPath (String mandatedElectivesListNameWithPath) {
		formValues.mandatedElectivesListNameWithPath = mandatedElectivesListNameWithPath;
	}
	public static String getMandatedElectivesListNameWithPath () {
		return mandatedElectivesListNameWithPath;
	}
	
	//####################################################################################################
	// batch-wise credit limit file path (creditLimit.csv from FORM3 - defaultSetOfFilesWizard3)
	public static String batchwiseCreditLimitFileNameWithPath;
	public static void setBatchwiseCreditLimitFileNameWithPath (String batchwiseCreditLimitFileNameWithPath) {
		formValues.batchwiseCreditLimitFileNameWithPath = batchwiseCreditLimitFileNameWithPath;
	}
	public static String getBatchwiseCreditLimitFileNameWithPath () {
		return batchwiseCreditLimitFileNameWithPath;
	}
	
	//####################################################################################################
	// Selecting algorithm type (dropDown box in Wizard3 from FORM3 - defaultSetOfFilesWizard3)
	public static int algorithmIndex;
	public static void setAlgorithmIndex (int algorithmIndex) {
		formValues.algorithmIndex = algorithmIndex;
	}
	public static int getAlgorithmIndex () {
		return algorithmIndex;
	}
	
	//####################################################################################################
	// Displaying output progress area (textArea in Wizard3 from FORM3 - defaultSetOfFilesWizard3)
	public static String displayOutputProgressTextArea;
	public static String getDisplayOutputProgressTextArea () {
		return displayOutputProgressTextArea;
	}
	public static void setDisplayOutputProgressTextArea (String displayOutputProgressTextArea) {
		formValues.displayOutputProgressTextArea = displayOutputProgressTextArea;
	}

	//####################################################################################################
	// Files required for analysis window and general statistics window (in Wizard4 and wizard5 : analyseAllotmentsWizard4, getGeneralStatisticsOfAllotmentsWizard5)
	public static String aggregatedStatisticsFileName;

    public static void setAggregatedStatisticsFileName(String aggregatedStatisticsFileName) {
        formValues.aggregatedStatisticsFileName = aggregatedStatisticsFileName;
    }

    public static String getAggregatedStatisticsFileName() {
		return aggregatedStatisticsFileName;
	}

	public static  String batchwiseAllotmentStatisticsFileName;
	public static String getBatchwiseAllotmentStatisticsFileName() {
		return batchwiseAllotmentStatisticsFileName;
	}
	public static void setBatchwiseAllotmentStatisticsFileName(String batchwiseAllotmentStatisticsFileName) {
		formValues.batchwiseAllotmentStatisticsFileName = batchwiseAllotmentStatisticsFileName;
	}

	public static String allotmentsFileName;
	public static String getAllotmentsFileName() {
		return allotmentsFileName;
	}
	public static void setAllotmentsFileName(String allotmentsFileName) {
		formValues.allotmentsFileName = allotmentsFileName;
	}

	public static String allottedMandatedElectiveDetailsFileName;
    public static String getAllottedMandatedElectiveDetailsFileName() {
        return allottedMandatedElectiveDetailsFileName;
    }
    public static void setAllottedMandatedElectiveDetailsFileName(String allottedMandatedElectiveDetailsFileName) {
        formValues.allottedMandatedElectiveDetailsFileName = allottedMandatedElectiveDetailsFileName;
    }

    public static String perStudentAllottedStatisticsFileName;
	public static String getPerStudentAllottedStatsFileName() {
		return perStudentAllottedStatisticsFileName;
	}
	public static void setPerStudentAllottedStatisticsFileName(String perStudentAllottedStatsFileName) {
		formValues.perStudentAllottedStatisticsFileName = perStudentAllottedStatsFileName;
	}

	public static String perStudentAllottedCoursesFileName;
	public static String getPerStudentAllottedCoursesFileName() {
		return perStudentAllottedCoursesFileName;
	}
	public static void setPerStudentAllottedCoursesFileName(String perStudentAllottedCoursesFileName) {
		formValues.perStudentAllottedCoursesFileName = perStudentAllottedCoursesFileName;
	}

	public static String rejectionReasonsFileName;
	public static String getRejectionReasonsFileName() {
		return rejectionReasonsFileName;
	}
	public static void setRejectionReasonsFileName(String rejectionReasonsFileName) {
		formValues.rejectionReasonsFileName = rejectionReasonsFileName;
	}

	public static String courseListNameWithPath;
	public static String getCourseListNameWithPath() {
		return courseListNameWithPath;
	}
	public static void setCourseListNameWithPath(String courseListNameWithPath) {
		formValues.courseListNameWithPath = courseListNameWithPath;
	}

	public static String perCourseAllottedStatisticsFileNameWithPath;
	public static String getPerCourseAllottedStatisticsFileNameWithPath() {
		return perCourseAllottedStatisticsFileNameWithPath;
	}
	public static void setPerCourseAllottedStatisticsFileNameWithPath(String perCourseAllottedStatisticsFileNameWithPath) {
		formValues.perCourseAllottedStatisticsFileNameWithPath = perCourseAllottedStatisticsFileNameWithPath;
	}

}


