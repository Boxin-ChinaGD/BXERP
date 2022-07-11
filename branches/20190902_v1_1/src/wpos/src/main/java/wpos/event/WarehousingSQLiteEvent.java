package wpos.event;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.UI.BaseSQLiteEvent;


@Component("warehousingSQLiteEvent")
@Scope("prototype")
public class WarehousingSQLiteEvent extends BaseSQLiteEvent {
}
