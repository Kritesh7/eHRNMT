package in.co.cfcs.ehrnmt.Manager.ManagerModel;

public class ManagerAttendanceRequestModel {

    private String ZoneName;

    private String ApprovalStatusText;

    private String EmpID;

    private String LogTime;

    private String LogDateText;

    private String UserName;

    private String DeviceLogID;

    private String LogTypeText;

    private String DesignationName;

    public ManagerAttendanceRequestModel(String deviceLogID, String userName, String empID, String designationName, String zoneName, String logDateText, String logTime, String logTypeText, String approvalStatusText) {
        this.ZoneName = zoneName;
        this.ApprovalStatusText = approvalStatusText;
        this.EmpID = empID;
        this.LogTime = logTime;
        this.LogDateText = logDateText;
        this.UserName = userName;
        this.DeviceLogID = deviceLogID;
        this.LogTypeText = logTypeText;
        this.DesignationName = designationName;
    }

    public String getZoneName ()
    {
        return ZoneName;
    }

    public void setZoneName (String ZoneName)
    {
        this.ZoneName = ZoneName;
    }

    public String getApprovalStatusText ()
    {
        return ApprovalStatusText;
    }

    public void setApprovalStatusText (String ApprovalStatusText)
    {
        this.ApprovalStatusText = ApprovalStatusText;
    }

    public String getEmpID ()
    {
        return EmpID;
    }

    public void setEmpID (String EmpID)
    {
        this.EmpID = EmpID;
    }

    public String getLogTime ()
    {
        return LogTime;
    }

    public void setLogTime (String LogTime)
    {
        this.LogTime = LogTime;
    }

    public String getLogDateText ()
    {
        return LogDateText;
    }

    public void setLogDateText (String LogDateText)
    {
        this.LogDateText = LogDateText;
    }

    public String getUserName ()
    {
        return UserName;
    }

    public void setUserName (String UserName)
    {
        this.UserName = UserName;
    }

    public String getDeviceLogID ()
    {
        return DeviceLogID;
    }

    public void setDeviceLogID (String DeviceLogID)
    {
        this.DeviceLogID = DeviceLogID;
    }

    public String getLogTypeText ()
    {
        return LogTypeText;
    }

    public void setLogTypeText (String LogTypeText)
    {
        this.LogTypeText = LogTypeText;
    }

    public String getDesignationName ()
    {
        return DesignationName;
    }

    public void setDesignationName (String DesignationName)
    {
        this.DesignationName = DesignationName;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [ZoneName = "+ZoneName+", ApprovalStatusText = "+ApprovalStatusText+", EmpID = "+EmpID+", LogTime = "+LogTime+", LogDateText = "+LogDateText+", UserName = "+UserName+", DeviceLogID = "+DeviceLogID+", LogTypeText = "+LogTypeText+", DesignationName = "+DesignationName+"]";
//    }


}
