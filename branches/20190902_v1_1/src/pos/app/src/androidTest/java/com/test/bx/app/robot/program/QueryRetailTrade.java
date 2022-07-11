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
            // 随机查单
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
            Assert.assertTrue("请求服务器超时!", false);
        }
        return retailTradeHttpBO.getHttpEvent().getListMasterTable();
    }

    private boolean randomQueryRetailTrade(int queryCase, StringBuilder sbError) throws Exception {
        Random random = new Random();
        switch (queryCase){
            case 0:
                // 没有查询条件
                ShopRobotTest.caseLog("没有查询条件零售单");
                RetailTrade queryRetailTradeForNothing = new RetailTrade();
                queryRetailTradeForNothing.setQueryKeyword("");
                queryRetailTradeForNothing.setDatetimeStart(Constants.getDefaultSyncDatetime());
                queryRetailTradeForNothing.setDatetimeEnd(new Date());
                List<?> quertRetailTradeList = queryRetailTradeForNBR(queryRetailTradeForNothing);
                if (quertRetailTradeList == null || quertRetailTradeList.size() == 0){
                    sbError.append("无条件查询零售单失败！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("无条件查询零售单成功！" + quertRetailTradeList);
                break;
            case 1:
                // 根据SN查询c
                ShopRobotTest.caseLog("根据SN查询零售单");
                RetailTrade queryRetailTradeForSN = new RetailTrade();
                queryRetailTradeForSN.setQueryKeyword(listRTForQuery.get(random.nextInt(listRTForQuery.size())).getSn());
                List<?> quertRetailTradeList2 = queryRetailTradeForNBR(queryRetailTradeForSN);
                if (quertRetailTradeList2 == null || quertRetailTradeList2.size() != 1){
                    sbError.append("根据SN查询零售单失败！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("根据SN查询零售单成功！" + quertRetailTradeList2);
                break;
            case 2:
                // 根据时间查询
                ShopRobotTest.caseLog("根据时间查询零售单");
                RetailTrade queryRetailTradeForDate = new RetailTrade();
                queryRetailTradeForDate.setQueryKeyword("");
                queryRetailTradeForDate.setDatetimeStart(listRTForQuery.get(random.nextInt(listRTForQuery.size())).getDatetimeStart());
                List<?> quertRetailTradeList3 = queryRetailTradeForNBR(queryRetailTradeForDate);
                if (quertRetailTradeList3 == null || quertRetailTradeList3.size() == 0){
                    sbError.append("根据时间查询零售单失败！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("根据时间查询零售单成功！" + quertRetailTradeList3);
                break;
            default:
                // 根据SN以及时间查询
                ShopRobotTest.caseLog("根据SN以及时间查询零售单");
                List<?> quertRetailTradeList4 = queryRetailTradeForNBR(listRTForQuery.get(random.nextInt(listRTForQuery.size())));
                if (quertRetailTradeList4 == null || quertRetailTradeList4.size() == 0){
                    sbError.append("根据SN以及时间查询零售单失败！！");
                    System.out.println(sbError.toString());
                    return false;
                }
                System.out.println("根据SN以及时间查询零售单成功！" + quertRetailTradeList4);
                break;
        }
        //
        return true;
    }

}
