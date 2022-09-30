package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class RetailTradeAggregation extends BaseModel {
	private static final long serialVersionUID = 7515841128731689249L;
	public static final RetailTradeAggregationField field = new RetailTradeAggregationField();

	public static final int Min_ID = 0;
	public static final int Min_TradeNO = 0;
	public static final double Min_ReserveAmount = 0.000000d;
	public static final double Max_ReserveAmount = 10000.000000d;
	public static final double Min_Amount = 0.000000d;

	public static final String FIELD_ERROR_StaffID = "StaffID不能小于" + Min_ID;
	public static final String FIELD_ERROR_PosID = "PosID不能小于" + Min_ID;
	public static final String FIELD_ERROR_TradeNO = "交易单数不能小于" + Min_TradeNO;
	public static final String FIELD_ERROR_ReserveAmount = "准备金在[" + Min_ReserveAmount + "," + Max_ReserveAmount + "]之间";
	public static final String FIELD_ERROR_workTimeStartAndEnd = "上班时间不能比下班时间晚";
	public static final String FIELD_ERROR_workTimeStart = "上班时间不能为null";
	public static final String FIELD_ERROR_workTimeEnd = "下班时间不能为null";
	public static final String FIELD_ERROR_Amount = "%s金额必须大于等于" + Min_Amount;
	public static final String FIELD_ERROR_amountTotal = "每种支付方式对应的钱加起来要等于营业额";


	private int staffID;

	private int posID;

	private Date workTimeStart;

	private Date workTimeEnd;

	private int tradeNO;

	private double amount;

	private double reserveAmount;

	private double cashAmount;

	private double wechatAmount;

	private double alipayAmount;

	private double amount1;

	private double amount2;

	private double amount3;

	private double amount4;

	private double amount5;

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getPosID() {
		return posID;
	}

	public void setPosID(int posID) {
		this.posID = posID;
	}

	public Date getWorkTimeStart() {
		return workTimeStart;
	}

	public void setWorkTimeStart(Date workTimeStart) {
		this.workTimeStart = workTimeStart;
	}

	public Date getWorkTimeEnd() {
		return workTimeEnd;
	}

	public void setWorkTimeEnd(Date workTimeEnd) {
		this.workTimeEnd = workTimeEnd;
	}

	public int getTradeNO() {
		return tradeNO;
	}

	public void setTradeNO(int tradeNO) {
		this.tradeNO = tradeNO;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getReserveAmount() {
		return reserveAmount;
	}

	public void setReserveAmount(double reserveAmount) {
		this.reserveAmount = reserveAmount;
	}

	public double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public double getWechatAmount() {
		return wechatAmount;
	}

	public void setWechatAmount(double wechatAmount) {
		this.wechatAmount = wechatAmount;
	}

	public double getAlipayAmount() {
		return alipayAmount;
	}

	public void setAlipayAmount(double alipayAmount) {
		this.alipayAmount = alipayAmount;
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

	@Override
	public String toString() {
		return "RetailTradeAggregation [staffID=" + staffID + ", posID=" + posID + ", workTimeStart=" + workTimeStart + ", workTimeEnd=" + workTimeEnd + ", tradeNO=" + tradeNO + ", amount=" + amount + ", reserveAmount=" + reserveAmount
				+ ", cashAmount=" + cashAmount + ", wechatAmount=" + wechatAmount + ", alipayAmount=" + alipayAmount + ", amount1=" + amount1 + ", amount2=" + amount2 + ", amount3=" + amount3 + ", amount4=" + amount4 + ", amount5="
				+ amount5 + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		RetailTradeAggregation rta = new RetailTradeAggregation();
		rta.setID(ID);
		rta.setStaffID(staffID);
		rta.setPosID(posID);
		rta.setWorkTimeStart(workTimeStart == null ? null : (Date) workTimeStart.clone());
		rta.setWorkTimeEnd(workTimeEnd == null ? null : (Date) workTimeEnd.clone());
		rta.setTradeNO(tradeNO);
		rta.setAmount(amount);
		rta.setReserveAmount(reserveAmount);
		rta.setCashAmount(cashAmount);
		rta.setWechatAmount(wechatAmount);
		rta.setAlipayAmount(alipayAmount);
		rta.setAmount1(amount1);
		rta.setAmount2(amount2);
		rta.setAmount3(amount3);
		rta.setAmount4(amount4);
		rta.setAmount5(amount5);

		return rta;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		RetailTradeAggregation rta = (RetailTradeAggregation) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_staffID(), rta.getStaffID());
		params.put(field.getFIELD_NAME_posID(), rta.getPosID());
		params.put(field.getFIELD_NAME_workTimeStart(), rta.getWorkTimeStart());
		params.put(field.getFIELD_NAME_workTimeEnd(), rta.getWorkTimeEnd());
		params.put(field.getFIELD_NAME_tradeNO(), rta.getTradeNO());
		params.put(field.getFIELD_NAME_amount(), rta.getAmount());
		params.put(field.getFIELD_NAME_reserveAmount(), rta.getReserveAmount());
		params.put(field.getFIELD_NAME_cashAmount(), rta.getCashAmount());
		params.put(field.getFIELD_NAME_wechatAmount(), rta.getWechatAmount());
		params.put(field.getFIELD_NAME_alipayAmount(), rta.getAlipayAmount());
		params.put(field.getFIELD_NAME_amount1(), rta.getAmount1());
		params.put(field.getFIELD_NAME_amount2(), rta.getAmount2());
		params.put(field.getFIELD_NAME_amount3(), rta.getAmount3());
		params.put(field.getFIELD_NAME_amount4(), rta.getAmount4());
		params.put(field.getFIELD_NAME_amount5(), rta.getAmount5());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Map<String, Object> params = new HashMap<String, Object>();
		RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) bm;
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_staffID(), retailTradeAggregation.getStaffID());
			break;
		}
		return params;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}

		// ... TODO hard code应替换为FIELD_NAME_xxxxxxx
		RetailTradeAggregation rta = (RetailTradeAggregation) arg0;
		if ((ignoreIDInComparision == true ? true : (rta.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rta.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID()) //
				&& rta.getPosID() == posID && printComparator(field.getFIELD_NAME_posID()) //
				&& rta.getTradeNO() == tradeNO && printComparator(field.getFIELD_NAME_tradeNO()) //
				&& DatetimeUtil.compareDate(rta.getWorkTimeStart(), workTimeStart) && printComparator(field.getFIELD_NAME_workTimeStart()) //
				&& DatetimeUtil.compareDate(rta.getWorkTimeEnd(), workTimeEnd) && printComparator(field.getFIELD_NAME_workTimeEnd()) //
				&& Math.abs(GeneralUtil.sub(rta.getAmount(), amount)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount()) //
				&& Math.abs(GeneralUtil.sub(rta.getReserveAmount(), this.getReserveAmount())) < TOLERANCE && printComparator(field.getFIELD_NAME_reserveAmount()) //
				&& Math.abs(GeneralUtil.sub(rta.getCashAmount(), cashAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_cashAmount()) //
				&& Math.abs(GeneralUtil.sub(rta.getWechatAmount(), wechatAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_wechatAmount()) //
				&& Math.abs(GeneralUtil.sub(rta.getAlipayAmount(), alipayAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_alipayAmount()) //
				&& Math.abs(GeneralUtil.sub(rta.getAmount1(), amount1)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount1()) //
				&& Math.abs(GeneralUtil.sub(rta.getAmount2(), amount2)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount2()) //
				&& Math.abs(GeneralUtil.sub(rta.getAmount3(), amount3)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount3()) //
				&& Math.abs(GeneralUtil.sub(rta.getAmount4(), amount4)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount4()) //
				&& Math.abs(GeneralUtil.sub(rta.getAmount5(), amount5)) < TOLERANCE && printComparator(field.getFIELD_NAME_amount5()) //
		) {

			return 0;
		}

		return -1;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) //
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_posID(), FIELD_ERROR_PosID, sbError) && !FieldFormat.checkID(posID)) //
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_tradeNO(), FIELD_ERROR_TradeNO, sbError) && tradeNO < 0) //
		{
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_reserveAmount(), FIELD_ERROR_ReserveAmount, sbError) && (reserveAmount < Min_ReserveAmount || reserveAmount > Max_ReserveAmount)) {
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_workTimeStart(), FIELD_ERROR_workTimeStart, sbError) &&  workTimeStart == null) {
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_workTimeEnd(), FIELD_ERROR_workTimeEnd, sbError) &&  workTimeEnd == null) {
			return sbError.toString();
		}

		if (printCheckField(field.getFIELD_NAME_workTimeStart() + "," + field.getFIELD_NAME_workTimeEnd(), FIELD_ERROR_workTimeStartAndEnd, sbError) && workTimeStart.getTime() >= workTimeEnd.getTime()) {
			return sbError.toString();
		}
		//
