package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class StaffResetPassword extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Staff_ResetPassword;
    }
}