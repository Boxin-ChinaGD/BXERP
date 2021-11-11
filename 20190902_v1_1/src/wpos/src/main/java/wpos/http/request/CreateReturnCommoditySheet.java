package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreateReturnCommoditySheet extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_CreateReturnCommoditySheet;
    }
}
