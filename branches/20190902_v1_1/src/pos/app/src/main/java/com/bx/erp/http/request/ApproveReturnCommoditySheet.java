package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2018/10/17.
 */

public class ApproveReturnCommoditySheet extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ApproveReturnCommoditySheet;
    }
}
