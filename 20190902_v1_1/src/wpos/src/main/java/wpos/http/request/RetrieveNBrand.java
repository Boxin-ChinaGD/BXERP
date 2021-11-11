package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNBrand extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Brand_RetrieveN;
    }
}
