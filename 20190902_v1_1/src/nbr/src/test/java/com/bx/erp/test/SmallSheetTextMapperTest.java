package com.bx.erp.test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class SmallSheetTextMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static SmallSheetText sst = new SmallSheetText();

		protected static final SmallSheetText getSmallSheetText() throws Exception {
			Random r = new Random();

			sst.setContent("11" + String.valueOf(System.currentTimeMillis()).substring(3));
			sst.setSize(r.nextDouble() + 1);
			sst.setBold(0);
			sst.setFrameId(r.nextInt(6) + 1); // DB存在1-6的小票格式（主表ID）
			sst.setGravity(r.nextInt(10) + 1);

			return (SmallSheetText) sst.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create  SmallSheetText Test ------------------------");

		SmallSheetText smallSheetText = DataInput.getSmallSheetText();
		Map<String, Object> createParam = smallSheetText.getCreateParam(BaseBO.INVALID_CASE_ID, smallSheetText);

		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SmallSheetText sst = (SmallSheetText) smallSheetTextMapper.create(createParam);
		smallSheetText.setIgnoreIDInComparision(true);
		if (smallSheetText.compareTo(sst) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Update  SmallSheetText Test ------------------------");

		SmallSheetText smallSheetText = DataInput.getSmallSheetText();
		Map<String, Object> createParam = smallSheetText.getCreateParam(BaseBO.INVALID_CASE_ID, smallSheetText);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SmallSheetText sstCreate = (SmallSheetText) smallSheetTextMapper.create(createParam);
		smallSheetText.setIgnoreIDInComparision(true);
		if (smallSheetText.compareTo(sstCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		SmallSheetText sst = DataInput.getSmallSheetText();
		sst.setID(sstCreate.getID());
		Map<String, Object> updateParam = sst.getUpdateParam(BaseBO.INVALID_CASE_ID, sst);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SmallSheetText sstUpdate = (SmallSheetText) smallSheetTextMapper.update(updateParam);
		sst.setIgnoreIDInComparision(true);
		if (sst.compareTo(sstUpdate) != 0) {
			Assert.assertTrue(false, "更新的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 删除创建出的小票格式从表
		SmallSheetText sstdelete = new SmallSheetText();
		sstdelete.setID(sstCreate.getID());
		Map<String, Object> params = sst.getDeleteParam(BaseBO.INVALID_CASE_ID, sstdelete);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		smallSheetTextMapper.delete(params);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ RetrieveN SmallSheetText Test ------------------------");

		SmallSheetText sst = DataInput.getSmallSheetText();
		Map<String, Object> params = sst.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sst);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = smallSheetTextMapper.retrieveN(params);
		for (BaseModel bm : list) {
			System.out.println(bm);
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询成功！");
	}

	@Test
	public void deleteTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Delete  SmallSheetText Test ------------------------");

		SmallSheetText smallSheetText = DataInput.getSmallSheetText();
		Map<String, Object> createParam = smallSheetText.getCreateParam(BaseBO.INVALID_CASE_ID, smallSheetText);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SmallSheetText sst = (SmallSheetText) smallSheetTextMapper.create(createParam);
		smallSheetText.setIgnoreIDInComparision(true);
		if (smallSheetText.compareTo(sst) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		Map<String, Object> params = sst.getDeleteParam(BaseBO.INVALID_CASE_ID, sst);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		smallSheetTextMapper.delete(params);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}
}
