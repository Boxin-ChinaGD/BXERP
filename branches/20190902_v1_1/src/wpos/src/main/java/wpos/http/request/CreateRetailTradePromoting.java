package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreateRetailTradePromoting extends HttpRequestUnit  {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_RetailTradePromoting_Create;
    }
}
