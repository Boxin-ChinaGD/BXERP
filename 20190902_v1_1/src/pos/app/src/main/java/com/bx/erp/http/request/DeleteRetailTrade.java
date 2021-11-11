package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2018/11/13.
 */

public class DeleteRetailTrade extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTrade_Delete;
    }
}
