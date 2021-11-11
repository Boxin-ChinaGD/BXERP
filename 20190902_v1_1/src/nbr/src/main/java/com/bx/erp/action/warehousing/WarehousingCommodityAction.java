package com.bx.erp.action.warehousing;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;


@Controller
@RequestMapping("/warehousingCommodity")
@Scope("prototype")
public class WarehousingCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(WarehousingCommodityAction.class);

	@Resource
	private WarehousingCommodityBO warehousingCommodityBO;

	@Resource
	private CommodityBO commodityBO;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		return WAREHOUSINGCOMMODITY_ToIndex;
	}

	/*
	  Request Body in Fiddler: id=5&NO=250,301&price=5,6&commID=2,3
	  GET URL:http://localhost:8080/nbr/warehousingCommodity/create.bx
	 */
	// @RequestMapping(value = "/createEx", method = RequestMethod.POST)
	// @ResponseBody
	// public String createEx(ModelMap model, HttpServletRequest req) {
	// // 获取前端传来的数值并循环。根据商品ID查商品，并把该商品的信息赋予给入库单商品表所需要的数据。
	// String id = GetStringFromRequest(req, "id",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// String NOs = GetStringFromRequest(req, "NO",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// String prices = GetStringFromRequest(req, "price",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// String commIDs = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (id.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0 ||
	// NOs.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0 ||
	// prices.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0
	// || commIDs.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0) {
	// return ""; // ...
	// }
	//
	// String[] no = NOs.split(",");
	// String[] price = prices.split(",");
	// String[] commID = commIDs.split(",");
	// Commodity comm = new Commodity();
	// try {
	// WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
	// // if (Integer.parseInt(commIDs.trim()) >= 0) {
	// for (int i = 0; i < commID.length; i++) {
	// comm.setID(Integer.parseInt(commID[i]));
	// Commodity commodity = (Commodity)
	// commodityBO.retrieve1Object(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, comm);
	// warehousingCommodity.setCommodityID(commodity.getID());
	// warehousingCommodity.setWarehousingID(Integer.parseInt(id));
	// warehousingCommodity.setNO(Integer.parseInt(no[i]));
	// warehousingCommodity.setPackageUnitID(commodity.getPackageUnitID());
	// warehousingCommodity.setPrice(Integer.parseInt(price[i]));
	// warehousingCommodity.setAmount(Integer.parseInt(no[i]) *
	// Integer.parseInt(price[i]));
	// warehousingCommodity.setShelfLife(commodity.getShelfLife());
	//
	// BaseModel bm =
	// warehousingCommodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, warehousingCommodity);
	// if (bm != null && warehousingCommodityBO.getLastErrorCode() ==
	// EnumErrorCode.EC_NoError) {// ...尝试一下，在SP里直接返回错误码3，然后看看这时bm是不是null
	// CacheManager.getCache(EnumCacheType.ECT_WarehousingCommodity).write1(bm);
	// }
	// logger.info("Create WarehousingCommodity error code=" +
	// warehousingCommodityBO.getLastErrorCode());
	// }
	// // }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Map<String, Object> params = new HashMap<String, Object>();
	//
	// switch (warehousingCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// default:
	// // "", "未知错误");
	// params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// }

	// http://localhost:8080/nbr/warehousingCommodity/retrieveN.bx
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveN", method = RequestMethod.POST)
	public void retrieveN(@ModelAttribute("SpringWeb") WarehousingCommodity warehousingCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}
		
		logger.info("查询多个入库单商品，warehousingCommodity=" + warehousingCommodity);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		List<WarehousingCommodity> ls = (List<WarehousingCommodity>) warehousingCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehousingCommodity);

		logger.info("Retrieve WarehousingCommodity error code=" + warehousingCommodityBO.getLastErrorCode());

		switch (warehousingCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info(ls);
			break;
		default:
			break;
		}
	}

//  不用session，前端也未用此接口，接口废弃
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/update", method = RequestMethod.GET)
//	public String updateEx(@ModelAttribute("SpringWeb") WarehousingCommodity warehousingCommodity, ModelMap model, HttpServletRequest req) {
//		HttpSession session = req.getSession();
//
//		List<WarehousingCommodity> ls = (List<WarehousingCommodity>) session.getAttribute(EnumSession.SESSION_CommodityList.getName());
//		logger.info("List in session = " + ls);
//
//		String sCommIDs = GetStringFromRequest(req, "commID", String.valueOf(BaseAction.INVALID_ID)).trim();
//		if (sCommIDs.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0) {
//			logger.info("参数异常");
//			return ""; // ...
//		} else {
//			logger.info("sCommIDs=" + sCommIDs);
//		}
//
//		String sPrice = GetStringFromRequest(req, "price", String.valueOf(BaseAction.INVALID_ID)).trim();
//		if (sPrice.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0) {
//			logger.info("参数异常");
//			return ""; // ...
//		} else {
//			logger.info("sPrice=" + sPrice);
//		}
//
//		String sAmount = GetStringFromRequest(req, "amount", String.valueOf(BaseAction.INVALID_ID)).trim();
//		if (sAmount.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0) {
//			logger.info("参数异常");
//			return ""; // ...
//		} else {
//			logger.info("sAmount=" + sAmount);
//		}
//
//		try {
//			if (Integer.parseInt(sCommIDs.trim()) >= 0) {
//				for (int i = 0; i < ls.size(); i++) {
//					int newCommID = Integer.parseInt(sCommIDs.trim());
//					if (ls.get(i).getCommodityID() == newCommID) {
//						ls.get(i).setPrice(Float.parseFloat(sPrice));
//						ls.get(i).setAmount(Float.parseFloat(sAmount));
//						break;
//					}
//				}
//				logger.info("ls=" + ls.toString());
//				session.setAttribute(EnumSession.SESSION_CommodityList.getName(), ls);
//
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("commodityList", ls);
//				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//				logger.info("返回的数据=" + params);
//
//				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return "";
//	}

	/*
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/warehousingCommodity/appendToCommodityListEx.bx
	 */
