package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 14-10-2017.
 */

public class SendListModel
{
    public String ItemID;
    public String ItemName;
    public String Qty;
    public String Remark;

    public SendListModel(String itemID, String itemName, String qty, String remark) {
        ItemID = itemID;
        ItemName = itemName;
        Qty = qty;
        Remark = remark;
    }

    public String getItemID() {
        return ItemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getQty() {
        return Qty;
    }

    public String getRemark() {
        return Remark;
    }
}
