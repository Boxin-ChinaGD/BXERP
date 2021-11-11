package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreatePos extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Pos_CreateSync;
    }
}
