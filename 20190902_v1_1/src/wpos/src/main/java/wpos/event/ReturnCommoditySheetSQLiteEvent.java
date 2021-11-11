package wpos.event;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.UI.BaseSQLiteEvent;

@Component("returnCommoditySheetSQLiteEvent")
@Scope("prototype")
public class ReturnCommoditySheetSQLiteEvent extends BaseSQLiteEvent {
}
