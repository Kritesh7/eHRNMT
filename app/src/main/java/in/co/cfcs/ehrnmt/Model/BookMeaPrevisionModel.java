package in.co.cfcs.ehrnmt.Model;

import java.io.Serializable;

/**
 * Created by Admin on 21-09-2017.
 */

public class BookMeaPrevisionModel implements Serializable {

    public String itemName;
    public String ItemID;
    public String MaxQuantity;
    public String remark;
    public String checkValue;
    public String fillQuanty;
    private boolean toKill;
    public boolean setToFill;
    public String remarkString ;
    public String quantity;

    public BookMeaPrevisionModel(String itemName, String ItemID, String MaxQuantity, String remark, String checkValue,
                                 String quantity) {
        this.itemName = itemName;
        this.ItemID = ItemID;
        this.MaxQuantity = MaxQuantity;
        this.quantity = quantity;
        this.remark = remark;
        this.checkValue = checkValue;
    }


    ///getters/setters


    public String getQuantity() {
        return quantity;
    }

    public String getRemarkString() {
        return remarkString;
    }

    public void setRemarkString(String remarkString) {
        this.remarkString = remarkString;
    }

    public boolean isSetToFill() {
        return setToFill;
    }

    public void setSetToFill(boolean setToFill) {
        this.setToFill = setToFill;
    }

    public String getFillQuanty() {
        return fillQuanty;
    }

    public void setFillQuanty(String fillQuanty) {
        this.fillQuanty = fillQuanty;
    }

    public boolean isToKill() {
        return toKill;
    }
    public void setToKill(boolean toKill) {
        this.toKill = toKill;
    }




    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }



    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public String getItemID() {
        return ItemID;
    }

    public String getMaxQuantity() {
        return MaxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        MaxQuantity = maxQuantity;
    }

    public String getItemName() {
        return itemName;
    }
}
