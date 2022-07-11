package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RefCommodityHubActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNExTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1：正常case");

		MvcResult mrl1 = mvc.perform(//
				get("/refCommodityHub/retrieveNByBarcodeEx.bx?barcode=123456789")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mrl1);
	}

	@Test
	public void retrieveNExTest2() throws Exception {
		Shared.caseLog("case2:没有权限进行操作");
//		MvcResult mrl2 = mvc.perform(//
//				get("/refCommodityHub/retrieveNByBarcodeEx.bx?barcode=123456789")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_NoError); // SP_RefCommodityHub_RetrieveN的权限修改为所有人

	}

	@Test
	public void retrieveNExTest3() throws Exception {
		// 原本的测试用例是公司A查询到参考商品a，公司B也可以查询到参考商品a，但是没有第二个公司拿来测试
		Shared.caseLog("case3:A公司可以查询到商品a");
		// 公司A
		MvcResult mrlA = mvc.perform(//
				get("/refCommodityHub/retrieveNByBarcodeEx.bx?barcode=545895185659721561")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mrlA, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNExTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4：条形码格式错误");

		MvcResult mr = mvc.perform(//
				get("/refCommodityHub/retrieveNByBarcodeEx.bx?barcode=")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}
}
