package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.helper.Constants;
import wpos.utils.FieldFormat;
import wpos.utils.StringUtils;
//import com.google.gson.Gson;
//
//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.NotNull;
//import org.greenrobot.greendao.annotation.Property;
//import org.greenrobot.greendao.annotation.Transient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Entity(nameInDb = "warehousingCommodity")
public class WarehousingCommodity extends BaseModel{

    public static final double DEFAULT_VALUE_Amount = 0.000000d;

    public static final String FIELD_ERROR_no = "数量必须是大于0的整数";
    public static final String FIELD_ERROR_amount = "金额必须大于等于" + DEFAULT_VALUE_Amount;
    public static final String FIELD_ERROR_price = "进货价必须大于等于0";
    public static final String FIELD_ERROR_shelfLife = "保质期只允许是正整数（自然数）";
    public static final String FIELD_ERROR_warehousingID = "warehousingID必须大于0";
    public static final String FIELD_ERROR_commodityID = "commodityID必须大于0";
    public static final String FIELD_ERROR_barcodeID = "barcodeID必须大于0";
    public static final WarehousingCommodityField field = new WarehousingCommodityField();

    //@Id
    ////@Property(nameInDb = "F_ID")
    protected Integer ID;

    //@NotNull
    //@Property(nameInDb = "F_WarehousingID")
    protected int warehousingID;

    //@NotNull
    //@Property(nameInDb = "F_CommodityID")
    protected int commodityID;

    //@NotNull
    //@Property(nameInDb = "F_NO")
    protected int NO;

    //@NotNull
    //@Property(nameInDb = "F_Price")
    protected double price;

    //@NotNull
    //@Property(nameInDb = "F_Amount")
    protected double amount;

    //@NotNull
    //@Property(nameInDb = "F_ProductionDatetime")
    protected Date productionDatetime;

    //@NotNull
    //@Property(nameInDb = "F_ShelfLife")
    protected int shelfLife;

    //@NotNull
    //@Property(nameInDb = "F_ExpireDatetime")
    protected Date expireDatetime;

    //@NotNull
    //@Property(nameInDb = "F_PackageUnitID")
    protected int packageUnitID;

    //@NotNull
    //@Property(nameInDb = "F_CommodityName")
    protected String commodityName;

    //@NotNull
    //@Property(nameInDb = "F_BarcodeID")
    protected int barcodeID;

    //@NotNull
    //@Property(nameInDb = "F_CreateDatetime")
    protected Date createDatetime;

    //@NotNull
    //@Property(nameInDb = "F_UpdateDatetime")
    protected Date updateDatetime;

    //@Generated(hash = 500486435)
//    public WarehousingCommodity(Long ID, int warehousingID, int commodityID, int NO, double price, double amount, //@NotNull Date productionDatetime, int shelfLife,
//            //@NotNull Date expireDatetime, int packageUnitID, //@NotNull String commodityName, int barcodeID, //@NotNull Date createDatetime,
//            //@NotNull Date updateDatetime) {
//        this.ID = ID;
//        this.warehousingID = warehousingID;
//        this.commodityID = commodityID;
//        this.NO = NO;
//        this.price = price;
//        this.amount = amount;
//        this.productionDatetime = productionDatetime;
//        this.shelfLife = shelfLife;
//        this.expireDatetime = expireDatetime;
//        this.packageUnitID = packageUnitID;
//        this.commodityName = commodityName;
//        this.barcodeID = barcodeID;
//        this.createDatetime = createDatetime;
//        this.updateDatetime = updateDatetime;
//    }

