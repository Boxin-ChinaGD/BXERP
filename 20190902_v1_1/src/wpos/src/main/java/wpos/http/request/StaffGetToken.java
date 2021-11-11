package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class StaffGetToken extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_StaffGetToken;
    }
}
