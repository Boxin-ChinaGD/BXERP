package com.bx.erp.util;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Company;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.Coupon.EnumCouponType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.Provider;

public class FieldFormat {
	public static final int LENGTH_Mobile = 11;
	public static final int LENGTH_Submchid = 10;
	public static final int MAX_LENGTH_dbName = 20;
	public static final int MAX_LENGTH_HumanName = 12;
	public static final int MIN_LENGTH_HumanName = 1;
	public static final int MAX_LENGTH_Password = 16;
	public static final int MIN_LENGTH_Password = 6;
	/** 商品、采购、入库、采购退货时每次每一种商品最高单价，其它地方不要乱用！ */
	public static final double MAX_OneCommodityPrice = 10000.000000d;
	/** 采购、入库、采购退货时每次每一种商品最多件数，其它地方不要乱用！ */
	public static final int MAX_OneCommodityNO = 10000;
	public static final int MAX_LENGTH_OpenID = 50;
	public static final int MAX_LENGTH_UnionID = 50;

	public static final String FIELD_ERROR_wechat = "必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文";
	public static final String FIELD_ERROR_dbName = "必须以字母开头，5位以上20位以下，由字母、数字和下划线组成，不支持设置中文";
	public static final String FIELD_ERROR_HumanName = "人名格式应为：中英文，" + MIN_LENGTH_HumanName + "到" + MAX_LENGTH_HumanName + "字符";
	public static final String FIELD_ERROR_Password = "密码不能有中文，首尾不能有空格并且格式必须是" + MIN_LENGTH_Password + "到" + MAX_LENGTH_Password + "位";
	public static final String FIELD_ERROR_ID = "ID必须>0";
	public static final String FIELD_ERROR_ID_ForRetrieveN = "%s不能小于" + BaseAction.INVALID_ID; // %s为INT型字段的名称
	public static final String FIELD_ERROR_Paging = "PageIndex和PageSize必须都大于0";
	public static final String FIELD_ERROR_openID = "Openid只能为数字、英文、下划线、横杆的组合，且长度不超过" + MAX_LENGTH_OpenID;
	public static final String FIELD_ERROR_UnionID = "UnionID只能为数字、英文、下划线、横杆的组合，且长度不超过" + MAX_LENGTH_UnionID;

