package in.co.cfcs.ehrnmt.Model;


public class yValueOfBarChart {

    public String LeaveTypeName;

    public String LeaveBalance;

    public String LeaveAvail;

    public yValueOfBarChart(String leaveTypeName, String leaveBalance, String leaveAvail) {

        this.LeaveTypeName = leaveTypeName;
        this.LeaveBalance = leaveBalance;
        this.LeaveAvail = leaveAvail;
    }


    public String getLeaveTypeName() {
        return LeaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.LeaveTypeName = leaveTypeName;
    }

    public String getLeaveBalance() {
        return LeaveBalance;
    }

    public void setLeaveBalance(String leaveBalance) {
        this.LeaveBalance = leaveBalance;
    }

    public String getLeaveAvail() {
        return LeaveAvail;
    }

    public void setLeaveAvail(String leaveAvail) {
        this.LeaveAvail = leaveAvail;
    }

}
