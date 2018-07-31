package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 26-10-2017.
 */

public class PolicyTypeModel
{
    public String policyType;
    public String policyId;

    public PolicyTypeModel(String policyType, String policyId) {
        this.policyType = policyType;
        this.policyId = policyId;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getPolicyId() {
        return policyId;
    }

    @Override
    public String toString() {
        return getPolicyType();
    }
}
