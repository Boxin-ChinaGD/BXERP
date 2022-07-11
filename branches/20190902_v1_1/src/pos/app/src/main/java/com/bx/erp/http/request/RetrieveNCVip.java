package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2019/1/14.
 */

public class RetrieveNCVip extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Vip_RetrieveNC;
    }
}
