package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ReturnCommoditySheet extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final ReturnCommoditySheetField field = new ReturnCommoditySheetField();

	public static final int MaxStringLength = 32;

	public static final String FIELD_ERROR_staffID = "staffID必须大于0";
	public static final String FIELD_ERROR_providerID = "providerID必须大于0";
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字的长度不能大于" + MaxStringLength;
	public static final String FIELD_ERROR_status = "状态必须是-1," + EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex() + "或" + EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex();
	public static final String FIELD_ERROR_date1AndDate2 = "非法时间格式！正确的时间格式应为：" + BaseAction.DATETIME_FORMAT_Default3;
	public static final String FIELD_ERROR_shopID = "shopID必须大于0, 且不能为1（虚拟总部）";
	
	protected Provider provider;// 供应商
	protected List<Commodity> listCommodity; // 商品表

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public List<Commodity> getListCommodity() {
		return listCommodity;
	}

	public void setListCommodity(List<Commodity> listCommodity) {
		this.listCommodity = listCommodity;
	}

	protected int commodityID;

	protected double pricePurchase;

	protected int NO;

	protected String specification;

	protected int staffID;

	protected String sn;

	protected String commodityIDs;
	
	protected int shopID;
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getCommodityIDs() {
		return commodityIDs;
	}

	public void setCommodityIDs(String commodityIDs) {
		this.commodityIDs = commodityIDs;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	protected int providerID;

	protected Date createDate;

	protected int status;

	protected int bReturnCommodityListIsModified;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(int commodityID) {
		this.commodityID = commodityID;
	}

	protected String commodityName;

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public double getPricePurchase() {
		return pricePurchase;
	}

	public void setPricePurchase(double pricePurchase) {
		this.pricePurchase = pricePurchase;
	}

	public int getNO() {
		return NO;
	}

	public void setNO(int NO) {
		this.NO = NO;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public int getProviderID() {
		return providerID;
	}

	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	protected String providerName;
	
	protected String shopName;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public int getbReturnCommodityListIsModified() {
		return bReturnCommodityListIsModified;
	}

	public void setbReturnCommodityListIsModified(int bReturnCommodityListIsModified) {
		this.bReturnCommodityListIsModified = bReturnCommodityListIsModified;
	}

	@Override
	public String toString() {
		return "ReturnCommoditySheet [provider=" + provider + ", listCommodity=" + listCommodity + ", commodityID=" + commodityID + ", pricePurchase=" + pricePurchase + ", NO=" + NO + ", specification=" + specification + ", staffID="
				+ staffID + ", sn=" + sn + ", providerID=" + providerID + ", createDate=" + createDate + ", status=" + status + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		ReturnCommoditySheet obj = new ReturnCommoditySheet();
		obj.setID(ID);
		obj.setStaffID(staffID);
		obj.setProviderID(providerID);
		obj.setStatus(status);
		obj.setSn(sn);
		obj.setShopID(shopID);
		obj.setbReturnCommodityListIsModified(bReturnCommodityListIsModified);
		if (listSlave1 != null) {
			List<ReturnCommoditySheetCommodity> list = new ArrayList<>();
			for (Object o : listSlave1) {
				ReturnCommoditySheetCommodity rcsComm = (ReturnCommoditySheetCommodity) o;
				list.add((ReturnCommoditySheetCommodity) rcsComm.clone());
			}
			obj.setListSlave1(list);
		}

		return obj;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		ReturnCommoditySheet rcs = (ReturnCommoditySheet) arg0;
		if ((ignoreIDInComparision == true ? true : (rcs.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& rcs.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID()) //
				&& rcs.getProviderID() == providerID && printComparator(field.getFIELD_NAME_providerID()) //
				&& rcs.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID()) //
		// && rcs.getStatus() == this.getStatus() &&
		// printComparator(getFIELD_NAME_status()) //
		) {
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && rcs.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && rcs.getListSlave1() != null) {
					if (rcs.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && rcs.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && rcs.getListSlave1() != null) {
					if (listSlave1.size() != rcs.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						ReturnCommoditySheetCommodity returnCommoditySheetCommodity = (ReturnCommoditySheetCommodity) listSlave1.get(i);
						returnCommoditySheetCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < rcs.getListSlave1().size(); j++) {
							ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) rcs.getListSlave1().get(j);
							if (returnCommoditySheetCommodity.getCommodityID() == rcsc.getCommodityID()) {
								exist = true;
								if (returnCommoditySheetCommodity.compareTo(rcsc) != 0) {
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
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ReturnCommoditySheet bd = (ReturnCommoditySheet) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_staffID(), bd.getStaffID());
		params.put(field.getFIELD_NAME_providerID(), bd.getProviderID());
		params.put(field.getFIELD_NAME_shopID(), bd.getShopID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		ReturnCommoditySheet bd = (ReturnCommoditySheet) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveReturnCommoditySheet:
			params.put(field.getFIELD_NAME_ID(), bd.getID());
			break;

		default:
			params.put(field.getFIELD_NAME_ID(), bd.getID());
			params.put(field.getFIELD_NAME_providerID(), bd.getProviderID());

			break;
		}
		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		checkParameterInput(bm);

		ReturnCommoditySheet rcs = (ReturnCommoditySheet) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		default:
			params.put(field.getFIELD_NAME_queryKeyword(), rcs.getQueryKeyword() == null ? "" : rcs.getQueryKeyword());
			params.put(field.getFIELD_NAME_staffID(), rcs.getStaffID() == 0 ? BaseAction.INVALID_ID : rcs.getStaffID());
			params.put(field.getFIELD_NAME_shopID(), rcs.getShopID() == 0 ? BaseAction.INVALID_ID : rcs.getShopID());
			params.put(field.getFIELD_NAME_status(), rcs.getStatus());
			params.put(field.getFIELD_NAME_providerID(), rcs.getProviderID() == 0 ? BaseAction.INVALID_ID : rcs.getProviderID());
			params.put(field.getFIELD_NAME_date1(), rcs.getDate1() == null ? 0 : sdf.format(rcs.getDate1()));
			params.put(field.getFIELD_NAME_date2(), rcs.getDate2() == null ? 0 : sdf.format(rcs.getDate2()));
			params.put(field.getFIELD_NAME_iPageIndex(), rcs.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), rcs.getPageSize());

			break;
		}

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) {
			return sbError.toString();
		}
		if (printCheckField(field.getFIELD_NAME_shopID(), String.format(FIELD_ERROR_shopID, field.getFIELD_NAME_shopID()), sbError) && (shopID <= 0 || shopID == 1)) {
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveReturnCommoditySheet:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			return "";
		default:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}

			if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
				return sbError.toString();
			}
			return "";
		}

	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		default:
			queryKeyword = queryKeyword == null ? "" : queryKeyword; // stirng1为null时默认是"";
			if (this.printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && queryKeyword.length() > MaxStringLength) {
				return sbError.toString();
			}

			if (this.printCheckField(field.getFIELD_NAME_staffID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_staffID()), sbError) && staffID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}

			if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !EnumStatusReturnCommoditySheet.inBoundForRetrieveN(status)) {
				return sbError.toString();
			}

			if (this.printCheckField(field.getFIELD_NAME_providerID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_providerID()), sbError) && providerID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}

			if (date1 != null) {
				if (this.printCheckField(field.getFIELD_NAME_date1(), FIELD_ERROR_date1AndDate2, sbError)) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
					try {
						simpleDateFormat.format(date1);
					} catch (Exception e) {
						return sbError.toString();
					}
				}
			}

			if (date2 != null) {
				if (this.printCheckField(field.getFIELD_NAME_date2(), FIELD_ERROR_date1AndDate2, sbError)) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
					try {
						simpleDateFormat.format(date2);
					} catch (Exception e) {
						return sbError.toString();
					}
				}
			}
			return "";
		}
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	public enum EnumStatusReturnCommoditySheet {
		ESRCS_ToApprove("ToApprove", 0), //
		ESRCS_Approved("Approved", 1);

		private String name;
		private int index;

		private EnumStatusReturnCommoditySheet(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBoundForRetrieveN(int index) {
			if (index != BaseAction.INVALID_STATUS && index != ESRCS_ToApprove.getIndex() && index != ESRCS_Approved.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumStatusReturnCommoditySheet c : EnumStatusReturnCommoditySheet.values()) {
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
			sn = jo.getString(field.getFIELD_NAME_sn());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			providerID = jo.getInt(field.getFIELD_NAME_providerID());
			status = jo.getInt(field.getFIELD_NAME_status());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}
			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			// 解析从表
			List<ReturnCommoditySheetCommodity> returnCommoditySheetCommodityList = new ArrayList<>();
			JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				JSONObject object = jr.getJSONObject(i);
				ReturnCommoditySheetCommodity comm = (ReturnCommoditySheetCommodity) new ReturnCommoditySheetCommodity().parse1(object.toString());
				returnCommoditySheetCommodityList.add(comm);
			}
			listSlave1 = returnCommoditySheetCommodityList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
