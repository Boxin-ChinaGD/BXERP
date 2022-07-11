package com.bx.erp.event;

import com.bx.erp.common.GlobalController;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.utils.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class LogoutHttpEvent extends BaseHttpEvent {
    private Logger log = Logger.getLogger(this.getClass());

    public LogoutHttpEvent() {

    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("getRequestType()", getRequestType());

        do {
            if (lastErrorCode != ErrorInfo.EnumErrorCode.EC_NoError) {
                log.info("退出登录失败" + lastErrorCode);
                break;
            }
            //
            JSONObject jsonObject = parseError(getResponseData());
            if (jsonObject == null || getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_DuplicatedSession) {
                break;
            }
            //
            switch (getRequestType()) {
                case ERT_StaffLogout:
                    GlobalController.getInstance().setSessionID(null);
                    break;
                default:
                    break;
            }
        } while (false);

        //让调用者不要再等待
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        setEventProcessed(true);
    }
}
