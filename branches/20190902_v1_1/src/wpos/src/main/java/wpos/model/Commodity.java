
package wpos.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.bo.NtpHttpBO;
import wpos.helper.Constants;
import wpos.utils.DatetimeUtil;
import wpos.utils.FieldFormat;
import wpos.utils.GeneralUtil;
import wpos.utils.StringUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

////@Entity(name = COMMODITY_TABLE_NAME)
@Entity
@Table(name = "T_Commodity")
public class Commodity extends BaseModel {
    public static final double PURCHASING_PRICE_START_Default = -1d;
    public static final String HTTP_Commodity_CREATE = "commoditySync/createEx.bx";
    public static final String HTTP_Commodity_RETRIEVEN = "commoditySync/retrieveNEx.bx";
    public static final String commodityfeedbackURL_FRONT = "commoditySync/feedbackEx.bx?sID=";
    public static final String commodityfeedbackURL_BEHIND = "&errorCode= EC_NoError";
    public static final String HTTP_Commodity_RETRIEVENC_PageIndex = "commodity/retrieveNEx.bx?pageIndex=";
    public static final String HTTP_Commodity_retrieveInventory = "commodity/retrieveInventoryEx.bx?barcodes=";
    public static final String HTTP_Commodity_RETRIEVENC_ShopID = "&shopID=";
    public static final String HTTP_Commodity_RETRIEVENC_PageSize = "&pageSize=";
    public static final String HTTP_Commodity_RETRIEVENC_TypeStatus = "&type=-1&status=-1";
    public static final String HTTP_Commodity_Retrieve1C = "commodity/retrieve1Ex.bx?ID=";
    public static final String PAGEINDEX_START = "start";
    public static final String PAGEINDEX_END = "end";
    // check() 的错误信息以及内容

    public static final int MIN_LENGTH_Barcodes = 7;
    public static final int MAX_LENGTH_Barcodes = 64;

    public static final String FIELD_ERROR_priceRetail = "商品的零售价为非负浮点数";
    public static final String FIELD_ERROR_pricePurchase = "商品的进货价为非负浮点数";
    public static final String FIELD_ERROR_latestPricePurchase = "商品的最近进货价为非负浮点数";
    public static final String FIELD_ERROR_purchasingUnit = "商品采购单位为中文或英文或数字的组合，中间允许有空格";
    public static final String FIELD_ERROR_type = "普通商品的类型只能为0";
    public static final String FIELD_ERROR_commodityType = "查询的商品类型只能为0、1、2、3";
    public static final String FIELD_ERROR_statusOfMultipackaging = "多包装商品的状态只能是0或1";
    public static final String FIELD_ERROR_status = "添加或修改的商品状态只能是0";
    public static final String FIELD_ERROR_refCommodity = "单品、组合商品和服务商品的参照商品ID要是0,单品、组合商品和服务商品的参照商品倍数要是0";
    public static final String FIELD_ERROR_refCommodityOfMultiPackaging = "多包装商品的参照商品ID要是正整数,多包装商品的参照商品倍数要大于1";
    public static final String FIELD_ERROR_NOofMultiPackaging = "多包装商品的库存为0";
    public static final String FIELD_ERROR_mnemonicCode = "商品的助记码为英文或数字的组合";
    public static final String FIELD_ERROR_tag = "商品的标注为中文或英文或数字或符号的组合";
    public static final String FIELD_ERROR_shortName = "商品名字的简称为中文或英文或数字的组合，中间允许有空格且长度不能为0";
    public static final String FIELD_ERROR_name = "只允许以()（）-—_、中英数值、空格形式出现，长度1到32";
    public static final String FIELD_ERROR_specification = "商品规格为中文或英文或数字的组合，中间允许有空格";
    public static final String FIELD_ERROR_packageUnitID = "商品包装单位ID正整数";
    public static final String FIELD_ERROR_brandID = "商品品牌ID必须是大于0的数字";
    public static final String FIELD_ERROR_categoryID = "商品类别ID只能为数字";
    public static final String FIELD_ERROR_priceVIP = "商品的会员价为非负浮点数";
    public static final String FIELD_ERROR_priceWholesale = "商品的批发价为非负浮点数";
    public static final String FIELD_ERROR_ruleOfPoint = "商品的积分规则为正整数";
    public static final String FIELD_ERROR_purchaseFlag = "商品的采购阀值为正整数";
    //    public static final String FIELD_ERROR_NO = "商品的库存为整数";
    public static final String FIELD_ERROR_shelfLife = "商品的保质期为正整数";
    public static final String FIELD_ERROR_returnDays = "商品的退货天数为正整数";
    public static final String FIELD_ERROR_typeOfComposition = "组合商品的类型应该为1";
    public static final String FIELD_ERROR_barcode = "条形码仅允许英文、数值形式出现，长度范围[" + MIN_LENGTH_Barcodes + ", " + MAX_LENGTH_Barcodes + "]";
    public static final String FIELD_ERROR_nOStart = "期初值必须大于0";
    public static final String FIELD_ERROR_purchasingPriceStart = "期初采购价必须大于0";
    public static final String FIELD_ERROR_pricingType = "商品的计价方式只能为0或1";
    public static final String FIELD_ERROR_canChangePrice = "前台改价只能为0或1";
    public static final String FIELD_ERROR_commodityStatus = "商品的状态只能是1或0";
    //    public static final String FIELD_ERROR_string1 = "string1的长度为1到64";
    public static final String FIELD_ERROR_includeDeleted = "int1只能为正整数";
    public static final String FIELD_ERROR_startValueRemark = "期初值备注长度[0, 50]";
    public static final String FIELD_ERROR_propertyValue = "属性值长度[0, 50]";
    public static final String FIELD_ERROR_checkUniqueFieldID = "ID必须大于或等于0";
    public static final String FIELD_ERROR_checkUniqueFieldString1 = "string1的长度为1到32";
    public static final String FIELD_ERROR_checkUniqueFieldInt1 = "int1必须为1";
    public static final String FIELD_ERROR_staff = "店员ID必须大于0";
    // public static final String FIELD_ERROR_ruleOfPointOfComposition =
    // "组合商品的积分规则必须为-1";
    // public static final String FIELD_ERROR_shelfLifeOfComposition =
    // "组合商品的保质期必须为-1";
    // public static final String FIELD_ERROR_purchaseFlagOfComposition =
    // "组合商品的采购阀值必须为-1";
    public static final String FIELD_ERROR_typeOfMultipackaging = "多包装商品的类型应该为2";
    public static final String FIELD_ERROR_typeOfService = "服务商品的类型应该为3";
    public static final String FIELD_ERROR_nOStart_purchasingPriceStart_type = "期初数量和期初采购价都有值，则本商品必须是单品，但现在本商品的类型并不是单品！";
    public static final String FIELD_ERROR_checkUniqueField = "非法的值";
    public static final String FIELD_ERROR_purchasingUnitOfService = "服务商品的采购单位必须为空";
    public static final String FEIDL_ERROR_noOfService = "服务商品的库存必须为0";
    public static final String FIELD_ERROR_nOStartOfService = "服务商品的期初值必须为-1";
    public static final String FIELD_ERROR_purchasingPriceStartOfService = "服务商品的期初采购价必须为-1";
    public static final String FIELD_ERROR_shelfLifeOfService = "服务商品的保质期必须为0";
    public static final String FIELD_ERROR_latestPricePurchaseOfService = "服务商品的最近采购价必须为-1";
    public static final String FIELD_ERROR_purchaseFlagOfService = "服务商品的采购阀值必须为0";

    public static final int DEFAULT_VALUE_CommodityStatus = 0;
    public static final double DEFAULT_VALUE_LatestPricePurchase = -1.000000d;
    public static final int DEFAULT_VALUE_RefCommodityID = 0;
    public static final int DEFAULT_VALUE_RefCommodityMultiple = 0;
    public static final int DEFAULT_VALUE_CommodityNO = 0;
    public static final int DEFAULT_VALUE_CommodityAccumulated = 0;
    public static final int DEFAULT_VALUE_ShelfLife = 365;
    public static final int DEFAULT_VALUE_PurchaseFlag = 0;

