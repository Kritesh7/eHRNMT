package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 26-10-2017.
 */

public class BloodGroupModel
{
    public String bloodGroupId;
    public String bloodGroupName;

    public BloodGroupModel(String bloodGroupId, String bloodGroupName) {
        this.bloodGroupId = bloodGroupId;
        this.bloodGroupName = bloodGroupName;
    }

    public String getBloodGroupId() {
        return bloodGroupId;
    }

    public String getBloodGroupName() {
        return bloodGroupName;
    }

    @Override
    public String toString() {
        return getBloodGroupName();
    }
}
