package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.ReturnCommoditySheet;

import java.util.List;

/**
 * Created by BOXIN on 2018/10/17.
 */

@Component("returnCommoditySheetEvent")
@Scope("prototype")
public class ReturnCommoditySheetEvent extends BaseHttpEvent {
    List<ReturnCommoditySheet> returnCommoditySheetList;

    public List<ReturnCommoditySheet> getReturnCommoditySheetList() {
        return returnCommoditySheetList;
    }

    public void setReturnCommoditySheetList(List<ReturnCommoditySheet> returnCommoditySheetList) {
        this.returnCommoditySheetList = returnCommoditySheetList;
    }
}
