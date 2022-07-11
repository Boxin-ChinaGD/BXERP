package com.bx.erp.model;

import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;


@Entity(nameInDb = "purchasingOrderCommodity")
public class PurchasingOrderCommodity extends BaseModel {
    public static final String FIELD_ERROR_PurchasingOrderID = "采购订单ID必须>0";
    public static final String FIELD_ERROR_CommodityID = "商品ID必须>0";
    public static final String FIELD_ERROR_BarcodeID = "条形码ID必须>0";
    public static final String FIELD_ERROR_CommodityNO = "商品数量必须大于0";
    public static final String FIELD_ERROR_PriceSuggestion = "建议采购单价必须大于或者等于0";
    public static final PurchasingOrderCommodityField field = new PurchasingOrderCommodityField();
    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;
    @NotNull
    @Property(nameInDb = "F_PurchasingOrderID")
    protected int purchasingOrderID;

    @NotNull
    @Property(nameInDb = "F_PriceSuggestion")
    protected double priceSuggestion;

    @NotNull
    @Property(nameInDb = "F_CommodityID")
    protected int commodityID;

    @NotNull
    @Property(nameInDb = "F_CommodityNO")
    protected int commodityNO;

    @NotNull
    @Property(nameInDb = "F_CommodityName")
    protected String commodityName;

    @NotNull
    @Property(nameInDb = "F_BarcodeID")
    protected int barcodeID;

    @NotNull
    @Property(nameInDb = "F_PackageUnitID")
    protected int packageUnitID;


    @Transient
    protected String totalPrices; // 在机器人中使用到，

    public String getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(String totalPrices) {
        this.totalPrices = totalPrices;
    }


    @Generated(hash = 376911580)
    public PurchasingOrderCommodity(Long ID, int purchasingOrderID, double priceSuggestion, int commodityID, int commodityNO, @NotNull String commodityName,
            int barcodeID, int packageUnitID) {
        this.ID = ID;
        this.purchasingOrderID = purchasingOrderID;
        this.priceSuggestion = priceSuggestion;
        this.commodityID = commodityID;
        this.commodityNO = commodityNO;
        this.commodityName = commodityName;
        this.barcodeID = barcodeID;
        this.packageUnitID = packageUnitID;
    }

    @Generated(hash = 575472927)
    public PurchasingOrderCommodity() {
    }

    @Override
    public String toString() {
        return "PurchasingOrderCommodity{" +
                "ID=" + ID +
                ", purchasingOrderID=" + purchasingOrderID +
                ", priceSuggestion=" + priceSuggestion +
                ", commodityID=" + commodityID +
                ", commodityNO=" + commodityNO +
                ", commodityName='" + commodityName + '\'' +
                ", barcodeID='" + barcodeID + '\'' +
                ", packageUnitID='" + packageUnitID + '\'' +
                '}';
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getBarcodeID() {
        return barcodeID;
    }

    public void setBarcodeID(int barcodeID) {
        this.barcodeID = barcodeID;
    }

    public int getPackageUnitID() {
        return packageUnitID;
    }

    public void setPackageUnitID(int packageUnitID) {
        this.packageUnitID = packageUnitID;
    }


    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getPurchasingOrderID() {
        return purchasingOrderID;
    }

    public void setPurchasingOrderID(int purchasingOrderID) {
        this.purchasingOrderID = purchasingOrderID;
    }

    public double getPriceSuggestion() {
        return priceSuggestion;
    }

    public void setPriceSuggestion(double priceSuggestion) {
        this.priceSuggestion = priceSuggestion;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public int getCommodityNO() {
        return commodityNO;
    }

    public void setCommodityNO(int commodityNO) {
        this.commodityNO = commodityNO;
    }

    public BaseModel clone() {
        PurchasingOrderCommodity obj = new PurchasingOrderCommodity();
        obj.setID(this.getID());
        obj.setPurchasingOrderID(this.getPurchasingOrderID());
        obj.setPriceSuggestion(this.getPriceSuggestion());
        obj.setCommodityID(this.getCommodityID());
        obj.setCommodityNO(this.getCommodityNO());
        obj.setCommodityName(this.getCommodityName());
        obj.setBarcodeID(this.getBarcodeID());
        obj.setPackageUnitID(this.getPackageUnitID());

        return obj;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        PurchasingOrderCommodity orderComm = (PurchasingOrderCommodity) arg0;

        if ((ignoreIDInComparision == true ? true : orderComm.getID().equals(ID) && printComparator(field.getFIELD_NAME_ID())) //
                && orderComm.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
                && orderComm.getCommodityNO() == commodityNO && printComparator(field.getFIELD_NAME_commodityNO())//
                && orderComm.getPurchasingOrderID() == purchasingOrderID && printComparator(field.getFIELD_NAME_purchasingOrderID()) //
                && Math.abs(GeneralUtil.sub(orderComm.getPriceSuggestion(), priceSuggestion)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceSuggestion()) //
        ) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 PurchasingOrderCommodity.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            purchasingOrderID = Integer.valueOf(jo.getString(field.getFIELD_NAME_purchasingOrderID()));
            priceSuggestion = jo.getDouble(field.getFIELD_NAME_priceSuggestion());
            commodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_commodityID()));
            commodityNO = Integer.valueOf(jo.getString(field.getFIELD_NAME_commodityNO()));
            commodityName = jo.getString(field.getFIELD_NAME_commodityName());
            barcodeID = jo.getInt(field.getFIELD_NAME_barcodeID());
            packageUnitID = jo.getInt(field.getFIELD_NAME_packageUnitID());

            return this;
        } catch (Exception e) {
            System.out.println(" PurchasingOrderCommodity.doParse1出现异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 PurchasingOrderCommodity.parseN，s=" + s);

        List<BaseModel> pocList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = new JSONArray(s);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                PurchasingOrderCommodity p = new PurchasingOrderCommodity();
                p.doParse1(jsonObject1);
                pocList.add(p);
            }
        } catch (Exception e) {
            System.out.println(" PurchasingOrderCommodity.parseN，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }

        return pocList;
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 PurchasingOrderCommodity.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtcList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
                if (poc.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtcList.add(poc);
            }
            return rtcList;
        } catch (Exception e) {
            System.out.println(" PurchasingOrderCommodity.parseN，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toJson(BaseModel bm) {
        Gson gs = new Gson();
        String json = gs.toJson(bm);

        return json;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
//        pos发送请求创建入库单时主表尚未创建，此时的从表中的该字段为空，不应检查该字段。(nbr中是从前端拿到数据然后分别插入主从表)
//        if (this.printCheckField(getFIELD_NAME_purchasingOrderID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) //
//        {
//            return sbError.toString();
//        }

        if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_commodityNO(), FIELD_ERROR_CommodityNO, sbError) && commodityNO <= 0) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_BarcodeID, sbError) && !FieldFormat.checkID(barcodeID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_priceSuggestion(), FIELD_ERROR_PriceSuggestion, sbError) && priceSuggestion < 0.000000d) //
        {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), FIELD_ERROR_PurchasingOrderID, sbError) && !FieldFormat.checkID(purchasingOrderID)) //
        {
            return sbError.toString();
        }

        return "";
    }
}
