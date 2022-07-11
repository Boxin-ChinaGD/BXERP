package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class CreateNRetailTrade extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTrade_CreateN;
    }
}
