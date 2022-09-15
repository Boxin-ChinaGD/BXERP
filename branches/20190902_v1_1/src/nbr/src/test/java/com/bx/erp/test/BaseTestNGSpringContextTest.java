package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.MD5Util;

import net.sf.json.JSONObject;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Permission;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;

@ContextConfiguration(locations = { "classpath:ApplicationContext.xml" })
public class BaseTestNGSpringContextTest extends AbstractTestNGSpringContextTests {
	/** 用于生成指定日期的报表，数据库中已经准备好了这天的数据。传递2号，会生成1号的零售单的报表 */
	public static final String REPORT_DATE = "2030-01-02";
	/** 用于生成指定月报的时间。SP会生成2030-01-01~2030-01-31的报表，这里只需要传递某月最后一天的日期时间 */
	public static final String REPORT_DATE_END = "2030-01-31 00:00:00";

	public static final int STAFF_ID3 = 3;
	public static final int STAFF_ID4 = 4;
	public static final int STAFF_ID1 = 1;
	public static final int STAFF_ID2 = 2;

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();

	}

	protected static final MockHttpServletRequestBuilder getPosBuilder(String url, MediaType contentType, Pos p) {
		MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
				.param(Pos.field.getFIELD_NAME_ID(), String.valueOf(p.getID())) //
				.param(Pos.field.getFIELD_NAME_pwdEncrypted(), p.getPwdEncrypted())//
				.param(Pos.field.getFIELD_NAME_companySN(), p.getCompanySN())//
				.param(Pos.field.getFIELD_NAME_pos_SN(), p.getPos_SN())//
				.param(Pos.field.getFIELD_NAME_passwordInPOS(), Shared.PASSWORD_DEFAULT) //
				.param(Pos.field.getFIELD_NAME_shopID(), String.valueOf(p.getShopID()))//
				.param(Pos.field.getFIELD_NAME_returnObject(), "1"); //
		return builder;
	}

	public static class DataInput {
		private static Message msInput = null;
		private static Provider providerInput = null;
		private static Permission permissionInput = null;

		private static Pos posInput = null;
		private static WarehousingCommodity warehousingCommodityInput = null;
		private static SmallSheetText smallSheetTextInput = null;

		public static final SmallSheetText getSmallSheetText() throws CloneNotSupportedException {
			smallSheetTextInput = new SmallSheetText();
			smallSheetTextInput.setContent("小票");
			smallSheetTextInput.setSize(20);
			smallSheetTextInput.setBold(0);
			smallSheetTextInput.setFrameId(1);
			smallSheetTextInput.setGravity(17);
			return (SmallSheetText) smallSheetTextInput.clone();
		}

		public static final RetailTrade getRetailTradeAndRetailTradeCommodity() throws InterruptedException {
			RetailTrade retailTrade = new RetailTrade();
			retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
			Thread.sleep(1000);
			retailTrade.setPos_ID(1);
			retailTrade.setSn(Shared.generateRetailTradeSN(1));
			retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTrade.setSaleDatetime(new Date());
			retailTrade.setLogo("1");
			// retailTrade.setInt2(1); //... 看action暂时不知道有什么用
			retailTrade.setPaymentType(1);
			retailTrade.setPaymentAccount("12");
			retailTrade.setRemark("010");
			retailTrade.setSourceID(-1);
			retailTrade.setAmount(1425d);
			retailTrade.setAmountCash(1425d);
			retailTrade.setSmallSheetID(1);
			retailTrade.setSyncDatetime(new Date());
			retailTrade.setSaleDatetime(new Date());
			retailTrade.setReturnObject(1);// 测试不通过
			retailTrade.setVipID(0);
			retailTrade.setWxOrderSN("");
			retailTrade.setAliPayOrderSN("");
			retailTrade.setWxRefundNO("");
			retailTrade.setDatetimeStart(new Date());
			retailTrade.setDatetimeEnd(new Date());
			retailTrade.setShopID(2);

			RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
			retailTradeCommodity.setCommodityID(1);
			retailTradeCommodity.setNO(1);
			retailTradeCommodity.setPriceOriginal(222.6);
			retailTradeCommodity.setBarcodeID(1);
			retailTradeCommodity.setNOCanReturn(1);
			retailTradeCommodity.setPriceReturn(0.5d);
			retailTradeCommodity.setPriceVIPOriginal(1d);

			List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
			listRetailTradeCommodity.add(retailTradeCommodity);

			retailTrade.setListSlave1(listRetailTradeCommodity);

			return retailTrade;
		}

		public static final Pos getPOS() throws Exception {
			posInput = new Pos();
			posInput.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW)); // 其它测试在获取服务器session时，需要登录，需要用默认密码000000。//如果不设置这个，DB里salt值为空串，导致后面的测试出问题，比如POS无法正常登录
			Thread.sleep(1);
			posInput.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			posInput.setShopID(1);
			posInput.setStatus(EnumStatusPos.ESP_Active.getIndex());
			posInput.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));
			posInput.setCompanySN(Shared.DB_SN_Test);

			return (Pos) posInput.clone();
		}

		public static final Permission getData() throws CloneNotSupportedException {
			permissionInput = new Permission();
			permissionInput.setSP("SP_Permission_Create" + String.valueOf(System.currentTimeMillis()).substring(6));
			permissionInput.setName("删除" + String.valueOf(System.currentTimeMillis()).substring(6));
			permissionInput.setDomain("供应商权限操作" + String.valueOf(System.currentTimeMillis()).substring(6));
			permissionInput.setRemark("供应商的删除" + String.valueOf(System.currentTimeMillis()).substring(6));
			return (Permission) permissionInput.clone();
		}

		public static final Provider getProvider() throws CloneNotSupportedException, InterruptedException {
			providerInput = new Provider();
			providerInput.setName(Shared.getLongestProviderName("淘宝"));
			Thread.sleep(1);
			providerInput.setDistrictID(1);
			providerInput.setAddress("广州市天河区二十八中学" + String.valueOf(System.currentTimeMillis()).substring(7));
			providerInput.setContactName("zda" + String.valueOf(System.currentTimeMillis()).substring(6));
			providerInput.setMobile(Shared.getValidStaffPhone());

			return (Provider) providerInput.clone();
		}

		public static final Message getMessage() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			msInput = new Message();
			msInput.setCategoryID(ran.nextInt(5) + 1);
			msInput.setCompanyID(1);
			msInput.setIsRead(0);
			msInput.setParameter("Json");
			msInput.setSenderID(ran.nextInt(5) + 1);
			msInput.setReceiverID(ran.nextInt(5) + 1);

			return (Message) msInput.clone();
		}

		public static final WarehousingCommodity getWarehousingCommodity() throws CloneNotSupportedException, InterruptedException {
			warehousingCommodityInput = new WarehousingCommodity();
			warehousingCommodityInput.setCommodityID(1);
			warehousingCommodityInput.setPackageUnitID(1);
			warehousingCommodityInput.setNO(new Random().nextInt(200) + 1);
			warehousingCommodityInput.setBarcodeID(1);
			warehousingCommodityInput.setPrice(10.1f);
			warehousingCommodityInput.setAmount(111.1f);
			warehousingCommodityInput.setShelfLife(36);

			return (WarehousingCommodity) warehousingCommodityInput.clone();
		}

	}

	// protected void setPosInDB(PosBO posBO) {
	// Pos p = new Pos();
	// p.setPageIndex(1);
	// p.setPageSize(BaseAction.PAGE_SIZE_Infinite);
	// List<?> listPos = posBO.retrieveNObject(p);
	// if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// assertTrue(false, "获取POS DB数据错误！");
	// }
	// if (listPos.size() < 5) {
	// assertTrue(false, "DB中POS不足5台！");
	// }
	// if (listPos.size() > 5) {
	// System.out.println(("DB中POS超过5台，需要减少至5台·····");
	// for (int i = 5; i < listPos.size(); i++) {
	// p = new Pos();
	// int posID = ((Pos) listPos.get(i)).getID();
	// p.setID(posID);
	// posBO.deleteObject(p);
	// assertTrue(posBO.getLastErrorCode() == EnumErrorCode.EC_NoError,
	// "删除DB中的Pos时出错，posID=" + posID);
	// }
	// }
	// }

	/** 用于SyncActionTest.testCreateEx()的结果验证 TODO 应放到BaseSyncCP中 */
	public static void verifySyncCreateResult(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, BaseBO syncCacheBO, BaseBO syncCacheDispatcherBO, EnumCacheType ect, EnumSyncCacheType esct, int posID, String dbName) throws Exception {
		// 结果验证：判断DB是否创建正确
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel bmOfDB = bmCreateObjet.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(bmCreateObjet) == 0, "创建失败");
		// 判断普通缓存中是否存在C出的对象
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, ect).read1(bmOfDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null && bm.getID() == bmOfDB.getID(), "普通缓存不存在本次创建出来的对象");
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 结果验证：判断同步缓存中是否有创建出来的对象
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
			boolean b = false;
			BaseSyncCache baseSyncCache = null;
			for (BaseModel bms : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bms;
				if (baseSyncCache.getSyncData_ID() == bmOfDB.getID()) {
					b = true;
					break;
				}

			}
			Assert.assertTrue(b == true, "同步缓存不存在创建出来的对象");
			b = false;
			// 结果验证：同步缓存DB里有无相应的对象
			DataSourceContextHolder.setDbName(dbName);
			List<?> bcslist = syncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			List<Integer> listID = new ArrayList<Integer>();
			for (Object obj2 : bcslist) {
				BaseSyncCache bsc = (BaseSyncCache) obj2;
				if (bsc.getSyncData_ID() == bmOfDB.getID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_C)) {
					listID.add(bsc.getID());
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存DB不存在创建出来的对象");
			b = false;
			// 结果验证：检查同步缓存调度表（从表）的对象。因为是POS端创建的对象，所以需要作结果验证
			if (posID > 0) {
				// pos端创建，需要检查同步缓存从表
				DataSourceContextHolder.setDbName(dbName);
				List<?> bcsdlist = syncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
				for (Object obj3 : bcsdlist) {
					BaseSyncCacheDispatcher bscd = (BaseSyncCacheDispatcher) obj3;
					if (listID.contains(bscd.getSyncCacheID())) {
						b = true;
						break;
					}
				}
				Assert.assertTrue(b == true, "同步缓存从表DB不存在创建出来的对象");
			}
		}
	}

	/** 用于SyncActionTest.testUpdateEx()的结果验证 TODO 应放到BaseSyncCP中 */
	public static void verifySyncUpdateResult(MvcResult mr, BaseModel bmUpdateObjet, PosBO posBO, BaseBO syncCacheBO, BaseBO syncCacheDispatcherBO, EnumCacheType ect, EnumSyncCacheType esct, int posID, String dbName) throws Exception {
		// 结果验证：判断DB是否创建正确
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel bmOfDB = bmUpdateObjet.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(bmUpdateObjet) == 0, "修改失败");
		// 判断普通缓存中是否存在U型的对象
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, ect).read1(bmOfDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null && bm.getID() == bmOfDB.getID(), "普通缓存不存在本次U型的对象");
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 结果验证：判断同步缓存中是否有U型的对象
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
			boolean b = false;
			BaseSyncCache baseSyncCache = null;
			for (BaseModel bms : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bms;
				if (baseSyncCache.getSyncData_ID() == bmOfDB.getID()) {
					b = true;
					break;
				}

			}
			Assert.assertTrue(b == true, "同步缓存不存在U型的对象");
			b = false;
			// 结果验证：同步缓存DB里有无相应的对象
			DataSourceContextHolder.setDbName(dbName);
			List<?> bcslist = syncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			List<Integer> listID = new ArrayList<Integer>();
			for (Object obj2 : bcslist) {
				BaseSyncCache bsc = (BaseSyncCache) obj2;
				if (bsc.getSyncData_ID() == bmOfDB.getID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_U)) {
					listID.add(bsc.getID());
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存DB不存在U型的对象");
			b = false;
			// 结果验证：检查同步缓存调度表（从表）的对象。因为是POS端创建的对象，所以需要作结果验证
			if (posID > 0) { // posID > 0 代表是pos發送的請求，需要同步緩存從表
				// pos端创建，需要检查同步缓存从表
				DataSourceContextHolder.setDbName(dbName);
				List<?> bcsdlist = syncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
				for (Object obj3 : bcsdlist) {
					BaseSyncCacheDispatcher bscd = (BaseSyncCacheDispatcher) obj3;
					if (listID.contains(bscd.getSyncCacheID())) {
						b = true;
						break;
					}
				}
				Assert.assertTrue(b == true, "同步缓存从表DB不存在U型的对象");
			}
		}
	}

	/** TODO 应放到BaseSyncCP中 */
	public static void verifySyncDeleteSuccessResult(BaseModel bmDeleteObjet, PosBO posBO, BaseBO syncCacheBO, BaseBO syncCacheDispatcherBO, EnumSyncCacheType esct, int posID, String dbName) throws Exception {
		// 结果验证：如果pos机大于1，判断同步缓存中是否进行相应的D型插入且只有这个对象的1条数据
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 结果验证：判断同步缓存中是否有创建出来的对象
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
			boolean b = false;
			BaseSyncCache baseSyncCache = null;
			for (BaseModel bms : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bms;
				if (baseSyncCache.getSyncData_ID() == bmDeleteObjet.getID() && baseSyncCache.getSyncType().equals(SyncCache.SYNC_Type_D)) {
					b = true;
					break;
				}

			}
			Assert.assertTrue(b == true, "同步缓存不存在创建出来的D型块");
			b = false;
			// 结果验证：判断同步DB中主表是否有相应的D型插入
			DataSourceContextHolder.setDbName(dbName);
			List<?> bcslist = syncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			List<Integer> listSyncCacheID = new ArrayList<Integer>();
			for (Object obj2 : bcslist) {
				BaseSyncCache bsc = (BaseSyncCache) obj2;
				if (bsc.getSyncData_ID() == bmDeleteObjet.getID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_D)) {
					listSyncCacheID.add(bsc.getID());
					b = true;
					break;
				}
			}
			Assert.assertTrue(b == true, "同步缓存DB不存在创建出来的D型块");
			b = false;
			// 结果验证：如果是pos机发送的请求，判断同步缓存从表是否进行了D型的插入
			if (posID > 0) {
				// pos端创建，需要检查同步缓存从表
				DataSourceContextHolder.setDbName(dbName);
				List<?> bcsdlist = syncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
				for (Object obj3 : bcsdlist) {
					BaseSyncCacheDispatcher bscd = (BaseSyncCacheDispatcher) obj3;
					if (listSyncCacheID.contains(bscd.getSyncCacheID())) {
						b = true;
						break;
					}
				}
				Assert.assertTrue(b == true, "同步缓存从表DB不存在创建出来的D型块");
			}

		}
	}

	/** 验证D型同步块没有创建的情况 TODO 应放到BaseSyncCP中 */
	public void verifySyncDeleteFailureResultResult(BaseModel bmDeleteObjet, PosBO posBO, BaseBO syncCacheBO, BaseBO syncCacheDispatcherBO, EnumSyncCacheType esct, int posID, String dbName) throws Exception {
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 结果验证：判断同步缓存中是否有创建出来的对象
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
			boolean b = false;
			BaseSyncCache baseSyncCache = null;
			for (BaseModel bms : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bms;
				if (baseSyncCache.getSyncData_ID() == bmDeleteObjet.getID()) {
					b = true;
					break;
				}

			}
			Assert.assertTrue(b == false, "同步缓存存在创建出来的D型块");
		}
	}

	/** TODO 应放到BaseSyncCP中 */
	@SuppressWarnings("unchecked")
	public void verifySyncDeleteListResult(StringBuilder stringBuilder, PosBO posBO, BaseBO syncCacheBO, BaseBO syncCacheDispatcherBO, EnumCacheType ect, EnumSyncCacheType esct, HttpSession session, String dbName) throws Exception {
		// 结果验证：解析StringBuilder，看存在哪些对象的ID
		String[] listSB = stringBuilder.toString().split(",");
		List<Integer> listIDs = new ArrayList<Integer>();
		for (String string : listSB) {
			listIDs.add(Integer.parseInt(string));
		}

		// 结果验证： 查询所有普通缓存，判断查询出的结果id是否存在（如果存在，则判断状态）
		List<BaseModel> bmList = CacheManager.getCache(dbName, ect).readN(false, false);
		// Assert.assertTrue(bmList.size() > 0);
		// 获取bm中对象的ID
		List<Integer> listID2 = new ArrayList<Integer>();
		List<BaseModel> bmList2 = new ArrayList<BaseModel>();
		for (BaseModel bm : bmList) {
			bmList2.add(bm);
			listID2.add(bm.getID());
		}
		//
		for (int i = 0; i < listIDs.size(); i++) {
			Assert.assertFalse(listID2.contains(listIDs.get(i)), "普通缓存存在本次创建出来的对象"); // 如果普通缓存存在删除的数据，则失败！
		}

		// 结果验证：如果pos机大于1，判断同步缓存是否进行了D型的插入
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		if (list.size() > 1) {
			// 结果验证：判断同步缓存中是否有D型插入
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, esct).readN(false, false);
			BaseSyncCache baseSyncCache = null;
			List<Integer> bscList = new ArrayList<Integer>();
			for (BaseModel bm : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bm;
				bscList.add(baseSyncCache.getSyncData_ID()); // 将D型同步块关联的对象ID放入bscList数组，用于同步块DB进行对比
			}
			//
			for (int i = 0; i < listIDs.size(); i++) {
				if (!bscList.contains(listIDs.get(i))) { // 只要有一个对象没有D型同步块，就证明有错误，break出来
					Assert.assertFalse(false, "同步缓存不存在创建出来的D型块，F_SyncData_ID=" + listIDs.get(i));
				}
			}

			// 结果验证：判断同步块DB是否进行相应的D型插入
			DataSourceContextHolder.setDbName(dbName);
			List<BaseSyncCache> bcslist = (List<BaseSyncCache>) syncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			List<Integer> listObjectID = new ArrayList<Integer>();
			List<Integer> listSyncCacheID2 = new ArrayList<Integer>();
			for (BaseSyncCache bsc : bcslist) {
				listObjectID.add(bsc.getSyncData_ID()); // 将同步块的SyncData_ID放入数组
				listSyncCacheID2.add(bsc.getID()); // 将同步块的ID放入数组，用于同步块从表对比
			}
			//
			for (int i = 0; i < listIDs.size(); i++) {
				System.out.println(bcslist.get(i).getSyncType());
				if (listObjectID.contains(listIDs.get(i)) && bcslist.get(i).getSyncType().equals(SyncCache.SYNC_Type_D)) {
				} else {
					Assert.assertFalse(false, "同步缓存DB不存在创建出来的D型块，F_SyncData_ID=" + listIDs.get(i));
				}
			}

			// 结果验证：如果是pos进行操作，判断同步块DB从表是否进行了相应的D型插入
			Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
			int posID = pos.getID();
			if (posID > 0) {
				// pos端创建，需要检查同步缓存从表
				DataSourceContextHolder.setDbName(dbName);
				List<BaseSyncCacheDispatcher> bcsdlist = (List<BaseSyncCacheDispatcher>) syncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
				for (int i = 0; i < listIDs.size(); i++) {
					for (BaseSyncCacheDispatcher bscd : bcsdlist) {
						if (listSyncCacheID2.contains(bscd.getSyncCacheID())) {
						} else {
							Assert.assertFalse(false, "同步缓存从表DB不存在创建出来的D型块" + listIDs.get(i));
						}
					}
				}
			}
		}
	}

	// 在这批测试类中，令每个测试方法运行后，令线程sleep(5000);，实现方法为使用TESTNG的基类的实现为：
	public void slowerWxRequest() {
		try {
			Thread.sleep(Shared.SLEEP_5_SECOND);// 令线程sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 将零售单rt(可能是退货单)的所有单品查找出来（带有这个单品被卖出的总数量信息，放在saleNO字段中）,放在mapCommodity中返回,已去重（单品没有重复）
	 * @throws CloneNotSupportedException 
	 */
	public static HashMap<Integer, Commodity> getSimpleCommodityListFromRetailTrade(RetailTrade rt, String dbName, boolean bIncludeDeletedCommodity, CommodityBO commodityBO) throws CloneNotSupportedException {
		HashMap<Integer, Commodity> mapCommodity = new HashMap<Integer, Commodity>(); // KEY为退货的单品ID，value为commodity（内含saleNO，即售出的数量或退货的数量）
		Commodity commodity = null;
		for (Object object : rt.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) object;
			List<List<BaseModel>> bmList = getCommodityInfo(rtc.getCommodityID(), commodityBO, dbName, true);
			if (bmList.get(0) instanceof BaseModel) {
				commodity = (Commodity) bmList.get(0);
			} else {
				commodity = (Commodity) bmList.get(0).get(0);
			}
			//
			if (commodity.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				int multiPackagcommodityNO = commodity.getRefCommodityMultiple();
				List<List<BaseModel>> ls = getCommodityInfo(commodity.getRefCommodityID(), commodityBO, dbName, true/* 零售单中有可能包含已删除商品 */);
				Commodity simpleCommodity = (Commodity) ls.get(0);

				int noVariation = rtc.getNO() * multiPackagcommodityNO;
				Commodity simpleCommodityFromMap = mapCommodity.get(simpleCommodity.getID());
				if (simpleCommodityFromMap != null) {
					noVariation += simpleCommodityFromMap.getSaleNO();
				}
				simpleCommodity.setSaleNO(noVariation);
				// 查询商品门店信息
				List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(simpleCommodity);
				simpleCommodity.setListSlave2(listCommodityShopInfo);
				mapCommodity.put(simpleCommodity.getID(), simpleCommodity);
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				List<BaseModel> subCommList = (List<BaseModel>) bmList.get(1);
				for (BaseModel bm : subCommList) {
					SubCommodity subCommodity = (SubCommodity) bm;
					List<List<BaseModel>> commodityInfo = getCommodityInfo(subCommodity.getSubCommodityID(), commodityBO, dbName, true/* 零售单中有可能包含已删除商品 */);
					Commodity simpleCommodity = (Commodity) commodityInfo.get(0);
					int noVariation = rtc.getNO() * subCommodity.getSubCommodityNO();
					Commodity simpleCommodityFromMap = mapCommodity.get(simpleCommodity.getID());
					if (simpleCommodityFromMap != null) {
						noVariation += simpleCommodityFromMap.getSaleNO();
					}
					simpleCommodity.setSaleNO(noVariation);
					// 查询商品门店信息
					List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(simpleCommodity);
					simpleCommodity.setListSlave2(listCommodityShopInfo);
					mapCommodity.put(simpleCommodity.getID(), simpleCommodity);
				}
			} else if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				int noVariation = rtc.getNO();
				Commodity simpleCommodityFromMap = mapCommodity.get(commodity.getID());
				if (simpleCommodityFromMap != null) {
					noVariation += simpleCommodityFromMap.getSaleNO();
				}
				commodity.setSaleNO(noVariation);
				// 查询商品门店信息
				List<BaseModel> commodityShopInfoList = BaseCommodityTest.getListCommodityShopInfoByCommID(commodity, dbName, rt.getShopID());
				commodity.setListSlave2(commodityShopInfoList);
				mapCommodity.put(commodity.getID(), commodity);
			} else {
				System.out.println("服务商品不需要计算入库");
			}
		}
		return mapCommodity;
	}

	/** 传入商品ID，从DB中获取商品（包含已删除商品）放在bmList.get(0)，如果是组合商品，也返回子商品放在bmList.get(1) */
	protected static List<List<BaseModel>> getCommodityInfo(int commodityID, CommodityBO commodityBO, String dbName, Boolean bIncludeDeletedCommodity) {
		Commodity commodity = new Commodity();
		commodity.setID(commodityID);
		commodity.setIncludeDeleted(bIncludeDeletedCommodity ? EnumBoolean.EB_Yes.getIndex() : EnumBoolean.EB_NO.getIndex());
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = commodityBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查询商品失败：" + commodityBO.printErrorInfo());
		}
		assertTrue(bmList.size() > 0, "查询商品异常！！！");
		return bmList;
	}

	/**
	 * 根据售卖前检索出的入库单查询售卖后的入库单，放入List<Warehousing>中，再放入mapWarehousing中（key=commodityID,value=List<Warehousing>），记录单品对应的入库
	 */
	protected static Map<Integer, List<Warehousing>> queryWarehousingAfterSale(Map<Integer, List<Warehousing>> mapBeforeSale, String dbName, WarehousingBO warehousingBO) {
		Map<Integer, List<Warehousing>> mapWarehousing = new HashMap<Integer, List<Warehousing>>();
		List<Warehousing> warehousingList = null;
		// 遍历mapBeforeSale，获取到售卖前操作过的入库单
		for (Iterator<Integer> it = mapBeforeSale.keySet().iterator(); it.hasNext();) {
			warehousingList = new ArrayList<Warehousing>();
			Integer commID = it.next();
			List<Warehousing> warehousingListBefore = mapBeforeSale.get(commID);
			for (Warehousing warehousing : warehousingListBefore) {
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> wsList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
				if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || wsList.size() == 0) {
					assertTrue(false, "获取warehousing DB数据错误！" + warehousingBO.printErrorInfo());
				}
				// 将从表放入主表
				Warehousing wsWarehousing = (Warehousing) wsList.get(0).get(0);
				wsWarehousing.setListSlave1(wsList.get(1));
				warehousingList.add(wsWarehousing);
			}
			mapWarehousing.put(commID, warehousingList);
		}
		return mapWarehousing;
	}

	/**
	 * 检索出售卖前进行操作过的入库单（Key:CommodityID，Value:入库单信息(里面的slaveList1包括从表信息)），放入List<Warehousing>中
	 */
	protected static Map<Integer, List<Warehousing>> queryWarehousingBeforeSale(Map<Integer, Commodity> simpleCommodityList, String dbName, RetailTrade retailTrade, WarehousingBO warehousingBO) throws CloneNotSupportedException {
		Map<Integer, List<Warehousing>> mapWarehousing = new HashMap<Integer, List<Warehousing>>();
		// 遍历simpleCommodityList，获取到操作过的入库单
		for (Iterator<Integer> it = simpleCommodityList.keySet().iterator(); it.hasNext();) {
			Integer commID = it.next();
			Commodity simpleCommodity = simpleCommodityList.get(commID);
			if (simpleCommodity.getCurrentWarehousingID() == 0) { // 单品可能未进行入库,但店里本身有,商家对它进行售卖
				continue;
			}
			// 查询商品的当值入库单
			Warehousing warehousing = new Warehousing();
			warehousing.setID(simpleCommodity.getCurrentWarehousingID());
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> wsList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);

			if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || wsList.size() == 0) { // 传入一个存在的当值入库ID，如果查询为null，则异常
				assertTrue(false, "获取warehousing DB数据错误！" + warehousingBO.printErrorInfo());
			}
			if (retailTrade.getSourceID() != BaseAction.INVALID_ID) { // 退货
				// 计算退货需要操作多少张退货单， 将进行操作过的入库单放入mapWarehousing中
				returnToWhichWarehousings(wsList, simpleCommodity, mapWarehousing, warehousingBO, dbName);
			} else { // 售卖
				// 计算售卖需要操作多少张退货单， 将进行操作过的入库单放入mapWarehousing中
				sellFromWhichWarehousing(wsList, simpleCommodity, mapWarehousing, warehousingBO, dbName);
			}
		}
		return mapWarehousing;
	}

	/**
	 * 计算售卖commodityRetailTrade对应哪几张入库单的货
	 * 
	 * @param currentWarehousing
	 *            当前入库单
	 * @param commodityRetailTrade
	 *            操作的商品
	 * @param mapWarehousingOut
	 *            需要返回的map，key：commodityID value:存放操作过的入库单（跨库时会有多张）
	 */
	private static void sellFromWhichWarehousing(List<List<BaseModel>> currentWarehousing, Commodity commodityRetailTrade, Map<Integer, List<Warehousing>> mapWarehousingOut, WarehousingBO warehousingBO, String dbName)
			throws CloneNotSupportedException {
		List<Warehousing> wcList = new ArrayList<Warehousing>();

		for (BaseModel bm : currentWarehousing.get(1)) {
			WarehousingCommodity currentWarehousingCommodity = (WarehousingCommodity) bm;
			if (currentWarehousingCommodity.getCommodityID() == commodityRetailTrade.getID()) {
				int salableNO = currentWarehousingCommodity.getNoSalable();
				int overflow = (salableNO > 0 ? salableNO : 0) - commodityRetailTrade.getSaleNO(); // 当值入库单ID的可售数量为负时,会去找新的入库单，并不会再操作当前这个入库单。所以溢出数量的就是本次卖的数量
				if (salableNO > 0) { // 当值入库单ID的可售数量为负时,会去找新的入库单。如果没有新的入库单，则在最后还是会使用当前的入库单
					// 先将此次操作的入库单放入wcList
					Warehousing warehousing = (Warehousing) currentWarehousing.get(0).get(0);
					warehousing.setListSlave1(currentWarehousing.get(1));
					wcList.add((Warehousing) warehousing.clone());
				}
				if (overflow < 0) { // 如果大于等于0，代表不需要进行下一张单的售卖，当前这张单刚好卖完。否则需要跨库卖
					Warehousing tmp = new Warehousing();
					tmp.setPageIndex(BaseAction.PAGE_StartIndex);
					tmp.setPageSize(1); // 只拿一条数据，F_ID最大的那条
					DataSourceContextHolder.setDbName(dbName);
					List<?> warehousingList = warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, tmp);// SP内是降序查询获取数据
					if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "获取warehousing DB数据错误！" + warehousingBO.printErrorInfo());
					}
					// 如果溢出值小于0，代表这张单还不能售卖完，此时需要找下一张入库单，直到数量卖完
					for (int i = currentWarehousingCommodity.getWarehousingID() + 1; i <= ((Warehousing) warehousingList.get(0)).getID(); i++) {
						Warehousing tmpWarehousing = new Warehousing();
						tmpWarehousing.setID(i);
						DataSourceContextHolder.setDbName(dbName);
						List<List<BaseModel>> ws = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, tmpWarehousing);
						if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
							assertTrue(false, "获取warehousing DB数据错误！" + warehousingBO.printErrorInfo());
						}
						if (CollectionUtils.isEmpty(ws) || CollectionUtils.isEmpty(ws.get(0))) { // 如果中间一张入库单被删掉了，那么将寻找下一张
							continue;
						}
						if (((Warehousing) ws.get(0).get(0)).getStatus() != EnumStatusWarehousing.ESW_Approved.getIndex()) {
							continue;
						}
						for (BaseModel bm2 : ws.get(1)) {
							WarehousingCommodity wc = (WarehousingCommodity) bm2;
							if (wc.getCommodityID() == commodityRetailTrade.getID()) { // 当入库商品为操作商品时，才进行计算
								salableNO = wc.getNoSalable();
								overflow = overflow + salableNO;
								Warehousing warehousing3 = (Warehousing) ws.get(0).get(0);
								warehousing3.setListSlave1(ws.get(1));
								wcList.add((Warehousing) warehousing3.clone()); // 将有操作的入库单放入wcList
								break;// 一个入库单不会重复入库同一种商品
							}
						}
						if (overflow >= 0) {
							break;
						}
					}
				} else {
					System.out.println("overflow >= 0,当前入库单可以容纳售卖数量！！！");
				}

				if (wcList.size() == 0) {
					// 此商品的当值入库商品的可售数量为负数，并且无新的入库单。计算可售数量继续使用此入库单
					Warehousing warehousing = (Warehousing) currentWarehousing.get(0).get(0);
					warehousing.setListSlave1(currentWarehousing.get(1));
					wcList.add((Warehousing) warehousing.clone());
				}

				mapWarehousingOut.put(commodityRetailTrade.getID(), wcList);
				break;
			}
		}
	}

	/**
	 * 计算针对commodityReturned退货时，退回到哪几张入库单。有可能跨库
	 * 
	 * @param currentWarehousing
	 *            当值入库单。从表为currentWarehousing.get(1)
	 * @param commodityReturned
	 *            操作的商品
	 * @param mapWarehousingOut
	 *            需要返回的map，key：commodityID value:存放操作过的入库单（跨库时会有多张）
	 * @param warehousingCommodityBO
	 */
	protected static void returnToWhichWarehousings(List<List<BaseModel>> currentWarehousing, Commodity commodityReturned, Map<Integer, List<Warehousing>> mapWarehousingOut, WarehousingBO warehousingBO, String dbName)
			throws CloneNotSupportedException {
		List<Warehousing> wcList = new ArrayList<Warehousing>();
		for (BaseModel bm : currentWarehousing.get(1)) {
			WarehousingCommodity currentWarehousingCommodity = (WarehousingCommodity) bm;
			if (currentWarehousingCommodity.getCommodityID() == commodityReturned.getID()) {
				int warehousingNO = currentWarehousingCommodity.getNO();
				int salableNO = currentWarehousingCommodity.getNoSalable();
				int overflow = commodityReturned.getSaleNO() + salableNO - warehousingNO; // 溢出值，溢出的数量需要加在后来入库的入库单
				//
				Warehousing warehousing = (Warehousing) currentWarehousing.get(0).get(0);
				warehousing.setListSlave1(currentWarehousing.get(1));
				wcList.add((Warehousing) warehousing.clone());
				if (overflow > 0) { // 如果溢出值大于0，代表还有数量可以进行退货，此时需要找下一张入库单，直到数量退完
					for (int i = currentWarehousingCommodity.getWarehousingID() - 1; i > 0; i--) {
						Warehousing tmp = new Warehousing();
						tmp.setID(i);
						DataSourceContextHolder.setDbName(dbName);
						List<List<BaseModel>> ws = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, tmp);
						if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
							assertTrue(false, "获取warehousingDB数据错误！" + warehousingBO.printErrorInfo());
						}
						if (CollectionUtils.isEmpty(ws) || CollectionUtils.isEmpty(ws.get(0))) { // 如果中间一张入库单被删掉了，那么将寻找下一张
							continue;
						}
						if (((Warehousing) ws.get(0).get(0)).getStatus() != EnumStatusWarehousing.ESW_Approved.getIndex()) {
							continue;
						}
						// 遍历从表看看有没有当前退货的商品在入库单内
						for (BaseModel bm2 : ws.get(1)) {
							WarehousingCommodity wc = (WarehousingCommodity) bm2;
							if (wc.getCommodityID() == commodityReturned.getID()) { // 当入库商品为退货商品时，才进行计算
								// 当查询到下一张入库单后，再得到这张单的入库数量和可售数量（可售数量有可能为负）
								warehousingNO = wc.getNO();
								salableNO = wc.getNoSalable();
								overflow = overflow + salableNO - warehousingNO;
								Warehousing warehousing3 = (Warehousing) ws.get(0).get(0);
								warehousing3.setListSlave1(ws.get(1)); // 设置从表
								wcList.add((Warehousing) warehousing3.clone()); // 将有操作的入库单放入wcList
								break; // 一个入库单不会重复入库同一种商品
							}
						}
						if (overflow <= 0) {
							break;
						}
					}
				} else {
					System.out.println("溢出值<=0，当前入库单已经能够完全容纳退货的数量");
				}
				mapWarehousingOut.put(commodityReturned.getID(), wcList);
				break;
			}
		}
	}

	public static void deleteSmallSheetSync(MockMvc mvc) throws Exception {
		// 清除数据，否则影响后面测试
		MvcResult mr = mvc.perform(get("/smallSheetFrame/retrieveNEx.bx") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
		//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());
		//
		SmallSheetFrame ssf = new SmallSheetFrame();
		List<BaseModel> listSmallSheetFrame = new ArrayList<BaseModel>();
		listSmallSheetFrame = ssf.parseN(json.getString(BaseAction.KEY_ObjectList));
		//
		for (BaseModel bm : listSmallSheetFrame) {
			SmallSheetFrame smallSheetFrame = (SmallSheetFrame) bm;
			if (smallSheetFrame.getID() > 6) {// 删除ID大于6的小票，因为初始数据库只有6张，其他的都是测试添加的
				MvcResult mr1 = mvc.perform(get("/smallSheetSync/deleteEx.bx?" + SmallSheetFrame.field.getFIELD_NAME_ID() + "=" + smallSheetFrame.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				)//
						.andExpect(status().isOk())//
						.andDo(print())//
						.andReturn();

				Shared.checkJSONErrorCode(mr1);
			}
		}
	}
}
