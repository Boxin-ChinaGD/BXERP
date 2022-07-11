package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class StaffResetPassword extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Staff_ResetPassword;
    }
}