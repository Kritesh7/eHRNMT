package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 07-10-2017.
 */

public class LeaveYearTypeModel
{
    public String LeaveYear;
    public String LeaveYearText;

    public LeaveYearTypeModel(String leaveYear, String leaveYearText) {
        LeaveYear = leaveYear;
        LeaveYearText = leaveYearText;
    }

    public String getLeaveYear() {
        return LeaveYear;
    }

    public String getLeaveYearText() {
        return LeaveYearText;
    }

    @Override
    public String toString() {
        return getLeaveYearText();
    }
}
