package com.bx.erp.event;

import com.bx.erp.model.ReturnCommoditySheet;

import java.util.List;

/**
 * Created by BOXIN on 2018/10/17.
 */

public class ReturnCommoditySheetEvent extends BaseHttpEvent {
    List<ReturnCommoditySheet> returnCommoditySheetList;

    public List<ReturnCommoditySheet> getReturnCommoditySheetList() {
        return returnCommoditySheetList;
    }

    public void setReturnCommoditySheetList(List<ReturnCommoditySheet> returnCommoditySheetList) {
        this.returnCommoditySheetList = returnCommoditySheetList;
    }
}
