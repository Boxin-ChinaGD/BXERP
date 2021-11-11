package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class CreateShop extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Shop_Create;
    }
}
