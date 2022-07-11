package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class RetrieveNRetailTradeC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTrade_RetrieveNC;
    }
}
