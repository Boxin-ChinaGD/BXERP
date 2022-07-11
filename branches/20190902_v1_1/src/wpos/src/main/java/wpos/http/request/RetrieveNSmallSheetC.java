package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class RetrieveNSmallSheetC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_SmallSheet_RetrieveNC;
    }
}
