package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNBrandC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Brand_RetrieveNC;
    }
}
