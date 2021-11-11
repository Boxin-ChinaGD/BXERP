package com.bx.erp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.BxConfigGeneral;

public class SmallSheetFrameMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test(groups = { "SmallSheetFrameUnitTest" })
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Create SmallSheetFrame Test");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("小票底部空行数在允许范围" + SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom + "到" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom  + "之间");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setCountOfBlankLineAtBottom(2);
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:分隔符为空串");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setDelimiterToRepeat("");
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:分隔符为空格");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setDelimiterToRepeat(" ");
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:分隔符为中文的一(yi)");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setDelimiterToRepeat("一");
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:重复创建小票格式（创建时间重复），期望返回数据库中已存在的那条记录");
		// 首次创建
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		Map<String, Object> params = ssf.getCreateParam(BaseBO.INVALID_CASE_ID, ssf);
		// 重复创建
		s = (SmallSheetFrame) smallSheetFrameMapper.create(params);
		Assert.assertTrue(s != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}
	
	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7:小票底部空行数小于" + SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(-2);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}
	
	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8:小票底部空行数大于" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(6);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}
	
	@Test
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9:小票分隔符为null");
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat(null);
		String error = "";
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_delimiterToRepeat);
	}

	@Test
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:logo长度大于163840");
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setID(1);
		StringBuilder sb = new StringBuilder();
		String logo = "123456789012345679801234567890123456798012345678901234567980";
		BxConfigGeneral smallSheetLogoVolume = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
				.read1(BaseCache.MAX_SmallSheetLogoVolume, BaseBO.SYSTEM, new ErrorInfo(), BaseAction.DBName_Public);
		int maxVolume = Integer.parseInt(smallSheetLogoVolume.getValue()) / logo.length() + 1;
		for(int i = 0; i < maxVolume; i++) {
			sb.append(logo);
		}
		smallSheetFrame.setLogo(sb.toString());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_logoSize);
	}
	
	@Test
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:创建小票的数量大于10");
		Shared.caseLog("RetrieveN  SmallSheetFrame Test");
		SmallSheetFrame ssf = new SmallSheetFrame();
		Map<String, Object> params = ssf.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ssf);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ssfList = smallSheetFrameMapper.retrieveN(params);
		//
		int remainderNumber = 10 - ssfList.size();
		List<SmallSheetFrame> ssfListCreated = new ArrayList<>();
		for(int i = 0; i < remainderNumber; i++) {
			SmallSheetFrame smallSheetFrameGet = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
			SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaMapper(smallSheetFrameGet, Shared.DBName_Test);
			ssfListCreated.add(ssfCreated);
			CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_SmallSheet).write1(ssfCreated, Shared.DBName_Test, BaseBO.SYSTEM);
		}
		SmallSheetFrame smallSheetFrameGet = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		String error = smallSheetFrameGet.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_NO);	
		for(int i = 0; i < ssfListCreated.size(); i++) {
			SmallSheetFrame ssfCreated = ssfListCreated.get(i);
			BaseSmallSheetFrameTest.deleteViaMapper(ssfCreated, Shared.DBName_Test);
			CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_SmallSheet).delete1(ssfCreated);
		}
	}
	
	@Test(groups = { "SmallSheetFrameUnitTest" })
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("RetrieveN  SmallSheetFrame Test");
		SmallSheetFrame ssf = new SmallSheetFrame();
		List<BaseModel> ssfList = BaseSmallSheetFrameTest.retrieveNViaMapper(ssf, Shared.DBName_Test);

		Assert.assertTrue(ssfList.size() > 0, "查询失败！");
	}

	@Test(groups = { "SmallSheetFrameUnitTest" })
	public void deleteTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Delete  SmallSheetFrame Test");
		// 创建
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		// 删除
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test(groups = { "SmallSheetFrameUnitTest" })
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Retrieve1  SmallSheetFrame Test");
		// 创建
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		//
		SmallSheetFrame ssfRetrieve1 = BaseSmallSheetFrameTest.retrieve1ViaMapper(s, Shared.DBName_Test);
		Assert.assertTrue(ssfRetrieve1 != null, "删除失败！");
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test(groups = { "SmallSheetFrameUnitTest" })
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常更新");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		s.setLogo("updateTest:xxsf5d6f");
		BaseSmallSheetFrameTest.updateViaMapper(s, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:更新小票格式的小票底部空行数(在"+ SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom + "到" +SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom  +"之间)");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		SmallSheetFrame s = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		s.setCountOfBlankLineAtBottom(2);
		BaseSmallSheetFrameTest.updateViaMapper(s, Shared.DBName_Test);
		// 删除创建的测试对象
		BaseSmallSheetFrameTest.deleteViaMapper(s, Shared.DBName_Test);
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:传入不存在的ID");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setID(Shared.BIG_ID);
		String error = ssf.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), error);
		Map<String, Object> updateParam = ssf.getUpdateParam(BaseBO.INVALID_CASE_ID, ssf);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SmallSheetFrame updateSSF = (SmallSheetFrame) smallSheetFrameMapper.update(updateParam);
		Assert.assertTrue(updateSSF == null && EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	@Test
	public void updateTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:logo长度大于163840");
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setID(1);
		StringBuilder sb = new StringBuilder();
		String logo = "123456789012345679801234567890123456798012345678901234567980";
		BxConfigGeneral smallSheetLogoVolume = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
				.read1(BaseCache.MAX_SmallSheetLogoVolume, BaseBO.SYSTEM, new ErrorInfo(), BaseAction.DBName_Public);
		int maxVolume = Integer.parseInt(smallSheetLogoVolume.getValue()) / logo.length() + 1;
		for(int i = 0; i < maxVolume; i++) {
			sb.append(logo);
		}
		smallSheetFrame.setLogo(sb.toString());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_logoSize);
	}
	
	@Test
	public void updateTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:传入的ID小于等于0");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setID(-1);
		String error = ssf.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		ssf.setID(0);
		error = ssf.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
	}
	
	@Test
	public void updateTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case6:小票底部空行数小于" + SmallSheetFrame.MIN_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(-2);
		smallSheetFrame.setID(1);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}
	
	@Test
	public void updateTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case7:小票底部空行数大于" + SmallSheetFrame.MAX_LENGTH_CountOfBlankLineAtBottom);
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat("-");
		smallSheetFrame.setCountOfBlankLineAtBottom(6);
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
	}
	
	@Test
	public void updateTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8:小票分隔符为null");
		SmallSheetFrame smallSheetFrame = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		smallSheetFrame.setDelimiterToRepeat(null);
		smallSheetFrame.setCountOfBlankLineAtBottom(3);
		smallSheetFrame.setID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		String error = smallSheetFrame.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_delimiterToRepeat);
	}
}
