package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingFlowBO;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseRetailTradePromotingTest extends BaseTestNGSpringContextTest {
	@SuppressWarnings("unchecked")
	public static void verifyRetailTradePromoting(RetailTrade retailTradeInDB, RetailTrade tmpCreateRetailTrade, RetailTradePromotingBO retailTradePromotingBO, RetailTradePromotingFlowBO retailTradePromotingFlowBO, String dbName,
			boolean isNoError) {
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		retailTradePromoting.setTradeID(retailTradeInDB.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradePromoting> retailTradePromotingListInDB = (List<RetailTradePromoting>) retailTradePromotingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradePromoting);
		if (retailTradePromotingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "根据零售单查找零售促销表失败," + retailTradePromotingBO.printErrorInfo());
		}
		assertTrue((retailTradePromotingListInDB != null && retailTradePromotingListInDB.size() == 1), "零售单促销表为空或者数目不正确");
		RetailTradePromoting retailTradePromotingInDB = retailTradePromotingListInDB.get(0);

		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setRetailTradePromotingID(retailTradePromotingInDB.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<RetailTradePromotingFlow> retailTradePromotingFlowInDB = (List<RetailTradePromotingFlow>) retailTradePromotingFlowBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradePromotingFlow);
		if (retailTradePromotingFlowBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "根据零售单查找零售促销过程表失败," + retailTradePromotingFlowBO.printErrorInfo());
		}

		RetailTradePromoting tmpRetailTradePromoting = (RetailTradePromoting) tmpCreateRetailTrade.getListSlave2().get(0);
		if (isNoError) {
			assertTrue((retailTradePromotingFlowInDB != null && retailTradePromotingFlowInDB.size() == tmpRetailTradePromoting.getListSlave1().size()), "零售单促销过程表为空或者数目不正确");
			retailTradePromotingInDB.setListSlave1(retailTradePromotingFlowInDB);
		} else {
			// 如果测试结果是部分成功则仅对部分成功创建的进行比较
			if (retailTradePromotingInDB.getListSlave1() != null && retailTradePromotingInDB.getListSlave1().size() > 0) {
				for (int i = 0; i < retailTradePromotingInDB.getListSlave1().size(); i++) {
					RetailTradePromotingFlow rtpfInDB = (RetailTradePromotingFlow) retailTradePromotingInDB.getListSlave1().get(i);
					for (int j = 0; j < tmpRetailTradePromoting.getListSlave1().size(); j++) {
						RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) tmpRetailTradePromoting.getListSlave1().get(j);
						if (rtpf.getPromotionID() == rtpfInDB.getPromotionID()) {
							rtpf.setIgnoreIDInComparision(true);
							assertTrue(rtpf.compareTo(rtpfInDB) == 0, "RetailTradePromotingFlow DB的数据和准备数据不一致！");
						}
					}
				}
			}

			tmpRetailTradePromoting.setIgnoreSlaveListInComparision(true);
		}

		tmpRetailTradePromoting.setIgnoreIDInComparision(true);
		tmpRetailTradePromoting.setTradeID(retailTradePromotingInDB.getTradeID()); // 临时的零售单促销表是并不知道真实的零售单ID
		if (tmpRetailTradePromoting.compareTo(retailTradePromotingInDB) != 0) {
			assertTrue(false, "DB的数据和准备数据不一致！");
		}
	}
}
