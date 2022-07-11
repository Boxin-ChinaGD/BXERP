package com.bx.erp.test.commodity;


import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;

@WebAppConfiguration
public class CategorySyncThread2 extends BaseSyncSITTestThread2 {
	
	protected final String SyncActionRetrieveNExURL = "/categorySync/retrieveNEx.bx";

	@Override
	protected String getSyncActionRetrieveNExURL() {
		return SyncActionRetrieveNExURL;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_CategorySyncInfo;
	}

	@Override
	protected AtomicInteger getNumberSynchronized() {
		return aiNumberSynchronized;
	}

	@Override
	protected String getSyncActionFeedbackExURL(String objIDs, String errorCode) {
		String SyncActionFeedbackExURL = "/categorySync/feedbackEx.bx?sID=" + objIDs + "&errorCode=" + errorCode;
		return SyncActionFeedbackExURL;
	}

	// 记录已经同步成功的POS的数目
	public static AtomicInteger aiNumberSynchronized = new AtomicInteger();

	public CategorySyncThread2(MockMvc mvc, HttpSession session) {
		super(mvc, session);
	}

}
