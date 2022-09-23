package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class DeleteVip extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Vip_Delete;
    }
}