	protected static Pattern pNumber = Pattern.compile("^-?[0-9]\\d*$");// 只允许正负整数
	protected static Pattern pNum = Pattern.compile("^[0-9]\\d*$");// 只允许整数
	protected static Pattern pBusinessLicenseSN = Pattern.compile("^([A-Z0-9]{15}|[A-Z0-9]{18})$");// 只允许数字或大写字母组合，15位或18位
	protected static Pattern pNature = Pattern.compile("^[1-9]\\d*$");// 只允许正整数（自然数）
	protected static Pattern pNumEn = Pattern.compile("^[A-Za-z0-9]+$");// 只允许数字和英文
	protected static Pattern pOpenID = Pattern.compile("^[A-Za-z0-9_-]{1," + MAX_LENGTH_OpenID + "}$");// 只允许数字和英文、下划线，不超过50
	protected static Pattern pUnionID = Pattern.compile("^[A-Za-z0-9_-]{1," + MAX_LENGTH_UnionID + "}$");// 只允许数字和英文、下划线，不超过50
	protected static Pattern pNumEnSpace = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9\\s]+$");// 只允许中文、空格、数字和英文和空格
	protected static Pattern pHumanName = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z]{" + MIN_LENGTH_HumanName + "," + MAX_LENGTH_HumanName + "}$");// 只允许中文和英文，[1, 12]字符
	protected static Pattern pNumEnChinese = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]+$");// 只允许中文、数字和英文
	protected static Pattern pBrandName = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]{1," + Brand.MAX_LENGTH_BrandName + "}$");// 只允许中文、数字和英文，[1,20]字符
	protected static Pattern pVipCategory = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]{1,30}$");// 只允许中文、数字和英文，[1, 30]]位
	protected static Pattern pCompanyName = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z]{" + Company.MIN_LENGTH_Name + "," + Company.MAX_LENGTH_Name + "}$");// 只允许中英文，[1, 12]字符
	protected static Pattern pDBUserName = Pattern.compile("^[a-zA-Z][A-Za-z0-9_]{0,19}$");// 数字、字母和下划线的组合，但首字符必须是字母，不能出现空格。[1, 20]字符
	protected static Pattern pPosSN = Pattern.compile("^[A-Za-z0-9]{1,32}$");// 只允许[1,32]数字和英文
	protected static Pattern pQQ = Pattern.compile("^[0-9]{5,10}$");// [5,10]位整数
	protected static Pattern pAccount = Pattern.compile("^[A-Za-z0-9]+$");// 只允许数字和英文
	protected static Pattern pCouponTitle = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9.]+$");// 只允许中文、数字和英文
	//
	// protected static Pattern pNumEnChinese =
	// Pattern.compile("^[^\\s][\\u4E00-\\u9FA5A-Za-z0-9\\s]+[^\\s]$");//
	// 只允许中文、数字、英文和空格且空格不能出现在首尾
	protected static Pattern pDecimals = Pattern.compile("^([0-9][0-9]*)+(\\.[0-9]{1,6})?$");// 非零开头的最多带6位小数的数字
	protected static Pattern pPhone = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");// 手机号码
	// protected static Pattern pRowPassword = Pattern.compile("^[^ ].{4,14}[^ ]$");
	// // 首尾不能为空，6-16位任意字符
	protected static Pattern pRowPassword = Pattern.compile("^[^ \\u4e00-\\u9fa5][^\\u4e00-\\u9fa5]{4,14}[^ \\u4e00-\\u9fa5]$"); // 不能有中文并且首尾不能为空，6-16位任意字符
	protected static Pattern pSalt = Pattern.compile("^[A-Z0-9]{32}$");// 盐值应该为32个数字或大写字母的组合
	protected static Pattern pWeChat = Pattern.compile("^[a-zA-Z0-9_-]{5,20}$");// 必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文。其中20位以下是我们系统的要求，不是微信官方要求
	protected static Pattern pDbName = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,19}$");// 数字、字母和下划线的组合，但首字符必须是字母，中间不能出现空格。(0, 20]个字符
	protected static Pattern pICID = Pattern.compile("^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$");// 身份证格式
	protected static Pattern pProviderAddress = Pattern.compile("^[^ ].{0,48}[^ ]$"); // 首尾不能为空，2-50位任意字符
	protected static Pattern pProviderName = Pattern.compile("^[^ ][\\u4E00-\\u9FA5A-Za-z0-9\\s]{0,18}[^ ]$");// 只允许中文、空格、数字和英文,首尾不能为空格，2-20位
	// protected static Pattern pCommodityName = Pattern.compile("^[^
	// ][\\u4E00-\\u9FA5A-Za-z0-9\\s]{0,30}[^ ]$");// 只允许中文、空格、数字和英文,首尾不能为空格，2-32位
	protected static Pattern pCommodityName = Pattern.compile("^[\\u4E00-\\u9FA5a-zA-Z0-9_\\-—*“”、$#/()（）\\s]{" + Commodity.MIN_LENGTH_CommodityName + "," + Commodity.MAX_LENGTH_CommodityName + "}$"); // 只允许以()（）-—_、中英数值、空格形式出现
	protected static Pattern PATTERN_RetailTrade_QueryKeyword = Pattern.compile("^[\\u4E00-\\u9FA5a-zA-Z0-9_\\-—()（）\\s]{" + 0 + "," + RetailTrade.Max_LengthQueryKeyword + "}$"); // 只允许以()（）-—_、中英数值、空格形式出现
	protected static Pattern pShopName = Pattern.compile("^[^ ].{0,18}[^ ]$"); // 首尾不能为空，2-20位任意字符
	protected static Pattern pShopAddress = Pattern.compile("^[^ ].{0,28}[^ ]$"); // 首尾不能为空，2-30位任意字符
	protected static Pattern pEmail = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	protected static final Pattern PATTERN_RetailTrade_SN_ForCreate = Pattern.compile("^LS[0-9]{" + String.valueOf(RetailTrade.LENGTH_SN - 2) + "}(_1){0,1}$"); // LS开头，年月日、时分秒要符合常识，pos_id是四位（不够前面补零），后面四位随机数。末尾_1是检查退货单
	protected static final Pattern PATTERN_RetailTrade_SN_ForQuery = Pattern.compile("^(LS[0-9]{" + String.valueOf(RetailTrade.Min_LengthSN_Query - 2) + "," + String.valueOf(RetailTrade.Max_LengthSN_Query - 2) + "}|[0-9]{"
			+ String.valueOf(RetailTrade.Min_LengthSN_Query) + "," + String.valueOf(RetailTrade.Max_LengthSN_Query - 2) + "})(_1){0,1}$", Pattern.CASE_INSENSITIVE); // LS后8-22位数字，或10-24位数字,结尾_1表示是退货单，可有可无

	/** 判断参照商品,单品、组合商品和服务商品为0，多包装商品为正整数 <br />
	 * 判断参照商品倍数,单品和组合商品为0，多包装商品大于1 **/
	public static boolean checkRefCommodity(int type, int refCommodityID, int refCommodityMultiple) {
		if (type == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
			if (refCommodityID <= Commodity.DEFAULT_VALUE_RefCommodityID || refCommodityMultiple <= 1) {
				return false;
			}
			return true;
		} else {
			if (refCommodityID != Commodity.DEFAULT_VALUE_RefCommodityID || refCommodityMultiple != Commodity.DEFAULT_VALUE_RefCommodityMultiple) {
				return false;
			}
			return true;
		}
	}

	public static boolean checkProviderAddress(String address) {
		if (address == null) {
			return false;
		}
		// 因为正则冲突，只能限制长度为2，故判断长度为1的情况
		if (address.length() == 1) {
			if (address.trim().length() == 0) {
				return false;
			} else {
				Matcher m = pNumEnChinese.matcher(address);
				return m.matches();
			}
		} else {
			Matcher m = pProviderAddress.matcher(String.valueOf(address));
			return m.matches();
		}
	}

	public static boolean checkCommodityName(String commodityName) {
		if (commodityName == null) {
			return false;
		}
		if (commodityName.trim().length() == 0) {
			return false;
		}
		if (commodityName.trim().length() != commodityName.length()) {
			return false;
		}
		Matcher m = pCommodityName.matcher(commodityName);
		return m.matches();
	}

	public static boolean checkInventorySheetRemark(String remark) { // ...无测试
		if (remark == null) {
			return false;
		}
		Matcher m = pNumEnChinese.matcher(remark);
		return m.matches();
	}

	public static boolean checkHumanName(String humanName) {
		if (humanName == null) {
			return false;
		}
		Matcher m = pHumanName.matcher(humanName);
		return m.matches();
	}

	public static boolean checkRoleName(String roleName) {// ...没有相应的测试
		if (roleName == null) {
			return false;
		}
		Matcher m = pNumEnChinese.matcher(roleName);
		return m.matches();
	}

	public static boolean checkWarehouseName(String warehouseName) {
		if (warehouseName == null) {
			return false;
		}
		Matcher m = pNumEnChinese.matcher(warehouseName);
		return m.matches();
	}

	public static boolean checkBrandName(String brandName) {
		if (brandName == null) {
			return false;
		}
		Matcher m = pBrandName.matcher(brandName);
		return m.matches();
	}

	public static boolean checkProviderName(String providerName) {
		if (providerName == null) {
			return false;
		}
		Matcher m = pNumEnChinese.matcher(providerName);
		return m.matches();
	}

	public static boolean checkCategoryName(String categoryName) {
		if (categoryName == null) {
			return false;
		}
		Matcher m = pNumEnChinese.matcher(categoryName);
		return m.matches();
	}

	public static boolean checkVipCategoryName(String vipCategoryName) {
		if (vipCategoryName == null) {
			return false;
		}
		Matcher m = pVipCategory.matcher(vipCategoryName);
		return m.matches();
	}

	/** 采购、入库、采购退货时每次每一种商品最大数量，其它地方不要乱用！ */
	public static boolean checkCommodityNO(int commodityNO) {
		if (commodityNO > MAX_OneCommodityNO) {
			return false;
		}
		//
		Matcher m = pNature.matcher(String.valueOf(commodityNO));
		return m.matches();
	}

	public static boolean checkMobile(String mobile) {
		if (mobile == null) {
			return false;
		}
		Matcher m = pPhone.matcher(String.valueOf(mobile));
		return m.matches();
	}

	public static boolean checkContactName(String contactName) {
		if (contactName == null) {
			return true;
		}
		// 因为正则冲突，只能限制长度为2，故判断长度为1的情况
		if (contactName.length() == 1) {
			if (contactName.trim().length() == 0) {
				return false;
			} else {
				Matcher m = pNumEnChinese.matcher(contactName);
				return m.matches();
			}
		} else {
			Matcher m = pProviderName.matcher(String.valueOf(contactName));
			return m.matches();
		}
	}

	public static boolean checkAddress(String address) {
		if (address == null) {
			return true;
		}
		Matcher m = pNumEnSpace.matcher(address);// 中文、英文、数字和空格
		if (address.trim().length() == 1) {
			return m.matches();
		} else {
			Pattern noLeftSpace = Pattern.compile("^[^\\s][\\u4E00-\\u9FA5A-Za-z0-9\\s]+$");// 不允许左空格
			Pattern noRightSpace = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9\\s]+[^\\s]$");// 不允许右空格
			Matcher mNoLeftSpace = noLeftSpace.matcher(address);
			Matcher mNoRightSpace = noRightSpace.matcher(address);
			return (mNoLeftSpace.matches() && m.matches() && mNoRightSpace.matches());
		}
	}

	public static boolean checkName(String name) {
		if (name == null || name.trim().length() == 0) {
			return false;
		}
		Matcher m = pNumEnSpace.matcher(name);
		return m.matches();
	}

	public static boolean checkShortName(String shortName) {
		if (shortName == null) {
			return false;
		}
		Matcher m = pNumEnSpace.matcher(shortName);
		return m.matches();
	}

	public static boolean checkShopName(String shopName) {
		if (shopName == null) {
			return false;
		}
		// if (shopName.length() != shopName.trim().length()) {
		// return false;
		// }
		// if (shopName.length() < 1 || shopName.length() > 20) {
		// return false;
		// }
		//
		// return true;
		if (shopName.length() == 1) {
			if (shopName.trim().length() == 0) {
				return false;
			} else {
				Matcher m = pNumEnChinese.matcher(shopName);
				return m.matches();
			}
		} else {
			Matcher m = pShopName.matcher(String.valueOf(shopName));
			return m.matches();
		}
	}

	public static boolean checkShopAddress(String shopAddress) {
		if (shopAddress == null) {
			return false;
		}
		if (shopAddress.length() == 1) {
			if (shopAddress.trim().length() == 0) {
				return false;
			} else {
				Matcher m = pNumEnChinese.matcher(shopAddress);
				return m.matches();
			}
		} else {
			Matcher m = pShopAddress.matcher(String.valueOf(shopAddress));
			return m.matches();
		}
	}

	public static boolean checkBarcode(String barcode) {
		if (barcode == null) {
			return false;
		}
		Matcher m = pNumEn.matcher(barcode);
		return m.matches();
	}

	public static boolean checkSpecification(String specification) {
		if (specification == null) {
			return false;
		}
		Matcher m = pNumEnSpace.matcher(specification);
		return m.matches();
	}

	public static boolean checkPackageUnitID(String packageUnitID) {
		if (packageUnitID == null) {
			return false;
		}
		Matcher m = pNum.matcher(String.valueOf(packageUnitID));
		return m.matches();
	}

	public static boolean checkPurchasingUnit(String purchasingUnit) {
		if (purchasingUnit == null) {
			return true;
		}
		if (purchasingUnit.trim().length() == 0) {
			return false;
		}
		Matcher m = pNumEnSpace.matcher(purchasingUnit);
		return m.matches();
	}

	public static boolean checkProviderID(String providerID) {
		if (providerID == null) {
			return false;
		}
		Matcher m = pNature.matcher(String.valueOf(providerID));
		return m.matches();
	}

	public static boolean checkBrandID(String brandID) {
		if (brandID == null) {
			return false;
		}
		Matcher m = pNature.matcher(String.valueOf(brandID));
		return m.matches();
	}

	public static boolean checkCategoryID(String categoryID) {
		if (categoryID == null) {
			return false;
		}
		Matcher m = pNature.matcher(String.valueOf(categoryID));
		return m.matches();
	}

	public static boolean checkMnemonicCode(String mnemonicCode) {
		if (mnemonicCode == null) {
			return false;
		}
		Matcher m = pNumEn.matcher(mnemonicCode);
		return m.matches();
	}

	/** 商品、采购、入库、采购退货时每次每一种商品最高单价，其它地方不要乱用！ */
	public static boolean checkCommodityPrice(double price) {
		return price >= 0 && price <= MAX_OneCommodityPrice;
	}

	public static boolean checkRatioGrossMargin(String ratioGrossMargin) {
		if (ratioGrossMargin == null) {
			return false;
		}
		Matcher m = pDecimals.matcher(String.valueOf(ratioGrossMargin));
		return m.matches();
	}

	public static boolean checkPurchaseFlag(String purchaseFlag) {
		if (purchaseFlag == null) {
			return true;
		}
		Matcher m = pNum.matcher(purchaseFlag);
		return m.matches();
	}

	public static boolean checkRuleOfPoint(String ruleOfPoint) {
		if (ruleOfPoint == null) {
			return true;
		}
		Matcher m = pNum.matcher(ruleOfPoint);
		return m.matches();
	}

	public static boolean checkRefCommodityID(String refCommodityID) {
		if (refCommodityID == null) {
			return false;
		}

		Matcher m = pNum.matcher(refCommodityID);
		return m.matches();
	}

	public static boolean checkRefCommodityMultiple(String refCommodityMultiple) {
		if (refCommodityMultiple == null) {
			return false;
		}

		Matcher m = pNum.matcher(refCommodityMultiple);
		return m.matches();
	}

	public static boolean checkTag(String tag) {
		if (tag == null) {
			return false;
		}
		Matcher m = pNumEnChinese.matcher(tag);
		return m.matches();
	}

	public static boolean checkSenderID(String ID) {
		if (ID == null) {
			return false;
		}
		Matcher m = pNum.matcher(ID);
		return m.matches();
	}

	public static boolean checkNO(String NO) {
		if (NO == null) {
			return false;
		}
		Matcher m = pNum.matcher(NO);
		return m.matches();
	}

	public static boolean checkInventoryCommodityNoReal(String noReal) {
		if (noReal == null) {
			return false;
		}
		Matcher m = pNumber.matcher(noReal);
		return m.matches();
	}

	public static boolean checkNOAccumulated(String NOAccumulated) {
		if (NOAccumulated == null) {
			return false;
		}
		Matcher m = pNum.matcher(String.valueOf(NOAccumulated));
		return m.matches();
	}

	public static boolean checkReturnDays(String returnDays) {
		if (returnDays == null) {
			return false;
		}
		Matcher m = pNum.matcher(String.valueOf(returnDays));
		return m.matches();
	}

	public static boolean checkShelfLife(String shelfLife) {
		if (shelfLife == null) {
			return true;
		}
		Matcher m = pNature.matcher(shelfLife);
		return m.matches();
	}

	public static boolean checkDate(String date) {
		if (date == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);
		try {
			sdf.parse(date);
		} catch (ParseException e) {
			sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			try {
				sdf.parse(date);
			} catch (ParseException e1) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkPhone(String phone) {
		if (phone == null) {
			return false;
		}
		Matcher m = pPhone.matcher(phone);
		return m.matches();
	}

	public static boolean checkRawPassword(String password) {
		if (password == null) {
			return false;
		}
		Matcher m = pRowPassword.matcher(password);
		return m.matches();
	}

	/** @param weChat
	 *            不能为null */
	public static boolean checkWeChat(String weChat) {
		Matcher m = pWeChat.matcher(weChat);
		return m.matches();
	}

	public static boolean checkDbName(String dbName) {
		if (dbName == null) {
			return false;
		}
		Matcher m = pDbName.matcher(dbName);
		return m.matches();
	}

	public static boolean checkSubmchid(String submchid) {
		if (submchid == null) {
			return true;
		}
		if (submchid.length() == 0) {
			return true;
		}
		if (submchid.length() != LENGTH_Submchid) {
			return false;
		}

		Matcher m = pNum.matcher(submchid);
		return m.matches();
	}

	public static boolean checkSalt(String salt) {
		if (salt == null) {
			return false;
		}
		Matcher m = pSalt.matcher(salt);
		return m.matches();
	}

	public static boolean checkICID(String ICID) {
		if (ICID == null) {
			return true;
		}
		Matcher m = pICID.matcher(ICID);
		return m.matches();
	}

	/**
	 * <p>
	 * 判断多包装商品类型是否符合
	 * </p>
	 **/
	public static boolean checkIfMultiPackagingType(int type) {
		if (type != EnumCommodityType.ECT_MultiPackaging.getIndex()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断多包装商品类型是否符合
	 * </p>
	 **/
	public static boolean checkIfServiceType(int type) {
		if (type != EnumCommodityType.ECT_Service.getIndex()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断多包装商品状态是否符合
	 * </p>
	 **/
	public static boolean checkIfMultiPackagingStatus(int status) {
		if (status != 1 && status != 0) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断组合商品和普通商品状态是否符合
	 * </p>
	 **/
	public static boolean checkIfCommodityStatus(int status) {
		if (status == 0) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * 判断普通商品类型是否符合
	 * </p>
	 **/
	public static boolean checkIfCommodityType(int type) {
		if (type == 0) {
			return true;
		}
		return false;
	}

	/** @param multiPackagingInfo
	 * @return */
	public static boolean checkBarcodeInMultiPackagingInfo(String multiPackagingInfo) {
		if (multiPackagingInfo == null) {
			return false;
		}
		// 对参数进行切割
		String[] multiPackagingInfoList = multiPackagingInfo.split(";");
		List<String> barcodeslist = Arrays.asList(multiPackagingInfoList[0].split(","));
		// 拿到barcode进行判断
		for (String barcodes : barcodeslist) {
			Matcher m = pNumEn.matcher(barcodes);
			if (!(m.matches() && barcodes.length() >= Barcodes.MIN_LENGTH_Barcodes && barcodes.length() <= Barcodes.MAX_LENGTH_Barcodes)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>
	 * 判断多包装商品条形码要是英文或数字的组合，长度在7~64之间。
	 * </p>
	 **/
	public static boolean checkIfMultiPackagingBarcodes(String barcodes) {
		if (barcodes == null) {
			return false;
		}
		Matcher m = pNumEn.matcher(barcodes);
		if (m.matches() && barcodes.length() >= Barcodes.MIN_LENGTH_Barcodes && barcodes.length() <= Barcodes.MAX_LENGTH_Barcodes) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * 判断多包装商品和组合商品的库存是否为0
	 * </p>
	 **/
	public static boolean checkIfMultiPackagingNO(int no) {
		if (no != 0) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断实盘数量是否大于0
	 * </p>
	 **/
	public static boolean checkNoReal(int noReal) {
		if (noReal < 0) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断组合商品的采购阀值是否为-1
	 * </p>
	 **/
	public static boolean checkIfCompositionPurchaseFlag(int purchaseFlag) {
		if (purchaseFlag != -1) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断组合商品的保质期是否为-1
	 * </p>
	 **/
	public static boolean checkIfCompositionShelfLife(int shelfLife) {
		if (shelfLife != -1) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断组合商品的积分规则是否为-1
	 * </p>
	 **/
	public static boolean checkIfCompositionRuleOfPoint(int ruleOfPoint) {
		if (ruleOfPoint != -1) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 判断组合商品的类型是否为1
	 * </p>
	 **/
	public static boolean checkIfCompositionType(int type) {
		if (type != 1) {
			return false;
		}
		return true;
	}

	public static boolean checkCompanyKey(String key) {
		if (key == null) {
			return false;
		}
		Matcher m = pSalt.matcher(key);
		return m.matches();
	}

	public static boolean checkBusinessLicenseSN(String businessLicenseSN) {
		if (businessLicenseSN == null) {
			return false;
		}
		Matcher m = pBusinessLicenseSN.matcher(businessLicenseSN);
		return m.matches();
	}

	public static boolean checkProviderMobile(String providerMobile) {
		return providerMobile.length() >= Provider.Min_LengthMoile && providerMobile.length() <= Provider.Max_LengthMoile;
	}

	public static boolean checkCompanyName(String str) {
		if (str == null) {
			return false;
		}
		Matcher m = pCompanyName.matcher(String.valueOf(str));
		return m.matches();
	}

	public static boolean checkDBUserName(String str) {
		if (str == null) {
			return false;
		}
		Matcher m = pDBUserName.matcher(String.valueOf(str));
		return m.matches();
	}

	public static boolean checkID(int iD) {
		return iD > 0;
	}

	public static boolean checkNoStart(int noStart) {
		return noStart > 0;
	}

	public static boolean checkRoleIDToCreateStaff(int roleID) {
		return roleID == EnumTypeRole.ETR_Cashier.getIndex() || roleID == EnumTypeRole.ETR_Boss.getIndex() || roleID == EnumTypeRole.ETR_PreSale.getIndex();
	}

	public static boolean checkShopID(int shopID) {
		return shopID > 0 || shopID == -1;
	}

	public static boolean checkPaging(int checkPageIndex, int checkPageSize) {
		return checkPageIndex > 0 && checkPageSize > 0;
	}

	public static boolean checkPosSN(String posSN) {
		if (StringUtils.isEmpty(posSN)) {
			return false;
		}
		Matcher m = pPosSN.matcher(posSN);
		return m.matches();
	}

	public static boolean checkEmail(String email) {
		if (email == null) {
			return false;
		}

		Matcher m = pEmail.matcher(email);
		return m.matches();
	}

	public static boolean checkQQ(String qq) {
		if (StringUtils.isEmpty(qq)) {
			return false;
		}

		Matcher m = pQQ.matcher(qq);
		return m.matches();
	}

	public static boolean checkAccount(String account) {
		if (StringUtils.isEmpty(account)) {
			return false;
		}

		Matcher m = pAccount.matcher(account);
		return m.matches();
	}

	public static boolean checkRetailTradeSN(String sn) { // checkCreate
		if (!StringUtils.isNotEmpty(sn)) {
			return false;
		}
		Matcher m = PATTERN_RetailTrade_SN_ForCreate.matcher(sn);
		if (!m.matches()) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		try {
			Date date = sdf.parse(sn.substring(2, 15)); // 要符合时间规范,不能有其它字符或中文等
			if (date.getTime() > new Date().getTime()) { // 不能比当前时间还晚
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean checkRetailTradeRetrieveNByQueryKeyword(String queryKeyword) {
		if ("".equals(queryKeyword)) {
			return true;
		} else if (queryKeyword == null || queryKeyword.trim().length() != queryKeyword.length() || queryKeyword.length() > RetailTrade.Max_LengthQueryKeyword) {
			return false;
		} else {
			return PATTERN_RetailTrade_QueryKeyword.matcher(queryKeyword).matches();
		}
	}

	/** pos端根据SN（可以是SN的一部分）来查单，输入内容要求是10-26位，同时允许空串，LS可大小写。 */
	public static boolean checkRetailTradeRetrieveNBySN(String partOfSN) {
		if ("".equals(partOfSN)) {
			return true;
		} else if (partOfSN == null) {
			return false;
		} else {
			return PATTERN_RetailTrade_SN_ForQuery.matcher(partOfSN).matches();
		}
	}

	public static boolean checkCountOfBlankLineAtBottom(int countOfBlankLineAtBottom) {
		if (countOfBlankLineAtBottom < SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom || countOfBlankLineAtBottom > SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom) {
			return false;
		}
		return true;
	}

	public static boolean checkBoolean(int iBoolean) {
		if (iBoolean < EnumBoolean.EB_NO.getIndex() || iBoolean > EnumBoolean.EB_Yes.getIndex()) {
			return false;
		}
		return true;
	}

	public static boolean checkCardType(String card_type) {
		if (card_type == null) {
			return true;
		}
		if (!card_type.equals(EnumCouponType.ECT_Cash.getName()) && !card_type.equals(EnumCouponType.ECT_Discount.getName())) {
			return false;
		}
		return true;
	}

	public static boolean checkCouponTitle(String title) {
		if (title == null) {
			return false;
		}
		if (title.length() > Coupon.Max_Length_title) {
			return false;
		}
		Matcher m = pCouponTitle.matcher(title);
		return m.matches();
	}

	public static boolean validate2RGBColor(String backgroundColor) {
		if (backgroundColor == null) {
			return false;
		}

		String[] backgroundColors = backgroundColor.split(";");
		if (backgroundColors.length != 2) {
			return false;
		}
		for (int i = 0; i < backgroundColors.length; i++) {
			Integer[] rgbs = GeneralUtil.toIntArray(backgroundColors[i]);
			if (rgbs == null || rgbs.length != 3) {
				return false;
			}
			for (int j = 0; j < rgbs.length; j++) {
				if (rgbs[j] > 255 || rgbs[j] < 0) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checkVipName(String vipName) {
		if (StringUtils.isEmpty(vipName)) {
			return false;
		} else if (vipName.length() < Vip.MIN_LENGTH_WxNickName || vipName.length() > Vip.MAX_LENGTH_WxNickName) {
			return false;
		}
		return true;
	}

	/** openid暂时以此格式检查 */
	public static boolean checkOpenID(String openID) {
		if (StringUtils.isEmpty(openID)) {
			return false;
		}
		return pOpenID.matcher(openID).matches();
	}
	
	public static boolean checkUnionID(String unionID) {
		if (StringUtils.isEmpty(unionID)) {
			return false;
		}
		return pUnionID.matcher(unionID).matches();
	}
	
	public static boolean checkCardCode(String cardCode) {
		if (StringUtils.isEmpty(cardCode)) {
			return false;
		}
		if(cardCode.length() > VipCardCode.SN_LENGTH) {
			return false;
		}
		Matcher m = pNum.matcher(cardCode);
		return m.matches();
	}
}
