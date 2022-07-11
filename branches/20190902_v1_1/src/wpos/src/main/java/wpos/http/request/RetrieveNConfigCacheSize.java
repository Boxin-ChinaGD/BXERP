package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class RetrieveNConfigCacheSize extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ConfigCacheSize_RetrieveN;
    }
}
