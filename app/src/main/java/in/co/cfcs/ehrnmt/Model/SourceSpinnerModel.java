package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 28-10-2017.
 */

public class SourceSpinnerModel
{
    public String sourceName;
    public String sourceId;

    public SourceSpinnerModel(String sourceName, String sourceId) {
        this.sourceName = sourceName;
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceId() {
        return sourceId;
    }

    @Override
    public String toString() {
        return getSourceName();
    }
}
