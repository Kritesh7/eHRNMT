package in.co.cfcs.ehrnmt.Model;

import java.io.Serializable;

/**
 * Created by Admin on 06-12-2017.
 */

public class AddNewStationoryRequestModel implements Serializable
{
    public String itemName;
    public String setMaxQuantity;
    public String itemId;
    public String quantity;
    public String remark;

    public AddNewStationoryRequestModel(String itemName, String setMaxQuantity, String itemId, String quantity, String remark) {
        this.itemName = itemName;
        this.setMaxQuantity = setMaxQuantity;
        this.itemId = itemId;
        this.quantity = quantity;
        this.remark = remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getRemark() {
        return remark;
    }

    public String getSetMaxQuantity() {
        return setMaxQuantity;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

}
