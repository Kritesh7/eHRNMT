package in.co.cfcs.ehrnmt.Manager.ManagerModel;

/**
 * Created by Admin on 14-11-2017.
 */

public class ManagerEmployeeSpinnerModel
{
    public String empId;
    public String empName;

    public ManagerEmployeeSpinnerModel(String empId, String empName) {
        this.empId = empId;
        this.empName = empName;
    }

    public String getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    @Override
    public String toString() {
        return getEmpName();
    }
}
