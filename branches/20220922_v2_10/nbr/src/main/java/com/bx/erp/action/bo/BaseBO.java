package com.bx.erp.action.bo;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.BaseAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public abstract class BaseBO {
	private Log logger = LogFactory.getLog(BaseBO.class);

	/** 有些DB表里默认的数据行不可写，这些数据行的F_ID=1。比如默认供应商，默认品牌 */
	public static final int DEFAULT_DB_Row_ID = 1;

	public static final int INVALID_CASE_ID = -100000001;
	//
	public static final int CASE_Normal = 10086;
	public static final int CASE_ResetMyPassword = CASE_Normal + 1;
	public static final int CASE_GetPassword = CASE_Normal + 2;
	public static final int CASE_Login = CASE_Normal + 3;
	public static final int CASE_RetrieveSuggestion = CASE_Normal + 4;
	// public static final int CASE_Commodity_RetrieveNByNFields = CASE_Normal + 5;
	/** 更新商品的最近采购价、零售价 */
	public static final int CASE_Commodity_UpdatePrice = CASE_Normal + 6;
	public static final int CASE_Commodity_CreateMultiPackaging = CASE_Normal + 7;
	// public static final int CASE_SmallSheetSyncCache_DeleteSameCache =
	// CASE_Normal + 8;
	public static final int CASE_RetrieveNMultiPackageCommodity = CASE_Normal + 9;
	public static final int CASE_CheckDeleteDependency = CASE_Normal + 10;
	public static final int CASE_UpdateStartValue = CASE_Normal + 11;
	/** Currently Only For Vip Sync Cache 使用。请勿滥用！ */
	public static final int CASE_X_DeleteAllVipSyncCache = CASE_Normal + 12;
	/** Currently Only For SmallSheet Sync Cache 使用。请勿滥用！ */
	public static final int CASE_X_DeleteAllSmallSheetSyncCache = CASE_Normal + 13;
	public static final int CASE_UpdateCommodityOfMultiPackaging = CASE_Normal + 15;
	/** 获取仓库的库存总额和最大总额 */
	public static final int CASE_Warehouse_RetrieveInventory = CASE_Normal + 16;
	public static final int CASE_UpdatePurchasingUnit = CASE_Normal + 17;
	public static final int CASE_UpdateRoleName = CASE_Normal + 18;
	public static final int CASE_ApprovePurchasingOrder = CASE_Normal + 19;
	public static final int CASE_UpdatePurchasingOrderStatus = CASE_Normal + 20;
	public static final int CASE_ApproveInventorySheet = CASE_Normal + 21;
	public static final int CASE_SubmitInventorySheet = CASE_Normal + 22;
	public static final int CASE_RetrieveNWarhousingByOrderID = CASE_Normal + 23;
	public static final int CASE_ApproveWarhousing = CASE_Normal + 24;
	public static final int CASE_ApproveReturnCommoditySheet = CASE_Normal + 25;
	/** Currently Only For Commodity Sync Cache 使用。请勿滥用！ */
	public static final int CASE_X_DeleteAllCommoditySyncCache = CASE_Normal + 27;
	/** Currently Only For VipCategory Sync Cache 使用。请勿滥用！ */
	public static final int CASE_X_DeleteAllVipCategorySyncCache = CASE_Normal + 28;
	/** Currently Only For CommodityAction pictureUpload 使用。请勿滥用！ */
	public static final int CASE_UpdateCommodityPicture = CASE_Normal + 29;
	public static final int CASE_PurchasingOrderCommodityRetrieveNWarhousing = CASE_Normal + 30;
	public static final int CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing = CASE_Normal + 31;
	/** 获取商品的库存 */
	public static final int CASE_Commodity_RetrieveInventory = CASE_Normal + 32;
	public static final int CASE_X_DeleteAllBrandSyncCache = CASE_Normal + 33;
	public static final int CASE_RetrieveNWarhousingByFields = CASE_Normal + 34;
	public static final int CASE_X_DeleteAllBarcodesSyncCache = CASE_Normal + 35;
	public static final int CASE_DeleteBarcodesByCommodityID = CASE_Normal + 36;
	public static final int CASE_X_DeleteAllCategorySyncCache = CASE_Normal + 37;
//	public static final int CASE_X_DeleteAllConfigGeneralSyncCache = CASE_Normal + 38;
	public static final int CASE_Commodity_DeleteSimple = CASE_Normal + 39;
	public static final int CASE_UpdateInventoryCommodityNoReal = CASE_Normal + 40;
	public static final int CASE_Commodity_DeleteMultiPackaging = CASE_Normal + 41;
	public static final int CASE_Staff_Update_OpenidAndUnionid = CASE_Normal + 42; // 更新staff中的open跟unionid字段
	public static final int CASE_Pos_Retrieve1BySN = CASE_Normal + 43;
	public static final int CASE_Vip_RetrieveNVipConsumeHistory = CASE_Normal + 44;
	public static final int CASE_X_DeleteAllPromotionSyncCache = CASE_Normal + 45;
	public static final int CASE_Category_RetrieveNByParent = CASE_Normal + 46; // 根据大类查询小类信息
	public static final int CASE_RetailTradeDailyReportSummary_RetrieveNForChart = CASE_Normal + 47; // 查询一段时间内的销售总额、销售毛利和日期
	public static final int CASE_Message_RetrieveNForWx = CASE_Normal + 48; // 根据companyID和status查询Message
	public static final int CASE_X_DeleteAllRetailTradePromotingSyncCache = CASE_Normal + 49;
	public static final int CASE_Message_UpdateStatus = CASE_Normal + 50; // 更新消息中stuas的字段为1，即已发送
	public static final int CASE_Role_RetrieveAlsoStaff = CASE_Normal + 51;
	public static final int CASE_Permission_RetrieveAlsoRoleStaff = CASE_Normal + 52;
	public static final int CASE_Vip_RetrieveNByMobileOrVipCardSN = CASE_Normal + 53;
	public static final int CASE_Provider_RetrieveNByFields = CASE_Normal + 54;
	public static final int CASE_PurchasingOrder_RetrieveNByNFields = CASE_Normal + 55;
	public static final int CASE_InventorySheet_RetrieveNByNFields = CASE_Normal + 56;
	public static final int CASE_POS_Reset = CASE_Normal + 57;
	public static final int CASE_Commodity_DeleteCombination = CASE_Normal + 58;
	public static final int CASE_Commodity_CreateSingle = CASE_Normal + 59;
	public static final int CASE_Commodity_CreateComposition = CASE_Normal + 60;
	public static final int CASE_CheckUniqueField = CASE_Normal + 61;
	public static final int CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount = CASE_Normal + 62;
	public static final int CASE_POS_RecycleApp = CASE_Normal + 63;
	/** Currently Only For pOS Sync Cache 使用。请勿滥用！ */
	public static final int CASE_X_DeleteAllPosSyncCache = CASE_Normal + 64;
	public static final int CASE_ResetOtherPassword = CASE_Normal + 65;
	/** 针对测试的特别的结果验证使用的CASE。绝对不能在功能代码中使用本CASE ID！
	 * 部分对象create出来后无法通过BaseModel.checkCreate(INVALID_CASE_ID)的结果验证，这时需要实现本CASE的结果验证 */
	public static final int CASE_SpecialResultVerification = CASE_Normal + 66;
	public static final int CASE_Commodity_CreateService = CASE_Normal + 67;
	public static final int CASE_Commodity_DeleteService = CASE_Normal + 68;
	public static final int CASE_UpdateCommodityOfService = CASE_Normal + 69;
	public static final int CASE_Company_UpdateSubmchid = CASE_Normal + 70;
	public static final int CASE_UnsalableCommodity_RetrieveN = CASE_Normal + 71;
	public static final int CASE_RetailTrade_RetrieveNFromApp = CASE_Normal + 72;
	public static final int CASE_RetailTrade_RetrieveNBySNFromWeb = CASE_Normal + 73;
	public static final int CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb = CASE_Normal + 74;
	/** 当一个ACTION需要创建主表和从表时，为了避免出现有头无尾或有头烂尾的情况，在ACTION的开头先做从表的MODEL的checkCreate()检查。
	 * <br />
	 * 有头无尾：主表创建成功，从表没有一行创建成功。 <br />
	 * 有头烂尾:主表创建成功，从表部分创建成功，部分创建失败。 */
	public static final int CASE_CheckCreateForAction = CASE_Normal + 75;
	public static final int CASE_Staff_Update_Unsubscribe = CASE_Normal + 76;
	public static final int CASE_CouponCode_Consume = CASE_Normal + 77;

	public static final int CASE_WxSubMch_UpdateStatus = CASE_Normal + 73;
	
	public static final int CASE_Vip_UpdateBonus = CASE_Normal + 76;
	public static final int CASE_Vip_ResetBonus = CASE_Normal + 77;
	public static final int CASE_WxVip_UpdateBonus = CASE_Normal + 78;
	public static final int CASE_WxVip_ResetBonus = CASE_Normal + 79;
//	public static final int CASE_Vip_RetrieveNByMobileOrCardCode = CASE_Normal + 80;
	public static final int CASE_Company_retrieveNByVipMobile = CASE_Normal + 81;
	public static final int CASE_Company_updateVipSystemTip = CASE_Normal + 82;
	
	public static final int CASE_CouponCode_retrieveNTotalByVipID = CASE_Normal + 83;
	public static final int CASE_CouponCode_retrieveNByVipID = CASE_Normal + 84;
	public static final int CASE_Company_matchVip = CASE_Normal + 85;
	public static final int CASE_Vip_ImportFromOldSystem = CASE_Normal + 86;
	public static final int CASE_VipCardCode_ImportFromOldSystem = CASE_Normal + 87;
	public static final int CASE_Shop_RetrieveNByFields = CASE_Normal + 88;
	
	
	// public static Pos CURRENT_POS = null;
	/** 代表系统。在一些操作当中，需要传递此常量，代表是系统进行的操作或一些不需要考虑用户权限的操作 */
	public static final int SYSTEM = -100000000;
	/** 符合条件的搜索结果的总条数 */
	protected int totalRecord;
	/** 符合条件的搜索结果的总页数 */
	protected int pageCount;

	/** 当Mysql断开后，会进行重连。花RETRY_TIME_GAP毫秒等待Mysql就绪 */
	protected int MYSQL_ReconnectTimeGap = 2000;

	/** 多个BO实例共用的mapper。通过mapper访问DB */
	protected BaseMapper mapper;

	/** 每个BO在CRUD时，其mapper为null。必须设置一个有效的mapper。这个mapper在子类做Component-scan时（项目启动时），会通过@Resource实例化一个 */
	protected void setMapper() {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 在运行时，通过本方法，给各BO提供一个有效的mapper */
	protected void checkMapper() {
		if (mapper == null) {
			setMapper();
		}
	}

	public BaseBO() {
		lastErrorCode = EnumErrorCode.EC_NoError;
		lastErrorMessage = "";
		pageSize = BaseAction.PAGE_SIZE;
		mapper = null;
	}

	public int getPageCount() {
		return pageCount;
	}

	protected int pageSize;

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	protected void setTotalRecord(int iTotalRecord) {
		this.totalRecord = iTotalRecord;
		pageCount = iTotalRecord / pageSize + (iTotalRecord % pageSize == 0 ? 0 : 1);
	}

	protected EnumErrorCode lastErrorCode;
	protected String lastErrorMessage;

	public String getLastErrorMessage() {
		return lastErrorMessage;
	}

	public EnumErrorCode getLastErrorCode() {
		return lastErrorCode;
	}

	protected boolean preCheckCreate(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkUpdateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkDeleteExPermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 有时会直接从缓存中读取一个对象。读取前需要执行权限检查，所以本函数是public */
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected boolean preCheckCreateEx(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean doCheckCreate(int iUseCaseID, BaseModel s) {
		String err = s.checkCreate(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			logger.debug("本对象为：" + s);
			return false;
		}
		return true;
	}

	protected boolean doCheckUpdate(int iUseCaseID, BaseModel s) {
		String err = s.checkUpdate(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			return false;
		}
		return true;
	}

	/** Model字段数据验证 */
	protected boolean checkCreate(int iUseCaseID, BaseModel s) {
		return doCheckCreate(iUseCaseID, s);
	}

	protected boolean checkCreateEx(int iUseCaseID, BaseModel s) {
		return doCheckCreate(iUseCaseID, s);
	}

	protected BaseModel doCreate(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getCreateParam(iUseCaseID, s);
		BaseModel bm = mapper.create(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
		return bm;
	}

	protected List<List<BaseModel>> doCreateEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getCreateParamEx(iUseCaseID, s);
		List<List<BaseModel>> lsls = mapper.createEx(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return lsls;
	}

	@SuppressWarnings("static-access")
	protected BaseModel create(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkCreatePermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckCreate(iUseCaseID, s) || !setDefaultValueToCreate(iUseCaseID, s) || !checkCreate(iUseCaseID, s)) {
			return null;
		}

		BaseModel ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doCreate(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);

					ls = doCreate(iUseCaseID, s);
					logger.debug("doCreate(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	/** 前端传递过来的MODEL中，部分字段不能采用前端的值，必须由后端指定 */
	protected boolean setDefaultValueToCreate(int iUseCaseID, BaseModel s) {
		return s.setDefaultValueToCreate(iUseCaseID);
	}

	@SuppressWarnings("static-access")
	protected List<List<BaseModel>> createEx(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkCreateExPermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckCreateEx(iUseCaseID, s) || !setDefaultValueToCreate(iUseCaseID, s) || !checkCreateEx(iUseCaseID, s)) {
			return null;
		}

		List<List<BaseModel>> ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doCreateEx(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doCreateEx(iUseCaseID, s);
					logger.debug("doCreateEx(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage());
				return null;
			}
		}
		return ls;
	}

	protected boolean preCheckDelete(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean preCheckDeleteEx(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean doCheckDelete(int iUseCaseID, BaseModel s) {
		String err = s.checkDelete(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			return false;
		}
		return true;
	}

	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		return doCheckDelete(iUseCaseID, s);
	}

	protected boolean checkDeleteEx(int iUseCaseID, BaseModel s) {
		return doCheckDelete(iUseCaseID, s);
	}

	protected BaseModel doDelete(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getDeleteParam(iUseCaseID, s);
		System.out.println(DataSourceContextHolder.getDbName()); // ... ???
		BaseModel bm = mapper.delete(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return bm;
	}

	protected List<List<BaseModel>> doDeleteEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getDeleteParamEx(iUseCaseID, s);
		List<List<BaseModel>> lsls = mapper.deleteEx(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return lsls;
	}

	@SuppressWarnings("static-access")
	protected BaseModel delete(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkDeletePermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckDelete(iUseCaseID, s) || !checkDelete(iUseCaseID, s)) {
			return null;
		}

		BaseModel ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doDelete(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doDelete(iUseCaseID, s);
					logger.debug("doDelete(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	@SuppressWarnings("static-access")
	protected List<List<BaseModel>> deleteEx(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();

		if (!checkDeleteExPermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckDeleteEx(iUseCaseID, s) || !checkDeleteEx(iUseCaseID, s)) {
			return null;
		}

		List<List<BaseModel>> ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doDeleteEx(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doDeleteEx(iUseCaseID, s);
					logger.debug("doDeleteEx(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	protected boolean preCheckUpdate(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean preCheckUpdateEx(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		return doCheckUpdate(iUseCaseID, s);
	}

	protected boolean checkUpdateEx(int iUseCaseID, BaseModel s) {
		return doCheckUpdate(iUseCaseID, s);
	}

	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
		BaseModel bm = mapper.update(params);
		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return bm;
	}

	protected List<List<BaseModel>> doUpdateEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getUpdateParamEx(iUseCaseID, s);
		List<List<BaseModel>> lsls = mapper.updateEx(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return lsls;
	}

	@SuppressWarnings("static-access")
	protected BaseModel update(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();

		if (!checkUpdatePermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}

		if (!preCheckUpdate(iUseCaseID, s) || !checkUpdate(iUseCaseID, s)) {
			return null;
		}

		BaseModel ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doUpdate(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					ls = doUpdate(iUseCaseID, s);
					logger.debug("doUpdate(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	@SuppressWarnings("static-access")
	protected List<List<BaseModel>> updateEx(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkUpdateExPermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckUpdateEx(iUseCaseID, s) || !checkUpdateEx(iUseCaseID, s)) {
			return null;
		}

		List<List<BaseModel>> ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doUpdateEx(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doUpdateEx(iUseCaseID, s);
					logger.debug("doUpdateEx(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	protected boolean preCheckRetrieve1(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean preCheckRetrieve1Ex(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean doCheckRetrieve1(int iUseCaseID, BaseModel s) {
		String err = s.checkRetrieve1(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			return false;
		}
		return true;
	}

	protected boolean checkRetrieve1(int iUseCaseID, BaseModel s) {
		return doCheckRetrieve1(iUseCaseID, s);
	}

	protected boolean checkRetrieve1Ex(int iUseCaseID, BaseModel s) {
		return doCheckRetrieve1(iUseCaseID, s);
	}

	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieve1Param(iUseCaseID, s);
		BaseModel bm = mapper.retrieve1(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return bm;
	}

	protected List<List<BaseModel>> doRetrieve1Ex(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieve1ParamEx(iUseCaseID, s);
		List<List<BaseModel>> lsls = mapper.retrieve1Ex(params);

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return lsls;
	}

	@SuppressWarnings("static-access")
	protected BaseModel retrieve1(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkRetrieve1Permission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckRetrieve1(iUseCaseID, s) || !checkRetrieve1(iUseCaseID, s)) {
			return null;
		}

		BaseModel ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doRetrieve1(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doRetrieve1(iUseCaseID, s);
					logger.debug("doRetrieve1(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	@SuppressWarnings("static-access")
	protected List<List<BaseModel>> retrieve1Ex(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkRetrieve1ExPermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckRetrieve1Ex(iUseCaseID, s) || !checkRetrieve1Ex(iUseCaseID, s)) {
			return null;
		}

		List<List<BaseModel>> ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doRetrieve1Ex(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doRetrieve1Ex(iUseCaseID, s);
					logger.debug("doRetrieve1Ex(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	protected boolean preCheckRetrieveN(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean preCheckRetrieveNEx(int iUseCaseID, BaseModel s) {
		return (s != null);
	}

	protected boolean doCheckRetrieveN(int iUseCaseID, BaseModel s) {
		if (!s.checkPageSize(s)) {
			lastErrorCode = EnumErrorCode.EC_Hack;
			lastErrorMessage = BaseModel.FIELD_ERROR_pageSize;
			return false;
		}
		String err = s.checkRetrieveN(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			return false;
		}
		return true;
	}

	protected boolean doCheckRetrieveNEx(int iUseCaseID, BaseModel s) {
		if (!s.checkPageSize(s)) {
			lastErrorCode = EnumErrorCode.EC_Hack;
			lastErrorMessage = BaseModel.FIELD_ERROR_pageSize;
			return false;
		}
		String err = s.checkRetrieveNEx(iUseCaseID);
		if (err.length() > 0) {
			lastErrorCode = EnumErrorCode.EC_WrongFormatForInputField;
			lastErrorMessage = err;
			return false;
		}
		return true;
	}

	protected boolean checkRetrieveN(int iUseCaseID, BaseModel s) {
		return doCheckRetrieveN(iUseCaseID, s);
	}

	protected boolean checkRetrieveNEx(int iUseCaseID, BaseModel s) {
		return doCheckRetrieveNEx(iUseCaseID, s);
	}

	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
		List<BaseModel> ls = mapper.retrieveN(params);

		if (params.get(BaseAction.SP_OUT_PARAM_iTotalRecord) != null) {
			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
		} else {
			setTotalRecord(0);
		}

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
		return ls;
	}

	protected List<List<BaseModel>> doRetrieveNEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParamEx(iUseCaseID, s);
		List<List<BaseModel>> ls = mapper.retrieveNEx(params);

		setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return ls;
	}

	@SuppressWarnings("static-access")
	protected List<?> retrieveN(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkRetrieveNPermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckRetrieveN(iUseCaseID, s) || !checkRetrieveN(iUseCaseID, s)) {
			return null;
		}

		List<?> ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doRetrieveN(iUseCaseID, s);
		} catch (Exception e) {
			logger.debug("retrieveN(int staffID, int iUseCaseID, BaseModel s)出错2：" + e.getMessage());
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doRetrieveN(iUseCaseID, s);
					logger.debug("doRetrieveN(iUseCaseID, s); OK");
				} catch (Exception e2) {
					logger.debug("retrieveN(int staffID, int iUseCaseID, BaseModel s)出错2：" + e2.getMessage());
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	@SuppressWarnings("static-access")
	protected List<List<BaseModel>> retrieveNEx(int staffID, int iUseCaseID, BaseModel s) {
		checkMapper();
		if (!checkRetrieveNExPermission(staffID, iUseCaseID, s)) {
			lastErrorCode = EnumErrorCode.EC_NoPermission;
			lastErrorMessage = "权限不足";
			return null;
		}
		if (!preCheckRetrieveNEx(iUseCaseID, s) || !checkRetrieveNEx(iUseCaseID, s)) {
			return null;
		}

		List<List<BaseModel>> ls = null;
		try {
			lastErrorCode = EnumErrorCode.EC_OtherError;
			lastErrorMessage = "";
			ls = doRetrieveNEx(iUseCaseID, s);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLNonTransientConnectionException && e.getCause() != null && e.getCause().getMessage().equals("Connection is closed.")
					&& ((SQLNonTransientConnectionException) e.getCause()).getSQLState().equals("08003")) {
				try {
					Thread.currentThread().sleep(MYSQL_ReconnectTimeGap);
					// ...
					ls = doRetrieveNEx(iUseCaseID, s);
					logger.debug("doRetrieveNEx(iUseCaseID, s); OK");
				} catch (Exception e2) {
					lastErrorCode = EnumErrorCode.EC_OtherError;
					lastErrorMessage = e2.getMessage();
					return null;
				}
			} else {
				lastErrorCode = EnumErrorCode.EC_OtherError;
				lastErrorMessage = e.getMessage();
				logger.debug("e.getCause()=" + e.getCause().getMessage() + "\t((SQLNonTransientConnectionException) e.getCause()).getSQLState()=" + ((SQLNonTransientConnectionException) e.getCause()).getSQLState());
				return null;
			}
		}
		return ls;
	}

	public BaseModel createObject(int staffID, int iUseCaseID, BaseModel c) {
		return create(staffID, iUseCaseID, c);
	}

	public List<List<BaseModel>> createObjectEx(int staffID, int iUseCaseID, BaseModel c) {
		return createEx(staffID, iUseCaseID, c);
	}

	public BaseModel updateObject(int staffID, int iUseCaseID, BaseModel c) {
		return update(staffID, iUseCaseID, c);
	}

	public List<List<BaseModel>> updateObjectEx(int staffID, int iUseCaseID, BaseModel c) {
		return updateEx(staffID, iUseCaseID, c);
	}

	public BaseModel retrieve1Object(int staffID, int iUseCaseID, BaseModel c) {
		return retrieve1(staffID, iUseCaseID, c);
	}

	public List<List<BaseModel>> retrieve1ObjectEx(int staffID, int iUseCaseID, BaseModel c) {
		return retrieve1Ex(staffID, iUseCaseID, c);
	}

	public List<?> retrieveNObject(int staffID, int iUseCaseID, BaseModel c) {
		return retrieveN(staffID, iUseCaseID, c);
	}

	public List<List<BaseModel>> retrieveNObjectEx(int staffID, int iUseCaseID, BaseModel c) {
		return retrieveNEx(staffID, iUseCaseID, c);
	}

	public BaseModel deleteObject(int staffID, int iUseCaseID, BaseModel c) {
		return delete(staffID, iUseCaseID, c);
	}

	public List<List<BaseModel>> deleteObjectEx(int staffID, int iUseCaseID, BaseModel c) {
		return deleteEx(staffID, iUseCaseID, c);
	}

	/** 检查1个staff有无权限permission */
	protected boolean checkStaffPermission(int staffID, String permission) {
		if (staffID == SYSTEM) {
			return true;
		}

		StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(DataSourceContextHolder.getDbName(), EnumCacheType.ECT_StaffPermission);
		ErrorInfo ecOut = new ErrorInfo();
		StaffPermission sp = spc.read1(staffID, permission, ecOut);
		if (sp == null) {
			lastErrorMessage = "Staff(" + staffID + ")没有权限(" + permission + ")";
			logger.debug(lastErrorMessage);
			return false;
		}
		return true;
	}

	public String printErrorInfo() {
		return "错误码=" + lastErrorCode + "\t错误信息=" + lastErrorMessage;
	}

	public String printErrorInfo(String dbName, Object param) {
		return "错误码=" + lastErrorCode + "\t错误信息=" + lastErrorMessage + "\t参数(DB:" + dbName + ")：" + param;
	}
}
