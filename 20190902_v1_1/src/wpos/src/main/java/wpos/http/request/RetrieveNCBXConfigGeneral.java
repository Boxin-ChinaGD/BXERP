package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class RetrieveNCBXConfigGeneral extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_BXConfigGeneral_RetrieveNC;
    }
}
