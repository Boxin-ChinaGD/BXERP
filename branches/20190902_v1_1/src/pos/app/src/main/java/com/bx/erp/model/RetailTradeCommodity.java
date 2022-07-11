package com.bx.erp.model;

import com.bx.erp.helper.Constants;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.StringUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(nameInDb = "retailTradeCommodity")
public class RetailTradeCommodity extends BaseModel {
    public static final int Min_ID = 0;
    public static final int Min_NO = 0;
    public static final int Max_NO = 9999;
    public static final double Min_Double = 0.000000d;

    public static final String FIELD_ERROR_TradeID = "TradeID不能小于或等于" + Min_ID;
    public static final String FIELD_ERROR_CommodityID = "CommodityID不能小于或等于" + Min_ID;
    public static final String FIELD_ERROR_BarcodeID = "BarcodeID不能小于或等于" + Min_ID;
    public static final String FIELD_ERROR_NO = "数量不能小于或等于" + Min_NO + ",并且不能大于" + Max_NO;
    public static final String FIELD_ERROR_Price = "商品的零售价必须大于或等于" + Min_Double;
    //    public static final String FIELD_ERROR_Discount = "促销折扣必须大于" + Min_Double;
   // public static final String FIELD_ERROR_NOCanReturn = "可退货的数量不能小于" + Min_NO + ",并且不能大于" + Max_NO;
    public static final String FIELD_ERROR_PriceReturn = "退货价必须大于或等于" + Min_Double;
    //    public static final String FIELD_ERROR_PriceVIPOriginal = "商品的会员价必须大于等于" + Min_Double; // POS端不传这个参数，故不检查这个字段
    public static final RetailTradeCommodityField field = new RetailTradeCommodityField();
    @Id
    @Property(nameInDb = "F_ID")
    protected Long ID;

    @Property(nameInDb = "F_CommodityName")
    protected String commodityName;

    @NotNull
    @Property(nameInDb = "F_TradeID")
    protected Long tradeID;

    @Property(nameInDb = "F_SyncDatetime")
    protected Date syncDatetime;

    @Property(nameInDb = "F_SyncType")
    protected String syncType;

    @NotNull
    @Property(nameInDb = "F_CommodityID")
    protected int commodityID;

    @NotNull
    @Property(nameInDb = "F_NO")
    protected int NO;

    @NotNull
    @Property(nameInDb = "F_PriceOriginal")
    protected double priceOriginal;//商品原零售价

    @Transient
    protected double discount;//折扣

    @NotNull
    @Property(nameInDb = "F_NOCanReturn")
    protected int NOCanReturn;//可退货数量

    @NotNull
    @Property(nameInDb = "F_PriceReturn")
    protected double priceReturn;//退货价

    @NotNull
    @Property(nameInDb = "F_PriceSpecialOffer")
    protected double priceSpecialOffer;//商品特价

    @Property(nameInDb = "F_PriceVIPOriginal")
    protected double priceVIPOriginal;//商品原会员价

    @Property(nameInDb = "F_BarcodeID")
    protected int barcodeID;

    @Property(nameInDb = "F_PromotionID")
    protected Long promotionID;

    @Transient
    protected int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Transient
    protected String name;

    public String getFIELD_NEME_name() {
        return "name";
    }

    @Transient
    protected String FIELD_NEME_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 一批零售单商品的总价格
    @Transient
    protected double amountWeChat;

    public double getAmountWeChat() {
        return amountWeChat;
    }

    public void setAmountWeChat(double amountWeChat) {
        this.amountWeChat = amountWeChat;
    }

    @Override
    public String toString() {
        return "RetailTradeCommodity{" +
                "NO=" + NO +
                ", ID=" + ID +
                ", commodityName=" + commodityName +
                ", tradeID=" + tradeID +
                ", syncDatetime=" + syncDatetime +
                ", syncType='" + syncType + '\'' +
                ", commodityID=" + commodityID +
                ", priceOriginal=" + priceOriginal +
                ", discount=" + discount +
                ", NOCanReturn=" + NOCanReturn +
                ", priceReturn=" + priceReturn +
                ", priceSpecialOffer=" + priceSpecialOffer +
                ", priceVIPOriginal=" + priceVIPOriginal +
                ", barcodeID=" + barcodeID +
                ", name=" + name +
                '}';
    }

    public int getBarcodeID() {
        return barcodeID;
    }

