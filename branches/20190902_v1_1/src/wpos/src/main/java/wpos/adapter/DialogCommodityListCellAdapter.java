package wpos.adapter;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import wpos.model.Commodity;
import wpos.model.CommodityShopInfo;
import wpos.utils.GeneralUtil;

public class DialogCommodityListCellAdapter extends ListCell<Commodity> {
    //用于记录被选中的commodity
    private Commodity commody;

    public DialogCommodityListCellAdapter() {
    }


    @Override
    protected void updateItem(Commodity item, boolean empty) {
        if (item != null && !empty) {
            myGrid gridPane = new myGrid(item);
            System.out.println(item.getName());
            setGraphic(gridPane);
        } else {
            /**
             * 为了重绘列表成功
             */
//            updateItem(...)需要处理单元为空的情况。这就是删除某项（或由于TreeView折叠了一个节点而将其变为空）并将先前显示该项目的单元格重用为空单元格的情况
            setText(null);
            setGraphic(null);
        }
        super.updateItem(item, empty);
    }

    public void setSelectCommody(Commodity commody) {
        this.commody = commody;
    }

    public Commodity getSelectCommody() {
        return commody;
    }

    class ItemLabel extends Label {
        public ItemLabel(String s) {
            setFont(new Font(24));
            setStyle("-fx-text-fill: #263238;");
            setText(s);
        }
    }

    class myGrid extends GridPane {
        private final Label num;
        private final Label bar_code;
        private final Label name_attr;
        private final Label unit;
        private final Label retail_price;
        private final Label inventory;

        public myGrid(Commodity item) {
            setPrefHeight(62);
            setMaxHeight(62);
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100);
            getRowConstraints().add(rowConstraints);
            //
            getColumnConstraints().add(new ColumnConstraints(100,100,100, Priority.ALWAYS,HPos.CENTER,true));
            num = new ItemLabel(String.valueOf(item.getNumber()));
            GridPane.setConstraints(num, 0, 0);

            getColumnConstraints().add(new ColumnConstraints(300,300,300, Priority.ALWAYS,HPos.CENTER,true));
            bar_code = new ItemLabel(String.valueOf(item.getBarcode()));
            GridPane.setConstraints(bar_code, 1, 0);

            getColumnConstraints().add(new ColumnConstraints(200,200,200, Priority.ALWAYS,HPos.CENTER,true));
            name_attr = new ItemLabel(String.valueOf(item.getName()));
            GridPane.setConstraints(name_attr, 2, 0);

            getColumnConstraints().add(new ColumnConstraints(100,100,100, Priority.ALWAYS,HPos.CENTER,true));
            unit = new ItemLabel(String.valueOf(item.getPackageUnit()));
            GridPane.setConstraints(unit, 3, 0);

            getColumnConstraints().add(new ColumnConstraints(200,200,200, Priority.ALWAYS,HPos.CENTER,true));
            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) item.getListSlave2().get(0);
            retail_price = new ItemLabel(GeneralUtil.formatToShow(commodityShopInfo.getPriceRetail()));
            GridPane.setConstraints(retail_price, 4, 0);

            getColumnConstraints().add(new ColumnConstraints(100,100,100, Priority.ALWAYS,HPos.CENTER,true));
//            inventory = new ItemLabel(String.valueOf(item.getNO()));
            inventory = new Label();
            GridPane.setConstraints(inventory, 5, 0);


            getChildren().addAll(num, bar_code, name_attr, unit, retail_price, inventory);
        }

        public Label getNum() {
            return num;
        }

        public Label getBar_code() {
            return bar_code;
        }

        public Label getName_attr() {
            return name_attr;
        }

        public Label getUnit() {
            return unit;
        }

        public Label getRetail_price() {
            return retail_price;
        }

        public Label getInventory() {
            return inventory;
        }
    }
}
