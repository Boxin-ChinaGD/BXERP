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
        // ??????
        if (!programEx[ProgramEx.EnumProgramType.EPT_PosLogin.getIndex()].run(errorInfo)) {
            return false;
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        calendarStart.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
        workTimeStart = calendarStart.getTime();
        // ????????????
        if (!programEx[ProgramEx.EnumProgramType.EPT_SyncData.getIndex()].run(errorInfo)) {
            return false;
        }
        /** ??????????????????, ???????????????????????????????????????????????????????????? */
        while (!ShopRobotTestEx.bfinishedSyncData) {
            Thread.sleep(1000);
        }
        // ????????????????????????
        ShopRobotTest.caseLog("???????????????????????????");
        BaseActivity.retailTradeAggregation.setReserveAmount(new Random().nextInt(10000));
        BaseActivity.retailTradeAggregation.setWorkTimeStart(workTimeStart);
        BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));//????????????????????????????????????????????????
        if(!createRetailTradeAggregation()) {
            ShopRobotTest.caseLog("??????????????????????????????");
            return false;
        }
        /** pos?????????????????????*/
        // nbr????????????pos????????????
        while (!robotEx.getClientHandlerEx().isCloseConnect()) {
            // ????????????
            if (ClientHandlerEx.bLogOut) {
                // ???????????????
                if (!syncRetailTrade(errorInfo)) {
                    return false;
                }
                // ??????????????????
                if (!programEx[ProgramEx.EnumProgramType.EPT_RetailTradeAggregation.getIndex()].run(errorInfo)) {
                    return false;
                }
                // ??????
                if (!doStaffLogout(errorInfo)) {
                    return false;
                }
                // ?????????
                synchronized (this) {
                    ShopRobotTestEx.dayNumber++;
                }
                // ??????
                if (!programEx[ProgramEx.EnumProgramType.EPT_PosLogin.getIndex()].run(errorInfo)) {
                    return false;
                }
                // ????????????
                if (!programEx[ProgramEx.EnumProgramType.EPT_SyncData.getIndex()].run(errorInfo)) {
                    return false;
                }
                /** ??????????????????????????????????????? */
                while (!ShopRobotTestEx.bfinishedSyncData) {
                    Thread.sleep(1000);
                }
                // ??????bfinishedSyncData
                ShopRobotTestEx.bfinishedSyncData = false;
                // ????????????????????????????????????workTimeStart????????????????????????????????????????????????
                Calendar calendarStartAfterLogout = Calendar.getInstance();
                calendarStartAfterLogout.setTime(new Date());
                calendarStartAfterLogout.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
                workTimeStart = calendarStartAfterLogout.getTime();
                // ?????????????????????????????????????????????
                ShopRobotTest.caseLog("???????????????????????????");
                BaseActivity.retailTradeAggregation.setWorkTimeStart(workTimeStart);
                BaseActivity.retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.addSecond(BaseActivity.retailTradeAggregation.getWorkTimeStart(), 1));//????????????????????????????????????????????????
                BaseActivity.retailTradeAggregation.setReserveAmount(new Random().nextInt(10000));
                if(!createRetailTradeAggregation()) {
                    ShopRobotTest.caseLog("??????????????????????????????");
                    return false;
                }
                ClientHandlerEx.bLogOut = false;
                HeaderEx headerEx = new HeaderEx();
                headerEx.setBodyLength(0);
                headerEx.setCommand(HeaderEx.EnumCommandType.ECT_DoneLogout.getIndex());
                StringBuilder sb = new StringBuilder();
                sb.append(headerEx.toBufferString());
                System.err.println("????????????" + new Date());
                robotEx.getSession().write(sb.toString());
            }
            // nbr??????pos??????????????????
            if (ClientHandlerEx.canRunNow) {
                if (!programEx[ProgramEx.EnumProgramType.EPT_RetailTrade.getIndex()].run(errorInfo)) {
                    return false;
                }
                // ?????????????????????nbr?????????
                ClientHandlerEx.canRunNow = false;
                HeaderEx headerEx = new HeaderEx();
                headerEx.setBodyLength(0);
                headerEx.setCommand(HeaderEx.EnumCommandType.ECT_DoneRun.getIndex());
                StringBuilder sb = new StringBuilder();
                sb.append(headerEx.toBufferString());
                System.err.println("????????????" + new Date());
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
            Assert.assertTrue("??????????????????????????????", false);
        }
        // ??????????????????
        long lTimeOut = 60;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            System.out.println("????????????????????????????????????????????????????????????");
            return false;
        }
        return true;
    }

    private boolean doStaffLogout(StringBuilder sbError) {
        if (!logoutHttpBO.logoutAsync()) {
            sbError.append("??????????????????! ");
            return false;
        }
        System.out.println("??????????????????");
        return true;
    }

    /**
     * ????????????ReserveAmount?????????????????????????????????sqlite???
     */
    private boolean createRetailTradeAggregation() throws InterruptedException {
        BaseActivity.retailTradeAggregation.setPosID(Constants.posID);
        //??????????????????????????????day end ???????????? dialog?????????????????????dialog???????????????????????????work time start??????2????????????????????????work time start????????????????????????
        //?????????????????????????????????????????????????????????day end dialog??????????????????????????????retailTradeAggregation???SQLite??????????????????????????????????????????????????????retailTradeAggregation??????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
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
            ShopRobotTestEx.caseLog("???????????????????????????");
            return false;
        }
        int lTimeout = 50;
        while (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeAggregationSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            throw new RuntimeException("?????????????????????????????????????????????");
        }
        return true;
    }
}
