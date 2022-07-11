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
import wpos.model.Commodity;
import wpos.utils.GeneralUtil;


public class CommodityListCellAdapter extends ListCell<Commodity> {

    private onClickListener listener;

    public CommodityListCellAdapter() {

    }

    @Override
    protected void updateItem(Commodity item, boolean empty) {
        if (item != null && !empty) {
            GridPane gridPane = new GridPane();
            gridPane.setPrefHeight(72);
            //添加一行
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            gridPane.getRowConstraints().add(rowConstraints);

            gridPane.getColumnConstraints().add(new ColumnConstraints(100,100,100, Priority.ALWAYS, HPos.CENTER,true));
            Label num = new ItemLabel(String.valueOf(item.getNumber()));
            GridPane.setConstraints(num, 0, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(300,300,300, Priority.ALWAYS, HPos.CENTER,true));
            Label bar_code = new ItemLabel(String.valueOf(item.getBarcode()));
            GridPane.setConstraints(bar_code, 1, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(300,300,300, Priority.ALWAYS, HPos.CENTER,true));
            Label name_attr = new ItemLabel(String.valueOf(item.getName()));
            GridPane.setConstraints(name_attr, 2, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label unit = new ItemLabel(String.valueOf(item.getPackageUnit()));
            GridPane.setConstraints(unit, 3, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label goods_quantity = new ItemLabel(String.valueOf(item.getCommodityQuantity()));
            GridPane.setConstraints(goods_quantity, 4, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label unit_price = new ItemLabel(GeneralUtil.formatToShow(item.getPriceRetail()));
            GridPane.setConstraints(unit_price, 5, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label discount = new ItemLabel(GeneralUtil.formatToShow(item.getDiscount()));
            GridPane.setConstraints(discount, 6, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label after_discount = new ItemLabel(GeneralUtil.formatToShow(item.getAfter_discount()));
            GridPane.setConstraints(after_discount, 7, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label total_money = new ItemLabel(GeneralUtil.formatToShow(item.getSubtotal()));
            GridPane.setConstraints(total_money, 8, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(150,150,150, Priority.ALWAYS, HPos.CENTER,true));
            Label remark = new ItemLabel(String.valueOf(item.getTag()));
            GridPane.setConstraints(remark, 9, 0);

            gridPane.getChildren().addAll(num, bar_code, name_attr, unit, goods_quantity, unit_price, discount, after_discount, total_money, remark);
            gridPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY ){
                    //右击事件
                    listener.onRightClick();
                }else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    //左击事件
                    listener.onLeftClick(item);
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
    protected boolean isItemChanged(Commodity oldItem, Commodity newItem) {
        return super.isItemChanged(oldItem, newItem);
    }

    public void setOnClickListener(onClickListener listener) {
        this.listener = listener;
    }

    class ItemLabel extends Label {
        public ItemLabel(String s) {
            setFont(new Font(24));
            setStyle("-fx-text-fill: #263238;");
            setText(s);
        }
    }



    public interface onClickListener {
        void onRightClick();
        void onLeftClick(Commodity newValue);
    }
}