    public static final int MAX_LENGTH_String1 = 32;
    public static final int MIN_LENGTH_String1 = 1;
    public static final int MAX_LENGTH_Name = 32;
    public static final int MIN_LENGTH_Name = 1;
    public static final int MAX_LENGTH_ShortName = 32;
    public static final int MAX_LENGTH_Tag = 32;
    public static final int MIN_LENGTH_Tag = 0;
    public static final int MAX_LENGTH_PurchasingUnit = 16;
    public static final int MIN_LENGTH_PurchasingUnit = 1;
    public static final int MAX_LENGTH_PropertyValue = 50;
    public static final int MAX_LENGTH_StartValueRemark = 50;
    public static final int MAX_LENGTH_Specification = 8;
    public static final int MIN_LENGTH_Specification = 0;
    public static final int MAX_LENGTH_MnemonicCode = 32;
    public static final int MIN_LENGTH_MnemonicCode = 0;
    public static final int MIN_NO = 0;

    public static final CommodityField field = new CommodityField();

    /**
     * 默认期初数量
     */
    public static final int NO_START_Default = -1;
    /** 默认期初采购价 */
//    public static final double PURCHASING_PRICE_START_Default = -1.000000d;
    /**
     * 默认采购阀值
     */
    public static final int PurchaseFlag_START_Default = 1;
    /**
     * 默认积分规则
     */
    public static final int RuleOfPoint_START_Default = 1;

    /**
     * 默认助记码
     */
    public static final String Default_MnemonicCode = "XX";

    @Transient
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Transient
    protected int number;//列表中的序号

    @Transient
    protected String barcode;//条形码，因为不在同一个数据表，所以不作为字段

    @Transient
    protected String category;

    @Transient
    protected String packageUnit;

    @Transient
    protected double discount;//折扣，因为数据表暂时没有这个字段，所以先不作为数据表字段

    @Transient
    protected double after_discount;//折后单价。因为数据表暂时没有这个字段，所以先不作为数据表字段

    @Transient
    protected double subtotal;//小计。因为数据表暂时没有这个字段，所以先不作为数据表字段

    @Transient
    protected int commodityQuantity;//商品数量，因为数据表暂时没有这个字段，所以先不作为数据表字段

