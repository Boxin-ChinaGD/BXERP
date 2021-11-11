package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNSmallSheet extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_SmallSheet_RetrieveN;
    }
}
