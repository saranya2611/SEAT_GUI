package models;

/**
 * An object of this class is used to represent information about
 * any batch which has been recommended to be allotted a particular elective type
 * (eg. CS department recommends an HS elective in 3rd semester)
 *
 * @author vedant
 */
public class BatchSpecificMandatedElectives {
    public String batch;
    public String electiveType; //Ex. "HS" or "MA"

    public BatchSpecificMandatedElectives(String inp_batch, String inp_electiveType) {
        batch = inp_batch;
        electiveType = inp_electiveType;
    }

    public String getBatch() {
        return batch;
    }

    public String getElectiveType() {
        return electiveType;
    }
}
