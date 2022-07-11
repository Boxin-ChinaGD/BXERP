package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2019/5/27.
 */

public class RetrieveResignedStaff extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Staff_RetrieveResigned;
    }
}
