package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.Company;
import com.bx.erp.model.Staff;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.test.checkPoint.CheckPoint;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CacheManagerTest extends BaseActionTest {
	// 为了不和数据库中的商品ID混淆，故此设置一个较大的数
	protected static int commodityID = 100000000;
	
	@Resource
	private CommoditySyncAction csa;

	private static List<List<BaseModel>> oldCacheTypeList = new ArrayList<List<BaseModel>>();
	private static List<List<BaseModel>> newCacheTypeList = new ArrayList<List<BaseModel>>();

	EnumCacheType ect = EnumCacheType.ECT_Commodity;
	EnumSyncCacheType esct = EnumSyncCacheType.ESCT_CommoditySyncInfo;

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();

		try {
			sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	public static class DataInput {
		private static Staff staffInput;

		protected static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType) {
			MockHttpServletRequestBuilder builder = post(url) //
					.session((MockHttpSession) sessionBoss) //
					.contentType(contentType)//
					.param(Commodity.field.getFIELD_NAME_status(), "0") //
					// .param(Commodity.field.getFIELD_NAME_name(), "焦糖瓜子" +
					// String.valueOf(System.currentTimeMillis()).substring(6)) //
					// .param(Commodity.field.getFIELD_NAME_shortName(), "瓜子") //
					.param(Commodity.field.getFIELD_NAME_specification(), "克") //
					// .param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
					.param(Commodity.field.getFIELD_NAME_purchasingUnit(), "箱") //
					// .param(Commodity.field.getFIELD_NAME_brandID(), "3") //
					// .param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
					.param(Commodity.field.getFIELD_NAME_mnemonicCode(), "SP") //
					.param(Commodity.field.getFIELD_NAME_pricingType(), "1") //
					.param(Commodity.field.getFIELD_NAME_priceVIP(), "0.8") //
					.param(Commodity.field.getFIELD_NAME_priceWholesale(), "0.8") //
					// .param(Commodity.field.getFIELD_NAME_ratioGrossMargin(), "0.8") //
					.param(Commodity.field.getFIELD_NAME_canChangePrice(), "1") //
					.param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
					.param(Commodity.field.getFIELD_NAME_picture(), "url=116843435555") //
					.param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
					.param(Commodity.field.getFIELD_NAME_returnDays(), "1") //
					.param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
					.param(Commodity.field.getFIELD_NAME_refCommodityID(), "0") //
					.param(Commodity.field.getFIELD_NAME_refCommodityMultiple(), "0") //
					// .param(Commodity.field.getFIELD_NAME_isGift(), "0") //
					.param(Commodity.field.getFIELD_NAME_tag(), "1") //
					.param(Commodity.field.getFIELD_NAME_NO(), "2421") //
					// .param(Commodity.field.getFIELD_NAME_NOAccumulated(), "1") //
					.param(Commodity.field.getFIELD_NAME_priceRetail(), "1").param(Commodity.field.getFIELD_NAME_nOStart(), String.valueOf(Commodity.NO_START_Default)) //
					.param(Commodity.field.getFIELD_NAME_purchasingPriceStart(), String.valueOf(Commodity.PURCHASING_PRICE_START_Default)) //
			;

			return builder;
		}

		protected static final Staff getStaff() throws Exception {
			staffInput = new Staff();
			staffInput.setPhone(Shared.getValidStaffPhone());
			Thread.sleep(100);
			staffInput.setName("店员" + Shared.generateCompanyName(6));//
			Thread.sleep(100);
			staffInput.setICID(Shared.getValidICID()); //
			Thread.sleep(100);
			staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);//
			staffInput.setPasswordExpireDate(sdf.parse("2018/02/15 12:30:45"));//
			staffInput.setSalt("123456");
			staffInput.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex()); //
			staffInput.setShopID(1);//
			staffInput.setDepartmentID(1);//
			// staffInput.setInt1(1);
			// staffInput.setInt2(1);
			staffInput.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());//

			return (Staff) staffInput.clone();
		}
	}

	private void CreateCommodity(String iPhone, String SN, String password) throws Exception {
		MvcResult req = uploadPicture(iPhone, SN, password);

		String commodityA = "可乐薯片南瓜" + System.currentTimeMillis() % 1000000;
		Thread.sleep(100);
		String commodityB = "可乐薯片南瓜" + System.currentTimeMillis() % 1000000;
		Thread.sleep(100);
		String barcode1 = "233" + System.currentTimeMillis() % 1000000;
		Thread.sleep(100);
		String barcode2 = "233" + System.currentTimeMillis() % 1000000;
		Thread.sleep(100);
		String commodityShortName = "薯片" + System.currentTimeMillis() % 1000000;
		//
		MvcResult mrl = mvc.perform(DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON)//
				.param(Commodity.field.getFIELD_NAME_multiPackagingInfo(), barcode1 + "," + barcode2 + ";1,2;1,5;1,5;8,3;8,8;" //
						+ commodityA + System.currentTimeMillis() % 1000000 + "," + commodityB + System.currentTimeMillis() % 1000000 + ";") //
				.param(Commodity.field.getFIELD_NAME_name(), "薯片" + System.currentTimeMillis() % 1000000)//
				.param(Commodity.field.getFIELD_NAME_shortName(), commodityShortName).param("providerIDs", "1,1") //
				.param(Commodity.field.getFIELD_NAME_propertyValue1(), "自定义内容1")//
				.param(Commodity.field.getFIELD_NAME_propertyValue2(), "自定义内容2")//
				.param(Commodity.field.getFIELD_NAME_propertyValue3(), "自定义内容3")//
				.param(Commodity.field.getFIELD_NAME_propertyValue4(), "自定义内容4")//
				.param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
				.param(Commodity.field.getFIELD_NAME_brandID(), "1")//
				.param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
				.param(Commodity.field.getFIELD_NAME_type(), "0") //
				.param(Commodity.field.getFIELD_NAME_operatorStaffID(), "1") //
				.param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
				.param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
				.param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
				.session((MockHttpSession) req.getRequest().getSession())//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl);
	}

	private MvcResult uploadPicture(String iPhone, String SN, String password) throws FileNotFoundException, IOException, Exception {
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";

		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/png", fis);

		Shared.caseLog(" case1:商品创建时上传图片");
		MvcResult mr1 = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) getStaffLoginSession(mvc, iPhone, SN, password)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);

		Assert.assertNotNull(mr1.getRequest().getSession().getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName()));
		Assert.assertNotNull(mr1.getRequest().getSession().getAttribute(EnumSession.SESSION_PictureFILE.getName()), "图片文件不存在！");
		return mr1;
	}

	private void CreateStaff(String bossPhone, String sn, String password) throws UnsupportedEncodingException, Exception {
		HttpSession session = getStaffLoginSession(mvc, bossPhone, sn, password);
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = getToken(session, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = DataInput.getStaff();
		staff.setRoleID(1);
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mr = mvc.perform(//
				post("/staff/createEx.bx") //
						.param(Staff.field.getFIELD_NAME_ID(), String.valueOf(staff.getID())) //
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone()) //
						.param(Staff.field.getFIELD_NAME_name(), String.valueOf(staff.getName()))//
						.param(Staff.field.getFIELD_NAME_ICID(), staff.getICID())//
						.param(Staff.field.getFIELD_NAME_weChat(), staff.getWeChat())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), staff.getPwdEncrypted())//
						.param(Staff.field.getFIELD_NAME_passwordExpireDate(), String.valueOf(staff.getPasswordExpireDate()))//
						.param(Staff.field.getFIELD_NAME_isFirstTimeLogin(), String.valueOf(staff.getIsFirstTimeLogin()))//
						.param(Staff.field.getFIELD_NAME_shopID(), String.valueOf(staff.getShopID()))//
						.param(Staff.field.getFIELD_NAME_departmentID(), String.valueOf(staff.getDepartmentID()))//
						.param(Staff.field.getFIELD_NAME_roleID(), String.valueOf(staff.getRoleID()))//
						.param(Staff.field.getFIELD_NAME_returnSalt(), String.valueOf(staff.getReturnSalt()))//
						.param(Staff.field.getFIELD_NAME_status(), String.valueOf(staff.getStatus()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);

	}

	@SuppressWarnings("unused")
	private MvcResult getToken(String Staff_Phone) throws Exception, UnsupportedEncodingException {
		MvcResult ret = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(ret);
		return ret;
	}

	private MvcResult getToken(HttpSession session, String phone, int forModifyPassword) throws Exception {
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session) //
				.param(Staff.field.getFIELD_NAME_phone(), phone)//
				.param(Staff.field.getFIELD_NAME_forModifyPassword(), String.valueOf(forModifyPassword))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(ret);

		return ret;
	}

	private HttpSession getStaffLoginSession(MockMvc mvc, String iPhone, String SN, String password) throws Exception {
		MvcResult result = (MvcResult) mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), iPhone)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andReturn();

		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, password);

		MvcResult result1 = (MvcResult) mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), iPhone)//
						.param(Staff.field.getFIELD_NAME_salt(), "")//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_companySN(), SN)//
						.session((MockHttpSession) result.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andReturn();//
		Shared.checkJSONErrorCode(result1);

		return result1.getRequest().getSession();
	}

	private void cloneOldCache() {
		for (EnumCacheType cacheType : EnumCacheType.values()) {
			// 由于nbr没有这四个缓存，所以跳过
			if (cacheType == EnumCacheType.ECT_BxStaff || cacheType == EnumCacheType.ECT_BXConfigGeneral || cacheType == EnumCacheType.ECT_Company) {
				continue;
			}

			oldCacheTypeList.add((List<BaseModel>) CacheManager.getCache(Shared.DBName_Test, cacheType).readN(false, false));
		}
	}

	private boolean compare() {
		for (EnumCacheType cacheType : EnumCacheType.values()) {
			// 由于nbr没有这三个缓存，所以跳过
			if (cacheType == EnumCacheType.ECT_BxStaff || cacheType == EnumCacheType.ECT_BXConfigGeneral || cacheType == EnumCacheType.ECT_Company) {
				continue;
			}
			// System.out.println(cacheType);
			newCacheTypeList.add((List<BaseModel>) CacheManager.getCache(Shared.DBName_Test, cacheType).readN(false, false));
		}
		for (int i = 0; i < oldCacheTypeList.size(); i++) {
			Assert.assertTrue(oldCacheTypeList.get(i).size() == newCacheTypeList.get(i).size(), "oldCacheTypeList：" + oldCacheTypeList.get(i) + ", \r\n newCacheTypeList:" + newCacheTypeList.get(i));
			for (int j = 0; j < oldCacheTypeList.get(i).size(); j++) {
				if (oldCacheTypeList.get(i).get(j).getID() != newCacheTypeList.get(i).get(j).getID()) {
					return false;
				}
			}
		}
		return true;
	}

	private RetailTrade getRetailTrade() throws InterruptedException {
		Random ran = new Random();

		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(5));
		retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
		Thread.sleep(1000);
		retailTrade.setVipID(1);
		retailTrade.setPos_ID(5);
		retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTrade.setSaleDatetime(new Date());
		retailTrade.setLogo("11" + ran.nextInt(1000));
		retailTrade.setStaffID(1);
		retailTrade.setPaymentType(1);
		retailTrade.setPaymentAccount("12");
		retailTrade.setRemark("11111");
		retailTrade.setSourceID(-1);
		retailTrade.setAmount(2222.200000f);
		retailTrade.setAmountCash(2222.200000f);
		retailTrade.setSmallSheetID(ran.nextInt(7) + 1);
		retailTrade.setSyncDatetime(new Date());
		retailTrade.setSaleDatetime(new Date());
		retailTrade.setReturnObject(1);// 测试不通过
		retailTrade.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setWxRefundNO(String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1000);

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(1);
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setPriceOriginal(222.6);
		retailTradeCommodity.setBarcodeID(1);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setPriceReturn(0.5d);
		retailTradeCommodity.setPriceVIPOriginal(1d);

		RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(2);
		retailTradeCommodity2.setNO(10);
		retailTradeCommodity2.setPriceOriginal(222.6);
		retailTradeCommodity2.setBarcodeID(1);
		retailTradeCommodity2.setNOCanReturn(10);
		retailTradeCommodity2.setPriceReturn(0.5d);
		retailTradeCommodity2.setPriceVIPOriginal(1d);

		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		listRetailTradeCommodity.add(retailTradeCommodity2);

		retailTrade.setListSlave1(listRetailTradeCommodity);

		return retailTrade;
	}

	@Test
	public void testCache() throws Exception {
		Shared.printTestMethodStartInfo();

		// 1、生成nbr公司的商品的同存。
		CreateCommodity(Shared.PhoneOfBoss, Shared.DB_SN_Test, Shared.PASSWORD_DEFAULT);
		// 2、clone nbr的所有普通缓存和同步缓存，命名为旧缓存。
		cloneOldCache();
		// 3、创建一个新公司。老板首次登录并修改密码，修改后的密码
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company createCompany = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		// 4、比较缓存和旧缓存是否相同。
		Assert.assertTrue(compare());
		// 5、增加员工1和多包装商品1。
		CreateStaff(createCompany.getBossPhone(), createCompany.getSN(), BaseCompanyTest.bossPassword_New);
		CreateCommodity(createCompany.getBossPhone(), createCompany.getSN(), BaseCompanyTest.bossPassword_New);
		// 6、重做第4步。
		Assert.assertTrue(compare());
	}

	/** 测试一个对象中包含Double类型字段，写入缓存后read1出来，看对象是否相等
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCache2() throws Exception {
		Shared.printTestMethodStartInfo();

		RetailTrade rt = getRetailTrade();
		String json = JSONObject.fromObject(rt, JsonUtil.jsonConfig).toString();
		// 创建一个零售单对象
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", json) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		JSONObject json1 = JSONObject.fromObject(mr.getResponse().getContentAsString());

		RetailTrade bmOfDB = new RetailTrade();
		bmOfDB = (RetailTrade) bmOfDB.parse1(json1.getString(BaseAction.KEY_Object));
		bmOfDB.setSyncDatetime(new Date());
	}

	@Test
	public void testDeleteCache1() throws Exception {
		// 删除单品缓存。普通商品缓存删除成功
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		comm.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(comm, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(comm);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, comm, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache2() throws Exception {
		// 删除多包装商品B缓存。参照商品为普通商品A，商品A,B都不存在缓存中
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		comm.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(comm, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity multiPackagingComm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		multiPackagingComm.setID(++commodityID);
		multiPackagingComm.setRefCommodityID(comm.getID());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(multiPackagingComm, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(multiPackagingComm);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, comm, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(false, multiPackagingComm, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache3() throws Exception {
		// 删除组合商品A缓存，子商品为普通商品B,普通商品C。商品A,B,C都不存在缓存中
		Commodity commA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commA.setID(++commodityID);
		//
		Commodity commB = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commB.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commB, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity commC = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commC.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commC, Shared.DBName_Test, Shared.BigStaffID);
		//
		List<SubCommodity> subCommList = new ArrayList<>();
		SubCommodity subCommodityA = new SubCommodity();
		subCommodityA.setCommodityID(commA.getID());
		subCommodityA.setSubCommodityID(commB.getID());
		subCommList.add(subCommodityA);

		SubCommodity subCommodityB = new SubCommodity();
		subCommodityB.setCommodityID(commA.getID());
		subCommodityB.setSubCommodityID(commC.getID());
		subCommList.add(subCommodityB);
		commA.setListSlave1(subCommList);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commA, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commA);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(false, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(false, commC, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache4() throws Exception {
		// 删除服务商品A后，缓存中不存在服务商品A
		Commodity commA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commA.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commA, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commA);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache5() throws Exception {
		// 删除普通商品A，缓存中普通商品A不存在，原先包含普通商品A为子商品的组合商品还在缓存中
		Commodity commA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commA.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commA, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity commB = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commB.setID(++commodityID);
		List<SubCommodity> subCommList = new ArrayList<>();
		SubCommodity subCommodityA = new SubCommodity();
		subCommodityA.setCommodityID(commB.getID());
		subCommodityA.setSubCommodityID(commA.getID());
		subCommList.add(subCommodityA);
		commB.setListSlave1(subCommList);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commB, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commA);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(true, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		// 不将测试数据写入缓存中，测试完后将其删除
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commB);
		CheckPoint.verifyFromCacheIfCacheExists(false, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache6() throws Exception {
		// 删除普通商品A，缓存中普通商品A不存在，原先参照普通商品A的多包装商品还在缓存中。
		Commodity commA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commA.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commA, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity commB = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commB.setID(++commodityID);
		commB.setRefCommodityID(commA.getID());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commB, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commA);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(true, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		// 不将测试数据写入缓存中，测试完后将其删除
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commB);
		CheckPoint.verifyFromCacheIfCacheExists(false, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache7() throws Exception {
		// 删除组合商品A的缓存，其子商品B,C的缓存也删除，但是以参照商品B的多包装商品D缓存未被删除
		Commodity commA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commA.setID(++commodityID);
		//
		Commodity commB = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commB.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commB, Shared.DBName_Test, Shared.BigStaffID);
		Commodity commC = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commC.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commC, Shared.DBName_Test, Shared.BigStaffID);
		//
		List<SubCommodity> subCommList = new ArrayList<>();
		SubCommodity subCommodityA = new SubCommodity();
		subCommodityA.setCommodityID(commA.getID());
		subCommodityA.setSubCommodityID(commB.getID());
		subCommList.add(subCommodityA);

		SubCommodity subCommodityB = new SubCommodity();
		subCommodityB.setCommodityID(commA.getID());
		subCommodityB.setSubCommodityID(commC.getID());
		subCommList.add(subCommodityB);
		commA.setListSlave1(subCommList);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commA, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity commD = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commD.setID(++commodityID);
		commD.setRefCommodityID(commB.getID());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commD, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commA);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(false, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(false, commC, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(true, commD, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		// 不将测试数据写入缓存中，测试完后将其删除
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commD);
		CheckPoint.verifyFromCacheIfCacheExists(false, commD, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}

	@Test
	public void testDeleteCache8() throws Exception {
		// 删除多包装商品A的缓存,其参照商品B缓存也删除，但包含子商品B的组合商品缓存未被删除
		Commodity commB = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commB.setID(++commodityID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commB, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity multiPackagingCommA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		multiPackagingCommA.setID(++commodityID);
		multiPackagingCommA.setRefCommodityID(commB.getID());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(multiPackagingCommA, Shared.DBName_Test, Shared.BigStaffID);
		//
		Commodity commA = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commA.setID(++commodityID);
		//
		List<SubCommodity> subCommList = new ArrayList<SubCommodity>();
		SubCommodity subComm = new SubCommodity();
		subComm.setCommodityID(commA.getID());
		subComm.setSubCommodityID(commB.getID());
		subCommList.add(subComm);
		commA.setListSlave1(subCommList);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commA, Shared.DBName_Test, Shared.BigStaffID);
		//
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(multiPackagingCommA);
		//
		CheckPoint.verifyFromCacheIfCacheExists(false, multiPackagingCommA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(false, commB, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		CheckPoint.verifyFromCacheIfCacheExists(true, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
		// 不将测试数据写入缓存中，测试完后将其删除
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commA);
		CheckPoint.verifyFromCacheIfCacheExists(false, commA, EnumCacheType.ECT_Commodity, Shared.DBName_Test);
	}
}
