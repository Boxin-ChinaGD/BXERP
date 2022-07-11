package com.test.bx.app.robot.program;

import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradePromotingHttpBO;
import com.bx.erp.bo.RetailTradePromotingSQLiteBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.RetailTradePromotingHttpEvent;
import com.bx.erp.event.RetailTradePromotingSQLiteEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.event.WXPayHttpEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradePromoting;
import com.bx.erp.model.RetailTradePromotingFlow;
import com.bx.erp.model.WXPayInfo;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.presenter.RetailTradePromotingFlowPresenter;
import com.bx.erp.presenter.RetailTradePromotingPresenter;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.PoiUtils;
import com.bx.erp.utils.Shared;
import com.bx.erp.utils.StringUtils;
import com.bx.erp.utils.WXPayUtil;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.robot.Config;
import com.test.bx.app.robot.Robot;
import com.test.bx.app.robot.RobotConfig;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.client.ClientHandler;
import com.test.bx.app.robot.protocol.Header;

import junit.framework.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class RetailTrade extends Program {
    /**
     * 每一个顾客一次购买的商品的最大种类数。一个种类由一个商品ID标识
     */
    private final int MAX_NOOfCommodityPerTrade = 20;
    /**
     * 每一个顾客一次购买的单个商品的最大数目
     */
    private final int MAX_NOPerCommodity = 10;

    private Map<String, String> microPayResponse = new HashMap<>();//微信支付responseData

    private RetailTradePresenter retailTradePresenter;
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    private RetailTradeHttpBO retailTradeHttpBO;
    private RetailTradeHttpEvent retailTradeHttpEvent;
    private WXPayHttpBO wxPayHttpBO;
    //
    private static RetailTradePromotingSQLiteBO retailTradePromotingSQLiteBO = null;
    private static RetailTradePromotingSQLiteEvent retailTradePromotingSQLiteEvent = null;
    private static RetailTradePromotingHttpEvent retailTradePromotingHttpEvent = null;
    private static RetailTradePromotingHttpBO retailTradePromotingHttpBO = null;
    public static final int Event_ID_RetailTrade = 100000;
    public static final int queryRetailTradeByNoConditions = 0;
    public static final int queryRetailTradeBySNConditions = 1;
    public static final int queryRetailTradeByTimeConditions = 2;
    public static final int queryRetailTradeBySNandTimeConditions = 3;

    //
    public RetailTrade(Date startDatetime, Date endDatetime, final RobotConfig rc, RetailTradeHttpEvent retailTradeHttpEvent, RetailTradeSQLiteBO retailTradeSQLiteBO, WXPayHttpBO wxPayHttpBO, RetailTradeSQLiteEvent retailTradeSQLiteEvent, boolean bRunInRandomMode) {
        super(startDatetime, endDatetime, rc, bRunInRandomMode);
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        listRT = new ArrayList<com.bx.erp.model.RetailTrade>();
        listRTIsFromNBR = new ArrayList<com.bx.erp.model.RetailTrade>();
//        willUploadRetailTradeList = new ArrayList<BaseModel>();
        this.retailTradeSQLiteBO = retailTradeSQLiteBO;
        this.wxPayHttpBO = wxPayHttpBO;
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
//        retailTradeHttpBO = GlobalController.getInstance().getRetailTradeHttpBO();
        this.retailTradeHttpEvent = retailTradeHttpEvent;
        this.retailTradeSQLiteEvent = retailTradeSQLiteEvent;

        initEventAndBO();
    }

    /**
     * 跟APP端的Config.EnumRetailTradeOperationType一一对应。若改这里，需要改那里
     */
    public enum EnumOperationType {
        EOT_CreateRetailTrade("EOT_CreateRetailTrade", 0),
        EOT_CreateReturnRetailTrade("EOT_CreateReturnRetailTrade", 1), //
        EOT_QueryRetailTrade("EOT_QueryRetailTrade", 2); //

        private String name;
        private int index;

        private EnumOperationType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumOperationType ept : EnumOperationType.values()) {
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

    private void initEventAndBO() {
        //
        if (retailTradePromotingSQLiteEvent == null) {
            retailTradePromotingSQLiteEvent = new RetailTradePromotingSQLiteEvent();
            retailTradePromotingSQLiteEvent.setId(Event_ID_RetailTrade);
        }
        if (retailTradePromotingHttpEvent == null) {
            retailTradePromotingHttpEvent = new RetailTradePromotingHttpEvent();
            retailTradePromotingHttpEvent.setId(Event_ID_RetailTrade);
        }
        if (retailTradePromotingHttpBO == null) {
            retailTradePromotingHttpBO = new RetailTradePromotingHttpBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        if (retailTradePromotingSQLiteBO == null) {
            retailTradePromotingSQLiteBO = new RetailTradePromotingSQLiteBO(GlobalController.getInstance().getContext(), retailTradePromotingSQLiteEvent, retailTradePromotingHttpEvent);
        }
        retailTradePromotingSQLiteEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingSQLiteEvent.setHttpBO(retailTradePromotingHttpBO);
        retailTradePromotingHttpEvent.setSqliteBO(retailTradePromotingSQLiteBO);
        retailTradePromotingHttpEvent.setHttpBO(retailTradePromotingHttpBO);
    }

    public RetailTrade() {

    }

    public void setMaxTradePerDay(int iMaxTradePerDay) {
        this.maxTradePerDay = iMaxTradePerDay;
    }

    public List<com.bx.erp.model.RetailTrade> getListRT() {
        return listRT;
    }

    /**
     * 记录今天生成的零售单
     */
    protected List<com.bx.erp.model.RetailTrade> listRT;

    /**
     * 记录今天生成的零售单
     */
    protected List<com.bx.erp.model.RetailTrade> listRTIsFromNBR;

    public List<com.bx.erp.model.RetailTrade> getListRTIsFromNBR() {
        return listRTIsFromNBR;
    }

//    /**
//     * 记录间隔上传的零售单
//     */
//    protected List<BaseModel> willUploadRetailTradeList;

    /**
     * 每天最多能收银多少次。默认为200
     */
    protected int maxTradePerDay = 200;

    public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) throws Exception {
        if (!bRunInRandomMode) {
            while (!queueIn.isEmpty()) {
                ProgramUnit pu = queueIn.peek();
                if (ShopRobotTest.activitySequence >= pu.getNo()) {
                    pu = queueIn.poll();
                    do {
                        switch (EnumOperationType.values()[pu.getOperationType()]) {
                            // 创建零售单
                            case EOT_CreateRetailTrade:
                                if (!createRetailTrade(currentDatetime, sbError, bRunInRandomMode, Program.ReadExcelMode, pu.getBaseModelIn1())) {
                                    return false;
                                }
                                // 创建后需要把pos机器蜜发给nbr机器人，所以上传同步创建好的零售单给服务器，拿到服务器返回的真实零售单(机器蜜)
                                uploadTempRetailTrade(pu);
                                break;
                            // 创建退货型零售单
                            case EOT_CreateReturnRetailTrade:
                                if (!createReturnRetailTrade(sbError, pu.getBaseModelIn1())) {
                                    return false;
                                }
                                uploadTempRetailTrade(pu);
                                break;
                            case EOT_QueryRetailTrade:
                                if (!queryRetailTrade(1, sbError)) {
                                    return false;
                                }
                                break;
                            default:
                                break;
                        }
                    } while (false);
                    queueOut.add(pu);
                    System.out.println("Pos创建零售单完毕,序列号：" + pu.getNo() + "时间：" + ShopRobotTest.sdf.format(new Date()));
                    // 告诉nbr创建零售单完毕
                    Header header = new Header();
                    header.setCommand(Header.EnumCommandType.ECT_DoneCreateRetailTrade.getIndex());
                    header.setActivitySequence(pu.getNo());
                    header.setBodyLength(0);
                    header.setPosName("1111");
                    pu.setDatetimeStart(new Date());
                    String body = pu.toJson(pu);
                    header.setBodyLength(body.length());
                    StringBuilder sb = new StringBuilder();
                    sb.append(header.toBufferString());
                    sb.append(body);
                    ShopRobotTest.clientSession.write(sb.toString());
                    // pos执行到活动9就让pos退出运行
//                    if(pu.getNo() == 16) {
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        ShopRobotTest.bIsDone = true;
//                    }
                } else {
                    break;
                }
            }
        } else {
            do {
                if (canRunNow(currentDatetime)) {
                    // 收银1单。如果有错，记录在sbError中
                    List<Commodity> listComm = ShopRobotTest.getCommodityList(MAX_NOOfCommodityPerTrade, false, sbError, bRunInRandomMode);
                    if (listComm == null || listComm.size() == 0) {
                        sbError.append("查找本地商品失败！");
                        break;
                    }
                    //
                    com.bx.erp.model.RetailTrade rt = null;
                    try {
                        rt = createRetailTradeInSQLite(listComm, currentDatetime, sbError, wxPayHttpBO, bRunInRandomMode);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (rt == null) {
                        sbError.append("创建零售单在SQLite中失败");
                        break;
                    }
                    //
                    listRT.add(rt);

                    // 每十张零售单生成一张退货
                    if (listRT.size() % 10 == 0) {
                        try {
                            // 上传临时零售单
                            retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                            retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
                            if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
                                Assert.assertTrue("查找临时零售单失败！", false);
                            }

                            lTimeout = 60;
                            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
                                Thread.sleep(1000);
                            }
                            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                                    retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
                                System.out.println("ShopRobot 上传临时零售单同步数据失败！原因：超时！");
                            }

                            List<com.bx.erp.model.RetailTrade> retailTrades = (List<com.bx.erp.model.RetailTrade>) retailTradeHttpEvent.getListMasterTable();
                            Assert.assertTrue("返回的零售单List为null", retailTrades != null && retailTrades.size() != 0);
                            // 保存服务器返回的零售单
                            for (com.bx.erp.model.RetailTrade retailTrade : retailTrades) {
                                listRTIsFromNBR.add(retailTrade);
                            }

                            // 根据服务器返回的零售单创建退货单
                            com.bx.erp.model.RetailTrade retailTrade = retailTrades.get(new Random().nextInt(retailTrades.size()));

                            Assert.assertTrue("根据服务器返回的零售单创建退货单其从表为null", retailTrade.getListSlave1().size() > 0);

                            com.bx.erp.model.RetailTrade returnRetailTrade = createReturnRetailTradeInSQLite(retailTrade, currentDatetime, sbError);
                            System.out.println("====生成的退货单为：" + returnRetailTrade);
                            Assert.assertTrue("零售单创建退货单失败！！", returnRetailTrade != null);
                            if (retailTrade.getPaymentType() == 4) {
                                com.bx.erp.model.RetailTrade refundRT = (com.bx.erp.model.RetailTrade) returnRetailTrade.clone();
                                refundRT.setAmount(WXPayUtil.formatAmount(refundRT.getAmount()));
                                Map<String, String> refundResponse = wxRefund(refundRT);
                                // 根据退货零售单微信交易单号进行退款申请
                                if (refundResponse == null) {
                                    System.out.println("微信申请退款失败！使用现金进行退款");
                                    listRT.add(returnRetailTrade);
                                } else {
                                    // 更新退货零售单(update退款交易单号)
                                    com.bx.erp.model.RetailTrade returnRetailTradeWithRefundNO = updateRetailTrade(returnRetailTrade, refundResponse.get("out_refund_no"));
                                    System.out.println("微信退款单号：" + returnRetailTradeWithRefundNO.getWxRefundNO());
                                    listRT.add(returnRetailTradeWithRefundNO);
                                }
                            } else {
                                listRT.add(returnRetailTrade);
                            }

                            lastExecutionDatetime = currentDatetime;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Random r = new Random();
                    nextScheduledRunDatetime = DatetimeUtil.addMinutes(currentDatetime, r.nextInt(10));

                    executionNO++;
                }

            } while (false);
        }
        return true;
    }


    @Override
    protected void generateReport() {

    }

    @Override
    protected boolean canRunNow(Date currentDatetime) {
        if (executionNO >= maxTradePerDay) {
            return false;
        }
        //
        if (executionNO == 0) {
            Random r = new Random();
            nextScheduledRunDatetime = DatetimeUtil.addMinutes(currentDatetime, r.nextInt(20));//生成下一次执行的最早时间
            lastExecutionDatetime = currentDatetime;
            return true;
        }
        //
        if (currentDatetime.getTime() >= nextScheduledRunDatetime.getTime()) {
            return true;
        }

        return false;
    }

    protected com.bx.erp.model.RetailTrade createRetailTradeInSQLite(List<Commodity> listComm, Date currentDatetime, StringBuilder sbError, WXPayHttpBO wxPayHttpBO, boolean bRunInRandomMode) throws ParseException {
        com.bx.erp.model.RetailTrade rt = new com.bx.erp.model.RetailTrade();
        if (!bRunInRandomMode) {
            try {
                long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    sbError.append("查找本地零售单最大ID失败！");
                    return null;
                }
                rt.setStatus(1);
                rt.setID(maxFrameIDInSQLite);
                rt.setSaleDatetime(currentDatetime);
                rt.setDatetimeStart(new Date());
                rt.setDatetimeEnd(new Date());
                rt.setSourceID(-1);
                rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setAmountWeChat(listComm.get(0).getPriceRetail());
                rt.setAmount(GeneralUtil.sumN(rt.getAmountCash(), rt.getAmountAlipay(), rt.getAmountWeChat(), rt.getAmount1(), rt.getAmount2(), rt.getAmount3(), rt.getAmount4(), rt.getAmount5()));

                rt.setPaymentType(4);
                rt.setSmallSheetID(1); //... 将来使用当前使用的小票格式
                rt.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                rt.setPos_ID(Constants.posID);
                rt.setLocalSN((int) maxFrameIDInSQLite);
                rt.setSn(rt.generateRetailTradeSN(Constants.posID));
                rt.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setReturnObject(1);
                rt.setSyncType(BasePresenter.SYNC_Type_C);

                // 在沙箱环境中模拟支付该零售单
                com.bx.erp.model.RetailTrade microPayRT = (com.bx.erp.model.RetailTrade) rt.clone();
                if (!wxMicroPay(microPayRT, sbError)) {
                    sbError.append("--Pos机器人沙箱微信支付失败，使用现金支付--");
                    rt.setPaymentType(1);
                    rt.setAmountCash(rt.getAmountWeChat());
                    rt.setAmountWeChat(0.00000d);
                } else {
                    // 沙箱环境的微信支付成功
                    rt.setWxRefundSubMchID(Constants.submchid);
                    rt.setWxOrderSN(microPayResponse.get("transaction_id"));
                    rt.setWxTradeNO(microPayResponse.get("out_trade_no"));
                }


                List<RetailTradeCommodity> listRTComm = ShopRobotTest.getRetailTradeCommodityList(listComm, MAX_NOPerCommodity, rt, sbError, bRunInRandomMode);
                if (listRTComm == null) {
                    sbError.append("获取零售单商品失败！");

                    return null;
                }
                rt.setListSlave1(listRTComm);
                if (rt.getPaymentType() == 1) {
                    rt.setAmountCash(listRTComm.get(0).getAmountWeChat());
                } else {
                    rt.setAmountWeChat(listRTComm.get(0).getAmountWeChat()); //带有这批零售单商品的总价格
                }
                // 汇总零售单的价格
                rt.setAmount(GeneralUtil.sumN(rt.getAmountCash(), rt.getAmountAlipay(), rt.getAmountWeChat(), rt.getAmount1(), rt.getAmount2(), rt.getAmount3(), rt.getAmount4(), rt.getAmount5()));

                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
                if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
                    System.out.println("创建零售单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                    sbError.append("创建零售单失败：" + rt);
                    return null;
                }
                lTimeout = 60;
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                    Thread.sleep(1000);
                }
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                    System.out.println(sbError);
                    Assert.assertTrue("创建零售单超时！", false);
                }

                com.bx.erp.model.RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
                Assert.assertTrue("创建了一个临时零售单后查出来失败", retailTrade != null && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade.getListSlave1() != null && retailTrade.getListSlave1().size() > 0);
                // 促销过程
                RetailTradePromoting retailTradePromoting = ShopRobotTest.retailTradePromoting;
                RetailTradePromotingPresenter retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
                RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
                if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null) {
                    if (retailTradePromoting.getListSlave1().size() > 0) {
                        //为RetailTradePromoting设置ID、tradeID和RetailTradePromotingFlow设置ID
                        long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                        long tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                        //
                        retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
                        retailTradePromoting.setTradeID(rt.getID().intValue());
                        for (int i = 0; i < retailTradePromoting.getListSlave1().size(); i++) {
                            ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(i)).setID(tmpRetailTradePromotingFlowIDInSQLite + i);
                        }
                    }
                }
                // 判断是否需要上传计算过程
                if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null && retailTradePromoting.getListSlave1().size() > 0) {
                    RetailTradePromoting rtpCreate = (RetailTradePromoting) retailTradePromotingSQLiteBO.createSync(BaseSQLiteBO.CASE_RetailTradePromoting_CreateMasterSlaveSQLite, retailTradePromoting);
                    if (rtpCreate == null) {
//                    log.error("在本地插入临时计算过程失败！");
//                    hm.setErrorCode(EC_OtherError);
//                    hm.setSubErrorCode(SubErrorCode_RetailTradePromoting_OtherError);
//                    return false;
                    }
                    if (retailTradePromotingSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError && retailTradePromotingSQLiteEvent.getLastErrorCode() != null) {
//                    log.error("在本地插入临时计算过程失败：" + retailTradePromotingSQLiteEvent.getLastErrorMessage());
//                    hm.setErrorCode(retailTradePromotingSQLiteEvent.getLastErrorCode());
//                    hm.setMsg(retailTradePromotingSQLiteEvent.getLastErrorMessage());
//                    return false;
                    }
                }
            } catch (Exception e) {
                System.out.println("创建临时零售单失败，异常信息：" + e.getMessage());
                sbError.append("上传零售单失败！");

                return null;
            }
        } else {
            try {
                long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    sbError.append("查找本地零售单最大ID失败！");
                    return null;
                }
                rt.setStatus(1);
                rt.setID(maxFrameIDInSQLite);
                rt.setSaleDatetime(currentDatetime);
                rt.setDatetimeStart(new Date());
                rt.setDatetimeEnd(new Date());
                rt.setSourceID(-1);
                rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setAmountWeChat(listComm.get(0).getPriceRetail());
                rt.setAmount(GeneralUtil.sumN(rt.getAmountCash(), rt.getAmountAlipay(), rt.getAmountWeChat(), rt.getAmount1(), rt.getAmount2(), rt.getAmount3(), rt.getAmount4(), rt.getAmount5()));

                rt.setPaymentType(4);
                rt.setSmallSheetID(1); //... 将来使用当前使用的小票格式
                rt.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                rt.setPos_ID(Constants.posID);
                rt.setLocalSN((int) maxFrameIDInSQLite);
                rt.setSn(rt.generateRetailTradeSN(Constants.posID));
                rt.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setReturnObject(1);
                rt.setSyncType(BasePresenter.SYNC_Type_C);

                // 在沙箱环境中模拟支付该零售单
                com.bx.erp.model.RetailTrade microPayRT = (com.bx.erp.model.RetailTrade) rt.clone();
                if (!wxMicroPay(microPayRT, sbError)) {
                    sbError.append("--Pos机器人沙箱微信支付失败，使用现金支付--");
                    rt.setPaymentType(1);
                    rt.setAmountCash(rt.getAmountWeChat());
                    rt.setAmountWeChat(0.00000d);
                } else {
                    // 沙箱环境的微信支付成功
                    rt.setWxRefundSubMchID(Constants.submchid);
                    rt.setWxOrderSN(microPayResponse.get("transaction_id"));
                    rt.setWxTradeNO(microPayResponse.get("out_trade_no"));
                }


                List<RetailTradeCommodity> listRTComm = ShopRobotTest.getRetailTradeCommodityList(listComm, MAX_NOPerCommodity, rt, sbError, bRunInRandomMode);
                if (listRTComm == null) {
                    sbError.append("获取零售单商品失败！");

                    return null;
                }
                rt.setListSlave1(listRTComm);
                if (rt.getPaymentType() == 1) {
                    rt.setAmountCash(listRTComm.get(0).getAmountWeChat());
                } else {
                    rt.setAmountWeChat(listRTComm.get(0).getAmountWeChat()); //带有这批零售单商品的总价格
                }
                // 汇总零售单的价格
                rt.setAmount(GeneralUtil.sumN(rt.getAmountCash(), rt.getAmountAlipay(), rt.getAmountWeChat(), rt.getAmount1(), rt.getAmount2(), rt.getAmount3(), rt.getAmount4(), rt.getAmount5()));

                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
                if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
                    System.out.println("创建零售单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                    sbError.append("创建零售单失败：" + rt);
                    return null;
                }
                lTimeout = 60;
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                    Thread.sleep(1000);
                }
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                    System.out.println(sbError);
                    Assert.assertTrue("创建零售单超时！", false);
                }

                com.bx.erp.model.RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
                Assert.assertTrue("创建了一个临时零售单后查出来失败", retailTrade != null && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade.getListSlave1() != null && retailTrade.getListSlave1().size() > 0);
            } catch (Exception e) {
                System.out.println("创建临时零售单失败，异常信息：" + e.getMessage());
                sbError.append("上传零售单失败！");

                return null;
            }
        }

        return rt;
    }

    public void resetForNextDay() {
        int iReturnSheetNO = 0;
        for (com.bx.erp.model.RetailTrade rt : listRT) {
            if (rt.getSourceID() > 0) {
                iReturnSheetNO++;
                System.out.println("存在的退货单为：" + rt);
            }
        }
        System.out.println("今日的零售单总数：" + (listRT.size() - iReturnSheetNO) + "\t购物退货单总数：" + iReturnSheetNO);
        listRT.clear();
        executionNO = 0;
    }

    protected com.bx.erp.model.RetailTrade createReturnRetailTradeInSQLite(com.bx.erp.model.RetailTrade retailTrade, Date currentDatetime, StringBuilder sbError) throws ParseException {
        com.bx.erp.model.RetailTrade rt = new com.bx.erp.model.RetailTrade();
        // 设置从表的临时ID
        List<RetailTradeCommodity> rtcListTemp = new ArrayList<>();
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        if (retailTrade.getListSlave1() != null) {
            for (int i = 0; i < retailTrade.getListSlave1().size(); i++) {
                RetailTradeCommodity rtc = (RetailTradeCommodity) retailTrade.getListSlave1().get(i);
                rtc.setID(maxRetailTradeCommodityIDInSQLite + i);
                rtcListTemp.add(rtc);
            }
        }
        // 随机退单件商品，或者退所有商品
        List<RetailTradeCommodity> list = null;
        int AllOrPart = new Random().nextInt(2);
        double returnAmout = 0.00000d;
        if (AllOrPart == 0) {
            list = (List<RetailTradeCommodity>) GlobalController.getInstance().getRetailTradeCommodityPresenter().createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtcListTemp);
            ErrorInfo.EnumErrorCode ec = GlobalController.getInstance().getRetailTradeCommodityPresenter().getLastErrorCode();
            Assert.assertTrue("创建退货零售单从表失败，错误码：" + ec, ErrorInfo.EnumErrorCode.EC_NoError == ec);
            Assert.assertTrue("创建退货零售单从表失败，从表list.size()=" + list.size(), list.size() != 0);
            rt.setListSlave1(list);
            for (RetailTradeCommodity retailTradeCommodity : list) {
                returnAmout += retailTradeCommodity.getPriceReturn();
            }
        } else {
            List<RetailTradeCommodity> rtcList = new ArrayList<RetailTradeCommodity>();
            rtcList.add(rtcListTemp.get(0));

            list = (List<RetailTradeCommodity>) GlobalController.getInstance().getRetailTradeCommodityPresenter().createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtcList);
            ErrorInfo.EnumErrorCode ec = GlobalController.getInstance().getRetailTradeCommodityPresenter().getLastErrorCode();
            Assert.assertTrue("创建退货零售单从表失败，错误码：" + ec, ErrorInfo.EnumErrorCode.EC_NoError == ec);
            Assert.assertTrue("创建退货零售单从表失败，从表list.size()=" + list.size(), list.size() != 0);

            rt.setListSlave1(list);
            for (RetailTradeCommodity retailTradeCommodity : list) {
                returnAmout += retailTradeCommodity.getPriceReturn();
            }
        }
        try {
            long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("查找本地零售单最大ID失败！");
                return null;
            }
            rt.setID(maxFrameIDInSQLite);
            rt.setLocalSN((int) maxFrameIDInSQLite);
            rt.setSourceID(retailTrade.getID().intValue());
            rt.setSn(retailTrade.getSn() + "_1");
            rt.setPaymentType(retailTrade.getPaymentType());
            rt.setPos_ID(Constants.posID);
            rt.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            rt.setSmallSheetID(1);
            rt.setLogo(retailTrade.getLogo());
            rt.setRemark(retailTrade.getRemark());
            rt.setDatetimeStart(new Date());
            rt.setDatetimeEnd(new Date());
            rt.setSaleDatetime(new Date());
            rt.setStatus(1);
            rt.setReturnObject(1);
            rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
            if (retailTrade.getPaymentType() == 4) {  // 微信退款
                rt.setSaleAmountWeChat(returnAmout);
            }
            rt.setAmount(returnAmout);
            if (rt.getPaymentType() == 1) {
                rt.setAmountCash(returnAmout);
            } else {
                rt.setAmountWeChat(returnAmout); //带有这批零售单商品的总价格
            }
            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
            if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
                System.out.println("创建退货单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                sbError.append("创建退货单失败：" + rt);
                return null;
            }
            lTimeout = 60;
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                System.out.println(sbError);
                Assert.assertTrue("创建退货单超时！", false);
            }
            ShopRobotTest.caseLog("创建的退货单退货为：" + retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1());
