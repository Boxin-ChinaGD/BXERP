package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNCommodityC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Commodity_RetrieveNC;
    }
}
