package wpos.http.request;

import wpos.http.HttpRequestUnit;


public class DeletePos extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_POS_Delete;
    }
}