    //@Generated(hash = 1608602440)
//    public WarehousingCommodity() {
//    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public int getWarehousingID() {
        return warehousingID;
    }

    public void setWarehousingID(int warehousingID) {
        this.warehousingID = warehousingID;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getProductionDatetime() {
        return productionDatetime;
    }

    public void setProductionDatetime(Date productionDatetime) {
        this.productionDatetime = productionDatetime;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Date getExpireDatetime() {
        return expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
    }

    public int getPackageUnitID() {
        return packageUnitID;
    }

    public void setPackageUnitID(int packageUnitID) {
        this.packageUnitID = packageUnitID;
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

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public String toString() {
        return "WarehousingCommodity{" +
                "ID=" + ID +
                ", warehousingID=" + warehousingID +
                ", commodityID=" + commodityID +
                ", NO=" + NO +
                ", price=" + price +
                ", amount=" + amount +
                ", productionDatetime=" + productionDatetime +
                ", shelfLife=" + shelfLife +
                ", expireDatetime=" + expireDatetime +
                ", packageUnitID=" + packageUnitID +
                ", commodityName='" + commodityName + '\'' +
                ", barcodeID=" + barcodeID +
                ", createDatetime=" + createDatetime +
                ", updateDatetime=" + updateDatetime +
                '}';
    }

    @Override
    public BaseModel clone() {
        WarehousingCommodity obj = new WarehousingCommodity();
        obj.setID(this.getID());
        obj.setPackageUnitID(this.getPackageUnitID());
        obj.setCommodityName(this.getCommodityName());
        obj.setBarcodeID(this.getBarcodeID());
        obj.setCreateDatetime((Date) this.getCreateDatetime().clone());
        obj.setUpdateDatetime((Date) this.getUpdateDatetime().clone());
        obj.setWarehousingID(this.getWarehousingID());
        obj.setCommodityID(this.getCommodityID());
        obj.setNO(this.getNO());
        obj.setPrice(this.getPrice());
        obj.setAmount(this.getAmount());
        obj.setProductionDatetime((Date) this.getProductionDatetime().clone());
        obj.setShelfLife(this.getShelfLife());
        obj.setExpireDatetime((Date) this.getExpireDatetime().clone());

        return obj;
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行 WarehousingCommodity.parse1，s=" + s);

        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (JSONException e) {
            System.out.println("WarehousingCommodity.parse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行 WarehousingCommodity.doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            packageUnitID = jo.getInteger(field.getFIELD_NAME_packageUnitID());
            commodityName = jo.getString(field.getFIELD_NAME_commodityName());
            barcodeID = jo.getInteger(field.getFIELD_NAME_barcodeID());
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            warehousingID = Integer.valueOf(jo.getString(field.getFIELD_NAME_warehousingID()));
            commodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_commodityID()));
            NO = Integer.valueOf(jo.getString(field.getFIELD_NAME_NO()));
            price = jo.getDouble(field.getFIELD_NAME_price());
            amount = jo.getDouble(field.getFIELD_NAME_amount());
            shelfLife = Integer.valueOf(jo.getString(field.getFIELD_NAME_shelfLife()));

            // 这种做法可能还有问题，以后再找Giggs讨论 ...
            String tmp = jo.getString(field.getFIELD_NAME_productionDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                productionDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (productionDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_productionDatetime() + "=" + tmp);
                }
            }

            tmp = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (createDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
                }
            }

            tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (updateDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmp);
                }
            }

            tmp = jo.getString(field.getFIELD_NAME_expireDatetime());
            if (!StringUtils.isEmpty(tmp)) {
                expireDatetime = Constants.getSimpleDateFormat2().parse(tmp);
                if (expireDatetime == null) {
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_expireDatetime() + "=" + tmp);
                }
            }
            return this;
        } catch (Exception e) {
            System.out.println("WarehousingCommodity.doParse1 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行 WarehousingCommodity.parseN，s=" + s);

        List<BaseModel> wcList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray = JSONArray.parseArray(s);
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                WarehousingCommodity c = new WarehousingCommodity();
                c.doParse1(jsonObject1);
                wcList.add(c);
            }
        } catch (Exception e) {
            System.out.println("WarehousingCommodity.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return wcList;
    }

    @Override
    public List<?> parseN(JSONArray jsonArray) {
        System.out.println("正在执行 WarehousingCommodity.parseN，jsonArray=" + (jsonArray == null ? null : jsonArray.toString()));

        List<BaseModel> rtcList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                WarehousingCommodity wc = new WarehousingCommodity();
                if (wc.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                rtcList.add(wc);
            }
            return rtcList;
        } catch (Exception e) {
            System.out.println("WarehousingCommodity.parseN 出现异常，错误信息为" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String checkCreate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();

        if (doCheckCreateUpdate(sbError).trim().length() > 0) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_barcodeID(), FIELD_ERROR_barcodeID, sbError) && !FieldFormat.checkID(barcodeID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife, sbError) && !FieldFormat.checkShelfLife(String.valueOf(shelfLife))) {
            return sbError.toString();
        }

        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
            return sbError.toString();
        }
        if (doCheckCreateUpdate(sbError).trim().length() > 0) {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID){
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (this.printCheckField(field.getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
            return sbError.toString();
        }
        return "";
    }

    @Override
    public String checkDelete(int iUseCaseID){
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
            return sbError.toString();
        }
        return "";
    }

    protected String doCheckCreateUpdate(StringBuilder sbError) {
        // pos发送请求创建入库单时主表尚未创建，此时的从表中的该字段为空，不应检查该字段。(nbr中是从前端拿到数据然后分别插入主从表)
//        if (this.printCheckField(getFIELD_NAME_warehousingID(), FIELD_ERROR_warehousingID, sbError) && !FieldFormat.checkID(warehousingID)) {
//            return sbError.toString();
//        }
        if (this.printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !FieldFormat.checkID(commodityID)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_no, sbError) && !FieldFormat.checkNOStart(String.valueOf(NO))) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_price(), FIELD_ERROR_price, sbError) && !FieldFormat.checkPrice(price)) {
            return sbError.toString();
        }
        if (this.printCheckField(field.getFIELD_NAME_amount(), FIELD_ERROR_amount, sbError) && amount < DEFAULT_VALUE_Amount) {
            return sbError.toString();
        }
        return "";
    }

}
