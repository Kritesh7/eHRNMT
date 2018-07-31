package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 07-10-2017.
 */

public class NationnalityModel
{
    public String nameNationlaity;
    public String nationlityId;

    public NationnalityModel(String nameNationlaity, String nationlityId) {
        this.nameNationlaity = nameNationlaity;
        this.nationlityId = nationlityId;
    }

    public String getNameNationlaity() {
        return nameNationlaity;
    }

    public String getNationlityId() {
        return nationlityId;
    }

    @Override
    public String toString() {
        return getNameNationlaity();
    }
}
