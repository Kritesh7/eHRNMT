package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 25-09-2017.
 */

public class WarningModel
{
    public String warningDate;
    public String warningDetails;
    public String warningTitle;
    public String FileNameText;

    public WarningModel(String warningDate, String warningDetails, String warningTitle, String FileNameText) {
        this.warningDate = warningDate;
        this.warningDetails = warningDetails;
        this.warningTitle = warningTitle;
        this.FileNameText = FileNameText;
    }

    public String getFileNameText() {
        return FileNameText;
    }

    public String getWarningTitle() {
        return warningTitle;
    }

    public String getWarningDate() {
        return warningDate;
    }

    public String getWarningDetails() {
        return warningDetails;
    }
}
