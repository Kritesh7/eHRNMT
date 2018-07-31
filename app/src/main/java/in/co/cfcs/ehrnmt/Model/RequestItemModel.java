package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 12-10-2017.
 */

public class RequestItemModel
{
    public String itemName;
    public String itemQuantity;
    public String itemRemark;
    public String ItemID;

    public RequestItemModel(String itemName, String itemQuantity, String itemRemark, String itemID) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemRemark = itemRemark;
        ItemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemRemark() {
        return itemRemark;
    }

    public String getItemID() {
        return ItemID;
    }
}
