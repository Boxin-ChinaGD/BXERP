package wpos.event;

import wpos.model.Commodity;

public class DeliveryWayClickEvent {
  public int position;
  public Commodity model;

  public DeliveryWayClickEvent(int position, Commodity model) {
    this.position = position;
    this.model = model;
  }
}
