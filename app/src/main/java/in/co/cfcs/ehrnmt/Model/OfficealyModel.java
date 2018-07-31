package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 20-09-2017.
 */

public class OfficealyModel
{
    public String documentType;
    public String noOfDocuments;
    public String issuesDate;
    public String expiryDate;
    public String placeOfIssues;
    public String FileNameText;
    public String Deleteable;
    public String RecordID;

    public OfficealyModel(String documentType, String noOfDocuments, String issuesDate, String expiryDate, String placeOfIssues,
                          String FileNameText, String Deleteable, String RecordID) {
        this.documentType = documentType;
        this.noOfDocuments = noOfDocuments;
        this.issuesDate = issuesDate;
        this.expiryDate = expiryDate;
        this.placeOfIssues = placeOfIssues;
        this.FileNameText = FileNameText;
        this.Deleteable = Deleteable;
        this.RecordID = RecordID;
    }

    public String getRecordID() {
        return RecordID;
    }

    public String getFileNameText() {
        return FileNameText;
    }

    public String getDeleteable() {
        return Deleteable;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getNoOfDocuments() {
        return noOfDocuments;
    }

    public String getIssuesDate() {
        return issuesDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getPlaceOfIssues() {
        return placeOfIssues;
    }
}
