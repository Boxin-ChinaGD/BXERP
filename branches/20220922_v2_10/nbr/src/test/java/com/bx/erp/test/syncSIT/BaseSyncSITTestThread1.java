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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BaseSyncSITTestThread1 extends Thread {
	protected String dbName;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	protected String getSyncActionRetrieveNExURL() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected String getSyncActionFeedbackExURL(String objIDs, String errorCode) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected EnumSyncCacheType getSyncCacheType() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected AtomicInteger getNumberSynchronized() {
		throw new RuntimeException("Not yet implemented!");
	}

	// 记录已经同步成功的POS的数目
	private MockMvc mvc;
	private HttpSession session;
	/** 做同步的POS机的总数 */
	private int iPosNO;
	/** 要同步的块的总数，即同步表主表总行数 */
	private int iSyncBlockNO;
	/** 不同的SIT测试阶段，退出线程的方法不同：<br />
	 * iPhase = 1, SIT测试的第1阶段。只要同存数目=iPosNO * iSyncBlockNO，即可结束线程。<br />
	 * iPhase = 2, SIT测试的第2阶段。只要所有POS同步成功，及同步块全部清空，即可结束线程。<br />
	 */
	private int iPhase;
	/** 记录同步块从表条数，用来校验数据是否正确 */
	private int slaveSyncBlockNO;

	/** @param mvc
	 * @param session
	 * @param iPhase
	 *            SIT测试阶段代码
	 * @param iPosNO
	 *            做同步的POS机的总数
	 * @param iSyncBlockNO
	 *            要同步的块的总数，即同步表主表总行数 */
	public BaseSyncSITTestThread1(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		this.session = session;
		this.mvc = mvc;
		this.iPhase = iPhase;
		this.iPosNO = iPosNO;
		this.iSyncBlockNO = iSyncBlockNO;
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
				MvcResult mr = mvc.perform(get(getSyncActionRetrieveNExURL()).contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) session)).andExpect(status().isOk()).andDo(print()).andReturn();
				Shared.checkJSONErrorCode(mr);
				// 获取到返回objectID和ErrorCode
				String json = mr.getResponse().getContentAsString();
				JSONObject o = JSONObject.fromObject(json);
				String errorCode = JsonPath.read(o, "$.ERROR");
				List<?> bmList = JsonPath.read(o, "$.objectList[*].ID");
				bmList = bmList.stream().distinct().collect(Collectors.toList());// 去除重复

				String ids = "";
				for (Object bmID : bmList) {
					ids += bmID + ",";
				}

				mvc.perform(get(getSyncActionFeedbackExURL(ids, errorCode)).contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) session)).andExpect(status().isOk()).andDo(print()).andReturn();
				slaveSyncBlockNO = 0;

				List<BaseModel> bmList1 = SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).readN(false, false);
				for (BaseModel bm : bmList1) {
					slaveSyncBlockNO += bm.getListSlave1().size();
				}

				if (iPhase == 1) {
					if (slaveSyncBlockNO == iPosNO * iSyncBlockNO) {
						getNumberSynchronized().incrementAndGet();// 当前所有POS机已经同步所有块，结束同步
						break;
					}
				} else if (iPhase == 2) {
					if (slaveSyncBlockNO == iPosNO * iSyncBlockNO) {
						getNumberSynchronized().incrementAndGet();// 当前所有POS机已经同步所有块，结束同步
						break;
					}
				} else if (iPhase == 3) {
					if (getNumberSynchronized().get() > 0 && bmList1.size() == 0) {// 有POS已经同步，但是同步块又被清空了，证明所有POS都已经同步了块，块才被清空的，同步结束
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