    public void setBarcodeID(int barcodeID) {
        this.barcodeID = barcodeID;
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getTradeID() {
        return tradeID;
    }

    public void setTradeID(Long tradeID) {
        this.tradeID = tradeID;
    }

    public int getCommodityID() {
        return commodityID;
    }

    public void setCommodityID(int commodityID) {
        this.commodityID = commodityID;
    }

    public int getNO() {
        return NO;
    }

    public void setNO(int NO) {
        this.NO = NO;
    }

    public double getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(double priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getNOCanReturn() {
        return NOCanReturn;
    }

    public void setNOCanReturn(int NOCanReturn) {
        this.NOCanReturn = NOCanReturn;
    }

    public double getPriceReturn() {
        return priceReturn;
    }

    public void setPriceReturn(double priceReturn) {
        this.priceReturn = priceReturn;
    }

    public double getPriceSpecialOffer() {
        return priceSpecialOffer;
    }

    public void setPriceSpecialOffer(double priceSpecialOffer) {
        this.priceSpecialOffer = priceSpecialOffer;
    }

    public double getPriceVIPOriginal() {
        return priceVIPOriginal;
    }

    public void setPriceVIPOriginal(double priceVIPOriginal) {
        this.priceVIPOriginal = priceVIPOriginal;
    }

    @Transient
    public boolean isSelect = false;
    /**
     * 零售单商品的数量是否可编辑
     */
    @Transient
    public static boolean isRetailTradeCommodityNumberEditable = false;

    @Generated(hash = 1768041908)
    public RetailTradeCommodity(Long ID, String commodityName, @NotNull Long tradeID, Date syncDatetime, String syncType, int commodityID, int NO, double priceOriginal,
                                int NOCanReturn, double priceReturn, double priceSpecialOffer, double priceVIPOriginal, int barcodeID, Long promotionID) {
        this.ID = ID;
        this.commodityName = commodityName;
        this.tradeID = tradeID;
        this.syncDatetime = syncDatetime;
        this.syncType = syncType;
        this.commodityID = commodityID;
        this.NO = NO;
        this.priceOriginal = priceOriginal;
        this.NOCanReturn = NOCanReturn;
        this.priceReturn = priceReturn;
        this.priceSpecialOffer = priceSpecialOffer;
        this.priceVIPOriginal = priceVIPOriginal;
        this.barcodeID = barcodeID;
        this.promotionID = promotionID;
    }

    @Generated(hash = 1966375863)
    public RetailTradeCommodity() {
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 RetailTradeCommodity.parseN() ，s=" + s);

        List<BaseModel> rtcList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray1 = new JSONArray(s);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                RetailTradeCommodity c = new RetailTradeCommodity();
                c.doParse1(jsonObject1);
                rtcList.add(c);
            }
        } catch (Exception e) {
            System.out.println("执行 RetailTradeCommodity.parseN() 异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }

        return rtcList;
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 RetailTradeCommodity.parseN() ，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtcList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RetailTradeCommodity rtc = new RetailTradeCommodity();
                if (rtc.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtcList.add(rtc);
            }
            return rtcList;
        } catch (Exception e) {
            System.out.println("执行 RetailTradeCommodity.parseN() 异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 RetailTradeCommodity.parse1() ，s=" + s);

        try {
            return doParse1(new JSONObject(s));
        } catch (JSONException e) {
            System.out.println("执行 RetailTradeCommodity.parse1() 异常，错误信息为" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 RetailTradeCommodity.doParse1() ，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Long.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            tradeID = Long.valueOf(jo.getString(field.getFIELD_NAME_tradeID()));
            commodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_commodityID()));
            NO = Integer.valueOf(jo.getString(field.getFIELD_NAME_NO()));
            priceOriginal = Double.valueOf(jo.getString(field.getFIELD_NAME_price()));
            NOCanReturn = Integer.valueOf(jo.getString(field.getFIELD_NAME_NOCanReturn()));
            priceReturn = Double.valueOf(jo.getString(field.getFIELD_NAME_priceReturn()));
            priceSpecialOffer = Double.valueOf(jo.getString(field.getFIELD_NAME_priceSpecialOffer()));
            priceVIPOriginal = Double.valueOf(jo.getString(field.getFIELD_NAME_priceVIPOriginal()));
            barcodeID = Integer.valueOf(jo.getString(field.getFIELD_NAME_barcodeID()));
            commodityName = jo.getString(field.getFIELD_NAME_commodityName());

            //
            String tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (syncDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
                }
            }

            return this;
        } catch (Exception e) {
            System.out.println("执行 RetailTradeCommodity.doParse1() 异常，错误信息为" + e.getMessage());

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

    public BaseModel clone() {
        RetailTradeCommodity rtc = new RetailTradeCommodity();
        rtc.setID(this.getID());
        rtc.setBarcodeID(this.getBarcodeID());
        rtc.setTradeID(this.getTradeID());
        rtc.setCommodityID(this.getCommodityID());
        rtc.setNOCanReturn(this.getNOCanReturn());
        rtc.setNO(this.getNO());
        rtc.setPriceOriginal(this.getPriceOriginal());
        rtc.setDiscount(this.getDiscount());
        rtc.setPriceReturn(this.getPriceReturn());
        rtc.setPriceSpecialOffer(this.getPriceSpecialOffer());
        rtc.setPriceVIPOriginal(this.getPriceVIPOriginal());
        if (this.getSyncDatetime() != null) {
            rtc.setSyncDatetime((Date) this.getSyncDatetime().clone());
        }

        return rtc;
    }

    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }
        RetailTradeCommodity rtc = (RetailTradeCommodity) arg0;
        if ((ignoreIDInComparision == true ? true
                : compareSQLiteRowID(rtc.getID(), ID) && printComparator(field.getFIELD_NAME_ID()) //
                && compareSQLiteRowID(rtc.getTradeID(), tradeID) && printComparator(field.getFIELD_NAME_tradeID())) //
                && rtc.getCommodityID() == this.getCommodityID() && printComparator(field.getFIELD_NAME_commodityID()) //
                && rtc.getNO() == this.getNO() && printComparator(field.getFIELD_NAME_NO()) //
                && Math.abs(GeneralUtil.sub(rtc.getPriceOriginal(), this.getPriceOriginal())) < TOLERANCE && printComparator(field.getFIELD_NAME_price()) //
                && Math.abs(GeneralUtil.sub(rtc.getPriceReturn(), this.getPriceReturn())) < TOLERANCE && printComparator(field.getFIELD_NAME_priceReturn()) //
                && Math.abs(GeneralUtil.sub(rtc.getPriceSpecialOffer(), this.getPriceSpecialOffer())) < TOLERANCE && printComparator(field.getFIELD_NAME_priceSpecialOffer()) //
                && Math.abs(GeneralUtil.sub(rtc.getPriceVIPOriginal(), this.getPriceVIPOriginal())) < TOLERANCE && printComparator(field.getFIELD_NAME_priceVIPOriginal()) //
                ) {
            return 0;
        }
        return -1;
    }


