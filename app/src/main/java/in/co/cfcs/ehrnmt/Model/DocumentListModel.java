package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 18-09-2017.
 */

public class DocumentListModel
{

    public String employName;
    public String zoneName;
    public String quantity;
    public String RequestDate;
    public String IdleClousersDate;
    public String followUpDate;
    public String status;
    public String RID;
    public String ItemCatID;
    public String AppStatus;


    public DocumentListModel(String employName, String zoneName, String quantity, String requestDate,
                             String idleClousersDate, String followUpDate, String status, String RID, String itemCatID, String AppStatus) {
        this.employName = employName;
        this.zoneName = zoneName;
        this.quantity = quantity;
        RequestDate = requestDate;
        IdleClousersDate = idleClousersDate;
        this.followUpDate = followUpDate;
        this.status = status;
        this.RID = RID;
        ItemCatID = itemCatID;
        this.AppStatus = AppStatus;
    }

    public String getAppStatus() {
        return AppStatus;
    }

    public String getEmployName() {
        return employName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public String getIdleClousersDate() {
        return IdleClousersDate;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRID() {
        return RID;
    }

    public String getItemCatID() {
        return ItemCatID;
    }
}
