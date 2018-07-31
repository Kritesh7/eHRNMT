package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 25-10-2017.
 */

public class HotelNameModel
{
    public String hotelName;
    public String hotelId;

    public HotelNameModel(String hotelName, String hotelId) {
        this.hotelName = hotelName;
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelId() {
        return hotelId;
    }

    @Override
    public String toString() {
        return getHotelName();
    }
}
