package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 27-10-2017.
 */

public class CountryModel
{
    public String countryId;
    public String countryName;

    public CountryModel(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return getCountryName();
    }
}
