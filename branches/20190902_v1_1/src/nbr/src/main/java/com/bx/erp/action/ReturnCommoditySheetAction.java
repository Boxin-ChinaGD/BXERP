package com.bx.erp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.ReturnCommoditySheetBO;
import com.bx.erp.action.bo.ReturnCommoditySheetCommodityBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/returnCommoditySheet")
@Scope("prototype")
public class ReturnCommoditySheetAction extends BaseAction {
	private Log logger = LogFactory.getLog(ReturnCommoditySheetAction.class);

	@Resource
	private ReturnCommoditySheetBO returnCommoditySheetBO;

	@Resource
	private ReturnCommoditySheetCommodityBO returnCommoditySheetCommodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private PackageUnitBO packageUnitBO;
	
	@Resource
	private ShopBO shopBO;

	/** 1、更新退货单 <br />
	 * 2、用会话中的从表信息更新退货单的从表。更新前先删除退货单的从表信息 */
	private ReturnCommoditySheet doUpdate(ReturnCommoditySheet returnCommoditySheet, List<ReturnCommoditySheetCommodity> listRCSCSession, Map<String, Object> params, String dbName, HttpServletRequest req) {
		logger.info("修改对应的退货单并更新退货单商品，returnCommoditySheet=" + returnCommoditySheet);
		if (!checkCreate(req, dbName, params, listRCSCSession)) {
			return null;
		}
		//
		DataSourceContextHolder.setDbName(dbName);
		returnCommoditySheet = (ReturnCommoditySheet) returnCommoditySheetBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, returnCommoditySheet);
		if (returnCommoditySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("修改退货单失败，错误信息=" + returnCommoditySheetBO.printErrorInfo());
			params.put(JSON_ERROR_KEY, returnCommoditySheetBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, returnCommoditySheetBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("修改退货单成功,rcsUpdate=" + returnCommoditySheet);
		}

		// 先删除退货单原先的商品，再添加新的商品
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(returnCommoditySheet.getID());
		DataSourceContextHolder.setDbName(dbName);
		returnCommoditySheetCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, rcsc);
		if (returnCommoditySheetCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("删除退货单商品失败，错误信息=" + returnCommoditySheetBO.printErrorInfo());
			params.put(JSON_ERROR_KEY, returnCommoditySheetBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, returnCommoditySheetBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("删除退货单商品成功");
		}

		// 创建退货单商品list
		List<ReturnCommoditySheetCommodity> listRcsc = new ArrayList<ReturnCommoditySheetCommodity>();
		boolean hasError = false;
		for (int i = 0; i < listRCSCSession.size(); i++) {
			ReturnCommoditySheetCommodity returnCommoditySheetCommodity = listRCSCSession.get(i);
			DataSourceContextHolder.setDbName(dbName);
			BaseModel bm = returnCommoditySheetCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodity);
			if (returnCommoditySheetCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				hasError = true;
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("商品ID=" + returnCommoditySheetCommodity.getCommodityID() + ":" + returnCommoditySheetCommodityBO.printErrorInfo());
				}
				System.out.println("商品ID=" + returnCommoditySheetCommodity.getCommodityID() + ":" + returnCommoditySheetCommodityBO.printErrorInfo());
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(returnCommoditySheetCommodity.getCommodityID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个商品信息失败:" + ecOut.toString());
				}
				logger.error("退货商品：" + (commodity == null ? "未知商品" : commodity.getName()) + "[ID=" + returnCommoditySheetCommodity.getCommodityID() + "]创建失败\t");
				continue;
			}

