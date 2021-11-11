package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class VipSourceMapperTest extends BaseMapperTest {
	protected static final int vipID = 1;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常添加");
		// 创建会员来源表
		VipSource vipSource = BaseVipSourceTest.DataInput.getVipSource(vipID);
		VipSource VipSourceCreate = BaseVipSourceTest.createViaMapper(vipSource);
		//
		BaseVipSourceTest.deleteViaMapper(VipSourceCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:会员不存在");
		// 创建会员来源表
		VipSource vipSource = BaseVipSourceTest.DataInput.getVipSource(BaseAction.INVALID_ID);
		Map<String, Object> params = vipSource.getCreateParam(BaseBO.INVALID_CASE_ID, vipSource);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipSource vipCardCodeCreate = (VipSource) vipSourceMapper.create(params);
		Assert.assertTrue(vipCardCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r vipSource=" + vipSource);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:重复添加，错误码0，返回数据库对象");
		// 创建会员来源表
		VipSource vipSource = BaseVipSourceTest.DataInput.getVipSource(vipID);
		VipSource VipSourceCreate = BaseVipSourceTest.createViaMapper(vipSource);
		//
		VipSource VipSourceCreate2 = BaseVipSourceTest.createViaMapper(vipSource);
		Assert.assertTrue(VipSourceCreate2 != null, "重复创建会员来源失败");
		//
		BaseVipSourceTest.deleteViaMapper(VipSourceCreate);
	}
	
	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case4:第一次创建会员来源,ID1、ID2、ID3都传空串，第二次创建ID1、ID2、ID3不传空串(模拟在nbr创建会员，然后在小程序登录) ");
		// 创建会员来源表
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreated = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		VipSource vipSourceGet = BaseVipSourceTest.DataInput.getVipSource(vipCreated.getID());
		vipSourceGet.setID1("");
		vipSourceGet.setID2("");
		vipSourceGet.setID3("");
		VipSource VipSourceCreate = BaseVipSourceTest.createViaMapper(vipSourceGet);
		//
		VipSource vipSourceGet2 = BaseVipSourceTest.DataInput.getVipSource(vipCreated.getID());
		vipSourceGet2.setID1("1111111111111111111111");
		vipSourceGet2.setID2("2222222222222222222222");
		vipSourceGet2.setID3("3333333333333333333333");
		// createViaMapper已进行了compareTo
		BaseVipSourceTest.createViaMapper(vipSourceGet2);
		//
		BaseVipSourceTest.deleteViaMapper(VipSourceCreate);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常查询");
		// 创建会员来源表
		VipSource vipSource = BaseVipSourceTest.DataInput.getVipSource(vipID);
		VipSource VipSourceCreate = BaseVipSourceTest.createViaMapper(vipSource);
		//
		VipSource vipSourceRetrieve1 = (VipSource) BaseVipSourceTest.retrieve1ViaMapper(VipSourceCreate);
		Assert.assertTrue(vipSourceRetrieve1 != null, "查询会员来源失败");
		//
		BaseVipSourceTest.deleteViaMapper(VipSourceCreate);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常查询");
		// 创建会员来源表
		VipSource vipSource = BaseVipSourceTest.DataInput.getVipSource(vipID);
		VipSource VipSourceCreate = BaseVipSourceTest.createViaMapper(vipSource);
		//
		Map<String, Object> params = vipSource.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vipSource);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipSourceRetrieveN = vipSourceMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : vipSourceRetrieveN) {
			VipSource vs = (VipSource) bm;
			err = vs.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		BaseVipSourceTest.deleteViaMapper(VipSourceCreate);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除");
		// 创建会员来源表
		VipSource vipSource = BaseVipSourceTest.DataInput.getVipSource(vipID);
		VipSource VipSourceCreate = BaseVipSourceTest.createViaMapper(vipSource);
		// 删除会员来源
		BaseVipSourceTest.deleteViaMapper(VipSourceCreate);
	}
}
