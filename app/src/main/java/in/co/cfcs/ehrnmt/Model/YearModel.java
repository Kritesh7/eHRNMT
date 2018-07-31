package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 30-10-2017.
 */

public class YearModel
{
    public String year;
    public String yearId;

    public YearModel(String year, String yearId) {
        this.year = year;
        this.yearId = yearId;
    }

    public String getYear() {
        return year;
    }

    public String getYearId() {
        return yearId;
    }

    @Override
    public String toString() {
        return getYear();
    }
}
