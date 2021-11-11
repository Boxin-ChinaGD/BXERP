package com.bx.erp.model.trade;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class RetailTradePromotingTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException {
		// 到时候重构把DataInput.getRetailTradePromoting()改到baseXxxTest;
		RetailTradePromoting retailTradePromoting = BasePromotionTest.DataInput.getRetailTradePromoting();
		return retailTradePromoting;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BasePromotionTest.DataInput.getRetailTradePromoting();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		RetailTradePromotingFlow rpf1 = BasePromotionTest.DataInput.getRetailTradePromotingFlow();
		RetailTradePromotingFlow rpf2 = BasePromotionTest.DataInput.getRetailTradePromotingFlow();
		/*因为rpf1,rpf2具有相同主表，那么他们的promotionID必须是不一样的(一样的话也会导致compareTo通不过)*/
		rpf1.setPromotionID(1);
		rpf2.setPromotionID(2);
		//
		bmList.add(rpf1);
		bmList.add(rpf2);
		return bmList;
	}

	@Override
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		slave.remove(0);
		return slave;
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkCreate() {
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		//
		assertEquals(retailTradePromoting.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromoting.FIELD_ERROR_tradeID);
		//
		retailTradePromoting.setTradeID(BaseAction.INVALID_ID);
		assertEquals(retailTradePromoting.checkCreate(BaseBO.INVALID_CASE_ID), RetailTradePromoting.FIELD_ERROR_tradeID);
		//
		retailTradePromoting.setTradeID(1);
		assertEquals(retailTradePromoting.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieve1() {
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		//
		assertEquals(retailTradePromoting.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		retailTradePromoting.setID(BaseAction.INVALID_ID);
		assertEquals(retailTradePromoting.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		retailTradePromoting.setID(1);
		assertEquals(retailTradePromoting.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
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
