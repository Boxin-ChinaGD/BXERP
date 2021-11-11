package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.UI.BaseSQLiteEvent;

@Component("retailTradeCommoditySQLiteEvent")
@Scope("prototype")
public class RetailTradeCommoditySQLiteEvent extends BaseSQLiteEvent {
}
