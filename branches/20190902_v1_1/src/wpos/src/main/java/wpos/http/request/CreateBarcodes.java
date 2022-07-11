package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class CreateBarcodes extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Barcodes_Create;
    }
}
