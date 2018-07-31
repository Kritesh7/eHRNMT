package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 20-09-2017.
 */

public class DependentModel
{
    public String firstName;
    public String lastName;
    public String dob;
    public String gender;
    public String relationship;
    public String recordId;

    public DependentModel(String firstName, String lastName, String dob, String gender, String relationship, String recordId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.relationship = relationship;
        this.recordId = recordId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getRelationship() {
        return relationship;
    }
}
