package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.UI.BaseSQLiteEvent;

@Component("smallSheetTextSQLiteEvent")
@Scope("prototype")
public class SmallSheetTextSQLiteEvent extends BaseSQLiteEvent {
}
