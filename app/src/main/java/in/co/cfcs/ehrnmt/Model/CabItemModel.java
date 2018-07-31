package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 25-10-2017.
 */

public class CabItemModel
{
    public String bookTime;
    public String sourceAdd;
    public String destinationAdd;

    public CabItemModel(String bookTime, String sourceAdd, String destinationAdd) {
        this.bookTime = bookTime;
        this.sourceAdd = sourceAdd;
        this.destinationAdd = destinationAdd;
    }

    public String getBookTime() {
        return bookTime;
    }

    public String getSourceAdd() {
        return sourceAdd;
    }

    public String getDestinationAdd() {
        return destinationAdd;
    }
}
