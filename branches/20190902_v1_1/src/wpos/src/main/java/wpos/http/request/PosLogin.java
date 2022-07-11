package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class PosLogin extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_PosLogin;
    }
}
