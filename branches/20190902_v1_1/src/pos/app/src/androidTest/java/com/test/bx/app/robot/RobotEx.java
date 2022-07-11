package com.test.bx.app.robot;


import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.LogoutHttpBO;
import com.bx.erp.bo.PosLoginHttpBO;
import com.bx.erp.bo.RetailTradeAggregationHttpBO;
import com.bx.erp.bo.RetailTradeAggregationSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.StaffLoginHttpBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.LogoutHttpEvent;
import com.bx.erp.event.PosLoginHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.StaffLoginHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.robot.client.ClientHandlerEx;
import com.test.bx.app.robot.client.RobotClientEx;
import com.test.bx.app.robot.program.PosLoginEx;
import com.test.bx.app.robot.program.ProgramEx;
import com.test.bx.app.robot.program.RetailTradeAggregationEx;
import com.test.bx.app.robot.program.RetailTradeEx;
import com.test.bx.app.robot.program.SyncDataEx;
import com.test.bx.app.robot.protocol.HeaderEx;

import org.junit.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RobotEx {

    public static final int Event_ID_RobotEx = 100001;
    //
    private PosLoginHttpBO posLoginHttpBO;
    private PosLoginHttpEvent posLoginHttpEvent;
    //
    private static StaffLoginHttpBO staffLoginHttpBO = null;
    private static StaffLoginHttpEvent staffLoginHttpEvent = null;
    //
    private static LogoutHttpBO logoutHttpBO = null;
    private static LogoutHttpEvent logoutHttpEvent = null;
    //
    private static WXPayHttpEvent wxPayHttpEvent = null;
    private static WXPayHttpBO wxPayHttpBO = null;

    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;
    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;
    private static RetailTradeHttpBO retailTradeHttpBO = null;
    //
    private static RetailTradeAggregationSQLiteBO retailTradeAggregationSQLiteBO = null;
    private static RetailTradeAggregationHttpBO retailTradeAggregationHttpBO = null;
    private static RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent = null;
    private static RetailTradeAggregationHttpEvent retailTradeAggregationHttpEvent = null;

    public volatile static Date workTimeStart = null;

    protected StringBuilder errorInfo;


    protected ProgramEx[] programEx;

    boolean canRunNow = false;

    public StringBuilder getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(StringBuilder errorInfo) {
        this.errorInfo = errorInfo;
    }

    public RobotEx() {
        initBOAndEvent();
        setPrograms();
    }

    private void initBOAndEvent() {
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(Event_ID_RobotEx);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_RobotEx);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);

        }
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        if (posLoginHttpEvent == null) {
            posLoginHttpEvent = new PosLoginHttpEvent();
            posLoginHttpEvent.setId(Event_ID_RobotEx);
        }
        if (posLoginHttpBO == null) {
            posLoginHttpBO = new PosLoginHttpBO(GlobalController.getInstance().getContext(), null, posLoginHttpEvent);
        }
        posLoginHttpEvent.setHttpBO(posLoginHttpBO);
        //
        if (staffLoginHttpEvent == null) {
            staffLoginHttpEvent = new StaffLoginHttpEvent();
            staffLoginHttpEvent.setId(Event_ID_RobotEx);
        }
        if (staffLoginHttpBO == null) {
            staffLoginHttpBO = new StaffLoginHttpBO(GlobalController.getInstance().getContext(), null, staffLoginHttpEvent);
        }
        staffLoginHttpEvent.setHttpBO(staffLoginHttpBO);

        //
        if (logoutHttpEvent == null) {
            logoutHttpEvent = new LogoutHttpEvent();
            logoutHttpEvent.setId(Event_ID_RobotEx);
        }
        if (logoutHttpBO == null) {
            logoutHttpBO = new LogoutHttpBO(GlobalController.getInstance().getContext(), null, logoutHttpEvent);
        }
        logoutHttpEvent.setHttpBO(logoutHttpBO);
        //
        if (wxPayHttpEvent == null) {
            wxPayHttpEvent = new WXPayHttpEvent();
            wxPayHttpEvent.setId(Event_ID_RobotEx);
        }
        if (wxPayHttpBO == null) {
            wxPayHttpBO = new WXPayHttpBO(GlobalController.getInstance().getContext(), null, wxPayHttpEvent);
        }
        wxPayHttpEvent.setHttpBO(wxPayHttpBO);
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(Event_ID_RobotEx);
        }
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(Event_ID_RobotEx);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);

        }
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);
        //
        if (retailTradeAggregationSQLiteEvent == null) {
            retailTradeAggregationSQLiteEvent = new RetailTradeAggregationSQLiteEvent();
            retailTradeAggregationSQLiteEvent.setId(Event_ID_RobotEx);
        }
        if (retailTradeAggregationHttpEvent == null) {
            retailTradeAggregationHttpEvent = new RetailTradeAggregationHttpEvent();
            retailTradeAggregationHttpEvent.setId(Event_ID_RobotEx);
        }
        if (retailTradeAggregationSQLiteBO == null) {
            retailTradeAggregationSQLiteBO = new RetailTradeAggregationSQLiteBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        if (retailTradeAggregationHttpBO == null) {
            retailTradeAggregationHttpBO = new RetailTradeAggregationHttpBO(GlobalController.getInstance().getContext(), retailTradeAggregationSQLiteEvent, retailTradeAggregationHttpEvent);
        }
        retailTradeAggregationSQLiteEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationSQLiteEvent.setHttpBO(retailTradeAggregationHttpBO);
        retailTradeAggregationHttpEvent.setSqliteBO(retailTradeAggregationSQLiteBO);
        retailTradeAggregationHttpEvent.setHttpBO(retailTradeAggregationHttpBO);
    }

    public void setPrograms() {
        programEx = new ProgramEx[ProgramEx.EnumProgramType.values().length];
        programEx[ProgramEx.EnumProgramType.EPT_PosLogin.getIndex()] = new PosLoginEx(posLoginHttpBO, staffLoginHttpBO, logoutHttpBO);
        programEx[ProgramEx.EnumProgramType.EPT_RetailTrade.getIndex()] = new RetailTradeEx(wxPayHttpBO, retailTradeSQLiteBO, retailTradeSQLiteEvent, retailTradeHttpEvent, retailTradeHttpBO);
        programEx[ProgramEx.EnumProgramType.EPT_SyncData.getIndex()] = new SyncDataEx();
        programEx[ProgramEx.EnumProgramType.EPT_RetailTradeAggregation.getIndex()] = new RetailTradeAggregationEx(retailTradeAggregationSQLiteBO, retailTradeAggregationHttpBO, retailTradeAggregationSQLiteEvent,  retailTradeAggregationHttpEvent);
    }

    public boolean run(RobotClientEx robotEx) throws Exception {
        // 登录
        if (!programEx[ProgramEx.EnumProgramType.EPT_PosLogin.getIndex()].run(errorInfo)) {
            return false;
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        calendarStart.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
        workTimeStart = calendarStart.getTime();
        // 开机同步
        if (!programEx[ProgramEx.EnumProgramType.EPT_SyncData.getIndex()].run(errorInfo)) {
            return false;
        }
        /** 登录开机同步, 开机同步是异步操作，同步完完才继续往下走 */
        while (!ShopRobotTestEx.bfinishedSyncData) {
            Thread.sleep(1000);
        }
        // 准备金、收银汇总
        ShopRobotTest.caseLog("上班，创建收银汇总");
        BaseActivity.retailTradeAggregation.setReserveAmount(new Random().nextInt(10000));
        BaseActivity.retailTradeAggregation.setWorkTimeStart(workTimeStart);
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));//下班时间的初始值比上班时间多一秒
        if(!createRetailTradeAggregation()) {
            ShopRobotTest.caseLog("本地创建收银汇总失败");
            return false;
        }
        /** pos登录，开机同步*/
        // nbr是否告诉pos断开连接
        while (!robotEx.getClientHandlerEx().isCloseConnect()) {
            // 是否下班
            if (ClientHandlerEx.bLogOut) {
                // 上传零售单
                if (!syncRetailTrade(errorInfo)) {
                    return false;
                }
                // 上传收银汇总
                if (!programEx[ProgramEx.EnumProgramType.EPT_RetailTradeAggregation.getIndex()].run(errorInfo)) {
                    return false;
                }
                // 下班
                if (!doStaffLogout(errorInfo)) {
                    return false;
                }
                // 下一天
                synchronized (this) {
                    ShopRobotTestEx.dayNumber++;
                }
                // 登录
                if (!programEx[ProgramEx.EnumProgramType.EPT_PosLogin.getIndex()].run(errorInfo)) {
                    return false;
                }
                // 开机同步
                if (!programEx[ProgramEx.EnumProgramType.EPT_SyncData.getIndex()].run(errorInfo)) {
                    return false;
                }
                /** 登录开机同步完才继续往下走 */
                while (!ShopRobotTestEx.bfinishedSyncData) {
                    Thread.sleep(1000);
                }
                // 重置bfinishedSyncData
                ShopRobotTestEx.bfinishedSyncData = false;
                // 完成登录、开机同步，修改workTimeStart的时间加一天，创建本地的收银汇总
                Calendar calendarStartAfterLogout = Calendar.getInstance();
                calendarStartAfterLogout.setTime(new Date());
                calendarStartAfterLogout.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
                workTimeStart = calendarStartAfterLogout.getTime();
                // 登录完成后需要创建一个收银汇总
                ShopRobotTest.caseLog("上班，创建收银汇总");
                BaseActivity.retailTradeAggregation.setWorkTimeStart(workTimeStart);
                BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));//下班时间的初始值比上班时间多一秒
                BaseActivity.retailTradeAggregation.setReserveAmount(new Random().nextInt(10000));
                if(!createRetailTradeAggregation()) {
                    ShopRobotTest.caseLog("本地创建收银汇总失败");
                    return false;
                }
                ClientHandlerEx.bLogOut = false;
                HeaderEx headerEx = new HeaderEx();
                headerEx.setBodyLength(0);
                headerEx.setCommand(HeaderEx.EnumCommandType.ECT_DoneLogout.getIndex());
                StringBuilder sb = new StringBuilder();
                sb.append(headerEx.toBufferString());
                System.err.println("发送消息" + new Date());
                robotEx.getSession().write(sb.toString());
            }
            // nbr告诉pos运行一个活动
            if (ClientHandlerEx.canRunNow) {
                if (!programEx[ProgramEx.EnumProgramType.EPT_RetailTrade.getIndex()].run(errorInfo)) {
                    return false;
                }
                // 活动结束，告诉nbr已完成
                ClientHandlerEx.canRunNow = false;
                HeaderEx headerEx = new HeaderEx();
                headerEx.setBodyLength(0);
                headerEx.setCommand(HeaderEx.EnumCommandType.ECT_DoneRun.getIndex());
                StringBuilder sb = new StringBuilder();
                sb.append(headerEx.toBufferString());
                System.err.println("发送消息" + new Date());
                robotEx.getSession().write(sb.toString());
            }
            Thread.sleep(1000);
        }
        return true;
    }

    private boolean syncRetailTrade(StringBuilder sbError) throws InterruptedException {
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("查询临时零售单失败！", false);
        }
        // 等待上传完毕
        long lTimeOut = 60;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            System.out.println("上传临时零售单同步数据失败！原因：超时！");
            return false;
        }
        return true;
    }

    private boolean doStaffLogout(StringBuilder sbError) {
        if (!logoutHttpBO.logoutAsync()) {
            sbError.append("退出登录失败! ");
            return false;
        }
        System.out.println("成功退出登录");
        return true;
    }

    /**
     * 用户设置ReserveAmount后，创建收银汇总到本地sqlite中
     */
    private boolean createRetailTradeAggregation() throws InterruptedException {
        BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
        //准备金被点确定之前，day end 收银汇总 dialog已经弹出来。当dialog消失时，如果不设置work time start为第2天的时间，会导致work time start是前一天的时间。
        //这不符合我们的业务逻辑且在上传时，因为day end dialog结束时已经创建了一个retailTradeAggregation在SQLite中，现在用户点击确定后，又创建了一个retailTradeAggregation。这种情况无法避免但是暂时可以接受。
        //我们规定收银汇总必须是当天的，不可以跨天。
        BaseActivity.retailTradeAggregation.setWorkTimeStart(DatetimeUtil.addSecond(new Date(), 1));
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));
        //
        RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
        retailTradeAggregation.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
        retailTradeAggregation.setPosID(BaseActivity.retailTradeAggregation.getPosID());
        retailTradeAggregation.setWorkTimeStart(BaseActivity.retailTradeAggregation.getWorkTimeStart());
        retailTradeAggregation.setReserveAmount(BaseActivity.retailTradeAggregation.getReserveAmount());
        retailTradeAggregation.setWorkTimeEnd(BaseActivity.retailTradeAggregation.getWorkTimeEnd());
        //
        retailTradeAggregationSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTradeAggregation_CreateAsync);
        if (!retailTradeAggregationSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTradeAggregation)) {
            ShopRobotTestEx.caseLog("创建零售单汇总失败");
            return false;
        }
        int lTimeout = 50;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            throw new RuntimeException("收银汇总上传失败！！原因：超时");
        }
        return true;
    }
}
