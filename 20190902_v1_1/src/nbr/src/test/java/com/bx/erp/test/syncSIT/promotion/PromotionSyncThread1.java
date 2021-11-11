package com.bx.erp.test.syncSIT.promotion;


import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.test.web.servlet.MockMvc;

import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread1;

public class PromotionSyncThread1 extends BaseSyncSITTestThread1 {
	protected final String SyncActionRetrieveNExURL = "/promotionSync/retrieveNEx.bx";

	@Override
	protected String getSyncActionRetrieveNExURL() {
		return SyncActionRetrieveNExURL;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_PromotionSyncInfo;
	}

	@Override
	protected AtomicInteger getNumberSynchronized() {
		return aiNumberSynchronized;
	}

	@Override
	protected String getSyncActionFeedbackExURL(String objIDs, String errorCode) {
		String SyncActionFeedbackExURL = "/promotionSync/feedbackEx.bx?sID=" + objIDs + "&errorCode=" + errorCode;
		return SyncActionFeedbackExURL;
	}

	// 记录已经同步成功的POS的数目
	public static AtomicInteger aiNumberSynchronized = new AtomicInteger();

	/** @param mvc
	 * @param session
	 * @param iPhase
	 *            SIT测试阶段代码
	 * @param iPosNO
	 *            做同步的POS机的总数
	 * @param iSyncBlockNO
	 *            要同步的块的总数，即同步表主表总行数 */
	public PromotionSyncThread1(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		super(mvc, session, iPhase, iPosNO, iSyncBlockNO);
	}
}
