package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 28-10-2017.
 */

public class SkillsSpinnerModel
{
    public String skillsName;
    public String skillsId;

    public SkillsSpinnerModel(String skillsName, String skillsId) {
        this.skillsName = skillsName;
        this.skillsId = skillsId;
    }

    public String getSkillsName() {
        return skillsName;
    }

    public String getSkillsId() {
        return skillsId;
    }

    @Override
    public String toString() {
        return getSkillsName();
    }
}
