package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2018/10/17.
 */

public class ApproveWarehousing extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_ApproveWarehousing;
    }
}
