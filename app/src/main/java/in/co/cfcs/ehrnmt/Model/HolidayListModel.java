package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 10-10-2017.
 */

public class HolidayListModel
{
    public String HolidayName;
    public String HolidayDate;
    public String HolidayType;
    public String description;

    public HolidayListModel(String holidayName, String holidayDate, String holidayType, String description) {
        HolidayName = holidayName;
        HolidayDate = holidayDate;
        HolidayType = holidayType;
        this.description = description;
    }

    public String getHolidayName() {
        return HolidayName;
    }

    public String getHolidayDate() {
        return HolidayDate;
    }

    public String getHolidayType() {
        return HolidayType;
    }

    public String getDescription() {
        return description;
    }
}
