package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by WPNA on 2018/9/11.
 */

public class RequestSmallSheet extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_SmallSheet_RetrieveN;
    }
}
