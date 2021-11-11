package com.bx.erp.model.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RetailTradePromotingFlow extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final RetailTradePromotingFlowField field = new RetailTradePromotingFlowField();

	public static final String FIELD_ERROR_processFlow = "记录过程不能为null或空字符串";
	public static final String FIELD_ERROR_retailTradePromotingID = "retailTradePromotingID必须大于0";
	public static final String FIELD_ERROR_promotionID = "promotionID必须大于0";

	protected int retailTradePromotingID;

	public int getRetailTradePromotingID() {
		return retailTradePromotingID;
	}

	public void setRetailTradePromotingID(int retailTradePromotingID) {
		this.retailTradePromotingID = retailTradePromotingID;
	}

	/**
	 * 当零售单没有促销只有优惠券时，值为0
	 */
	protected int promotionID;

	public int getPromotionID() {
		return promotionID;
	}

	public void setPromotionID(int promotionID) {
		this.promotionID = promotionID;
	}

	protected String processFlow;

	public String getProcessFlow() {
		return processFlow;
	}

	public void setProcessFlow(String processFlow) {
		this.processFlow = processFlow;
	}

	@Override
	public String toString() {
		return "RetailTradePromotingFlow [retailTradePromotingID=" + retailTradePromotingID + ", promotionID=" + promotionID + ", processFlow=" + processFlow + ", ID=" + ID + ", createDatetime=" + createDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) arg0;
		if ((ignoreIDInComparision == true ? true : (rtpf.getID() == ID && printComparator(field.getFIELD_NAME_ID()))//
				&& rtpf.getRetailTradePromotingID() == retailTradePromotingID && printComparator(field.getFIELD_NAME_retailTradePromotingID())) //
				&& rtpf.getPromotionID() == promotionID && printComparator(field.getFIELD_NAME_promotionID()) //
				&& rtpf.getProcessFlow().equals(processFlow) && printComparator(field.getFIELD_NAME_processFlow()) //
		// 不比较创建时间
		) {
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_retailTradePromotingID(), rtpf.getRetailTradePromotingID());
		params.put(field.getFIELD_NAME_iPageIndex(), rtpf.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), rtpf.getPageSize());
		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_retailTradePromotingID(), rtpf.getRetailTradePromotingID());
		params.put(field.getFIELD_NAME_promotionID(), (rtpf.getPromotionID() == 0 ? null : rtpf.getPromotionID()));
		params.put(field.getFIELD_NAME_processFlow(), rtpf.getProcessFlow());
		return params;
	}

	@Override
	public BaseModel clone(){
		RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
		rtpf.setID(ID);
		rtpf.setRetailTradePromotingID(retailTradePromotingID);
		rtpf.setPromotionID(promotionID);
		rtpf.setProcessFlow(processFlow);
		rtpf.setCreateDatetime(createDatetime);
		rtpf.setIgnoreIDInComparision(ignoreIDInComparision);
		return rtpf;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_retailTradePromotingID(), FIELD_ERROR_retailTradePromotingID, sbError) && !FieldFormat.checkID(retailTradePromotingID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_promotionID(), FIELD_ERROR_promotionID, sbError) && (!FieldFormat.checkID(promotionID) && promotionID != 0)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_processFlow(), FIELD_ERROR_processFlow, sbError) && StringUtils.isEmpty(processFlow)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_retailTradePromotingID(), FIELD_ERROR_retailTradePromotingID, sbError) && !FieldFormat.checkID(retailTradePromotingID)) {
			return sbError.toString();
		}
		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			promotionID = Integer.valueOf(jo.getString("promotionID"));
			processFlow = String.valueOf(jo.getString("processFlow"));
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<?> parseN(JSONArray jsonArray) {
		List<BaseModel> rtcList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
				if (rtpf.parse1(jsonObject.toString()) == null) {
					return null;
				}
				rtcList.add(rtpf);
			}
			return rtcList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
