package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class CreateRetailTradePromoting extends HttpRequestUnit  {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_RetailTradePromoting_Create;
    }
}
