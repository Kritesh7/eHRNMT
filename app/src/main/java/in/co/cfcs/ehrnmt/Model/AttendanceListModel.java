package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 10-10-2017.
 */
public class AttendanceListModel
{

    public String Name;
    public String AttendanceLogID;
    public String Date;
    public String InTime;
    public String OutTime;
    public String WorkTime;
    public String Halfday;
    public String LateArrival;
    public String EarlyLeaving;
    public String Status;
    public String IsRequest;


    public AttendanceListModel(String Name, String attendanceLogID, String date, String inTime,
                               String outTime, String workTime, String halfday, String lateArrival, String earlyLeaving, String status,
                               String IsRequest) {
        this.Name = Name;
        AttendanceLogID = attendanceLogID;
        Date = date;
        InTime = inTime;
        OutTime = outTime;
        WorkTime = workTime;
        Halfday = halfday;
        LateArrival = lateArrival;
        EarlyLeaving = earlyLeaving;
        Status = status;
        IsRequest = IsRequest;
    }

    public String getName() {
        return Name;
    }

    public String getIsRequest() {
        return IsRequest;
    }

    public String getAttendanceLogID() {
        return AttendanceLogID;
    }

    public String getDate() {
        return Date;
    }

    public String getInTime() {
        return InTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public String getWorkTime() {
        return WorkTime;
    }

    public String getHalfday() {
        return Halfday;
    }

    public String getLateArrival() {
        return LateArrival;
    }

    public String getEarlyLeaving() {
        return EarlyLeaving;
    }

    public String getStatus() {
        return Status;
    }
}
