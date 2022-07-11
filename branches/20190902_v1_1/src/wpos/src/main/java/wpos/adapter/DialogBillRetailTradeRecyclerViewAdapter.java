package wpos.adapter;


import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import wpos.helper.Constants;
import wpos.model.RetailTrade;
import wpos.utils.DatetimeUtil;
import wpos.utils.GeneralUtil;

import java.text.SimpleDateFormat;
import java.util.List;


public class DialogBillRetailTradeRecyclerViewAdapter extends ListCell<RetailTrade> {
    private DialogBillRetailTradeRecyclerViewAdapter.OnClickListener onClickListener;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default7);

    public DialogBillRetailTradeRecyclerViewAdapter() {

    }

    @Override
    protected void updateItem(RetailTrade item, boolean empty) {
        if (item != null && !empty) {
            System.out.println("进来了updateItem");
            GridPane gridPane = new GridPane();
            gridPane.setPrefHeight(72);
            //添加一行
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            gridPane.getRowConstraints().add(rowConstraints);
            //
            gridPane.getColumnConstraints().add(new ColumnConstraints(150, 150, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label sn = new DialogBillRetailTradeRecyclerViewAdapter.ItemLabel(item.getSn());
            GridPane.setConstraints(sn, 0, 0);
            //
            gridPane.getColumnConstraints().add(new ColumnConstraints(150, 150, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label holdBillTime = new DialogBillRetailTradeRecyclerViewAdapter.ItemLabel(sdf.format(item.getHoldBillTime()));
            GridPane.setConstraints(holdBillTime, 1, 0);
            //
            gridPane.getColumnConstraints().add(new ColumnConstraints(150, 150, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label firstCommodity = new DialogBillRetailTradeRecyclerViewAdapter.ItemLabel(item.getFirstCommodityName());
            GridPane.setConstraints(firstCommodity, 2, 0);
            //
            gridPane.getColumnConstraints().add(new ColumnConstraints(150, 150, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label vipMobile = new DialogBillRetailTradeRecyclerViewAdapter.ItemLabel(item.getVip() == null ? "" : item.getVip().getMobile());
            GridPane.setConstraints(vipMobile, 3, 0);

            gridPane.getChildren().addAll(sn, holdBillTime, firstCommodity, vipMobile);
            gridPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    //左击事件
                    if (onClickListener != null) {
                        onClickListener.onLeftClick(item);
                    }
                }
            });
            setGraphic(gridPane);
        }
        super.updateItem(item, empty);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    @Override
    protected boolean isItemChanged(RetailTrade oldItem, RetailTrade newItem) {
        return super.isItemChanged(oldItem, newItem);
    }


    public void setOnLickListener(DialogBillRetailTradeRecyclerViewAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    class ItemLabel extends Label {
        public ItemLabel(String s) {
            setFont(new Font(24));
            setStyle("-fx-text-fill: #263238;");
            setText(s);
        }
    }

    public interface OnClickListener {
        void onLeftClick(RetailTrade newValue);
    }
}
