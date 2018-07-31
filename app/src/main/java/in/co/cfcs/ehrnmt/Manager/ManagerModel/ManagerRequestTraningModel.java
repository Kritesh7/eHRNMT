package in.co.cfcs.ehrnmt.Manager.ManagerModel;

/**
 * Created by Admin on 21-11-2017.
 */

public class ManagerRequestTraningModel
{
    public String domainName;
    public String courseName;
    public String startDate;
    public String endDate;
    public String proficenacyName;
    public String empName;
    public String empId;
    public String Status;

    public ManagerRequestTraningModel(String domainName, String courseName, String startDate,
                                      String endDate, String proficenacyName, String empName, String empId,String Status) {
        this.domainName = domainName;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.proficenacyName = proficenacyName;
        this.empName = empName;
        this.empId = empId;
        this.Status = Status;
    }

    public String getStatus() {
        return Status;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getProficenacyName() {
        return proficenacyName;
    }

    public String getEmpName() {
        return empName;
    }

    public String getEmpId() {
        return empId;
    }
}
