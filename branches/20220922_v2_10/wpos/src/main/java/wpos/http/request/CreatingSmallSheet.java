package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreatingSmallSheet extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_SmallSheet_Create;
    }
}
