package wpos.event.UI;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.BaseEvent;

@Component("downloadBaseDataMessageEvent")
@Scope("prototype")
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
