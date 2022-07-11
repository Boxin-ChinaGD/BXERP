package com.bx.erp.model.commodity;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class CommodityShopInfo extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String FIELD_ERROR_commodityID = "commodityID必须是大于0的数字";
	
	public static final CommodityShopInfoField field = new CommodityShopInfoField();

	private int commodityID;
	
	private int shopID;
	
	private double latestPricePurchase;
	
	private double priceRetail;
	
	/** 库存 */
	private int NO;
	
	private int nOStart;
	
	private double purchasingPriceStart;
	
	private int currentWarehousingID;
	
	/** 代表要搜索的员工的ID */
	private int operatorStaffID;
	
	public int getOperatorStaffID() {
		return operatorStaffID;
	}

	public void setOperatorStaffID(int operatorStaffID) {
		this.operatorStaffID = operatorStaffID;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
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

	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
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

	public int getCurrentWarehousingID() {
		return currentWarehousingID;
	}

	public void setCurrentWarehousingID(int currentWarehousingID) {
		this.currentWarehousingID = currentWarehousingID;
	}
	
	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		CommodityShopInfo obj = new CommodityShopInfo();
		obj.setID(ID);
		obj.setCommodityID(commodityID);
		obj.setShopID(shopID);
		obj.setLatestPricePurchase(latestPricePurchase);
		obj.setPriceRetail(priceRetail);
		obj.setNO(NO);
		obj.setnOStart(nOStart);
		obj.setPurchasingPriceStart(purchasingPriceStart);
		obj.setCurrentWarehousingID(currentWarehousingID);
		return obj;
	}
	
	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_priceRetail(), Commodity.FIELD_ERROR_priceRetail, sbError) && !FieldFormat.checkCommodityPrice(priceRetail)) {//
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_operatorStaffID(), Commodity.FIELD_ERROR_staff, sbError) && !FieldFormat.checkID(operatorStaffID)) {
			return sbError.toString();
		}
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateComposition:
			if (printCheckField(field.getFIELD_NAME_nOStart(), Commodity.FIELD_ERROR_nOStartOfComposition, sbError) && nOStart != Commodity.NO_START_Default) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), Commodity.FIELD_ERROR_purchasingPriceStartOfComposition, sbError) && purchasingPriceStart != Commodity.PURCHASING_PRICE_START_Default) {
				return sbError.toString();
			}
			return "";
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			if (printCheckField(field.getFIELD_NAME_NO(), Commodity.FIELD_ERROR_NOofMultiPackaging, sbError) && FieldFormat.checkIfMultiPackagingNO(NO)//
					&& printCheckField(field.getFIELD_NAME_nOStart(), Commodity.FIELD_ERROR_nOStartOfMultipackaging, sbError) && nOStart == Commodity.NO_START_Default //
					&& printCheckField(field.getFIELD_NAME_purchasingPriceStart(), Commodity.FIELD_ERROR_purchasingPriceStartOfMultipackaging, sbError) && purchasingPriceStart == Commodity.PURCHASING_PRICE_START_Default) {
				return "";
			}
			return sbError.toString();
		case BaseBO.CASE_Commodity_CreateService:
			if (printCheckField(field.getFIELD_NAME_NO(), Commodity.FEIDL_ERROR_noOfService, sbError) && NO != 0) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_nOStart(), Commodity.FIELD_ERROR_nOStartOfService, sbError) && nOStart != Commodity.NO_START_Default) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), Commodity.FIELD_ERROR_purchasingPriceStartOfService, sbError) && purchasingPriceStart != Commodity.PURCHASING_PRICE_START_Default) {
				return sbError.toString();
			}

			if (printCheckField(field.getFIELD_NAME_latestPricePurchase(), Commodity.FIELD_ERROR_latestPricePurchaseOfService, sbError) && latestPricePurchase != Commodity.DEFAULT_VALUE_LatestPricePurchase) {
				return sbError.toString();
			}
			return "";
		default: // case BaseBO.CASE_Commodity_CreateSingle:
			// ... 只有普通商品才能够设置为期初商品。
			if (nOStart != -1 || purchasingPriceStart != -1D) { // 期初数量和期初采购价或者同时为-1，或者数量>0、期初采购价属于(0, 10000]
				if (printCheckField(field.getFIELD_NAME_nOStart(), Commodity.FIELD_ERROR_nOStart, sbError) && !FieldFormat.checkNoStart(nOStart)) {
					return sbError.toString();
				}
				if (printCheckField(field.getFIELD_NAME_purchasingPriceStart(), Commodity.FIELD_ERROR_purchasingPriceStart, sbError) && (purchasingPriceStart <= 0 || purchasingPriceStart > FieldFormat.MAX_OneCommodityPrice)) {
					return sbError.toString();
				}
			}
		}
		return "";
	}
	
	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		CommodityShopInfo commShopInfo = (CommodityShopInfo) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_commodityID(), commShopInfo.getCommodityID());
		params.put(field.getFIELD_NAME_shopID(), commShopInfo.getShopID());
		params.put(field.getFIELD_NAME_latestPricePurchase(), commShopInfo.getLatestPricePurchase());
		params.put(field.getFIELD_NAME_priceRetail(), commShopInfo.getPriceRetail());
		params.put(field.getFIELD_NAME_nOStart(), commShopInfo.getnOStart());
		params.put(field.getFIELD_NAME_purchasingPriceStart(), commShopInfo.getPurchasingPriceStart());
		params.put(field.getFIELD_NAME_operatorStaffID(), commShopInfo.getOperatorStaffID());
		params.put(field.getFIELD_NAME_currentWarehousingID(), commShopInfo.getCurrentWarehousingID());
		return params;
	}
	
	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		CommodityShopInfo comm = (CommodityShopInfo) arg0;
		if ((ignoreIDInComparision == true ? true : (comm.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& comm.getCommodityID() == commodityID && printComparator(field.getFIELD_NAME_commodityID()) //
				&& comm.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
				&& Math.abs(GeneralUtil.sub(comm.getLatestPricePurchase(), latestPricePurchase)) < TOLERANCE && printComparator(field.getFIELD_NAME_latestPricePurchase()) //
				&& Math.abs(GeneralUtil.sub(comm.getPriceRetail(), priceRetail)) < TOLERANCE && printComparator(field.getFIELD_NAME_priceRetail()) //
				&& comm.getNO() == NO && printComparator(field.getFIELD_NAME_NO()) //
				&& comm.getnOStart() == nOStart && printComparator(field.getFIELD_NAME_nOStart()) //
				&& Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), purchasingPriceStart)) < TOLERANCE && printComparator(field.getFIELD_NAME_purchasingPriceStart())//
				&& comm.getCurrentWarehousingID() == currentWarehousingID && printComparator(field.getFIELD_NAME_currentWarehousingID()) //
		) {
			return 0;
		}
		return -1;
	}
	
	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_commodityID(), commodityShopInfo.getCommodityID());
			params.put(field.getFIELD_NAME_shopID(), commodityShopInfo.getShopID());

			break;
		}

		return params;
	}
	
	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			commodityID = jo.getInt(field.getFIELD_NAME_commodityID());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			latestPricePurchase = jo.getDouble(field.getFIELD_NAME_latestPricePurchase());
			priceRetail = jo.getDouble(field.getFIELD_NAME_priceRetail());
			NO = jo.getInt(field.getFIELD_NAME_NO());
			nOStart = jo.getInt(field.getFIELD_NAME_nOStart());
			purchasingPriceStart = jo.getDouble(field.getFIELD_NAME_purchasingPriceStart());
			currentWarehousingID = jo.getInt(field.getFIELD_NAME_currentWarehousingID());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}
	
	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		default:
			if (printCheckField(field.getFIELD_NAME_commodityID(), FIELD_ERROR_commodityID, sbError) && !(commodityID > 0 || commodityID == BaseAction.INVALID_ID)) {
				return sbError.toString();
			}

			return "";
		}

	}
}
