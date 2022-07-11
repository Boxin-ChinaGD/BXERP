package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNCConfigCacheSize extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ConfigCacheSize_RetrieveNC;
    }
}
