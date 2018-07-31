package in.co.cfcs.ehrnmt.Manager.ManagerModel;

/**
 * Created by Admin on 14-11-2017.
 */

public class TeamFilterModel
{
    public String adminId;
    public String name;

    public TeamFilterModel(String adminId, String name) {
        this.adminId = adminId;
        this.name = name;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
