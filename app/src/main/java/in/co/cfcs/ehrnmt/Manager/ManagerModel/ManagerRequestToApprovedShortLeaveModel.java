package in.co.cfcs.ehrnmt.Manager.ManagerModel;

/**
 * Created by Admin on 13-11-2017.
 */

public class ManagerRequestToApprovedShortLeaveModel
{
    public String UserName;
    public String LeaveApplication_Id;
    public String LeaveTypeName;
    public String StartDate;
    public String TimeFrom;
    public String TimeTo;
    public String AppliedDate;
    public String StatusText;
    public String CommentText;


    public ManagerRequestToApprovedShortLeaveModel(String UserName , String leaveApplication_Id, String leaveTypeName, String startDate, String timeFrom,
                                  String timeTo, String appliedDate, String statusText, String commentText) {
        this.UserName = UserName;
        LeaveApplication_Id = leaveApplication_Id;
        LeaveTypeName = leaveTypeName;
        StartDate = startDate;
        TimeFrom = timeFrom;
        TimeTo = timeTo;
        AppliedDate = appliedDate;
        StatusText = statusText;
        CommentText = commentText;

    }

    public String getUserName() {
        return UserName;
    }

    public String getLeaveApplication_Id() {
        return LeaveApplication_Id;
    }

    public String getLeaveTypeName() {
        return LeaveTypeName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public String getTimeFrom() {
        return TimeFrom;
    }

    public String getTimeTo() {
        return TimeTo;
    }

    public String getAppliedDate() {
        return AppliedDate;
    }

    public String getStatusText() {
        return StatusText;
    }

    public String getCommentText() {
        return CommentText;
    }
}
