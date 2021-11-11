package com.bx.erp.action.commodity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseSyncAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BarcodesSyncCacheBO;
import com.bx.erp.action.bo.BarcodesSyncCacheDispatcherBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityShopInfoBO;
import com.bx.erp.action.bo.commodity.CommoditySyncCacheBO;
import com.bx.erp.action.bo.commodity.CommoditySyncCacheDispatcherBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.cache.BarcodesCache;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/commoditySync")
@Scope("prototype")
public class CommoditySyncAction extends BaseSyncAction {
	private Log logger = LogFactory.getLog(CommoditySyncAction.class);

	/** 商品图片类型 */
	public static final String JPEGType = "image/jpeg";
	public static final String PNGType = "image/png";
	public static final String JPGType = "image/jpg";
	public static final String ContentType_JPEG = "jpeg";

	public final static String DIR = "d:/nbr/pic";

	@Override
	protected int getSequence() {
		return aiSequenceSyncBlockForCommodityAndBarcodes.incrementAndGet();
	}

	@Resource
	protected com.bx.erp.cache.commodity.CommoditySyncCache csc;

	@Resource
	protected com.bx.erp.cache.BarcodesSyncCache barcodesSyncCache;

	@Resource
	protected BarcodesCache barcodesCache;

	@Resource
	protected CommoditySyncCacheBO commoditySyncCacheBO;

	@Resource
	protected CommoditySyncCacheDispatcherBO commoditySyncCacheDispatcherBO;

	@Resource
	protected BarcodesSyncCacheBO barcodesSyncCacheBO;

	@Resource
	protected BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO;

	@Resource
	protected CommodityBO commodityBO;
	
	@Resource
	protected CommodityShopInfoBO commodityShopInfoBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private ProviderCommodityBO providerCommodityBO;

	@Resource
	private PackageUnitBO packageUnitBO;

	@Resource
	private SubCommodityBO subCommodityBO;

	@Override
	protected EnumCacheType getCacheType() {
		return EnumCacheType.ECT_Commodity;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_CommoditySyncInfo;
	}

	protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	protected BaseBO getSyncCacheBO() {
		return commoditySyncCacheBO;
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return commoditySyncCacheDispatcherBO;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new CommoditySyncCacheDispatcher();
	}

	@Override
	protected BaseBO getModelBO() {
		return commodityBO;
	}

	@Override
	protected BaseModel getModel() {
		return new Commodity();
	}

	@Override
	protected BaseSyncCache getSyncCacheModel() {
		return new com.bx.erp.model.commodity.CommoditySyncCache();
	}

	@Override
	protected SyncCache getSyncCache() {
		return csc;
	}

	private static final int MULTI_PACKAGING_ELEMENT_COUNT = 7;

	private static final String FLAG_WillNotUpdatePictureInSP = ":";

	/** 用户创建、R1、RN商品后，没有对图片进行任何操作。这是默认值 */
	private static final int LAST_OPERATION_TO_PICTURE_None = 0;

	/** 用户最后一个操作是：上传图片 */
	private static final int LAST_OPERATION_TO_PICTURE_Upload = 1;

	/** 用户最后一个操作是：清空图片 */
	private static final int LAST_OPERATION_TO_PICTURE_Clear = 2;

