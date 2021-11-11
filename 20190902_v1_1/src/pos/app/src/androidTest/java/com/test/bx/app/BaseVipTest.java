package com.test.bx.app;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.VipHttpBO;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.model.Vip;
import com.bx.erp.utils.Shared;

import org.junit.Assert;

import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync;

public class BaseVipTest {
    public static Vip retrieve1SyncViaHttp(Vip vip, VipHttpBO vipHttpBO, VipHttpEvent vipHttpEvent, VipSQLiteEvent vipSQLiteEvent) throws InterruptedException {
        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
        if (!vipHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vip)) {
            Assert.assertTrue("查询VIP失败！" + vipHttpBO.getHttpEvent().printErrorInfo(), false);
        }
        int lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.assertTrue("查询超时！", false);
        }
        vip = (Vip) vipHttpEvent.getListMasterTable().get(0);
        System.out.println("VIP信息： " + vip);
        Assert.assertTrue("查询不到VIP数据！", vip != null);

        return vip;
    }
}