//            Assert.assertTrue("创建退货单失败",rt.getSn().equals(((com.bx.erp.model.RetailTrade)retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1()).getSn()));;
            Assert.assertTrue("创建退货单失败", rt.compareTo((com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1()) == 0);
        } catch (Exception e) {
            System.out.println("创建临时退货零售单失败，异常信息：" + e.getMessage());
            sbError.append("创建临时退货零售单失败！");

            return null;
        }

        return (com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
    }

    /**
     * 根据零售单金额进行微信支付操作
     *
     * @throws Exception
     */
    public boolean wxMicroPay(com.bx.erp.model.RetailTrade retailTrade, StringBuilder sbError) {
        String dPaymentMoney = WXPayUtil.formatAmountToPayViaWX(retailTrade.getAmountWeChat());
        WXPayInfo wxPayInfo = new WXPayInfo();// 由于是沙箱环境，wxPayInfo传过去也是没意义的
        wxPayInfo.setAuth_code("134617607342397775");
        wxPayInfo.setTotal_fee(dPaymentMoney);

        if (!wxPayHttpBO.microPayAsync(wxPayInfo)) {
            System.out.println("微信支付失败！");
            sbError.append("--微信支付失败，当前零售单为：" + retailTrade + "--");
            return false;
        }

        long lTimeOut = 10;
        while (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        if (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
//            System.out.println("微信支付超时..");
//            return false;
//        }
        if (wxPayHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            System.out.println("微信支付成功！！");
        } else {
            System.out.println("微信支付失败！！");
            return false;
        }

        return true;
    }

    /**
     * 根据退款零售单金额进行微信退款申请
     *
     * @throws Exception
     */
    public Map<String, String> wxRefund(com.bx.erp.model.RetailTrade retailTradeCreated) {
        if (!wxPayHttpBO.refundAsync(retailTradeCreated)) {
            System.out.println("调用refundAsync失败！");
            return null;
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        wxPayHttpBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (wxPayHttpBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done) {
            System.out.println("微信退款超时...");
            return null;
        }

        WXPayHttpEvent wxPayHttpEvent = (WXPayHttpEvent) wxPayHttpBO.getHttpEvent();
        if (wxPayHttpBO.getHttpEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            System.out.println("微信退款申请成功！！");
            return wxPayHttpEvent.getRefundResponse();
        } else {
            System.out.println("微信退款申请失败！！");
            return null;
        }
    }

    /**
     * 更新退货零售单(插入退款交易单号)
     *
     * @throws Exception
     */
    public com.bx.erp.model.RetailTrade updateRetailTrade(com.bx.erp.model.RetailTrade returnRetailTrade, String out_refund_no) {
        System.out.println("退款的交易单号：" + out_refund_no);
        returnRetailTrade.setWxRefundNO(out_refund_no);//退款交易单号
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_UpdateAsync);
        if (!retailTradeSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, returnRetailTrade)) {
            org.junit.Assert.assertTrue("插入退款单号失败！！", false);
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeOut-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
            org.junit.Assert.assertTrue("更新本地零售单超时...", false);
        }

        System.out.println(retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode());
        org.junit.Assert.assertTrue("插入退款单号失败！！", retailTradeSQLiteBO.getSqLiteEvent().getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);

        com.bx.erp.model.RetailTrade updateReturnRetailTrade = (com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        org.junit.Assert.assertTrue("更新本地零售单返回null", updateReturnRetailTrade != null);

        return updateReturnRetailTrade;
    }

    @Override
    protected boolean doLoadProgramUnit() {
        if (!bRunInRandomMode) {
            List<String> listRowTitle = mapBaseModels.get("零售单主表").get("ID"); // 标题
            Map<String, List<String>> mapMachineMeal = mapBaseModels.get(Config.KEY_NAME_MyMeal);
            Map<String, List<String>> mapActivitySheet = mapBaseModels.get(Config.Acitivity_NO[1]);
//            int maxMealsNo =
            for (int i = 1; i < mapActivitySheet.size(); i++) {
                List<String> rowMachineMeal = mapMachineMeal.get(i + "");
                if (rowMachineMeal == null) {
                    continue;
                }
//            for (List<String> rowMachineMeal : mapMachineMeal.values()) {
                if ("零售单主表".equals(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_SheetTableName.getIndex()))) {
                    ProgramUnit pu = new ProgramUnit();
                    List<String> retailtradeStrList = mapBaseModels.get(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_SheetTableName.getIndex())).get(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_MasterTable.getIndex()));
                    Map<String, Object> params = new HashMap<String, Object>();
                    for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
                        if (retailtradeStrList.get(columnNO) == null || retailtradeStrList.get(columnNO).equals("null")) {
                            params.put(listRowTitle.get(columnNO), retailtradeStrList.get(columnNO));
                        } else {
                            params.put(listRowTitle.get(columnNO), "\"" + retailtradeStrList.get(columnNO) + "\"");
                        }
                    }
                    List<String> listFromTableTitle = mapBaseModels.get("零售单从表").get("ID"); // 标题
                    List<String> listRtComm = new ArrayList<String>();
                    for (List<String> rtComm : mapBaseModels.get("零售单从表").values()) {
                        if (rtComm.get(Config.EnumXlsRetailTradeCommodityTableColumnName.ERTCTCN_tradeID.getIndex()).equals(retailtradeStrList.get(Config.EnumXlsRetailTradeMasterTableColumnName.ERTMTCN_ID.getIndex()))) {
                            Map<String, Object> mapFromTable = new HashMap<String, Object>();
                            for (int columnNO = 0; columnNO < rtComm.size(); columnNO++) {
                                if (rtComm.get(columnNO) == null || rtComm.get(columnNO).equals("null")) {
                                    mapFromTable.put(listFromTableTitle.get(columnNO), rtComm.get(columnNO));
                                } else {
                                    mapFromTable.put(listFromTableTitle.get(columnNO), "\"" + rtComm.get(columnNO) + "\"");
                                }
                            }
                            mapFromTable.put("priceSpecialOffer", 0.0);
                            mapFromTable.put("syncDatetime", "\"\""); // 空字符串
//                            mapFromTable.put("commodityIDs", Program.maxCommodityID + 1);
//                            mapFromTable.put("commodityIDs", 1);
                            listRtComm.add(mapFromTable.toString());
                        }
                    }
                    params.put("listSlave1", listRtComm);
                    Random ran = new Random();
                    params.put("localSN", ran.nextInt(1000) + 1);
                    params.put("syncSequence", 0);
                    params.put("errorInfo", null);
                    // TODO
                    params.put("couponAmount", 0);
                    params.put("consumerOpenID",1);
                    params.put("listSlave2", null);
                    params.put("listSlave3", null);
                    // TODO 日报表的时间格式不是DATE_FORMAT_Default2，先设为空字符串
                    String temp = params.get("saleDatetime").toString();
                    temp = temp.substring(1, temp.length() - 1);
                    Date dateInExcel = null;
                    try {
                        dateInExcel = Constants.getSimpleDateFormat().parse(temp);
                        temp = Constants.getSimpleDateFormat2().format(dateInExcel);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    params.put("saleDatetime", "\"" + temp + "\"");
//                    params.put("saleDatetime", "\"\"");
                    params.put("syncDatetime", "\"\"");
                    com.bx.erp.model.RetailTrade retailTrade = new com.bx.erp.model.RetailTrade();
                    retailTrade = (com.bx.erp.model.RetailTrade) retailTrade.parse1(params.toString());
                    if (retailTrade == null) {
                        throw new RuntimeException("Retailtrade进行doParse1时转换失败！");
                    }
                    pu.setBaseModelIn1(retailTrade);
                    pu.setNo(Integer.parseInt(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_ProgramUnitNO.getIndex())));
//                    pu.setNo(0);
                    pu.setOperationType(Integer.parseInt(rowMachineMeal.get(Config.EnumXlsMealColumn.EB_OperationType.getIndex())));
                    queueIn.offer(pu);
                }
            }
        }
        return true;
    }

    private boolean createRetailTrade(Date currentDatetime, StringBuilder sbError, boolean bRunInRandomMode, int readExcelMode, BaseModel bm) throws ParseException {
        if (!bRunInRandomMode) {
            com.bx.erp.model.RetailTrade rt = (com.bx.erp.model.RetailTrade) bm;
            try {
                rt = createRetailTradeInSQLiteInExcelMode(rt, currentDatetime, sbError, wxPayHttpBO, bRunInRandomMode);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            if (rt == null) {
                sbError.append("创建零售单在SQLite中失败");
                return false;
            }
        } else {
            // 收银1单。如果有错，记录在sbError中
            List<Commodity> listComm = ShopRobotTest.getCommodityList(MAX_NOOfCommodityPerTrade, false, sbError, bRunInRandomMode);
            if (listComm == null || listComm.size() == 0) {
                sbError.append("查找本地商品失败！");
                return false;
            }
            com.bx.erp.model.RetailTrade rt = null;
            try {
                rt = createRetailTradeInSQLite(listComm, currentDatetime, sbError, wxPayHttpBO, bRunInRandomMode);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            if (rt == null) {
                sbError.append("创建零售单在SQLite中失败");
                return false;
            }
        }
        return true;
    }

    private boolean createReturnRetailTrade(StringBuilder sbError, BaseModel bm) {
        //因为测试使用Excel表相对ID来进行创建退货型零售单的，所以确保各个pos机创建完零售单后执行上传零售单的同步操作，运行测试前，本地没有临时零售单

        //
//        com.bx.erp.model.RetailTrade retrieve1RetailTradeCondition = new com.bx.erp.model.RetailTrade();
//        retrieve1RetailTradeCondition.setSn(Pro);
//        com.bx.erp.model.RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
//        Assert.assertTrue("创建了一个临时零售单后查出来失败", retailTrade != null && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade.getListSlave1() != null && retailTrade.getListSlave1().size() > 0);

//        com.bx.erp.model.RetailTrade retailTradeExcel = (com.bx.erp.model.RetailTrade) bm;
        // TODO 这里使用相对ID，那么excel表中的零售单主表只能对应一个活动，因为使用相对ID，必须确保excel表的零售单必须按顺序创建了
//        retailTradeExcel.setID(Program.maxRetailTradeID + 1L);
        com.bx.erp.model.RetailTrade rt = (com.bx.erp.model.RetailTrade) bm;
        // TODO 退一张单或多张单，ID未确定
//        long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//        if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
//            sbError.append("查找本地零售单最大ID失败！");
//            return false;
//        }
//        long returnRetailTradeID = 1l;
//        com.bx.erp.model.RetailTrade retailtradeRetrieve1Conditions = new com.bx.erp.model.RetailTrade();
//        retailtradeRetrieve1Conditions.setID(returnRetailTradeID);
//        // 从SQLite或者从服务器获取
//        com.bx.erp.model.RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeRetrieve1Conditions);
//        // 设置从表的临时ID
        List<RetailTradeCommodity> RetailTradeCommodityList = (List<RetailTradeCommodity>) retailTradeCommodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        org.junit.Assert.assertTrue("RetrieveNSync测试失败,原因:返回错误码不正确!", retailTradeCommodityPresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError);
        List<RetailTradeCommodity> rtcListTemp = new ArrayList<>();
        long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        System.out.println(new Date());
        if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            sbError.append("零售单商品表查找本地最大ID失败！");
            return false;
        }
//        if (retailTrade.getListSlave1() != null) {
//            for (int i = 0; i < retailTrade.getListSlave1().size(); i++) {
//                RetailTradeCommodity rtc = (RetailTradeCommodity) retailTrade.getListSlave1().get(i);
//                rtc.setID(maxRetailTradeCommodityIDInSQLite + i);
//                rtcListTemp.add(rtc);
//            }
//        }
        int totalPrices = 0; //记录这批零售单商品的总价格
        if (rt.getListSlave1() != null) {
            for (int i = 0; i < rt.getListSlave1().size(); i++) {
                RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(i);
                rtc.setID(maxRetailTradeCommodityIDInSQLite + i);
//                rtc.setBarcodeID(Program.maxBarcodeID + Integer.parseInt(String.valueOf(rtc.getCommodityIDs())));
//                rtc.setCommodityIDs(Program.maxCommodityID + Integer.parseInt(String.valueOf(rtc.getCommodityIDs())));
                String commAndBarcodeIDs = ClientHandler.bodyInfoForCreateRt[i];
                String[] commAndBarcodeIdArr = commAndBarcodeIDs.split(",");
                int commodityID = Integer.parseInt(commAndBarcodeIdArr[0]);
                int barcodeID = Integer.parseInt(commAndBarcodeIdArr[1]);
                rtc.setCommodityID(commodityID);
                rtc.setBarcodeID(barcodeID);
                totalPrices += rtc.getNO() * rtc.getPriceReturn();
                rtcListTemp.add(rtc);
            }
        }
        // 随机退单件商品，或者退所有商品
        List<RetailTradeCommodity> list = null;
        int AllOrPart = new Random().nextInt(2);
        double returnAmout = 0.00000d;
        // 创建零售单的时候会创建零售商品，所以这里不能单独创建零售商品，否则零售商品ID会重复，创建零售单从表失败
//        if (AllOrPart == 0) {
//            list = (List<RetailTradeCommodity>) GlobalController.getInstance().getRetailTradeCommodityPresenter().createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtcListTemp);
//            ErrorInfo.EnumErrorCode ec = GlobalController.getInstance().getRetailTradeCommodityPresenter().getLastErrorCode();
//            Assert.assertTrue("创建退货零售单从表失败，错误码：" + ec, ErrorInfo.EnumErrorCode.EC_NoError == ec);
//            Assert.assertTrue("创建退货零售单从表失败，从表list.size()=" + list.size(), list.size() != 0);
//            rt.setListSlave1(list);
//            for (RetailTradeCommodity retailTradeCommodity : list) {
//                returnAmout += retailTradeCommodity.getPriceReturn();
//            }
//        } else {
//            List<RetailTradeCommodity> rtcList = new ArrayList<RetailTradeCommodity>();
//            rtcList.add(rtcListTemp.get(0));
//
//            list = (List<RetailTradeCommodity>) GlobalController.getInstance().getRetailTradeCommodityPresenter().createNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rtcList);
//            ErrorInfo.EnumErrorCode ec = GlobalController.getInstance().getRetailTradeCommodityPresenter().getLastErrorCode();
//            Assert.assertTrue("创建退货零售单从表失败，错误码：" + ec, ErrorInfo.EnumErrorCode.EC_NoError == ec);
//            Assert.assertTrue("创建退货零售单从表失败，从表list.size()=" + list.size(), list.size() != 0);
//
//            rt.setListSlave1(list);
//            for (RetailTradeCommodity retailTradeCommodity : list) {
//                returnAmout += retailTradeCommodity.getPriceReturn();
//            }
//        }
        rt.setListSlave1(rtcListTemp);
        try {
            long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("查找本地零售单最大ID失败！");
                return false;
            }
            // TODO 日报表测试用例excel表的ID是从43开始的
//            rt.setSourceID(retailTradeExcel.getID().intValue());
            if (maxRetailTradeID == 0) {
                throw new RuntimeException("错误的源单零售单ID");
            }
            rt.setSourceID(maxRetailTradeID);
            rt.setID(maxFrameIDInSQLite);
            rt.setLocalSN((int) maxFrameIDInSQLite);
            //
//            rt.setSn(retailTradeExcel.getSn());
//            rt.setPaymentType(retailTradeExcel.getPaymentType());
            rt.setPos_ID(Constants.posID);
            rt.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            rt.setSmallSheetID(1);
//            rt.setLogo(retailTradeExcel.getLogo());
//            rt.setRemark(retailTradeExcel.getRemark());
            rt.setDatetimeStart(new Date());
            rt.setDatetimeEnd(new Date());
//            rt.setSaleDatetime(new Date());
            // TODO 修改一下零售商品时间
            Date dateTimeInExcel = rt.getSaleDatetime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTimeInExcel);
            calendar.add(Calendar.YEAR, 15);
            rt.setSaleDatetime(calendar.getTime());
            rt.setStatus(1);
            rt.setReturnObject(1);
            rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
            if (rt.getPaymentType() == 2) {  // 支付宝退款
                rt.setSaleAmountAlipay(rt.getAmount());
            }
            if (rt.getPaymentType() == 4) {  // 微信退款
                rt.setSaleAmountWeChat(rt.getAmount());
            }
//            rt.setAmount(totalPrices);
//            if (rt.getPaymentType() == 1) {
//                rt.setAmountCash(totalPrices);
//            } else {
//                rt.setAmountWeChat(totalPrices); // 带有这批零售单商品的总价格
//            }
            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
            if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
                System.out.println("创建退货单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                sbError.append("创建退货单失败：" + rt);
                return false;
            }
//            lTimeout = 60;
            lTimeout = 600;
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                System.out.println(sbError);
                Assert.assertTrue("创建退货单超时！", false);
            }
            ShopRobotTest.caseLog("创建的退货单退货为：" + retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1());
