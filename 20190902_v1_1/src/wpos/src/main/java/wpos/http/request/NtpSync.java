package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class NtpSync extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {return EnumRequestType.ERT_NtpSync;}
}
