package com.bx.erp.event;

import com.bx.erp.model.RetailTrade;

import java.util.List;

public class RetailTradeEvent extends BaseEvent {
    private List<RetailTrade> retailTradeList;

    public RetailTradeEvent(List<RetailTrade> retailTradeList) {
        this.retailTradeList = retailTradeList;
    }

    public List<RetailTrade> getRetailTradeList() {
        return retailTradeList;
    }
}