	@Override
	protected EnumErrorCode deleteSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Commodity没有从表数据
	}

	@Override
	protected EnumErrorCode createSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Commodity没有从表
	}

	/** 检查商品的副单位的变动情况并记录在各集合中：<br/>
	 * 拿商品修改前和修改后的副单位的ID的集合（是ID，不是副单位的名称）进行比较（排序加消除重复），看看有无差异（包括倍数、零售价、条形码、的差异）。<br/>
	 * 有差异则为有修改，同时记录新增、修改、删除了哪些副单位以便在Action中处理 <br />
	 * 如果要判断用户在前端有无修改多包装单位的信息，只需要判断lspuCreated、lspuUpdated、lspuDeleted的size是否>0。>0为有修改过。
	 * 
	 * @param c
	 *            前端传递过来的修改后的商品对象
	 * @param lspuAll
	 *            保存c里的全部包装单位，包括原单位和副单位
	 * @param lspuCreated
	 *            记录前端新增的副单位
	 * @param lspuUpdated
	 *            记录前端修改的副单位。用位标记哪些字段被修改了，多包装商品的名称=位3，条形码=位2，商品倍数=1，零售价=位0，目前不管会员价和批发价的变化
	 * @param lspuDeleted
	 *            记录前端删除的副单位
	 * @return true，成功检查。false，检查时出现意外。 */
	@SuppressWarnings("unchecked")
	public static boolean checkPackageUnit(Commodity c, List<PackageUnit> lspuAll, List<PackageUnit> lspuCreated, List<PackageUnit> lspuUpdated, List<PackageUnit> lspuDeleted, String dbName, Log logger, PackageUnitBO packageUnitBO,
			CommodityBO commodityBO, BarcodesBO barcodesBO, int staffID) {
		logger.info("检查商品的包装单位,lspuAll=" + lspuAll + ",lspuCreated=" + lspuCreated + ",lspuUpdated=" + lspuUpdated + ",lspuDeleted=" + lspuDeleted);

		// 拿到c的原有副单位集合
		PackageUnit pu = new PackageUnit();
		pu.setNormalCommodityID(c.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<?> puList = (List<?>) packageUnitBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, pu);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询多个包装单位，puList=" + puList);
			return false;
		}
		int[] iaPuIDOld = new int[puList.size()];
		for (int i = 0; i < puList.size(); i++) {
			PackageUnit p = (PackageUnit) puList.get(i);
			iaPuIDOld[i] = p.getID();
		}

		// ...如果没有副单位怎么办？用例不足。

		// 从c中拿到修改后的副单位的集合
		String[] sarr = c.getMultiPackagingInfo().split(";");
		if (sarr.length != MULTI_PACKAGING_ELEMENT_COUNT) {
			logger.info("得到的数据与预期的数据的长度不一致");
			return false;
		}

		// 从缓存获取到副单位的配置数据
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cg = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxVicePackageUnit, staffID, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || cg == null) {
			// ...
			logger.info("查找不到MaxVicePackageUnit，错误码=" + ecOut.getErrorCode().toString());
			return false;
		}

		String[] iaBarcodes = sarr[0].split(",");
		Integer[] iaPuID = GeneralUtil.toIntArray(sarr[1]);
		Integer[] iaCommMultiples = GeneralUtil.toIntArray(sarr[2]);
		Double[] iaPriceRetail = GeneralUtil.toDoubleArray(sarr[3]);
		// float[] iaPricePurchase = tofloatArray(sarr[4]);
		Double[] iaPriceVIP = GeneralUtil.toDoubleArray(sarr[4]);
		Double[] iaPriceWholesale = GeneralUtil.toDoubleArray(sarr[5]);
		String[] iaPuName = sarr[6].split(",");
		if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null || iaPriceRetail == null || iaPriceVIP == null || iaPriceWholesale == null || iaBarcodes.length - 1 > Integer.valueOf(cg.getValue())) {
			logger.info("参数缺失");
			return false;
		}
		// 检查长度是否一致
		if (iaBarcodes.length != iaPuName.length || iaBarcodes.length != iaPuID.length || iaCommMultiples.length != iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length || iaPriceVIP.length != iaBarcodes.length
				|| iaPriceWholesale.length != iaBarcodes.length) {
			logger.info("数组长度不相等");
			return false;
		}

		for (int i = 0; i < iaPuID.length; i++) { // 保存c里的全部副单位
			PackageUnit p = new PackageUnit();
			p.setID(iaPuID[i]);
			lspuAll.add(p);
		}

		int[] iaPuIDNew = new int[iaPuID.length - 1];
		for (int i = 1; i < iaPuID.length; i++) { // 去除单品包装单位
			iaPuIDNew[i - 1] = iaPuID[i];
		}

		iaPuIDNew = GeneralUtil.toIntDesc(iaPuIDNew);// ...这个地方为什么需要排序？

		boolean isPackageExist; // 判断包装单位是否存在。//...这个变量名无法表明其使用的目的，故可以考虑重命名

		// ...比较
		for (int i = 0; i < iaPuIDNew.length; i++) { // 判断是否有新增包装单位
			isPackageExist = false;
			for (int j = 0; j < iaPuIDOld.length; j++) {
				if (iaPuIDNew[i] == iaPuIDOld[j]) {
					isPackageExist = true;
					break;
				}
			}
			if (!isPackageExist) {
				PackageUnit p = new PackageUnit();
				p.setID(iaPuIDNew[i]);
				lspuCreated.add(p);
			}
		}

		for (int i = 0; i < iaPuIDOld.length; i++) { // 判断是否有删除包装单位
			isPackageExist = false;
			for (int j = 0; j < iaPuIDNew.length; j++) {
				if (iaPuIDOld[i] == iaPuIDNew[j]) {
					isPackageExist = true;
					break;
				}
			}
			if (!isPackageExist) {
				PackageUnit p = new PackageUnit();
				p.setID(iaPuIDOld[i]);
				lspuDeleted.add(p);
			}
		}

		// 判断是否有修改包装单位（即包装单位不变，但它的属性有变。属性包含：参照商品倍数、零售价、采购价、VIP价、批发价、条形码）
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> commList = (List<Commodity>) commodityBO.retrieveNObject(staffID, BaseBO.CASE_RetrieveNMultiPackageCommodity, c);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取多包装商品失败,错误码=" + commodityBO.getLastErrorCode());
			return false;
		}
		//
		for (int i = 0; i < iaPuID.length; i++) {
			int change = 0;
			for (Commodity comm : commList) {
				if (comm.getPackageUnitID() == iaPuID[i]) {
					if (!comm.getName().equals(iaPuName[i])) {// 判断多包装名称的差异
						change |= 8; // 8 = 0x1000;
					}
					if (comm.getRefCommodityMultiple() != iaCommMultiples[i]) {
						change |= 2; // 2 = 0x00010;
					}
					if (comm.getPriceRetail() != iaPriceRetail[i]) {
						change |= 1; // 1 = 0x00001;
					}
					// 目前会员价和批发价先不管
					// if (comm.getPriceVIP() != iaPriceVIP[i]) {
					// }
					// if (comm.getPriceWholesale() != iaPriceWholesale[i]) {
					// }

					// 判断条形码的差异
					Barcodes b = new Barcodes();
					b.setCommodityID(comm.getID());
					DataSourceContextHolder.setDbName(dbName);
					List<Barcodes> barcodesList = (List<Barcodes>) barcodesBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, b);
					if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError || barcodesList.size() == 0) {
						logger.info("读取多个条形码失败。" + barcodesBO.printErrorInfo());
						return false;
					}
					for (Barcodes barcode : barcodesList) { // ...暂时默认多包装商品只有一个条形码
						if (barcode.getBarcode().equals(iaBarcodes[i])) {
							continue;
						}
						change |= 4; // 4 = 0x0100;
						break;
					}

					if (change > 0) {
						break;
					}
				}
			}
			if (change > 0) {
				PackageUnit p = new PackageUnit();
				p.setID(iaPuID[i]);
				p.setWhichPropertyIsChanged(change);
				lspuUpdated.add(p);
			}
		}

		return true;
	}

	protected boolean checkNameAndCommMultiples(List<Commodity> multiPackagingList) {
		String checkPuName;
		for (int i = 0; i < multiPackagingList.size() - 1; i++) {
			checkPuName = multiPackagingList.get(i).getName();
			for (int j = i + 1; j < multiPackagingList.size(); j++) {
				if (checkPuName.equals(multiPackagingList.get(j).getName())) {
					logger.error("多包装商品的名称有重复");
					return false;
				}
			}
			if (multiPackagingList.get(i + 1).getRefCommodityMultiple() <= 1) {
				logger.error("多包装商品的参照商品倍数要大于1");
				return false;
			}
		}
		return true;
	}

	@Override
	protected Map<String, Object> handleCreateEx(Boolean useSYSTEM, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) throws CloneNotSupportedException {
		logger.info("商品的 handleCreateEx()，baseModel=" + baseModel);

		Commodity commodity = (Commodity) baseModel;
		File dest = (File) req.getSession().getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName());
		// 处理商品图片字段
		int lastOperationToPicture = commodity.getLastOperationToPicture();
		switch (lastOperationToPicture) {
		case LAST_OPERATION_TO_PICTURE_Upload:
			if (dest != null) {
				StringBuilder filePath = new StringBuilder(dest.getPath().replaceAll("\\\\", "/"));// filePath一定形如d:/nbr/pic/nbr_MYJ/18.jpg
				for (int i = 0; i < DIR.length(); i++) {
					filePath.setCharAt(i, Character.toLowerCase(filePath.charAt(i)));// 确保下面的替换真正替换到东西
				}
				logger.debug("filePath=" + filePath.toString());
				String commPictureDir = filePath.toString().replaceAll(DIR, "/p");
				commodity.setPicture(commPictureDir);
			}
			break;
		case LAST_OPERATION_TO_PICTURE_Clear:
		case LAST_OPERATION_TO_PICTURE_None:
		default:
			commodity.setPicture("");
			break;
		}

		// 多包装商品字段检查。获取条形码，包装单位，商品倍数，零售价，会员价，批发价，多包装商品的名字，拆分成一个个数组
		List<Commodity> multiPackagingList = getMultiPackaging(commodity);
		if (multiPackagingList == null) {
			return null;
		}
		// 生成商品的助记码
		if (commodity.getMnemonicCode() == null || commodity.getMnemonicCode().equals("")) {
			commodity.setMnemonicCode(GeneralUtil.generateMnemonicCode(commodity.getName(), Commodity.Default_MnemonicCode));
		}
		//
		Map<String, Object> params = new HashMap<String, Object>();
		boolean bReturnObject = (baseModel.getReturnObject() == 1);
		ErrorInfo ec = new ErrorInfo();
		Commodity bmFromDB = null;
		do {
			// 单品
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex() && multiPackagingList.size() == 1 && commodity.getSubCommodityInfo() == null) {
				bmFromDB = createSimple(commodity, req, dbName, multiPackagingList.get(0).getBarcodes(), ec);
			} else if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex() && multiPackagingList.size() > 1 && commodity.getSubCommodityInfo() == null) { // 单品+多包装
				if (!checkNameAndCommMultiples(multiPackagingList)) {
					return null;
				}
				bmFromDB = createSimpleAndMultiPackaging(commodity, req, dbName, multiPackagingList, ec, params);
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex() && multiPackagingList.size() == 1 && commodity.getSubCommodityInfo() != null) { // 组合商品+子商品
				bmFromDB = createCombination(commodity, req, dbName, multiPackagingList.get(0).getBarcodes(), ec, params);
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex() && multiPackagingList.size() == 1 && commodity.getSubCommodityInfo() == null) { // 服务商品
				bmFromDB = createService(commodity, req, dbName, multiPackagingList.get(0).getBarcodes(), ec);
			} else {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("未符合创建商品的规范");
				}
				return null;
			}
			//
			if (bmFromDB == null) {
				params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage());
				break;
			}
			// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此同步块
			int iPosSize = getPosSize(false, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			}
			int posID = getLoginPOSID(req);
			if (posID == INVALID_POS_ID || iPosSize > 1) {
				logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要创建同步块" : "非网页端在操作，POS机数量>1，即将创建同步块...");
				// 3、更新同步对象的DB表
				int objSequence = getSequence();
				List<List<BaseModel>> list = updateSyncDBObject(false, posID, req, bmFromDB.getID(), objSequence, SyncCache.SYNC_Type_C, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("创建同步块对象失败，将不会更新同步块缓存，错误码:" + ec.getErrorCode() + ", posID:" + posID + ", CommodityID:" + bmFromDB.getID());
					break;
				}
				logger.info("成功更新同步对象的DB表。list=" + list);

				// 4、更新同步块缓存
				updateSyncCacheObjectInMemory(false, SyncCache.SYNC_Type_C, getSyncCacheType(), objSequence, list, dbName, req);
				logger.info("成功更新同步块的缓存");
				// 处理图片文件
				switch (lastOperationToPicture) {
				case LAST_OPERATION_TO_PICTURE_Upload:
					// 上传图片（如果有）
					MultipartFile file = (MultipartFile) req.getSession().getAttribute(EnumSession.SESSION_PictureFILE.getName());
					if (dest != null && file != null) {
						renameCommodityPicture(req, dbName, bmFromDB, dest, file, ec);
					}
					break;
				case LAST_OPERATION_TO_PICTURE_Clear:
				case LAST_OPERATION_TO_PICTURE_None:
				default:
					break;
				}
			}
		} while (false);
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		if (bReturnObject) {
			params.put(KEY_Object, bmFromDB);
		}
		logger.info("返回的数据=" + params);
		return params;
	}

	// ... 函数名还不够明确说明该函数做了什么
	protected List<Commodity> getMultiPackaging(Commodity commodity) {
		if (commodity.getMultiPackagingInfo() == null) {
			logger.error("黑客行为，MultiPackagingInfo为空");
			return null;
		}
		String[] sarr = commodity.getMultiPackagingInfo().split(";");
		if (sarr.length != MULTI_PACKAGING_ELEMENT_COUNT) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("sarr的长度不符合预期的长度");
			}
			return null;
		}
		String[] iaBarcodes = sarr[0].split(",");
		Integer[] iaPuID = GeneralUtil.toIntArray(sarr[1]);
		Integer[] iaCommMultiples = GeneralUtil.toIntArray(sarr[2]);
		Double[] iaPriceRetail = GeneralUtil.toDoubleArray(sarr[3]);
		Double[] iaPriceVIP = GeneralUtil.toDoubleArray(sarr[4]);
		Double[] iaPriceWholesale = GeneralUtil.toDoubleArray(sarr[5]);
		String[] sNames = sarr[6].split(",");
		if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null || iaPriceRetail == null || iaPriceVIP == null || iaPriceWholesale == null || sNames == null) {
			logger.error("sarr参数缺失");
			return null;// 黑客行为
		}
		if (iaBarcodes.length != iaPuID.length || iaCommMultiples.length != iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length || iaPriceVIP.length != iaBarcodes.length || iaPriceWholesale.length != iaBarcodes.length
				|| sNames.length != iaBarcodes.length) {
			logger.error("数据长度不一致");
			return null;// 黑客行为
		}
		//
		List<Commodity> commodityList = new ArrayList<Commodity>();
		for (int i = 0; i < iaBarcodes.length; i++) {
			Commodity comm = new Commodity();
			comm.setName(sNames[i]);
			comm.setPackageUnitID(iaPuID[i]);
			comm.setPriceRetail(iaPriceRetail[i]);
			comm.setRefCommodityMultiple(iaCommMultiples[i]);
			comm.setPriceVIP(iaPriceVIP[i]);
			comm.setPriceWholesale(iaPriceWholesale[i]);
			comm.setBarcodes(iaBarcodes[i]);
			commodityList.add(comm);
		}
		//
		return commodityList;
	}

	protected Commodity createService(Commodity comm, HttpServletRequest req, String dbName, String sBarcodes, ErrorInfo ec) throws CloneNotSupportedException {
		do {
			Commodity bmFromDB = null;
			List<List<BaseModel>> listObjects = createCommodity(comm, req, BaseBO.CASE_Commodity_CreateService, dbName, sBarcodes, ec);
			if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
				bmFromDB = (Commodity) listObjects.get(0).get(0);
				logger.info("创建对象成功：" + bmFromDB);
				// 更新barcodes的普通缓存和同步缓存
				BaseModel barcode = listObjects.get(1).get(0);
				CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).write1(barcode, dbName, getStaffFromSession(req.getSession()).getID());
				BaseModel barcodeSyncCache = listObjects.get(2).get(0);
				SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).write1(barcodeSyncCache, dbName, BaseBO.SYSTEM);
				createCommodityShopInfo(bmFromDB, dbName, comm, getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateService, new Warehousing());
				CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, getStaffFromSession(req.getSession()).getID());
				return bmFromDB;
			}
		} while (false);
		return null;
	}

	protected Commodity createCombination(Commodity comm, HttpServletRequest req, String dbName, String sBarcodes, ErrorInfo ec, Map<String, Object> params) throws CloneNotSupportedException {
		do {
			Commodity bmFromDB = null;
			List<List<BaseModel>> listObjects = createCommodity(comm, req, BaseBO.CASE_Commodity_CreateComposition, dbName, sBarcodes, ec);
			if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
				bmFromDB = (Commodity) listObjects.get(0).get(0);
				logger.info("创建对象成功：" + bmFromDB);
				// 更新barcodes的普通缓存和同步缓存
				BaseModel barcode = listObjects.get(1).get(0);
				CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).write1(barcode, dbName, getStaffFromSession(req.getSession()).getID());
				BaseModel barcodeSyncCache = listObjects.get(2).get(0);
				SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).write1(barcodeSyncCache, dbName, BaseBO.SYSTEM);
				// 创建组合商品的子商品
				((Commodity) bmFromDB).setSubCommodityInfo(comm.getSubCommodityInfo());//
				Commodity subComm = (Commodity) createCombinationCommodity(bmFromDB, params, ec, dbName, req);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("创建组合商品的子商品失败，请运营彻底删除（不是修改状态）组合商品(ID=" + bmFromDB.getID() + ")");
					break;
				}
				bmFromDB.setListSlave1(subComm.getListSlave1());
