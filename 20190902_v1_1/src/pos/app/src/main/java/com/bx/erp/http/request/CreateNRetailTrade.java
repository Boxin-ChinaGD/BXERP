package com.bx.erp.http.request;


import com.bx.erp.http.HttpRequestUnit;

public class CreateNRetailTrade extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTrade_CreateN;
    }
}
