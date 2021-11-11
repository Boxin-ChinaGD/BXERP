package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreateRetailTrade extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTrade_Create;
    }
}
