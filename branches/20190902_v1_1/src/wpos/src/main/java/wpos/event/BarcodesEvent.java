package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.model.Barcodes;

import java.util.List;

/**
 * Created by WPNA on 2018/8/17.
 */

@Component("barcodesEvent")
@Scope("prototype")
public class BarcodesEvent extends BaseHttpEvent{
    public BarcodesEvent(){

    }
    private List<Barcodes> barcodesList;
    public BarcodesEvent(List<Barcodes> barcodesList){
        this.barcodesList=barcodesList;
    }
    public List<Barcodes> getBarcodesList(){
        return barcodesList;
    }
    public void setBarcodesList(List<Barcodes> barcodesList){
        this.barcodesList=barcodesList;
    }
}
