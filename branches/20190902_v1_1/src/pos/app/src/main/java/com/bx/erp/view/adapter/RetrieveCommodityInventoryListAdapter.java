package com.bx.erp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.model.Commodity;

import java.util.List;

public class RetrieveCommodityInventoryListAdapter extends BaseAdapter {
    private List<Commodity> list;
    private LayoutInflater inflater;

    public RetrieveCommodityInventoryListAdapter(Context context, List<Commodity> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (list != null) {
            count = list.size();
        }
        return count;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Commodity commodity = (Commodity) this.getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.retrieve_commodity_inventory_list_item, null);
            viewHolder.commodityBarcode = convertView.findViewById(R.id.commodity_barcode);
            viewHolder.commodityName = convertView.findViewById(R.id.commodity_name);
            viewHolder.commodityCategory = convertView.findViewById(R.id.commodity_category);
            viewHolder.commoditySpecification = convertView.findViewById(R.id.commodity_specification);
            viewHolder.commodityNO = convertView.findViewById(R.id.commodity_NO);
            viewHolder.commodityPackageUnit = convertView.findViewById(R.id.commodity_packageUnit);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.commodityBarcode.setText(commodity.getBarcode());
        viewHolder.commodityBarcode.setTextSize(13);
        viewHolder.commodityName.setText(commodity.getName());
        viewHolder.commodityName.setTextSize(13);
        viewHolder.commodityCategory.setText(String.valueOf(commodity.getCategory()));
        viewHolder.commodityCategory.setTextSize(13);
        viewHolder.commoditySpecification.setText(commodity.getSpecification());
        viewHolder.commoditySpecification.setTextSize(13);
        viewHolder.commodityNO.setText(String.valueOf(commodity.getNO()));
        viewHolder.commodityNO.setTextSize(13);
        viewHolder.commodityPackageUnit.setText(String.valueOf(commodity.getPackageUnit()));
        viewHolder.commoditySpecification.setTextSize(13);

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
    }
}
