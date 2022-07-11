package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.RetailTrade;

import java.util.List;

@Component("retailTradeEvent")
@Scope("prototype")
public class RetailTradeEvent extends BaseEvent {
    private List<RetailTrade> retailTradeList;

    public RetailTradeEvent(){}

    public RetailTradeEvent(List<RetailTrade> retailTradeList) {
        this.retailTradeList = retailTradeList;
    }

    public List<RetailTrade> getRetailTradeList() {
        return retailTradeList;
    }
}
