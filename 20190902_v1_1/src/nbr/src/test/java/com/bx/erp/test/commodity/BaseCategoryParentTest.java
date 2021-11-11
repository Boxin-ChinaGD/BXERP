package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheBO;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.model.commodity.CategorySyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CategoryCP;
import com.bx.erp.test.checkPoint.CategoryParentCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

/** 包含大类和小类 */
public class BaseCategoryParentTest extends BaseMapperTest {
	protected final static String URL_CreateCategorySync = "/categorySync/createEx.bx";
	protected final static String URL_UpdateCategorySync = "/categorySync/updateEx.bx";
	protected final static String URL_CreateCategoryParent = "/categoryParent/createEx.bx";
	protected final static String URL_UpdateCategoryParent = "/categoryParent/updateEx.bx";

	public static class DataInput {
		private static CategoryParent categoryParentInput = null;
		private static Category categoryInput = null;

		public static final Category getCategory() throws CloneNotSupportedException, InterruptedException {
			categoryInput = new Category();
			categoryInput.setName("烟熏" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			categoryInput.setParentID(1);
			categoryInput.setReturnObject(EnumBoolean.EB_Yes.getIndex());
			return (Category) categoryInput.clone();
		}

		public static final CategoryParent getCategoryParent() throws Exception {
			categoryParentInput = new CategoryParent();
			categoryParentInput.setName("生品" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			categoryParentInput.setReturnObject(EnumBoolean.EB_Yes.getIndex());

			return (CategoryParent) categoryParentInput.clone();
		}

		public static final MockHttpServletRequestBuilder getCategoryBuilder(String url, MediaType contentType, Category category, HttpSession session) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType).session((MockHttpSession) session)//
					.param(Category.field.getFIELD_NAME_ID(), String.valueOf(category.getID()))//
					.param(Category.field.getFIELD_NAME_name(), category.getName())//
					.param(Category.field.getFIELD_NAME_parentID(), String.valueOf(category.getParentID()))//
					.param(Category.field.getFIELD_NAME_returnObject(), String.valueOf(category.getReturnObject()));

			return builder;
		}

		public static final MockHttpServletRequestBuilder getCategoryParentBuilder(String url, MediaType contentType, CategoryParent categoryParent, HttpSession session) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType).session((MockHttpSession) session)//
					.param(CategoryParent.field.getFIELD_NAME_ID(), String.valueOf(categoryParent.getID()))//
					.param(CategoryParent.field.getFIELD_NAME_name(), categoryParent.getName())//
					.param(CategoryParent.field.getFIELD_NAME_returnObject(), String.valueOf(categoryParent.getReturnObject()));

