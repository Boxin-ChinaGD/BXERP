package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class SeekRetailTradeCommodity extends HttpRequestUnit {

    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_RetailTradeCommodity_RetrieveN;
    }
}
