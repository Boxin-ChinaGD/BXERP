package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2018/12/14.
 */

public class UpdateVipCategory extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_VipCategory_Update;
    }
}
