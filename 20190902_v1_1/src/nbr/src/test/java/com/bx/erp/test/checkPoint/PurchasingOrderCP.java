package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PurchasingOrderCP {

	@Resource
	private static PurchasingOrderBO purchasingOrderBO;

	@Resource
	private static PurchasingOrderCommodityBO purchasingOrderCommodityBO;

	/** 1、采购单A普通缓存是否创建，采购单A的ListSlave1()中采购商品数据是否正确。
	 * 2、检查数据库T_PurchasingOrder是否有创建采购单A的数据。
	 * 3、检查数据库T_PurchasingOrderCommodity是否有创建采购单A相关的采购单商品数据。
	 * 4、检查数据库T_Message是否有采购单创建的消息提醒。（微信验证） */
	public static void verifyCreate(MvcResult mr, PurchasingOrder purchasingOrder, PurchasingOrderBO purchasingOrderBO, String dbName) throws Exception {
		// 结果验证：判断DB是否创建正确
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder2 = new PurchasingOrder();
		purchasingOrder2 = (PurchasingOrder) purchasingOrder2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertFalse(purchasingOrder2 == null);
		// 对对象设置后进行比较
		PurchasingOrder purchasingOrderClone = (PurchasingOrder) purchasingOrder.clone();
		// 获取参数
		Staff staff = (Staff) mr.getRequest().getSession().getAttribute(EnumSession.SESSION_Staff.getName());
		String remark = mr.getRequest().getParameter(PurchasingOrder.field.getFIELD_NAME_remark());
		String providerID = mr.getRequest().getParameter(PurchasingOrder.field.getFIELD_NAME_providerID());
		String shopID = mr.getRequest().getParameter(PurchasingOrder.field.getFIELD_NAME_shopID());
		// 设置参数
		purchasingOrderClone.setRemark(remark);
		purchasingOrderClone.setProviderID(Integer.parseInt(providerID));
		purchasingOrderClone.setShopID(Integer.parseInt(shopID));
		purchasingOrderClone.setStaffID(staff.getID());
		// purchasingOrderClone.setListSlave1(listSlave1);
		//
		purchasingOrder2.setIgnoreIDInComparision(true);
		purchasingOrder2.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(purchasingOrder2.compareTo(purchasingOrderClone) == 0, "创建失败");
		// 结果验证：采购单A普通缓存是否创建，采购单A的ListSlave1()中采购商品数据是否正确
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).read1(purchasingOrder2.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null && bm.getID() == purchasingOrder2.getID(), "普通缓存不存在本次创建出来的对象");
		// 验证从表
		for (Object object2 : bm.getListSlave1()) {
			Assert.assertTrue(bm.getID() == ((PurchasingOrderCommodity) object2).getPurchasingOrderID(), "普通缓存不存在本次创建出来对象的从表");
		}
		// 2.检查数据库T_PurchasingOrder是否有创建采购单A的数据。
		// 3、检查数据库T_PurchasingOrderCommodity是否有创建采购单A相关的采购单商品数据。
		verifyCheckPurchasingOrderAndCheckPurchasingOrderCommodity(purchasingOrderBO, purchasingOrder2, dbName);
	}

	/** 1、采购单A普通缓存是否修改，采购单A的ListSlave1()中采购商品数据是否正确。
	 * 2、检查数据库T_PurchasingOrder是否有修改的采购单A数据是否正确。
	 * 3、检查数据库T_PurchasingOrderCommodity，检查修改后的采购单商品数据是否正确。 */
	public static void verifyUpdate(MvcResult mr, PurchasingOrder purchasingOrder, PurchasingOrderBO purchasingOrderBO, String dbName) throws Exception {
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder2 = new PurchasingOrder();
		purchasingOrder2 = (PurchasingOrder) purchasingOrder2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertFalse(purchasingOrder2 == null);
		//
		PurchasingOrder purchasingOrderClone = (PurchasingOrder) purchasingOrder.clone();
		//
		purchasingOrder2.setIgnoreIDInComparision(true);
		Assert.assertTrue(purchasingOrder2.compareTo(purchasingOrderClone) == 0, "修改失败");
		// 结果验证：采购单A普通缓存是否创建，采购单A的ListSlave1()中采购商品数据是否正确
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).read1(purchasingOrder2.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(purchasingOrder2.compareTo(bm) == 0, "普通缓存未进行修改");
		Assert.assertTrue(bm != null && bm.getID() == purchasingOrder2.getID(), "普通缓存不存在本次修改的对象");
		// 验证从表
		for (Object object2 : bm.getListSlave1()) {
			Assert.assertTrue(bm.getID() == ((PurchasingOrderCommodity) object2).getPurchasingOrderID(), "普通缓存不存在本次修改的对象从表");
		}
		// 2、检查数据库T_PurchasingOrder是否有修改的采购单A数据是否正确。
		// 3、检查数据库T_PurchasingOrderCommodity，检查修改后的采购单商品数据是否正确。
		verifyCheckPurchasingOrderAndCheckPurchasingOrderCommodity(purchasingOrderBO, purchasingOrderClone, dbName);
	}

	/** 1、采购单A普通缓存是否删除。
	 * 2、检查数据库T_PurchasingOrder中，采购单A的F_Status是否为4(已删除)，F_ProviderID是否为NULL。 */
	public static void verifyDelete(PurchasingOrder purchasingOrder, PurchasingOrderBO purchasingOrderBO, String dbName) throws Exception {
		PurchasingOrder purchasingOrderclone = (PurchasingOrder) purchasingOrder.clone();
		// 1、采购单A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).read1(purchasingOrderclone.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm == null, "普通缓存存在本次删除的对象");
		// 2、检查数据库T_PurchasingOrder中，采购单A的F_Status是否为4(已删除)，F_ProviderID是否为NULL。
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, purchasingOrderclone);
		if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个入库单失败，错误码=" + purchasingOrderBO.getLastErrorCode() + "，错误信息=" + purchasingOrderBO.getLastErrorMessage());
		}
		Assert.assertTrue(poList.get(0).size() == 0, "DB中的采购订单没有被删除");
	}

	/** 1、采购单A普通缓存F_Status是否为1(已审核)。如果审核前有修改操作，还需要检查采购单A以及采购商品是否正确修改。
	 * 2、检查数据库T_PurchasingOrder，采购单A的F_Status是否为1(已审核)。如果审核前有修改操作，还需要检查采购单A是否正确修改。
	 * 3、检查数据库T_PurchasingOrderCommodity，查看采购单A的采购商品F_Name是否更新为最新商品名称。如果审核前有修改操作，还需要检查采购商品的其他信息。
	 * 4、检查数据库T_ProviderCommodity，查看采购单A中的采购商品供应商关联是否正确修改。 */

	@SuppressWarnings("unchecked")
	public static void verifyApprove(PurchasingOrder purchasingOrder, PurchasingOrderBO purchasingOrderBO, ProviderCommodityBO providerCommodityBO, String dbName) throws Exception {
		PurchasingOrder purchasingOrderclone = (PurchasingOrder) purchasingOrder.clone();
		// 1、采购单A普通缓存F_Status是否为1(已审核)。如果审核前有修改操作，还需要检查采购单A以及采购商品是否正确修改。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).read1(purchasingOrderclone.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(((PurchasingOrder) bm).getStatus() == 1, "普通缓存审核状态异常");
		// 2、检查数据库T_PurchasingOrder，采购单A的F_Status是否为1(已审核)。如果审核前有修改操作，还需要检查采购单A是否正确修改。
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, purchasingOrderclone);
		if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个入库单失败，错误码=" + purchasingOrderBO.getLastErrorCode() + "，错误信息=" + purchasingOrderBO.getLastErrorMessage());
		}
		Assert.assertTrue(poList.get(0).size() != 0 && ((PurchasingOrder) poList.get(0).get(0)).getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex(), "DB中的采购订单状态异常");
		// 3、检查数据库T_PurchasingOrderCommodity，查看采购单A的采购商品F_Name是否更新为最新商品名称。如果审核前有修改操作，还需要检查采购商品的其他信息。
		ErrorInfo errorInfo = new ErrorInfo();
		Commodity commodity = null;
		for (Object object : purchasingOrder.getListSlave1()) {
			PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) object;
			commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(purchasingOrderCommodity.getCommodityID(), BaseBO.SYSTEM, errorInfo, dbName);
			assert commodity != null;
			assertTrue(commodity.getName().equals(purchasingOrderCommodity.getCommodityName()), "商品名称未进行修改");
			//
			ProviderCommodity providerCommodity = new ProviderCommodity();
			providerCommodity.setCommodityID(purchasingOrderCommodity.getCommodityID());
			providerCommodity.setPageSize(BaseAction.PAGE_SIZE_MAX);
			DataSourceContextHolder.setDbName(dbName);
			List<ProviderCommodity> pcList = (List<ProviderCommodity>) providerCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, providerCommodity);
			Assert.assertTrue(providerCommodityBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "错误信息=" + providerCommodityBO.printErrorInfo());
			Assert.assertTrue(pcList != null, "providerCommodity retrieveNObject失败，pcList为null");
			boolean bExist = false;
			for (ProviderCommodity pc : pcList) {
				if (pc.getProviderID() == purchasingOrderclone.getProviderID() && pc.getCommodityID() == purchasingOrderCommodity.getCommodityID()) {
					bExist = true;
					break;
				}
			}
			Assert.assertTrue(bExist, "供应商商品创建失败");
		}
	}

	/** 验证T_PurchasingOrder 与 T_PurchasingOrderCommodity 是否有创建采购单A相关的数据
	 * 
	 * @param purchasingOrderBO
	 * @param purchasingOrder
	 */
	private static void verifyCheckPurchasingOrderAndCheckPurchasingOrderCommodity(PurchasingOrderBO purchasingOrderBO, BaseModel purchasingOrder, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, purchasingOrder);
		assertTrue(poList != null && poList.get(0).size() != 0);
		//
		PurchasingOrder po = (PurchasingOrder) poList.get(0).get(0);
		assertTrue(po.getID() == purchasingOrder.getID());
		//
		List<BaseModel> poc = poList.get(1);
		for (BaseModel bm : poc) {
			assertTrue(((PurchasingOrderCommodity) bm).getPurchasingOrderID() == po.getID());
		}
	}

}
