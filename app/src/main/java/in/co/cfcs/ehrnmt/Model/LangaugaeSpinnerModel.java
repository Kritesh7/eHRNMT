package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 30-10-2017.
 */

public class LangaugaeSpinnerModel
{
    public String langaugeName;
    public String langaugeId ;

    public LangaugaeSpinnerModel(String langaugeName, String langaugeId) {
        this.langaugeName = langaugeName;
        this.langaugeId = langaugeId;
    }

    public String getLangaugeName() {
        return langaugeName;
    }

    public String getLangaugeId() {
        return langaugeId;
    }

    @Override
    public String toString() {
        return getLangaugeName();
    }
}
