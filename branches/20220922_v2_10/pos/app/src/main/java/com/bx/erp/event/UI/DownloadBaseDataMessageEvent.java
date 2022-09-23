package com.bx.erp.event.UI;

import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.BaseHttpEvent;

public class DownloadBaseDataMessageEvent extends BaseSQLiteEvent {
    private String message;

    public String getMessage() {
        setStatus(BaseEvent.EnumEventStatus.EES_Http_Done);
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
