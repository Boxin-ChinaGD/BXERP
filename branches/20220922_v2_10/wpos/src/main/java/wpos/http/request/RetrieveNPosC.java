package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNPosC extends HttpRequestUnit {

    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Pos_RetrieveNC;
    }
}
