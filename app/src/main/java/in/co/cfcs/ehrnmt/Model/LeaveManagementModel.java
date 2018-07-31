package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 18-09-2017.
 */

public class LeaveManagementModel
{
    public String UserName;
    public String leaveType;
    public String startDate;
    public String endDate;
    public String appliedOn;
    public String status;
    public String LeaveApplication_Id;
    public String Noofdays;
    public String IsDeleteable;
    public String CancelStatus;
    public String StatusId;

    public LeaveManagementModel(String userName, String leaveType, String startDate, String endDate, String appliedOn, String status, String leaveApplication_Id,
                                String noofdays, String isDeleteable, String cancelStatus, String StatusId) {
        UserName = userName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.appliedOn = appliedOn;
        this.status = status;
        LeaveApplication_Id = leaveApplication_Id;
        Noofdays = noofdays;
        IsDeleteable = isDeleteable;
        CancelStatus = cancelStatus;
        this.StatusId = StatusId;
    }

    public String getStatusId() {
        return StatusId;
    }

    public String getCancelStatus() {
        return CancelStatus;
    }

    public String getUserName() {
        return UserName;
    }

    public String getIsDeleteable() {
        return IsDeleteable;
    }

    public String getNoofdays() {
        return Noofdays;
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
}
