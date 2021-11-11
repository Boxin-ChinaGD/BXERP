package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Vip;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@WebAppConfiguration
public class RetailTradeMapperTest extends BaseMapperTest {
	private Random random = new Random();

	/** 被退货的零售单的ID。值为-1或>0的整数 */
	private int iSourceRetailTradeID = -1;

	public static class DataInput {
		private static Vip vip = null;

		protected static final Vip getVip() throws CloneNotSupportedException, InterruptedException {
			vip = new Vip();
			vip.setMobile(Shared.getValidStaffPhone());
			vip.setiCID(Shared.getValidICID());
			Thread.sleep(1);
			vip.setName("Tom" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			vip.setEmail(String.valueOf(System.currentTimeMillis()).substring(6) + "@bx.vip");
			vip.setConsumeTimes(10);
			vip.setConsumeAmount(100);
			vip.setDistrict("广州");
			vip.setCategory(1);
			vip.setBirthday(new Date());
			vip.setBonus(100);
			vip.setLastConsumeDatetime(new Date());
			vip.setSn("");
			vip.setSex(0);
			vip.setLogo("123456123456");
			vip.setRemark("1111");
			return (Vip) vip.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setup();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Create RetailTrade Test");
		//
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("SN和POSID都相同的情况直接返回该零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		//
		Map<String, Object> params = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(params); // ...
		Assert.assertTrue(createEx != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "创建对象失败");
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("SN和POSID不相同的情况下是正常创建");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setVipID(vipCreate.getID());
		rt2.setAmount(100d); // 为了下面检查积分变动.单次积分最大增加1000分.1元10积分.所以设置100元
		rt2.setAmountCash(100d);
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade2, commCreate, barcodes);
		// 检查会员是否正确的处理积分，消费记录等
		Vip vipRetrieve1 = (Vip) BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipRetrieve1.getConsumeTimes() == (vipCreate.getConsumeTimes() + 1), "消费次数错误");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(vipRetrieve1.getConsumeAmount(), GeneralUtil.sum(vipCreate.getConsumeAmount(), localRetailTrade2.getAmount()))) < BaseModel.TOLERANCE, "消费金额不正确");
		Assert.assertTrue(vipRetrieve1.getBonus() == (vipCreate.getBonus() + GeneralUtil.mul(localRetailTrade2.getAmount(), 10)), "积分变动不正确");
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("iSourceID为不存在的零售单ID，返回错误码7");
		RetailTrade rt3 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt3.setSourceID(99999);
		Map<String, Object> params3 = rt3.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(params3); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined, "错误码比对不正确");
		//
		RetailTrade rt5 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt5.setSourceID(0);
		//
		Map<String, Object> params5 = rt5.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(params5);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined, "错误码比对不正确");
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("iSourceID为存在的零售单ID，进行退货操作");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		//
		vipCreate = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade2, commCreate, barcodes);
		//
		Random ran = new Random();
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setSourceID(localRetailTrade2.getID());
		rt.setLocalSN(ran.nextInt(1000));
		rt.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade3 = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建零售退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade3, commCreate, barcodes);
		//
		Vip vipRetrieve1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipRetrieve1.getConsumeTimes() == (vipCreate.getConsumeTimes() - 1), "会员的消费次数不正确");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(vipRetrieve1.getConsumeAmount(), GeneralUtil.sub(vipCreate.getConsumeAmount(), localRetailTrade3.getAmount()))) < BaseModel.TOLERANCE, "消费金额不正确");
		Assert.assertTrue(vipRetrieve1.getBonus() == vipCreate.getBonus(), "退货修改了会员的积分");
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("对一张退货单再次进行退货");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		//
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setSourceID(localRetailTrade.getID());
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		// 创建零售退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade2, commCreate, barcodes);
		//
		RetailTrade rt3 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt3.setSourceID(localRetailTrade2.getID());
		Map<String, Object> createParam3 = rt3.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(createParam3); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				createParam3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:一张零售单，最多只能进行一次退货操作");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		// 第一次退货
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setSourceID(localRetailTrade.getID());
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		Assert.assertTrue((localRetailTrade.getSn() + "_1").equals(localRetailTrade2.getSn()), "创建退货单的SN码不正确");
		// 创建零售退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade2, commCreate, barcodes);
		// 第二次退货
		RetailTrade rt3 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt3.setSourceID(localRetailTrade.getID());
		Map<String, Object> params3 = rt3.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(params3); // ...
		Assert.assertTrue(createEx.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "错误码不正确");
	}

	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:没有子商户号的公司进行零售，使用的是微信支付，错误信息：支付失败，公司的子商户号未设置！不能进行微信支付");
		//
		Company company = new Company();
		company.setID(1);
		company.setSubmchid(null);
		Map<String, Object> paramsUpdate = company.getUpdateParam(BaseBO.CASE_Company_UpdateSubmchid, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		companyMapper.updateSubmchid(paramsUpdate);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
				"修改对象失败！错误信息=" + paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setPaymentType(5);
		rt.setAmount(10d);
		rt.setAmountCash(3d);
		rt.setAmountWeChat(7d);
		String err = rt.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		Map<String, Object> params1 = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(params1); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 恢复公司的数据
		company.setID(1);
		company.setSubmchid(BaseCompanyTest.nbrSubmchid);
		Map<String, Object> paramsUpdate1 = company.getUpdateParam(BaseBO.CASE_Company_UpdateSubmchid, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		companyMapper.updateSubmchid(paramsUpdate1);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(paramsUpdate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
				"修改对象失败！错误信息=" + paramsUpdate1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9: iSourceID为存在的零售单ID，但是交易时间超过一年的订单禁止退款");
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		//
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);// 过去一年
		c.add(Calendar.DATE, -1);// 再过去一天，大于一年
		rt2.setSaleDatetime(c.getTime());
		rt2.setVipID(vipCreate.getID());
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade2, commCreate, barcodes);
		//
		Random ran = new Random();
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setSourceID(localRetailTrade2.getID());
		rt.setLocalSN(ran.nextInt(1000));
		rt.setVipID(vipCreate.getID());
		Map<String, Object> createParam = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(createParam);
		Assert.assertTrue(createEx.size() == 0 && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "错误码不正确");
	}

	@Test
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: iSourceID为存在的零售单ID，支付宝退款金额大于零售时的支付宝支付金额");

		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setPaymentType(3);
		rt.setAmount(100d);
		rt.setAmountCash(50d);
		rt.setAmountAlipay(50d);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);// 过去一天
		rt.setSaleDatetime(c.getTime());
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		//
		Random ran = new Random();
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setPaymentType(3);
		rt2.setAmount(100d);
		rt2.setAmountCash(40d);
		rt2.setAmountAlipay(60d);
		rt2.setSourceID(localRetailTrade.getID());
		rt2.setLocalSN(ran.nextInt(1000));
		Map<String, Object> createParam = rt2.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(createParam);
		Assert.assertTrue(createEx.size() == 0 && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).equals("退款失败，支付宝退款金额不能比零售时支付宝支付的金额多"), "错误信息不正确");
	}

	@Test
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11: iSourceID为存在的零售单ID，微信退款金额大于零售时的微信金额");

		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setPaymentType(5);
		rt.setAmount(100d);
		rt.setAmountCash(50d);
		rt.setAmountWeChat(50d);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);// 过去一天
		rt.setSaleDatetime(c.getTime());
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		//
		Random ran = new Random();
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setPaymentType(5);
		rt2.setAmount(100d);
		rt2.setAmountCash(40d);
		rt2.setAmountWeChat(60d);
		rt2.setSourceID(localRetailTrade.getID());
		rt2.setLocalSN(ran.nextInt(1000));
		Map<String, Object> createParam = rt2.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(createParam);
		Assert.assertTrue(createEx.size() == 0 && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).equals("退款失败，微信退款金额不能比零售时微信支付的金额多"), "错误信息不正确");
	}

	@Test
	public void createTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: sn为24位普通零售单，且格式正确，结果通过。");

		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
