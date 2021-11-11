
package com.bx.erp.model.warehousing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Shop;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class InventorySheet extends BaseModel {
	private static final long serialVersionUID = -9097575967000627586L;
	public static final InventorySheetField field = new InventorySheetField();

	protected static final int MaxInventoryRemark = 128;
	protected static final int MaxCommodityNameLength = 32;

	public static final String FIELD_ERROR_remark = "盘点单备注长度不大于 " + MaxInventoryRemark;
	public static final String FIELD_ERROR_warehouseID = "warehouseID必须大于0";
	public static final String FIELD_ERROR_shopID = "shopID必须大于0";
	public static final String FIELD_ERROR_VirtualShopID = "ShopID不能是虚拟门店ID";
	public static final String FIELD_ERROR_staffID = "staffID必须大于0";
	public static final String FIELD_ERROR_scope = "盘点范围必须为" + EnumScopeInventorySheet.ESIS_WholeShop.getIndex() + "~" + EnumScopeInventorySheet.ESIS_SpecifiedBrand.getIndex() + "的正整数";
	public static final String FIELD_ERROR_approverID = "approverID必须大于0";
	public static final String FIELD_ERROR_StatusForRN = "只能查询状态在" + EnumStatusInventorySheet.ESIS_ToInput.getIndex() + "~" + EnumStatusInventorySheet.ESIS_Approved.getIndex() + "的盘点单";
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字长度不能大于" + MaxCommodityNameLength;

	protected int warehouseID;
	
	protected int shopID;

	protected int scope;

	protected int status;

	protected int staffID;

	protected Date approveDatetime;

	protected String remark;

	private int approverID;

	protected String sn;

	protected List<Commodity> listCommodity; // 商品表
	protected Provider provider;
	protected Shop shop;
	protected Warehouse warehouse;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getApproverID() {
		return approverID;
	}

	public void setApproverID(int approverID) {
		this.approverID = approverID;
	}

	public int getWarehouseID() {
		return warehouseID;
	}

	public void setWarehouseID(int warehouseID) {
		this.warehouseID = warehouseID;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public Date getApproveDatetime() {
		return approveDatetime;
	}

	public void setApproveDatetime(Date approveDatetime) {
		this.approveDatetime = approveDatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public List<Commodity> getListCommodity() {
		return listCommodity;
	}

	public void setListCommodity(List<Commodity> listCommodity) {
		this.listCommodity = listCommodity;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	// protected String sValue; // 非数据表字段
	//
	// public String getSValue() {
	// return sValue;
	// }
	//
	// public void setSValue(String sValue) {
	// this.sValue = sValue;
	// }

	protected String approverName; // 非数据表字段

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	protected String creatorName;

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	protected String commodityName;

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	private int messageID; // 非数据表字段

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	
	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@Override
	public String toString() {
		return "InventorySheet [warehouseID=" + warehouseID + ", shopID=" + shopID + ", scope=" + scope + ", status=" + status + ", staffID=" + staffID + ", approveDatetime=" + approveDatetime + ", remark=" + remark + ", approverID="
				+ approverID + ", sn=" + sn + ", listCommodity=" + listCommodity + ", provider=" + provider + ", shop=" + shop + ", warehouse=" + warehouse + ", approverName=" + approverName + ", creatorName=" + creatorName
				+ ", commodityName=" + commodityName + ", messageID=" + messageID + "]";
	}

	@Override
	public int compareTo(BaseModel arg0) {
		InventorySheet its = (InventorySheet) arg0;
		if ((ignoreIDInComparision == true ? true : its.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID())) //
				&& its.getWarehouseID() == this.getWarehouseID() && printComparator(field.getFIELD_NAME_warehouseID())//
				&& its.getStatus() == this.getStatus() && printComparator(field.getFIELD_NAME_status())//
				&& its.getApproverID() == this.getApproverID() && printComparator(field.getFIELD_NAME_approverID())//
				&& its.getShopID() == this.getShopID() && printComparator(field.getFIELD_NAME_shopID())//
				&& its.getStaffID() == this.getStaffID() && printComparator(field.getFIELD_NAME_staffID())//
				&& its.getRemark().equals(this.getRemark()) && printComparator(field.getFIELD_NAME_remark())//
		) {
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && its.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && its.getListSlave1() != null) {
					if (its.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && its.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && its.getListSlave1() != null) {
					if (listSlave1.size() != its.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						InventoryCommodity inventoryCommodity = (InventoryCommodity) listSlave1.get(i);
						inventoryCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < its.getListSlave1().size(); j++) {
							InventoryCommodity ic = (InventoryCommodity) its.getListSlave1().get(j);
							if (inventoryCommodity.getCommodityID() == ic.getCommodityID()) {
								exist = true;
								if (inventoryCommodity.compareTo(ic) != 0) {
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
	public BaseModel clone() throws CloneNotSupportedException {
		InventorySheet in = new InventorySheet();
		in.setID(ID);
		in.setShopID(shopID);
		in.setStatus(status);
		in.setWarehouseID(warehouseID);
		in.setScope(scope);
		in.setStaffID(staffID);
		in.setApproverID(approverID);
		in.setRemark(remark);
		in.setCreateDatetime(createDatetime == null ? null : (Date) createDatetime.clone());
		in.setApproveDatetime(approveDatetime == null ? null : (Date) approveDatetime.clone());
		in.setSn(sn);
		if (listSlave1 != null) {
			List<InventoryCommodity> list = new ArrayList<InventoryCommodity>();
			for (Object o : listSlave1) {
				InventoryCommodity ic = (InventoryCommodity) o;
				list.add((InventoryCommodity) ic.clone());
			}
			in.setListSlave1(list);
		}
		in.setMessageID(messageID);

		return in;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);
		InventorySheet is = (InventorySheet) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_InventorySheet_RetrieveNByNFields:
			params.put(field.getFIELD_NAME_queryKeyword(), is.getQueryKeyword() == null ? "" : is.getQueryKeyword());
			params.put(field.getFIELD_NAME_iPageIndex(), is.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), is.getPageSize());

			break;
		default:
			params.put(field.getFIELD_NAME_shopID(), is.getShopID() == 0 ? BaseAction.INVALID_ID : is.getShopID());
			params.put(field.getFIELD_NAME_status(), is.getStatus());
			params.put(field.getFIELD_NAME_iPageIndex(), is.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), is.getPageSize());

			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		InventorySheet is = (InventorySheet) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_shopID(), is.getShopID());
		params.put(field.getFIELD_NAME_warehouseID(), is.getWarehouseID());
		params.put(field.getFIELD_NAME_scope(), is.getScope());
		params.put(field.getFIELD_NAME_staffID(), is.getStaffID());
		params.put(field.getFIELD_NAME_remark(), is.getRemark() == null ? "" : is.getRemark());

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);
		InventorySheet is = (InventorySheet) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), is.getID());

		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveInventorySheet:
			params.put(field.getFIELD_NAME_approverID(), is.getApproverID());

			break;
		case BaseBO.CASE_SubmitInventorySheet:

			break;
		default:
			params.put(field.getFIELD_NAME_remark(), is.getRemark() == null ? "" : is.getRemark());

			break;
		}

		return params;
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_warehouseID(), FIELD_ERROR_warehouseID, sbError) && !FieldFormat.checkID(warehouseID)) //
		{
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) //
		{
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_VirtualShopID, sbError) && shopID == BaseAction.VirtualShopID) //
		{
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) //
		{
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_scope(), FIELD_ERROR_scope, sbError) && !EnumScopeInventorySheet.inBound(scope)) //
		{
			return sbError.toString();
		}
		return doCheckCreateUpdate(iUseCaseID);
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
		{
			return sbError.toString();
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_InventorySheet_RetrieveNByNFields:
			if (!StringUtils.isEmpty(queryKeyword)) {
				// queryKeyword可以代表搜索的因子为：商品名称、盘点单单号。其中商品名称最长，所以queryKeyword最长能传MaxCommodityNameLength位
				if (this.printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && queryKeyword.length() > MaxCommodityNameLength) //
				{
					return sbError.toString();
				}
			}
			return "";
		default:
			if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_StatusForRN, sbError) && !EnumStatusInventorySheet.inBoundForRetrieveN(status)) //
			{
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_shopID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_shopID()), sbError) && shopID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			return "";
		}
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(BaseBO.INVALID_CASE_ID);
	}

	@Override
	public String checkUpdate(int iUseCaseID) {

		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveInventorySheet:
			StringBuilder sbError = new StringBuilder();
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
			{
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_approverID(), FIELD_ERROR_approverID, sbError) && !FieldFormat.checkID(approverID)) //
			{
				return sbError.toString();
			}
			return "";
		default:
			return doCheckCreateUpdate(iUseCaseID);
		}
	}

	protected String doCheckCreateUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (!StringUtils.isEmpty(remark)) {
			if (this.printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_remark, sbError) && remark.trim().length() > MaxInventoryRemark) {
				return sbError.toString();
			}
		}
		return "";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			status = jo.getInt(field.getFIELD_NAME_status());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			warehouseID = jo.getInt(field.getFIELD_NAME_warehouseID());
			scope = jo.getInt(field.getFIELD_NAME_scope());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			approverID = jo.getInt(field.getFIELD_NAME_approverID());
			remark = jo.getString(field.getFIELD_NAME_remark());
			messageID = jo.getInt(field.getFIELD_NAME_messageID());
			sn = jo.getString(field.getFIELD_NAME_sn());
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
			// 查询子商品集合
			List<InventoryCommodity> listInventorySheet = new ArrayList<>();
			JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				JSONObject object = jr.getJSONObject(i);
				InventoryCommodity comm = (InventoryCommodity) new InventoryCommodity().parse1(object.toString());
				listInventorySheet.add(comm);
			}
			listSlave1 = listInventorySheet;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	public enum EnumStatusInventorySheet {
		ESIS_ToInput("ToInput", 0), //
		ESIS_Submitted("Submitted", 1), //
		ESIS_Approved("Approved", 2), //
		ESIS_Deleted("Deleted", 3);

		private String name;
		private int index;

		public static boolean inBoundForRetrieveN(int index) {
			if (index != BaseAction.INVALID_STATUS && index != ESIS_ToInput.getIndex() && index != ESIS_Submitted.getIndex() && index != ESIS_Approved.getIndex() && index != ESIS_Submitted.getIndex()) {
				return false;
			}

			return true;
		}

		public static boolean inBound(int index) {
			if (index < ESIS_ToInput.getIndex() || index > ESIS_Deleted.getIndex()) {
				return false;
			}
			return true;
		}

		private EnumStatusInventorySheet(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusInventorySheet c : EnumStatusInventorySheet.values()) {
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

	public enum EnumScopeInventorySheet {
		ESIS_WholeShop("全店盘点", 0), //
		ESIS_SpecifiedCategory("指定类别", 1), //
		ESIS_SpecifiedProvider("指定供应商", 2), //
		ESIS_SpecifiedBrand("指定品牌", 3);

		private String name;
		private int index;

		public static boolean inBound(int index) {
			if (index < ESIS_WholeShop.getIndex() || index > ESIS_SpecifiedBrand.getIndex()) {
				return false;
			}

			return true;
		}

		private EnumScopeInventorySheet(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumScopeInventorySheet esit : EnumScopeInventorySheet.values()) {
				if (esit.getIndex() == index) {
					return esit.name;
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
		return EnumConfigCacheSizeCache.ECC_InventorySheetCacheSize.getIndex();
	}

}