			listRcsc.add((ReturnCommoditySheetCommodity) bm);
		}
		returnCommoditySheet.setListSlave1(listRcsc);
		if (hasError) {
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
			params.put(KEY_HTMLTable_Parameter_msg, "修改退货单失败。请重试。");
		} else {
			params.put(JSON_ERROR_KEY, returnCommoditySheetCommodityBO.getLastErrorCode());
		}

		return returnCommoditySheet;

		// 对退货单修改后，清除原先缓存
		// 现在没有退货单缓存
		// CacheManager.getCache(EnumCacheType.ECT_ReturnCommoditySheet).write1(returnCommoditySheet);
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * commodityID=1&pricePurchase=10.5&NO=20&specification=箱&providerID=1
	 * URL:http://localhost:8080/nbr/returnCommoditySheetAction/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") ReturnCommoditySheet returnCommoditySheet, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个退货单，returnCommoditySheet=" + returnCommoditySheet);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			// 解析用户输入
			List<ReturnCommoditySheetCommodity> rcscList = parseReturnCommoditySheetCommodityList(returnCommoditySheet, req);
			if (rcscList == null) {
				logger.error("黑客行为");
				return null;
			}
			// 检查输入合法性
			if (!checkCreate(req, dbName, params, rcscList)) {
				break;
			}

			Staff staff = getStaffFromSession(req.getSession());
			//
			if(staff.getShopID() != 1 && staff.getShopID() != returnCommoditySheet.getShopID()) {
				logger.error("不能跨门店创建采购退货单");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(KEY_HTMLTable_Parameter_msg, "不能跨门店创建采购退货单");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			// 创建入库单和入库单商品
			ReturnCommoditySheet rcs = createMasterAndSalve(returnCommoditySheet, dbName, staff.getID(), params, rcscList);
			if (rcs == null) {
				break;
			}

			params.put(BaseAction.KEY_Object, rcs);
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected List<ReturnCommoditySheetCommodity> parseReturnCommoditySheetCommodityList(ReturnCommoditySheet returnCommoditySheet, HttpServletRequest req) {
		String sCommIDs = GetStringFromRequest(req, "commIDs", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sRcscNOs = GetStringFromRequest(req, "rcscNOs", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sCommPrices = GetStringFromRequest(req, "commPrices", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sBarcodeIDs = GetStringFromRequest(req, "barcodeIDs", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sRcscSpecifications = GetStringFromRequest(req, "rcscSpecifications", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sProviderID = GetStringFromRequest(req, "providerID", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sRcscNOs.equals(String.valueOf(BaseAction.INVALID_ID)) || sCommPrices.equals(String.valueOf(BaseAction.INVALID_ID))
				|| sRcscSpecifications.equals(String.valueOf(BaseAction.INVALID_ID)) || sProviderID.equals(String.valueOf(BaseAction.INVALID_ID)) || sBarcodeIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.error("参数缺失");
			return null;
		} else {
			logger.info("所有参数都符合要求，sCommIDs=" + sCommIDs + "，sRcscNOs=" + sRcscNOs + "，sCommPrices=" + "，sBarcodeIDs=" + sBarcodeIDs + "，sRcscSpecifications=" + sRcscSpecifications + "，sProviderID=" + sProviderID);
		}
		//
		Integer[] iaCommID = GeneralUtil.toIntArray(sCommIDs);
		Integer[] iaRcscNO = GeneralUtil.toIntArray(sRcscNOs);
		Integer[] iaBarcodeID = GeneralUtil.toIntArray(sBarcodeIDs);
		Double[] iaCommPrices = GeneralUtil.toDoubleArray(sCommPrices);
		String[] saRcscSpecification = sRcscSpecifications.split(",");
		int iProviderID = Integer.valueOf(sProviderID);
		if (iaCommID == null || iaBarcodeID == null || iaRcscNO == null || iaCommPrices == null || saRcscSpecification == null || iaCommID.length != iaRcscNO.length || iaCommID.length != iaBarcodeID.length
				|| iaCommID.length != iaCommPrices.length || iaCommID.length != saRcscSpecification.length || iaCommID.length == 0 || iProviderID <= 0) {
			logger.error("参数缺失");
			return null;
		} else {
			logger.info("iaCommID=" + iaCommID + "，iaRcscNO=" + iaRcscNO + "，iaBarcodeID=" + iaBarcodeID + "，iaCommPrices=" + iaCommPrices + "，saRcscSpecification=" + saRcscSpecification + "，iProviderID=" + iProviderID);
		}

		// 一旦发现iaCommID内部有重复元素，当黑客行为处理
		if (GeneralUtil.hasDuplicatedElement(iaCommID)) {
			logger.error("黑客传递的参数有重复！");
			return null;
		}

		List<ReturnCommoditySheetCommodity> listRcsc = new ArrayList<ReturnCommoditySheetCommodity>();
		for (int i = 0; i < iaCommID.length; i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(returnCommoditySheet.getID());
			rcsc.setCommodityID(iaCommID[i]);
			rcsc.setBarcodeID(iaBarcodeID[i]);
			rcsc.setNO(iaRcscNO[i]);
			rcsc.setSpecification(saRcscSpecification[i]);
			rcsc.setPurchasingPrice(iaCommPrices[i]);

			listRcsc.add(rcsc);
		}
		return listRcsc;
	}

	private boolean checkCreate(HttpServletRequest req, String dbName, Map<String, Object> params, List<ReturnCommoditySheetCommodity> rcscList) {
		Commodity commRetrieved = null;
		ErrorInfo eiOut = new ErrorInfo();
		for (ReturnCommoditySheetCommodity rcsc : rcscList) {
			String err = rcsc.checkCreate(BaseBO.CASE_CheckCreateForAction); // 先开始检查从表，保证主表创建成功后从表也能正常创建，保证数据不会出现有主表没从表或从表少了。
			if (!err.equals("")) {
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
				params.put(KEY_HTMLTable_Parameter_msg, err);
				return false;
			}

			DataSourceContextHolder.setDbName(dbName);
			commRetrieved = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(rcsc.getCommodityID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (commRetrieved == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {// 有可能该商品是存在的，只是读取时发生了DB异常，但这种情况几乎不会出现，所以认为是商品不存在
				logger.error("不能退货一个不存在的商品：CommodityID=" + rcsc.getCommodityID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "不能退货一个不存在的商品");
				return false;
			} else {
				logger.info("查询商品成功=" + commRetrieved);
				if (commRetrieved.getType() != Commodity.EnumStatusCommodity.ESC_Normal.getIndex()) {
					logger.error("不能退货单品以外的商品：" + commRetrieved);
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
					params.put(KEY_HTMLTable_Parameter_msg, "不能退货单品以外的商品（商品" + commRetrieved.getName() + "的类型不是单品）");
					return false;
				}
			}

			DataSourceContextHolder.setDbName(dbName);
			Barcodes barcodesRetrieve1 = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(rcsc.getBarcodeID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (barcodesRetrieve1 == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("该条形码不存在：BarcodeID=" + rcsc.getBarcodeID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "该条形码不存在");
				return false;
			}
			if (barcodesRetrieve1.getCommodityID() != rcsc.getCommodityID()) {
				logger.error("条形码ID与商品实际条形码ID不对应：BarcodeID=" + rcsc.getBarcodeID() + ",CommodityID=" + rcsc.getCommodityID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(KEY_HTMLTable_Parameter_msg, "条形码与商品实际条形码不对应");
				return false;
			}
		}

		return true;
	}

	private ReturnCommoditySheet createMasterAndSalve(ReturnCommoditySheet returnCommoditySheet, String dbName, int staffID, Map<String, Object> params, List<ReturnCommoditySheetCommodity> rcscList) {
		returnCommoditySheet.setStaffID(staffID);
		DataSourceContextHolder.setDbName(dbName);
		ReturnCommoditySheet rcs = (ReturnCommoditySheet) returnCommoditySheetBO.createObject(staffID, BaseBO.INVALID_CASE_ID, returnCommoditySheet);
		if (returnCommoditySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("创建退货单失败:" + returnCommoditySheetBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, returnCommoditySheetBO.getLastErrorCode().toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, returnCommoditySheetBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("创建退货单成功：" + rcs);
		}

		// 创建退货单商品list
		List<ReturnCommoditySheetCommodity> listRcsc = new ArrayList<ReturnCommoditySheetCommodity>();
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();

		StringBuilder sbErrorMsg = new StringBuilder();
		boolean hasError = false;
		for (int i = 0; i < rcscList.size(); i++) {
			rcsc = rcscList.get(i);
			rcsc.setReturnCommoditySheetID(rcs.getID());
			DataSourceContextHolder.setDbName(dbName);
			ReturnCommoditySheetCommodity rcsCommodity = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityBO.createObject(staffID, BaseBO.INVALID_CASE_ID, rcsc);
			if (returnCommoditySheetCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				int commodityID = ((ReturnCommoditySheetCommodity) rcscList.get(i)).getCommodityID();
				logger.error("商品ID=" + commodityID + ":" + returnCommoditySheetCommodityBO.printErrorInfo());

				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, staffID, ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个商品信息失败:" + ecOut.toString());
				}
				sbErrorMsg.append("退货商品：" + (commodity == null ? "未知商品" : commodity.getName()) + "[ID=" + commodityID + "]创建失败\t");
				hasError = true;
				continue;
			}

			listRcsc.add(rcsCommodity);
		}
		rcs.setListSlave1(listRcsc);
		if (hasError) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
			params.put(KEY_HTMLTable_Parameter_msg, sbErrorMsg.toString());
		} else {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
			params.put(KEY_HTMLTable_Parameter_msg, "");
		}

		return rcs;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/returnCommoditySheetAction/retrieve1Ex.bx?ID=1&
	 * rcs.String1=Provider&rcsc.String1=barcode rcs.setListSlave1(listRcsc);
	 * 退货单从表信息放在主表的ListSlave1中
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") ReturnCommoditySheet returnCommoditySheet, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个退货单,returnCommoditySheet" + returnCommoditySheet);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		// 清空会话，以免下面DB有错误时，无法更新会话
		// session.removeAttribute(EnumSession.SESSION_ReturnCommoditySheet.getName());
		// session.removeAttribute(EnumSession.SESSION_ReturnCommoditySheetTempReturnCommoditySheetCommodityList.getName());

		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> rcsList = returnCommoditySheetBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, returnCommoditySheet);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (returnCommoditySheetBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！rcs=" + rcsList);
			//
			ReturnCommoditySheet rcs = null;
			if (rcsList.get(0).size() != 0 && rcsList != null) {
				rcs = (ReturnCommoditySheet) rcsList.get(0).get(0);
			}
			//
			ErrorInfo ecOut = new ErrorInfo();
			Boolean hasDBError = false;
			Provider provider = (Provider) CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(rcs.getProviderID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个供应商信息失败:" + ecOut.toString());
				hasDBError = true;
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
				break;
			} else {
				logger.info("查询一个供应商信息成功！provider=" + provider);
				rcs.setProviderName(provider.getName());
			}
			// 查询门店
			Shop shopR1Condition = new Shop();
			shopR1Condition.setID(rcs.getShopID());
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> listBM = shopBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, shopR1Condition);
			if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(listBM != null && listBM.size() > 0 && listBM.get(0).get(0) != null)) {
				logger.error("查询一个门店信息失败:" + ecOut.toString());
				hasDBError = true;
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
				break;
			} else {
				Shop shopR1 = (Shop) listBM.get(0).get(0);
				logger.info("查询一个门店信息成功！shopR1=" + shopR1);
				rcs.setShopName(shopR1.getName());
			}

			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(rcs.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个经办人信息失败:" + ecOut.toString());
				hasDBError = true;
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
				break;
			} else {
				logger.info("查询一个经办人信息成功！staff=" + staff);
				rcs.setStaffName(staff.getName());
			}

			ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
			returnCommoditySheetCommodity.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			returnCommoditySheetCommodity.setReturnCommoditySheetID(rcs.getID());
			//
			List<BaseModel> listRcsc = rcsList.get(1);
			for (BaseModel bm : listRcsc) {
				ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) bm;
				Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(rcsc.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);

				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个商品信息失败:" + ecOut.toString());
					hasDBError = true;
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					break;
				} else {
					logger.info("查询一个商品信息成功！commodity=" + commodity);
					rcsc.setCommodity(commodity);
				}

				PackageUnit pu = new PackageUnit();
				pu.setID(commodity.getPackageUnitID());
				//
				DataSourceContextHolder.setDbName(dbName);
				PackageUnit packageUnit = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
				if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个包装单位失败:" + packageUnitBO.printErrorInfo());
					hasDBError = true;
					params.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					break;
				} else {
					logger.info("查询一个包装单位成功！packageUnit=" + packageUnit);
					rcsc.setPackageUnit(packageUnit.getName());
				}

				Barcodes b = new Barcodes();
				b.setID(rcsc.getBarcodeID());
				DataSourceContextHolder.setDbName(dbName);
				Barcodes barcode = (Barcodes) barcodesBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
				if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询条形码失败：" + barcodesBO.printErrorInfo());
					hasDBError = true;
					params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					break;
				} else {
					logger.info("查询条形码成功！barcode=" + barcode);
				}

				rcsc.setBarcodes(barcode.getBarcode());
			}

			if (hasDBError) {
				break;
			}
			rcs.setListSlave1(listRcsc);
			// session.setAttribute(EnumSession.SESSION_ReturnCommoditySheet.getName(),
			// rcs);
			// session.setAttribute(EnumSession.SESSION_ReturnCommoditySheetTempReturnCommoditySheetCommodityList.getName(),
			// listRcsc);

			params.put("returnCommoditySheet", rcs);
			// params.put("returnCommoditySheetCommodity", listRcsc);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.error("未知错误！");
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/returnCommoditySheetAction/retrieveNEx.bx?
	 * string1=providerName&string2=staffName
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") ReturnCommoditySheet returnCommoditySheet, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个退货单，returnCommoditySheet=" + returnCommoditySheet);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		List<ReturnCommoditySheet> bmList = (List<ReturnCommoditySheet>) returnCommoditySheetBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, returnCommoditySheet);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (returnCommoditySheetBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！bmList=" + bmList);

			ErrorInfo ecOut = new ErrorInfo();
			Boolean hasDBError = false;
			for (ReturnCommoditySheet rcs : bmList) {
				Provider provider = (Provider) CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(rcs.getProviderID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个供应商信息失败:" + ecOut.toString());
					hasDBError = true;
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					break;
				} else {
					logger.info("查询一个供应商信息成功！provider=" + provider);
					rcs.setProviderName(provider.getName());
				}

				Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(rcs.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
				// staff可能为null
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || staff == null) {
					logger.error("查询一个经办人信息失败:" + ecOut.toString());
					hasDBError = true;
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					break;
				} else {
					logger.info("查询一个经办人信息成功！staff=" + staff);
					rcs.setStaffName(staff.getName());
				}
			}

			if (hasDBError) {
				break;
			}
			
			params.put(BaseAction.KEY_ObjectList, bmList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_NoPermission:
			logger.error("没有权限进行操作");
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("未知错误！");
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params);
		params.put(KEY_HTMLTable_Parameter_TotalRecord, returnCommoditySheetBO.getTotalRecord());
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") ReturnCommoditySheet returnCommoditySheet, ModelMap model, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个退货单,returnCommoditySheet=" + returnCommoditySheet);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			// 解析用户输入
			List<ReturnCommoditySheetCommodity> rcscList = parseReturnCommoditySheetCommodityList(returnCommoditySheet, req);
			if (rcscList == null) {
				logger.error("黑客行为");
				return null;
			}
			//
			returnCommoditySheet = doUpdate(returnCommoditySheet, rcscList, params, dbName, req);
			if (params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV && params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_WrongFormatForInputField) {
					logger.error("退货单修改失败，错误码=" + params.get(BaseAction.JSON_ERROR_KEY.toString()));
				}
				break;
			} else {
				logger.info("退货单修改成功");
			}

			logger.info("returnCommoditySheet=" + returnCommoditySheet);
			params.put(BaseAction.KEY_Object, returnCommoditySheet);
			break;
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/returnCommoditySheetAction/updateEx.bx?ID=1&
	 * int1=0 int1： =1代表前端有修改过退货单的商品；=0代表没有修改过
	 */
	@RequestMapping(value = "/approveEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String approveEx(@ModelAttribute("SpringWeb") ReturnCommoditySheet returnCommoditySheet, ModelMap model, HttpServletRequest req) throws IOException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("审核一个退货单,returnCommoditySheet=" + returnCommoditySheet);

		HttpSession session = req.getSession();
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> rcsList = returnCommoditySheetBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, returnCommoditySheet);
		if(returnCommoditySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(rcsList != null && rcsList.get(0).size() != 0)) {
			logger.error("查询该退货单失败");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该采购退货单");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		ReturnCommoditySheet rcsR1 = (ReturnCommoditySheet) rcsList.get(0).get(0);
		// 获取staffID
		logger.info("调用staffSession得到相关退货单的审核人信息");
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		if (staff == null) {
			logger.error("session没有值");
			return null;
		} else {
			logger.info("staff=" + staff);
		}
		if(staff.getShopID() != 1 && staff.getShopID() != rcsR1.getShopID()) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店审核退货单");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		logger.info("开始审核");
		do {

			returnCommoditySheet.setStaffID(staff.getID());

			ReturnCommoditySheet returnCommoditySheetUpdated = null;
			if (returnCommoditySheet.getbReturnCommodityListIsModified() == EnumBoolean.EB_NO.getIndex()) {
				logger.info("用户没修改退货单，直接进行审核");
				returnCommoditySheetUpdated = returnCommoditySheet;
			} else if (returnCommoditySheet.getbReturnCommodityListIsModified() == EnumBoolean.EB_Yes.getIndex()) { // web前端已经修改过退货单的商品列表
				// 解析用户输入
				List<ReturnCommoditySheetCommodity> rcscList = parseReturnCommoditySheetCommodityList(returnCommoditySheet, req);
				if (rcscList == null) {
					logger.error("黑客行为");
					return null;
				}

				// 修改退货单
				returnCommoditySheetUpdated = doUpdate(returnCommoditySheet, rcscList, params, dbName, req);
				if (params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_NoError) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("退货单修改失败，错误码=" + params.get(BaseAction.JSON_ERROR_KEY.toString()));
					}
					break; // 可能是部分成功也可能是错误，不再进行审核
				} else {
					logger.info("退货单修改成功");
				}
			} else {
				logger.error("黑客行为：前端没有传递合法的参数！");
				return null;
			}

			Integer[] iaCommodityID = null;
			DataSourceContextHolder.setDbName(dbName);
			ReturnCommoditySheet approveReturnCommoditySheet = (ReturnCommoditySheet) returnCommoditySheetBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_ApproveReturnCommoditySheet, returnCommoditySheetUpdated);
			if (returnCommoditySheetBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
				iaCommodityID = GeneralUtil.toIntArray(approveReturnCommoditySheet.getCommodityIDs());
				logger.info("审核成功。商品库存被改变，准备更新商品的缓存...");
				// 删除对应商品缓存，因修改了商品的库存
				logger.info("iaCommodityID=" + iaCommodityID);
				if (iaCommodityID != null) {
					Commodity commodity = new Commodity();
					for (int commID : iaCommodityID) {
						commodity.setID(commID);
						CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).delete1(commodity);
					}
				} else {
					logger.error("无法更新商品的缓存，这可能导致web前端看到的商品的库存数目不正确。");
				}
				params.put(BaseAction.JSON_ERROR_KEY, returnCommoditySheetBO.getLastErrorCode());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
				params.put(BaseAction.KEY_Object, approveReturnCommoditySheet);
				// 还要更新入库单的缓存，因修改了入库单的可售数量
				CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).deleteAll();
			} else {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("审核失败:" + returnCommoditySheetBO.printErrorInfo());
				}
				params.put(BaseAction.JSON_ERROR_KEY, returnCommoditySheetBO.getLastErrorCode());
				if (returnCommoditySheet.getbReturnCommodityListIsModified() == 1) {
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "更新退货单成功，但审核失败：" + returnCommoditySheetBO.getLastErrorMessage());
				} else {
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：" + returnCommoditySheetBO.getLastErrorMessage());
				}
			}
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
