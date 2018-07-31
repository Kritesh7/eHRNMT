package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 02-11-2017.
 */

public class ContactModel
{
    public String addressType;
    public String address;
    public String city;
    public String state;
    public String postalcode;
    public String counteryName;
    public String lastUpdate;
    public String recordId;

    public ContactModel(String addressType, String address, String city, String state,
                        String postalcode, String counteryName, String lastUpdate, String recordId) {

        this.addressType = addressType;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalcode = postalcode;
        this.counteryName = counteryName;
        this.lastUpdate = lastUpdate;
        this.recordId = recordId;
    }

    public String getAddressType() {
        return addressType;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getRecordId() {
        return recordId;
    }
}