//	@RequestMapping(value = "/appendToCommodityListEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	public String appendToCommodityListEx(@ModelAttribute("SpringWeb") WarehousingCommodity warehousingCommodity, ModelMap model, HttpServletRequest req) {
//		logger.info("添加商品到入库单商品的session中，warehousingCommodity=" + warehousingCommodity.toString());
//
//		HttpSession session = req.getSession();
//
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//
//		// 商品IDs
//		String commIDList = GetStringFromRequest(req, "commID", String.valueOf(INVALID_ID)).trim(); // Format: 1,2,3
//		String barcodeIDList = GetStringFromRequest(req, "barcodeIDs", String.valueOf(INVALID_ID)).trim();
//		String NOs = GetStringFromRequest(req, "NOs", String.valueOf(INVALID_ID)).trim();
//		String packageUnitIDs = GetStringFromRequest(req, "packageUnitIDs", String.valueOf(INVALID_ID)).trim();
//		String priceSuggestions = GetStringFromRequest(req, "priceSuggestions", String.valueOf(INVALID_ID)).trim();
//		String commNames = GetStringFromRequest(req, "commNames", String.valueOf(INVALID_ID)).trim();
//		if (commIDList.equals(String.valueOf(INVALID_ID)) || barcodeIDList.equals(String.valueOf(INVALID_ID)) || NOs.equals(String.valueOf(INVALID_ID)) || commNames.equals(String.valueOf(INVALID_ID))
//				|| packageUnitIDs.equals(String.valueOf(INVALID_ID)) || priceSuggestions.equals(String.valueOf(INVALID_ID))) {
//			logger.info("参数异常");
//			return ""; // ...
//		} else {
//			logger.info("commIDList=" + commIDList + ",barcodeIDList=" + barcodeIDList + ",NOs=" + NOs + ",packageUnitIDs=" + packageUnitIDs + ",priceSuggestions=" + priceSuggestions + ",commNames=" + commNames);
//		}
//
//		Integer[] iArrCommID = GeneralUtil.toIntArray(commIDList);
//		Integer[] iArrBarcodeID = GeneralUtil.toIntArray(barcodeIDList);
//		Integer[] iArrNO = GeneralUtil.toIntArray(NOs);
//		Integer[] iArrPackageUnitIDs = GeneralUtil.toIntArray(packageUnitIDs);
//		Double[] iArrPriceSuggestions = GeneralUtil.toDoubleArray(priceSuggestions);
//		String[] iArrCommNames = commNames.split(",");
//		if (iArrCommID == null || iArrBarcodeID == null || iArrNO == null || iArrPackageUnitIDs == null || iArrPriceSuggestions == null || iArrCommNames == null || iArrCommID.length != iArrBarcodeID.length
//				|| iArrCommID.length != iArrNO.length || iArrCommID.length != iArrPackageUnitIDs.length || iArrCommID.length != iArrPriceSuggestions.length || iArrCommID.length != iArrCommNames.length) {
//			logger.info("参数为空或者长度不一致");
//			return ""; // ...
//		} else {
//			logger.info("iArrCommID=" + iArrCommID + ",iArrBarcodeID=" + iArrBarcodeID + ",iArrNO=" + iArrNO + ",iArrPackageUnitIDs=" + iArrPackageUnitIDs + ",iArrPriceSuggestions=" + iArrPriceSuggestions + //
//					",iArrCommNames=" + iArrCommNames);
//		}
//
//		List<Commodity> listExistingCommodity = new ArrayList<Commodity>();
//		for (int ID : iArrCommID) {
//			appendCommodity(ID, listExistingCommodity, dbName, session);
//		}
//
//		Warehousing ws = (Warehousing) session.getAttribute(EnumSession.SESSION_Warehousing.getName());
//		if (ws == null) {
//			logger.info("获取到的入库单的session为空");
//			return ""; // ...TODO
//		} else {
//			logger.info("获取到的入库单的session=" + ws.toString());
//		}
//
//		List<WarehousingCommodity> wcCommList = new ArrayList<WarehousingCommodity>();
//		for (int i = 0; i < iArrCommID.length; i++) {
//			appendCommodity(ws.getID(), iArrCommID[i], iArrBarcodeID[i], iArrNO[i], iArrPackageUnitIDs[i], iArrPriceSuggestions[i], iArrCommNames[i], wcCommList);
//		}
//
//		// 修改Session里的采购商品前，先清除原先的采购商品
//		session.removeAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName());
//		session.setAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName(), wcCommList);
//		logger.info("Session SESSION_WarehousingTempWarehousingCommodityList updated to :" + wcCommList);
//		// 修改Session里的商品详情前，先清除原先的商品详情
//		session.removeAttribute(EnumSession.SESSION_WarehousingCommodityList.getName());
//		session.setAttribute(EnumSession.SESSION_WarehousingCommodityList.getName(), listExistingCommodity);
//		logger.info("Session SESSION_WarehousingCommodityList updated to :" + listExistingCommodity);
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("wcCommList", wcCommList);
//		params.put("commList", listExistingCommodity);
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}

