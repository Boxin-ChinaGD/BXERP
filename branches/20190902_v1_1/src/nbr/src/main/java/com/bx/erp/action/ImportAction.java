package com.bx.erp.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.action.bo.VipCardCodeBO;
import com.bx.erp.action.bo.VipSourceBO;
import com.bx.erp.action.bo.commodity.BrandBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Vip;
import com.bx.erp.model.Vip.EnumVipInfoInExcel;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role;
import com.bx.erp.model.VipSource.EnumVipSourceCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumCommodityInfoInExcel;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.Provider.EnumProviderInfoInExcel;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.util.AppUtil;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.OkHttpUtil;
import com.bx.erp.util.PoiUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

/** ?????????????????????<br />
 * 1?????????????????????xlsx??????????????????HashMap????????????HashMap Parse1?????????Model????????????????????????????????????DB???????????? <br />
 */
@RequestMapping("import")
@Component("importAction")
@Scope("prototype")
public class ImportAction extends BaseAction {
	private Log logger = LogFactory.getLog(ImportAction.class);

	public static final String PATH_SaveMultipartFileToDestination = BaseAction.ImportCommodityAndVipDir + "%s/ImportCommodityAndVip.xlsm";
	private static final String PATH_SaveMultipartFileToDestinationDirectory = BaseAction.ImportCommodityAndVipDir + "%s";
	private static final String SHEET_NAME_Vip = "??????";
	private static final String SHEET_NAME_Commodity = "??????";
	private static final String SHEET_NAME_Provider = "?????????";

	public final static String TEMPLATE_FILE_NAME_ImportData = "???????????????????????????.xlsm";
	
	public static final String TOMCAT_MAP_DIR_DownloadCommodityAndVip = "/fe/";

	// ??????N?????????0??????????????????????????????????????????HashMap???Key????????????HashMap?????????Model???Parse1?????????Model????????????????????????????????????DB????????????
	//
	private static final String VIP_SHEET_RowIndex = "2";
	private static final String PROVIDER_SHEET_RowIndex = "2";
	private static final String COMMODITY_SHEET_RowIndex = "2";

	/** ???????????????????????????????????????????????? */
	private static final String DEFAULT_VALUE_IfCellEmpty = "NULL";

	List<Commodity> commodityListToCreate = new ArrayList<>();
	List<Vip> vipListToCreate = new ArrayList<>();
	List<Provider> providerListToCreate = new ArrayList<>();

	@Resource
	private BrandBO brandBO;

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private PackageUnitBO packageUnitBO;

	@Resource
	private ProviderBO providerBO;

	@Resource
	private VipBO vipBO;

	@Resource
	private VipSourceBO vipSourceBO;

	@Resource
	private VipCardCodeBO vipCardCodeBO;

	@Resource
	private ProviderDistrictBO providerDistrictBO;

	protected PoiUtils poiXls;
	
	/* ????????????????????? */
	private int vipTotalToCreate;
	
	private int commodityTotalToCreate;
	
	private int providerTotalToCreate;
	
	/* ????????????????????????????????????????????? */
	private int vipWrongFormatNumber;
	
	private int vipFailCreateNumber;
	
	private int commodityWrongFormatNumber;
	
	private int commodityFailCreateNumber;
	
	// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	private int providerWrongFormatNumber;
	
	/** ????????????needToDownload=1????????????????????????????????????????????????*/
	private int needToDownload = EnumBoolean.EB_NO.getIndex();
	public static final String KEY_needToDownload = "needToDownload";
	
	/** ???????????????key ????????????*/
	public static final String KEY_commodityTotalToCreate = "commodityTotalToCreate";
	public static final String KEY_vipTotalToCreate = "vipTotalToCreate";
	public static final String KEY_providerTotalToCreate = "providerTotalToCreate";
	//
	/** ???????????????key ????????????????????????????????????????????????vip???commodity???????????? */
	public static final String KEY_vipWrongFormatNumber = "vipWrongFormatNumber";
	public static final String KEY_commodityWrongFormatNumber = "commodityWrongFormatNumber";
	public static final String KEY_providerWrongFormatNumber = "providerWrongFormatNumber";
	//
	/** ???????????????key ??????????????????????????????????????????????????????????????????????????? */
	public static final String KEY_vipFailCreateNumber = "vipFailCreateNumber";
	public static final String KEY_commodityFailCreateNumber = "commodityFailCreateNumber";
	
	private List<Integer> vip_excelLineToDelete = new ArrayList<>();
	private List<Integer> commodity_excelLineToDelete = new ArrayList<>();
	
	// <key,??????>
	private HashMap<String, Integer> mapCheckDuplicateCommName = new HashMap<>();
	private HashMap<String, Integer> mapCheckDuplicateVipMobile = new HashMap<>();
	private HashMap<String, Integer> mapCheckDuplicateProviderName = new HashMap<>();
	private HashMap<String, Integer> mapCheckDuplicateProviderMobile = new HashMap<>();
	
	/** ???????????????excel??????????????????????????? */
	private boolean firstFindDuplicateProviderMobile = true;
	/** ???????????????excel??????????????????????????? */
	private boolean firstFindDuplicateProviderName = true;
	/** ???????????????excel???????????????????????? */
	private boolean firstFindDuplicateCommodityName = true;
	/** ???????????????excel??????????????????????????? */
	private boolean firstFindDuplicateVipMobile = true;
	
