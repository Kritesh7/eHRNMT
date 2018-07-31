package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 30-10-2017.
 */

public class DisciplineSpinnerModel
{
    public String descipline;
    public String desciplineId;

    public DisciplineSpinnerModel(String descipline, String desciplineId) {
        this.descipline = descipline;
        this.desciplineId = desciplineId;
    }

    public String getDescipline() {
        return descipline;
    }

    public String getDesciplineId() {
        return desciplineId;
    }

    @Override
    public String toString() {
        return getDescipline();
    }
}
