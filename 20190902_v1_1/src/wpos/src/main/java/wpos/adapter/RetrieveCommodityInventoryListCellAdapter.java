package wpos.adapter;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import wpos.model.Commodity;
import wpos.model.CommodityShopInfo;

public class RetrieveCommodityInventoryListCellAdapter extends ListCell<Commodity> {

    private static OnLoadMoreListener mLoadMoreListener;
    public static int lastIndex = 15;
    public static boolean mIsLoading = false;//是否正在加载

    public RetrieveCommodityInventoryListCellAdapter() {

    }

    // 从不可见变为可见，也会调用这个方法， 比如滑动
    @Override
    protected void updateItem(Commodity item, boolean empty) {
        if (item != null && !empty) {
            if (getGraphic() == null) {
                myGrid gridPane = new myGrid(item);
                setGraphic(gridPane);
            } else {
                myGrid graphic = (myGrid) getGraphic();

                graphic.getBar_code().setText(item.getBarcode());
                graphic.getName_attr().setText(item.getName());
                graphic.getCategory().setText(item.getCategory());
                graphic.getGoods_quantity().setText(item.getSpecification());
                CommodityShopInfo commodityShopInfo = (CommodityShopInfo) item.getListSlave2().get(0);
                graphic.getUnit().setText(String.valueOf(commodityShopInfo.getNO()));
                graphic.getPackageUnit().setText(item.getPackageUnit());
            }

            //如果当前加载到的item的index刚好等于最后一个，那就查询更多的数据
            if (item.getIndex() == RetrieveCommodityInventoryListCellAdapter.lastIndex && !RetrieveCommodityInventoryListCellAdapter.mIsLoading) {
                if (mLoadMoreListener != null) {
                    RetrieveCommodityInventoryListCellAdapter.mIsLoading = true;
                    mLoadMoreListener.onloadMore();
                }
            }
        }
        super.updateItem(item, empty);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
    }

    public void setLastIndex(int lastIndex) {
        RetrieveCommodityInventoryListCellAdapter.lastIndex = lastIndex;
    }

    public int getLastIndex() {
        return RetrieveCommodityInventoryListCellAdapter.lastIndex;
    }

    @Override
    protected boolean isItemChanged(Commodity oldItem, Commodity newItem) {
        return super.isItemChanged(oldItem, newItem);
    }

    class ItemLabel extends Label {
        public ItemLabel(String s) {
            setFont(new Font(24));
            setStyle("-fx-text-fill: #263238;");
            setText(s);
        }
    }

    class myGrid extends GridPane {
        private final Label bar_code;
        private final Label name_attr;
        private final Label category;
        private final Label goods_quantity;
        private final Label unit;
        private final Label packageUnit;

        public myGrid(Commodity item) {
            setPrefHeight(62);
            setMaxHeight(62);
            RowConstraints rowConstraints = new RowConstraints();
            getRowConstraints().add(rowConstraints);

            getColumnConstraints().add(new ColumnConstraints(300,300,300, Priority.ALWAYS, HPos.CENTER,true));
            bar_code = new ItemLabel(item.getBarcode());
            GridPane.setConstraints(bar_code, 0, 0);

            getColumnConstraints().add(new ColumnConstraints(300,300,300, Priority.ALWAYS,HPos.CENTER,true));
            name_attr = new ItemLabel(item.getName());
            GridPane.setConstraints(name_attr, 1, 0);

            getColumnConstraints().add(new ColumnConstraints(300,300,300, Priority.ALWAYS,HPos.CENTER,true));
            category = new ItemLabel(item.getCategory());
            GridPane.setConstraints(category, 2, 0);

            getColumnConstraints().add(new ColumnConstraints(250,250,250, Priority.ALWAYS,HPos.CENTER,true));
            goods_quantity = new ItemLabel(item.getSpecification());
            GridPane.setConstraints(goods_quantity, 3, 0);

            getColumnConstraints().add(new ColumnConstraints(250,250,250, Priority.ALWAYS,HPos.CENTER,true));
            CommodityShopInfo commodityShopInfo = (CommodityShopInfo) item.getListSlave2().get(0);
            unit = new ItemLabel(String.valueOf(commodityShopInfo.getNO()));
            GridPane.setConstraints(unit, 4, 0);

            getColumnConstraints().add(new ColumnConstraints(250,250,250, Priority.ALWAYS,HPos.CENTER,true));
            packageUnit = new ItemLabel(item.getPackageUnit());
            GridPane.setConstraints(packageUnit, 5, 0);

            getChildren().addAll(bar_code, name_attr, category, goods_quantity, unit, packageUnit);
        }

        public Label getBar_code() {
            return bar_code;
        }

        public Label getName_attr() {
            return name_attr;
        }

        public Label getCategory() {
            return category;
        }

        public Label getGoods_quantity() {
            return goods_quantity;
        }

        public Label getUnit() {
            return unit;
        }

        public Label getPackageUnit() {
            return packageUnit;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onloadMore();
    }

    public void setLoadCompleted() {
        RetrieveCommodityInventoryListCellAdapter.mIsLoading = false;
    }

}
