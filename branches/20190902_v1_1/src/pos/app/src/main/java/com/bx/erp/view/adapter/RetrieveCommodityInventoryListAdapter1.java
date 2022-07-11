package com.bx.erp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityShopInfo;

import java.util.List;

/**
 * Created by WPNA on 2020/3/3.
 */

public class RetrieveCommodityInventoryListAdapter1 extends BaseAdapter{
    private List<Commodity> list;
    private LayoutInflater inflater;
    private int layout;
    private int type = 1;
    private int selection = -1;

    public RetrieveCommodityInventoryListAdapter1(Context context, List<Commodity> list,int layout) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.layout = layout;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (list != null) {
            count = list.size();
        }
        return count;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Commodity commodity = (Commodity) this.getItem(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.retrieve_commodity_inventory_list_item1,parent,false);
            convertView.setTag(viewHolder);

            //第一种布局
            viewHolder.layout = convertView.findViewById(R.id.linearlayout);
            viewHolder.commodityBarcode = convertView.findViewById(R.id.commodity_barcode);
            viewHolder.commodityName = convertView.findViewById(R.id.commodity_name);
            viewHolder.commodityCategory = convertView.findViewById(R.id.commodity_category);
            viewHolder.commoditySpecification = convertView.findViewById(R.id.commodity_specification);
            viewHolder.commodityNO = convertView.findViewById(R.id.commodity_NO);
            viewHolder.commodityPackageUnit = convertView.findViewById(R.id.commodity_packageUnit);

            //第二种布局
            viewHolder.item2 = convertView.findViewById(R.id.item2);
            viewHolder.commodityBarcode2 = convertView.findViewById(R.id.commodity_barcode2);
            viewHolder.commodityName2= convertView.findViewById(R.id.commodity_name2);
            viewHolder.commodityCategory2 = convertView.findViewById(R.id.commodity_category2);
            viewHolder.commoditySpecification2 = convertView.findViewById(R.id.commodity_specification2);
            viewHolder.commodityNO2 = convertView.findViewById(R.id.commodity_NO2);
            viewHolder.commodityPackageUnit2 = convertView.findViewById(R.id.commodity_packageUnit2);
            viewHolder.layout2 = convertView.findViewById(R.id.linearlayout2);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (type){
            case 1://第一种布局
                if (viewHolder.layout.getVisibility() == View.GONE) {
                    viewHolder.layout.setVisibility(View.VISIBLE);
                    viewHolder.item2.setVisibility(View.GONE);
                }

                if (selection == position){     //判断是否被选中，是的话背景颜色高亮
                    viewHolder.layout.setBackgroundColor(0xFF2196F3);
                }else {
                    if (position%2 == 0){   //做隔行变色
                        viewHolder.layout.setBackgroundColor(0xFFFFFFFF);
                    }else {
                        viewHolder.layout.setBackgroundColor(0xFFF0F5F7);
                    }
                }
                viewHolder.commodityBarcode.setText(commodity.getBarcode());
                viewHolder.commodityName.setText(commodity.getName());
                viewHolder.commodityCategory.setText(String.valueOf(commodity.getCategory()));
                viewHolder.commoditySpecification.setText(commodity.getSpecification());
                viewHolder.commodityNO.setText(String.valueOf(((CommodityShopInfo) commodity.getListSlave2().get(0)).getNO()));
                viewHolder.commodityPackageUnit.setText(String.valueOf(commodity.getPackageUnit()));
                break;
            case 2://第二种布局
                if (viewHolder.item2.getVisibility() == View.GONE) {
                    viewHolder.item2.setVisibility(View.VISIBLE);
                    viewHolder.layout.setVisibility(View.GONE);
                }
                if (selection == position){     //判断是否被选中，是的话背景颜色高亮
                    viewHolder.layout2.setBackgroundColor(0xFF0090FF);
                }else {
                    if (position%2 == 0){   //做隔行变色
                        viewHolder.layout2.setBackgroundColor(0xFFFFFFFF);
                    }else {
                        viewHolder.layout2.setBackgroundColor(0xFFF0F5F7);
                    }
                }
                viewHolder.commodityBarcode2.setText(commodity.getBarcode());
                viewHolder.commodityName2.setText(commodity.getName());
                viewHolder.commodityCategory2.setText(String.valueOf(commodity.getCategory()));
                viewHolder.commoditySpecification2.setText(commodity.getSpecification());
                viewHolder.commodityNO2.setText(String.valueOf(((CommodityShopInfo) commodity.getListSlave2().get(0)).getNO()));
                viewHolder.commodityPackageUnit2.setText(String.valueOf(commodity.getPackageUnit()));
                break;
        }

        return convertView;
    }
    

    public static class ViewHolder {
        public TextView commodityBarcode;
        public TextView commodityName;
        public TextView commodityCategory;
        public TextView commoditySpecification;
        public TextView commodityPricePurchase;
        public TextView commodityNO;
        public TextView commodityPackageUnit;
        public LinearLayout layout;

        public LinearLayout item2;
        public TextView commodityBarcode2;
        public TextView commodityName2;
        public TextView commodityCategory2;
        public TextView commoditySpecification2;
        public TextView commodityPricePurchase2;
        public TextView commodityNO2;
        public TextView commodityPackageUnit2;
        public LinearLayout layout2;
    }
    
}
