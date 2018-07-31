package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 18-11-2017.
 */

public class AttendanceLogDetailsModel
{
    public String name;
    public String empId;
    public String designation;
    public String punchTime;
    public String punchDate;
    public String punchType;
    public String punchLocation;
    public String remark;
    public String profilePic;
    public String zoneName;
    public String approvalStatus;
    public String approvalDate;
    public String approvalBy;

    public AttendanceLogDetailsModel(String name, String empId, String designation, String punchTime, String punchDate,
                                     String punchType, String punchLocation, String remark, String profilePic, String zoneName, String approvalStatus, String approvalDate , String approvalBy) {
        this.name = name;
        this.empId = empId;
        this.designation = designation;
        this.punchTime = punchTime;
        this.punchDate = punchDate;
        this.punchType = punchType;
        this.punchLocation = punchLocation;
        this.remark = remark;
        this.profilePic = profilePic;
        this.zoneName = zoneName;
        this.approvalStatus=approvalStatus;
        this.approvalDate=approvalDate;
        this.approvalBy=approvalBy;
    }



    public String getProfilePic() {
        return profilePic;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getName() {
        return name;
    }

    public String getEmpId() {
        return empId;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPunchTime() {
        return punchTime;
    }

    public String getPunchDate() {
        return punchDate;
    }

    public String getPunchType() {
        return punchType;
    }

    public String getPunchLocation() {
        return punchLocation;
    }

    public String getRemark() {
        return remark;
    }

    public String getApprovalStatus(){return  approvalStatus;}

    public String getApprovalDate(){return  approvalDate;}

    public String getApprovalBy(){return approvalBy;}


}
