package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 18-09-2017.
 */

public class TraningModel
{
    public String domain;
    public String course;
    public String estimatedStartDate;
    public String estimatedEndDate;
    public String status;

    public TraningModel(String domain, String course, String estimatedStartDate, String estimatedEndDate, String status) {
        this.domain = domain;
        this.course = course;
        this.estimatedStartDate = estimatedStartDate;
        this.estimatedEndDate = estimatedEndDate;
        this.status = status;
    }

    public String getDomain() {
        return domain;
    }

    public String getCourse() {
        return course;
    }

    public String getEstimatedStartDate() {
        return estimatedStartDate;
    }

    public String getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public String getStatus() {
        return status;
    }
}
