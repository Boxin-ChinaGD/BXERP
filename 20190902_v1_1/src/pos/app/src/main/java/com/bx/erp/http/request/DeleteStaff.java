package com.bx.erp.http.request;


import com.bx.erp.http.HttpRequestUnit;

public class DeleteStaff extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Staff_Delete;
    }
}
