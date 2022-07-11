package com.test.bx.app.robot;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.CommodityHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.CommodityHttpEvent;
import com.bx.erp.event.UI.CommoditySQLiteEvent;
import com.bx.erp.model.Commodity;
import com.bx.erp.presenter.CommodityPresenter;


import java.util.List;

public class RobotConfig {
    private static CommodityPresenter commodityPresenter = null;
    private static CommodityHttpBO commodityHttpBO = null;
    private static CommodityHttpEvent commodityHttpEvent = null;
    private static CommoditySQLiteEvent commoditySQLiteEvent = null;

    public long getMaxCommodityID() {
        return maxCommodityID;
    }

    public void setMaxCommodityID(long maxCommodityID) {
        this.maxCommodityID = maxCommodityID;
    }

    //机器人跑之前，先将DB中最大的F_ID读出来，那么机器餐中的ID，在DB中实际上就是F_ID+ID
    //
    public static long maxCommodityID = 0l;

    public boolean load() {
        //
        commodityPresenter = GlobalController.getInstance().getCommodityPresenter();
        List<Commodity> commodityList = (List<Commodity>) commodityPresenter.retrieveNObjectSync(BaseSQLiteBO.INVALID_CASE_ID, null);
        maxCommodityID = commodityList.get(commodityList.size() - 1).getID();//...数组可能越界
        System.out.print("+++++++" + maxCommodityID);
        return true;


    }
}
