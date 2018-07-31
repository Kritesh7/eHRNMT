package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 27-10-2017.
 */

public class EmergencyContactModel
{
    public String title;
    public String name;
    public String address;
    public String city;
    public String state;
    public String postalcode;
    public String counteryName;
    public String telephoneNumber;
    public String mobileNumber;
    public String email;
    public String relationshipname;
    public String lastUpdate;
    public String type;
    public String recordId;

    public EmergencyContactModel(String title, String name, String address, String city, String state, String postalcode, String counteryName, String telephoneNumber, String mobileNumber,
                                 String email, String relationshipname, String lastUpdate, String type, String recordId) {
        this.title = title;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalcode = postalcode;
        this.counteryName = counteryName;
        this.telephoneNumber = telephoneNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.relationshipname = relationshipname;
        this.lastUpdate = lastUpdate;
        this.type = type;
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getCounteryName() {
        return counteryName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getRelationshipname() {
        return relationshipname;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getType() {
        return type;
    }

    public String getRecordId() {
        return recordId;
    }
}
