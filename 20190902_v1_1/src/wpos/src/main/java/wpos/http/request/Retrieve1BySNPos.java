package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class Retrieve1BySNPos extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_POS_Retrieve1BySN;
    }
}
