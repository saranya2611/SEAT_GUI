package models;

/**
 * 	An object of this class is used to specify any maxCredit limit other than 60, 
 * for a student from this 'department' and 'year'.
 *  (For example: 5th semester CS students may be allowed to take 63 credits)
 *  @author akshay
 *
 */
public class DepartmentSpecificMaxCreditLimit {
	public String department;
	public int year;
	public int maxCreditLimit; 
	
	public DepartmentSpecificMaxCreditLimit(String inp_department,int inp_year, int inp_maxCreditLimit){
		department = inp_department;
		year = inp_year;
		maxCreditLimit = inp_maxCreditLimit;
	}
}
