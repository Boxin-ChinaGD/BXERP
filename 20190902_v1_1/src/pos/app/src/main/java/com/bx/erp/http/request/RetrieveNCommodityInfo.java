package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;


public class RetrieveNCommodityInfo extends HttpRequestUnit {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_CommodityInfo_RetrieveN;
    }
}
