package com.bx.erp.model.trade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionShopScope;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Promotion extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final PromotionField field = new PromotionField();

	public static final int Max_LengthName = 32;
	public static final int Max_excecutionDiscount = 1;
	public static final int MAX_ExcecutionThreshold = 10000;
	public static final int MAX_ExcecutionAmount = 10000;

	public static final String FIELD_ERROR_startDatetime = "活动开始时间必须至少从明天开始！";
	public static final String FIELD_ERROR_excecutionThreshold = "阀值只能在1~" + MAX_ExcecutionThreshold + "之间";
	public static final String FIELD_ERROR_excecutionAmount = "满减金额只能在1~" + MAX_ExcecutionAmount + "之间";
	public static final String FIELD_ERROR_excecutionAmount_excecutionThreshold = "阀值需大于或等于满减金额！";
	public static final String FIELD_ERROR_excecutionDiscount = "折扣需大于0，小于等于" + Max_excecutionDiscount;
	public static final String FIELD_ERROR_datetimeStart_datetimeEnd = "开始时间必须在结束时间之前！";
	public static final String FIELD_ERROR_type = "不是满减满折类型,折扣类型必须是" + EnumTypePromotion.ETP_DecreaseOnAmount.getIndex() + "或" + EnumTypePromotion.ETP_DiscountOnAmount.getIndex() + "！";
	public static final String FIELD_ERROR_name = "名称长度不能大于" + Max_LengthName + "位且不能为null或空字符串！";
	public static final String FIELD_ERROR_scope = "参与的商品的范围值必须是" + EnumPromotionScope.EPS_AllCommodities.getIndex() + "或" + EnumPromotionScope.EPS_SpecifiedCommodities.getIndex() + "！";
	public static final String FIELD_ERROR_shopScope = "参与的门店的范围值必须是" + EnumPromotionShopScope.EPS_AllShops.getIndex() + "或" + EnumPromotionShopScope.EPS_SpecifiedShops.getIndex() + "！";
	public static final String FIELD_ERROR_status = "状态必须是" + EnumStatusPromotion.ESP_Active.getIndex() + "或" + EnumStatusPromotion.ESP_Deleted.getIndex() + "！";
	public static final String FIELD_ERROR_statusForRetrieveN = "状态必须是" + EnumStatusPromotion.ESP_Active.getIndex() + "或" + EnumStatusPromotion.ESP_Deleted.getIndex() + "或" + BaseAction.INVALID_STATUS + "！";
	public static final String FIELD_ERROR_nameForRetrieveN = "名称长度不能大于" + Max_LengthName + "位";
	public static final String FIELD_ERROR_staff = "店员ID必须大于0";
	public static final String FIELD_ERROR_Paging = "PageIndex和PageSize不符合POS请求参数";
	public static final String FIELD_ERROR_subStatusOfStatus = "POS端不可查找其他无关状态的促销";
	public static final String FIELD_ERROR_queryKeyword = "关键字的查询条件必须为空";
	public static final String FIELD_ERROR_posID = "posID只能为" + BaseAction.INVALID_ID + "或者大于0";

	/** 返回所有状态为0的促销活动 */
	public static final int ACTIVE = 0;
	/** 返回所有状态为0且未开始的促销活动 */
	public static final int ACTIVE_ButNotYetStarted = 10;
	/** 返回所有状态为0且进行中的促销活动 */
	public static final int ACTIVE_And_Working = 11;
	/** 返回所有状态为0且已结束的促销活动 */
	public static final int ACTIVE_ButEnded = 12;
	/** 查询进行中还有将要进行的 */
	public static final int WORKING_And_ToWork = 13;

	protected String name;

	protected int type;

	protected int status;

	protected Date datetimeStart;

	protected Date datetimeEnd;

	protected double excecutionThreshold;

	protected double excecutionAmount;

	protected double excecutionDiscount;

	protected int scope;

	protected int staff;

	protected String sn;
	
	protected int shopScope;
	
	public int getShopScope() {
		return shopScope;
	}

	public void setShopScope(int shopScope) {
		this.shopScope = shopScope;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDatetimeStart() {
		return datetimeStart;
	}

	public void setDatetimeStart(Date datetimeStart) {
		this.datetimeStart = datetimeStart;
	}

	public Date getDatetimeEnd() {
		return datetimeEnd;
	}

	public void setDatetimeEnd(Date datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
	}

	public double getExcecutionThreshold() {
		return excecutionThreshold;
	}

	public void setExcecutionThreshold(double excecutionThreshold) {
		this.excecutionThreshold = excecutionThreshold;
	}

	public double getExcecutionAmount() {
		return excecutionAmount;
	}

	public void setExcecutionAmount(double excecutionAmount) {
		this.excecutionAmount = excecutionAmount;
	}

	public double getExcecutionDiscount() {
		return excecutionDiscount;
	}

	public void setExcecutionDiscount(double excecutionDiscount) {
		this.excecutionDiscount = excecutionDiscount;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public int getStaff() {
		return staff;
	}

	public void setStaff(int staff) {
		this.staff = staff;
	}

	private List<PromotionScope> listPromotionScope;

	public List<PromotionScope> getListPromotionScope() {
		return listPromotionScope;
	}

	public void setListPromotionScope(List<PromotionScope> listPromotionScope) {
		this.listPromotionScope = listPromotionScope;
	}

	protected int retailTradeNO;
	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public int getRetailTradeNO() {
		return retailTradeNO;
	}

	public void setRetailTradeNO(int retailTradeNO) {
		this.retailTradeNO = retailTradeNO;
	}

	// 用于存放指定商品促销的指定商品ID
	protected String commodityIDs;
	
	// 用于存放指定门店的指定门店ID
	protected String shopIDs;
	
	public String getShopIDs() {
		return shopIDs;
	}

	public void setShopIDs(String shopIDs) {
		this.shopIDs = shopIDs;
	}

	public String getCommodityIDs() {
		return commodityIDs;
	}

	public void setCommodityIDs(String commodityID) {
		this.commodityIDs = commodityID;
	}

	protected int subStatusOfStatus;

	public int getSubStatusOfStatus() {
		return subStatusOfStatus;
	}

	public void setSubStatusOfStatus(int subStatusOfStatus) {
		this.subStatusOfStatus = subStatusOfStatus;
	}

	@Override
	public String toString() {
		return "Promotion [name=" + name + ", type=" + type + ", status=" + status + ", datetimeStart=" + datetimeStart + ", datetimeEnd=" + datetimeEnd + ", excecutionThreshold=" + excecutionThreshold + ", excecutionAmount="
				+ excecutionAmount + ", excecutionDiscount=" + excecutionDiscount + ", scope=" + scope + ", staff=" + staff + ", sn=" + sn + ", listPromotionScope=" + listPromotionScope + ", retailTradeNO=" + retailTradeNO + ", staffName="
				+ staffName + ", listSlave1=" + listSlave1 + ", ID=" + ID + "]";
	}

	public String toPromotionString() {
		return "促销名称 = " + name + ", 起始日期 = " + datetimeStart + ", 结束日期 = " + datetimeEnd + "参与的商品的范围 0=全部商品，1=部分商品= " + scope + ", 满减阀值= " + excecutionThreshold + ", 满减金额 type为0时有效= " + excecutionAmount + ", 满减折扣type为0时有效= "
				+ excecutionDiscount + "活动类型 = " + type;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		Promotion p = (Promotion) arg0;
		if ((ignoreIDInComparision == true ? true : p.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& name.equals(p.getName()) && printComparator(field.getFIELD_NAME_name())//
				&& p.getStatus() == status && printComparator(field.getFIELD_NAME_status())//
				&& p.getType() == type && printComparator(field.getFIELD_NAME_type())//
				&& DatetimeUtil.compareDate(p.getDatetimeStart(), datetimeStart) && printComparator(field.getFIELD_NAME_datetimeStart())//
				&& DatetimeUtil.compareDate(p.getDatetimeEnd(), datetimeEnd) && printComparator(field.getFIELD_NAME_datetimeEnd())//
				&& Math.abs(GeneralUtil.sub(p.getExcecutionThreshold(), excecutionThreshold)) < TOLERANCE && printComparator(field.getFIELD_NAME_excecutionThreshold())//
				&& (p.getType() == 1 ? true : Math.abs(GeneralUtil.sub(p.getExcecutionAmount(), excecutionAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_excecutionAmount()))//
				&& (p.getType() == 0 ? true : Math.abs(GeneralUtil.sub(p.getExcecutionDiscount(), excecutionDiscount)) < TOLERANCE && printComparator(field.getFIELD_NAME_excecutionDiscount()))//
				&& p.getScope() == scope && printComparator(field.getFIELD_NAME_scope())//
				&& p.getStaff() == staff && printComparator(field.getFIELD_NAME_staff())//
		// 不比较创建时间和修改时间
		) {
			//
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && p.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && p.getListSlave1() != null) {
					if (p.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && p.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && p.getListSlave1() != null) {
					if (listSlave1.size() != p.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						PromotionScope promotionScope = (PromotionScope) listSlave1.get(i);
						promotionScope.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < p.getListSlave1().size(); j++) {
							PromotionScope rtc = (PromotionScope) p.getListSlave1().get(j);
							if (promotionScope.getCommodityID() == rtc.getCommodityID()) {
								exist = true;
								if (promotionScope.compareTo(rtc) != 0) {
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
				//
				if (listSlave2 == null && p.getListSlave2() == null) {
					return 0;
				}
				if (listSlave2 == null && p.getListSlave2() != null) {
					if (p.getListSlave2().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave2 != null && p.getListSlave2() == null) {
					if (listSlave2.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave2 != null && p.getListSlave2() != null) {
					if (listSlave2.size() != p.getListSlave2().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave2.size(); i++) {
						PromotionShopScope promotionShopScope = (PromotionShopScope) listSlave2.get(i);
						promotionShopScope.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < p.getListSlave2().size(); j++) {
							PromotionShopScope rtc = (PromotionShopScope) p.getListSlave2().get(j);
							if (promotionShopScope.getShopID() == rtc.getShopID()) {
								exist = true;
								if (promotionShopScope.compareTo(rtc) != 0) {
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
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		Promotion p = (Promotion) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_name(), p.getName() == null ? "" : p.getName());
		params.put(field.getFIELD_NAME_status(), p.getStatus());
		params.put(field.getFIELD_NAME_type(), p.getType());
		params.put(field.getFIELD_NAME_datetimeStart(), p.getDatetimeStart());
		params.put(field.getFIELD_NAME_datetimeEnd(), p.getDatetimeEnd());
		params.put(field.getFIELD_NAME_excecutionThreshold(), p.getExcecutionThreshold());
		params.put(field.getFIELD_NAME_excecutionAmount(), p.getExcecutionAmount());
		params.put(field.getFIELD_NAME_excecutionDiscount(), p.getExcecutionDiscount());
		params.put(field.getFIELD_NAME_scope(), p.getScope());
		params.put(field.getFIELD_NAME_shopScope(), p.getShopScope());
		params.put(field.getFIELD_NAME_staff(), p.getStaff());
		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		if (bm == null) {
			throw new RuntimeException("传入的参数 bm不能为null！");
		}

		Promotion p = (Promotion) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), p.getID());
		params.put(field.getFIELD_NAME_name(), p.getName() == null ? "" : p.getName());
		params.put(field.getFIELD_NAME_status(), p.getStatus());
		params.put(field.getFIELD_NAME_type(), p.getType());
		params.put(field.getFIELD_NAME_datetimeStart(), p.getDatetimeStart());
		params.put(field.getFIELD_NAME_datetimeEnd(), p.getDatetimeEnd());
		params.put(field.getFIELD_NAME_excecutionThreshold(), p.getExcecutionThreshold());
		params.put(field.getFIELD_NAME_excecutionAmount(), p.getExcecutionAmount());
		params.put(field.getFIELD_NAME_excecutionDiscount(), p.getExcecutionDiscount());
		params.put(field.getFIELD_NAME_scope(), p.getScope());
		params.put(field.getFIELD_NAME_staff(), p.getStaff());
		return params;
	}

	@Override
	public Map<String, Object> getDeleteParamEx(int iUseCaseID, final int objID) {
		return getSimpleParam(iUseCaseID, objID);
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		Promotion p = (Promotion) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_subStatusOfStatus(), p.getSubStatusOfStatus());
		params.put(field.getFIELD_NAME_status(), p.getStatus());
		params.put(field.getFIELD_NAME_queryKeyword(), p.getQueryKeyword() == null ? "" : p.getQueryKeyword());
		params.put(field.getFIELD_NAME_iPageIndex(), p.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), p.getPageSize());
		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		// TODO Auto-generated method stub
		return super.getRetrieve1Param(iUseCaseID, bm);
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Promotion p = new Promotion();
		p.setID(ID);
		p.setName(name);
		p.setStatus(status);
		p.setType(type);
		p.setDatetimeStart(datetimeStart);
		p.setDatetimeEnd(datetimeEnd);
		p.setExcecutionThreshold(excecutionThreshold);
		p.setExcecutionAmount(excecutionAmount);
		p.setExcecutionDiscount(excecutionDiscount);
		p.setScope(scope);
		p.setStaff(staff);
		p.setCreateDatetime(createDatetime);
		p.setUpdateDatetime(updateDatetime);
		if (listPromotionScope != null) {
			List<PromotionScope> list = new ArrayList<PromotionScope>();
			for (PromotionScope promotionScope : listPromotionScope) {
				list.add((PromotionScope) promotionScope.clone());
			}
			p.setListPromotionScope(list);
		}
		if (listSlave1 != null) {
			List<PromotionScope> list = new ArrayList<PromotionScope>();
			for (Object o : listSlave1) {
				PromotionScope promotionScope = (PromotionScope) o;
				list.add((PromotionScope) promotionScope.clone());
			}
			p.setListSlave1(list);
		}
		p.setIgnoreIDInComparision(ignoreIDInComparision);
		return p;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (!EnumTypePromotion.inBound(type)) {
			return FIELD_ERROR_type;
		}
		if (StringUtils.isEmpty(name) || name.length() > Max_LengthName) {
			return FIELD_ERROR_name;
		}
		if (!EnumPromotionScope.inBound(scope)) {
			return FIELD_ERROR_scope;
		}
		if (!EnumPromotionShopScope.inBound(shopScope)) {
			return FIELD_ERROR_shopScope;
		}
		if (!EnumStatusPromotion.inBound(status)) {
			return FIELD_ERROR_status;
		}
		if (printCheckField(field.getFIELD_NAME_staff(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(staff)) {
			return sbError.toString();
		}
		switch (iUseCaseID) {
		case BaseBO.CASE_SpecialResultVerification:
			if (!DatetimeUtil.isAfterDate(datetimeEnd, datetimeStart, 0)) {
				return FIELD_ERROR_datetimeStart_datetimeEnd;
			}
			//
			// Date tomorrow = DatetimeUtil.get2ndDayStart(new Date());
			// if (datetimeStart.compareTo(tomorrow) <= 0) {
			// return FIELD_ERROR_startDatetime;
			// }
			if (excecutionThreshold > MAX_ExcecutionThreshold || excecutionThreshold < 0 || Math.abs(GeneralUtil.sub(excecutionThreshold, 0)) < TOLERANCE) {
				return FIELD_ERROR_excecutionThreshold;
			}
			if (type == EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()) {
				if (excecutionAmount > MAX_ExcecutionAmount || excecutionAmount < 0 || Math.abs(GeneralUtil.sub(excecutionAmount, 0)) < TOLERANCE) {
					return FIELD_ERROR_excecutionAmount;
				}
				if (excecutionAmount > excecutionThreshold) {
					return FIELD_ERROR_excecutionAmount_excecutionThreshold;
				}
			}
			if (type == EnumTypePromotion.ETP_DiscountOnAmount.getIndex()) {
				if (excecutionDiscount > Max_excecutionDiscount || excecutionDiscount < 0 || Math.abs(GeneralUtil.sub(excecutionDiscount, 0)) < TOLERANCE) {
					return FIELD_ERROR_excecutionDiscount;
				}
			}
			return "";
		default:
			if (!DatetimeUtil.isAfterDate(datetimeEnd, datetimeStart, 0)) {
				return FIELD_ERROR_datetimeStart_datetimeEnd;
			}
			//
			Date tomorrow = DatetimeUtil.getDays(new Date(), 1);
			if (datetimeStart.compareTo(tomorrow) <= 0) {
				return FIELD_ERROR_startDatetime;
			}
			if (excecutionThreshold > MAX_ExcecutionThreshold || excecutionThreshold < 0 || Math.abs(GeneralUtil.sub(excecutionThreshold, 0)) < TOLERANCE) {
				return FIELD_ERROR_excecutionThreshold;
			}
			if (type == EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()) {
				if (excecutionAmount > MAX_ExcecutionAmount || excecutionAmount < 0 || Math.abs(GeneralUtil.sub(excecutionAmount, 0)) < TOLERANCE) {
					return FIELD_ERROR_excecutionAmount;
				}
				if (excecutionAmount > excecutionThreshold) {
					return FIELD_ERROR_excecutionAmount_excecutionThreshold;
				}
			}
			if (type == EnumTypePromotion.ETP_DiscountOnAmount.getIndex()) {
				if (excecutionDiscount > Max_excecutionDiscount || excecutionDiscount < 0 || Math.abs(GeneralUtil.sub(excecutionDiscount, 0)) < TOLERANCE) {
					return FIELD_ERROR_excecutionDiscount;
				}
			}
			return "";
		}
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
			return sbError.toString();
		}
		if (!EnumTypePromotion.inBound(type)) {
			return FIELD_ERROR_type;
		}
		if (StringUtils.isEmpty(name) || name.length() > Max_LengthName) {
			return FIELD_ERROR_name;
		}
		if (!EnumPromotionScope.inBound(scope)) {
			return FIELD_ERROR_scope;
		}
		if (!EnumStatusPromotion.inBound(status)) {
			return FIELD_ERROR_status;
		}
		if (printCheckField(field.getFIELD_NAME_staff(), FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(staff)) {
			return sbError.toString();
		}
		if (!DatetimeUtil.isAfterDate(datetimeEnd, datetimeStart, 0)) {
			return FIELD_ERROR_datetimeStart_datetimeEnd;
		}
		//
		Date tomorrow = DatetimeUtil.getDays(new Date(), 1);
		if (datetimeStart.compareTo(tomorrow) <= 0) {
			return FIELD_ERROR_startDatetime;
		}
		if (excecutionThreshold > MAX_ExcecutionThreshold || excecutionThreshold < 0 || Math.abs(GeneralUtil.sub(excecutionThreshold, 0)) < TOLERANCE) {
			return FIELD_ERROR_excecutionThreshold;
		}
		if (type == EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()) {
			if (excecutionAmount > MAX_ExcecutionAmount || excecutionAmount < 0 || Math.abs(GeneralUtil.sub(excecutionAmount, 0)) < TOLERANCE) {
				return FIELD_ERROR_excecutionAmount;
			}
			if (excecutionAmount > excecutionThreshold) {
				return FIELD_ERROR_excecutionAmount_excecutionThreshold;
			}
		}
		if (type == EnumTypePromotion.ETP_DiscountOnAmount.getIndex()) {
			if (excecutionDiscount > Max_excecutionDiscount || excecutionDiscount < 0 || Math.abs(GeneralUtil.sub(excecutionDiscount, 0)) < TOLERANCE) {
				return FIELD_ERROR_excecutionDiscount;
			}
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_posID(), FIELD_ERROR_posID, sbError) && !(posID > 0 || posID == BaseAction.INVALID_ID)) {
			return sbError.toString();
		}
		if (posID == BaseAction.INVALID_ID) { // 网页请求
			if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_statusForRetrieveN, sbError) && !EnumStatusPromotion.inBoundForRetrieveN(status)) //
			{
				return sbError.toString();
			}
			if (!StringUtils.isEmpty(queryKeyword)) { // queryKeyword可以代表搜索的因子为：商品名称、促销单号。其中商品名称最长，所以queryKeyword最长能传Max_LengthName位
				if (this.printCheckField(field.getFIELD_NAME_name(), FIELD_ERROR_nameForRetrieveN, sbError) && queryKeyword.length() > Max_LengthName) //
				{
					return sbError.toString();
				}
			}
		} else { // pos请求
			if (printCheckField(field.getFIELD_NAME_pageIndex(), FIELD_ERROR_Paging, sbError) && printCheckField(field.getFIELD_NAME_pageSize(), FIELD_ERROR_Paging, sbError)
					&& !(pageIndex == BaseAction.PAGE_StartIndex && pageSize == BaseAction.PAGE_SIZE_Infinite)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_subStatusOfStatus, sbError) && printCheckField(field.getFIELD_NAME_subStatusOfStatus(), FIELD_ERROR_subStatusOfStatus, sbError)
					&& !(status == EnumStatusPromotion.ESP_Active.getIndex() && subStatusOfStatus == 13)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && !StringUtils.isEmpty(queryKeyword)) {
				return sbError.toString();
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
		return super.checkDelete(iUseCaseID);
	}

	public enum EnumStatusPromotion {
		ESP_Active("Active", 0), // 有效
		ESP_Deleted("Deleted", 1);// 已经删除

		private String name;
		private int index;

		private EnumStatusPromotion(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBoundForRetrieveN(int index) {
			if (index != BaseAction.INVALID_STATUS && index != ESP_Active.getIndex() && index != ESP_Deleted.getIndex()) {
				return false;
			}

			return true;
		}

		public static boolean inBound(int index) {
			if (index < ESP_Active.getIndex() || index > ESP_Deleted.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumStatusPromotion c : EnumStatusPromotion.values()) {
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

	/**
	 * ESP_Active类型的促销,根据查询的条件,又细分为几种
	 */
	public enum EnumSubStatusPromotion {
		ESSP_ToStart("ToStart", 10), // 未开始的促销
		ESSP_Promoting("Promoting", 11),// 正在促销中
		ESSP_Terminated("Terminated", 12),// 已经结束
		ESSP_ToStartAndPromoting("ToStartAndPromoting", 13);// 正在促销中和还未开始促销的

		private String name;
		private int index;

		private EnumSubStatusPromotion(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumSubStatusPromotion c : EnumSubStatusPromotion.values()) {
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

	public enum EnumTypePromotion {
		ETP_DecreaseOnAmount("DecreaseOnAmount", 0), // 满减
		ETP_DiscountOnAmount("DiscountOnAmount", 1);// 满折

		private String name;
		private int index;

		private EnumTypePromotion(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBound(int index) {
			if (index < ETP_DecreaseOnAmount.getIndex() || index > ETP_DiscountOnAmount.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumStatusPromotion c : EnumStatusPromotion.values()) {
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
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			name = jo.getString(field.getFIELD_NAME_name());
			status = jo.getInt(field.getFIELD_NAME_status());
			type = jo.getInt(field.getFIELD_NAME_type());
			sn = jo.getString(field.getFIELD_NAME_sn());
			String tmp = jo.getString(field.getFIELD_NAME_datetimeStart());
			if (!"".equals(tmp)) {
				datetimeStart = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (datetimeStart == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_datetimeStart() + "=" + tmp);
				}
			}
			tmp = jo.getString(field.getFIELD_NAME_datetimeEnd());
			if (!"".equals(tmp)) {
				datetimeEnd = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (datetimeEnd == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_datetimeEnd() + "=" + tmp);
				}
			}
			excecutionThreshold = jo.getDouble(field.getFIELD_NAME_excecutionThreshold());
			excecutionAmount = jo.getDouble(field.getFIELD_NAME_excecutionAmount());
			excecutionDiscount = jo.getDouble(field.getFIELD_NAME_excecutionDiscount());
			scope = jo.getInt(field.getFIELD_NAME_scope());
			staff = jo.getInt(field.getFIELD_NAME_staff());
			tmp = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmp)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmp);
				}
			}
			tmp = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmp)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmp);
				}
			}
			queryKeyword = jo.getString(field.getFIELD_NAME_queryKeyword());
			staffName = jo.getString(field.getFIELD_NAME_staffName());
			retailTradeNO = jo.getInt(field.getFIELD_NAME_retailTradeNO());
			// 查询范围表的集合
			List<PromotionScope> listPS = new ArrayList<>();
			JSONArray o = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < o.size(); i++) {
				JSONObject object = o.getJSONObject(i);
				PromotionScope ps = (PromotionScope) new PromotionScope().parse1(object.toString());
				listPS.add(ps);
			}
			listSlave1 = listPS;
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> protmotionList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Promotion promotion = new Promotion();
				promotion.doParse1(jsonObject);
				protmotionList.add(promotion);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return protmotionList;
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_PromotionCacheSize.getIndex();
	}
}
