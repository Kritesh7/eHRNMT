package in.co.cfcs.ehrnmt.Model;

/**
 * Created by Admin on 06-12-2017.
 */

public class getQuantAndRemarkModel
{
    public String itemName;
    public String itemQuantity;
    public String itemRemark;
    public String itemId;

    public getQuantAndRemarkModel(String itemName, String itemQuantity, String itemRemark, String itemId) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemRemark = itemRemark;
        this.itemId = itemId;
    }


    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
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

    public String getItemId() {
        return itemId;
    }
}
