package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 28-10-2017.
 */

public class ProficiencySpiinerModel
{
    public String proficiencyName;
    public String proficiencyId;

    public ProficiencySpiinerModel(String proficiencyName, String proficiencyId) {
        this.proficiencyName = proficiencyName;
        this.proficiencyId = proficiencyId;
    }

    public String getProficiencyName() {
        return proficiencyName;
    }

    public String getProficiencyId() {
        return proficiencyId;
    }

    @Override
    public String toString() {
        return getProficiencyName();
    }
}
