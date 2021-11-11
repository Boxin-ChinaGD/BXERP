package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RequestSmallSheetText extends HttpRequestUnit{

    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RequestSmallSheetText;
    }
}
