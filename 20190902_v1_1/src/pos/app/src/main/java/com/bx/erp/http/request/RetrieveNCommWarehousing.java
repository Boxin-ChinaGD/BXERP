package com.bx.erp.http.request;


import com.bx.erp.http.HttpRequestUnit;

public class RetrieveNCommWarehousing extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Warehousing_RetrieveNC;
    }
}
