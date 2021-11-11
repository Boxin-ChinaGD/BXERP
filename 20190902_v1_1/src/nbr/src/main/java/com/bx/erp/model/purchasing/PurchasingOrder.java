package com.bx.erp.model.purchasing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Shop;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.FieldFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PurchasingOrder extends BaseModel {
	private static final long serialVersionUID = -4993370173233248296L;
	public static final PurchasingOrderField field = new PurchasingOrderField();

	public static int MIN_Status = EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex();
	public static int MAX_Status = EnumStatusPurchasingOrder.ESPO_Deleted.getIndex();
	public static int MAX_LengthRemark = 128;
	public static int MAX_LengthQueryKeyword = 32;

	public static final String FIELD_ERROR_QueryKeyword = "搜索关键字不能为空并且要小于 " + MAX_LengthQueryKeyword + "个字符";
	public static final String FIELD_ERROR_StaffID = "StaffID必须>0";
	public static final String FIELD_ERROR_ApproveID = "ApproveID必须>0";
	public static final String FIELD_ERROR_ProviderID = "ProviderID必须>0";
	public static final String FIELD_ERROR_ShopID = "ShopID必须>0";
	public static final String FIELD_ERROR_VirtualShopID = "ShopID不能是虚拟门店ID";
	public static final String FIELD_ERROR_Remark = "备注的长度不能超过" + MAX_LengthRemark + "个字符";
	public static final String FIELD_ERROR_Status = "采购订单的状态只能在" + MIN_Status + "到" + MAX_Status + "之间";
	public static final String FIELD_ERROR_Date = "传入的日期时间不正确 ";

	protected Shop shop; // 门店
	protected Provider provider;// 供应商
	protected List<Provider> listProvider;
	protected List<Commodity> listCommodity; // 商品表
	protected ProviderCommodity providerCommodity;// 供应商商品表

	protected int status;

	private Date approveDatetime;

	private Date endDatetime;

	private int staffID;

	private Date updateDatetime;

	protected int providerID;
	
	protected int shopID;

	protected String providerName;

	protected String remark;

	private int approverID;

	private String sn;

	private int messageID;

	/** 一个采购订单能入库的最大次数 */
	public static final int MAX_WarehousingNO = 3; // 非表字段,在机器人中使用到。

	/** 记录本采购订单已经入库多少次。用于控制入库频率：一张采购订单不能太多次入库 */
	protected int warehousingNO; // 非表字段,在机器人中使用到。

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
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

	public int getApproverID() {
		return approverID;
	}

	public void setApproverID(int approverID) {
		this.approverID = approverID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getProviderID() {
		return providerID;
	}

	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getApproveDatetime() {
		return approveDatetime;
	}

	public void setApproveDatetime(Date approveDatetime) {
		this.approveDatetime = approveDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public List<Provider> getListProvider() {
		return listProvider;
	}

	public void setListProvider(List<Provider> listProvider) {
		this.listProvider = listProvider;
	}

	public List<Commodity> getListCommodity() {
		return listCommodity;
	}

	public void setListCommodity(List<Commodity> listCommodity) {
		this.listCommodity = listCommodity;
	}

	public ProviderCommodity getProviderCommodity() {
		return providerCommodity;
	}

	public void setProviderCommodity(ProviderCommodity providerCommodity) {
		this.providerCommodity = providerCommodity;
	}

	protected String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public static int getMAX_WarehousingNO() {
		return MAX_WarehousingNO;
	}

	public int getWarehousingNO() {
		return warehousingNO;
	}

	public void setWarehousingNO(int warehousingNO) {
		this.warehousingNO = warehousingNO;
	}

	protected int isModified; // 非数据表字段

	public int getIsModified() {
		return isModified;
	}

	public void setIsModified(int isModified) {
		this.isModified = isModified;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "PurchasingOrder [shop=" + shop + ", provider=" + provider + ", listProvider=" + listProvider + ", listCommodity=" + listCommodity + ", providerCommodity=" + providerCommodity + ", status=" + status + ", approveDatetime="
				+ approveDatetime + ", endDatetime=" + endDatetime + ", staffID=" + staffID + ", updateDatetime=" + updateDatetime + ", providerID=" + providerID + ", shopID=" + shopID + ", providerName=" + providerName + ", remark="
				+ remark + ", approverID=" + approverID + ", sn=" + sn + ", messageID=" + messageID + ", warehousingNO=" + warehousingNO + ", staffName=" + staffName + ", isModified=" + isModified + "]";
	}

	@Override
	protected BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			sn = jo.getString(field.getFIELD_NAME_sn());
			status = jo.getInt(field.getFIELD_NAME_status());
			staffID = jo.getInt(field.getFIELD_NAME_staffID());
			providerID = jo.getInt(field.getFIELD_NAME_providerID());
			shopID = jo.getInt(field.getFIELD_NAME_shopID());
			providerName = jo.getString(field.getFIELD_NAME_providerName());
			remark = jo.getString(field.getFIELD_NAME_remark());
			approverID = jo.getInt(field.getFIELD_NAME_approverID());
			//
			String CreateDatetime = jo.getString(field.getFIELD_NAME_createDatetime());
			if (!"".equals(CreateDatetime)) {
				createDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(CreateDatetime);
				if (createDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_createDatetime() + "=" + CreateDatetime);
				}
			}
			//
			String UpdateDatetime = jo.getString(field.getFIELD_NAME_updateDatetime());
			if (!"".equals(UpdateDatetime)) {
				updateDatetime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1).parse(UpdateDatetime);
				if (updateDatetime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_updateDatetime() + "=" + UpdateDatetime);
				}
			}
			// 采购订单从表
			List<PurchasingOrderCommodity> POlistSlave = new ArrayList<PurchasingOrderCommodity>();
			JSONArray jr = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < jr.size(); i++) {
				JSONObject object = jr.getJSONObject(i);
				PurchasingOrderCommodity poc = (PurchasingOrderCommodity) new PurchasingOrderCommodity().parse1(object.toString());
				POlistSlave.add(poc);
			}
			listSlave1 = POlistSlave;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return this;
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> purchasingOrderList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				PurchasingOrder purchasingOrder = new PurchasingOrder();
				purchasingOrder.doParse1(jsonObject);
				purchasingOrderList.add(purchasingOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return purchasingOrderList;
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		PurchasingOrder p = new PurchasingOrder();
		p.setID(ID);
		p.setStatus(status);
		p.setStaffID(staffID);
		p.setProviderID(providerID);
		p.setShopID(shopID);
		p.setProviderName(providerName);
		p.setApproverID(approverID);
		p.setRemark(remark);
		p.setCreateDatetime(createDatetime);
		p.setApproveDatetime(approveDatetime);
		p.setUpdateDatetime(updateDatetime);
		p.setMessageID(messageID);
		p.setSn(sn);

		if (listSlave1 != null) {
			List<PurchasingOrderCommodity> list = new ArrayList<PurchasingOrderCommodity>();
			for (Object o : listSlave1) {
				PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) o;
				list.add((PurchasingOrderCommodity) purchasingOrderCommodity.clone());
			}
			p.setListSlave1(list);
		}

		return p;
	}

	public int compareTo(final BaseModel arg0) {
		PurchasingOrder po = (PurchasingOrder) arg0;

		if ((ignoreIDInComparision == true ? true : po.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& po.getStatus() == status && printComparator(field.getFIELD_NAME_status())//
				&& po.getStaffID() == staffID && printComparator(field.getFIELD_NAME_staffID())//
				// && po.getApproverID() == this.getApproverID() &&
				// printComparator(getFIELD_NAME_approverID())//
				&& po.getProviderID() == providerID && printComparator(field.getFIELD_NAME_providerID())//
				&& po.getShopID() == shopID && printComparator(field.getFIELD_NAME_shopID())//
				&& po.getRemark().equals(remark) && printComparator(field.getFIELD_NAME_remark())//
		) {
			if (!ignoreSlaveListInComparision) {
				if (listSlave1 == null && po.getListSlave1() == null) {
					return 0;
				}
				if (listSlave1 == null && po.getListSlave1() != null) {
					if (po.getListSlave1().size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && po.getListSlave1() == null) {
					if (listSlave1.size() == 0) {
						return 0;
					}
					return -1;
				}
				if (listSlave1 != null && po.getListSlave1() != null) {
					if (listSlave1.size() != po.getListSlave1().size()) {
						return -1;
					}
					for (int i = 0; i < listSlave1.size(); i++) {
						PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) listSlave1.get(i);
						purchasingOrderCommodity.setIgnoreIDInComparision(ignoreIDInComparision); // 主表不比较ID，那么从表也不比较
						Boolean exist = false;
						for (int j = 0; j < po.getListSlave1().size(); j++) {
							PurchasingOrderCommodity poc = (PurchasingOrderCommodity) po.getListSlave1().get(j);
							if (purchasingOrderCommodity.getCommodityID() == poc.getCommodityID()) {
								exist = true;
								if (purchasingOrderCommodity.compareTo(poc) != 0) {
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
		checkParameterInput(bm);

		PurchasingOrder po = (PurchasingOrder) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_shopID(), po.getShopID());
		params.put(field.getFIELD_NAME_status(), po.getStatus());
		params.put(field.getFIELD_NAME_staffID(), po.getStaffID());
		params.put(field.getFIELD_NAME_providerID(), po.getProviderID());
		params.put(field.getFIELD_NAME_remark(), po.getRemark() == null ? "" : po.getRemark());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieve1ParamEx(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		PurchasingOrder po = (PurchasingOrder) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_ID(), po.getID());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);
		PurchasingOrder po = (PurchasingOrder) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_PurchasingOrder_RetrieveNByNFields:
			params.put(field.getFIELD_NAME_queryKeyword(), po.getQueryKeyword() == null ? "" : po.getQueryKeyword());
			params.put(field.getFIELD_NAME_staffID(), po.getStaffID());
			params.put(field.getFIELD_NAME_date1(), po.getDate1());
			params.put(field.getFIELD_NAME_date2(), po.getDate2());
			params.put(field.getFIELD_NAME_iPageIndex(), po.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), po.getPageSize());

			break;
		default:
			params.put(field.getFIELD_NAME_createDatetime(), po.getCreateDatetime());
			params.put(field.getFIELD_NAME_status(), po.getStatus());
			// params.put(field.getFIELD_NAME_ID(), po.getID() == 0 ? BaseAction.INVALID_ID
			// : po.getID());
			params.put(field.getFIELD_NAME_queryKeyword(), po.getQueryKeyword() == null ? "" : po.getQueryKeyword());
			params.put(field.getFIELD_NAME_staffID(), po.getStaffID() == 0 ? BaseAction.INVALID_ID : po.getStaffID());
			params.put(field.getFIELD_NAME_shopID(), po.getShopID() == 0 ? BaseAction.INVALID_ID : po.getShopID());
			params.put(field.getFIELD_NAME_date1(), po.getDate1());
			params.put(field.getFIELD_NAME_date2(), po.getDate2());
			params.put(field.getFIELD_NAME_iPageIndex(), po.getPageIndex());
			params.put(field.getFIELD_NAME_iPageSize(), po.getPageSize());

			break;
		}

		return params;
	}

	@Override
	public Map<String, Object> getUpdateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);
		PurchasingOrder po = (PurchasingOrder) bm;
		Map<String, Object> params = new HashMap<String, Object>();

		switch (iUseCaseID) {
		case BaseBO.CASE_ApprovePurchasingOrder:
			params.put(field.getFIELD_NAME_ID(), po.getID());
			params.put(field.getFIELD_NAME_approverID(), po.getApproverID());

			break;
		case BaseBO.CASE_UpdatePurchasingOrderStatus:
			params.put(field.getFIELD_NAME_status(), po.getStatus());
			params.put(field.getFIELD_NAME_ID(), po.getID());

			break;
		default:
			params.put(field.getFIELD_NAME_ID(), po.getID());
			params.put(field.getFIELD_NAME_providerID(), po.getProviderID());
			params.put(field.getFIELD_NAME_remark(), po.getRemark());
			break;
		}

		return params;
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_ID(), FieldFormat.FIELD_ERROR_ID, sbError) && !FieldFormat.checkID(ID)) //
		{
			return sbError.toString();
		}

		switch (iUseCaseID) {
		case BaseBO.CASE_ApprovePurchasingOrder:
			if (this.printCheckField(field.getFIELD_NAME_approverID(), FIELD_ERROR_ApproveID, sbError) && !FieldFormat.checkID(approverID)) //
			{
				return sbError.toString();
			}

			break;
		case BaseBO.CASE_UpdatePurchasingOrderStatus:
			if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_Status, sbError) && !EnumStatusPurchasingOrder.inBound(status)) //
			{
				return sbError.toString();
			}

			break;
		default:
			if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_ProviderID, sbError) && !FieldFormat.checkID(providerID)) //
			{
				return sbError.toString();
			}

			if (!StringUtils.isEmpty(remark)) {
				if (this.printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_Remark, sbError) && remark.length() > MAX_LengthRemark) //
				{
					return sbError.toString();
				}
			}

			break;
		}
		return "";
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		switch (iUseCaseID) {
		case BaseBO.CASE_PurchasingOrder_RetrieveNByNFields:
			// 功能已经合并到RN中，这里不做验证，只在RN中做验证即可
			return "";
		default:
			if (status != BaseAction.INVALID_STATUS) {
				if (this.printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_Status, sbError) && !EnumStatusPurchasingOrder.inBound(status)) //
				{
					return sbError.toString();
				}
			}

			if (!StringUtils.isEmpty(queryKeyword)) {
				if (queryKeyword.length() > MAX_LengthQueryKeyword) {
					return FIELD_ERROR_QueryKeyword;
				}
			}

			// if (staffID != 0) {
			// if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID,
			// sbError) && !FieldFormat.checkID(staffID)) //
			// {
			// return sbError.toString();
			// }
			// }

			if (this.printCheckField(field.getFIELD_NAME_staffID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_staffID()), sbError) && staffID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			
			if (this.printCheckField(field.getFIELD_NAME_shopID(), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, field.getFIELD_NAME_shopID()), sbError) && shopID < BaseAction.INVALID_ID) {
				return sbError.toString();
			}


			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			if (date1 != null) {
				try {
					simpleDateFormat.format(date1);
				} catch (Exception e) {
					return FIELD_ERROR_Date;
				}
			}

			if (date2 != null) {
				try {
					simpleDateFormat.format(date2);
				} catch (Exception e) {
					return FIELD_ERROR_Date;
				}
			}

			return "";
		}
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (this.printCheckField(field.getFIELD_NAME_staffID(), FIELD_ERROR_StaffID, sbError) && !FieldFormat.checkID(staffID)) //
		{
			return sbError.toString();
		}

		if (this.printCheckField(field.getFIELD_NAME_providerID(), FIELD_ERROR_ProviderID, sbError) && !FieldFormat.checkID(providerID)) //
		{
			return sbError.toString();
		}
		
		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_ShopID, sbError) && !FieldFormat.checkID(shopID)) //
		{
			return sbError.toString();
		}
		
		if (this.printCheckField(field.getFIELD_NAME_shopID(), FIELD_ERROR_VirtualShopID, sbError) && shopID == BaseAction.VirtualShopID) //
		{
			return sbError.toString();
		}

		if (!StringUtils.isEmpty(remark)) {
			if (this.printCheckField(field.getFIELD_NAME_remark(), FIELD_ERROR_Remark, sbError) && remark.length() > MAX_LengthRemark) //
			{
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

	public enum EnumStatusPurchasingOrder {
		ESPO_ToApprove("Not approved", 0), //
		ESPO_Approved("Approved", 1), //
		ESPO_PartWarehousing("PartWarehousing", 2), //
		ESPO_AllWarehousing("AllWarehousing", 3), //
		ESPO_Deleted("Deleted", 4);

		private String name;
		private int index;

		public static boolean inBoundForRetrieveN(int index) {
			if (index != BaseAction.INVALID_STATUS && index != ESPO_ToApprove.getIndex() && index != ESPO_Approved.getIndex() && index != ESPO_PartWarehousing.getIndex() //
					&& index != ESPO_AllWarehousing.getIndex() && index != ESPO_Deleted.getIndex()) {
				return false;
			}

			return true;
		}

		public static boolean inBound(int index) {
			if (index < ESPO_ToApprove.getIndex() || index > ESPO_Deleted.getIndex()) {
				return false;
			}
			return true;
		}

		private EnumStatusPurchasingOrder(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumStatusPurchasingOrder c : EnumStatusPurchasingOrder.values()) {
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
	public int getCacheSizeID() {
		return EnumConfigCacheSizeCache.ECC_PurchasingOrderCacheSize.getIndex();
	}
}