	@RequestMapping(value = "/importEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String importEx(HttpSession session, @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}

		long startTime = System.currentTimeMillis();

		Company company = getCompanyFromSession(session);
		Staff staff = getStaffFromSession(session);

		Map<String, Object> param = getDefaultParamToReturn(true);
		param.put("needToDownload", EnumBoolean.EB_NO.getIndex());
		// ???????????????????????????????????????????????????????????????????????????DB????????????????????????????????????????????????????????????--????????????????????????
		logger.info("???????????????");
		if (!checkFile(param, file)) {
			logger.info(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("??????????????????");
		HashMap<String, Integer> mapBrandName = new HashMap<>();
		if (!queryBrand(company.getDbName(), staff.getID(), param, mapBrandName)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("??????????????????");
		HashMap<String, Integer> mapCategory = new HashMap<>();
		if (!queryCategory(company.getDbName(), staff.getID(), param, mapCategory)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("??????????????????");
		HashMap<String, Integer> mapPackageUnit = new HashMap<>();
		if (!queryPackageUnit(company.getDbName(), staff.getID(), company, param, mapPackageUnit)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("???????????????");
		HashMap<String, Integer> mapProvider = new HashMap<>();
		if (!queryProvider(company.getDbName(), staff.getID(), company, param, mapProvider)) {
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("?????????????????????");
		HashMap<String, Integer> mapProviderDistrict = new HashMap<>();
		if (!queryProviderDisctrict(company.getDbName(), staff.getID(), company, param, mapProviderDistrict)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}

		logger.info("???MultipartFile?????????????????????");
		String direct = String.format(PATH_SaveMultipartFileToDestinationDirectory, company.getDbName());
		File destDirect = new File(direct);
		if  (!destDirect.exists() && !destDirect.isDirectory()) {       
		    System.out.println("???????????????, ????????????");  
		    destDirect.mkdir();    
		} else {  
		    System.out.println("????????????");  
		}
		String xlsxFilePath = String.format(PATH_SaveMultipartFileToDestination, company.getDbName());
		File destFile = new File(xlsxFilePath);
		if(!destFile.exists()) {
			destFile.createNewFile();
			System.out.println("??????????????????!");
		} else {
			System.out.println("???????????????!");
		}
		try {
			file.transferTo(destFile);
		} catch(Exception e) {
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			logger.error("?????????????????????" + e.getMessage());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		poiXls = new PoiUtils(xlsxFilePath);
		logger.info("??????excel???????????????");
		Map<String, Map<String, List<String>>> mapBaseModels = new HashMap<>();
		if (!loadExcelSheet(mapBaseModels, param, xlsxFilePath)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		// ??????????????????????????????????????????(??????????????????????????????????????????????????????????????????excel????????????????????????UUID?????? ?????? ?????????)
		logger.info("okhttp??????");
		OkHttpUtil.sessionID = "JSESSIONID=" + session.getId();
		logger.info("?????????????????????????????????");
		if (!parseProviderFromExcel(company.getDbName(), staff.getID(), param, mapProvider, mapBaseModels, mapProviderDistrict, xlsxFilePath)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		createProvider(company.getDbName(), staff.getID(), param, mapProvider, xlsxFilePath);
		logger.info("excel???????????????model");
		if (!parseCommodityFromExcel(company.getDbName(), staff.getID(), mapBrandName, mapCategory, mapBaseModels, mapPackageUnit, mapProvider, param, xlsxFilePath)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("excel???????????????model");
		if (!parseVipFromExcel(mapBaseModels, param, xlsxFilePath)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		// ???????????????????????????????????????
		param.put(KEY_commodityTotalToCreate, commodityTotalToCreate);
		param.put(KEY_vipTotalToCreate, vipTotalToCreate);
		param.put(KEY_providerTotalToCreate, providerTotalToCreate);
		if(vipWrongFormatNumber > 0 || commodityWrongFormatNumber > 0 || providerWrongFormatNumber > 0) {
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "?????????????????????????????????????????????????????????");
			param.put(KEY_vipWrongFormatNumber, vipWrongFormatNumber);
			param.put(KEY_commodityWrongFormatNumber, commodityWrongFormatNumber);
			param.put(KEY_providerWrongFormatNumber, providerWrongFormatNumber);
			needToDownload = EnumBoolean.EB_Yes.getIndex();
			param.put(KEY_needToDownload, needToDownload);
			logger.error("???????????????????????????????????????dbName=" + company.getDbName());
			logger.info("????????????????????????" + vipTotalToCreate);
			logger.info("???????????????????????????" + vipWrongFormatNumber);
			logger.info("????????????????????????" + commodityTotalToCreate);
			logger.info("???????????????????????????" + commodityWrongFormatNumber);
			logger.info("???????????????????????????" + providerTotalToCreate);
			logger.info("??????????????????????????????" + providerWrongFormatNumber);
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		} 
		//
		logger.info("????????????????????????");
		if(!createCommodityAndBarcodes(xlsxFilePath, param)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		logger.info("???????????????????????????????????????");
		if (!createVip(staff.getID(), company, param, xlsxFilePath)) {
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		param.put(KEY_needToDownload, needToDownload);
		//
		if(vipFailCreateNumber > 0 || commodityFailCreateNumber > 0) {
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "??????????????????????????????????????????????????????????????????");
			param.put(KEY_vipFailCreateNumber, vipFailCreateNumber);
			param.put(KEY_commodityFailCreateNumber, commodityFailCreateNumber);
			logger.error("????????????????????????????????????????????????dbName=" + company.getDbName());
			param.put(KEY_needToDownload, EnumBoolean.EB_Yes.getIndex());
			logger.info("????????????????????????" + vipTotalToCreate);
			logger.info("???????????????????????????" + vipFailCreateNumber);
			logger.info("????????????????????????" + commodityTotalToCreate);
			logger.info("???????????????????????????" + commodityFailCreateNumber);
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		} 
		//
		param.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
		param.put(KEY_HTMLTable_Parameter_msg, "");
		long endTime = System.currentTimeMillis();
		System.out.println("?????????" + (endTime - startTime) / 1000 + "???");
		logger.info(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());

		return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
	}

	private boolean queryProviderDisctrict(String dbName, int staffID, Company company, Map<String, Object> param, HashMap<String, Integer> providerDistrictSet) {
		ProviderDistrict providerDistrict = new ProviderDistrict();
		providerDistrict.setPageIndex(BaseAction.PAGE_StartIndex);
		providerDistrict.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> pdList = providerDistrictBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, providerDistrict);
		if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			param.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode());
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
			logger.error("??????????????????????????????" + providerDistrictBO.printErrorInfo());
			return false;
		}
		for (Object providerDistrictDB : pdList) {
			ProviderDistrict providerDistrictBm = (ProviderDistrict) providerDistrictDB;
			providerDistrictSet.put(providerDistrictBm.getName(), providerDistrictBm.getID());
		}
		return true;
	}

	private boolean loadExcelSheet(Map<String, Map<String, List<String>>> mapBaseModels, Map<String, Object> param, String xlsxFilePath) {
		int sheetNO = poiXls.readExcelSheetNO(xlsxFilePath);
		System.out.println("sheetNO:" + sheetNO);
		for (int i = 0; i < sheetNO; i++) {
			String sheetName = poiXls.readExcelSheetName(xlsxFilePath, i);
			if ("".equals(sheetName)) {
				break;
			}
			if(sheetName.equals("????????????")) {
				continue;
			}
			System.out.println("\tsheetName=" + sheetName);
			//
			List<String> row = new ArrayList<String>();
			Map<String, List<String>> mapBaseModel = new HashMap<String, List<String>>();
			List<String> listCell = poiXls.readExcelCell(xlsxFilePath, i, 0); // ???0??????????????????
			if (listCell == null) {
				param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
				logger.error(sheetName + "?????????????????????");
				param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "???????????????" + sheetName + "????????????????????????");
				param.put(KEY_needToDownload, EnumBoolean.EB_Yes.getIndex());
				return false;
			}
			// ???????????????????????????
			for (int k = 0; k < listCell.size(); k++) {
				row = poiXls.readExcelRow(xlsxFilePath, i, k);
				if (row == null) {
					break;
				}
				// ????????????????????????key
				mapBaseModel.put(String.valueOf(k), row);
			}
			mapBaseModels.put(sheetName, mapBaseModel);
		}
		return true;
	}

	private boolean parseVipFromExcel(Map<String, Map<String, List<String>>> mapBaseModels, Map<String, Object> param, String xlsxFilePath) {
		Map<String, List<String>> vipSheet = mapBaseModels.get(SHEET_NAME_Vip);
		if (vipSheet == null) {
			logger.info("??????????????????????????????");
			return true;
		}
		List<String> listRowTitle = mapBaseModels.get(SHEET_NAME_Vip).get(VIP_SHEET_RowIndex); // ??????
		for (int i = 3; i < vipSheet.size(); i++) {
			vipTotalToCreate++;
			List<String> comm = mapBaseModels.get(SHEET_NAME_Vip).get(i + "");
			if (comm == null) {
				continue;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			putIntoHashMap(listRowTitle, comm, params);
			Vip vip = new Vip();
			//??????????????????????????????????????????????????????????????????????????????vip.setXXX()?????????????????????????????????????????????xls???????????????
			params.put(Vip.field.getFIELD_NAME_ID(), 0);
			params.put(Vip.field.getFIELD_NAME_sn(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_cardID(), 1);
			params.put(Vip.field.getFIELD_NAME_localPosSN(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_iCID(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_email(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_consumeTimes(), 0);
			params.put(Vip.field.getFIELD_NAME_consumeAmount(), 0);
			params.put(Vip.field.getFIELD_NAME_district(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_category(), 1);
			params.put(Vip.field.getFIELD_NAME_remark(), "\"" + "???????????????" + "\"");
			params.put(Vip.field.getFIELD_NAME_logo(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_createDatetime(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_updateDatetime(), "\"\"");
			params.put(Vip.field.getFIELD_NAME_cardCode(), "\"\"");
			// ???????????????????????????????????????,??????????????????????????????null
			String birthDayStr = (String) params.get(Vip.field.getFIELD_NAME_birthday());
			birthDayStr = birthDayStr.substring(1, birthDayStr.length() - 1);
			if(birthDayStr.equals(DEFAULT_VALUE_IfCellEmpty)) {
				params.put(Vip.field.getFIELD_NAME_birthday(), "\"\"");
			}
			String lastConsumeDatetime = (String) params.get(Vip.field.getFIELD_NAME_lastConsumeDatetime());
			lastConsumeDatetime = lastConsumeDatetime.substring(1, lastConsumeDatetime.length() - 1);
			if(lastConsumeDatetime.equals(DEFAULT_VALUE_IfCellEmpty)) {
				params.put(Vip.field.getFIELD_NAME_lastConsumeDatetime(), "\"\"");
			}
			vip = (Vip) vip.parse1(params.toString());
			if (vip == null) {
				String vipErrorField = new Vip().doParse1_returnField(params.toString());
				addCommentInVipSheet(param, i, vipErrorField, "????????????", xlsxFilePath);
				vipWrongFormatNumber++;
				continue;
			}
			if (vip.getName().equals(DEFAULT_VALUE_IfCellEmpty)) {
				vip.setName(null);
			}
			System.out.println(vip);
			String modelCheckMsg = vip.checkCreate(BaseBO.CASE_Vip_ImportFromOldSystem);
			if (modelCheckMsg.length() > 0) {
				String vipErrorField = vip.checkCreate_returnField(BaseBO.CASE_Vip_ImportFromOldSystem);
				addCommentInVipSheet(param, i, vipErrorField, modelCheckMsg, xlsxFilePath);
				vipWrongFormatNumber++;
				continue;
			}
			vip.setExcelLineID(i);
			// ???????????????????????????
			if(mapCheckDuplicateVipMobile.get(vip.getMobile()) != null) {
				int previousCommLineID = mapCheckDuplicateVipMobile.get(vip.getMobile());
				addCommentInVipSheet(param, i, EnumVipInfoInExcel.EVIIE_mobile.getName(), "????????????????????????????????????????????????", xlsxFilePath);
				addCommentInVipSheet(param, previousCommLineID, EnumVipInfoInExcel.EVIIE_mobile.getName(), "????????????????????????????????????????????????", xlsxFilePath);
				if(firstFindDuplicateVipMobile) {
					vipWrongFormatNumber += 2;
					firstFindDuplicateVipMobile = false;
				} else {
					vipWrongFormatNumber++;
				}
				continue;
			}
			mapCheckDuplicateVipMobile.put(vip.getMobile(), vip.getExcelLineID());
			vipListToCreate.add(vip);
		}
		return true;
	}

	private boolean parseProviderFromExcel(String dbName, int staffID, Map<String, Object> param, HashMap<String, Integer> providerSet, Map<String, Map<String, List<String>>> mapBaseModels, HashMap<String, Integer> providerDistrictSet,
			String xlsxFilePath) {
		Map<String, List<String>> providerSheet = mapBaseModels.get(SHEET_NAME_Provider);
		if (providerSheet == null) {
			logger.info("?????????????????????????????????");
			return true;
		}
		List<String> listRowTitle = mapBaseModels.get(SHEET_NAME_Provider).get(PROVIDER_SHEET_RowIndex); // ??????
		for (int i = 3; i < providerSheet.size(); i++) {
			providerTotalToCreate++;
			List<String> comm = mapBaseModels.get(SHEET_NAME_Provider).get(i + "");
			if (comm == null) {
				continue;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			putIntoHashMap(listRowTitle, comm, params);
			Provider provider = new Provider();
			params.put(Provider.field.getFIELD_NAME_ID(), 0);
			params.put(Provider.field.getFIELD_NAME_districtID(), 0);
			params.put(Provider.field.getFIELD_NAME_createDatetime(), "\"\"");
			params.put(Provider.field.getFIELD_NAME_updateDatetime(), "\"\"");
			provider = (Provider) provider.parse1(params.toString());
			if (provider == null) {
				param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
				param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "parse1?????????????????????");
				providerWrongFormatNumber++;
				continue;
			}
			String providerdistrict = params.get("district").toString();
			providerdistrict = providerdistrict.substring(1, providerdistrict.length() - 1);
			if (providerDistrictSet.containsKey(providerdistrict)) {
				int districtID = (int) providerDistrictSet.get(providerdistrict);
				provider.setDistrictID(districtID);
			} else {
				ProviderDistrict providerDistrictGet = new ProviderDistrict();
				providerDistrictGet.setName(providerdistrict);
				String modelCheckMsg = providerDistrictGet.checkCreate(BaseBO.INVALID_CASE_ID);
				if (modelCheckMsg.length() > 0) {
					addCommentInProviderSheet(param, i, EnumProviderInfoInExcel.EPIIE_district.getName(), modelCheckMsg, xlsxFilePath);
					providerWrongFormatNumber++;
					continue;
				}
				DataSourceContextHolder.setDbName(dbName);
				ProviderDistrict providerDistrictCreated = (ProviderDistrict) providerDistrictBO.createObject(staffID, BaseBO.INVALID_CASE_ID, providerDistrictGet);
				if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError || providerDistrictCreated == null) {
					param.put(BaseAction.JSON_ERROR_KEY, providerDistrictBO.getLastErrorCode());
					param.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerDistrictBO.getLastErrorMessage());
					providerWrongFormatNumber++;
					continue;
				}
				providerDistrictSet.put(providerDistrictCreated.getName(), providerDistrictCreated.getID());
				provider.setDistrictID(providerDistrictCreated.getID());
			}
			System.out.println(provider);
			String modelCheckMsg = provider.checkCreate(BaseBO.INVALID_CASE_ID);
			if (modelCheckMsg.length() > 0) {
				String providerErrorField = provider.doCheckCreateUpdate_returnField(BaseBO.INVALID_CASE_ID);
				addCommentInProviderSheet(param, i, providerErrorField, modelCheckMsg, xlsxFilePath);
				providerWrongFormatNumber++;
				continue;
			}
			provider.setExcelLineID(i);
			// excel???????????????DB?????????????????????
			if (!providerSet.containsKey(provider.getName())) {
				// ????????????????????????
				if(mapCheckDuplicateProviderName.get(provider.getName()) != null) {
					int previousLineID = mapCheckDuplicateProviderName.get(provider.getName());
					addCommentInProviderSheet(param, i, EnumProviderInfoInExcel.EPIIE_name.getName(), "??????????????????????????????????????????????????????", xlsxFilePath);
					addCommentInProviderSheet(param, previousLineID, EnumProviderInfoInExcel.EPIIE_name.getName(), "??????????????????????????????????????????????????????", xlsxFilePath);
					Iterator<Provider> it = providerListToCreate.iterator();
					while(it.hasNext()) {
						Provider pd = it.next();
						if(pd.getName().equals(provider.getName())) {
							it.remove();
						}
					}
					if(firstFindDuplicateProviderName) {
						providerWrongFormatNumber += 2;
						firstFindDuplicateProviderName = false;
					} else {
						providerWrongFormatNumber++;
					}
					continue;
				}
				mapCheckDuplicateProviderName.put(provider.getName(), provider.getExcelLineID());
				if(mapCheckDuplicateProviderMobile.get(provider.getMobile()) != null) {
					int previousLineID = mapCheckDuplicateProviderMobile.get(provider.getMobile());
					addCommentInProviderSheet(param, i, EnumProviderInfoInExcel.EPIIE_mobile.getName(), "????????????????????????????????????????????????????????????", xlsxFilePath);
					addCommentInProviderSheet(param, previousLineID, EnumProviderInfoInExcel.EPIIE_mobile.getName(), "????????????????????????????????????????????????????????????", xlsxFilePath);
					Iterator<Provider> it = providerListToCreate.iterator();
					while(it.hasNext()) {
						Provider pd = it.next();
						if(pd.getMobile().equals(provider.getMobile())) {
							it.remove();
						}
					}
					if(firstFindDuplicateProviderMobile) {
						providerWrongFormatNumber += 2;
						firstFindDuplicateProviderMobile = false;
					} else {
						providerWrongFormatNumber++;
					}
					continue;
				}
				mapCheckDuplicateProviderMobile.put(provider.getMobile(), provider.getExcelLineID());
				providerListToCreate.add(provider);
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param dbName
	 * @param staffID
	 * @param param
	 * @param providerSet ?????????????????????????????????DB?????????????????????????????????????????????
	 * @param xlsxFilePath
	 */
	private void createProvider(String dbName, int staffID, Map<String, Object> param, HashMap<String, Integer> providerSet, String xlsxFilePath) {
		for(Provider provider : providerListToCreate) {
			DataSourceContextHolder.setDbName(dbName);
			Provider providerCreated = (Provider) providerBO.createObject(staffID, BaseBO.INVALID_CASE_ID, provider);
			if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError || providerCreated == null) {
				param.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode());
				param.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
				addCommentInProviderSheet(param, provider.getExcelLineID(), Provider.field.getFIELD_NAME_name(), providerBO.getLastErrorMessage(), xlsxFilePath);
				providerWrongFormatNumber++;
				continue;
			}
			providerSet.put(providerCreated.getName(), providerCreated.getID());
		}
	}

	@SuppressWarnings("unchecked")
	private boolean queryProvider(String dbName, int staffID, Company company, Map<String, Object> param, HashMap<String, Integer> providerSet) {
		Provider provider = new Provider();
		provider.setPageIndex(BaseAction.PAGE_StartIndex);
		provider.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<Provider> ls = (List<Provider>) providerBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, provider);
		if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			param.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerBO.getLastErrorMessage());
			logger.error("??????????????????????????????" + providerBO.printErrorInfo());
			return false;
		}
		for (Object providerDB : ls) {
			Provider providerBm = (Provider) providerDB;
			providerSet.put(providerBm.getName(), providerBm.getID());
		}
		return true;
	}

	private boolean queryPackageUnit(String dbName, int staffID, Company company, Map<String, Object> param, HashMap<String, Integer> packageUnitSet) {
		PackageUnit packageUnit = new PackageUnit();
		packageUnit.setPageIndex(BaseAction.PAGE_StartIndex);
		packageUnit.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> ls = packageUnitBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, packageUnit);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			param.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode());
			param.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorMessage());
			logger.error("?????????????????????????????????" + packageUnitBO.printErrorInfo());
			return false;
		}
		for (Object packageUnitDB : ls) {
			PackageUnit packageUnitBm = (PackageUnit) packageUnitDB;
			packageUnitSet.put(packageUnitBm.getName(), packageUnitBm.getID());
		}
		return true;
	}

	private boolean parseCommodityFromExcel(String dbName, int staffID, HashMap<String, Integer> brandNameSet, HashMap<String, Integer> categorySet, Map<String, Map<String, List<String>>> mapBaseModels,
			HashMap<String, Integer> packageUnitSet, HashMap<String, Integer> providerSet, Map<String, Object> param, String xlsxFilePath) {
		// ??????????????????
		Map<String, List<String>> commoditySheet = mapBaseModels.get(SHEET_NAME_Commodity);
		if (commoditySheet == null) {
			logger.info("????????????????????????????????????");
			return true;
		}
		List<String> listRowTitle = mapBaseModels.get(SHEET_NAME_Commodity).get(COMMODITY_SHEET_RowIndex); // ??????
		for (int i = 3; i < commoditySheet.size(); i++) {
			commodityTotalToCreate++;
			List<String> comm = mapBaseModels.get(SHEET_NAME_Commodity).get(i + "");
			if (comm == null) {
				continue;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			putIntoHashMap(listRowTitle, comm, params);
			setCommodityProperty(params);
			Commodity commodity = new Commodity();
			commodity = (Commodity) commodity.parse1(params.toString());
			if (commodity == null) {
				String commodityField = new Commodity().doParse1_returnField(params.toString());
				addCommentInCommoditySheet(param, i, commodityField, "???????????????", xlsxFilePath);
				commodityWrongFormatNumber++;
				continue;
			}
			// ???????????????????????????null
			if (commodity.getName().equals(DEFAULT_VALUE_IfCellEmpty)) {
				addCommentInCommoditySheet(param, i, EnumCommodityInfoInExcel.ECIIE_name.getName(), "????????????????????????", xlsxFilePath);
				commodityWrongFormatNumber++;
				continue;
			}
			// ????????????
			String brandName = params.get(Commodity.field.getFIELD_NAME_brandName()).toString();
			brandName = brandName.substring(1, brandName.length() - 1);
			if (brandName.equals(DEFAULT_VALUE_IfCellEmpty)) {
				brandName = "????????????";
			}
			if (brandNameSet.containsKey(brandName)) {
				int brandID = (int) brandNameSet.get(brandName);
				commodity.setBrandID(brandID);
			} else {
				// ????????????
				Brand brandGet = new Brand();
				brandGet.setName(brandName);
				brandGet.setReturnObject(1);
				String errorMsg = brandGet.checkCreate(BaseBO.INVALID_CASE_ID);
				if (errorMsg.length() > 0) {
					addCommentInCommoditySheet(param, i, EnumCommodityInfoInExcel.ECIIE_brandName.getName(), errorMsg, xlsxFilePath);
					commodityWrongFormatNumber++;
					continue;
				}

				Brand brandCreated = createBrand(brandGet);
				commodity.setBrandID(brandCreated.getID());
				brandNameSet.put(brandCreated.getName(), brandCreated.getID());
			}
			// ????????????
			String categoryName = params.get(Commodity.field.getFIELD_NAME_categoryName()).toString();
			categoryName = categoryName.substring(1, categoryName.length() - 1);
			if (categoryName.equals(DEFAULT_VALUE_IfCellEmpty)) {
				categoryName = "????????????";
			}
			if (categorySet.containsKey(categoryName)) {
				int categoryID = (int) categorySet.get(categoryName);
				commodity.setCategoryID(categoryID);
			} else {
				// ??????????????????
				Category categoryGet = new Category();
				categoryGet.setName(categoryName);
				categoryGet.setParentID(1); // ??????parentID
				categoryGet.setReturnObject(1);
				String errorMsg = categoryGet.checkCreate(BaseBO.INVALID_CASE_ID);
				if (errorMsg.length() > 0) {
					addCommentInCommoditySheet(param, i, EnumCommodityInfoInExcel.ECIIE_categoryName.getName(), errorMsg, xlsxFilePath);
					commodityWrongFormatNumber++;
					continue;
				}
				Category categoryCreated = createCategory(categoryGet);
				commodity.setCategoryID(categoryCreated.getID());
				categorySet.put(categoryCreated.getName(), categoryCreated.getID());
			}
			// ????????????
			String packageUnitName = params.get(Commodity.field.getFIELD_NAME_packageUnitName()).toString();
			packageUnitName = packageUnitName.substring(1, packageUnitName.length() - 1);
			if (packageUnitSet.containsKey(packageUnitName)) {
				int packageUnitID = (int) packageUnitSet.get(packageUnitName);
				commodity.setPackageUnitID(packageUnitID);
			} else {
				// ??????????????????
				PackageUnit packageUnitGet = new PackageUnit();
				packageUnitGet.setName(packageUnitName);
				String errorMsg = packageUnitGet.checkCreate(BaseBO.INVALID_CASE_ID);
				if (errorMsg.length() > 0) {
					addCommentInCommoditySheet(param, i, EnumCommodityInfoInExcel.ECIIE_packageUnitName.getName(), errorMsg, xlsxFilePath);
					commodityWrongFormatNumber++;
					continue;
				}
				DataSourceContextHolder.setDbName(dbName);
				BaseModel bmCreated = packageUnitBO.createObject(staffID, BaseBO.INVALID_CASE_ID, packageUnitGet);
				if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmCreated == null) {
					param.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode());
					param.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorMessage());
					commodityWrongFormatNumber++;
					continue;
				}
				PackageUnit packageUnitCreated = (PackageUnit) bmCreated;
				commodity.setPackageUnitID(packageUnitCreated.getID());
				packageUnitSet.put(packageUnitCreated.getName(), packageUnitCreated.getID());
			}
			if (commodity.getPurchasingUnit().equals(DEFAULT_VALUE_IfCellEmpty)) {
				commodity.setPurchasingUnit(packageUnitName);
			}
			// ??????ID
			commodity.setOperatorStaffID(staffID);
			// ???????????????
			String providerName = params.get(Commodity.field.getFIELD_NAME_providerName()).toString();
			providerName = providerName.substring(1, providerName.length() - 1);
			if (providerName.equals(DEFAULT_VALUE_IfCellEmpty)) {
				providerName = "???????????????";
			}
			if (providerSet.containsKey(providerName)) {
				int providerID = (int) providerSet.get(providerName);
				commodity.setProviderIDs(String.valueOf(providerID));
			} else {
				// ????????????????????????DB????????????????????????????????????????????????excel????????????????????????????????????
				addCommentInCommoditySheet(param, i, EnumCommodityInfoInExcel.ECIIE_providerName.getName(), "??????????????????????????????:" + providerName, xlsxFilePath);
				commodityWrongFormatNumber++;
				continue;
			}
			System.out.println(commodity);
			String modelCheckMsg = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
			if (modelCheckMsg.length() > 0) {
				String commodityErrorField = commodity.checkCreate_returnField(BaseBO.INVALID_CASE_ID);
				// ?????????????????????poiXls????????????????????????????????????excel??????
				addCommentInCommoditySheet(param, i, commodityErrorField, modelCheckMsg, xlsxFilePath);
				commodityWrongFormatNumber++;
				continue;
			}
			//
			String iaBarcodes = commodity.getBarcodes();
			String iaPuID = String.valueOf(commodity.getPackageUnitID());
			String iaCommMultiples = String.valueOf(commodity.getRefCommodityMultiple());
			String iaPriceRetail = String.valueOf(commodity.getPriceRetail());
			String iaPriceVIP = String.valueOf(commodity.getPriceVIP());
			String iaPriceWholesale = String.valueOf(commodity.getPriceWholesale());
			String sNames = commodity.getName();
			commodity.setName(sNames);
			commodity.setMultiPackagingInfo(iaBarcodes + ";" + iaPuID + ";" + iaCommMultiples + ";" + iaPriceRetail + ";" + iaPriceVIP + ";" + iaPriceWholesale + ";" + sNames + ";");
			commodity.setReturnObject(1);
			commodity.setExcelLineID(i);
			// ?????????????????????
			if(mapCheckDuplicateCommName.get(commodity.getName()) != null) {
				int previousCommLineID = mapCheckDuplicateCommName.get(commodity.getName());
				addCommentInCommoditySheet(param, i, EnumCommodityInfoInExcel.ECIIE_name.getName(), "???????????????????????????????????????????????????", xlsxFilePath);
				addCommentInCommoditySheet(param, previousCommLineID, EnumCommodityInfoInExcel.ECIIE_name.getName(), "??????????????????????????????????????????", xlsxFilePath);
				if(firstFindDuplicateCommodityName) {
					commodityWrongFormatNumber += 2;
					firstFindDuplicateCommodityName = false;
				} else {
					commodityWrongFormatNumber++;
				}
				continue;
			}
			mapCheckDuplicateCommName.put(commodity.getName(), commodity.getExcelLineID());
			commodityListToCreate.add(commodity);
		}
		return true;
	}

	private boolean queryCategory(String dbName, int staffID, Map<String, Object> param, HashMap<String, Integer> categorySet) {
		Category category = new Category();
		logger.info("?????????????????????,category=" + category);
		DataSourceContextHolder.setDbName(dbName);
		category.setPageIndex(PAGE_StartIndex);
		category.setPageSize(PAGE_SIZE_Infinite);
		List<?> ls = categoryBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, category);
		logger.info("RetrieveN category error code=" + categoryBO.getLastErrorCode());
		if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			param.put(BaseAction.JSON_ERROR_KEY, categoryBO.getLastErrorCode());
			param.put(BaseAction.JSON_ERROR_KEY, categoryBO.getLastErrorMessage());
			logger.error("RetrieveN category error code=" + categoryBO.getLastErrorCode());
			return false;
		}
		for (Object categoryDB : ls) {
			Category categoryBm = (Category) categoryDB;
			categorySet.put(categoryBm.getName(), categoryBm.getID());
		}
		return true;
	}

	private boolean queryBrand(String dbName, int staffID, Map<String, Object> param, HashMap<String, Integer> brandNameSet) {
		Brand brand = new Brand();
		brand.setPageIndex(BaseAction.PAGE_StartIndex);
		brand.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> brandList = brandBO.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, brand);
		logger.info("retrieveAll Brands error code=" + brandBO.getLastErrorCode());
		if (brandBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			param.put(BaseAction.JSON_ERROR_KEY, brandBO.getLastErrorCode());
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, brandBO.getLastErrorMessage());
			logger.error("retrieveAll Brands error code=" + brandBO.getLastErrorCode());
			return false;
		}
		for (Object brandDB : brandList) {
			Brand brandBm = (Brand) brandDB;
			brandNameSet.put(brandBm.getName(), brandBm.getID());
		}
		return true;
	}

	/*??????excel???????????????*/
	private void setCommodityProperty(Map<String, Object> params) {
		params.put(Commodity.field.getFIELD_NAME_ID(), 0);
		params.put(Commodity.field.getFIELD_NAME_NO(), 0);
		params.put(Commodity.field.getFIELD_NAME_status(), 0);
		params.put(Commodity.field.getFIELD_NAME_shortName(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_categoryID(), 0);
		params.put(Commodity.field.getFIELD_NAME_packageUnitID(), 0);
		params.put(Commodity.field.getFIELD_NAME_brandID(), 0);
		params.put(Commodity.field.getFIELD_NAME_mnemonicCode(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_pricingType(), "1");
		params.put(Commodity.field.getFIELD_NAME_type(), "0");
		params.put(Commodity.field.getFIELD_NAME_latestPricePurchase(), 0);
		params.put(Commodity.field.getFIELD_NAME_priceWholesale(), 0);
		params.put(Commodity.field.getFIELD_NAME_canChangePrice(), 1);
		params.put(Commodity.field.getFIELD_NAME_ruleOfPoint(), 0);
		params.put(Commodity.field.getFIELD_NAME_picture(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_createDate(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_refCommodityID(), 0);
		params.put(Commodity.field.getFIELD_NAME_refCommodityMultiple(), 0);
		params.put(Commodity.field.getFIELD_NAME_tag(), "111");
		params.put(Commodity.field.getFIELD_NAME_startValueRemark(), "\"" + "???????????????" + "\"");
		params.put(Commodity.field.getFIELD_NAME_propertyValue1(), "\"" + "???????????????1" + "\"");
		params.put(Commodity.field.getFIELD_NAME_propertyValue2(), "\"" + "???????????????2" + "\"");
		params.put(Commodity.field.getFIELD_NAME_propertyValue3(), "\"" + "???????????????3" + "\"");
		params.put(Commodity.field.getFIELD_NAME_propertyValue4(), "\"" + "???????????????4" + "\"");
		params.put(Commodity.field.getFIELD_NAME_createDatetime(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_updateDatetime(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_currentWarehousingID(), 0);
		params.put(Commodity.field.getFIELD_NAME_listSlave1(), "[]");
		params.put(Commodity.field.getFIELD_NAME_operatorStaffID(), 0);
		params.put(Commodity.field.getFIELD_NAME_syncType(), 0);
		params.put(Commodity.field.getFIELD_NAME_subCommodityInfo(), null);
		params.put(Commodity.field.getFIELD_NAME_purchaseFlag(), 1); // ??????checkCreate????????????1
	}

	// 1???????????????
	// 2?????????????????????
	// 3????????????????????????
	// (1)16?????????????????????0?????????16?????????excel
	// (2)????????????????????????????????????????????????????????????????????????????????????SQL???NULL
	private boolean createVip(int staffID, Company company, Map<String, Object> params, String xlsxFilePath) {
		// ??????????????????
		for (Vip vip : vipListToCreate) {
			do {
				vip.setCategory(BaseAction.DEFAULT_VipCategoryID);
				vip.setCreateDatetime(new Date());
				vip.setIsImported(EnumBoolean.EB_Yes.getIndex());
				DataSourceContextHolder.setDbName(company.getDbName());
				Vip vipCreate = null;
				if(vip.getIsImported() == EnumBoolean.EB_NO.getIndex()) {
					vipCreate = (Vip) vipBO.createObject(staffID, BaseBO.INVALID_CASE_ID, vip);
				} else {
					vipCreate = (Vip) vipBO.createObject(staffID, BaseBO.CASE_Vip_ImportFromOldSystem, vip);
				}
				if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("?????????????????????" + vipBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
					// ???????????????????????????EC_BusinessLogicNotDefined
					// TODO SP???????????????????????????????????????????????????
					if(vipBO.getLastErrorCode() == EnumErrorCode.EC_BusinessLogicNotDefined && vipBO.getLastErrorMessage().equals("???????????????????????????????????????????????????")) {
						vip_excelLineToDelete.add(vip.getExcelLineID());
						continue;
					}
					addCommentInVipExcelLine(xlsxFilePath, vip, vipBO.getLastErrorMessage());
					vipFailCreateNumber++;
					needToDownload = EnumBoolean.EB_Yes.getIndex();
					continue;
				}
				// ??????????????????????????????Excel????????????????????????
				vip_excelLineToDelete.add(vip.getExcelLineID());
				// ??????????????????
				CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).write1(vipCreate, company.getDbName(), staffID);

				params.put(BaseAction.KEY_Object, vipCreate);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				// ?????????????????????
				VipSource vipSource = new VipSource();
				vipSource.setVipID(vipCreate.getID());
				vipSource.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex());
				vipSource.setID1(""); // openID
				vipSource.setID2(""); // unionID
				vipSource.setID3(""); // MiniProgramOpenID
				DataSourceContextHolder.setDbName(company.getDbName());
				VipSource vipSourceCreated = (VipSource) vipSourceBO.createObject(staffID, BaseBO.INVALID_CASE_ID, vipSource);
				if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipSourceCreated == null) {
					logger.error("???????????????????????????" + vipBO.printErrorInfo()); // ???vip source????????????????????????????????????
					vipFailCreateNumber++;
					break; // ????????????????????????????????????????????????????????????
				}
				// ?????????????????????vipcardcode
				VipCardCode vipCardCode = new VipCardCode();
				vipCardCode.setVipID(vipCreate.getID());
				vipCardCode.setVipCardID(BaseAction.DEFAULT_VipCardID);
				vipCardCode.setCompanySN(company.getSN());
				DataSourceContextHolder.setDbName(company.getDbName());
				VipCardCode vipCardCodeCreated = (VipCardCode) vipCardCodeBO.createObject(staffID, BaseBO.INVALID_CASE_ID, vipCardCode);
				if (vipCardCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipCardCodeCreated == null) {
					logger.error("??????????????????????????????" + vipCardCodeBO.printErrorInfo(company.getDbName(), vipCardCode));
					params.put(BaseAction.JSON_ERROR_KEY, vipCardCodeBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipCardCodeBO.getLastErrorMessage());
					addCommentInVipExcelLine(xlsxFilePath, vip, vipCardCodeBO.getLastErrorMessage());
					vipFailCreateNumber++;
					// ??????????????????
					if(vipCardCodeBO.getLastErrorCode() == EnumErrorCode.EC_Duplicated) {
						needToDownload = EnumBoolean.EB_Yes.getIndex();
					}
					continue;
				}
			} while (false);
		}
		// ???????????? TODO ????????????????????????????????????????????????????????????
        Collections.sort(vip_excelLineToDelete, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
            	return o2.compareTo(o1);
            }
        });
        for(int i = 0; i < vip_excelLineToDelete.size(); i++) {
			deleteVipExcelLine(xlsxFilePath, SHEET_NAME_Vip, vip_excelLineToDelete.get(i));
        }
		return true;
	}


	// (1)???????????????DB??????????????????????????????????????????(????????????NULL???)
	// (2)???????????????DB????????????????????????????????????
	// (3)???????????????DB????????????????????????????????????
	// (4)???????????????DB????????????????????????????????????
	// (5)???????????????????????????????????????365
	// (6)??????????????????????????????????????????7
	// (7)?????????????????????????????????100
	// (8)??????????????????>=0,
	// (9)????????????????????????>0
	// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????xls????????????????????????????????????????????????????????????????????????????????????????????????
	private boolean createCommodityAndBarcodes(String xlsxFilePath, Map<String, Object> param) {
		// ?????????????????????????????????????????????
		// ?????????????????????
		return doCreateCommodities(xlsxFilePath, param);
	}

	// ??????????????????????????????
	private boolean doCreateCommodities(String xlsxFilePath, Map<String, Object> param) {
		for (Commodity commodity : commodityListToCreate) {
			Commodity com = createCommodity("commoditySync/createEx.bx", commodity, xlsxFilePath, param, logger);
			if (com == null) {
				continue;
			}
		}
		// ????????????????????????DB????????????excel???
		// ??????????????? TODO ????????????????????????????????????????????????????????????
        Collections.sort(commodity_excelLineToDelete, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
            	return o2.compareTo(o1);
            }
        });
		for(int i = 0; i < commodity_excelLineToDelete.size(); i++) {
			deleteVipExcelLine(xlsxFilePath, SHEET_NAME_Commodity, commodity_excelLineToDelete.get(i));
		}
		
		return true;
	}

