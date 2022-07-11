package wpos.adapter;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import wpos.model.Commodity;
import wpos.model.RetailTradeCommodity;
import wpos.utils.GeneralUtil;

import java.util.List;

public class DialogBillRecyclerViewAdapter extends ListCell<Commodity> {
//    List<Commodity> list;
//    Context context;
//
//    public DialogBillRecyclerViewAdapter(List<Commodity> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }

    private DialogBillRecyclerViewAdapter.ModifyCountInterface modifyCountInterface;

    private DialogBillRecyclerViewAdapter.OnClickListener listener;

    public DialogBillRecyclerViewAdapter() {

    }

    @Override
    protected void updateItem(Commodity item, boolean empty) {
        if (item != null && !empty) {
            GridPane gridPane = new GridPane();
            //添加一行
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            gridPane.getRowConstraints().add(rowConstraints);

            gridPane.getColumnConstraints().add(new ColumnConstraints(110, 0, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label num = new DialogBillRecyclerViewAdapter.ItemLabel(String.valueOf(item.getOrderNumber()));
            GridPane.setConstraints(num, 0, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(110, 0, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label name = new DialogBillRecyclerViewAdapter.ItemLabel(item.getName());
            GridPane.setConstraints(name, 1, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(110, 0, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label priceRetail = new DialogBillRecyclerViewAdapter.ItemLabel(GeneralUtil.formatToShow(item.getPriceRetail()));
            GridPane.setConstraints(priceRetail, 2, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(130, 0, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label total_number = new DialogBillRecyclerViewAdapter.ItemLabel(String.valueOf(item.getCommodityQuantity()));
            GridPane.setConstraints(total_number, 3, 0);

            gridPane.getColumnConstraints().add(new ColumnConstraints(110, 0, 300, Priority.ALWAYS, HPos.CENTER, true));
            Label subtotal = new DialogBillRecyclerViewAdapter.ItemLabel(GeneralUtil.formatToShow(item.getSubtotal()));
            GridPane.setConstraints(subtotal, 4, 0);
//
            gridPane.getChildren().addAll(num, name, priceRetail, total_number, subtotal);
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

    public void setListener(DialogBillRecyclerViewAdapter.OnClickListener listener) {
        this.listener = listener;
    }

    class ItemLabel extends Label {
        public ItemLabel(String s) {
            setFont(new Font(24));
            setStyle("-fx-text-fill: #263238;");
            setText(s);
        }
    }

    public interface DoubleClickListener {
        void onDoubleClick();
    }

    public interface ModifyCountInterface {
        void doIncrease(int position, Label showCountView, Label total_money, MouseEvent event);

        void doDecrease(int position, Label showCountView, Label total_money, MouseEvent event);
    }

    public void setModifyCountInterface(DialogBillRecyclerViewAdapter.ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public interface OnClickListener {
        void onLeftClick(RetailTradeCommodity newValue);
    }
}
