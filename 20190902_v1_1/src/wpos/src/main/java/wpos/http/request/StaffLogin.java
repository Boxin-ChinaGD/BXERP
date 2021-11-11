package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class StaffLogin extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_StaffLogin;
    }
}
