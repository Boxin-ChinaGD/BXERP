package com.bx.erp.http.request;


import com.bx.erp.http.HttpRequestUnit;

public class Retrieve1CommodityC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Commodity_Retrieve1C;
    }
}
