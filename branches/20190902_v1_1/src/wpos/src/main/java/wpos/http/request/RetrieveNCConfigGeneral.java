package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNCConfigGeneral extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ConfigGeneral_RetrieveNC;
    }
}
