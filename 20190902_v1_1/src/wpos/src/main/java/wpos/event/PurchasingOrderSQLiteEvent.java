package wpos.event;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.UI.BaseSQLiteEvent;

@Component("purchasingOrderSQLiteEvent")
@Scope("prototype")
public class PurchasingOrderSQLiteEvent extends BaseSQLiteEvent {
}
