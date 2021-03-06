package com.test.bx.app.robot.program;

import android.widget.Toast;

import com.bx.erp.bo.BaseHttpBO;
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
import com.bx.erp.presenter.RetailTradeCommodityPresenter;
import com.bx.erp.presenter.RetailTradePresenter;
import com.bx.erp.utils.Shared;
import com.test.bx.app.robot.Robot;
import com.test.bx.app.robot.RobotConfig;
import com.test.bx.app.robot.ShopRobotTest;

import org.junit.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by WPNA on 2019/10/11.
 */

public class QueryRetailTrade extends Program {

    private RetailTradePresenter retailTradePresenter;
    private RetailTradeCommodityPresenter retailTradeCommodityPresenter;
    private RetailTradeSQLiteBO retailTradeSQLiteBO;
    private RetailTradeSQLiteEvent retailTradeSQLiteEvent;
    private RetailTradeHttpBO retailTradeHttpBO;
    private RetailTradeHttpEvent retailTradeHttpEvent;
    private List<RetailTrade> listRTForQuery;

    public void setListRTForQuery(List<RetailTrade> listRTForQuery) {
        this.listRTForQuery = listRTForQuery;
    }

    public QueryRetailTrade(Date startDatetime, Date endDatetime, final RobotConfig rc, RetailTradeHttpEvent retailTradeHttpEvent, RetailTradeSQLiteBO retailTradeSQLiteBO, RetailTradeHttpBO retailTradeHttpBO , RetailTradeSQLiteEvent retailTradeSQLiteEvent, boolean bRunInRandomMode) {
        super(startDatetime, endDatetime, rc, bRunInRandomMode);
        retailTradePresenter = GlobalController.getInstance().getRetailTradePresenter();
        this.retailTradeSQLiteBO = retailTradeSQLiteBO;
        retailTradeCommodityPresenter = GlobalController.getInstance().getRetailTradeCommodityPresenter();
        this.retailTradeHttpEvent = retailTradeHttpEvent;
        this.retailTradeSQLiteEvent = retailTradeSQLiteEvent;
        this.retailTradeHttpBO = retailTradeHttpBO;
        //
        listRTForQuery = new ArrayList<>();
    }


    @Override
    protected void generateReport() {

    }

    @Override
    public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) throws Exception {
        if (canRunNow(currentDatetime)){
            Random random = new Random();
            // ????????????
            return randomQueryRetailTrade(random.nextInt(4), sbError);
        }
        return false;
    }

    @Override
    protected boolean canRunNow(Date d) {
        if (listRTForQuery != null && listRTForQuery.size() > 0){
            return true;
        }

        return false;
    }

    @Override
    public void resetForNextDay() {

    }

    private List<?> queryRetailTradeForNBR(RetailTrade retailTrade) throws InterruptedException {
        retailTrade.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
        retailTrade.setPageSize(BaseHttpBO.PAGE_SIZE_Default);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_updateNAsync);
        if (!retailTradeHttpBO.retrieveNAsyncC(BaseHttpBO.CASE_RetailTrade_RetrieveNOldRetailTradeAsyncC, retailTrade)) {
            System.out.println("??????????????????????????????");
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
            Assert.assertTrue("?????????????????????!", false);
        }
        return retailTradeHttpBO.getHttpEvent().getListMasterTable();
    }

    private boolean randomQueryRetailTrade(int queryCase, StringBuilder sbError) throws Exception {
        Random random = new Random();
        switch (queryCase){
            case 0:
                // ??????????????????
                ShopRobotTest.caseLog("???????????????????????????");
                RetailTrade queryRetailTradeForNothing = new RetailTrade();
                queryRetailTradeForNothing.setQueryKeyword("");
                queryRetailTradeForNothing.setDatetimeStart(Constants.getDefaultSyncDatetime());
                queryRetailTradeForNothing.setDatetimeEnd(new Date());
                List<?> quertRetailTradeList = queryRetailTradeForNBR(queryRetailTradeForNothing);
                if (quertRetailTradeList == null || quertRetailTradeList.size() == 0){
                    sbError.append("?????????????????????????????????");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("?????????????????????????????????" + quertRetailTradeList);
                break;
            case 1:
                // ??????SN??????c
                ShopRobotTest.caseLog("??????SN???????????????");
                RetailTrade queryRetailTradeForSN = new RetailTrade();
                queryRetailTradeForSN.setQueryKeyword(listRTForQuery.get(random.nextInt(listRTForQuery.size())).getSn());
                List<?> quertRetailTradeList2 = queryRetailTradeForNBR(queryRetailTradeForSN);
                if (quertRetailTradeList2 == null || quertRetailTradeList2.size() != 1){
                    sbError.append("??????SN????????????????????????");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("??????SN????????????????????????" + quertRetailTradeList2);
                break;
            case 2:
                // ??????????????????
                ShopRobotTest.caseLog("???????????????????????????");
                RetailTrade queryRetailTradeForDate = new RetailTrade();
                queryRetailTradeForDate.setQueryKeyword("");
                queryRetailTradeForDate.setDatetimeStart(listRTForQuery.get(random.nextInt(listRTForQuery.size())).getDatetimeStart());
                List<?> quertRetailTradeList3 = queryRetailTradeForNBR(queryRetailTradeForDate);
                if (quertRetailTradeList3 == null || quertRetailTradeList3.size() == 0){
                    sbError.append("????????????????????????????????????");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("????????????????????????????????????" + quertRetailTradeList3);
                break;
            default:
                // ??????SN??????????????????
                ShopRobotTest.caseLog("??????SN???????????????????????????");
                List<?> quertRetailTradeList4 = queryRetailTradeForNBR(listRTForQuery.get(random.nextInt(listRTForQuery.size())));
                if (quertRetailTradeList4 == null || quertRetailTradeList4.size() == 0){
                    sbError.append("??????SN???????????????????????????????????????");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("??????SN????????????????????????????????????" + quertRetailTradeList4);
                break;
        }
        //
        return true;
    }

}