    @Transient
    protected int index;//当前为第几个数据

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Id
    @Column(name = "F_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer ID;

    //@NotNull
    @Column(name = "F_Status")
    protected int status;

    //@NotNull
    @Column(name = "F_Name")
    protected String name;

    //@NotNull
    @Column(name = "F_ShortName")
    protected String shortName;

    //@NotNull
    @Column(name = "F_Specification")
    protected String specification;

    @Column(name = "F_PackageUnitID")
    protected int packageUnitID;

    @Column(name = "F_PurchasingUnit")
    protected String purchasingUnit;

    @Column(name = "F_BrandID")
    protected int brandID;

    @Column(name = "F_CategoryID")
    protected int categoryID;

    //@NotNull
    @Column(name = "F_MnemonicCode")
    protected String mnemonicCode;

    //@NotNull
    @Column(name = "F_PricingType")
    protected int pricingType;

    //@NotNull
    @Column(name = "F_LatestPricePurchase")
    protected double latestPricePurchase;

    //@NotNull
    @Column(name = "F_PriceRetail")
    protected double priceRetail;

    //@NotNull
    @Column(name = "F_PriceVIP")
    protected double priceVIP;

    //@NotNull
    @Column(name = "F_PriceWholesale")
    protected double priceWholesale;

    //@NotNull
    @Column(name = "F_CanChangePrice")
    protected int canChangePrice;

    //@NotNull
    @Column(name = "F_RuleOfPoint")
    protected int ruleOfPoint;

    //@NotNull
    @Column(name = "F_Picture")
    protected String picture;

    //@NotNull
    @Column(name = "F_ShelfLife")
    protected int shelfLife;

    //@NotNull
    @Column(name = "F_ReturnDays")
    protected int returnDays;


    @Column(name = "F_CreateDate")
    protected Date createDate;

    //@NotNull
    @Column(name = "F_PurchaseFlag")
    protected int purchaseFlag;

    //@NotNull
    @Column(name = "F_RefCommodityID")
    protected int refCommodityID;

    //@NotNull
    @Column(name = "F_RefCommodityMultiple")
    protected int refCommodityMultiple;

    //@NotNull
    @Column(name = "F_Tag")
    protected String tag;

    //@NotNull
    @Column(name = "F_NO")
    protected int NO;


    //@NotNull
    @Column(name = "F_Type")
    protected int type;

    //@NotNull
    @Column(name = "F_NOStart")
    protected int nOStart;

    //@NotNull
    @Column(name = "F_PurchasingPriceStart")
    protected double purchasingPriceStart;

    @Column(name = "F_StartValueRemark")
    protected String startValueRemark;

    @Column(name = "F_PropertyValue1")
    protected String propertyValue1;

    @Column(name = "F_PropertyValue2")
    protected String propertyValue2;

    @Column(name = "F_PropertyValue3")
    protected String propertyValue3;

    @Column(name = "F_PropertyValue4")
    protected String propertyValue4;

    //@NotNull
    @Column(name = "F_CreateDatetime")
    protected Date createDatetime;

    //@NotNull
    @Column(name = "F_UpdateDatetime")
    protected Date updateDatetime;

    @Transient
    public boolean isSelect = false;    //...

    @Column(name = "F_SyncDatetime")
    protected Date syncDatetime;

    @Column(name = "F_SyncType")
    protected String syncType;

    @Column(name = "F_CurrentWarehousingID")
    protected int currentWarehousingID;

    @Transient
    protected String subCommodityInfo;

    @Transient
    protected String multiPackagingInfo;

    @Transient
    protected String providerIDs;

    @Transient
    protected int barcodeID;

    @Transient
    protected int orderNumber;//列表中的序号

    //@Generated(hash = 1142105552)
//    public Commodity(Long ID, int status, //@NotNull String name, //@NotNull String shortName, //@NotNull String specification, int packageUnitID, String purchasingUnit, int brandID, int categoryID, //@NotNull String mnemonicCode, int pricingType,
//                     double latestPricePurchase, double priceRetail, double priceVIP, double priceWholesale, int canChangePrice, int ruleOfPoint, //@NotNull String picture, int shelfLife, int returnDays, Date createDate, int purchaseFlag, int refCommodityID,
//                     int refCommodityMultiple, //@NotNull String tag, int NO, int type, int nOStart, double purchasingPriceStart, String startValueRemark, String propertyValue1, String propertyValue2, String propertyValue3, String propertyValue4,
//                     //@NotNull Date createDatetime, //@NotNull Date updateDatetime, Date syncDatetime, String syncType) {
//        this.ID = ID;
//        this.status = status;
//        this.name = name;
//        this.shortName = shortName;
//        this.specification = specification;
//        this.packageUnitID = packageUnitID;
//        this.purchasingUnit = purchasingUnit;
//        this.brandID = brandID;
//        this.categoryID = categoryID;
//        this.mnemonicCode = mnemonicCode;
//        this.pricingType = pricingType;
//        this.latestPricePurchase = latestPricePurchase;
//        this.priceRetail = priceRetail;
//        this.priceVIP = priceVIP;
//        this.priceWholesale = priceWholesale;
//        this.canChangePrice = canChangePrice;
//        this.ruleOfPoint = ruleOfPoint;
//        this.picture = picture;
//        this.shelfLife = shelfLife;
//        this.returnDays = returnDays;
//        this.createDate = createDate;
//        this.purchaseFlag = purchaseFlag;
//        this.refCommodityID = refCommodityID;
//        this.refCommodityMultiple = refCommodityMultiple;
//        this.tag = tag;
//        this.NO = NO;
//        this.type = type;
//        this.nOStart = nOStart;
//        this.purchasingPriceStart = purchasingPriceStart;
//        this.startValueRemark = startValueRemark;
//        this.propertyValue1 = propertyValue1;
//        this.propertyValue2 = propertyValue2;
//        this.propertyValue3 = propertyValue3;
//        this.propertyValue4 = propertyValue4;
//        this.createDatetime = createDatetime;
//        this.updateDatetime = updateDatetime;
//        this.syncDatetime = syncDatetime;
//        this.syncType = syncType;
//    }

    //@Generated(hash = 1425960444)
//    public Commodity() {
//    }


//    public String getFIELD_NAME_barcodeID() {
//        return "barcodeID";
//    }


    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCurrentWarehousingID() {
        return currentWarehousingID;
    }

    public void setCurrentWarehousingID(int currentWarehousingID) {
        this.currentWarehousingID = currentWarehousingID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getAfter_discount() {
        return after_discount;
    }

    public void setAfter_discount(double after_discount) {
        this.after_discount = after_discount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getCommodityQuantity() {
        return commodityQuantity;
    }

    public void setCommodityQuantity(int commodityQuantity) {
        this.commodityQuantity = commodityQuantity;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPurchasingUnit() {
        return purchasingUnit;
    }

    public void setPurchasingUnit(String purchasingUnit) {
        this.purchasingUnit = purchasingUnit;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public int getPricingType() {
        return pricingType;
    }

    public void setPricingType(int pricingType) {
        this.pricingType = pricingType;
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

    public double getPriceVIP() {
        return priceVIP;
    }

    public void setPriceVIP(double priceVIP) {
        this.priceVIP = priceVIP;
    }

    public double getPriceWholesale() {
        return priceWholesale;
    }

    public void setPriceWholesale(double priceWholesale) {
        this.priceWholesale = priceWholesale;
    }

    public int getCanChangePrice() {
        return canChangePrice;
    }

    public void setCanChangePrice(int canChangePrice) {
        this.canChangePrice = canChangePrice;
    }

    public int getRuleOfPoint() {
        return ruleOfPoint;
    }

    public void setRuleOfPoint(int ruleOfPoint) {
        this.ruleOfPoint = ruleOfPoint;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public int getReturnDays() {
        return returnDays;
    }

    public void setReturnDays(int returnDays) {
        this.returnDays = returnDays;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getPurchaseFlag() {
        return purchaseFlag;
    }

    public void setPurchaseFlag(int purchaseFlag) {
        this.purchaseFlag = purchaseFlag;
    }

    public int getRefCommodityID() {
        return refCommodityID;
    }

    public void setRefCommodityID(int refCommodityID) {
        this.refCommodityID = refCommodityID;
    }

    public int getRefCommodityMultiple() {
        return refCommodityMultiple;
    }

    public void setRefCommodityMultiple(int refCommodityMultiple) {
        this.refCommodityMultiple = refCommodityMultiple;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getNO() {
        return NO;
    }

    public void setNO(int NO) {
        this.NO = NO;
    }

    public String getSubCommodityInfo() {
        return subCommodityInfo;
    }

    public void setSubCommodityInfo(String subCommodityInfo) {
        this.subCommodityInfo = subCommodityInfo;
    }

    public String getMultiPackagingInfo() {
        return multiPackagingInfo;
    }

    public void setMultiPackagingInfo(String multiPackagingInfo) {
        this.multiPackagingInfo = multiPackagingInfo;
    }

    public String getProviderIDs() {
        return providerIDs;
    }

    public void setProviderIDs(String providerIDs) {
        this.providerIDs = providerIDs;
    }

    public int getBarcodeID() {
        return barcodeID;
    }

    public void setBarcodeID(int barcodeID) {
        this.barcodeID = barcodeID;
    }


    @Override
    public String toString() {
        return "Commodity{" +
                "type='" + type + '\'' +
                ",subtotal='" + subtotal + '\'' +
                ",after_discount='" + after_discount + '\'' +
                ",discount='" + discount + '\'' +
                ",NO='" + NO + '\'' +
                ", tag='" + tag + '\'' +
                ", refCommodityMultiple='" + refCommodityMultiple + '\'' +
                ", refCommodityID='" + refCommodityID + '\'' +
                ", purchaseFlag='" + purchaseFlag + '\'' +
                ", createDate='" + createDate + '\'' +
                ", returnDays='" + returnDays + '\'' +
                ", shelfLife='" + shelfLife + '\'' +
                ", picture='" + picture + '\'' +
                ", ruleOfPoint='" + ruleOfPoint + '\'' +
                ", canChangePrice='" + canChangePrice + '\'' +
                ", priceWholesale='" + priceWholesale + '\'' +
                ", priceRetail='" + priceRetail + '\'' +
                ", priceVIP='" + priceVIP + '\'' +
                ", latestPricePurchase='" + latestPricePurchase + '\'' +
                ", pricingType='" + pricingType + '\'' +
                ", mnemonicCode='" + mnemonicCode + '\'' +
                ", categoryID='" + categoryID + '\'' +
                ", brandID='" + brandID + '\'' +
                ", purchasingUnit='" + purchasingUnit + '\'' +
                ", packageUnitID='" + packageUnitID + '\'' +
                ", specification='" + specification + '\'' +
                ", shortName='" + shortName + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", ID=" + ID + '\'' +
                ",commodityQuantity" + commodityQuantity +
                '}';
    }

    @Override
    public List<BaseModel> parseN(String s) {
        System.out.println("正在执行Barcodes的parseN，s=" + s);

        List<BaseModel> commList = new ArrayList<BaseModel>();
        try {
            JSONArray jsonArray = JSONArray.parseArray(s);
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Commodity c = new Commodity();
                c.doParse1(jsonObject1);
                commList.add(c);
            }
        } catch (Exception e) {
            System.out.println("Commodity.parseN()出错：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return commList;
    }

    @Override
    public List<BaseModel> parseN(JSONArray ja) {
        System.out.println("正在执行Barcodes的parseN，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> commodityList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                Commodity commodity = new Commodity();
                if (commodity.parse1(jsonObject.toString()) == null) {
                    return null;
                }
                commodityList.add(commodity);
            }
            return commodityList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Commodity.parseN()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    public BaseModel parse1(String s) {
        System.out.println("正在执行Barcodes的parse1，s=" + s);

        JSONObject jo = null;
        try {
            return doParse1(JSONObject.parseObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Commodity.parse1()出错：" + e.getMessage());
            return null;
        }
    }

    protected BaseModel doParse1(JSONObject jo) {
        System.out.println("正在执行Commodity的doParse1，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = Integer.valueOf(jo.getString(field.getFIELD_NAME_ID()));
            status = Integer.parseInt(jo.getString(field.getFIELD_NAME_status()));
            name = jo.getString(field.getFIELD_NAME_name());
            shortName = jo.getString(field.getFIELD_NAME_shortName());
            specification = jo.getString(field.getFIELD_NAME_specification());
            packageUnitID = Integer.valueOf(jo.getString(field.getFIELD_NAME_packageUnitID()));
            purchasingUnit = jo.getString(field.getFIELD_NAME_purchasingUnit());
            brandID = Integer.valueOf(jo.getString(field.getFIELD_NAME_brandID()));
            categoryID = Integer.valueOf(jo.getString(field.getFIELD_NAME_categoryID()));
            mnemonicCode = jo.getString(field.getFIELD_NAME_mnemonicCode());
            pricingType = Integer.valueOf(jo.getString(field.getFIELD_NAME_pricingType()));
//            pricePurchase = Double.valueOf(jo.getString(getFIELD_NAME_pricePurchase()));
            latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
            priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
            priceVIP = jo.getDouble(field.getFIELD_NAME_priceVIP());
            priceWholesale = jo.getDouble(field.getFIELD_NAME_priceWholesale());
            canChangePrice = Integer.valueOf(jo.getString(field.getFIELD_NAME_canChangePrice()));
            ruleOfPoint = Integer.valueOf(jo.getString(field.getFIELD_NAME_ruleOfPoint()));
            picture = jo.getString(field.getFIELD_NAME_picture());
            shelfLife = Integer.valueOf(jo.getString(field.getFIELD_NAME_shelfLife()));
            returnDays = Integer.valueOf(jo.getString(field.getFIELD_NAME_returnDays()));
            purchaseFlag = Integer.valueOf(jo.getString(field.getFIELD_NAME_purchaseFlag()));
            refCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_refCommodityID()));
            refCommodityMultiple = Integer.valueOf(jo.getString(field.getFIELD_NAME_refCommodityMultiple()));
            tag = jo.getString(field.getFIELD_NAME_tag());
            NO = Integer.valueOf(jo.getString(field.getFIELD_NAME_NO()));
            type = jo.getInteger(field.getFIELD_NAME_type());
            nOStart = jo.getInteger(field.getFIELD_NAME_nOStart());
            purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
            syncSequence = jo.getInteger(field.getFIELD_NAME_syncSequence());
            syncType = jo.getString(field.getFIELD_NAME_syncType()); // ...
//            startValueRemark = jo.getString("startValueRemark");
//            propertyValue1 = jo.getString("propertyValue1");
//            propertyValue2 = jo.getString("propertyValue2");
//            propertyValue3 = jo.getString("propertyValue3");
//            propertyValue4 = jo.getString("propertyValue4");

            String tmpCreateDate = jo.getString(field.getFIELD_NAME_createDate());
            if (!StringUtils.isEmpty(tmpCreateDate)) {
                createDate = Constants.getSimpleDateFormat2().parse(tmpCreateDate);
                if (createDate == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpCreateDate);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpCreateDate);
                }
            }

            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!StringUtils.isEmpty(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
                if (createDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
                }
            }

            String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
            if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
                if (updateDatetime == null) {
                    System.out.println("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                    throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
                }
            }
            operatorStaffID = jo.getInteger(field.getFIELD_NAME_operatorStaffID());
            barcode = jo.getString(field.getFIELD_NAME_barcodes());
            // 查询商品门店集合
            List<CommodityShopInfo> listCommShopSlave = new ArrayList<CommodityShopInfo>();
            JSONArray jrCommShopSlave = jo.getJSONArray(field.getFIELD_NAME_listSlave2());
            for (int i = 0; i < jrCommShopSlave.size(); i++) {
                JSONObject object2 = jrCommShopSlave.getJSONObject(i);
                CommodityShopInfo commodityShopInfo = (CommodityShopInfo) new CommodityShopInfo().parse1(object2.toString());
                listCommShopSlave.add(commodityShopInfo);
            }
            listSlave2 = listCommShopSlave;
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Commodity.doParse1()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<BaseModel> parseNC(JSONArray ja) {
        System.out.println("正在执行Commodity的parseNC，ja=" + (ja == null ? null : ja.toString()));

        List<BaseModel> commodityList = new ArrayList<BaseModel>();
        try {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                JSONObject jo = jsonObject.getJSONObject("commodity");
                Commodity commodity = new Commodity();
                if (commodity.parse1C(jo.toString()) == null) {
                    return null;
                }

                JSONArray jsonArray = jsonObject.getJSONArray("listMultiPackageCommodity");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject jo2 = jsonArray.getJSONObject(j);
                    Commodity commodity1 = new Commodity();
                    if (commodity1.parse1C(jo2.toString()) == null) {
                        return null;
                    }
                    commodityList.add(commodity1);
                }
                commodityList.add(commodity);
            }
            return commodityList;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Commodity.parseNC()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    public BaseModel parse1C(String s) {
        System.out.println("正在执行Commodity的parse1C，s=" + s);

        try {
            return doParse1C(JSONObject.parseObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Commodity.parse1C()出错：" + e.getMessage());
            return null;
        }
    }

    @Override
    public BaseModel doParse1C(JSONObject jo) {
        System.out.println("正在执行Commodity的doParse1C，jo=" + (jo == null ? null : jo.toString()));

        try {
            ID = jo.getInteger(field.getFIELD_NAME_ID());
            NO = jo.getInteger(field.getFIELD_NAME_NO());
            brandID = jo.getInteger(field.getFIELD_NAME_brandID());
            canChangePrice = jo.getInteger(field.getFIELD_NAME_canChangePrice());
            categoryID = jo.getInteger(field.getFIELD_NAME_categoryID());
            latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
            mnemonicCode = jo.getString(field.getFIELD_NAME_mnemonicCode());
            nOStart = jo.getInteger(field.getFIELD_NAME_nOStart());
            name = jo.getString(Commodity.field.getFIELD_NAME_name());
            packageUnitID = jo.getInteger(field.getFIELD_NAME_packageUnitID());
            picture = jo.getString(field.getFIELD_NAME_picture());
//            pricePurchase = Double.parseDouble(jo.getString(getFIELD_NAME_pricePurchase()));
            priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
            priceVIP = jo.getDouble(field.getFIELD_NAME_priceVIP());
            priceWholesale = jo.getDouble(field.getFIELD_NAME_priceWholesale());
            pricingType = jo.getInteger(field.getFIELD_NAME_pricingType());
            purchaseFlag = jo.getInteger(field.getFIELD_NAME_purchaseFlag());
            purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
            purchasingUnit = jo.getString(field.getFIELD_NAME_purchasingUnit());
            refCommodityID = jo.getInteger(field.getFIELD_NAME_refCommodityID());
            refCommodityMultiple = jo.getInteger(field.getFIELD_NAME_refCommodityMultiple());
            returnDays = jo.getInteger(field.getFIELD_NAME_returnDays());
            ruleOfPoint = jo.getInteger(field.getFIELD_NAME_ruleOfPoint());
            shelfLife = jo.getInteger(field.getFIELD_NAME_shelfLife());
            shortName = jo.getString(field.getFIELD_NAME_shortName());
            specification = jo.getString(field.getFIELD_NAME_specification());
            status = jo.getInteger(field.getFIELD_NAME_status());
//            int2 = jo.getInteger(getFIELD_NAME_int2());
            subCommodityInfo = jo.getString(field.getFIELD_NAME_subCommodityInfo());
            multiPackagingInfo = jo.getString(field.getFIELD_NAME_multiPackagingInfo());
            providerIDs = jo.getString(field.getFIELD_NAME_providerIDs());
            tag = jo.getString(field.getFIELD_NAME_tag());
            type = jo.getInteger(field.getFIELD_NAME_type());

            String tmpCreateDate = jo.getString(field.getFIELD_NAME_createDate());
            if (!"".equals(tmpCreateDate)) {
                createDate = Constants.getSimpleDateFormat2().parse(tmpCreateDate);
            }

            String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
            if (!"".equals(tmpCreateDatetime)) {
                createDatetime = Constants.getSimpleDateFormat2().parse(tmpCreateDatetime);
            }

            String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
            if (!"".equals(tmpUpdateDatetime)) {
                updateDatetime = Constants.getSimpleDateFormat2().parse(tmpUpdateDatetime);
            }

            String tmpSyncDatetime = jo.getString(field.getFIELD_NAME_syncDatetime());
            if (!"".equals(tmpSyncDatetime)) {
                syncDatetime = Constants.getSimpleDateFormat2().parse(tmpSyncDatetime);
            }

            syncType = jo.getString(field.getFIELD_NAME_syncType());
            barcode = jo.getString(field.getFIELD_NAME_barcodes());
            // 查询商品门店集合
            List<CommodityShopInfo> listCommShopSlave = new ArrayList<CommodityShopInfo>();
            JSONArray jrCommShopSlave = jo.getJSONArray(field.getFIELD_NAME_listSlave2());
            for (int i = 0; i < jrCommShopSlave.size(); i++) {
                JSONObject object2 = jrCommShopSlave.getJSONObject(i);
                CommodityShopInfo commodityShopInfo = (CommodityShopInfo) new CommodityShopInfo().parse1(object2.toString());
                listCommShopSlave.add(commodityShopInfo);
            }
            listSlave2 = listCommShopSlave;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("commodity.doParse1C()出错：" + e.getMessage());
            return null;
        }
        return this;
    }

    @Override
    public int compareTo(final BaseModel arg0) {
        if (arg0 == null) {
            return -1;
        }

        Commodity comm = (Commodity) arg0;
        if ((ignoreIDInComparision == true ? true : (comm.getID().equals(this.getID()) && printComparator("ID"))) //
                && comm.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status()) //
                && comm.getName().equals(this.getName()) && printComparator(field.getFIELD_NAME_name()) //
                && comm.getShortName().equals(this.getShortName()) && printComparator(field.getFIELD_NAME_shortName()) //
                && comm.getSpecification().equals(this.getSpecification()) && printComparator(field.getFIELD_NAME_specification()) //
                && comm.getPackageUnitID() == this.getPackageUnitID() && printComparator(field.getFIELD_NAME_packageUnitID()) //
                && comm.getPurchasingUnit().equals(this.getPurchasingUnit()) && printComparator(field.getFIELD_NAME_purchasingUnit()) //
                && comm.getBrandID() == this.getBrandID() && printComparator(field.getFIELD_NAME_brandID()) //
                && comm.getCategoryID() == this.getCategoryID() && printComparator(field.getFIELD_NAME_categoryID()) //
                && comm.getMnemonicCode().equals(this.getMnemonicCode()) && printComparator(field.getFIELD_NAME_mnemonicCode()) //
                && comm.getPricingType() == this.getPricingType() && printComparator(field.getFIELD_NAME_pricingType()) //
                && Math.abs(GeneralUtil.sub(comm.getLatestPricePurchase(), this.getLatestPricePurchase())) < TOLERANCE && printComparator(field.getFIELD_NAME_latestPricePurchase())
                && Math.abs(GeneralUtil.sub(comm.getPriceRetail(), this.getPriceRetail())) < TOLERANCE && printComparator(field.getFIELD_NAME_priceRetail())
                && Math.abs(GeneralUtil.sub(comm.getPriceVIP(), this.getPriceVIP())) < TOLERANCE && printComparator(field.getFIELD_NAME_priceVIP()) //
                && Math.abs(GeneralUtil.sub(comm.getPriceWholesale(), this.getPriceWholesale())) < TOLERANCE && printComparator(field.getFIELD_NAME_priceWholesale()) //
                && comm.getCanChangePrice() == this.getCanChangePrice() && printComparator(field.getFIELD_NAME_canChangePrice()) //
                && comm.getRuleOfPoint() == this.getRuleOfPoint() && printComparator(field.getFIELD_NAME_ruleOfPoint()) //
                && comm.getPicture().equals(this.getPicture()) && printComparator(field.getFIELD_NAME_picture()) //
                && comm.getShelfLife() == this.getShelfLife() && printComparator(field.getFIELD_NAME_shelfLife()) //
                && comm.getReturnDays() == this.getReturnDays() && printComparator(field.getFIELD_NAME_returnDays()) //
                && DatetimeUtil.compareDate(comm.getCreateDate(), this.getCreateDate()) && printComparator(field.getFIELD_NAME_createDate()) //
                && comm.getPurchaseFlag() == this.getPurchaseFlag() && printComparator(field.getFIELD_NAME_purchaseFlag()) //
                && comm.getRefCommodityID() == this.getRefCommodityID() && printComparator(field.getFIELD_NAME_refCommodityID()) //
                && comm.getRefCommodityMultiple() == this.getRefCommodityMultiple() && printComparator(field.getFIELD_NAME_refCommodityMultiple()) //
                && comm.getTag().equals(this.getTag()) && printComparator(field.getFIELD_NAME_tag()) //
                && comm.getNO() == this.getNO() && printComparator(field.getFIELD_NAME_NO()) //
                && comm.getType() == this.getType() && printComparator(field.getFIELD_NAME_type())
                && comm.getnOStart() == this.getnOStart() && printComparator(field.getFIELD_NAME_nOStart())
                && Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), this.getPurchasingPriceStart())) < TOLERANCE && printComparator(field.getFIELD_NAME_purchasingPriceStart())
                && comm.getStartValueRemark().equals(this.getStartValueRemark()) && printComparator(field.getFIELD_NAME_startValueRemark())
                && comm.getPropertyValue1().equals(this.getPropertyValue1()) && printComparator(field.getFIELD_NAME_propertyValue1())
                && comm.getPropertyValue2().equals(this.getPropertyValue2()) && printComparator(field.getFIELD_NAME_propertyValue2())
                && comm.getPropertyValue3().equals(this.getPropertyValue3()) && printComparator(field.getFIELD_NAME_propertyValue3())
                && comm.getPropertyValue4().equals(this.getPropertyValue4()) && printComparator(field.getFIELD_NAME_propertyValue4())

//                && comm.getSyncDatetime().equals(this.getSyncDatetime()) && printComparator(getFIELD_NAME_syncDatetime())

                && DatetimeUtil.compareDate(comm.getCreateDatetime(), this.getCreateDatetime()) && printComparator(field.getFIELD_NAME_createDatetime())
                && DatetimeUtil.compareDate(comm.getUpdateDatetime(), this.getUpdateDatetime()) && printComparator(field.getFIELD_NAME_updateDatetime())
                && comm.getSyncType().equals(this.getSyncType()) && printComparator(field.getFIELD_NAME_syncType())
        ) {
            return 0;
        }
        return -1;
    }

    @Override
    public BaseModel clone() throws CloneNotSupportedException {
        Commodity obj = new Commodity();
        obj.setID(this.getID());
        obj.setStatus(this.getStatus());
        obj.setName(new String(this.getName()));
        obj.setShortName(new String(this.getShortName()));
        obj.setSpecification(new String(this.getSpecification()));
        obj.setPackageUnitID(this.getPackageUnitID());
        obj.setPackageUnit(this.getPackageUnit());
        obj.setPurchasingUnit(new String(this.getPurchasingUnit()));
        obj.setBrandID(this.getBrandID());
        obj.setCategoryID(this.getCategoryID());
        obj.setMnemonicCode(new String(this.getMnemonicCode()));
        obj.setPricingType(this.getPricingType());
        obj.setLatestPricePurchase(this.getLatestPricePurchase());
        obj.setPriceRetail(this.getPriceRetail());
        obj.setPriceVIP(this.getPriceVIP());
        obj.setPriceWholesale(this.getPriceWholesale());
        obj.setCanChangePrice(this.getCanChangePrice());
        obj.setRuleOfPoint(this.getRuleOfPoint());
        obj.setPicture(new String(this.getPicture()));
        obj.setShelfLife(this.getShelfLife());
        obj.setReturnDays(this.getReturnDays());
        obj.setCreateDate(this.getCreateDate() == null ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : (Date) this.getCreateDate().clone());
        obj.setPurchaseFlag(this.getPurchaseFlag());
        obj.setRefCommodityID(this.getRefCommodityID());
        obj.setRefCommodityMultiple(this.getRefCommodityMultiple());
        obj.setTag(new String(this.getTag()));
        obj.setNO(this.getNO());
        obj.setType(this.getType());
        obj.setnOStart(this.getnOStart());
        obj.setPurchasingPriceStart(this.getPurchasingPriceStart());
        obj.setStartValueRemark(startValueRemark);
        obj.setPropertyValue1(propertyValue1);
        obj.setPropertyValue2(propertyValue2);
        obj.setPropertyValue3(propertyValue3);
        obj.setPropertyValue4(propertyValue4);
//        obj.setStartValueRemark(new String(this.getStartValueRemark()));
//        obj.setPropertyValue1(new String(this.getPropertyValue1()));
//        obj.setPropertyValue2(new String(this.getPropertyValue2()));
//        obj.setPropertyValue3(new String(this.getPropertyValue3()));
//        obj.setPropertyValue4(new String(this.getPropertyValue4()));
        obj.setCreateDatetime(this.getCreateDatetime() == null ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : (Date) this.getCreateDatetime().clone());
        obj.setUpdateDatetime(this.getUpdateDatetime() == null ? new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference) : (Date) this.getUpdateDatetime().clone());
        obj.setMultiPackagingInfo(this.getMultiPackagingInfo());
        obj.setBarcode(new String(this.getBarcode()));
        obj.setOperatorStaffID(this.getOperatorStaffID()); // ...
        obj.setSubCommodityInfo(this.getSubCommodityInfo());
        obj.setCommodityQuantity(this.getCommodityQuantity());
        obj.setSubtotal(this.getSubtotal());
        if (listSlave1 != null) {
            List<BaseModel> list = new ArrayList<BaseModel>();
            for (Object o : listSlave1) {
                SubCommodity subCommodity = (SubCommodity) o;
                list.add(subCommodity.clone());
            }
            obj.setListSlave1(list);
        }
        // listSlave2：商品门店信息
        if(listSlave2 != null) {
            List<BaseModel> list = new ArrayList<BaseModel>();
            for (Object o : listSlave2) {
                CommodityShopInfo commodityShopInfo = (CommodityShopInfo) o;
                list.add(commodityShopInfo.clone());
            }
            obj.setListSlave2(list);
        }
        obj.setReturnObject(this.getReturnObject());
        obj.setProviderIDs(this.getProviderIDs());
        obj.setNumber(this.getNumber());

        return obj;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getnOStart() {
        return this.nOStart;
    }

    public void setnOStart(int nOStart) {
        this.nOStart = nOStart;
    }

    public double getPurchasingPriceStart() {
        return this.purchasingPriceStart;
    }

    public void setPurchasingPriceStart(double purchasingPriceStart) {
        this.purchasingPriceStart = purchasingPriceStart;
    }

    public int getPackageUnitID() {
        return this.packageUnitID;
    }

    public void setPackageUnitID(int packageUnitID) {
        this.packageUnitID = packageUnitID;
    }

    public int getNOStart() {
        return this.nOStart;
    }

    public void setNOStart(int nOStart) {
        this.nOStart = nOStart;
    }

    public String getStartValueRemark() {
        return this.startValueRemark;
    }

    public void setStartValueRemark(String startValueRemark) {
        this.startValueRemark = startValueRemark;
    }

    public String getPropertyValue1() {
        return this.propertyValue1;
    }

    public void setPropertyValue1(String propertyValue1) {
        this.propertyValue1 = propertyValue1;
    }

    public String getPropertyValue2() {
        return this.propertyValue2;
    }

    public void setPropertyValue2(String propertyValue2) {
        this.propertyValue2 = propertyValue2;
    }

    public String getPropertyValue3() {
        return this.propertyValue3;
    }

    public void setPropertyValue3(String propertyValue3) {
        this.propertyValue3 = propertyValue3;
    }

    public String getPropertyValue4() {
        return this.propertyValue4;
    }

    public void setPropertyValue4(String propertyValue4) {
        this.propertyValue4 = propertyValue4;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public Date getSyncDatetime() {
        return this.syncDatetime;
    }

    @Override
    public void setSyncDatetime(Date syncDatetime) {
        this.syncDatetime = syncDatetime;
    }

    @Override
    public String getSyncType() {
        return this.syncType;
    }

    @Override
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
    }

    public enum EnumStatusCommodity {
        ESC_Normal("Normal", 0), //
        ESC_ToEliminated("ToEliminated", 1), //
        ESC_Deleted("Deleted", 2);

        private String name;
        private int index;

        private EnumStatusCommodity(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumStatusCommodity c : EnumStatusCommodity.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    @Override
    public String checkCreate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (!StringUtils.isEmpty(mnemonicCode)) {
            if (printCheckField(field.getFIELD_NAME_mnemonicCode(), FIELD_ERROR_mnemonicCode, sbError)
                    && !(FieldFormat.checkMnemonicCode(mnemonicCode) && mnemonicCode.length() >= MIN_LENGTH_MnemonicCode && mnemonicCode.length() <= MAX_LENGTH_MnemonicCode)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(tag)) {
            if (printCheckField(field.getFIELD_NAME_tag(), FIELD_ERROR_tag, sbError) && !(tag.length() >= MIN_LENGTH_Tag && tag.length() <= MAX_LENGTH_Tag)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(shortName)) {
            if (printCheckField(field.getFIELD_NAME_shortName(), FIELD_ERROR_shortName, sbError) && !(FieldFormat.checkShortName(shortName) && shortName.length() <= MAX_LENGTH_ShortName)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(startValueRemark)) {
            if (printCheckField(field.getFIELD_NAME_startValueRemark(), FIELD_ERROR_startValueRemark, sbError) && !(FieldFormat.checkShortName(startValueRemark) && startValueRemark.length() <= MAX_LENGTH_StartValueRemark)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue1)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue1(), FIELD_ERROR_propertyValue, sbError) && propertyValue1.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue2)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue2(), FIELD_ERROR_propertyValue, sbError) && propertyValue2.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue3)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue3(), FIELD_ERROR_propertyValue, sbError) && propertyValue3.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue4)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue4(), FIELD_ERROR_propertyValue, sbError) && propertyValue4.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCommodityName(name) && name.length() >= MIN_LENGTH_Name && name.length() <= MAX_LENGTH_Name)) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_specification(), FIELD_ERROR_specification, sbError)
                && !(FieldFormat.checkSpecification(specification) && specification.length() > MIN_LENGTH_Specification && specification.length() <= MAX_LENGTH_Specification)) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_packageUnitID(), FIELD_ERROR_packageUnitID, sbError) && !(FieldFormat.checkPackageUnitID(String.valueOf(packageUnitID)))) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_brandID(), FIELD_ERROR_brandID, sbError) && !FieldFormat.checkBrandID(String.valueOf(brandID))) {//
            return sbError.toString();
        }

        pricingType = EnumPricingType.EPT_ByCount.getIndex();// 暂时写死计价方式字段为计件
        if (printCheckField(field.getFIELD_NAME_pricingType(), FIELD_ERROR_pricingType, sbError) && pricingType != EnumPricingType.EPT_ByCount.getIndex()) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && !FieldFormat.checkCategoryID(String.valueOf(categoryID))) {//
            return sbError.toString();
        }
        if (printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && !FieldFormat.checkPrice(priceRetail)) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && !FieldFormat.checkPriceVIP(priceVIP)) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && !FieldFormat.checkPriceWholesale(priceWholesale)) {//
            return sbError.toString();
        }

        canChangePrice = EnumBoolean.EB_NO.getIndex();// 暂时写死前台是否能改价字段为否
        if (printCheckField(field.getFIELD_NAME_canChangePrice(), FIELD_ERROR_canChangePrice, sbError) && canChangePrice != EnumBoolean.EB_NO.getIndex()) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_returnDays(), FIELD_ERROR_returnDays, sbError) && !FieldFormat.checkReturnDays(String.valueOf(returnDays))) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(operatorStaffID)) {
            return sbError.toString();
        }
        // ..TODO
