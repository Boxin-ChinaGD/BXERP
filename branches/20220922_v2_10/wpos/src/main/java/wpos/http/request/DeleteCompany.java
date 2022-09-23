package wpos.http.request;

import wpos.http.HttpRequestUnit;


public class DeleteCompany extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Company_Delete;
    }
}