			return builder;
		}
	}

	/** 从Action正常创建或正常更改小类，适用于ActionTest */
	public static Category createOrUpdateCategoryViaActionWithNoError(Category c, boolean createOrUpdate, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception, UnsupportedEncodingException {
		String url = (createOrUpdate ? URL_CreateCategorySync : URL_UpdateCategorySync);
		// 创建小类
		MvcResult mr = mvc.perform(DataInput.getCategoryBuilder(url, MediaType.APPLICATION_JSON, c, session) //
				.session((MockHttpSession) session)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		//
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		CategorySyncCacheBO categorySyncCacheBO = (CategorySyncCacheBO) mapBO.get(CategorySyncCacheBO.class.getSimpleName());
		//
		if (createOrUpdate) {
			CategoryCP.verifyCreate(mr, c, posBO, categorySyncCacheBO, dbName);
		} else {
			CategoryCP.verifyUpdate(mr, c, posBO, categorySyncCacheBO, dbName);
		}
		// 把Category解析出来
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Category category = (Category) c.parse1(jsonObject.getString(BaseAction.KEY_Object));

		return category;
	}

	/** 小类从Action创建或修改失败，返回期望的错误码，适用于ActionTest */
	public static String createOrUpdateCategoryViaActionWithExpectedError(Category c, boolean createOrUpdate, MockMvc mvc, HttpSession session, String dbName, EnumErrorCode ecExpected) throws Exception, UnsupportedEncodingException {
		String url = (createOrUpdate ? URL_CreateCategorySync : URL_UpdateCategorySync);
		// 创建小类
		MvcResult mr = mvc.perform(DataInput.getCategoryBuilder(url, MediaType.APPLICATION_JSON, c, session) //
				.session((MockHttpSession) session)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr, ecExpected);
		//
		if (ecExpected != EnumErrorCode.EC_NoError) {
			// 把错误信息解析出来
			JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
			String msg = JsonPath.read(object, "$.msg");
			return msg;
		}

		return null;
	}

	/** 从Action正常创建或正常更改大类，适用于ActionTest */
	public static CategoryParent createOrUpdateCategoryParentViaActionWithNoError(CategoryParent cParent, boolean createOrUpdate, MockMvc mvc, HttpSession session, String dbName) throws Exception, UnsupportedEncodingException {
		String url = (createOrUpdate ? URL_CreateCategoryParent : URL_UpdateCategoryParent);
		// 创建大类
		MvcResult mr = mvc.perform(DataInput.getCategoryParentBuilder(url, MediaType.APPLICATION_JSON, cParent, session) //
				.session((MockHttpSession) session)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		//
		if (createOrUpdate) {
			CategoryParentCP.verifyCreate(mr, cParent, dbName);
		} else {
			CategoryParentCP.verifyUpdate(mr, cParent, dbName);
		}
		// 把CategoryParent解析出来
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		CategoryParent categoryParent = (CategoryParent) cParent.parse1(jsonObject.getString("categoryParent"));

		return categoryParent;
	}

	/** 大类从Action创建或修改失败，返回期望的错误码，适用于ActionTest */
	public static String createOrUpdateCategoryParentViaActionWithExpectedError(CategoryParent cParent, boolean createOrUpdate, MockMvc mvc, HttpSession session, String dbName, EnumErrorCode ecExpected)
			throws Exception, UnsupportedEncodingException {
		String url = (createOrUpdate ? URL_CreateCategoryParent : URL_UpdateCategoryParent);
		// 创建大类
		MvcResult mr = mvc.perform(DataInput.getCategoryParentBuilder(url, MediaType.APPLICATION_JSON, cParent, session) //
				.session((MockHttpSession) session)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr, ecExpected);
		//
		if (ecExpected != EnumErrorCode.EC_NoError) {
			// 把错误信息解析出来
			JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
			String msg = JsonPath.read(object, "$.msg");
			return msg;
		}

		return null;
	}

	/**
	 * @param category
	 * @return category mapper层正常创建小类
	 */
	public static Category createCategoryViaMapper(Category category) {
		// 字段验证
		String error = category.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = category.getCreateParam(BaseBO.INVALID_CASE_ID, category);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category createCategory = (Category) categoryMapper.create(params);
		//
		category.setIgnoreIDInComparision(true);
		if (category.compareTo(createCategory) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(createCategory != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		error = createCategory.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		return createCategory;
	}

	/**
	 * @param category
	 * @return category mapper层正常修改小类
	 */
	public static Category updateCategoryViaMapper(Category category) {
		// 字段验证
		String error = category.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = category.getUpdateParam(BaseBO.INVALID_CASE_ID, category);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category updateCategory = (Category) categoryMapper.update(params);
		//
		category.setIgnoreIDInComparision(true);
		if (category.compareTo(updateCategory) != 0) {
			Assert.assertTrue(false, "修改的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(updateCategory != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象成功");
		error = updateCategory.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		return updateCategory;
	}

	// mapper层正常创建大类
	public static CategoryParent createCategoryParentViaMapper(CategoryParent categoryParent) {
		// 字段验证
		String error = categoryParent.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> createParam = categoryParent.getCreateParam(BaseBO.INVALID_CASE_ID, categoryParent);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategoryParent createC1 = (CategoryParent) categoryParentMapper.create(createParam);
		//
		categoryParent.setIgnoreIDInComparision(true);
		if (categoryParent.compareTo(createC1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createC1 != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		// 字段验证
		error = createC1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		return createC1;
	}

	// mapper层正常修改大类
	public static CategoryParent updateCategoryParentViaMapper(CategoryParent categoryParent) {
		// 字段验证
		String error = categoryParent.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> updateParam = categoryParent.getUpdateParam(BaseBO.INVALID_CASE_ID, categoryParent);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategoryParent updateU1 = (CategoryParent) categoryParentMapper.update(updateParam);
		//
		categoryParent.setIgnoreIDInComparision(true);
		if (categoryParent.compareTo(updateU1) != 0) {
			Assert.assertTrue(false, "修改的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(updateU1 != null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象成功");
		// 字段验证
		error = updateU1.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		return updateU1;
	}

	public static List<BaseModel> queryCategoryViaMapper(Category category, String dbName) {
		Map<String, Object> param = category.getRetrieveNParam(BaseBO.INVALID_CASE_ID, category);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> list = categoryMapper.retrieveN(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		return list;
	}

	// 删除并查询验证小类数据
	public static void deleteAndVerifyCategory(Category createCategory) {
		// 删除添加的测试数据
		Map<String, Object> paramsForDelete = createCategory.getDeleteParam(BaseBO.INVALID_CASE_ID, createCategory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryMapper.delete(paramsForDelete); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 验证是否成功删除
		Map<String, Object> paramsRetrieve1 = createCategory.getRetrieve1Param(BaseBO.INVALID_CASE_ID, createCategory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category c = (Category) categoryMapper.retrieve1(paramsRetrieve1);
		Assert.assertTrue(c == null && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");
	}

	// 删除并查询验证大类数据
	public static void deleteAndVerifyCategoryParent(CategoryParent categoryParent) {
		// 删除大类数据
		Map<String, Object> deleteParam2 = categoryParent.getDeleteParam(BaseBO.INVALID_CASE_ID, categoryParent);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryParentMapper.delete(deleteParam2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象成功");
		// 验证是否成功删除
		Map<String, Object> retrieve1Param = categoryParent.getRetrieve1Param(BaseBO.INVALID_CASE_ID, categoryParent);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategoryParent retrieve1C = (CategoryParent) categoryParentMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve1C == null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象成功");

	}

	/**
	 * @param bmList
	 *            RN返回的需要同步的类别List
	 */
	public static List<Category> getCategoryListIfAllSync(List<BaseModel> bmList, Map<String, BaseBO> mapBO) {
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		//
		List<Pos> posList = Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_CommoditySyncInfo).readN(false, false);
		List<Category> categoryList = new ArrayList<Category>();

		BaseSyncCache baseSyncCache = new BaseSyncCache();
		for (BaseModel bm : bmList) {
			Category category = (Category) bm;

			// 获取该商品的同步块信息
			for (BaseModel bm2 : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bm2;
				if (baseSyncCache.getSyncData_ID() == category.getID()) {
					if (baseSyncCache.getListSlave1() != null && baseSyncCache.getListSlave1().size() == (posList.size() - 1)) {
						category.setIsSync(1); // 设isSync为1，表示该商品同步块已经完全同步
					}
				}
			}
			categoryList.add(category);
		}
		return categoryList;
	}

	//
	public static List<BaseModel> categorySyncCacheRetrieveN(CategorySyncCache csc) {
		//
		Map<String, Object> paramsForRetrieveN = csc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, csc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> cscList = categorySyncCacheMapper.retrieveN(paramsForRetrieveN);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		return cscList;
	}

	/** 删除DB中的同步块信息 */
	public static void deleteCategorySyncCache(CategorySyncCache categorySyncCache) {
		//
		Pos pos = new Pos();
		pos.setStatus(EnumStatusPos.ESP_Active.getIndex());
		pos.setShopID(BaseAction.INVALID_ID);
		pos.setPos_SN("");
		//
		Map<String, Object> paramsStatus = pos.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pos);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> posList = posMapper.retrieveN(paramsStatus);
		Assert.assertTrue(posList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsStatus.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		CategorySyncCacheDispatcher dispatcher = new CategorySyncCacheDispatcher();
		for (int i = 0; i < posList.size(); i++) {
			dispatcher.setSyncCacheID(categorySyncCache.getID());
			dispatcher.setPos_ID(posList.get(i).getID());
			Map<String, Object> params = dispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, dispatcher);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			CategorySyncCacheDispatcher dispatcherCreate = (CategorySyncCacheDispatcher) categorySyncCacheDispatcherMapper.create(params);
			dispatcher.setIgnoreIDInComparision(true);
			if (dispatcher.compareTo(dispatcherCreate) != 0) {
				Assert.assertTrue(false, "创建对象的字段与DB读出的字段不相等！");
			}
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		//
		Map<String, Object> params = categorySyncCache.getDeleteParam(BaseBO.INVALID_CASE_ID, categorySyncCache);
		categorySyncCacheMapper.delete(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static void verificationDelete_Category(Category category) {
		//
		String error = category.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(error), error);
		//
		Map<String, Object> paramsRetrieve = category.getRetrieve1Param(BaseBO.INVALID_CASE_ID, category);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category c = (Category) categoryMapper.retrieve1(paramsRetrieve);
		Assert.assertTrue(c == null && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsRetrieve.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static void deleteCategoryParent(int id) {
		//
		CategoryParent c = new CategoryParent();
		c.setID(id);
		Map<String, Object> deleteParam = c.getDeleteParam(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryParentMapper.delete(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象成功");
	}
}
