package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 21-09-2017.
 */

public class PreviousExpreinceModel
{

    public String companyName;
    public String joiningDate;
    public String jobDescription;
    public String jobPeriode;
    public String designation;
    public String editable;
    public String Deleteable;
    public String Status;
    public String Comments;
    public String RecordID;
    public String RelievingDate;
    public String jobYearId;
    public String jobMonthId;

    public PreviousExpreinceModel(String companyName, String joiningDate, String jobDescription, String jobPeriode, String designation,
                                  String editable , String deleteable, String status, String comments, String RecordID,
                                  String RelievingDate, String jobYearId, String jobMonthId) {
        this.companyName = companyName;
        this.joiningDate = joiningDate;
        this.jobDescription = jobDescription;
        this.jobPeriode = jobPeriode;
        this.designation = designation;
        this.editable = editable;
        this.Deleteable = deleteable;
        this.Status = status;
        this.Comments = comments;
        this.RecordID = RecordID;
        this.RelievingDate = RelievingDate;
        this.jobYearId = jobYearId;
        this.jobMonthId = jobMonthId;
    }

    public String getJobYearId() {
        return jobYearId;
    }

    public String getJobMonthId() {
        return jobMonthId;
    }

    public String getRelievingDate() {
        return RelievingDate;
    }

    public String getRecordID() {
        return RecordID;
    }

    public String getStatus() {
        return Status;
    }

    public String getComments() {
        return Comments;
    }

    public String getEditable() {
        return editable;
    }

    public String getDeleteable() {
        return Deleteable;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getJobPeriode() {
        return jobPeriode;
    }

    public String getDesignation() {
        return designation;
    }
}
