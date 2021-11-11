package com.bx.erp.model;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.BaseSmallSheetFrameTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;

public class SmallSheetFrameTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		return smallSheetFrame;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		SmallSheetText sst1 = BaseTestNGSpringContextTest.DataInput.getSmallSheetText();
		SmallSheetText sst2 = BaseTestNGSpringContextTest.DataInput.getSmallSheetText();
		// poc2.setCommodityID(2); // 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(sst1);
		bmList.add(sst2);
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

	// 需要继承BaseTestNGSpringContextTest（因要访问缓存），才能测试通过，所以在这里注释掉，然后复制到了ActionTest那边
//	@Test
//	public void testCheckCreate1() {
//		Shared.printTestMethodStartInfo();
//
//		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
//		smallSheetFrame.setDelimiterToRepeat("-");
//		String error = "";
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(error, "");
//	}

	@Test
	public void testCheckCreate2() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票底部空行数在"+ SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom + "到" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom  + "之间");
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(2);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 小票底部空行数小于0
		// 小票底部空行数大于5
	}

	@Test
	public void testCheckCreate3() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票底部空行数小于" + SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(-2);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}

	@Test
	public void testCheckCreate4() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票底部空行数大于" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(6);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}

	@Test
	public void testCheckCreate5() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票分隔符为null");
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat(null);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_delimiterToRepeat);
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setID(1);
		String error = smallSheetFrame.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 测试ID
		smallSheetFrame.setID(BaseAction.INVALID_ID);
		error = smallSheetFrame.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		smallSheetFrame.setID(1);
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setPageIndex(1);
		smallSheetFrame.setPageSize(10);
		String error = smallSheetFrame.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 测试PageIndex
		smallSheetFrame.setPageIndex(0);
		error = smallSheetFrame.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		smallSheetFrame.setPageIndex(1);
		// 测试PageSize
		smallSheetFrame.setPageSize(0);
		error = smallSheetFrame.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		smallSheetFrame.setPageSize(10);
	}

	@Test
	public void testCheckUpdate1() {
		Shared.printTestMethodStartInfo();

		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate2() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票底部空行数"+ SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom + "到" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom  + "之间");
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(2);
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate3() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票底部空行数小于" + SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(-2);
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}

	@Test
	public void testCheckUpdate4() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票底部空行数大于" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(6);
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}

	@Test
	public void testCheckUpdate5() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票分隔符为null");
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat(null);
		smallSheetFrame.setCountOfBlankLineAtBottom(3);
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_delimiterToRepeat);
	}
	
	@Test
	public void testCheckUpdate6() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("小票ID小于等于0");
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(3);
		smallSheetFrame.setID(-1);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		smallSheetFrame.setID(0);
		error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
	}
	
//	需要继承BaseTestNGSpringContextTest（因要访问缓存），才能测试通过，所以在这里注释掉，然后复制到了ActionTest那边
//	@Test
//	public void testCheckUpdate6() throws CloneNotSupportedException {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("logo长度大于163840");
//		SmallSheetFrame smallSheetFrame = BaseTestNGSpringContextTest.DataInput.getSmallSheetFrame();
//		smallSheetFrame.setID(1);
//		StringBuilder sb = new StringBuilder();
//		for(int i = 0; i < 16385; i++) {
//			sb.append("1234567890");
//		}
//		smallSheetFrame.setLogo(sb.toString());
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_logoSize);
//	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setID(0);
		String error = smallSheetFrame.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		smallSheetFrame.setID(BaseAction.INVALID_ID);
		error = smallSheetFrame.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		smallSheetFrame.setID(1);
		error = smallSheetFrame.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
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
