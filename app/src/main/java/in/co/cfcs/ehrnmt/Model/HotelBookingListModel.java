package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 18-09-2017.
 */

public class HotelBookingListModel
{
    public String empName;
    public String cityName;
    public String requestDate;
    public String checkInDate;
    public String checkInTime;
    public String checkOutDate;
    public String staus;
    public String followUpDate;
    public String BID;
    public String Visibility;
    public String EmpRemark;
    public String HotelTypeId;
    public String AppStatus;

    public HotelBookingListModel(String empName, String cityName, String requestDate, String checkInDate,
                                 String checkInTime, String checkOutDate, String staus, String followUpDate, String BID,
                                 String Visibility, String EmpRemark, String HotelTypeId, String AppStatus) {
        this.empName = empName;
        this.cityName = cityName;
        this.requestDate = requestDate;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.checkOutDate = checkOutDate;
        this.staus = staus;
        this.followUpDate = followUpDate;
        this.BID = BID;
        this.Visibility= Visibility;
        this.EmpRemark = EmpRemark;
        this.HotelTypeId = HotelTypeId;
        this.AppStatus =AppStatus;
    }

    public String getAppStatus() {
        return AppStatus;
    }

    public String getHotelTypeId() {
        return HotelTypeId;
    }

    public String getEmpRemark() {
        return EmpRemark;
    }

    public String getVisibility() {
        return Visibility;
    }

    public String getEmpName() {
        return empName;
    }

    public String getBID() {
        return BID;
    }

    public String getCityName() {
        return cityName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getStaus() {
        return staus;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }
}
