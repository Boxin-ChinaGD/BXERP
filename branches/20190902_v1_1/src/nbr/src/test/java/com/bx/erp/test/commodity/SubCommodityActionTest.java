package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

@WebAppConfiguration
public class SubCommodityActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void testCreate() throws Exception {
		Shared.printTestMethodStartInfo();

		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity subComm = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(1);
		comm2.setRefCommodityMultiple(3);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + comm2.getBarcodes() + ";");
		Commodity c2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);

		MvcResult mr = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=49&subCommodityID=" + subComm.getID() + "&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		SubCommodity sub = new SubCommodity();
		sub.setCommodityID(49);
		sub.setSubCommodityID(subComm.getID());
		BaseCommodityTest.deleteSubCommodityViaMapper(sub);

		// case1:添加相同的商品到同一组合商品中,错误码为1
		MvcResult mr1 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=49&subCommodityID=9&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_Duplicated);

		// case2：将普通商品当做组合商品插入子商品,错误码为7
		MvcResult mr2 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=35&subCommodityID=10&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);

		// case3:用不存在的商品创建组合商品，错误码为7
		MvcResult mr3 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=999999999&subCommodityID=10&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);

		// case4:用不存在的子商品创建组合商品，错误码为7
		MvcResult mr4 = mvc
				.perform(get("/subCommodity/createEx.bx?commodityID=45&subCommodityID=999999999&subCommodityNO=7").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);

		// Case5:添加多包装商品为子商品，错误码为7
		MvcResult mr5 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=45&subCommodityID=51&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);

		// Case6:添加组合商品为子商品，错误码为7
		MvcResult mr6 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=45&subCommodityID=49&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_BusinessLogicNotDefined);

		// Case7:没有权限进行添加
		MvcResult mr7 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=49&subCommodityID=7&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);

		// case8：将多包装商品当做组合商品插入子商品,错误码为7
		MvcResult mr8 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=" + c2.getID() + "&subCommodityID=10&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_BusinessLogicNotDefined);

		Shared.caseLog("case9:将服务商品当做组合商品插入子商品");
		Commodity serviceCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity createServiceCommodity = BaseCommodityTest.createCommodityViaAction(serviceCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		MvcResult mr9 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=" + createServiceCommodity.getID() + "&subCommodityID=10&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_BusinessLogicNotDefined);

		Shared.caseLog("case10:组合商品添加服务商品为子商品");
		MvcResult mr10 = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=45&subCommodityID=" + createServiceCommodity.getID() + "&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreate11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:组合商品价格大于" + FieldFormat.MAX_OneCommodityPrice + ",创建失败");
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity subComm = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=49&subCommodityID=" + subComm.getID() + "&subCommodityNO=7&" + SubCommodity.field.getFIELD_NAME_price() + "=" + (FieldFormat.MAX_OneCommodityPrice + 0.01))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		//
		BaseCommodityTest.deleteCommodityViaAction(subComm, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testDelete() throws Exception {
		Shared.printTestMethodStartInfo();
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity subComm = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr = mvc.perform(//
				get("/subCommodity/createEx.bx?commodityID=45&subCommodityID=" + subComm.getID() + "&subCommodityNO=7")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		MvcResult mr1 = mvc.perform(//
				get("/subCommodity/deleteEx.bx?commodityID=45&subCommodityID=" + subComm.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1);

		System.out.println("----------------------case2:，没有权限进行操作--------------------------------------");
		MvcResult mr2 = mvc.perform(//
				get("/subCommodity/deleteEx.bx?commodityID=45")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				get("/subCommodity/retrieveNEx.bx?commodityID=2")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("----------------------case2:，没有权限进行操作--------------------------------------");
//		MvcResult mr1 = mvc.perform(//
//				get("/subCommodity/retrieveNEx.bx?commodityID=2")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}
}
