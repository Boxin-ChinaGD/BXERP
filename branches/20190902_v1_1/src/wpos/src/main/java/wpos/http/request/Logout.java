package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class Logout extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_StaffLogout;
    }
}
