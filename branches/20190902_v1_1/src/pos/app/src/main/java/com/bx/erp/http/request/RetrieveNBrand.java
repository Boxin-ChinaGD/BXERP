package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class RetrieveNBrand extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Brand_RetrieveN;
    }
}
