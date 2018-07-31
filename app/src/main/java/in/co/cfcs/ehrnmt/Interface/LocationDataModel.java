package in.co.cfcs.ehrnmt.Interface;

public class LocationDataModel {


    public String Lat;

    public String Lng;

    public String WaitCounter;

    public String ReportTime;

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        this.Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        this.Lng = lng;
    }

    public String getWaitCounter() {
        return WaitCounter;
    }

    public void setWaitCounter(String waitCounter) {
        this.WaitCounter = waitCounter;
    }

    public String getReportTime() {
        return ReportTime;
    }

    public void setReportTime(String reportTime) {
        this.ReportTime = reportTime;
    }

}
