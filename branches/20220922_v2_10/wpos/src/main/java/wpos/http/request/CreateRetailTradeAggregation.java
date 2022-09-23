package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreateRetailTradeAggregation extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTradeAggregation_Create;
    }
}
