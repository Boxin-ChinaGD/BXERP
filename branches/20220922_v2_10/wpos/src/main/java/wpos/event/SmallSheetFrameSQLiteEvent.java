package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.UI.BaseSQLiteEvent;


@Component("smallSheetFrameSQLiteEvent")
@Scope("prototype")
public class SmallSheetFrameSQLiteEvent extends BaseSQLiteEvent {
}