//		pos只做退货操作后交班，收银金额会小于0
//		if (printCheckField(field.getFIELD_NAME_amount(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_amount()), sbError) && amount < Min_Amount) {
//			return sbError.toString();
//		}
//		if (printCheckField(field.getFIELD_NAME_amount(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_cashAmount()), sbError) && cashAmount < Min_Amount) {
//			return sbError.toString();
//		}
//		if (printCheckField(field.getFIELD_NAME_amount(), String.format(FIELD_ERROR_Amount, field.getFIELD_NAME_wechatAmount()), sbError) && wechatAmount < Min_Amount) {
//			return sbError.toString();
//		}
		if (printCheckField(field.getFIELD_NAME_amount(), FIELD_ERROR_amountTotal, sbError) //
				&& Math.abs(GeneralUtil.sub(amount, GeneralUtil.sumN(cashAmount, alipayAmount, wechatAmount, amount1, amount2, amount3, amount4, amount5))) >= TOLERANCE) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			this.staffID = jo.getInt(field.getFIELD_NAME_staffID());
			this.posID = jo.getInt(field.getFIELD_NAME_posID());
			String tmpWorkTimeStart = jo.getString(field.getFIELD_NAME_workTimeStart());
			if (!"".equals(tmpWorkTimeStart)) {
				workTimeStart = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpWorkTimeStart);
				if (workTimeStart == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_workTimeStart() + "=" + tmpWorkTimeStart);
				}
			}
			String tmpWorkTimeEnd = jo.getString(field.getFIELD_NAME_workTimeEnd());
			if (!"".equals(tmpWorkTimeEnd)) {
				workTimeEnd = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpWorkTimeEnd);
				if (workTimeEnd == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_workTimeEnd() + "=" + tmpWorkTimeEnd);
				}
			}
			this.tradeNO = jo.getInt(field.getFIELD_NAME_tradeNO());
			this.amount = jo.getDouble(field.getFIELD_NAME_amount());
			this.reserveAmount = jo.getDouble(field.getFIELD_NAME_reserveAmount());
			this.cashAmount = jo.getDouble(field.getFIELD_NAME_cashAmount());
			this.wechatAmount = jo.getDouble(field.getFIELD_NAME_wechatAmount());
			this.alipayAmount = jo.getDouble(field.getFIELD_NAME_alipayAmount());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
