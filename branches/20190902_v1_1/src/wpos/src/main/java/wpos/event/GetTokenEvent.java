package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by WPNA on 2018/9/14.
 */

@Component("getTokenEvent")
@Scope("prototype")
public class GetTokenEvent extends BaseEvent{
}
