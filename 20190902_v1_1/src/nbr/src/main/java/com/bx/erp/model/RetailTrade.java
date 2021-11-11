package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.shared.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONNull;

/**
 * listSlave1:零售单商品  <br />品
 * listSlave2:促销及优惠计算过程。有促销则其中有促销计算过程，（会员）有使用优惠券，则有优惠券使用记录  <br />
 * listSlave3:零售单优惠券使用表（最多只会使用一张优惠券）  <br />
 */
public class RetailTrade extends BaseModel {
	private Log logger = LogFactory.getLog(RetailTrade.class);
	private static final long serialVersionUID = 1L;
	public static final RetailTradeField field = new RetailTradeField();

	public static final int Max_LengthLogo = 128;
	public static final int LENGTH_SN = 24;
	/**
	 * 根据编号搜索零售单时，输入的关键字不必是长度LENGTH_SN，其范围为[Min_LengthSN_Query, Max_LengthSN_Query]
	 */
	public static final int Min_LengthSN_Query = 10;
	public static final int Max_LengthSN_Query = 26;
	public static final int Min_ID = 0;
	public static final int Min_LocalSN = 0;
	public static final int Min_paymentType = 1;
	public static final int Max_paymentType = 7;
	public static final int Max_LengthPaymentAccount = 20;
	public static final int Max_LengthRemark = 20;
	public static final double Min_Amount = 0.000000d;
	public static final int Max_LengthQueryKeyword = 32;
	public static final int Max_LengthConsumerOpenID = 100;

	public static final String FIELD_ERROR_status = "创建零售单时其状态必须为" + EnumStatusRetailTrade.ESRT_Normal.getIndex() + "或者" + EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex();
	public static final String FIELD_ERROR_sn = "单据流水号长度必须是" + LENGTH_SN + ",且只能LS开头、中间数字、可_1结尾";
	public static final String FIELD_ERROR_sn_ForQuery = "单据流水号长度不能大于" + Max_LengthSN_Query + ",且不能够小于" + Min_LengthSN_Query + "个字符，不能含LS及数字外的字符";
	public static final String FIELD_ERROR_LOCALSN = "localSN不能小于" + Min_LocalSN;
	public static final String FIELD_ERROR_VipID = "VipID不能小于" + Min_ID;
	public static final String FIELD_ERROR_POSID = "POSID不能小于" + Min_ID;
	public static final String FIELD_ERROR_paymentType = "paymentType不能小于" + Min_paymentType + ",不能大于" + Max_paymentType;
	public static final String FIELD_ERROR_StaffID = "StaffID不能小于" + Min_ID;
	public static final String FIELD_ERROR_logo = "logo不能为空，长度不能大于" + Max_LengthLogo;
	public static final String FIELD_ERROR_paymentAccount = "paymentAccount不能为空，不能大于" + Max_LengthPaymentAccount + "个字符";
	public static final String FIELD_ERROR_remark = "备注不能大于" + Max_LengthRemark + "个字符";
	public static final String FIELD_ERROR_sourceID = "sourceID必须为-1";
	public static final String FIELD_ERROR_Amount = "%s金额必须大于等于" + Min_Amount;
	public static final String FIELD_ERROR_smallSheetID = "smallSheetID必须大于" + Min_ID;
	public static final String FIELD_ERROR_queryKeyword = "输入内容的长度不能大于" + Max_LengthQueryKeyword + "个字符,且首尾不能有空格";
	public static final String FIELD_ERROR_amountTotal = "每种支付方式对应的钱加起来要等于应收款";
	public static final String FIELD_ERROR_payment = "使用%s的支付方式，那么%s应大于" + Min_Amount;
	public static final String FIELD_ERROR_notPayment = "不使用%s的支付方式，那么%s应为" + Min_Amount;
	public static final String FIELD_ERROR_consumerOpenID = "openID必须为NULL或长度小于等于" + Max_LengthConsumerOpenID;
	public static final String FIELD_ERROR_shopID = "shopID必须大于0";

	protected String sn;

	protected int vipID;

	protected int localSN;

	protected int pos_ID;

	protected String logo;

	protected Date saleDatetime;

	protected int staffID;

	protected int paymentType;

	protected String paymentAccount;

	protected int status;

	protected String remark;

	protected int sourceID;

	/** 零售单应收款，可能经过了促销优惠的计算 */
	protected double amount;

	protected double amountCash;

	protected double amountAlipay;

	protected double amountWeChat;

	protected double amount1;

	protected double amount2;

	protected double amount3;

	protected double amount4;

	protected double amount5;

	protected int smallSheetID;

	protected String aliPayOrderSN;

	protected String wxOrderSN;

	protected String wxTradeNO;

	protected String wxRefundNO;

	protected String wxRefundDesc;

	protected String wxRefundSubMchID;

	/** 非数据库字段，RN零售单时，excludeReturned = 0 包含退货单，excludeReturned = 1不包含退货单 */
	protected int excludeReturned;

	protected Date datetimeStart;

	protected Date datetimeEnd;

	protected int bRequestFromApp; // 判断是app请求还是web请求，1表示app请求，默认0表示nbr请求

	protected ErrorInfo errorInfo;

	protected double couponAmount;

	protected String consumerOpenID;
	
	protected int shopID;
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public String getConsumerOpenID() {
		return consumerOpenID;
	}

