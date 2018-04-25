package services;

import models.Student;
import models.StudentPreference;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This Class is simply a collection of some common functions used by algorithms.
 * 1. ENSURE THAT THE SEMANTICS OF EACH FUNCTION IS UNDERSTOOD BEFORE YOU USE IT.
 * <p>
 * 2. This was initially developed for the 3 iterative algorithms : IterativeHRalgorithm,FirstPreferenceAllotmentAlgorithm,SlotBasedHRalgorithm
 * HENCE AVOID MAKING CHANGES TO THE ALREADY IMPLEMENTED METHODS SINCE IT MAY CAUSE ANY OF THESE
 * 3 ALGORITHMS TO BREAK.
 * Make changes to the methods only if you are absolutely sure that they have a bug or need changing,
 * and will not break the previously implemented algorithms. If you need a functionality which is slightly
 * different from any of the implemented functions, just implement a new function.
 */
public class CommonAlgorithmUtilities {

    /* (Used by Iterative algorithms)
     * This function iterates through the students and removes students who have no courses left on their
     * preference list (i.e. coursesUnallotted)
     */
    public static void removeStudentsWithNoCourseLeftToApplyTo(ArrayList<Student> studentsWithCapacityLeft) {
        //Note that we must use an iterator object to iterate, if we are also doing deletes along the way.
        for (Iterator<Student> iterator = studentsWithCapacityLeft.iterator(); iterator.hasNext(); ) {
            Student s = iterator.next();
            if (s.coursesUnallotted.size() == 0) {
                iterator.remove();
            }
        }
    }

    /* (Used by Iterative algorithms)
     * This function iterates through the student preferences and removes those which have a credit
     * requirement which is greater than the number of elective credits the student has left
     */
    public static void removeStudentPreferencesWithCreditRequirementExceedingCreditLimit(ArrayList<Student> studentsWithCapacityLeft) {
        //Note that we must use an iterator object to iterate, if we are also doing deletes along the way.
        for (Student s : studentsWithCapacityLeft) {
            for (Iterator<StudentPreference> iterator = s.coursesUnallotted.iterator(); iterator.hasNext(); ) {
                StudentPreference sp = iterator.next();
                if (sp.getCourseObj().getCredits() > s.electiveCreditsLeft) {
                    iterator.remove();
                }
            }
        }
    }

    /* (Used by Iterative algorithms)
     * This function iterates through the student preferences and removes those which have a slot
     * conflict with the course allotted in the current iteration (i.e. courseAllottedInCurrentIteration)
     */
    public static void removeStudentPreferencesWithSlotConflict(ArrayList<Student> studentsWithCapacityLeft) {
        //Note that we must use an iterator object to iterate, if we are also doing deletes along the way.
        for (Student s : studentsWithCapacityLeft) {
            for (Iterator<StudentPreference> iterator = s.coursesUnallotted.iterator(); iterator.hasNext(); ) {
                StudentPreference sp = iterator.next();
                if (s.courseAllottedInCurrentIteration != null) {
                    if (sp.getCourseObj().slotClashWithCourse(s.courseAllottedInCurrentIteration.getCourseObj()) == true) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    /* (Used by Iterative algorithms)
     * This function iterates through the student preferences and removes those which have a colour
     * conflict with the course allotted in the current iteration (i.e. courseAllottedInCurrentIteration)
     */
    public static void removeStudentPreferencesWithColourConflict(ArrayList<Student> studentsWithCapacityLeft) {
        //Note that we must use an iterator object to iterate, if we are also doing deletes along the way.
        for (Student s : studentsWithCapacityLeft) {
            for (Iterator<StudentPreference> iterator = s.coursesUnallotted.iterator(); iterator.hasNext(); ) {
                StudentPreference sp = iterator.next();
                if (s.courseAllottedInCurrentIteration != null) {
                    if (sp.getColour() == s.courseAllottedInCurrentIteration.getColour()) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    /* (Used by Iterative algorithms)
     * This function iterates through the students and freezes the allotment made in this iteration. This
     * means that the courseAllottedInCurrentIteration will be added to the 'coursesAllotted' of the
     * student. It will never be removed from here for the iterative algorithms.
     */
    public static void freezeAllotmentOfCurrentIteration(ArrayList<Student> studentsWithCapacityLeft) {
        //Note that we must use an iterator object to iterate, if we are also doing deletes along the way.
        for (Student s : studentsWithCapacityLeft) {
            if (s.courseAllottedInCurrentIteration != null) {
                s.orderedListOfcoursesAllotted.add(s.courseAllottedInCurrentIteration);
            }
        }
    }

    /* (Used by Iterative algorithms)
     * This function iterates through the students and recalculates the elective credits left for
     * each student in case the student got allotted a course in the current iteration.
     * It simply subtracts the number of credits of the courseAllottedInCurrentIteration
     */
    public static void recalculateStudentsLeftoverCredits(ArrayList<Student> studentsWithCapacityLeft) {
        //Note that we must use an iterator object to iterate, if we are also doing deletes along the way.
        for (Student s : studentsWithCapacityLeft) {
            if (s.courseAllottedInCurrentIteration != null) {
                s.electiveCreditsLeft -= s.courseAllottedInCurrentIteration.getCourseObj().getCredits();
            }
        }

    }

    //Just a helper function used to do a deep array copy
    public static <T> ArrayList<T> copyArrayList(ArrayList<T> inputList) {
        ArrayList<T> copiedList = new ArrayList<T>();

        for (int i = 0; i < inputList.size(); i++) {
            copiedList.add(inputList.get(i));
        }
        return copiedList;
    }
}