//            Assert.assertTrue("创建退货单失败",rt.getSn().equals(((com.bx.erp.model.RetailTrade)retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1()).getSn()));;
            Assert.assertTrue("创建退货单失败", rt.compareTo((com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1()) == 0);
        } catch (Exception e) {
            System.out.println("创建临时退货零售单失败，异常信息：" + e.getMessage());
            sbError.append("创建临时退货零售单失败！");
            return false;
        }
        com.bx.erp.model.RetailTrade returnRetailTrade = (com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
        System.out.println("====生成的退货单为：" + returnRetailTrade);
        Assert.assertTrue("零售单创建退货单失败！！", returnRetailTrade != null);
//        if (retailTrade.getPaymentType() == 4) {
//            com.bx.erp.model.RetailTrade refundRT = (com.bx.erp.model.RetailTrade) returnRetailTrade.clone();
//            refundRT.setAmount(WXPayUtil.formatAmount(refundRT.getAmount()));
//            Map<String, String> refundResponse = wxRefund(refundRT);
//            // 根据退货零售单微信交易单号进行退款申请
//            if (refundResponse == null) {
//                System.out.println("微信申请退款失败！使用现金进行退款");
//                listRT.add(returnRetailTrade);
//            } else {
//                // 更新退货零售单(update退款交易单号)
//                com.bx.erp.model.RetailTrade returnRetailTradeWithRefundNO = updateRetailTrade(returnRetailTrade, refundResponse.get("out_refund_no"));
//                System.out.println("微信退款单号：" + returnRetailTradeWithRefundNO.getWxRefundNO());
//                listRT.add(returnRetailTradeWithRefundNO);
//            }
//        }
        return true;
    }

    private boolean queryRetailTrade(int iUseCaseID, StringBuilder sbError) throws Exception {
        // 查询零售单，根据时间、SN查询？
        Random random = new Random();
        switch (iUseCaseID) {
            case queryRetailTradeByNoConditions:
                // 没有查询条件
                ShopRobotTest.caseLog("没有查询条件零售单");
                com.bx.erp.model.RetailTrade queryRetailTradeForNothing = new com.bx.erp.model.RetailTrade();
                queryRetailTradeForNothing.setQueryKeyword("");
                queryRetailTradeForNothing.setDatetimeStart(Constants.getDefaultSyncDatetime());
                queryRetailTradeForNothing.setDatetimeEnd(new Date());
                List<?> quertRetailTradeList = queryRetailTradeForNBR(queryRetailTradeForNothing);
                if (quertRetailTradeList == null || quertRetailTradeList.size() == 0) {
                    sbError.append("无条件查询零售单失败！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("无条件查询零售单成功！" + quertRetailTradeList);
                break;
            case queryRetailTradeBySNConditions:
                // 根据SN查询c
                ShopRobotTest.caseLog("根据SN查询零售单");
                com.bx.erp.model.RetailTrade queryRetailTradeForSN = new com.bx.erp.model.RetailTrade();
//                queryRetailTradeForSN.setQueryKeyword(listRTForQuery.get(random.nextInt(listRTForQuery.size())).getSn());
                queryRetailTradeForSN.setQueryKeyword("SN");// TODO SN未确定
                List<?> quertRetailTradeList2 = queryRetailTradeForNBR(queryRetailTradeForSN);
                if (quertRetailTradeList2 == null || quertRetailTradeList2.size() != 1) {
                    sbError.append("根据SN查询零售单失败！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("根据SN查询零售单成功！" + quertRetailTradeList2);
                break;
            case queryRetailTradeByTimeConditions:
                // 根据时间查询
                ShopRobotTest.caseLog("根据时间查询零售单");
                com.bx.erp.model.RetailTrade queryRetailTradeForDate = new com.bx.erp.model.RetailTrade();
                queryRetailTradeForDate.setQueryKeyword("");
//                queryRetailTradeForDate.setDatetimeStart(listRTForQuery.get(random.nextInt(listRTForQuery.size())).getDatetimeStart());
                queryRetailTradeForDate.setDatetimeStart(new Date()); // TODO 时间未确定
                List<?> quertRetailTradeList3 = queryRetailTradeForNBR(queryRetailTradeForDate);
                if (quertRetailTradeList3 == null || quertRetailTradeList3.size() == 0) {
                    sbError.append("根据时间查询零售单失败！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("根据时间查询零售单成功！" + quertRetailTradeList3);
                break;
            case queryRetailTradeBySNandTimeConditions:
                // 根据SN以及时间查询
                ShopRobotTest.caseLog("根据SN以及时间查询零售单");
//                List<?> quertRetailTradeList4 = queryRetailTradeForNBR(listRTForQuery.get(random.nextInt(listRTForQuery.size())));
                com.bx.erp.model.RetailTrade retailTradeRetrieveNconditions = new com.bx.erp.model.RetailTrade();
                retailTradeRetrieveNconditions.setQueryKeyword("SN");
                retailTradeRetrieveNconditions.setDatetimeStart(new Date());
                retailTradeRetrieveNconditions.setDatetimeEnd(new Date());
                List<?> quertRetailTradeList4 = queryRetailTradeForNBR(retailTradeRetrieveNconditions); // TODO SN、时间未确定
                if (quertRetailTradeList4 == null || quertRetailTradeList4.size() == 0) {
                    sbError.append("根据SN以及时间查询零售单失败！！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("根据SN以及时间查询零售单成功！" + quertRetailTradeList4);
                break;
            default:
                throw new RuntimeException("未正确指定查询方式！");
        }
        //
        return true;
    }

    private List<?> queryRetailTradeForNBR(com.bx.erp.model.RetailTrade retailTrade) throws InterruptedException {
        retailTrade.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTrade.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
            System.out.println("查询旧零售单失败！！");
            return null;
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteBO.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBO.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            org.junit.Assert.assertTrue("请求服务器超时!", false);
        }
        return retailTradeHttpBO.getHttpEvent().getListMasterTable();
    }

    protected com.bx.erp.model.RetailTrade createRetailTradeInSQLiteInExcelMode(com.bx.erp.model.RetailTrade bm, Date currentDatetime, StringBuilder sbError, WXPayHttpBO wxPayHttpBO, boolean bRunInRandomMode) throws ParseException {
        com.bx.erp.model.RetailTrade rt = bm;
        if (!bRunInRandomMode) {
            try {
                long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    sbError.append("查找本地零售单最大ID失败！");
                    return null;
                }
                rt.setStatus(1);
                rt.setID(maxFrameIDInSQLite);
//                rt.setSaleDatetime(currentDatetime);
                // TODO 修改一下零售商品时间
                Date dateTimeInExcel = rt.getSaleDatetime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateTimeInExcel);
                calendar.add(Calendar.YEAR, 15);
                rt.setSaleDatetime(calendar.getTime());
                rt.setDatetimeStart(new Date());
                rt.setDatetimeEnd(new Date());
                rt.setSourceID(-1);
                rt.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
                rt.setLocalSN((int) maxFrameIDInSQLite);
                rt.setSn(rt.generateRetailTradeSN(Constants.posID));
                rt.setReturnObject(1);
                rt.setSyncType(BasePresenter.SYNC_Type_C);
                // 在沙箱环境中模拟支付该零售单
                com.bx.erp.model.RetailTrade microPayRT = (com.bx.erp.model.RetailTrade) rt.clone();
                if (!wxMicroPay(microPayRT, sbError)) {
                    sbError.append("--Pos机器人沙箱微信支付失败，使用现金支付--");
//                    rt.setPaymentType(1);
//                    rt.setAmountCash(rt.getAmountWeChat());
//                    rt.setAmountWeChat(0.00000d);
                } else {
                    // 沙箱环境的微信支付成功
                    rt.setWxRefundSubMchID(Constants.submchid);
                    rt.setWxOrderSN(microPayResponse.get("transaction_id"));
                    rt.setWxTradeNO(microPayResponse.get("out_trade_no"));
                }
                List<RetailTradeCommodity> listRTComm = ShopRobotTest.getRetailTradeCommodityListInExcelMode(rt, sbError, bRunInRandomMode);
                if (listRTComm == null) {
                    sbError.append("获取零售单商品失败！");
                    return null;
                }
                rt.setListSlave1(listRTComm);
                // TODO 对于excel表，支付方式对应的金额已经算好了，可以注释掉下面几行
//                if (rt.getPaymentType() == 1) {
//                    rt.setAmountCash(listRTComm.get(0).getAmountWeChat());
//                } else {
//                    rt.setAmountWeChat(listRTComm.get(0).getAmountWeChat()); //带有这批零售单商品的总价格
//                }
//                // 汇总零售单的价格
//                rt.setAmount(GeneralUtil.sumN(rt.getAmountCash(), rt.getAmountAlipay(), rt.getAmountWeChat(), rt.getAmount1(), rt.getAmount2(), rt.getAmount3(), rt.getAmount4(), rt.getAmount5()));
                //
                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
                if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, rt)) {
                    System.out.println("创建零售单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                    sbError.append("创建零售单失败：" + rt);
                    return null;
                }
//                lTimeout = 60;
                lTimeout = 600;
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                    Thread.sleep(1000);
                }
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                    System.out.println(sbError);
                    Assert.assertTrue("创建零售单超时！", false);
                }
                //
                com.bx.erp.model.RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, rt);
                Assert.assertTrue("创建了一个临时零售单后查出来失败", retailTrade != null && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade.getListSlave1() != null && retailTrade.getListSlave1().size() > 0);
//                // 促销过程
//                RetailTradePromoting retailTradePromoting = ShopRobotTest.retailTradePromoting;
//                RetailTradePromotingPresenter retailTradePromotingPresenter = GlobalController.getInstance().getRetailTradePromotingPresenter();
//                RetailTradePromotingFlowPresenter retailTradePromotingFlowPresenter = GlobalController.getInstance().getRetailTradePromotingFlowPresenter();
//                if (retailTradePromoting != null && retailTradePromoting.getListSlave1() != null) {
//                    if (retailTradePromoting.getListSlave1().size() > 0) {
//                        //为RetailTradePromoting设置ID、tradeID和RetailTradePromotingFlow设置ID
//                        long tmpRetailTradePromotingIDInSQLite = retailTradePromotingPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                        long tmpRetailTradePromotingFlowIDInSQLite = retailTradePromotingFlowPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
//                        //
//                        retailTradePromoting.setID(tmpRetailTradePromotingIDInSQLite);
//                        retailTradePromoting.setTradeID(rt.getID().intValue());
//                        for (int i = 0; i < retailTradePromoting.getListSlave1().size(); i++) {
//                            ((RetailTradePromotingFlow) retailTradePromoting.getListSlave1().get(i)).setID(tmpRetailTradePromotingFlowIDInSQLite + i);
//                        }
//                    }
//                }
            } catch (Exception e) {
                System.out.println("创建临时零售单失败，异常信息：" + e.getMessage());
                sbError.append("上传零售单失败！");

                return null;
            }
        }
        return rt;
    }

    private void uploadTempRetailTrade(ProgramUnit pu) throws InterruptedException {
        // 上传临时零售单
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("查找临时零售单失败！", false);
        }

        lTimeout = 60;
        while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction && lTimeout-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData &&//
                retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_NoAction) {
            System.out.println("ShopRobot 上传临时零售单同步数据失败！原因：超时！");
        }

        List<com.bx.erp.model.RetailTrade> retailTrades = (List<com.bx.erp.model.RetailTrade>) retailTradeHttpEvent.getListMasterTable();
        Assert.assertTrue("返回的零售单List为null", retailTrades != null && retailTrades.size() != 0);
        // 因为每一次创建零售单都上传，所以上传后服务器应该是返回一条，否则就是跑测试前本地有临时零售单,影响机器人测试
        Assert.assertFalse("返回的零售单List不等于1", retailTrades.size() != 1);
        com.bx.erp.model.RetailTrade retailTrade = retailTrades.get(0);
        retailTrade.setDatetimeStart(new Date());
        retailTrade.setDatetimeEnd(new Date());
        pu.setBaseModelIn1(retailTrade);
    }
}