//		rt.setSn("LS2099083000110100011234");
		BaseRetailTradeTest.createRetailTrade(rt);
	}

	@Test
	public void createTest13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13: sn为26位的退货单，结尾为_1，且格式正确，结果通过。");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
//		rt.setSn("LS2099083000110100011234_1");
		rt.setSn(rt.getSn() + "_1");
		//
		BaseRetailTradeTest.createRetailTrade(rt);
	}

	@Test
	public void createTest14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14: iSourceID为SQLite中的临时零售单的F_ID字段（断网情况），进行退货操作,退货成功");
		// 创建零售单
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade2, commCreate, barcodes);
		// 退货
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setSourceID(Shared.SOURCE_ID_OnlyFoundInSQLite);
		rt.setSn(localRetailTrade2.getSn() + "_1");
		Map<String, Object> createParam = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(createParam);
		Assert.assertTrue(createEx != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		RetailTrade localRetailTrade3 = (RetailTrade) createEx.get(0).get(0);
		rt.setIgnoreIDInComparision(true);
		rt.setSourceID(localRetailTrade3.getSourceID()); // 退货单模拟的sourceID和数据返回的正确sourceID不同，不比较
		if (rt.compareTo(localRetailTrade3) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
	}

	/**
	 * 断网情况下，A商品已经被删除，但POS1不知道。A在POS1上售出，后来又在POS1上退货，零售单和退货单都生成并上传到后台。后台应当接受这样的零售单
	 */
	@Test
	public void createTest15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: 创建状态为包含已删除商品的零售单，并对其进行退货，均成功");
		// 创建零售单
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setStatus(EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
		// checkCreate
		RetailTrade localRetailTrade2 = BaseRetailTradeTest.createRetailTrade(rt2);
		// 创建零售单商品
		int deletedCommodityID = 50;
		RetailTradeCommodity rtc2 = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc2.setCommodityID(deletedCommodityID);
		rtc2.setTradeID(localRetailTrade2.getID());
		Map<String, Object> rtc2Params = rtc2.getCreateParam(BaseBO.INVALID_CASE_ID, rtc2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity localRetailTradeCommodity2 = (RetailTradeCommodity) retailTradeCommodityMapper.create(rtc2Params); // ...
		Assert.assertTrue(localRetailTradeCommodity2 != null && EnumErrorCode.values()[Integer.parseInt(rtc2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				rtc2Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 由于localRetailTradeCommodity2对应的是已删除商品，数据库中的BarcodeID为NULL,但是int型的默认值是0，不会是NULL。
		Assert.assertTrue(localRetailTradeCommodity2.getBarcodeID() == 0, "返回的对象的条形码有误！");
		//
		rtc2.setIgnoreIDInComparision(true);
		rtc2.setBarcodeID(localRetailTradeCommodity2.getBarcodeID());// 为了让assert能通过
		if (rtc2.compareTo(localRetailTradeCommodity2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		// 创建退货单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setStatus(EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
		rt.setSourceID(localRetailTrade2.getID());
		rt.setSn(localRetailTrade2.getSn() + "_1");
		//
		RetailTrade localRetailTrade3 = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建退货零售单商品
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(deletedCommodityID);
		rtc.setTradeID(localRetailTrade3.getID());
		Map<String, Object> rtcParams = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity localRetailTradeCommodity = (RetailTradeCommodity) retailTradeCommodityMapper.create(rtcParams); // ...
		Assert.assertTrue(localRetailTradeCommodity != null && EnumErrorCode.values()[Integer.parseInt(rtcParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				rtcParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 由于localRetailTradeCommodity2对应的是已删除商品，数据库中的BarcodeID为NULL,但是int型的默认值是0，不会是NULL。
		Assert.assertTrue(localRetailTradeCommodity.getBarcodeID() == 0, "返回的对象的条形码有误！");
		//
		rtc.setIgnoreIDInComparision(true);
		rtc.setBarcodeID(localRetailTradeCommodity.getBarcodeID());// 让下面的assert能通过
		if (rtc.compareTo(localRetailTradeCommodity) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 恢复rt的SourceID。在这里不需要做。
	}

	@Test
	public void createTest16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16: VIP不存在");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setVipID(Shared.BIG_ID);
		//
		String errMsg = rt.checkCreate(BaseAction.INVALID_ID);
		Assert.assertEquals(errMsg, "");
		//
		Map<String, Object> rt2Params = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(rt2Params); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(rt2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, rt2Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17: POS不存在");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setPos_ID(Shared.BIG_ID);
		//
		String errMsg = rt.checkCreate(BaseAction.INVALID_ID);
		Assert.assertEquals(errMsg, "");
		//
		Map<String, Object> rt2Params = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(rt2Params); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(rt2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, rt2Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19: 员工不存在");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setStaffID(Shared.BIG_ID);
		//
		String errMsg = rt.checkCreate(BaseAction.INVALID_ID);
		Assert.assertEquals(errMsg, "");
		//
		Map<String, Object> rt2Params = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.createEx(rt2Params); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(rt2Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, rt2Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20: 会员A的消费单，对其进行退货,vipID为会员B。");
		// 创建会员
		Vip vip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 使用此会员进行消费
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setVipID(vip.getID());
		//
		RetailTrade createRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);

		// 查询会员的消费信息
		Vip vipA = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);

		// 进行退货,使用其他的会员ID
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		// 退货前，先查询传递的vipID的vip信息
		Vip vipB = new Vip();
		vipB.setID(returnRetailTrade.getVipID());
		vipB = BaseVipTest.retrieve1ViaMapper(vipB, Shared.DBName_Test);
		//
		returnRetailTrade.setSourceID(createRetailTrade.getID());
		RetailTrade createReturnRetailTrade = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);

		// 检查是否正确的减少会员的消费
		Vip afterVipA = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);
		Assert.assertTrue(
				vipA.getConsumeTimes() == afterVipA.getConsumeTimes() + 1 && Math.abs(GeneralUtil.sub(vipA.getConsumeAmount(), GeneralUtil.sum(afterVipA.getConsumeAmount(), createReturnRetailTrade.getAmount()))) < BaseModel.TOLERANCE,
				"会员A计算消费次数不正确");
		//
		Vip afterVipB = BaseVipTest.retrieve1ViaMapper(vipB, Shared.DBName_Test);
		Assert.assertTrue(vipB.getConsumeTimes() == afterVipB.getConsumeTimes() && Math.abs(GeneralUtil.sub(vipB.getConsumeAmount(), afterVipB.getConsumeAmount())) < BaseModel.TOLERANCE, "未对会员b的单进行退货，但是会员B的消费记录却发生了改变");
	}
	
	@Test
	public void createTest21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case21: iSourceID为存在的零售单ID，微信和现金混合退款，大于源单的现金和微信混合支付，退款失败");

		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setPaymentType(5);
		rt.setAmount(100d);
		rt.setAmountCash(50d);
		rt.setAmountWeChat(50d);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);// 过去一天
		rt.setSaleDatetime(c.getTime());
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		//
		Random ran = new Random();
		RetailTrade rt2 = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt2.setPaymentType(5);
		rt2.setAmount(120d);
		rt2.setAmountCash(60d);
		rt2.setAmountWeChat(60d);
		rt2.setSourceID(localRetailTrade.getID());
		rt2.setLocalSN(ran.nextInt(1000));
		Map<String, Object> createParam = rt2.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(createParam);
		Assert.assertTrue(createEx.size() == 0 && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).equals("退款失败，退款金额不能比零售时微信支付和现金支付的总金额多"), "错误信息不正确");
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web请求，用最小长度(10)的零售单单号来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword(retailTradeCreate.getSn().substring(0, 10));
		Map<String, Object> paramsForID = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : bm) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
		System.out.println("根据SN来查询零售单成功！" + bm.toString());
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用localSN 和 POS_ID来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForlocalSN_ID = new RetailTrade();
		rtForlocalSN_ID.setPos_ID(retailTradeCreate.getPos_ID());
		rtForlocalSN_ID.setLocalSN(retailTradeCreate.getLocalSN());
		Map<String, Object> paramsForlocalSN_ID = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForlocalSN_ID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listlocalSN_ID = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForlocalSN_ID);
		Assert.assertTrue(listlocalSN_ID.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForlocalSN_ID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : listlocalSN_ID) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
		System.out.println("根据localSN 和 POS_ID来查询零售单成功！" + listlocalSN_ID.toString());
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用POS_ID来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForPosID = new RetailTrade();
		rtForPosID.setPos_ID(retailTradeCreate.getPos_ID());
		Map<String, Object> paramsForPos_ID = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForPosID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listPos_ID = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForPos_ID);
		Assert.assertTrue(listPos_ID.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForPos_ID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : listPos_ID) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex() || retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex());
		}
		System.out.println("根据POS_ID来查询零售单成功！" + listPos_ID.toString());
	}

	@Test
	public void retrieveNTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，查询所有零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForNull = new RetailTrade();
		Map<String, Object> paramsForNull = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForNull);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForNull);
		Assert.assertTrue(list2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForNull.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : list2) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
		System.out.println("查询所有零售单！" + list2.toString());
	}

	@Test
	public void retrieveNTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用时间来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtDate = new RetailTrade();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		String string = "2017-8-6 00:00:00";
		rtDate.setDatetimeStart(sdf.parse(string));
		rtDate.setDatetimeEnd(new Date());
		Map<String, Object> paramsDate = rtDate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtDate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listDate = (List<BaseModel>) retailTradeMapper.retrieveN(paramsDate);
		Assert.assertTrue(listDate.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsDate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : listDate) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
		System.out.println("查询该期间的零售单！" + listDate.toString());
	}

	@Test
	public void retrieveNTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用ilocalSN来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForlocalSN_ID6 = new RetailTrade();
		rtForlocalSN_ID6.setLocalSN(retailTradeCreate.getLocalSN());
		Map<String, Object> paramsForlocalSN_ID6 = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForlocalSN_ID6);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listlocalSN_ID6 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForlocalSN_ID6);
		Assert.assertTrue(listlocalSN_ID6.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForlocalSN_ID6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : listlocalSN_ID6) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
		System.out.println("根据localSN来查询零售单成功！" + listlocalSN_ID6.toString());
	}

	@Test
	public void retrieveNTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用iVipID来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForVip_ID7 = new RetailTrade();
		rtForVip_ID7.setVipID(retailTradeCreate.getVipID());
		Map<String, Object> paramsForVip_ID7 = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForVip_ID7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listVip_ID7 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForVip_ID7);
		Assert.assertTrue(listVip_ID7.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForVip_ID7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		System.out.println("根据iVipID来查询零售单成功！" + listVip_ID7.toString());
	}

	@Test
	public void retrieveNTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用paymentType来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForPaymentType8 = new RetailTrade();
		rtForPaymentType8.setPaymentType(retailTradeCreate.getPaymentType());
		Map<String, Object> paramsForPaymentType8 = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForPaymentType8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listPaymentType8 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForPaymentType8);
		Assert.assertTrue(listPaymentType8.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForPaymentType8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		boolean hasDBError = false;
		for (BaseModel baseModel : listPaymentType8) {
			RetailTrade retailTrade = (RetailTrade) baseModel;
			if (retailTrade.getID() == retailTradeCreate.getID()) {
				hasDBError = true;
				break;
			}
		}
		Assert.assertTrue(hasDBError, "查询对象失败");
		System.out.println("根据paymentType来查询零售单成功！" + listPaymentType8.toString());
	}

	@Test
	public void retrieveNTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web端，用iStaffID来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setStaffID(retailTradeCreate.getStaffID());
		Map<String, Object> params1 = retailTrade.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, retailTrade);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list1 = (List<BaseModel>) retailTradeMapper.retrieveN(params1);
		Assert.assertTrue(list1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel baseModel : list1) {
			RetailTrade rt1 = (RetailTrade) baseModel;
			assertTrue(rt1.getStaffID() == retailTrade.getStaffID());
		}
		System.out.println("根据iStaffID来查询零售单成功！" + list1.toString());
	}

	@Test
	public void retrieveNTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: app端，用等于最大长度(26)且以LS开头、以_1结尾的零售单单号来查询退货单,期望的是查询到数据");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		// 创建退货单
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(localRetailTrade.getID());
		RetailTrade returnRetailTradeCreate = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		// 创建退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(returnRetailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForNull = new RetailTrade();
		rtForNull.setQueryKeyword(returnRetailTradeCreate.getSn());
		Map<String, Object> paramsForNull = localRetailTrade.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNFromApp, rtForNull);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForNull);
		Assert.assertTrue(list2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForNull.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
	}

	@Test
	public void retrieveNTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11: web端，使用零售单存在的商品名称进行模糊查询");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		RetailTradeCommodity retailTradeCommodityCreate = BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		// 查询零售单
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword(retailTradeCommodityCreate.getCommodityName());
		Map<String, Object> paramsForID = rtForID.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		System.out.println("根据商品名称来查询零售单成功！" + bm.toString());
	}

	@Test
	public void retrieveNTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: web端，使用零售单不存在的商品名称进行模糊查询");
		// 查询零售单
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword("不存在的商品名称");
		Map<String, Object> paramsForID = rtForID.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		System.out.println("根据商品名称来查询零售单成功！" + bm.toString());
	}

	@Test
	public void retrieveNTest13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13: web端，使用空串''进行模糊查询");
		// 查询零售单
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword("");
		Map<String, Object> paramsForID = rtForID.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		System.out.println("查询零售单成功！" + bm.toString());
	}

	@Test
	public void retrieveNTest14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14: web端，使用商品名称的一部分进行模糊查询");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建零售单商品
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setTradeID(retailTradeCreate.getID());
		rtc.setCommodityID(163);
		rtc.setNO(10);
		rtc.setOperatorStaffID(STAFF_ID1);
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		// 查询零售单
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword("A");
		Map<String, Object> paramsForID = rtForID.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		System.out.println("根据商品名称来查询零售单成功！" + bm.toString());
	}

	@Test
	public void retrieveNTest15() throws CloneNotSupportedException, InterruptedException {

		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: web端，根据商品名称传入特殊字符“_”模糊查询零售单");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName("营养快_kahd" + System.currentTimeMillis());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, comm1);
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);

		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建零售单商品
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setTradeID(retailTradeCreate.getID());
		rtc.setCommodityID(commCreated.getID());
		rtc.setBarcodeID(barcodes.getID());
		rtc.setNO(10);
		rtc.setOperatorStaffID(STAFF_ID1);
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		// 查询零售单
		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setQueryKeyword("_");
		Map<String, Object> paramsForID = retailTrade.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, retailTrade);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList1 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bmList1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel bm : bmList1) {
			RetailTrade rt2 = (RetailTrade) bm;
			//
			// 查询当前零售订单的相关零售订单商品表
			RetailTradeCommodity rtc1 = new RetailTradeCommodity();
			rtc1.setTradeID(rt2.getID());
			Map<String, Object> param = rtc1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rtc1);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = retailTradeCommodityMapper.retrieveN(param);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			// 用商品名称对比
			for (BaseModel bm1 : ls) {
				rtc1 = (RetailTradeCommodity) bm1;
				Assert.assertTrue(rtc1.getCommodityName().contains(retailTrade.getQueryKeyword()), "查询商品名称没有包含_特殊字符");
			}

		}

	}

	@Test
	public void retrieveNTest16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16: web端，excludeReturned = 0，查询出的零售单包含退货单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		// 创建退货单
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(retailTradeCreate.getID());
		RetailTrade returnRetailTradeCreate = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		// 创建退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(returnRetailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForNull = new RetailTrade();
		Map<String, Object> paramsForNull = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForNull);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForNull);
		Assert.assertTrue(list2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForNull.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		// 判断查询出来的零售单时候包含退货单
		boolean existReturnTrade = false;
		for (BaseModel b1 : list2) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
			if (retailTrade.getSourceID() != BaseAction.INVALID_ID) {
				existReturnTrade = true;
			}
		}
		Assert.assertTrue(existReturnTrade, "预期RN返回退货单，但没有返回退货单");
		System.out.println("查询所有零售单！" + list2.toString());
	}

	@Test
	public void retrieveNTest17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17: web端，excludeReturned = 1，查询出的零售单不包含退货单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		// 创建退货单
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(retailTradeCreate.getID());
		RetailTrade returnRetailTradeCreate = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		// 创建退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(returnRetailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForNull = new RetailTrade();
		rtForNull.setExcludeReturned(1);
		Map<String, Object> paramsForNull = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForNull);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForNull);
		Assert.assertTrue(list2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForNull.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		// 判断查询出来的零售单时候包含退货单
		boolean existReturnTrade = false;
		for (BaseModel b1 : list2) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
			if (retailTrade.getSourceID() != BaseAction.INVALID_ID) {
				existReturnTrade = true;
			}
		}
		Assert.assertTrue(!existReturnTrade, "预期RN不返回退货单，但返回了退货单");
		System.out.println("查询所有零售单！" + list2.toString());
	}

	@Test
	public void retrieveNTest18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18: app端，用等于最大长度(26)且以ls开头、以_1结尾的零售单单号来查询退货单,期望的是查询到数据");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);

		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(localRetailTrade, commCreate, barcodes);
		// 创建退货单
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(localRetailTrade.getID());
		RetailTrade returnRetailTradeCreate = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		// 创建退货单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(returnRetailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForNull = new RetailTrade();
		rtForNull.setQueryKeyword(returnRetailTradeCreate.getSn().toLowerCase());
		Map<String, Object> paramsForNull = localRetailTrade.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNFromApp, rtForNull);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForNull);
		Assert.assertTrue(list2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForNull.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");

	}

	@Test
	public void retrieveNTest19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19: app端，使用空串''进行模糊查询");
		// 查询零售单
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword("");
		Map<String, Object> paramsForID = rtForID.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNFromApp, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		System.out.println("查询零售单成功！" + bm.toString());
	}

	@Test
	public void retrieveNTest20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20: app请求，用最小长度(10)的零售单单号来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword(retailTradeCreate.getSn().substring(2, 12));
		Map<String, Object> paramsForID = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNFromApp, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : bm) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
	}

	@Test
	public void retrieveNTest21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case21: web请求，根据日期(过去一月内)查询零售单时，获得满足需求的结果");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1); // 上个月
		Date lastMonth = ca.getTime();
		rt.setSaleDatetime(lastMonth);
		rt.setDatetimeStart(lastMonth);
		//
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword(retailTradeCreate.getSn().substring(2, 12));
		rtForID.setDatetimeStart(lastMonth);
		Map<String, Object> paramsForID = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNFromApp, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : bm) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
	}

	@Test
	public void retrieveNTest22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case22: web请求，根据日期(过去一周内)查询零售单时，获得满足需求的结果");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		Date lastWeek = DatetimeUtil.getDays(new Date(), -7);// 上周
		rt.setSaleDatetime(lastWeek);
		//
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		RetailTrade rtForID = new RetailTrade();
		rtForID.setQueryKeyword(retailTradeCreate.getSn().substring(2, 12));
		rtForID.setDatetimeStart(lastWeek);
		Map<String, Object> paramsForID = retailTradeCreate.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNFromApp, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (BaseModel b1 : bm) {
			RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) b1;
			Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex());
		}
	}

	@Test
	public void retrieveNTest24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("web请求，查看默认返回的数据是否降序");
		RetailTrade rtForID = new RetailTrade();
		Map<String, Object> paramsForID = rtForID.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNBySNFromWeb, rtForID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = (List<BaseModel>) retailTradeMapper.retrieveN(paramsForID);
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		for (int i = 1; i < bmList.size(); i++) {
			assertTrue(bmList.get(i - 1).getID() > bmList.get(i).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE1: 用ID来查询零售单");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> list = (List<List<BaseModel>>) BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Commodity commCreate = (Commodity) list.get(0).get(0);
		Barcodes barcodes = (Barcodes) list.get(1).get(0);
		// 创建零售单商品
		BaseRetailTradeTest.createRetailtradeCommodityViaMapper(retailTradeCreate, commCreate, barcodes);
		//
		Map<String, Object> paramsRetrieve1 = retailTradeCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, retailTradeCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTrade retailTradeRetrieve1 = (RetailTrade) retailTradeMapper.retrieve1(paramsRetrieve1);
		Assert.assertTrue(retailTradeRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		if (retailTradeRetrieve1.compareTo(retailTradeCreate) != 0) {
			Assert.assertTrue(false, "查询出的对象的字段与DB读出的不相等");
		}
	}

	@Test
	public void retrieve1Test2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("使用不存在的ID查询零售单");
		RetailTrade rt = new RetailTrade();
		rt.setID(BaseAction.INVALID_ID);
		Map<String, Object> params2 = rt.getRetrieve1Param(BaseBO.INVALID_CASE_ID, rt);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTrade retailTradeCreate2 = (RetailTrade) retailTradeMapper.retrieve1(params2);
		Assert.assertTrue(retailTradeCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1：正常查询，根据商品关键字查询零售单，只有普通商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 零售单主表1
		// 零售单商品1
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个正常状态的普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("贝贝" + Shared.generateStringByTime(4));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO };
		double[] warehousingCommPriceArr = { warehousingCommPrice };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword(commodityGet.getName());
		// 创建零售单
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commIdArrForCreateRetailTrade = { commodityCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCreated.getID() };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, retailTrade1Amount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确:" + totalRetailTradeGross);
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2：根据商品关键字查询零售单，只有多包装商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 参照商品倍数
		final int refCommodityMultiple = random.nextInt(100) + 2;
		// 零售单主表1
		// 零售单商品1
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个正常状态的普通商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		System.out.println(barcodeCreated);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("晶晶多包装" + Shared.generateStringByTime(4));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO };
		double[] warehousingCommPriceArr = { warehousingCommPrice };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword(commmodityMultiPackaging.getName());
		// 创建零售单
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commIdArrForCreateRetailTrade = { commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeMultiPackagingCreated.getID() };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, retailTrade1Amount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3：正常查询，根据商品关键字查询零售单，只有组合商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(100) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 零售单主表1
		// 零售单商品1
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		//
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("组合商品" + Shared.generateStringByTime(4));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("组合");
		// 创建零售单
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID() };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, retailTrade1Amount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确" + totalRetailTradeGross);
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4：正常查询，根据商品关键字查询零售单，只有服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// 零售单主表1
		// 零售单商品1
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = 0;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("迎迎" + Shared.generateStringByTime(4));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword(commodityServiceGet.getName());
		// 创建零售单
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commIdArrForCreateRetailTrade = { commodityServiceCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeServiceCreated.getID() };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, retailTrade1Amount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 ");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		// 创建零售单
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单)");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2, iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		System.out.println("totalpurchasingPrice1" + totalpurchasingPrice1 + "totalpurchasingPrice2" + totalpurchasingPrice2);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确" + totalRetailTradeGross);
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单)");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),商品名称不存在");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("龙行龘龘");
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount == 0, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == 0, "销售数量不正确");
		Assert.assertTrue(dTotalGross == 0, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),staffID不存在");
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setStaffID(BaseAction.INVALID_ID);
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount == 0, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == 0, "销售数量不正确");
		Assert.assertTrue(dTotalGross == 0, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case10：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),paymentype不存在");
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setPaymentType(BaseAction.INVALID_ID);
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount == 0, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == 0, "销售数量不正确");
		Assert.assertTrue(dTotalGross == 0, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case11：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),不存在的时间段");
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		Thread.sleep(1000);
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		Thread.sleep(1000);
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount == 0, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == 0, "销售数量不正确");
		Assert.assertTrue(dTotalGross == 0, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest12() throws Exception {
		Shared.printTestMethodStartInfo();
		/* （ps：零售单售卖商品A10件、40元，售卖商品B20件、60元，那么搜索关键字商品A，结果应该是销售数量为10、销售总额为40元） */
		Shared.caseLog("case12：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),有的零售商品符合传入的关键字，有的不符合关键字");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet3.setName("不匹配" + Shared.generateStringByTime(6));
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaMapper(commodityGet3, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated3);
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID(), commodityCreated3.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID(), barcodeCreated3.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID(), commodityCreated3.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID(), barcodeCreated3.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确:" + totalRetailTradeGross);
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case13：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),有的零售商品符合传入的关键字，有的不符合关键字,零售商品价格有小数");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet3.setName("不匹配" + Shared.generateStringByTime(6));
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaMapper(commodityGet3, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated3);
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID(), commodityCreated3.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID(), barcodeCreated3.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID(), commodityCreated3.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID(), barcodeCreated3.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest14() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case14：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),有的零售商品符合传入的关键字，有的不符合关键字，跳库，创建入库单1不审核、创建入库单2并审核");
		//
		final int warehousingCommNO2 = random.nextInt(100) + 1;
		final double warehousingCommPrice2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000;// 入库价2
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet3.setName("不匹配" + Shared.generateStringByTime(6));
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaMapper(commodityGet3, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated3);
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID(), commodityCreated3.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID(), barcodeCreated3.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// 入库单2，创建但不审核
		// 入库商品
		Warehousing warehousingGet2 = BaseWarehousingTest.DataInput.getWarehousing();
		warehousingGet2.setWarehouseID(1);
		warehousingGet2.setStaffID(Shared.BossID);
		warehousingGet2.setProviderID(1);
		warehousingGet2.setPurchasingOrderID(purchasingOrderApproved.getID());
		Warehousing warehousingCreated2 = BaseWarehousingTest.createViaMapper(warehousingGet2);
		//
		WarehousingCommodity warehousingCommodityGet23 = new WarehousingCommodity();
		warehousingCommodityGet23.setCommodityID(commodityCreated3.getID());
		warehousingCommodityGet23.setNO(warehousingCommNO2);
		warehousingCommodityGet23.setBarcodeID(barcodeCreated3.getID());
		warehousingCommodityGet23.setPrice(warehousingCommPrice2);
		warehousingCommodityGet23.setAmount(warehousingCommPrice2 * warehousingCommNO);
		warehousingCommodityGet23.setShelfLife(22);
		warehousingCommodityGet23.setWarehousingID(warehousingCreated2.getID());
		// 检查字段合法性
		String errorMassage23 = warehousingCommodityGet23.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage23, "");
		//
		WarehousingCommodity warehousingCommCreated23 = BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousingCommodityGet23);
		System.out.println("warehousingCommCreated23=" + warehousingCommCreated23);
		//
		WarehousingCommodity warehousingCommodityGet22 = new WarehousingCommodity();
		warehousingCommodityGet22.setCommodityID(commodityCreated2.getID());
		warehousingCommodityGet22.setNO(warehousingCommNO);
		warehousingCommodityGet22.setBarcodeID(barcodeCreated2.getID());
		warehousingCommodityGet22.setPrice(warehousingCommPrice2);
		warehousingCommodityGet22.setAmount(warehousingCommNO * warehousingCommPrice2);
		warehousingCommodityGet22.setShelfLife(22);
		warehousingCommodityGet22.setWarehousingID(warehousingCreated2.getID());
		// 检查字段合法性
		String errorMassage22 = warehousingCommodityGet22.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage22, "");
		//
		WarehousingCommodity warehousingCommCreated22 = BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousingCommodityGet22);
		System.out.println("warehousingCommCreated22=" + warehousingCommCreated22);
		WarehousingCommodity warehousingCommodityGet21 = new WarehousingCommodity();
		warehousingCommodityGet21.setCommodityID(commodityCreated.getID());
		warehousingCommodityGet21.setNO(warehousingCommNO);
		warehousingCommodityGet21.setBarcodeID(barcodeCreated.getID());
		warehousingCommodityGet21.setPrice(warehousingCommPrice2);
		warehousingCommodityGet21.setAmount(warehousingCommNO * warehousingCommPrice2);
		warehousingCommodityGet21.setShelfLife(22);
		warehousingCommodityGet21.setWarehousingID(warehousingCreated2.getID());
		// 检查字段合法性
		String errorMassage21 = warehousingCommodityGet21.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage21, "");
		//
		WarehousingCommodity warehousingCommCreated21 = BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousingCommodityGet21);
		System.out.println(warehousingCommCreated21);
		// // 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID(), commodityCreated3.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID(), barcodeCreated3.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确");
	}

	@Test
	public void retrieveNByCommodityNameInTimeRangeTest15() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case15：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利 (2个零售单，1个退货单),有的零售商品符合传入的关键字，有的不符合关键字，跨库，入库单1售卖完、接着售卖入库单2");
		final double warehousingCommPrice2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000;// 入库价2
		final int warehousingCommNO2 = 1;// 入库商品数量为1, 为了实现跨库，这个入库数量不随机
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + warehousingCommNO2;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + warehousingCommNO2;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + warehousingCommNO2;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2 + warehousingCommPrice2 * warehousingCommNO2 * 2 - warehousingComm2Price - warehousingCommPrice;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet3.setName("不匹配" + Shared.generateStringByTime(6));
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaMapper(commodityGet3, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated3);
		// 创建子商品1
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated);
		// 创建子商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityGet2.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaMapper(commodityGet2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCreated2);
		// 创建组合商品
		Commodity commodityCombinationGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCombinationGet.setName("多商品" + Shared.generateStringByTime(6));
		commodityCombinationGet.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commodityCombinationCreated = BaseCommodityTest.createCommodityViaMapperWithoutCreateSubCommodity(commodityCombinationGet, BaseBO.CASE_Commodity_CreateComposition);
		// 创建对应的条形码
		Barcodes barcodeCombinationCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityCombinationCreated);
		// 创建从表
		SubCommodity subCommodity1 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity1.setCommodityID(commodityCombinationCreated.getID());
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity1);
		SubCommodity subCommodity2 = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity2.setCommodityID(commodityCombinationCreated.getID());
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		//
		BaseCommodityTest.createSubCommodityViaMapper(subCommodity2);
		// 创建多包装
		Commodity commmodityMultiPackaging = BaseCommodityTest.DataInput.getCommodity();
		commmodityMultiPackaging.setName("多商品" + Shared.generateStringByTime(6));
		commmodityMultiPackaging.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commmodityMultiPackaging.setRefCommodityID(commodityCreated.getID());
		commmodityMultiPackaging.setRefCommodityMultiple(refCommodityMultiple);
		commmodityMultiPackaging.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commmodityMultipleCreated = BaseCommodityTest.createCommodityViaMapper(commmodityMultiPackaging, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建对应的条形码
		Barcodes barcodeMultiPackagingCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commmodityMultipleCreated);
		// 创建服务商品
		Commodity commodityServiceGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityServiceGet.setName("多商品" + Shared.generateStringByTime(6));
		Commodity commodityServiceCreated = BaseCommodityTest.createCommodityViaMapper(commodityServiceGet, BaseBO.CASE_Commodity_CreateService);
		// 创建对应的条形码
		Barcodes barcodeServiceCreated = BaseCommodityTest.retrieveNBarcodesViaMapper(commodityServiceCreated);
		// 创建并审核采购订单
		int[] commIdArr = { commodityCreated.getID(), commodityCreated2.getID(), commodityCreated3.getID() };
		int[] barcodeIdArr = { barcodeCreated.getID(), barcodeCreated2.getID(), barcodeCreated3.getID() };
		PurchasingOrder purchasingOrderApproved = BaseRetailTradeTest.createAndApprovePurchasingOrderViaMapper(purchasingOrderCommNO, priceSuggestion, commIdArr, barcodeIdArr);
		// // 入库单2，入库单品1、单品2、不匹配商品数量均为1，售卖完这张入库单继续售卖下一张
		// // 创建并审核入库单2
		int[] warehousingCommNOArr2 = { warehousingCommNO2, warehousingCommNO2, warehousingCommNO2 };
		double[] warehousingCommPriceArr2 = { warehousingCommPrice2, warehousingCommPrice2, warehousingCommPrice2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr2, warehousingCommPriceArr2, commIdArr, barcodeIdArr, purchasingOrderApproved);
		// 创建并审核入库单1
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		BaseRetailTradeTest.createAndApproveWarehousingViaMapper(warehousingCommNOArr, warehousingCommPriceArr, commIdArr, barcodeIdArr, purchasingOrderApproved);
		//
		RetailTrade retailTradeRetrieveNCondition = new RetailTrade();
		retailTradeRetrieveNCondition.setDatetimeStart(new Date());
		retailTradeRetrieveNCondition.setQueryKeyword("多商品");
		// 创建零售单2
		int[] commIdArrForCreateRetailTrade2 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID(), commodityCreated3.getID() };
		int[] barcodeIdArrForCreateRetailTrade2 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID(), barcodeCreated3.getID() };
		int[] retailTradeCommodityNOArr2 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price, retailTradeCommodity23Price };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr2, retailTradeCommodityPriceArr2, totalRetailTradeAmount2, commIdArrForCreateRetailTrade2, barcodeIdArrForCreateRetailTrade2,
				iSourceRetailTradeID);
		// 创建零售单1
		int[] commIdArrForCreateRetailTrade = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr = { retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity4NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity4Price, retailTradeCommodity3Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr, retailTradeCommodityPriceArr, totalRetailTradeAmount, commIdArrForCreateRetailTrade, barcodeIdArrForCreateRetailTrade, iSourceRetailTradeID);
		// 创建零售单3
		int[] commIdArrForCreateRetailTrade3 = { commodityCombinationCreated.getID(), commodityCreated.getID(), commodityServiceCreated.getID(), commmodityMultipleCreated.getID() };
		int[] barcodeIdArrForCreateRetailTrade3 = { barcodeCombinationCreated.getID(), barcodeCreated.getID(), barcodeServiceCreated.getID(), barcodeMultiPackagingCreated.getID() };
		int[] retailTradeCommodityNOArr3 = { retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity24NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity24Price, retailTradeCommodity23Price };
		BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCommodityNOArr3, retailTradeCommodityPriceArr3, totalRetailTradeAmount2, commIdArrForCreateRetailTrade3, barcodeIdArrForCreateRetailTrade3, retailTradeCreated2.getID());
		// 查询零售单
		retailTradeRetrieveNCondition.setDatetimeEnd(new Date());
		Map<String, Object> paramsForID = retailTradeRetrieveNCondition.getRetrieveNParam(BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb, retailTradeRetrieveNCondition);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bm = (List<BaseModel>) retailTradeMapper.retrieveNByCommodityNameInTimeRange(paramsForID);
		Assert.assertTrue(bm.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		double dRetailAmount = Double.parseDouble(paramsForID.get("dRetailAmount").toString());
		int dTotalCommNO = Integer.parseInt(paramsForID.get("dTotalCommNO").toString());
		double dTotalGross = Double.parseDouble(paramsForID.get("dTotalGross").toString());
		System.out.println("dRetailAmount:" + dRetailAmount);
		System.out.println("dTotalCommNO:" + dTotalCommNO);
		System.out.println("dTotalGross:" + dTotalGross);
		Assert.assertTrue(dRetailAmount - totalRetailTradeAmount < BaseModel.TOLERANCE, "销售总额不正确");
		Assert.assertTrue(dTotalCommNO == totalRetailTradeCommodityNO, "销售数量不正确");
		Assert.assertTrue(dTotalGross - totalRetailTradeGross < BaseModel.TOLERANCE, "销售毛利不正确：" + totalRetailTradeGross);
	}
}
