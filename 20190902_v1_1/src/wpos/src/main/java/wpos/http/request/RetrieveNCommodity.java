package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNCommodity extends HttpRequestUnit {

    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Commodity_RetrieveN;
    }
}
