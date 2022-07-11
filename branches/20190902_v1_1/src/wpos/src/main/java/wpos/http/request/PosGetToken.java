package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class PosGetToken extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_PosGetToken;
    }
}
