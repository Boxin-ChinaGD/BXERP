package com.bx.erp.model.commodity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.utils.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Commodity extends BaseModel {
	private static final long serialVersionUID = -3206086047167310460L;
	public static final int MIN_LENGTH_Barcodes = 7;
	public static final int MAX_LENGTH_Barcodes = 64;

	public static final String FIELD_ERROR_priceRetail = "商品的零售价为大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_pricePurchase = "商品的进货价为大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_latestPricePurchase = "商品的最近进货价为大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
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
	public static final String FIELD_ERROR_name = "只允许以*“”、$#/()（）-—_、中英数值、空格形式出现，长度1到32";
	public static final String FIELD_ERROR_specification = "商品规格为中文或英文或数字的组合，中间允许有空格";
	public static final String FIELD_ERROR_packageUnitID = "商品包装单位ID正整数";
	public static final String FIELD_ERROR_brandID = "商品品牌ID必须是大于0的数字";
	public static final String FIELD_ERROR_categoryID = "商品类别ID只能为数字";
	public static final String FIELD_ERROR_priceVIP = "商品的会员价为大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_priceWholesale = "商品的批发价为大于等于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_ruleOfPoint = "商品的积分规则为正整数";
	public static final String FIELD_ERROR_purchaseFlag = "商品的采购阀值为正整数";
	public static final String FIELD_ERROR_NO = "商品的库存为整数";
	public static final String FIELD_ERROR_shelfLife = "商品的保质期为正整数";
	public static final String FIELD_ERROR_returnDays = "商品的退货天数为正整数";
	public static final String FIELD_ERROR_typeOfComposition = "组合商品的类型应该为1";
	public static final String FIELD_ERROR_barcode = "条形码仅允许英文、数值形式出现，长度范围[" + MIN_LENGTH_Barcodes + ", " + MAX_LENGTH_Barcodes + "]";
	public static final String FIELD_ERROR_nOStart = "期初值必须大于0";
	public static final String FIELD_ERROR_purchasingPriceStart = "期初采购价必须大于0，小于等于" + FieldFormat.MAX_OneCommodityPrice;
	public static final String FIELD_ERROR_pricingType = "商品的计价方式只能为0或1";
	public static final String FIELD_ERROR_canChangePrice = "前台改价只能为0或1";
	public static final String FIELD_ERROR_commodityStatus = "商品的状态只能是1或0";
	/**
	 * queryKeyword可以代表搜索的因子为：商品名称、商品简称、商品助记码、商品的条形码。其中商品的条形码最长，所以queryKeyword最长能传Barcodes.MAX_LENGTH_Barcodes位
	 */
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字的长度为1到" + Barcodes.MAX_LENGTH_Barcodes;
	public static final String FIELD_ERROR_includeDeleted = "includeDeleted只能为正整数";
	public static final String FIELD_ERROR_startValueRemark = "期初值备注长度[0, 50]";
	public static final String FIELD_ERROR_startValueRemark_Simple = "只有普通商品才可以修改期初值备注";
	public static final String FIELD_ERROR_propertyValue = "属性值长度[0, 50]";
	public static final String FIELD_ERROR_checkUniqueFieldID = "ID必须大于或等于0";
	public static final String FIELD_ERROR_staff = "店员ID必须大于0";
	public static final String FIELD_ERROR_shopID = "门店ID必须大于0";
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
	public static final String FIELD_ERROR_nOStartOfMultipackaging = "多包装商品的期初值必须为-1";
	public static final String FIELD_ERROR_nOStartOfComposition = "组合商品的期初值必须为-1";
	public static final String FIELD_ERROR_purchasingPriceStartOfService = "服务商品的期初采购价必须为-1";
	public static final String FIELD_ERROR_purchasingPriceStartOfComposition = "组合商品的期初采购价必须为-1";
	public static final String FIELD_ERROR_purchasingPriceStartOfMultipackaging = "多包装商品的期初采购价必须为-1";
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

	public static final int MAX_LENGTH_CommodityName = 32;
	public static final int MIN_LENGTH_CommodityName = 1;
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

	/** 默认期初数量 */
	public static final int NO_START_Default = -1;
	/** 默认期初采购价 */
	public static final double PURCHASING_PRICE_START_Default = -1.000000d;
	/** 默认采购阀值 */
	public static final int PurchaseFlag_START_Default = 1;
	/** 默认积分规则 */
	public static final int RuleOfPoint_START_Default = 1;

	public static final CommodityField field = new CommodityField();

	/** 默认助记码 */
	public static final String Default_MnemonicCode = "XX";

	public static final int IsIncludeDeleted = 1;

	protected int lastOperationToPicture;

	public int getLastOperationToPicture() {
		return lastOperationToPicture;
	}

	public void setLastOperationToPicture(int lastOperationToPicture) {
		this.lastOperationToPicture = lastOperationToPicture;
	}

	protected int status;

	protected String name;
	protected String shortName;
	protected String specification;
	protected int packageUnitID;
	protected String purchasingUnit;
	protected int brandID;
	private int categoryID;
	protected String mnemonicCode;
	protected int pricingType;
	// protected double pricePurchase;
	// protected String FIELD_NAME_pricePurchase;
	//
	// public String field.getFIELD_NAME_pricePurchase() {
	// return "pricePurchase";
	// }

	protected double latestPricePurchase;

	protected double priceRetail;
	protected double priceVIP;

	protected double priceWholesale;
	// protected double ratioGrossMargin;
	protected int canChangePrice;
	protected int ruleOfPoint;
	protected String picture;
	protected int shelfLife;
	protected int returnDays;
	protected Date createDate;
	protected int purchaseFlag;

	protected int refCommodityID;
	protected int refCommodityMultiple;

	protected String propertyValue1;
	protected String propertyValue2;

	protected String propertyValue3;

	protected String propertyValue4;

	protected String tag;
	/** 库存 */
	protected int NO;

	protected int type;

	protected int nOStart;

	protected double purchasingPriceStart;

	protected String startValueRemark;
	protected int currentWarehousingID;

	protected String brandName;

	protected String categoryName;

	/** 非数据表字段。代表一张零售单中单品实际售出的数量。一张零售单中可能包括多包装商品和组合商品 */
	protected int saleNO;
	
	/** 在excel表的行号*/ 
	protected int excelLineID;
	
	/** commodityMapper.retrieveInventory 用到*/
	protected int shopID;
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public int getExcelLineID() {
		return excelLineID;
	}

	public void setExcelLineID(int excelLineID) {
		this.excelLineID = excelLineID;
	}

	public int getSaleNO() {
		return saleNO;
	}

	public void setSaleNO(int saleNOInRetailTrade) {
		this.saleNO = saleNOInRetailTrade;
	}

	/** 非数据表字段 */
	protected String providerIDs;

	public String getProviderIDs() {
		return providerIDs;
	}

	public void setProviderIDs(String providerIDs) {
		this.providerIDs = providerIDs;
	}

	/** 非数据表字段 */
	protected String providerName;

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/** 非数据表字段 */
	protected String baseUnit;

	public String getBaseUnit() {
		return baseUnit;
	}

	public void setBaseUnit(String baseUnit) {
		this.baseUnit = baseUnit;
	}

	/** 非数据表字段 */
	protected String totalPriceStart;

	/** 非数据表字段 */
	protected int includeDeleted;

	/** 非数据表字段 */
	protected int barcodeID;

	/** 组合商品子商品的数量 */
	protected int subCommodityNO;

	public int getSubCommodityNO() {
		return subCommodityNO;
	}

	public void setSubCommodityNO(int subCommodityNO) {
		this.subCommodityNO = subCommodityNO;
	}

	public int getBarcodeID() {
		return barcodeID;
	}

	public void setBarcodeID(int barcodeID) {
		this.barcodeID = barcodeID;
	}

	public String getTotalPriceStart() {
		return totalPriceStart;
	}

	public void setTotalPriceStart(String totalPriceStart) {
		this.totalPriceStart = totalPriceStart;
	}

	public int getIncludeDeleted() {
		return includeDeleted;
	}

	public void setIncludeDeleted(int includeDeleted) {
		this.includeDeleted = includeDeleted;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCurrentWarehousingID() {
		return currentWarehousingID;
	}

	public void setCurrentWarehousingID(int currentWarehousingID) {
		this.currentWarehousingID = currentWarehousingID;
	}

	public String getStartValueRemark() {
		return startValueRemark;
	}

	public void setStartValueRemark(String startValueRemark) {
		this.startValueRemark = startValueRemark;
	}

	public String getPropertyValue1() {
		return propertyValue1;
	}

	public void setPropertyValue1(String propertyValue1) {
		this.propertyValue1 = propertyValue1;
	}

	public String getPropertyValue2() {
		return propertyValue2;
	}

	public void setPropertyValue2(String propertyValue2) {
		this.propertyValue2 = propertyValue2;
	}

	public String getPropertyValue3() {
		return propertyValue3;
	}

	public void setPropertyValue3(String propertyValue3) {
		this.propertyValue3 = propertyValue3;
	}

	public String getPropertyValue4() {
		return propertyValue4;
	}

	public void setPropertyValue4(String propertyValue4) {
		this.propertyValue4 = propertyValue4;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getLatestPricePurchase() {
		return latestPricePurchase;
	}

	public void setLatestPricePurchase(double latestPricePurchase) {
		this.latestPricePurchase = latestPricePurchase;
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

	public int getPackageUnitID() {
		return packageUnitID;
	}

	public void setPackageUnitID(int packageUnitID) {
		this.packageUnitID = packageUnitID;
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

	public void setCreateDate(Date cd) {
		createDate = cd;
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

	public void setTag(String t) {
		tag = t;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	protected String barcodes;

	public String getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(String barcodes) {
		this.barcodes = barcodes;
	}

	/**
	 * 非DB字段。multiPackagingInfo格式：条形码，包装单位，商品倍数，零售价，会员价， 批发价，商品名称 以X,X,X; X,X,X;
	 * X,X,X;
	 */
	protected String multiPackagingInfo;

	public String getMultiPackagingInfo() {
		return multiPackagingInfo;
	}

	public void setMultiPackagingInfo(String multiPackagingInfo) {
		this.multiPackagingInfo = multiPackagingInfo;
	}

	protected String packageUnitName;

	public String getPackageUnitName() {
		return packageUnitName;
	}

	public void setPackageUnitName(String packageUnitName) {
		this.packageUnitName = packageUnitName;
	}

	protected String subCommodityInfo;

	public String getSubCommodityInfo() {
		return subCommodityInfo;
	}

	public void setSubCommodityInfo(String subCommodityInfo) {
		this.subCommodityInfo = subCommodityInfo;
	}

	protected String subCommodityPrice;

	public String getSubCommodityPrice() {
		return subCommodityPrice;
	}

	public void setSubCommodityPrice(String subCommodityPrice) {
		this.subCommodityPrice = subCommodityPrice;
	}

	/** 非DB字段，关于滞销商品消息的字段 */
	protected int messageIsRead;
	protected String messageParameter;
	protected int messageCategoryID;
	protected int companyID;
	protected int messageSenderID;
	protected int messageReceiverID;

	// public static String DEFINE_SET_SubCommodityPrice(String subCommodityPrice) {
	// return subCommodityPrice;
	// }

	public int getMessageIsRead() {
		return messageIsRead;
	}

	public void setMessageIsRead(int messageIsRead) {
		this.messageIsRead = messageIsRead;
	}

	public String getMessageParameter() {
		return messageParameter;
	}

	public void setMessageParameter(String messageParameter) {
		this.messageParameter = messageParameter;
	}

	public int getMessageCategoryID() {
		return messageCategoryID;
	}

	public void setMessageCategoryID(int messageCategoryID) {
		this.messageCategoryID = messageCategoryID;
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public int getMessageSenderID() {
		return messageSenderID;
	}

	public void setMessageSenderID(int messageSenderID) {
		this.messageSenderID = messageSenderID;
	}

	public int getMessageReceiverID() {
		return messageReceiverID;
	}

	public void setMessageReceiverID(int messageReceiverID) {
		this.messageReceiverID = messageReceiverID;
	}

	@Override
	public String toString() {
		return "Commodity [status=" + status + ", name=" + name + ", shortName=" + shortName + ", specification=" + specification + ", packageUnitID=" + packageUnitID + ", purchasingUnit=" + purchasingUnit + ", brandID=" + brandID
				+ ", categoryID=" + categoryID + ", mnemonicCode=" + mnemonicCode + ", pricingType=" + pricingType + ", latestPricePurchase=" + latestPricePurchase + ", priceRetail=" + priceRetail + ", priceVIP=" + priceVIP
				+ ", priceWholesale=" + priceWholesale + ", canChangePrice=" + canChangePrice + ", ruleOfPoint=" + ruleOfPoint + ", picture=" + picture + ", shelfLife=" + shelfLife + ", returnDays=" + returnDays + ", createDate="
				+ createDate + ", purchaseFlag=" + purchaseFlag + ", refCommodityID=" + refCommodityID + ", refCommodityMultiple=" + refCommodityMultiple + ", propertyValue1=" + propertyValue1 + ", propertyValue2=" + propertyValue2
				+ ", propertyValue3=" + propertyValue3 + ", propertyValue4=" + propertyValue4 + ", tag=" + tag + ", NO=" + NO + ", type=" + type + ", nOStart=" + nOStart + ", purchasingPriceStart=" + purchasingPriceStart
				+ ", startValueRemark=" + startValueRemark + ", currentWarehousingID=" + currentWarehousingID + ", ID=" + ID + ", brandName=" + brandName + ", categoryName=" + categoryName + ", syncSequence=" + syncSequence + "]";
	}

	public String toPromotionString(double price, double priceSpecialOffer, int NO) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		sb.append("<tr><th>名称</th><th>简称</th><th>规格</th><th>包装单位</th><th>品牌</th><th>类别</th><th>助记码</th><th>计价方式</th><th>零售价</th><th> 会员价</th><th>保质期</th><th>退货天数</th><th>单价</th><th>特价</th><th>数量</th></tr>");
		sb.append("<tr><td>").append(name);
		sb.append("</td><td>").append(shortName);
		sb.append("</td><td>").append(specification);
		sb.append("</td><td>").append(packageUnitID);
		sb.append("</td><td>").append(brandID);
		sb.append("</td><td>").append(categoryID);
		sb.append("</td><td>").append(mnemonicCode);
		sb.append("</td><td>").append(pricingType);
		sb.append("</td><td>").append(priceRetail);
		sb.append("</td><td>").append(priceVIP);
		sb.append("</td><td>").append(shelfLife);
		sb.append("</td><td>").append(returnDays);
		sb.append("</td><td>").append(price);
		sb.append("</td><td>").append(priceSpecialOffer);
		sb.append("</td><td>").append(NO);
		sb.append("</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		Commodity comm = (Commodity) arg0;
		// 组合商品时为RuleOfPoint = 0, ShelfLife = 0, PurchaseFlag = 0;
		if (type == 1) {
			this.setRuleOfPoint(0);
			this.setShelfLife(0);
			this.setPurchaseFlag(0);
		}
		if ((ignoreIDInComparision == true ? true : (comm.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& comm.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
				&& comm.getName().equals(name) && printComparator(field.getFIELD_NAME_name()) //
				&& comm.getShortName().equals(shortName) && printComparator(field.getFIELD_NAME_shortName()) //
				&& comm.getSpecification().equals(specification) && printComparator(field.getFIELD_NAME_specification()) //
				&& comm.getPackageUnitID() == packageUnitID && printComparator(field.getFIELD_NAME_packageUnitID()) //
				&& comm.getPurchasingUnit().equals(purchasingUnit) && printComparator(field.getFIELD_NAME_purchasingUnit()) //
				&& comm.getBrandID() == brandID && printComparator(field.getFIELD_NAME_brandID()) //
				&& comm.getCategoryID() == categoryID && printComparator(field.getFIELD_NAME_categoryID()) //
				&& comm.getMnemonicCode().equals(mnemonicCode) && printComparator(field.getFIELD_NAME_mnemonicCode()) //
				&& comm.getPricingType() == pricingType && printComparator(field.getFIELD_NAME_pricingType()) //
				// && Math.abs(comm.getPricePurchase() - this.getPricePurchase()) < TOLERANCE &&
				// printComparator("PricePurchase") //
				// && Math.abs(comm.getPriceRetail() - this.getPriceRetail()) < TOLERANCE &&
				// printComparator("PriceRetail") //
				// && Math.abs(comm.getLatestPricePurchase() - this.getLatestPricePurchase()) <
				// TOLERANCE && printComparator("LatestPricePurchase") //
//				&& Math.abs(GeneralUtil.sub(comm.getLatestPricePurchase(), latestPricePurchase)) < TOLERANCE && printComparator(field.getFIELD_NAME_latestPricePurchase()) //
//				&& Math.abs(GeneralUtil.sub(comm.getPriceRetail(), priceRetail)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceRetail()) //
				&& Math.abs(GeneralUtil.sub(comm.getPriceVIP(), priceVIP)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceVIP()) //
				&& Math.abs(GeneralUtil.sub(comm.getPriceWholesale(), priceWholesale)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceWholesale()) //
				&& comm.getCanChangePrice() == canChangePrice && printComparator(field.getFIELD_NAME_canChangePrice()) //
				&& comm.getRuleOfPoint() == ruleOfPoint && printComparator(field.getFIELD_NAME_ruleOfPoint()) //
				// && comm.getPicture().equals(picture) &&
				// printComparator(field.getFIELD_NAME_picture()) //
				&& comm.getShelfLife() == shelfLife && printComparator(field.getFIELD_NAME_shelfLife()) //
				&& comm.getReturnDays() == returnDays && printComparator(field.getFIELD_NAME_returnDays()) //
				// && comm.getCreateDate().equals(this.getCreateDate()) &&
				// printComparator("CreateDate") //
				&& comm.getPurchaseFlag() == purchaseFlag && printComparator(field.getFIELD_NAME_purchaseFlag()) //
				&& comm.getRefCommodityID() == refCommodityID && printComparator(field.getFIELD_NAME_refCommodityID()) //
				&& comm.getRefCommodityMultiple() == refCommodityMultiple && printComparator(field.getFIELD_NAME_refCommodityMultiple()) // ...
				&& comm.getTag().equals(tag) && printComparator(field.getFIELD_NAME_tag()) //
				// TODO 放到从表listSlave2比较
//				&& comm.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
				// && comm.getNOAccumulated() == this.getNOAccumulated() &&
				// printComparator(field.getFIELD_NAME_NOAccumulated()) //
				&& comm.getType() == type && printComparator(field.getFIELD_NAME_type()) //
//				&& comm.getnOStart() == nOStart && printComparator(field.getFIELD_NAME_nOStart()) //
//				&& Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), purchasingPriceStart)) < TOLERANCE && printComparator(field.getFIELD_NAME_purchasingPriceStart())//
				&& (comm.getStartValueRemark() == null ? "" : comm.getStartValueRemark()).equals(startValueRemark == null ? "" : startValueRemark) && printComparator(field.getFIELD_NAME_startValueRemark()) //
		) {
			if (!ignoreSlaveListInComparision) {
				if (comm.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
					if (listSlave1 == null && comm.getListSlave1() == null) {
						return 0;
					}
					if (listSlave1 == null && comm.getListSlave1() != null) {
						if (comm.getListSlave1().size() == 0) {
							return 0;
						}
						return -1;
					}
					if (listSlave1 != null && comm.getListSlave1() == null) {
						if (listSlave1.size() == 0) {
							return 0;
						}
						return -1;
					}

					if (listSlave1 != null && comm.getListSlave1() != null) {
						if (listSlave1.size() != comm.getListSlave1().size()) {
							return -1;
						}
						for (int i = 0; i < listSlave1.size(); i++) {
							SubCommodity subCommodity = (SubCommodity) listSlave1.get(i);
							subCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
							Boolean exist = false;
							for (int j = 0; j < comm.getListSlave1().size(); j++) {
								SubCommodity sc = (SubCommodity) comm.getListSlave1().get(j);
								if (subCommodity.getSubCommodityID() == sc.getSubCommodityID()) {
									exist = true;
									if (subCommodity.compareTo(sc) != 0) {
										return -1;
									}
									break;
								}
							}
							if (!exist) {
								return -1;
							}
						}
					}
				}
				// listSlave2：商品门店信息
				if (listSlave2 == null && comm.getListSlave2() == null) {
					return 0;
				}
				if (listSlave2 == null && comm.getListSlave2() != null) {
					if (comm.getListSlave2().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave2 != null && comm.getListSlave2() == null) {
					if (listSlave2.size() == 0) {
						return 0;
					}
					return -1;
				}

				if (listSlave2 != null && comm.getListSlave2() != null) {
					if (listSlave2.size() != comm.getListSlave2().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave2.size(); i++) {
						CommodityShopInfo commodityShopInfo = (CommodityShopInfo) listSlave2.get(i);
						commodityShopInfo.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						for(int j = 0; j < comm.getListSlave2().size(); j++) {
							CommodityShopInfo commodityShopInfoSlave2 = (CommodityShopInfo) comm.getListSlave2().get(j);
							if(commodityShopInfo.getCommodityID() == commodityShopInfoSlave2.getCommodityID() && commodityShopInfo.getShopID() == commodityShopInfoSlave2.getShopID()) {
								if(commodityShopInfo.compareTo(commodityShopInfoSlave2) != 0) {
									return -1;
								}
							}
						}
					}
				}
			}
			return 0;
		}
		return -1;
	}

	public Commodity() {
		super();

		brandID = BaseAction.INVALID_ID;
		categoryID = BaseAction.INVALID_ID;
		NO = BaseAction.INVALID_NO;

	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Commodity obj = new Commodity();
		obj.setID(ID);
		if (createDate != null) {
			obj.setCreateDate((Date) createDate.clone());
		}
		obj.setStatus(status);
		obj.setName(name);
		obj.setShortName(shortName);
		obj.setSpecification(specification);
		obj.setPackageUnitID(packageUnitID);
		obj.setPurchasingUnit(purchasingUnit);
		obj.setBrandID(brandID);
		obj.setCategoryID(categoryID);
		obj.setMnemonicCode(mnemonicCode);
		obj.setPricingType(pricingType);
		// obj.setPricePurchase(pricePurchase);
		obj.setLatestPricePurchase(latestPricePurchase);
		obj.setPriceRetail(priceRetail);
		obj.setPriceVIP(priceVIP);
		obj.setPriceWholesale(priceWholesale);
		// obj.setRatioGrossMargin(ratioGrossMargin);
		obj.setCanChangePrice(canChangePrice);
		obj.setRuleOfPoint(ruleOfPoint);
		obj.setPicture(picture);
		obj.setShelfLife(shelfLife);
		obj.setReturnDays(returnDays);
		obj.setPurchaseFlag(purchaseFlag);
		obj.setRefCommodityID(refCommodityID);
		obj.setRefCommodityMultiple(refCommodityMultiple);
		obj.setTag(new String(tag));
		obj.setNO(NO);
		// obj.setNOAccumulated(NOAccumulated);
		obj.setType(type);
		obj.setnOStart(nOStart);
		obj.setPurchasingPriceStart(purchasingPriceStart);
		obj.setStartValueRemark(startValueRemark);
		obj.setReturnObject(returnObject);
		obj.setOperatorStaffID(operatorStaffID);
		obj.setMultiPackagingInfo(multiPackagingInfo);
		obj.setPropertyValue1(propertyValue1);
		obj.setPropertyValue2(propertyValue2);
		obj.setPropertyValue3(propertyValue3);
		obj.setPropertyValue4(propertyValue4);
		obj.setBrandName(brandName);
		obj.setCategoryName(categoryName);
		obj.setProviderIDs(providerIDs);
		obj.setCurrentWarehousingID(currentWarehousingID);
		obj.setSubCommodityInfo(subCommodityInfo);
		obj.setShopID(shopID);
		if (listSlave1 != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : listSlave1) {
				SubCommodity subCommodity = (SubCommodity) o;
				list.add(subCommodity.clone());
			}
			obj.setListSlave1(list);
		}
		obj.setBarcodes(barcodes);
		obj.setBarcodeID(barcodeID);
		// listSlave2：商品门店信息
		if(listSlave2 != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : listSlave2) {
				CommodityShopInfo commodityShopInfo = (CommodityShopInfo) o;
				list.add(commodityShopInfo.clone());
			}
			obj.setListSlave2(list);
		}
		return obj;
	}

	@Override
	public Map<String, Object> getCreateParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Commodity comm = (Commodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), comm.getID());
		params.put(field.getFIELD_NAME_status(), comm.getStatus());
		params.put(field.getFIELD_NAME_name(), comm.getName() == null ? "" : comm.getName());
		params.put(field.getFIELD_NAME_shortName(), comm.getShortName() == null ? "" : comm.getShortName());
		params.put(field.getFIELD_NAME_specification(), comm.getSpecification() == null ? "" : comm.getSpecification());
		params.put(field.getFIELD_NAME_packageUnitID(), comm.getPackageUnitID());
		params.put(field.getFIELD_NAME_purchasingUnit(), comm.getPurchasingUnit() == null ? "" : comm.getPurchasingUnit());
		params.put(field.getFIELD_NAME_brandID(), comm.getBrandID());
		params.put(field.getFIELD_NAME_categoryID(), comm.getCategoryID());
		params.put(field.getFIELD_NAME_mnemonicCode(), comm.getMnemonicCode() == null ? "" : comm.getMnemonicCode());
		params.put(field.getFIELD_NAME_pricingType(), comm.getPricingType());
//		params.put(field.getFIELD_NAME_latestPricePurchase(), comm.getLatestPricePurchase());
//		params.put(field.getFIELD_NAME_priceRetail(), comm.getPriceRetail());
		params.put(field.getFIELD_NAME_priceVIP(), comm.getPriceVIP());
		params.put(field.getFIELD_NAME_priceWholesale(), comm.getPriceWholesale());
		params.put(field.getFIELD_NAME_canChangePrice(), comm.getCanChangePrice());
		params.put(field.getFIELD_NAME_ruleOfPoint(), comm.getRuleOfPoint());
		params.put(field.getFIELD_NAME_picture(), comm.getPicture() == null ? "" : comm.getPicture());
		params.put(field.getFIELD_NAME_shelfLife(), comm.getShelfLife());
		params.put(field.getFIELD_NAME_returnDays(), comm.getReturnDays());
		params.put(field.getFIELD_NAME_purchaseFlag(), comm.getPurchaseFlag());
		params.put(field.getFIELD_NAME_refCommodityID(), comm.getRefCommodityID());
		params.put(field.getFIELD_NAME_refCommodityMultiple(), comm.getRefCommodityMultiple());
		params.put(field.getFIELD_NAME_tag(), comm.getTag() == null ? "" : comm.getTag());
		params.put(field.getFIELD_NAME_type(), comm.getType());
		params.put(field.getFIELD_NAME_nOStart(), comm.getnOStart());
		params.put(field.getFIELD_NAME_purchasingPriceStart(), comm.getPurchasingPriceStart());
		params.put(field.getFIELD_NAME_startValueRemark(), comm.getStartValueRemark());
		params.put(field.getFIELD_NAME_operatorStaffID(), comm.getOperatorStaffID());
		params.put(field.getFIELD_NAME_barcodes(), comm.getBarcodes());
		params.put(field.getFIELD_NAME_propertyValue1(), comm.getPropertyValue1() == null ? "" : comm.getPropertyValue1());
		params.put(field.getFIELD_NAME_propertyValue2(), comm.getPropertyValue2() == null ? "" : comm.getPropertyValue2());
		params.put(field.getFIELD_NAME_propertyValue3(), comm.getPropertyValue3() == null ? "" : comm.getPropertyValue3());
		params.put(field.getFIELD_NAME_propertyValue4(), comm.getPropertyValue4() == null ? "" : comm.getPropertyValue4());
		params.put(field.getFIELD_NAME_syncSequence(), comm.getSyncSequence());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Commodity comm = (Commodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_RetrieveNMultiPackageCommodity:
			params.put(field.getFIELD_NAME_ID(), comm.getID());
			params.put(field.getFIELD_NAME_iPageIndex(), comm.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), comm.getPageSize());

			break;
		case BaseBO.CASE_CheckDeleteDependency:
			params.put(field.getFIELD_NAME_ID(), comm.getID());

			break;
		case BaseBO.CASE_CheckUniqueField:
			params.put(field.getFIELD_NAME_ID(), comm.getID());
			params.put(field.getFIELD_NAME_fieldToCheckUnique(), comm.getFieldToCheckUnique());
			params.put(field.getFIELD_NAME_uniqueField(), comm.getUniqueField());

			break;
		default:
			params.put(field.getFIELD_NAME_status(), comm.getStatus());
			params.put(field.getFIELD_NAME_NO(), comm.getNO() == 0 ? BaseAction.INVALID_ID : comm.getNO());
			params.put(field.getFIELD_NAME_categoryID(), comm.getCategoryID() == 0 ? BaseAction.INVALID_ID : comm.getCategoryID());
			params.put(field.getFIELD_NAME_brandID(), comm.getBrandID() == 0 ? BaseAction.INVALID_ID : comm.getBrandID());
			params.put(field.getFIELD_NAME_queryKeyword(), comm.getQueryKeyword() == null ? "" : comm.getQueryKeyword());
			params.put(field.getFIELD_NAME_type(), comm.getType());
			params.put(field.getFIELD_NAME_date1(), comm.getDate1() == null ? "1970/01/01 00:00:00" : comm.getDate1());
			params.put(field.getFIELD_NAME_date2(), comm.getDate2() == null ? new Date() : comm.getDate2());
			params.put(field.getFIELD_NAME_iPageIndex(), comm.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), comm.getPageSize());

			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		Commodity comm = (Commodity) bm;
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_RetrieveInventory:
			params.put(field.getFIELD_NAME_ID(), comm.getID());
			params.put(field.getFIELD_NAME_shopID(), comm.getShopID());
			break;
		default:
			throw new RuntimeException("未定义的CASE！");
		}
		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		Commodity comm = (Commodity) bm;
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_ID(), comm.getID());
			// 我们测试时，添加一个includeDeleted字段，includeDeleted = 1时可以查询一个删除状态的商品
			params.put(field.getFIELD_NAME_includeDeleted(), comm.getIncludeDeleted());
		}
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Map<String, Object> params = new HashMap<String, Object>();
		Commodity comm = (Commodity) bm;
		params.put(field.getFIELD_NAME_ID(), comm.getID());

		switch (iUseCaseID) {
		case BaseBO.CASE_UpdateCommodityPicture:
			params.put(field.getFIELD_NAME_picture(), comm.getPicture());
			params.put(field.getFIELD_NAME_updateDatetime(), new Date());

			break;
		default:
			throw new RuntimeException("未定义的CASE！");
		}

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Map<String, Object> params = new HashMap<String, Object>();
		Commodity comm = (Commodity) bm;
		params.put(field.getFIELD_NAME_ID(), comm.getID());

		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_UpdatePrice:
			params.put(field.getFIELD_NAME_operatorStaffID(), comm.getOperatorStaffID());
			params.put(field.getFIELD_NAME_priceRetail(), comm.getPriceRetail());
			// params.put(field.getFIELD_NAME_pricePurchase(), comm.getPricePurchase());
			params.put(field.getFIELD_NAME_latestPricePurchase(), comm.getLatestPricePurchase());
			params.put(field.getFIELD_NAME_shopID(), comm.getShopID());

			break;
		case BaseBO.CASE_UpdatePurchasingUnit:
			params.put(field.getFIELD_NAME_purchasingUnit(), comm.getPurchasingUnit() == null ? "" : comm.getPurchasingUnit());

			break;

		case BaseBO.CASE_UpdateCommodityPicture:
			params.put(field.getFIELD_NAME_picture(), comm.getPicture());

			break;
		default:
			params.put(field.getFIELD_NAME_name(), comm.getName() == null ? "" : comm.getName());
			params.put(field.getFIELD_NAME_shortName(), comm.getShortName() == null ? "" : comm.getShortName());
			params.put(field.getFIELD_NAME_specification(), comm.getSpecification() == null ? "" : comm.getSpecification());
			params.put(field.getFIELD_NAME_packageUnitID(), comm.getPackageUnitID());
			params.put(field.getFIELD_NAME_brandID(), comm.getBrandID());
			params.put(field.getFIELD_NAME_categoryID(), comm.getCategoryID());
			params.put(field.getFIELD_NAME_mnemonicCode(), comm.getMnemonicCode() == null ? "" : comm.getMnemonicCode());
			params.put(field.getFIELD_NAME_pricingType(), comm.getPricingType());
			params.put(field.getFIELD_NAME_priceVIP(), comm.getPriceVIP());
			params.put(field.getFIELD_NAME_priceWholesale(), comm.getPriceWholesale());
			// params.put(field.getFIELD_NAME_ratioGrossMargin(),
			// comm.getRatioGrossMargin());
			params.put(field.getFIELD_NAME_canChangePrice(), comm.getCanChangePrice());
			params.put(field.getFIELD_NAME_ruleOfPoint(), comm.getRuleOfPoint());
			params.put(field.getFIELD_NAME_picture(), comm.getPicture() == null ? "" : comm.getPicture());
			params.put(field.getFIELD_NAME_shelfLife(), comm.getShelfLife());
			params.put(field.getFIELD_NAME_returnDays(), comm.getReturnDays());
			params.put(field.getFIELD_NAME_purchaseFlag(), comm.getPurchaseFlag());
			// params.put(field.getFIELD_NAME_refCommodityID(), comm.getRefCommodityID());
			params.put(field.getFIELD_NAME_refCommodityMultiple(), comm.getRefCommodityMultiple());
			params.put(field.getFIELD_NAME_tag(), comm.getTag() == null ? "" : comm.getTag());
			// params.put(field.getFIELD_NAME_type(), comm.getType());
			params.put(field.getFIELD_NAME_operatorStaffID(), comm.getOperatorStaffID());
			params.put(field.getFIELD_NAME_updateDatetime(), comm.getUpdateDatetime());
			params.put(field.getFIELD_NAME_startValueRemark(), comm.getStartValueRemark() == null ? "" : comm.getStartValueRemark());
			params.put(field.getFIELD_NAME_propertyValue1(), comm.getPropertyValue1() == null ? "" : comm.getPropertyValue1());
			params.put(field.getFIELD_NAME_propertyValue2(), comm.getPropertyValue2() == null ? "" : comm.getPropertyValue2());
			params.put(field.getFIELD_NAME_propertyValue3(), comm.getPropertyValue3() == null ? "" : comm.getPropertyValue3());
			params.put(field.getFIELD_NAME_propertyValue4(), comm.getPropertyValue4() == null ? "" : comm.getPropertyValue4());
			params.put(field.getFIELD_NAME_shopID(), comm.getShopID());

			break;
		}

		return params;

	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Map<String, Object> params = new HashMap<String, Object>();
		Commodity comm = (Commodity) bm;

		params.put(field.getFIELD_NAME_ID(), comm.getID());
		params.put(field.getFIELD_NAME_operatorStaffID(), comm.getOperatorStaffID());

		return params;

	}

	@Override
	public boolean setDefaultValueToCreate(int iUseCaseID) {
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			this.setStatus(DEFAULT_VALUE_CommodityStatus);
			this.setLatestPricePurchase(DEFAULT_VALUE_LatestPricePurchase);
			// this.setRatioGrossMargin(DEFAULT_VALUE_RatioGrossMargin);
			this.setNO(DEFAULT_VALUE_CommodityNO);
			this.setnOStart(NO_START_Default);
			this.setPurchasingPriceStart(PURCHASING_PRICE_START_Default);
			// this.setNOAccumulated(DEFAULT_VALUE_CommodityAccumulated);
			break;
		case BaseBO.CASE_Commodity_CreateService:
			this.setPurchaseFlag(DEFAULT_VALUE_PurchaseFlag);
			this.setnOStart(NO_START_Default);
			this.setPurchasingPriceStart(PURCHASING_PRICE_START_Default);
		default:
			this.setStatus(DEFAULT_VALUE_CommodityStatus);
			this.setLatestPricePurchase(DEFAULT_VALUE_LatestPricePurchase);
			// this.setRatioGrossMargin(DEFAULT_VALUE_RatioGrossMargin);
			this.setRefCommodityID(DEFAULT_VALUE_RefCommodityID);
			this.setRefCommodityMultiple(DEFAULT_VALUE_RefCommodityMultiple);
			this.setNO(DEFAULT_VALUE_CommodityNO);
			// this.setNOAccumulated(DEFAULT_VALUE_CommodityAccumulated);
			break;
		}
		return true;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

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
				&& printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && (FieldFormat.checkCommodityName(name)) //
				//
				&& printCheckField(field.getFIELD_NAME_specification(), FIELD_ERROR_specification, sbError)
				&& (FieldFormat.checkSpecification(specification) && specification.length() >= MIN_LENGTH_Specification && specification.length() <= MAX_LENGTH_Specification) //
				//
				&& printCheckField(field.getFIELD_NAME_packageUnitID(), FIELD_ERROR_packageUnitID, sbError) && (FieldFormat.checkPackageUnitID(String.valueOf(packageUnitID))) //
				&& printCheckField(field.getFIELD_NAME_brandID(), FIELD_ERROR_brandID, sbError) && FieldFormat.checkBrandID(String.valueOf(brandID))//
				&& printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && FieldFormat.checkCategoryID(String.valueOf(categoryID)) //
				&& printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && FieldFormat.checkCommodityPrice(priceRetail) //
				&& printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && FieldFormat.checkCommodityPrice(priceVIP) //
				&& printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && FieldFormat.checkCommodityPrice(priceWholesale) //
				// && printCheckField(field.getFIELD_NAME_ruleOfPoint(),
				// FIELD_ERROR_ruleOfPoint, sbError) &&
				// FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint)) //
				// && printCheckField(field.getFIELD_NAME_purchaseFlag(),
				// FIELD_ERROR_purchaseFlag, sbError) &&
				// FieldFormat.checkPurchaseFlag(String.valueOf(purchaseFlag))//
				&& printCheckField(field.getFIELD_NAME_returnDays(), FIELD_ERROR_returnDays, sbError) && FieldFormat.checkReturnDays(String.valueOf(returnDays)) //
				&& printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_commodityStatus, sbError) && (status == EnumStatusCommodity.ESC_Normal.index || status == EnumStatusCommodity.ESC_ToEliminated.index)) {
		} else {
			return sbError.toString();
		}

		switch (iUseCaseID) {
		case BaseBO.CASE_UpdateCommodityOfMultiPackaging:
			if (printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && FieldFormat.checkCommodityPrice(priceVIP) //
					&& printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && FieldFormat.checkCommodityPrice(priceWholesale) //
					&& printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeOfMultipackaging, sbError) && FieldFormat.checkIfMultiPackagingType(type) //
					&& printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodityOfMultiPackaging, sbError) //
					&& FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple) //
					&& printCheckField(field.getFIELD_NAME_barcodes(), FIELD_ERROR_barcode, sbError) && FieldFormat.checkBarcodeInMultiPackagingInfo(multiPackagingInfo) //
					&& this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NOofMultiPackaging, sbError) && FieldFormat.checkIfMultiPackagingNO(NO)) {
				return "";
			}
			return sbError.toString();
		case BaseBO.CASE_Commodity_UpdatePrice:
			if ((printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && FieldFormat.checkCommodityPrice(priceRetail)) //
					// && (this.printCheckField(field.getFIELD_NAME_pricePurchase(),
					// FIELD_ERROR_pricePurchase, sbError) &&
					// FieldFormat.checkPricePurchase(String.valueOf(pricePurchase))) //
					&& printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && FieldFormat.checkID(operatorStaffID) //
					&& (printCheckField(field.getFIELD_NAME_latestPricePurchase(), FIELD_ERROR_latestPricePurchase, sbError) //
					&& (Math.abs(GeneralUtil.sub(Math.abs(latestPricePurchase), Math.abs(DEFAULT_VALUE_LatestPricePurchase))) < TOLERANCE || FieldFormat.checkCommodityPrice(latestPricePurchase)) //
					&& printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && FieldFormat.checkID(shopID) //
				) //
			) {
				return "";
			}
			return sbError.toString();

		case BaseBO.CASE_UpdatePurchasingUnit:
			if (printCheckField(field.getFIELD_NAME_purchasingUnit(), FIELD_ERROR_purchasingUnit, sbError)//
					&& (FieldFormat.checkPurchasingUnit(purchasingUnit) && purchasingUnit.length() >= MIN_LENGTH_PurchasingUnit && purchasingUnit.length() <= MAX_LENGTH_PurchasingUnit) //
			) {
				return "";
			}
			return sbError.toString();
		case BaseBO.CASE_UpdateCommodityOfService:
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

			if (printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(operatorStaffID)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_latestPricePurchase(), FIELD_ERROR_latestPricePurchaseOfService, sbError) && latestPricePurchase != DEFAULT_VALUE_LatestPricePurchase) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_startValueRemark(), FIELD_ERROR_startValueRemark_Simple, sbError) && !StringUtils.isEmpty(startValueRemark)) {
				return sbError.toString();
			}

			// 服务型商品创建时，只传1个条形码。修改时亦然。
			if (printCheckField(field.getFIELD_NAME_barcodes(), FIELD_ERROR_barcode, sbError) && !FieldFormat.checkBarcode(barcodes)) {
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

			if (printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(operatorStaffID)) {
				return sbError.toString();
			}

			if (!StringUtils.isEmpty(startValueRemark)) {
				if (printCheckField(field.getFIELD_NAME_startValueRemark(), FIELD_ERROR_startValueRemark, sbError) && startValueRemark.length() > MAX_LENGTH_StartValueRemark) {
					return sbError.toString();
				}
			}
		}
		return "";

	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		// status = EnumStatusCommodity.ESC_Normal.getIndex(); // 防止黑客创建奇怪状态的商品
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

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCommodityName(name))) {//
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
		if (printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && !FieldFormat.checkCommodityPrice(priceRetail)) {//
			return sbError.toString();
		}

//		if (printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && !FieldFormat.checkCommodityPrice(priceVIP)) {//
//			return sbError.toString();
//		}
//
//		if (printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && !FieldFormat.checkCommodityPrice(priceWholesale)) {//
//			return sbError.toString();
//		}

		if (printCheckField(field.getFIELD_NAME_barcodes(), FIELD_ERROR_barcode, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(String.valueOf(barcodes))) {// barcodes存储条形码信息
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

		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateComposition:
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

			if (printCheckField(field.getFIELD_NAME_nOStart(), FIELD_ERROR_nOStartOfComposition, sbError) && nOStart != NO_START_Default) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), FIELD_ERROR_purchasingPriceStartOfComposition, sbError) && purchasingPriceStart != PURCHASING_PRICE_START_Default) {
				return sbError.toString();
			}

			return "";
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeOfMultipackaging, sbError) && FieldFormat.checkIfMultiPackagingType(type) //
					&& printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_statusOfMultipackaging, sbError) && FieldFormat.checkIfMultiPackagingStatus(status) //
					&& printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodityOfMultiPackaging, sbError) //
					&& FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple) //
					&& this.printCheckField(field.getFIELD_NAME_barcodes(), FIELD_ERROR_barcode, sbError) && FieldFormat.checkBarcodeInMultiPackagingInfo(multiPackagingInfo) //
					// && printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife,
					// sbError) && FieldFormat.checkShelfLife(String.valueOf(shelfLife)) //
					&& printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NOofMultiPackaging, sbError) && FieldFormat.checkIfMultiPackagingNO(NO)//
					&& printCheckField(field.getFIELD_NAME_nOStart(), FIELD_ERROR_nOStartOfMultipackaging, sbError) && nOStart == NO_START_Default //
					&& printCheckField(field.getFIELD_NAME_purchasingPriceStart(), FIELD_ERROR_purchasingPriceStartOfMultipackaging, sbError) && purchasingPriceStart == PURCHASING_PRICE_START_Default) {
				return "";
			}
			return sbError.toString();
		case BaseBO.CASE_Commodity_CreateService:
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
		default: // case BaseBO.CASE_Commodity_CreateSingle:
			if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_type, sbError) && !FieldFormat.checkIfCommodityType(type)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !FieldFormat.checkIfCommodityStatus(status)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife, sbError) && !FieldFormat.checkShelfLife(String.valueOf(shelfLife))) { // ?？
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
			// ... 只有普通商品才能够设置为期初商品。
			if (nOStart != -1 || purchasingPriceStart != -1D) { // 期初数量和期初采购价或者同时为-1，或者数量>0、期初采购价属于(0, 10000]
				if (printCheckField(field.getFIELD_NAME_nOStart(), FIELD_ERROR_nOStart, sbError) && !FieldFormat.checkNoStart(nOStart)) {
					return sbError.toString();
				}
				if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), FIELD_ERROR_purchasingPriceStart, sbError) && (purchasingPriceStart <= 0 || purchasingPriceStart > FieldFormat.MAX_OneCommodityPrice)) {
					return sbError.toString();
				}
			}

			ruleOfPoint = RuleOfPoint_START_Default;// 暂时把积分规则字段写死为1
			if (printCheckField(field.getFIELD_NAME_ruleOfPoint(), FIELD_ERROR_ruleOfPoint, sbError) && !FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint))) { // 积分规则允许为0的情况
				return sbError.toString();
			}

			if (!StringUtils.isEmpty(startValueRemark)) {
				if (printCheckField(field.getFIELD_NAME_startValueRemark(), FIELD_ERROR_startValueRemark, sbError) && startValueRemark.length() > MAX_LENGTH_StartValueRemark) {
					return sbError.toString();
				}
			}
		}
		return "";

	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(operatorStaffID)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_CheckUniqueField:
			if (printCheckField(field.getFIELD_NAME_ID(), FIELD_ERROR_checkUniqueFieldID, sbError) && ID < 0) { // 这个CASE允许ID=0的情况
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_fieldToCheckUnique(), FIELD_ERROR_checkUniqueField, sbError) && fieldToCheckUnique != 1) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_uniqueField(), FIELD_ERROR_checkUniqueField, sbError) && !FieldFormat.checkCommodityName(uniqueField)) {
				return sbError.toString();
			}

			return "";
		case BaseBO.CASE_RetrieveNMultiPackageCommodity:
			if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}

			return "";
		default:
			if (!StringUtils.isEmpty(queryKeyword)) {
				// queryKeyword可以代表搜索的因子为：商品名称、商品简称、商品助记码、商品的条形码。其中商品的条形码最长，所以queryKeyword最长能传Barcodes.MAX_LENGTH_Barcodes位
				if ((printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && queryKeyword.length() > Barcodes.MAX_LENGTH_Barcodes)) {
					return sbError.toString();
				}
			}

			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_commodityStatus, sbError) && status != BaseAction.INVALID_STATUS //
					&& !FieldFormat.checkIfMultiPackagingStatus(status)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NO, sbError) && NO != BaseAction.INVALID_NO && !FieldFormat.checkNO(String.valueOf(NO))) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_brandID(), FIELD_ERROR_brandID, sbError) && brandID != BaseAction.INVALID_ID //
					&& !FieldFormat.checkBrandID(String.valueOf(brandID))) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && categoryID != BaseAction.INVALID_ID //
					&& !FieldFormat.checkCategoryID(String.valueOf(categoryID))) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_commodityType, sbError) && type != BaseAction.INVALID_Type && //
					!(type == EnumCommodityType.ECT_Normal.getIndex() || type == EnumCommodityType.ECT_Combination.getIndex() || type == EnumCommodityType.ECT_MultiPackaging.getIndex() || type == EnumCommodityType.ECT_Service.getIndex())) {
				return sbError.toString();
			}

			return "";
		}

	}

	@Override
	protected BaseModel doParse1(com.alibaba.fastjson.JSONObject jo) {
		try {
			ID = jo.getInteger(field.getFIELD_NAME_ID());
			status = jo.getInteger(field.getFIELD_NAME_status());
			name = jo.getString(field.getFIELD_NAME_name());
			shortName = jo.getString(field.getFIELD_NAME_shortName());
			specification = jo.getString(field.getFIELD_NAME_specification());
			packageUnitID = Integer.valueOf(jo.getString(field.getFIELD_NAME_packageUnitID()));
			purchasingUnit = jo.getString(field.getFIELD_NAME_purchasingUnit());
			brandID = Integer.valueOf(jo.getString(field.getFIELD_NAME_brandID()));
			categoryID = Integer.valueOf(jo.getString(field.getFIELD_NAME_categoryID()));
			mnemonicCode = jo.getString(field.getFIELD_NAME_mnemonicCode());
			pricingType = Integer.valueOf(jo.getString(field.getFIELD_NAME_pricingType()));
			// pricePurchase =
			// Float.valueOf(jo.getString(field.getFIELD_NAME_pricePurchase()));
			latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
			priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
			priceVIP = jo.getDouble(field.getFIELD_NAME_priceVIP());
			priceWholesale = jo.getDouble(field.getFIELD_NAME_priceWholesale());
			// ratioGrossMargin =
			// Float.valueOf(jo.getString(field.getFIELD_NAME_ratioGrossMargin()));
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
			// NOAccumulated = jo.getInt(field.getFIELD_NAME_NOAccumulated());
			type = jo.getInteger(field.getFIELD_NAME_type());
			nOStart = jo.getInteger(field.getFIELD_NAME_nOStart());
			purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
			operatorStaffID = jo.getInteger(field.getFIELD_NAME_operatorStaffID());
			syncType = jo.getString(field.getFIELD_NAME_syncType());
			subCommodityInfo = jo.getString(field.getFIELD_NAME_subCommodityInfo());
			packageUnitName = jo.getString(field.getFIELD_NAME_packageUnitName());
			startValueRemark = jo.getString(field.getFIELD_NAME_startValueRemark());

			String tmpCreateDate = jo.getString(field.getFIELD_NAME_createDate());
			if (!StringUtils.isEmpty(tmpCreateDate)) {
				createDate = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDate);
				if (createDate == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpCreateDate);
				}
			}

			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!StringUtils.isEmpty(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}

			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			// 查询商品对应的条码集合
			barcodes = jo.getString(field.getFIELD_NAME_barcodes());
			// 查询子商品集合
			List<SubCommodity> listCommSlave = new ArrayList<SubCommodity>();
			com.alibaba.fastjson.JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				com.alibaba.fastjson.JSONObject object = jr.getJSONObject(i);
				SubCommodity subComm = (SubCommodity) new SubCommodity().parse1(object);
				listCommSlave.add(subComm);
			}
			listSlave1 = listCommSlave;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			status = jo.getInt(field.getFIELD_NAME_status());
			name = jo.getString(field.getFIELD_NAME_name());
			shortName = jo.getString(field.getFIELD_NAME_shortName());
			specification = jo.getString(field.getFIELD_NAME_specification());
			packageUnitID = Integer.valueOf(jo.getString(field.getFIELD_NAME_packageUnitID()));
			purchasingUnit = jo.getString(field.getFIELD_NAME_purchasingUnit());
			brandID = Integer.valueOf(jo.getString(field.getFIELD_NAME_brandID()));
			categoryID = Integer.valueOf(jo.getString(field.getFIELD_NAME_categoryID()));
			mnemonicCode = jo.getString(field.getFIELD_NAME_mnemonicCode());
			pricingType = Integer.valueOf(jo.getString(field.getFIELD_NAME_pricingType()));
			// pricePurchase =
			// Float.valueOf(jo.getString(field.getFIELD_NAME_pricePurchase()));
			latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
			priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
			priceVIP = jo.getDouble(field.getFIELD_NAME_priceVIP());
			priceWholesale = jo.getDouble(field.getFIELD_NAME_priceWholesale());
			// ratioGrossMargin =
			// Float.valueOf(jo.getString(field.getFIELD_NAME_ratioGrossMargin()));
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
			// NOAccumulated = jo.getInt(field.getFIELD_NAME_NOAccumulated());
			type = jo.getInt(field.getFIELD_NAME_type());
			nOStart = jo.getInt(field.getFIELD_NAME_nOStart());
			purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
			operatorStaffID = jo.getInt(field.getFIELD_NAME_operatorStaffID());
			syncType = jo.getString(field.getFIELD_NAME_syncType());
			subCommodityInfo = jo.getString(field.getFIELD_NAME_subCommodityInfo());
			packageUnitName = jo.getString(field.getFIELD_NAME_packageUnitName());
			startValueRemark = jo.getString(field.getFIELD_NAME_startValueRemark());

			String tmpCreateDate = jo.getString(field.getFIELD_NAME_createDate());
			if (!StringUtils.isEmpty(tmpCreateDate)) {
				createDate = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDate);
				if (createDate == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpCreateDate);
				}
			}

			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!StringUtils.isEmpty(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}

			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			// 查询商品对应的条码集合
			barcodes = jo.getString(field.getFIELD_NAME_barcodes());
			// 查询子商品集合
			List<SubCommodity> listCommSlave = new ArrayList<SubCommodity>();
			JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				JSONObject object = jr.getJSONObject(i);
				SubCommodity subComm = (SubCommodity) new SubCommodity().parse1(object.toString());
				listCommSlave.add(subComm);
			}
			listSlave1 = listCommSlave;
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
			return null;
		}

		return this;
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> commodityList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				Commodity c = new Commodity();
				c.doParse1(jsonObject1);
				commodityList.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return commodityList;
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

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex();
	}

	@Override
	public String checkRetrieveNEx(int iUseCaseID) {
		return "";
	}

	@Override
	public Map<String, Object> getRetrieveNParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Commodity comm = (Commodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_UnsalableCommodity_RetrieveN:
			params.put(field.getFIELD_NAME_date1(), comm.getDate1());
			params.put(field.getFIELD_NAME_date2(), comm.getDate2());
			params.put(field.getFIELD_NAME_messageIsRead(), comm.getMessageIsRead());
			params.put(field.getFIELD_NAME_messageParameter(), comm.getMessageParameter());
			params.put(field.getFIELD_NAME_messageCategoryID(), comm.getMessageCategoryID());
			params.put(field.getFIELD_NAME_companyID(), comm.getCompanyID());
			params.put(field.getFIELD_NAME_messageSenderID(), comm.getMessageSenderID());
			params.put(field.getFIELD_NAME_messageReceiverID(), comm.getMessageReceiverID());
			break;
		default:
			throw new RuntimeException("尚未实现getRetriveNParamEx()方法！");
		}
		return params;
	}

	// 从R1Ex查询出来的List<List<BaseModel>> bmList 结果集中获取Commodity
	public static Commodity fetchCommodityFromResultSet(List<List<BaseModel>> bmList) {
		Commodity comm = null;
		if (bmList.size() == 1) {
			comm = (Commodity) bmList.get(0);
		} else if (bmList.size() == 2) { // SP返回最多2个结果集
			comm = (Commodity) bmList.get(0).get(0);
			comm.setListSlave1(bmList.get(1));
		}

		return comm;
	}
	
	
	public String checkCreate_returnField(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		// status = EnumStatusCommodity.ESC_Normal.getIndex(); // 防止黑客创建奇怪状态的商品
		if (!StringUtils.isEmpty(mnemonicCode)) {
			if (printCheckField(field.getFIELD_NAME_mnemonicCode(), FIELD_ERROR_mnemonicCode, sbError)
					&& !(FieldFormat.checkMnemonicCode(mnemonicCode) && mnemonicCode.length() >= MIN_LENGTH_MnemonicCode && mnemonicCode.length() <= MAX_LENGTH_MnemonicCode)) {
				return field.getFIELD_NAME_mnemonicCode();
			}
		}

		if (!StringUtils.isEmpty(tag)) {
			if (printCheckField(field.getFIELD_NAME_tag(), FIELD_ERROR_tag, sbError) && !(tag.length() >= MIN_LENGTH_Tag && tag.length() <= MAX_LENGTH_Tag)) {
				return field.getFIELD_NAME_tag();
			}
		}

		if (!StringUtils.isEmpty(shortName)) {
			if (printCheckField(field.getFIELD_NAME_shortName(), FIELD_ERROR_shortName, sbError) && !(FieldFormat.checkShortName(shortName) && shortName.length() <= MAX_LENGTH_ShortName)) {
				return field.getFIELD_NAME_shortName();
			}
		}

		if (!StringUtils.isEmpty(propertyValue1)) {
			if (printCheckField(field.getFIELD_NAME_propertyValue1(), FIELD_ERROR_propertyValue, sbError) && propertyValue1.length() > MAX_LENGTH_PropertyValue) {
				return field.getFIELD_NAME_propertyValue1();
			}
		}

		if (!StringUtils.isEmpty(propertyValue2)) {
			if (printCheckField(field.getFIELD_NAME_propertyValue2(), FIELD_ERROR_propertyValue, sbError) && propertyValue2.length() > MAX_LENGTH_PropertyValue) {
				return field.getFIELD_NAME_propertyValue2();
			}
		}

		if (!StringUtils.isEmpty(propertyValue3)) {
			if (printCheckField(field.getFIELD_NAME_propertyValue3(), FIELD_ERROR_propertyValue, sbError) && propertyValue3.length() > MAX_LENGTH_PropertyValue) {
				return field.getFIELD_NAME_propertyValue3();
			}
		}

		if (!StringUtils.isEmpty(propertyValue4)) {
			if (printCheckField(field.getFIELD_NAME_propertyValue4(), FIELD_ERROR_propertyValue, sbError) && propertyValue4.length() > MAX_LENGTH_PropertyValue) {
				return field.getFIELD_NAME_propertyValue4();
			}
		}

		if (printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_name, sbError) && !(FieldFormat.checkCommodityName(name))) {//
			return field.getFIELD_NAME_name();
		}

		if (printCheckField(field.getFIELD_NAME_specification(), FIELD_ERROR_specification, sbError)
				&& !(FieldFormat.checkSpecification(specification) && specification.length() > MIN_LENGTH_Specification && specification.length() <= MAX_LENGTH_Specification)) {//
			return field.getFIELD_NAME_specification();
		}

		if (printCheckField(field.getFIELD_NAME_packageUnitID(), FIELD_ERROR_packageUnitID, sbError) && !(FieldFormat.checkPackageUnitID(String.valueOf(packageUnitID)))) {//
			return field.getFIELD_NAME_packageUnitID();
		}

		if (printCheckField(field.getFIELD_NAME_brandID(), FIELD_ERROR_brandID, sbError) && !FieldFormat.checkBrandID(String.valueOf(brandID))) {//
			return field.getFIELD_NAME_brandID();
		}

		pricingType = EnumPricingType.EPT_ByCount.getIndex();// 暂时写死计价方式字段为计件
		if (printCheckField(field.getFIELD_NAME_pricingType(), FIELD_ERROR_pricingType, sbError) && pricingType != EnumPricingType.EPT_ByCount.getIndex()) {//
			return field.getFIELD_NAME_pricingType();
		}

		if (printCheckField(field.getFIELD_NAME_categoryID(), FIELD_ERROR_categoryID, sbError) && !FieldFormat.checkCategoryID(String.valueOf(categoryID))) {//
			return field.getFIELD_NAME_categoryID();
		}
		if (printCheckField(field.getFIELD_NAME_priceRetail(), FIELD_ERROR_priceRetail, sbError) && !FieldFormat.checkCommodityPrice(priceRetail)) {//
			return field.getFIELD_NAME_priceRetail();
		}

		if (printCheckField(field.getFIELD_NAME_priceVIP(), FIELD_ERROR_priceVIP, sbError) && !FieldFormat.checkCommodityPrice(priceVIP)) {//
			return field.getFIELD_NAME_priceVIP();
		}

		if (printCheckField(field.getFIELD_NAME_priceWholesale(), FIELD_ERROR_priceWholesale, sbError) && !FieldFormat.checkCommodityPrice(priceWholesale)) {//
			return field.getFIELD_NAME_priceWholesale();
		}

		if (printCheckField(field.getFIELD_NAME_barcodes(), FIELD_ERROR_barcode, sbError) && !FieldFormat.checkIfMultiPackagingBarcodes(String.valueOf(barcodes))) {// barcodes存储条形码信息
			return field.getFIELD_NAME_barcodes();
		}

		canChangePrice = EnumBoolean.EB_NO.getIndex();// 暂时写死前台是否能改价字段为否
		if (printCheckField(field.getFIELD_NAME_canChangePrice(), FIELD_ERROR_canChangePrice, sbError) && canChangePrice != EnumBoolean.EB_NO.getIndex()) {//
			return field.getFIELD_NAME_canChangePrice();
		}

		if (printCheckField(field.getFIELD_NAME_returnDays(), FIELD_ERROR_returnDays, sbError) && !FieldFormat.checkReturnDays(String.valueOf(returnDays))) {//
			return field.getFIELD_NAME_returnDays();
		}

		if (printCheckField(field.getFIELD_NAME_operatorStaffID(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(operatorStaffID)) {
			return field.getFIELD_NAME_operatorStaffID();
		}

		switch (iUseCaseID) {
		default: // case BaseBO.CASE_Commodity_CreateSingle:
			if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_type, sbError) && !FieldFormat.checkIfCommodityType(type)) {
				return field.getFIELD_NAME_type();
			}

			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !FieldFormat.checkIfCommodityStatus(status)) {
				return field.getFIELD_NAME_status();
			}

			if (printCheckField(field.getFIELD_NAME_shelfLife(), FIELD_ERROR_shelfLife, sbError) && !FieldFormat.checkShelfLife(String.valueOf(shelfLife))) { // ?？
				return field.getFIELD_NAME_shelfLife();
			}

			purchaseFlag = PurchaseFlag_START_Default;// 暂时把采购阀值字段写死为1
			if (printCheckField(field.getFIELD_NAME_purchaseFlag(), FIELD_ERROR_purchaseFlag, sbError) && !FieldFormat.checkPurchaseFlag(String.valueOf(purchaseFlag))) {
				return field.getFIELD_NAME_purchaseFlag();
			}
			if (printCheckField(field.getFIELD_NAME_refCommodityID() + "," + field.getFIELD_NAME_refCommodityMultiple(), FIELD_ERROR_refCommodity, sbError) //
					&& !FieldFormat.checkRefCommodity(type, refCommodityID, refCommodityMultiple)) {
				return field.getFIELD_NAME_refCommodityID();
			}
			// ... 只有普通商品才能够设置为期初商品。
			if (nOStart != -1 || purchasingPriceStart != -1D) {
				if (printCheckField(field.getFIELD_NAME_nOStart(), FIELD_ERROR_nOStart, sbError) && !FieldFormat.checkID(nOStart)) {
					return field.getFIELD_NAME_nOStart();
				}
				if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), FIELD_ERROR_purchasingPriceStart, sbError) && (purchasingPriceStart <= 0 || purchasingPriceStart > FieldFormat.MAX_OneCommodityPrice)) {
					return field.getFIELD_NAME_purchasingPriceStart();
				}
			}

			ruleOfPoint = RuleOfPoint_START_Default;// 暂时把积分规则字段写死为1
			if (printCheckField(field.getFIELD_NAME_ruleOfPoint(), FIELD_ERROR_ruleOfPoint, sbError) && !FieldFormat.checkRuleOfPoint(String.valueOf(ruleOfPoint))) { // 积分规则允许为0的情况
				return field.getFIELD_NAME_ruleOfPoint();
			}

			if (!StringUtils.isEmpty(startValueRemark)) {
				if (printCheckField(field.getFIELD_NAME_startValueRemark(), FIELD_ERROR_startValueRemark, sbError) && startValueRemark.length() > MAX_LENGTH_StartValueRemark) {
					return field.getFIELD_NAME_startValueRemark();
				}
			}
		}
		return "";
	}
	

	public enum EnumCommodityInfoInExcel {
		ECIIE_barcodes("barcodes", 0), //
		ECIIE_name("name", 1), //
		ECIIE_packageUnitName("packageUnitName", 2),
		ECIIE_priceRetail("priceRetail", 3),
		ECIIE_providerName("providerName", 4),
		ECIIE_categoryName("categoryName", 5),
		ECIIE_specification("specification", 6),
		ECIIE_purchasingUnit("purchasingUnit", 7),
		ECIIE_brandName("brandName", 8),
		ECIIE_priceVIP("priceVIP", 9),
		ECIIE_shelfLife("shelfLife", 10),
		ECIIE_returnDays("returnDays", 11),
		ECIIE_nOStart("nOStart", 12),
		ECIIE_purchasingPriceStart("purchasingPriceStart", 13);
		
		private String name;
		private int index;

		private EnumCommodityInfoInExcel(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCommodityInfoInExcel c : EnumCommodityInfoInExcel.values()) {
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
	
	
	public String doParse1_returnField(String s) {
		try {
			JSONObject jo = JSONObject.fromObject(s);
			ID = jo.getInt(field.getFIELD_NAME_ID());
			status = jo.getInt(field.getFIELD_NAME_status());
			name = jo.getString(field.getFIELD_NAME_name());
			shortName = jo.getString(field.getFIELD_NAME_shortName());
			specification = jo.getString(field.getFIELD_NAME_specification());
			packageUnitID = Integer.valueOf(jo.getString(field.getFIELD_NAME_packageUnitID()));
			purchasingUnit = jo.getString(field.getFIELD_NAME_purchasingUnit());
			brandID = Integer.valueOf(jo.getString(field.getFIELD_NAME_brandID()));
			categoryID = Integer.valueOf(jo.getString(field.getFIELD_NAME_categoryID()));
			mnemonicCode = jo.getString(field.getFIELD_NAME_mnemonicCode());
			pricingType = Integer.valueOf(jo.getString(field.getFIELD_NAME_pricingType()));
			// pricePurchase =
			// Float.valueOf(jo.getString(field.getFIELD_NAME_pricePurchase()));
			latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
			priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
			priceVIP = jo.getDouble(field.getFIELD_NAME_priceVIP());
			priceWholesale = jo.getDouble(field.getFIELD_NAME_priceWholesale());
			// ratioGrossMargin =
			// Float.valueOf(jo.getString(field.getFIELD_NAME_ratioGrossMargin()));
			canChangePrice = Integer.valueOf(jo.getString(field.getFIELD_NAME_canChangePrice()));
			ruleOfPoint = Integer.valueOf(jo.getString(field.getFIELD_NAME_ruleOfPoint()));
			picture = jo.getString(field.getFIELD_NAME_picture());
			shelfLife = jo.getInt(field.getFIELD_NAME_shelfLife());
			returnDays = jo.getInt(field.getFIELD_NAME_returnDays());
			purchaseFlag = jo.getInt(field.getFIELD_NAME_purchaseFlag());
			refCommodityID = Integer.valueOf(jo.getString(field.getFIELD_NAME_refCommodityID()));
			refCommodityMultiple = Integer.valueOf(jo.getString(field.getFIELD_NAME_refCommodityMultiple()));
			tag = jo.getString(field.getFIELD_NAME_tag());
			NO = Integer.valueOf(jo.getString(field.getFIELD_NAME_NO()));
			// NOAccumulated = jo.getInt(field.getFIELD_NAME_NOAccumulated());
			type = jo.getInt(field.getFIELD_NAME_type());
			nOStart = jo.getInt(field.getFIELD_NAME_nOStart());
			purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
			operatorStaffID = jo.getInt(field.getFIELD_NAME_operatorStaffID());
			syncType = jo.getString(field.getFIELD_NAME_syncType());
			subCommodityInfo = jo.getString(field.getFIELD_NAME_subCommodityInfo());
			packageUnitName = jo.getString(field.getFIELD_NAME_packageUnitName());
			startValueRemark = jo.getString(field.getFIELD_NAME_startValueRemark());

			String tmpCreateDate = jo.getString(field.getFIELD_NAME_createDate());
			if (!StringUtils.isEmpty(tmpCreateDate)) {
				createDate = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDate);
				if (createDate == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDate() + "=" + tmpCreateDate);
				}
			}

			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!StringUtils.isEmpty(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}

			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!StringUtils.isEmpty(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			// 查询商品对应的条码集合
			barcodes = jo.getString(field.getFIELD_NAME_barcodes());
			// 查询子商品集合
			List<SubCommodity> listCommSlave = new ArrayList<SubCommodity>();
			JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				JSONObject object = jr.getJSONObject(i);
				SubCommodity subComm = (SubCommodity) new SubCommodity().parse1(object.toString());
				listCommSlave.add(subComm);
			}
			listSlave1 = listCommSlave;
		} catch (Exception e) {
			e.printStackTrace();
			String[] str = e.getMessage().split("\"");
			return str[1];
		}
		return "";
	}
	
	public Map<String, String> putInMap() {
		Map<String, String> params = new HashMap<String, String>(); 
		params.put(field.getFIELD_NAME_multiPackagingInfo(), getMultiPackagingInfo());
		params.put(field.getFIELD_NAME_name(), getName());
		params.put(field.getFIELD_NAME_shortName(), getShortName());
		params.put(field.getFIELD_NAME_specification(), getSpecification());
		params.put(field.getFIELD_NAME_packageUnitID(), String.valueOf(getPackageUnitID()));
		params.put(field.getFIELD_NAME_purchasingUnit(), getPurchasingUnit());
		params.put(field.getFIELD_NAME_brandID(), String.valueOf(getBrandID()));
		params.put(field.getFIELD_NAME_categoryID(), String.valueOf(getCategoryID()));
		params.put(field.getFIELD_NAME_mnemonicCode(), getMnemonicCode());
		params.put(field.getFIELD_NAME_pricingType(), String.valueOf(getPricingType()));
		params.put(field.getFIELD_NAME_priceRetail(), String.valueOf(getPriceRetail()));
		params.put(field.getFIELD_NAME_priceVIP(), String.valueOf(getPriceVIP()));
		params.put(field.getFIELD_NAME_priceWholesale(), String.valueOf(getPriceWholesale()));
		params.put(field.getFIELD_NAME_canChangePrice(), String.valueOf(getCanChangePrice()));
		params.put(field.getFIELD_NAME_ruleOfPoint(), String.valueOf(getRuleOfPoint()));
		params.put(field.getFIELD_NAME_picture(), getPicture());
		params.put(field.getFIELD_NAME_shelfLife(), String.valueOf(getShelfLife()));
		params.put(field.getFIELD_NAME_returnDays(), String.valueOf(getReturnDays()));
		params.put(field.getFIELD_NAME_purchaseFlag(), String.valueOf(getPurchaseFlag()));
		params.put(field.getFIELD_NAME_refCommodityID(), String.valueOf(getRefCommodityID()));
		params.put(field.getFIELD_NAME_refCommodityMultiple(), String.valueOf(getRefCommodityMultiple()));
		params.put(field.getFIELD_NAME_tag(), getTag());
		params.put(field.getFIELD_NAME_NO(), String.valueOf(getNO()));
		params.put(field.getFIELD_NAME_type(), String.valueOf(getType()));
		params.put(field.getFIELD_NAME_nOStart(), getnOStart() == 0 ? String.valueOf(NO_START_Default) : String.valueOf(getnOStart()));
		params.put(field.getFIELD_NAME_purchasingPriceStart(),
				Math.abs(GeneralUtil.sub(getPurchasingPriceStart(), 0)) < BaseModel.TOLERANCE ? String.valueOf(PURCHASING_PRICE_START_Default) : String.valueOf(getPurchasingPriceStart()));
		params.put(field.getFIELD_NAME_returnObject(), String.valueOf(getReturnObject()));
		params.put(field.getFIELD_NAME_multiPackagingInfo(), getMultiPackagingInfo());
		params.put(field.getFIELD_NAME_providerIDs(), getProviderIDs());
		params.put(field.getFIELD_NAME_propertyValue1(), getPropertyValue1());
		params.put(field.getFIELD_NAME_propertyValue2(), getPropertyValue2());
		params.put(field.getFIELD_NAME_propertyValue3(), getPropertyValue3());
		params.put(field.getFIELD_NAME_propertyValue4(), getPropertyValue4());
		params.put(field.getFIELD_NAME_returnObject(), String.valueOf(getReturnObject()));
		
		return params;
	}
}
