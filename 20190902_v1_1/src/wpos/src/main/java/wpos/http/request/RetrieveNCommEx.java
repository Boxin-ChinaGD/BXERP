package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2018/10/16.
 */

public class RetrieveNCommEx extends HttpRequestUnit{
    @Override
    public HttpRequestUnit.EnumRequestType getEnumRequestType() {
        return HttpRequestUnit.EnumRequestType.ERT_CommEx_RetrieveN;
    }
}
