package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;


@WebAppConfiguration
public class SmallSheetFrameActionTest extends BaseActionTest {

	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\SmallSheetLogoCase.png";

	public static String textList = "[" + "{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}" 
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ ",{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":%s,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":%s,\"gravity\":17,\"size\":32.0}"
	+ "]";
	
	public static String defaultTextList = String.format(textList, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
	
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testload() {
		// 检查是否有默认小票格式的缓存是否存在
		int defalutSmallSheetID = 1;
		List<BaseModel> listBM = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_SmallSheet).readN(false, false);
		for (BaseModel baseModel : listBM) {
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			SmallSheetFrame cacheSize = (SmallSheetFrame) baseModel;
			if (cacheSize.getID() == defalutSmallSheetID) {
				Assert.assertTrue(cacheSize.getListSlave1().size() == 20);
			}
			String error = cacheSize.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建小票格式");

		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		ssf.setCreateDatetime(new Date());
		SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		List<BaseModel> list = BaseSmallSheetFrameTest.retrieveNViaMapper(ssf, Shared.DBName_Test);
		for (BaseModel bm : list) {
			SmallSheetFrame s = (SmallSheetFrame) bm;
			if (s.getLogo().equals(ssf.getLogo())) {
				assertTrue(true);
			}
		}
		// 清除数据（因为小票格式数目有上限），否则影响后面测试
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreated, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:创建超过小票格式上限的条数，创建失败，提示小票格式上限");
		List<BaseModel> listBm = BaseSmallSheetFrameTest.retrieveNViaAction(mvc, sessionBoss);
		Assert.assertTrue(listBm != null, "数据异常，小票格式为null");
		Assert.assertTrue(listBm.size() != 0, "数据异常，查询出的小票格式条数为0条");
		Assert.assertTrue(listBm.size() <= 10, "数据异常，查询出的小票格式条数大于上限条数10条");
		//
		List<Integer> listCreateSmallSheetID = new ArrayList<Integer>();
		for (int i = 0; i <= (10 - listBm.size()); i++) {
			SmallSheetFrame ssf = new SmallSheetFrame();
			ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
			ssf.setDelimiterToRepeat("-");
			ssf.setCreateDatetime(new Date());
			Thread.sleep(1000);
			// 结果验证：检查错误码
			if (i < (10 - listBm.size())) {
				SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
				listCreateSmallSheetID.add(ssfCreated.getID());
			} else {
				BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField, "创建的小票格式的总数已经到达上限，不允许再创建新的小票格式！");
			}
		}
		// 清除数据，否则影响后面测试
		for (Integer i : listCreateSmallSheetID) {
			SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
			ssf.setID(i);
			BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssf, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		}
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:创建超过小票格式图片大小上限的图片，创建失败，提示小票格式图片太大了");
		File file = new File(PATH);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[fis.available()];
		fis.read(data);
		String path = new String(Base64.encodeBase64(data));
		fis.close();
		//
		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo(path);
		ssf.setDelimiterToRepeat("-");
		ssf.setCreateDatetime(new Date());
		BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, "[{\"bold\":false,\"content\":\"博昕POS机收银系统\",\"frameId\":1,\"gravity\":17,\"size\":32.0},{\"bold\":false,\"content\":\"博昕POS机收银系统2\",\"frameId\":1,\"gravity\":17,\"size\":32.0}]", Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField, "Logo图片太大!");
	}

	@Test
	public void testCreateEx4() throws Exception { //
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:收银员没有创建小票格式的权限");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		BaseSmallSheetFrameTest.createViaAction(mvc, sessionCashier, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:传入的小票底部空行数小于0");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setCountOfBlankLineAtBottom(-1);
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:传入的小票底部空行数大于5");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setCountOfBlankLineAtBottom(6);
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6:分隔符为空串");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setCountOfBlankLineAtBottom(3);
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("");
		ssf.setCreateDatetime(new Date());
		SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		// 清除数据，否则影响后面测试
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreated, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7:分隔符为空格");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setCountOfBlankLineAtBottom(3);
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat(" ");
		ssf.setCreateDatetime(new Date());
		SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		// 清除数据，否则影响后面测试
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreated, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8:分隔符为中文的一(yi)");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setCountOfBlankLineAtBottom(3);
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("一");
		ssf.setCreateDatetime(new Date());
		SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		// 清除数据，否则影响后面测试
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreated, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:重复创建小票格式（相同创建时间），期望得到存在的那一条记录");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		ssf.setCreateDatetime(new Date());
		SmallSheetFrame ssfCreated = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		// 重复创建
		BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_Duplicated);
		// 清除数据（因为小票格式数目有上限），否则影响后面测试
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreated, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		BaseSmallSheetFrameTest.retrieveNViaAction(mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss));
		

		// System.out.println("-------------------case2:没有权限进行操作----------------------------------");
		// //... cjs 说现在所有人都有查询全部小票格式的权限，先进行注释
		// MvcResult mr2 = mvc.perform(get("/smallSheetFrame/retrieveNEx.bx") //
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfManager)) //
		// .contentType(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
		// //
		// ).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------case1:正常查询----------------------------------");
		BaseSmallSheetFrameTest.retrieve1ViaAction(mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss), 2, EnumErrorCode.EC_NoError);
		

		System.out.println("-------------------case2:没有权限进行操作----------------------------------");
		BaseSmallSheetFrameTest.retrieve1ViaAction(mvc, Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale), 2, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testDeleteEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case:正常删除小票格式");

		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试");
		ssf.setDelimiterToRepeat("-");
		ssf.setCreateDatetime(new Date());
		Thread.sleep(1000);
		SmallSheetFrame ssfCreate = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		//
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:删除默认的小票格式ID为1，删除失败");
		SmallSheetFrame ssfCreate = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssfCreate.setID(BaseAction.DEFAULT_SmallSheetID);
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreate, Shared.DBName_Test, EnumErrorCode.EC_OtherError);

	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:收银员没有删除小票格式的权限");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setDelimiterToRepeat("-");
		SmallSheetFrame ssfCreate = BaseSmallSheetFrameTest.createViaMapper(ssf, Shared.DBName_Test);
		//
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionCashier, ssfCreate, Shared.DBName_Test, EnumErrorCode.EC_NoPermission);
		// 避免数据混乱，用完删除
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssfCreate, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:更新字段，成功");
		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		ssf.setCreateDatetime(new Date());
		SmallSheetFrame smallSheetFrameINDB = BaseSmallSheetFrameTest.createViaAction(mvc, sessionBoss, ssf, defaultTextList, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		// 更新
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		int frameID = smallSheetFrameINDB.getID();
		ssf.setID(frameID);
		BaseSmallSheetFrameTest.updateViaAction(mvc, sessionBoss, ssf, String.format(textList, frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID,frameID), EnumErrorCode.EC_NoError);
		// 清除数据（因为小票格式数目有上限），否则影响后面测试
		BaseSmallSheetFrameTest.deleteViaAction(mvc, sessionBoss, ssf, Shared.DBName_Test, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case:收银员没有修改小票格式的权限");
		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		ssf.setID(4);
		BaseSmallSheetFrameTest.updateViaAction(mvc, sessionCashier, ssf, "[{\"bold\":false,\"content\":\"博昕\",\"frameId\":" + ssf.getID() + ",\"gravity\":17,\"size\":32.0,\"ID\":3},{\"bold\":false,\"content\":\"博昕POS\",\"frameId\":" + ssf.getID()
		+ ",\"gravity\":17,\"size\":32.0,\"ID\":4}]", EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:更新小票底部空行数小于0");
		SmallSheetFrame ssf = BaseSmallSheetFrameTest.DataInput.getSmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setDelimiterToRepeat("-");
		ssf.setCountOfBlankLineAtBottom(-1);
		ssf.setID(4);
		//
		BaseSmallSheetFrameTest.updateViaAction(mvc, sessionBoss, ssf, "[{\"bold\":false,\"content\":\"博昕\",\"frameId\":" + ssf.getID() + ",\"gravity\":17,\"size\":32.0,\"ID\":3},{\"bold\":false,\"content\":\"博昕POS\",\"frameId\":" + ssf.getID()
		+ ",\"gravity\":17,\"size\":32.0,\"ID\":4}]", EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:更新小票底部空行数大于5");
		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setCountOfBlankLineAtBottom(6);
		ssf.setDelimiterToRepeat("-");
		ssf.setID(4);
		//
		BaseSmallSheetFrameTest.updateViaAction(mvc, sessionBoss, ssf, "[{\"bold\":false,\"content\":\"博昕\",\"frameId\":" + ssf.getID() + ",\"gravity\":17,\"size\":32.0,\"ID\":3},{\"bold\":false,\"content\":\"博昕POS\",\"frameId\":" + ssf.getID()
		+ ",\"gravity\":17,\"size\":32.0,\"ID\":4}]", EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:传入不存在的ID");
		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setCountOfBlankLineAtBottom(2);
		ssf.setDelimiterToRepeat("-");
		ssf.setID(Shared.BIG_ID);
		//
		BaseSmallSheetFrameTest.updateViaAction(mvc, sessionBoss, ssf, "[{\"bold\":false,\"content\":\"博昕\",\"frameId\":" + ssf.getID() + ",\"gravity\":17,\"size\":32.0,\"ID\":3},{\"bold\":false,\"content\":\"博昕POS\",\"frameId\":" + ssf.getID()
		+ ",\"gravity\":17,\"size\":32.0,\"ID\":4}]", EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:上传到服务器的小票格式对象的主从表不匹配");
		SmallSheetFrame ssf = new SmallSheetFrame();
		ssf.setLogo("测试" + String.valueOf(System.currentTimeMillis()).substring(6));
		ssf.setCountOfBlankLineAtBottom(2);
		ssf.setDelimiterToRepeat("-");
		ssf.setID(4);
		//
		MvcResult mr = mvc.perform(post("/smallSheetFrame/updateEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
				.param(SmallSheetFrame.field.getFIELD_NAME_ID(), String.valueOf(ssf.getID())) //
				.param(SmallSheetFrame.field.getFIELD_NAME_logo(), ssf.getLogo()) //
				.param(SmallSheetFrame.field.getFIELD_NAME_countOfBlankLineAtBottom(), String.valueOf(ssf.getCountOfBlankLineAtBottom())) //
				.param(SmallSheetFrame.field.getFIELD_NAME_delimiterToRepeat(), ssf.getDelimiterToRepeat()) //
				.param(SmallSheetFrame.field.getFIELD_NAME_textList(),
						"[{\"bold\":false,\"content\":\"博昕\",\"frameId\":" + ssf.getID() + 1 + ",\"gravity\":17,\"size\":32.0,\"ID\":3},{\"bold\":false,\"content\":\"博昕POS\",\"frameId\":" + ssf.getID() + 1
								+ ",\"gravity\":17,\"size\":32.0,\"ID\":4}]")) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证:检查错误码
		Assert.assertTrue(mr.getResponse().getContentAsString().equals(""), "主表F_ID与从表的F_FrameID一致");
	}
}