	private boolean checkFile(Map<String, Object> param, MultipartFile file) {
		if (!TEMPLATE_FILE_NAME_ImportData.equals(file.getOriginalFilename())) {
			param.put(JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
			param.put(KEY_HTMLTable_Parameter_msg, "?????????????????????" + TEMPLATE_FILE_NAME_ImportData);
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return false;
		}
		if (!checkFileSize(file)) {
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "?????????????????????");
			logger.error(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
			return false;
		}

		return true;
	}

	// (3)???????????????????????????????????????????????????????????????
	// <= 5MB
	private boolean checkFileSize(MultipartFile file) {
		System.out.println("file.size():" + file.getSize() / 1024 + "KB");
		ErrorInfo ecOut = new ErrorInfo();
		BxConfigGeneral maxImportFileSize = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MAX_IMPORT_FILE_SIZE, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		BxConfigGeneral extraDiskSpaceSize = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.EXTRA_DISK_SPACE_SIZE, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		logger.info("file:" + file);
		if(maxImportFileSize == null || extraDiskSpaceSize == null) {
			logger.error("maxImportFileSize=" + maxImportFileSize + ",extraDiskSpaceSize=" + extraDiskSpaceSize);
			return false;
		}
		File diskPath = new File("D:/");
		if (file.getSize() > Integer.valueOf(maxImportFileSize.getValue())) { // ...
			logger.error("file.getSize()=" + file.getSize() + ",maxImportFileSize.getValue()=" + maxImportFileSize.getValue());
			return false;
		} else if (diskPath.getUsableSpace() - Integer.valueOf(extraDiskSpaceSize.getValue()) < file.getSize()) {
			logger.error("??????????????????!");
			return false;
		}
		return true;
	}

	public Commodity createCommodity(String url, Commodity commodity, String xlsxFilePath, Map<String, Object> param, Log logger) {
		String response = AppUtil.createCommodityViaOkHttp(url, commodity);
		logger.debug("?????????????????????" + response);
		//
		JSONObject object = JSONObject.fromObject(response);
		String err = JsonPath.read(object, "$." + BaseAction.JSON_ERROR_KEY);
		String msg = "??????????????????";
		try {
			msg = JsonPath.read(object, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		} catch (Exception e) {
			
		}
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			if(err.compareTo(EnumErrorCode.EC_Duplicated.toString()) == 0) {
				commodity_excelLineToDelete.add(commodity.getExcelLineID());
			} else {
				PoiUtils poiXls2;
				try {
					poiXls2 = new PoiUtils(xlsxFilePath);
					poiXls2.updateOneExcelCellStyle(xlsxFilePath, SHEET_NAME_Commodity, commodity.getExcelLineID(), 0, msg);
				} catch (IOException e) {
					// TODO
					e.printStackTrace();
				}
				param.put(BaseAction.JSON_ERROR_KEY, err);
				param.put(BaseAction.KEY_HTMLTable_Parameter_msg, msg);
				needToDownload = EnumBoolean.EB_Yes.getIndex();
				commodityFailCreateNumber++;
			}
			return null;
		}
		// ?????????????????????Excel??????????????????
		commodity_excelLineToDelete.add(commodity.getExcelLineID());
		Commodity commodity1 = new Commodity();
		commodity1 = (Commodity) commodity1.parse1(object.getString(BaseAction.KEY_Object));
		//
		return commodity1;
	}

	public static Brand createBrand(Brand brand) {
		Map<String, String> params = new HashMap<>();
		params.put(Brand.field.getFIELD_NAME_name(), brand.getName());
		params.put(Brand.field.getFIELD_NAME_returnObject(), String.valueOf(brand.getReturnObject()));
		String response = OkHttpUtil.post("brandSync/createEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Brand brandCreated = new Brand();
		brandCreated = (Brand) brandCreated.parse1(object.getString(BaseAction.KEY_Object));
		//
		return brandCreated;
	}

	public static Category createCategory(Category category) {
		Map<String, String> params = new HashMap<>();
		params.put(Category.field.getFIELD_NAME_name(), category.getName());
		params.put(Category.field.getFIELD_NAME_parentID(), String.valueOf(category.getParentID()));
		params.put(Category.field.getFIELD_NAME_returnObject(), String.valueOf(category.getReturnObject()));
		String response = OkHttpUtil.post("categorySync/createEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Category categoryCreated = new Category();
		categoryCreated = (Category) categoryCreated.parse1(object.getString(BaseAction.KEY_Object));
		//
		return categoryCreated;
	}

	/*???????????????????????????????????????*/
	private void addCommentInCommoditySheet(Map<String, Object> param, int row, String commodityErrorField, String comment, String xlsxFilePath) {
		PoiUtils poiXls2;
		try {
			int column = -1;
			for (EnumCommodityInfoInExcel enumCommodityInfoInExcel : EnumCommodityInfoInExcel.values()) {
				if (enumCommodityInfoInExcel.getName().equals(commodityErrorField)) {
					column = enumCommodityInfoInExcel.getIndex();
					break;
				}
			}
			if (column == -1) {
				throw new RuntimeException("??????????????????????????????" + commodityErrorField);
			}
			poiXls2 = new PoiUtils(xlsxFilePath);
			poiXls2.updateOneExcelCellStyle(xlsxFilePath, SHEET_NAME_Commodity, row, column, comment);
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
		param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
		param.put(BaseAction.KEY_HTMLTable_Parameter_msg, comment);
	}

	private void addCommentInVipSheet(Map<String, Object> param, int row, String vipErrorField, String comment, String xlsxFilePath) {
		PoiUtils poiXls2;
		int column = -1;
		try {
			for (EnumVipInfoInExcel enumVipInfoInExcel : EnumVipInfoInExcel.values()) {
				if (enumVipInfoInExcel.getName().equals(vipErrorField)) {
					column = enumVipInfoInExcel.getIndex();
					break;
				}
			}
			if (column == -1) {
				throw new RuntimeException("??????????????????????????????" + vipErrorField);
			}
			poiXls2 = new PoiUtils(xlsxFilePath);
			poiXls2.updateOneExcelCellStyle(xlsxFilePath, SHEET_NAME_Vip, row, column, comment);
		} catch (IOException e) {
			logger.error("??????????????????" + "\tSHEET_NAME_Vip=" + SHEET_NAME_Vip + "\trow=" + row + "\tcolumn=" + column);
			e.printStackTrace();
		}
		param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
		param.put(BaseAction.KEY_HTMLTable_Parameter_msg, comment);
	}

	private void addCommentInProviderSheet(Map<String, Object> param, int row, String providerErrorField, String modelCheckMsg, String xlsxFilePath) {
		PoiUtils poiXls2;
		int column = -1;
		try {
			for (EnumProviderInfoInExcel enumProviderInfoInExcel : EnumProviderInfoInExcel.values()) {
				if (enumProviderInfoInExcel.getName().equals(providerErrorField)) {
					column = enumProviderInfoInExcel.getIndex();
					break;
				}
			}
			if (column == -1) {
				throw new RuntimeException("?????????????????????????????????" + providerErrorField);
			}
			poiXls2 = new PoiUtils(xlsxFilePath);
			poiXls2.updateOneExcelCellStyle(xlsxFilePath, SHEET_NAME_Provider, row, column, modelCheckMsg);
		} catch (IOException e) {
			logger.error("??????????????????" + "\tSHEET_NAME_Vip=" + SHEET_NAME_Vip + "\trow=" + row + "\tcolumn=" + column);
			e.printStackTrace();
		}
		param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
		param.put(BaseAction.KEY_HTMLTable_Parameter_msg, modelCheckMsg);
	}

	private void putIntoHashMap(List<String> listRowTitle, List<String> comm, Map<String, Object> params) {
		for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
			if (comm.get(columnNO) == null) {
				params.put(listRowTitle.get(columnNO), "\"" + DEFAULT_VALUE_IfCellEmpty + "\"");
			} else {
				params.put(listRowTitle.get(columnNO), "\"" + comm.get(columnNO) + "\"");
			}
		}
	}
	
	private void addCommentInVipExcelLine(String xlsxFilePath, Vip vip, String errorMsg) {
		// ?????????
		PoiUtils poiXls2;
		try {
			poiXls2 = new PoiUtils(xlsxFilePath);
			poiXls2.updateOneExcelCellStyle(xlsxFilePath, SHEET_NAME_Vip, vip.getExcelLineID(), 0, errorMsg);
		} catch (IOException e) {
			logger.debug("??????????????????" + e.getMessage() + "?????????xlsxFilePath=" + xlsxFilePath + "\tvip=" +vip);
		}
	}
	
	private void deleteVipExcelLine(String xlsxFilePath, String sheetName, int excelRow) {
		// ?????????
		PoiUtils poiXls2;
		try {
			poiXls2 = new PoiUtils(xlsxFilePath);
			poiXls2.deleteOneExcelRow(xlsxFilePath, sheetName, excelRow);
		} catch (IOException e) {
			logger.debug("??????????????????" + e.getMessage() + "?????????xlsxFilePath=" + xlsxFilePath + "\texcelRow=" + excelRow);
		}
	}
	
	
	@RequestMapping(value = "/downloadEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String downloadEx(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		Company company = getCompanyFromSession(session);
		Map<String, Object> param = getDefaultParamToReturn(true);
		Staff staff = getStaffFromSession(session);
		// ??????????????????????????????
		if(staff.getRoleID() != Role.EnumTypeRole.ETR_Boss.getIndex()) {
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????");
			return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
		}
		String xlsxFilePath = String.format(PATH_SaveMultipartFileToDestination, company.getDbName());
		// ????????????????????????
		File file = new File(xlsxFilePath);
		if(!file.exists()) {
			param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
			param.put(BaseAction.KEY_HTMLTable_Parameter_msg, "????????????????????????????????????");
		}
		//
		String filePath = xlsxFilePath.replace(ImportCommodityAndVipDir, TOMCAT_MAP_DIR_DownloadCommodityAndVip);
		logger.info("??????????????????" + filePath);
		param.put("filePath", filePath);
		param.put(KEY_HTMLTable_Parameter_msg, "");
		param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		logger.info(JSONObject.fromObject(param, JsonUtil.jsonConfig).toString());
		return JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();
	}
}
