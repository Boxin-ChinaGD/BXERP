package com.test.bx.app.robot.program;


import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.bo.WXPayHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.*;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.Shared;
import com.bx.erp.utils.WXPayUtil;
import com.bx.erp.view.activity.BaseActivity;
import com.test.bx.app.RetailTradeJUnit;
import com.test.bx.app.robot.ShopRobotTest;
import com.test.bx.app.robot.ShopRobotTestEx;

import junit.framework.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RetailTradeEx extends ProgramEx {

    private WXPayHttpBO wxPayHttpBO;

    private RetailTradePresenter retailTradePresenter;
    private Map<String, String> microPayResponse = new HashMap<>();//微信支付responseData

    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    //

    private RetailTradeHttpEvent retailTradeHttpEvent;
    private RetailTradeHttpBO retailTradeHttpBO;

    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;

    public static final int queryRetailTradeByNoConditions = 0;
    public static final int queryRetailTradeBySNConditions = 1;
    public static final int queryRetailTradeByTimeConditions = 2;
    public static final int queryRetailTradeBySNandTimeConditions = 3;

    // 存储本pos机器人创建并且已经上传到服务器的零售单
    public static List<RetailTrade> retailTradesCreatedInServer = new ArrayList<>();

    private RetailTradeSQLiteBO retailTradeSQLiteBoForQueryRetailTrade;
    private RetailTradeSQLiteEvent retailTradeSQLiteEventForQeueryRetailTrade;
    private RetailTradeHttpEvent retailTradeHttpEventForQueryRetailTrade;
    private RetailTradeHttpBO retailTradeHttpBOForQueryRetailTrade;

    /**
     * 每一个顾客一次购买的单个商品的最大数目
     */
    private final int MAX_NOPerCommodity = 10;
    /**
     * 每一个顾客一次购买的商品的最大种类数。一个种类由一个商品ID标识
     */
    private final int MAX_NOOfCommodityPerTrade = 20;

    public static final int Event_ID_RetailTradeEx = 100002;

    public RetailTradeEx(WXPayHttpBO wxPayHttpBO, RetailTradeSQLiteBO retailTradeSQLiteBO, RetailTradeSQLiteEvent retailTradeSQLiteEvent, RetailTradeHttpEvent retailTradeHttpEvent, RetailTradeHttpBO retailTradeHttpBO) {
        this.wxPayHttpBO = wxPayHttpBO;
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        this.retailTradeSQLiteBO = retailTradeSQLiteBO;
        this.wxPayHttpBO = wxPayHttpBO;
        this.retailTradeSQLiteEvent = retailTradeSQLiteEvent;
        this.retailTradeHttpEvent = retailTradeHttpEvent;
        this.retailTradeHttpBO = retailTradeHttpBO;
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        initBoAndEvent();
    }

    private void initBoAndEvent() {
        if (retailTradeHttpEventForQueryRetailTrade == null) {
            retailTradeHttpEventForQueryRetailTrade = new RetailTradeHttpEvent();
            retailTradeHttpEventForQueryRetailTrade.setId(Event_ID_RetailTradeEx);
        }
        if (retailTradeSQLiteEventForQeueryRetailTrade == null) {
            retailTradeSQLiteEventForQeueryRetailTrade = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEventForQeueryRetailTrade.setId(Event_ID_RetailTradeEx);
        }
        if (retailTradeSQLiteBoForQueryRetailTrade == null) {
            retailTradeSQLiteBoForQueryRetailTrade = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEventForQeueryRetailTrade, retailTradeHttpEventForQueryRetailTrade);
        }
        if (retailTradeHttpBOForQueryRetailTrade == null) {
            retailTradeHttpBOForQueryRetailTrade = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEventForQeueryRetailTrade, retailTradeHttpEventForQueryRetailTrade);

        }
        retailTradeHttpEventForQueryRetailTrade.setHttpBO(retailTradeHttpBOForQueryRetailTrade);
        retailTradeHttpEventForQueryRetailTrade.setSqliteBO(retailTradeSQLiteBoForQueryRetailTrade);
        retailTradeSQLiteEventForQeueryRetailTrade.setHttpBO(retailTradeHttpBOForQueryRetailTrade);
        retailTradeSQLiteEventForQeueryRetailTrade.setSqliteBO(retailTradeSQLiteBoForQueryRetailTrade);
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

    @Override
    public boolean run(StringBuilder errorInfo) throws InterruptedException, ParseException {
        switch (EnumOperationType.values()[new Random().nextInt(3)]) {
            case EOT_CreateRetailTrade:
                if (!createRetailTrade()) {
                    return false;
                }
                uploadTempRetailTrade();
                break;
            case EOT_CreateReturnRetailTrade:
                if (!createReturnRetailTrade(errorInfo)) {
                    return false;
                }
                break;
            case EOT_QueryRetailTrade:
                // TODO 进行查单后，retailTradesCreatedInServer会add一些SQLite不存在的零售单
                // 原因尚未找到，跟多线程有关系？先注释
                if (!queryRetailTrade(errorInfo)) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean queryRetailTrade(StringBuilder sbError) throws InterruptedException, ParseException {
        System.out.println("查询零售单");
        if (!randomQueryRetailTrade(sbError)) {
            return false;
        }
        return true;
    }

    private boolean randomQueryRetailTrade(StringBuilder sbError) throws ParseException, InterruptedException {
        if (retailTradesCreatedInServer.size() > 0) {
            Random random = new Random();
            switch (random.nextInt(4)) {
                case queryRetailTradeByNoConditions:
                    // 没有查询条件
                    ShopRobotTest.caseLog("没有查询条件零售单");
                    RetailTrade queryRetailTradeWithoutCondition = new RetailTrade();
                    queryRetailTradeWithoutCondition.setQueryKeyword("");
                    queryRetailTradeWithoutCondition.setDatetimeStart(Constants.getDefaultSyncDatetime());
                    queryRetailTradeWithoutCondition.setDatetimeEnd(new Date());
                    List<?> quertRetailTradeList = queryRetailTradeForNBR(queryRetailTradeWithoutCondition);
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
                    RetailTrade queryRetailTradeForSN = new RetailTrade();
                    queryRetailTradeForSN.setQueryKeyword(retailTradesCreatedInServer.get(random.nextInt(retailTradesCreatedInServer.size())).getSn());
                    Calendar calendarSn = Calendar.getInstance();
                    calendarSn.setTime(new Date());
                    calendarSn.add(Calendar.YEAR, 1);
                    queryRetailTradeForSN.setDatetimeEnd(calendarSn.getTime());
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
                    RetailTrade queryRetailTradeForDate = new RetailTrade();
                    queryRetailTradeForDate.setQueryKeyword("");
                    queryRetailTradeForDate.setDatetimeStart(retailTradesCreatedInServer.get(random.nextInt(retailTradesCreatedInServer.size())).getSaleDatetime());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(queryRetailTradeForDate.getDatetimeStart());
                    calendar.add(Calendar.MONTH, 1);
                    queryRetailTradeForDate.setDatetimeEnd(calendar.getTime());
                    ShopRobotTest.caseLog("datetimeStart" + queryRetailTradeForDate.getDatetimeStart());
                    List<?> quertRetailTradeList3 = queryRetailTradeForNBR(queryRetailTradeForDate);
                    if (quertRetailTradeList3 == null || quertRetailTradeList3.size() == 0) {
                        sbError.append("根据时间查询零售单失败！");
                        System.out.println("时间:" + queryRetailTradeForDate.getDatetimeStart() );
                        ShopRobotTest.caseLog(sbError.toString());
                        return false;
                    }
                    System.out.println("根据时间查询零售单成功！" + quertRetailTradeList3);
                    break;
                case queryRetailTradeBySNandTimeConditions:
                    // 根据SN以及时间查询
                    ShopRobotTest.caseLog("根据SN以及时间查询零售单");
                    RetailTrade queryRetailTradeForDateAndSn = new RetailTrade();
                    RetailTrade retailTradeCreated = retailTradesCreatedInServer.get(random.nextInt(retailTradesCreatedInServer.size()));
                    queryRetailTradeForDateAndSn.setDatetimeStart(retailTradeCreated.getSaleDatetime());
                    queryRetailTradeForDateAndSn.setQueryKeyword(retailTradeCreated.getSn());
                    Calendar calendarSnAndDate = Calendar.getInstance();
                    calendarSnAndDate.setTime(queryRetailTradeForDateAndSn.getDatetimeStart());
                    calendarSnAndDate.add(Calendar.MONTH, 1);
                    queryRetailTradeForDateAndSn.setDatetimeEnd(calendarSnAndDate.getTime());
                    List<?> quertRetailTradeList4 = queryRetailTradeForNBR(queryRetailTradeForDateAndSn);
                    if (quertRetailTradeList4 == null || quertRetailTradeList4.size() == 0) {
                        sbError.append("根据SN以及时间查询零售单失败！！");
                        System.out.println(sbError.toString());
                        System.out.println("SN：" + queryRetailTradeForDateAndSn.getQueryKeyword() + ",时间:" + queryRetailTradeForDateAndSn.getDatetimeStart() );
                        return false;
                    }
                    System.out.println("根据SN以及时间查询零售单成功！" + quertRetailTradeList4);
                    break;
                default:
                    throw new RuntimeException("未正确指定查询方式！");
            }
        }
        return true;
    }

    private List<?> queryRetailTradeForNBR(RetailTrade retailTrade) throws InterruptedException {
        retailTradeHttpBOForQueryRetailTrade.getHttpEvent().setListMasterTable(null);
        retailTrade.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTrade.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEventForQeueryRetailTrade.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEventForQeueryRetailTrade.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBOForQueryRetailTrade.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
            System.out.println(retailTradeSQLiteEventForQeueryRetailTrade.getLastErrorMessage());
            System.out.println("查询旧零售单失败！！");
            return null;
        }

        long lTimeOut = Shared.UNIT_TEST_TimeOut;
        retailTradeSQLiteBoForQueryRetailTrade.getHttpEvent().setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        while (retailTradeSQLiteBoForQueryRetailTrade.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBoForQueryRetailTrade.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction && lTimeOut-- > 0) {
            Thread.sleep(1000);
        }
        if (retailTradeSQLiteBoForQueryRetailTrade.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done &&//
                retailTradeSQLiteBoForQueryRetailTrade.getHttpEvent().getStatus() != BaseEvent.EnumEventStatus.EES_Http_NoAction) {
            org.junit.Assert.assertTrue("请求服务器超时!", false);
            throw new RuntimeException("请求服务器超时!");
        }
        return retailTradeHttpBOForQueryRetailTrade.getHttpEvent().getListMasterTable();
    }

    private boolean createReturnRetailTrade(StringBuilder sbError) {
        // 本pos机有创建过零售单才可以创建退货型零售单
        if (retailTradesCreatedInServer.size() > 0) {
            int ramdomIndex = new Random().nextInt(retailTradesCreatedInServer.size());
            RetailTrade retailtradeToReturn = retailTradesCreatedInServer.get(ramdomIndex);
            // TODO 这里只是为了查询retailtradeToReturn是否存在,跑测试的时候有时候R1不出来，但是测试失败后，在RetailTradePresenterTest中有时候可以查询出来，有时候查不出来
            RetailTrade retailTradeValidate = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeToReturn);
            int timeOut = 5;
            while (retailTradeValidate == null && timeOut-- > 0) {
               System.out.println("ShopRobotTestEx.idAddLis" + ShopRobotTestEx.idAddLis);
               retailTradeValidate = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeToReturn);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Assert.assertTrue("上传零售单后的数据不存在，零售单ID：" + retailtradeToReturn.getID(), retailTradeValidate != null);
            List<RetailTradeCommodity> rtcListTemp = new ArrayList<>();
            long maxRetailTradeCommodityIDInSQLite = retailTradeCommodityPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            System.out.println(new Date());
            if (retailTradeCommodityPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("零售单商品表查找本地最大ID失败！");
                return false;
            }
            RetailTrade retailTrade = (RetailTrade) retailtradeToReturn.clone();
            int totalPrices = 0; //记录这批零售单商品的总价格
            for (int i = 0; i < retailtradeToReturn.getListSlave1().size(); i++) {
                RetailTradeCommodity rtc = (RetailTradeCommodity) retailtradeToReturn.getListSlave1().get(i);
                RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) rtc.clone();
                retailTradeCommodity.setNO(new Random().nextInt(rtc.getNO()) + 1);
                retailTradeCommodity.setID(maxRetailTradeCommodityIDInSQLite + i);
                totalPrices += retailTradeCommodity.getNO() * retailTradeCommodity.getPriceReturn();
                rtcListTemp.add(retailTradeCommodity);
            }
            retailTrade.setListSlave1(rtcListTemp);
            try {
                long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
                if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    sbError.append("查找本地零售单最大ID失败！");
                    return false;
                }
                retailTrade.setSourceID(retailtradeToReturn.getID().intValue());
                retailTrade.setID(maxFrameIDInSQLite);
                retailTrade.setLocalSN((int) maxFrameIDInSQLite);
                retailTrade.setPos_ID(Constants.posID);
                retailTrade.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
                retailTrade.setSmallSheetID(1);
                retailTrade.setDatetimeStart(new Date());
                retailTrade.setDatetimeEnd(new Date());
                retailTrade.setStatus(1);
                retailTrade.setReturnObject(1);
                retailTrade.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
                retailTrade.setAmount(totalPrices);
                if (retailTrade.getPaymentType() == 1) {
                    retailTrade.setAmountCash(totalPrices);
                } else if (retailTrade.getPaymentType() == 2) {  // 支付宝退款
                    retailTrade.setSaleAmountAlipay(retailTrade.getAmount());
                } else if (retailTrade.getPaymentType() == 4) {  // 微信退款
                    retailTrade.setSaleAmountWeChat(retailTrade.getAmount());
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
                retailTrade.setSaleDatetime(calendar.getTime());
                retailTrade.setDatetimeStart(new Date());
                RetailTrade retailTrade3 = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtradeToReturn);
                Assert.assertTrue("上传零售单后的数据不存在", retailTrade3 != null);
                retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
                if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade)) {
                    System.out.println("创建退货单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                    sbError.append("创建退货单失败：" + retailTrade);
                    ShopRobotTest.caseLog("已创建的零售单：");
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0; i<retailTradesCreatedInServer.size(); i++) {
                        stringBuilder.append(retailTradesCreatedInServer.get(i).getID() + ",");
                    }
                    System.out.println(stringBuilder.toString());
                    return false;
                }
                int lTimeout = 60;
                while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                    Thread.sleep(1000);
                }
                if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                    System.out.println(sbError);
                    Assert.assertTrue("创建退货单超时！", false);
                }
                ShopRobotTest.caseLog("创建的退货单退货为：" + retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1());
                Assert.assertTrue("创建退货单失败", retailTrade.compareTo((com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1()) == 0);
            } catch (Exception e) {
                System.out.println("创建临时退货零售单失败，异常信息：" + e.getMessage());
                sbError.append("创建临时退货零售单失败！");
                System.out.println("源单ID:" + retailTrade.getSourceID());
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0; i<retailTradesCreatedInServer.size(); i++) {
                    stringBuilder.append(retailTradesCreatedInServer.get(i).getID() + ",");
                }
                System.out.println(stringBuilder.toString());
                return false;
            }
            com.bx.erp.model.RetailTrade returnRetailTrade = (com.bx.erp.model.RetailTrade) retailTradeSQLiteBO.getSqLiteEvent().getBaseModel1();
            System.out.println("====生成的退货单为：" + returnRetailTrade);
            Assert.assertTrue("零售单创建退货单失败！！", returnRetailTrade != null);
            // 上传退货型零售单
            try {
                uploadTempRetailTrade();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retailTradesCreatedInServer.remove(ramdomIndex);
        }
        return true;
    }

    private boolean createRetailTrade() {
        // 收银1单。如果有错，记录在sbError中
        StringBuilder sbError = new StringBuilder();
        List<Commodity> commodityList = ShopRobotTestEx.getCommodityList(MAX_NOOfCommodityPerTrade, false, sbError);
        if (commodityList == null || commodityList.size() == 0) {
            return false;
        }
        com.bx.erp.model.RetailTrade retailTrade = null;
        retailTrade = createRetailTradeInSQLite(commodityList, sbError, wxPayHttpBO);
        if (retailTrade == null) {
            sbError.append("创建零售单在SQLite中失败");
            return false;
        }
        return true;
    }

    private com.bx.erp.model.RetailTrade createRetailTradeInSQLite(List<Commodity> commodityList, StringBuilder sbError, WXPayHttpBO wxPayHttpBO) {
        com.bx.erp.model.RetailTrade retailtrade = new com.bx.erp.model.RetailTrade();
        try {
            long maxFrameIDInSQLite = retailTradePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
            if (retailTradePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                sbError.append("查找本地零售单最大ID失败！");
                return null;
            }
            retailtrade.setStatus(1);
            retailtrade.setID(maxFrameIDInSQLite);
//            retailtrade.setSaleDatetime(new Date());
            /** 模拟多天上班*/
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, ShopRobotTestEx.dayNumber);
            retailtrade.setSaleDatetime(calendar.getTime());
            retailtrade.setDatetimeStart(new Date());
            retailtrade.setDatetimeEnd(new Date());
            retailtrade.setSourceID(-1);
            retailtrade.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailtrade.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
//            retailtrade.setAmountWeChat(commodityList.get(0).getPriceRetail());
            retailtrade.setAmountCash(commodityList.get(0).getPriceRetail());
            retailtrade.setAmount(GeneralUtil.sumN(retailtrade.getAmountCash(), retailtrade.getAmountAlipay(), retailtrade.getAmountWeChat(), retailtrade.getAmount1(), retailtrade.getAmount2(), retailtrade.getAmount3(), retailtrade.getAmount4(), retailtrade.getAmount5()));
            // TODO 暂时设为1
            retailtrade.setPaymentType(1);
            retailtrade.setSmallSheetID(1); //... 将来使用当前使用的小票格式
            retailtrade.setStaffID(BaseActivity.retailTradeAggregation.getStaffID());
            retailtrade.setPos_ID(Constants.posID);
            retailtrade.setLocalSN((int) maxFrameIDInSQLite);
            retailtrade.setSn(retailtrade.generateRetailTradeSN(Constants.posID));
            retailtrade.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
            retailtrade.setReturnObject(1);
            retailtrade.setSyncType(BasePresenter.SYNC_Type_C);
            // 在沙箱环境中模拟支付该零售单
            com.bx.erp.model.RetailTrade microPayRT = (com.bx.erp.model.RetailTrade) retailtrade.clone();
            if(retailtrade.getPaymentType() == 4) {
                if (!wxMicroPay(microPayRT, sbError)) {
                    sbError.append("--Pos机器人沙箱微信支付失败，使用现金支付--");
                    retailtrade.setPaymentType(1);
                    retailtrade.setAmountCash(retailtrade.getAmountWeChat());
                    retailtrade.setAmountWeChat(0.00000d);
                } else {
                    // 沙箱环境的微信支付成功
                    retailtrade.setWxRefundSubMchID(Constants.submchid);
                    retailtrade.setWxOrderSN(microPayResponse.get("transaction_id"));
                    retailtrade.setWxTradeNO(microPayResponse.get("out_trade_no"));
                }
            }
            List<RetailTradeCommodity> listRTComm = ShopRobotTestEx.getRetailTradeCommodityList(commodityList, MAX_NOPerCommodity, retailtrade, sbError);
            if (listRTComm == null || listRTComm.size() == 0) {
                org.junit.Assert.assertTrue("获取零售单商品失败", false);
                sbError.append("获取零售单商品失败！");
                return null;
            }
            retailtrade.setListSlave1(listRTComm);
            if (retailtrade.getPaymentType() == 1) {
                retailtrade.setAmountCash(listRTComm.get(0).getAmountWeChat());
            } else {
                retailtrade.setAmountWeChat(listRTComm.get(0).getAmountWeChat()); //带有这批零售单商品的总价格
            }
            // 汇总零售单的价格
            retailtrade.setAmount(GeneralUtil.sumN(retailtrade.getAmountCash(), retailtrade.getAmountAlipay(), retailtrade.getAmountWeChat(), retailtrade.getAmount1(), retailtrade.getAmount2(), retailtrade.getAmount3(), retailtrade.getAmount4(), retailtrade.getAmount5()));
            retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
            retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_CreateAsync);
            if (!retailTradeSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, retailtrade)) {
                System.out.println("创建零售单失败：" + retailTradeSQLiteEvent.getLastErrorCode() + "错误信息：" + retailTradeSQLiteEvent.getLastErrorMessage());
                sbError.append("创建零售单失败：" + retailtrade);
                return null;
            }
            lTimeout = 60;
            while (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done && lTimeout-- > 0) {
                Thread.sleep(1000);
            }
            if (retailTradeSQLiteBO.getSqLiteEvent().getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_Done) {
                sbError.append("创建零售单超时！");
//                Assert.assertTrue("创建零售单超时！", false);
            }
            com.bx.erp.model.RetailTrade retailTrade = (com.bx.erp.model.RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailtrade);
            Assert.assertTrue("创建了一个临时零售单后查出来失败", retailTrade != null && retailTradePresenter.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError && retailTrade.getListSlave1() != null && retailTrade.getListSlave1().size() > 0);
        } catch (Exception e) {
            System.out.println("创建临时零售单失败，异常信息：" + e.getMessage());
            sbError.append("上传零售单失败！");
            System.out.println(retailTradesCreatedInServer);
            return null;
        }
        return retailtrade;
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
        //
        return true;
    }

    private void uploadTempRetailTrade() throws InterruptedException {
        // 上传临时零售单
        // 重置ListMasterTable，避免先前设置的数据，用到了后面去
        retailTradeHttpEvent.setListMasterTable(null);
        retailTradeSQLiteBO.getSqLiteEvent().setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
        retailTradeSQLiteBO.getSqLiteEvent().setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            Assert.assertTrue("查找临时零售单失败！", false);
        }
        //
        int lTimeOut = 60;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
                    || (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeHttpEvent.getStatus() != BaseEvent.EnumEventStatus.EES_Http_Done || retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            System.out.println("retailTradeHttpEvent.printErrorInfo():" + retailTradeHttpEvent.printErrorInfo());
            Assert.assertTrue("ShopRobot 上传临时零售单同步数据失败！原因：超时！", false);
        }
        List<com.bx.erp.model.RetailTrade> retailTrades = (List<com.bx.erp.model.RetailTrade>) retailTradeHttpEvent.getListMasterTable();
        Assert.assertTrue("返回的零售单List为null", retailTrades != null && retailTrades.size() != 0);
        // 因为每一次创建零售单都上传，所以上传后服务器应该是返回一条，否则就是跑测试前本地有临时零售单,影响机器人测试
        Assert.assertTrue("返回的零售单List不等于1", retailTrades.size() == 1);
        //
        RetailTrade retailTrade2 = (RetailTrade) GlobalController.getInstance().getRetailTradePresenter().retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrades.get(0));
        Assert.assertTrue("上传零售单后的数据不存在,ID" + retailTrades.get(0).getID(), retailTrade2 != null);
        com.bx.erp.model.RetailTrade retailTrade = retailTrades.get(0);
        if (retailTrade.getSourceID() == -1) {
            ShopRobotTestEx.idAddLis.add(retailTrade.getID());
            retailTradesCreatedInServer.add(retailTrade);
        } else
            System.out.println("退货型零售单：" + retailTrade);
    }
}
