package wpos.http.request;

import wpos.http.HttpRequestUnit;

public class RetrieveNPackageUnitC extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_PackageUnit_RetrieveNC;
    }
}