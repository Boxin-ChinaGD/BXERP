package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class DeleteSmallSheet extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_SmallSheet_Delete;
    }
}
