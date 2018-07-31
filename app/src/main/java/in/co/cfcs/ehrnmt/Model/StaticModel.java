package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 10-11-2017.
 */

public class StaticModel
{
    public String time;
    public String source;
    public String destination;

    public StaticModel(String time, String source, String destination) {
        this.time = time;
        this.source = source;
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
}
