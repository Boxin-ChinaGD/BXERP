package com.bx.erp.model;

import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.json.JSONException;
import org.json.JSONObject;

@Entity(nameInDb = "T_CommodityShopInfo")
public class CommodityShopInfo extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ERROR_commodityID = "commodityID必须是大于0的数字";

    public static final CommodityShopInfoField field = new CommodityShopInfoField();

    @Id(autoincrement = true)
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_CommodityID")
    private int commodityID;

    @Property(nameInDb = "F_ShopID")
    private int shopID;

    @Property(nameInDb = "F_LatestPricePurchase")
    private double latestPricePurchase;

    @Property(nameInDb = "F_PriceRetail")
    private double priceRetail;

    /** 库存 */
    @Property(nameInDb = "F_NO")
    private int NO;

    @Property(nameInDb = "F_NOStart")
    private int nOStart;

    @Property(nameInDb = "F_PurchasingPriceStart")
    private double purchasingPriceStart;

    @Property(nameInDb = "F_CurrentWarehousingID")
    private int currentWarehousingID;

    /** 代表要搜索的员工的ID */
    @Transient
    private int operatorStaffID;

    @Generated(hash = 682868254)
    public CommodityShopInfo(Long ID, int commodityID, int shopID, double latestPricePurchase, double priceRetail, int NO, int nOStart, double purchasingPriceStart,
            int currentWarehousingID) {
        this.ID = ID;
        this.commodityID = commodityID;
        this.shopID = shopID;
        this.latestPricePurchase = latestPricePurchase;
        this.priceRetail = priceRetail;
        this.NO = NO;
        this.nOStart = nOStart;
        this.purchasingPriceStart = purchasingPriceStart;
        this.currentWarehousingID = currentWarehousingID;
    }

    @Generated(hash = 2125898267)
    public CommodityShopInfo() {
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getOperatorStaffID() {
        return operatorStaffID;
    }

    public void setOperatorStaffID(int operatorStaffID) {
        this.operatorStaffID = operatorStaffID;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public double getLatestPricePurchase() {
        return latestPricePurchase;
    }

    public void setLatestPricePurchase(double latestPricePurchase) {
        this.latestPricePurchase = latestPricePurchase;
    }

    public double getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(double priceRetail) {
        this.priceRetail = priceRetail;
    }

    public int getNO() {
        return NO;
    }

    public void setNO(int nO) {
        NO = nO;
    }

    public int getnOStart() {
        return nOStart;
    }

    public void setnOStart(int nOStart) {
        this.nOStart = nOStart;
    }

    public double getPurchasingPriceStart() {
        return purchasingPriceStart;
    }

    public void setPurchasingPriceStart(double purchasingPriceStart) {
        this.purchasingPriceStart = purchasingPriceStart;
    }

    public int getCurrentWarehousingID() {
        return currentWarehousingID;
    }

    public void setCurrentWarehousingID(int currentWarehousingID) {
        this.currentWarehousingID = currentWarehousingID;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        CommodityShopInfo obj = new CommodityShopInfo();
        obj.setID(ID);
        obj.setCommodityID(commodityID);
        obj.setShopID(shopID);
        obj.setLatestPricePurchase(latestPricePurchase);
        obj.setPriceRetail(priceRetail);
        obj.setNO(NO);
        obj.setnOStart(nOStart);
        obj.setPurchasingPriceStart(purchasingPriceStart);
        obj.setCurrentWarehousingID(currentWarehousingID);
        return obj;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        return "";
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        CommodityShopInfo comm = (CommodityShopInfo) arg0;
        if ((ignoreIDInComparision == true ? true : (comm.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
                && comm.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
                && comm.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
                && Math.abs(GeneralUtil.sub(comm.getLatestPricePurchase(), latestPricePurchase)) < TOLERANCE && printComparator(field.getFIELD_NAME_latestPricePurchase()) //
                && Math.abs(GeneralUtil.sub(comm.getPriceRetail(), priceRetail)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceRetail()) //
                && comm.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
                && comm.getnOStart() == nOStart && printComparator(field.getFIELD_NAME_nOStart()) //
                && Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), purchasingPriceStart)) < TOLERANCE && printComparator(field.getFIELD_NAME_purchasingPriceStart())//
                && comm.getCurrentWarehousingID() == currentWarehousingID && printComparator(field.getFIELD_NAME_currentWarehousingID()) //
        ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Barcodes的parse1，s=" + s);
        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Commodity.parse1()出错：" + e.getMessage());
            return null;
        }
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行CommodityShopInfo的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
            shopID = jo.getInt(field.getFIELD_NAME_shopID());
            latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
            priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
            NO = jo.getInt(field.getFIELD_NAME_NO());
            nOStart = jo.getInt(field.getFIELD_NAME_nOStart());
            purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
            currentWarehousingID = jo.getInt(field.getFIELD_NAME_currentWarehousingID());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return this;
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        switch (iUseCaseID) {
            default:
//                if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
//                    return sbError.toString();
//                }

                return "";
        }

    }

    public int getNOStart() {
        return this.nOStart;
    }

    public void setNOStart(int nOStart) {
        this.nOStart = nOStart;
    }
}
