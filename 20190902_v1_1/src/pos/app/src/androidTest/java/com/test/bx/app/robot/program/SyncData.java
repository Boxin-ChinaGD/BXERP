package com.test.bx.app.robot.program;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.bo.CommoditySQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.RetailTradeAggregationHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.event.UI.RetailTradeAggregationSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.event.UI.VipSQLiteEvent;
import com.bx.erp.event.VipHttpEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.*;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;
import com.test.bx.app.robot.Config;
import com.test.bx.app.robot.Robot;
import com.test.bx.app.robot.RobotConfig;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.protocol.Header;


import org.junit.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.test.bx.app.robot.Robot.waitSyncDone;


public class SyncData extends Program {
    private List<SmallSheetFrame> tempSmallSheetFrameList = new ArrayList<SmallSheetFrame>();//用于保存POS机启动时在SQLite找到的需要上传的SmallSheet数据
    private List<BaseModel> tempRetailTradeList = new ArrayList<BaseModel>();//用于保存POS机启动时在SQLite找到的需要上传的RetailTrade数据
    private List<com.bx.erp.model.RetailTradeAggregation> tempRetailTradeAggregationList = new ArrayList<RetailTradeAggregation>();//用于保存POS机启动时在SQLite找到的需要上传的RetailTradeAggregation数据
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    private SmallSheetSQLiteEvent smallSheetSQLiteEvent;
    private RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent;

    private static CommoditySQLiteBO commoditySQLiteBO = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    public static final int Event_ID_SyncData = 100000;

    public static boolean bSendToNbr = false;

    @Override
    protected void generateReport() {

    }

    @Override
    protected boolean doLoadProgramUnit() {
        // TODO 这里是否需要将同步上传小票、零售单、收银汇总、同步商品作为一个活动
        if (!bRunInRandomMode) {
            Map<String, List<String>> mapMachineMeal = mapBaseModels.get(Config.KEY_NAME_MyMeal);//TODO
            Map<String, List<String>> mapActivitySheet = mapBaseModels.get(Config.Acitivity_NO[1]);
            for (int i = 1; i < mapActivitySheet.size(); i++) {
                List<String> rowMachineMeal = mapMachineMeal.get(i + "");
                if (rowMachineMeal == null) {
                    continue;
                }
//            for (List<String> rowMachineMeal : mapMachineMeal.values()) {
                if ("同步".equals(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
                    int activitySequence = Integer.parseInt(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_ProgramUnitNO.getIndex()));
                    ProgramUnit pu = new ProgramUnit();
                    pu.setNo(activitySequence);
                    pu.setOperationType(EnumSyncDataType.SDT_SmallSheet.getIndex());
                    queueIn.offer(pu);
                    ProgramUnit pu2 = new ProgramUnit();
                    pu2.setNo(activitySequence);
                    pu2.setOperationType(EnumSyncDataType.SDT_RetailTradeAggregation.getIndex());
                    queueIn.offer(pu2);
                    ProgramUnit pu3 = new ProgramUnit();
                    pu3.setNo(activitySequence);
                    pu3.setOperationType(EnumSyncDataType.SDT_RetailTrade.getIndex());
                    queueIn.offer(pu3);
                    ProgramUnit pu4 = new ProgramUnit();
                    pu4.setNo(activitySequence);
                    pu4.setOperationType(EnumSyncDataType.SDT_Commodity.getIndex());
                    queueIn.offer(pu4);
                }
            }
        }
        return true;
    }

    public SyncData(Date startDatetime, Date endDatetime, final RobotConfig rc, RetailTradeSQLiteBO retailTradeSQLiteBO, RetailTradeSQLiteEvent retailTradeSQLiteEvent, SmallSheetSQLiteEvent smallSheetSQLiteEvent,
                    RetailTradeAggregationSQLiteEvent retailTradeAggregationSQLiteEvent, boolean bRunInRandomMode) {
        super(startDatetime, endDatetime, rc, bRunInRandomMode);
        this.retailTradeSQLiteBO = retailTradeSQLiteBO;
        this.retailTradeSQLiteEvent = retailTradeSQLiteEvent;
        this.smallSheetSQLiteEvent = smallSheetSQLiteEvent;
        this.retailTradeAggregationSQLiteEvent = retailTradeAggregationSQLiteEvent;

        initBOAndEvent();
    }

