package com.bx.erp.sit.sit1.sg.cms.cmsShop;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class CMSShopListTset extends BaseActionTest {
	protected AtomicInteger order;
	protected AtomicLong atSubmchid;

	private Map<String, BaseModel> companyListMap;

	private final static int DefaultShopID = 1;
	private static String company6_SN = "";
	private static String company6_Pos_SN = "";
	private static int company6_Pos_ID;
	private static String company8_SN = "";
	private static String company8_Pos_SN = "";
	private static int company8_Pos_ID;
	private static String company9_SN = "";

	@BeforeClass
	public void setup() {
		super.setUp();

		order = new AtomicInteger();
		atSubmchid = new AtomicLong();
		atSubmchid.set(System.currentTimeMillis() % 10000000000l);

		companyListMap = new HashMap<String, BaseModel>();

		try {
			sessionBoss = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createCompanyTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司并通过条件与关键字进行查询");
		// SIT1_nbr_SG_cmsShopList_1

		// 创建公司1
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		companyListMap.put("company1", company1);
		// ...页面上查询公司的功能暂未实现

	}

	@Test(dependsOnMethods = "createCompanyTest")
	public void createCompanyAndUpdateTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后修改公司的信息并保存");
		// SIT1_nbr_SG_cmsShopList_2

		// 创建公司2
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setSubmchid(String.valueOf(atSubmchid));
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		// 修改公司2信息
		company.setID(company1.getID());
		company.setName(Shared.generateCompanyName(Company.MAX_LENGTH_Name));
		company.setBossName(Shared.generateCompanyName(Company.MAX_LENGTH_Name));
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
	}

	@Test(dependsOnMethods = "createCompanyAndUpdateTest")
	public void cancelUpdteCompanyTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后修改公司的信息后取消");
		// SIT1_nbr_SG_cmsShopList_3

		// 创建公司3
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		// 修改公司3信息并取消是页面操作，并没有调用接口
	}

	@Test(dependsOnMethods = "cancelUpdteCompanyTest")
	public void updateShopTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后修改门店的信息并保存");
		// SIT1_nbr_SG_cmsShopList_4

		// 创建公司4
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		// 页面上的修改门店信息功能暂未实现
	}

	@Test(dependsOnMethods = "updateShopTest")
	public void cancelUpdteShopTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后修改门店的信息后取消");
		// SIT1_nbr_SG_cmsShopList_5

		// 创建公司5
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		// ...修改门店信息后取消是页面操作，并没有调用接口
		// 页面上的修改门店信息功能暂未实现
	}

	@Test(dependsOnMethods = "cancelUpdteShopTest")
	public void createPOSTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后添加POS机并保存");
		// SIT1_nbr_SG_cmsShopList_6

		// 创建公司6
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		company6_SN = company1.getSN();
		// 添加POS机1
		company6_Pos_SN = "LB11122" + System.currentTimeMillis() % 100000000;
		Pos pos = DataInput.getPOS();
		pos.setPos_SN(company6_Pos_SN);
		pos.setCompanySN(company6_SN);
		pos.setShopID(DefaultShopID);
		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		company6_Pos_ID = JsonPath.read(o1, "$.object.ID");
		// ...检查点
	}

	@Test(dependsOnMethods = "createPOSTest")
	public void deletePOSTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后添加POS机并保存之后删除pos 机");
		// SIT1_nbr_SG_cmsShopList_7

		// 创建公司7
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		// 添加POS机2
		Pos pos = DataInput.getPOS();
		pos.setOperationType(1);
		pos.setPos_SN("DP111222" + System.currentTimeMillis() % 100000000);
		pos.setCompanySN(company1.getSN());
		pos.setShopID(DefaultShopID);
		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		int ID = JsonPath.read(o1, "$.object.ID");
		// 检查点

		// 删除新建的pos机2
		MvcResult mr2 = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + ID + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + company1.getSN()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "deletePOSTest")
	public void cancelDeletePOSTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "创建一个新的公司后，添加POS机，然后删除该POS机时取消");
		// SIT1_nbr_SG_cmsShopList_8

		// 创建公司8
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		company8_SN = company1.getSN();

		// 添加POS机3
		company8_Pos_SN = "mlxg111222" + System.currentTimeMillis() % 100000000;
		Pos pos = DataInput.getPOS();
		pos.setOperationType(1);
		pos.setPos_SN(company8_Pos_SN);
		pos.setCompanySN(company8_SN);
		pos.setShopID(DefaultShopID);
		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		company8_Pos_ID = JsonPath.read(o1, "$.object.ID");

		// 点击删除pos机后，会有一个弹窗，再点击取消删除。主要是页面操作并没有调用接口
	}

	@Test(dependsOnMethods = "cancelDeletePOSTest")
	public void retrieveNByFieldsCompanyTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "修改一个公司的信息后通过关键字或条件进行查询");
		// SIT1_nbr_SG_cmsShopList_9

		// 创建公司9
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		company9_SN = company1.getSN();
		// 修改公司9信息
		company.setID(company1.getID());
		company.setName(Shared.generateCompanyName(Company.MAX_LENGTH_Name));
		company.setBossName(Shared.generateCompanyName(Company.MAX_LENGTH_Name));
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);

		// ...页面上查询公司的功能暂未实现
	}

	@Test(dependsOnMethods = "retrieveNByFieldsCompanyTest")
	public void retrieveNByFieldsShopTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "修改一个门店的信息后通过关键字或条件进行查询");
		// SIT1_nbr_SG_cmsShopList_10

		// 创建公司10
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);

		// ...页面上修改门店的功能暂未实现
	}

	@Test(dependsOnMethods = "retrieveNByFieldsShopTest")
	public void retrieveNByFieldsPosTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "修改一个门店的POS机后通过关键字或条件进行查询");
		// SIT1_nbr_SG_cmsShopList_11

		// 创建公司11
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		// 添加POS机4
		Pos pos = DataInput.getPOS();
		pos.setOperationType(1);
		pos.setPos_SN("zs111222" + System.currentTimeMillis() % 100000000);
		pos.setCompanySN(company1.getSN());
		pos.setShopID(DefaultShopID);
		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		int ID = JsonPath.read(o1, "$.object.ID");
		// ...检查点

		// ...查询pos机的功能暂未实现

		// 删除pos机4
		MvcResult mr2 = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + ID + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + company1.getSN()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr2);
		// 检查点
		// ...查询pos机的功能暂未实现
	}

	@Test(dependsOnMethods = "retrieveNByFieldsPosTest")
	public void retrieveNTest() throws Exception { // java无法实现
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");
		// SIT1_nbr_SG_cmsShopList_12
		// ...ActionTest

		// ...结果验证
	}

	@Test(dependsOnMethods = "retrieveNTest")
	public void curdTest() throws Exception { // java层无法实现
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");
		// SIT1_nbr_SG_cmsShopList_13
		// ...ActionTest

		// ...结果验证
	}

	@Test(dependsOnMethods = "curdTest")
	public void deletePosAndaddTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "删除一个POS机后重新添加该POS机");
		// SIT1_nbr_SG_cmsShopList_14

		// 删除公司6的pos机后
		MvcResult mr = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + company6_Pos_ID + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + company6_SN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		// 公司6再添加该(相同的SN码的pos机)pos机
		Pos pos = DataInput.getPOS();
		pos.setOperationType(1);
		pos.setPos_SN(company6_Pos_SN);
		pos.setCompanySN(company6_SN);
		pos.setShopID(DefaultShopID);
		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		company6_Pos_ID = JsonPath.read(o1, "$.object.ID");
	}

	@Test(dependsOnMethods = "deletePosAndaddTest")
	public void deletePosAndaddTest2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "删除一个POS机后添加该POS机至其它公司");
		// SIT1_nbr_SG_cmsShopList_15

		// 删除公司6的pos机后
		MvcResult mr = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + company6_Pos_ID + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + company6_SN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		// 把公司6删除的pos机添加到公司9
		Pos pos = DataInput.getPOS();
		pos.setOperationType(1);
		pos.setPos_SN(company6_Pos_SN);
		pos.setCompanySN(company9_SN);
		pos.setShopID(DefaultShopID);
		MvcResult mr1 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
	}

	@Test(dependsOnMethods = "deletePosAndaddTest2")
	public void deletePosAndaddTest3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "删除一个POS机后创建一个新公司添加该POS机");
		// SIT1_nbr_SG_cmsShopList_15

		// 删除公司8的pos机后
		MvcResult mr = mvc.perform(get("/posSync/deleteEx.bx?" + Pos.field.getFIELD_NAME_ID() + "=" + company8_Pos_ID + "&" + Pos.field.getFIELD_NAME_companySN() + "=" + company8_SN) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		// 创建公司12
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company company1 = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		// 把公司8删除的pos机添加到公司12
		Pos pos = DataInput.getPOS();
		pos.setOperationType(1);
		pos.setPos_SN(company8_Pos_SN);
		pos.setCompanySN(company1.getSN());
		pos.setShopID(DefaultShopID);
		MvcResult mr2 = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "deletePosAndaddTest3")
	public void bxStaffTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "售前人员无法进行任何的CRUD操作");
		// SIT1_nbr_SG_cmsShopList_16

		// 暂时不写
	}

	@Test(dependsOnMethods = "bxStaffTest")
	public void repeatCreate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "重复添加子商户号");
		// SIT1_nbr_SG_cmsShopList_17

		// 创建公司失败
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setSubmchid(String.valueOf(atSubmchid));
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionBoss) //
				.session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
		Shared.checkJSONMsg(mr, "其它公司已存在这个子商户号", "其它公司已存在这个子商户号");
	}

	@Test(dependsOnMethods = "repeatCreate")
	public void updateToRepeat() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_cmsShopList_", order, "修改成别的公司存在的子商户号");
		// SIT1_nbr_SG_cmsShopList_18

		Company company = (Company) companyListMap.get("company1");
		company.setSubmchid(String.valueOf(atSubmchid));
		sessionBoss = (MockHttpSession) BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType).getRequest().getSession();

		MvcResult mr = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
		Shared.checkJSONMsg(mr, "其它公司已存在这个子商户号", "其它公司已存在这个子商户号");
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

}
