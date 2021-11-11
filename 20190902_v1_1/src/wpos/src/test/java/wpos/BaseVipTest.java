package wpos;


import org.junit.Assert;
import wpos.bo.BaseHttpBO;
import wpos.bo.VipHttpBO;
import wpos.event.BaseEvent;
import wpos.event.UI.VipSQLiteEvent;
import wpos.event.VipHttpEvent;
import wpos.model.Vip;
import wpos.utils.Shared;

import static wpos.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_Vip_CreateAsync;


public class BaseVipTest {
    public static Vip retrieve1SyncViaHttp(Vip vip, VipHttpBO vipHttpBO, VipHttpEvent vipHttpEvent, VipSQLiteEvent vipSQLiteEvent) throws InterruptedException {
        vipSQLiteEvent.setEventTypeSQLite(ESET_Vip_CreateAsync);
        if (!vipHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, vip)) {
            Assert.fail("查询VIP失败！" + vipHttpBO.getHttpEvent().printErrorInfo());
        }
        int lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                vipHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            Assert.fail("查询超时！");
        }
        vip = (Vip) vipHttpEvent.getListMasterTable().get(0);
        System.out.println("VIP信息： " + vip);
        Assert.assertNotNull("查询不到VIP数据！", vip);

        return vip;
    }
}
