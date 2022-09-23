package com.bx.erp.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ellen on 2018/06/20.
 */

public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder  {
  public AbstractViewHolder(final View itemView) {
    super(itemView);
  }

  public abstract void update(T obj);
}
