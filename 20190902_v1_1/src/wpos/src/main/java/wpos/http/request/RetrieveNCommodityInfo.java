package wpos.http.request;

import wpos.http.HttpRequestUnit;


public class RetrieveNCommodityInfo extends HttpRequestUnit {
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_CommodityInfo_RetrieveN;
    }
}
