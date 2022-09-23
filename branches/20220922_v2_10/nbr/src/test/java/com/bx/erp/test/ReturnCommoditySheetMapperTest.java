package com.bx.erp.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class ReturnCommoditySheetMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:Create ReturnCommoditySheet Test");
		ReturnCommoditySheet returnCommoditySheet = BaseReturnCommoditySheetTest.createReturnCommoditySheet();
		createReturnCommoditySheetCommodity(returnCommoditySheet.getID(), 1, 1, 1, "克");
		//
		System.out.println("【创建退货单】测试成功！");
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "当前帐户不允许创建退货单";
		Shared.caseLog("CASE2:" + message);
		ReturnCommoditySheet rcs2 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs2.setStaffID(BaseAction.INVALID_ID);
		Map<String, Object> params2 = rcs2.getCreateParam(BaseBO.INVALID_CASE_ID, rcs2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate2 = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params2); // ...
		//
		Assert.assertTrue(rcsCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "该供应商不存在，请重新选择供应商";
		Shared.caseLog("CASE3:" + message);
		ReturnCommoditySheet rcs3 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs3.setProviderID(BaseAction.INVALID_ID);
		Map<String, Object> params3 = rcs3.getCreateParam(BaseBO.INVALID_CASE_ID, rcs3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate3 = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params3); // ...
		//
		Assert.assertTrue(rcsCreate3 == null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieve1ExTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Retrieve1 ReturnCommoditySheet Test ------------------------");

		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		// 创建退货商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), 1, 1, 1, "克");

		ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		rcs2.setID(rcsCreate.getID());

		Map<String, Object> paramsRetrieve1 = rcs2.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, rcs2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> rcsRetrieve1List = returnCommoditySheetMapper.retrieve1Ex(paramsRetrieve1);

		Assert.assertTrue(rcsRetrieve1List.get(0).size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		Assert.assertTrue(rcsRetrieve1List.get(1).size() != 0, "退货单不能没有退货商品");
		// 验证从表数据
		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) rcsRetrieve1List.get(1).get(0);
		assertTrue(rcsc.getReturnCommoditySheetID() == rcs2.getID(), "测试时！返回的数据不是期望的，期望的是返回的退货商品的退货单ID等于返回的退货单ID");
		//
		System.out.println(rcsRetrieve1List == null ? "" : rcsRetrieve1List);
		System.out.println("【查询单个退货单】测试成功！");

		System.out.println("\n------------------------ case2: 退货单id不存在时 ------------------------");

		ReturnCommoditySheet rcs4 = new ReturnCommoditySheet();

		rcs4.setID(Shared.BIG_ID);
		Map<String, Object> paramsRetrieve2 = rcs4.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, rcs4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> rcsRetrieve2List = returnCommoditySheetMapper.retrieve1Ex(paramsRetrieve2);

		Assert.assertTrue(rcsRetrieve2List.get(0).size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		System.out.println(rcsRetrieve2List == null ? "" : rcsRetrieve2List);

	}

	@DataProvider(name = "getRetrieveNData")
	public Object[][] getRetrieveNData() {// 润泽玻尿酸面膜 润泽保湿补水喷雾
		return new Object[][] {
				// queryKeyword,staffID,status,providerID,date1,date2,message,ec
				{ "TH20190605", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE1:根据10位退货单单号模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH20190605", 4, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE2:根据10位退货单单号和经办人模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH20190605", BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE3:根据10位退货单单号和退货单状态模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH20190605", BaseAction.INVALID_ID, BaseAction.INVALID_ID, 6, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE4:根据10位退货单单号和供应商模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH20190605", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE5:根据10位退货单单号和创建时间模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "润泽", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE6:根据商品名称模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "润泽", 6, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE7:根据商品名称和经办人模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "润泽", BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE8:根据商品名称和退货单状态模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "润泽", BaseAction.INVALID_ID, BaseAction.INVALID_ID, 6, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE9:根据商品名称和供应商模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "润泽", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE10:根据商品名称和创建时间模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "", 6, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE11:根据经办人模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE12:根据退货单状态模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, 3, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE13:根据供应商模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE14:根据创建时间模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE15:根据空条件模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH20190605", 2, 1, 1, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE16:根据全部条件(queryKeyword为10位退货单单号)模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "润泽玻尿酸面膜", 2, 1, 1, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE17:根据全部条件(queryKeyword为商品名称)模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH2019060", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE18:根据小于10位退货单单号模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH20190605123451234512345", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE19:根据大于20位退货单单号模糊查询退货单", EnumErrorCode.EC_NoError }, //
				{ "TH201906051234512345", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE20:根据20位退货单单号模糊查询退货单", EnumErrorCode.EC_NoError }//
		};
	}

	public EnumErrorCode retrieveN(ApplicationContext ctx, String queryKeyword, int staffID, int status, int providerID, String date1, String date2, String message) throws ParseException {
		DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setQueryKeyword(queryKeyword);
		rcs.setStaffID(staffID);
		rcs.setStatus(status);
		rcs.setProviderID(providerID);
		rcs.setDate1(df.parse(date1));
		rcs.setDate2(df.parse(date2));
		//
		Map<String, Object> params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = returnCommoditySheetMapper.retrieveN(params);
		//
		if (ls.size() > 0) {
			String err;
			for (BaseModel bm : ls) {
				err = ((ReturnCommoditySheet) bm).checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
				System.out.println(bm);
			}
			//
			Assert.assertTrue(ls != null && ls.size() > 0, "退货单的retrieveN失败");
		} else {
			Assert.assertTrue(ls.size() == 0, "退货单的retrieveN失败");
		}

		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(groups = { "retrieveN" }, dataProvider = "getRetrieveNData")
	public void retrieveNTest(String queryKeyword, int staffID, int status, int providerID, String date1, String date2, String message, EnumErrorCode ec) throws ParseException {
		Shared.printTestMethodStartInfo();
		Assert.assertEquals(retrieveN(this.applicationContext, queryKeyword, staffID, status, providerID, date1, date2, message), ec);
	}

	@Test
	public void retrieveNTest20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE20:根据商品名称查询(未审核的)");
		// 创建单品
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		// 创建退货单
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.createReturnCommoditySheet();
		// 创建退货单商品
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setCommodityID(commCreate.getID());
		rcsc.setCommodityName(commCreate.getName());
		createReturnCommoditySheetCommodity(rcs.getID(), rcsc.getCommodityID(), barcodesCreated.getID(), rcsc.getNO(), rcsc.getSpecification());
		// 更改退货单商品名称
		commCreate.setName("可乐薯片1" + System.currentTimeMillis() % 1000000);
		commCreate.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);
		//
		DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		rcs.setQueryKeyword(commCreate.getName());
		rcs.setStaffID(BaseAction.INVALID_ID);
		rcs.setStatus(BaseAction.INVALID_STATUS);
		rcs.setProviderID(BaseAction.INVALID_ID);
		rcs.setDate1(df.parse("2010/01/01 00:00:00"));
		rcs.setDate2(df.parse("2030/01/01 00:00:00"));
		Map<String, Object> params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = returnCommoditySheetMapper.retrieveN(params);
		Assert.assertTrue(ls.size() == 1, "查询出来的数据不正确");
		// 未审核的退货单模糊查询是查到商品表的名称而不会查到本表的CommodityName
		rcs.setQueryKeyword(rcsc.getCommodityName());
		params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ls = returnCommoditySheetMapper.retrieveN(params);
		Assert.assertTrue(ls.size() == 0, "查询出来的数据不正确");
	}

	@Test
	public void retrieveNTest21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE21:根据商品名称查询(已审核的)");
		// 创建单品
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		// 创建退货单
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.createReturnCommoditySheet();
		// 创建退货单商品
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setCommodityID(commCreate.getID());
		rcsc.setCommodityName(commCreate.getName());
		createReturnCommoditySheetCommodity(rcs.getID(), rcsc.getCommodityID(), barcodesCreated.getID(), rcsc.getNO(), rcsc.getSpecification());
		// 审核退货单
		BaseReturnCommoditySheetTest.approveReturnCommoditySheet(rcs);
		// 更改退货单商品名称
		commCreate.setName("可乐薯片1" + System.currentTimeMillis() % 1000000);
		commCreate.setOperatorStaffID(STAFF_ID3);
		commCreate.setNO(-50);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);
		//
		DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		rcs.setQueryKeyword(commCreate.getName());
		rcs.setStaffID(BaseAction.INVALID_ID);
		rcs.setStatus(BaseAction.INVALID_STATUS);
		rcs.setProviderID(BaseAction.INVALID_ID);
		rcs.setDate1(df.parse("2010/01/01 00:00:00"));
		rcs.setDate2(df.parse("2030/01/01 00:00:00"));
		Map<String, Object> params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = returnCommoditySheetMapper.retrieveN(params);
		Assert.assertTrue(ls.size() == 0, "查询出来的数据不正确");
		// 未审核的退货单模糊查询是查到商品表的名称而不会查到本表的CommodityName
		rcs.setQueryKeyword(rcsc.getCommodityName());
		params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ls = returnCommoditySheetMapper.retrieveN(params);
		Assert.assertTrue(ls.size() == 1, "查询出来的数据不正确");
	}

	@Test
	public void retrieveNTest22() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case22:根据输入_特殊字符商品名称查询退货单");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName("退货营养快_ka)（hd" + System.currentTimeMillis());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreated.getID(), Shared.DBName_Test);
		// 创建退货单
		ReturnCommoditySheet rcs1 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		ReturnCommoditySheet returnCommoditySheet = BaseReturnCommoditySheetTest.returnCommoditySheetCreate(rcs1);
		// 创建退货单商品
		ReturnCommoditySheetCommodity rcsc1 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc1.setCommodityID(commCreated.getID());
		rcsc1.setBarcodeID(barcodesCreated.getID());
		rcsc1.setReturnCommoditySheetID(returnCommoditySheet.getID());
		Map<String, Object> params = rcsc1.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheetCommodity rcscCreate = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params);
		rcsc1.setIgnoreIDInComparision(true);
		if (rcsc1.compareTo(rcscCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcscCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		String errorMsg = rcscCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");

		// 模糊查询退货单
		ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		rcs2.setQueryKeyword("_ka)（h");

		Map<String, Object> retrieveNParams1 = rcs2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> rcsList1 = returnCommoditySheetMapper.retrieveN(retrieveNParams1);
		for (BaseModel bm : rcsList1) {
			ReturnCommoditySheet rcs3 = (ReturnCommoditySheet) bm;
			//
			// 查询当前退货单的相关退货单商品表
			ReturnCommoditySheetCommodity rcsc2 = new ReturnCommoditySheetCommodity();
			rcsc2.setReturnCommoditySheetID(rcs3.getID());
			Map<String, Object> param = rcsc2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc2);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = returnCommoditySheetCommodityMapper.retrieveN(param);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			//
			for (BaseModel bm1 : ls) {
				rcsc2 = (ReturnCommoditySheetCommodity) bm1;
				Assert.assertTrue(rcsc2.getCommodityName().contains(rcs2.getQueryKeyword()), "查询商品名称没有包含_特殊字符");
			}

		}
		Assert.assertTrue(rcsList1 != null && rcsList1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除退货单商品
		// 因为现在退货单还没有做删除操作，以后会有
		// deleteReturnCommoditySheetCommodity(rcscCreate);
		// deleteAndVerificationReturnCommoditySheet(returnCommoditySheet);
		// doDeleteCommodity(commCreated);
	}
	
	@Test
	public void retrieveNTest23() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE23:根据门店ID查询");
		// 创建单品
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		// 创建退货单
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.createReturnCommoditySheet();
		// 创建退货单商品
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setCommodityID(commCreate.getID());
		rcsc.setCommodityName(commCreate.getName());
		createReturnCommoditySheetCommodity(rcs.getID(), rcsc.getCommodityID(), barcodesCreated.getID(), rcsc.getNO(), rcsc.getSpecification());
		// 更改退货单商品名称
		commCreate.setName("可乐薯片1" + System.currentTimeMillis() % 1000000);
		commCreate.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);
		//
		DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		rcs.setQueryKeyword(commCreate.getName());
		rcs.setStaffID(BaseAction.INVALID_ID);
		rcs.setStatus(BaseAction.INVALID_STATUS);
		rcs.setProviderID(BaseAction.INVALID_ID);
		rcs.setDate1(df.parse("2010/01/01 00:00:00"));
		rcs.setDate2(df.parse("2030/01/01 00:00:00"));
		rcs.setShopID(Shared.DEFAULT_Shop_ID);
		Map<String, Object> params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = returnCommoditySheetMapper.retrieveN(params);
		Assert.assertTrue(ls.size() == 1, "查询出来的数据不正确");
		// 未审核的退货单模糊查询是查到商品表的名称而不会查到本表的CommodityName
		rcs.setQueryKeyword(rcsc.getCommodityName());
		params = rcs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ls = returnCommoditySheetMapper.retrieveN(params);
		Assert.assertTrue(ls.size() == 0, "查询出来的数据不正确");
	}



	public void createReturnCommoditySheetCommodity(int returnCommoditySheetID, int commodityID, int barcodeID, int NO, String specification) throws CloneNotSupportedException, InterruptedException {
		//
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(returnCommoditySheetID);
		rcsc.setCommodityID(commodityID);
		rcsc.setBarcodeID(barcodeID);
		rcsc.setNO(NO);
		rcsc.setSpecification(specification);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcscparams = rcsc.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc);

		ReturnCommoditySheetCommodity rcscCreate = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(rcscparams);
		rcsc.setIgnoreIDInComparision(true);
		if (rcsc.compareTo(rcscCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcscCreate == null ? "null" : rcscCreate);

		Assert.assertTrue(rcscCreate != null && EnumErrorCode.values()[Integer.parseInt(rcscparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单成功");
	}


	@Test
	public void updateTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		// 创建退货单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity.setNO(100);
		commodity.setnOStart(commodity.getNO());
		commodity.setPurchasingPriceStart(11.1D);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcodesCreated.getID(), commCreate.getnOStart(), commCreate.getSpecification());

		System.out.println("\n------------------------ Update ReturnCommoditySheet Test ------------------------");

		System.out.println("\n------------------------ CASE1:该退货单未审核------------------------");

		ReturnCommoditySheet rcs2 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs2.setID(rcsCreate.getID());
		rcs2.setStaffID(5);
		rcs2.setProviderID(5);

		Map<String, Object> paramsUpdate = rcsCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, rcs2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate = (ReturnCommoditySheet) returnCommoditySheetMapper.update(paramsUpdate); // ...
		rcs2.setIgnoreIDInComparision(true);
		if (rcs2.compareTo(rcsUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		System.out.println(rcsUpdate == null ? "" : rcsUpdate);

		System.out.println("\n------------------------ CASE2:该退货单未审核，传入不存在的ProviderID------------------------");
		ReturnCommoditySheet rcs4 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs4.setID(rcsCreate.getID());
		rcs4.setStaffID(5);
		rcs4.setProviderID(-5);

		Map<String, Object> paramsUpdate3 = rcsCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, rcs4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate3 = (ReturnCommoditySheet) returnCommoditySheetMapper.update(paramsUpdate3); // ...
		rcs4.setIgnoreIDInComparision(true);

		Assert.assertTrue(rcsUpdate3 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");
		System.out.println(rcsUpdate3 == null ? "" : rcsUpdate3);

		System.out.println("\n------------------------ CASE3:该退货单已审核------------------------");
		ReturnCommoditySheet rcs5 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params1 = rcs5.getCreateParam(BaseBO.INVALID_CASE_ID, rcs5);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate1 = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params1); // ...
		rcs5.setIgnoreIDInComparision(true);
		if (rcs5.compareTo(rcsCreate1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate1 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		// 创建退货单商品
		Commodity commodity4 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity4.setNO(100);
		commodity4.setnOStart(commodity4.getNO());
		commodity4.setPurchasingPriceStart(11.1D);
		Commodity commCreate4 = BaseCommodityTest.createCommodityViaMapper(commodity4, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated4 = BaseBarcodesTest.retrieveNBarcodes(commCreate4.getID(), Shared.DBName_Test);
		createReturnCommoditySheetCommodity(rcsCreate1.getID(), commCreate4.getID(), barcodesCreated4.getID(), commCreate4.getnOStart(), commCreate4.getSpecification());
		// 退货单变为已审核
		ReturnCommoditySheet rcs6 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs6.setID(rcsCreate1.getID());
		Map<String, Object> paramsUpdate4 = rcsCreate1.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs6);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate4 = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate4); // ...
		rcs6.setIgnoreIDInComparision(true);
		if (rcs6.compareTo(rcsUpdate4) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsUpdate4 != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		// 更新StaffID，ProviderID，退货单已审核，更新不成功
		rcs6.setID(rcsCreate1.getID());
		rcs6.setStaffID(1);
		rcs6.setProviderID(1);
		Map<String, Object> paramsUpdate5 = rcsCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, rcs6);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate5 = (ReturnCommoditySheet) returnCommoditySheetMapper.update(paramsUpdate5); // ...
		rcs5.setIgnoreIDInComparision(true);

		Assert.assertTrue(rcsUpdate5 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");
		System.out.println(rcsUpdate5 == null ? "" : rcsUpdate5);

		System.out.println("\n------------------------ CASE4:修改没有退货商品的退货单------------------------");
		// 创建退货单
		ReturnCommoditySheet rcs7 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params7 = rcs7.getCreateParam(BaseBO.INVALID_CASE_ID, rcs7);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate7 = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params7); // ...
		rcs7.setIgnoreIDInComparision(true);
		if (rcs7.compareTo(rcsCreate7) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate7 != null && EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		// 修改退货单
		ReturnCommoditySheet rcs8 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs8.setID(rcsCreate7.getID());
		rcs8.setStaffID(5);
		rcs8.setProviderID(5);

		Map<String, Object> paramsUpdate7 = rcsCreate7.getUpdateParam(BaseBO.INVALID_CASE_ID, rcs8);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate7 = (ReturnCommoditySheet) returnCommoditySheetMapper.update(paramsUpdate7); // ...
		//
		Assert.assertTrue(rcsUpdate7 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");
		// 给没有退货商品的退货单关联退货商品
		Commodity commodity7 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity7.setNO(100);
		commodity7.setnOStart(commodity7.getNO());
		commodity7.setPurchasingPriceStart(11.1D);
		Commodity commCreate7 = BaseCommodityTest.createCommodityViaMapper(commodity7, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated7 = BaseBarcodesTest.retrieveNBarcodes(commCreate7.getID(), Shared.DBName_Test);
		createReturnCommoditySheetCommodity(rcsCreate7.getID(), commCreate7.getID(), barcodesCreated7.getID(), commCreate7.getnOStart(), commCreate7.getSpecification());
	}
	// ---------------------- 更新退货单end---------------------------

	@Test
	public void approveTest1() throws CloneNotSupportedException, InterruptedException {// ...本测试结果验证不足。若能将审核前后商品的库存变化assert一下，效果更佳
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: 审核正常退货单");
		//
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		//
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 创建退货单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity.setNO(100);
		commodity.setnOStart(commodity.getNO());
		commodity.setPurchasingPriceStart(11.1D);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcodesCreated.getID(), commCreate.getnOStart(), commCreate.getSpecification());
		//
		ReturnCommoditySheet rcs1 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs1.setID(rcsCreate.getID());
		Map<String, Object> paramsUpdate = rcsCreate.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate); // ...
		rcs1.setIgnoreIDInComparision(true);
		if (rcs1.compareTo(rcsUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(rcsUpdate == null ? "" : rcsUpdate);

	}

	@Test
	public void approveTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "该退货单已审核，请勿重复操作";
		Shared.caseLog("CASE2: " + message);
		// 先创建一个测试对象审核
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		//
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 创建退货单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity.setNO(100);
		commodity.setnOStart(commodity.getNO());
		commodity.setPurchasingPriceStart(11.1D);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcodesCreated.getID(), commCreate.getnOStart(), commCreate.getSpecification());
		//
		ReturnCommoditySheet rcs1 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs1.setID(rcsCreate.getID());
		Map<String, Object> paramsUpdate = rcsCreate.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate); // ...
		rcs1.setIgnoreIDInComparision(true);
		if (rcs1.compareTo(rcsUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(rcsUpdate == null ? "" : rcsUpdate);
		// 重复审核
		ReturnCommoditySheet rcs2 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs2.setID(rcsCreate.getID());
		Map<String, Object> paramsUpdate1 = rcsCreate.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate1 = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate1); // ...
		rcs2.setIgnoreIDInComparision(true);
		//
		Assert.assertTrue(rcsUpdate1 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsUpdate1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsUpdate1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + paramsUpdate1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(rcsUpdate1 == null ? "" : rcsUpdate1);
	}

	@Test
	public void approveTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "查无该采购退货单";
		Shared.caseLog("CASE3: " + message);
		//
		ReturnCommoditySheet rcs3 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs3.setID(BaseAction.INVALID_ID);
		Map<String, Object> paramsUpdate2 = rcs3.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate2 = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate2); // ...
		//
		Assert.assertTrue(rcsUpdate2 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + paramsUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(rcsUpdate2 == null ? "" : rcsUpdate2);
	}

	@Test
	public void approveTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4: 审核没有退货商品的退货单");
		// 先创建一个测试对象审核
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		//
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsUpdate2 = rcsCreate.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcsCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate2 = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate2); // ...
		//
		Assert.assertTrue(rcsUpdate2 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 给没有退货商品的退货单关联退货商品
		Commodity commodity4 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity4.setNO(100);
		commodity4.setnOStart(commodity4.getNO());
		commodity4.setPurchasingPriceStart(11.1D);
		Commodity commCreate4 = BaseCommodityTest.createCommodityViaMapper(commodity4, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreated4 = BaseBarcodesTest.retrieveNBarcodes(commCreate4.getID(), Shared.DBName_Test);
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate4.getID(), barcodesCreated4.getID(), commCreate4.getnOStart(), commCreate4.getSpecification());
	}
}
