package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class DeleteVip extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Vip_Delete;
    }
}