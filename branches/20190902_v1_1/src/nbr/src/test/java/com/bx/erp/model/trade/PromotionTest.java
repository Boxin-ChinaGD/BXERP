package com.bx.erp.model.trade;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelTest;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumSubStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;

public class PromotionTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException {
		Promotion promotion = BasePromotionTest.DataInput.getPromotion();
		return promotion;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException {
		master = BasePromotionTest.DataInput.getPromotion();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException, InterruptedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		PromotionScope ps1 = BasePromotionTest.DataInput.getPromotioninfo();
		PromotionScope ps2 = BasePromotionTest.DataInput.getPromotioninfo();
		ps2.setCommodityID(2);// 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(ps1);
		bmList.add(ps2);
		return bmList;
	}

	@Override
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		slave.remove(0);
		return slave;
	}

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkUpdate() throws ParseException {
		Shared.printTestMethodStartInfo();
		Promotion p = new Promotion();

		// 设除了starttime以外的正常初始值
		String end = "2039-3-16 10:10:20";
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		Date endDate = sdf.parse(end);
		p.setID(1);
		p.setDatetimeEnd(endDate);
		p.setExcecutionThreshold(1);
		p.setExcecutionAmount(1);
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setName("checkUpdate");
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setStaff(1);
		String err = "";

		// 活动开始时间必须至少从明天开始！
		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 1));
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
		//
		p.setDatetimeStart(new Date());
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
		//
		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2));
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 开始时间必须在结束时间之前！
		String start = "3320-3-13 0:0:0";
		Date startDate = sdf.parse(start);
		p.setDatetimeStart(startDate);
		end = "3320-3-13 0:0:0";
		endDate = sdf.parse(end);
		p.setDatetimeEnd(endDate);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_datetimeStart_datetimeEnd);

		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2));
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 阀值不能小于等于0
		p.setExcecutionThreshold(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
		p.setExcecutionThreshold(-10);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);

		p.setExcecutionThreshold(1);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setExcecutionThreshold(11);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 阀值需大于满减金额！
		p.setExcecutionThreshold(11);
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionAmount(11);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setExcecutionAmount(12);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
		//
		p.setExcecutionAmount(-1);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
		//
		p.setExcecutionAmount(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
		//
		p.setExcecutionAmount(10);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		p.setExcecutionDiscount(1.01f);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(1);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setExcecutionDiscount(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(-0.01f);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(-1);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(0.99f);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setExcecutionDiscount(0.01f);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// type必须是0或1
		p.setType(BaseAction.INVALID_Type);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
		//
		p.setType(2);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
		//
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2));
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// name不为null且长度不大于32
		p.setName(null);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
		//
		p.setName("阿斯顿122309阿萨德请问才杂事0说的insert4-1IKEA132出去玩313认识的罚点球2312");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
		//
		p.setName("");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
		//
		p.setName("name不为null");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// scope必须是0或1
		p.setScope(-1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
		//
		p.setScope(2);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
		//
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// status必须是0或1
		p.setStatus(-1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
		//
		p.setStatus(2);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
		//
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		// ID必须大于0
		p.setID(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(1);
		// staffID必须大于0
		p.setStaff(0);
		err = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_staff);
		p.setStaff(1);
	}

	@Test
	public void checkCreate() throws ParseException {
		Shared.printTestMethodStartInfo();
		Promotion p = new Promotion();

		// 设除了starttime以外的正常初始值
		String end = "2039-3-16 10:10:20";
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		Date endDate = sdf.parse(end);
		p.setDatetimeEnd(endDate);
		p.setExcecutionThreshold(1);
		p.setExcecutionAmount(1);
		p.setName("case1");
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setStaff(1);
		String err = "";

		// 活动开始时间必须至少从明天开始！
		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 1));
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
		//
		p.setDatetimeStart(new Date());
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
		//
		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2));
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 开始时间必须在结束时间之前！
		String start = "3320-3-13 0:0:0";
		Date startDate = sdf.parse(start);
		p.setDatetimeStart(startDate);
		end = "3320-3-13 0:0:0";
		endDate = sdf.parse(end);
		p.setDatetimeEnd(endDate);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_datetimeStart_datetimeEnd);

		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2));
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 阀值不能小于等于0
		p.setExcecutionThreshold(0);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
		p.setExcecutionThreshold(-10);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);

		p.setExcecutionThreshold(1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		p.setExcecutionThreshold(11);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// 阀值需大于满减金额！
		p.setExcecutionThreshold(11);
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionAmount(11);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setExcecutionAmount(12);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
		//
		p.setExcecutionAmount(-1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
		//
		p.setExcecutionAmount(0);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
		//
		p.setExcecutionAmount(10);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		p.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		p.setExcecutionDiscount(1.01f);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setExcecutionDiscount(0);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(-0.01f);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(-1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
		//
		p.setExcecutionDiscount(0.99f);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setExcecutionDiscount(0.01f);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// type必须是0或1
		p.setType(BaseAction.INVALID_Type);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
		//
		p.setType(2);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
		//
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// name不为null且长度不大于32
		p.setName(null);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
		//
		p.setName("阿斯顿122309阿萨德请问才杂事0说的insert4-1IKEA132出去玩313认识的罚点球2312");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
		//
		p.setName("");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
		//
		p.setName("name不为null");
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// scope必须是0或1
		p.setScope(-1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
		//
		p.setScope(2);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
		//
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// status必须是0或1
		p.setStatus(-1);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
		//
		p.setStatus(2);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
		//
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		// staffID必须大于0
		p.setStaff(0);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_staff);
		p.setStaff(1);
		// 满减阀值大于满减金额 允许的
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(100);
		p.setExcecutionAmount(10);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		// 满减阀值等于满减金额 允许的
		p.setExcecutionThreshold(100);
		p.setExcecutionAmount(100);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		// 满减阀值小于满减金额 不允许的
		p.setExcecutionThreshold(10);
		p.setExcecutionAmount(20);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
		// 满减阀值大于满减金额但是满减阀值超过最大值 不允许的
		p.setExcecutionThreshold(10001);
		p.setExcecutionAmount(10);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
		// 满减阀值等于满减金额但是满减阀值超过最大值 不允许的
		p.setExcecutionThreshold(10001);
		p.setExcecutionAmount(10001);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
		// 满减阀值小于满减金额但是满减金额超过最大值 不允许的
		p.setExcecutionThreshold(10);
		p.setExcecutionAmount(10001);
		err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();

		// ID必须大于0
		Promotion p = new Promotion();
		p.setID(0);
		assertEquals(p.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(-1);
		assertEquals(p.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		assertEquals(p.checkDelete(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		Promotion p = new Promotion();

		// 检查POSID
		p.setPosID(0);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_posID);
		
		// 检查web请求
		p.setPosID(BaseAction.INVALID_ID);
		// 检查status
		p.setStatus(-2);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_statusForRetrieveN);
		//
		p.setStatus(3);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_statusForRetrieveN);
		//
		p.setStatus(BaseAction.INVALID_STATUS);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查queryKeyword
		p.setQueryKeyword(RandomStringUtils.randomAlphanumeric(40));
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_nameForRetrieveN);
		//
		p.setQueryKeyword(null);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		p.setQueryKeyword(RandomStringUtils.randomAlphanumeric(12));
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		p.setSubStatusOfStatus(Promotion.ACTIVE);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查page indx
		p.setPageIndex(-1);
		p.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		p.setPageIndex(BaseAction.PAGE_StartIndex);
		p.setPageSize(-1);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		p.setPageIndex(BaseAction.PAGE_StartIndex);
		p.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		
		// 检查pos请求
		p.setPosID(1);
		// 检查page indx
		p.setPageIndex(2);
		p.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_Paging);
		p.setPageIndex(1);
		//
		p.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_Paging);
		p.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		// 检查status
		p.setStatus(Promotion.EnumStatusPromotion.ESP_Deleted.getIndex());
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_subStatusOfStatus);
		p.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
		//
		p.setSubStatusOfStatus(EnumSubStatusPromotion.ESSP_ToStart.getIndex());
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_subStatusOfStatus);
		p.setSubStatusOfStatus(EnumSubStatusPromotion.ESSP_ToStartAndPromoting.getIndex());
		//检查queryKeyword
		p.setQueryKeyword(RandomStringUtils.randomAlphanumeric(12));
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_queryKeyword);
		p.setQueryKeyword(null);
		assertEquals(p.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();

		// ID必须大于0
		Promotion p = new Promotion();
		p.setID(0);
		assertEquals(p.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(-1);
		assertEquals(p.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		assertEquals(p.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCompareTo1() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case1();
	}

	@Test
	public void testCompareTo2() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case2();
	}

	@Test
	public void testCompareTo3() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case3();
	}

	@Test
	public void testCompareTo4() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case4();
	}

	@Test
	public void testCompareTo5() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case5();
	}

	@Test
	public void testCompareTo6() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case6();
	}

	@Test
	public void testCompareTo7() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case7();
	}

	@Test
	public void testCompareTo8() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case8();
	}

	@Test
	public void testCompareTo9() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case9();
	}

	@Test
	public void testCompareTo10() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case10();
	}

}
