package com.bx.erp.test.checkPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.SmallSheetFrameMapper;
import com.bx.erp.dao.SmallSheetTextMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SmallSheetCP {

	// 1、检查小票格式A普通缓存是否创建，小票格式A的ListSlave1()中小票格式详情是否正确。
	// 2、检查数据库T_SmallsheetFrame是否创建了小票格式A的数据。
	// 3、检查数据库T_SmallsheetText是否创建小票格式A的详情。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, SmallSheetTextMapper smallSheetTextMapper, String dbName) throws Exception {
		// 检查数据库T_SmallsheetFrame是否创建了小票格式A的数据。
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		SmallSheetFrame smallSheetFrameDB = new SmallSheetFrame();
		smallSheetFrameDB = (SmallSheetFrame) smallSheetFrameDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(smallSheetFrameDB != null, "创建小票格式返回的对象为null");
		// 检查check
		DataSourceContextHolder.setDbName(dbName);
		// 刚好创建第10张小票，这里进行checkCreate会判断到已经有10张小票，不通过
//		String error = smallSheetFrameDB.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertTrue(error.equals(""), "数据库中的数据不合法:" + error);
		// 检查数据库T_SmallsheetText是否创建小票格式A的详情。
		SmallSheetText sst = new SmallSheetText();
		sst.setFrameId(smallSheetFrameDB.getID());
		sst.setPageIndex(1);
		sst.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		Map<String, Object> params = sst.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sst);
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> smallSheetTextDBList = smallSheetTextMapper.retrieveN(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询成功！");
		Assert.assertTrue(smallSheetTextDBList.size() > 0, "没有创建小票格式的详情");
		// 创建后返回的小票格式没有从表，需要设置
		List<SmallSheetText> smallSheetTextList = new ArrayList<SmallSheetText>();
		for (BaseModel bm : smallSheetTextDBList) {
			SmallSheetText ssf = (SmallSheetText) bm;
			smallSheetTextList.add(ssf);
		}
		smallSheetFrameDB.setListSlave1(smallSheetTextList);
		// 检查是否正确创建，与前端传进来的数据是否一致
		// 解析前端传进来的小票详情
		String str = mr.getRequest().getParameter("smallSheetTextList").trim();
		Assert.assertTrue(str != null, "从表数据格式不正确!");
		List<SmallSheetText> smallSheetTextListReq = new ArrayList<>();
		JSONArray jsonArray = JSONArray.fromObject(str);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = (JSONObject) jsonArray.get(i);
			SmallSheetText smallSheetText = (SmallSheetText) JSONObject.toBean(json, SmallSheetText.class);
			Assert.assertTrue(smallSheetText != null, "从表数据解析失败！");
			smallSheetText.setFrameId(smallSheetFrameDB.getID());
			smallSheetTextListReq.add(smallSheetText);
		}
		SmallSheetFrame smallSheetFrameCreate = (SmallSheetFrame) bmCreateObjet.clone();
		smallSheetFrameCreate.setListSlave1(smallSheetTextListReq);
		smallSheetFrameCreate.setIgnoreIDInComparision(true);
		Assert.assertTrue(smallSheetFrameCreate.compareTo(smallSheetFrameDB) == 0, "返回的对象信息有误");
		// 检查小票格式A普通缓存是否创建,ListSlave1()中小票格式详情是否正确。
		ErrorInfo ecOut = new ErrorInfo();
		SmallSheetFrame bmCache = (SmallSheetFrame) CacheManager.getCache(dbName, EnumCacheType.ECT_SmallSheet).read1(smallSheetFrameDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bmCache != null && bmCache.getID() == smallSheetFrameDB.getID(), "普通缓存不存在本次创建出来的对象");
		// 检查缓存主从表与DB中的是否一致
		Assert.assertTrue(smallSheetFrameDB.compareTo(bmCache) == 0, "缓存的小票格式与DB中的不一致");

		return true;
	}

	// 1、检查小票格式A普通缓存是否修改，小票格式A的ListSlave1()中小票格式详情是否正确。
	// 2、检查数据库T_SmallsheetFrame是否修改了小票格式A的数据。
	// 3、检查数据库T_SmallsheetText是否修改小票格式A的详情。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmUpdateObjet, String dbName, SmallSheetTextMapper smallSheetTextMapper) throws Exception {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		SmallSheetFrame smallSheetFrameDB = new SmallSheetFrame();
		smallSheetFrameDB = (SmallSheetFrame) smallSheetFrameDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(smallSheetFrameDB != null, "更新退货单返回的对象为null");
		DataSourceContextHolder.setDbName(dbName);
		String error = smallSheetFrameDB.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), "数据库中的数据不合法:" + error);
		// 检查数据库T_SmallsheetFrame是否修改了小票格式A的数据。
		// 查询从表信息
		// 检查数据库T_SmallsheetText是否创建小票格式A的详情。
		SmallSheetText sst = new SmallSheetText();
		sst.setFrameId(smallSheetFrameDB.getID());
		sst.setPageIndex(1);
		sst.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		Map<String, Object> params = sst.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sst);
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> smallSheetTextDBList = smallSheetTextMapper.retrieveN(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询成功！");
		//
		List<SmallSheetText> smallSheetTextList = new ArrayList<>();
		for (BaseModel bm : smallSheetTextDBList) {
			SmallSheetText ssf = (SmallSheetText) bm;
			smallSheetTextList.add(ssf);
		}
		// 检查缓存主从表与DB中的是否一致
		smallSheetFrameDB.setListSlave1(smallSheetTextList);
		// 检查是否正确创建，与前端传进来的数据是否一致
		// 解析前端传进来的小票详情
		String str = mr.getRequest().getParameter("smallSheetTextList").trim();
		Assert.assertTrue(str != null, "从表数据格式不正确!");
		List<SmallSheetText> smallSheetTextListReq = new ArrayList<>();
		JSONArray jsonArray = JSONArray.fromObject(str);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = (JSONObject) jsonArray.get(i);
			SmallSheetText smallSheetText = (SmallSheetText) JSONObject.toBean(json, SmallSheetText.class);
			Assert.assertTrue(smallSheetText != null, "从表数据解析失败！");
			smallSheetText.setFrameId(smallSheetFrameDB.getID());
			smallSheetTextListReq.add(smallSheetText);
		}
		SmallSheetFrame smallSheetFrameUpdate = (SmallSheetFrame) bmUpdateObjet.clone();
		smallSheetFrameUpdate.setListSlave1(smallSheetTextListReq);
		smallSheetFrameUpdate.setIgnoreIDInComparision(true);
		Assert.assertTrue(smallSheetFrameUpdate.compareTo(smallSheetFrameDB) == 0, "返回的对象信息有误");
		// 检查小票格式A普通缓存是否创建,ListSlave1()中小票格式详情是否正确。
		ErrorInfo ecOut = new ErrorInfo();
		SmallSheetFrame bmCache = (SmallSheetFrame) CacheManager.getCache(dbName, EnumCacheType.ECT_SmallSheet).read1(smallSheetFrameDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bmCache != null && bmCache.getID() == smallSheetFrameDB.getID(), "普通缓存不存在本次创建出来的对象");
		Assert.assertTrue(smallSheetFrameDB.compareTo(bmCache) == 0, "缓存的小票格式与DB中的不一致");

		return true;
	}

	// 1、检查小票格式A普通缓存是否删除
	// 2、检查数据库T_SmallsheetFrame是否删除了小票格式A的数据。
	// 3、检查数据库T_SmallsheetText是否删除小票格式A的详情。
	public static boolean verifyDelete(BaseModel bmDeleteObject, SmallSheetFrameMapper smallSheetFrameMapper, String dbName) throws Exception {
		// 检查数据库T_SmallsheetFrame是否删除了小票格式A的数据。
		SmallSheetFrame ssf = (SmallSheetFrame) bmDeleteObject.clone();
		Map<String, Object> params = ssf.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, ssf);
		DataSourceContextHolder.setDbName(dbName);
		SmallSheetFrame smallSheetFrameDB = (SmallSheetFrame) smallSheetFrameMapper.retrieve1(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		Assert.assertTrue(smallSheetFrameDB == null, "数据库没有删除小票格式");
		// 检查小票格式A普通缓存是否删除
		ErrorInfo ecOut = new ErrorInfo();
		SmallSheetFrame smallSheetFrameCache = (SmallSheetFrame) CacheManager.getCache(dbName, EnumCacheType.ECT_SmallSheet).read1(bmDeleteObject.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(ecOut.getErrorCode() == EnumErrorCode.EC_NoError, "查询小票格式缓存出错");
		Assert.assertTrue(smallSheetFrameCache == null, "缓存没有删除从小票格式");
		return true;
	}

	// 1、检查配置文件的缓存是否修改。
	// 2、检查数据库T_Configgeneral，查看默认的小票格式是否修改。
	public static boolean verifyUpdateDefaultSmallSheet(MvcResult mr, BaseModel bmUpdateObjet, String dbName) throws Exception {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ConfigGeneral configGeneralDB = new ConfigGeneral();
		configGeneralDB = (ConfigGeneral) configGeneralDB.parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(configGeneralDB != null, "更新默认小票格式返回的对象为null");
		// 检查数据库T_Configgeneral，查看默认的小票格式是否修改。
		ConfigGeneral configGeneralUpdate = (ConfigGeneral) bmUpdateObjet.clone();
		Assert.assertTrue(configGeneralUpdate.compareTo(configGeneralDB) == 0, "数据库T_Configgeneral小票格式没有修改");
		// 检查配置文件的缓存是否修改。
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral configGeneralCache = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(configGeneralDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(configGeneralCache != null && configGeneralCache.getID() == configGeneralDB.getID(), "普通缓存不存在本次创建出来的对象");
		Assert.assertTrue(configGeneralCache.compareTo(configGeneralDB) == 0, "配置文件的缓存没有修改");
		return true;
	}
}