//	private EnumErrorCode appendCommodity(int warehousingID, int commID, int barcodeID, int NO, int packageUnitID, double price, String commName, List<WarehousingCommodity> list) {
//		logger.info("添加入库单商品，list=" + list.toString());
//
//		WarehousingCommodity wc = new WarehousingCommodity();
//		wc.setCommodityID(commID);
//		wc.setWarehousingID(warehousingID);
//		wc.setBarcodeID(barcodeID);
//		wc.setNO(NO);
//		wc.setPackageUnitID(packageUnitID);
//		wc.setPrice(price);
//		wc.setCommodityName(commName);
//		list.add(wc);
//
//		return EnumErrorCode.EC_NoError;
//	}

	/*
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/warehousingCommodity/appendToCommodityEx.bx?commodityID=2&barcodeID=1&commodityNO=10&packageUnitID=1&priceSuggestion=1.1&commodityName=薯片
	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/appendToCommodityEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
//	@ResponseBody
//	public String appendToCommodityEx(@ModelAttribute("SpringWeb") WarehousingCommodity warehousingCommodity, ModelMap model, HttpServletRequest req) {
//		HttpSession session = req.getSession();
//
//		Warehousing ws = (Warehousing) session.getAttribute(EnumSession.SESSION_Warehousing.getName());
//		if (ws == null) {
//			logger.info("获取到的入库单的session为空");
//			return ""; // ...TODO
//		} else {
//			logger.info("获取到的入库单的session=" + ws.toString());
//		}
//
//		List<WarehousingCommodity> warehousingTempWarehousingCommodityList = (List<WarehousingCommodity>) session.getAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName());
//		if (warehousingTempWarehousingCommodityList == null) {
//			logger.info("获取到的入库单商品的session为空");
//			return "";
//		} else {
//			logger.info("获取到的入库单商品的session=" + warehousingTempWarehousingCommodityList.toString());
//		}
//
//		warehousingCommodity.setWarehousingID(ws.getID());
//		warehousingTempWarehousingCommodityList.add(warehousingCommodity);
//
//		session.setAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName(), warehousingTempWarehousingCommodityList);
//		logger.info("Session SESSION_WarehousingTempWarehousingCommodityList updated to :" + warehousingTempWarehousingCommodityList);
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("wcCommList", warehousingTempWarehousingCommodityList);
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}

	/*
	 * Request Body in Fiddler: GET URL:
	 * http://localhost:8080/nbr/warehousingCommodity/updateNOPriceUnitEx.bx?ID=1&no=10&price=16.6&amount=166
	 */
//	@SuppressWarnings({ "unchecked" })
//	@RequestMapping(value = "/updateNOPriceUnitEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	public String updateNOPriceUnitEx(@ModelAttribute("SpringWeb") WarehousingCommodity warehousingCommodity, ModelMap model, HttpServletRequest req) {
//		logger.info("从session中修改入库单单价数量总额");
//
//		HttpSession session = req.getSession();
//
//		List<WarehousingCommodity> wcList = (List<WarehousingCommodity>) session.getAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName());
//		logger.info("List in session:" + wcList);
//
//		try {
//			if (warehousingCommodity.getCommodityID() >= 0 && warehousingCommodity.getPrice() >= 0 && warehousingCommodity.getNO() >= 1 && warehousingCommodity.getAmount() >= 0) {
//				for (int i = 0; i < wcList.size(); i++) {
//					if (wcList.get(i).getCommodityID() == warehousingCommodity.getCommodityID()) {
//						wcList.get(i).setNO(warehousingCommodity.getNO());
//						wcList.get(i).setPrice(warehousingCommodity.getPrice());
//						wcList.get(i).setAmount(warehousingCommodity.getAmount());
//						break;
//					}
//				}
//
//				logger.info("List to update session:" + wcList);
//				session.setAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName(), wcList);
//
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName(), wcList);
//				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//
//				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//			} else {
//				logger.info("修改的参数格式不正确！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return ""; // ...
//	}
}