    public enum EnumSyncDataType {
        SDT_SmallSheet("SDT_SmallSheet", 0),
        SDT_RetailTradeAggregation("SDT_RetailTradeAggregation", 1),
        SDT_RetailTrade("SDT_RetailTrade", 2),
        SDT_Commodity("SDT_Commodity", 3);

        private String name;
        private int index;

        private EnumSyncDataType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumSyncDataType ept : EnumSyncDataType.values()) {
                if (ept.getIndex() == index) {
                    return ept.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }
    }

    private void initBOAndEvent() {
        //
        if (commodityHttpEvent == null) {
            commodityHttpEvent = new CommodityHttpEvent();
            commodityHttpEvent.setId(Event_ID_SyncData);
        }
        if (commoditySQLiteEvent == null) {
            commoditySQLiteEvent = new CommoditySQLiteEvent();
            commoditySQLiteEvent.setId(Event_ID_SyncData);
        }
        if (commodityHttpBO == null) {
            commodityHttpBO = new CommodityHttpBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        if (commoditySQLiteBO == null) {
            commoditySQLiteBO = new CommoditySQLiteBO(GlobalController.getInstance().getContext(), commoditySQLiteEvent, commodityHttpEvent);
        }
        commodityHttpEvent.setHttpBO(commodityHttpBO);
        commodityHttpEvent.setSqliteBO(commoditySQLiteBO);
        commoditySQLiteEvent.setHttpBO(commodityHttpBO);
        commoditySQLiteEvent.setSqliteBO(commoditySQLiteBO);
    }

    @Override
    public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) throws InterruptedException {
        if (!bRunInRandomMode) {
            int i = 0;
            while (!queueIn.isEmpty()) {
                ProgramUnit pu = queueIn.peek();
                if (ShopRobotTest.activitySequence >= pu.getNo()) {
                    i++;
                    // 如果已经到了
                    pu = queueIn.poll();
                    switch (EnumSyncDataType.values()[pu.getOperationType()]) {
                        case SDT_SmallSheet:
//                            if (!syncSmallSheet(sbError)) {
//                                return false;
//                            }
                            break;
                        case SDT_RetailTradeAggregation:
                            if (!syncRetailTradeAggregation(sbError)) {
                                return false;
                            }
                            break;
                        case SDT_RetailTrade:
                            if (!syncRetailTrade(sbError)) {
                                return false;
                            }
                            break;
                        case SDT_Commodity:
//                            if (!syncCommodity(sbError)) {
//                                return false;
//                            }
                            break;
                        default:
                            break;
                    }
                    queueOut.offer(pu);
                    System.out.println("Pos上传同步完毕,序列号：" + pu.getNo() + "时间：" + ShopRobotTest.sdf.format(new Date()));
                    // 告诉nbr同步完毕
                    // 这里的同步上传有4个小活动，定义一个bSendToNbr变量保证一次同步上传只发送一次消息
                    if (i==4) {
                        Header header = new Header();
                        header.setActivitySequence(pu.getNo());
                        header.setCommand(Header.EnumCommandType.ECT_DoneSyncData.getIndex());
                        header.setPosName("1111");
                        StringBuilder sb = new StringBuilder();
                        pu.setDatetimeStart(new Date());
                        String body = pu.toJson(pu);
                        header.setBodyLength(body.length());
                        sb.append(header.toBufferString());
                        sb.append(body);
                        ShopRobotTest.clientSession.write(sb.toString());
                        bSendToNbr = true;
                        i=0;
                    }
                } else {
                    break;
                }
            }
            if (bSendToNbr) {
                bSendToNbr = false;
            }
        } else {
            tempSmallSheetFrameList = Robot.retrieveNSmallSheetTempDataInSQLite();
//        tempRetailTradeList = Shared.retrieveNRetailTradeTempDataInSQLite();
            tempRetailTradeAggregationList = Robot.retrieveNRetailTradeAggregationTempDataInSQLite();// TODO: 2019/8/9
            if (tempSmallSheetFrameList != null) {
                if (tempSmallSheetFrameList.size() > 0) {
                    try {
                        ShopRobotTest.tempSmallSheetFrameList = tempSmallSheetFrameList;
                        smallSheetSQLiteEvent.setId(Robot.ROBOT_EVENT_ID_STARTUP_SYNC); // TODO: 2019/8/9
                        Robot.uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sbError.append("上传小票格式失败！");
                        return false;
                    }
                } else {
                    if (tempRetailTradeList != null) {
                        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                        retailTradeSQLiteEvent.setId(Robot.ROBOT_EVENT_ID_STARTUP_SYNC);
                        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                            Assert.assertTrue("查询临时零售单失败！", false);
                        }
//                    List<?> retailTradeList = retailTradeSQLiteBO.retrieveNSync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null);
//                    if (retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//                        Assert.assertTrue("查询临时零售单失败！", false);
//                    }
//                    waitSyncDone();//...这里是干嘛的？

                    }
                    if (tempRetailTradeAggregationList != null) {
                        if (tempRetailTradeAggregationList.size() > 0) {
                            ShopRobotTest.tempRetailTradeAggregationList = tempRetailTradeAggregationList;
                            retailTradeAggregationSQLiteEvent.setId(Robot.ROBOT_EVENT_ID_STARTUP_SYNC);
                            Robot.uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
                        } else {
                            Robot.syncTime(); //...
                            waitSyncDone();//...等待同步
                        }
                    }
//                    }
                }
            }
        }
        return true;
    }

    @Override
    protected boolean canRunNow(Date d) {
        return false;
    }

    @Override
    public void resetForNextDay() {

    }

    private boolean syncSmallSheet(StringBuilder sbError) throws InterruptedException {
        tempSmallSheetFrameList = Robot.retrieveNSmallSheetTempDataInSQLite();
        if (tempSmallSheetFrameList != null) {
            if (tempSmallSheetFrameList.size() > 0) {
                try {
                    ShopRobotTest.tempSmallSheetFrameList = tempSmallSheetFrameList;
                    smallSheetSQLiteEvent.setId(Robot.ROBOT_EVENT_ID_STARTUP_SYNC); // TODO: 2019/8/9
                    Robot.uploadSmallSheetTempData(tempSmallSheetFrameList.get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    sbError.append("上传小票格式失败！");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean syncRetailTradeAggregation(StringBuilder sbError) throws InterruptedException {
        Robot.syncTime(); //
        tempRetailTradeAggregationList = Robot.retrieveNRetailTradeAggregationTempDataInSQLite();// TODO: 2019/8/9
        if (tempRetailTradeAggregationList != null) {
            if (tempRetailTradeAggregationList.size() > 0) {
                ShopRobotTest.tempRetailTradeAggregationList = tempRetailTradeAggregationList;
                retailTradeAggregationSQLiteEvent.setId(Robot.ROBOT_EVENT_ID_STARTUP_SYNC);
                Robot.uploadRetailTradeAggregationTempData(tempRetailTradeAggregationList.get(0));
            } else {
                Robot.syncTime(); //...
                waitSyncDone();//...等待同步
            }
        }
        return true;
    }

    private boolean syncRetailTrade(StringBuilder sbError) throws InterruptedException {
        if (tempRetailTradeList != null) {
            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
            retailTradeSQLiteEvent.setId(Robot.ROBOT_EVENT_ID_STARTUP_SYNC);
            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                Assert.assertTrue("查询临时零售单失败！", false);
            }
            // 等待上传完毕
            long lTimeOut = 600;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && //
                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                System.out.println("上传临时零售单同步数据失败！原因：超时！");
                return false;
            }
        }
        return true;
    }

    private boolean syncCommodity(StringBuilder sbError) throws InterruptedException {
        // 同步nbr机器人新建的商品
        if (!commodityHttpBO.retrieveNAsync(BaseHttpBO.INVALID_CASE_ID, null)) {
            sbError.append("RN请求需要同步的Commodity失败！");
            return false;
        }
        //
        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        while (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData
                && commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (commoditySQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
            Assert.assertTrue("请求同步Commodity超时！", false);
        }
        //
        if (commoditySQLiteBO.getSqLiteEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            Assert.assertTrue("同步服务器返回的商品失败", false);
        }
        List<Commodity> commodityList = (List<Commodity>) commoditySQLiteBO.getSqLiteEvent().getListMasterTable();
        Assert.assertTrue("同步RN不应该没有数据返回", commodityList.size() > 0);
        return true;
    }

    private boolean feedRetailTradeProgram(Program[] programs) {
        ProgramUnit pu = new ProgramUnit();
        pu.setDatetimeStart(DatetimeUtil.addMinutes(getRobotStartDatetime(), 0)); // 这个时间零售商品的
        pu.setOperationType(RetailTrade.EnumOperationType.EOT_CreateRetailTrade.getIndex());
        feed(pu, programs[EnumProgramType.EPT_RetailTrade.getIndex()]);
        return true;
    }
}
