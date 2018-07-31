package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 25-10-2017.
 */

public class HotelTypeModel
{
    public String hotelTypeName;
    public String hotelTypeId;

    public HotelTypeModel(String hotelTypeName, String hotelTypeId) {
        this.hotelTypeName = hotelTypeName;
        this.hotelTypeId = hotelTypeId;
    }

    public String getHotelTypeName() {
        return hotelTypeName;
    }

    public String getHotelTypeId() {
        return hotelTypeId;
    }

    @Override
    public String toString() {
        return getHotelTypeName();
    }
}