    @Override
    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    public String getSyncType() {
        return this.syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public Long getPromotionID() {
        return this.promotionID;
    }

    public void setPromotionID(Long promotionID) {
        this.promotionID = promotionID;
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_tradeID(), FIELD_ERROR_TradeID, sbError) && !FieldFormat.checkID(tradeID.intValue())) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_CommodityID, sbError) && !FieldFormat.checkID(commodityID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_barcodeID(), FIELD_ERROR_BarcodeID, sbError) && !FieldFormat.checkID(barcodeID)) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NO, sbError) && NO <= Min_NO || NO > Max_NO) //
        {
            return sbError.toString();
        }

        if (this.printCheckField(field.getFIELD_NAME_price(), FIELD_ERROR_Price, sbError) && priceOriginal < Min_Double) //
        {
            return sbError.toString();
        }

//        if (this.printCheckField(getFIELD_NAME_discount(), FIELD_ERROR_Discount, sbError) && discount < Min_Double) //
//        {
//            return sbError.toString();
//        }

        if (this.printCheckField(field.getFIELD_NAME_priceReturn(), FIELD_ERROR_PriceReturn, sbError) && priceReturn < Min_Double) //
        {
            return sbError.toString();
        }

        return "";
    }

    public String getCommodityName() {
        return this.commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_tradeID(), FIELD_ERROR_TradeID, sbError) && !FieldFormat.checkID(tradeID.intValue())) //
        {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_tradeID(), FIELD_ERROR_TradeID, sbError) && !FieldFormat.checkID(tradeID.intValue())) //
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
    protected String doCheckRetrieveN(int iUseCaseID) {
        return "";
    }

}
