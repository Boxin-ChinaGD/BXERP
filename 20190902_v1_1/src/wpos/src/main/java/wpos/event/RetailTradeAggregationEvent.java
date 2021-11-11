
package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("retailTradeAggregationEvent")
@Scope("prototype")
public class RetailTradeAggregationEvent extends BaseHttpEvent{
    protected String MSG1;
    protected String MSG2;
    protected String MSG3;

    public String getMSG1() {
        return MSG1;
    }

    public void setMSG1(String MSG1) {
        this.MSG1 = MSG1;
    }

    public String getMSG2() {
        return MSG2;
    }

    public void setMSG2(String MSG2) {
        this.MSG2 = MSG2;
    }

    public String getMSG3() {
        return MSG3;
    }

    public void setMSG3(String MSG3) {
        this.MSG3 = MSG3;
    }

    @Override
    public void onEvent() {

    }
}
