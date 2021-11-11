package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

/**
 * Created by WPNA on 2018/9/11.
 */

public class RequestSmallSheet extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_SmallSheet_RetrieveN;
    }
}
