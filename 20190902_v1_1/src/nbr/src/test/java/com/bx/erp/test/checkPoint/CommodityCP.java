package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.action.bo.commodity.CommoditySyncCacheDispatcherBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

//@WebAppConfiguration
public class CommodityCP {

	// 1、商品A普通缓存是否创建。
	// 2、商品A的C型同步块缓存是否创建。
	// 3、商品A关联的条形码A普通缓存是否创建。
	// 4、条形码A的C型同步块缓存是否创建。
	// 5、商品A关联的入库单A普通缓存,入库单A的ListSlave1()中是否有且仅有商品A(期初商品)。
	// 6、检查数据库T_Commodity是否有创建商品A数据，如果有多包装商品，检查多包装商品的F_RefCommodityID是否为商品A的ID，F_RefCommodityMultiple是否大于1。
	// 7、检查数据库T_Barcodes是否有创建商品A关联的条形码。
	// 8、检查数据库T_Warehousing是否创建入库单A。(期初商品)
	// 9、检查数据库T_WarehousingCommodity是否创建关联入库单A的入库商品A(期初商品)。
	// 10、检查数据库T_Commodity的F_NO是否和F_NOStart一致(期初商品)。
	// 11、检查数据库T_CommoditySyncCache是否创建C型同步块，F_SyncData_ID为商品A的ID。
	// 12、检查数据库T_BarcodesSyncCache是否创建C型同步块，F_SyncData_ID为条形码A的ID。
	// 13、检查数据库T_ProviderCommodity是否创建商品A的供应商关联，关联的供应商是否正确，如果有创建多包装商品，也需要检查(组合商品、服务商品不会生成供应商)。
	// 14、检查数据库T_SubCommodity是否创建商品A关联的子商品，两条或两条以上(组合商品)。
	// 15、检查数据库T_CommodityHistory是否创建条形码A和包装单位的商品历史，F_CommodityID是商品A的ID。如果是期初商品，还会创建一条库存改变的商品历史。
	// 16、检查本地文件夹是否有上传的图片，数据库T_Commodity中F_Picture路径是否正确(上传了商品图片才会生成)。
	// 17、如果创建时有上传图片，上传图片后，检查SESSION_CommodityPictureDestination()数据是否正确，创建商品后，检查SESSION_CommodityPictureDestination()是否被清空。
	@SuppressWarnings({ "unchecked" })
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, BaseBO commoditySyncCacheBO, BaseBO barcodesSyncCacheBO, BarcodesBO barcodesBO, WarehousingBO warehousingBO,
			WarehousingCommodityBO warehousingCommodityBO, CommodityBO commodityBO, SubCommodityBO subCommodityBO, ProviderCommodityBO providerCommodityBO, CommodityHistoryBO commodityHistoryBO, PackageUnitBO packageUnitBO,
			CategoryBO categoryBO, String dbName) throws Exception {
		// 结果验证：判断DB是否创建正确
		com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(mr.getResponse().getContentAsString());
		
		Commodity bmOfDB = new Commodity();
		bmOfDB = (Commodity) bmOfDB.parse1(com.alibaba.fastjson.JSONObject.parseObject(json.getString(BaseAction.KEY_Object)));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Commodity commodityDefalutValue = (Commodity) bmCreateObjet.clone();
		if (bmOfDB.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
			commodityDefalutValue.setShelfLife(0);
			commodityDefalutValue.setPurchaseFlag(0);
			commodityDefalutValue.setRuleOfPoint(0);
		}
		if (bmOfDB.getType() == EnumCommodityType.ECT_Service.getIndex()) {
			commodityDefalutValue.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateService);
		} else {
			commodityDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);
		}
		commodityDefalutValue.setMnemonicCode(bmOfDB.getMnemonicCode()); // 因为创建时助记码如果为null，会拿商品名称拼音缩写当助记码
		// 期初库存
		if (commodityDefalutValue.getnOStart() > 0 && commodityDefalutValue.getPurchasingPriceStart() > 0) {
			commodityDefalutValue.setNO(commodityDefalutValue.getnOStart());
			commodityDefalutValue.setPurchasingPriceStart(commodityDefalutValue.getPurchasingPriceStart());
		}
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(commodityDefalutValue) == 0, "创建失败");

		// 1、商品A普通缓存是否创建。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Commodity, dbName);

		// 多包装商品字段检查。获取条形码，包装单位，商品倍数，零售价，会员价，批发价，多包装商品的名字，拆分成一个个数组
		String[] sarr = ((Commodity) bmCreateObjet).getMultiPackagingInfo().split(";");
		assertTrue(sarr.length == 7, "sarr的长度不符合预期的长度");

		String[] iaBarcodes = sarr[0].split(",");
		Integer[] iaPuID = GeneralUtil.toIntArray(sarr[1]);
		Integer[] iaCommMultiples = GeneralUtil.toIntArray(sarr[2]);
		Double[] iaPriceRetail = GeneralUtil.toDoubleArray(sarr[3]);
		Double[] iaPriceVIP = GeneralUtil.toDoubleArray(sarr[4]);
		Double[] iaPriceWholesale = GeneralUtil.toDoubleArray(sarr[5]);
		String[] sNames = sarr[6].split(",");
		if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null || iaPriceRetail == null || iaPriceVIP == null || iaPriceWholesale == null || sNames == null) {
			assertTrue(false, "sarr参数缺失");
		}
		if (iaBarcodes.length != iaPuID.length || iaCommMultiples.length != iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length || iaPriceVIP.length != iaBarcodes.length || iaPriceWholesale.length != iaBarcodes.length
				|| sNames.length != iaBarcodes.length) {
			assertTrue(false, "数据长度不一致");
		}

		// 3、商品A关联的条形码A普通缓存是否创建。
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(bmOfDB.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		List<Barcodes> listBarcodes = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes);
		if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取Barcodes DB数据错误！");
		}
		assertTrue(listBarcodes.size() == 1 && listBarcodes.get(0).getBarcode().equals(iaBarcodes[0]) && listBarcodes.get(0).getCommodityID() == bmOfDB.getID(), "条形码DB没有正确创建");

		// 检查返回字段是否合法
		Commodity commodityForCheckCreate = (Commodity) bmOfDB.clone();
		commodityForCheckCreate.setBarcodes(iaBarcodes[0]); // 将条形码Set到String1中，用于字段检查
		commodityForCheckCreate.setOperatorStaffID(1); // 将员工ID Set到String1中，用于字段检查
		String error = "";
		if (commodityForCheckCreate.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
			commodityForCheckCreate.setnOStart(Commodity.NO_START_Default);
			commodityForCheckCreate.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			error = commodityForCheckCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		} else if (commodityForCheckCreate.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
			commodityForCheckCreate.setnOStart(Commodity.NO_START_Default);
			commodityForCheckCreate.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			error = commodityForCheckCreate.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		} else if (commodityForCheckCreate.getType() == CommodityType.EnumCommodityType.ECT_Service.getIndex()) {
			commodityForCheckCreate.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
			commodityForCheckCreate.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
			commodityForCheckCreate.setnOStart(Commodity.NO_START_Default);
			commodityForCheckCreate.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			error = commodityForCheckCreate.checkCreate(BaseBO.CASE_Commodity_CreateService);
		}
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 检查商品历史
		verifyCommodityHistory(commodityHistoryBO, packageUnitBO, categoryBO, bmOfDB, listBarcodes.get(0), BaseAction.INVALID_NO, dbName);

		// 检查有效的pos机（新创建出来的公司，创建商品时是没有Pos机的，那么就没有同步缓存）
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		if (list != null && list.size() > 1) {
			// 2、商品A的C型同步块缓存是否创建。
			BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmOfDB, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在创建出来的对象");
			// 11、检查数据库T_CommoditySyncCache是否创建C型同步块，F_SyncData_ID为商品A的ID。
			BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, bmOfDB, commoditySyncCache, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");

			// 4、条形码A的C型同步块缓存是否创建。
			BaseSyncCache barcodeSyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(listBarcodes.get(0), EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(barcodeSyncCache != null, "同步缓存不存在创建出来的对象");
			// 12、检查数据库T_BarcodesSyncCache是否创建C型同步块，F_SyncData_ID为条形码A的ID。
			BaseSyncCache barcodeSyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(barcodesSyncCacheBO, listBarcodes.get(0), barcodeSyncCache, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(barcodeSyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		if (bmOfDB.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
			// 检查组合商品
			verifySubCommodity(bmOfDB, subCommodityBO, dbName);
		} else if (bmOfDB.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
			// 检查供应商商品
			verifyProviderCommodity(bmOfDB, commodityDefalutValue, providerCommodityBO, dbName);

			// 9、检查数据库T_WarehousingCommodity是否创建关联入库单A的入库商品A(期初商品)。
			if (bmOfDB.getnOStart() > 0 && bmOfDB.getPurchasingPriceStart() > 0) {
				List<WarehousingCommodity> listWarehousingCommodity = verifyWarehousing(warehousingBO, warehousingCommodityBO, dbName);
				assertTrue(listWarehousingCommodity.size() == 1 //
						&& listWarehousingCommodity.get(0).getCommodityID() == bmOfDB.getID() //
						&& listWarehousingCommodity.get(0).getNO() == bmOfDB.getnOStart() //
						, "期初入库单DB没有正常创建");
			}
			// 检查多包装商品
			if (iaBarcodes.length > 1) {
				verifyMultipleCommodity(bmOfDB, commodityBO, barcodesBO, commodityHistoryBO, packageUnitBO, categoryBO, commoditySyncCacheBO, barcodesSyncCacheBO, iaBarcodes, iaPuID, iaCommMultiples, iaPriceRetail, iaPriceVIP,
						iaPriceWholesale, sNames, commodityDefalutValue, providerCommodityBO, null, null, null, null, dbName);
			}
		} else if (bmOfDB.getType() != CommodityType.EnumCommodityType.ECT_Service.getIndex()) {
			Assert.assertTrue(false, "创建的商品类型异常！");
		}

		return true;
	}

	// 无多包装：
	// 1、商品A普通缓存是否修改。
	// 2、商品A的U型同步块缓存是否创建。
	// 3、如果修改了商品A的条码(Action中的操作是先删除后增加，所以会产生两个同步块)，检查原条形码的普通缓存是否删除，并创建对应的D型同步块，检查新条形码的普通缓存是否创建，并创建新条形码的C型同步块。
	// 4、检查数据库T_Commodity是否正确修改商品A数据。
	// 5、检查数据库T_CommoditySyncCache是否创建U型同步块，F_SyncData_ID为商品A的ID。
	// 6、如果修改了商品A的条码，需要检查数据库T_BarcodesSyncCache是否创建旧条形码的D型同步块，以及新条形码的C型同步块，F_SyncData_ID为商品A的ID。
	// 7、如果修改了商品A的供应商，需要检查数据库T_ProviderCommodity是否创建商品A与新供应商关联，并删除旧的供应商关联。
	// 8、如果修改了商品A的商品条码、商品名称、规格、包装单位、商品类别、零售价，商品库存，需要检查数据库T_CommodityHistory是否创建相应的商品历史，F_CommodityID是商品A的ID。
	// 9、如果修改了商品A的图片，需要检查本地文件夹的图片是否修改，数据库T_Commodity中F_Picture路径是否正确，上传图片后，检查SESSION_CommodityPictureDestination()数据是否正确，修改商品后，检查SESSION_CommodityPictureDestination()是否被清空。
	//
	// 修改多包装包装单位：(如果修改了多包装商品的包装单位，在Action中，会把原多包装商品删除，然后创建新的多包装商品，所以如果修改了包装单位，新增和删除的都需要检查)
	// 1、检查旧多包装商品及其条形码的普通缓存是否删除，并创建相应的D型同步块缓存，检查新多包装商品及其条形码的普通缓存是否创建，并创建相应的C型同步块缓
	// 2、检查数据库T_Commodity是否删除旧多包装商品数据，并创建新的多包装商品数据。
	// 3、检查数据库T_Barcodes是否删除旧的条形码数据，并创建新的条形码数据。
	// 4、检查数据库T_ProviderCommodity是否删除旧多包装商品的供应商关联，并创建新多包装商品的供应商关联。
	// 5、检查数据库T_CommoditySyncCache是否创建一个旧多包装商品的D型同步块，还有新多包装商品的C型同步块。
	// 6、检查数据库T_BarcodesSyncCache是否创建一个旧条形码的D型同步块，还有新条形码的C型同步块。
	// 7、检查数据库T_CommodityHistory是否创建新多包装商品的条形码和包装单位的商品历史，以及旧多包装商品的条形码和包装单位的商品历史。
	//
	// 修改多包装基本信息：
	// 1、需要检查该多包装商品及其条形码的普通缓存是否修改，并创建相应的U型同步块缓存。
	// 2、需要检查数据库T_Commodity是否修改该多包装商品数据。
	// 3、需要检查数据库T_CommoditySyncCache是否创建对应的U型同步块。
	// 4、检查数据库T_Barcodes是否删除旧的条形码数据，并创建新的条形码数据。
	// 5、检查数据库T_BarcodesSyncCache是否创建一个旧条形码的D型同步块，还有新条形码的C型同步块。
	// 6、如果修改了多包装商品的商品条码、商品名称、零售价，需要检查数据库T_CommodityHistory是否创建相应的商品历史，F_CommodityID是商品A的ID。
	@SuppressWarnings("unchecked")
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, BaseBO commoditySyncCacheBO, BaseBO barcodesSyncCacheBO, BarcodesBO barcodesBO, WarehousingBO warehousingBO,
			WarehousingCommodityBO warehousingCommodityBO, CommodityBO commodityBO, SubCommodityBO subCommodityBO, ProviderCommodityBO providerCommodityBO, CommodityHistoryBO commodityHistoryBO, PackageUnitBO packageUnitBO,
			CategoryBO categoryBO, int iOldNO, List<PackageUnit> lspuCreated, List<PackageUnit> lspuUpdated, List<PackageUnit> lspuDeleted, List<Commodity> multioleCommdotyList, String dbName, String picType) throws Exception {
		// 结果验证：判断DB是否创建正确
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Commodity bmOfDB = new Commodity();
		bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);

		// 多包装商品字段检查。获取条形码，包装单位，商品倍数，零售价，会员价，批发价，多包装商品的名字，拆分成一个个数组
		String[] sarr = ((Commodity) bmCreateObjet).getMultiPackagingInfo().split(";");
		assertTrue(sarr.length == 7, "sarr的长度不符合预期的长度");

		String[] iaBarcodes = sarr[0].split(",");
		Integer[] iaPuID = GeneralUtil.toIntArray(sarr[1]);
		Integer[] iaCommMultiples = GeneralUtil.toIntArray(sarr[2]);
		Double[] iaPriceRetail = GeneralUtil.toDoubleArray(sarr[3]);
		Double[] iaPriceVIP = GeneralUtil.toDoubleArray(sarr[4]);
		Double[] iaPriceWholesale = GeneralUtil.toDoubleArray(sarr[5]);
		String[] sNames = sarr[6].split(",");
		if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null || iaPriceRetail == null || iaPriceVIP == null || iaPriceWholesale == null || sNames == null) {
			assertTrue(false, "sarr参数缺失");
		}
		if (iaBarcodes.length != iaPuID.length || iaCommMultiples.length != iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length || iaPriceVIP.length != iaBarcodes.length || iaPriceWholesale.length != iaBarcodes.length
				|| sNames.length != iaBarcodes.length) {
			assertTrue(false, "数据长度不一致");
		}

		// 设置对象默认值后进行比较
		Commodity commodityDefalutValue = (Commodity) bmCreateObjet.clone();
		commodityDefalutValue.setLatestPricePurchase(bmOfDB.getLatestPricePurchase());
		commodityDefalutValue.setNO(bmOfDB.getNO());
		List<BaseModel> bmList = null;
		if(bmOfDB.getListSlave2() != null && bmOfDB.getListSlave2().size() > 1) {
			bmList = BaseCommodityTest.getListCommodityShopInfoByCommID(commodityDefalutValue, dbName, 0);
		} else {
			bmList = BaseCommodityTest.getListCommodityShopInfoByCommID(commodityDefalutValue, dbName, commodityDefalutValue.getShopID());
		}
		commodityDefalutValue.setListSlave2(bmList);
		Assert.assertTrue(bmOfDB.compareTo(commodityDefalutValue) == 0, "修改失败");
		// 检查商品的图片格式是否正确
		if (!StringUtils.isEmpty(picType)) {
			String filePath = bmOfDB.getPicture();
			String picTypeOfDB = filePath.substring(filePath.lastIndexOf(".") + 1);
			Assert.assertTrue(picType.equals(picTypeOfDB), "修改商品图片为" + picType + "失败。");
		}
		// 检查返回字段是否合法
		Commodity commodityForCheckCreate = (Commodity) bmOfDB.clone();
		commodityForCheckCreate.setBarcodes(iaBarcodes[0]); // 将条形码Set到String1中，用于字段检查
		commodityForCheckCreate.setOperatorStaffID(1);
		String error = "";
		if (commodityForCheckCreate.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
			commodityForCheckCreate.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);
			commodityForCheckCreate.setnOStart(Commodity.NO_START_Default);
			commodityForCheckCreate.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			error = commodityForCheckCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		} else if (commodityForCheckCreate.getType() == EnumCommodityType.ECT_Service.getIndex()) {
			commodityForCheckCreate.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateService);
			error = commodityForCheckCreate.checkCreate(BaseBO.CASE_Commodity_CreateService);
		} else {
			assertTrue(false, "修改的商品类型异常！");
		}
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 商品A普通缓存是否更新。
		CheckPoint.verifyFromCacheIfCacheExists(false, bmOfDB, EnumCacheType.ECT_Commodity, dbName);

		// 检查商品历史
		CommodityHistory commodityHistory = new CommodityHistory();
		commodityHistory.setCommodityID(bmOfDB.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		List<CommodityHistory> listCommodityHistory = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityHistory);
		if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取CommodityHistory DB数据错误！");
		}

		int iNewNO = listCommodityHistory.size() - iOldNO;
		bmOfDB.setShopID(commodityDefalutValue.getShopID());
		verifyCommodityHistory(commodityHistoryBO, packageUnitBO, categoryBO, bmOfDB, null, iNewNO, dbName);

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		if (list != null && list.size() > 1) {
			// 2、商品A的U型同步块缓存是否创建。
			BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmOfDB, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_U, dbName);
			Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在创建出来的对象");
			// 11、检查数据库T_CommoditySyncCache是否创建U型同步块，F_SyncData_ID为商品A的ID。
			BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, bmOfDB, commoditySyncCache, SyncCache.SYNC_Type_U, dbName);
			Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		if (bmOfDB.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
			// 检查供应商商品
			verifyProviderCommodity(bmOfDB, commodityDefalutValue, providerCommodityBO, dbName);

			// 检查多包装商品
			if (iaBarcodes.length > 1) {
				verifyMultipleCommodity(bmOfDB, commodityBO, barcodesBO, commodityHistoryBO, packageUnitBO, categoryBO, commoditySyncCacheBO, barcodesSyncCacheBO, iaBarcodes, iaPuID, iaCommMultiples, iaPriceRetail, iaPriceVIP,
						iaPriceWholesale, sNames, commodityDefalutValue, providerCommodityBO, lspuCreated, lspuUpdated, lspuDeleted, multioleCommdotyList, dbName);
			}
		} else if (bmOfDB.getType() != CommodityType.EnumCommodityType.ECT_Service.getIndex()) {
			Assert.assertTrue(false, "修改的商品类型异常！");
		}

		return true;
	}

	// 1、商品A普通缓存是否删除。
	// 2、商品A的D型同步块缓存是否创建。
	// 3、商品A关联的条形码A普通缓存是否删除。
	// 4、条形码A的D型同步块缓存是否创建。
	// 5、检查数据库T_Commodity商品A数据F_Status是否为2(已删除),F_BrandID、F_PackageUnitID、F_CategoryID是否为NULL。
	// 6、检查数据库T_Barcodes商品A关联的条形码是否删除。
	// 7、检查数据库T_CommoditySyncCache是否创建D型同步块，F_SyncData_ID为商品A的ID。
	// 8、检查数据库T_BarcodesSyncCache是否创建D型同步块，F_SyncData_ID为条形码A的ID。
	// 9、检查数据库T_ProviderCommodity是否删除商品A的供应商关联。
	// 10、检查数据库T_CommodityHistory是否创建条形码A和包装单位的商品历史，F_CommodityID是商品A的ID。
	// 11、检查商品A的本地图片是否删除。
	@SuppressWarnings("unchecked")
	public static boolean verifyDelete(Commodity bmCreateObjet, List<Barcodes> listBarcodes, PosBO posBO, BaseBO commoditySyncCacheBO, BaseBO barcodesSyncCacheBO, BarcodesBO barcodesBO, CommodityBO commodityBO,
			ProviderCommodityBO providerCommodityBO, CommodityHistoryBO commodityHistoryBO, PackageUnitBO packageUnitBO, CategoryBO categoryBO, int iOldNO, String dbName) throws Exception {

		// 商品A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(bmCreateObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoSuchData) {
			assertTrue(false, "查找普通缓存有误，应该是查询不到才正确，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(bm == null, "普通缓存没有正确删除");

		// 检查数据库中商品A是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoSuchData) {
			assertTrue(false, "查找DB中的商品失败，应为查询不到，错误码=" + commodityBO.getLastErrorCode().toString());
		}
		assertTrue(commR1.size() == 0, "数据库的数据没有正确删除");

		// 检查商品历史
		CommodityHistory commodityHistory = new CommodityHistory();
		commodityHistory.setCommodityID(bmCreateObjet.getID());
		commodityHistory.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<CommodityHistory> listCommodityHistory = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityHistory);
		if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取CommodityHistory DB数据错误！");
		}

		int iNewNO = listCommodityHistory.size() - iOldNO;

		if (iNewNO > 0) {
			commodityHistory.setPageSize(iNewNO);
		}

		DataSourceContextHolder.setDbName(dbName);
		List<CommodityHistory> listCommodityHistoryNew = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityHistory);
		if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取CommodityHistory DB数据错误！");
		}

		for (CommodityHistory ch : listCommodityHistoryNew) {
			if (ch.getFieldName().equals("条形码")) {
				assertTrue(ch.getNewValue().equals(""), "条形码历史插入有误！");
			} else if (ch.getFieldName().equals("包装单位")) {
				// 查询包装单位
				PackageUnit packageUnit = new PackageUnit();
				packageUnit.setID(bmCreateObjet.getPackageUnitID());

				DataSourceContextHolder.setDbName(dbName);
				PackageUnit packageUnitR1 = (PackageUnit) packageUnitBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, packageUnit);
				if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "获取PackageUnit DB数据错误！");
				}
				assertTrue(ch.getNewValue().equals("") && ch.getOldValue().equals(packageUnitR1.getName()), "包装单位历史插入有误！");
			}
		}

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		if (list != null && list.size() > 1) {
			// 2、商品A的D型同步块缓存是否创建。
			BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmCreateObjet, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_D, dbName);
			Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在创建出来的对象");
			// 11、检查数据库T_CommoditySyncCache是否创建U型同步块，F_SyncData_ID为商品A的ID。
			BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, bmCreateObjet, commoditySyncCache, SyncCache.SYNC_Type_D, dbName);
			Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");

			for (Barcodes barcodes : listBarcodes) {
				// 条形码D型同步块
				BaseSyncCache barcodeSyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(barcodes, EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_D, dbName);
				Assert.assertTrue(barcodeSyncCache != null, "同步缓存不存在创建出来的对象");
				//
				BaseSyncCache barcodeSyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(barcodesSyncCacheBO, barcodes, commoditySyncCache, SyncCache.SYNC_Type_D, dbName);
				Assert.assertTrue(barcodeSyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
			}
		}

		// 查询供应商商品表是否有对应删除该商品
		ProviderCommodity pc = new ProviderCommodity();
		pc.setCommodityID(bmCreateObjet.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		List<?> pcRetrieveN = providerCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, pc);
		Assert.assertTrue(pcRetrieveN.size() == 0);

		// 检查多包装商品
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, bmCreateObjet);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		assertTrue(listMultiPackageCommodity.size() == 0, "多包装商品没有正确删除");
		return true;
	}

	// 1、POS1同步商品A数据后，检查商品A同步块缓存的ListSlava1()中，是否有POS1的数据存在。
	// 2、检查T_CommoditySyncCacheDispatcher中是否创建了POS1的同步数据。
	// 3、如果商品A的同步块已经被全部POS机同步，则删除商品A的同步块缓存，检查商品A的同步块缓存是否被删除。检查数据库T_CommoditySyncCache和T_CommoditySyncCacheDispatcher中商品A相关的同步块数据是否删除。"
	@SuppressWarnings("unchecked")
	public static boolean verifySyncCommodity(List<Commodity> commList, int posID, PosBO posBO, CommoditySyncCacheDispatcherBO cacheDispatcherBO, String dbName) throws Exception {
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		// 1、POS1同步商品A数据后，检查商品A同步块缓存的ListSlava1()中，是否有POS1的数据存在。
		if (list != null && list.size() > 1) {
			CommoditySyncCacheDispatcher commoditySyncCacheDispatcher = new CommoditySyncCacheDispatcher();
			// 获取数据库中同步块数据
			DataSourceContextHolder.setDbName(dbName);
			List<CommoditySyncCacheDispatcher> cscdList = (List<CommoditySyncCacheDispatcher>) cacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commoditySyncCacheDispatcher);
			if (cacheDispatcherBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找同步调度表失败，错误码=" + cacheDispatcherBO.getLastErrorCode().toString());
			}

			boolean posIDInListSlave1;
			boolean posIDInSyncCacheDispatcherDB;
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CommoditySyncInfo).readN(false, false);
			for (Commodity c : commList) {
				posIDInListSlave1 = false;
				posIDInSyncCacheDispatcherDB = false;

				BaseSyncCache baseSyncCache = null;
				// 如果商品的int1为1，代表这个商品已经完全同步，同步块主表和从表都已被删除
				if (c.getIsSync() == 1) {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == c.getID()) {
							assertTrue(false, "该同步块已经完全同步，缓存中不应该还有该同步块！");
						}
					}
				} else {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == c.getID()) {
							break;
						}
					}
					assertTrue(baseSyncCache.getSyncData_ID() == c.getID(), "同步块不存在");

					// 检查该同步块的从表信息是否有POS机同步的数据
					CommoditySyncCacheDispatcher cscdOfCache = new CommoditySyncCacheDispatcher();
					for (Object o : baseSyncCache.getListSlave1()) {
						cscdOfCache = (CommoditySyncCacheDispatcher) o;
						if (cscdOfCache.getPos_ID() == posID) {
							posIDInListSlave1 = true;
							break;
						}
					}
					if (!posIDInListSlave1) {
						assertTrue(false, "同步块从表没有正确插入。");
					}
					// 检查数据库的数据是否和缓存中的数据相等
					for (CommoditySyncCacheDispatcher cscdOfDB : cscdList) {
						if (cscdOfCache.getID() == cscdOfDB.getID() && cscdOfDB.compareTo(cscdOfCache) == 0) {
							posIDInSyncCacheDispatcherDB = true;
							break;
						}
					}
					if (!posIDInSyncCacheDispatcherDB) {
						assertTrue(false, "同步块从表没有正确插入数据库。");
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static void verifyProviderCommodity(Commodity bmOfDB, Commodity commodityDefalutValue, ProviderCommodityBO providerCommodityBO, String dbName) {
		Integer[] iaProviderID = GeneralUtil.toIntArray(commodityDefalutValue.getProviderIDs());
		if (iaProviderID == null || iaProviderID.length == 0) {
			assertTrue(false, "");
		}
		// 去重
		List<Integer> iArrProviderIDList = GeneralUtil.sortAndDeleteDuplicated(iaProviderID);

		ProviderCommodity providerCommodity = new ProviderCommodity();
		providerCommodity.setCommodityID(bmOfDB.getID());

		DataSourceContextHolder.setDbName(dbName);
		List<ProviderCommodity> listProviderCommodity = (List<ProviderCommodity>) providerCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, providerCommodity);
		assertTrue(listProviderCommodity.size() == iArrProviderIDList.size(), "DB查询出来的商品供应商数量不一致");

		boolean bSameAsInProviderIDs = false;
		for (ProviderCommodity pc : listProviderCommodity) {
			for (int i = 0; i < iArrProviderIDList.size(); i++) {
				if (pc.getProviderID() == iArrProviderIDList.get(i)) {
					bSameAsInProviderIDs = true;
					break;
				}
			}
			if (!bSameAsInProviderIDs) {
				assertTrue(false, "供应商商品创建异常，供应商商品：" + pc);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void verifyMultipleCommodity(Commodity bmOfDB, CommodityBO commodityBO, BarcodesBO barcodesBO, CommodityHistoryBO commodityHistoryBO, PackageUnitBO packageUnitBO, CategoryBO categoryBO, BaseBO commoditySyncCacheBO,
			BaseBO barcodesSyncCacheBO, String[] iaBarcodes, Integer[] iaPuID, Integer[] iaCommMultiples, Double[] iaPriceRetail, Double[] iaPriceVIP, Double[] iaPriceWholesale, String[] sNames, Commodity commodityDefalutValue,
			ProviderCommodityBO providerCommodityBO, List<PackageUnit> lspuCreated, List<PackageUnit> lspuUpdated, List<PackageUnit> lspuDeleted, List<Commodity> multipleCommdityListIn, String dbName) throws CloneNotSupportedException {
		boolean bSameAsInString1 = false;
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> listMultiPackageCommodityNew = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, bmOfDB);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		assertTrue(listMultiPackageCommodityNew.size() == (iaBarcodes.length - 1), "多包装商品的数量不正确");

		boolean isCreateCheckPoint = false;
		if (lspuCreated == null && lspuUpdated == null && lspuDeleted == null && multipleCommdityListIn == null) { // 这是创建商品检查点的多包装检查
			isCreateCheckPoint = true;
		}

		if (!isCreateCheckPoint) {
			for (Commodity c : multipleCommdityListIn) {
				for (PackageUnit pu : lspuDeleted) { // 删除的多包装
					if (c.getPackageUnitID() == pu.getID()) {
						// 多包装商品的D型同步块缓存是否创建。
						BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(c, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_D, dbName);
						Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在D型同步块");
						// 检查数据库T_CommoditySyncCache是否创建D型同步块
						BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, c, commoditySyncCache, SyncCache.SYNC_Type_D, dbName);
						Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在D型同步块");
						break;
					}
				}
			}
		}

		for (Commodity comm : listMultiPackageCommodityNew) {
			Barcodes barcodesMultiPackage = new Barcodes();
			barcodesMultiPackage.setCommodityID(comm.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> listBarcodesMultiPackageNew = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodesMultiPackage);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "获取Barcodes DB数据错误！");
			}
			assertTrue(listBarcodesMultiPackageNew != null && listBarcodesMultiPackageNew.size() == 1, "多包装商品条形码DB不正确，多包装商品：" + comm);
			comm.setShopID(bmOfDB.getShopID());
			List<BaseModel> commShopInfoList = BaseCommodityTest.getListCommodityShopInfoByCommID(comm);
			CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commShopInfoList.get(commShopInfoList.size() - 1);
			for (int i = 1; i < iaBarcodes.length; i++) {
				if (comm.getPackageUnitID() == iaPuID[i] && //
						comm.getRefCommodityMultiple() == iaCommMultiples[i] && //
						commodityShopInfo.getPriceRetail() == iaPriceRetail[i] && //
//						commodityShopInfo.getPriceVIP() == iaPriceVIP[i] && //
//						commodityShopInfo.getPriceWholesale() == iaPriceWholesale[i] && //
						listBarcodesMultiPackageNew.get(0).getBarcode().equals(iaBarcodes[i])) {
					bSameAsInString1 = true;
					break;
				}
			}

			if (!bSameAsInString1) {
				assertTrue(false, "多包装商品DB创建和传入参数不符，多包装商品：" + comm);
			}

			if (isCreateCheckPoint) {
				// 多包装商品的C型同步块缓存是否创建。
				BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(comm, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_C, dbName);
				Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在创建出来的对象");
				// 检查数据库T_CommoditySyncCache是否创建C型同步块
				BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, comm, commoditySyncCache, SyncCache.SYNC_Type_C, dbName);
				Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");

				// 多包装条形码的C型同步块缓存是否创建。
				BaseSyncCache barcodeSyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(listBarcodesMultiPackageNew.get(0), EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_C, dbName);
				Assert.assertTrue(barcodeSyncCache != null, "同步缓存不存在创建出来的对象");
				// 检查数据库T_BarcodesSyncCache是否创建C型同步块
				BaseSyncCache barcodeSyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(barcodesSyncCacheBO, listBarcodesMultiPackageNew.get(0), barcodeSyncCache, SyncCache.SYNC_Type_C, dbName);
				Assert.assertTrue(barcodeSyncCacheDB != null, "同步缓存DB不存在创建出来的对象");

				// ...必须检查T_Barcodes中已经有多包装商品和条形码的关系。
			} else {
				for (PackageUnit pu : lspuCreated) { // 新建的多包装
					if (comm.getPackageUnitID() == pu.getID()) {
						// 多包装商品的C型同步块缓存是否创建。
						BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(comm, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_C, dbName);
						Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在创建出来的对象");
						// 检查数据库T_CommoditySyncCache是否创建C型同步块
						BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, comm, commoditySyncCache, SyncCache.SYNC_Type_C, dbName);
						Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
						break;
					}
				}

				for (PackageUnit pu : lspuUpdated) { // 修改的多包装
					if (comm.getPackageUnitID() == pu.getID()) {
						// 修改多包装商品的Name、倍数、零售价、采购价、会员价、批发价。才会修改多包装商品，生成同步块这些。只修改了多包装商品的条形码是不会生成同步块，同步块缓存。只有条形码的同步块
						boolean retailPriceIsChanged = ((pu.getWhichPropertyIsChanged() & 1) == 1);
						boolean nameIsChanged = ((pu.getWhichPropertyIsChanged() & 8) == 8);
						boolean multipleIsChanged = ((pu.getWhichPropertyIsChanged() & 2) == 2);
						if (multipleIsChanged || nameIsChanged || retailPriceIsChanged) {
							// 多包装商品的U型同步块缓存是否创建。
							BaseSyncCache commoditySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(comm, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_U, dbName);
							Assert.assertTrue(commoditySyncCache != null, "同步缓存不存在创建出来的对象");
							// 检查数据库T_CommoditySyncCache是否创建U型同步块
							BaseSyncCache commoditySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(commoditySyncCacheBO, comm, commoditySyncCache, SyncCache.SYNC_Type_U, dbName);
							Assert.assertTrue(commoditySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
						}
						boolean barcodeIsChange = ((pu.getWhichPropertyIsChanged() & 4) == 4);
						if (barcodeIsChange) {
							// 检查多包装商品的条形码
							for (Commodity multiplecommodity : multipleCommdityListIn) {
								if (multiplecommodity.getID() == comm.getID()) {
									Barcodes oldBarcodes = new Barcodes();
									oldBarcodes.setBarcode(multiplecommodity.getBarcodes());
									oldBarcodes.setCommodityID(multiplecommodity.getID());
									oldBarcodes.setID(multiplecommodity.getBarcodeID());
									Barcodes newBarcodes = listBarcodesMultiPackageNew.get(0);
									// 检查旧条形码的缓存是否删除，新条形码的缓存是否增加
									CheckPoint.verifyFromCacheIfCacheExists(false, oldBarcodes, EnumCacheType.ECT_Barcodes, dbName);
									CheckPoint.verifyFromCacheIfCacheExists(true, newBarcodes, EnumCacheType.ECT_Barcodes, dbName);
									// 检查同步块缓存中是否存在旧条形码的D型数据，检查同步块缓存中是否存在新条形码的C型数据
									BaseSyncCache barcodesSyncCache_D = CheckPoint.verifyFromCacheIfSyncCacheExists(oldBarcodes, EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_D, dbName);
									Assert.assertTrue(barcodesSyncCache_D != null, "条形码同步缓存不存在D型的 对象");
									BaseSyncCache barcodesSyncCache_C = CheckPoint.verifyFromCacheIfSyncCacheExists(newBarcodes, EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_C, dbName);
									Assert.assertTrue(barcodesSyncCache_C != null, "条形码同步缓存不存在C型的 对象");
									// 检查旧条形码的D型同步块数据是否存在数据库中，检查新条形码的C型同步块是否存在数据库中
									CheckPoint.verifyFromDBIfSyncCacheExists(barcodesSyncCacheBO, oldBarcodes, barcodesSyncCache_D, SyncCache.SYNC_Type_D, dbName);
									CheckPoint.verifyFromDBIfSyncCacheExists(barcodesSyncCacheBO, newBarcodes, barcodesSyncCache_C, SyncCache.SYNC_Type_C, dbName);
									// 检查旧条形码的数据在DB中被删除， newBarcodes就是从数据库中获取的，所有并不需要再进行DB查询
									DataSourceContextHolder.setDbName(dbName);
									BaseModel oldBarcodesOfDB = barcodesBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, oldBarcodes);
									if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
										assertTrue(false, "获取Barcodes DB数据错误！");
									}
									assertTrue(oldBarcodesOfDB == null, "多包装商品的条形码删除失败。");
								}
							}
						}
						break;
					}
				}
			}

			// 检查供应商商品
			verifyProviderCommodity(bmOfDB, commodityDefalutValue, providerCommodityBO, dbName);

			// 检查商品历史
			verifyCommodityHistory(commodityHistoryBO, packageUnitBO, categoryBO, comm, listBarcodesMultiPackageNew.get(0), BaseAction.INVALID_NO, dbName);
		}
	}

	@SuppressWarnings("unchecked")
	private static void verifySubCommodity(Commodity bmOfDB, SubCommodityBO subCommodityBO, String dbName) {
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(bmOfDB.getID());

		DataSourceContextHolder.setDbName(dbName);
		List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
		if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取SubCommodity DB数据错误！");
		}
		assertTrue(listSubCommodity.size() >= 2 && listSubCommodity.size() == bmOfDB.getListSlave1().size(), "子商品的个数不正确，应该有两个或两个以上的子商品");

		boolean bSameAsInListSlave1;
		for (SubCommodity sc : listSubCommodity) {
			bSameAsInListSlave1 = false;
			for (Object o : bmOfDB.getListSlave1()) {
				SubCommodity sc2 = (SubCommodity) o;
				if (sc.getID() == sc2.getID() && //
						sc.getCommodityID() == sc2.getCommodityID() && //
						sc.getSubCommodityID() == sc2.getSubCommodityID() && //
						sc.getSubCommodityNO() == sc2.getSubCommodityNO() && //
						sc.getPrice() == sc2.getPrice()) {
					bSameAsInListSlave1 = true;
					break;
				}
			}

			if (!bSameAsInListSlave1) {
				assertTrue(false, "组合商品子商品和没有在ListSlave1()中，子商品商品：" + sc);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static List<WarehousingCommodity> verifyWarehousing(WarehousingBO warehousingBO, WarehousingCommodityBO warehousingCommodityBO, String dbName) {
		Warehousing warehousing = new Warehousing();
		warehousing.setPageSize(1);

		DataSourceContextHolder.setDbName(dbName);
		List<Warehousing> listWarehousing = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取Warehousing DB数据错误！");
		}

		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setWarehousingID(listWarehousing.get(0).getID());

		DataSourceContextHolder.setDbName(dbName);
		List<WarehousingCommodity> listWarehousingCommodity = (List<WarehousingCommodity>) warehousingCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousingCommodity);
		if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取warehousingCommodity DB数据错误！");
		}
		return listWarehousingCommodity;
	}

	/** 检查商品修改历史是否正确 
	 * @throws CloneNotSupportedException */
	@SuppressWarnings("unchecked")
	private static void verifyCommodityHistory(CommodityHistoryBO commodityHistoryBO, PackageUnitBO packageUnitBO, CategoryBO categoryBO, Commodity bmOfDB, Barcodes barcodes, int iNewNO, String dbName) throws CloneNotSupportedException {
		CommodityHistory commodityHistory = new CommodityHistory();
		commodityHistory.setCommodityID(bmOfDB.getID());
		if (iNewNO > 0) {
			commodityHistory.setPageSize(iNewNO);
		}

		DataSourceContextHolder.setDbName(dbName);
		List<CommodityHistory> listCommodityHistory = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityHistory);
		if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取CommodityHistory DB数据错误！");
		}

		// 由于一个商品可能会生成多个相同类型的商品历史，这个时候如果已经检查过一次该类型的商品历史，就不需要再对后面相同类型的修改历史做检查。
		boolean bBarcodeVerified = false;
		boolean bNameVerified = false;
		boolean bSpecificationVerified = false;
		boolean bPriceRetailVerified = false;
		boolean bNOVerified = false;
		boolean bPackageUnitVerified = false;
		boolean bCategoryVerified = false;
		for (CommodityHistory ch : listCommodityHistory) {
			List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(bmOfDB);
			CommodityShopInfo commodityShopInfo = (CommodityShopInfo) listCommodityShopInfo.get(listCommodityShopInfo.size() - 1);
			if (ch.getFieldName().equals("条形码")) {
				if (barcodes == null || bBarcodeVerified) { // 如果是修改商品，会传null，不检查条形码
					continue;
				}
				assertTrue(barcodes.getBarcode().equals(ch.getNewValue()), "条形码历史插入有误！");
				bBarcodeVerified = true;
			} else if (ch.getFieldName().equals("商品名称")) {
				if (!bNameVerified) {
					assertTrue(bmOfDB.getName().equals(ch.getNewValue()), "商品名称历史插入有误！");
					bNameVerified = true;
				}
			} else if (ch.getFieldName().equals("规格")) {
				if (!bSpecificationVerified) {
					assertTrue(bmOfDB.getSpecification().equals(ch.getNewValue()), "商品规格历史插入有误！");
					bSpecificationVerified = true;
				}
			} else if (ch.getFieldName().equals("零售价")) {
				if (!bPriceRetailVerified) {
					assertTrue(Math.abs(GeneralUtil.sub(GeneralUtil.round(commodityShopInfo.getPriceRetail(), 2), Double.valueOf(ch.getNewValue()))) < BaseModel.TOLERANCE, "零售价历史插入有误！");
					bPriceRetailVerified = true;
				}
			} else if (ch.getFieldName().equals("库存")) {
				if (!bNOVerified) {
					assertTrue(commodityShopInfo.getNO() == Integer.valueOf(ch.getNewValue()), "库存历史插入有误！");
					bNOVerified = true;
				}
			} else if (ch.getFieldName().equals("包装单位")) {
				// 查询包装单位
				if (!bPackageUnitVerified) {
					PackageUnit packageUnit = new PackageUnit();
					packageUnit.setID(bmOfDB.getPackageUnitID());

					DataSourceContextHolder.setDbName(dbName);
					PackageUnit packageUnitR1 = (PackageUnit) packageUnitBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, packageUnit);
					if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "获取PackageUnit DB数据错误！");
					}
					assertTrue(packageUnitR1 != null && packageUnitR1.getName().equals(ch.getNewValue()), "商品修改历史的包装单位历史有误！");
					bPackageUnitVerified = true;
				}
			} else if (ch.getFieldName().equals("商品类别")) {
				if (!bCategoryVerified) {
					Category category = new Category();
					category.setID(bmOfDB.getCategoryID());

					DataSourceContextHolder.setDbName(dbName);
					Category categoryR1 = (Category) categoryBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, category);
					if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "获取PackageUnit DB数据错误！");
					}
					assertTrue(categoryR1 != null && categoryR1.getName().equals(ch.getNewValue()), "商品修改历史的商品类别历史有误！");
					bCategoryVerified = true;
				}
			}
		}

	}
}
