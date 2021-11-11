package wpos.http.request;

import wpos.http.HttpRequestUnit;


public class UpdateBrand extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Brand_Update;
    }
}
