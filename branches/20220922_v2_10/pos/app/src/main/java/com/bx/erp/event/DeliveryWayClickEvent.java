package com.bx.erp.event;

import com.bx.erp.model.Commodity;

public class DeliveryWayClickEvent {
  public int position;
  public Commodity model;

  public DeliveryWayClickEvent(int position, Commodity model) {
    this.position = position;
    this.model = model;
  }
}
