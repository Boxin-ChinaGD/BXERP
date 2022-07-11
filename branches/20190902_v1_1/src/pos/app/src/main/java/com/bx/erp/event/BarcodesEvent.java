package com.bx.erp.event;

import com.bx.erp.model.Barcodes;

import java.util.List;

/**
 * Created by WPNA on 2018/8/17.
 */

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
