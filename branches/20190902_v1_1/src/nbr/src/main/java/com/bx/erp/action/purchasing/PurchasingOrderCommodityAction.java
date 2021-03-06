package com.bx.erp.action.purchasing;

import java.util.ArrayList;
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

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/purchasingOrderCommodity")
@Scope("prototype")
public class PurchasingOrderCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(PurchasingOrderCommodityAction.class);

	@Resource
	private PurchasingOrderCommodityBO purchasingOrderCommodityBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private ProviderBO providerBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private PackageUnitBO packageUnitBO;

	@Resource
	private WarehousingCommodityBO warehousingCommodityBO;

	// /*
	// * Request Body in Fiddler: URL: GET
	// * :http://localhost:8080/nbr/purchasingOrderCommodity/retrieveN.bx?
	// * purchasingOrderID=1
	// */
	// @RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
	// public void retrieveN(@ModelAttribute("SpringWeb") PurchasingOrderCommodity
	// purchasingOrderCommodity, ModelMap model) {
	// List<?> ls =
	// purchasingOrderCommodityBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, purchasingOrderCommodity);
	//
	// logger.info("RetrieveN purchasingOrder error code=" +
	// purchasingOrderCommodityBO.getLastErrorCode());
	// switch (purchasingOrderCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("ls:" + ls);
	// break;
	// default:
	// // "", "???????????????");
	// break;
	// }
	// }

	// /*
	// * Request Body in Fiddler: purchasingOrderID=2&commodityID=5&commodityNO=70
	// * URL: POST: http://localhost:8080/nbr/purchasingOrderCommodity/create.bx
	// */
	// @RequestMapping(value = "/create", method = RequestMethod.POST)
	// public void create(@ModelAttribute("SpringWeb") PurchasingOrderCommodity
	// purchasingOrderCommodity, ModelMap model) {
	// BaseModel bm =
	// purchasingOrderCommodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, purchasingOrderCommodity);
	//
	// logger.info("Create purchasingOrder error code=" +
	// purchasingOrderCommodityBO.getLastErrorCode());
	// switch (purchasingOrderCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_PurchasingOrderCommodity).write1(bm);
	//
	// PurchasingOrderCommodity orderCommodity = (PurchasingOrderCommodity) bm;
	// logger.info(orderCommodity);
	// break;
	// case EC_BusinessLogicNotDefined:
	// // ...
	// break;
	// default:
	// // "", "???????????????");
	// break;
	// }
	// }

	// /*
	// * Request Body in Fiddler: URL: GET
	// * :http://localhost:8080/nbr/purchasingOrderCommodity/delete.bx?
	// * purchasingOrderID=4&commodityID=4
	// */
	// @RequestMapping(value = "/delete", method = RequestMethod.GET)
	// public void delete(@ModelAttribute("SpringWeb") PurchasingOrderCommodity
	// purchasingOrderCommodity, ModelMap model) {
	// purchasingOrderCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, purchasingOrderCommodity);
	//
	// logger.info("Delete purchasingOrder error code=" +
	// purchasingOrderCommodityBO.getLastErrorCode());
	// switch (purchasingOrderCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_PurchasingOrderCommodity).delete1(purchasingOrderCommodity);
	//
	// logger.info("???????????????");
	// break;
	// default:
	// // "", "???????????????");
	// break;
	// }
	// }

	/*
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/purchasingOrderCommodity/appendToCommodityListEx.bx
	 * ?commID=2,5
	 */
	// @RequestMapping(value = "/appendToCommodityListEx", produces = "plain/text;
	// charset=utf-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String appendToCommodityListEx(@ModelAttribute("SpringWeb")
	// PurchasingOrderCommodity purchasingOrderCommodity, ModelMap model,
	// HttpServletRequest req) {
	// logger.info("?????????????????????session??????purchasingOrderCommodity=" +
	// purchasingOrderCommodity.toString());
	// HttpSession session = req.getSession();
	//
	// String dbName = getDBNameFromSession(session);
	// // ??????IDs
	// String commIDList = GetStringFromRequest(req, "commID",
	// String.valueOf(INVALID_ID)).trim(); // Format: 1,2,3
	// String barcodeIDList = GetStringFromRequest(req, "barcodeIDs",
	// String.valueOf(INVALID_ID)).trim();
	// String NOs = GetStringFromRequest(req, "NOs",
	// String.valueOf(INVALID_ID)).trim();
	// String packageUnitIDs = GetStringFromRequest(req, "packageUnitIDs",
	// String.valueOf(INVALID_ID)).trim();
	// String priceSuggestions = GetStringFromRequest(req, "priceSuggestions",
	// String.valueOf(INVALID_ID)).trim();
	// String commNames = GetStringFromRequest(req, "commNames",
	// String.valueOf(INVALID_ID)).trim();
	//
	// if (commIDList.equals(String.valueOf(INVALID_ID)) ||
	// barcodeIDList.equals(String.valueOf(INVALID_ID)) ||
	// NOs.equals(String.valueOf(INVALID_ID)) ||
	// commNames.equals(String.valueOf(INVALID_ID))
	// || packageUnitIDs.equals(String.valueOf(INVALID_ID)) ||
	// priceSuggestions.equals(String.valueOf(INVALID_ID))) {
	// logger.info("????????????");
	// return "";
	// } else {
	// logger.info("commIDList=" + commIDList + ",barcodeIDList=" + barcodeIDList +
	// ",NOs=" + NOs + ",priceSuggestions=" + priceSuggestions + ",commNames=" +
	// commNames);
	// }
	//
	// int[] iArrCommID = toIntArray(commIDList);
	// int[] iArrBarcodeID = toIntArray(barcodeIDList);
	// int[] iArrNO = toIntArray(NOs);
	// int[] iArrPackageUnitIDs = toIntArray(packageUnitIDs);
	// float[] iArrPriceSuggestions = tofloatArray(priceSuggestions);
	// String[] iArrCommNames = commNames.split(",");
	//
	// if (iArrCommID == null || iArrBarcodeID == null || iArrNO == null ||
	// iArrPackageUnitIDs == null || iArrPriceSuggestions == null || iArrCommNames
	// == null || iArrCommID.length != iArrBarcodeID.length
	// || iArrCommID.length != iArrNO.length || iArrCommID.length !=
	// iArrPackageUnitIDs.length || iArrCommID.length != iArrPriceSuggestions.length
	// || iArrCommID.length != iArrCommNames.length) {
	// logger.info("???????????????????????????????????????");
	// return ""; // ...
	// } else {
	// logger.info("iArrCommID=" + iArrCommID + ",iArrBarcodeID=" + iArrBarcodeID +
	// ",iArrPackageUnitIDs=" + iArrPackageUnitIDs + ",iArrPriceSuggestions=" +
	// iArrPriceSuggestions + ",iArrCommNames=" + iArrCommNames);
	// }
	//
	// List<Commodity> listExistingCommodity = new ArrayList<Commodity>();
	// for (int ID : iArrCommID) {
	// appendCommodity(ID, listExistingCommodity, dbName);
	// }
	//
	// PurchasingOrder po = (PurchasingOrder)
	// session.getAttribute(EnumSession.SESSION_PurchasingOrder.getName());
	// if (po == null) {
	// logger.info("po??????");
	// return ""; // ...TODO
	// } else {
	// logger.info("po=" + po.toString());
	// }
	//
	// List<PurchasingOrderCommodity> poCommList = new
	// ArrayList<PurchasingOrderCommodity>();
	// for (int i = 0; i < iArrCommID.length; i++) {
	// appendCommodity(po.getID(), iArrCommID[i], iArrBarcodeID[i], iArrNO[i],
	// iArrPackageUnitIDs[i], iArrPriceSuggestions[i], iArrCommNames[i],
	// poCommList);
	// }
	//
	// // ??????Session??????????????????????????????????????????????????????
	// session.removeAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName());
	// session.setAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName(),
	// poCommList);
	// logger.info("Session SESSION_PurchasingOrderTempPurchasingOrderCommodityList
	// updated to :" + poCommList);
	// // ??????Session??????????????????????????????????????????????????????
	// session.removeAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName());
	// session.setAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName(),
	// listExistingCommodity);
	// logger.info("Session SESSION_PurchasingOrderCommodityList updated to :" +
	// listExistingCommodity);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("orderCommList", poCommList);
	// params.put("commList", listExistingCommodity);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	// private EnumErrorCode appendCommodity(int purchasingOrderID, int commID, int
	// barcodeID, int NO, int packageUnitID, double priceSuggestions, String
	// commName, List<PurchasingOrderCommodity> list) {
	// logger.info("????????????????????????????????????");
	//
	// PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
	// poc.setCommodityID(commID);
	// poc.setPurchasingOrderID(purchasingOrderID);
	// poc.setBarcodeID(barcodeID);
	// poc.setCommodityNO(NO);
	// poc.setPackageUnitID(packageUnitID);
	// poc.setPriceSuggestion(priceSuggestions);
	// poc.setCommodityName(commName);
	// list.add(poc);
	//
	// logger.info("???????????????poc=" + poc.toString() + ",?????????list=" + list.toString());
	// return EnumErrorCode.EC_NoError;
	// }

	/*??????????????????
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/purchasingOrderCommodity/reduceEx.bx?commodityID=2
	 */
	//	@SuppressWarnings("unchecked")
	//	@RequestMapping(value = "/reduceEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	//	@ResponseBody
	//	public String reduceCommodityEx(@ModelAttribute("SpringWeb") PurchasingOrderCommodity purchasingOrderCommodity, ModelMap model, HttpServletRequest req) {
	//		logger.info("???session????????????purchasingOrderCommodity=" + purchasingOrderCommodity.toString());
	//
	//		HttpSession session = req.getSession();
	//
	//		List<PurchasingOrderCommodity> listPOC = (List<PurchasingOrderCommodity>) session.getAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName());
	//		List<Commodity> listComm = (List<Commodity>) session.getAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName());
	//		PurchasingOrder po = (PurchasingOrder) session.getAttribute(EnumSession.SESSION_PurchasingOrder.getName());
	//		logger.info("Order in session:" + po);
	//		if (listPOC == null || listComm == null || po == null) {
	//			logger.info("session???????????????");
	//			return "";
	//		} else {
	//			logger.info("????????????,listPOC=" + listPOC.toString() + ",listComm=" + listComm.toString() + ",po=" + po.toString());
	//		}
	//
	//		for (PurchasingOrderCommodity poc : listPOC) {
	//			if (poc.getCommodityID() == purchasingOrderCommodity.getCommodityID()) {
	//				listPOC.remove(poc);
	//				break;
	//			}
	//		}
	//
	//		for (Commodity c : listComm) {
	//			if (c.getID() == purchasingOrderCommodity.getCommodityID()) {
	//				listComm.remove(c);
	//				break;
	//			}
	//		}
	//
	//		if (listPOC.size() == 0) {
	//			po.setProviderID(0);
	//		}
	//
	//		logger.info("Session SESSION_PurchasingOrderTempPurchasingOrderCommodityList updated to :" + session.getAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName()));
	//		logger.info("Session SESSION_PurchasingOrderCommodityList updated to :" + session.getAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName()));
	//		logger.info("Session SESSION_PurchasingOrder updated to :" + session.getAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName()));
	//
	//		Map<String, Object> params = new HashMap<String, Object>();
	//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//		params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
	//		logger.info("???????????????=" + params);
	//
	//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	//	}

	/*
	 * Request Body in Fiddler: POST
	 * commID=2&commNO=60&commPrice=29.9&commPurchasingUnit=??? :URL:
	 * http://localhost:8080/nbr/purchasingOrderCommodity/updateNOPriceUnitEx.bx
	 */
	// @SuppressWarnings({ "unchecked" })
	// @RequestMapping(value = "/updateNOPriceUnitEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.POST)
	// @ResponseBody
	// public String updateNOPriceUnitEx(@ModelAttribute("SpringWeb")
	// PurchasingOrderCommodity purchasingOrderCommodity, ModelMap model,
	// HttpServletRequest req) {
	// logger.info("???session?????????????????????????????????");
	//
	// HttpSession session = req.getSession();
	//
	// List<Commodity> commList = (List<Commodity>)
	// session.getAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName());
	// List<PurchasingOrderCommodity> orderCommList =
	// (List<PurchasingOrderCommodity>)
	// session.getAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName());
	// logger.info("List in session:" + orderCommList);
	// String sCommID = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// String sCommNO = GetStringFromRequest(req, "commNO",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// String sCommPrices = GetStringFromRequest(req, "commPrice",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// // String scommPurchasingUnit = GetStringFromRequest(req,
	// "commPurchasingUnit",
	// // String.valueOf(BaseAction.INVALID_ID)).trim();
	// // || scommPurchasingUnit.equals(String.valueOf(BaseAction.INVALID_ID))
	// if (sCommID.equals(String.valueOf(BaseAction.INVALID_ID)) ||
	// (sCommNO.equals(String.valueOf(BaseAction.INVALID_ID))) ||
	// sCommPrices.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("????????????");
	// return ""; // ...
	// } else {
	// logger.info("????????????,sCommID=" + sCommID + ",sCommNO=" + sCommNO +
	// ",sCommPrices" + sCommPrices);
	// }
	//
	// try {
	// int iCommID = Integer.parseInt(sCommID);
	// int iCommNO = Integer.parseInt(sCommNO);
	// double fCommPrices = Float.parseFloat(sCommPrices);
	//
	// if (iCommID >= 0 && fCommPrices >= 0) {
	// for (int i = 0; i < orderCommList.size(); i++) {
	// if (orderCommList.get(i).getCommodityID() == iCommID) {
	// orderCommList.get(i).setCommodityNO(iCommNO);
	// orderCommList.get(i).setPriceSuggestion(fCommPrices);
	// break;
	// }
	// }
	//
	// // for (int j = 0; j < commList.size(); j++) {
	// // commList.get(j).setPurchasingUnit(scommPurchasingUnit);
	// // }
	//
	// logger.info("List to update session:" + orderCommList);
	// session.setAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName(),
	// orderCommList);
	// logger.info("List to update session:" + commList);
	// session.setAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName(),
	// commList);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put(EnumSession.SESSION_PurchasingOrderCommodityList.getName(),
	// commList);
	// params.put(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName(),
	// orderCommList);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.info("iCommID??????0???fCommPrices??????0");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return ""; // ...
	// }

	/*??????????????????
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/purchasingOrderCommodity/appendToCommodityEx.bx?
	 * commodityID=2&barcodeID=1&commodityNO=10&packageUnitID=1&priceSuggestion=1.1&
	 * commodityName=??????
	 */
	//	@SuppressWarnings("unchecked")
	//	@RequestMapping(value = "/appendToCommodityEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	//	@ResponseBody
	//	public String appendToCommodityEx(@ModelAttribute("SpringWeb") PurchasingOrderCommodity purchasingOrderCommodity, ModelMap model, HttpServletRequest req) {
	//		logger.info("???????????????session??????purchasingOrderCommodity=" + purchasingOrderCommodity.toString());
	//
	//		HttpSession session = req.getSession();
	//
	//		PurchasingOrder po = (PurchasingOrder) session.getAttribute(EnumSession.SESSION_PurchasingOrder.getName());
	//		if (po == null) {
	//			logger.info("???????????????session??????");
	//			return ""; // ...TODO
	//		} else {
	//			logger.info("po=" + po.toString());
	//		}
	//
	//		List<PurchasingOrderCommodity> purchasingOrderTempPurchasingOrderCommodityList = (List<PurchasingOrderCommodity>) session.getAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName());
	//		if (purchasingOrderTempPurchasingOrderCommodityList == null || purchasingOrderTempPurchasingOrderCommodityList.size() == 0) {
	//			logger.info("???????????????session??????");
	//			return "";
	//		} else {
	//			logger.info("purchasingOrderTempPurchasingOrderCommodityList=" + purchasingOrderTempPurchasingOrderCommodityList.toString());
	//		}
	//
	//		purchasingOrderCommodity.setPurchasingOrderID(po.getID());
	//		purchasingOrderTempPurchasingOrderCommodityList.add(purchasingOrderCommodity);
	//
	//		session.setAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName(), purchasingOrderTempPurchasingOrderCommodityList);
	//		logger.info("Session SESSION_PurchasingOrderTempPurchasingOrderCommodityList updated to :" + purchasingOrderTempPurchasingOrderCommodityList);
	//
	//		Map<String, Object> params = new HashMap<String, Object>();
	//		params.put("orderCommList", purchasingOrderTempPurchasingOrderCommodityList);
	//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//		params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
	//		logger.info("???????????????=" + params);
	//
	//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	//	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * ID=1&int=0 URL: POST:
	 * http://localhost:8080/nbr/purchasingOrder/retrieveNWarehousingEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNWarehousingEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNWarehousingEx(@ModelAttribute("SpringWeb") PurchasingOrderCommodity purchasingOrderCommodity, ModelMap model, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("???????????????????????????????????????,purchasingOrderCommodity=" + purchasingOrderCommodity);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		PurchasingOrder poi = new PurchasingOrder();
		List<Warehousing> listWs = new ArrayList<Warehousing>();
		//
		do {
			// 0:????????????????????? 1:?????????????????????
			if (purchasingOrderCommodity.getPurchasingOrderCommodityIsInWarehouse() == 0) {
				DataSourceContextHolder.setDbName(dbName);
				List<PurchasingOrderCommodity> pcList = (List<PurchasingOrderCommodity>) purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing,
						purchasingOrderCommodity);
				if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("???????????????????????????,?????????=" + purchasingOrderCommodityBO.getLastErrorCode().toString());
					params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderCommodityBO.getLastErrorCode().toString());
					break;
				} else {
					logger.info("????????????????????????????????????pcList=" + pcList);
				}
				poi.setListSlave1(pcList);
				if (!loadCommodityList(poi, dbName, session) || !loadBarcodeAndPackageUnit(poi, dbName, session)) {
					logger.info("???????????????????????????????????????");
					break; // ...
				} else {
					logger.info("????????????????????????????????????,poi=" + poi);
				}

			} else if (purchasingOrderCommodity.getPurchasingOrderCommodityIsInWarehouse() == 1) {
				boolean hasDBError = false;

				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> bmList = purchasingOrderCommodityBO.retrieveNObjectEx(getStaffFromSession(session).getID(), BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing, purchasingOrderCommodity);
				if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("?????????????????????????????????????????????=" + purchasingOrderCommodityBO.getLastErrorCode().toString());
					params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderCommodityBO.getLastErrorCode().toString());
					break;
				} else {
					logger.info("????????????????????????????????????pcList=" + bmList);
				}

				List<Warehousing> wsList = new ArrayList<Warehousing>();
				for (BaseModel bm : bmList.get(0)) {
					Warehousing w = (Warehousing) bm;

					ErrorInfo ecOut = new ErrorInfo();
					Warehouse wh = (Warehouse) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).read1(w.getWarehouseID(), getStaffFromSession(session).getID(), ecOut, dbName);
					if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("????????????????????????????????????=" + ecOut.getErrorCode().toString());
						params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					} else {
						logger.info("???????????????????????????wh=" + wh);
					}

					w.setWarehouseName(wh.getName());
					wsList.add(w);
				}

				//
				for (Warehousing ws : listWs) {
					if (!loadWarehousingCommodity(ws, dbName, session) || !loadWarehousingCommodityList(ws, dbName, session)
							|| !loadBarcodeAndPackageUnit(ws, dbName, session) || !setWarehousingCreatorAndAssessor(listWs, wsList, dbName, session)) { // ...
						hasDBError = true;
						break;
					}
				}
				if (hasDBError) {
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					break;
				}

				logger.info(listWs.toString());
			} else {
				return ""; // ...
			}

			params.put("purchasingOrderCommodity", poi);
			params.put("warehousingInfoList", listWs);
			params.put("count", purchasingOrderCommodityBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("???????????????=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private boolean setWarehousingCreatorAndAssessor(List<Warehousing> returnWsList, List<Warehousing> warehousingList, String dbName, HttpSession session) {
		logger.info("?????????????????????????????????????????????");

		for (Warehousing w : warehousingList) {
			// ????????????????????????
			ErrorInfo ecOut = new ErrorInfo();
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(w.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("???????????????????????????????????????=" + ecOut.getErrorCode().toString() + ",????????????=" + ecOut.getErrorMessage().toString());
				return false;
			} else {
				logger.info("??????????????????????????????staff=" + staff);
			}
			w.setStaffName(staff.getName());

			// ????????????????????????
			if (w.getApproverID() > 0) {
				Staff s = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(w.getApproverID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("???????????????????????????????????????=" + ecOut.getErrorCode().toString());
					return false;
				} else {
					logger.info("??????????????????????????????s=" + s);
				}
				w.setStaffName(s.getName());
			}

			returnWsList.add(w);

			logger.info("??????????????????????????????????????????????????????wsInfoList=" + returnWsList);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean loadWarehousingCommodity(Warehousing ws, String dbName, HttpSession session) {
		logger.info("????????????????????????");

		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(ws.getID());
		wc.setPageIndex(PAGE_StartIndex);
		wc.setPageSize(PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<?> listTmp = warehousingCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, wc);
		if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("?????????????????????????????????????????????=" + warehousingCommodityBO.getLastErrorCode().toString());
			return false;
		} else {
			logger.info("????????????????????????????????????listTmp=" + listTmp);
		}

		ws.setListSlave1((List<WarehousingCommodity>) listTmp);

		logger.info("?????????????????????????????????wsi=" + ws);

		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean loadWarehousingCommodityList(Warehousing ws, String dbName, HttpSession session) {
		logger.info("??????????????????????????????");

		List<WarehousingCommodity> listWC = (List<WarehousingCommodity>) ws.getListSlave1();
		List<Commodity> listComm = new ArrayList<Commodity>();
		for (WarehousingCommodity wc : listWC) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("????????????????????????????????????=" + ecOut.getErrorCode().toString() + ",????????????=" + ecOut.getErrorMessage().toString());
				return false;
			} else {
				logger.info("???????????????????????????bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setCommodityID(bm.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("??????????????????????????????????????????=" + barcodesBO.getLastErrorCode().toString());
				return false;
			} else {
				logger.info("?????????????????????????????????barcodeList=" + barcodeList);
			}

			String barcodes = "";
			for (Barcodes bc : barcodeList) {
				barcodes += bc.getBarcode() + " ";
			}
			((Commodity) bm).setBarcodes(barcodes);

			listComm.add((Commodity) bm);
		}
		ws.setListCommodity(listComm);

		logger.info("???????????????????????????????????????wsi=" + ws);

		return true;
	}

	@SuppressWarnings("unchecked")
	protected boolean loadPurchasingOrderCommodity(PurchasingOrder po, String dbName, HttpSession session) {
		logger.info("??????????????????????????????");
		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setPurchasingOrderID(po.getID());
		poc.setPageIndex(PAGE_StartIndex);
		poc.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> listTmp = purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, poc);
		if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("????????????????????????????????????????????????=" + purchasingOrderCommodityBO.getLastErrorCode().toString());
			return false;
		} else {
			logger.info("???????????????????????????????????????listTmp=" + listTmp);
		}

		po.setListSlave1((List<PurchasingOrderCommodity>) listTmp);

		logger.info("?????????????????????????????????poi=" + po);

		return true;
	}

	protected boolean loadBarcodeAndPackageUnit(PurchasingOrder po, String dbName, HttpSession session) {
		logger.info("???????????????????????????????????????????????????");

		if (po.getListSlave1() == null) {
			logger.info("???????????????????????????");
			return true;
		} else {
			logger.info("poi=" + po);
		}
		List<PurchasingOrderCommodity> listPOC = new ArrayList<PurchasingOrderCommodity>();
		for (Object o : po.getListSlave1()) {
			PurchasingOrderCommodity poc = (PurchasingOrderCommodity) o;

			ErrorInfo ecOut = new ErrorInfo();
			Barcodes barcode = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(poc.getBarcodeID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("???????????????????????????????????????=" + ecOut.getErrorCode().toString() + ",????????????=" + ecOut.getErrorMessage().toString());
				return false;
			} else {
				logger.info("barcode=" + barcode);
			}

			poc.setBarcode(barcode.getBarcode());

			PackageUnit pu = new PackageUnit();
			pu.setID(poc.getPackageUnitID());
			DataSourceContextHolder.setDbName(dbName);
			pu = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
			if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("??????????????????????????????????????????=" + packageUnitBO.getLastErrorCode().toString());
				return false;
			} else {
				logger.info("pu=" + pu);
			}

			poc.setPackageUnitName(pu.getName());

			listPOC.add(poc);
		}
		po.setListSlave1(listPOC);

		logger.info("????????????????????????????????????????????????????????????poi=" + po.toString());

		return true;
	}

	protected boolean loadBarcodeAndPackageUnit(Warehousing ws, String dbName, HttpSession session) {
		logger.info("????????????????????????????????????????????????");

		if (ws.getListSlave1() == null) {
			logger.info("???????????????????????????");
			return true;
		} else {
			logger.info("wsi=" + ws);
		}

		List<WarehousingCommodity> listWC = new ArrayList<WarehousingCommodity>();
		for (Object o : ws.getListSlave1()) {
			WarehousingCommodity wc = (WarehousingCommodity) o;

			ErrorInfo ecOut = new ErrorInfo();
			Barcodes barcode = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(wc.getBarcodeID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("???????????????????????????????????????=" + ecOut.getErrorCode().toString() + ",????????????=" + ecOut.getErrorMessage().toString());
				return false;
			} else {
				logger.info("barcode=" + barcode);
				wc.setBarcode(barcode == null ? "" : barcode.getBarcode());
			}

			PackageUnit pu = new PackageUnit();
			pu.setID(wc.getPackageUnitID());
			DataSourceContextHolder.setDbName(dbName);
			pu = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
			logger.info("Retrieve 1 PackageUnit error code=" + packageUnitBO.getLastErrorCode());
			if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("??????????????????????????????????????????=" + packageUnitBO.getLastErrorCode().toString());
				return false;
			} else {
				logger.info("?????????????????????????????????pu=" + pu);
			}
			wc.setPackageUnitName(pu.getName());

			listWC.add(wc);
		}
		ws.setListSlave1(listWC);

		logger.info("?????????????????????????????????????????????????????????wsi=" + ws);

		return true;
	}

	@SuppressWarnings("unchecked")
	protected boolean loadCommodityList(PurchasingOrder po, String dbName, HttpSession session) {
		logger.info("?????????????????????????????????");

		if (po.getListSlave1() == null) {
			logger.info("???????????????????????????");
			return true;
		} else {
			logger.info("poi=" + po);
		}
		List<PurchasingOrderCommodity> listPOC = (List<PurchasingOrderCommodity>) po.getListSlave1();
		List<Commodity> listComm = new ArrayList<Commodity>();
		for (PurchasingOrderCommodity POC : listPOC) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(POC.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("????????????????????????????????????=" + ecOut.getErrorCode().toString() + ",????????????=" + ecOut.getErrorMessage().toString());
				return false;
			} else {
				logger.info("???????????????????????????bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setCommodityID(bm.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("???????????????????????????????????????=" + barcodesBO.getLastErrorCode().toString());
				return false;
			} else {
				logger.info("??????????????????????????????barcodeList=" + barcodeList);
			}

			String barcodes = "";
			for (Barcodes bc : barcodeList) {
				barcodes += bc.getBarcode() + " ";
			}
			((Commodity) bm).setBarcodes(barcodes);

			listComm.add((Commodity) bm);
		}
		po.setListCommodity(listComm);
		logger.info("????????????????????????????????????poi=" + po);

		return true;
	}
}
