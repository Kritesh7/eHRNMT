package in.co.cfcs.ehrnmt.Manager.ManagerModel;

/**
 * Created by Admin on 14-11-2017.
 */

public class ManagerLeaveRequestApproveAndRejectModel
{
    public String UserName;
    public String leaveType;
    public String startDate;
    public String endDate;
    public String appliedOn;
    public String status;
    public String LeaveApplication_Id;
    public String Noofdays;

    public ManagerLeaveRequestApproveAndRejectModel(String UserName, String leaveType, String startDate, String endDate,
                                                    String appliedOn, String status, String leaveApplication_Id, String noofdays) {
        this.UserName = UserName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.appliedOn = appliedOn;
        this.status = status;
        LeaveApplication_Id = leaveApplication_Id;
        Noofdays = noofdays;
    }

    public String getUserName() {
        return UserName;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getAppliedOn() {
        return appliedOn;
    }

    public String getStatus() {
        return status;
    }

    public String getLeaveApplication_Id() {
        return LeaveApplication_Id;
    }

    public String getNoofdays() {
        return Noofdays;
    }
}
