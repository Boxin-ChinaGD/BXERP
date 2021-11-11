package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class SeekRetailTradeCommodity extends HttpRequestUnit {

    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTradeCommodity_RetrieveN;
    }
}
