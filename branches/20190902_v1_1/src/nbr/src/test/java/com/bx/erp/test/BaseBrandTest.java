
package com.bx.erp.test;

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
import com.bx.erp.action.bo.commodity.BrandSyncCacheBO;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.test.checkPoint.BrandCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BaseBrandTest extends BaseMapperTest {
	protected final static String URL_CreateBrandSync = "/brandSync/createEx.bx";
	protected final static String URL_UpdateBrandSync = "/brandSync/updateEx.bx";

	public static class DataInput {
		private static Brand brandInput = null;

		public static final Brand getBrand() throws CloneNotSupportedException, InterruptedException {
			brandInput = new Brand();
			brandInput.setName("士力架88" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			brandInput.setReturnObject(1);
			return (Brand) brandInput.clone();
		}

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Brand brand, HttpSession session) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType).session((MockHttpSession) session)//
					.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brand.getID()))//
					.param(Brand.field.getFIELD_NAME_name(), brand.getName())//
					.param(Brand.field.getFIELD_NAME_returnObject(), String.valueOf(brand.getReturnObject()));

			return builder;
		}
	}

	/**
	 * @param brand
	 * @return brand对象
	 * @describe mapper层创建品牌测试数据
	 */
	public static final Brand createBrandViaMapper(Brand brand) {
		String error = brand.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForCreate = brand.getCreateParam(BaseBO.INVALID_CASE_ID, brand);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandCreate = (Brand) brandMapper.create(paramsForCreate); // ...
		//
		System.out.println("创建品牌对象所需要的对象：" + brand);
		System.out.println("创建出的品牌对象是：" + brandCreate);
		//
		brand.setIgnoreIDInComparision(true);
		if (brand.compareTo(brandCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(brandCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		error = brandCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		return brandCreate;
	}

	public static final List<BaseModel> retrieveNViaMapper(Brand brand, String dbName) {
		String error = brand.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = brand.getRetrieveNParam(BaseBO.INVALID_CASE_ID, brand);
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> list = brandMapper.retrieveN(params); // ...
		//
		Assert.assertTrue(list != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return list;
	}

	/**
	 * @param brand
	 * @return brand对象
	 * @describe mapper层更改品牌测试数据
	 */
	public static final Brand updateBrandViaMapper(Brand brand) {
		//
		String error = brand.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate = brand.getUpdateParam(BaseBO.INVALID_CASE_ID, brand);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandUpdate = (Brand) brandMapper.update(paramsForUpdate);
		//
		System.out.println("update品牌对象所需要的对象：" + brand);
		System.out.println("update的品牌对象是：" + brandUpdate);
		//
		brand.setIgnoreIDInComparision(true);
		if (brand.compareTo(brandUpdate) != 0) {
			Assert.assertTrue(false, "update的对象的字段与DB读出的相等");
		}
		Assert.assertTrue(brandUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		error = brandUpdate.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		return brandUpdate;
	}

	/** 从Action正常创建或正常更改品牌，适用于ActionTest */
	public static Brand createOrUpdateBrandViaActionWithNoError(Brand b, boolean createOrUpdate, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception, UnsupportedEncodingException {
		String url = (createOrUpdate ? URL_CreateBrandSync : URL_UpdateBrandSync);
		// 创建品牌
		MvcResult mr = mvc.perform(DataInput.getBuilder(url, MediaType.APPLICATION_JSON, b, session) //
				.session((MockHttpSession) session)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		//
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		BrandSyncCacheBO brandSyncCacheBO = (BrandSyncCacheBO) mapBO.get(BrandSyncCacheBO.class.getSimpleName());
		//
		if (createOrUpdate) {
			BrandCP.verifyCreate(mr, b, posBO, brandSyncCacheBO, dbName);
		} else {
			BrandCP.verifyUpdate(mr, b, posBO, brandSyncCacheBO, dbName);
		}

		// 把brand解析出来
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Brand brand = (Brand) b.parse1(jsonObject.getString(BaseAction.KEY_Object));
		return brand;

	}

	/** 品牌从Action创建失败或修改失败，返回期望的错误码，适用于ActionTest */
	public static String createOrUpdateBrandViaActionWithExpectedError(Brand b, boolean createOrUpdate, MockMvc mvc, HttpSession session, String dbName, EnumErrorCode ecExpected) throws Exception, UnsupportedEncodingException {
		String url = (createOrUpdate ? URL_CreateBrandSync : URL_UpdateBrandSync);
		// 创建品牌
		MvcResult mr = mvc.perform(DataInput.getBuilder(url, MediaType.APPLICATION_JSON, b, session) //
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
	 * @param bmList
	 *            RN返回的需要同步的品牌List
	 */
	public static List<Brand> queryBrandListIfAllSync(List<BaseModel> bmList, Map<String, BaseBO> mapBO) {
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		//
		List<Pos> posList = Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_CommoditySyncInfo).readN(false, false);
		List<Brand> brandList = new ArrayList<Brand>();

		BaseSyncCache baseSyncCache = new BaseSyncCache();
		for (BaseModel bm : bmList) {
			Brand brand = (Brand) bm;

			// 获取该品牌的同步块信息
			for (BaseModel bm2 : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bm2;
				if (baseSyncCache.getSyncData_ID() == brand.getID()) {
					if (baseSyncCache.getListSlave1() != null && baseSyncCache.getListSlave1().size() == (posList.size() - 1)) {
						brand.setIsSync(EnumBoolean.EB_Yes.getIndex()); // 设isSync为1，表示该品牌同步块已经完全同步
					}
				}
			}
			brandList.add(brand);
		}
		return brandList;
	}

	/** mapper层删除品牌测试数据并进行验证 */
	public static void deleteAndVerifyBrand(Brand brandCreate) {
		// 删除添加的测试数据
		Map<String, Object> paramsForDelete = brandCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, brandCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		brandMapper.delete(paramsForDelete);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 验证是否成功删除
		Map<String, Object> paramsRetrieve = brandCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, brandCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandRetrieve1 = (Brand) brandMapper.retrieve1(paramsRetrieve);// ...
		Assert.assertNull(brandRetrieve1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}
}
