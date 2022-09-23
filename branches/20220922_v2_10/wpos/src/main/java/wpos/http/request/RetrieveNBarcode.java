package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNBarcode extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Barcodes_RetrieveN;
    }
}
