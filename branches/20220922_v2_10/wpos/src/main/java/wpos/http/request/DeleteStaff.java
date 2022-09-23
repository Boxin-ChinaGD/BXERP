package wpos.http.request;


import wpos.http.HttpRequestUnit;

public class DeleteStaff extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Staff_Delete;
    }
}
