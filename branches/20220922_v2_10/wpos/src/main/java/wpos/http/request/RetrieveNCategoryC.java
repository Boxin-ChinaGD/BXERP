package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNCategoryC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_CommodityCategory_RetrieveNC;
    }
}
