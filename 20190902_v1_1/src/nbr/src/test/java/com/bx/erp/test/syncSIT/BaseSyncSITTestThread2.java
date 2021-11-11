package com.bx.erp.test.syncSIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BaseSyncSITTestThread2 extends Thread {
	public static AtomicInteger aiNumberSynchronized;
	private MockMvc mvc;
	private HttpSession session;

	public BaseSyncSITTestThread2(MockMvc mvc, HttpSession session) {
		this.session = session;
		this.mvc = mvc;
	}

	protected String getSyncActionRetrieveNExURL() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected EnumSyncCacheType getSyncCacheType() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected AtomicInteger getNumberSynchronized() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected String getSyncActionFeedbackExURL(String objIDs, String errorCode) {
		throw new RuntimeException("Not yet implemented!");
	}

	@Override
	public void run() {
		doRun();
	}

	public void doRun() {
		while (true) {
			try {
				System.out.println("该线程名称：" + this.getName());
				Thread.sleep(1000);// 每1秒运行一次
				MvcResult mr = mvc.perform//
				(//
						get(getSyncActionRetrieveNExURL())//
								.session((MockHttpSession) session)//
								.contentType(MediaType.APPLICATION_JSON)//
				)//
						.andExpect(status().isOk())//
						.andDo(print())//
						.andReturn();
				Shared.checkJSONErrorCode(mr);
				// 获取到返回vipID和ErrorCode
				String json = mr.getResponse().getContentAsString();
				JSONObject o = JSONObject.fromObject(json);
				String errorCode = JsonPath.read(o, "$.ERROR");
				List<?> bmList = JsonPath.read(o, "$.objectList[*].ID");
				bmList = bmList.stream().distinct().collect(Collectors.toList());// 去除重复

				String ids = "";
				for (Object PromotionID : bmList) {
					ids += PromotionID + ",";
				}

				mvc.perform(//
						get(getSyncActionFeedbackExURL(ids, errorCode))//
								.session((MockHttpSession) session)//
								.contentType(MediaType.APPLICATION_JSON)//
								.session((MockHttpSession) session)//
				)//
						.andExpect(status().isOk()).andDo(print()).andReturn();
				List<BaseModel> bmList1 = SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).readN(false, false);

				if (bmList1.size() == 0) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
