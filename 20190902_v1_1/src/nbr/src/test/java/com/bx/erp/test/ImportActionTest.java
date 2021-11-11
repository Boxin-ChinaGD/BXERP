package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.ImportAction;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip.EnumVipInfoInExcel;
import com.bx.erp.model.commodity.Commodity.EnumCommodityInfoInExcel;
import com.bx.erp.model.purchasing.Provider.EnumProviderInfoInExcel;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.PoiUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ImportActionTest extends BaseActionTest {
	private static final String saveMultipartFileToDestinationAddress = String.format(ImportAction.PATH_SaveMultipartFileToDestination, "nbr");
	private static final String vipSheetName = "会员";
	private static final String commoditySheetName = "商品";
	private static final String providerSheetName = "供应商";
	
	private static int wrongRow = 3;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testImportEx1() throws Exception {
		Shared.caseLog("case1: 正常创建（要前端使用Selenium录制，再转换为Java测试代码）");
		//
		// renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		// renameFile("import_Case1.xlsm", "博销宝资料导入模板.xlsm");
		// //
		// File file = new
		// File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// String originalFilename = file.getName();
		// FileInputStream fis = new FileInputStream(file);
		// MockMultipartFile multipart = new MockMultipartFile("file", originalFilename,
		// "xls", fis);
		// MvcResult mr = mvc.perform(//
		// fileUpload("/import/importEx.bx")//
		// .file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// //
		// renameFile("博销宝资料导入模板.xlsm", "import_Case1.xlsm");
		// renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		// //
		// Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testImportEx2() throws Exception {
		Shared.caseLog("case2: 文件类型不正确(txt)");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case2.txt", "博销宝资料导入模板.txt");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.txt");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.txt", "import_Case2.txt");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}

	@Test
	public void testImportEx3() throws Exception {
		Shared.caseLog("case3: 文件类型不正确(rar)");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.rar");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}

	@Test
	public void testImportEx4() throws Exception {
		Shared.caseLog("case4: 文件类型不正确(zip)");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.zip");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}

	@Test
	public void testImportEx5() throws Exception {
		Shared.caseLog("case5: 文件为空, actionTest模仿不了");
	}


	@Test
	public void testImportEx7() throws Exception {
		Shared.caseLog("case7: 创建100个会员耗时（要前端使用Selenium录制，再转换为Java测试代码）");
		// TODO 放开
		// File file = new
		// File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case7.xlsm");
		// String originalFilename = file.getName();
		// FileInputStream fis = new FileInputStream(file);
		// MockMultipartFile multipart = new MockMultipartFile("file", originalFilename,
		// "xls", fis);
		// MvcResult mr = mvc.perform(//
		// fileUpload("/import/importEx.bx")//
		// .file(multipart).contentType(MediaType.MULTIPART_FORM_DATA)
		// .session(sessionBoss)
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testImportEx8() throws Exception {
		Shared.caseLog("case8: 创建100个商品耗时（要前端使用Selenium录制，再转换为Java测试代码）");
		// TODO 放开
		// File file = new
		// File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case8.xlsm");
		// String originalFilename = file.getName();
		// FileInputStream fis = new FileInputStream(file);
		// MockMultipartFile multipart = new MockMultipartFile("file", originalFilename,
		// "xls", fis);
		// MvcResult mr = mvc.perform(//
		// fileUpload("/import/importEx.bx")//
		// .file(multipart).contentType(MediaType.MULTIPART_FORM_DATA)
		// .session(sessionBoss)
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testImportEx9() throws Exception {
		Shared.caseLog("case9: 创建1000个商品耗时（要前端使用Selenium录制，再转换为Java测试代码）"); // 200秒
		// TODO 放开
		// File file = new
		// File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case9.xlsm");
		// String originalFilename = file.getName();
		// FileInputStream fis = new FileInputStream(file);
		// MockMultipartFile multipart = new MockMultipartFile("file", originalFilename,
		// "xls", fis);
		// MvcResult mr = mvc.perform(//
		// fileUpload("/import/importEx.bx")//
		// .file(multipart).contentType(MediaType.MULTIPART_FORM_DATA)
		// .session(sessionBoss)
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testImportEx10() throws Exception {
		Shared.caseLog("case10: 创建10000个商品耗时（要前端使用Selenium录制，再转换为Java测试代码）");// 2292秒
		// TODO 放开
		// File file = new
		// File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case10.xlsm");
		// String originalFilename = file.getName();
		// FileInputStream fis = new FileInputStream(file);
		// MockMultipartFile multipart = new MockMultipartFile("file", originalFilename,
		// "xls", fis);
		// MvcResult mr = mvc.perform(//
		// fileUpload("/import/importEx.bx")//
		// .file(multipart).contentType(MediaType.MULTIPART_FORM_DATA)
		// .session(sessionBoss)
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testImportEx11() throws Exception {
		Shared.caseLog("case11: 商品barcodes格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case11_wrongBarcodes.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case11_wrongBarcodes.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		// 结果验证
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_barcodes.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx12() throws Exception {
		Shared.caseLog("case12: 商品名称格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case12_wrongCommName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case12_wrongCommName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx13() throws Exception {
		Shared.caseLog("case13: 包装单位名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case13_wrongCommPackageUnitName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case13_wrongCommPackageUnitName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_packageUnitName.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx14() throws Exception {
		Shared.caseLog("case14: 零售价格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case14_wrongCommPriceRetail.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case14_wrongCommPriceRetail.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_priceRetail.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx15() throws Exception {
		Shared.caseLog("case15: 供应商名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case15_wrongProviderName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case15_wrongProviderName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx17() throws Exception {
		Shared.caseLog("case17: 商品规格格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case17_wrongCommSpecification.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case17_wrongCommSpecification.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_specification.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx19() throws Exception {
		Shared.caseLog("case19: 品牌名称名称格式错误，验证目标excel文件是否加标注");
	}

	@Test
	public void testImportEx20() throws Exception {
		Shared.caseLog("case20: 会员价格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case20_wrongCommPriceVIP.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case20_wrongCommPriceVIP.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_priceVIP.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx21() throws Exception {
		Shared.caseLog("case21: 保质期格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case21_wrongCommShelfLife.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case21_wrongCommShelfLife.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_shelfLife.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx22() throws Exception {
		Shared.caseLog("case22: 退货天数格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case22_wrongCommReturnDays.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case22_wrongCommReturnDays.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_returnDays.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	
	@Test
	public void testImportEx24() throws Exception {
		Shared.caseLog("case24: 期初数量格式错误，doPase1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case24_wrongCommNOStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case24_wrongCommNOStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_nOStart.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx25() throws Exception {
		Shared.caseLog("case25: 期初采购价格式错误，doPase1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case25_wrongCommPurchasingPriceStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case25_wrongCommPurchasingPriceStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_purchasingPriceStart.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx27() throws Exception {
		Shared.caseLog("case27: 供应商地址格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case27_wrongProviderAddress.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case27_wrongProviderAddress.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_address.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx28() throws Exception {
		Shared.caseLog("case28: 供应商联系人格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case28_wrongProviderContactName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case28_wrongProviderContactName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_contactName.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx29() throws Exception {
		Shared.caseLog("case29: 供应商联系人电话格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case29_wrongProviderMobile.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case29_wrongProviderMobile.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx30() throws Exception {
		Shared.caseLog("case30: 供应商区域格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case30_wrongProviderDisctrict.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case30_wrongProviderDisctrict.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_district.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx31() throws Exception {
		Shared.caseLog("case31: 会员积分格式错误，doPase1失败，验证目标excel文件是否加标注");

		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case31_wrongVipBonus.xlsm", "博销宝资料导入模板.xlsm");

		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case31_wrongVipBonus.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

//	@Test
//	public void testImportEx32() throws Exception {
//		Shared.caseLog("case32: 会员卡号格式错误，验证目标excel文件是否加标注");
//		//
//		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
//		renameFile("import_Case32_wrongVipCarCode.xlsm", "博销宝资料导入模板.xlsm");
//		
//		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
//		String originalFilename = file.getName();
//		FileInputStream fis = new FileInputStream(file);
//		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
//		MvcResult mr = mvc.perform(//
//				fileUpload("/import/importEx.bx")//
//						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		
//		renameFile("博销宝资料导入模板.xlsm", "import_Case32_wrongVipCarCode.xlsm");
//		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
//		
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
//		//
//		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_cardCode.getIndex());
//		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
//	}

	@Test
	public void testImportEx35() throws Exception {
		Shared.caseLog("case35: 会员名称格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case35_wrongVipName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case35_wrongVipName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_name.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx36() throws Exception {
		Shared.caseLog("case36: 会员性别格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case36_wrongVipSex.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case36_wrongVipSex.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_sex.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx37() throws Exception {
		Shared.caseLog("case37: 会员生日格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case37_wrongVipBirthday.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case37_wrongVipBirthday.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_birthday.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx38() throws Exception {
		Shared.caseLog("case38: 会员上次消费日期时间格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case38_lastConsumeDatetime.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case38_lastConsumeDatetime.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_lastConsumeDatetime.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx39() throws Exception {
		Shared.caseLog("case39: 会员性别格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case36_2_wrongVipSex.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case36_2_wrongVipSex.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_sex.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx40() throws Exception {
		Shared.caseLog("case40: 会员性别不填写，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case36_3_wrongVipSex.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case36_3_wrongVipSex.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_sex.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx41() throws Exception {
		Shared.caseLog("case41: 会员手机号格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case34_2_wrongVipMobile.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case34_2_wrongVipMobile.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx42() throws Exception {
		Shared.caseLog("case42: 会员积分格式错误，checkCreate失败，验证目标excel文件是否加标注");

		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case31_2_wrongVipBonus.xlsm", "博销宝资料导入模板.xlsm");

		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case31_2_wrongVipBonus.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx43() throws Exception {
		Shared.caseLog("case43: 期初采购价格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case25_2_wrongCommPurchasingPriceStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case25_2_wrongCommPurchasingPriceStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_purchasingPriceStart.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx44() throws Exception {
		Shared.caseLog("case44: 期初数量格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case24_2_wrongCommNOStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case24_2_wrongCommNOStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_nOStart.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	
	@Test
	public void testImportEx46() throws Exception {
		Shared.caseLog("case46: 退货天数格式错误，checkCreate不通过，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case22_2_wrongCommReturnDays.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case22_2_wrongCommReturnDays.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_returnDays.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx47() throws Exception {
		Shared.caseLog("case47: 保质期格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case21_2_wrongCommShelfLife.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case21_2_wrongCommShelfLife.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_shelfLife.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx48() throws Exception {
		Shared.caseLog("case48: 会员价格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case20_2_wrongCommPriceVIP.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case20_2_wrongCommPriceVIP.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_priceVIP.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx49() throws Exception {
		Shared.caseLog("case49: 品牌名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case49_wrongBrandName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case49_wrongBrandName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_brandName.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	// TODO
	// @Test
	// public void testImportEx50() throws Exception {
	// Shared.caseLog("case50: 商品采购单位格式错误，checkCreate失败，验证目标excel文件是否加标注");
	// // TODO 普通商品采购单位没有做checkCreate检查
	// }

	@Test
	public void testImportEx51() throws Exception {
		Shared.caseLog("case51: 商品规格格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case51_wrongCommSpecification.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case51_wrongCommSpecification.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_specification.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx52() throws Exception {
		Shared.caseLog("case52: 商品小类名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case52_wrongCommCategoryName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case52_wrongCommCategoryName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_categoryName.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx53() throws Exception {
		Shared.caseLog("case53: 没有提供供应商信息，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case15_2_wrongProviderName.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case15_2_wrongProviderName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_providerName.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx54() throws Exception {
		Shared.caseLog("case54: 零售价格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case14_2_wrongCommPriceRetail.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case14_2_wrongCommPriceRetail.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, wrongRow, EnumCommodityInfoInExcel.ECIIE_priceRetail.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	/*验证excel的sheetName表，row行，column列是否添加了批注*/
	private void verifyCommentInExcel(String sheetName, int row, int column) throws IOException {
		PoiUtils poiXls = new PoiUtils(saveMultipartFileToDestinationAddress);
		String comment = poiXls.readOneExcelCellStyle(saveMultipartFileToDestinationAddress, sheetName, row, column);
		System.out.println("comment:" + comment);
		Assert.assertTrue(comment != null && comment.length() > 0, "没有添加批注");
	}

	@Test
	public void testImportEx55() throws Exception { // 文件名称不正确， 前端会提示
		Shared.caseLog("case55:文件名称不正确");

		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case55_wrongFlieName.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}

	@Test
	public void testImportEx56() throws Exception {
		Shared.caseLog("case56:文件类型不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case56_wrongFlieType.txt", "博销宝资料导入模板.txt");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.txt");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.txt", "import_Case56_wrongFlieType.txt");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}

	@Test
	public void testImportEx57() throws Exception {
		Shared.caseLog("case57:文件大小不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case57_wrongFileSize.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case57_wrongFileSize.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}

	public void renameFile(String oldName, String newName) {
		try {
			File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\" + oldName);
			file.renameTo(new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\" + newName));
		} catch (Exception ex) {
			Assert.assertTrue(false, ex.getMessage());
		}
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\" + newName);
		if (!file.exists()) {
			Assert.assertTrue(false, "重命名失败，可能是运行测试时了打开文件，或该目录下已经有1.xlsm，请到对应的文件夹进行svn update并删除1.xlsm");
		}
	}

	/* 注意，只能运行一次，要想再次运行，必须修改excel或刷新数据库，否则会员手机号和卡号重复 */
	@Test
	public void testImportEx58() throws Exception {
		Shared.caseLog("case58: 创建会员后，会员初始积分、会员卡号是否是excel定义的的，而不是积分规则的初始积分（商品那边要前端使用Selenium录制，再转换为Java测试代码）");

		// //
		// renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		// renameFile("import_Case58.xlsm", "博销宝资料导入模板.xlsm");
		//
		// File file = new
		// File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// String originalFilename = file.getName();
		// FileInputStream fis = new FileInputStream(file);
		// MockMultipartFile multipart = new MockMultipartFile("file", originalFilename,
		// "xls", fis);
		// MvcResult mr = mvc.perform(//
		// fileUpload("/import/importEx.bx")//
		// .file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// renameFile("博销宝资料导入模板.xlsm", "import_Case58.xlsm");
		// renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		// Shared.checkJSONErrorCode(mr);
		//
		// //TODO
		// 缺乏会员初始积分、会员卡号结果验证：读出DB中的积分，与xlsm中的积分比较。重复运行一次测试？本测试函数只支持运行一次。原先DB里有会员的话，要正确处理F_ID与xlsm中的会员的关系。
		// PoiUtils poiXls = new PoiUtils(saveMultipartFileToDestinationAddress);
		// String vipMobile =
		// poiXls.readOneExcelCell(saveMultipartFileToDestinationAddress, vipSheetName,
		// 3, 2);
		// System.out.println("vipMobile:" + vipMobile);
		// Assert.assertTrue(vipMobile != null, "手机号为空");
		// // 验证手机号
		// Vip vip = new Vip();
		// vip.setPageIndex(1);
		// vip.setPageSize(10);
		// List<BaseModel> bmList = BaseVipTest.retrieveNViaMapper(vip,
		// BaseBO.INVALID_CASE_ID);
		// Vip vipCreated = (Vip) bmList.get(0);
		// Assert.assertTrue(vipCreated.getMobile().equals(vipMobile),
		// "创建出来的vip的手机号与excel表填写的不一致！");
		// // 验证会员卡号
		// String cardCode =
		// poiXls.readOneExcelCell(saveMultipartFileToDestinationAddress, vipSheetName,
		// 3, 1);
		// System.out.println("cardCode:" + cardCode);
		// Assert.assertTrue(cardCode != null, "手机号为空");
		// VipCardCode vipCardCode = new VipCardCode();
		// vipCardCode.setVipID(vipCreated.getID());
		// List<BaseModel> cardBmList =
		// BaseVipCardCodeTest.retrieveNViaMapper(vipCardCode, Shared.DBName_Test);
		// VipCardCode vipCardCodeCreated = (VipCardCode) cardBmList.get(0);
		// Assert.assertTrue(vipCardCodeCreated.getSN().equals(cardCode),
		// "创建出来的vip的卡号与excel表填写的不一致！");
	}

	@Test
	public void testImportEx59() throws Exception {
		Shared.caseLog("case59: 创建的供应商手机号与DB中的重复，创建失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case59_providerExistInDB.xlsm", "博销宝资料导入模板.xlsm");

		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case59_providerExistInDB.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx60() throws Exception {
		Shared.caseLog("case60: excel文件有多个商品文件格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case60_sumCommWrongFormatNO.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case60_sumCommWrongFormatNO.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int totalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_commodityWrongFormatNumber);
		System.out.println(totalWrongFormatNumber);
		Assert.assertTrue(totalWrongFormatNumber == 13, "没有正确返回格式错误个数");
		int totalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_commodityTotalToCreate);
		System.out.println(totalToCreate);
		Assert.assertTrue(totalToCreate == 30, "没有正确返回要创建的总数");
		//
		verifyCommentInExcel(commoditySheetName, 3, EnumCommodityInfoInExcel.ECIIE_barcodes.getIndex());
		verifyCommentInExcel(commoditySheetName, 5, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 7, EnumCommodityInfoInExcel.ECIIE_packageUnitName.getIndex());
		verifyCommentInExcel(commoditySheetName, 9, EnumCommodityInfoInExcel.ECIIE_priceRetail.getIndex());
		verifyCommentInExcel(commoditySheetName, 11, EnumCommodityInfoInExcel.ECIIE_providerName.getIndex());
		verifyCommentInExcel(commoditySheetName, 13, EnumCommodityInfoInExcel.ECIIE_categoryName.getIndex());
		verifyCommentInExcel(commoditySheetName, 15, EnumCommodityInfoInExcel.ECIIE_specification.getIndex());
		verifyCommentInExcel(commoditySheetName, 19, EnumCommodityInfoInExcel.ECIIE_brandName.getIndex());
		verifyCommentInExcel(commoditySheetName, 21, EnumCommodityInfoInExcel.ECIIE_priceVIP.getIndex());
		verifyCommentInExcel(commoditySheetName, 23, EnumCommodityInfoInExcel.ECIIE_shelfLife.getIndex());
		verifyCommentInExcel(commoditySheetName, 25, EnumCommodityInfoInExcel.ECIIE_returnDays.getIndex());
		verifyCommentInExcel(commoditySheetName, 29, EnumCommodityInfoInExcel.ECIIE_nOStart.getIndex());
		verifyCommentInExcel(commoditySheetName, 31, EnumCommodityInfoInExcel.ECIIE_purchasingPriceStart.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}

	@Test
	public void testImportEx61() throws Exception {
		Shared.caseLog("case61: excel文件有多个会员格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case61_sumWrongVipFormatNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case61_sumWrongVipFormatNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int totalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_vipWrongFormatNumber);
		System.out.println(totalWrongFormatNumber);
		Assert.assertTrue(totalWrongFormatNumber == 6, "没有正确返回格式错误个数");
		int totalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_vipTotalToCreate);
		System.out.println(totalToCreate);
		Assert.assertTrue(totalToCreate == 13, "没有正确返回要创建的总数");
		//
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 5, EnumVipInfoInExcel.EVIIE_cardCode.getIndex());
		verifyCommentInExcel(vipSheetName, 7, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 9, EnumVipInfoInExcel.EVIIE_name.getIndex());
		verifyCommentInExcel(vipSheetName, 11, EnumVipInfoInExcel.EVIIE_sex.getIndex());
		verifyCommentInExcel(vipSheetName, 13, EnumVipInfoInExcel.EVIIE_birthday.getIndex());
		verifyCommentInExcel(vipSheetName, 15, EnumVipInfoInExcel.EVIIE_lastConsumeDatetime.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	
	@Test
	public void testImportEx62() throws Exception {
		Shared.caseLog("case62: excel文件有多个供应商格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case62_sumWrongProviderFormatNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case62_sumWrongProviderFormatNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int totalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_providerWrongFormatNumber);
		System.out.println(totalWrongFormatNumber);
		Assert.assertTrue(totalWrongFormatNumber == 5, "没有正确返回格式错误个数");
		int totalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_providerTotalToCreate);
		System.out.println(totalToCreate);
		Assert.assertTrue(totalToCreate == 10, "没有正确返回要创建的总数");
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 5, EnumProviderInfoInExcel.EPIIE_district.getIndex());
		verifyCommentInExcel(providerSheetName, 7, EnumProviderInfoInExcel.EPIIE_address.getIndex());
		verifyCommentInExcel(providerSheetName, 9, EnumProviderInfoInExcel.EPIIE_contactName.getIndex());
		verifyCommentInExcel(providerSheetName, 11, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx63() throws Exception {
		Shared.caseLog("case63: excel文件有多个供应商、商品、会员格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case63_sumWrongProviderVipCommFormatNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case63_sumWrongProviderVipCommFormatNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int providerTotalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_providerWrongFormatNumber);
		System.out.println(providerTotalWrongFormatNumber);
		Assert.assertTrue(providerTotalWrongFormatNumber == 5, "没有正确返回格式错误个数" + providerTotalWrongFormatNumber);
		int commodityTotalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_commodityWrongFormatNumber);
		System.out.println(commodityTotalWrongFormatNumber);
		Assert.assertTrue(commodityTotalWrongFormatNumber == 13, "没有正确返回格式错误个数" + commodityTotalWrongFormatNumber);
		int vipTotalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_vipWrongFormatNumber);
		System.out.println(vipTotalWrongFormatNumber);
		Assert.assertTrue(vipTotalWrongFormatNumber == 6, "没有正确返回格式错误个数" + vipTotalWrongFormatNumber);
		int providerTotalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_providerTotalToCreate);
		System.out.println(providerTotalToCreate);
		Assert.assertTrue(providerTotalToCreate == 10, "没有正确返回要创建的总数" + providerTotalToCreate);
		int commodityTotalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_commodityTotalToCreate);
		System.out.println(commodityTotalToCreate);
		Assert.assertTrue(commodityTotalToCreate == 30, "没有正确返回要创建的总数" + commodityTotalToCreate);
		int vipTotalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_vipTotalToCreate);
		System.out.println(vipTotalToCreate);
		Assert.assertTrue(vipTotalToCreate == 13, "没有正确返回要创建的总数" + vipTotalToCreate);
		//
		verifyCommentInExcel(commoditySheetName, 3, EnumCommodityInfoInExcel.ECIIE_barcodes.getIndex());
		verifyCommentInExcel(commoditySheetName, 5, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 7, EnumCommodityInfoInExcel.ECIIE_packageUnitName.getIndex());
		verifyCommentInExcel(commoditySheetName, 9, EnumCommodityInfoInExcel.ECIIE_priceRetail.getIndex());
		verifyCommentInExcel(commoditySheetName, 11, EnumCommodityInfoInExcel.ECIIE_providerName.getIndex());
		verifyCommentInExcel(commoditySheetName, 13, EnumCommodityInfoInExcel.ECIIE_categoryName.getIndex());
		verifyCommentInExcel(commoditySheetName, 15, EnumCommodityInfoInExcel.ECIIE_specification.getIndex());
		verifyCommentInExcel(commoditySheetName, 19, EnumCommodityInfoInExcel.ECIIE_brandName.getIndex());
		verifyCommentInExcel(commoditySheetName, 21, EnumCommodityInfoInExcel.ECIIE_priceVIP.getIndex());
		verifyCommentInExcel(commoditySheetName, 23, EnumCommodityInfoInExcel.ECIIE_shelfLife.getIndex());
		verifyCommentInExcel(commoditySheetName, 25, EnumCommodityInfoInExcel.ECIIE_returnDays.getIndex());
		verifyCommentInExcel(commoditySheetName, 29, EnumCommodityInfoInExcel.ECIIE_nOStart.getIndex());
		verifyCommentInExcel(commoditySheetName, 31, EnumCommodityInfoInExcel.ECIIE_purchasingPriceStart.getIndex());
		//
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 5, EnumVipInfoInExcel.EVIIE_cardCode.getIndex());
		verifyCommentInExcel(vipSheetName, 7, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 9, EnumVipInfoInExcel.EVIIE_name.getIndex());
		verifyCommentInExcel(vipSheetName, 11, EnumVipInfoInExcel.EVIIE_sex.getIndex());
		verifyCommentInExcel(vipSheetName, 13, EnumVipInfoInExcel.EVIIE_birthday.getIndex());
		verifyCommentInExcel(vipSheetName, 15, EnumVipInfoInExcel.EVIIE_lastConsumeDatetime.getIndex());
		//
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 5, EnumProviderInfoInExcel.EPIIE_district.getIndex());
		verifyCommentInExcel(providerSheetName, 7, EnumProviderInfoInExcel.EPIIE_address.getIndex());
		verifyCommentInExcel(providerSheetName, 9, EnumProviderInfoInExcel.EPIIE_contactName.getIndex());
		verifyCommentInExcel(providerSheetName, 11, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx64() throws Exception {
		Shared.caseLog("case64: excel文件有多个商品创建失败，验证目标excel文件是否加标注，并正确返回失败个数");
		// TODO 需要前端用selenium实现
//		("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case64_sumFailCreateCommNumber.xlsm")
	}
	
	@Test
	public void testImportEx65() throws Exception {
		// TODO 需要模拟服务器错误
		Shared.caseLog("case65: excel文件有多个会员创建失败，验证目标excel文件是否加标注，并正确返回失败个数");
//		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
//		renameFile("import_Case65_sumFailCreateVipNumber.xlsm", "博销宝资料导入模板.xlsm");
//		//
//		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
//		String originalFilename = file.getName();
//		FileInputStream fis = new FileInputStream(file);
//		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
//		MvcResult mr = mvc.perform(//
//				fileUpload("/import/importEx.bx")//
//						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		//
//		renameFile("博销宝资料导入模板.xlsm", "import_Case65_sumFailCreateVipNumber.xlsm");
//		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
//		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
//		int totalFailCreateNumber = JsonPath.read(o, "$." + "totalFailCreateNumber");
//		System.out.println(totalFailCreateNumber);
//		Assert.assertTrue(totalFailCreateNumber == 13, "没有正确返回格式错误个数");
//		int totalToCreate = JsonPath.read(o, "$." + "totalToCreate");
//		System.out.println(totalToCreate);
//		Assert.assertTrue(totalToCreate == 13, "没有正确返回要创建的总数");
//		//
//		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 4, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 5, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 6, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 7, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 8, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 9, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 10, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 11, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 12, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 13, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 14, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyCommentInExcel(vipSheetName, 15, EnumVipInfoInExcel.EVIIE_bonus.getIndex());
//		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx66() throws Exception {
		Shared.caseLog("case66: excel文件有多个供应商格式有错误(与DB中的手机号重复)，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case66_sumFailCreateProviderNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case66_sumFailCreateProviderNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		// 因为要先把没有的供应商创建出来，再检查商品格式，所以供应商创建失败个数当做格式错误个数
		int totalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_providerWrongFormatNumber);
		System.out.println(totalWrongFormatNumber);
		Assert.assertTrue(totalWrongFormatNumber == 10, "没有正确返回创建错误个数:" + totalWrongFormatNumber);
		int totalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_providerTotalToCreate);
		System.out.println(totalToCreate);
		Assert.assertTrue(totalToCreate == 10, "没有正确返回要创建的总数");
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 4, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 5, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 6, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 7, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 8, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 9, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 10, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 11, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 12, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx67() throws Exception {
		// TODO 需要模拟服务器错误
		Shared.caseLog("case67: excel文件有多个商品、会员创建失败，验证目标excel文件是否加标注，并正确返回失败个数");
		// TODO 需要前端用selenium实现
//		("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\import_Case67_sumFailCreateCommAndVipNumber.xlsm")
	}
	
	@Test
	public void testImportEx68() throws Exception {
		Shared.caseLog("case68: D:\\nbr\\file\\import目录下文件夹为空，判断是否会自动生成目录和文件(测试时需要手动清空import，然后查看是否自动生成目录和文件)");
		//
		// 将import重命名和恢复名称。让其在DEV上仍然能跑得过。
		// 注意跑这个测试的时候不要打开D:\nbr\file\import，否则重命名会失败
		File file1 = new File("D:\\nbr\\file\\import");
		if(!file1.exists()) {
			Assert.assertTrue(false, "目录不存在");
		}
		if(!file1.renameTo(new File("D:\\nbr\\file\\import2"))) {
			Assert.assertTrue(false, "重命名失败");
		}
		File file2 = new File("D:\\nbr\\file\\import");
		file2.mkdir();
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case68.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xlsm", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case68.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");

		if(!GeneralUtil.deleteDir(file2)) {
			Assert.assertTrue(false, "删除目录下的文件失败");
		}
		File file3 = new File("D:\\nbr\\file\\import2");
		if(!file3.renameTo(new File("D:\\nbr\\file\\import"))) {
			Assert.assertTrue(false, "重命名失败");
		}
		//
		Shared.checkJSONErrorCode(mr);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}
	
	@Test
	public void testImportEx69() throws Exception {
		Shared.caseLog("case69: excel文件有多个会员创建成功，多个会员手机号与DB中的重复，这时候不用提示用户下载");
		// 
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case69.xlsm", "博销宝资料导入模板.xlsm");
		// 
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case69.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");

		Shared.checkJSONErrorCode(mr);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}
	
	@Test
	public void testImportEx70() throws Exception {
		Shared.caseLog("case70: excel文件有多个会员创建成功，多个会员手机号与DB中的重复，多个因为数据库等原因创建失败，这时候需要提示用户下载，下载后的内容只包含因为数据库等原因创建失败的数据行");
		// TODO 怎么模拟因为数据库等原因创建失败
	}
	
	
	@Test
	public void testImportEx71() throws Exception {
		Shared.caseLog("case71: excel文件有多个商品创建成功，多个商品名称与DB中的重复，这时候不用提示用户下载");
		// TODO 创建商品需要用到OkHttp，所以只能通过前端selenium实现
	}
	
	@Test
	public void testImportEx72() throws Exception {
		Shared.caseLog("case72: excel文件有多个商品创建成功，多个商品重复，多个商品因数据库等原因失败，上传后提示有错误，点击下载，期望是下载的文件只包含数据库等原因失败创建失败的数据行");
		// TODO 创建商品需要用到OkHttp，所以只能通过前端selenium实现
		// TODO 怎么模拟因为数据库等原因创建失败
	}
	
	@Test
	public void testImportEx73() throws Exception {
		Shared.caseLog("case73: excel文件商品为null(不填写)，格式错误，验证是否加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case73.xlsm", "博销宝资料导入模板.xlsm");
		// 
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case73.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, 3, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx74() throws Exception {
		Shared.caseLog("case74: excel文件会员生日和上次消费时间为null(不填写)，属于格式正常，验证是否创建了生日和消费时间为null的会员");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case74.xlsm", "博销宝资料导入模板.xlsm");
		// 
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case74.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");

		Shared.checkJSONErrorCode(mr);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}
	
	
	@Test
	public void testImportEx75() throws Exception {
		Shared.caseLog("case75: excel文件商品名称、会员手机，供应商名称、供应商手机有重复，格式错误，验证是否添加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case75.xlsm", "博销宝资料导入模板.xlsm");
		// 
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case75.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyCommentInExcel(commoditySheetName, 3, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 4, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 5, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 6, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 7, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 8, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 9, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 10, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		//
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 4, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 5, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 6, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 7, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 8, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		// 
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 4, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 5, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 6, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 7, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 8, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 9, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 10, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 11, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 12, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 13, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 14, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 15, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 16, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 17, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 18, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 19, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 20, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 21, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 22, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx76() throws Exception {
		Shared.caseLog("case76: excel文件供应商格式正确，正常创建供应商");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case76.xlsm", "博销宝资料导入模板.xlsm");
		// 
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case76.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");

		Shared.checkJSONErrorCode(mr);
		verifyNeedToDownload(mr, EnumBoolean.EB_NO.getIndex());
	}
	
	@Test
	public void testImportEx77() throws Exception {
		Shared.caseLog("case77: excel文件有多个商品名称重复，多个会员手机号重复，多个供应商名称重复，多个供应商手机号重复，验证是否正确返回格式错误个数和添加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case77.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case77.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int providerTotalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_providerWrongFormatNumber);
		System.out.println(providerTotalWrongFormatNumber);
		Assert.assertTrue(providerTotalWrongFormatNumber == 6, "没有正确返回格式错误个数" + providerTotalWrongFormatNumber);
		int commodityTotalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_commodityWrongFormatNumber);
		System.out.println(commodityTotalWrongFormatNumber);
		Assert.assertTrue(commodityTotalWrongFormatNumber == 10, "没有正确返回格式错误个数" + commodityTotalWrongFormatNumber);
		int vipTotalWrongFormatNumber = JsonPath.read(o, "$." + ImportAction.KEY_vipWrongFormatNumber);
		System.out.println(vipTotalWrongFormatNumber);
		Assert.assertTrue(vipTotalWrongFormatNumber == 6, "没有正确返回格式错误个数" + vipTotalWrongFormatNumber);
		int providerTotalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_providerTotalToCreate);
		System.out.println(providerTotalToCreate);
		Assert.assertTrue(providerTotalToCreate == 10, "没有正确返回要创建的总数" + providerTotalToCreate);
		int commodityTotalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_commodityTotalToCreate);
		System.out.println(commodityTotalToCreate);
		Assert.assertTrue(commodityTotalToCreate == 19, "没有正确返回要创建的总数" + commodityTotalToCreate);
		int vipTotalToCreate = JsonPath.read(o, "$." + ImportAction.KEY_vipTotalToCreate);
		System.out.println(vipTotalToCreate);
		Assert.assertTrue(vipTotalToCreate == 12, "没有正确返回要创建的总数" + vipTotalToCreate);
		//
		verifyCommentInExcel(commoditySheetName, 3, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 5, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 7, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 9, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 11, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 13, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 15, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 17, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 19, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		verifyCommentInExcel(commoditySheetName, 21, EnumCommodityInfoInExcel.ECIIE_name.getIndex());
		//
		verifyCommentInExcel(vipSheetName, 3, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 5, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 7, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 9, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 11, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		verifyCommentInExcel(vipSheetName, 13, EnumVipInfoInExcel.EVIIE_mobile.getIndex());
		//
		verifyCommentInExcel(providerSheetName, 3, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 5, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 7, EnumProviderInfoInExcel.EPIIE_name.getIndex());
		verifyCommentInExcel(providerSheetName, 8, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 10, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyCommentInExcel(providerSheetName, 12, EnumProviderInfoInExcel.EPIIE_mobile.getIndex());
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void testImportEx78() throws Exception {
		Shared.caseLog("case78: excel文件商品表、会员表、供应商表中间有空白行，验证是否正确返回格式错误个数和添加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case78.xlsm", "博销宝资料导入模板.xlsm");
		//
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipart = new MockMultipartFile("file", originalFilename, "xls", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/import/importEx.bx")//
						.file(multipart).contentType(MediaType.MULTIPART_FORM_DATA).session(sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		renameFile("博销宝资料导入模板.xlsm", "import_Case78.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		verifyNeedToDownload(mr, EnumBoolean.EB_Yes.getIndex());
	}
	
	@Test
	public void downloadEx1() throws Exception {
		Shared.caseLog("case1: 正常下载");
		MvcResult mr = mvc.perform(//
				post("/import/downloadEx.bx")//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}
	
	@Test
	public void downloadEx2() throws Exception {
		Shared.caseLog("case2: 没有权限");
		MvcResult mr = mvc.perform(//
				post("/import/downloadEx.bx")//
						.session(sessionCashier)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}
	
	private void verifyNeedToDownload(MvcResult mr, int expected) throws UnsupportedEncodingException {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int needToDownload = JsonPath.read(o, "$." + "needToDownload");
		Assert.assertTrue(needToDownload == expected, "needToDownload返回值不正确");
	}
}
