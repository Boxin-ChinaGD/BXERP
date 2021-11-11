package com.bx.erp.model.warehousing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Warehousing extends BaseModel {
	private static final long serialVersionUID = -3988279267202948336L;
	public static final WarehousingField field = new WarehousingField();

	public static final int MaxQueryKeywordLength = 32;

	// public static final String FIELD_ERROR_bToApproveStartValue =
	// "ApproveStartValue必须是0或1"; //此参数已去除
	public static final String FIELD_ERROR_queryKeyword = "搜索关键字的长度不能大于" + MaxQueryKeywordLength;
	public static final String FIELD_ERROR_status = "状态必须是-1," + EnumStatusWarehousing.ESW_ToApprove.getIndex() + "或" + EnumStatusWarehousing.ESW_Approved.getIndex();
	public static final String FIELD_ERROR_date1AndDate2 = "非法时间格式！正确的时间格式应为：" + BaseAction.DATETIME_FORMAT_Default3;
	public static final String FIELD_ERROR_staffID = "staffID必须大于0";
	public static final String FIELD_ERROR_shopID = "shopID必须大于0";
	public static final String FIELD_ERROR_VirtualShopID = "ShopID不能是虚拟门店ID";
	public static final String FIELD_ERROR_warehouseID = "warehouseID必须大于0";
	public static final String FIELD_ERROR_purchasingOrderID = "purchasingOrderID必须大于0";
	public static final String FIELD_ERROR_approverID = "approverID必须大于0";
	public static final String FIELD_ERROR_providerID = "providerID必须大于0";

	protected List<Commodity> listCommodity; // 商品表
	protected Provider provider;
	protected Warehouse warehouse;
	protected Shop shop;

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

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	protected int shopID;

	protected int status;

	protected int warehouseID;

	protected int staffID;

	protected Date createDatetime;

	protected int purchasingOrderID;

	private int approverID;

	protected int providerID;

	protected String sn;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getProviderID() {
		return providerID;
	}

	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}

	public int getApproverID() {
		return approverID;
	}

	public void setApproverID(int approverID) {
		this.approverID = approverID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getWarehouseID() {
		return warehouseID;
	}

	public void setWarehouseID(int warehouseID) {
		this.warehouseID = warehouseID;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getPurchasingOrderID() {
		return purchasingOrderID;
	}

	public void setPurchasingOrderID(int purchasingOrderID) {
		this.purchasingOrderID = purchasingOrderID;
	}

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	protected String creatorName;

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	protected String approverName;

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	protected String warehouseName;

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	protected String purchasingOrderSN;

	public String getPurchasingOrderSN() {
		return purchasingOrderSN;
	}

	public void setPurchasingOrderSN(String purchasingOrderSN) {
		this.purchasingOrderSN = purchasingOrderSN;
	}

	// public static float DEFINE_GET_TotalNO(float totalNO) {
	// return totalNO;
	// }

	protected int isModified; // 非数据表字段

	public int getIsModified() {
		return isModified;
	}

	public void setIsModified(int isModified) {
		this.isModified = isModified;
	}

	protected int messageID; // 非数据表字段

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
		return "Warehousing [listCommodity=" + listCommodity + ", provider=" + provider + ", warehouse=" + warehouse + ", shop=" + shop + ", shopID=" + shopID + ", status=" + status + ", warehouseID=" + warehouseID + ", staffID=" + staffID
				+ ", createDatetime=" + createDatetime + ", purchasingOrderID=" + purchasingOrderID + ", approverID=" + approverID + ", providerID=" + providerID + ", sn=" + sn + ", staffName=" + staffName + ", creatorName=" + creatorName
				+ ", approverName=" + approverName + ", warehouseName=" + warehouseName + ", purchasingOrderSN=" + purchasingOrderSN + ", isModified=" + isModified + ", messageID=" + messageID + "]";
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}
		Warehousing warehousingSheet = (Warehousing) arg0;
		if ((ignoreIDInComparision == true ? true : warehousingSheet.getID() == this.getID() && printComparator(field.getFIELD_NAME_ID())) //
				&& warehousingSheet.getWarehouseID() == this.getWarehouseID() && printComparator(field.getFIELD_NAME_warehouseID()) //
				&& warehousingSheet.getStaffID() == this.getStaffID() && printComparator(field.getFIELD_NAME_staffID())//
				&& warehousingSheet.getPurchasingOrderID() == this.getPurchasingOrderID() && printComparator(field.getFIELD_NAME_purchasingOrderID())//
				&& warehousingSheet.getApproverID() == this.getApproverID() && printComparator(field.getFIELD_NAME_approverID())//
				&& warehousingSheet.getShopID() == this.getShopID() && printComparator(field.getFIELD_NAME_shopID())//
		) {
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && warehousingSheet.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && warehousingSheet.getListSlave1() != null) {
					if (warehousingSheet.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && warehousingSheet.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && warehousingSheet.getListSlave1() != null) {
					if (listSlave1.size() != warehousingSheet.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						WarehousingCommodity warehousingCommodity = (WarehousingCommodity) listSlave1.get(i);
						warehousingCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < warehousingSheet.getListSlave1().size(); j++) {
							WarehousingCommodity whc = (WarehousingCommodity) warehousingSheet.getListSlave1().get(j);
							if (warehousingCommodity.getCommodityID() == whc.getCommodityID()) {
								exist = true;
								if (warehousingCommodity.compareTo(whc) != 0) {
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
		Warehousing obj = new Warehousing();
		obj.setID(ID);
		obj.setStatus(status);
		obj.setWarehouseID(warehouseID);
		obj.setStaffID(staffID);
		obj.setPurchasingOrderID(purchasingOrderID);
		obj.setApproverID(approverID);
		obj.setProviderID(providerID);
		obj.setCreateDatetime(createDatetime);
		obj.setSn(sn);
		obj.setShopID(shopID);
		if (this.listSlave1 != null) {
			List<WarehousingCommodity> list = new ArrayList<WarehousingCommodity>();
			for (Object o : listSlave1) {
				WarehousingCommodity wc = (WarehousingCommodity) o;
				list.add((WarehousingCommodity) wc.clone());
			}
			obj.setListSlave1(list);
		}

		obj.setMessageID(messageID);// 标记审核入库时创建消息的messageID
		obj.setPurchasingOrderSN(purchasingOrderSN);// 入库单对应的采购订单SN
		return obj;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Warehousing warehousingSheet = (Warehousing) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_shopID(), warehousingSheet.getShopID());
		params.put(field.getFIELD_NAME_providerID(), warehousingSheet.getProviderID());
		params.put(field.getFIELD_NAME_warehouseID(), warehousingSheet.getWarehouseID());
		params.put(field.getFIELD_NAME_staffID(), warehousingSheet.getStaffID());
		params.put(field.getFIELD_NAME_purchasingOrderID(), warehousingSheet.getPurchasingOrderID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		checkParameterInput(bm);
		Warehousing warehousingSheet = (Warehousing) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_RetrieveNWarhousingByOrderID:
			params.put(field.getFIELD_NAME_purchasingOrderID(), warehousingSheet.getPurchasingOrderID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getPurchasingOrderID());

			break;
		case BaseBO.CASE_RetrieveNWarhousingByFields:
			params.put(field.getFIELD_NAME_queryKeyword(), warehousingSheet.getQueryKeyword() == null ? "" : warehousingSheet.getQueryKeyword());
			params.put(field.getFIELD_NAME_shopID(), warehousingSheet.getShopID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getShopID());
			params.put(field.getFIELD_NAME_staffID(), warehousingSheet.getStaffID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getStaffID());
			params.put(field.getFIELD_NAME_status(), warehousingSheet.getStatus());
			params.put(field.getFIELD_NAME_providerID(), warehousingSheet.getProviderID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getProviderID());
			params.put("dtStart", warehousingSheet.getDate1() == null ? 0 : sdf.format(warehousingSheet.getDate1()));
			params.put("dtEnd", warehousingSheet.getDate2() == null ? 0 : sdf.format(warehousingSheet.getDate2()));
			params.put(field.getFIELD_NAME_iPageIndex(), warehousingSheet.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), warehousingSheet.getPageSize());

			break;
		default:
			params.put(field.getFIELD_NAME_ID(), warehousingSheet.getID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getID());
			params.put(field.getFIELD_NAME_providerID(), warehousingSheet.getProviderID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getProviderID());
			params.put(field.getFIELD_NAME_warehouseID(), warehousingSheet.getWarehouseID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getWarehouseID());
			params.put(field.getFIELD_NAME_staffID(), warehousingSheet.getStaffID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getStaffID());
			params.put(field.getFIELD_NAME_purchasingOrderID(), warehousingSheet.getPurchasingOrderID() == 0 ? BaseAction.INVALID_ID : warehousingSheet.getPurchasingOrderID());
			params.put(field.getFIELD_NAME_iPageIndex(), warehousingSheet.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), warehousingSheet.getPageSize());

			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Warehousing warehousing = (Warehousing) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveWarhousing:
			params.put(field.getFIELD_NAME_ID(), warehousing.getID());
			// params.put("bToApproveStartValue", warehousing.getInt1()); //此参数已去除
			params.put(field.getFIELD_NAME_approverID(), warehousing.getApproverID());

			return params;
		default:
			params.put(field.getFIELD_NAME_ID(), warehousing.getID());
			params.put(field.getFIELD_NAME_warehouseID(), warehousing.getWarehouseID());
			params.put(field.getFIELD_NAME_providerID(), warehousing.getProviderID());

			return params;
		}

	}

	@Override
	public Map<String, Object> getUpdateParamEx(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		Warehousing warehousing = (Warehousing) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveWarhousing:
			params.put(field.getFIELD_NAME_ID(), warehousing.getID());
			// params.put("bToApproveStartValue", warehousing.getInt1()); //此参数已去除
			params.put(field.getFIELD_NAME_approverID(), warehousing.getApproverID());

			return params;
		default:
			params.put(field.getFIELD_NAME_ID(), warehousing.getID());
			params.put(field.getFIELD_NAME_warehouseID(), warehousing.getWarehouseID());
			params.put(field.getFIELD_NAME_providerID(), warehousing.getProviderID());

			return params;
		}

	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_providerID, sbError) && !FieldFormat.checkID(providerID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_warehouseID(), FIELD_ERROR_warehouseID, sbError) && !FieldFormat.checkID(warehouseID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_staffID, sbError) && !FieldFormat.checkID(staffID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_shopID, sbError) && !FieldFormat.checkID(shopID)) {
			return sbError.toString();
		}
		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_VirtualShopID, sbError) && shopID == BaseAction.VirtualShopID) //
		{
			return sbError.toString();
		}

		return "";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		case BaseBO.CASE_ApproveWarhousing:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_approverID(), FIELD_ERROR_approverID, sbError) && !FieldFormat.checkID(approverID)) {
				return sbError.toString();
			}
			// if (this.printCheckField("bToApproveStartValue",
			// FIELD_ERROR_bToApproveStartValue, sbError) && (int1 < 0 || int1 > 1)) {
			// return sbError.toString();
			// } //此参数已去除
			return "";
		default:
			if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) {
				return sbError.toString();
			}

			if (this.printCheckField(field.getFIELD_NAME_warehouseID(), FIELD_ERROR_warehouseID, sbError) && !FieldFormat.checkID(warehouseID)) {
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
		case BaseBO.CASE_RetrieveNWarhousingByOrderID:
			if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_purchasingOrderID()), sbError) && purchasingOrderID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			return "";
		case BaseBO.CASE_RetrieveNWarhousingByFields:
			queryKeyword = queryKeyword == null ? "" : queryKeyword; // stirng1为null时默认是"";
			if (this.printCheckField(field.getFIELD_NAME_queryKeyword(), FIELD_ERROR_queryKeyword, sbError) && queryKeyword.length() > MaxQueryKeywordLength) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_shopID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_shopID()), sbError) && shopID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_staffID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_staffID()), sbError) && staffID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !EnumStatusWarehousing.inBoundForRetrieveN(status)) {
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
		default:
			if (this.printCheckField(field.getFIELD_NAME_providerID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_providerID()), sbError) && providerID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_warehouseID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_warehouseID()), sbError) && warehouseID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_staffID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_staffID()), sbError) && staffID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			if (this.printCheckField(field.getFIELD_NAME_purchasingOrderID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_purchasingOrderID()), sbError) && purchasingOrderID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			return "";
		}
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	public enum EnumStatusWarehousing {
		ESW_ToApprove("ToApprove", 0), //
		ESW_Approved("Approved", 1);

		private String name;
		private int index;

		private EnumStatusWarehousing(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBoundForRetrieveN(int index) {
			if (index != BaseAction.INVALID_STATUS && index != ESW_ToApprove.getIndex() && index != ESW_Approved.getIndex()) {
				return false;
			}

			return true;
		}

		public static boolean inBound(int index) {
			if (index < ESW_ToApprove.getIndex() || index > ESW_Approved.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumStatusWarehousing c : EnumStatusWarehousing.values()) {
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
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			status = jo.getInt(field.getFIELD_NAME_status());
			providerID = jo.getInt(field.getFIELD_NAME_providerID());
			warehouseID = jo.getInt(field.getFIELD_NAME_warehouseID());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			approverID = jo.getInt(field.getFIELD_NAME_approverID());
			sn = jo.getString(field.getFIELD_NAME_sn());
			String tmpCreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(tmpCreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpCreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + tmpCreateDatetime);
				}
			}
			purchasingOrderID = jo.getInt(field.getFIELD_NAME_purchasingOrderID());
			String tmpUpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(tmpUpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(tmpUpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + tmpUpdateDatetime);
				}
			}
			// 查询入库单对应的入库商品集合
			// 查询子商品集合
			List<WarehousingCommodity> listWhousingCommSlave = new ArrayList<>();
			JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				JSONObject object = jr.getJSONObject(i);
				WarehousingCommodity whComm = (WarehousingCommodity) new WarehousingCommodity().parse1(object.toString());
				listWhousingCommSlave.add(whComm);
			}
			listSlave1 = listWhousingCommSlave;
			messageID = jo.getInt(field.getFIELD_NAME_messageID());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	@Override
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_WarehousingCacheSize.getIndex();
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> warehousingList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Warehousing wareshousing = new Warehousing();
				wareshousing = (Warehousing) wareshousing.doParse1(jsonObject);
				warehousingList.add(wareshousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return warehousingList;
	}
}
