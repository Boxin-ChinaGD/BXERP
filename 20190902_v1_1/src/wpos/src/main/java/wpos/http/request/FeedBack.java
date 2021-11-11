package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class FeedBack extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_FeedBack;
    }
}
