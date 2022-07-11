package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveCommodityInventoryC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Commodity_RetrieveInventoryC;
    }
}
