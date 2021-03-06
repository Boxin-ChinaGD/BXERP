package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.VipCategory;
import com.bx.erp.util.DataSourceContextHolder;

public class VipCategoryMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static VipCategory vipCategoryInput = null;

		protected static final VipCategory getData() throws CloneNotSupportedException, InterruptedException {
			vipCategoryInput = new VipCategory();
			Thread.sleep(1000);
			vipCategoryInput.setName("ι»ιδΌε1" + String.valueOf(System.currentTimeMillis()).substring(6));

			return (VipCategory) vipCategoryInput.clone();
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
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Retrieve VipCategory Test ----------------------");

		VipCategory vc = new VipCategory();
		Map<String, Object> params = vc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = vipCategoryMapper.retrieveN(params);
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "εε»Ίηε―Ήθ±‘ηε­ζ?΅δΈDBθ―»εΊηδΈηΈη­");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ Retrieve1 VipCategory Test ----------------------");

		System.out.println("\n------------------------ CASE1:η¨IDζ₯θ―’ ----------------------");
		VipCategory vc1 = new VipCategory();
		vc1.setID(vipCategiryCreate.getID());
		Map<String, Object> params1 = vc1.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategory = (VipCategory) vipCategoryMapper.retrieve1(params1);
		Assert.assertNotNull(vipCategory, "ζ₯θ―’ε€±θ΄₯");
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ CASE2:η¨δΈε­ε¨ηIDζ₯θ―’ ----------------------");
		vc1.setID(-22);
		Map<String, Object> params2 = vc1.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategory2 = (VipCategory) vipCategoryMapper.retrieve1(params2);
		assertTrue(vipCategory2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create VipCategory Test ----------------------");
		// Case1: ζ­£εΈΈεε»Ί
		System.out.println("\n------------------------ Case1: ζ­£εΈΈεε»Ί ----------------------");
		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "εε»Ίηε―Ήθ±‘ηε­ζ?΅δΈDBθ―»εΊηδΈηΈη­");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// case2οΌδΌεη±»ε«εη§°ιε€
		System.out.println("\n------------------------ Case2οΌδΌεη±»ε«εη§°ιε€----------------------");
		vc.setName("ι»ιδΌε");
		Map<String, Object> paramsForCreate1 = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.create(paramsForCreate1);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void updateTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Update VipCategory Test ----------------------");

		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		//
		System.out.println("εε»ΊδΌεη±»ε«ε―Ήθ±‘ζιθ¦ηε―Ήθ±‘οΌ" + vc);
		System.out.println("εε»ΊεΊηδΌεη±»ε«ε―Ήθ±‘ζ―οΌ" + vipCategiryCreate);
		//
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "εε»Ίηε―Ήθ±‘ηε­ζ?΅δΈDBθ―»εΊηδΈηΈη­");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ case 1:δΌεη±»ε«εη§°ιε€οΌιθ――η εΊθ―₯ζ―1----------------------");
		VipCategory vc1 = DataInput.getData();
		vc1.setID(vipCategiryCreate.getID());
		vc1.setName("ι»ιδΌε");
		Map<String, Object> paramsForUpdate1 = vc1.getUpdateParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.update(paramsForUpdate1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);

		System.out.println("\n------------------------ case 2: δΌεη±»ε«εη§°δΈιε€οΌιθ――η εΊθ―₯ζ―0 ----------------------");
		vc1.setName("θ¨εΎ·1" + String.valueOf(System.currentTimeMillis()).substring(6));
		Map<String, Object> paramsForUpdate = vc1.getUpdateParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.update(paramsForUpdate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Delete VipCategoty Test ------------------------");

		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "εε»Ίηε―Ήθ±‘ηε­ζ?΅δΈDBθ―»εΊηδΈηΈη­");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ case1:δΌεθ‘¨δΈ­ε·²ζθ―₯δΌεηη±»ε«γε ι€δΈδΊοΌιθ――δ»£η δΈΊ7----------------------");
		vc.setID(1);
		Map<String, Object> paramsForDelete1 = vc.getDeleteParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.delete(paramsForDelete1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		System.out.println("\n------------------------ case2:δΌεθ‘¨δΈ­ζ²‘ζθΏη§δΌεηη±»ε«γε―δ»₯η΄ζ₯ε ι€οΌιθ――δ»£η δΈΊ0----------------------");
		VipCategory vc1 = DataInput.getData();
		vc1.setID(vipCategiryCreate.getID());
		Map<String, Object> paramsForDelete2 = vc1.getDeleteParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.delete(paramsForDelete2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ case3:ε ι€δΈδΈͺδΈε­ε¨ηId----------------------");
		vc1.setID(-22);
		Map<String, Object> paramsForDelete3 = vc1.getDeleteParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.delete(paramsForDelete3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

	}
}
