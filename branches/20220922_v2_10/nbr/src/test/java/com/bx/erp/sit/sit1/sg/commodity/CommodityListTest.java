
package com.bx.erp.sit.sit1.sg.commodity;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class CommodityListTest extends BaseActionTest {
	protected AtomicInteger order;
	protected AtomicInteger commodityOrder;
	protected AtomicLong barcode;
	protected final String CommonCommodity = "普通商品";
	protected final String CombinationCommodity = "组合商品";
	protected final String MultiPackagingCommodity = "多包装商品";
	protected final String ServiceCommodity = "服务商品";
	protected static String dbNameOfNewCompany1 = null;
	protected final String bossPhoneOfNewCompany1 = "18814126900";
	protected static String bossPasswordOfNewCompany1 = "666666";
	protected Map<String, BaseModel> CommodityListMap;
	private static final String PhoneOfPreSale = "13888888888";
	private static final String Password_Default = "000000";
	private static String companySNOfNewCompany1 = "668868";
	private Company newCompany1 = null;

	/** 存放创建公司1时返回的MvcResult 由于做company的结果验证会第一次登录老板账号并修改密码之后删除售前账号
	 * 所以存起来以便测试完售前账号之后做company结果验证 */
	private static MvcResult mvcResult_Company;

	private List<PackageUnit> lspu;
	private List<PackageUnit> lspuCreated;
	private List<PackageUnit> lspuUpdated;
	private List<PackageUnit> lspuDeleted;

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(1);
		barcode = new AtomicLong();
		barcode.set(6821423302394L);
		CommodityListMap = new HashMap<String, BaseModel>();
	}

	@Test
	public void createCompany1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建公司1");

		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPhone(bossPhoneOfNewCompany1);
		company.setBossPassword(bossPasswordOfNewCompany1);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP).session(sessionOP)).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Company companyCreate = (Company) new Company().parse1(jsonObject.getString(BaseAction.KEY_Object));
		companySNOfNewCompany1 = companyCreate.getSN();
		dbNameOfNewCompany1 = companyCreate.getDbName();
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到下下个用例再做结果验证吧
		mvcResult_Company = mr1;
		newCompany1 = companyCreate;
		//
		// ...可能会有手动操作，比如手动交付POS机一台
	}

	/** 创建公司之后一旦登录了老板账号进行首次修改密码，售前账号会被删除，所以把售前账号的测试放在创建公司后面 */
	@Test(dependsOnMethods = "createCompany1")
	public void cannotUsePhoneOfPreSaleLoginAndCUDCommodityList() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "售前人员无法进行CUD操作");

		// 售前账号登录
		sessionPreSale = Shared.getStaffLoginSession(mvc, PhoneOfPreSale, Password_Default, companySNOfNewCompany1);

		// 创建普通商品53
		Commodity c53 = BaseCommodityTest.DataInput.getCommodity();
		c53.setName("普通商品53");
		c53.setMultiPackagingInfo(barcode + ";" + c53.getPackageUnitID() + ";" + c53.getRefCommodityMultiple() + ";" + c53.getPriceRetail() + ";" + c53.getPriceVIP() + ";" + c53.getPriceWholesale() + ";" + c53.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c53, sessionPreSale)).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
		//
		// ...刚创建的公司什么数据都没有，不能进行UD操作
	}

	@Test(dependsOnMethods = "cannotUsePhoneOfPreSaleLoginAndCUDCommodityList")
	public void openCommodityListPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "新店开张，打开商品列表页面");

		JSONObject jsonObject = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		companySNOfNewCompany1 = JsonPath.read(jsonObject, "$.object.SN");
		// 结果验证
		CompanyCP.verifyCreate(mvcResult_Company, newCompany1, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, newCompany1);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(newCompany1.getDbName(), mvc, mapBO, companySNOfNewCompany1, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1);
		bossPasswordOfNewCompany1 = BaseCompanyTest.bossPassword_New;// 修改密码成功后把旧密码替换
		newCompany1.setBossPassword(bossPasswordOfNewCompany1);
		// 使用创建出来的BOSS账号登录SESSION
		try {
			sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "openCommodityListPage")
	public void createCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建普通商品1/2/3/4，组合商品5(2+3)，多包装商品6(ref=4)后重新打开商品列表页面");

		// 创建普通商品1
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName(CommonCommodity + commodityOrder);
		c1.setMultiPackagingInfo(barcode + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity1", commodity1);
		// 创建普通商品2
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setName(CommonCommodity + getCommodityOrder());
		c2.setMultiPackagingInfo(getBarcode() + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		Commodity commodity2 = BaseCommodityTest.createCommodityViaAction(c2, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity2", commodity2);
		// 创建普通商品3
		Commodity c3 = BaseCommodityTest.DataInput.getCommodity();
		c3.setName(CommonCommodity + getCommodityOrder());
		c3.setMultiPackagingInfo(getBarcode() + ";" + c3.getPackageUnitID() + ";" + c3.getRefCommodityMultiple() + ";" + c3.getPriceRetail() + ";" + c3.getPriceVIP() + ";" + c3.getPriceWholesale() + ";" + c3.getName() + ";");
		Commodity commodity3 = BaseCommodityTest.createCommodityViaAction(c3, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity3", commodity3);
		//
		// 创建普通商品4和多包装商品5(ref=4)
		Commodity c4 = BaseCommodityTest.DataInput.getCommodity();
		c4.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c5 = BaseCommodityTest.DataInput.getCommodity();
		c5.setRefCommodityMultiple(2);
		c5.setPackageUnitID(2);
		c5.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c4.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c4.getPackageUnitID() + "," + c5.getPackageUnitID() + ";" + c4.getRefCommodityMultiple() + "," + c5.getRefCommodityMultiple() + ";" + c4.getPriceRetail() + ","
				+ c5.getPriceRetail() + ";" + c4.getPriceVIP() + "," + c5.getPriceVIP() + ";" + c4.getPriceWholesale() + "," + c5.getPriceWholesale() + ";" + c4.getName() + "," + c5.getName() + ";");
		Commodity commodity4 = BaseCommodityTest.createCommodityViaAction(c4, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity4", commodity4);

		// 创建组合商品6(2+3)
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(commodity2.getID());
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(commodity3.getID());
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm2);
		ListCommodity.add(comm3);
		//
		Commodity c6 = BaseCommodityTest.DataInput.getCommodity();
		c6.setShelfLife(0);
		c6.setPurchaseFlag(0);
		c6.setRuleOfPoint(0);
		c6.setListSlave1(ListCommodity);
		c6.setType(EnumCommodityType.ECT_Combination.getIndex());
		c6.setName(CombinationCommodity + getCommodityOrder());
		c6.setMultiPackagingInfo(getBarcode() + ";" + c6.getPackageUnitID() + ";" + c6.getRefCommodityMultiple() + ";" + c6.getPriceRetail() + ";" + c6.getPriceVIP() + ";" + c6.getPriceWholesale() + ";" + c6.getName() + ";");
		String json = JSONObject.fromObject(c6).toString();
		c6.setSubCommodityInfo(json);
		Commodity commodity6 = BaseCommodityTest.createCommodityViaAction(c6, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity6", commodity6);
	}

	@Test(dependsOnMethods = "createCommodityTest")
	public void retrieveCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询普通商品1、组合商品6、多包装商品5");

		// 查询普通商品1
		String CommodityName = "普通商品1";
		String CommodityShortName = "商品";
		String CommodityBarcode = "6821423302301";
		String CommodityID = String.valueOf(CommodityListMap.get("commodity1").getID());
		// 根据商品名字查询
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + CommodityName)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		//
		// 根据商品简称查询
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_shortName() + "=" + CommodityShortName)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		//
		// 根据条形码查询
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_barcodes() + "=" + CommodityBarcode)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		//
		// 根据ID查询
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + CommodityID)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
		//
		// 查询多包装商品5
		CommodityName = "多包装商品5";
		CommodityShortName = "商品";
		CommodityBarcode = "6821423302305";
		CommodityID = "5";
		// 根据商品名字查询
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + CommodityName)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		//
		// 根据商品简称查询
		MvcResult mr6 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_shortName() + "=" + CommodityShortName)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		//
		// 根据条形码查询
		MvcResult mr7 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_barcodes() + "=" + CommodityBarcode)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr7);
		//
		// 根据ID查询
		MvcResult mr8 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + CommodityID)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr8);
		//
		// 查询组合商品6
		CommodityName = "组合商品6";
		CommodityShortName = "商品";
		CommodityBarcode = "6821423302306";
		CommodityID = String.valueOf(CommodityListMap.get("commodity6").getID());
		;
		// 根据商品名字查询
		MvcResult mr9 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + CommodityName)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr9);
		//
		// 根据商品简称查询
		MvcResult mr10 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_shortName() + "=" + CommodityShortName)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr10);
		//
		// 根据条形码查询
		MvcResult mr11 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_barcodes() + "=" + CommodityBarcode)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr11);
		//
		// 根据ID查询
		MvcResult mr12 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + CommodityID)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr12);
	}

	@Test(dependsOnMethods = "retrieveCommodity")
	public void updateCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改普通商品1、组合商品6、多包装商品5");

		// 修改普通商品1
		Commodity commodity1 = (Commodity) CommodityListMap.get("commodity1");
		Commodity c1 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c1.setID(commodity1.getID());
		c1.setName("修改普通商品1");
		Commodity commodity1Update = updateCommodity(commodity1, c1);
		CommodityListMap.replace("commodity1", commodity1Update);
		// 修改多包装商品5
		Commodity commodity4 = (Commodity) CommodityListMap.get("commodity4");
		Commodity c4 = BaseCommodityTest.DataInput.getCommodity();
		c4.setName("普通商品4");
		c4.setID(commodity4.getID());
		//
		Commodity c5 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c5.setRefCommodityMultiple(2);
		c5.setPackageUnitID(3);
		c5.setName("修改多包装商品5");
		//
		c4.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c4.getPackageUnitID() + "," + c5.getPackageUnitID() + ";" + c4.getRefCommodityMultiple() + "," + c5.getRefCommodityMultiple() + ";" + c4.getPriceRetail() + ","
				+ c5.getPriceRetail() + ";" + c4.getPriceVIP() + "," + c5.getPriceVIP() + ";" + c4.getPriceWholesale() + "," + c5.getPriceWholesale() + ";" + c4.getName() + "," + c5.getName() + ";");
		//
		Commodity commodity4Update = updateCommodity(commodity4, c4);
		CommodityListMap.replace("commodity4", commodity4Update);
		//
		// ...修改组合商品6(暂未实现)

	}

	@Test(dependsOnMethods = "updateCommodity")
	public void cancelUpdateCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改普通商品1、组合商品6、多包装商品5时取消修改");

	}

	@Test(dependsOnMethods = "cancelUpdateCommodity")
	public void createCommodityAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建商品7/8/9/10/11（普通7/8/9、组合11(7+8+2)、多包装10(ref=9)）后删除之");

		// 创建普通商品7
		Commodity c7 = BaseCommodityTest.DataInput.getCommodity();
		c7.setName(CommonCommodity + getCommodityOrder());
		c7.setMultiPackagingInfo(getBarcode() + ";" + c7.getPackageUnitID() + ";" + c7.getRefCommodityMultiple() + ";" + c7.getPriceRetail() + ";" + c7.getPriceVIP() + ";" + c7.getPriceWholesale() + ";" + c7.getName() + ";");
		Commodity commodity7 = BaseCommodityTest.createCommodityViaAction(c7, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建普通商品8
		Commodity c8 = BaseCommodityTest.DataInput.getCommodity();
		c8.setName(CommonCommodity + getCommodityOrder());
		c8.setMultiPackagingInfo(getBarcode() + ";" + c8.getPackageUnitID() + ";" + c8.getRefCommodityMultiple() + ";" + c8.getPriceRetail() + ";" + c8.getPriceVIP() + ";" + c8.getPriceWholesale() + ";" + c8.getName() + ";");
		Commodity commodity8 = BaseCommodityTest.createCommodityViaAction(c8, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建普通商品9和多包装商品10(ref=9)
		Commodity c9 = BaseCommodityTest.DataInput.getCommodity();
		c9.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c10 = BaseCommodityTest.DataInput.getCommodity();
		c10.setRefCommodityMultiple(2);
		c10.setPackageUnitID(2);
		c10.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c9.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c9.getPackageUnitID() + "," + c10.getPackageUnitID() + ";" + c9.getRefCommodityMultiple() + "," + c10.getRefCommodityMultiple() + ";" + c9.getPriceRetail() + ","
				+ c10.getPriceRetail() + ";" + c9.getPriceVIP() + "," + c10.getPriceVIP() + ";" + c9.getPriceWholesale() + "," + c10.getPriceWholesale() + ";" + c9.getName() + "," + c10.getName() + ";");
		//
		Commodity commodity9 = BaseCommodityTest.createCommodityViaAction(c9, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建组合商品11(7+8+2)
		SubCommodity comm7 = new SubCommodity();
		comm7.setSubCommodityID(commodity7.getID());
		comm7.setSubCommodityNO(1);
		comm7.setPrice(20);
		//
		SubCommodity comm8 = new SubCommodity();
		comm8.setSubCommodityID(commodity8.getID());
		comm8.setSubCommodityNO(1);
		comm8.setPrice(20);
		//
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(CommodityListMap.get("commodity2").getID());
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm7);
		ListCommodity.add(comm8);
		ListCommodity.add(comm2);
		//
		Commodity c11 = BaseCommodityTest.DataInput.getCommodity();
		c11.setShelfLife(0);
		c11.setPurchaseFlag(0);
		c11.setRuleOfPoint(0);
		c11.setListSlave1(ListCommodity);
		c11.setType(EnumCommodityType.ECT_Combination.getIndex());
		c11.setName(CombinationCommodity + getCommodityOrder());
		c11.setMultiPackagingInfo(getBarcode() + ";" + c11.getPackageUnitID() + ";" + c11.getRefCommodityMultiple() + ";" + c11.getPriceRetail() + ";" + c11.getPriceVIP() + ";" + c11.getPriceWholesale() + ";" + c11.getName() + ";");
		String json = JSONObject.fromObject(c11).toString();
		MvcResult mr11 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c11, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr11);
		CommodityCP.verifyCreate(mr11, c11, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, dbNameOfNewCompany1);
		// 把ID和type拿出来
		JSONObject o = JSONObject.fromObject(mr11.getResponse().getContentAsString());
		Commodity commodity11 = (Commodity) c11.parse1(o.getString(BaseAction.KEY_Object));
		//
		deleteCommodity(commodity11, dbNameOfNewCompany1);
		// 删除多包装商品10
		// 先把多包装查出来转换成对象
		MvcResult mr12 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity9.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr12);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		List<?> MultiPackagingCommodityList = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity10 = new Commodity();
		commodity10 = (Commodity) commodity10.parse1(MultiPackagingCommodityList.get(0).toString());
		//
		deleteCommodity(commodity10, dbNameOfNewCompany1);
		deleteCommodity(commodity9, dbNameOfNewCompany1);
		deleteCommodity(commodity8, dbNameOfNewCompany1);
		deleteCommodity(commodity7, dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCommodityAndDelete")
	public void createCommodityWithInitialValueAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建有期初值的商品12/13/14（普通商品12/13、多包装14(ref=13)）后删除之");

		// 创建普通商品12
		Commodity c12 = BaseCommodityTest.DataInput.getCommodity();
		c12.setName(CommonCommodity + getCommodityOrder());
		c12.setnOStart(10);
		c12.setPurchasingPriceStart(8);
		c12.setMultiPackagingInfo(getBarcode() + ";" + c12.getPackageUnitID() + ";" + c12.getRefCommodityMultiple() + ";" + c12.getPriceRetail() + ";" + c12.getPriceVIP() + ";" + c12.getPriceWholesale() + ";" + c12.getName() + ";");

		Commodity commodity12 = BaseCommodityTest.createCommodityViaAction(c12, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity12", commodity12);
		//
		// 创建普通商品13和多包装商品14(ref=13)
		Commodity c13 = BaseCommodityTest.DataInput.getCommodity();
		c13.setName(CommonCommodity + getCommodityOrder());
		c13.setnOStart(10);
		c13.setPurchasingPriceStart(8);
		//
		Commodity c14 = BaseCommodityTest.DataInput.getCommodity();
		c14.setRefCommodityMultiple(2);
		c14.setPackageUnitID(2);
		c14.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c13.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c13.getPackageUnitID() + "," + c14.getPackageUnitID() + ";" + c13.getRefCommodityMultiple() + "," + c14.getRefCommodityMultiple() + ";" + c13.getPriceRetail() + ","
				+ c14.getPriceRetail() + ";" + c13.getPriceVIP() + "," + c14.getPriceVIP() + ";" + c13.getPriceWholesale() + "," + c14.getPriceWholesale() + ";" + c13.getName() + "," + c14.getName() + ";");
		//

		Commodity commodity13 = BaseCommodityTest.createCommodityViaAction(c13, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity13", commodity13);
		//
		// 删除普通商品12（删除失败）
		MvcResult mr14 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commodity12.getID() + "&type=" + commodity12.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr14, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// 删除多包装商品14（删除成功）
		// 先把多包装查出来转换成对象
		MvcResult mr15 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity13.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr15);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr15.getResponse().getContentAsString());
		List<?> MultiPackagingCommodityList = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity14 = new Commodity();
		commodity14 = (Commodity) commodity14.parse1(MultiPackagingCommodityList.get(0).toString());
		// 删除
		deleteCommodity(commodity14, dbNameOfNewCompany1);
		//
		// 删除普通商品13（删除失败）
		MvcResult mr16 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity13.getID() + "&" + Commodity.field.getFIELD_NAME_type() + "=" + commodity13.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test(dependsOnMethods = "createCommodityWithInitialValueAndDelete")
	public void createCommodityThenUpdateAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个商品15/16/17（普通15/16、多包装17(ref=16)）后修改其名称，再搜索之");

		// 创建普通商品15
		Commodity c15 = BaseCommodityTest.DataInput.getCommodity();
		c15.setName(CommonCommodity + getCommodityOrder());
		c15.setMultiPackagingInfo(getBarcode() + ";" + c15.getPackageUnitID() + ";" + c15.getRefCommodityMultiple() + ";" + c15.getPriceRetail() + ";" + c15.getPriceVIP() + ";" + c15.getPriceWholesale() + ";" + c15.getName() + ";");
		// 创建商品（方法里已有结果验证）
		Commodity commodity15 = BaseCommodityTest.createCommodityViaAction(c15, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建商品16和多包装17(ref=16)
		Commodity c16 = BaseCommodityTest.DataInput.getCommodity();
		c16.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c17 = BaseCommodityTest.DataInput.getCommodity();
		c17.setRefCommodityMultiple(2);
		c17.setPackageUnitID(2);
		c17.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c16.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c16.getPackageUnitID() + "," + c17.getPackageUnitID() + ";" + c16.getRefCommodityMultiple() + "," + c17.getRefCommodityMultiple() + ";" + c16.getPriceRetail() + ","
				+ c17.getPriceRetail() + ";" + c16.getPriceVIP() + "," + c17.getPriceVIP() + ";" + c16.getPriceWholesale() + "," + c17.getPriceWholesale() + ";" + c16.getName() + "," + c17.getName() + ";");
		// 创建商品（已有结果验证）
		Commodity commodity16 = BaseCommodityTest.createCommodityViaAction(c16, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 修改普通商品15名称
		Commodity c15Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c15Update.setID(commodity15.getID());
		c15Update.setName("修改商品15");
		// 修改商品方法（里面已有结果验证）
		Commodity commodity15Update = updateCommodity(commodity15, c15Update);
		//
		CommodityListMap.put("commodity15", commodity15Update);
		//
		// 修改商品16和多包装17的名称
		Commodity c16Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c16Update.setName("修改商品16");
		c16Update.setID(commodity16.getID());
		//
		Commodity c17Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c17Update.setRefCommodityMultiple(2);
		c17Update.setPackageUnitID(3);
		c17Update.setName("修改多包装17");
		//
		c16Update.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c16Update.getPackageUnitID() + "," + c17Update.getPackageUnitID() + ";" + c16Update.getRefCommodityMultiple() + "," + c17Update.getRefCommodityMultiple()
				+ ";" + c16Update.getPriceRetail() + "," + c17Update.getPriceRetail() + ";" + c16Update.getPriceVIP() + "," + c17Update.getPriceVIP() + ";" + c16Update.getPriceWholesale() + "," + c17Update.getPriceWholesale() + ";"
				+ c16Update.getName() + "," + c17Update.getName() + ";");
		//
		// 修改商品（方法里已有结果验证）
		Commodity commodity16Update = updateCommodity(commodity16, c16Update);
		CommodityListMap.put("commodity16", commodity16Update);
		//
		// 根据原名称搜索普通商品15（查询为空但返回的错误码是0）
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c15.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		//
		// 根据原名称搜索商品16（查询为空但返回的错误码是0）
		MvcResult mr6 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c16.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		//
		// 根据修改后名称搜索普通商品15（成功）
		MvcResult mr7 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c15Update.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr7);
		//
		// 根据修改后商品搜索商品16（成功）
		MvcResult mr8 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c16Update.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr8);
	}

	@Test(dependsOnMethods = "createCommodityThenUpdateAndRetrieve")
	public void createCommodityThenUpdateAndCreateAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个商品18/19（普通18、多包装19(ref=18)）后修改该商品名称，再创建一个原名称的商品20/21");

		// 创建商品18和多包装19
		Commodity c18 = BaseCommodityTest.DataInput.getCommodity();
		c18.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c19 = BaseCommodityTest.DataInput.getCommodity();
		c19.setRefCommodityMultiple(2);
		c19.setPackageUnitID(2);
		c19.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c18.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c18.getPackageUnitID() + "," + c19.getPackageUnitID() + ";" + c18.getRefCommodityMultiple() + "," + c19.getRefCommodityMultiple() + ";" + c18.getPriceRetail() + ","
				+ c19.getPriceRetail() + ";" + c18.getPriceVIP() + "," + c19.getPriceVIP() + ";" + c18.getPriceWholesale() + "," + c19.getPriceWholesale() + ";" + c18.getName() + "," + c19.getName() + ";");
		// 创建
		Commodity commodity18 = BaseCommodityTest.createCommodityViaAction(c18, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 修改商品18和多包装19的商品名称
		Commodity c18Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c18Update.setName(CommonCommodity + getCommodityOrder());
		c18Update.setID(commodity18.getID());
		//
		Commodity c19Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c19Update.setRefCommodityMultiple(2);
		c19Update.setPackageUnitID(3);
		c19Update.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c18Update.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c18Update.getPackageUnitID() + "," + c19Update.getPackageUnitID() + ";" + c18Update.getRefCommodityMultiple() + "," + c19Update.getRefCommodityMultiple()
				+ ";" + c18Update.getPriceRetail() + "," + c19Update.getPriceRetail() + ";" + c18Update.getPriceVIP() + "," + c19Update.getPriceVIP() + ";" + c18Update.getPriceWholesale() + "," + c19Update.getPriceWholesale() + ";"
				+ c18Update.getName() + "," + c19Update.getName() + ";");
		//
		// 因为commodity18的名字改成普通商品20了，所以直接用commodity20来接
		Commodity commodity20 = updateCommodity(commodity18, c18Update);
		CommodityListMap.put("commodity20", commodity20);
		//
		// 创建原名称的商品和多包装
		Commodity c20 = BaseCommodityTest.DataInput.getCommodity();
		c20.setName(c18.getName());
		//
		Commodity c21 = BaseCommodityTest.DataInput.getCommodity();
		c21.setRefCommodityMultiple(2);
		c21.setPackageUnitID(2);
		c21.setName(c19.getName());
		//
		c20.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c20.getPackageUnitID() + "," + c21.getPackageUnitID() + ";" + c20.getRefCommodityMultiple() + "," + c21.getRefCommodityMultiple() + ";" + c20.getPriceRetail() + ","
				+ c21.getPriceRetail() + ";" + c20.getPriceVIP() + "," + c21.getPriceVIP() + ";" + c20.getPriceWholesale() + "," + c21.getPriceWholesale() + ";" + c20.getName() + "," + c21.getName() + ";");
		//
		commodity18 = BaseCommodityTest.createCommodityViaAction(c20, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity18", commodity18);
	}

	@Test(dependsOnMethods = "createCommodityThenUpdateAndCreateAgain")
	public void createCommodityThenUpdateAndRetrieveByFieldName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个商品22/23（普通22、多包装23(ref=22)）后修改该商品名称/简称/条形码，再根据名称/简称/条形码搜索该商品");

		// 创建商品22和多包装23
		Commodity c22 = BaseCommodityTest.DataInput.getCommodity();
		c22.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c23 = BaseCommodityTest.DataInput.getCommodity();
		c23.setRefCommodityMultiple(2);
		c23.setPackageUnitID(2);
		c23.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c22.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c22.getPackageUnitID() + "," + c23.getPackageUnitID() + ";" + c22.getRefCommodityMultiple() + "," + c23.getRefCommodityMultiple() + ";" + c22.getPriceRetail() + ","
				+ c23.getPriceRetail() + ";" + c22.getPriceVIP() + "," + c23.getPriceVIP() + ";" + c22.getPriceWholesale() + "," + c23.getPriceWholesale() + ";" + c22.getName() + "," + c23.getName() + ";");
		// 创建
		Commodity commodity22 = BaseCommodityTest.createCommodityViaAction(c22, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 修改商品22和多包装23
		Commodity c22Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c22Update.setName("修改商品22");
		c22Update.setID(commodity22.getID());
		//
		Commodity c23Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c23Update.setRefCommodityMultiple(2);
		c23Update.setPackageUnitID(3);
		c23Update.setName("修改多包装23");
		//
		c22Update.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c22Update.getPackageUnitID() + "," + c23Update.getPackageUnitID() + ";" + c22Update.getRefCommodityMultiple() + "," + c23Update.getRefCommodityMultiple()
				+ ";" + c22Update.getPriceRetail() + "," + c23Update.getPriceRetail() + ";" + c22Update.getPriceVIP() + "," + c23Update.getPriceVIP() + ";" + c22Update.getPriceWholesale() + "," + c23Update.getPriceWholesale() + ";"
				+ c22Update.getName() + "," + c23Update.getName() + ";");
		// 修改
		Commodity commodity22Update = updateCommodity(commodity22, c22Update);
		CommodityListMap.put("commodity22", commodity22Update);
		// 修改商品22条形码
		// 1.先根据商品22的ID查询拿到商品22的条形码ID
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity22Update.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> BarcodeIDs = JsonPath.read(jsonObject, "$.objectList[*].listBarcodes[*].ID");
		int comm22BarcodeID = 0;
		try {
			comm22BarcodeID = Integer.parseInt(BarcodeIDs.get(0).toString());
		} catch (NumberFormatException e) {
			Assert.assertTrue(false, e.getMessage());
		}
		// 2.修改商品22的条形码
		String comm22Barcode = "15119114463";
		MvcResult mr4 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(comm22BarcodeID))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity22.getID()))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), comm22Barcode)//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		//
		// 根据名称/简称/条形码搜索该商品
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c22Update.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		//
		MvcResult mr6 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_shortName() + "=" + c22Update.getShortName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		//
		MvcResult mr7 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_barcodes() + "=" + comm22Barcode)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr7);
	}

	@Test(dependsOnMethods = "createCommodityThenUpdateAndRetrieveByFieldName")
	public void createCommodityThenUpdateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建商品24/25/26/27（普通24/25、多包装26(ref=25)、组合商品27(24+3)）后修改并删除该商品");

		// 创建普通商品24
		Commodity c24 = BaseCommodityTest.DataInput.getCommodity();
		c24.setName(CommonCommodity + getCommodityOrder());
		c24.setMultiPackagingInfo(getBarcode() + ";" + c24.getPackageUnitID() + ";" + c24.getRefCommodityMultiple() + ";" + c24.getPriceRetail() + ";" + c24.getPriceVIP() + ";" + c24.getPriceWholesale() + ";" + c24.getName() + ";");
		Commodity commodity24 = BaseCommodityTest.createCommodityViaAction(c24, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建商品25和多包装26
		Commodity c25 = BaseCommodityTest.DataInput.getCommodity();
		c25.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c26 = BaseCommodityTest.DataInput.getCommodity();
		c26.setRefCommodityMultiple(2);
		c26.setPackageUnitID(2);
		c26.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c25.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c25.getPackageUnitID() + "," + c26.getPackageUnitID() + ";" + c25.getRefCommodityMultiple() + "," + c26.getRefCommodityMultiple() + ";" + c25.getPriceRetail() + ","
				+ c26.getPriceRetail() + ";" + c25.getPriceVIP() + "," + c26.getPriceVIP() + ";" + c25.getPriceWholesale() + "," + c26.getPriceWholesale() + ";" + c25.getName() + "," + c26.getName() + ";");
		//
		Commodity commodity25 = BaseCommodityTest.createCommodityViaAction(c25, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建组合商品27(24+3)
		SubCommodity comm24 = new SubCommodity();
		comm24.setSubCommodityID(commodity24.getID());
		comm24.setSubCommodityNO(1);
		comm24.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(CommodityListMap.get("commodity3").getID());
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm24);
		ListCommodity.add(comm3);
		//
		Commodity c27 = BaseCommodityTest.DataInput.getCommodity();
		c27.setShelfLife(0);
		c27.setPurchaseFlag(0);
		c27.setRuleOfPoint(0);
		c27.setListSlave1(ListCommodity);
		c27.setType(EnumCommodityType.ECT_Combination.getIndex());
		c27.setName(CombinationCommodity + getCommodityOrder());
		c27.setMultiPackagingInfo(getBarcode() + ";" + c27.getPackageUnitID() + ";" + c27.getRefCommodityMultiple() + ";" + c27.getPriceRetail() + ";" + c27.getPriceVIP() + ";" + c27.getPriceWholesale() + ";" + c27.getName() + ";");
		String json = JSONObject.fromObject(c27).toString();
		MvcResult mr3 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c27, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		CommodityCP.verifyCreate(mr3, c27, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Commodity commodity27 = (Commodity) c27.parse1(jsonObject.getString(BaseAction.KEY_Object));
		//
		// 修改普通商品24
		Commodity c24Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c24Update.setID(commodity24.getID());
		c24Update.setName("修改商品24");
		//
		Commodity commodity24Update = updateCommodity(commodity24, c24Update);
		//
		// 修改商品25和多包装26
		Commodity c25Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c25Update.setName("修改商品25");
		c25Update.setID(commodity25.getID());
		//
		Commodity c26Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c26Update.setRefCommodityMultiple(2);
		c26Update.setPackageUnitID(3);
		c26Update.setName("修改多包装26");
		//
		c25Update.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c25Update.getPackageUnitID() + "," + c26Update.getPackageUnitID() + ";" + c25Update.getRefCommodityMultiple() + "," + c26Update.getRefCommodityMultiple()
				+ ";" + c25Update.getPriceRetail() + "," + c26Update.getPriceRetail() + ";" + c25Update.getPriceVIP() + "," + c26Update.getPriceVIP() + ";" + c25Update.getPriceWholesale() + "," + c26Update.getPriceWholesale() + ";"
				+ c25Update.getName() + "," + c26Update.getName() + ";");
		//
		Commodity commodity25Update = updateCommodity(commodity25, c25Update);
		//
		// ...修改组合商品27（修改组合商品功能暂未实现）

		//
		// 删除普通商品24（有组合商品依赖，删除失败）
		MvcResult mr6 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commodity24Update.getID() + "&type=" + commodity24Update.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// 删除商品25（有多包装依赖，删除失败）
		MvcResult mr7 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commodity25Update.getID() + "&type=" + commodity25Update.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// 删除多包装26
		// 1.先查出多包装26的ID
		MvcResult mr8 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c26Update.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr8);
		// 把ID拿出来
		jsonObject = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity26 = new Commodity();
		commodity26 = (Commodity) commodity26.parse1(listMultiPackageCommodity.get(0).toString());

		// 删除
		deleteCommodity(commodity26, dbNameOfNewCompany1);
		deleteCommodity(commodity27, dbNameOfNewCompany1);
		deleteCommodity(commodity24Update, dbNameOfNewCompany1);
		deleteCommodity(commodity25Update, dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCommodityThenUpdateAndDelete")
	public void createDuplicatedCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个商品28/29/30/31（普通28/29、多包装30(ref=29)、组合31(28+4)）后，再创建一个同名的商品");

		// 创建普通商品28
		Commodity c28 = BaseCommodityTest.DataInput.getCommodity();
		c28.setName(CommonCommodity + getCommodityOrder());
		c28.setMultiPackagingInfo(getBarcode() + ";" + c28.getPackageUnitID() + ";" + c28.getRefCommodityMultiple() + ";" + c28.getPriceRetail() + ";" + c28.getPriceVIP() + ";" + c28.getPriceWholesale() + ";" + c28.getName() + ";");
		Commodity commodity28 = BaseCommodityTest.createCommodityViaAction((Commodity) c28.clone(), mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity28", commodity28.clone());
		//
		// 创建商品29和多包装30(ref=29)
		Commodity c29 = BaseCommodityTest.DataInput.getCommodity();
		c29.setName(CommonCommodity + getCommodityOrder());
		//
		Commodity c30 = BaseCommodityTest.DataInput.getCommodity();
		c30.setRefCommodityMultiple(2);
		c30.setPackageUnitID(2);
		c30.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c29.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c29.getPackageUnitID() + "," + c30.getPackageUnitID() + ";" + c29.getRefCommodityMultiple() + "," + c30.getRefCommodityMultiple() + ";" + c29.getPriceRetail() + ","
				+ c30.getPriceRetail() + ";" + c29.getPriceVIP() + "," + c30.getPriceVIP() + ";" + c29.getPriceWholesale() + "," + c30.getPriceWholesale() + ";" + c29.getName() + "," + c30.getName() + ";");
		//
		Commodity commodity29 = BaseCommodityTest.createCommodityViaAction((Commodity) c29.clone(), mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity29", commodity29.clone());
		//
		// 创建组合商品31(28+4)
		SubCommodity comm28 = new SubCommodity();
		comm28.setSubCommodityID(CommodityListMap.get("commodity28").getID());
		comm28.setSubCommodityNO(1);
		comm28.setPrice(20);
		//
		SubCommodity comm4 = new SubCommodity();
		comm4.setSubCommodityID(CommodityListMap.get("commodity4").getID());
		comm4.setSubCommodityNO(1);
		comm4.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm28);
		ListCommodity.add(comm4);
		//
		Commodity c31 = BaseCommodityTest.DataInput.getCommodity();
		c31.setShelfLife(0);
		c31.setPurchaseFlag(0);
		c31.setRuleOfPoint(0);
		c31.setListSlave1(ListCommodity);
		c31.setType(EnumCommodityType.ECT_Combination.getIndex());
		c31.setName(CombinationCommodity + getCommodityOrder());
		c31.setMultiPackagingInfo(getBarcode() + ";" + c31.getPackageUnitID() + ";" + c31.getRefCommodityMultiple() + ";" + c31.getPriceRetail() + ";" + c31.getPriceVIP() + ";" + c31.getPriceWholesale() + ";" + c31.getName() + ";");
		String json = JSONObject.fromObject(c31).toString();
		MvcResult mr3 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c31, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		CommodityCP.verifyCreate(mr3, c31, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Commodity c = new Commodity();
		Commodity commodity31 = (Commodity) c.parse1(jsonObject.getString("object"));
		CommodityListMap.put("commodity31", commodity31.clone());
		//
		// 重复创建普通商品28
		MvcResult mr4 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c28, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_Duplicated);
		//
		// 重复创建商品29和多包装30(ref=29)
		MvcResult mr5 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c29, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_Duplicated);
		//
		// 重复创建组合商品
		MvcResult mr6 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c31, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_Duplicated);
	}

	@Test(dependsOnMethods = "createDuplicatedCommodity")
	public void createCombinationCommodityThenDeleteSubCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个期初值=0的组合商品32(1+2)，再删除该组合商品的子商品");

		// 创建期初值为0的组合商品32(1+2)
		SubCommodity comm1 = new SubCommodity();
		comm1.setSubCommodityID(CommodityListMap.get("commodity1").getID());
		comm1.setSubCommodityNO(1);
		comm1.setPrice(20);
		//
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(CommodityListMap.get("commodity2").getID());
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm1);
		ListCommodity.add(comm2);
		//
		Commodity c32 = BaseCommodityTest.DataInput.getCommodity();
		c32.setShelfLife(0);
		c32.setPurchaseFlag(0);
		c32.setRuleOfPoint(0);
		c32.setListSlave1(ListCommodity);
		c32.setType(EnumCommodityType.ECT_Combination.getIndex());
		c32.setName(CombinationCommodity + getCommodityOrder());
		c32.setMultiPackagingInfo(getBarcode() + ";" + c32.getPackageUnitID() + ";" + c32.getRefCommodityMultiple() + ";" + c32.getPriceRetail() + ";" + c32.getPriceVIP() + ";" + c32.getPriceWholesale() + ";" + c32.getName() + ";");
		String json = JSONObject.fromObject(c32).toString();
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c32, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c32, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity32 = (Commodity) c32.parse1(jsonObject.getString("object"));
		CommodityListMap.put("commodity32", commodity32);
		//
		// 删除其子商品1(删除失败)
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + CommodityListMap.get("commodity1").getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// 删除其子商品2(删除失败)
		MvcResult mr3 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + CommodityListMap.get("commodity2").getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test(dependsOnMethods = "createCombinationCommodityThenDeleteSubCommodity")
	public void createMultiPackagingCommodityThenDeleteBasicCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个期初值=0的多包装商品33(ref=1)，再删除该多包装商品的基本商品");

		// 创建多包装33(ref=1)
		Commodity commodity1 = (Commodity) CommodityListMap.get("commodity1");
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("普通商品1");
		c1.setID(CommodityListMap.get("commodity1").getID());
		//
		Commodity c33 = BaseCommodityTest.DataInput.getCommodity();
		c33.setRefCommodityMultiple(2);
		c33.setPackageUnitID(2);
		c33.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c1.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c1.getPackageUnitID() + "," + c33.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + "," + c33.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ","
				+ c33.getPriceRetail() + ";" + c1.getPriceVIP() + "," + c33.getPriceVIP() + ";" + c1.getPriceWholesale() + "," + c33.getPriceWholesale() + ";" + c1.getName() + "," + c33.getName() + ";");
		Commodity commodity1Update = updateCommodity(commodity1, c1);
		CommodityListMap.replace("commodity1", commodity1Update);
		//
		// 删除其基本商品(删除失败)
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + CommodityListMap.get("commodity1").getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test(dependsOnMethods = "createMultiPackagingCommodityThenDeleteBasicCommodity")
	public void createCommonCommodityAndCombinationCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个普通商品34，再创建一个以该普通商品作为子商品的组合商品35(34+3)");

		// 创建普通商品34
		Commodity c34 = BaseCommodityTest.DataInput.getCommodity();
		c34.setName(CommonCommodity + getCommodityOrder());
		c34.setMultiPackagingInfo(getBarcode() + ";" + c34.getPackageUnitID() + ";" + c34.getRefCommodityMultiple() + ";" + c34.getPriceRetail() + ";" + c34.getPriceVIP() + ";" + c34.getPriceWholesale() + ";" + c34.getName() + ";");
		Commodity commodity34 = BaseCommodityTest.createCommodityViaAction(c34, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity34", commodity34);
		//
		// 创建组合商品35(34+3)
		SubCommodity comm34 = new SubCommodity();
		comm34.setSubCommodityID(commodity34.getID());
		comm34.setSubCommodityNO(1);
		comm34.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(CommodityListMap.get("commodity3").getID());
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm34);
		ListCommodity.add(comm3);
		//
		Commodity c35 = BaseCommodityTest.DataInput.getCommodity();
		c35.setShelfLife(0);
		c35.setPurchaseFlag(0);
		c35.setRuleOfPoint(0);
		c35.setListSlave1(ListCommodity);
		c35.setType(EnumCommodityType.ECT_Combination.getIndex());
		c35.setName(CombinationCommodity + getCommodityOrder());
		c35.setMultiPackagingInfo(getBarcode() + ";" + c35.getPackageUnitID() + ";" + c35.getRefCommodityMultiple() + ";" + c35.getPriceRetail() + ";" + c35.getPriceVIP() + ";" + c35.getPriceWholesale() + ";" + c35.getName() + ";");
		String json = JSONObject.fromObject(c35).toString();
		MvcResult mr2 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c35, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		CommodityCP.verifyCreate(mr2, c35, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Commodity commodity35 = (Commodity) c35.parse1(jsonObject.getString("object"));
		CommodityListMap.put("commodity35", commodity35);
	}

	@Test(dependsOnMethods = "createCommonCommodityAndCombinationCommodity")
	public void createCommonCommodityAndMultiPackagingCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个普通商品36，再创建一个以该普通商品作为基本商品的多包装商品37(ref=36)");

		// 创建普通商品36
		Commodity c36 = BaseCommodityTest.DataInput.getCommodity();
		c36.setName(CommonCommodity + getCommodityOrder());
		c36.setMultiPackagingInfo(getBarcode() + ";" + c36.getPackageUnitID() + ";" + c36.getRefCommodityMultiple() + ";" + c36.getPriceRetail() + ";" + c36.getPriceVIP() + ";" + c36.getPriceWholesale() + ";" + c36.getName() + ";");
		Commodity commodity36 = BaseCommodityTest.createCommodityViaAction((Commodity) c36.clone(), mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity36", commodity36);
		//
		// 创建多包装37(ref=36)
		Commodity c37 = BaseCommodityTest.DataInput.getCommodity();
		c37.setRefCommodityMultiple(2);
		c37.setPackageUnitID(2);
		c37.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c36.setID(commodity36.getID());
		c36.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c36.getPackageUnitID() + "," + c37.getPackageUnitID() + ";" + c36.getRefCommodityMultiple() + "," + c37.getRefCommodityMultiple() + ";" + c36.getPriceRetail() + ","
				+ c37.getPriceRetail() + ";" + c36.getPriceVIP() + "," + c37.getPriceVIP() + ";" + c36.getPriceWholesale() + "," + c37.getPriceWholesale() + ";" + c36.getName() + "," + c37.getName() + ";");
		Commodity commodity36Update = updateCommodity(commodity36, c36);
		CommodityListMap.replace("commodity36", commodity36Update);
	}

	@Test(dependsOnMethods = "createCommonCommodityAndMultiPackagingCommodity")
	public void createCommonCommodityAndCombinationCommodityThenUpdateSubcommodityAndRetireve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个普通商品38，再创建一个以该普通商品作为子商品的组合商品39(38+4)，再修改该子商品，再查看该组合商品");

		// 创建普通商品38
		Commodity c38 = BaseCommodityTest.DataInput.getCommodity();
		c38.setName(CommonCommodity + getCommodityOrder());
		c38.setMultiPackagingInfo(getBarcode() + ";" + c38.getPackageUnitID() + ";" + c38.getRefCommodityMultiple() + ";" + c38.getPriceRetail() + ";" + c38.getPriceVIP() + ";" + c38.getPriceWholesale() + ";" + c38.getName() + ";");
		Commodity commodity38 = BaseCommodityTest.createCommodityViaAction(c38, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 创建组合商品39(38+4)
		SubCommodity comm38 = new SubCommodity();
		comm38.setSubCommodityID(commodity38.getID());
		comm38.setSubCommodityNO(1);
		comm38.setPrice(20);
		//
		SubCommodity comm4 = new SubCommodity();
		comm4.setSubCommodityID(CommodityListMap.get("commodity4").getID());
		comm4.setSubCommodityNO(1);
		comm4.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm38);
		ListCommodity.add(comm4);
		//
		Commodity c39 = BaseCommodityTest.DataInput.getCommodity();
		c39.setShelfLife(0);
		c39.setPurchaseFlag(0);
		c39.setRuleOfPoint(0);
		c39.setListSlave1(ListCommodity);
		c39.setType(EnumCommodityType.ECT_Combination.getIndex());
		c39.setName(CombinationCommodity + getCommodityOrder());
		c39.setMultiPackagingInfo(getBarcode() + ";" + c39.getPackageUnitID() + ";" + c39.getRefCommodityMultiple() + ";" + c39.getPriceRetail() + ";" + c39.getPriceVIP() + ";" + c39.getPriceWholesale() + ";" + c39.getName() + ";");
		String json = JSONObject.fromObject(c39).toString();
		MvcResult mr2 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c39, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		CommodityCP.verifyCreate(mr2, c39, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Commodity commodity39 = (Commodity) c39.parse1(jsonObject.getString("object"));
		CommodityListMap.put("commodity39", commodity39);
		//
		// 修改其子商品38
		Commodity c38Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c38Update.setID(commodity38.getID());
		c38Update.setName("修改商品38");
		//
		Commodity commodity38Update = updateCommodity(commodity38, c38Update);
		CommodityListMap.put("commodity38", commodity38Update);
		//
		// 查看该组合商品
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity39.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
	}

	@Test(dependsOnMethods = "createCommonCommodityAndCombinationCommodityThenUpdateSubcommodityAndRetireve")
	public void createCommodityThenDeleteAndCreateAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个商品40/41/42/43（普通40/41、多包装42(ref=40)、组合43(40+41+1)）后，再删除该商品，删除成功后再创建一个同名的商品");

		// 创建普通商品40
		Commodity c40 = BaseCommodityTest.DataInput.getCommodity();
		c40.setName(CommonCommodity + getCommodityOrder());
		c40.setMultiPackagingInfo(getBarcode() + ";" + c40.getPackageUnitID() + ";" + c40.getRefCommodityMultiple() + ";" + c40.getPriceRetail() + ";" + c40.getPriceVIP() + ";" + c40.getPriceWholesale() + ";" + c40.getName() + ";");
		Commodity commodity40 = BaseCommodityTest.createCommodityViaAction((Commodity) c40.clone(), mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 删除普通商品40
		deleteCommodity(commodity40, dbNameOfNewCompany1);
		//
		// 重新创建商品40（创建成功）
		Commodity commodity40Duplicated = BaseCommodityTest.createCommodityViaAction((Commodity) c40.clone(), mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity40", commodity40Duplicated);
		//
		// 创建普通商品41
		Commodity c41 = BaseCommodityTest.DataInput.getCommodity();
		c41.setName(CommonCommodity + getCommodityOrder());
		c41.setMultiPackagingInfo(getBarcode() + ";" + c41.getPackageUnitID() + ";" + c41.getRefCommodityMultiple() + ";" + c41.getPriceRetail() + ";" + c41.getPriceVIP() + ";" + c41.getPriceWholesale() + ";" + c41.getName() + ";");
		Commodity commodity41 = BaseCommodityTest.createCommodityViaAction((Commodity) c41.clone(), mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 删除普通商品41
		deleteCommodity(commodity41, dbNameOfNewCompany1);
		//
		// 重新创建商品41（创建成功）
		Commodity commodity41Duplicated = BaseCommodityTest.createCommodityViaAction(c41, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity41", commodity41Duplicated);
		//
		// 创建多包装42(ref=40)
		Commodity c42 = BaseCommodityTest.DataInput.getCommodity();
		c42.setRefCommodityMultiple(2);
		c42.setPackageUnitID(2);
		c42.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c40.setID(commodity40Duplicated.getID());
		c40.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c40.getPackageUnitID() + "," + c42.getPackageUnitID() + ";" + c40.getRefCommodityMultiple() + "," + c42.getRefCommodityMultiple() + ";" + c40.getPriceRetail() + ","
				+ c42.getPriceRetail() + ";" + c40.getPriceVIP() + "," + c42.getPriceVIP() + ";" + c40.getPriceWholesale() + "," + c42.getPriceWholesale() + ";" + c40.getName() + "," + c42.getName() + ";");
		Commodity commodity40Update = updateCommodity(commodity40Duplicated, (Commodity) c40.clone());
		CommodityListMap.replace("commodity40", commodity40Update);
		//
		// 删除多包装42
		// 1.先查出多包装42的ID
		MvcResult mr8 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c42.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr8);
		// 把对象拿出来
		JSONObject o = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(o, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity42 = (Commodity) c42.parse1(listMultiPackageCommodity.get(0).toString());
		// 删除之
		deleteCommodity(commodity42, dbNameOfNewCompany1);
		//
		// 重新创建多包装42（创建成功）
		Commodity commodity40update2 = updateCommodity(commodity40Update, c40);
		CommodityListMap.replace("commodity40", commodity40update2);
		//
		// 创建组合商品43(40+41+1)
		SubCommodity comm1 = new SubCommodity();
		comm1.setSubCommodityID(CommodityListMap.get("commodity1").getID());
		comm1.setSubCommodityNO(1);
		comm1.setPrice(20);
		//
		SubCommodity comm40 = new SubCommodity();
		comm40.setSubCommodityID(commodity40.getID());
		comm40.setSubCommodityNO(1);
		comm40.setPrice(20);
		//
		SubCommodity comm41 = new SubCommodity();
		comm41.setSubCommodityID(commodity41.getID());
		comm41.setSubCommodityNO(1);
		comm41.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm1);
		ListCommodity.add(comm41);
		ListCommodity.add(comm40);
		//
		Commodity c43 = BaseCommodityTest.DataInput.getCommodity();
		c43.setShelfLife(0);
		c43.setPurchaseFlag(0);
		c43.setRuleOfPoint(0);
		c43.setListSlave1(ListCommodity);
		c43.setType(EnumCommodityType.ECT_Combination.getIndex());
		c43.setName(CombinationCommodity + getCommodityOrder());
		c43.setMultiPackagingInfo(getBarcode() + ";" + c43.getPackageUnitID() + ";" + c43.getRefCommodityMultiple() + ";" + c43.getPriceRetail() + ";" + c43.getPriceVIP() + ";" + c43.getPriceWholesale() + ";" + c43.getName() + ";");
		String json = JSONObject.fromObject(c43).toString();
		MvcResult mr10 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c43, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr10);
		CommodityCP.verifyCreate(mr10, c43, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, dbNameOfNewCompany1);
		o = JSONObject.fromObject(mr10.getResponse().getContentAsString());
		Commodity commodity43 = new Commodity();
		commodity43 = (Commodity) commodity43.parse1(o.getString(BaseAction.KEY_Object));
		// 删除组合商品43
		deleteCommodity(commodity43, dbNameOfNewCompany1);
		//
		// 重新创建组合商品43（创建成功）
		MvcResult mr12 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c43, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr12);
		o = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		Commodity commodity43Duplicated = (Commodity) c43.parse1(o.getString("object"));
		CommodityListMap.put("commodity43", commodity43Duplicated);
	}

	@Test(dependsOnMethods = "createCommodityThenDeleteAndCreateAgain")
	public void createCommonCommodityAndCombinationCommodityWithSameBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个子商品44和一个组合商品45(44+4)，且它们的条形码相同，再根据条形码进行搜索");

		// 创建普通商品44
		Commodity c44 = BaseCommodityTest.DataInput.getCommodity();
		c44.setName(CommonCommodity + getCommodityOrder());
		String c44Barcode = String.valueOf(getBarcode());
		c44.setMultiPackagingInfo(c44Barcode + ";" + c44.getPackageUnitID() + ";" + c44.getRefCommodityMultiple() + ";" + c44.getPriceRetail() + ";" + c44.getPriceVIP() + ";" + c44.getPriceWholesale() + ";" + c44.getName() + ";");
		Commodity commodity44 = BaseCommodityTest.createCommodityViaAction(c44, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("commodity44", commodity44);
		//
		// 创建组合商品45(44+4)
		SubCommodity comm4 = new SubCommodity();
		comm4.setSubCommodityID(CommodityListMap.get("commodity4").getID());
		comm4.setSubCommodityNO(1);
		comm4.setPrice(20);
		//
		SubCommodity comm44 = new SubCommodity();
		comm44.setSubCommodityID(commodity44.getID());
		comm44.setSubCommodityNO(1);
		comm44.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm4);
		ListCommodity.add(comm44);
		//
		Commodity c45 = BaseCommodityTest.DataInput.getCommodity();
		c45.setShelfLife(0);
		c45.setPurchaseFlag(0);
		c45.setRuleOfPoint(0);
		c45.setListSlave1(ListCommodity);
		c45.setType(EnumCommodityType.ECT_Combination.getIndex());
		c45.setName(CombinationCommodity + getCommodityOrder());
		c45.setMultiPackagingInfo(c44Barcode + ";" + c45.getPackageUnitID() + ";" + c45.getRefCommodityMultiple() + ";" + c45.getPriceRetail() + ";" + c45.getPriceVIP() + ";" + c45.getPriceWholesale() + ";" + c45.getName() + ";");
		String json = JSONObject.fromObject(c45).toString();
		MvcResult mr2 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c45, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		CommodityCP.verifyCreate(mr2, c45, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		//
		JSONObject o = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Commodity commodity45 = (Commodity) c45.parse1(o.getString("object"));
		CommodityListMap.put("commodity45", commodity45);
		//
		// 根据条形码搜索
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_barcodes() + "=" + c44Barcode)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
	}

	@Test(dependsOnMethods = "createCommonCommodityAndCombinationCommodityWithSameBarcode")
	public void retrieveCommodityAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询某个商品(40/42/43)的详情信息并修改");

		// 查询商品40和多包装42(ref=40)
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + CommodityListMap.get("commodity40").getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		//
		// 修改商品40和多包装42(ref=40)
		Commodity c40 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c40.setName("修改商品40");
		c40.setID(CommodityListMap.get("commodity40").getID());
		//
		Commodity c42 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c42.setName("修改多包装42");
		c42.setRefCommodityMultiple(3);
		c42.setPackageUnitID(3);
		//
		c40.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c40.getPackageUnitID() + "," + c42.getPackageUnitID() + ";" + c40.getRefCommodityMultiple() + "," + c42.getRefCommodityMultiple() + ";" + c40.getPriceRetail() + ","
				+ c42.getPriceRetail() + ";" + c40.getPriceVIP() + "," + c42.getPriceVIP() + ";" + c40.getPriceWholesale() + "," + c42.getPriceWholesale() + ";" + c40.getName() + "," + c42.getName() + ";");
		//
		Commodity commodity40 = BaseCommodityTest.createCommodityViaAction(c40, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.replace("commodity40", commodity40);
		//
		// 重新查询商品40和多包装42
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity40.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		//
		// 查询组合商品43
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + CommodityListMap.get("commodity43").getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
		//
		// ...修改组合商品43（暂未实现）

	}

	@Test(dependsOnMethods = "retrieveCommodityAndUpdate")
	public void retrieveCommodityAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询某个商品(40/42/43)的详情信息并删除");

		// 查询商品40和多包装42(ref=40)
		Commodity commodity40 = (Commodity) CommodityListMap.get("commodity40");
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity40.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		//
		// 查询商品多包装42的ID
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=多包装商品42")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity42 = new Commodity();
		commodity42 = (Commodity) commodity42.parse1(listMultiPackageCommodity.get(0).toString());
		//
		// 查询组合商品43
		Commodity commodity43 = (Commodity) CommodityListMap.get("commodity43");
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity43.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		//
		// 删除组合商品43
		deleteCommodity(commodity43, dbNameOfNewCompany1);
		//
		// 删除商品40和多包装42(ref=40)
		deleteCommodity(commodity42, dbNameOfNewCompany1);
		//
		deleteCommodity(commodity40, dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "retrieveCommodityAndDelete")
	public void retrieveCommodityThenUpdateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询某个商品(36/37/39)的详情信息并进行修改和删除");

		// 查询商品36和多包装37(ref=36)
		Commodity commodity36 = (Commodity) CommodityListMap.get("commodity36");
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity36.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		//
		// 修改商品36和多包装37(ref=36)
		Commodity c36 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c36.setName("修改商品36");
		c36.setID(commodity36.getID());
		//
		Commodity c37 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c37.setName("修改多包装37");
		c37.setRefCommodityMultiple(3);
		c37.setPackageUnitID(3);
		//
		c36.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c36.getPackageUnitID() + "," + c37.getPackageUnitID() + ";" + c36.getRefCommodityMultiple() + "," + c37.getRefCommodityMultiple() + ";" + c36.getPriceRetail() + ","
				+ c37.getPriceRetail() + ";" + c36.getPriceVIP() + "," + c37.getPriceVIP() + ";" + c36.getPriceWholesale() + "," + c37.getPriceWholesale() + ";" + c36.getName() + "," + c37.getName() + ";");
		// 修改
		Commodity commodity36Update = updateCommodity(commodity36, c36);
		//
		// 查询商品多包装37的ID
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c37.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity37 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < listMultiPackageCommodity.size(); i++) {
			commodity37 = (Commodity) commodity37.parse1(listMultiPackageCommodity.get(i).toString());
			if ((commodity37.getName()).equals(c37.getName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		//
		// 查询组合商品39
		Commodity commodity39 = (Commodity) CommodityListMap.get("commodity39");
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity39.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
		//
		// ...修改组合商品39（暂未实现）

		// 删除组合商品39
		deleteCommodity(commodity39, dbNameOfNewCompany1);
		//
		// 删除多包装37和商品36
		deleteCommodity(commodity37, dbNameOfNewCompany1);
		//
		deleteCommodity(commodity36Update, dbNameOfNewCompany1);

	}

	@Test(dependsOnMethods = "retrieveCommodityThenUpdateAndDelete")
	public void retrieveCommodityAndUpdateThenCancel() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询某个商品的详情进行修改时取消修改");

		// 查询商品44
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + CommodityListMap.get("commodity44").getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
	}

	@Test(dependsOnMethods = "retrieveCommodityAndUpdateThenCancel")
	public void retrieveCommodityThenDeleteAndCreateAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询某个商品的详情进行删除，在创建一个同名的商品（普通29、多包装30、组合31）");

		// 查询普通商品29和多包装30(ref=29)
		Commodity commodity29 = (Commodity) CommodityListMap.get("commodity29");
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity29.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 查出多包装30的ID
		//
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity30 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < listMultiPackageCommodity.size(); i++) {
			commodity30 = (Commodity) commodity30.parse1(listMultiPackageCommodity.get(0).toString());
			if ("多包装商品30".equals(commodity30.getName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		//
		// 删除多包装30和商品29
		deleteCommodity(commodity30, dbNameOfNewCompany1);
		//
		deleteCommodity(commodity29, dbNameOfNewCompany1);
		//
		// 重新创建商品29和多包装30
		Commodity c29 = BaseCommodityTest.DataInput.getCommodity();
		c29.setName(commodity29.getName());
		//
		Commodity c30 = BaseCommodityTest.DataInput.getCommodity();
		c30.setRefCommodityMultiple(2);
		c30.setPackageUnitID(2);
		c30.setName(commodity30.getName());
		//
		c29.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c29.getPackageUnitID() + "," + c30.getPackageUnitID() + ";" + c29.getRefCommodityMultiple() + "," + c30.getRefCommodityMultiple() + ";" + c29.getPriceRetail() + ","
				+ c30.getPriceRetail() + ";" + c29.getPriceVIP() + "," + c30.getPriceVIP() + ";" + c29.getPriceWholesale() + "," + c30.getPriceWholesale() + ";" + c29.getName() + "," + c30.getName() + ";");
		//
		Commodity commodity29Update = BaseCommodityTest.createCommodityViaAction(c29, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.replace("commodity29", commodity29Update);
		//
		// 查询组合商品31(28+4)
		Commodity commodity31 = (Commodity) CommodityListMap.get("commodity31");
		MvcResult mr6 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity31.getName() + "&" + Commodity.field.getFIELD_NAME_type() + "=" + commodity31.getType())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		//
		// 删除组合商品31
		deleteCommodity(commodity31, dbNameOfNewCompany1);
		//
		// 重新创建组合商品31
		SubCommodity comm28 = new SubCommodity();
		comm28.setSubCommodityID(CommodityListMap.get("commodity28").getID());
		comm28.setSubCommodityNO(1);
		comm28.setPrice(20);
		//
		SubCommodity comm4 = new SubCommodity();
		comm4.setSubCommodityID(CommodityListMap.get("commodity4").getID());
		comm4.setSubCommodityNO(1);
		comm4.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm28);
		ListCommodity.add(comm4);
		//
		Commodity c31 = BaseCommodityTest.DataInput.getCommodity();
		c31.setShelfLife(0);
		c31.setPurchaseFlag(0);
		c31.setRuleOfPoint(0);
		c31.setListSlave1(ListCommodity);
		c31.setType(EnumCommodityType.ECT_Combination.getIndex());
		c31.setName(commodity31.getName());
		c31.setMultiPackagingInfo(getBarcode() + ";" + c31.getPackageUnitID() + ";" + c31.getRefCommodityMultiple() + ";" + c31.getPriceRetail() + ";" + c31.getPriceVIP() + ";" + c31.getPriceWholesale() + ";" + c31.getName() + ";");
		String json = JSONObject.fromObject(c31).toString();
		MvcResult mr8 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c31, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr8);
		CommodityCP.verifyCreate(mr8, c31, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		Commodity commodity31Update = (Commodity) c31.parse1(jsonObject.getString("object"));
		CommodityListMap.replace("commodity31", commodity31Update);
	}

	@Test(dependsOnMethods = "retrieveCommodityThenDeleteAndCreateAgain")
	public void createCommodityWithTheSameNameAfterUpdateCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个普通18/多包装商品19后再创建一个与原来名称相同的商品");

		// 修改商品18和多包装19(ref=18)
		Commodity commodity18 = (Commodity) CommodityListMap.get("commodity18");
		Commodity c18Update = BaseCommodityTest.DataInput.getCommodity();
		c18Update.setName("修改普通商品18");
		c18Update.setID(commodity18.getID());
		//
		Commodity c19Update = BaseCommodityTest.DataInput.getUpdateCommodity();
		c19Update.setRefCommodityMultiple(2);
		c19Update.setPackageUnitID(3);
		c19Update.setName("修改多包装商品19");
		//
		c18Update.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c18Update.getPackageUnitID() + "," + c19Update.getPackageUnitID() + ";" + c18Update.getRefCommodityMultiple() + "," + c19Update.getRefCommodityMultiple()
				+ ";" + c18Update.getPriceRetail() + "," + c19Update.getPriceRetail() + ";" + c18Update.getPriceVIP() + "," + c19Update.getPriceVIP() + ";" + c18Update.getPriceWholesale() + "," + c19Update.getPriceWholesale() + ";"
				+ c18Update.getName() + "," + c19Update.getName() + ";");
		//
		updateCommodity(commodity18, c18Update);
		//
		// 创建与原来名称相同的商品
		Commodity c18 = BaseCommodityTest.DataInput.getCommodity();
		c18.setName("普通商品18");
		//
		Commodity c19 = BaseCommodityTest.DataInput.getCommodity();
		c19.setRefCommodityMultiple(2);
		c19.setPackageUnitID(2);
		c19.setName("多包装商品19");
		//
		c18.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c18.getPackageUnitID() + "," + c19.getPackageUnitID() + ";" + c18.getRefCommodityMultiple() + "," + c19.getRefCommodityMultiple() + ";" + c18.getPriceRetail() + ","
				+ c19.getPriceRetail() + ";" + c18.getPriceVIP() + "," + c19.getPriceVIP() + ";" + c18.getPriceWholesale() + "," + c19.getPriceWholesale() + ";" + c18.getName() + "," + c19.getName() + ";");
		//
		commodity18 = BaseCommodityTest.createCommodityViaAction(c18, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.replace("commodity18", commodity18);
	}

	@Test(dependsOnMethods = "createCommodityWithTheSameNameAfterUpdateCommodity")
	public void updateSubCommodityThenRetrieveCombinationCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个组合商品35的子商品后再查看该组合商品");

		// 修改组合商品35的子商品34
		Commodity commodity34 = (Commodity) CommodityListMap.get("commodity34");
		Commodity c34 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c34.setID(CommodityListMap.get("commodity34").getID());
		c34.setName("修改商品34");
		//
		Commodity commodity34Update = updateCommodity(commodity34, c34);
		CommodityListMap.replace("commodity34", commodity34Update);
		//
		// 查看组合商品35
		Commodity commodity35 = (Commodity) CommodityListMap.get("commodity35");
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity35.getName() + "&" + Commodity.field.getFIELD_NAME_type() + "=" + commodity35.getType())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "updateSubCommodityThenRetrieveCombinationCommodity")
	public void updateCommodityThenRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个商品的名称（普通商品41、多包装商品的基本商品4）后，再根据名称搜索");

		// 修改普通商品41名称
		Commodity commodity41 = (Commodity) CommodityListMap.get("commodity41");
		Commodity c41 = BaseCommodityTest.DataInput.getCommodity();
		c41.setID(CommodityListMap.get("commodity41").getID());
		c41.setName("修改商品41");
		c41.setMultiPackagingInfo(getBarcode() + ";" + c41.getPackageUnitID() + ";" + c41.getRefCommodityMultiple() + ";" + c41.getPriceRetail() + ";" + c41.getPriceVIP() + ";" + c41.getPriceWholesale() + ";" + c41.getName() + ";");
		//
		Commodity commodity41Update = updateCommodity(commodity41, c41);
		CommodityListMap.replace("commodity41", commodity41Update);
		//
		// 根据名称搜索
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c41.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		//
		// 修改多包装商品的基本商品4的名称
		Commodity commodity4 = (Commodity) CommodityListMap.get("commodity4");
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity4.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		JSONObject jObject = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		List<?> MultiPackagingCommodityList = JsonPath.read(jObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity c5 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < MultiPackagingCommodityList.size(); i++) {
			c5 = (Commodity) c5.parse1(MultiPackagingCommodityList.get(i).toString());
			if ((c5.getName()).equals("修改多包装商品5")) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		//
		Commodity c4 = BaseCommodityTest.DataInput.getCommodity();
		c4.setID(commodity4.getID());
		c4.setName("修改商品4");
		c4.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c4.getPackageUnitID() + "," + c5.getPackageUnitID() + ";" + c4.getRefCommodityMultiple() + "," + c5.getRefCommodityMultiple() + ";" + c4.getPriceRetail() + ","
				+ c5.getPriceRetail() + ";" + c4.getPriceVIP() + "," + c5.getPriceVIP() + ";" + c4.getPriceWholesale() + "," + c5.getPriceWholesale() + ";" + c4.getName() + "," + c5.getName() + ";");
		//
		Commodity commodity4Update = updateCommodity(commodity4, c4);
		CommodityListMap.replace("commodity4", commodity4Update);
		//
		// 根据名称搜索
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + commodity4Update.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
	}

	@Test(dependsOnMethods = "updateCommodityThenRetrieve")
	public void updateCommodityThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个商品（普通商品20、多包装商品30的基本商品29）后删除，再搜索该商品");

		// 修改商品29
		Commodity commodity29 = (Commodity) CommodityListMap.get("commodity29");
		Commodity c29 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c29.setID(commodity29.getID());
		c29.setName("修改商品29");
		c29.setMultiPackagingInfo(getBarcode() + ";" + c29.getPackageUnitID() + ";" + c29.getRefCommodityMultiple() + ";" + c29.getPriceRetail() + ";" + c29.getPriceVIP() + ";" + c29.getPriceWholesale() + ";" + c29.getName() + ";");
		// 修改
		Commodity commodity29Update = updateCommodity(commodity29, c29);
		//
		// 删除商品29
		deleteCommodity(commodity29Update, dbNameOfNewCompany1);
		//
		// 再搜索该商品
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + c29.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		//
		// 修改普通商品20
		Commodity commodity20 = (Commodity) CommodityListMap.get("commodity20");
		Commodity c20 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c20.setID(commodity20.getID());
		c20.setName("修改商品20");
		c20.setMultiPackagingInfo(getBarcode() + ";" + c20.getPackageUnitID() + ";" + c20.getRefCommodityMultiple() + ";" + c20.getPriceRetail() + ";" + c20.getPriceVIP() + ";" + c20.getPriceWholesale() + ";" + c20.getName() + ";");
		//
		Commodity commodity20Update = updateCommodity(commodity20, c20);
		//
		// 删除之
		deleteCommodity(commodity20Update, dbNameOfNewCompany1);
		//
		// 再搜索该商品
		MvcResult mr6 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + c20.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
	}

	@Test(dependsOnMethods = "updateCommodityThenDelete")
	public void updateCategoryThenRetrieveByCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个商品（普通商品41、多包装商品的基本商品4）的商品分类后，再根据商品分类查询");

		// 修改普通商品41商品分类
		Commodity commodity41 = (Commodity) CommodityListMap.get("commodity41");
		Commodity c41 = BaseCommodityTest.DataInput.getCommodity();
		c41.setID(commodity41.getID());
		c41.setName("修改商品41");
		c41.setCategoryID(2);
		c41.setMultiPackagingInfo(getBarcode() + ";" + c41.getPackageUnitID() + ";" + c41.getRefCommodityMultiple() + ";" + c41.getPriceRetail() + ";" + c41.getPriceVIP() + ";" + c41.getPriceWholesale() + ";" + c41.getName() + ";");
		//
		Commodity commodity41Update = updateCommodity(commodity41, c41);
		CommodityListMap.replace("commodity41", commodity41Update);
		//
		// 搜索
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_categoryID() + "=" + c41.getCategoryID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		//
		// 修改多包装商品的基本商品4的商品分类
		Commodity commodity4 = (Commodity) CommodityListMap.get("commodity4");
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity4.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr5);
		JSONObject jObject = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		List<?> MultiPackagingCommodityList = JsonPath.read(jObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity c5 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < MultiPackagingCommodityList.size(); i++) {
			c5 = (Commodity) c5.parse1(MultiPackagingCommodityList.get(i).toString());
			if ((c5.getName()).equals("修改多包装商品5")) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		//
		Commodity c4 = BaseCommodityTest.DataInput.getCommodity();
		c4.setID(commodity4.getID());
		c4.setName("修改商品4");
		c4.setCategoryID(2);
		c4.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c4.getPackageUnitID() + "," + c5.getPackageUnitID() + ";" + c4.getRefCommodityMultiple() + "," + c5.getRefCommodityMultiple() + ";" + c4.getPriceRetail() + ","
				+ c5.getPriceRetail() + ";" + c4.getPriceVIP() + "," + c5.getPriceVIP() + ";" + c4.getPriceWholesale() + "," + c5.getPriceWholesale() + ";" + c4.getName() + "," + c5.getName() + ";");
		//
		Commodity commodity4Update = updateCommodity(commodity4, c4);
		CommodityListMap.replace("commodity4", commodity4Update);
		//
		// 根据名称搜索
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_categoryID() + "=" + c4.getCategoryID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
	}

	@Test(dependsOnMethods = "updateCategoryThenRetrieveByCategory")
	public void updateCommodityAndTurnPageThenSave() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改商品18/19信息时，翻页后，再点击保存");

		Commodity commodity18 = (Commodity) CommodityListMap.get("commodity18");
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity18.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		JSONObject jObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(jObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity c19 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < listMultiPackageCommodity.size(); i++) {
			c19 = (Commodity) c19.parse1(listMultiPackageCommodity.get(i).toString());
			if ((c19.getName()).equals("多包装商品19")) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		c19.setName("修改多包装19");
		c19.setPackageUnitID(4);
		//
		Commodity c18 = BaseCommodityTest.DataInput.getUpdateCommodity();
		c18.setID(CommodityListMap.get("commodity18").getID());
		c18.setName("修改商品18");
		c18.setCategoryID(2);
		c18.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c18.getPackageUnitID() + "," + c19.getPackageUnitID() + ";" + c18.getRefCommodityMultiple() + "," + c19.getRefCommodityMultiple() + ";" + c18.getPriceRetail() + ","
				+ c19.getPriceRetail() + ";" + c18.getPriceVIP() + "," + c19.getPriceVIP() + ";" + c18.getPriceWholesale() + "," + c19.getPriceWholesale() + ";" + c18.getName() + "," + c19.getName() + ";");
		//
		Commodity commodity18Update = updateCommodity(commodity18, c18);
		CommodityListMap.replace("commodity18", commodity18Update);
	}

	@Test(dependsOnMethods = "updateCommodityAndTurnPageThenSave")
	public void deleteCommodityThenRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "删除一个商品（普通商品22、多包装商品23）后再搜索该商品");

		// 查出多包装23的ID
		Commodity commodity22 = (Commodity) CommodityListMap.get("commodity22");
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity22.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<?> listMultiPackageCommodity = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity c23 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < listMultiPackageCommodity.size(); i++) {
			c23 = (Commodity) c23.parse1(listMultiPackageCommodity.get(i).toString());
			if ((c23.getName()).equals("修改多包装23")) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		//
		// 删除多包装23
		deleteCommodity(c23, dbNameOfNewCompany1);
		//
		// 删除商品22
		deleteCommodity(commodity22, dbNameOfNewCompany1);
		//
		// 搜索该商品
		MvcResult mr4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity22.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr4);
	}

	@Test(dependsOnMethods = "deleteCommodityThenRetrieve")
	public void deleteCommodityThenCreateAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "删除一个商品（普通44、多包装33、组合45）后再创建一个同名的商品");

		// 删除组合商品45
		Commodity commodity45 = (Commodity) CommodityListMap.get("commodity45");
		deleteCommodity(commodity45, dbNameOfNewCompany1);
		//
		// 删除普通商品44
		Commodity commodity44 = (Commodity) CommodityListMap.get("commodity44");
		deleteCommodity(commodity44, dbNameOfNewCompany1);
		//
		// 删除多包装33
		// 查出多包装33的ID
		Commodity commodity1 = (Commodity) CommodityListMap.get("commodity1");
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodity1.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr3);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> MultiPackagingCommodityList = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity c33 = new Commodity();
		Boolean flag = false;
		for (int i = 0; i < MultiPackagingCommodityList.size(); i++) {
			c33 = (Commodity) c33.parse1(MultiPackagingCommodityList.get(i).toString());
			if ("多包装商品33".equals(c33.getName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品不存在");
		//
		deleteCommodity(c33, dbNameOfNewCompany1);
		//
		// 创建同名商品
		// 创建普通商品44
		Commodity c44 = BaseCommodityTest.DataInput.getCommodity();
		c44.setName("普通商品44");
		String c44Barcode = String.valueOf(getBarcode());
		c44.setMultiPackagingInfo(c44Barcode + ";" + c44.getPackageUnitID() + ";" + c44.getRefCommodityMultiple() + ";" + c44.getPriceRetail() + ";" + c44.getPriceVIP() + ";" + c44.getPriceWholesale() + ";" + c44.getName() + ";");
		commodity44 = BaseCommodityTest.createCommodityViaAction(c44, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.replace("commodity44", commodity44);
		//
		// 创建组合商品45(44+4)
		SubCommodity comm4 = new SubCommodity();
		comm4.setSubCommodityID(CommodityListMap.get("commodity4").getID());
		comm4.setSubCommodityNO(1);
		comm4.setPrice(20);
		//
		SubCommodity comm44 = new SubCommodity();
		comm44.setSubCommodityID(commodity44.getID());
		comm44.setSubCommodityNO(1);
		comm44.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm4);
		ListCommodity.add(comm44);
		//
		Commodity c45 = BaseCommodityTest.DataInput.getCommodity();
		c45.setShelfLife(0);
		c45.setPurchaseFlag(0);
		c45.setRuleOfPoint(0);
		c45.setListSlave1(ListCommodity);
		c45.setType(EnumCommodityType.ECT_Combination.getIndex());
		c45.setName("组合商品45");
		c45.setMultiPackagingInfo(c44Barcode + ";" + c45.getPackageUnitID() + ";" + c45.getRefCommodityMultiple() + ";" + c45.getPriceRetail() + ";" + c45.getPriceVIP() + ";" + c45.getPriceWholesale() + ";" + c45.getName() + ";");
		String json = JSONObject.fromObject(c45).toString();
		MvcResult mr6 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c45, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c45, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		JSONObject o = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		commodity45 = (Commodity) c45.parse1(o.getString("object"));
		CommodityListMap.replace("commodity45", commodity45);
		//
		// 创建多包装33
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("普通商品1");
		c1.setID(commodity1.getID());
		c1.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c1.getPackageUnitID() + "," + c33.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + "," + c33.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ","
				+ c33.getPriceRetail() + ";" + c1.getPriceVIP() + "," + c33.getPriceVIP() + ";" + c1.getPriceWholesale() + "," + c33.getPriceWholesale() + ";" + c1.getName() + "," + c33.getName() + ";");
		Commodity commodity1Update = updateCommodity(commodity1, c1);
		CommodityListMap.replace("commodity1", commodity1Update);
	}

	@Test(dependsOnMethods = "deleteCommodityThenCreateAgain")
	public void deleteTheLastPageCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "删除商品列表最后一页的数据");

		// 查询出所有商品的数量
		MvcResult mr1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 查出商品总数量
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		int commodityNum = JsonPath.read(jsonObject, "$.count");
		// 每页显示的条数
		int pageSize = 10;
		// 页数
		int pageNum = 0;
		if (commodityNum % pageSize == 0) {
			pageNum = commodityNum / pageSize;
		} else {
			pageNum = (commodityNum / pageSize) + 1;
		}
		// 查询出最后一页的商品
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_pageIndex(), String.valueOf(pageNum))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> commodityJSONArray = JsonPath.read(jsonObject, "$.objectList");
		Commodity commodity = new Commodity();
		// 先把在最后一页的组合商品给删掉，以免删除单品时有组合商品依赖
		for (int i = 0; i < commodityJSONArray.size(); i++) {
			int commodityType = JsonPath.read(commodityJSONArray.get(i), "$.commodity.type");
			if (commodityType == EnumCommodityType.ECT_Combination.getIndex()) {
				Commodity commodityToDelete = new Commodity();
				commodityToDelete = (Commodity) commodityToDelete.parse1(JsonPath.read(commodityJSONArray.get(i), "$.commodity").toString());
				deleteCommodity(commodityToDelete, dbNameOfNewCompany1);
			}
		}
		//
		// 把所有的组合商品的子商品ID查出来，再与最后一页的商品ID比较，如果相等的话说明该商品已被其他页的组合商品引用，所以删不了
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=" + EnumCommodityType.ECT_Combination.getIndex())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<Integer> listCombinationCommodityID = JsonPath.read(jsonObject, "$.objectList[*].commodity.listSlave1[*].ID");
		//
		// 删除类型为0的商品
		for (int i = 0; i < commodityJSONArray.size(); i++) {
			// 把商品ID拿出来
			Commodity commodityToDelete = new Commodity();
			commodityToDelete = (Commodity) commodityToDelete.parse1(JsonPath.read(commodityJSONArray.get(i), "$.commodity").toString());
			// 检查商品是不是其他页的组合商品的子商品
			boolean subcommodityFlag = false;
			for (int j = 0; j < listCombinationCommodityID.size(); j++) {
				int subcommodityID = listCombinationCommodityID.get(j);
				if (subcommodityID == commodityToDelete.getID()) {
					subcommodityFlag = true;
					break;
				}
			}
			// 检查是否还有库存
			int commodityNO = JsonPath.read(commodityJSONArray.get(i), "$.commodity.NO");
			if (commodityNO != 0 || subcommodityFlag) {
				// 如果商品是其他页的组合商品的子商品或者还有库存则不能删除，跳过
				continue;
			}
			//
			// 判断商品类型是否为0
			int commodityType = JsonPath.read(commodityJSONArray.get(i), "$.commodity.type");
			if (commodityType == EnumCommodityType.ECT_Normal.getIndex()) {
				// 查看有无多包装
				List<?> listMultiPackageCommodity = JsonPath.read(commodityJSONArray.get(i), "$.listMultiPackageCommodity");
				// 如果有
				if (listMultiPackageCommodity.size() != 0) {
					// 先删除多包装
					for (int k = 0; k < listMultiPackageCommodity.size(); k++) {
						Commodity multiPackagecommodity = (Commodity) commodity.parse1(listMultiPackageCommodity.get(k).toString());
						deleteCommodity(multiPackagecommodity, dbNameOfNewCompany1);
					}
					// 再删除该商品
					deleteCommodity(commodityToDelete, dbNameOfNewCompany1);
				} else {
					// 如果没有多包装则直接删除该商品
					deleteCommodity(commodityToDelete, dbNameOfNewCompany1);
				}
			}
		}
	}

	@Test(dependsOnMethods = "deleteTheLastPageCommodity")
	public void useLocalComputerCURDCommodityListThenUseOtherComputerLoginAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDCommodityListThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDCommodityList() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

	}

	@Test(dependsOnMethods = "useTwoOrMoreComputerLoginAtTheSameTimeAndCURDCommodityList")
	public void bxStaffTest() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "售前人员无法进行CURD操作");
		// 售前人员无法进行CURD操作，可以先不进行测试
	}

	@Test(dependsOnMethods = "bxStaffTest")
	public void specialCharactersRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "传入包含下划线_的特殊字符进行模糊搜索");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建普通商品54
		Commodity c54 = BaseCommodityTest.DataInput.getCommodity();
		c54.setName(CommonCommodity + "_" + getCommodityOrder());
		c54.setMultiPackagingInfo(getBarcode() + ";" + c54.getPackageUnitID() + ";" + c54.getRefCommodityMultiple() + ";" + c54.getPriceRetail() + ";" + c54.getPriceVIP() + ";" + c54.getPriceWholesale() + ";" + c54.getName() + ";");
		BaseCommodityTest.createCommodityViaAction(c54, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		// 模糊搜索带有特殊字符下划线_
		Commodity c = new Commodity();
		c.setQueryKeyword("_");
		retrieveNExCommodity(c);

	}

	@Test(dependsOnMethods = "specialCharactersRetrieveN")
	public void createServiceCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建服务类商品45后重新打开商品列表页面");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品45
		Commodity c45 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate45 = BaseCommodityTest.createCommodityViaAction(c45, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate45.setBarcodes(c45.getBarcodes());
		CommodityListMap.put("serviceCommodity45", commCreate45);
		// 重新打开商品列表页面查看新建的服务商品45是否存在
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		Assert.assertTrue(commodity != null, "解析commodity失败！");
		//
		c45.setIgnoreIDInComparision(true);
		if (c45.compareTo(commodity) != 0) {
			Assert.assertTrue(false, "返回的商品信息不正确，期望的是新建的" + c45.getName() + "在第一位");
		}
	}

	@Test(dependsOnMethods = "createServiceCommodity")
	public void retrieveNServiceCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建服务类商品46后查询服务类商品46");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品46
		Commodity c46 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate46 = BaseCommodityTest.createCommodityViaAction(c46, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate46.setBarcodes(c46.getBarcodes());
		CommodityListMap.put("serviceCommodity46", commCreate46); // 把该商品保存起来，方便下面的测试方法使用
		// 通过分类和关键字查询新建的服务商品46
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_categoryID(), String.valueOf(commCreate46.getCategoryID()))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), "服务")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		Assert.assertTrue(commodity != null, "解析commodity失败！");
		//
		c46.setIgnoreIDInComparision(true);
		if (c46.compareTo(commodity) != 0) {
			Assert.assertTrue(false, "返回的商品信息不正确，期望的是新建的" + c46.getName() + "在第一位");
		}
	}

	@Test(dependsOnMethods = "retrieveNServiceCommodity")
	public void updateServiceCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改服务类商品46");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 修改服务商品46
		Commodity c46 = (Commodity) CommodityListMap.get("serviceCommodity46");
		Commodity comm46 = getServiceCommodity("修改" + c46.getName(), String.valueOf(getBarcode()));
		comm46.setID(c46.getID());
		comm46.setMultiPackagingInfo(comm46.getBarcodes() + ";" + comm46.getPackageUnitID() + ";" + comm46.getRefCommodityMultiple() + ";" //
				+ comm46.getPriceRetail() + ";" + comm46.getPriceVIP() + ";" + comm46.getPriceWholesale() + ";" + comm46.getName() + ";");
		Commodity commUpdate46 = updateCommodity(c46, comm46);
		CommodityListMap.replace("serviceCommodity46", commUpdate46);
	}

	@Test(dependsOnMethods = "updateServiceCommodityTest1")
	public void cancelUpdateServiceCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改服务类商品45取消修改");
	}

	@Test(dependsOnMethods = "cancelUpdateServiceCommodityTest1")
	public void deleteServiceCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建服务类商品47后删除之");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品47
		Commodity c47 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate47 = BaseCommodityTest.createCommodityViaAction(c47, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		// 删除服务商品47
		deleteCommodity(commCreate47, dbNameOfNewCompany1); // 商品的删除检查点有检查商品是否被删除
	}

	@Test(dependsOnMethods = "deleteServiceCommodityTest1")
	public void updateAndRetrieveNServiceCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个服务类商品48后修改其名称，再搜索之");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品48
		Commodity c48 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate48 = BaseCommodityTest.createCommodityViaAction(c48, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate48.setBarcodes(c48.getBarcodes());
		// 修改服务商品48
		Commodity comm48 = getServiceCommodity("顺丰快递", commCreate48.getBarcodes());
		comm48.setID(commCreate48.getID());
		comm48.setMultiPackagingInfo(comm48.getBarcodes() + ";" + comm48.getPackageUnitID() + ";" + comm48.getRefCommodityMultiple() + ";" //
				+ comm48.getPriceRetail() + ";" + comm48.getPriceVIP() + ";" + comm48.getPriceWholesale() + ";" + comm48.getName() + ";");
		Commodity commUpdate48 = updateCommodity(commCreate48, comm48);
		commUpdate48.setBarcodes(comm48.getBarcodes());
		CommodityListMap.put("serviceCommodity48", commUpdate48);
		// 根据修改前的商品名称查询商品，期望的是查询不到商品
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), String.valueOf(commCreate48.getName()))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(commJSONArray.size() == 0, "测试失败！期望的是返回空");
		// 根据修改后的商品名称查询商品，期望的是能查询到商品
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), String.valueOf(commUpdate48.getName()))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 结果验证
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		JSONArray commJSONArray2 = jsonObject2.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json2 = commJSONArray2.getJSONObject(0);
		Commodity commodity2 = (Commodity) new Commodity().parse1(json2.getString("commodity"));
		Assert.assertTrue(commodity2 != null, "解析commodity失败！");
		//
		commUpdate48.setIgnoreIDInComparision(true);
		if (commUpdate48.compareTo(commodity2) != 0) {
			Assert.assertTrue(false, "测试失败！期望的是能查询到商品");
		}
	}

	@Test(dependsOnMethods = "updateAndRetrieveNServiceCommodityTest1")
	public void createTheOriginalCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个服务类商品后修改该商品名称，再创建一个原名称的商品");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品49
		Commodity c49 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate49 = BaseCommodityTest.createCommodityViaAction(c49, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate49.setBarcodes(c49.getBarcodes());
		//
		// 修改服务商品49的名称
		Commodity comm49 = getServiceCommodity("修改" + commCreate49.getName(), commCreate49.getBarcodes());
		comm49.setID(commCreate49.getID());
		comm49.setMultiPackagingInfo(comm49.getBarcodes() + ";" + comm49.getPackageUnitID() + ";" + comm49.getRefCommodityMultiple() + ";" //
				+ comm49.getPriceRetail() + ";" + comm49.getPriceVIP() + ";" + comm49.getPriceWholesale() + ";" + comm49.getName() + ";");
		Commodity commUpdate49 = updateCommodity(commCreate49, comm49);
		CommodityListMap.put("serviceCommodity49", commUpdate49);
		//
		// 使用服务商品的原名称创建普通商品50
		Commodity c50 = BaseCommodityTest.DataInput.getCommodity();
		c50.setName(commCreate49.getName());
		c50.setBarcodes(String.valueOf(getBarcode()));
		c50.setMultiPackagingInfo(c50.getBarcodes() + ";" + c50.getPackageUnitID() + ";" + c50.getRefCommodityMultiple() + ";" //
				+ c50.getPriceRetail() + ";" + c50.getPriceVIP() + ";" + c50.getPriceWholesale() + ";" + c50.getName() + ";");
		Commodity commCreate50 = BaseCommodityTest.createCommodityViaAction(c50, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate50.setBarcodes(c50.getBarcodes());
		//
		// 修改普通商品50的名称
		Commodity comm50 = BaseCommodityTest.DataInput.getCommodity();
		comm50.setID(commCreate50.getID());
		comm50.setName(CommonCommodity + getCommodityOrder());
		comm50.setBarcodes(commCreate50.getBarcodes());
		comm50.setMultiPackagingInfo(comm50.getBarcodes() + ";" + comm50.getPackageUnitID() + ";" + comm50.getRefCommodityMultiple() + ";" //
				+ comm50.getPriceRetail() + ";" + comm50.getPriceVIP() + ";" + comm50.getPriceWholesale() + ";" + comm50.getName() + ";");
		Commodity commUpdate50 = updateCommodity(commCreate50, comm50);
		commUpdate50.setBarcodes(comm50.getBarcodes());
		//
		// 使用服务商品49的原名称创建服务商品51
		Commodity c51 = getServiceCommodity(commCreate49.getName(), String.valueOf(getBarcode()));
		Commodity commCreate51 = BaseCommodityTest.createCommodityViaAction(c51, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate51.setBarcodes(c51.getBarcodes());
		//
		// 修改服务商品51的名称
		Commodity comm51 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), commCreate51.getBarcodes());
		comm51.setID(commCreate51.getID());
		comm51.setMultiPackagingInfo(comm51.getBarcodes() + ";" + comm51.getPackageUnitID() + ";" + comm51.getRefCommodityMultiple() + ";" //
				+ comm51.getPriceRetail() + ";" + comm51.getPriceVIP() + ";" + comm51.getPriceWholesale() + ";" + comm51.getName() + ";");
		Commodity commUpdate51 = updateCommodity(commCreate51, comm51);
		commUpdate51.setBarcodes(comm51.getBarcodes());
		CommodityListMap.put("serviceCommodity51", commUpdate51);
		//
		// 使用服务商品49的原名称创建多包装商品52(参考商品为修改普通商品50，参考倍数为4)
		Commodity c52 = BaseCommodityTest.DataInput.getCommodity();
		c52.setRefCommodityMultiple(4);
		c52.setName(commCreate49.getName());
		c52.setBarcodes(String.valueOf(getBarcode()));
		//
		// 给名称为修改普通商品50增加多包装商品52
		Commodity commodity50 = BaseCommodityTest.DataInput.getCommodity();
		commodity50.setName(commUpdate50.getName());
		commodity50.setID(commUpdate50.getID());
		commodity50.setBarcodes(commUpdate50.getBarcodes());
		commodity50.setMultiPackagingInfo(commodity50.getBarcodes() + "," + c52.getBarcodes() + ";" + commodity50.getPackageUnitID() + "," + c52.getPackageUnitID() + ";" //
				+ commUpdate50.getRefCommodityMultiple() + "," + c52.getRefCommodityMultiple() + ";" + commUpdate50.getPriceRetail() + "," + c52.getPriceRetail() + ";" //
				+ commUpdate50.getPriceVIP() + "," + c52.getPriceVIP() + ";" + commUpdate50.getPriceWholesale() + "," + c52.getPriceWholesale() + ";" + commUpdate50.getName() + "," + c52.getName() + ";");
		Commodity commodityUpdate50 = updateCommodity(commUpdate50, commodity50);
		//
		// 删除多包装商品52
		updateCommodity(commodityUpdate50, comm50);
		//
		// 使用服务商品49的原名称创建服务商品53
		Commodity c53 = getServiceCommodity(commCreate49.getName(), String.valueOf(getBarcode()));
		Commodity commCreate53 = BaseCommodityTest.createCommodityViaAction(c53, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate53.setBarcodes(c53.getBarcodes());
		//
		// 修改服务商品53的名称
		Commodity comm53 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), commCreate53.getBarcodes());
		comm53.setID(commCreate53.getID());
		comm53.setMultiPackagingInfo(comm53.getBarcodes() + ";" + comm53.getPackageUnitID() + ";" + comm53.getRefCommodityMultiple() + ";" //
				+ comm53.getPriceRetail() + ";" + comm53.getPriceVIP() + ";" + comm53.getPriceWholesale() + ";" + comm53.getName() + ";");
		Commodity cpmmUpdate53 = updateCommodity(commCreate53, comm53);
		cpmmUpdate53.setBarcodes(comm53.getBarcodes());
		CommodityListMap.put("serviceCommodity53", cpmmUpdate53);
		//
		// 创建组合商品的子商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName(CommonCommodity + getCommodityOrder());
		comm1.setBarcodes(String.valueOf(getBarcode()));
		comm1.setMultiPackagingInfo(
				comm1.getBarcodes() + ";" + comm1.getPackageUnitID() + ";" + comm1.getRefCommodityMultiple() + ";" + comm1.getPriceRetail() + ";" + comm1.getPriceVIP() + ";" + comm1.getPriceWholesale() + ";" + comm1.getName() + ";");
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaAction(comm1, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setName(CommonCommodity + getCommodityOrder());
		comm2.setBarcodes(String.valueOf(getBarcode()));
		comm2.setMultiPackagingInfo(
				comm2.getBarcodes() + ";" + comm2.getPackageUnitID() + ";" + comm2.getRefCommodityMultiple() + ";" + comm2.getPriceRetail() + ";" + comm2.getPriceVIP() + ";" + comm2.getPriceWholesale() + ";" + comm2.getName() + ";");
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(comm2, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		SubCommodity subComm1 = new SubCommodity();
		subComm1.setSubCommodityID(commCreate1.getID());
		subComm1.setSubCommodityNO(1);
		subComm1.setPrice(20);
		//
		SubCommodity subComm2 = new SubCommodity();
		subComm2.setSubCommodityID(commCreate2.getID());
		subComm2.setSubCommodityNO(1);
		subComm2.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(subComm1);
		ListCommodity.add(subComm2);
		//
		Commodity c54 = BaseCommodityTest.DataInput.getCommodity();
		c54.setShelfLife(0);
		c54.setPurchaseFlag(0);
		c54.setRuleOfPoint(0);
		c54.setListSlave1(ListCommodity);
		c54.setType(EnumCommodityType.ECT_Combination.getIndex());
		c54.setName(CombinationCommodity + getCommodityOrder());
		c54.setMultiPackagingInfo(getBarcode() + ";" + c54.getPackageUnitID() + ";" + c54.getRefCommodityMultiple() + ";" + c54.getPriceRetail() + ";" + c54.getPriceVIP() + ";" + c54.getPriceWholesale() + ";" + c54.getName() + ";");
		String json = JSONObject.fromObject(c54).toString();
		MvcResult mr6 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c54, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c54, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		//
		JSONObject o = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Commodity commodity54 = (Commodity) c54.parse1(o.getString("object"));
		//
		// 删除组合商品54
		deleteCommodity(commodity54, dbNameOfNewCompany1);
		// 使用服务商品49的原名称创建服务商品55
		Commodity c55 = getServiceCommodity(commCreate49.getName(), String.valueOf(getBarcode()));
		Commodity commCreate55 = BaseCommodityTest.createCommodityViaAction(c55, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate55.setBarcodes(c55.getBarcodes());
		CommodityListMap.put("serviceCommodity55", commCreate55);
	}

	@Test(dependsOnMethods = "createTheOriginalCommodityTest1")
	public void updateAndRetrieveNServiceCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个服务类商品56，后修改该商品名称/简称/条形码，再根据名称/简称/条形码/助机码搜索该商品");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品56
		Commodity c56 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate56 = BaseCommodityTest.createCommodityViaAction(c56, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate56.setBarcodes(c56.getBarcodes());
		// 修改服务商品56的条形码
		String barcodeUpdate = String.valueOf(getBarcode());
		List<Barcodes> list = getListBarcodes(commCreate56);
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(list.get(0).getID()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commCreate56.getID()))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodeUpdate)//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		// 修改服务商品56的名称/简称
		Commodity comm56 = getServiceCommodity("修改" + commCreate56.getName(), barcodeUpdate);
		comm56.setID(commCreate56.getID());
		comm56.setShortName("56号服务商品");
		comm56.setMultiPackagingInfo(comm56.getBarcodes() + ";" + comm56.getPackageUnitID() + ";" + comm56.getRefCommodityMultiple() + ";" //
				+ comm56.getPriceRetail() + ";" + comm56.getPriceVIP() + ";" + comm56.getPriceWholesale() + ";" + comm56.getName() + ";");
		Commodity commUpdate56 = updateCommodity(commCreate56, comm56);
		commUpdate56.setBarcodes(comm56.getBarcodes());
		CommodityListMap.put("serviceCommodity56", commUpdate56);
		// 根据名称搜索该商品
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), commUpdate56.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		Assert.assertTrue(commodity != null, "解析commodity失败！");
		//
		commUpdate56.setIgnoreIDInComparision(true);
		if (commUpdate56.compareTo(commodity) != 0) {
			Assert.assertTrue(false, "测试失败！期望的是能查询到商品");
		}
		// 根据简称搜索该商品
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), commUpdate56.getShortName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 结果验证
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		JSONArray commJSONArray2 = jsonObject2.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json2 = commJSONArray2.getJSONObject(0);
		Commodity commodity2 = (Commodity) new Commodity().parse1(json2.getString("commodity"));
		Assert.assertTrue(commodity2 != null, "解析commodity失败！");
		//
		commUpdate56.setIgnoreIDInComparision(true);
		if (commUpdate56.compareTo(commodity2) != 0) {
			Assert.assertTrue(false, "测试失败！期望的是能查询到商品");
		}
		// 根据条形码搜索该商品
		MvcResult mr3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), commUpdate56.getBarcodes())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// 结果验证
		JSONObject jsonObject3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		JSONArray commJSONArray3 = jsonObject3.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json3 = commJSONArray3.getJSONObject(0);
		Commodity commodity3 = (Commodity) new Commodity().parse1(json3.getString("commodity"));
		Assert.assertTrue(commodity3 != null, "解析commodity失败！");
		//
		commUpdate56.setIgnoreIDInComparision(true);
		if (commUpdate56.compareTo(commodity3) != 0) {
			Assert.assertTrue(false, "测试失败！期望的是能查询到商品");
		}
	}

	@Test(dependsOnMethods = "updateAndRetrieveNServiceCommodityTest2")
	public void updateAndDeleteServiceCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建服务类商品后，先修改该商品的信息，再进行删除");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品57
		Commodity c57 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate57 = BaseCommodityTest.createCommodityViaAction(c57, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate57.setBarcodes(c57.getBarcodes());
		// 修改服务商品57
		Commodity comm57 = getServiceCommodity("修改" + commCreate57.getName(), String.valueOf(getBarcode()));
		comm57.setID(commCreate57.getID());
		comm57.setShortName("56号服务商品");
		comm57.setMultiPackagingInfo(comm57.getBarcodes() + ";" + comm57.getPackageUnitID() + ";" + comm57.getRefCommodityMultiple() + ";" //
				+ comm57.getPriceRetail() + ";" + comm57.getPriceVIP() + ";" + comm57.getPriceWholesale() + ";" + comm57.getName() + ";");
		Commodity commUpdate57 = updateCommodity(commCreate57, comm57);
		// 删除服务商品57
		deleteCommodity(commUpdate57, dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "updateAndDeleteServiceCommodityTest")
	public void createTheOriginalCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "创建一个服务类商品后删除该商品，再创建一个原名称的商品");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 创建服务商品58
		Commodity c58 = getServiceCommodity(ServiceCommodity + getCommodityOrder(), String.valueOf(getBarcode()));
		Commodity commCreate58 = BaseCommodityTest.createCommodityViaAction(c58, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 删除服务商品58
		deleteCommodity(commCreate58, dbNameOfNewCompany1);
		//
		// 使用服务商品58的名称创建普通商品59
		Commodity c59 = BaseCommodityTest.DataInput.getCommodity();
		c59.setName(commCreate58.getName());
		c59.setBarcodes(String.valueOf(getBarcode()));
		c59.setMultiPackagingInfo(c59.getBarcodes() + ";" + c59.getPackageUnitID() + ";" + c59.getRefCommodityMultiple() + ";" //
				+ c59.getPriceRetail() + ";" + c59.getPriceVIP() + ";" + c59.getPriceWholesale() + ";" + c59.getName() + ";");
		Commodity commCreate59 = BaseCommodityTest.createCommodityViaAction(c59, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 删除普通商品59
		deleteCommodity(commCreate59, dbNameOfNewCompany1);
		//
		// 使用服务商品58的名称创建服务商品60
		Commodity c60 = getServiceCommodity(commCreate58.getName(), String.valueOf(getBarcode()));
		Commodity commCreate60 = BaseCommodityTest.createCommodityViaAction(c60, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 删除服务商品60
		deleteCommodity(commCreate60, dbNameOfNewCompany1);
		//
		// 使用服务商品58的原名称创建多包装商品61(参考倍数为4)
		Commodity c61 = BaseCommodityTest.DataInput.getCommodity();
		c61.setRefCommodityMultiple(4);
		c61.setName(commCreate58.getName());
		c61.setBarcodes(String.valueOf(getBarcode()));
		//
		// 创建多包装商品61的参考商品
		Commodity comm61 = BaseCommodityTest.DataInput.getCommodity();
		comm61.setName(commCreate58.getName() + "的参考商品");
		comm61.setBarcodes(String.valueOf(getBarcode()));
		comm61.setMultiPackagingInfo(comm61.getBarcodes() + "," + c61.getBarcodes() + ";" + comm61.getPackageUnitID() + "," + c61.getPackageUnitID() + ";" //
				+ comm61.getRefCommodityMultiple() + "," + c61.getRefCommodityMultiple() + ";" + comm61.getPriceRetail() + "," + c61.getPriceRetail() + ";" //
				+ comm61.getPriceVIP() + "," + c61.getPriceVIP() + ";" + comm61.getPriceWholesale() + "," + c61.getPriceWholesale() + ";" + comm61.getName() + "," + c61.getName() + ";");
		Commodity commCreate61 = BaseCommodityTest.createCommodityViaAction(comm61, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		commCreate61.setBarcodes(comm61.getBarcodes());
		//
		// 删除多包装商品61
		Commodity commodity61 = BaseCommodityTest.DataInput.getCommodity();
		commodity61.setName(comm61.getName());
		commodity61.setID(commCreate61.getID());
		commodity61.setBarcodes(commCreate61.getBarcodes());
		commodity61.setMultiPackagingInfo(commodity61.getBarcodes() + ";" + commodity61.getPackageUnitID() + ";" + commodity61.getRefCommodityMultiple() + ";" //
				+ commodity61.getPriceRetail() + ";" + commodity61.getPriceVIP() + ";" + commodity61.getPriceWholesale() + ";" + commodity61.getName() + ";");
		updateCommodity(commCreate61, commodity61);
		//
		// 使用服务商品58的名称创建服务商品62
		Commodity c62 = getServiceCommodity(commCreate58.getName(), String.valueOf(getBarcode()));
		Commodity commCreate62 = BaseCommodityTest.createCommodityViaAction(c62, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		// 删除服务商品62
		deleteCommodity(commCreate62, dbNameOfNewCompany1);

		//
		// 创建组合商品63的子商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName(CommonCommodity + getCommodityOrder());
		comm1.setBarcodes(String.valueOf(getBarcode()));
		comm1.setMultiPackagingInfo(comm1.getBarcodes() + ";" + comm1.getPackageUnitID() + ";" + comm1.getRefCommodityMultiple() + ";" //
				+ comm1.getPriceRetail() + ";" + comm1.getPriceVIP() + ";" + comm1.getPriceWholesale() + ";" + comm1.getName() + ";");
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaAction(comm1, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setName(CommonCommodity + getCommodityOrder());
		comm2.setBarcodes(String.valueOf(getBarcode()));
		comm2.setMultiPackagingInfo(comm2.getBarcodes() + ";" + comm2.getPackageUnitID() + ";" + comm2.getRefCommodityMultiple() + ";" //
				+ comm2.getPriceRetail() + ";" + comm2.getPriceVIP() + ";" + comm2.getPriceWholesale() + ";" + comm2.getName() + ";");
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(comm2, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		//
		SubCommodity subComm1 = new SubCommodity();
		subComm1.setSubCommodityID(commCreate1.getID());
		subComm1.setSubCommodityNO(1);
		subComm1.setPrice(20);
		//
		SubCommodity subComm2 = new SubCommodity();
		subComm2.setSubCommodityID(commCreate2.getID());
		subComm2.setSubCommodityNO(1);
		subComm2.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(subComm1);
		ListCommodity.add(subComm2);
		//
		Commodity c63 = BaseCommodityTest.DataInput.getCommodity();
		c63.setShelfLife(0);
		c63.setPurchaseFlag(0);
		c63.setRuleOfPoint(0);
		c63.setListSlave1(ListCommodity);
		c63.setType(EnumCommodityType.ECT_Combination.getIndex());
		c63.setName(CombinationCommodity + getCommodityOrder());
		c63.setBarcodes(String.valueOf(getBarcode()));
		c63.setMultiPackagingInfo(c63.getBarcodes() + ";" + c63.getPackageUnitID() + ";" + c63.getRefCommodityMultiple() + ";" + c63.getPriceRetail() + ";" + c63.getPriceVIP() + ";" + c63.getPriceWholesale() + ";" + c63.getName() + ";");
		String json = JSONObject.fromObject(c63).toString();
		MvcResult mr6 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c63, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c63, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany1);
		//
		JSONObject o = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Commodity commodity63 = (Commodity) c63.parse1(o.getString("object"));
		//
		// 删除组合商品63
		deleteCommodity(commodity63, dbNameOfNewCompany1);
		// 使用服务商品58的名称创建服务商品64
		Commodity c64 = getServiceCommodity(commCreate58.getName(), String.valueOf(getBarcode()));
		Commodity commCreate64 = BaseCommodityTest.createCommodityViaAction(c64, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
		CommodityListMap.put("serviceCommodity64", commCreate64);
	}

	@Test(dependsOnMethods = "createTheOriginalCommodityTest2")
	public void updateServiceCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询服务类商品45的详情信息并修改");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 查询服务商品45
		Commodity c45 = (Commodity) CommodityListMap.get("serviceCommodity45");
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c45.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		// 修改服务商品45
		Commodity comm45 = getServiceCommodity("修改" + commodity.getName(), c45.getBarcodes());
		comm45.setID(commodity.getID());
		comm45.setMultiPackagingInfo(comm45.getBarcodes() + ";" + comm45.getPackageUnitID() + ";" + comm45.getRefCommodityMultiple() + ";" //
				+ comm45.getPriceRetail() + ";" + comm45.getPriceVIP() + ";" + comm45.getPriceWholesale() + ";" + comm45.getName() + ";");
		Commodity commUpdate45 = updateCommodity(commodity, comm45);
		commUpdate45.setBarcodes(comm45.getBarcodes());
		CommodityListMap.replace("serviceCommodity45", commUpdate45);
	}

	@Test(dependsOnMethods = "updateServiceCommodityTest2")
	public void deleteServiceCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询服务类商品46的详情信息并删除");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 查询服务商品46
		Commodity c46 = (Commodity) CommodityListMap.get("serviceCommodity46");
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c46.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		// 删除服务商品46
		deleteCommodity(commodity, dbNameOfNewCompany1);
		CommodityListMap.remove("serviceCommodity46");
	}

	@Test(dependsOnMethods = "deleteServiceCommodityTest2")
	public void updateAndDeleteServiceCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询服务类商品48的详情信息并进行修改和删除");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 查询服务商品48
		Commodity c48 = (Commodity) CommodityListMap.get("serviceCommodity48");
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c48.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		// 修改服务商品48
		Commodity commUpdate = getServiceCommodity("修改" + commodity.getName(), c48.getBarcodes());
		commUpdate.setID(commodity.getID());
		commUpdate.setMultiPackagingInfo(commUpdate.getBarcodes() + ";" + commUpdate.getPackageUnitID() + ";" + commUpdate.getRefCommodityMultiple() + ";" //
				+ commUpdate.getPriceRetail() + ";" + commUpdate.getPriceVIP() + ";" + commUpdate.getPriceWholesale() + ";" + commUpdate.getName() + ";");
		Commodity commodityUpdate = updateCommodity(commodity, commUpdate);
		// 删除服务商品48
		deleteCommodity(commodityUpdate, dbNameOfNewCompany1);
		CommodityListMap.remove("serviceCommodity48");
	}

	@Test(dependsOnMethods = "updateAndDeleteServiceCommodityTest1")
	public void cancelUpdateServiceCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询服务类商品49的详情进行修改时取消修改");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 查询服务商品49
		Commodity c49 = (Commodity) CommodityListMap.get("serviceCommodity49");
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c49.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 取消修改服务商品49
	}

	@Test(dependsOnMethods = "cancelUpdateServiceCommodityTest2")
	public void deleteAndCreateServiceCommodityTest1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "查询服务类商品55的详情进行删除，再创建一个同名的服务类商品65");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 查询服务商品55
		Commodity c55 = (Commodity) CommodityListMap.get("serviceCommodity55");
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c55.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
		// 删除服务商品55
		deleteCommodity(commodity, dbNameOfNewCompany1);
		// 使用服务商品55的名称创建服务商品65
		Commodity comm = getServiceCommodity(commodity.getName(), String.valueOf(getBarcode()));
		BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "deleteAndCreateServiceCommodityTest1")
	public void updateAndDeleteServiceCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个服务商品51后删除，再搜索该商品");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 修改商品51
		Commodity c51 = (Commodity) CommodityListMap.get("serviceCommodity51");
		Commodity comm51 = getServiceCommodity(c51.getName(), c51.getBarcodes());
		comm51.setID(c51.getID());
		comm51.setMultiPackagingInfo(comm51.getBarcodes() + ";" + comm51.getPackageUnitID() + ";" + comm51.getRefCommodityMultiple() + ";" //
				+ comm51.getPriceRetail() + ";" + comm51.getPriceVIP() + ";" + comm51.getPriceWholesale() + ";" + comm51.getName() + ";");
		Commodity commUpdate = updateCommodity(c51, comm51);
		// 删除商品51
		deleteCommodity(commUpdate, dbNameOfNewCompany1);
		CommodityListMap.remove("serviceCommodity51");
		// 查询商品51
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), commUpdate.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(commJSONArray.size() == 0, "测试失败！期望的是返回空");
	}

	@Test(dependsOnMethods = "updateAndDeleteServiceCommodityTest2")
	public void updateAndRetrieveNServiceCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改一个服务类商品56的商品分类后，再根据商品分类查询");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 修改服务商品56
		Commodity c56 = (Commodity) CommodityListMap.get("serviceCommodity56");
		Commodity comm56 = getServiceCommodity(c56.getName(), c56.getBarcodes());
		comm56.setID(c56.getID());
		comm56.setCategoryID(2);
		comm56.setMultiPackagingInfo(comm56.getBarcodes() + ";" + comm56.getPackageUnitID() + ";" + comm56.getRefCommodityMultiple() + ";" //
				+ comm56.getPriceRetail() + ";" + comm56.getPriceVIP() + ";" + comm56.getPriceWholesale() + ";" + comm56.getName() + ";");
		Commodity commUpdate56 = updateCommodity(c56, comm56);
		commUpdate56.setBarcodes(comm56.getBarcodes());
		CommodityListMap.replace("serviceCommodity56", commUpdate56);
		// 使用修改前的商品分类查询服务商品56
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_categoryID(), String.valueOf(c56.getCategoryID()))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), commUpdate56.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < commJSONArray.size(); i++) {
			JSONObject json = commJSONArray.getJSONObject(i);
			Commodity commodity = (Commodity) new Commodity().parse1(json.getString("commodity"));
			if (commUpdate56.compareTo(commodity) == 0) {
				Assert.assertTrue(false, "测试失败！期望的是返回的数据不包含商品：" + commUpdate56.getName());
			}
		}
		// 使用修改后的商品分类查询服务商品56
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_categoryID(), String.valueOf(commUpdate56.getCategoryID()))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), commUpdate56.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 结果验证
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		JSONArray commJSONArray2 = jsonObject2.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json2 = commJSONArray2.getJSONObject(0);
		Commodity commodity2 = (Commodity) new Commodity().parse1(json2.getString("commodity"));
		Assert.assertTrue(commodity2 != null, "解析commodity失败！");
		//
		commUpdate56.setIgnoreIDInComparision(true);
		if (commUpdate56.compareTo(commodity2) != 0) {
			Assert.assertTrue(false, "测试失败！期望的是能查询到商品");
		}
	}

	@Test(dependsOnMethods = "updateAndRetrieveNServiceCommodity")
	public void updateServiceCommodityTest3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "修改服务类商品56信息时，翻页后，再点击保存");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 查询第二页商品信息
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_pageSize(), String.valueOf(10))//
						.param(Commodity.field.getFIELD_NAME_pageIndex(), String.valueOf(2))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 修改服务商品56
		Commodity c56 = (Commodity) CommodityListMap.get("serviceCommodity56");
		Commodity comm56 = getServiceCommodity("修改" + c56.getName(), c56.getBarcodes());
		comm56.setID(c56.getID());
		comm56.setMultiPackagingInfo(comm56.getBarcodes() + ";" + comm56.getPackageUnitID() + ";" + comm56.getRefCommodityMultiple() + ";" //
				+ comm56.getPriceRetail() + ";" + comm56.getPriceVIP() + ";" + comm56.getPriceWholesale() + ";" + comm56.getName() + ";");
		Commodity commUpdate56 = updateCommodity(c56, comm56);
		CommodityListMap.replace("serviceCommodity56", commUpdate56);
	}

	@Test(dependsOnMethods = "updateServiceCommodityTest3")
	public void deleteServiceCommodityTest3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "删除一个服务类商品53后再搜索该商品");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 删除服务商品53
		Commodity c53 = (Commodity) CommodityListMap.get("serviceCommodity53");
		deleteCommodity(c53, dbNameOfNewCompany1);
		CommodityListMap.remove("serviceCommodity53");
		// 查询服务商品53
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c53.getName())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(commJSONArray.size() == 0, "测试失败！期望的是返回空");
	}

	@Test(dependsOnMethods = "deleteServiceCommodityTest3")
	public void deleteAndCreateServiceCommodityTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityList_", order, "删除一个服务类商品64后再创建一个同名的商品66");

		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhoneOfNewCompany1, bossPasswordOfNewCompany1, companySNOfNewCompany1);
		// 删除服务商品64
		Commodity c64 = (Commodity) CommodityListMap.get("serviceCommodity64");
		deleteCommodity(c64, dbNameOfNewCompany1);
		CommodityListMap.remove("serviceCommodity64");
		// 使用服务商品64的名称创建服务商品66
		Commodity comm66 = getServiceCommodity(c64.getName(), String.valueOf(getBarcode()));
		BaseCommodityTest.createCommodityViaAction(comm66, mvc, sessionBoss, mapBO, dbNameOfNewCompany1);
	}

	protected void retrieveNExCommodity(Commodity c) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), c.getQueryKeyword())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray commJSONArray = jsonObject.getJSONArray(BaseAction.KEY_ObjectList);
		Commodity commodity = new Commodity();
		List<Commodity> commList = new ArrayList<Commodity>();
		for (int i = 0; i < commJSONArray.size(); i++) {
			JSONObject json = commJSONArray.getJSONObject(i);
			commodity = (Commodity) commodity.parse1(json.getString("commodity"));
			Assert.assertTrue(commodity != null, "解析commodity失败！");
			commList.add(commodity);
		}
		//
		Assert.assertTrue(commList.size() != 0, "查询出来的数据不正常");
		//
		for (Object object : commList) {
			Commodity commodity1 = (Commodity) object;
			Assert.assertTrue(commodity1.getName().contains("_"), "查询出来的数据不正常");
		}
	}

	private void deleteCommodity(Commodity commodity, String dbName) throws Exception, UnsupportedEncodingException {
		// commodity.setInt1(0); //...暂时未知这个int1是何作用
		// 把commodity的barcode查出来做结果验证
		List<Barcodes> listBarcodes = getListBarcodes(commodity);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		//
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID() + "&" + Commodity.field.getFIELD_NAME_type() + "=" + commodity.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		CommodityCP.verifyDelete(commodity, listBarcodes, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, dbName);

	}

	private Commodity updateCommodity(Commodity commodity, Commodity cUpdate) throws Exception, UnsupportedEncodingException {
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbNameOfNewCompany1, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(cUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbNameOfNewCompany1, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> commList = getMultioleCommodityList(commodity, dbNameOfNewCompany1);
		MvcResult mr3 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3);
		CommodityCP.verifyUpdate(mr3, cUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, commList, dbNameOfNewCompany1, null);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Commodity commodityUpdate = (Commodity) new Commodity().parse1(jsonObject.getString("object"));
		return commodityUpdate;
	}

	private List<Barcodes> getListBarcodes(Commodity commodity) throws Exception, UnsupportedEncodingException {
		MvcResult mr12 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12);
		JSONObject jsonObject = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		List<?> Barcodes = JsonPath.read(jsonObject, "$.barcodesList[*]");
		List<Barcodes> listBarcodes = new ArrayList<Barcodes>();
		Barcodes barcodes = new Barcodes();
		for (int i = 0; i < Barcodes.size(); i++) {
			barcodes = (Barcodes) barcodes.parse1(Barcodes.get(i).toString());
			listBarcodes.add(barcodes);
		}
		return listBarcodes;
	}

	@SuppressWarnings("unchecked")
	private List<Commodity> getMultioleCommodityList(Commodity commodity, String dbName) {
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		return listMultiPackageCommodity;
	}

	private Commodity getServiceCommodity(String commodityName, String barcodes) throws CloneNotSupportedException, InterruptedException {
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_Service.getIndex());
		comm.setName(commodityName);
		comm.setSpecification("个");
		comm.setPurchasingUnit("");
		comm.setShelfLife(0);
		comm.setPurchaseFlag(0);
		comm.setBarcodes(barcodes);
		comm.setMultiPackagingInfo(
				comm.getBarcodes() + ";" + comm.getPackageUnitID() + ";" + comm.getRefCommodityMultiple() + ";" + comm.getPriceRetail() + ";" + comm.getPriceVIP() + ";" + comm.getPriceWholesale() + ";" + comm.getName() + ";");
		return comm;
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

}