//        if (printCheckField(getFIELD_NAME_string1(), FIELD_ERROR_barcode, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(String.valueOf(string1))) {// string1存储条形码信息
//            return sbError.toString();
//        }

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_Commodity_CreateComposition:
                if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !FieldFormat.checkIfCommodityStatus(status)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeOfComposition, sbError) && !FieldFormat.checkIfCompositionType(type)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodity, sbError) //
                        && !FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple)) {
                    return sbError.toString();
                }
                return "";
            case BaseSQLiteBO.CASE_Commodity_CreateMultiPackaging:
                if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeOfMultipackaging, sbError) && FieldFormat.checkIfMultiPackagingType(type) //
                        && printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_statusOfMultipackaging, sbError) && FieldFormat.checkIfMultiPackagingStatus(status) //
                        && printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodityOfMultiPackaging, sbError) //
                        && FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple) //
                        // && this.printCheckField("barcodes", "条形码为英文或数字的组合，长度为1 ~ 64", sbError) &&
                        // FieldFormat.checkIfMultiPackagingBarcodes(this.getString1()) //...条形码的问题有待商榷
                        // && printCheckField(getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife,
                        // sbError) && FieldFormat.checkShelfLife(String.valueOf(shelfLife)) //
                        && printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NOofMultiPackaging, sbError) && FieldFormat.checkIfMultiPackagingNO(NO)) {
                    return "";
                }
                return sbError.toString();
            case BaseSQLiteBO.CASE_Commodity_CreateService:
                if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !FieldFormat.checkIfCommodityStatus(status)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeOfService, sbError) && !FieldFormat.checkIfServiceType(type)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_purchasingUnit(), FIELD_ERROR_purchasingUnitOfService, sbError) && !StringUtils.isEmpty(purchasingUnit)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_NO(), FEIDL_ERROR_noOfService, sbError) && NO != 0) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_nOStart(), FIELD_ERROR_nOStartOfService, sbError) && nOStart != NO_START_Default) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), FIELD_ERROR_purchasingPriceStartOfService, sbError) && purchasingPriceStart != PURCHASING_PRICE_START_Default) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodity, sbError) //
                        && !FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLifeOfService, sbError) && shelfLife != 0) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_latestPricePurchase(), FIELD_ERROR_latestPricePurchaseOfService, sbError) && latestPricePurchase != DEFAULT_VALUE_LatestPricePurchase) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_purchaseFlag(), FIELD_ERROR_purchaseFlagOfService, sbError) && purchaseFlag != 0) {
                    return sbError.toString();
                }
                return "";
            default: // case BaseHttpBO.CASE_Commodity_CreateSingle:
                if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_type, sbError) && !FieldFormat.checkIfCommodityType(type)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !FieldFormat.checkIfCommodityStatus(status)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife, sbError) && !FieldFormat.checkShelfLife(String.valueOf(shelfLife))) {
                    return sbError.toString();
                }

                purchaseFlag = PurchaseFlag_START_Default;// 暂时把采购阀值字段写死为1
                if (printCheckField(field.getFIELD_NAME_purchaseFlag(), FIELD_ERROR_purchaseFlag, sbError) && !FieldFormat.checkPurchaseFlag(String.valueOf(purchaseFlag))) {
                    return sbError.toString();
                }
                if (printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodity, sbError) //
                        && !FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple)) {
                    return sbError.toString();
                }
                //... 只有普通商品才能够设置为期初商品。
                if (nOStart != -1 || purchasingPriceStart != -1D) {
                    if (printCheckField(field.getFIELD_NAME_nOStart(), FIELD_ERROR_nOStart, sbError) && !FieldFormat.checkID(nOStart)) {
                        return sbError.toString();
                    }
                    if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), FIELD_ERROR_purchasingPriceStart, sbError) && purchasingPriceStart <= 0) {
                        return sbError.toString();
                    }
                }

                ruleOfPoint = RuleOfPoint_START_Default;// 暂时把积分规则字段写死为1
                if (printCheckField(field.getFIELD_NAME_ruleOfPoint(), FIELD_ERROR_ruleOfPoint, sbError) && !FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint))) { // 积分规则允许为0的情况
                    return sbError.toString();
                }
        }
        return "";
    }

    @Override
    public String checkUpdate(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();

        if (iUseCaseID == BaseSQLiteBO.CASE_UpdateCommodityOfNO) {
            if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
            {
                return sbError.toString();
            }
            //库存可以小于0
//            if (this.printCheckField(getFIELD_NAME_NO(), FIELD_ERROR_NO, sbError) && NO < MIN_NO) //
//            {
//                return sbError.toString();
//            }
            return "";
        }

        if (!StringUtils.isEmpty(mnemonicCode)) {
            if (printCheckField(field.getFIELD_NAME_mnemonicCode(), FIELD_ERROR_mnemonicCode, sbError) && !(FieldFormat.checkMnemonicCode(mnemonicCode) && mnemonicCode.length() >= 0 && mnemonicCode.length() <= 32)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(tag)) {
            if (printCheckField(field.getFIELD_NAME_tag(), FIELD_ERROR_tag, sbError) && !(tag.length() >= MIN_LENGTH_Tag && tag.length() <= MAX_LENGTH_Tag)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(shortName)) {
            if (printCheckField(field.getFIELD_NAME_shortName(), FIELD_ERROR_shortName, sbError) && !(FieldFormat.checkShortName(shortName) && shortName.length() <= MAX_LENGTH_ShortName)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(startValueRemark)) {
            if (printCheckField(field.getFIELD_NAME_startValueRemark(), FIELD_ERROR_startValueRemark, sbError) && !(FieldFormat.checkShortName(startValueRemark) && startValueRemark.length() <= MAX_LENGTH_StartValueRemark)) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue1)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue1(), FIELD_ERROR_propertyValue, sbError) && propertyValue1.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue2)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue2(), FIELD_ERROR_propertyValue, sbError) && propertyValue2.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue3)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue3(), FIELD_ERROR_propertyValue, sbError) && propertyValue3.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        if (!StringUtils.isEmpty(propertyValue4)) {
            if (printCheckField(field.getFIELD_NAME_propertyValue4(), FIELD_ERROR_propertyValue, sbError) && propertyValue4.length() > MAX_LENGTH_PropertyValue) {
                return sbError.toString();
            }
        }

        pricingType = EnumPricingType.EPT_ByCount.getIndex();// 暂时写死计价方式字段为计件
        if (printCheckField(field.getFIELD_NAME_pricingType(), FIELD_ERROR_pricingType, sbError) && pricingType != EnumPricingType.EPT_ByCount.getIndex()) {//
            return sbError.toString();
        }

        canChangePrice = EnumBoolean.EB_NO.getIndex();// 暂时写死前台是否能改价字段为否
        if (printCheckField(field.getFIELD_NAME_canChangePrice(), FIELD_ERROR_canChangePrice, sbError) && canChangePrice != EnumBoolean.EB_NO.getIndex()) {//
            return sbError.toString();
        }

        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && FieldFormat.checkID(ID)//
                && printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && (FieldFormat.checkCommodityName(name) && name.length() >= MIN_LENGTH_Name && name.length() <= MAX_LENGTH_Name) //
                && printCheckField(field.getFIELD_NAME_specification(), FIELD_ERROR_specification, sbError)
                && (FieldFormat.checkSpecification(specification) && specification.length() >= MIN_LENGTH_Specification && specification.length() <= MAX_LENGTH_Specification) //
                && printCheckField(field.getFIELD_NAME_packageUnitID(), FIELD_ERROR_packageUnitID, sbError) && (FieldFormat.checkPackageUnitID(String.valueOf(packageUnitID))) //
                && printCheckField(field.getFIELD_NAME_brandID(), FIELD_ERROR_brandID, sbError) && FieldFormat.checkBrandID(String.valueOf(brandID))//
                && printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && FieldFormat.checkCategoryID(String.valueOf(categoryID)) //
                && printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && FieldFormat.checkPrice(priceRetail) //
                && printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && FieldFormat.checkPriceVIP(priceVIP) //
                && printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && FieldFormat.checkPriceWholesale(priceWholesale) //
                // && printCheckField(getFIELD_NAME_string1(), FIELD_ERROR_barcode,
                // sbError) &&
                // FieldFormat.checkIfMultiPackagingBarcodes(String.valueOf(string1)) //
                // string1存储条形码信息
                // && printCheckField(getFIELD_NAME_ruleOfPoint(),
                // FIELD_ERROR_ruleOfPoint, sbError) &&
                // FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint)) //
                // && printCheckField(getFIELD_NAME_purchaseFlag(),
                // FIELD_ERROR_purchaseFlag, sbError) &&
                // FieldFormat.checkPurchaseFlag(String.valueOf(purchaseFlag))//
                && printCheckField(field.getFIELD_NAME_returnDays(), FIELD_ERROR_returnDays, sbError) && FieldFormat.checkReturnDays(String.valueOf(returnDays)) //
                && printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_commodityStatus, sbError) && (status == EnumStatusCommodity.ESC_Normal.index || status == EnumStatusCommodity.ESC_ToEliminated.index)) {
        } else {
            return sbError.toString();
        }

        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_UpdateCommodityOfMultiPackaging:
                if (printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && FieldFormat.checkPriceVIP(priceVIP) //
                        && printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && FieldFormat.checkPriceWholesale(priceWholesale) //
                        && printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeOfMultipackaging, sbError) && FieldFormat.checkIfMultiPackagingType(type) //
                        && printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodityOfMultiPackaging, sbError) //
                        && FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple) //
                        // && this.printCheckField("barcodes", "条形码为英文或数字的组合，长度为1 ~ 64", sbError) &&
                        // FieldFormat.checkIfMultiPackagingBarcodes(this.getString1()) //
                        // ...因为不知道当前多包装对应的是哪个条形码，所以可能要到数据库或者缓存拿到对应的条形码//...条形码的问题有待商榷
                        && this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NOofMultiPackaging, sbError) && FieldFormat.checkIfMultiPackagingNO(NO)) {
                    return "";
                }
                return sbError.toString();
            case BaseSQLiteBO.CASE_Commodity_UpdatePrice:
                if ((printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && FieldFormat.checkPrice(priceRetail)) //
                        // && (this.printCheckField(getFIELD_NAME_pricePurchase(),
                        // FIELD_ERROR_pricePurchase, sbError) &&
                        // FieldFormat.checkPricePurchase(String.valueOf(pricePurchase))) //
                        && printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && FieldFormat.checkID(operatorStaffID) //
                        && (printCheckField(field.getFIELD_NAME_latestPricePurchase(), FIELD_ERROR_latestPricePurchase, sbError) //
                        && ((Math.abs(latestPricePurchase) - Math.abs(DEFAULT_VALUE_LatestPricePurchase)) < TOLERANCE || FieldFormat.checkPrice(latestPricePurchase)) //
                ) //
                ) {
                    return "";
                }
                return sbError.toString();

            case BaseSQLiteBO.CASE_UpdatePurchasingUnit:
                if (printCheckField(field.getFIELD_NAME_purchasingUnit(), FIELD_ERROR_purchasingUnit, sbError)//
                        && (FieldFormat.checkPurchasingUnit(purchasingUnit) && purchasingUnit.length() >= MIN_LENGTH_PurchasingUnit && purchasingUnit.length() <= MAX_LENGTH_PurchasingUnit) //
                ) {
                    return "";
                }
                return sbError.toString();
            case BaseSQLiteBO.CASE_UpdateCommodityOfService:
                if (printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodity, sbError) //
                        && !FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple)) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLifeOfService, sbError) && shelfLife != 0) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_purchaseFlag(), FIELD_ERROR_purchaseFlagOfService, sbError) && purchaseFlag != 0) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_ruleOfPoint(), FIELD_ERROR_ruleOfPoint, sbError) && !FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint))) {
                    return sbError.toString();
                }

