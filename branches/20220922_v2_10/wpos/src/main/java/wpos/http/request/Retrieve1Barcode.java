package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class Retrieve1Barcode extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {return EnumRequestType.ERT_Barcodes_Retrieve1;}
}
