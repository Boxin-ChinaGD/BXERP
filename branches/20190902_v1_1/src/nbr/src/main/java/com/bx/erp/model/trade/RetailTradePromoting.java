package com.bx.erp.model.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class RetailTradePromoting extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final RetailTradePromotingField field = new RetailTradePromotingField();

	public static final String FIELD_ERROR_tradeID = "tradeID必须大于0";

	protected int tradeID;

	public int getTradeID() {
		return tradeID;
	}

	public void setTradeID(int tradeID) {
		this.tradeID = tradeID;
	}

	@Override
	public String toString() {
		return "RetailTradePromoting [tradeID=" + tradeID + ", listRetailTradePromotingFlow=" + listSlave1 + ", ID=" + ID + ", createDatetime=" + createDatetime + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		RetailTradePromoting rtp = (RetailTradePromoting) arg0;
		if ((ignoreIDInComparision == true ? true : (rtp.getID() == ID && printComparator(field.getFIELD_NAME_ID())))//
				&& rtp.getTradeID() == tradeID && printComparator(field.getFIELD_NAME_tradeID()) //
		// 不比较创建时间
		) {
			//
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && rtp.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && rtp.getListSlave1() != null) {
					if (rtp.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && rtp.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && rtp.getListSlave1() != null) {
					if (listSlave1.size() != rtp.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) listSlave1.get(i);
						retailTradePromotingFlow.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < rtp.getListSlave1().size(); j++) {
							RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) rtp.getListSlave1().get(j);
							if (retailTradePromotingFlow.getProcessFlow().equals(rtpf.getProcessFlow())) { // 这里不能用promotionID来比较，因为只有优惠券而无促销时，promotionID为0
								exist = true;
								if (retailTradePromotingFlow.compareTo(rtpf) != 0) {
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
			//
			return 0;
		}
		return -1;
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		RetailTradePromoting rp = (RetailTradePromoting) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_tradeID(), rp.getTradeID());
			params.put(field.getFIELD_NAME_iPageIndex(), rp.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), rp.getPageSize());
			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		RetailTradePromoting rtp = (RetailTradePromoting) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_tradeID(), rtp.getTradeID());
		return params;
	}

	@Override
	public BaseModel clone(){
		RetailTradePromoting rtp = new RetailTradePromoting();
		rtp.setID(ID);
		rtp.setTradeID(tradeID);
		if (listSlave1 != null && listSlave1.size() > 0) {
			List<RetailTradePromotingFlow> list = new ArrayList<RetailTradePromotingFlow>();
			for (Object o : listSlave1) {
				RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) o;
				list.add((RetailTradePromotingFlow) retailTradePromotingFlow.clone());
			}
			rtp.setListSlave1(list);
		}
		rtp.setIgnoreIDInComparision(ignoreIDInComparision);
		rtp.setCreateDatetime(createDatetime);
		return rtp;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_tradeID(), FIELD_ERROR_tradeID, sbError) && !FieldFormat.checkID(tradeID)) //
		{
			return sbError.toString();
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseModel parse1(String s) {
		try {
			JSONObject joRT = JSONObject.fromObject(s);
			RetailTradePromoting rtp = (RetailTradePromoting) doParse1(joRT);
			if (rtp == null) {
				return null;
			}
			JSONArray rtcArr = joRT.getJSONArray("listSlave1");//
			if (rtcArr != null && rtcArr.size() > 0) {
				RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
				List<?> listRtpf = rtpf.parseN(rtcArr);
				if (listRtpf == null) {
					return null;
				}
				rtp.setListSlave1((List<RetailTradePromotingFlow>) listRtpf); // 非常关键！！
			}

			return rtp;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			tradeID = jo.getInt(field.getFIELD_NAME_tradeID());
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<?> parseN(JSONArray jsonArray) {
		List<BaseModel> bmList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (this.parse1(jsonObject.toString()) == null) {
					return null;
				}
				bmList.add(this.clone());
			}
			return bmList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_RetailTredePromotingNumberCacheSize.getIndex();
	}
}
