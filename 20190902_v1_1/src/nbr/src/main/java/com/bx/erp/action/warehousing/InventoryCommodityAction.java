package com.bx.erp.action.warehousing;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.warehousing.InventoryCommodityBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

// ...整个类都是事务，需要讨论
//@Transactional
@Controller
@RequestMapping("/inventoryCommodity")
@Scope("prototype")
public class InventoryCommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(InventoryCommodityAction.class);

	@Resource
	private InventoryCommodityBO inventoryCommodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private PackageUnitBO packageUnitBO;

	/*
	 * Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/inventoryCommodity/createEx.bx?inventorySheetID
	 * =8&commodityID=12&noReal=199&noSystem=1212
	 */
	@RequestMapping(value = "/createEx", method = RequestMethod.GET)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") InventoryCommodity inventoryCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一个盘点单商品表，inventoryCommodity=" + inventoryCommodity);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		BaseModel bm = inventoryCommodityBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, inventoryCommodity);

		logger.info("Create inventoryCommodity error code=" + inventoryCommodityBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(false);
		switch (inventoryCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建成功，对象为：" + bm);
			params.put("inventoryCommodity", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_Duplicated:
			logger.info("重复创建相同的盘点单商品");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("不能添加组合商品到盘点单");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, inventoryCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	//
	// /*
	// * Request Body in Fiddler: inventorySheetID=1&commodityID=1&noReal=300
	// * URL:http://localhost:8080/nbr/inventoryCommodity/updateCommodity.bx
	// */
	// @RequestMapping(value = "/updateCommodity", method = RequestMethod.GET)
	// public String updateCommodity(@ModelAttribute("SpringWeb") InventoryCommodity
	// inventoryCommodity, ModelMap model, HttpSession session) {
	// inventoryCommodity = (InventoryCommodity)
	// inventoryCommodityBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, inventoryCommodity);
	//
	// logger.info("update Commodity error code=" +
	// inventoryCommodityBO.getLastErrorCode());
	//
	// switch (inventoryCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_InventorySheet).write1(inventoryCommodity);
	//
	// logger.info("更新后的盘点商品：" + inventoryCommodity);
	// session.setAttribute("list", inventoryCommodity);
	//
	// return INVENTORYCOMM_Update;
	// default:
	// // "", "未知错误！");
	// break;
	// }
	// return INVENTORYCOMM_ToUpdate;
	//
	// }

	// /*
	// * Request Body in Fiddler: inventorySheetID=1 URL:
	// * http://localhost:8080/nbr/inventoryCommodity/list.bx
	// */
	// @SuppressWarnings({ "unchecked", "unused" })
	// @RequestMapping(value = "/list", method = RequestMethod.POST)
	// public String list(@ModelAttribute("SpringWeb") InventoryCommodity
	// inventoryCommodity, ModelMap model) {
	// inventoryCommodity.setPageIndex(0);
	// List<?> list =
	// inventoryCommodityBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, inventoryCommodity);
	//
	// logger.info("list Commodity error code=" +
	// inventoryCommodityBO.getLastErrorCode());
	//
	// switch (inventoryCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// List<Commodity> commoditylist = (List<Commodity>) list;
	// // put("commoditylist", commoditylist);
	// logger.info("此盘点单里面的商品有：" + list);
	// return INVENTORYCOMM_RetrieveN; // 。。。
	// default:
	// // "", "未知错误！");
	// break;
	// }
	// return ""; // 。。。
	// }

	// /*
	// * Request Body in Fiddler: isID=3&commID=2 URL:
	// * http://localhost:8080/nbr/inventoryCommodity/updateCommodityEx.bx
	// */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/updateCommodityEx", method = RequestMethod.POST)
	// @ResponseBody
	// public String updateCommodityEx(@ModelAttribute("SpringWeb")
	// InventoryCommodity inventoryCommodity, ModelMap model, HttpSession session)
	// throws IOException {
	// // List<InventorySheet> listExistingSheet = (List<InventorySheet>)
	// // session.get(SESSION_SHEET);
	// InventorySheet ExistingSheet = (InventorySheet)
	// session.getAttribute(SESSION_Sheet);
	// if (ExistingSheet == null) {
	// return "";
	// }
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// boolean bHasUpdateError = false;
	// // 如果商品的session为空，则只删除当前盘点单的所有商品。否则就删除所有商品后，添加session里面的商品。
	// if (session.getAttribute(SESSION_InventoryCommodity) == null) {
	// inventoryCommodity.setInventorySheetID(ExistingSheet.getID());
	// inventoryCommodity.setCommodityID(-1);
	// inventoryCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, inventoryCommodity);
	// logger.info(inventoryCommodityBO.getLastErrorCode());
	// switch (inventoryCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// default:
	// logger.info("delete 失败！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// } else {
	// List<InventoryCommodity> listExistingCommodity = (List<InventoryCommodity>)
	// session.getAttribute(SESSION_InventoryCommodity);
	// inventoryCommodity.setInventorySheetID(ExistingSheet.getID());
	// inventoryCommodity.setCommodityID(-1);
	// inventoryCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, inventoryCommodity);
	// logger.info(inventoryCommodityBO.getLastErrorCode());
	// switch (inventoryCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// for (InventoryCommodity ic : listExistingCommodity) {
	// inventoryCommodity = (InventoryCommodity)
	// inventoryCommodityBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, ic);// 有可能会出现第一个update成功，第二个update失败的情况
	// logger.info(inventoryCommodityBO.getLastErrorCode());
	// switch (inventoryCommodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// CacheManager.getCache(dbName,EnumCacheType.ECT_InventoryCommodity).write1(ic);
	//
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("update 失败！");
	// bHasUpdateError = true;
	// break;
	// }
	// if (bHasUpdateError) {
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// throw new RuntimeException("数据库操作异常");
	// }
	// }
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());//
	// ... What if bHasUpdateError == true???
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// default:
	// logger.info("delete 失败！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// }
	//
	// // return ""; // ...
	// }

	// /*
	// * Request Body in Fiddler:
	// *
	// URL:http://localhost:8080/nbr/inventoryCommodity/prepareEx.bx?isID=2&commID=3
	// * ,4,5
	// */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/prepareEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String prepareCommodityListEx(@ModelAttribute("SpringWeb")
	// InventoryCommodity inventoryCommodity, ModelMap model, HttpServletRequest
	// req) {
	// HttpSession session = req.getSession();
	//
	// if (GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim().equals("-1") ||
	// GetStringFromRequest(req, "isID",
	// String.valueOf(BaseAction.INVALID_ID)).trim().equals("-1")) {
	// return ""; // ...
	// }
	// String sCommIDs = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// String sISID = GetStringFromRequest(req, "isID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// logger.info("sCommIDs=" + sCommIDs + "\tsISID=" + sISID);
	//
	// String[] sarrCommID = sCommIDs.split(",");
	//
	// try {
	// int isID = Integer.parseInt(sISID);
	//
	// List<InventoryCommodity> listExistingCommodity = (List<InventoryCommodity>)
	// session.getAttribute(SESSION_InventoryCommodity);
	// logger.info("List in session:" + listExistingCommodity);
	//
	// List<InventoryCommodity> listToAppend = new ArrayList<InventoryCommodity>();
	// for (String sID : sarrCommID) {
	// boolean bIsInList = false;
	// for (InventoryCommodity ic : listExistingCommodity) {
	// if (Integer.parseInt(sID.trim()) == ic.getCommodityID()) {
	// bIsInList = true;
	// break;
	// }
	// }
	// if (!bIsInList) {
	// InventoryCommodity icNew = new InventoryCommodity();
	// int commID = Integer.parseInt(sID.trim());
	// icNew.setCommodityID(commID);
	// icNew.setNoReal(INVALID_NO);
	// icNew.setInventorySheetID(isID);
	// listToAppend.add(icNew);
	// }
	// }
	// logger.info("List to append to existing session:" + listToAppend);
	//
	// for (InventoryCommodity ic : listToAppend) {
	// listExistingCommodity.add(ic);
	// }
	//
	// session.setAttribute(SESSION_InventoryCommodity, listExistingCommodity);
	//
	// Map<String, Object> params = getDefaultParamToReturn(false);
	// params.put("incList", listExistingCommodity);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } catch (NumberFormatException e) {
	// e.printStackTrace();
	// }
	//
	// return "";// ...
	// }

	// /*
	// * Request Body in Fiddler:
	// *
	// URL:http://localhost:8080/nbr/inventoryCommodity/reduceCommodityEx.bx?commID=
	// * 5
	// */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/reduceEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String reduceCommodityEx(@ModelAttribute("SpringWeb")
	// InventoryCommodity inventoryCommodity, ModelMap model, HttpServletRequest
	// req) {
	// HttpSession session = req.getSession();
	// logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() +
	// "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
	//
	// if (GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim().equals("-1")) {
	// return ""; // ...
	// }
	// Map<String, Object> params = getDefaultParamToReturn(false);
	// String sCommIDs = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// try {
	// List<InventoryCommodity> listExistingCommodity = (List<InventoryCommodity>)
	// session.getAttribute(SESSION_InventoryCommodity);
	// logger.info("List in session:" + listExistingCommodity);
	//
	// for (int i = 0; i < listExistingCommodity.size(); i++) {
	// int newCommID = Integer.parseInt(sCommIDs.trim());
	// if (listExistingCommodity.get(i).getCommodityID() == newCommID) {
	// listExistingCommodity.remove(i);
	// break;
	// }
	// }
	// logger.info("List to delete session:" + listExistingCommodity);
	// session.setAttribute(SESSION_InventoryCommodity, listExistingCommodity);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// params.put("incList", listExistingCommodity);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return ""; // ...
	// }

	/*
	 * Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/inventoryCommodity/updateNOEx.bx?
	 * commID=5&realNO=200
	 */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/updateNOEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String updateCommodityNOEx(@ModelAttribute("SpringWeb")
	// InventoryCommodity inventoryCommodity, ModelMap model, HttpServletRequest
	// req) {
	// logger.info("修改盘点单商品的商品的数量，inventoryCommodity=" +
	// inventoryCommodity.toString());
	//
	// HttpSession session = req.getSession();
	// logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() +
	// "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
	//
	// String sRealNO = GetStringFromRequest(req, "realNO",
	// String.valueOf(BaseAction.INVALID_ID));
	// String scommID = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID));
	// if (sRealNO == null || Integer.valueOf(sRealNO) < -1 || scommID == null ||
	// Integer.valueOf(scommID) <= 0) {// ...定义一个本类的常量=-1，代表无效的实盘数量
	// logger.info("参数缺失");
	// return "";
	// } else {
	// logger.info("sRealNO=" + sRealNO + ",scommID=" + scommID);
	// }
	//
	// List<InventoryCommodity> listExistingCommodity = (List<InventoryCommodity>)
	// session.getAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName());
	// if (listExistingCommodity == null || listExistingCommodity.size() == 0) {
	// logger.info("listExistingCommodity为空");
	// return "";
	// } else {
	// logger.info("List in session:" + listExistingCommodity);
	// }
	//
	// Map<String, Object> params = getDefaultParamToReturn(false);
	// try {
	// if (Integer.valueOf(sRealNO.trim()) >= 0) {
	// for (int i = 0; i < listExistingCommodity.size(); i++) {
	// int newCommID = Integer.valueOf(scommID);
	// if (listExistingCommodity.get(i).getCommodityID() == newCommID) {
	// listExistingCommodity.get(i).setNoReal(Integer.parseInt(sRealNO.trim()));
	// break;
	// }
	// }
	// session.setAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName(),
	// listExistingCommodity);
	// logger.info("List to update session:" + listExistingCommodity);
	//
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.info("参数缺失");
	// return ""; // ...
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return ""; // ...
	// }

	/*与前端沟通，接口已废弃
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/inventoryCommodity/retrieveNEx.bx?
	 * InventorySheetID=2
	 */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/retrieveNEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String retrieveNEx(@ModelAttribute("SpringWeb") InventoryCommodity
	// inventoryCommodity, ModelMap mm, HttpServletRequest req) {
	// logger.info("读取多个盘点单商品，inventoryCommodity=" + inventoryCommodity.toString());
	//
	// HttpSession session = req.getSession();
	// logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() +
	// "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
	// Company company = getCompanyFromSession(session);
	// String dbName = company.getDbName();
	//
	// List<InventorySheetInfo> listInventorySheet = new
	// ArrayList<InventorySheetInfo>();
	//
	// Map<String, Object> params = getDefaultParamToReturn(false);
	// do {
	// List<InventoryCommodity> icList = (List<InventoryCommodity>)
	// session.getAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName());
	// if (icList == null) {
	// logger.info("session缺失");
	// return "";
	// } else {
	// logger.info("icList=" + icList.toString());
	// }
	//
	// // 实现session的分页功能
	// List<InventoryCommodity> inventoryCommodityList = new
	// ArrayList<InventoryCommodity>();
	// int iPageIndex = inventoryCommodity.getPageIndex() - 1;
	// int iPageSize = inventoryCommodity.getPageSize();
	// int recordIndex = iPageIndex * iPageSize;
	// if (recordIndex <= icList.size()) {
	// for (int i = recordIndex; i < recordIndex + iPageSize; i++) {
	// inventoryCommodityList.add(icList.get(i));
	//
	// if (icList.size() == i + 1) {
	// break;
	// }
	// }
	// }
	//
	// ErrorInfo ecOut = new ErrorInfo();
	// Barcodes b = new Barcodes();
	// PackageUnit pu = new PackageUnit();
	// for (InventoryCommodity icTmp : inventoryCommodityList) {
	// Commodity comm = (Commodity) CacheManager.getCache(dbName,
	// EnumCacheType.ECT_Commodity).read1(((InventoryCommodity)
	// icTmp).getCommodityID(), getStaffFromSession(req.getSession()).getID(),
	// ecOut, dbName);
	// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询一个商品失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" +
	// ecOut.getErrorMessage());
	// // throw new RuntimeException("商品读取失败！即将回滚DB...");
	// params.put(KEY_HTMLTable_Parameter_msg, "查询一个商品失败，错误原因：" +
	// ecOut.getErrorMessage());
	// params.put(JSON_ERROR_KEY, ecOut.getErrorCode());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.info("查询成功！comm=" + (comm == null ? "NULL" : comm.toString()));
	// }
	//
	// b.setCommodityID(comm.getID());
	// DataSourceContextHolder.setDbName(dbName);
	// List<Barcodes> barcodeList = (List<Barcodes>)
	// barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, b);
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("读取多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
	// // throw new RuntimeException("条形码读取失败！即将回滚DB...");
	// params.put(KEY_HTMLTable_Parameter_msg, "查询条形码失败，错误原因：" +
	// barcodesBO.getLastErrorMessage());
	// params.put(JSON_ERROR_KEY, barcodesBO.getLastErrorCode());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.info("barcodeList=" + barcodeList.toString());
	// }
	//
	// String barcodes = "";
	// for (Barcodes bc : barcodeList) {
	// barcodes += bc.getBarcode() + " ";
	// }
	// comm.setString1(Commodity.DEFINE_SET_Barcodes(barcodes));
	//
	// pu.setID(comm.getPackageUnitID());
	// DataSourceContextHolder.setDbName(dbName);
	// PackageUnit packageUnit = (PackageUnit)
	// packageUnitBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, pu);
	// if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询一个包装单位失败，错误码=" + packageUnitBO.getLastErrorCode());
	// // throw new RuntimeException("包装单位读取失败！即将回滚DB...");
	// params.put(KEY_HTMLTable_Parameter_msg, "查询包装单位失败，错误原因：" +
	// packageUnitBO.getLastErrorMessage());
	// params.put(JSON_ERROR_KEY, packageUnitBO.getLastErrorCode());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// } else {
	// logger.info("查询成功！packageUnit=" + (packageUnit == null ? "NULL" :
	// packageUnit.toString()));
	// }
	//
	// //
	// comm.setString2(Commodity.DEFINE_SET_PackageUnitName(packageUnit.getName()));
	//
	// InventorySheetInfo isInfo = new InventorySheetInfo();
	// isInfo.setCommodity(comm);
	// isInfo.setInventoryCommodity((InventoryCommodity) icTmp);
	// listInventorySheet.add(isInfo);
	// }
	//
	// logger.info("listInventorySheet=" + listInventorySheet.toString());
	// params.put(KEY_Object, listInventorySheet);
	// // ？？？该接口都没有使用到inventoryCommodityBO,
	// // params.put("count", inventoryCommodityBO.getTotalRecord());
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// params.put(KEY_HTMLTable_Parameter_code, "0");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// } while (false);
	// // params.put(KEY_HTMLTable_Parameter_msg,
	// // inventoryCommodityBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/inventoryCommodity/appendToCommodityEx.bx?
	 * commodityID=2&barcodeID=1&packageUnitID=1&commodityName=薯片
	 */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/appendToCommodityEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.POST)
	// @ResponseBody
	// public String appendToCommodityEx(@ModelAttribute("SpringWeb")
	// InventoryCommodity inventoryCommodity, ModelMap model, HttpServletRequest
	// req) {
	// HttpSession session = req.getSession();
	//
	// InventorySheet is = (InventorySheet)
	// session.getAttribute(EnumSession.SESSION_InventorySheet.getName());
	// if (is == null) {
	// logger.info("盘点单的session缺失");
	// return ""; // ...TODO
	// } else {
	// logger.info("盘点单的session=" + is.toString());
	// }
	//
	// List<InventoryCommodity> icTempWarehousingCommodityList =
	// (List<InventoryCommodity>)
	// session.getAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName());
	// if (icTempWarehousingCommodityList == null) {
	// logger.info("盘点单商品的session缺失");
	// return "";
	// } else {
	// logger.info("盘点单商品的session=" + icTempWarehousingCommodityList.toString());
	// }
	//
	// inventoryCommodity.setInventorySheetID(is.getID());
	// icTempWarehousingCommodityList.add(inventoryCommodity);
	//
	// session.setAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName(),
	// icTempWarehousingCommodityList);
	// logger.info("Session SESSION_InventorySheetTempInventoryCommodityList updated
	// to :" + icTempWarehousingCommodityList);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("icCommList", icTempWarehousingCommodityList);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// params.put(KEY_HTMLTable_Parameter_msg,
	// inventoryCommodityBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/inventoryCommodity/appendToCommodityListEx.bx
	 */
	// @RequestMapping(value = "/appendToCommodityListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String appendToCommodityListEx(@ModelAttribute("SpringWeb")
	// InventoryCommodity inventoryCommodity, ModelMap model, HttpServletRequest
	// req) {
	// logger.info("往盘点单商品表中添加商品，inventoryCommodity=" +
	// inventoryCommodity.toString());
	//
	// HttpSession session = req.getSession();
	// Company company = getCompanyFromSession(session);
	//
	// // 商品IDs
	// String commIDList = GetStringFromRequest(req, "commID",
	// String.valueOf(INVALID_ID)).trim(); // Format: 1,2,3
	// String barcodeIDList = GetStringFromRequest(req, "barcodeIDs",
	// String.valueOf(INVALID_ID)).trim();
	// String specifications = GetStringFromRequest(req, "specifications",
	// String.valueOf(INVALID_ID)).trim();
	// String packageUnitIDs = GetStringFromRequest(req, "packageUnitIDs",
	// String.valueOf(INVALID_ID)).trim();
	// String commNames = GetStringFromRequest(req, "commNames",
	// String.valueOf(INVALID_ID)).trim();
	// if (commIDList.equals(String.valueOf(INVALID_ID)) ||
	// barcodeIDList.equals(String.valueOf(INVALID_ID)) ||
	// specifications.equals(String.valueOf(INVALID_ID)) ||
	// commNames.equals(String.valueOf(INVALID_ID))
	// || packageUnitIDs.equals(String.valueOf(INVALID_ID))) {
	// logger.info("参数缺失");
	// return ""; // ...
	// } else {
	// logger.info("commIDList=" + commIDList + "，barcodeIDList=" + barcodeIDList +
	// ",specifications=" + specifications + ",commNames=" + commNames);
	// }
	//
	// Integer[] iArrCommID = GeneralUtil.toIntArray(commIDList);
	// Integer[] iArrBarcodeID = GeneralUtil.toIntArray(barcodeIDList);
	// Integer[] iArrPackageUnitIDs = GeneralUtil.toIntArray(packageUnitIDs);
	// String[] sArrCommNames = commNames.split(",");
	// String[] sArrSpecification = specifications.split(",");
	//
	// if (iArrCommID == null || iArrBarcodeID == null || iArrPackageUnitIDs == null
	// || sArrSpecification == null || sArrCommNames == null || iArrCommID.length !=
	// iArrBarcodeID.length || iArrCommID.length != iArrPackageUnitIDs.length
	// || iArrCommID.length != sArrSpecification.length || iArrCommID.length !=
	// sArrCommNames.length) {
	// logger.info("参数类型不匹配");
	// return ""; // ...
	// } else {
	// logger.info("iArrCommID=" + iArrCommID + "，iArrBarcodeID=" + iArrBarcodeID +
	// ",sArrCommNames=" + iArrPackageUnitIDs + ",iArrPackageUnitIDs=" +
	// sArrCommNames + ",sArrSpecification=" + sArrSpecification);
	// }
	//
	// List<Commodity> listExistingCommodity = new ArrayList<Commodity>();
	// for (int ID : iArrCommID) {
	// appendCommodity(ID, listExistingCommodity, company.getDbName(), session);
	// }
	//
	// InventorySheet is = (InventorySheet)
	// session.getAttribute(EnumSession.SESSION_InventorySheet.getName());
	// if (is == null) {
	// logger.info("盘点单商品的session缺失");
	// return ""; // ...TODO
	// } else {
	// logger.info("盘点单商品的session,is=" + is.toString());
	// }
	//
	// List<InventoryCommodity> icCommList = new ArrayList<InventoryCommodity>();
	// for (int i = 0; i < iArrCommID.length; i++) {
	// appendCommodity(is.getID(), iArrCommID[i], iArrBarcodeID[i],
	// iArrPackageUnitIDs[i], sArrSpecification[i], sArrCommNames[i], icCommList);
	// }
	//
	// // 修改Session里的采购商品前，先清除原先的采购商品
	// session.removeAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName());
	// session.setAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName(),
	// icCommList);
	// logger.info("Session SESSION_InventorySheetTempInventoryCommodityList updated
	// to :" + icCommList);
	// // 修改Session里的商品详情前，先清除原先的商品详情
	// session.removeAttribute(EnumSession.SESSION_InventoryCommodityList.getName());
	// session.setAttribute(EnumSession.SESSION_InventoryCommodityList.getName(),
	// listExistingCommodity);
	// logger.info("Session SESSION_InventoryCommodityList updated to :" +
	// listExistingCommodity);
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("icCommList", icCommList);
	// params.put(EnumSession.SESSION_CommList.getName(), listExistingCommodity);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// params.put(KEY_HTMLTable_Parameter_msg,
	// inventoryCommodityBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	//
	// protected EnumErrorCode appendCommodity(int inventorySheetID, int commID, int
	// barcodeID, int packageUnitID, String specification, String commName,
	// List<InventoryCommodity> list) {
	// InventoryCommodity ic = new InventoryCommodity();
	// ic.setCommodityID(commID);
	// ic.setInventorySheetID(inventorySheetID);
	// ic.setBarcodeID(barcodeID);
	// ic.setPackageUnitID(packageUnitID);
	// ic.setSpecification(specification);
	// ic.setCommodityName(commName);
	// list.add(ic);
	//
	// return EnumErrorCode.EC_NoError;
	// }

	/*
	 * Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/inventoryCommodity/updateNoRealEx.bx?ID=1&
	 * noReal=1&noSystem=1
	 */
	@RequestMapping(value = "/updateNoRealEx", method = RequestMethod.GET)
	@ResponseBody
	public String updateNoRealEx(@ModelAttribute("SpringWeb") InventoryCommodity inventoryCommodity, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改盘点单商品的实盘数量，inventoryCommodity=" + inventoryCommodity);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		BaseModel bm = inventoryCommodityBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_UpdateInventoryCommodityNoReal, inventoryCommodity);

		Map<String, Object> params = new HashMap<String, Object>();
		switch (inventoryCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("bm=" + bm);
			params.put("inventoryCommodity", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, inventoryCommodityBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