//                if (printCheckField(getFIELD_NAME_int2(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(int2)) {  // staffID
//                    return sbError.toString();
//                }

                if (printCheckField(field.getFIELD_NAME_latestPricePurchase(), FIELD_ERROR_latestPricePurchaseOfService, sbError) && latestPricePurchase != DEFAULT_VALUE_LatestPricePurchase) {
                    return sbError.toString();
                }

                return "";
            default:
                if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_type, sbError) && !FieldFormat.checkIfCommodityType(type)) {
                    return sbError.toString();
                }

                // assert type == EnumCommodityType.ECT_Normal.getIndex();
                // refCommodityMultiple = DEFAULT_VALUE_RefCommodityMultiple;
                refCommodityID = DEFAULT_VALUE_RefCommodityID;
                if (printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodity, sbError) //
                        && !FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple)) {
                    return sbError.toString();
                }

                purchaseFlag = PurchaseFlag_START_Default;
                if (printCheckField(field.getFIELD_NAME_purchaseFlag(), FIELD_ERROR_purchaseFlag, sbError) && !FieldFormat.checkPurchaseFlag(String.valueOf(purchaseFlag))) {
                    return sbError.toString();
                }

                ruleOfPoint = RuleOfPoint_START_Default;
                if (printCheckField(field.getFIELD_NAME_ruleOfPoint(), FIELD_ERROR_ruleOfPoint, sbError) && !FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint))) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife, sbError) && !FieldFormat.checkShelfLife(String.valueOf(shelfLife))) {
                    return sbError.toString();
                }

