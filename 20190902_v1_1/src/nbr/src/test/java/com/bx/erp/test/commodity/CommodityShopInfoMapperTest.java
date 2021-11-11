package com.bx.erp.test.commodity;

import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CommodityShopInfoMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}
	
	@Test
	public void createNTest1() {
		// TODO
	}
	
	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:根据商品ID进行查询");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(commCreated);
		Assert.assertTrue(listCommodityShopInfo.size() != 0);
		for(BaseModel bm : listCommodityShopInfo) {
			CommodityShopInfo commodityShopInfoRN = (CommodityShopInfo) bm;
			Assert.assertTrue(commodityShopInfoRN.getCommodityID() == commCreated.getID(), "根据商品ID查询，查询到的commodityShopInfo的商品ID应该要等于条件商品ID");
		}
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:根据商品ID和门店ID进行查询");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		int shopID = 2;
		commCreated.setShopID(shopID);
		//
		List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(commCreated);
		Assert.assertTrue(listCommodityShopInfo.size() != 0);
		for(BaseModel bm : listCommodityShopInfo) {
			CommodityShopInfo commodityShopInfoRN = (CommodityShopInfo) bm;
			Assert.assertTrue(commodityShopInfoRN.getCommodityID() == commCreated.getID() && commodityShopInfoRN.getShopID() == 2, "根据商品ID和门店ID查询，查询到的commodityShopInfo的商品ID应该要等于条件商品ID");
		}
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void retrieveNTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:根据商品ID和门店ID进行查询, 门店ID不存在");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		int shopID = -1;
		commCreated.setShopID(shopID);
		//
		List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(commCreated);
		Assert.assertTrue(listCommodityShopInfo.size() == 0);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void retrieveNTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:根据门店ID进行查询");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setShopID(2);
		comm.setID(BaseAction.INVALID_ID);
		//
		List<BaseModel> listCommodityShopInfo = BaseCommodityTest.getListCommodityShopInfoByCommID(comm);
		Assert.assertTrue(listCommodityShopInfo.size() != 0);
		for(BaseModel bm : listCommodityShopInfo) {
			CommodityShopInfo commodityShopInfoRN = (CommodityShopInfo) bm;
			Assert.assertTrue(commodityShopInfoRN.getShopID() == 2, "根据商品ID和门店ID查询，查询到的commodityShopInfo的商品ID应该要等于条件商品ID");
		}
		System.out.println("listCommodityShopInfo：" + listCommodityShopInfo);
		//
	}
}
