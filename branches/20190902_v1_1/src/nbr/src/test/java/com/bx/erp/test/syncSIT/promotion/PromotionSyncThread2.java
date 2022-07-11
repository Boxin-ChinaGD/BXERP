package com.bx.erp.test.syncSIT.promotion;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;


@WebAppConfiguration
public class PromotionSyncThread2 extends BaseSyncSITTestThread2 {
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

	public PromotionSyncThread2(MockMvc mvc, HttpSession session) {
		super(mvc, session);
	}
}
