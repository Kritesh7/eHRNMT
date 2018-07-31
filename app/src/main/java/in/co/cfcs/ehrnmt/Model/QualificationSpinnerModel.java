package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 30-10-2017.
 */

public class QualificationSpinnerModel
{
    public String qualification;
    public String qualificationId;

    public QualificationSpinnerModel(String qualification, String qualificationId) {
        this.qualification = qualification;
        this.qualificationId = qualificationId;
    }

    public String getQualification() {
        return qualification;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    @Override
    public String toString() {
        return getQualification();
    }
}
