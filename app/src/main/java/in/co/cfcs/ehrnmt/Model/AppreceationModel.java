package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 25-09-2017.
 */

public class AppreceationModel
{

    public String appreceationTitle;
    public String appreceationDate;
    public String appreceationDetails;
    public String FileNameText;

    public AppreceationModel(String appreceationTitle, String appreceationDate, String appreceationDetails, String FileNameText) {
        this.appreceationTitle = appreceationTitle;
        this.appreceationDate = appreceationDate;
        this.appreceationDetails = appreceationDetails;
        this.FileNameText = FileNameText;
    }

    public String getFileNameText() {
        return FileNameText;
    }

    public String getAppreceationTitle() {
        return appreceationTitle;
    }

    public String getAppreceationDate() {
        return appreceationDate;
    }

    public String getAppreceationDetails() {
        return appreceationDetails;
    }
}


