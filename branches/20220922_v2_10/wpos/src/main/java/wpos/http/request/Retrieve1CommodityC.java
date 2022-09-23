package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class Retrieve1CommodityC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Commodity_Retrieve1C;
    }
}
