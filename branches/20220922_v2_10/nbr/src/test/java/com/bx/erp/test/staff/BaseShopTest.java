package com.bx.erp.test.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.model.Staff;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.Company;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseShopTest extends BaseMapperTest {
	public static final int RETRIEVE1EX_NOEXIST_CASEID = 0;
	public static final int RETRIEVE1EX_CASEID1 = 1;
	public static final int RETRIEVE1EX_DELETESTATUS_CASEID2 = 2;

	public static class DataInput {
		private static Shop shopInput = null;
		private static ShopDistrict pd = null;

		public static final Shop getShop() throws CloneNotSupportedException, InterruptedException {
			shopInput = new Shop();

			shopInput.setName("博昕" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			shopInput.setCompanyID(1);
			shopInput.setAddress("岗顶");
			shopInput.setStatus(1);
			shopInput.setLongitude(123.123d);
			shopInput.setLatitude(123.12d);
			shopInput.setKey("123456");
			shopInput.setBxStaffID(1);
			shopInput.setRemark("333");
			shopInput.setDistrictID(1);

			return (Shop) shopInput.clone();
		}

		public static final ShopDistrict getShopDistrict() throws CloneNotSupportedException, InterruptedException {
			pd = new ShopDistrict();
			pd.setName("昆明1" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			return (ShopDistrict) pd.clone();
		}
	}

	public static Shop createViaMapper(Shop shop) {
		String err = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> paramsForCreate = shop.getCreateParam(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopCreate = (Shop) shopMapper.create(paramsForCreate); // ...
		//
		Assert.assertTrue(shopCreate != null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = shopCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		shop.setIgnoreIDInComparision(true);
		if (shop.compareTo(shopCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return shopCreate;
	}

	public static List<List<BaseModel>> retrieveNExViaMapper(Shop shop) {
		Map<String, Object> retrieveNParamEx = shop.getRetrieveNParamEx(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = shopMapper.retrieveNEx(retrieveNParamEx);
		//
		Assert.assertTrue(list.size() >= 0//
				&& EnumErrorCode.values()[Integer.parseInt(retrieveNParamEx.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				retrieveNParamEx.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		String err;
		for (List<BaseModel> bmList : list) {
			for (BaseModel bm : bmList) {
				if (bm instanceof Shop) {
					((Shop) bm).setLongitude(shop.getLongitude());
					((Shop) bm).setLatitude(shop.getLatitude());
					err = ((Shop) bm).checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertTrue("".equals(err), err);
				} else if (bm instanceof Company) {
					err = ((Company) bm).checkCreate(BaseBO.CASE_SpecialResultVerification);
					Assert.assertTrue("".equals(err), err);
				} else {
					err = ((BxStaff) bm).checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertTrue("".equals(err), err);
				}
			}
		}

		return list;
	}

	public static List<BaseModel> retrieveNViaMapper(Shop shop) {
		Map<String, Object> paramsRN = shop.getRetrieveNParam(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = shopMapper.retrieveN(paramsRN);
		//
		Assert.assertTrue(list.size() >= 0 && //
				EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : list) {
			((Shop) bm).setLongitude(shop.getLongitude());
			((Shop) bm).setLatitude(shop.getLatitude());
			err = ((Shop) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return list;
	}

	public static Shop updateViaMapper(Shop shop) {
		String err = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		Map<String, Object> paramsUpdate = shop.getUpdateParam(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopUpdate = (Shop) shopMapper.update(paramsUpdate);
		//
		Assert.assertTrue(shopUpdate != null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = shopUpdate.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		if (shop.compareTo(shopUpdate) != 0) {
			Assert.assertTrue(false, "修改的对象和DB的不一致，修改失败!");
		}

		return shopUpdate;
	}

	public static List<List<BaseModel>> retrieve1ExViaMapper(int shopID, int iCaseID) {
		Shop s = new Shop();
		s.setID(shopID);
		Map<String, Object> params = s.getRetrieve1Param(BaseBO.INVALID_CASE_ID, s);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> listBm = shopMapper.retrieve1Ex(params);

		Assert.assertTrue(listBm.size() >= 0//
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		String err;
		for (List<BaseModel> bmList : listBm) {
			for (BaseModel bm : bmList) {
				if (bm instanceof Shop) {
					err = ((Shop) bm).checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertEquals(err, "");
				} else if (bm instanceof Company) {
					err = ((Company) bm).checkCreate(BaseBO.CASE_SpecialResultVerification);
					Assert.assertEquals(err, "");
				} else {
					err = ((BxStaff) bm).checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertEquals(err, "");
				}
			}
		}

		// case：检查不存在的ID case1：检查一般的shop case2：查询删除后的shop
		if (iCaseID == RETRIEVE1EX_NOEXIST_CASEID) {
			assertTrue(listBm.get(0).size() == 0);
		} else if (iCaseID == RETRIEVE1EX_CASEID1) {
			Shop shop = (Shop) listBm.get(0).get(0);
			assertTrue(shop != null && shop.getID() == shopID);

			Company comany = (Company) listBm.get(1).get(0);
			assertTrue(shop.getCompanyID() == comany.getID());

			BxStaff bxStaff = (BxStaff) listBm.get(2).get(0);
			assertTrue(shop.getBxStaffID() == bxStaff.getID());
		} else if (iCaseID == RETRIEVE1EX_DELETESTATUS_CASEID2) {
			Shop shop = (Shop) listBm.get(0).get(0);
			assertTrue(shop != null && shop.getID() == shopID && shop.getStatus() == Shop.EnumStatusShop.ESS_ArrearageLocking.getIndex());

			Company comany = (Company) listBm.get(1).get(0);
			assertTrue(shop.getCompanyID() == comany.getID());

			BxStaff bxStaff = (BxStaff) listBm.get(2).get(0);
			assertTrue(shop.getBxStaffID() == bxStaff.getID());
		}

		return listBm;
	}

	/**
	 * @param staffID
	 *            门店下的员工ID 用于检查是否删除
	 * @param posID
	 *            门店下的POSID
	 */
	public static void deleteViaMapper(Shop shop, int[] staffID, int[] posID) {
		Map<String, Object> paramsDelete = shop.getDeleteParamEx(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = shopMapper.deleteEx(paramsDelete);
		//
		Assert.assertTrue(list.size() == 2//
				&& EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检查pos是否被删除
		Pos pos = new Pos();
		for (int i = 0; i < posID.length; i++) {
			pos.setID(posID[i]);
			Map<String, Object> params = pos.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pos);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Pos posR1 = (Pos) posMapper.retrieve1(params);
			//
			assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(posR1.getStatus() != EnumBoolean.EB_NO.getIndex(), "POS未被删除！");
		}

		// 检查staff是否被删除
		Staff staff = new Staff();
		for (int i = 0; i < staffID.length; i++) {
			staff.setID(staffID[i]);
			staff.setInvolvedResigned(EnumBoolean.EB_Yes.getIndex());
			Staff staffR1 = BaseStaffTest.retrieve1StaffViaMapper(staff);
			Assert.assertTrue(staffR1.getStatus() != EnumBoolean.EB_NO.getIndex(), "Staff未被删除！");
		}

		// 检查shop是否被删除
		BaseShopTest.retrieve1ExViaMapper(shop.getID(), BaseShopTest.RETRIEVE1EX_DELETESTATUS_CASEID2);
	}

	public static Shop retrieve1ViaAction(Shop shop, MockMvc mvc, HttpSession session) throws Exception {
		MvcResult mr = mvc.perform(//
				get("/shop/retrieve1Ex.bx?" + Shop.field.getFIELD_NAME_ID() + "=" + shop.getID() + "&" + Shop.field.getFIELD_NAME_dbName() + "=" + Shared.DBName_Test)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		return (Shop) Shared.parse1Object(mr, shop, BaseAction.KEY_Object);
	}

	public static ShopDistrict createShopDistrictViaMapper(ShopDistrict shopDistrict) {
		String err = shopDistrict.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = shopDistrict.getCreateParam(BaseBO.INVALID_CASE_ID, shopDistrict);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ShopDistrict sdCreate = (ShopDistrict) shopDistrictMapper.create(params);
		//
		Assert.assertTrue(sdCreate != null//
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = sdCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		shopDistrict.setIgnoreIDInComparision(true);
		if (shopDistrict.compareTo(sdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return sdCreate;
	}
	
	public static ShopDistrict retrieve1ShopDistrictViaMapper(ShopDistrict shopDistrict) {
		Map<String, Object> paramsR1 = shopDistrict.getRetrieve1Param(BaseBO.INVALID_CASE_ID, shopDistrict);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ShopDistrict sdR1 = (ShopDistrict) shopDistrictMapper.retrieve1(paramsR1);
		//
		Assert.assertTrue(sdR1 != null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err = sdR1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		return sdR1;
	}
	
	public static List<BaseModel> retrieveNShopDistrictViaMapper(){
		ShopDistrict shopDistrict = new ShopDistrict();
		Map<String, Object> paramsReterivevN = shopDistrict.getRetrieveNParam(BaseBO.INVALID_CASE_ID, shopDistrict);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> pdReterivevN = (List<BaseModel>) shopDistrictMapper.retrieveN(paramsReterivevN);
		//
		Assert.assertTrue(pdReterivevN.size() >= 0//
				&& EnumErrorCode.values()[Integer.parseInt(paramsReterivevN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsReterivevN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return pdReterivevN;
	}

	public static Shop createViaCompanyAction(Shop shop, MockMvc mvc, Map<String, BaseBO> mapBO, MockHttpSession sessionOP, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/company/createShopEx.bx")//
						.param(Shop.field.getFIELD_NAME_name(), shop.getName())//
						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(shop.getCompanyID()))//
						.param(Shop.field.getFIELD_NAME_address(), shop.getAddress())//
						.param(Shop.field.getFIELD_NAME_districtID(), String.valueOf(shop.getDistrictID()))//
						.param(Shop.field.getFIELD_NAME_status(), String.valueOf(shop.getStatus()))//
						.param(Shop.field.getFIELD_NAME_longitude(), String.valueOf(shop.getLongitude()))//
						.param(Shop.field.getFIELD_NAME_latitude(), String.valueOf(shop.getLatitude()))//
						.param(Shop.field.getFIELD_NAME_key(), String.valueOf(shop.getRemark()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOP)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, eec);
		if(eec == EnumErrorCode.EC_NoError) {
			Shop shopCreated = (Shop) Shared.parse1Object(mr, shop, BaseAction.KEY_Object);
			if(shop.compareTo(shopCreated) != 0) {
				Assert.assertTrue(false, "创建的对象与数据库返回的对象不相等！");
			}
			Warehouse WarehouseCreated = (Warehouse) Shared.parse1Object(mr, new Warehouse(), BaseAction.KEY_Object2);
			Assert.assertTrue(WarehouseCreated != null, "没有返回门店对应的仓库");
			return shopCreated;
		} else {
			return null;
		}
	}
	
	public static Shop updateViaCompanyAction(Shop shop, MockMvc mvc, Map<String, BaseBO> mapBO, MockHttpSession sessionOP, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/company/updateShopEx.bx")//
						.param(Shop.field.getFIELD_NAME_ID(), String.valueOf(shop.getID()))//
						.param(Shop.field.getFIELD_NAME_name(), shop.getName())//
						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(shop.getCompanyID()))//
						.param(Shop.field.getFIELD_NAME_address(), shop.getAddress())//
						.param(Shop.field.getFIELD_NAME_districtID(), String.valueOf(shop.getDistrictID()))//
						.param(Shop.field.getFIELD_NAME_status(), String.valueOf(shop.getStatus()))//
						.param(Shop.field.getFIELD_NAME_longitude(), String.valueOf(shop.getLongitude()))//
						.param(Shop.field.getFIELD_NAME_latitude(), String.valueOf(shop.getLatitude()))//
						.param(Shop.field.getFIELD_NAME_key(), String.valueOf(shop.getRemark()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOP)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, eec);
		if(eec == EnumErrorCode.EC_NoError) {
			Shop shopUpdated = (Shop) Shared.parse1Object(mr, shop, BaseAction.KEY_Object);
			if(shop.compareTo(shopUpdated) != 0) {
				Assert.assertTrue(false, "创建的对象与数据库返回的对象不相等！");
			}
			return shopUpdated;
		} else {
			return null;
		}
	}
	
	public static void deleteViaCompanyAction(Shop shop, MockMvc mvc, Map<String, BaseBO> mapBO, MockHttpSession sessionOP, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/company/deleteShopEx.bx")//
						.param(Shop.field.getFIELD_NAME_companyID(), String.valueOf(shop.getCompanyID()))//
						.param(Shop.field.getFIELD_NAME_ID(), String.valueOf(shop.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOP)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, eec);
	}
}
