package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.Ntp;

@WebAppConfiguration
public class NtpActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testSyncEx() throws Exception {
		Shared.printTestMethodStartInfo();
		// Ntp ntp = new Ntp();
		mvc.perform(get("/ntp/syncEx.bx?" + Ntp.field.getFIELD_NAME_t1() + "=" + String.valueOf(new Date().getTime())) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

	}
}
