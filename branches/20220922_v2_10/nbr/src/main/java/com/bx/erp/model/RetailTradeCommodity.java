package com.bx.erp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;


public class RetailTradeCommodity extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final RetailTradeCommodityField field = new RetailTradeCommodityField();

	public static final int Min_ID = 0;
	public static final int Min_NO = 0;
	public static final int Max_NO = 9999;
	public static final double Min_Double = 0.000000d;

	public static final String FIELD_ERROR_TradeID = "TradeID不能小于或等于" + Min_ID;
	public static final String FIELD_ERROR_CommodityID = "CommodityID不能小于或等于" + Min_ID;
	public static final String FIELD_ERROR_BarcodeID = "BarcodeID不能小于或等于" + Min_ID;
	public static final String FIELD_ERROR_NO = "数量不能小于或等于" + Min_NO + ",并且不能大于" + Max_NO;
	public static final String FIELD_ERROR_NOCanReturn = "可退货的数量不能小于" + Min_NO + ",并且不能大于" + Max_NO;
	public static final String FIELD_ERROR_Price = "商品的零售价必须大于" + Min_Double;
	public static final String FIELD_ERROR_PriceReturn = "退货价必须大于或等于" + Min_Double;
	public static final String FIELD_ERROR_PriceVIPOriginal = "商品的会员价必须大于" + Min_Double;

	protected int tradeID;

	protected int commodityID;

	protected String commodityName;

	protected int barcodeID;
	
	@JSONField(name = "NO")
	protected int NO;

	protected double priceOriginal;
	
	@JSONField(name = "NOCanReturn")
	protected int NOCanReturn;

	protected double priceReturn;

	protected double priceSpecialOffer;

	protected double priceVIPOriginal;

	public double getPriceVIPOriginal() {
		return priceVIPOriginal;
	}

	public void setPriceVIPOriginal(double priceVIPOriginal) {
		this.priceVIPOriginal = priceVIPOriginal;
	}

	public int getNOCanReturn() {
		return NOCanReturn;
	}

	public void setNOCanReturn(int nOCanReturn) {
		NOCanReturn = nOCanReturn;
	}

	public int getTradeID() {
		return tradeID;
	}

	public void setTradeID(int tradeID) {
		this.tradeID = tradeID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public double getPriceOriginal() {
		return priceOriginal;
	}

	public void setPriceOriginal(double priceOriginal) {
		this.priceOriginal = priceOriginal;
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

	public int getBarcodeID() {
		return barcodeID;
	}

	public void setBarcodeID(int barcodeID) {
		this.barcodeID = barcodeID;
	}

	protected String barcodes;

	public String getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(String barcodes) {
		this.barcodes = barcodes;
	}

	protected int operatorStaffID;

	public int getOperatorStaffID() {
		return operatorStaffID;
	}

	public void setOperatorStaffID(int operatorStaffID) {
		this.operatorStaffID = operatorStaffID;
	}

	@Override
	public String toString() {
		return "RetailTradeCommodity [tradeID=" + tradeID + ", commodityID=" + commodityID + ", barcodeID=" + barcodeID + ", NO=" + NO + ", priceOriginal=" + priceOriginal + ", NOCanReturn=" + NOCanReturn + ", priceReturn=" + priceReturn
				+ ", priceSpecialOffer=" + priceSpecialOffer + ", priceVIPOriginal=" + priceVIPOriginal + ", ID=" + ID + "]";
	}

	public BaseModel clone() {
		RetailTradeCommodity rtc = new RetailTradeCommodity();
		rtc.setID(ID);
		rtc.setTradeID(tradeID);
		rtc.setCommodityID(commodityID);
		rtc.setBarcodeID(barcodeID);
		rtc.setNO(NO);
		rtc.setPriceOriginal(priceOriginal);
		rtc.setPriceReturn(priceReturn);
		rtc.setPriceSpecialOffer(priceSpecialOffer);
		rtc.setPriceVIPOriginal(priceVIPOriginal);
		rtc.setNOCanReturn(NOCanReturn);
		rtc.setOperatorStaffID(operatorStaffID);
		return rtc;
	}

	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradeCommodity rtc = (RetailTradeCommodity) arg0;
		if ((ignoreIDInComparision == true ? true
				: rtc.getID() == ID && printComparator(field.getFIELD_NAME_ID()) //
						&& rtc.getTradeID() == tradeID && printComparator(field.getFIELD_NAME_tradeID())) //
				// && rtc.getCommodityID() == this.getCommodityID() &&
				// printComparator(getFIELD_NAME_commodityID()) //
				&& rtc.getBarcodeID() == barcodeID && printComparator(field.getFIELD_NAME_barcodeID()) //
				&& rtc.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
				&& Math.abs(GeneralUtil.sub(rtc.getPriceOriginal(), priceOriginal)) < TOLERANCE && printComparator(field.getFIELD_NAME_price()) //
				&& Math.abs(GeneralUtil.sub(rtc.getPriceReturn(), priceReturn)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceReturn()) //
				&& Math.abs(GeneralUtil.sub(rtc.getPriceSpecialOffer(), priceSpecialOffer)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceSpecialOffer()) //
				&& Math.abs(GeneralUtil.sub(rtc.getPriceVIPOriginal(), priceVIPOriginal)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceVIPOriginal()) //
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		RetailTradeCommodity rtc = (RetailTradeCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_tradeID(), rtc.getTradeID());
		params.put(field.getFIELD_NAME_commodityID(), rtc.getCommodityID());
		params.put(field.getFIELD_NAME_barcodeID(), rtc.getBarcodeID());
		params.put(field.getFIELD_NAME_NO(), rtc.getNO());
		params.put(field.getFIELD_NAME_price(), rtc.getPriceOriginal());
		params.put(field.getFIELD_NAME_NOCanReturn(), rtc.getNOCanReturn());
		params.put(field.getFIELD_NAME_operatorStaffID(), rtc.getOperatorStaffID());
		params.put(field.getFIELD_NAME_priceReturn(), rtc.getPriceReturn());
		params.put(field.getFIELD_NAME_priceSpecialOffer(), rtc.getPriceSpecialOffer());
		params.put(field.getFIELD_NAME_priceVIPOriginal(), rtc.getPriceVIPOriginal());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		RetailTradeCommodity rtc = (RetailTradeCommodity) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_tradeID(), rtc.getTradeID());
		params.put(field.getFIELD_NAME_iPageIndex(), rtc.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), rtc.getPageSize());

		return params;
	}

	@Override
	public BaseModel parse1(String s) {
		try {
			return doParse1(com.alibaba.fastjson.JSONObject.parseObject(s));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected BaseModel doParse1(com.alibaba.fastjson.JSONObject jo) {
		try {
			// 这两个字段在POS端上传过来时，是没有的，但是在检查点需要用到，所以判断是否为空。
			if (jo.getString(field.getFIELD_NAME_ID()) != null) {
				ID = jo.getInteger(field.getFIELD_NAME_ID());
			}
			if (jo.getString(field.getFIELD_NAME_tradeID()) != null) {
				tradeID = jo.getInteger(field.getFIELD_NAME_tradeID());
			}
			commodityID = jo.getInteger(field.getFIELD_NAME_commodityID());
			barcodeID = jo.getInteger(field.getFIELD_NAME_barcodeID());
			NO = jo.getInteger(field.getFIELD_NAME_NO());
			priceOriginal = jo.getDouble(field.getFIELD_NAME_price());
			NOCanReturn = jo.getInteger(field.getFIELD_NAME_NOCanReturn());
			priceReturn = jo.getDouble(field.getFIELD_NAME_priceReturn());
			priceSpecialOffer = jo.getDouble(field.getFIELD_NAME_priceSpecialOffer());
			priceVIPOriginal = jo.getDouble(field.getFIELD_NAME_priceVIPOriginal());
			commodityName = jo.getString(field.getFIELD_NAME_commodityName());

			return this;
		} catch (JSONException e) {
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
				RetailTradeCommodity c = new RetailTradeCommodity();
				c.doParse1(jsonObject1);
				retailTradeList.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return retailTradeList;
	}

	@Override
	public List<BaseModel> parseN(JSONArray jsonArray) {
		List<BaseModel> rtcList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				RetailTradeCommodity rtc = new RetailTradeCommodity();
				if (rtc.parse1(jsonObject.toString()) == null) {
					return null;
				}
				rtcList.add(rtc);
			}
			return rtcList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toJson(BaseModel bm) {
		String json = JSON.toJSONString(bm);

		return json;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_tradeID(), FIELD_ERROR_TradeID, sbError) && !FieldFormat.checkID(tradeID)) //
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

		if (this.printCheckField(field.getFIELD_NAME_NO(), FIELD_ERROR_NO, sbError) && NO <= Min_NO || NO > Max_NO) // 不允许卖出0件商品
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_price(), FIELD_ERROR_Price, sbError) && priceOriginal < Min_Double) // 服务型商品priceOriginal=0d
		{
			return sbError.toString();
		}

		// 可退货数量大于或等于零，因为有退货单的情况
		if (this.printCheckField(field.getFIELD_NAME_NOCanReturn(), FIELD_ERROR_NOCanReturn, sbError) && NOCanReturn < Min_NO || NOCanReturn > Max_NO) //
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_priceReturn(), FIELD_ERROR_PriceReturn, sbError) && priceReturn < Min_Double) // 服务型商品
		{
			return sbError.toString();
		}

		// 现阶段先不管会员价
		// if (this.printCheckField(field.getFIELD_NAME_priceVIPOriginal(),
		// FIELD_ERROR_PriceVIPOriginal, sbError) && priceVIPOriginal <= Min_Double) //
		// {
		// return sbError.toString();
		// }

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_tradeID(), FIELD_ERROR_TradeID, sbError) && !FieldFormat.checkID(tradeID)) //
		{
			return sbError.toString();
		}

		return "";
	}

	@Override
	public boolean checkPageSize(BaseModel bm) {
		return true;
	}

}
