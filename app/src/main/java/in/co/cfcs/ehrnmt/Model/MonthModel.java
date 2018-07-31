package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 11-10-2017.
 */

public class MonthModel
{
    public int monthId;
    public String monthName;

    public MonthModel(int monthId, String monthName) {
        this.monthId = monthId;
        this.monthName = monthName;
    }

    public int getMonthId() {
        return monthId;
    }

    public String getMonthName() {
        return monthName;
    }

    @Override
    public String toString() {
        return getMonthName();
    }
}
