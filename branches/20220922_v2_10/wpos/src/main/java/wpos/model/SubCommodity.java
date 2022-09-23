package wpos.model;


import java.util.Date;

public class SubCommodity extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer ID;

    private int commodityID;

    private int subCommodityID;

    private int subCommodityNO;

    private double price;

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public int getSubCommodityID() {
        return subCommodityID;
    }

    public void setSubCommodityID(int subCommodityID) {
        this.subCommodityID = subCommodityID;
    }

    public int getSubCommodityNO() {
        return subCommodityNO;
    }

    public void setSubCommodityNO(int subCommodityNO) {
        this.subCommodityNO = subCommodityNO;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getSyncType() {
        return "";
    }


    public Date getSyncDatetime() {
        return null;
    }


    @Override
    public String toString() {
        return "SubCommodity [commodityID=" + commodityID + ", subCommodityID=" + subCommodityID + ", subCommodityNO=" + subCommodityNO + ", price=" + price + "]";
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        SubCommodity obj = new SubCommodity();
        obj.setCommodityID(commodityID);
        obj.setSubCommodityID(subCommodityID);
        obj.setSubCommodityNO(subCommodityNO);
        obj.setPrice(price);

        return obj;
    }
}
