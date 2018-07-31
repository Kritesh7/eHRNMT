package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 25-10-2017.
 */

public class CabCityModel
{
    public String cityName;
    public String cityId;

    public CabCityModel(String cityName, String cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityId() {
        return cityId;
    }

    @Override
    public String toString() {
        return getCityName();
    }
}
