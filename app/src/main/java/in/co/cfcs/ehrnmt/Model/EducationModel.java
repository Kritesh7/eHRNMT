package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 21-09-2017.
 */

public class EducationModel
{
    public String qualification;
    public String descipline;
    public String passingDate;
    public String institute;
    public String courseType;
    public String highestDegree;
    public String recordId;
    public String comment;
    public String statusTxt;
    public String editable;
    public String deletable;

    public EducationModel(String qualification, String descipline, String passingDate, String institute, String courseType, String highestDegree, String recordId,
                          String comment, String statusTxt, String editable, String deletable) {
        this.qualification = qualification;
        this.descipline = descipline;
        this.passingDate = passingDate;
        this.institute = institute;
        this.courseType = courseType;
        this.highestDegree = highestDegree;
        this.recordId = recordId;
        this.comment = comment;
        this.statusTxt = statusTxt;
        this.editable = editable;
        this.deletable = deletable;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getComment() {
        return comment;
    }

    public String getStatusTxt() {
        return statusTxt;
    }

    public String getEditable() {
        return editable;
    }

    public String getDeletable() {
        return deletable;
    }

    public String getQualification() {
        return qualification;
    }

    public String getDescipline() {
        return descipline;
    }

    public String getPassingDate() {
        return passingDate;
    }

    public String getInstitute() {
        return institute;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getHighestDegree() {
        return highestDegree;
    }
}