//                if (printCheckField(getFIELD_NAME_int2(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(int2)) { // staffID
//                    return sbError.toString();
//                }
        }
        return "";
    }

    @Override
    public String checkRetrieve1(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
        {
            return sbError.toString();
        }
        return "";
    }

    @Override
    protected String doCheckRetrieveN(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (!StringUtils.isEmpty(sql)) {
            int total = StringUtils.contain(sql, "%s");
            if (this.printCheckField(field.getFIELD_NAME_sql(), FIELD_ERROR_SQL, sbError) && conditions != null && total != conditions.length) {
                return sbError.toString();
            }
        }
        switch (iUseCaseID) {
            case BaseSQLiteBO.CASE_CheckUniqueField:
                if (printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_checkUniqueFieldID, sbError) && ID < 0) { // 这个CASE允许ID=0的情况
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_fieldToCheckUnique(), FIELD_ERROR_checkUniqueField, sbError) && fieldToCheckUnique != 1) {
                    return sbError.toString();
                }

                if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_checkUniqueField, sbError) && !FieldFormat.checkCommodityName(queryKeyword)) {
                    return sbError.toString();// && StringUtils.isEmpty(string1)
                }

                return "";
            case BaseSQLiteBO.CASE_RetrieveNMultiPackageCommodity:
                if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
                    return sbError.toString();
                }

                return "";
            case BaseSQLiteBO.CASE_Commodity_RetrieveNByConditions:
                switch (subUseCaseID) {
                    case ESUC_String:
                        String name = conditions[0];
                        if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCommodityName(name) && name.length() >= MIN_LENGTH_Name && name.length() <= MAX_LENGTH_Name)) {//
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Long:
                        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[0]))) {//
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Long_String:
                        if (conditions.length == 2) {
                            if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(Long.valueOf(conditions[0]))) {//
                                return sbError.toString();
                            }
                            if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCommodityName(conditions[1]) && conditions[1].length() >= MIN_LENGTH_Name && conditions[1].length() <= MAX_LENGTH_Name)) {//
                                return sbError.toString();
                            }
                        } else {
                            return FieldFormat.FIELD_ERROR_Parameter;
                        }
                        break;
                    case ESUC_Int:
                        if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_commodityStatus, sbError) && Integer.valueOf(conditions[0]) != BaseSQLiteBO.INVALID_STATUS //
                                && !FieldFormat.checkIfMultiPackagingStatus(Integer.valueOf(conditions[0]))) {
                            return sbError.toString();
                        }
                        break;
                    case ESUC_Ignore:
                        break;
                }
                return "";
            case BaseHttpBO.CASE_Commodity_RetrieveInventory:
                if (this.printCheckField(field.getFIELD_NAME_barcodes(), FIELD_ERROR_barcode, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(barcode)) {
                    return sbError.toString();
                }
                return "";
            default:
                return "";
        }
    }

    @Override
    public String checkDelete(int iUseCaseID) {
        StringBuilder sbError = new StringBuilder();
        if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
            return sbError.toString();
        }
        return "";
    }

    public enum EnumPricingType {
        EPT_ByWeight("ByWeight", 0), //
        EPT_ByCount("ByCount", 1);

        private String name;
        private int index;

        private EnumPricingType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumPricingType pt : EnumPricingType.values()) {
                if (pt.getIndex() == index) {
                    return pt.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}