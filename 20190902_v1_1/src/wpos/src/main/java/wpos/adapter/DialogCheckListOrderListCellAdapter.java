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
import wpos.model.Commodity;
import wpos.model.RetailTrade;
import wpos.utils.GeneralUtil;

public class DialogCheckListOrderListCellAdapter extends ListCell<RetailTrade> {

    private DialogCheckListOrderListCellAdapter.DoubleClickListener listener;

    private OnClickListener onClickListener;

    public DialogCheckListOrderListCellAdapter() {

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
            //添加十列
//            for (int i = 0; i < 10; i++) {
//                gridPane.getColumnConstraints().add(new ColumnConstraints());
//            }

//            Label num = new DialogCheckListOrderListCellAdapter.ItemLabel(String.format("%20s",String.valueOf(item.getNumber())));
//            GridPane.setConstraints(num, 0, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(520,300,300, Priority.ALWAYS, HPos.CENTER,true));
            Label sn = new DialogCheckListOrderListCellAdapter.ItemLabel(item.getSn());
            GridPane.setConstraints(sn, 0, 0);
//
            gridPane.getColumnConstraints().add(new ColumnConstraints(530,300,300, Priority.ALWAYS, HPos.CENTER,true));
            Label saleDatetime = new DialogCheckListOrderListCellAdapter.ItemLabel(Constants.getSimpleDateFormat().format((item.getSaleDatetime())));
            GridPane.setConstraints(saleDatetime, 1, 0);
//
            gridPane.getColumnConstraints().add(new ColumnConstraints(710,300,300, Priority.ALWAYS, HPos.CENTER,true));
            Label amount = new DialogCheckListOrderListCellAdapter.ItemLabel(String.valueOf(GeneralUtil.formatToShow(item.getAmount())));
            GridPane.setConstraints(amount, 2, 0);
//
//            Label goods_quantity = new DialogCheckListOrderListCellAdapter.ItemLabel(String.valueOf(item.getCommodityQuantity()));
//            GridPane.setConstraints(goods_quantity, 4, 0);
//
//            Label unit_price = new DialogCheckListOrderListCellAdapter.ItemLabel(GeneralUtil.formatToShow(item.getPriceRetail()));
//            GridPane.setConstraints(unit_price, 5, 0);
//
//            Label discount = new DialogCheckListOrderListCellAdapter.ItemLabel(GeneralUtil.formatToShow(item.getDiscount()));
//            GridPane.setConstraints(discount, 6, 0);
//
//            Label after_discount = new DialogCheckListOrderListCellAdapter.ItemLabel(GeneralUtil.formatToShow(item.getAfter_discount()));
//            GridPane.setConstraints(after_discount, 7, 0);
//
//            Label total_money = new DialogCheckListOrderListCellAdapter.ItemLabel(GeneralUtil.formatToShow(item.getSubtotal()));
//            GridPane.setConstraints(total_money, 8, 0);
//
//            Label remark = new DialogCheckListOrderListCellAdapter.ItemLabel(String.valueOf(item.getTag()));
//            GridPane.setConstraints(remark, 9, 0);

            gridPane.getChildren().addAll(sn, saleDatetime, amount);
            gridPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
//                    //双击事件
//                    listener.onDoubleClick();
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    //左击事件
                    if(onClickListener != null) {
                        onClickListener.onLeftClick(item);
                    }
                }
            });
            setGraphic(gridPane);
        }
        super.updateItem(item, empty);
//        if (item != null && !empty) {
//            System.out.println("进来了updateItem");
//        }
//        System.out.println("进来了updateItem");
//        super.updateItem(item, empty);
//        Rectangle rect = new Rectangle(100, 20);
//        if (item != null) {
//            System.out.println(item.toString());
//            rect.setFill(Color.web("red"));
//            setGraphic(rect);
//        }
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    @Override
    protected boolean isItemChanged(RetailTrade oldItem, RetailTrade newItem) {
        return super.isItemChanged(oldItem, newItem);
    }

    public void setListener(DialogCheckListOrderListCellAdapter.DoubleClickListener listener) {
        this.listener = listener;
    }

    public void setOnLickListener(DialogCheckListOrderListCellAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ItemLabel extends Label {
        public ItemLabel(String s) {
            setFont(new Font(24));
            setStyle("-fx-text-fill: #263238;");
            setText(s);
        }
    }

    public interface DoubleClickListener{
        void onDoubleClick();
    }

    public interface OnClickListener {
        void onRightClick();
        void onLeftClick(RetailTrade newValue);
    }
}
