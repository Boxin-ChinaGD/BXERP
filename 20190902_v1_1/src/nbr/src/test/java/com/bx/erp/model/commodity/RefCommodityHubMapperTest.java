package com.bx.erp.model.commodity;

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
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class RefCommodityHubMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static RefCommodityHub refCommodityHubInput = null;

		protected static final RefCommodityHub getRefCommodityHub() throws CloneNotSupportedException {
			refCommodityHubInput = new RefCommodityHub();
			refCommodityHubInput.setName("可乐薯片南瓜味" + System.currentTimeMillis() % 1000000);
			refCommodityHubInput.setBarcode("1231" + System.currentTimeMillis() % 1000000);
			refCommodityHubInput.setShortName("薯片");
			refCommodityHubInput.setSpecification("克");
			refCommodityHubInput.setPackageUnitName("瓶");
			refCommodityHubInput.setPurchasingUnit("箱");
			refCommodityHubInput.setBrandName("默认品牌");
			refCommodityHubInput.setCategoryName("");
			refCommodityHubInput.setMnemonicCode("SP");
			refCommodityHubInput.setPricingType(1);
			refCommodityHubInput.setPricePurchase(Math.abs(new Random().nextDouble()));
			refCommodityHubInput.setPriceRetail(Math.abs(new Random().nextDouble()));
			refCommodityHubInput.setPriceVIP(Math.abs(new Random().nextDouble()));
			refCommodityHubInput.setPriceWholesale(Math.abs(new Random().nextDouble()));
			refCommodityHubInput.setShelfLife(Math.abs(new Random().nextInt(18)));
			refCommodityHubInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
			refCommodityHubInput.setType(0);

			return (RefCommodityHub) refCommodityHubInput.clone();
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
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN RefCommodityHub Test ------------------------");

		Shared.caseLog("case 1:根据完整条形码查找所有有关的参照商品");
		RefCommodityHub RefCommodityHub = DataInput.getRefCommodityHub();
		RefCommodityHub.setBarcode("123456789");
		Map<String, Object> params = RefCommodityHub.getRetrieveNParam(BaseBO.INVALID_CASE_ID, RefCommodityHub);
		DataSourceContextHolder.setDbName(Shared.StaticDBName_Test);
		List<BaseModel> retrieveN = refCommodityHubMapper.retrieveN(params);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println(retrieveN);

		Shared.caseLog("case 2:根据不完整条形码查找所有有关的参照商品");
		RefCommodityHub.setBarcode("1234");
		Map<String, Object> params1 = RefCommodityHub.getRetrieveNParam(BaseBO.INVALID_CASE_ID, RefCommodityHub);
		DataSourceContextHolder.setDbName(Shared.StaticDBName_Test);
		List<BaseModel> retrieveN1 = refCommodityHubMapper.retrieveN(params1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertTrue(retrieveN1.size() == 0);

		Shared.caseLog("case 3:不输入条形码查找所有有关的参照商品");
		RefCommodityHub.setBarcode("");
		Map<String, Object> params2 = RefCommodityHub.getRetrieveNParam(BaseBO.INVALID_CASE_ID, RefCommodityHub);
		DataSourceContextHolder.setDbName(Shared.StaticDBName_Test);
		List<BaseModel> retrieveN2 = refCommodityHubMapper.retrieveN(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertTrue(retrieveN2.size() == 0);

		Shared.caseLog("case4:公司A可以查询到参考商品a"); // 原本的测试用例是公司A查询到参考商品a，公司B也可以查询到参考商品a，但是没有第二个公司拿来测试
		// 公司A
		RefCommodityHub RefCommodityHubA = DataInput.getRefCommodityHub();
		RefCommodityHubA.setBarcode("6926116000106");
		Map<String, Object> paramsA = RefCommodityHubA.getRetrieveNParam(BaseBO.INVALID_CASE_ID, RefCommodityHubA);
		DataSourceContextHolder.setDbName(Shared.StaticDBName_Test);
		List<BaseModel> retrieveNA = refCommodityHubMapper.retrieveN(paramsA);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertTrue(retrieveNA.size() == 1);
	}
}
