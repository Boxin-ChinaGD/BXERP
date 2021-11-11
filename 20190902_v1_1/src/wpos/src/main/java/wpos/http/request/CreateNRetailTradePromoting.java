package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreateNRetailTradePromoting extends HttpRequestUnit {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_RetailTradePromoting_CreateN;
    }
}
