package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 07-10-2017.
 */

public class LeaveTypeModel
{
    public String LeaveID;
    public String LeaveTypeName;

    public LeaveTypeModel(String leaveID, String leaveTypeName) {
        LeaveID = leaveID;
        LeaveTypeName = leaveTypeName;
    }

    public String getLeaveID() {
        return LeaveID;
    }

    public String getLeaveTypeName() {
        return LeaveTypeName;
    }

    @Override
    public String toString() {
        return getLeaveTypeName();
    }
}
