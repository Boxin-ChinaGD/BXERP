package com.bx.erp.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.event.DeliveryWayClickEvent;
import com.bx.erp.model.Commodity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommodityViewHolder extends AbstractViewHolder<Commodity> {
  @BindView(R.id.tv_line_num) TextView tvLineNum;
  @BindView(R.id.tv_code) TextView tvCode;
  @BindView(R.id.tv_name) TextView tvName;
  @BindView(R.id.tv_count) TextView tvCount;
  @BindView(R.id.tv_unit) TextView tvUnit;
  @BindView(R.id.tv_original_price) TextView tvOriginalPrice;
  @BindView(R.id.tv_sales_price) TextView tvSalesPrice;
  @BindView(R.id.tv_subtotal_price) TextView tvSubtotalPrice;
  @BindView(R.id.tv_delivery_way) TextView tvDeliveryWay;

  private Commodity data;

  public CommodityViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
//        if (!data.isSelected()) {
//          data.setSelected(true);
//          EventBus.getDefault().post(new ItemClickEvent(getLayoutPosition(), data));
//          update(data);
//        }
      }
    });
  }

  @Override public void update(Commodity obj) {
    this.data = obj;
    tvLineNum.setText(String.valueOf(getLayoutPosition()));
//    tvCode.setText(obj.getCode());
//    tvName.setText(obj.getName());
//    tvCount.setText(String.valueOf(obj.getNumber()));
//    tvUnit.setText(obj.getUnit());
//    tvOriginalPrice.setText(String.format("%.2f", obj.getUnit_price()));
//    tvSalesPrice.setText(String.format("%.2f", obj.getUnit_price()));
//    tvSubtotalPrice.setText(String.format("%.2f", Integer.valueOf(obj.getNumber()) * Double.valueOf(obj.getUnit_price())));
//    String way;
//    if (obj.getDeliveryWay() == DeliverWay.Locale) {
//      way = "现场取货";
//    } else {
//      way = "仓库配送";
//    }
//    tvDeliveryWay.setText(way);
//    itemView.setSelected(obj.isSelected());

  }

  @OnClick(R.id.tv_delivery_way) public void onViewClicked() {
    EventBus.getDefault().post(new DeliveryWayClickEvent(getLayoutPosition(), data));
  }
}
