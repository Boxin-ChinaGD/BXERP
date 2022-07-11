package wpos.event;

import wpos.model.Commodity;

public class ItemClickEvent {
  public int position;
  public Commodity data;

  public ItemClickEvent(int position, Commodity data) {
    this.position = position;
    this.data = data;
  }
}
