package in.co.cfcs.ehrnmt.Manager.ManagerModel;

/**
 * Created by Admin on 15-11-2017.
 */

public class ManagerTeamAvearageModel
{
    public String empName;
    public String empId;
    public String avgMonth;
    public String avgYear;
    public String totalNoOfDays;
    public String totalNoOfHours;
    public String average;

    public ManagerTeamAvearageModel(String empName, String empId, String avgMonth, String avgYear,
                                    String totalNoOfDays, String totalNoOfHours, String average) {
        this.empName = empName;
        this.empId = empId;
        this.avgMonth = avgMonth;
        this.avgYear = avgYear;
        this.totalNoOfDays = totalNoOfDays;
        this.totalNoOfHours = totalNoOfHours;
        this.average = average;
    }

    public String getEmpName() {
        return empName;
    }

    public String getEmpId() {
        return empId;
    }

    public String getAvgMonth() {
        return avgMonth;
    }

    public String getAvgYear() {
        return avgYear;
    }

    public String getTotalNoOfDays() {
        return totalNoOfDays;
    }

    public String getTotalNoOfHours() {
        return totalNoOfHours;
    }

    public String getAverage() {
        return average;
    }
}
