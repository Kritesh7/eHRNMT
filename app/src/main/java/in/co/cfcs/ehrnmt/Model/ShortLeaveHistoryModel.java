package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 09-10-2017.
 */

public class ShortLeaveHistoryModel
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
    public String IsDeleteable;

    public ShortLeaveHistoryModel(String UserName, String leaveApplication_Id, String leaveTypeName, String startDate, String timeFrom,
                                  String timeTo, String appliedDate, String statusText, String commentText, String IsDeleteable) {
        this.UserName = UserName;
        LeaveApplication_Id = leaveApplication_Id;
        LeaveTypeName = leaveTypeName;
        StartDate = startDate;
        TimeFrom = timeFrom;
        TimeTo = timeTo;
        AppliedDate = appliedDate;
        StatusText = statusText;
        CommentText = commentText;
        this.IsDeleteable = IsDeleteable;
    }

    public String getUserName() {
        return UserName;
    }

    public String getIsDeleteable() {
        return IsDeleteable;
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
