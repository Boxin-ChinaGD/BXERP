package wpos.adapter;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import wpos.model.RetailTrade;
import wpos.model.RetailTradeCommodity;
import wpos.utils.GeneralUtil;

public class DialogCheckListCommodityListCellAdapter extends ListCell<RetailTradeCommodity> {

    private ModifyCountInterface modifyCountInterface;

    private OnClickListener listener;

    public DialogCheckListCommodityListCellAdapter() {

    }

    @Override
    protected void updateItem(RetailTradeCommodity item, boolean empty) {
        if (item != null && !empty) {
            GridPane gridPane = new GridPane();
            //添加一行
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            gridPane.getRowConstraints().add(rowConstraints);
            //添加十列
//            for (int i = 0; i < 10; i++) {
//                gridPane.getColumnConstraints().add(new ColumnConstraints());
//            }

            gridPane.getColumnConstraints().add(new ColumnConstraints(110,0,300, Priority.ALWAYS, HPos.CENTER,true));
            Label num = new DialogCheckListCommodityListCellAdapter.ItemLabel(String.valueOf(item.getNumber()));
            GridPane.setConstraints(num, 0, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(130,0,300, Priority.ALWAYS, HPos.CENTER,true));
            Label bar_code = new DialogCheckListCommodityListCellAdapter.ItemLabel(item.getCommodityName());
            GridPane.setConstraints(bar_code, 1, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(110,0,300, Priority.ALWAYS, HPos.CENTER,true));
            Label name_attr = new DialogCheckListCommodityListCellAdapter.ItemLabel(String.valueOf(GeneralUtil.formatToShow(item.getPriceReturn())));
            GridPane.setConstraints(name_attr, 2, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(10,0,300, Priority.ALWAYS, HPos.CENTER,true));
            Label name_decrease = new DialogCheckListCommodityListCellAdapter.ItemLabel("-");
            name_decrease.setStyle("-fx-text-fill: #263238;");
            GridPane.setConstraints(name_decrease, 3, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(20,0,300, Priority.ALWAYS, HPos.CENTER,true));
            Label unit = new DialogCheckListCommodityListCellAdapter.ItemLabel(String.valueOf(item.getNO()));
            GridPane.setConstraints(unit, 4, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(10,0,300, Priority.ALWAYS, HPos.CENTER,true));
            Label name_increase = new DialogCheckListCommodityListCellAdapter.ItemLabel("+");
            GridPane.setConstraints(name_increase, 5, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(100,100,300, Priority.ALWAYS, HPos.CENTER,true));
            Label goods_quantity = new DialogCheckListCommodityListCellAdapter.ItemLabel(String.valueOf(GeneralUtil.formatToShow(item.getPriceReturn() * item.getNO())));
            GridPane.setConstraints(goods_quantity, 6, 0);
//
            gridPane.getChildren().addAll(num, bar_code, name_attr, name_decrease, unit, name_increase, goods_quantity);
//            gridPane.setOnMouseClicked(event -> {
//                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
////                    //双击事件
////                    listener.onRightClick();
//                }else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
//                        //左击事件
//                    listener.onLeftClick(item);
//                }
//            });
            name_decrease.setOnMouseClicked(event -> {
                modifyCountInterface.doDecrease(item.getNumber(), unit, goods_quantity, event);
            });
            name_increase.setOnMouseClicked(event -> {
                modifyCountInterface.doIncrease(item.getNumber(), unit, goods_quantity, event);
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
    protected boolean isItemChanged(RetailTradeCommodity oldItem, RetailTradeCommodity newItem) {
        return super.isItemChanged(oldItem, newItem);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
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

    public interface ModifyCountInterface {
        void doIncrease(int position, Label showCountView, Label total_money, MouseEvent event);
        void doDecrease(int position, Label showCountView, Label total_money, MouseEvent event);
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public interface OnClickListener {
        void onLeftClick(RetailTradeCommodity newValue);
    }
}
