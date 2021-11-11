package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2018/12/14.
 */

public class RetrieveNVipCategoryC extends HttpRequestUnit{
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_VipCategory_RetrieveNC;
    }
}