	public void setConsumerOpenID(String consumerOpenID) {
		this.consumerOpenID = consumerOpenID;
	}

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}

	public int getbRequestFromApp() {
		return bRequestFromApp;
	}

	public void setbRequestFromApp(int bRequestFromApp) {
		this.bRequestFromApp = bRequestFromApp;
	}

	public Date getDatetimeStart() {
		return datetimeStart;
	}

	public Date getDatetimeEnd() {
		return datetimeEnd;
	}

	public void setDatetimeStart(Date datetimeStart) {
		this.datetimeStart = datetimeStart;
	}

	public void setDatetimeEnd(Date datetimeEnd) {
		this.datetimeEnd = datetimeEnd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getWxTradeNO() {
		return wxTradeNO;
	}

	public void setWxTradeNO(String wxTradeNO) {
		this.wxTradeNO = wxTradeNO;
	}

	public String getWxRefundNO() {
		return wxRefundNO;
	}

	public void setWxRefundNO(String wxRefundNO) {
		this.wxRefundNO = wxRefundNO;
	}

	public String getWxRefundDesc() {
		return wxRefundDesc;
	}

	public void setWxRefundDesc(String wxRefundDesc) {
		this.wxRefundDesc = wxRefundDesc;
	}

	public String getWxRefundSubMchID() {
		return wxRefundSubMchID;
	}

	public void setWxRefundSubMchID(String wxRefundSubMchID) {
		this.wxRefundSubMchID = wxRefundSubMchID;
	}

	public String getAliPayOrderSN() {
		return aliPayOrderSN;
	}

	public void setAliPayOrderSN(String aliPayOrderSN) {
		this.aliPayOrderSN = aliPayOrderSN;
	}

	public String getWxOrderSN() {
		return wxOrderSN;
	}

	public void setWxOrderSN(String wxOrderSN) {
		this.wxOrderSN = wxOrderSN;
	}

	public int getSmallSheetID() {
		return smallSheetID;
	}

	public void setSmallSheetID(int smallSheetID) {
		this.smallSheetID = smallSheetID;
	}

	public double getAmountCash() {
		return amountCash;
	}

	public void setAmountCash(double amountCash) {
		this.amountCash = amountCash;
	}

	public double getAmountAlipay() {
		return amountAlipay;
	}

	public void setAmountAlipay(double amountAlipay) {
		this.amountAlipay = amountAlipay;
	}

	public double getAmountWeChat() {
		return amountWeChat;
	}

	public void setAmountWeChat(double amountWeChat) {
		this.amountWeChat = amountWeChat;
	}

	public double getAmount1() {
		return amount1;
	}

	public void setAmount1(double amount1) {
		this.amount1 = amount1;
	}

	public double getAmount2() {
		return amount2;
	}

	public void setAmount2(double amount2) {
		this.amount2 = amount2;
	}

	public double getAmount3() {
		return amount3;
	}

	public void setAmount3(double amount3) {
		this.amount3 = amount3;
	}

	public double getAmount4() {
		return amount4;
	}

	public void setAmount4(double amount4) {
		this.amount4 = amount4;
	}

	public double getAmount5() {
		return amount5;
	}

	public void setAmount5(double amount5) {
		this.amount5 = amount5;
	}

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLocalSN() {
		return localSN;
	}

	public void setLocalSN(int localSN) {
		this.localSN = localSN;
	}

	public int getPos_ID() {
		return pos_ID;
	}

	public void setPos_ID(int pos_ID) {
		this.pos_ID = pos_ID;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Date getSaleDatetime() {
		return saleDatetime;
	}

	public void setSaleDatetime(Date saleDatetime) {
		this.saleDatetime = saleDatetime;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSourceID() {
		return sourceID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public int getExcludeReturned() {
		return excludeReturned;
	}

	public void setExcludeReturned(int iInvolveReturned) {
		this.excludeReturned = iInvolveReturned;
	}

	@Override
	public String toString() {
		return "RetailTrade [sn=" + sn + ", vipID=" + vipID + ", localSN=" + localSN + ", pos_ID=" + pos_ID + ", logo=" + logo + ", saleDatetime=" + saleDatetime + ", staffID=" + staffID + ", paymentType=" + paymentType
				+ ", paymentAccount=" + paymentAccount + ", status=" + status + ", remark=" + remark + ", sourceID=" + sourceID + ", amount=" + amount + ", amountCash=" + amountCash + ", amountAlipay=" + amountAlipay + ", amountWeChat="
				+ amountWeChat + ", amount1=" + amount1 + ", amount2=" + amount2 + ", amount3=" + amount3 + ", amount4=" + amount4 + ", amount5=" + amount5 + ", smallSheetID=" + smallSheetID + ", aliPayOrderSN=" + aliPayOrderSN
				+ ", wxOrderSN=" + wxOrderSN + ", wxTradeNO=" + wxTradeNO + ", wxRefundNO=" + wxRefundNO + ", wxRefundDesc=" + wxRefundDesc + ", wxRefundSubMchID=" + wxRefundSubMchID + ", excludeReturned=" + excludeReturned
				+ ", datetimeStart=" + datetimeStart + ", datetimeEnd=" + datetimeEnd + ", bRequestFromApp=" + bRequestFromApp + ", errorInfo=" + errorInfo + ", couponAmount=" + couponAmount + ", consumerOpenID=" + consumerOpenID
				+ ", staffName=" + staffName + ", listSlave1=" + listSlave1 + ", listSlave2=" + listSlave2 + ", listSlave3=" + listSlave3 + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() {
		RetailTrade obj = new RetailTrade();
		obj.setID(ID);
		obj.setSn(sn);
		obj.setVipID(vipID);
		obj.setLocalSN(localSN);
		obj.setPos_ID(pos_ID);
		obj.setLogo(logo);
		obj.setSaleDatetime((Date) saleDatetime.clone());
		obj.setSyncDatetime((Date) syncDatetime.clone());
		obj.setDatetimeStart(datetimeStart == null ? null : (Date) datetimeStart.clone());
		obj.setDatetimeEnd(datetimeEnd == null ? null : (Date) datetimeEnd.clone());
		obj.setStaffID(staffID);
		obj.setPaymentType(paymentType);
		obj.setPaymentAccount(paymentAccount);
		obj.setStatus(status);
		obj.setRemark(remark);
		obj.setSourceID(sourceID);
		obj.setAmount(amount);
		obj.setAmount1(amount1);
		obj.setAmount2(amount2);
		obj.setAmount3(amount3);
		obj.setAmount4(amount4);
		obj.setAmount5(amount5);
		obj.setAmountAlipay(amountAlipay);
		obj.setAmountCash(amountCash);
		obj.setAmountWeChat(amountWeChat);
		obj.setSmallSheetID(smallSheetID);
		obj.setAliPayOrderSN(aliPayOrderSN);
		obj.setWxOrderSN(wxOrderSN);
		obj.setWxRefundDesc(this.getWxRefundDesc());
		obj.setWxRefundNO(wxRefundNO);
		obj.setWxRefundSubMchID(wxRefundSubMchID);
		obj.setWxTradeNO(wxTradeNO);
		obj.setbRequestFromApp(bRequestFromApp);
		obj.setReturnObject(returnObject);
		obj.setCouponAmount(couponAmount);
		obj.setConsumerOpenID(consumerOpenID);
		obj.setShopID(shopID);
		if (this.getListSlave1() != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : listSlave1) {
				RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) o;
				list.add(retailTradeCommodity.clone());
			}
			obj.setListSlave1(list);
		}

		if (listSlave2 != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : listSlave2) {
				RetailTradePromoting retailTradePromoting = (RetailTradePromoting) o;
				list.add(retailTradePromoting.clone());
			}
			obj.setListSlave2(list);
		}

		if (listSlave3 != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object o : listSlave3) {
				RetailTradeCoupon retailTradeCoupon = (RetailTradeCoupon) o;
				list.add(retailTradeCoupon.clone());
			}
			obj.setListSlave3(list);
		}

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		RetailTrade rt = (RetailTrade) arg0;
		if ((ignoreIDInComparision == true ? true : (rt.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rt.getSn().equals(this.getSn()) && printComparator(field.getFIELD_NAME_sn()) //
				&& rt.getLocalSN() == localSN && printComparator(field.getFIELD_NAME_localSN()) //
				&& rt.getPos_ID() == pos_ID && printComparator(field.getFIELD_NAME_pos_ID()) //
				&& rt.getLogo().equals(logo) && printComparator(field.getFIELD_NAME_logo()) //
				&& DatetimeUtil.compareDate(rt.getSaleDatetime(), saleDatetime) && printComparator(field.getFIELD_NAME_saleDatetime()) //
				&& rt.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID()) //
				&& rt.getPaymentType() == paymentType && printComparator(field.getFIELD_NAME_paymentType()) //
				&& rt.getPaymentAccount().equals(paymentAccount) && printComparator(field.getFIELD_NAME_paymentAccount()) //
				&& rt.getStatus() == status && printComparator(field.getFIELD_NAME_status())//
				&& rt.getRemark().equals(remark) && printComparator(field.getFIELD_NAME_remark()) //
				&& rt.getSourceID() == sourceID && printComparator(field.getFIELD_NAME_sourceID()) //
				// && DatetimeUtil.compareDate(rt.getSyncDatetime(), this.getSyncDatetime()) &&
				// printComparator(getFIELD_NAME_syncDatetime()) //
				&& Math.abs(GeneralUtil.sub(rt.getAmount(), amount)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount()) //
				&& Math.abs(GeneralUtil.sub(rt.getAmountWeChat(), amountWeChat)) < TOLERANCE && printComparator(field.getFIELD_NAME_amountWeChat())//
				&& Math.abs(GeneralUtil.sub(rt.getAmountCash(), amountCash)) < TOLERANCE && printComparator(field.getFIELD_NAME_amountCash())//
				&& Math.abs(GeneralUtil.sub(rt.getAmountAlipay(), amountAlipay)) < TOLERANCE && printComparator(field.getFIELD_NAME_amountAlipay())//
				&& Math.abs(GeneralUtil.sub(rt.getAmount1(), amount1)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount1())//
				&& Math.abs(GeneralUtil.sub(rt.getAmount2(), amount2)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount2())//
				&& Math.abs(GeneralUtil.sub(rt.getAmount3(), amount3)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount3())//
				&& Math.abs(GeneralUtil.sub(rt.getAmount4(), amount4)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount4())//
				&& Math.abs(GeneralUtil.sub(rt.getAmount5(), amount5)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount5())//
				&& rt.getSmallSheetID() == smallSheetID && printComparator(field.getFIELD_NAME_smallSheetID())//
				&& (rt.getAliPayOrderSN() == null ? aliPayOrderSN == null : rt.getAliPayOrderSN().equals(aliPayOrderSN == null ? "" : aliPayOrderSN)) //
				&& printComparator(field.getFIELD_NAME_aliPayOrderSN())//
				&& (rt.getWxOrderSN() == null ? wxOrderSN == null : rt.getWxOrderSN().equals(wxOrderSN == null ? "" : wxOrderSN)) //
				&& printComparator(field.getFIELD_NAME_wxOrderSN())//
				&& (rt.getWxRefundDesc() == null ? wxRefundDesc == null : rt.getWxRefundDesc().equals(wxRefundDesc == null ? "" : wxRefundDesc)) //
				&& printComparator(field.getFIELD_NAME_wxRefundDesc())//
				&& (rt.getWxRefundNO() == null ? wxRefundNO == null : rt.getWxRefundNO().equals(wxRefundNO == null ? "" : wxRefundNO)) //
				&& printComparator(field.getFIELD_NAME_wxRefundNO()) //
				&& (rt.getWxRefundSubMchID() == null ? wxRefundSubMchID == null : rt.getWxRefundSubMchID().equals(wxRefundSubMchID == null ? "" : wxRefundSubMchID)) //
				&& printComparator(field.getFIELD_NAME_wxRefundSubMchID())//
				&& (rt.getWxTradeNO() == null ? wxTradeNO == null : rt.getWxTradeNO().equals(wxTradeNO == null ? "" : wxTradeNO))//
				&& printComparator(field.getFIELD_NAME_wxTradeNO())//
				&& Math.abs(GeneralUtil.sub(rt.getCouponAmount(), couponAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_couponAmount())//
				&& (rt.getConsumerOpenID() == null ? consumerOpenID == null : rt.getConsumerOpenID().equals(consumerOpenID)) && printComparator(field.getFIELD_NAME_consumerOpenID())//
		// ErrorInfo不作检查
		) {
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && rt.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && rt.getListSlave1() != null) {
					if (rt.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && rt.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && rt.getListSlave1() != null) {
					if (listSlave1.size() != rt.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) listSlave1.get(i);
						retailTradeCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < rt.getListSlave1().size(); j++) {
							RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(j);
							if (retailTradeCommodity.getCommodityID() == rtc.getCommodityID()) {
								exist = true;
								if (retailTradeCommodity.compareTo(rtc) != 0) {
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
	public Map<String, Object> getCreateParamEx(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		RetailTrade rt = (RetailTrade) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_vipID(), rt.getVipID());
		params.put(field.getFIELD_NAME_sn(), rt.getSn() == null ? "" : rt.getSn());
		params.put(field.getFIELD_NAME_localSN(), rt.getLocalSN());
		params.put(field.getFIELD_NAME_pos_ID(), rt.getPos_ID());
		params.put(field.getFIELD_NAME_logo(), rt.getLogo() == null ? "" : rt.getLogo());
		params.put(field.getFIELD_NAME_saleDatetime(), rt.getSaleDatetime());
		params.put(field.getFIELD_NAME_staffID(), rt.getStaffID());
		params.put(field.getFIELD_NAME_paymentType(), rt.getPaymentType());
		params.put(field.getFIELD_NAME_paymentAccount(), rt.getPaymentAccount() == null ? "" : rt.getPaymentAccount());
		params.put(field.getFIELD_NAME_status(), rt.getStatus());
		params.put(field.getFIELD_NAME_remark(), rt.getRemark() == null ? "" : rt.getRemark());
		params.put(field.getFIELD_NAME_sourceID(), rt.getSourceID());
		params.put(field.getFIELD_NAME_amount(), rt.getAmount());
		params.put(field.getFIELD_NAME_amountCash(), rt.getAmountCash());
		params.put(field.getFIELD_NAME_amountAlipay(), rt.getAmountAlipay());
		params.put(field.getFIELD_NAME_amountWeChat(), rt.getAmountWeChat());
		params.put(field.getFIELD_NAME_amount1(), rt.getAmount1());
		params.put(field.getFIELD_NAME_amount2(), rt.getAmount2());
		params.put(field.getFIELD_NAME_amount3(), rt.getAmount3());
		params.put(field.getFIELD_NAME_amount4(), rt.getAmount4());
		params.put(field.getFIELD_NAME_amount5(), rt.getAmount5());
		params.put(field.getFIELD_NAME_smallSheetID(), rt.getSmallSheetID());
		params.put(field.getFIELD_NAME_aliPayOrderSN(), rt.getAliPayOrderSN() == null ? "" : rt.getAliPayOrderSN());
		params.put(field.getFIELD_NAME_wxOrderSN(), rt.getWxOrderSN() == null ? "" : rt.getWxOrderSN());
		params.put(field.getFIELD_NAME_wxRefundDesc(), rt.getWxRefundDesc() == null ? "" : rt.getWxRefundDesc());
		params.put(field.getFIELD_NAME_wxRefundNO(), rt.getWxRefundNO() == null ? "" : rt.getWxRefundNO());
		params.put(field.getFIELD_NAME_wxRefundSubMchID(), rt.getWxRefundSubMchID() == null ? "" : rt.getWxRefundSubMchID());
		params.put(field.getFIELD_NAME_wxTradeNO(), rt.getWxTradeNO() == null ? "" : rt.getWxTradeNO());
		params.put(field.getFIELD_NAME_couponAmount(), rt.getCouponAmount());
		params.put(field.getFIELD_NAME_consumerOpenID(), rt.getConsumerOpenID());
		params.put(field.getFIELD_NAME_shopID(), rt.getShopID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		// SimpleDateFormat sdf = new
		// SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		RetailTrade rt = (RetailTrade) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_shopID(), rt.getShopID());
		params.put(field.getFIELD_NAME_vipID(), rt.getVipID());
		params.put(field.getFIELD_NAME_queryKeyword(), rt.getQueryKeyword());
		params.put(field.getFIELD_NAME_localSN(), rt.getLocalSN());
		params.put(field.getFIELD_NAME_pos_ID(), rt.getPos_ID());
		params.put(field.getFIELD_NAME_datetimeStart(), rt.getDatetimeStart());
		params.put(field.getFIELD_NAME_datetimeEnd(), rt.getDatetimeEnd());
		params.put(field.getFIELD_NAME_paymentType(), rt.getPaymentType());
		params.put(field.getFIELD_NAME_staffID(), rt.getStaffID());
		params.put(field.getFIELD_NAME_iPageIndex(), rt.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), rt.getPageSize());
		params.put(field.getFIELD_NAME_excludeReturned(), rt.getExcludeReturned());
		switch (iUseCaseID) {
		case BaseBO.CASE_RetailTrade_RetrieveNFromApp:
			params.put(field.getFIELD_NAME_bRequestFromApp(), Integer.valueOf(1));
			break;
		case BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb:
		case BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb:
		default:
			params.put(field.getFIELD_NAME_bRequestFromApp(), Integer.valueOf(0));
			break;
		}
		return params;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseModel parse1(String s) {
		try {
			JSONObject joRT = JSONObject.parseObject(s);
			RetailTrade rt = (RetailTrade) doParse1(joRT);
			if (rt == null) {
				return null;
			}

			// 解析零售单商品
			JSONArray rtcArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave1());//
			if (rtcArr != null) {
				RetailTradeCommodity rtc = new RetailTradeCommodity();
				List<BaseModel> listRtc = rtc.parseN(rtcArr.toString());
				if (listRtc == null) {
					return null;
				}
				rt.setListSlave1(listRtc); // 非常关键！！
			}

			Object object = joRT.get(field.getFIELD_NAME_listSlave3());
			if (object != null && !"null".equals(object) && !JSONNull.getInstance().equals(object)) {
				JSONArray retailTradeCouponArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave3());
				if (retailTradeCouponArr != null) {
					RetailTradeCoupon retailTradeCoupon = new RetailTradeCoupon();
					List<BaseModel> retailTradeCouponList = (List<BaseModel>) retailTradeCoupon.parseN(retailTradeCouponArr);
					if (retailTradeCouponList == null) {
						return null;
					}
					rt.setListSlave3(retailTradeCouponList);
				}
			}

			// 解析零售单促销
			JSONArray rtpArr = joRT.getJSONArray(field.getFIELD_NAME_listSlave2());
			if (rtpArr != null) {
				List<BaseModel> listRtp = new ArrayList<BaseModel>();
				for (int i = 0; i < rtpArr.size(); i++) {
					RetailTradePromoting rtp = new RetailTradePromoting();
					JSONObject jsonObject = rtpArr.getJSONObject(i);
					rtp = (RetailTradePromoting) rtp.parse1(jsonObject.toString());
					if (rtp == null) {
						return null;
					}
					listRtp.add(rtp);
				}
				rt.setListSlave2(listRtp);
			}

			return rt;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BaseModel doParse1(com.alibaba.fastjson.JSONObject jo) {

		try {
			ID = (int) jo.get(field.getFIELD_NAME_ID());
			sn = jo.getString(field.getFIELD_NAME_sn());
			vipID = jo.getInteger(field.getFIELD_NAME_vipID());
			localSN = (int) jo.get(field.getFIELD_NAME_localSN());
			pos_ID = (int) jo.get(field.getFIELD_NAME_pos_ID());
			logo = jo.getString(field.getFIELD_NAME_logo());
			returnObject = jo.getInteger(field.getFIELD_NAME_returnObject());
			returnSalt = jo.getIntValue(field.getFIELD_NAME_returnSalt());
			excludeReturned = jo.getInteger(field.getFIELD_NAME_excludeReturned());
			bRequestFromApp = jo.getInteger(field.getFIELD_NAME_bRequestFromApp());
			// if (DatetimeUtil.toDate(jo.getString("saleDatetime")) != null) {
			// saleDatetime = DatetimeUtil.toDate(jo.getString("saleDatetime"));
			// } else if (DatetimeUtil.toDateOld(jo.getString("saleDatetime")) != null) {
			// saleDatetime = DatetimeUtil.toDateOld(jo.getString("saleDatetime"));
			// } else {
			// throw new RuntimeException("无法解析该日期");
			// }

			String tmp = jo.getString(field.getFIELD_NAME_saleDatetime());
			if (!StringUtils.isEmpty(tmp)) {
				saleDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (saleDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_saleDatetime() + "=" + tmp);
				}
			}

			tmp = jo.getString(field.getFIELD_NAME_syncDatetime());
			if (!StringUtils.isEmpty(tmp)) {
				syncDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (syncDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_syncDatetime() + "=" + tmp);
				}
			}

			tmp = jo.getString(field.getFIELD_NAME_datetimeStart());
			if (!StringUtils.isEmpty(tmp)) {
				datetimeStart = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (datetimeStart == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_datetimeStart() + "=" + tmp);
				}
			}

			tmp = jo.getString(field.getFIELD_NAME_datetimeEnd());
			if (!StringUtils.isEmpty(tmp)) {
				datetimeEnd = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (datetimeEnd == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_datetimeEnd() + "=" + tmp);
				}
			}
			staffID = (int) jo.get(field.getFIELD_NAME_staffID());
			paymentType = (int) jo.get(field.getFIELD_NAME_paymentType());
			paymentAccount = jo.getString(field.getFIELD_NAME_paymentAccount());
			status = (int) jo.get(field.getFIELD_NAME_status());
			remark = jo.getString(field.getFIELD_NAME_remark());
			sourceID = (int) jo.get(field.getFIELD_NAME_sourceID());
			// if (jo.getString("syncDatetime") != null) {
			// syncDatetime = DatetimeUtil.toDate(jo.getString("syncDatetime"));
			// }
			amount = Double.valueOf(jo.getString(field.getFIELD_NAME_amount()));
			amountCash = Double.valueOf(jo.getString(field.getFIELD_NAME_amountCash()));
			amountAlipay = Double.valueOf(jo.getString(field.getFIELD_NAME_amountAlipay()));
			amountWeChat = Double.valueOf(jo.getString(field.getFIELD_NAME_amountWeChat()));
			amount1 = Double.valueOf(jo.getString(field.getFIELD_NAME_amount1()));
			amount2 = Double.valueOf(jo.getString(field.getFIELD_NAME_amount2()));
			amount3 = Double.valueOf(jo.getString(field.getFIELD_NAME_amount3()));
			amount4 = Double.valueOf(jo.getString(field.getFIELD_NAME_amount4()));
			amount5 = Double.valueOf(jo.getString(field.getFIELD_NAME_amount5()));
			smallSheetID = Integer.valueOf(jo.getString(field.getFIELD_NAME_smallSheetID()));
			couponAmount = Double.valueOf(jo.getString(field.getFIELD_NAME_couponAmount()));
			consumerOpenID = jo.getString(field.getFIELD_NAME_consumerOpenID());
			shopID = jo.getInteger(field.getFIELD_NAME_shopID());
			String jsonErrorInfo = jo.getString(field.getFIELD_NAME_errorInfo());
			if (jsonErrorInfo != null) {
				errorInfo = new ErrorInfo();
				errorInfo = (ErrorInfo) errorInfo.parse1(jsonErrorInfo);
			}

			Object tmpForPOS = null;
			tmpForPOS = jo.get(field.getFIELD_NAME_aliPayOrderSN());
			if (tmpForPOS != null && !"".equals(tmpForPOS)) {
				aliPayOrderSN = (String) tmpForPOS;
			}
			//
			tmpForPOS = jo.get(field.getFIELD_NAME_wxOrderSN());
			if (tmpForPOS != null && !"".equals(tmpForPOS)) {
				wxOrderSN = (String) tmpForPOS;
			}
			//
			tmpForPOS = jo.get(field.getFIELD_NAME_wxRefundDesc());
			if (tmpForPOS != null && !"".equals(tmpForPOS)) {
				wxRefundDesc = (String) tmpForPOS;
			}
			//
			tmpForPOS = jo.get(field.getFIELD_NAME_wxRefundNO());
			if (tmpForPOS != null && !"".equals(tmpForPOS)) {
				wxRefundNO = (String) tmpForPOS;
			}
			//
			tmpForPOS = jo.get(field.getFIELD_NAME_wxRefundSubMchID());
			if (tmpForPOS != null && !"".equals(tmpForPOS)) {
				wxRefundSubMchID = (String) tmpForPOS;
			}
			//
			tmpForPOS = jo.get(field.getFIELD_NAME_wxTradeNO());
			if (tmpForPOS != null && !"".equals(tmpForPOS)) {
				wxTradeNO = (String) tmpForPOS;
			}
			
			// 解析零售单商品
			JSONArray aliJsonArray = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			if (aliJsonArray != null) {
				RetailTradeCommodity rtc = new RetailTradeCommodity();
				List<BaseModel> listRtc = rtc.parseN(aliJsonArray.toString());
				if (listRtc == null) {
					return null;
				}
				listSlave1 = listRtc; 
			}

			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> retailTradeList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.parseArray(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				RetailTrade c = new RetailTrade();
				c.parse1(jsonObject1.toString());
				retailTradeList.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return retailTradeList;
	}

	@Override
	public String toJson(BaseModel bm) {
		String json = JSON.toJSONString(bm);

		return json;
	}

	@Override
	public String toJson(List<BaseModel> bm) {
		String json = JSON.toJSONString(bm);

		return json;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		default:
			// if (printCheckField(field.getFIELD_NAME_vipID(), FIELD_ERROR_VipID, sbError)
			// && !FieldFormat.checkID(vipID)) {
			// return sbError.toString();
			// } //...RetailTrade注释了相关check，RetailTradeTest也应注释相关测试，否则报错，现已注释

			if (printCheckField(field.getFIELD_NAME_sn(), FIELD_ERROR_sn, sbError) && !FieldFormat.checkRetailTradeSN(sn)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_localSN(), FIELD_ERROR_LOCALSN, sbError) && localSN <= Min_LocalSN) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_pos_ID(), FIELD_ERROR_POSID, sbError) && !FieldFormat.checkID(pos_ID)) {
				return sbError.toString();
			}

			// pos端正在做默认小票格式相关的jira，做完后放开
			// //...RetailTrade注释了相关check，RetailTradeTest也应注释相关测试，否则报错，现已注释
			// if (printCheckField(field.getFIELD_NAME_logo(), FIELD_ERROR_logo, sbError) &&
			// !StringUtils.isEmpty(logo) && logo.length() <= Max_LengthLogo) {
			// } else {
			// return sbError.toString();
			// }

			if (printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_paymentType(), FIELD_ERROR_paymentType, sbError) && paymentType < Min_paymentType || paymentType > Max_paymentType) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && status != EnumStatusRetailTrade.ESRT_Normal.getIndex() && status != EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
				return sbError.toString();
			}

			// if (printCheckField(field.getFIELD_NAME_paymentAccount(),
			// FIELD_ERROR_paymentAccount, sbError) &&
			// !StringUtils.isEmpty(paymentAccount)//
			// && paymentAccount.length() <= Max_LengthPaymentAccount) {
			// } else {
			// return sbError.toString();
			// } //...RetailTrade注释了相关check，RetailTradeTest也应注释相关测试，否则报错，现已注释

			if (!StringUtils.isEmpty(remark)) {
				if (printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && remark.length() > Max_LengthRemark) {
					return sbError.toString();
				}
			}

			if (printCheckField(field.getFIELD_NAME_sourceID(), FIELD_ERROR_sourceID, sbError) && sourceID != BaseAction.INVALID_ID && sourceID <= 0) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount()), sbError) && amount < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amountCash(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amountCash()), sbError) && amountCash < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amountAlipay(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amountAlipay()), sbError) && amountAlipay < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amountWeChat(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amountWeChat()), sbError) && amountWeChat < Min_Amount) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_amount1(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount1()), sbError) && amount1 < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount2(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount2()), sbError) && amount2 < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount3(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount3()), sbError) && amount3 < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount4(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount4()), sbError) && amount4 < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount5(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount5()), sbError) && amount5 < Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_couponAmount(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_couponAmount()), sbError) && couponAmount < Min_Amount) {
				return sbError.toString();
			}

			if (consumerOpenID != null) {
				if (printCheckField(field.getFIELD_NAME_consumerOpenID(), String.format(FIELD_ERROR_consumerOpenID, field.getFIELD_NAME_consumerOpenID()), sbError) && consumerOpenID.length() > Max_LengthConsumerOpenID) {
					return sbError.toString();
				}
			}
			
			if (printCheckField(field.getFIELD_NAME_shopID(), String.format(FIELD_ERROR_shopID, field.getFIELD_NAME_shopID()), sbError) && !FieldFormat.checkID(shopID)) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_smallSheetID(), FIELD_ERROR_smallSheetID, sbError) && !FieldFormat.checkID(smallSheetID)) {
				return sbError.toString();
			}

			// 检查：每种支付方式对应的钱加起来=应收款
			if (printCheckField(field.getFIELD_NAME_amount(), FIELD_ERROR_amountTotal, sbError) //
					&& Math.abs(GeneralUtil.sub(amount, GeneralUtil.sumN(amountCash, amountAlipay, amountWeChat, amount1, amount2, amount3, amount4, amount5))) >= TOLERANCE) {
				return sbError.toString();
			}

			// 如果只有现金支付，则可以为0.000000d，如果混合支付带有现金支付，那么现金支付不可为0.000000d
			if (printCheckField(field.getFIELD_NAME_amountCash(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amountCash(), field.getFIELD_NAME_amountCash()), sbError) && paymentType != 1 && (paymentType & 1) == 1
					&& amountCash <= Min_Amount) {
				return sbError.toString();
			}

			// 检查：支付方式为2，对应的钱应该>0.000000d
			if (printCheckField(field.getFIELD_NAME_amountAlipay(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amountAlipay(), field.getFIELD_NAME_amountAlipay()), sbError) && (paymentType & 2) == 2
					&& amountAlipay <= Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amountWeChat(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amountWeChat(), field.getFIELD_NAME_amountWeChat()), sbError) && (paymentType & 4) == 4
					&& amountWeChat <= Min_Amount) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_amount1(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount1(), field.getFIELD_NAME_amount1()), sbError) && (paymentType & 8) == 8 && amount1 <= Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount2(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount2(), field.getFIELD_NAME_amount2()), sbError) && (paymentType & 16) == 16 && amount2 <= Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount3(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount3(), field.getFIELD_NAME_amount3()), sbError) && (paymentType & 32) == 32 && amount3 <= Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount4(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount4(), field.getFIELD_NAME_amount4()), sbError) && (paymentType & 64) == 64 && amount4 <= Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount5(), String.format(FIELD_ERROR_payment, field.getFIELD_NAME_amount5(), field.getFIELD_NAME_amount5()), sbError) && (paymentType & 128) == 128 && amount5 <= Min_Amount) {
				return sbError.toString();
			}

			// 如果没有这种支付方式，那么这种支付金额要为0；
			if (printCheckField(field.getFIELD_NAME_amountCash(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amountCash(), field.getFIELD_NAME_amountCash()), sbError) && (paymentType & 1) == 0 && amountCash != Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amountAlipay(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amountAlipay(), field.getFIELD_NAME_amountAlipay()), sbError) && (paymentType & 2) == 0
					&& amountAlipay != Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amountWeChat(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amountWeChat(), field.getFIELD_NAME_amountWeChat()), sbError) && (paymentType & 4) == 0
					&& amountWeChat != Min_Amount) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_amount1(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount1(), field.getFIELD_NAME_amount1()), sbError) && (paymentType & 8) == 0 && amount1 != Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount2(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount2(), field.getFIELD_NAME_amount2()), sbError) && (paymentType & 16) == 0 && amount2 != Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount3(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount3(), field.getFIELD_NAME_amount3()), sbError) && (paymentType & 32) == 0 && amount3 != Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount4(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount4(), field.getFIELD_NAME_amount4()), sbError) && (paymentType & 64) == 0 && amount4 != Min_Amount) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_amount5(), String.format(FIELD_ERROR_notPayment, field.getFIELD_NAME_amount5(), field.getFIELD_NAME_amount5()), sbError) && (paymentType & 128) == 0 && amount5 != Min_Amount) {
				return sbError.toString();
			}

			break;
		}

		return "";

	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_RetailTrade_RetrieveNFromApp:
			if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_sn_ForQuery, sbError) && !FieldFormat.checkRetailTradeRetrieveNBySN(queryKeyword)) {
				return sbError.toString();
			}
			break;
		case BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb:
		case BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb:
		default:
			if (vipID != Min_ID) {
				if (printCheckField(field.getFIELD_NAME_vipID(), FIELD_ERROR_VipID, sbError) && !FieldFormat.checkID(vipID)) {
					return sbError.toString();
				}
			}

			if (printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && !FieldFormat.checkRetailTradeRetrieveNByQueryKeyword(queryKeyword)) {
				return sbError.toString();
			}

			if (localSN != Min_LocalSN) {
				if (printCheckField(field.getFIELD_NAME_localSN(), FIELD_ERROR_LOCALSN, sbError) && localSN < Min_LocalSN) {
					return sbError.toString();
				}
			}

			if (pos_ID != Min_ID) {
				if (printCheckField(field.getFIELD_NAME_pos_ID(), FIELD_ERROR_POSID, sbError) && !FieldFormat.checkID(pos_ID)) {
					return sbError.toString();
				}
			}

			if (paymentType != 0) {
				if (printCheckField(field.getFIELD_NAME_paymentType(), FIELD_ERROR_paymentType, sbError) && Min_paymentType > paymentType) {
					return sbError.toString();
				}
			}

			if (staffID != Min_ID) {
				if (printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) {
					return sbError.toString();
				}
			}
			break;
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	public enum EnumStatusRetailTrade {
		ESRT_Hold("Hold", 0), //
		ESRT_Normal("Normal", 1), //
		ESRT_IncludeDeletedCommodity("Include Deleted Commodity", 2);

		private String name;
		private int index;

		private EnumStatusRetailTrade(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusRetailTrade c : EnumStatusRetailTrade.values()) {
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

	public static int DEFINE_SET_RetailTradeID(int id) {
		return id;
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex();
	}

	/** 判断当前零售单是否全部使用现金支付 */
	public boolean payViaCashOnly() {
		return paymentType == 1;
	}

	/** 判断当前零售单是否全部使用微信支付 */
	public boolean payViaWechatOnly() {
		return paymentType == 4;
	}
	
	public static RetailTrade fetchRetailTradeFromResultSet(List<List<BaseModel>> bmList) {
		RetailTrade rt = null;
		if (bmList.size() == 1) {
			rt = (RetailTrade) bmList.get(0);
		} else if (bmList.size() == 2) { // SP返回最多2个结果集
			rt = (RetailTrade) bmList.get(0).get(0);
		}

		return rt;
	}
	
	/**
	 * 计算rt中的商品促销后（如果有）的退货价
	 * @param rt Pos传递到NBR的零售单。但rt中的应付款促销后（如果有）使用优惠券前（如果有）的应付款
	 */
	@SuppressWarnings("unchecked")
	public void calculateReturnPrice(RetailTrade rt) {
		List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
		
		double originalTotalAmount = 0.000000d;
		for (RetailTradeCommodity rtc : retailTradeCommodityList) {
			originalTotalAmount = GeneralUtil.sum(originalTotalAmount, GeneralUtil.mul(rtc.getNO(), rtc.getPriceOriginal()));
		}
		logger.debug("促销前及未使用优惠券时的总价：" + originalTotalAmount);
		
		for (RetailTradeCommodity rtc : retailTradeCommodityList) {
			// 将退货价设置为最开始的价格
			rtc.setPriceReturn(rtc.getPriceOriginal());
			// 判断有无使用过促销。RetailTradePromotingFlow中，若能搜索到关键字“优惠券”，则其为记录优惠券的计算过程，否则，为促销的计算过程
			if (rt.getListSlave2() != null && rt.getListSlave2().size() > 0) {
				List<RetailTradePromotingFlow> promoting = (List<RetailTradePromotingFlow>) ((RetailTradePromoting)rt.getListSlave2().get(0)).getListSlave1();
				if (promoting.size() > 0) {
					if (rt.getListSlave3() != null && rt.getListSlave3().size() > 0) {
						if (promoting.size() == 1) {
							break; // 只使用了优惠券，没使用促销
						}
					}
					// 说明使用优惠券前参与了促销,需要将退货价变更改为使用了促销后使用优惠券前的退货价
					// 原来的计算过程是 rtc.getPriceReturn() * rtc.getNO() / getOriginalTotalAmount() * rt.getAmount() / rtc.getNO()，即rtc.getPriceReturn() / getOriginalTotalAmount() * rt.getAmount()
		            // 现改为 (rtc.getPriceReturn() * rt.getAmount()) / getOriginalTotalAmount()
		            // 原因是，按照先乘后除的计算顺序可以减少计算误差
					double dReturnPrice = GeneralUtil.div(GeneralUtil.mul(rtc.getPriceReturn(), rt.getAmount()), originalTotalAmount, 6);
					rtc.setPriceReturn(Double.valueOf(GeneralUtil.formatToCalculate(dReturnPrice)));
					logger.debug("商品" + rtc.getCommodityID() + "：" + rtc.getCommodityName() + "使用促销后的退货价是：" + rtc.getPriceReturn());
				}
			}
		}
	}
}
