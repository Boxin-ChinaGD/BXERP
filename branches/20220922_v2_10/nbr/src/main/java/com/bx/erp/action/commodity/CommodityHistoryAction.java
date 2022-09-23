package com.bx.erp.action.commodity;

import java.io.IOException;
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

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.commodity.CommodityHistoryField;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/commodityHistory")
@Scope("prototype")
public class CommodityHistoryAction extends BaseAction {
	private Log logger = LogFactory.getLog(CommodityHistoryAction.class);

	@Resource
	protected CommodityHistoryBO commodityHistoryBO;

	@Resource
	private StaffBO staffBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	/** R1时，代表已经删除的商品也可以查询到 */
	public static final int RETRIEVE_DELETED_COMMODITY = 1;
	/** R1时，代表已经删除的商品不可以查询到 */
	public static final int RETRIEVE_NO_DELETED_COMMODITY = 0;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body
	 * inFiddler:date1=2018/01/01&date2=2020/01/01
	 * URL:http://localhost:8080/nbr/commodityHistory.bx
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(@ModelAttribute("SpringWeb") CommodityHistory commodityHistory, ModelMap mm, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入商品历史页面的时候读取所有商品历史，commodityHistory=" + commodityHistory);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Staff s = new Staff();
		s.setPageSize(PAGE_SIZE_MAX); // TODO 获取所有员工信息页面显示
		DataSourceContextHolder.setDbName(dbName);
		/*
		 * List<Staff> staffList = (List<Staff>)
		 * staffBO.retrieveNObject(getStaffFromSession(session).getID(),
		 * BaseBO.INVALID_CASE_ID, s);
		 */
		List<BaseModel> staffList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).readN(false, false); // 从缓存中拿到所有员工

		if (staffList != null) {
			for (int i = 0; i < staffList.size(); i++) {
				Staff staff = (Staff) staffList.get(i);
				staff.clearSensitiveInfo();
			}
		}
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店

		mm.put("staffList", staffList);
		mm.put("shopList", shopList);
		mm.put("CommodityHistoryField", new CommodityHistoryField());
		mm.put("CommodityField", new CommodityField());
		return COMMODITYHISTORY_Index;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in
	 * Fiddler:date1=2018/01/01&date2=2020/01/01&string1=可乐 URL:
	 * http://localhost:8080/nbr/commodityHistory/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") CommodityHistory commodityHistory, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取部分商品历史,commodityHistory=" + commodityHistory);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<>();
		do {
			String queryKeyword = GetStringFromRequest(req, CommodityHistory.field.getFIELD_NAME_queryKeyword(), null);
			//
			commodityHistory.setQueryKeyword(queryKeyword);
			DataSourceContextHolder.setDbName(dbName);
			List<CommodityHistory> ls = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, commodityHistory);
			logger.info("retrieve N commodityHistory error code=" + commodityHistoryBO.getLastErrorCode() + ",ls=" + ls);
			if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取多个商品历史失败：" + commodityHistoryBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, commodityHistoryBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, commodityHistoryBO.getLastErrorMessage());
				break;
			}
			logger.info("读取多个商品历史成功，ls=" + ls);

			boolean hasDBError = false;
			for (CommodityHistory ch : ls) {
				ErrorInfo ecOut = new ErrorInfo();
				Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(ch.getStaffID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("读取一个员工失败，错误码=" + ecOut.getErrorCode().toString());
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
					hasDBError = true;
					break;
				}
				logger.info("读取一个员工成功，staff=" + staff);
				ch.setStaffName(staff == null ? "" : staff.getName());

				Commodity c = new Commodity();
				c.setID(ch.getCommodityID());
				c.setIncludeDeleted(RETRIEVE_DELETED_COMMODITY);
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, c);
				if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("商品查询失败：" + commodityBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
					hasDBError = true;
					break;
				}
				Commodity comm = Commodity.fetchCommodityFromResultSet(commR1);
				logger.info("商品查询成功，comm=" + comm);

				ch.setCommodityName(comm == null ? "" : comm.getName());

				Barcodes b = new Barcodes();
				b.setCommodityID(ch.getCommodityID());
				DataSourceContextHolder.setDbName(dbName);
				List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, b);
				logger.info("Retrieve N Barcodes error code=" + barcodesBO.getLastErrorCode() + ",barcodeList=" + barcodeList);
				if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("条形码查询失败：" + barcodesBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
					hasDBError = true;
					break;
				}
				logger.info("条形码查询成功，barcodeList=" + barcodeList);

				String barcodes = "";
				for (Barcodes bc : barcodeList) {
					barcodes += bc.getBarcode() + " ";
				}
				ch.setBarcodes(barcodes);
			}
			if (hasDBError) {
				break;
			}

			params.put(BaseAction.KEY_ObjectList, ls);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, commodityHistoryBO.getTotalRecord());
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
