package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNRetailTrade extends HttpRequestUnit {
        @Override
        public EnumRequestType getEnumRequestType() {
            return EnumRequestType.ERT_RetailTrade_RetrieveN;
        }
}
