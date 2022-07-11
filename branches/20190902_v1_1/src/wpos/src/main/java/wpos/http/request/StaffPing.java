package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2020/1/9.
 */

public class StaffPing extends HttpRequestUnit{
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_Staff_Ping;
    }
}
