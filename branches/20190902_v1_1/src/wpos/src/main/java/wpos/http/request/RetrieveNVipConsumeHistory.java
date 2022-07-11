package wpos.http.request;

import wpos.http.HttpRequestUnit;

/**
 * Created by BOXIN on 2019/2/1.
 */

public class RetrieveNVipConsumeHistory extends HttpRequestUnit {
    @Override
    public EnumRequestType getEnumRequestType() {
        return EnumRequestType.ERT_Vip_RetrieveNVipConsumeHistory;
    }
}
