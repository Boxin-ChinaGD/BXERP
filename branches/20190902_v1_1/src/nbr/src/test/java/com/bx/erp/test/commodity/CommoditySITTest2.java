package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CommoditySITTest2 extends BaseActionTest {
	protected final String barcode1 = "111111" + System.currentTimeMillis() % 1000000;
	protected final String barcode2 = "222222" + System.currentTimeMillis() % 1000000;
	protected final String barcode3 = "333333" + System.currentTimeMillis() % 1000000;
	protected final String barcode4 = "444444" + System.currentTimeMillis() % 1000000;
	protected final String barcode5 = "555555" + System.currentTimeMillis() % 1000000;

	protected final String packageUnit1 = "1";
	protected final String packageUnit2 = "2";
	protected final String packageUnit3 = "3";
	protected final String packageUnit4 = "4";
	protected final String packageUnit5 = "5";

	protected final String refCommodityMultiple1 = "0";
	protected final String refCommodityMultiple2 = "2";
	protected final String refCommodityMultiple3 = "3";
	protected final String refCommodityMultiple4 = "4";
	protected final String refCommodityMultiple5 = "5";

	protected final String priceRetail1 = "10.0f";
	protected final String priceRetail2 = "20.0f";
	protected final String priceRetail3 = "30.0f";
	protected final String priceRetail4 = "40.0f";
	protected final String priceRetail5 = "50.0f";

	protected final String priceVIP1 = "1";
	protected final String priceVIP2 = "2";
	protected final String priceVIP3 = "3";
	protected final String priceVIP4 = "4";
	protected final String priceVIP5 = "5";

	protected final String priceWholesale1 = "1";
	protected final String priceWholesale2 = "2";
	protected final String priceWholesale3 = "3";
	protected final String priceWholesale4 = "4";
	protected final String priceWholesale5 = "5";

	protected final String name1 = "111" + System.currentTimeMillis() % 1000000;
	protected final String name2 = "222" + System.currentTimeMillis() % 1000000;
	protected final String name3 = "322" + System.currentTimeMillis() % 1000000;
	protected final String name4 = "444" + System.currentTimeMillis() % 1000000;
	protected final String name5 = "555" + System.currentTimeMillis() % 1000000;

	private Staff staff;

	@BeforeClass
	public void setup() {
		super.setUp();
		staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	/** C ????????????????????????A <br />
	 * U ?????????A??????2????????????2???3 ???2???3???<br />
	 * U ?????????A???????????????3??????????????????4???5 ???2???4???5???<br />
	 * U ?????????A???????????????2????????????4???????????????5??????????????? ???2???4???5???<br />
	 * R1 RN<br />
	 * D ????????????A???????????????????????????????????????<br />
	 * D ?????????????????????????????????<br />
	 */
	@Test
	public void test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.resetPOS(mvc, 1);
		HttpSession session = Shared.getPosLoginSession(mvc, 1);

		System.out.println("------------------????????????A------------------------");

		Commodity commA = BaseCommodityTest.DataInput.getCommodity();
		commA.setOperatorStaffID(staff.getID());
		;
		Map<String, Object> params = commA.getCreateParamEx(BaseBO.INVALID_CASE_ID, commA);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		Commodity commCreated = (Commodity) bmList.get(0).get(0);

		commA.setIgnoreIDInComparision(true);
		if (commA.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		Barcodes b = new Barcodes();
		b.setCommodityID(commCreated.getID());
		b.setBarcode(barcode1);
		b.setOperatorStaffID(staff.getID());
		barcodesBO.createObject(staff.getID(), BaseBO.INVALID_CASE_ID, b);

		if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false);
		}

		System.out.println("------------------?????????A??????2????????????2???3 ------------------------");
		commCreated.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple3 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail3 + ";" //
				+ priceVIP1 + "," + priceVIP2 + "," + priceVIP3 + ";" //
				+ priceWholesale1 + "," + priceWholesale2 + "," + priceWholesale3 + ";" //
				+ name1 + "," + name2 + "," + name3 + ";");
		commCreated.setProviderIDs("7,2,3");
		MvcResult mrUpdate1 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commCreated, session))//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mrUpdate1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<?> commList = commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreated);

		assertTrue(commList.size() == 2);// ...????????????????????????

		System.out.println("------------------?????????A???????????????3??????????????????4???5 ------------------------");

		commCreated.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode4 + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit4 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail4 + "," + priceRetail5 + ";" //
				+ priceVIP1 + "," + priceVIP2 + "," + priceVIP4 + "," + priceVIP5 + ";" //
				+ priceWholesale1 + "," + priceWholesale2 + "," + priceWholesale4 + "," + priceWholesale5 + ";" + name1 + "," + name2 + "," + name4 + "," + name5 + ";");
		commCreated.setProviderIDs("7");
		MvcResult mrUpdate2 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commCreated, session)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mrUpdate2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commList = commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreated);
		assertTrue(commList.size() == 3);// ...????????????????????????

		System.out.println("------------------?????????A???????????????2????????????4???????????????5??????????????? ------------------------");

		commCreated.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + "45454" + System.currentTimeMillis() % 1000000 + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit4 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + "1" + System.currentTimeMillis() % 10 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail4 + "," + "1" + System.currentTimeMillis() % 100000 + ";" //
				+ priceVIP1 + "," + priceVIP2 + "," + priceVIP4 + "," + priceVIP5 + ";" //
				+ priceWholesale1 + "," + priceWholesale2 + "," + priceWholesale4 + "," + priceWholesale5 + ";" + "??????1" + name1 + "," + "??????2" + name2 + "," + "??????4" + name4 + "," + "??????5" + name5 + ";");
		commCreated.setProviderIDs("7");
		MvcResult mrUpdate3 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commCreated, session)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mrUpdate3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commList = commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreated);
		assertTrue(commList.size() == 3);// ...????????????????????????

		System.out.println("------------------D ????????????A??????????????????????????????????????? ------------------------");
		// ??????????????????????????????
		// ?????????????????????????????????????????????
		Map<String, Object> params2 = commCreated.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() != 0, "??????????????????");
		// ????????????????????????????????????
		for (int i = 0; i < retrieveNMultiPackageCommodity.size(); i++) {
			Commodity reCommodity = (Commodity) retrieveNMultiPackageCommodity.get(i);
			MvcResult mr2 = mvc.perform(//
					get("/commoditySync/deleteEx.bx?ID=" + reCommodity.getID() + "&type=" + reCommodity.getType()) //
							.contentType(MediaType.APPLICATION_JSON) //
							.session((MockHttpSession) session)) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();

			Shared.checkJSONErrorCode(mr2);
		}

		MvcResult mrDelete1 = mvc.perform( //
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID()) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mrDelete1);// ...????????????????????????????????????R1????????????????????????????????????commCreated.getID()????????????????????????????????????

		Map<String, Object> paramsR1 = commA.getRetrieve1Param(BaseBO.CASE_Commodity_RetrieveInventory, commCreated);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity commR1 = (Commodity) commodityMapper.retrieveInventory(paramsR1);
		assertTrue(commR1 == null);

	}

}
