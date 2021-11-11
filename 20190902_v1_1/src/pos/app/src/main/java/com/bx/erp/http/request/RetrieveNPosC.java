package com.bx.erp.http.request;

import com.bx.erp.http.HttpRequestUnit;

public class RetrieveNPosC extends HttpRequestUnit {

    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Pos_RetrieveNC;
    }
}