//				comm.setnOStart(Commodity.NO_START_Default);
//				comm.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
				createCommodityShopInfo(bmFromDB, dbName, comm, getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateComposition, new Warehousing());
				CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, getStaffFromSession(req.getSession()).getID());
				return bmFromDB;
			}
		} while (false);
		return null;
	}

	protected Commodity createSimpleAndMultiPackaging(Commodity comm, HttpServletRequest req, String dbName, List<Commodity> multiPackagingList, ErrorInfo ec, Map<String, Object> params) throws CloneNotSupportedException {
		do {
			Commodity bmFromDB = null;
			bmFromDB = createSimple(comm, req, dbName, multiPackagingList.get(0).getBarcodes(), ec);
			if (bmFromDB == null) {
				logger.info("创建商品失败，错误信息为：" + ec.getErrorMessage());
				break;
			}
			createMultiPackaging(comm, multiPackagingList, bmFromDB, dbName, req, ec, params);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建多包装商品失败！请运营彻底删除RefCommodityID=" + bmFromDB.getID() + "的多包装商品，并删除单品" + bmFromDB.getID() + "。如果此单品有期初值，则应删除对应的入库记录");
				params.put(JSON_ERROR_KEY, ec.getErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, "创建多包装商品失败，失败原因：" + ec.getErrorMessage());
				break;
			}

			return bmFromDB;
		} while (false);

		return null;
	}

	protected List<List<BaseModel>> createCommodity(Commodity commodity, HttpServletRequest req, int iUserCaseID, String dbName, String sBarcodes, ErrorInfo ec) {
		commodity.setOperatorStaffID(getStaffFromSession(req.getSession()).getID());
		commodity.setSyncSequence(getSequence()); // 同步的顺序，F_SyncSequence
		commodity.setBarcodes(sBarcodes);
		return createDBObjectEx(commodity, req, ec, dbName, iUserCaseID);
	}

	protected Commodity createSimple(Commodity comm, HttpServletRequest req, String dbName, String sBarcodes, ErrorInfo ec) {
		do {
			Commodity bmFromDB = null;
			List<List<BaseModel>> listObjects = createCommodity(comm, req, BaseBO.CASE_Commodity_CreateSingle, dbName, sBarcodes, ec);
			if (ec.getErrorCode() == EnumErrorCode.EC_NoError) {
				// 判断是否为期初商品，如果是期初商品，则拿最后一个返回值，否则就拿第一个返回值
				if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
					bmFromDB = (Commodity) listObjects.get(5).get(0);
				} else {
					bmFromDB = (Commodity) listObjects.get(0).get(0);
				}
				logger.info("创建对象成功：" + bmFromDB);
				// 更新barcodes的普通缓存和同步缓存
				BaseModel barcode = listObjects.get(1).get(0);
				CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).write1(barcode, dbName, getStaffFromSession(req.getSession()).getID());
				BaseModel barcodeSyncCache = listObjects.get(2).get(0);
				SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).write1(barcodeSyncCache, dbName, BaseBO.SYSTEM);
				// 创建供应商商品
				String sProviderIDs = comm.getProviderIDs();
				if (StringUtils.isEmpty(sProviderIDs)) {
					logger.error("sProviderIDs缺失,请运营彻底删除(不是修改状态)商品ID=" + bmFromDB.getID());
					break;// 黑客行为
				}
				//
				ConfigGeneral maxProviderNO = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxProviderNOPerCommodity, getStaffFromSession(req.getSession()).getID(), ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError || maxProviderNO == null) {
					logger.error("读取配置信息失败，请运营彻底删除（不是修改状态）组合商品(ID=" + bmFromDB.getID() + ")");
					ec.setErrorMessage("读取配置信息失败，失败原因：" + ec.getErrorMessage());
					break;
				}
				//
				Integer[] iArrProviderID = GeneralUtil.toIntArray(sProviderIDs);
				if (iArrProviderID == null || iArrProviderID.length > Integer.parseInt(maxProviderNO.getValue()) || iArrProviderID.length < 0) {
					logger.error("iArrProviderID数组长度与预期的不一致,请运营彻底删除(不是修改状态)商品ID=" + bmFromDB.getID());
					break;// 黑客行为
				}
				createProviderCommodty(iArrProviderID, bmFromDB, dbName, req, ec);// 创建供应商商品
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("创建商品" + bmFromDB.getID() + "时创建供应商商品关系失败。这将导致入库普存、商品普存同存没有本商品的信息，POS无法同步到本商品。用户必须需要修改本商品、设置本商品的供应商，以便其恢复正常。" + ec.toString());
					ec.setErrorMessage("创建商品成功，但创建供应商商品关系失败。请修改本商品并设置其供应商以便其恢复正常。");
					break;
				}
				// 更新缓存
				BxConfigGeneral configGeneralNO = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
						.read1(BaseCache.CommodityNOStart, getStaffFromSession(req.getSession()).getID(), ec, BaseAction.DBName_Public);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError || configGeneralNO == null) {
					logger.error("读取配置信息失败，请运营彻底删除（不是修改状态）商品(ID=" + bmFromDB.getID() + ")");
					ec.setErrorMessage("读取配置信息失败，失败原因：" + ec.getErrorMessage());
					break;
				}
				//
				BxConfigGeneral configGeneralPrice = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
						.read1(BaseCache.CommodityPurchasingPriceStart, getStaffFromSession(req.getSession()).getID(), ec, BaseAction.DBName_Public);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError || configGeneralPrice == null) {
					logger.error("读取配置信息失败，请运营彻底删除（不是修改状态）商品(ID=" + bmFromDB.getID() + ")");
					ec.setErrorMessage("读取配置信息失败，失败原因：" + ec.getErrorMessage());
					break;
				}
				//
				Warehousing warehousing = null;
				if (comm.getnOStart() > Integer.parseInt(configGeneralNO.getValue()) && comm.getPurchasingPriceStart() > Double.parseDouble(configGeneralPrice.getValue())) {
					warehousing = (Warehousing) listObjects.get(3).get(0);
					List<BaseModel> warehousingCommodityList = listObjects.get(4);
					if (warehousing == null || warehousingCommodityList.size() == 0) {
						logger.error("期初值入库单创建失败，期初值入库的错误码=" + commodityBO.getLastErrorCode() + ", Commodity:" + bmFromDB);
						ec.setErrorCode(EnumErrorCode.EC_OtherError);
						ec.setErrorMessage("创建商品成功，但是并没有生成相应的入库单");
						break;
					}
					warehousing.setListSlave1(warehousingCommodityList);
					CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).write1(warehousing, dbName, getStaffFromSession(req.getSession()).getID());
				}
				// 创建商品门店信息
				createCommodityShopInfo(bmFromDB, dbName, comm, getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateSingle, warehousing);
				// 更新商品普通缓存
				CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, getStaffFromSession(req.getSession()).getID());
				return bmFromDB;
			}
		} while (false);

		return null;
	}

	private void createProviderCommodty(Integer[] iArrProviderID, Commodity commCreated, String dbName, HttpServletRequest req, ErrorInfo ec) {
		ProviderCommodity pc = new ProviderCommodity();
		pc.setCommodityID(commCreated.getID());
		pc.setProviderID(BaseAction.PROVIDER_ID_ToDeleteAll);
		DataSourceContextHolder.setDbName(dbName);
		providerCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
		if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("删除供应商商品失败，错误码：" + providerCommodityBO.getLastErrorMessage() + ", Commodity:" + commCreated + ", ProviderCommodity" + pc);
			// throw new RuntimeException("ProviderCommodity Delete error");
			ec.setErrorCode(providerCommodityBO.getLastErrorCode());
			ec.setErrorMessage(providerCommodityBO.getLastErrorMessage());
			return;
		}

		List<Integer> iArrProviderIDList = GeneralUtil.sortAndDeleteDuplicated(iArrProviderID);
		Boolean hasDBError = false;
		for (int iID : iArrProviderIDList) {
			pc.setCommodityID(commCreated.getID());
			pc.setProviderID(iID);
			DataSourceContextHolder.setDbName(dbName);
			providerCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
			logger.info("供应商商品的创建的错误码=" + providerCommodityBO.getLastErrorCode());
			if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建供应商商品失败，错误码：" + providerCommodityBO.getLastErrorMessage() + ", Commodity:" + commCreated + ", ProviderCommodity" + pc);
				// throw new RuntimeException("添加供应商失败！即将回滚DB...");
				ec.setErrorMessage("创建供应商商品失败, 失败原因：" + providerCommodityBO.getLastErrorMessage() + "。 请重新尝试");
				hasDBError = true;
				break;
			}
		}

		if (hasDBError) {
			ec.setErrorCode(EnumErrorCode.EC_PartSuccess);
		}
	}

	private void createMultiPackaging(Commodity commodity, List<Commodity> multiPackagingList, Commodity commCreated, String dbName, HttpServletRequest req, ErrorInfo ec, Map<String, Object> params) {
		List<List<BaseModel>> listObjects = null;
		BaseModel barcode = null;
		Boolean hasDBError = false;
		for (int i = 1; i < multiPackagingList.size(); i++) { // 由于前端传来的多包装数组是包括商品本身的，所以从数组的第二个开始。
			Commodity comm = multiPackagingList.get(i);
			commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
			commodity.setRefCommodityID(commCreated.getID());
			commodity.setOperatorStaffID(getStaffFromSession(req.getSession()).getID());
			commodity.setnOStart(BaseAction.INVALID_NO);
			commodity.setPurchasingPriceStart(BaseAction.INVALID_NO);
			commodity.setStartValueRemark("");
			commodity.setSyncSequence(getSequence()); // 同步的顺序，F_SyncSequence
			commodity.setName(comm.getName());
			commodity.setPackageUnitID(comm.getPackageUnitID());
			commodity.setPriceRetail(comm.getPriceRetail());
			commodity.setRefCommodityMultiple(comm.getRefCommodityMultiple());
			commodity.setPriceVIP(comm.getPriceVIP());
			commodity.setPriceWholesale(comm.getPriceWholesale());
			commodity.setBarcodes(comm.getBarcodes());
			DataSourceContextHolder.setDbName(dbName);
			listObjects = commodityBO.createObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateMultiPackaging, commodity);
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (commodityBO.getLastErrorCode() == EnumErrorCode.EC_Duplicated) {
					logger.info("创建多包装商品时发现其名字跟已有的商品的名字重复，将不创建此多包装商品。");
					params.put(KEY_HTMLTable_Parameter_msg, "创建多包装商品失败，失败原因：" + commodityBO.getLastErrorMessage());
					continue; // 多包装商品的名字跟已有的商品的名字重复，在这里不认为是一个错误，但不会再创建这个多包装商品
				}
				ec.setErrorMessage(commodityBO.getLastErrorMessage());
				hasDBError = true;
				break;
			}
			logger.info("多包装商品创建的错误码=" + commodityBO.getLastErrorMessage() + "listObjects=" + listObjects);

			Commodity commodityCreate = (Commodity) listObjects.get(0).get(0);
			createCommodityShopInfo(commodityCreate, dbName, commodity, getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateMultiPackaging, new Warehousing());
			//
			CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).write1(commodityCreate, dbName, getStaffFromSession(req.getSession()).getID());

			List<List<BaseModel>> list = updateSyncDBObject(true, getLoginPOSID(req), req, commodityCreate.getID(), getSequence(), SyncCache.SYNC_Type_C, ec, dbName);
			if (list == null) {
				String err = "单品已经创建成功，但是创建多包装商品同步缓存到DB时失败。请重新修改一次商品的副单位。";
				logger.error(err);
				ec.setErrorMessage(err);
				hasDBError = true;
				break;
			} else {
				logger.info("创建多包装商品同步缓存到DB成功=" + list);
				BaseSyncCache commoditySyncCache = (list.size() == 1 ? (BaseSyncCache) list.get(0) : (BaseSyncCache) list.get(0).get(0));
				SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CommoditySyncInfo).write1(commoditySyncCache, dbName, BaseBO.SYSTEM);
			}

			// 更新barcodes的普通缓存和同步缓存
			barcode = listObjects.get(1).get(0);
			CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).write1(barcode, dbName, getStaffFromSession(req.getSession()).getID());
			BaseModel barcodeSyncCache = listObjects.get(2).get(0);
			SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).write1(barcodeSyncCache, dbName, BaseBO.SYSTEM);
		}

		if (hasDBError) {
			ec.setErrorCode(EnumErrorCode.EC_PartSuccess);
		}
	}

	/** 解析并创建组合商品的子商品 */
	private BaseModel createCombinationCommodity(BaseModel baseModel, Map<String, Object> params, ErrorInfo ec, String dbName, HttpServletRequest req) {
		logger.info("新增组合商品子商品，json=" + ((Commodity) baseModel).getSubCommodityInfo());

		com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(((Commodity) baseModel).getSubCommodityInfo());
		JSONArray jsonArray = json.getJSONArray(BaseModel.field.getFIELD_NAME_listSlave1());

		if (jsonArray.size() < 2) {
			logger.info("->子商品数量小于2!");
			ec.setErrorCode(EnumErrorCode.EC_BusinessLogicNotDefined);
			return null;
		}

		List<SubCommodity> listSubCommodity = new ArrayList<SubCommodity>();
		for (int i = 0; i < jsonArray.size(); i++) {
			com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
			SubCommodity subCommodity = (SubCommodity) new SubCommodity().parse1(jsonObject);
			subCommodity.setCommodityID(baseModel.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			SubCommodity sc = (SubCommodity) subCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, subCommodity);
			if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("->创建子商品失败。子商品ID=" + subCommodity.getSubCommodityID());
				ec.setErrorCode(EnumErrorCode.EC_PartSuccess);
				ec.setErrorMessage("创建子商品失败，失败原因：" + subCommodityBO.getLastErrorMessage());
				return null;
			}
			logger.info("创建成功，对象为" + sc);

			listSubCommodity.add(sc);
		}
		baseModel.setListSlave1(listSubCommodity);

		return baseModel;
	}

	// 重写的createDBObject，只适用CommoditySyncAction
	protected List<List<BaseModel>> createDBObjectEx(BaseModel baseModel, HttpServletRequest req, ErrorInfo ec, String dbName, int iUseCaseID) {
		logger.info("向DB中插入数据，可能需要插主表和从表");
		logger.info("baseModel=" + baseModel);

		List<List<BaseModel>> bmFromDB = null;
		DataSourceContextHolder.setDbName(dbName);
		bmFromDB = commodityBO.createObjectEx(getStaffFromSession(req.getSession()).getID(), iUseCaseID, baseModel);
		ec.setErrorCode(commodityBO.getLastErrorCode());
		ec.setErrorMessage(commodityBO.getLastErrorMessage());
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("创建普通对象失败，错误码=" + ec.getErrorCode());
			return null;
		} else {
			logger.info("创建普通对象成功，bmFromDB=" + bmFromDB.toString());
		}

		return bmFromDB;
	}

	@Override
	protected BaseModel updateDBObject(Boolean useSYSTEM, int caseID, BaseModel baseModel, ModelMap model, HttpServletRequest req, ErrorInfo ec, String dbName) {
		logger.info("商品的 updateDBObject()，baseModel=" + baseModel.toString());

		DataSourceContextHolder.setDbName(dbName);
		BaseModel bmFromDB = getModelBO().updateObject(getStaffFromSession(req.getSession()).getID(), caseID, baseModel);
		ec.setErrorCode(getModelBO().getLastErrorCode());
		logger.info("修改商品的普通对象，错误码=：" + ec.getErrorCode().toString() + ",bmFromDB=" + (bmFromDB == null ? "null" : bmFromDB.toString()));
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			// throw new RuntimeException("Commodity Update error");
			ec.setErrorMessage(getModelBO().getLastErrorMessage());
			return null;
		}

		// ...前端那边将来可能会专门出一个新页面做商品价格的修改。若然，这部分代码将移到相应的action中。
		DataSourceContextHolder.setDbName(dbName);
		bmFromDB = getModelBO().updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_UpdatePrice, baseModel);
		ec.setErrorCode(getModelBO().getLastErrorCode());
		logger.info("修改商品的普通对象的价格，错误码=：" + ec.getErrorCode().toString() + ",bmFromDB=" + (bmFromDB == null ? "null" : bmFromDB.toString()));
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			// throw new RuntimeException("CommodityPrice Update error");
			ec.setErrorMessage(getModelBO().getLastErrorMessage());
			return null;
		}

		return bmFromDB;
	}

	@Override
	protected Map<String, Object> handleUpdateEx(Boolean useSYSTEM, int caseID, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) throws CloneNotSupportedException {
		logger.info("商品的 handleUpdateEx()，baseModel=" + baseModel.toString());

		Staff staff = getStaffFromSession(req.getSession());
		Map<String, Object> params = new HashMap<String, Object>();
		Commodity commodity = (Commodity) baseModel;
		// ... 截取参数和handleCreateEx()中类似。
		File dest = (File) req.getSession().getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName());

		// 处理商品图片字段
		int lastOperationToPicture = commodity.getLastOperationToPicture();
		switch (lastOperationToPicture) {
		case LAST_OPERATION_TO_PICTURE_Upload:
			if (dest != null) {
				StringBuilder filePath = new StringBuilder(dest.getPath().replaceAll("\\\\", "/"));// filePath一定形如d:/nbr/pic/nbr_MYJ/18.jpg
				for (int i = 0; i < DIR.length(); i++) {
					filePath.setCharAt(i, Character.toLowerCase(filePath.charAt(i)));// 确保下面的替换真正替换到东西
				}
				logger.debug("filePath=" + filePath.toString());
				String commPictureDir = filePath.toString().replaceAll(DIR, "/p");
				commodity.setPicture(commPictureDir);
			}
			break;
		case LAST_OPERATION_TO_PICTURE_None:
			commodity.setPicture(FLAG_WillNotUpdatePictureInSP);
			break;
		case LAST_OPERATION_TO_PICTURE_Clear:
		default:
			commodity.setPicture("");
			break;
		}

		if (commodity.getMultiPackagingInfo() == null) {
			logger.error("黑客行为，MultiPackagingInfo为null");
			return null;
		}
		String[] sarr = (commodity.getMultiPackagingInfo()).split(";");
		if (sarr.length != MULTI_PACKAGING_ELEMENT_COUNT) {
			logger.info("sarr的长度不符合预期");
			params.put(KEY_Object, "");
			return params;
		}

		String[] iaBarcodes = sarr[0].split(",");
		Integer[] iaPuID = GeneralUtil.toIntArray(sarr[1]);
		Integer[] iaCommMultiples = GeneralUtil.toIntArray(sarr[2]);
		Double[] iaPriceRetail = GeneralUtil.toDoubleArray(sarr[3]);
		Double[] iaPriceVIP = GeneralUtil.toDoubleArray(sarr[4]);
		Double[] iaPriceWholesale = GeneralUtil.toDoubleArray(sarr[5]);
		String[] sNames = sarr[6].split(",");
		if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null || iaPriceRetail == null || iaPriceVIP == null || iaPriceWholesale == null || sNames == null) {// ...未检查它们的维度是否一致
			logger.info("sarr的参数缺失");
			params.put(KEY_Object, "");
			return params;
		}

		if (iaBarcodes.length != iaPuID.length || iaCommMultiples.length != iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length || iaPriceVIP.length != iaBarcodes.length || iaPriceWholesale.length != iaBarcodes.length
				|| sNames.length != iaBarcodes.length) {
			logger.info("数组的长度不相等");
			params.put(KEY_Object, "");
			return params;
		}

		// 判断是否多包装商品
		if (iaBarcodes.length > 1) {
			if (commodity.getType() != EnumCommodityType.ECT_Normal.getIndex()) {
				logger.error("多包装商品的参考商品必须是单品！");
				params.put(KEY_Object, "");
				return params;
			}
			// 判断多包装商品的名称是否有重复的
			boolean duPlicatePuName = false;
			String checkPuName;
			for (int i = 0; i < sNames.length - 1; i++) {
				checkPuName = sNames[i];
				for (int j = i + 1; j < sNames.length; j++) {
					if (checkPuName.equals(sNames[j])) {
						duPlicatePuName = true;
						break;
					}
				}
			}
			if (duPlicatePuName) {
				// logger.error("多包装商品的名称有重复");
				// return null;
				logger.info("多包装商品的名称有重复");
				params.put(KEY_Object, "");
				return params;
			}

			// 创建普通商品存在多包装时，如果商品倍数小于等于1
			boolean illegalIaCommMultiples = false;
			for (int i = 1; i < iaCommMultiples.length; i++) {
				if (iaCommMultiples[i] <= 1) {
					illegalIaCommMultiples = true;
					break;
				}
			}
			if (illegalIaCommMultiples) {
				// logger.error("多包装商品的参照商品倍数要大于1");
				// return null;
				logger.info("多包装商品的参照商品倍数要大于1");
				params.put(KEY_Object, "");
				return params;
			}
		}

		ErrorInfo ecOut = new ErrorInfo();
		Integer[] iArrProviderID = null;
		if (commodity.getType() != EnumCommodityType.ECT_Service.getIndex()) {
			ConfigGeneral maxProviderNO = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxProviderNOPerCommodity, getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("从缓存中读取MaxProviderNOPerCommodity失败！");
				params.put(JSON_ERROR_KEY, ecOut.getErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
				return params;
			}
			//
			String sProviderIDs = commodity.getProviderIDs();
			if (StringUtils.isEmpty(sProviderIDs)) {
				logger.info("sProviderID为空");
				params.put(KEY_Object, "");
				return params;
			}
			//
			iArrProviderID = GeneralUtil.toIntArray(sProviderIDs);
			if (iArrProviderID == null || iArrProviderID.length > Integer.parseInt(maxProviderNO.getValue()) || iArrProviderID.length < 0) {
				logger.info("iArrProviderID的参数不符合预期");
				params.put(KEY_Object, "");
				return params;
			}
		}

		// 判断要修改商品是否为单品或服务商品
		Commodity commR1 = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodity.getID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
		logger.info("读取一个商品，错误码=" + ecOut.getErrorCode().toString() + ",commR1=" + commR1);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commR1 == null) {
			// throw new RuntimeException("Commodity R1 error");
			params.put(JSON_ERROR_KEY, ecOut.getErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, "查询一个商品失败。失败原因：" + ecOut.getErrorMessage());
			return params;
		}
		//
		if (commR1.getType() != CommodityType.EnumCommodityType.ECT_Normal.getIndex() && commR1.getType() != EnumCommodityType.ECT_Service.getIndex()) {
			logger.info("commR1读取出来的商品不为普通商品或服务商品");
			params.put(KEY_Object, "");
			return params;
		}

		boolean bReturnObject = (baseModel.getReturnObject() == 1);

		BaseModel bmFromDB = null;
		ErrorInfo ec = new ErrorInfo();
		do {
			logger.info("即将修改对象：" + commR1);
			// 1、更新普通对象的DB表
			bmFromDB = updateDBObject(useSYSTEM, caseID, commodity, model, req, ec, dbName);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("修改商品普通对象失败，错误码=" + ec.getErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage());
				break;
			}
			logger.info("修改对象成功：" + bmFromDB);
			// 2、删除普通对象的缓存
			CacheManager.getCache(dbName, getCacheType()).delete1(bmFromDB);
			// 查询商品对应的商品门店信息
			CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
			commodityShopInfo.setCommodityID(bmFromDB.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodityShopInfoBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, commodityShopInfo);
			if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询商品门店信息对象失败，错误码=" + commodityShopInfoBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, commodityShopInfoBO.getLastErrorMessage());
				break;
			}
			bmFromDB.setListSlave2(listCommodityShopInfo);
			// 服务商品没有供应商商品且并不需要修改相关多包装
			if (commR1.getType() != EnumCommodityType.ECT_Service.getIndex()) {
				ProviderCommodity pc = new ProviderCommodity();
				pc.setCommodityID(bmFromDB.getID());
				DataSourceContextHolder.setDbName(dbName);
				providerCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
				logger.info("删除一个供应商商品的错误码=" + barcodesBO.getLastErrorCode());
				if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("删除供应商商品失败,需要删除供应商商品的商品ID为=" + pc.getCommodityID());
					// throw new RuntimeException("ProviderCommodity Delete error");
					ec.setErrorCode(providerCommodityBO.getLastErrorCode());
					params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,删除相应的供应商失败,失败原因:" + providerCommodityBO.getLastErrorMessage() + ",请重新修改后再次提交！");
					break;
				}
				// 去除iArrProviderID的重复值
				List<Integer> iArrProviderIDList = GeneralUtil.sortAndDeleteDuplicated(iArrProviderID);
				Boolean hasDBError = false;
				for (int iID : iArrProviderIDList) {
					pc.setCommodityID(bmFromDB.getID());
					pc.setProviderID(iID);
					DataSourceContextHolder.setDbName(dbName);
					providerCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
					logger.info("创建一个供应商商品的错误码=" + providerCommodityBO.getLastErrorCode());
					if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.error("创建供应商商品失败,供应商ID=" + iID + ",商品ID=" + bmFromDB.getID());
						// throw new RuntimeException("添加供应商失败！即将回滚DB...");
						params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,创建一部分供应商商品失败.失败原因：" + providerCommodityBO.getLastErrorMessage() + "请重新选择后再次尝试");
						hasDBError = true;
						break;
					}
				}
				//
				if (hasDBError) {
					ec.setErrorCode(EnumErrorCode.EC_PartSuccess);
					break;
				}
				//
				if (!processPackageUnitDifference(commodity, dbName, req, params, iArrProviderIDList, pc, iaBarcodes, iaPuID, iaCommMultiples, iaPriceRetail, iaPriceVIP, iaPriceWholesale, sNames, ec, staff, bmFromDB)) {
					break;
				}
			}
			// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此同步块
			int iPosSize = getPosSize(false, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取POS失败，错误码=" + ec.getErrorCode());
				break;
			}
			int posID = getLoginPOSID(req);
			if (posID == INVALID_POS_ID || iPosSize > 1) {
				logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要更新同步块" : "非网页端在操作，POS机数量>1，即将更新同步块...");
				// 3、更新同步对象的DB表
				int objSequence = getSequence();
				List<List<BaseModel>> list = updateSyncDBObject(false, posID, req, bmFromDB.getID(), objSequence, SyncCache.SYNC_Type_U, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("更新Commodity在DB中的同步对象，错误码=" + ec.getErrorCode());
					break;
				}
				logger.info("更新后:" + list);

				// 4、更新同步块缓存
				updateSyncCacheObjectInMemory(false, SyncCache.SYNC_Type_U, getSyncCacheType(), objSequence, list, dbName, req);

				// 处理图片文件
				switch (lastOperationToPicture) {
				case LAST_OPERATION_TO_PICTURE_Upload:
					// 上传图片（如果有）
					MultipartFile file = (MultipartFile) req.getSession().getAttribute(EnumSession.SESSION_PictureFILE.getName());
					if (dest != null && file != null) {
						renameCommodityPicture(req, dbName, bmFromDB, dest, file, ec);
					}
					break;
				case LAST_OPERATION_TO_PICTURE_Clear:
					deleteCommodityPicture(req, dbName, bmFromDB, ec);
					break;
				case LAST_OPERATION_TO_PICTURE_None:
				default:
					break;
				}
			}
			break;
		} while (false);

		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		if (bReturnObject) {
			params.put(KEY_Object, bmFromDB);
		}
		logger.info("返回的数据=" + params);

		return params;
	}

	// 商品图片的删除
	private void deleteCommodityPicture(HttpServletRequest req, String dbName, BaseModel bm, ErrorInfo ec) {
		try {
			// 现在传入的文件是否已经存在，如果有删除该文件
			File dest1 = (File) new File(BaseAction.CommodityPictureDir + dbName);
			String[] list1 = dest1.list();
			if (list1.length > 0) {
				for (String string : list1) {
					// 已有的文件获取除后缀名外的字符串，对比commodityID，如果存在删除该文件
					String picName = string.substring(0, string.lastIndexOf("."));
					if (picName.equals(String.valueOf(bm.getID()))) {
						File file5 = new File(BaseAction.CommodityPictureDir + dbName + "/" + string);
						file5.delete();
					}
				}
			}
		} catch (Exception e) {
			logger.info("删除图片失败，错误信息：" + e.getMessage());
			ec.setErrorCode(EnumErrorCode.EC_PartSuccess);
			ec.setErrorMessage("删除图片时出现错误！");
		}
	}

	@SuppressWarnings("unchecked")
	private boolean processPackageUnitDifference(Commodity commodity, String dbName, HttpServletRequest req, Map<String, Object> params, List<Integer> iArrProviderIDList, ProviderCommodity pc, String[] iaBarcodes, Integer[] iaPuID,
			Integer[] iaCommMultiples, Double[] iaPriceRetail, Double[] iaPriceVIP, Double[] iaPriceWholesale, String[] sNames, ErrorInfo ec, Staff staff, BaseModel bmFromDB) throws CloneNotSupportedException {
		// 获取条形码，包装单位，商品倍数，零售价，和批发价,拆分成一个个数组
		List<PackageUnit> lspu = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();
		boolean checkPackageUnitSuccess = checkPackageUnit(commodity, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbName, logger, packageUnitBO, commodityBO, barcodesBO, getStaffFromSession(req.getSession()).getID());
		if (checkPackageUnitSuccess && (lspuCreated.size() > 0 || lspuUpdated.size() > 0 || lspuDeleted.size() > 0)) { // ...需要重构。找Giggs。应该正确处理返回false，或根据各集合的情况分别进行处理
			DataSourceContextHolder.setDbName(dbName);
			// 找出商品commodity对应的多包装商品，放在listMPCommodity中
			List<Commodity> listMPCommodity = (List<Commodity>) commodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_RetrieveNMultiPackageCommodity, commodity);
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取多个条形码失败，错误码=" + commodityBO.getLastErrorCode());
				params.put(KEY_Object, "");
				return false;
			}

			// 重建多包装商品和供应商的关系
			for (Commodity comm : listMPCommodity) {
				// 删除每个多包装商品的供应商商品关系
				pc.setCommodityID(comm.getID());
				pc.setProviderID(BaseAction.PROVIDER_ID_ToDeleteAll);
				DataSourceContextHolder.setDbName(dbName);
				providerCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
				if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("删除一个供应商失败，错误=" + providerCommodityBO.getLastErrorCode() + "\t" + providerCommodityBO.getLastErrorMessage() + ",供应商的商品ID=" + comm.getID());
					params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,删除部分与商品相关联的供应商失败,失败原因：" + providerCommodityBO.getLastErrorMessage() + "。请重新选择后再次尝试");
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
					return false;
				}
				// 建立每个多包装商品的供应商商品关系
				for (int iID : iArrProviderIDList) {
					pc.setCommodityID(comm.getID());
					pc.setProviderID(iID);
					DataSourceContextHolder.setDbName(dbName);
					providerCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
					if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("添加一个供应商失败:" + providerCommodityBO.getLastErrorCode() + "\t" + providerCommodityBO.getLastErrorMessage());
						params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
						params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,添加相应的供应商失败,失败原因：" + providerCommodityBO.getLastErrorMessage() + "。请重新选择后再次尝试");
						return false;
					}
				}
			}
			// 检查 lspu内部有无重复的副单位。如果有，当黑客行为处理。
			if (lspu.size() != new HashSet<PackageUnit>(lspu).size()) {
				logger.info("lspu内部存在重复的副单位");
				params.put(KEY_Object, "");
				return false;
			}

			// 检查lspuDeleted中对应的多包装商品有无在其它地方使用，如果有，返回7。
			for (Object obj : listMPCommodity) {
				Commodity commodityOfMultiPackaging = (Commodity) obj;

				// 删除相应多包装商品
				for (PackageUnit pu : lspuDeleted) {
					if (commodityOfMultiPackaging.getPackageUnitID() != pu.getID()) {
						continue;
					}

					// 检查删除这个多包装商品的依赖性
					DataSourceContextHolder.setDbName(dbName);
					commodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_CheckDeleteDependency, commodityOfMultiPackaging);
					if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError && commodityBO.getLastErrorCode() == EnumErrorCode.EC_BusinessLogicNotDefined) {
						logger.info("lspuDeleted中对应的多包装商品已有其它地方使用" + commodityBO.printErrorInfo());
						ec.setErrorCode(commodityBO.getLastErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, "该商品已有零售记录不能修改");
						return false;// ...这个时候，并未恢复之前更新过的商品。需要讨论怎样做
					}

					commodityOfMultiPackaging.setOperatorStaffID(staff.getID());
					Map<String, Object> m = handleDeleteEx(false, commodityOfMultiPackaging, new ModelMap(), req, dbName, BaseBO.CASE_Commodity_DeleteMultiPackaging);
					if (m.get(BaseAction.JSON_ERROR_KEY) != EnumErrorCode.EC_NoError.toString()) {
						logger.info("错误信息：" + m.get(KEY_HTMLTable_Parameter_msg));
						params.put(JSON_ERROR_KEY, m.get(BaseAction.JSON_ERROR_KEY).toString());
						params.put(KEY_HTMLTable_Parameter_msg, m.get(KEY_HTMLTable_Parameter_msg).toString());
						return false;
					}
					//
					pc.setCommodityID(commodityOfMultiPackaging.getID());
					pc.setProviderID(BaseAction.PROVIDER_ID_ToDeleteAll);
					DataSourceContextHolder.setDbName(dbName);
					providerCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
					if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("删除供应商商品失败，错误码=" + providerCommodityBO.getLastErrorCode());
						// throw new RuntimeException("Delete MultipackCommodity provider error!!!");
						params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
						params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,删除部分相对应的多包装商品失败,失败原因：" + providerCommodityBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
						return false;
					}
					break;
				}

				// 更新对应的多包装商品的相关字段：Name、倍数、零售价、采购价、会员价、批发价。
				for (PackageUnit pu : lspuUpdated) {
					if (commodityOfMultiPackaging.getPackageUnitID() != pu.getID()) {
						continue;
					}
					for (int i = 1; i < iaBarcodes.length; i++) {
						if (pu.getID() == iaPuID[i]) {
							commodityOfMultiPackaging.setName(sNames[i]);
							commodityOfMultiPackaging.setRefCommodityMultiple(iaCommMultiples[i]);
							commodityOfMultiPackaging.setPriceRetail(iaPriceRetail[i]);
							commodityOfMultiPackaging.setPriceVIP(iaPriceVIP[i]);
							commodityOfMultiPackaging.setPriceWholesale(iaPriceWholesale[i]);
							commodityOfMultiPackaging.setOperatorStaffID(getStaffFromSession(req.getSession()).getID());
							commodityOfMultiPackaging.setMultiPackagingInfo(commodity.getMultiPackagingInfo()); // ...
							commodityOfMultiPackaging.setNO(Commodity.DEFAULT_VALUE_CommodityNO); // 多包装商品的库存为0
							//
							boolean nameIsChanged = ((pu.getWhichPropertyIsChanged() & 8) == 8);
							boolean multipleIsChanged = ((pu.getWhichPropertyIsChanged() & 2) == 2);
							if (nameIsChanged || multipleIsChanged) {
								DataSourceContextHolder.setDbName(dbName);
								commodityBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_UpdateCommodityOfMultiPackaging, commodityOfMultiPackaging);
								if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
									logger.info("修改多包装商品失败，错误码=" + commodityBO.getLastErrorCode());
									// throw new RuntimeException("Update MultipackCommodity error!!!");
									params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
									params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,修改部分相对应的多包装商品信息失败,失败原因：" + commodityBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
									return false;
								}
							}
							//
							boolean retailPriceIsChanged = ((pu.getWhichPropertyIsChanged() & 1) == 1);
							if (retailPriceIsChanged) {
								commodityOfMultiPackaging.setShopID(commodity.getShopID());
								DataSourceContextHolder.setDbName(dbName);
								commodityBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_UpdatePrice, commodityOfMultiPackaging); // ...
								if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
									logger.info("修改多包装价格失败，错误码=" + commodityBO.getLastErrorCode());
									// throw new RuntimeException("Update CommodityPrice error!!!");
									params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
									params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,更新部分商品的价格失败,失败原因：" + commodityBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
									return false;
								}
							}

							if (nameIsChanged || multipleIsChanged || retailPriceIsChanged) {
								// 修改了多包装，删除相应的普通缓存
								CacheManager.getCache(dbName, getCacheType()).delete1(commodityOfMultiPackaging);
								// 创建多包装商品的U型同步块
								// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此同步块
								int iPosSize = getPosSize(false, ec, dbName, req);
								if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
									logger.info("读取POS失败，错误码=" + ec.getErrorCode());
									break;
								}
								int posID = getLoginPOSID(req);
								if (posID == INVALID_POS_ID || iPosSize > 1) {
									logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要更新同步块" : "非网页端在操作，POS机数量>1，即将更新同步块...");
									// 3、更新同步对象的DB表
									int objSequence = getSequence();
									List<List<BaseModel>> list = updateSyncDBObject(false, posID, req, commodityOfMultiPackaging.getID(), objSequence, SyncCache.SYNC_Type_U, ec, dbName);
									if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
										logger.info("更新Commodity在DB中的同步对象，错误码=" + ec.getErrorCode());
										break;
									}
									logger.info("更新后:" + list);

									// 4、更新同步块缓存
									updateSyncCacheObjectInMemory(false, SyncCache.SYNC_Type_U, getSyncCacheType(), objSequence, list, dbName, req);
								}
							}
							//
							boolean barcodeIsChange = ((pu.getWhichPropertyIsChanged() & 4) == 4);
							if (barcodeIsChange) {
								// ...条形码最后一个不能够删除
								Barcodes barcodes = new Barcodes();
								barcodes.setCommodityID(commodityOfMultiPackaging.getID());

								DataSourceContextHolder.setDbName(dbName);
								List<?> barcodesList = barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, barcodes);
								if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
									logger.info("读取多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
									// throw new RuntimeException("RetrieveN Barcodes error!!!");
									params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
									params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,查询相应的条形码失败,失败原因：" + barcodesBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
									return false;
								}

								barcodes.setBarcode(iaBarcodes[i]);
								barcodes.setOperatorStaffID(staff.getID());

								for (Object o : barcodesList) {
									Barcodes b = (Barcodes) o;
									b.setOperatorStaffID(staff.getID());

									DataSourceContextHolder.setDbName(dbName);
									barcodesBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, b);
									if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
										logger.info("删除多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
										// throw new RuntimeException("Delete Barcodes error!!!");
										params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
										params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,删除部分商品相对应的条形码失败,失败原因：" + barcodesBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
										return false;
									}

									// 创建Barcode的D型同步块和更新缓存
									CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).delete1(b);
									List<List<BaseModel>> listBarcodeSyncDBObjectD = createBarcodeSyncCacheInDB(b, EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_D, req, dbName, ec);
									if (listBarcodeSyncDBObjectD == null) {
										break;
									}

									// 更新Barcode的同步块缓存
									updateSyncCacheObjectInMemory(false, SyncCache.SYNC_Type_D, EnumSyncCacheType.ESCT_BarcodesSyncInfo, getSequence(), listBarcodeSyncDBObjectD, dbName, req);
								}

								DataSourceContextHolder.setDbName(dbName);
								BaseModel createBarcodes = barcodesBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, barcodes);
								if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
									logger.info("创建多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
									// throw new RuntimeException("Create Barcodes error!!!");
									params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
									params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,创建部分商品相对应的条形码失败,失败原因：" + barcodesBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
									return false;
								}
								// 创建条形码C型同步块和更新缓存
								CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).write1(createBarcodes, dbName, staff.getID());
								List<List<BaseModel>> listBarcodeSyncDBObjectC = createBarcodeSyncCacheInDB(createBarcodes, EnumSyncCacheType.ESCT_BarcodesSyncInfo, SyncCache.SYNC_Type_C, req, dbName, ec);
								if (listBarcodeSyncDBObjectC == null) {
									break;
								}
								// 更新Barcode的同步块缓存
								updateSyncCacheObjectInMemory(false, SyncCache.SYNC_Type_C, EnumSyncCacheType.ESCT_BarcodesSyncInfo, getSequence(), listBarcodeSyncDBObjectC, dbName, req);
							}

							break;
						}
					}
				}
			}

			// 创建新增的多包装商品
			for (PackageUnit pu : lspuCreated) {
				for (int i = 1; i < iaBarcodes.length; i++) {
					if (pu.getID() == iaPuID[i]) {
						commodity.setRefCommodityID(bmFromDB.getID());
						commodity.setName(sNames[i]);
						commodity.setPackageUnitID(iaPuID[i]);
						commodity.setRefCommodityMultiple(iaCommMultiples[i]);
						commodity.setPriceRetail(iaPriceRetail[i]);
						commodity.setPriceVIP(iaPriceVIP[i]);
						commodity.setPriceWholesale(iaPriceWholesale[i]);
						commodity.setType(2);
						commodity.setnOStart(BaseAction.INVALID_NO);
						commodity.setPurchasingPriceStart(BaseAction.INVALID_NO);
						commodity.setBarcodes(iaBarcodes[i]);
						commodity.setNO(0);
						commodity.setOperatorStaffID(staff.getID());
						commodity.setLastOperationToPicture(LAST_OPERATION_TO_PICTURE_Upload);

						DataSourceContextHolder.setDbName(dbName);
						List<List<BaseModel>> bmList = commodityBO.createObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateMultiPackaging, commodity);
						if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
							logger.info("创建商品失败，错误码=" + commodityBO.getLastErrorCode());
							params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
							params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,创建部分多包装商品失败,失败原因：" + commodityBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
							return false;
						}
						// 创建了多包装商品，更新商品的普通缓存
						Commodity multyCommCreated = (Commodity) bmList.get(0).get(0);
						createCommodityShopInfo(multyCommCreated, dbName, commodity, getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_CreateMultiPackaging, new Warehousing());
						CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).write1(multyCommCreated, dbName, getStaffFromSession(req.getSession()).getID());
						// 创建多包装商品的C型同步块
						List<List<BaseModel>> list = updateSyncDBObject(true, getLoginPOSID(req), req, multyCommCreated.getID(), getSequence(), SyncCache.SYNC_Type_C, ec, dbName);
						if (list == null) {
							String err = "创建多包装商品同步缓存到DB时失败";
							logger.error(err);
							ec.setErrorMessage(err);
							params.put(JSON_ERROR_KEY, ec.getErrorCode());
							params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage());
							return false;
						} else {
							logger.info("创建多包装商品同步缓存到DB成功=" + list);
							BaseSyncCache commoditySyncCache = (list.size() == 1 ? (BaseSyncCache) list.get(0) : (BaseSyncCache) list.get(0).get(0));
							SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CommoditySyncInfo).write1(commoditySyncCache, dbName, BaseBO.SYSTEM);
						}
						//
						Barcodes bar = (Barcodes) bmList.get(1).get(0);
						if (bar == null) {
							logger.info("创建条形码失败，错误码=" + commodityBO.getLastErrorCode());
							// throw new RuntimeException("Barcodes create error!!!");
							params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
							params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,创建部分多包装商品的条形码失败,失败原因：" + commodityBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
							return false;
						}
						// 更新条形码的普通缓存
						CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).write1(bar, dbName, getStaffFromSession(req.getSession()).getID());
						// 在SP创建了条形码的的C型同步块之后，要加入条形码的同步缓存
						BaseSyncCache barcodesSyncCache = (BaseSyncCache) bmList.get(2).get(0);
						if (barcodesSyncCache == null) {
							logger.info("创建条形码同步块失败，错误码=" + commodityBO.getLastErrorCode());
							params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
							params.put(KEY_HTMLTable_Parameter_msg, "更新商品后,创建部分多包装商品的条形码同步块失败,失败原因：" + commodityBO.getLastErrorMessage() + "。请重新修改后再次尝试！");
							return false;
						}
						SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).write1(barcodesSyncCache, dbName, BaseBO.SYSTEM);
						break;
					}
				}
			}
		} else if (!checkPackageUnitSuccess) {
			ec.setErrorCode(EnumErrorCode.EC_WrongFormatForInputField);
			ec.setErrorMessage("多包装商品信息格式不正确"); // ...应当黑客行为处理
			return false;
		}

		return true;
	}

	protected List<List<BaseModel>> createBarcodeSyncCacheInDB(BaseModel bm, EnumSyncCacheType syncCacheType, String syncType, HttpServletRequest req, String dbName, ErrorInfo ec) {
		// 能够根据传入的同步块对象进行相应的创建同步块，和同步块调度
		int iPosSize = getPosSize(false, ec, dbName, req);
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取POS失败，错误码=" + ec.getErrorCode());
			return null;
		}
		int posID = getLoginPOSID(req);
		if (posID == INVALID_POS_ID || iPosSize > 1) {
			logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要更新同步块" : "非网页端在操作，POS机数量>1，即将更新同步块...");
			// 更新Barcode同步对象的DB表
			int objSequence = getSequence();

			logger.info("更新DB中Barcode的同步对象");

			BaseSyncCache vscCreate = getSyncCacheModel();
			vscCreate.setSyncData_ID(bm.getID());
			vscCreate.setSyncType(syncType);
			vscCreate.setSyncSequence(objSequence);
			vscCreate.setPosID(posID);
			//
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> list = barcodesSyncCacheBO.createObjectEx((getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, vscCreate);
			ec.setErrorCode(barcodesSyncCacheBO.getLastErrorCode());
			ec.setErrorMessage(barcodesSyncCacheBO.getLastErrorMessage());
			if (barcodesSyncCacheBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建DB中Barcode的同步对象失败，错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
				return null;
			} else {
				logger.info("Barcode同步对象的DB表插入的数据为:" + list);
			}

			logger.info("更新后:" + list);

			return list;
		}

		return null;
	}

	/*
	 * Request Body in Fiddler:
	 * string1=234%2C456%2C789%3B1%2C2%2C3%3B1%2C5%2C10%3B1%2C5%2C10%3B0.8%2C4%2C8%
	 * 3B&name=手表&status=0&shortName=薯片&specification=克&packageUnitID=1&
	 * purchasingUnit=箱&providerID=1&brandID=3&categoryID=1&mnemonicCode=SP&
	 * pricingType=1&
	 * isServiceType=1&priceRetail=12&priceVIP=11&priceWholesale=11&ratioGrossMargin
	 * =0&canChangePrice=1&ruleOfPoint=1&picture=116843434834&shelfLife=3&returnDays
	 * =15&purchaseFlag=20&refCommodityID=0&refCommodityMultiple=0&isGift=0&tag=232x
	 * &NO=11&NOAccumulated=1&type=0&nOStart=-1&purchasingPriceStart=-1&
	 * latestPricePurchase=8&pricePurchase=8 type=0 POST
	 * URL:http://localhost:8080/nbr/commoditySync/createEx.bx int1 =
	 * 1，返回创建出的商品对象；否则不返回 int2 = staffID，即操作人的ID
	 * 
	 * 前端传来的参数顺序：条形码，包装单位，商品倍数，零售价，会员价以及批发价，多包装商品名称以X,X,X; X,X,X; X,X,X;的格式传到后端
	 * 
	 */
	// @Transactional
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一个商品的同步缓存，commodity=" + commodity);

		Company company = getCompanyFromSession(req.getSession());

		String ret;

		lock.writeLock().lock();
		Exception e = null;
		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
			commodity.setOperatorStaffID(staff.getID()); // 需要指定修改商品的staff的ID
			m = handleCreateEx(false, commodity, model, req, company.getDbName());
		} catch (Exception ex) {
			ex.printStackTrace();
			e = ex;
			// m = null;
		}
		if (e != null || m == null) {
			// 创建商品成功后，上传商品图片出现错误。应当把创建成功的商品删除.
			if (m != null) {
				Commodity bm = (Commodity) m.get(KEY_Object);
				if (bm != null) {
					logger.error("请运营删除商品ID=" + bm.getID());
				}
			}
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}
		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		logger.info("ret created = " + ret);

		lock.writeLock().unlock();

		// if (e != null) {
		// if (e.getMessage().equals("添加供应商失败！即将回滚DB...")) {
		// throw new RuntimeException("添加供应商失败！即将回滚DB...");
		// } else if (e.getMessage().equals("添加供应商失败！即将回滚DB...")) {
		// throw new RuntimeException("添加供应商失败！即将回滚DB...");
		// }
		// throw new RuntimeException(e.getMessage());
		// }

		return ret;
	}

	/** 将形如D:/nbr/pic/private_db/companyX/tempXXXXXX.jpg的图片变成D:/nbr/pic/private_db/companyX/138.jpg，即重命名临时图片
	 * 做这个操作是因为商品ID已经知道，或修改商品最终成功了，其图片也需要更新。 */
	private void renameCommodityPicture(HttpServletRequest req, String dbName, BaseModel bm, File dest, MultipartFile file, ErrorInfo ec) {
		try {
			// 现在传入的文件是否已经存在，如果有删除该文件
			do {
				File file5 = new File(BaseAction.CommodityPictureDir + dbName + "/" + bm.getID() + ".jpg");
				if (file5.exists()) {
					file5.delete();
					break;
				}
				file5 = new File(BaseAction.CommodityPictureDir + dbName + "/" + bm.getID() + ".jpeg");
				if (file5.exists()) {
					file5.delete();
					break;
				}
				file5 = new File(BaseAction.CommodityPictureDir + dbName + "/" + bm.getID() + ".png");
				if (file5.exists()) {
					file5.delete();
					break;
				}
			} while (false);

			// 拿到新建出来的商品ID,上传商品图片
			logger.info("上传图片成功：" + dest);
			file.transferTo(dest);
			// 截取路径用来判断图片类型
			String s = dest.toString();
			String commodityPictureType = s.substring(s.length() - 4);
			if (ContentType_JPEG.equals(commodityPictureType)) {
				commodityPictureType = "." + commodityPictureType;
			}
			//
			String commodityPicturePath = BaseAction.CommodityPictureDir + dbName + "/" + bm.getID() + commodityPictureType;
			File companyBusinessLicensePictureDestination1 = new File(commodityPicturePath);
			dest.renameTo(companyBusinessLicensePictureDestination1);
			// 上传图片成功删除对应session
			req.getSession().removeAttribute(EnumSession.SESSION_CommodityPictureDestination.getName());
			req.getSession().removeAttribute(EnumSession.SESSION_PictureFILE.getName());
		} catch (Exception e) {
			logger.info("上传图片失败，错误信息：" + e.getMessage());
			ec.setErrorCode(EnumErrorCode.EC_PartSuccess);
			ec.setErrorMessage("上传图片时出现错误！");
		}
	}

	/*
	 * URL:http://localhost:8080/nbr/commoditySync/retrieveNEx.bx
	 * 
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取多个商品的同步缓存，commodity=" + commodity);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			int posID = ((Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName())).getID();
			m = handleRetrieveNEx(posID, commodity, model, req, company.getDbName());
		} catch (Exception e) {
			e.printStackTrace();
			m = null;
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}
		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	/** 1、 POS请求同步块。 <br />
	 * 2、查找同步块缓存，并返回相应的普存对象的列表 */
	protected Map<String, Object> handleRetrieveNEx(int posID, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) {
		logger.info("查找同步块缓存，并返回相应的普存对象的列表");

		Map<String, Object> params = new HashMap<String, Object>();
		ErrorInfo ec = new ErrorInfo();
		ec.setErrorCode(EnumErrorCode.EC_NoError);
		final List<BaseModel> vsInfoList = SyncCacheManager.getCache(dbName, getSyncCacheType()).readN(false, false);
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		for (BaseModel bm : vsInfoList) {
			final BaseSyncCache syncCache = (BaseSyncCache) bm;

			// 没有同步的话，才将数据发回给POS以便它同步
			SyncCache sc = (SyncCache) SyncCacheManager.getCache(dbName, getSyncCacheType());
			if (!sc.checkIfPosSyncThisBlock(syncCache.getID(), posID)) {
				logger.info("POS(ID=" + posID + ")未同步块:" + syncCache + "。正在获取块的信息...");
				BaseModel v = getModel();
				v.setID(syncCache.getSyncData_ID());
				BaseModel bmTmp = null;
				if (!syncCache.getSyncType().equals(SyncCache.SYNC_Type_D)) {
					logger.info("->非D型同步");
					DataSourceContextHolder.setDbName(dbName);
					List<List<BaseModel>> modelR1 = getModelBO().retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, v); // 系统初始化时需要加载所有同存DB
					ec.setErrorCode(getModelBO().getLastErrorCode());
					ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
					if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
						params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage().toString());
						logger.info("返回的普存对象失败，错误码=" + ec.getErrorCode());
						break;
					} else {
						bmTmp = Commodity.fetchCommodityFromResultSet(modelR1);
						logger.info("返回的普存对象成功：" + bmTmp);
						//
						CommodityShopInfo commodityShopInfoRnCondition = new CommodityShopInfo();
						commodityShopInfoRnCondition.setCommodityID(bmTmp.getID());
						DataSourceContextHolder.setDbName(dbName);
						List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodityShopInfoBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityShopInfoRnCondition);
						if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
							logger.error("查询商品门店信息对象失败，错误码=" + commodityShopInfoBO.getLastErrorCode());
							params.put(BaseAction.JSON_ERROR_KEY, commodityShopInfoBO.getLastErrorCode().toString());
							params.put(KEY_HTMLTable_Parameter_msg, commodityShopInfoBO.getLastErrorMessage());
							break;
						}
						bmTmp.setListSlave2(listCommodityShopInfo);					}

					logger.info("->主表数据：" + bmTmp);
					if (retrieveNSlaveDBObjects(bmTmp, dbName, req)) { // 设置bmTmp的从表数据
						ec.setErrorCode(getModelBO().getLastErrorCode());
						ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
						if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
							logger.info("->加载从表数据失败，错误码=" + ec.getErrorCode());
							break;
						} else {
							logger.info("->加载从表数据成功，bmTmp=" + bmTmp);
						}
					}
				} else { // D型同步
					logger.info("->D型同步");
					bmTmp = getModel();
					bmTmp.setID(syncCache.getSyncData_ID());
				}
				if (bmTmp != null) {
					bmTmp.setSyncType(syncCache.getSyncType());
					bmTmp.setSyncSequence(syncCache.getSyncSequence()); // 设置此DB对象的同步次序，以便POS收到该对象后，知道以什么顺序同步这个对象
					bmTmp.setSyncDatetime(new Date());
					bmList.add(bmTmp);
				} else {
					logger.info("->即将同步的对象为null!"); // 不可能事件
				}
			}
		}

		System.out.println("RN出的数据：" + bmList);
		params.put(KEY_ObjectList, bmList);
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());

		logger.info("handleRetrieveNEx()返回的数据=" + params);

		return params;
	}

	/*
	 * Request Body in Fiddler:commodity.ID=1(商品状态为2时不能删除) URL:
	 * http://localhost:8080/nbr/commoditySync/deleteEx.bx?ID=1
	 */
	// @Transactional
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("准备删除一个商品，commodity=" + commodity);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			int iCaseID; // ...根据type判断值
			//
			switch (EnumCommodityType.values()[commodity.getType()]) {
			case ECT_Combination:
				iCaseID = BaseBO.CASE_Commodity_DeleteCombination; // 组合商品
				break;
			case ECT_MultiPackaging:
				iCaseID = BaseBO.CASE_Commodity_DeleteMultiPackaging; // 多包装
				break;
			case ECT_Normal:
				iCaseID = BaseBO.CASE_Commodity_DeleteSimple; // 平常商品
				break;
			case ECT_Service:
				iCaseID = BaseBO.CASE_Commodity_DeleteService; // 服务商品
				break;
			default:
				logger.info("当前commodity的类型为:" + commodity.getType() + ",不存在系统中");
				return null;
			}
			//
			Staff staff = getStaffFromSession(req.getSession());
			commodity.setOperatorStaffID(staff.getID());
			m = handleDeleteEx(false, commodity, model, req, company.getDbName(), iCaseID); // ...由于SP_Commodity_Delete内，有可能删除了条形码，所以条形码的同存和缓存都要更新。找Giggs
		} catch (Exception e) {
			e.printStackTrace();
			m = null;
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void deleteRelatedObjectsAfterDeleteEx(int iPosSize, BaseModel baseModel, ErrorInfo ec, ModelMap model, HttpServletRequest req, String dbName) {
		logger.info("调用handleDeleteEx()后，同步更新对应的条形码缓存和同存，baseModel=" + baseModel);

		// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此块
		iPosSize = getPosSize(false, ec, dbName, req);
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("获取POS的数目失败，错误码=" + ec.getErrorCode());
			return;
		}

		int iCurrentPosID = getLoginPOSID(req);

		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> list = (List<BaseModel>) barcodesSyncCacheBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, baseModel);
		logger.info("读取多个缓存同步调度表，错误码=" + barcodesSyncCacheBO.getLastErrorCode());

		if (iPosSize > 1) {
			// 标记本POS已经同步了此块
			if (iCurrentPosID != BaseAction.INVALID_POS_ID) { // 网页端无POS会话，无有效的POS的ID
				BaseSyncCacheDispatcher bscd = new BaseSyncCacheDispatcher();
				bscd.setPos_ID(iCurrentPosID);
				bscd.setSyncCacheID(list.get(0).getID());
				DataSourceContextHolder.setDbName(dbName);
				barcodesSyncCacheDispatcherBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, bscd);
				ec.setErrorCode(barcodesSyncCacheDispatcherBO.getLastErrorCode());
				ec.setErrorMessage(barcodesSyncCacheDispatcherBO.getLastErrorMessage());
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("创建缓存的同步调度表失败，错误码=" + ec.getErrorCode());
					return;
				}
			}
		}
		// 重新加载所有条形码到缓存中
		barcodesCache.load(dbName);
		// 重新加载所有条形码的同步块缓存
		barcodesSyncCache.load(dbName);
	}

	/*
	 * 4、POS吸收同步块后告诉服务器自己是否成功同步了一个块 5、更新DB（其中可能会删除这个块，因为所有POS机都已经同步了这个块）。更新普通对象的缓存
	 * 6、更新同步块缓存（和上一步一样，可能会删除这个块，因为所有POS机都已经同步了这个块）
	 * 
	 * URL:http://localhost:8080/nbr/commoditySync/feedbackEx.bx?sID=1 ,2&errorCode=
	 * EC_NoError
	 */
	@RequestMapping(value = "/feedbackEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String feedbackEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		logger.info("POS机告知服务器已经成功的同步了数据，commodity=" + commodity);
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> param = null;
		try {
			param = handleFeedbackEx(false, commodity, model, req, company.getDbName());
		} catch (Exception e) {
			e.printStackTrace();
			param = null;
		}
		//
		if (param == null) {
			param = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + param.toString());
		}

		lock.writeLock().unlock();

		ret = JSONObject.fromObject(param).toString();

		return ret;
	}

	/*
	 * Request Body in Fiddler:ID=1&name=手表&status=0& shortName=薯片
	 * &specification=克&packageUnit=盒&purchasingUnit=箱 &providerID=1&brandID=3
	 * &categoryID=1&mnemonicCode=SP&pricingType=1&isServiceType=1
	 * &priceVIP=11.8&priceWholesale=11&RatioGrossMargin=0.05
	 * &canChangePrice=1&ruleOfPoint=1 &picture=url=116843434834&shelfLife=3&
	 * returnDays=15&purchaseFlag=20.1&refCommodityID=0
	 * &refCommodityMultiple=0&isGift=0&Tag=.......... ...x&NO=11&providerIDs=1,2
	 * 
	 * URL:http://localhost:8080/nbr/commoditySync/updateEx.bx int1 =
	 * 1，返回创建出的商品对象；否则不返回 int2 = staffID，即操作人的ID
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改一个商品的同步缓存，commodity=" + commodity);

		String ret;

		lock.writeLock().lock();

		Company company = getCompanyFromSession(req.getSession());

		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());// 避免用户传入StaffID即可进行修改的情况，需要从会话中获取登录的StaffID
			commodity.setOperatorStaffID(staff.getID()); // 需要指定修改商品的staff的ID

			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				m = handleUpdateEx(false, BaseBO.INVALID_CASE_ID, commodity, model, req, company.getDbName());
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				m = handleUpdateEx(false, BaseBO.CASE_UpdateCommodityOfService, commodity, model, req, company.getDbName());
			} else {
				logger.info("不支持的商品类型");
				lock.writeLock().unlock();

				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			m = null;
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
			m.put(KEY_HTMLTable_Parameter_msg, "商品部分信息修改失败。请重新修改！");
		} else {
			logger.info("返回的数据=" + m.toString());
		}

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		// if (e != null) {
		// throw new RuntimeException(e.getMessage());
		// }
		return ret;
	}

	/** 上传图片到D:/nbr/pic/private_db/companyX/tempXXXXXX.jpg(png/jpeg)，临时保存之。
	 * 此时，还未确定图片的名字，所以命名带有temp字眼。这个临时文件的路径和MultipartFile对象被保存在会话中，给后面的操作使用
	 * 之后，用户可能创建、修改、删除商品，这个临时文件将会被处理，比如重命名为138.jpg，对应商品F_ID=138 */
	@RequestMapping(value = "/uploadPictureEx", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadPictureEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req, @RequestParam("file") MultipartFile file) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("商品上传图片，commodity=" + commodity.toString());

		Company company = getCompanyFromSession(req.getSession());
		Map<String, Object> map = new HashMap<>();

		// 清空会话，确保session不受污染
		req.getSession().removeAttribute(EnumSession.SESSION_CommodityPictureDestination.getName());
		req.getSession().removeAttribute(EnumSession.SESSION_PictureFILE.getName());

		if (!file.isEmpty()) {
			do {
				String fileName = file.getOriginalFilename();
				String picType = "image/" + fileName.substring(fileName.lastIndexOf(".") + 1);
				if (!picType.equals(JPEGType) && !picType.equals(PNGType) && !picType.equals(JPGType)) {
					logger.error("商品图片的类型不对。当前类型为：" + picType);

					map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					map.put(KEY_HTMLTable_Parameter_msg, "商品图片的类型必须是.jpg/.jpeg/.png格式");
					break;
				}

				Long size = file.getSize();
				ErrorInfo ecOut = new ErrorInfo();
				ConfigGeneral commodityLogoVolumeMax = (ConfigGeneral) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.CommodityLogoVolumeMax, getStaffFromSession(req.getSession()).getID(), ecOut,
						company.getDbName());
				if (size <= 0 || size > Long.parseLong(commodityLogoVolumeMax.getValue())) {
					logger.error("商品图片大小不能超过100K。当前size=" + size);

					map.put(KEY_HTMLTable_Parameter_msg, "商品图片大小不能超过100K");
					map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					break;
				}
				String path = BaseAction.CommodityPictureDir;
				logger.info("商品图片上传路径：" + path);
				// 进行磁盘和路径检查
				if (!GeneralUtil.checkDiskSpaceAndCreateFolder(map, path + "/" + company.getDbName())) {
					break;
				}
				// 判断是jpg还是png类型，上传名称。如果多个线程在这里写，会存在隐患：出现相同的临时图片文件，不同的商品引用相同的图片
				String pictureName = "";
				if (picType.contains(JPGType)) {
					pictureName = "temp" + System.currentTimeMillis() % 1000000 + ".jpg";
				} else if (picType.contains(PNGType)) {
					pictureName = "temp" + System.currentTimeMillis() % 1000000 + ".png";
				} else {
					pictureName = "temp" + System.currentTimeMillis() % 1000000 + ".jpeg";
				}
				//
				logger.info("商品图片名称：" + pictureName);

				File commodityPictureDestination = new File(path + "/" + company.getDbName() + "/" + pictureName);

				BxConfigGeneral CommodityLogoDir = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
						.read1(BaseCache.CommodityLogoDir, getStaffFromSession(req.getSession()).getID(), ecOut, BaseAction.DBName_Public);
				if (!path.equals(CommodityLogoDir.getValue())) {
					logger.info("不存在此目录，path=" + path);

					map.put(KEY_HTMLTable_Parameter_msg, "不存在此目录，path=" + path);
					map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					break;
				}

				map.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				map.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
				req.getSession().setAttribute(EnumSession.SESSION_CommodityPictureDestination.getName(), commodityPictureDestination);
				req.getSession().setAttribute(EnumSession.SESSION_PictureFILE.getName(), file);
			} while (false);
			logger.info("返回的值，map=" + map);

			return map;
		} else {
			return map;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteCommodityPictureEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	public void deleteCommodityPictureEx(HttpSession session, Commodity commodity) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		logger.info("清空商品图片的session");
		session.removeAttribute(EnumSession.SESSION_CommodityPictureDestination.getName());
		session.removeAttribute(EnumSession.SESSION_PictureFILE.getName());
	}
	
	private void createCommodityShopInfo(Commodity bmFromDB, String dbName, Commodity comm, int staffID, int iUseCaseID, Warehousing warehousing) {
		CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
		commodityShopInfo.setCommodityID(bmFromDB.getID());
		List<BaseModel> shopList = CacheManager.getCache(dbName, EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
		if(shopList == null || shopList.size() == 0) {
			logger.error("查找门店信息失败");
		}
		List<CommodityShopInfo> commodityShopInfoList = new ArrayList<>();
		for(BaseModel bm : shopList) {
			Shop shop = (Shop) bm;
			if(shop.getID() != 1) {
				commodityShopInfo.setShopID(shop.getID());
				commodityShopInfo.setLatestPricePurchase(comm.getLatestPricePurchase());
				commodityShopInfo.setPriceRetail(comm.getPriceRetail());
				commodityShopInfo.setnOStart(comm.getnOStart());
				commodityShopInfo.setPurchasingPriceStart(comm.getPurchasingPriceStart());
				commodityShopInfo.setOperatorStaffID(staffID);
				if(comm.getnOStart() > 0 && comm.getPurchasingPriceStart() > 0) {
					commodityShopInfo.setCurrentWarehousingID(warehousing.getID());
				}
				DataSourceContextHolder.setDbName(dbName);
				CommodityShopInfo commodityShopInfoCreated = (CommodityShopInfo) commodityShopInfoBO.createObject(staffID, iUseCaseID, commodityShopInfo);
				if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError || commodityShopInfoCreated == null) {
					logger.error("创建商品门店信息失败，错误码=" + commodityShopInfoBO.printErrorInfo());
				} else {
					logger.info("创建商品门店信息成功，bmFromDB=" + bmFromDB.toString());
				}
				commodityShopInfoList.add(commodityShopInfoCreated);
			}
		}
		bmFromDB.setListSlave2(commodityShopInfoList);
	}
}
