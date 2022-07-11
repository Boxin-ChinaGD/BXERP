package com.bx.erp.event;

import com.bx.erp.model.Vip;

import java.util.List;

/**
 * Created by WPNA on 2018/8/13.
 */

public class RetrieveAllVipEvent {
    private List<Vip> vips;
    public RetrieveAllVipEvent(List<Vip> vips){
        this.vips=vips;
    }
    public List<Vip> getVips(){
        return vips;
    }
}
